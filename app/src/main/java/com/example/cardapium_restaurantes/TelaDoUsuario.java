package com.example.cardapium_restaurantes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import Model.UserModel;


public class TelaDoUsuario extends AppCompatActivity {
    private Button logout;
    private TextView ola;
    private ImageView iv;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private StorageReference storageReference;
    private UserModel currentUser;

    Button bttGerCardapio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_do_usuario);


        bttGerCardapio = findViewById(R.id.bttGerCardapio);
        bttGerCardapio.setOnClickListener(new View.OnClickListener() {
                                              @Override
                                              public void onClick(View v) {
                                                  Intent it = new Intent(TelaDoUsuario.this, TelaListagemCardapio.class);
                                                  startActivity(it);
                                              }
                                          });

        ola = findViewById(R.id.txtOlaUser);
        logout = findViewById(R.id.bttLogout);
        iv = findViewById(R.id.imageView3);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        getCurrentUserInfo();
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                Toast.makeText(getApplicationContext(), "Até mais!", Toast.LENGTH_LONG);
                }
            });
        }

    private void getCurrentUserInfo() {
        DatabaseReference ref = database.getReference().child("Users").child(mAuth.getUid());
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String nome = snapshot.child("nome").getValue(String.class);
                String sobrenome = snapshot.child("sobrenome").getValue(String.class);
                String email = snapshot.child("email").getValue(String.class);
                currentUser = new UserModel(mAuth.getUid(), nome, sobrenome, email);
                ola.setText("Olá, " + currentUser.getNome());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Ocorreu um erro na recuperação dos seus dados, tente novamente", Toast.LENGTH_LONG).show();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });

        StorageReference profileRef = storageReference.child("Profile").child(mAuth.getUid()+".jpeg");

        final long TEN_MEGABYTE = 10 * 1024 * 1024;
        profileRef.getBytes(TEN_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                iv.setImageBitmap((bmp));

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                storageReference.child("Profile").child("genericuser.png").getBytes(1560).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        iv.setImageBitmap((bmp));
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(TelaDoUsuario.this, "Não foi possível recuperar a foto de perfil", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
    }
}
