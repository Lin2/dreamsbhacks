package com.example.dream_sbhacks;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class Tutorial extends AppCompatActivity {
    ConstraintLayout constraintLayout;
    private Button next;
    private TextView textView;
    private TextView textView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);

        constraintLayout = findViewById(R.id.container);
        textView = findViewById(R.id.textView);
        textView2 = findViewById(R.id.textView2);


        next = (Button) findViewById(R.id.next);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Tutorial.this, LogDreamPage.class);
                startActivity(intent);
                return;
            }
        });
    }
}