package com.example.myapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;


public class MainActivity5 extends AppCompatActivity {


    ImageView im8,im9,im10,imageView52,im32;
    EditText namefood;
    Button name_food,button2,Clear,up;
    TextView textView;
    RecyclerView recyclerView;
    MainAdapter mainAdapter;

    private static final int PICK_IMAGES_REQUEST = 7;
    private String apiKey = "229170cdb5cfd8ded114f0e898f6a6b3646f958b";
    private List<Bitmap> selectedBitmaps = new ArrayList<>();
    String imageUrl = "https://www.unileverfoodsolutions.co.th/dam/global-ufs/mcos/SEA/calcmenu/recipes/TH-recipes/red-meats-&-red-meat-dishes/%E0%B8%8A%E0%B8%B5%E0%B8%AA%E0%B9%80%E0%B8%9A%E0%B8%AD%E0%B8%A3%E0%B9%8C%E0%B9%80%E0%B8%81%E0%B8%AD%E0%B8%A3%E0%B9%8C/%E0%B8%8A%E0%B8%B5%E0%B8%AA%E0%B9%80%E0%B8%9A%E0%B8%AD%E0%B8%A3%E0%B9%8C%E0%B9%80%E0%B8%81%E0%B8%AD%E0%B8%A3%E0%B9%8C_header.jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main5);

        textView = findViewById(R.id.textView);
        button2 = findViewById(R.id.button2);
        Clear = findViewById(R.id.Clear);
        imageView52 = findViewById(R.id.imageView52);
        im32 =findViewById(R.id.imageView32);

        Clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("not food");
                myRef.removeValue();
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openImagePicker();


            }
        });


        im8 = findViewById(R.id.eathome5);
        im8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent10 = new Intent(getApplicationContext(),MainActivity5.class);
                startActivity(intent10);
            }
        });

        im9 = findViewById(R.id.home5);
        im9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent10 = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent10);
            }
        });

        im10 = findViewById(R.id.sthome5);
        im10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent11 = new Intent(getApplicationContext(),MainActivity6.class);
                startActivity(intent11);
            }
        });

        namefood = findViewById(R.id.name);
        name_food = findViewById(R.id.namefood);

        name_food.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validatename()){

                }else {
                    checkname();
                }
            }
        });
        recyclerView =(RecyclerView) findViewById(R.id.rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<Notfood> options =
                new FirebaseRecyclerOptions.Builder<Notfood>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("not food"), Notfood.class)
                        .build();

        mainAdapter = new MainAdapter(options);
        recyclerView.setAdapter(mainAdapter);
    }

    public Boolean validatename() {
        String val = namefood.getText().toString().toLowerCase();
        if (val.isEmpty()) {
            namefood.setError("Name connot");
            return false;
        } else {
            namefood.setError(null);
            return true;
        }
    }

    public MainActivity5() {
        super();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mainAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mainAdapter.stopListening();
    }


    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);

        startActivityForResult(intent, PICK_IMAGES_REQUEST);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGES_REQUEST && resultCode == RESULT_OK) {

            if (data.getClipData() != null) {

                int count = data.getClipData().getItemCount();
                for (int i = 0; i < count; i++) {
                    Uri imageUri = data.getClipData().getItemAt(i).getUri();

                    try {
                       Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                       im32.setImageBitmap(bitmap);;
                       new ImageUploadTask(apiKey, im32, namefood).execute(String.valueOf(imageUrl));
                    } catch (FileNotFoundException e) {
                        throw new RuntimeException(e);
                    }



                }
            } else if (data.getData() != null) {

                Uri imageUri = data.getData();

                getBitmapFromUri(imageUri);

            }
        }
    }

    private void getBitmapFromUri(Uri uri) {
        try {
            Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
            im32.setImageBitmap(bitmap);
            new ImageUploadTask(apiKey, im32, namefood).execute(String.valueOf(imageUrl));
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    checkname();
                }
            }, 7000);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
//
    }


    public void checkname() {
        String foodname = namefood.getText().toString().toLowerCase().trim();
        String[] keywords = {"pizza", "ice creame ","hamburger","cheesecak ","carbonara","bacon","pastel de nata","fish and chips"};

        List<String> extractedTextList = new ArrayList<>();

        for (String keyword : keywords) {
            int startIndex = foodname.indexOf(keyword);

            while (startIndex != -1) {
                int endIndex = startIndex + keyword.length();
                String extractedText = foodname.substring(startIndex, endIndex);
                extractedTextList.add(extractedText);

                // Move to the next occurrence of the keyword
                startIndex = foodname.indexOf(keyword, endIndex);
            }
        }
        textView.setText(String.join(", ",extractedTextList));
        String foodname1 = textView.getText().toString().toLowerCase().trim();


        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("food");
        Query checkfoodname = reference.orderByChild("name").equalTo(foodname1);

        checkfoodname.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()){
                    namefood.setError(null);
                    DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("not food");
                    Query checknotfood = reference1.orderByChild("name").equalTo(foodname1);

                    checknotfood.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()){
                                namefood.setError(null);

                            }else {
                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                DatabaseReference myRef = database.getReference("not food").push();
                                String foodname1 = textView.getText().toString().toLowerCase().trim();
                                Task<Void> setValue = myRef.setValue(new Notfood(foodname1));
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }else{
                    namefood.setError( "Name connot");
                }

            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                namefood.setError("Name connot");
            }
        });

    }
}

