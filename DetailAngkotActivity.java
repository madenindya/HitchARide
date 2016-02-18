package com.example.madenindya.hitcharide;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


public class DetailAngkotActivity extends ActionBarActivity {

    long id;

    TextView tv_dtnomorTrayek;
    TextView tv_dtnamaTrayek;
    TextView tv_dtjenisAngkutan;
    TextView tv_dtwilayah;
    TextView tv_dtruteBerangkat;
    TextView tv_dtruteKembali;
    DBAdapter db_mydata;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_angkot);

        tv_dtnomorTrayek = (TextView)findViewById(R.id.textView_detail_nomorTrayek);
        tv_dtnamaTrayek = (TextView)findViewById(R.id.textView_detail_namaTrayek);
        tv_dtjenisAngkutan = (TextView)findViewById(R.id.textView_detail_jenisAngkutan);
        tv_dtwilayah = (TextView)findViewById(R.id.textView_detail_wilayah);
        tv_dtruteBerangkat = (TextView)findViewById(R.id.textView_detail_ruteBerangkat);
        tv_dtruteKembali = (TextView)findViewById(R.id.textView_detail_ruteKembali);

        Intent intent = getIntent();
        long id = Long.parseLong(intent.getStringExtra(MainActivity.EXTRA_MESSAGE));

        //initialize database;
        openMyDb();

        Cursor c = db_mydata.getRow(id);

        // set all textView
        tv_dtnomorTrayek.setText(c.getString(1));
        tv_dtnamaTrayek.setText(c.getString(2));
        tv_dtjenisAngkutan.setText(c.getString(3));
        tv_dtwilayah.setText(c.getString(4));
        tv_dtruteBerangkat.setText(c.getString(5));
        tv_dtruteKembali.setText(c.getString(6));


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail_angkot, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void openMyDb() {
        db_mydata = new DBAdapter(this);
        db_mydata.open();
    }
}
