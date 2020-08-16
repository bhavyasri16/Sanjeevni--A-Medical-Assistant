package com.finalproject.it.sanjeevni.activities.chatBot;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.finalproject.it.sanjeevni.R;
import com.finalproject.it.sanjeevni.activities.BaseActivity;

public class chatBot extends BaseActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatbot);

        String text0 = "Hi! I am SanBot and you are?";
        TextView tv1 = (TextView)findViewById(R.id.tv1);
        tv1.setText(text0);
        String text1 = "Please enter your age in years.";
        TextView tv2 = (TextView)findViewById(R.id.tv2);
        tv2.setText(text1);
        String text2 = "Please select you gender.";
        TextView tv3 = (TextView)findViewById(R.id.tv3);
        tv3.setText(text2);
        String text3 = "Do you have any ongoing medical condition?";
        TextView tv4 = (TextView)findViewById(R.id.tv4);
        tv4.setText(text3);

        Spinner dropdown = findViewById(R.id.spinner1);
        String[] items1 = new String[]{"Choose Option","Male","Female","Other"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items1);
        dropdown.setAdapter(adapter);

        Button btn = (Button)findViewById(R.id.bt1);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView textview = chatBot.this.findViewById(R.id.tv2);
                textview.setVisibility(View.VISIBLE);
                EditText edittext = chatBot.this.findViewById(R.id.et2);
                edittext.setVisibility(View.VISIBLE);
                Button button = chatBot.this.findViewById(R.id.bt2);
                button.setVisibility(View.VISIBLE);
            }
        });
        Button btn2 = (Button)findViewById(R.id.bt2);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView textview1 = chatBot.this.findViewById(R.id.tv3);
                textview1.setVisibility(View.VISIBLE);
                Spinner spinner = chatBot.this.findViewById(R.id.spinner1);
                spinner.setVisibility(View.VISIBLE);
                Button button2 = chatBot.this.findViewById(R.id.bt3);
                button2.setVisibility(View.VISIBLE);

            }
        });
        Button btn3 = (Button)findViewById(R.id.bt3);
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView textview3 = chatBot.this.findViewById(R.id.tv4);
                textview3.setVisibility(View.VISIBLE);
                EditText edittext2= chatBot.this.findViewById(R.id.et4);
                edittext2.setVisibility(View.VISIBLE);
                Button button3 = chatBot.this.findViewById(R.id.bt4);
                button3.setVisibility(View.VISIBLE);

            }
        });
        Button btn4 = (Button)findViewById(R.id.bt4);
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Button button4 = chatBot.this.findViewById(R.id.bt5);
                button4.setVisibility(View.VISIBLE);

            }
        });
        Button buttn = (Button)findViewById(R.id.bt5);
        buttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://colab.research.google.com/drive/11cqS2zXYUIetKN1WM5a4OIqAhQEMy22j?usp=sharing";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);

            }
        });




    }


}
