package com.aatif.milkhisab;

import android.app.*;
import android.content.*;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.view.*;
import android.widget.*;

public class Ui {
    public static final int GREEN=Color.rgb(46,125,50), DARK=Color.rgb(27,94,32), BG=Color.rgb(244,248,244);
    public static GradientDrawable bg(int color,int radius) {
        GradientDrawable d=new GradientDrawable();d.setColor(color);d.setCornerRadius(radius);return d;
    }
    public static LinearLayout page(Activity a,String title) {
        LinearLayout root=new LinearLayout(a);root.setOrientation(1);root.setPadding(18,16,18,18);root.setBackgroundColor(BG);
        TextView t=new TextView(a);t.setText(title);t.setTextSize(24);t.setTypeface(null,Typeface.BOLD);t.setTextColor(DARK);
        t.setPadding(2,8,2,18);root.addView(t);return root;
    }
    public static TextView text(Context c,String s,int size,int color) {
        TextView t=new TextView(c);t.setText(s);t.setTextSize(size);t.setTextColor(color);t.setPadding(0,5,0,5);return t;
    }
    public static Button button(Context c,String s) {
        Button b=new Button(c);b.setText(s);b.setTextColor(Color.WHITE);b.setAllCaps(false);b.setBackground(bg(GREEN,18));return b;
    }
    public static EditText edit(Context c,String hint,int inputType) {
        EditText e=new EditText(c);e.setSingleLine(true);e.setHint(hint);e.setInputType(inputType);
        e.setPadding(14,8,14,8);e.setBackground(bg(Color.WHITE,12));return e;
    }
    public static void gap(LinearLayout l,Context c,int h) { View v=new View(c);l.addView(v,new LinearLayout.LayoutParams(1,h)); }
    public static void card(LinearLayout root,View child,Context c) {
        LinearLayout wrap=new LinearLayout(c);wrap.setOrientation(1);wrap.setPadding(15,12,15,12);wrap.setBackground(bg(Color.WHITE,18));
        wrap.addView(child);LinearLayout.LayoutParams p=new LinearLayout.LayoutParams(-1,-2);p.bottomMargin=12;root.addView(wrap,p);
    }
    public static String money(double n) { return "₹"+String.format(java.util.Locale.US,"%.2f",n); }
    public static void toast(Context c,String s) { Toast.makeText(c,s,Toast.LENGTH_SHORT).show(); }
}
