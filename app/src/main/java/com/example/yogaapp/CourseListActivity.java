package com.example.yogaapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.yogaapp.adapters.CourseAdapter;
import com.example.yogaapp.api.ApiService;
import com.example.yogaapp.api.RetrofitClient;
import com.example.yogaapp.models.YogaCourse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CourseListActivity extends AppCompatActivity {

    private RecyclerView recyclerViewCourses;
    private CourseAdapter yogaCourseAdapter;
    private List<YogaCourse> courseList;
    private Spinner spinnerDays;
    private Button btnBack, btnAddCourse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_list);

        recyclerViewCourses = findViewById(R.id.recyclerViewCourses);
        recyclerViewCourses.setLayoutManager(new LinearLayoutManager(this));

        courseList = new ArrayList<>();
        yogaCourseAdapter = new CourseAdapter(courseList, this);
        recyclerViewCourses.setAdapter(yogaCourseAdapter);

        btnBack = findViewById(R.id.btnBack);
        btnAddCourse = findViewById(R.id.btnAddCourse);

        spinnerDays = findViewById(R.id.spinnerDays);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.days_of_week_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDays.setAdapter(adapter);

        spinnerDays.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedDay = parent.getItemAtPosition(position).toString();
                if (selectedDay.equals("All")) {
                    getAllYogaCourses();
                } else {
                    searchCourses(selectedDay);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnBack.setOnClickListener(view -> {
            Intent intent = new Intent(CourseListActivity.this, DashboardActivity.class);
            startActivity(intent);
        });

        btnAddCourse.setOnClickListener(view -> {
            Intent intent = new Intent(CourseListActivity.this, CourseFormActivity.class);
            startActivity(intent);
        });

        getAllYogaCourses();
    }

    private void getAllYogaCourses() {
        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
        Call<List<YogaCourse>> call = apiService.getAllYogaCourses();

        call.enqueue(new Callback<List<YogaCourse>>() {
            @Override
            public void onResponse(Call<List<YogaCourse>> call, Response<List<YogaCourse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    courseList.clear();
                    courseList.addAll(response.body());
                    yogaCourseAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(CourseListActivity.this, "No courses found.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<YogaCourse>> call, Throwable t) {
                Toast.makeText(CourseListActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void searchCourses(String dayOfWeek) {
        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
        Call<List<YogaCourse>> call = apiService.searchYogaCourses(dayOfWeek);

        call.enqueue(new Callback<List<YogaCourse>>() {
            @Override
            public void onResponse(Call<List<YogaCourse>> call, Response<List<YogaCourse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    courseList.clear();
                    courseList.addAll(response.body());
                    yogaCourseAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(CourseListActivity.this, "No courses found for this day.", Toast.LENGTH_SHORT).show();                }
            }

            @Override
            public void onFailure(Call<List<YogaCourse>> call, Throwable t) {
                Toast.makeText(CourseListActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getAllYogaCourses();
    }
}
