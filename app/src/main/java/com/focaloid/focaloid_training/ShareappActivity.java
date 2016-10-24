package com.focaloid.focaloid_training;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.File;

/**
 * Created by dinil on 22-08-2016.
 */
public class ShareappActivity extends AppCompatActivity {

    Button btnshareapp;
    Uri uriToImage;
    EditText edittxtcomment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.share_app);

        btnshareapp=(Button)findViewById(R.id.btnshare);
        edittxtcomment=(EditText)findViewById(R.id.edittxtcomment);

        btnshareapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                //uriToImage=Uri.parse("android.resource://" + getPackageName()+ "/drawable/" + "shareapp");

                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                //shareIntent.setData( uriToImage);
               // shareIntent.putExtra(Intent.EXTRA_STREAM, uriToImage);
                shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Focaloid App");
                shareIntent.putExtra(Intent.EXTRA_TEXT, edittxtcomment.getText().toString());
                //shareIntent.putExtra(Intent.EXTRA_TITLE, "Share App");
                shareIntent.setType("text/*");
                //shareIntent.setPackage("com.whatsapp");
                shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivity(Intent.createChooser(shareIntent, "Share text to.."));;


            }
        });


    }
}
