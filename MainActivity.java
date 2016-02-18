package com.example.madenindya.hitcharide;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {

    EditText edit_cari;
    EditText edit_dari;
    EditText edit_tujuan;
    TextView tx_path;
    DBAdapter db_mydata;

    public final static String EXTRA_MESSAGE = "com.example.madenindya.hitcharide.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // syncron with xml file
        edit_cari = (EditText) findViewById(R.id.et_cari);
        edit_dari = (EditText) findViewById(R.id.et_dari);
        edit_tujuan = (EditText) findViewById(R.id.et_tujuan);
        tx_path = (TextView) findViewById(R.id.text_path);

        // initialize tabhost
        TabHost tabhost = (TabHost) findViewById(R.id.tabHost);
        tabhost.setup();

        TabHost.TabSpec spec1 = tabhost.newTabSpec("spec1");
        spec1.setContent(R.id.tab1);
        spec1.setIndicator("Semua Angkutan");
        tabhost.addTab(spec1);

        TabHost.TabSpec spec2 = tabhost.newTabSpec("spec2");
        spec2.setContent(R.id.tab2);
        spec2.setIndicator("Buat Rute");
        tabhost.addTab(spec2);

        //initialize database;
        openMyDb();

        // insert all data when first build
//        db_mydata.deleteAll();
//        db_mydata.insertAllData();

        // populate listView when starting the app
        populate_listView();

        // listView item click listener
        listViewItemClick();

        // listener for search no trayek field
        edit_cari.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String carifield = edit_cari.getText().toString();

                if (!TextUtils.isEmpty(carifield)) {
                    populate_listView_searchCondition(carifield);
                } else {
                    populate_listView();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    protected void onRestart() {
        super.onRestart();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    private void populate_listView() {
        // get all value in database
        Cursor c = db_mydata.getAllRows();

        // array to store value from database
        String[] fromfieldnames = new String[]{DBAdapter.KEY_NOTRAYEK, DBAdapter.KEY_NAMATRAYEK};
        int[] toviewid = new int[]{R.id.text_nomortrayek, R.id.text_namatrayek};

        // use custom layout, cursor, data from database to the llistview
        SimpleCursorAdapter myadapter;
        myadapter = new SimpleCursorAdapter(getBaseContext(), R.layout.angkotview_layout, c, fromfieldnames, toviewid, 0);

        // get listview from xml and set it with my adapter
        ListView mylist = (ListView) findViewById(R.id.listView_angkots);
        mylist.setAdapter(myadapter);
    }

    private void populate_listView_searchCondition(String str) {
        // get all value in database
        Cursor c = db_mydata.getAllRows_searchCondition(str);

        // array to store value from database
        String[] fromfieldnames = new String[]{DBAdapter.KEY_NOTRAYEK, DBAdapter.KEY_NAMATRAYEK};
        int[] toviewid = new int[]{R.id.text_nomortrayek, R.id.text_namatrayek};

        // use custom layout, cursor, data from database to the llistview
        SimpleCursorAdapter myadapter;
        myadapter = new SimpleCursorAdapter(getBaseContext(), R.layout.angkotview_layout, c, fromfieldnames, toviewid, 0);

        // get listview from xml and set it with my adapter
        ListView mylist = (ListView) findViewById(R.id.listView_angkots);
        mylist.setAdapter(myadapter);
    }

    private void detailIntent(long id) {
        Intent intent = new Intent(this, DetailAngkotActivity.class);
        String message = "" + id;
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }

    private void listViewItemClick() {
        ListView myList = (ListView) findViewById(R.id.listView_angkots);
        myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // if a trayek is selected, close the database
                detailIntent(id);
            }
        });
    }
}
