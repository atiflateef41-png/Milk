package com.aatif.milkhisab;

import android.app.*;
import android.os.Bundle;
import android.text.InputType;
import android.view.*;
import android.widget.*;
import android.database.Cursor;
import java.text.SimpleDateFormat;
import java.util.*;

public class ReportActivity extends Activity {
    Db db;EditText month;LinearLayout root,rows;
    @Override public void onCreate(Bundle b){super.onCreate(b);db=new Db(this);render();}
    void render(){
        root=Ui.page(this,"Monthly Report | मासिक रिपोर्ट");
        month=Ui.edit(this,"Month YYYY-MM",InputType.TYPE_CLASS_TEXT);month.setText(new SimpleDateFormat("yyyy-MM",Locale.US).format(new Date()));root.addView(month);
        Button show=Ui.button(this,"Calculate | हिसाब करें");root.addView(show);show.setOnClickListener(v->loadRows());
        ScrollView sc=new ScrollView(this);rows=new LinearLayout(this);rows.setOrientation(1);sc.addView(rows);root.addView(sc,new LinearLayout.LayoutParams(-1,0,1));
        loadRows();setContentView(root);
    }
    void loadRows(){
        rows.removeAllViews();String m=month.getText().toString().trim();
        if(!m.matches("\\d{4}-\\d{2}")){Ui.toast(this,"Month format must be YYYY-MM");return;}
        Cursor c=db.monthRows(m);double grand=0,paidTotal=0;
        while(c.moveToNext()){
            long id=c.getLong(0);String name=c.getString(1);double rate=c.getDouble(2),kg=c.getDouble(3),paid=c.getDouble(4),bill=rate*kg,pending=Math.max(0,bill-paid);
            grand+=bill;paidTotal+=paid;
            LinearLayout card=new LinearLayout(this);card.setOrientation(1);
            card.addView(Ui.text(this,name,19,Ui.DARK));
            card.addView(Ui.text(this,String.format(Locale.US,"%.2f KG × %s = %s",kg,Ui.money(rate),Ui.money(bill)),15,0xff333333));
            card.addView(Ui.text(this,"Paid: "+Ui.money(paid)+"   Pending: "+Ui.money(pending),14,pending>0?0xffb71c1c:Ui.GREEN));
            Button payment=Ui.button(this,"Update payment | भुगतान अपडेट");
            card.addView(payment);payment.setOnClickListener(v->paymentDialog(id,m,bill,paid));
            TextView history=Ui.text(this,"View daily entries | रोज़ की एंट्री देखें",14,Ui.GREEN);card.addView(history);
            history.setOnClickListener(v->showEntries(id,name,m));
            Ui.card(rows,card,this);
        }
        c.close();
        rows.addView(Ui.text(this,"Grand total: "+Ui.money(grand)+"    Paid: "+Ui.money(paidTotal)+"    Pending: "+Ui.money(Math.max(0,grand-paidTotal)),16,Ui.DARK));
    }
    void paymentDialog(long id,String m,double bill,double oldPaid){
        EditText input=Ui.edit(this,"Total amount paid so far",InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);input.setText(String.valueOf(oldPaid));
        new AlertDialog.Builder(this).setTitle("Update payment").setView(input).setNegativeButton("Cancel",null).setPositiveButton("Save",(d,w)->{
            try{double x=Double.parseDouble(input.getText().toString());if(x<0||x>bill)throw new Exception();db.setPaid(id,m,x);loadRows();}
            catch(Exception e){Ui.toast(this,"Enter amount between 0 and bill total");}
        }).show();
    }
    void showEntries(long id,String name,String m){
        Cursor c=db.entriesFor(id,m);StringBuilder s=new StringBuilder();
        while(c.moveToNext())s.append(c.getString(0)).append(" : ").append(c.getDouble(1)).append(" KG\\n");
        c.close();if(s.length()==0)s.append("No entries for this month");
        new AlertDialog.Builder(this).setTitle(name+" — "+m).setMessage(s.toString()).setPositiveButton("Close",null).show();
    }
}
