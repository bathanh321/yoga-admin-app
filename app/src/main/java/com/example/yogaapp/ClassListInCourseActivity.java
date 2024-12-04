package com.example.yogaapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yogaapp.adapters.ClassAdapter;
import com.example.yogaapp.api.ApiService;
import com.example.yogaapp.api.RetrofitClient;
import com.example.yogaapp.models.ClassModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClassListInCourseActivity extends AppCompatActivity {

    private String courseId, dayOfWeek;
    private RecyclerView recyclerViewClasses;
    private ClassAdapter classAdapter;
    private List<ClassModel> classList;
    private Button btnBack, btnAddClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_list_in_course);

        courseId = getIntent().getStringExtra("courseId");
        dayOfWeek = getIntent().getStringExtra("dayOfWeek");

        recyclerViewClasses = findViewById(R.id.recyclerViewClasses);
        recyclerViewClasses.setLayoutManager(new LinearLayoutManager(this));
        classList = new ArrayList<>();
        classAdapter = new ClassAdapter(classList, this, courseId, dayOfWeek);
        recyclerViewClasses.setAdapter(classAdapter);
        btnBack = findViewById(R.id.btnBack);
        btnAddClass = findViewById(R.id.btnAddClass);

        btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(ClassListInCourseActivity.this, CourseListActivity.class);
            startActivity(intent);
        });

        btnAddClass.setOnClickListener(v -> {
            Intent intent = new Intent(ClassListInCourseActivity.this, ClassFormActivity.class);
            intent.putExtra("courseId", courseId);
            intent.putExtra("dayOfWeek", dayOfWeek);
            startActivity(intent);
        });

        getAllClassesInCourse(courseId);
    }

    private void getAllClassesInCourse(String courseId) {
        ApiService apiService = RetrofitClient.getApiService();
        Call<List<ClassModel>> call = apiService.getAllClassesInCourse(courseId);

        call.enqueue(new Callback<List<ClassModel>>() {
            @Override
            public void onResponse(Call<List<ClassModel>> call, Response<List<ClassModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    classList.clear();
                    classList.addAll(response.body());
                    classAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(ClassListInCourseActivity.this, "No classes found!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<ClassModel>> call, Throwable t) {
                Toast.makeText(ClassListInCourseActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getAllClassesInCourse(courseId);
    }
}