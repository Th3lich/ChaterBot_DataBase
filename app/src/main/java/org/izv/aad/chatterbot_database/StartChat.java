package org.izv.aad.chatterbot_database;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import org.izv.aad.chatterbot_database.BDConnection.SQLiteChatHelper;
import org.izv.aad.chatterbot_database.api.ChatterBot;
import org.izv.aad.chatterbot_database.api.ChatterBotFactory;
import org.izv.aad.chatterbot_database.api.ChatterBotSession;
import org.izv.aad.chatterbot_database.api.ChatterBotType;

public class StartChat extends AppCompatActivity {

    private Button btSend;
    private EditText etTexto;
    private ScrollView svScroll;
    private TextView tvconversation;

    private ChatterBot bot;
    private ChatterBotFactory factory;
    private ChatterBotSession botSession;
    private Hilo hilo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_chat);

        init();
    }

    private void init() {
        btSend = findViewById(R.id.btSend);
        etTexto = findViewById(R.id.etTexto);
        svScroll = findViewById(R.id.svScroll);
        tvconversation = findViewById(R.id.tvconversation);
        if(startBot()) {
            setEvents();
        }
    }

    private void chat(final String text) {
        String response;
        try {
            response = getString(R.string.bot) + " " + botSession.think(text);
        } catch (final Exception e) {
            response = getString(R.string.exception) + " " + e.toString();
        }
        tvconversation.post(showMessage(response));
    }

    private void setEvents() {
        btSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String text = getString(R.string.you) + " " + etTexto.getText().toString().trim();
                btSend.setEnabled(false);
                etTexto.setText("");
                tvconversation.append(text + "\n");
                hilo = new Hilo(text);
                hilo.execute();
            }
        });
    }

    private boolean startBot() {
        boolean result = true;
        String initialMessage;
        factory = new ChatterBotFactory();
        try {
            bot = factory.create(ChatterBotType.PANDORABOTS, "b0dafd24ee35a477");
            botSession = bot.createSession();
            initialMessage = getString(R.string.messageConnected) + "\n";
        } catch(Exception e) {
            initialMessage = getString(R.string.messageException) + "\n" + getString(R.string.exception) + " " + e.toString();
            result = false;
        }
        tvconversation.setText(initialMessage);
        return result;
    }

    private Runnable showMessage(final String message) {
        return new Runnable() {
            @Override
            public void run() {
                etTexto.requestFocus();
                tvconversation.append(message + "\n");
                svScroll.fullScroll(View.FOCUS_DOWN);
                btSend.setEnabled(true);
                hideKeyboard();
            }
        };
    }

    public void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = getCurrentFocus();
        if (view == null) {
            view = new View(this);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.save) {
            String conversation = tvconversation.getText().toString();
            conversation = conversation.replace("'"," ");
            String name = "Chat 1";

            if(conversation.length() > 0){
                SQLiteChatHelper chat = new SQLiteChatHelper(this, "DBChats", null, 1);
                SQLiteDatabase db = chat.getReadableDatabase();
                Cursor c = db.rawQuery("SELECT MAX(ChatId) FROM Chats", null);
                if (c.moveToFirst()) {
                    do {
                        int posicion = c.getInt(0)+1;
                        name = "Chat "+posicion;
                    } while(c.moveToNext());
                }

                db = chat.getWritableDatabase();

                db.execSQL("INSERT INTO Chats (Name, Conversation) VALUES ('"+name+"', '"+conversation+"');");
                db.close();

                Toast.makeText(this, "El chat se ha guardado exitosamente", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this, "El chat no se ha podido guardar", Toast.LENGTH_SHORT).show();
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public class Hilo extends android.os.AsyncTask {
        String text;

        public Hilo(String text) {
            this.text = text;
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            chat(text);
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
        }
    }
}
