package org.izv.aad.chatterbot_database;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toolbar;

import org.izv.aad.chatterbot_database.BDConnection.SQLiteChatHelper;
import org.izv.aad.chatterbot_database.ListAdapter.ListaAdapter;
import org.izv.aad.chatterbot_database.POJO.Chat;
import org.izv.aad.chatterbot_database.api.ChatterBot;
import org.izv.aad.chatterbot_database.api.ChatterBotFactory;
import org.izv.aad.chatterbot_database.api.ChatterBotSession;
import org.izv.aad.chatterbot_database.api.ChatterBotType;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

//https://github.com/pierredavidbelanger/chatter-bot-api

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "THELICH";
    public static List<Chat> chatList = new ArrayList<>();
    public static ListaAdapter adapter;

    private ListView lvchats;
    private TextView tvempty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tvempty = findViewById(R.id.tvempty);
        lvchats = findViewById(R.id.lvchats);
        tvempty.setVisibility(View.GONE);

        show();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), StartChat.class);
                startActivity(intent);
            }
        });
    }

    public void show(){
        SQLiteChatHelper chat = new SQLiteChatHelper(this, "DBChats", null, 1);
        SQLiteDatabase db = chat.getReadableDatabase();
        String name;
        String conversation;

        chatList.clear();
        //db.execSQL("DROP TABLE IF EXISTS Chats");
        Cursor c = db.rawQuery("SELECT Name, Conversation FROM Chats", null);

        if (c.moveToFirst()) {
            do {
                name = c.getString(0);
                conversation = c.getString(1);
                chatList.add(new Chat(name, conversation));
                Log.v(TAG,"nombre: "+name+" Conversacion: "+conversation);
            } while(c.moveToNext());
        }
        db.close();

        if(chatList.isEmpty()){
            tvempty.setVisibility(View.VISIBLE);
            Log.v(TAG, "está vacío");
        }else{
            Log.v(TAG, "Contiene algo");
            tvempty.setVisibility(View.GONE);
            adapter = new ListaAdapter(MainActivity.this, chatList);
            lvchats.setAdapter(adapter);
        }
    }
}
