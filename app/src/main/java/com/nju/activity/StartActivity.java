package com.nju.activity;

import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nju.activity.R;
import com.nju.db.ProvinceEntry;
import com.nju.mail.GMailSender;
import com.nju.model.College;
import com.nju.model.Province;

import com.nju.service.ProvinceService;
import com.nju.util.Constant;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class StartActivity extends AppCompatActivity {

    private static final String TAG = StartActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        try {
            GMailSender sender = new GMailSender("","");
            sender.sendMail("This is Subject",
                    "This is Body",
                    "15261893862@163.com",
                    "1637269790@qq.com"
            );
        } catch (Exception e) {
            Log.e("SendMail", e.getMessage(), e);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_start, menu);

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

    private class ImportDb extends AsyncTask<Void,Void,String>{


        @Override
        protected String doInBackground(Void... params) {

//            StringBuilder stringBuilder = new StringBuilder();
//            try {
//                InputStream inputStream = getAssets().open("province.json");
//                Reader reader = new InputStreamReader(inputStream);
//                char[] bf = new char[Constant.MAX_BUFFER];
//                int len = 0;
//                while((len = reader.read(bf))>0){
//                    stringBuilder.append(bf,0,len);
//                }
//                reader.close();
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            return stringBuilder.toString();
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
//            Gson gson = new Gson();
//            Type datasetListType = new TypeToken<Collection<Province>>() {}.getType();
//            ArrayList<Province> colleges = gson.fromJson(s, datasetListType);
//            ProvinceService service = new ProvinceService(getApplicationContext());
//            service.save(colleges);
        }
    }
}
