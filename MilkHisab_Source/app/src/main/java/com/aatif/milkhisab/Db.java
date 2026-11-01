package com.aatif.milkhisab;

import android.content.*;
import android.database.Cursor;
import android.database.sqlite.*;
import java.util.*;

public class Db extends SQLiteOpenHelper {
    public Db(Context c) { super(c, "milk_hisab.db", null, 1); }
    @Override public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE customers(id INTEGER PRIMARY KEY AUTOINCREMENT,name TEXT NOT NULL,phone TEXT,rate REAL NOT NULL DEFAULT 0)");
        db.execSQL("CREATE TABLE entries(id INTEGER PRIMARY KEY AUTOINCREMENT,customer_id INTEGER NOT NULL,entry_date TEXT NOT NULL,kg REAL NOT NULL,FOREIGN KEY(customer_id) REFERENCES customers(id))");
        db.execSQL("CREATE TABLE payments(id INTEGER PRIMARY KEY AUTOINCREMENT,customer_id INTEGER NOT NULL,month TEXT NOT NULL,paid REAL NOT NULL DEFAULT 0,UNIQUE(customer_id,month))");
    }
    @Override public void onUpgrade(SQLiteDatabase db,int oldV,int newV) {}
    public long addCustomer(String n,String p,double r) {
        ContentValues v=new ContentValues(); v.put("name",n);v.put("phone",p);v.put("rate",r);
        return getWritableDatabase().insert("customers",null,v);
    }
    public void updateCustomer(long id,String n,String p,double r) {
        ContentValues v=new ContentValues();v.put("name",n);v.put("phone",p);v.put("rate",r);
        getWritableDatabase().update("customers",v,"id=?",new String[]{String.valueOf(id)});
    }
    public void deleteCustomer(long id) {
        SQLiteDatabase d=getWritableDatabase();d.delete("entries","customer_id=?",new String[]{""+id});
        d.delete("payments","customer_id=?",new String[]{""+id});d.delete("customers","id=?",new String[]{""+id});
    }
    public Cursor customers() { return getReadableDatabase().rawQuery("SELECT id,name,phone,rate FROM customers ORDER BY name COLLATE NOCASE",null); }
    public Cursor customer(long id) { return getReadableDatabase().rawQuery("SELECT id,name,phone,rate FROM customers WHERE id=?",new String[]{""+id}); }
    public long addEntry(long cid,String date,double kg) {
        ContentValues v=new ContentValues();v.put("customer_id",cid);v.put("entry_date",date);v.put("kg",kg);
        return getWritableDatabase().insert("entries",null,v);
    }
    public Cursor monthRows(String month) {
        return getReadableDatabase().rawQuery(
            "SELECT c.id,c.name,c.rate,COALESCE(SUM(e.kg),0),COALESCE(p.paid,0) " +
            "FROM customers c LEFT JOIN entries e ON e.customer_id=c.id AND substr(e.entry_date,1,7)=? " +
            "LEFT JOIN payments p ON p.customer_id=c.id AND p.month=? " +
            "GROUP BY c.id ORDER BY c.name COLLATE NOCASE",new String[]{month,month});
    }
    public Cursor entriesFor(long cid,String month) {
        return getReadableDatabase().rawQuery("SELECT entry_date,kg FROM entries WHERE customer_id=? AND substr(entry_date,1,7)=? ORDER BY entry_date",
            new String[]{""+cid,month});
    }
    public void setPaid(long cid,String month,double paid) {
        SQLiteDatabase d=getWritableDatabase();
        ContentValues v=new ContentValues();v.put("customer_id",cid);v.put("month",month);v.put("paid",paid);
        d.insertWithOnConflict("payments",null,v,SQLiteDatabase.CONFLICT_REPLACE);
    }
    public double totalToday(String date) {
        Cursor c=getReadableDatabase().rawQuery("SELECT COALESCE(SUM(kg),0) FROM entries WHERE entry_date=?",new String[]{date});
        double x=0;if(c.moveToFirst())x=c.getDouble(0);c.close();return x;
    }
    public int customerCount() {
        Cursor c=getReadableDatabase().rawQuery("SELECT COUNT(*) FROM customers",null);
        int n=0;if(c.moveToFirst())n=c.getInt(0);c.close();return n;
    }
    public double totalMonth(String month) {
        Cursor c=getReadableDatabase().rawQuery("SELECT COALESCE(SUM(kg),0) FROM entries WHERE substr(entry_date,1,7)=?",new String[]{month});
        double n=0;if(c.moveToFirst())n=c.getDouble(0);c.close();return n;
    }
}
