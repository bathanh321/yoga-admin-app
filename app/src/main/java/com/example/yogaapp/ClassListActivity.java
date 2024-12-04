package com.example.yogaapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.yogaapp.adapters.ClassAdapter;

import com.example.yogaapp.adapters.SwipeToActionCallback;
import com.example.yogaapp.api.ApiService;
import com.example.yogaapp.api.RetrofitClient;
import com.example.yogaapp.models.ClassModel;
import com.example.yogaapp.models.YogaCourse;

import androidx.recyclerview.widget.ItemTouchHelper;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClassListActivity extends AppCompatActivity {

    private RecyclerView recyclerViewClasses;
    private ClassAdapter classAdapter;
    private List<ClassModel> classList;
    private EditText etSearch;
    private ImageView ivSearchIcon;
    private Button btnBack;
    private String dayOfWeek;
    private String courseId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_list);

        recyclerViewClasses = findViewById(R.id.recyclerViewClasses);
        etSearch = findViewById(R.id.etSearch);
        ivSearchIcon = findViewById(R.id.ivSearchIcon);
        btnBack = findViewById(R.id.btnBack);

        classList = new ArrayList<>();
        classAdapter = new ClassAdapter(classList, this, courseId, dayOfWeek);

        recyclerViewClasses.setAdapter(classAdapter);
        recyclerViewClasses.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewClasses.setAdapter(classAdapter);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SwipeToActionCallback(this) {
            @Override
            protected void onItemSwipedToEdit(int position) {
                // Handle edit swipe
            }

            @Override
            protected void onItemSwipedToDelete(int position) {
                showDeleteConfirmationDialog(position);
            }
        });
        itemTouchHelper.attachToRecyclerView(recyclerViewClasses);

        btnBack.setOnClickListener(view -> {
            Intent intent = new Intent(ClassListActivity.this, DashboardActivity.class);
            startActivity(intent);
        });

        fetchClasses();

        ivSearchIcon.setOnClickListener(v -> {
            String teacherName = etSearch.getText().toString();
            searchClasses(teacherName);
        });
    }

    private void fetchClasses() {
        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
        Call<List<ClassModel>> call = apiService.getAllClasses();
        call.enqueue(new Callback<List<ClassModel>>() {
            @Override
            public void onResponse(Call<List<ClassModel>> call, Response<List<ClassModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    classList.clear();
                    classList.addAll(response.body());
                    fetchCourseDetailsForClasses();
                } else {
                    Log.e("ClassListActivity", "Response error: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<ClassModel>> call, Throwable t) {
                Log.e("ClassListActivity", "API call failed: " + t.getMessage());
            }
        });
    }

    private void fetchCourseDetailsForClasses() {
        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
        for (ClassModel classModel : classList) {
            String yogaCourseId = classModel.getYogaCourseId();
            Call<YogaCourse> call = apiService.getYogaCourseById(yogaCourseId);
            call.enqueue(new Callback<YogaCourse>() {
                @Override
                public void onResponse(Call<YogaCourse> call, Response<YogaCourse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        YogaCourse yogaCourse = response.body();
                        dayOfWeek = yogaCourse.getDayOfWeek();
                        courseId = yogaCourse.getId();
                        classAdapter.setCourseDetails(courseId, dayOfWeek);
                        classAdapter.notifyDataSetChanged();
                    } else {
                        Log.e("ClassListActivity", "Response error: " + response.message());
                    }
                }

                @Override
                public void onFailure(Call<YogaCourse> call, Throwable t) {
                    Log.e("ClassListActivity", "API call failed: " + t.getMessage());
                }
            });
        }
    }

    private void searchClasses(String teacherName) {
        ApiService apiService = RetrofitClient.getApiService();
        Call<List<ClassModel>> call = apiService.searchClass(teacherName);
        call.enqueue(new Callback<List<ClassModel>>() {
            @Override
            public void onResponse(Call<List<ClassModel>> call, Response<List<ClassModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    classList.clear();
                    classList.addAll(response.body());
                    fetchCourseDetailsForClasses();
                } else {
                    Log.e("ClassListActivity", "Response error: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<ClassModel>> call, Throwable t) {
                Log.e("ClassListActivity", "API call failed: " + t.getMessage());
            }
        });
    }

    private void showDeleteConfirmationDialog(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirmation");
        builder.setMessage("Are you sure? This action cannot be undone");

        builder.setPositiveButton("Yes", (dialog, which) -> {
            ClassModel classToDelete = classList.get(position);
            String classId = classToDelete.getId();
            deleteClassById(classId, position);
        });

        builder.setNegativeButton("No", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void deleteClassById(String classId, int position) {
        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
        Call<Void> call = apiService.deleteClassById(classId);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    classList.remove(position);
                    classAdapter.notifyItemRemoved(position);
                    Toast.makeText(ClassListActivity.this, "Class has been deleted", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ClassListActivity.this, "Failed to delete class", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(ClassListActivity.this, "Something went wrong: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        fetchClasses();
    }
}