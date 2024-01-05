package com.example.myapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
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

import java.util.ArrayList;
import java.util.List;

public class MainActivity7 extends AppCompatActivity {

    private static final int REQUEST_CODE = 22;
    Button btnpicture,upload;
    ImageView imageView;

    Bitmap im ;
    TextView text,textView;
    RecyclerView recyclerView;
    String apiKey = "02373251a07ff047daf649da25337d7d97edaafe";
    String apiUrl = "https://api.logmeal.es/v2/image/recognition/complete";
    String imageUrl = "https://www.unileverfoodsolutions.co.th/dam/global-ufs/mcos/SEA/calcmenu/recipes/TH-recipes/red-meats-&-red-meat-dishes/%E0%B8%8A%E0%B8%B5%E0%B8%AA%E0%B9%80%E0%B8%9A%E0%B8%AD%E0%B8%A3%E0%B9%8C%E0%B9%80%E0%B8%81%E0%B8%AD%E0%B8%A3%E0%B9%8C/%E0%B8%8A%E0%B8%B5%E0%B8%AA%E0%B9%80%E0%B8%9A%E0%B8%AD%E0%B8%A3%E0%B9%8C%E0%B9%80%E0%B8%81%E0%B8%AD%E0%B8%A3%E0%B9%8C_header.jpg";

    static final int REQUEST_IMAGE_CAPTURE = 1;
    MainAdapter mainAdapter;
    private Drawable drawable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main7);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("not food1");

        btnpicture = findViewById(R.id.btncamera_id);
        imageView = findViewById(R.id.image);
        text = findViewById(R.id.text);
        textView = findViewById(R.id.textll);
        upload = findViewById(R.id.upload);
        drawable = imageView.getDrawable();
        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backto = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(backto);
            }
        });

        btnpicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                myRef.removeValue();
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);

            }
        });


        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ImageUploadTask(apiKey, imageView, textView).execute(String.valueOf(imageUrl));
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        checkname();
                    }
                }, 5000);



            }
        });

        recyclerView =(RecyclerView) findViewById(R.id.rv1);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<Notfood> options =
                new FirebaseRecyclerOptions.Builder<Notfood>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("not food1"), Notfood.class)
                        .build();

        mainAdapter = new MainAdapter(options);
        recyclerView.setAdapter(mainAdapter);
    }


    Bitmap imageBitmap;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            im = (Bitmap) extras.get("data") ;
            imageView.setImageBitmap(imageBitmap);
        }
    }

    public MainActivity7() {
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

    public void checkname() {
        String foodname = textView.getText().toString().toLowerCase().trim();
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
        Query checkfoodname = reference.orderByChild("name").equalTo(foodname);

        checkfoodname.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()){
                    textView.setError(null);
                    DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("not food1");
                    Query checknotfood = reference1.orderByChild("name").equalTo(foodname);

                    checknotfood.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()){
                                textView.setError(null);

                            }else {
                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                DatabaseReference myRef = database.getReference("not food1").push();
                                String foodname = textView.getText().toString().toLowerCase().trim();
                                Task<Void> setValue = myRef.setValue(new Notfood(foodname));
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }else{

                }

            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}
