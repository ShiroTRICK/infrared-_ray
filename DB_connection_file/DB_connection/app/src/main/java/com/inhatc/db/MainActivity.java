package com.inhatc.db;


import android.app.Activity;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;


public class MainActivity extends Activity implements View.OnClickListener{
    String TAG = "main";
    String myJSON;
    private static final String TAG_RESULTS = "result";
    private static final String TAG_ID = "username";
    private static final String TAG_NAME = "pw";

    JSONArray peoples = null;

    ArrayList<HashMap<String, String>> personList;

    ListView list;
    EditText insertid;
    EditText insertpw;
    Button insertbtn;
    Button searchbtn;
    TextView resultview;
    EditText searchtxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        list = (ListView) findViewById(R.id.listView);
        personList = new ArrayList<HashMap<String, String>>();
        GetDataJSON g = new GetDataJSON();
        g.execute("http:/192.168.219.104:8181/php_connection.php");
        insertid = (EditText) findViewById(R.id.insertid);
        insertpw = (EditText) findViewById(R.id.insertpw);
        insertbtn = (Button) findViewById(R.id.insertbtn);
        insertbtn.setOnClickListener(this);
        searchbtn = (Button) findViewById(R.id.searchbtn);
        resultview = (TextView) findViewById(R.id.result);
        searchtxt = (EditText) findViewById(R.id.searchtxt);
        searchbtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View arg){
        if(arg == insertbtn){
            String username = insertid.getText().toString();
            String pw = insertpw.getText().toString();
            insertData i = new insertData();
            i.execute("http:/192.168.219.104:8181/insert.php",username,pw);
            insertid.setText("");
            insertpw.setText("");
        }
        else if(arg == searchbtn){
            String username = searchtxt.getText().toString();
            searchData s = new searchData();
            s.execute("http:/192.168.219.104:8181/select.php",username);
        }
    }

    protected void showList() {
        try {
            JSONObject jsonObj = new JSONObject(myJSON);
            peoples = jsonObj.getJSONArray(TAG_RESULTS);

            for (int i = 0; i < peoples.length(); i++) {
                JSONObject c = peoples.getJSONObject(i);
                String id = c.getString(TAG_ID);
                String name = c.getString(TAG_NAME);
                HashMap<String, String> persons = new HashMap<String, String>();
                persons.put(TAG_ID, id);
                persons.put(TAG_NAME, name);
                personList.add(persons);
            }

            ListAdapter adapter = new SimpleAdapter(
                    MainActivity.this, personList, R.layout.list_item,
                    new String[]{TAG_ID, TAG_NAME},
                    new int[]{R.id.id, R.id.name}
            );


            list.setAdapter(adapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    class GetDataJSON extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String uri = params[0];
            BufferedReader bufferedReader = null;
            try {
                URL url = new URL(uri);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                StringBuilder sb = new StringBuilder();

                bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                String json;
                while ((json = bufferedReader.readLine()) != null) {
                    sb.append(json + "\n");
                }
                bufferedReader.close();
                return sb.toString().trim();

            } catch (Exception e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            myJSON = result;
            showList();
        }
    }


    class searchData extends AsyncTask<String, Void, String>{
        @Override
        protected String doInBackground(String... params) {
            String username = (String) params[1];

            String uri = params[0];
            String postparam = "username=" + username;
            BufferedReader bufferedReader = null;

            try {
                URL url = new URL(uri);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setReadTimeout(5000);
                con.setConnectTimeout(5000);
                con.setRequestMethod("POST");
                con.setDoInput(true);
                con.connect();


                OutputStream outputStream = con.getOutputStream();
                outputStream.write(postparam.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();


                int responseStatusCode = con.getResponseCode();
                Log.i(TAG, "response code - " + responseStatusCode);

                InputStream inputStream;
                if (responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = con.getInputStream();
                } else {
                    inputStream = con.getErrorStream();
                }

                StringBuilder sb = new StringBuilder();

                bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));

                String json;
                while ((json = bufferedReader.readLine()) != null) {
                    sb.append(json + "\n");
                }
                bufferedReader.close();
                return sb.toString().trim();
            } catch (Exception e) {
                return null;
            }
        }
        @Override
        protected void onPostExecute(String result){ //아직 미작동
            try{// 있는지 확인 용도
                JSONObject jsonObj = new JSONObject(myJSON);
                peoples = jsonObj.getJSONArray(TAG_RESULTS);
//                if(peoples.length() == 0){
//                    resultview.setText("no");
//                }
//                else{
//                    resultview.setText("yes");
//                }
            }catch (Exception e){

            }
        }
    }


    class insertData extends AsyncTask<String, Void, String>{
        @Override
        protected String doInBackground(String... params) {
            String username = (String) params[1];
            String pw = (String) params[2];

            String uri = params[0];
            String postparam = "username=" + username + "&pw=" + pw;
            BufferedReader bufferedReader = null;

            try {
                URL url = new URL(uri);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setReadTimeout(5000);
                con.setConnectTimeout(5000);
                con.setRequestMethod("POST");
                con.setDoInput(true);
                con.connect();


                OutputStream outputStream = con.getOutputStream();
                outputStream.write(postparam.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();


                int responseStatusCode = con.getResponseCode();
                Log.i(TAG, "response code - " + responseStatusCode);

                InputStream inputStream;
                if (responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = con.getInputStream();
                } else {
                    inputStream = con.getErrorStream();
                }

                StringBuilder sb = new StringBuilder();

                bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));

                String json;
                while ((json = bufferedReader.readLine()) != null) {
                    sb.append(json + "\n");
                }
                bufferedReader.close();
                return sb.toString().trim();
            } catch (Exception e) {
                return null;
            }
        }
        @Override
        protected void onPostExecute(String result){
            Log.d(TAG, "post");
        }
    }
    }