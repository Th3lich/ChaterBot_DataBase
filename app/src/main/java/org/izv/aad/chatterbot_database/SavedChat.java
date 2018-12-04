package org.izv.aad.chatterbot_database;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class SavedChat extends AppCompatActivity {

    private TextView tvheader, tvconversation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_chat);

        tvheader = findViewById(R.id.tvheader2);
        tvconversation = findViewById(R.id.tvconversation2);

        Intent intent = getIntent();

        String name = intent.getStringExtra("name");
        String conversation = intent.getStringExtra("conversation");

        tvheader.setText(name);
        tvconversation.setText(conversation);

    }
}
