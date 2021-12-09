package com.inhatc.beacon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    EditText ed_name;
    EditText ed_phone;
    EditText ed_address;
    RadioButton rd_noVaccin;
    RadioButton rd_yesVaccin;
    Button btn_commit;
    boolean chklogin = false;
    String logphone = null;
    String myJSON;
    String TAG_RESULTS = "result";
    JSONArray peoples = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ed_name = (EditText) findViewById(R.id.ed_name);
        ed_phone = (EditText) findViewById(R.id.ed_phone);
        ed_address = (EditText) findViewById(R.id.ed_address);
        rd_yesVaccin = (RadioButton) findViewById(R.id.rd_yesVaccin);
        rd_noVaccin = (RadioButton) findViewById(R.id.rd_noVaccin);
        btn_commit = (Button) findViewById(R.id.btn_commit);
        btn_commit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String phone = ed_phone.getText().toString();
                searchData s = new searchData();
                s.execute("http:/192.168.219.100:8181/select.php",phone);
            }
        });
    }

    protected void showList() {
        try {
            JSONObject jsonObj = new JSONObject(myJSON);
            peoples = jsonObj.getJSONArray(TAG_RESULTS);
            Log.d("d",peoples.getClass().getName());
            for (int i = 0; i < peoples.length(); i++) { //결과 있을 경우
                JSONObject c = peoples.getJSONObject(i); //넘어가기
                logphone = c.getString("phone");
                break;
            }
            if(logphone == null){
                String username = ed_name.getText().toString();
                String phone = ed_phone.getText().toString();
                String address = ed_address.getText().toString();
                String vaccin = "0";
                if(rd_yesVaccin.isChecked()){
                    vaccin = "1";
                }
                insertData i = new insertData();
                i.execute("http:/192.168.219.100:8181/insert.php",username,phone,address,vaccin);
            }
            Intent Beaconintent = new Intent(
                    MainActivity.this, BeaconActivity.class);
            Beaconintent.putExtra("Phone", logphone);
            startActivityForResult(Beaconintent, 1);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    class searchData extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String phone = (String) params[1];

            String uri = params[0];
            String postparam = "phone=" + phone;
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
                Log.d("d",sb.toString().trim());
                return sb.toString().trim();
            } catch (Exception e) {
                return null;
            }
        }
        @Override
        protected void onPostExecute(String result){
            myJSON = result;
            showList();
        }
    }

    class insertData extends AsyncTask<String, Void, String>{
        @Override
        protected String doInBackground(String... params) {
            String username = (String) params[1];
            String phone = (String) params[2];
            String address = (String) params[3];
            String vaccin = (String) params[4];
            String uri = params[0];
            String postparam = "username=" + username + "&phone=" + phone + "&address=" + address + "&vaccin=" + vaccin;
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

        }
    }

}