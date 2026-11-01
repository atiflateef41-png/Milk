package com.aatif.milkhisab;

import android.app.*;
import android.content.*;
import android.database.Cursor;
import android.os.Bundle;
import android.view.*;
import android.widget.*;

public class CustomersActivity extends Activity {
    Db db;LinearLayout root,list;
    @Override public void onCreate(Bundle b){super.onCreate(b);db=new Db(this);render();}
    @Override protected void onResume(){super.onResume();if(db!=null)render();}
    void render(){
        root=Ui.page(this,"Customers | ग्राहक");
        Button add=Ui.button(this,"+ Add Customer | ग्राहक जोड़ें");root.addView(add);add.setOnClickListener(v->startActivity(new Intent(this,CustomerFormActivity.class)));
        ScrollView sc=new ScrollView(this);list=new LinearLayout(this);list.setOrientation(1);sc.addView(list);root.addView(sc,new LinearLayout.LayoutParams(-1,0,1));
        Cursor c=db.customers();
        if(c.getCount()==0)list.addView(Ui.text(this,"No customers yet. Add your first customer.",15,0xff666666));
        while(c.moveToNext()){
            long id=c.getLong(0);String name=c.getString(1),phone=c.getString(2);double rate=c.getDouble(3);
            LinearLayout row=new LinearLayout(this);row.setOrientation(1);
            TextView info=Ui.text(this,name+"\n"+(phone==null?"":phone)+"   •   "+Ui.money(rate)+"/KG",17,Ui.DARK);row.addView(info);
            LinearLayout actions=new LinearLayout(this);
            Button edit=Ui.button(this,"Edit");Button del=Ui.button(this,"Delete");
            actions.addView(edit,new LinearLayout.LayoutParams(0,-2,1));actions.addView(del,new LinearLayout.LayoutParams(0,-2,1));row.addView(actions);
            edit.setOnClickListener(v->{Intent i=new Intent(this,CustomerFormActivity.class);i.putExtra("id",id);startActivity(i);});
            del.setOnClickListener(v->new AlertDialog.Builder(this).setMessage("Delete "+name+" and its milk records?").setNegativeButton("Cancel",null).setPositiveButton("Delete",(d,w)->{db.deleteCustomer(id);render();}).show());
            Ui.card(list,row,this);
        }
        c.close();setContentView(root);
    }
}
