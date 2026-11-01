package com.aatif.milkhisab;

import android.app.*;
import android.content.*;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends Activity {
    Db db; LinearLayout root; String today,month;
    @Override public void onCreate(Bundle b){super.onCreate(b);db=new Db(this);
        today=new SimpleDateFormat("yyyy-MM-dd",Locale.US).format(new Date());
        month=new SimpleDateFormat("yyyy-MM",Locale.US).format(new Date());render();}
    @Override protected void onResume(){super.onResume();if(db!=null)render();}
    void render(){
        root=Ui.page(this,"🥛 Milk Hisab");
        TextView sub=Ui.text(this,"Daily Milk Manager  |  रोज़ाना दूध का हिसाब",14,0xff607d60);root.addView(sub);
        Ui.gap(root,this,12);
        TextView c=Ui.text(this,"Total Customers  |  कुल ग्राहक\n"+db.customerCount(),19,Ui.DARK);Ui.card(root,c,this);
        TextView m=Ui.text(this,"Today's Milk  |  आज का दूध\n"+String.format(Locale.US,"%.2f KG",db.totalToday(today)),19,Ui.DARK);Ui.card(root,m,this);
        TextView mm=Ui.text(this,"This Month  |  इस महीने\n"+String.format(Locale.US,"%.2f KG",db.totalMonth(month)),19,Ui.DARK);Ui.card(root,mm,this);
        Button customers=Ui.button(this,"Customers  |  ग्राहक");root.addView(customers,lp());customers.setOnClickListener(v->startActivity(new Intent(this,CustomersActivity.class)));
        Button entry=Ui.button(this,"Add Daily Milk  |  दूध दर्ज करें");root.addView(entry,lp());entry.setOnClickListener(v->startActivity(new Intent(this,MilkEntryActivity.class)));
        Button report=Ui.button(this,"Monthly Report  |  मासिक रिपोर्ट");root.addView(report,lp());report.setOnClickListener(v->startActivity(new Intent(this,ReportActivity.class)));
        setContentView(root);
    }
    LinearLayout.LayoutParams lp(){LinearLayout.LayoutParams p=new LinearLayout.LayoutParams(-1,-2);p.bottomMargin=10;return p;}
}
