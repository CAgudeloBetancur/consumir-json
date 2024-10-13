package com.iud.api_fetching;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
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
import com.iud.api_fetching.adapter.ProductAdapter;
import com.iud.api_fetching.model.Category;
import com.iud.api_fetching.model.Product;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ProductActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private Context context;
    private RequestQueue requestQueue;
    private List<Product> productList = new ArrayList<>();
    private StringRequest stringRequest;
    private String categoryUrl;
    private TextView categoryName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_product);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        init();
        requestJsonData();
    }

    public void init() {
        Category category = (Category) getIntent().getSerializableExtra("category");

        recyclerView = findViewById(R.id.products_rv);
        categoryName = findViewById(R.id.title);

        context = ProductActivity.this;

        categoryUrl = category.getUrl();
        categoryName.setText(category.getName());
    }

    public void requestJsonData() {
        requestQueue = Volley.newRequestQueue(context);
        stringRequest = new StringRequest(
                Request.Method.GET,
                categoryUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response.toString());
                            JSONArray jsonArray = jsonObject.getJSONArray("products");
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
                JSONObject product = jsonArray.getJSONObject(i);
                productList.add(
                        new Product(
                                product.getString("title"),
                                "$" + product.get("price").toString(),
                                product.get("rating").toString(),
                                product.getString("description"),
                                product.getString("thumbnail"),
                                product.get("id").toString()
                        )
                );
            } catch (Exception ex) {
                ex.printStackTrace();
                showToast("product details error");
            }
        }
        ProductAdapter adapter = new ProductAdapter(productList, context);
        recyclerView.setLayoutManager(
                new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        );
        recyclerView.setAdapter(adapter);

        adapter.setOnClickListener(new ProductAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Product product) {
                Intent intent = new Intent(ProductActivity.this, DetailActivity.class);
                intent.putExtra("product_id", product.getId());
                startActivity(intent);
            }
        });
    }
}