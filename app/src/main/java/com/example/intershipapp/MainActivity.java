package com.example.intershipapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    Button login;
    EditText email,password;
    SharedPreferences sp;
    TextView createAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();

        sp = getSharedPreferences(ConstantSp.PREF,MODE_PRIVATE);

        login = findViewById(R.id.main_login);

        email = findViewById(R.id.main_email);
        password = findViewById(R.id.main_password);

        createAccount = findViewById(R.id.main_create_account);

        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(MainActivity.this,SignupActivity.class);
//                startActivity(intent);
                new CommonMethod(MainActivity.this,SignupActivity.class);
            }
        });

        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (email.getText().toString().trim().equals("")) {
                    login.setVisibility(View.GONE);
                } else if (password.getText().toString().trim().equals("")) {
                    login.setVisibility(View.GONE);
                } else {
                    login.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (email.getText().toString().trim().equals("")) {
                    login.setVisibility(View.GONE);
                } else if (password.getText().toString().trim().equals("")) {
                    login.setVisibility(View.GONE);
                } else {
                    login.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (email.getText().toString().trim().equals("")) {
                    email.setError("Email Id Required");
                } else if (password.getText().toString().trim().equals("")) {
                    password.setError("Password Required");
                } else {

                    if(new ConnectionDetector(MainActivity.this).isConnectingToInternet()){
                        new doLogin().execute();
                    }else{
                        new ConnectionDetector(MainActivity.this).connectiondetect();
                    }

                }

                    /*if(email.getText().toString().equals("admin@gmail.com") && password.getText().toString().equalsIgnoreCase("Admin@123")){
                        System.out.println("Login Successfully");
                        Log.d("RESPONDE", "Login Successfully");
                        Log.e("RESPONDE", "Login Successfully");

//                Toast.makeText(MainActivity.this, "Login Successfully", Toast.LENGTH_LONG).show();
//                Snackbar.make(view,"Login Success", Snackbar.LENGTH_SHORT).show();
                        new CommonMethod(MainActivity.this, "Login Successfully");
                        new CommonMethod(view, "Login Success");

                        sp.edit().putString(ConstantSp.EMAIL,email.getText().toString()).commit();
                        sp.edit().putString(ConstantSp.PASSWORD,password.getText().toString()).commit();

//                        Intent intent = new Intent(MainActivity.this,HomeActivity.class);
//                        startActivity(intent);

                        new CommonMethod(MainActivity.this,HomeActivity.class);



                        }else {
                             new CommonMethod(MainActivity.this, "Login Unsuccessfully");
                             new CommonMethod(view, "Login Unsuccess");
                        }

                    }*/
            }
        });


    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }

    private class doLogin extends AsyncTask<String,String,String> {

        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(MainActivity.this);
            pd.setMessage("Please Wait...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String,String> hashMap = new HashMap<>();
            hashMap.put("email",email.getText().toString());
            hashMap.put("password",password.getText().toString());
            return new MakeServiceCall().MakeServiceCall(ConstantUrl.LOGIN_URL,MakeServiceCall.POST,hashMap);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pd.dismiss();
            try {
                JSONObject object = new JSONObject(s);
                if(object.getBoolean("Status")){
                    new CommonMethod(MainActivity.this,object.getString("Message"));
                    JSONArray array = object.getJSONArray("UserData");
                    for(int i=0;i<array.length();i++){
                        JSONObject jsonObject = array.getJSONObject(i);
                        sp.edit().putString(ConstantSp.ID,jsonObject.getString("id")).commit();
                        sp.edit().putString(ConstantSp.NAME,jsonObject.getString("name")).commit();
                        sp.edit().putString(ConstantSp.EMAIL,jsonObject.getString("email")).commit();
                        sp.edit().putString(ConstantSp.CONTACT,jsonObject.getString("contact")).commit();
                        sp.edit().putString(ConstantSp.PASSWORD,jsonObject.getString("password")).commit();
                        sp.edit().putString(ConstantSp.GENDER,jsonObject.getString("gender")).commit();
                        sp.edit().putString(ConstantSp.CITY,jsonObject.getString("city")).commit();
                    }
                    new CommonMethod(MainActivity.this,HomeActivity.class);
                }
                else {
                    new CommonMethod(MainActivity.this,object.getString("Message"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
                new CommonMethod(MainActivity.this,e.getMessage());
            }
        }
    }
}