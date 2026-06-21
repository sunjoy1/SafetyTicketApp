package com.safety.ticket.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "safety_tickets.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 作业票主表
        db.execSQL("CREATE TABLE tickets (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "ticket_type TEXT NOT NULL," +
                "ticket_no TEXT UNIQUE NOT NULL," +
                "apply_unit TEXT," +
                "apply_time TEXT," +
                "location TEXT," +
                "content TEXT," +
                "risk_analysis TEXT," +
                "work_time_start TEXT," +
                "work_time_end TEXT," +
                "status INTEGER DEFAULT 0," +
                "create_time TEXT," +
                "update_time TEXT)");

        // 作业票动态字段表
        db.execSQL("CREATE TABLE ticket_fields (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "ticket_id INTEGER NOT NULL," +
                "field_name TEXT NOT NULL," +
                "field_value TEXT," +
                "FOREIGN KEY(ticket_id) REFERENCES tickets(id) ON DELETE CASCADE)");

        // 安全措施表
        db.execSQL("CREATE TABLE safety_measures (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "ticket_id INTEGER NOT NULL," +
                "measure_no INTEGER NOT NULL," +
                "measure_content TEXT NOT NULL," +
                "is_involved INTEGER DEFAULT 2," +
                "confirm_person TEXT," +
                "FOREIGN KEY(ticket_id) REFERENCES tickets(id) ON DELETE CASCADE)");

        // 签字表
        db.execSQL("CREATE TABLE signatures (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "ticket_id INTEGER NOT NULL," +
                "sign_type TEXT NOT NULL," +
                "sign_person TEXT," +
                "sign_time TEXT," +
                "sign_image BLOB," +
                "sign_comment TEXT," +
                "FOREIGN KEY(ticket_id) REFERENCES tickets(id) ON DELETE CASCADE)");

        // 现场照片表
        db.execSQL("CREATE TABLE photos (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "ticket_id INTEGER NOT NULL," +
                "photo_path TEXT NOT NULL," +
                "photo_desc TEXT," +
                "take_time TEXT," +
                "FOREIGN KEY(ticket_id) REFERENCES tickets(id) ON DELETE CASCADE)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS photos");
        db.execSQL("DROP TABLE IF EXISTS signatures");
        db.execSQL("DROP TABLE IF EXISTS safety_measures");
        db.execSQL("DROP TABLE IF EXISTS ticket_fields");
        db.execSQL("DROP TABLE IF EXISTS tickets");
        onCreate(db);
    }
}
