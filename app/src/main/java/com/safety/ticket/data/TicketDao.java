package com.safety.ticket.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class TicketDao {
    private DatabaseHelper dbHelper;

    public TicketDao(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public long insertTicket(Ticket ticket) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("ticket_type", ticket.getTicketType());
        values.put("ticket_no", ticket.getTicketNo());
        values.put("apply_unit", ticket.getApplyUnit());
        values.put("apply_time", ticket.getApplyTime());
        values.put("location", ticket.getLocation());
        values.put("content", ticket.getContent());
        values.put("risk_analysis", ticket.getRiskAnalysis());
        values.put("work_time_start", ticket.getWorkTimeStart());
        values.put("work_time_end", ticket.getWorkTimeEnd());
        values.put("status", ticket.getStatus());
        values.put("create_time", ticket.getCreateTime());
        values.put("update_time", ticket.getUpdateTime());
        long id = db.insert("tickets", null, values);
        db.close();
        return id;
    }

    public void updateTicket(Ticket ticket) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("apply_unit", ticket.getApplyUnit());
        values.put("apply_time", ticket.getApplyTime());
        values.put("location", ticket.getLocation());
        values.put("content", ticket.getContent());
        values.put("risk_analysis", ticket.getRiskAnalysis());
        values.put("work_time_start", ticket.getWorkTimeStart());
        values.put("work_time_end", ticket.getWorkTimeEnd());
        values.put("status", ticket.getStatus());
        values.put("update_time", ticket.getUpdateTime());
        db.update("tickets", values, "id=?", new String[]{String.valueOf(ticket.getId())});
        db.close();
    }

    public void deleteTicket(long ticketId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete("tickets", "id=?", new String[]{String.valueOf(ticketId)});
        db.close();
    }

    public Ticket getTicketById(long ticketId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query("tickets", null, "id=?", 
                new String[]{String.valueOf(ticketId)}, null, null, null);
        Ticket ticket = null;
        if (cursor.moveToFirst()) {
            ticket = cursorToTicket(cursor);
        }
        cursor.close();
        db.close();
        return ticket;
    }

    public List<Ticket> getAllTickets() {
        List<Ticket> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query("tickets", null, null, null, null, null, "create_time DESC");
        while (cursor.moveToNext()) {
            list.add(cursorToTicket(cursor));
        }
        cursor.close();
        db.close();
        return list;
    }

    public List<Ticket> getTicketsByType(String type) {
        List<Ticket> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query("tickets", null, "ticket_type=?", 
                new String[]{type}, null, null, "create_time DESC");
        while (cursor.moveToNext()) {
            list.add(cursorToTicket(cursor));
        }
        cursor.close();
        db.close();
        return list;
    }

    public List<Ticket> searchTickets(String keyword) {
        List<Ticket> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query("tickets", null, 
                "ticket_no LIKE ? OR apply_unit LIKE ? OR location LIKE ? OR content LIKE ?",
                new String[]{"%"+keyword+"%", "%"+keyword+"%", "%"+keyword+"%", "%"+keyword+"%"},
                null, null, "create_time DESC");
        while (cursor.moveToNext()) {
            list.add(cursorToTicket(cursor));
        }
        cursor.close();
        db.close();
        return list;
    }

    // Safety Measures
    public void insertMeasures(long ticketId, List<SafetyMeasure> measures) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        for (SafetyMeasure m : measures) {
            ContentValues values = new ContentValues();
            values.put("ticket_id", ticketId);
            values.put("measure_no", m.getMeasureNo());
            values.put("measure_content", m.getMeasureContent());
            values.put("is_involved", m.getIsInvolved());
            values.put("confirm_person", m.getConfirmPerson());
            db.insert("safety_measures", null, values);
        }
        db.close();
    }

    public void updateMeasure(SafetyMeasure measure) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("is_involved", measure.getIsInvolved());
        values.put("confirm_person", measure.getConfirmPerson());
        db.update("safety_measures", values, "id=?", 
                new String[]{String.valueOf(measure.getId())});
        db.close();
    }

    public List<SafetyMeasure> getMeasuresByTicketId(long ticketId) {
        List<SafetyMeasure> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query("safety_measures", null, "ticket_id=?",
                new String[]{String.valueOf(ticketId)}, null, null, "measure_no ASC");
        while (cursor.moveToNext()) {
            SafetyMeasure m = new SafetyMeasure();
            m.setId(cursor.getLong(cursor.getColumnIndexOrThrow("id")));
            m.setTicketId(cursor.getLong(cursor.getColumnIndexOrThrow("ticket_id")));
            m.setMeasureNo(cursor.getInt(cursor.getColumnIndexOrThrow("measure_no")));
            m.setMeasureContent(cursor.getString(cursor.getColumnIndexOrThrow("measure_content")));
            m.setIsInvolved(cursor.getInt(cursor.getColumnIndexOrThrow("is_involved")));
            m.setConfirmPerson(cursor.getString(cursor.getColumnIndexOrThrow("confirm_person")));
            list.add(m);
        }
        cursor.close();
        db.close();
        return list;
    }

    // Signatures
    public void insertSignature(Signature signature) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("ticket_id", signature.getTicketId());
        values.put("sign_type", signature.getSignType());
        values.put("sign_person", signature.getSignPerson());
        values.put("sign_time", signature.getSignTime());
        if (signature.getSignImage() != null) {
            values.put("sign_image", signature.getSignImage());
        }
        values.put("sign_comment", signature.getSignComment());
        db.insert("signatures", null, values);
        db.close();
    }

    public void updateSignature(Signature signature) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("sign_person", signature.getSignPerson());
        values.put("sign_time", signature.getSignTime());
        if (signature.getSignImage() != null) {
            values.put("sign_image", signature.getSignImage());
        }
        values.put("sign_comment", signature.getSignComment());
        db.update("signatures", values, "id=?", 
                new String[]{String.valueOf(signature.getId())});
        db.close();
    }

    public List<Signature> getSignaturesByTicketId(long ticketId) {
        List<Signature> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query("signatures", null, "ticket_id=?",
                new String[]{String.valueOf(ticketId)}, null, null, "sign_type ASC");
        while (cursor.moveToNext()) {
            Signature s = new Signature();
            s.setId(cursor.getLong(cursor.getColumnIndexOrThrow("id")));
            s.setTicketId(cursor.getLong(cursor.getColumnIndexOrThrow("ticket_id")));
            s.setSignType(cursor.getString(cursor.getColumnIndexOrThrow("sign_type")));
            s.setSignPerson(cursor.getString(cursor.getColumnIndexOrThrow("sign_person")));
            s.setSignTime(cursor.getString(cursor.getColumnIndexOrThrow("sign_time")));
            s.setSignImage(cursor.getBlob(cursor.getColumnIndexOrThrow("sign_image")));
            s.setSignComment(cursor.getString(cursor.getColumnIndexOrThrow("sign_comment")));
            list.add(s);
        }
        cursor.close();
        db.close();
        return list;
    }

    public Signature getSignatureByType(long ticketId, String signType) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query("signatures", null, "ticket_id=? AND sign_type=?",
                new String[]{String.valueOf(ticketId), signType}, null, null, null);
        Signature s = null;
        if (cursor.moveToFirst()) {
            s = new Signature();
            s.setId(cursor.getLong(cursor.getColumnIndexOrThrow("id")));
            s.setTicketId(cursor.getLong(cursor.getColumnIndexOrThrow("ticket_id")));
            s.setSignType(cursor.getString(cursor.getColumnIndexOrThrow("sign_type")));
            s.setSignPerson(cursor.getString(cursor.getColumnIndexOrThrow("sign_person")));
            s.setSignTime(cursor.getString(cursor.getColumnIndexOrThrow("sign_time")));
            s.setSignImage(cursor.getBlob(cursor.getColumnIndexOrThrow("sign_image")));
            s.setSignComment(cursor.getString(cursor.getColumnIndexOrThrow("sign_comment")));
        }
        cursor.close();
        db.close();
        return s;
    }

    // Photos
    public void insertPhoto(Photo photo) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("ticket_id", photo.getTicketId());
        values.put("photo_path", photo.getPhotoPath());
        values.put("photo_desc", photo.getPhotoDesc());
        values.put("take_time", photo.getTakeTime());
        db.insert("photos", null, values);
        db.close();
    }

    public void deletePhoto(long photoId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete("photos", "id=?", new String[]{String.valueOf(photoId)});
        db.close();
    }

    public List<Photo> getPhotosByTicketId(long ticketId) {
        List<Photo> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query("photos", null, "ticket_id=?",
                new String[]{String.valueOf(ticketId)}, null, null, "take_time DESC");
        while (cursor.moveToNext()) {
            Photo p = new Photo();
            p.setId(cursor.getLong(cursor.getColumnIndexOrThrow("id")));
            p.setTicketId(cursor.getLong(cursor.getColumnIndexOrThrow("ticket_id")));
            p.setPhotoPath(cursor.getString(cursor.getColumnIndexOrThrow("photo_path")));
            p.setPhotoDesc(cursor.getString(cursor.getColumnIndexOrThrow("photo_desc")));
            p.setTakeTime(cursor.getString(cursor.getColumnIndexOrThrow("take_time")));
            list.add(p);
        }
        cursor.close();
        db.close();
        return list;
    }

    public void loadTicketDetails(Ticket ticket) {
        ticket.setMeasures(getMeasuresByTicketId(ticket.getId()));
        ticket.setSignatures(getSignaturesByTicketId(ticket.getId()));
        ticket.setPhotos(getPhotosByTicketId(ticket.getId()));
    }

    private Ticket cursorToTicket(Cursor cursor) {
        Ticket ticket = new Ticket();
        ticket.setId(cursor.getLong(cursor.getColumnIndexOrThrow("id")));
        ticket.setTicketType(cursor.getString(cursor.getColumnIndexOrThrow("ticket_type")));
        ticket.setTicketNo(cursor.getString(cursor.getColumnIndexOrThrow("ticket_no")));
        ticket.setApplyUnit(cursor.getString(cursor.getColumnIndexOrThrow("apply_unit")));
        ticket.setApplyTime(cursor.getString(cursor.getColumnIndexOrThrow("apply_time")));
        ticket.setLocation(cursor.getString(cursor.getColumnIndexOrThrow("location")));
        ticket.setContent(cursor.getString(cursor.getColumnIndexOrThrow("content")));
        ticket.setRiskAnalysis(cursor.getString(cursor.getColumnIndexOrThrow("risk_analysis")));
        ticket.setWorkTimeStart(cursor.getString(cursor.getColumnIndexOrThrow("work_time_start")));
        ticket.setWorkTimeEnd(cursor.getString(cursor.getColumnIndexOrThrow("work_time_end")));
        ticket.setStatus(cursor.getInt(cursor.getColumnIndexOrThrow("status")));
        ticket.setCreateTime(cursor.getString(cursor.getColumnIndexOrThrow("create_time")));
        ticket.setUpdateTime(cursor.getString(cursor.getColumnIndexOrThrow("update_time")));
        return ticket;
    }
}
