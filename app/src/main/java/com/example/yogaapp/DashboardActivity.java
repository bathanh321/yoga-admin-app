package com.example.yogaapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.yogaapp.api.RetrofitClient;
import com.example.yogaapp.models.OrderModel;
import com.google.android.material.card.MaterialCardView;
import com.example.yogaapp.api.ApiService;
import com.example.yogaapp.models.YogaCourse;
import com.example.yogaapp.models.UserModel;
import com.example.yogaapp.models.ClassModel;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardActivity extends AppCompatActivity {

    private TextView tvTotalCourses, tvTotalClasses, tvTotalUsers, tvTotalOrders;
    private MaterialCardView cardCourses, cardClasses, cardUsers, cardOrders;
    private ApiService apiService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        tvTotalCourses = findViewById(R.id.tvTotalCourses);
        tvTotalClasses = findViewById(R.id.tvTotalClasses);
        tvTotalUsers = findViewById(R.id.tvTotalUsers);
        tvTotalOrders = findViewById(R.id.tvTotalOrders);
        cardCourses = findViewById(R.id.card_courses);
        cardClasses = findViewById(R.id.card_classes);
        cardUsers = findViewById(R.id.card_users);
        cardOrders = findViewById(R.id.card_orders);

        apiService = RetrofitClient.getApiService();

        fetchDashboardData();

        cardCourses.setOnClickListener(view -> {
            Intent intent = new Intent(DashboardActivity.this, CourseListActivity.class);
            startActivity(intent);
        });

        cardClasses.setOnClickListener(view -> {
            Intent intent = new Intent(DashboardActivity.this, ClassListActivity.class);
            startActivity(intent);
        });

        cardUsers.setOnClickListener(view -> {
            Intent intent = new Intent(DashboardActivity.this, UserListActivity.class);
            startActivity(intent);
        });

        cardOrders.setOnClickListener(view -> {
            Intent intent = new Intent(DashboardActivity.this, OrderListActivity.class);
            startActivity(intent);
        });
    }

    private void fetchDashboardData() {
        apiService.getAllYogaCourses().enqueue(new Callback<List<YogaCourse>>() {
            @Override
            public void onResponse(Call<List<YogaCourse>> call, Response<List<YogaCourse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    tvTotalCourses.setText("Total Courses: " + response.body().size());
                }
            }

            @Override
            public void onFailure(Call<List<YogaCourse>> call, Throwable t) {
                Toast.makeText(DashboardActivity.this, "Failed to load courses", Toast.LENGTH_SHORT).show();
            }
        });

        apiService.getAllClasses().enqueue(new Callback<List<ClassModel>>() {
            @Override
            public void onResponse(Call<List<ClassModel>> call, Response<List<ClassModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    tvTotalClasses.setText("Total Classes: " + response.body().size());
                }
            }

            @Override
            public void onFailure(Call<List<ClassModel>> call, Throwable t) {
                Toast.makeText(DashboardActivity.this, "Failed to load classes", Toast.LENGTH_SHORT).show();
            }
        });

        apiService.getAllUsers().enqueue(new Callback<List<UserModel>>() {
            @Override
            public void onResponse(Call<List<UserModel>> call, Response<List<UserModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    tvTotalUsers.setText("Total Users: " + response.body().size());
                }
            }

            @Override
            public void onFailure(Call<List<UserModel>> call, Throwable t) {
                Toast.makeText(DashboardActivity.this, "Failed to load users", Toast.LENGTH_SHORT).show();
            }
        });

        apiService.getAllOrders().enqueue(new Callback<List<OrderModel>>() {
            @Override
            public void onResponse(Call<List<OrderModel>> call, Response<List<OrderModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    tvTotalOrders.setText("Total Orders: " + response.body().size());
                }
            }

            @Override
            public void onFailure(Call<List<OrderModel>> call, Throwable t) {
                Toast.makeText(DashboardActivity.this, "Failed to load orders", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
