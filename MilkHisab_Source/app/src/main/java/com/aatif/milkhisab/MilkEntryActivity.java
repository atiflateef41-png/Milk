package com.aatif.milkhisab;

import android.app.*;
import android.os.Bundle;
import android.text.InputType;
import android.view.*;
import android.widget.*;
import android.database.Cursor;
import java.text.SimpleDateFormat;
import java.util.*;

public class MilkEntryActivity extends Activity {
    Db db;Spinner customers;EditText date,kg;ArrayList<Long> ids=new ArrayList<>();
    @Override public void onCreate(Bundle b){super.onCreate(b);db=new Db(this);
        LinearLayout root=Ui.page(this,"Daily Milk Entry | रोज़ का दूध");
        customers=new Spinner(this);ArrayList<String> names=new ArrayList<>();Cursor c=db.customers();
        while(c.moveToNext()){ids.add(c.getLong(0));names.add(c.getString(1)+" — "+Ui.money(c.getDouble(3))+"/KG");}c.close();
        customers.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,names));root.addView(Ui.text(this,"Select customer | ग्राहक चुनें",14,Ui.DARK));root.addView(customers);
        Ui.gap(root,this,12);root.addView(Ui.text(this,"Date | तारीख (YYYY-MM-DD)",14,Ui.DARK));
        date=Ui.edit(this,"2026-11-01",InputType.TYPE_CLASS_TEXT);date.setText(new SimpleDateFormat("yyyy-MM-dd",Locale.US).format(new Date()));root.addView(date);
        Ui.gap(root,this,12);root.addView(Ui.text(this,"Milk quantity in KG | दूध KG में",14,Ui.DARK));
        kg=Ui.edit(this,"Example: 1.5",InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);root.addView(kg);
        Ui.gap(root,this,15);Button save=Ui.button(this,"Save Entry | एंट्री सेव करें");root.addView(save);save.setOnClickListener(v->save());
        if(ids.isEmpty())save.setEnabled(false);setContentView(root);
    }
    void save(){
        if(ids.isEmpty()){Ui.toast(this,"Add a customer first");return;}
        String d=date.getText().toString().trim(),q=kg.getText().toString().trim();
        if(!d.matches("\\d{4}-\\d{2}-\\d{2}")||q.isEmpty()){Ui.toast(this,"Enter date and quantity");return;}
        try{double x=Double.parseDouble(q);if(x<=0)throw new Exception();long result=db.addEntry(ids.get(customers.getSelectedItemPosition()),d,x);
            if(result==-1)Ui.toast(this,"Could not save entry");else{Ui.toast(this,"Milk entry saved");finish();}
        }catch(Exception e){Ui.toast(this,"Enter valid quantity");}
    }
}
