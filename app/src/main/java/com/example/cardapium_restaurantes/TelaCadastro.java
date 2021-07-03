package com.example.cardapium_restaurantes;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.UUID;

import Model.UserModel;

public class TelaCadastro extends AppCompatActivity {
    private EditText edtTxtNome, edtTxtSobrenome, edtTxtEmail, edtTxtSenha, edtTxtConfirmarSenha;
    private Button button, uploadImage;
    private FirebaseAuth mAuth;
    private Uri filePath;
    private ImageView iv;
    private final int PICK_IMAGE_REQUEST = 22;
    private FirebaseStorage storage;
    private StorageReference sRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_cadastro);

        edtTxtNome = findViewById(R.id.edtTxtNome);
        edtTxtSobrenome = findViewById(R.id.edtTxtSobrenome);
        edtTxtEmail = findViewById(R.id.edtTxtEmail);
        edtTxtSenha = findViewById(R.id.edtTxtSenha);
        edtTxtConfirmarSenha = findViewById(R.id.edtTxtConfirmarSenha);
        uploadImage = findViewById(R.id.uploadImage);
        iv = findViewById(R.id.imageView4);
        button = findViewById(R.id.button);
        mAuth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        sRef = storage.getReference();

        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validarDados();
            }
        });
    }

    private void selectImage() {
        Intent it = new Intent();
        it.setType("image/*");
        it.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(it, "Selecionar foto"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == PICK_IMAGE_REQUEST) {
            filePath = data.getData();
            iv.setImageURI(filePath);
        }
    }

    private void sendPhoto() {
        Bitmap bitmap = ((BitmapDrawable)iv.getDrawable()).getBitmap();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] image = byteArrayOutputStream.toByteArray();

        StorageReference imgRef = sRef.child("Profile").child(mAuth.getUid()+".jpeg");
        UploadTask uploadTask = imgRef.putBytes(image);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                 Log.d("Success", "imageUploaded:success");
            }
        });
        Toast.makeText(this, imgRef.getDownloadUrl().toString(), Toast.LENGTH_LONG).show();;
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null) {
            Toast.makeText(getApplicationContext(), "Existe uma sessão ativa", Toast.LENGTH_LONG).show();
            startActivity(new Intent(getApplicationContext(), TelaDoUsuario.class));
        }
    }

    private void validarDados() {
        if(edtTxtNome.getText().toString().trim().equals("")) {
            Toast.makeText(getApplicationContext(), "O nome é obrigatório", Toast.LENGTH_SHORT).show();
            return;
        }

        if(edtTxtSobrenome.getText().toString().trim().equals("")) {
            Toast.makeText(getApplicationContext(), "O sobrenome é obrigatório", Toast.LENGTH_SHORT).show();
            return;
        }

        if(edtTxtEmail.getText().toString().trim().equals("")) {
            Toast.makeText(getApplicationContext(), "O Email é obrigatório", Toast.LENGTH_SHORT).show();
            return;
        }

        if(!edtTxtEmail.getText().toString().matches("([a-zA-Z0-9]+)@[a-z]{2,5}.[a-z]{2,4}")) {
            Toast.makeText(getApplicationContext(), "Informe um email válido", Toast.LENGTH_SHORT).show();
            return;
        }

        if(edtTxtSenha.length() < 6) {
            Toast.makeText(getApplicationContext(), "A senha deve possuir pelo menos 6 caracteres", Toast.LENGTH_SHORT).show();
            return;
        }

        if(edtTxtSenha.getText().toString().trim().equals("")) {
            Toast.makeText(getApplicationContext(), "A senha é obrigatória", Toast.LENGTH_SHORT).show();
            return;
        }

        if(!edtTxtSenha.getText().toString().equals(edtTxtConfirmarSenha.getText().toString())) {
            Toast.makeText(getApplicationContext(), "A senha e a confirmação devem ser iguais", Toast.LENGTH_SHORT).show();
            return;
        }

        cadastrarUsuario();
    }

    private void cadastrarUsuario() {
        UserModel newUser = new UserModel();
        newUser.setEmail(edtTxtEmail.getText().toString());
        newUser.setNome(edtTxtNome.getText().toString());
        newUser.setSobrenome(edtTxtSobrenome.getText().toString());

        mAuth.createUserWithEmailAndPassword(newUser.getEmail(), edtTxtSenha.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(null, "createUserWithEmail:success");
                            newUser.setId(mAuth.getUid());
                            newUser.salvar();

                            if (iv.getDrawable() != null)
                                sendPhoto();

                            Toast.makeText(getApplicationContext(), "Cadastro realizado com sucesso", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(getApplicationContext(), TelaDoUsuario.class));
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(null, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(getApplicationContext(), "Ocorreu um erro, tente novamente.",
                                    Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                });
    }
}