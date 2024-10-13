package com.iud.api_fetching;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.iud.api_fetching.adapter.CategoryAdapter;
import com.iud.api_fetching.model.Category;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private Context context;
    private RequestQueue requestQueue;
    private List<Category> categoryList = new ArrayList<>();
    private StringRequest stringRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        init();
        requestJsonData();
    }

    public void init() {
        recyclerView = findViewById(R.id.category_rv);
        context = MainActivity.this;
    }

    public void requestJsonData() {
        requestQueue = Volley.newRequestQueue(context);
        stringRequest = new StringRequest(
            Request.Method.GET,
            "https://dummyjson.com/products/categories",
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        fetchData(jsonArray);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    showToast("API call error");
                }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return super.getHeaders();
            }
        };
        requestQueue.add(stringRequest);
    }

    public void showToast(String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public void fetchData(JSONArray jsonArray) {
        for(int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject category = jsonArray.getJSONObject(i);
                categoryList.add(
                    new Category(
                        category.getString("name"),
                        category.getString("url")
                    )
                );
            } catch (Exception ex) {
                ex.printStackTrace();
                showToast("Mobile details error");
            }
        }
        CategoryAdapter adapter = new CategoryAdapter(categoryList, context);
        recyclerView.setLayoutManager(
            new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        );
        recyclerView.setAdapter(adapter);

        adapter.setOnClickListener(new CategoryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Category category) {
                Intent intent = new Intent(MainActivity.this, ProductActivity.class);
                intent.putExtra("category", category);
                startActivity(intent);
            }
        });
    }
}