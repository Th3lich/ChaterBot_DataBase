package org.izv.aad.chatterbot_database.BDConnection;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteChatHelper extends SQLiteOpenHelper {

    //Creamos una variable que contendrá la sentencia SQL de creación de la tabla
    String sql = "CREATE TABLE IF  NOT EXISTS Chats (ChatId INTEGER primary key AUTOINCREMENT, Name TEXT, Conversation TEXT)";

    public SQLiteChatHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);

    }

    public void onCreate(SQLiteDatabase db) {
        //Éste método se ejecuta automáticamente cuando la base de datos no existe
        //es decir, cuando se llama por primera vez a la clase, crea la BD
        db.execSQL(sql);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        //Éste método se ejecuta cuando se detecta que la versión de la base de datos
        //cambió, por lo que se debe definir todos los procesos de migración de la estructura
        //anterior a la estructura nueva. Por simplicidad del ejemplo, lo que haremos es eliminar la
        //tabla existente y crearla nuevamente
        db.execSQL("DROP TABLE IF EXISTS Chats");
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        onUpgrade(db, oldVersion, newVersion);
    }
}
