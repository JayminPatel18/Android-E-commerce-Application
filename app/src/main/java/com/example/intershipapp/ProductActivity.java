package com.example.intershipapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ProductActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<ProductList> arrayList;

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

    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        getSupportActionBar().setTitle("Product");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sp = getSharedPreferences(ConstantSp.PREF,MODE_PRIVATE);

        recyclerView = findViewById(R.id.product_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(ProductActivity.this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

      /*  arrayList = new ArrayList<>();
        for(int i=0;i<productNameArray.length;i++){
            ProductList list = new ProductList();
            list.setName(productNameArray[i]);
            list.setPrice(productPriceArray[i]);
            list.setUnit(productUnitArray[i]);
            list.setDescription(productDescriptionArray[i]);
            list.setImage(productImageArray[i]);
            arrayList.add(list);
        }

        ProductListAdapter adapter = new ProductListAdapter(ProductActivity.this,arrayList);
        recyclerView.setAdapter(adapter ); */

        if(new ConnectionDetector(ProductActivity.this).isConnectingToInternet()){
            new getProductData().execute();
        }
        else{
            new ConnectionDetector(ProductActivity.this).connectiondetect();
        }

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id==android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    private class getProductData extends AsyncTask<String,String,String> {

        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(ProductActivity.this);
            pd.setMessage("Please wait...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            if(sp.getString(ConstantSp.CATEGORY_ID,"").equalsIgnoreCase("")){
                return new MakeServiceCall().MakeServiceCall(ConstantUrl.PRODUCT_ALL_URL,MakeServiceCall.GET,new HashMap<>());
            }
            else{
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put("categoryId",sp.getString(ConstantSp.CATEGORY_ID,""));
                return new MakeServiceCall().MakeServiceCall(ConstantUrl.PRODUCT_URL, MakeServiceCall.POST, hashMap);
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pd.dismiss();
            try {
                JSONObject object = new JSONObject((s));
                if(object.getBoolean("Status")==true){
                    JSONArray jsonArray = object.getJSONArray("ProductData");
                    arrayList = new ArrayList<>();
                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        ProductList list = new ProductList();
                        list.setName(jsonObject.getString("name"));
                        list.setPrice(jsonObject.getString("price"));
                        list.setUnit(jsonObject.getString("unit"));
                        list.setDescription(jsonObject.getString("description"));
                        list.setImage(jsonObject.getString("image"));
                        arrayList.add(list);
                    }

                    ProductListAdapter productAdapter = new ProductListAdapter(ProductActivity.this,arrayList);
                    recyclerView.setAdapter(productAdapter);
                }
                else{
                    new CommonMethod(ProductActivity.this,object.getString("Message"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
                new CommonMethod(ProductActivity.this,e.getMessage());
            }
        }
    }

}