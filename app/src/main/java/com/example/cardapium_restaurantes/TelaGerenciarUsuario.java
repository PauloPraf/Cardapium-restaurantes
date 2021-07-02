package com.example.cardapium_restaurantes;

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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

import Model.UserModel;

public class TelaGerenciarUsuario extends AppCompatActivity {
    private EditText edtTxtNome, edtTxtSobrenome, edtTxtEmail, edtTxtSenha, edtTxtConfirmarSenha;
    private Button button, uploadImage;
    private FirebaseAuth mAuth;
    private Uri filePath;
    private ImageView iv;
    private String idUser, email;
    private final int PICK_IMAGE_REQUEST = 22;
    private FirebaseStorage storage;
    private StorageReference sRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_gerenciar_usuario);

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

        Bundle extras = getIntent().getExtras();

        if(extras != null) {
            edtTxtNome.setText(extras.getString("nome"));
            edtTxtSobrenome.setText(extras.getString("sobrenome"));
            email = extras.getString("email");
            edtTxtEmail.setText(email);
            idUser = extras.getString("id");
        }

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

    @Override
    public void onResume() {
        super.onResume();


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


        atualizarUsuario();
    }

    private void atualizarUsuario() {
        UserModel newUser = new UserModel();
        newUser.setId(idUser);
        newUser.setEmail(edtTxtEmail.getText().toString());
        newUser.setNome(edtTxtNome.getText().toString());
        newUser.setSobrenome(edtTxtSobrenome.getText().toString());

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        while(user == null) {
            if(user != null) break;
        }

        AuthCredential credential = EmailAuthProvider
                .getCredential(email, "123456");

        user.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        if(!newUser.getEmail().equals(email)) {
                            user.updateEmail(newUser.getEmail())
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(getApplicationContext(), "Email Atualizado", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        }

                        String newPassword = edtTxtSenha.getText().toString();

                        if(newPassword != "" && newPassword.length() > 5 && newPassword.equals(edtTxtConfirmarSenha.getText().toString())) {
                            user.updatePassword(newPassword)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(getApplicationContext(), "Senha Atualizada", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        }

                    }
                });

        if (iv.getDrawable() != null)
            sendPhoto();

        newUser.salvar();
        Toast.makeText(getApplicationContext(), "Dados Atualizados!", Toast.LENGTH_LONG).show();
        finish();
    }
}
