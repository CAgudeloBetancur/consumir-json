package com.iud.api_fetching;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.iud.api_fetching.adapter.ProductAdapter;
import com.iud.api_fetching.adapter.ReviewAdapter;
import com.iud.api_fetching.model.Category;
import com.iud.api_fetching.model.Product;
import com.iud.api_fetching.model.Review;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Array;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class DetailActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private Context context;
    private RequestQueue requestQueue;
    private StringRequest stringRequest;
    private String product_id;
    private List<Review> reviewList = new ArrayList<>();

    private TextView description, name, price, availabilityStatus, warranty;
    private ImageView img;
    private FloatingActionButton actionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        init();
        requestJsonData();
    }

    public void init() {

        context = DetailActivity.this;

        product_id = (String) getIntent().getSerializableExtra("product_id");

        name = findViewById(R.id.name);
        description = findViewById(R.id.description);
        price = findViewById(R.id.price);
        availabilityStatus = findViewById(R.id.availabiltyStatus);
        warranty = findViewById(R.id.warranty);
        img = findViewById(R.id.img);
        actionButton = findViewById(R.id.homeBtn);

        recyclerView = findViewById(R.id.review_rv);

        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    public void requestJsonData() {
        requestQueue = Volley.newRequestQueue(context);
        stringRequest = new StringRequest(
                Request.Method.GET,
                "https://dummyjson.com/products/" + product_id,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response.toString());
                            // JSONArray jsonArray = jsonObject.getJSONArray("products");
                            fetchData(jsonObject);
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

    public void fetchData(JSONObject jsonObject) {
        try {
            JSONArray reviews = jsonObject.getJSONArray("reviews");
            name.setText(jsonObject.getString("title"));
            description.setText(jsonObject.getString("description"));
            price.setText("$ " + jsonObject.get("price").toString());
            availabilityStatus.setText(jsonObject.getString("availabilityStatus"));
            warranty.setText(jsonObject.getString("warrantyInformation"));
            Picasso.get().load(jsonObject.getString("thumbnail")).into(img);

            // Formato de entrada de date
            SimpleDateFormat inputFormat = new SimpleDateFormat(
                "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US
            );
            // Formato de salida de date
            SimpleDateFormat outputFormat = new SimpleDateFormat(
                "dd/MM/yyyy", Locale.US
            );

            for(int i = 0; i < reviews.length(); i++) {
                JSONObject review = reviews.getJSONObject(i);
                // Cambiamos date de formato iso a uno más amigable
                Date date = inputFormat.parse(review.getString("date"));
                String formattedDate = outputFormat.format(date);
                reviewList.add(
                        new Review(
                                review.get("rating").toString(),
                                review.getString("comment"),
                                formattedDate,
                                review.getString("reviewerName")
                        )
                );
            }

        } catch(Exception ex) {
            ex.printStackTrace();
            showToast("product details error");
        }

        ReviewAdapter adapter = new ReviewAdapter(reviewList, context);
        recyclerView.setLayoutManager(
                new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false) {
                    @Override
                    public boolean canScrollVertically() {
                        return false;
                    }
                }
        );
        recyclerView.setAdapter(adapter);
    }
}