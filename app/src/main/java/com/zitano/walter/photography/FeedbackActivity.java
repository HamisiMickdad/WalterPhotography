package com.zitano.walter.photography;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class FeedbackActivity extends AppCompatActivity {
    Button btnSubmit;
    EditText txtFeed, txtEmail;
    String email;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        if(getSupportActionBar()!=null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        btnSubmit = (Button) findViewById(R.id.btnfeed);
        txtFeed = (EditText) findViewById(R.id.txtFeed);
        txtEmail = (EditText) findViewById(R.id.txtEmail);
        email = txtEmail.getText().toString();

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (email != null) {
                    Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", "footatips@gmail.com", null));
                    //intent.setType("text/plain");
                    intent.putExtra(Intent.EXTRA_EMAIL, email);
                    intent.putExtra(Intent.EXTRA_SUBJECT,"Walter Photography App Feedback");
                    intent.putExtra(Intent.EXTRA_TEXT, txtFeed.getText());
                    startActivity(Intent.createChooser(intent, "Send using"));

                } else {
                    Toast.makeText(FeedbackActivity.this, "please provide your email address", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if(id==android.R.id.home){
            finish();

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
