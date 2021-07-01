package com.example.cardapium_restaurantes;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;

public class TelaCriarItem extends AppCompatActivity {

    public ArrayAdapter<String> adapterSpinner;
//    private List<CategoriaCardapio> categorias;
    private Spinner spinnerCategorias;
    private final int PICK_IMAGE_REQUEST = 22;
    private Uri filePath;
    private Button btnAddFoto;
    private ImageView imageItem;
    String a [] = {"Pizza salgada", "Pizza doce"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_criar_item);

        spinnerCategorias = findViewById(R.id.spinnerCategoria);
        imageItem = findViewById(R.id.imageItemCriacao);
        btnAddFoto = findViewById(R.id.btnAddFoto);
        adapterSpinner = new ArrayAdapter<>(TelaCriarItem.this, android.R.layout.simple_spinner_item, a);
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategorias.setAdapter(adapterSpinner);

        btnAddFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });


    }
    public void selectImage() {
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
            imageItem.setImageURI(filePath);
        }
    }

//    private void sendPhoto() {
//        Bitmap bitmap = ((BitmapDrawable)imageItem.getDrawable()).getBitmap();
//        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
//        byte[] image = byteArrayOutputStream.toByteArray();

//        StorageReference imgRef = sRef.child("Profile").child(mAuth.getUid()+".jpeg");
//        UploadTask uploadTask = imgRef.putBytes(image);
//        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                Log.d("Success", "imageUploaded:success");
//            }
//        });
//        Toast.makeText(this, imgRef.getDownloadUrl().toString(), Toast.LENGTH_LONG).show();;
//    }
}