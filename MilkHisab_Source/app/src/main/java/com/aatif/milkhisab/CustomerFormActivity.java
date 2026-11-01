package com.aatif.milkhisab;

import android.app.*;
import android.os.Bundle;
import android.text.InputType;
import android.widget.*;
import android.database.Cursor;

public class CustomerFormActivity extends Activity {
    Db db;long id=-1;EditText name,phone,rate;
    @Override public void onCreate(Bundle b){super.onCreate(b);db=new Db(this);id=getIntent().getLongExtra("id",-1);
        LinearLayout root=Ui.page(this,id==-1?"Add Customer | ग्राहक जोड़ें":"Edit Customer | ग्राहक बदलें");
        name=Ui.edit(this,"Customer name | ग्राहक का नाम",InputType.TYPE_CLASS_TEXT);root.addView(name);
        Ui.gap(root,this,10);phone=Ui.edit(this,"Phone (optional) | मोबाइल (वैकल्पिक)",InputType.TYPE_CLASS_PHONE);root.addView(phone);
        Ui.gap(root,this,10);rate=Ui.edit(this,"Rate per KG (₹) | प्रति KG रेट",InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);root.addView(rate);
        if(id!=-1){Cursor c=db.customer(id);if(c.moveToFirst()){name.setText(c.getString(1));phone.setText(c.getString(2));rate.setText(String.valueOf(c.getDouble(3)));}c.close();}
        Ui.gap(root,this,15);Button save=Ui.button(this,"Save Customer | सेव करें");root.addView(save);
        save.setOnClickListener(v->save());setContentView(root);
    }
    void save(){
        String n=name.getText().toString().trim(),p=phone.getText().toString().trim(),r=rate.getText().toString().trim();
        if(n.isEmpty()||r.isEmpty()){Ui.toast(this,"Name and rate are required");return;}
        try{double x=Double.parseDouble(r);if(x<0)throw new Exception();
            if(id==-1)db.addCustomer(n,p,x);else db.updateCustomer(id,n,p,x);
            Ui.toast(this,"Customer saved");finish();
        }catch(Exception e){Ui.toast(this,"Enter a valid rate");}
    }
}
