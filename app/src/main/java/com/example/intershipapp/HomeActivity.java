package com.example.intershipapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.AsyncTaskLoader;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.icu.util.ULocale;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class HomeActivity extends AppCompatActivity {

    TextView title;
    SharedPreferences sp;

    RecyclerView recyclerView,productRecyclerView;

    String[] categoryNameArray = {"Vegetables","Fruits","Apple","Banana"};
    int[] categoryImageArray = {R.drawable.vegetables,R.drawable.fruits,R.drawable.apple,R.drawable.banana2};

    ArrayList<CategoryList> arrayList;

    String[] productNameArray = {"Apple","Banana","Blue Berry","Grape","Strawberry"};
    int[] productImageArray = {R.drawable.apple1,R.drawable.banana1,R.drawable.blueberries1,R.drawable.grape1,R.drawable.strawberry1};
    String[] productPriceArray = {"200","100","300","100","40"};
    String[] productUnitArray = {"1 KG","12 Items","500 GM","500 GM","1 BOX"};
    String[] productDescriptionArray ={
            "An Apple, (Malus domestica), fruit of the domesticated tree Malus domestica (family Rosaceae), one of the most widely cultivated tree fruits. Apples are an incredibly nutritious fruit that offers multiple health benefits. They're rich in fiber and antioxidants."
            ,"A Banana is an elongated, edible fruit – botanically a berry – produced by several kinds of large herbaceous flowering plants in the genus Musa. In some countries, bananas used for cooking may be called plantains, distinguishing them from dessert bananas."
            ,"The Blueberry is a fruit that grows wild in fresh areas of the N hemisphere. It is a bluish-black, rounded berry measuring 6mm of diameter. It is mainly consumed in jams, cakes or as garnish for various dishes. It is rich in vitamins and it supplies very few calories."
            ,"Grapes come in different colors and forms. There are red, green, and purple grapes, seedless grapes, grape jelly, grape jam and grape juice, raisins, currents, and sultanas, not to mention wine."
            ,"a small juicy red fruit that has small brown seeds on its surface, or the plant with white flowers on which this fruit grows: fresh strawberries. I thought we'd have strawberries and cream for dessert. strawberry jam/ice cream/tart."
    };

    ArrayList<ProductList> productArrayList;

    TextView viewAll;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        getSupportActionBar().setTitle("Dashboard");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sp = getSharedPreferences(ConstantSp.PREF,MODE_PRIVATE);

        title = findViewById(R.id.home_title);

        title.setText("Welcome to \n "+sp.getString(ConstantSp.EMAIL,""));

        viewAll = findViewById(R.id.home_view_all);

        viewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sp.edit().putString(ConstantSp.CATEGORY_ID,"").commit();
                new CommonMethod(HomeActivity.this,ProductActivity.class);
            }
        });

        recyclerView = findViewById(R.id.home_recycleview);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView .setNestedScrollingEnabled(false);

       /* arrayList = new ArrayList<>();
        for (int i = 0; i < categoryNameArray.length; i++) {
            CategoryList list = new CategoryList();
            list.setName(categoryNameArray[i]);
            list.setImage(categoryImageArray[i]);
            arrayList.add(list);
        }
        CategoryAdapter categoryAdapter = new CategoryAdapter(HomeActivity.this, arrayList);
        recyclerView.setAdapter(categoryAdapter);*/

        productRecyclerView = findViewById(R.id.home_product_recycleview);
        productRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.HORIZONTAL));
        productRecyclerView.setItemAnimator(new DefaultItemAnimator());
        productRecyclerView.setNestedScrollingEnabled(false);

//        productArrayList = new ArrayList<>();
//        for(int i=0;i<productNameArray.length;i++){
//            ProductList list = new ProductList();
//            list.setName(productNameArray[i]);
//            list.setPrice(productPriceArray[i]);
//            list.setUnit(productUnitArray[i]);
//            list.setDescription(productDescriptionArray[i]);
//            list.setImage(productImageArray[i]);
//            productArrayList.add(list);
//        }
//
//        ProductAdapter productAdapter = new ProductAdapter(HomeActivity.this,productArrayList);
//        productRecyclerView.setAdapter(productAdapter);

        if(new ConnectionDetector(HomeActivity.this).isConnectingToInternet()){
            new getCategoryData().execute();
            new getProductData().execute();
        }
        else{
            new ConnectionDetector(HomeActivity.this).connectiondetect();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        if(id==android.R.id.home){
            onBackPressed();
        }
        if(id==R.id.home_menu_logout){
            sp.edit().clear().commit();
            new CommonMethod(HomeActivity.this,MainActivity.class);
        }
        if(id==R.id.home_menu_chat){
            new CommonMethod(HomeActivity.this,ChatActivity.class);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
//          super.onBackPressed();
//        finish();
//        System.exit(0);
//       finishAffinity();
        alertMethod();
    }

    // Alert dialog box
    private void alertMethod() {

        AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setTitle("Exit Alert !");
        builder.setMessage("Are you sure want to Exit !");


        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finishAffinity();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        builder.setNeutralButton("Rate Us", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                new CommonMethod(HomeActivity.this,"Rate Us");
            }
        });

        builder.setCancelable(false);
        builder.show();

    }

    private class getProductData extends AsyncTask<String,String,String> {

        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(HomeActivity.this);
            pd.setMessage("Please wait...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            return new MakeServiceCall().MakeServiceCall(ConstantUrl.PRODUCT_ALL_URL,MakeServiceCall.GET,new HashMap<>());
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pd.dismiss();
            try {
                JSONObject object = new JSONObject(s);
                if(object.getBoolean("Status")==true){
                    JSONArray jsonArray = object.getJSONArray("ProductData");
                    productArrayList = new ArrayList<>();
                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        ProductList list = new ProductList();
                        list.setName(jsonObject.getString("name"));
                        list.setPrice(jsonObject.getString("price"));
                        list.setUnit(jsonObject.getString("unit"));
                        list.setDescription(jsonObject.getString("description"));
                        list.setImage(jsonObject.getString("image"));
                        productArrayList.add(list);
                    }

                    ProductAdapter productAdapter = new ProductAdapter(HomeActivity.this,productArrayList);
                    productRecyclerView.setAdapter(productAdapter);
                }
                else{
                    new CommonMethod(HomeActivity.this,object.getString("Message"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
                new CommonMethod(HomeActivity.this,e.getMessage());
            }
        }
    }


    private class getCategoryData extends AsyncTask<String,String,String> {

        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(HomeActivity.this);
            pd.setMessage("Please wait...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            return new MakeServiceCall().MakeServiceCall(ConstantUrl.CATEGORY_URL,MakeServiceCall.GET,new HashMap<>());
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pd.dismiss();
            try {
                JSONObject object = new JSONObject(s);
                if(object.getBoolean("Status")==true){
                    JSONArray jsonArray = object.getJSONArray("CategoryData");
                    arrayList = new ArrayList<>();
                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        CategoryList list = new CategoryList();
                        list.setId(jsonObject.getString("id"));
                        list.setName(jsonObject.getString("name"));
                        list.setImage(jsonObject.getString("image"));
                        arrayList.add(list);
                    }

                    CategoryAdapter adapter = new CategoryAdapter(HomeActivity.this,arrayList);
                    recyclerView.setAdapter(adapter);
                }
                else{
                    new CommonMethod(HomeActivity.this,object.getString("Message"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
                new CommonMethod(HomeActivity.this,e.getMessage());
            }
        }
    }




}