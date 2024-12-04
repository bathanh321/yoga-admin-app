package com.example.yogaapp;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.yogaapp.api.ApiService;
import com.example.yogaapp.api.RetrofitClient;
import com.example.yogaapp.models.ClassModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClassFormActivity extends AppCompatActivity {

    private EditText etClassDate, etTeacherName, etDescription, etDuration, etCapacity;
    private Button btnSubmit, btnBack;
    private String selectedDate;
    private String courseId, dayOfWeek, id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_form);

        try {
            etClassDate = findViewById(R.id.etDate);
            etTeacherName = findViewById(R.id.etTeacherName);
            etDescription = findViewById(R.id.etDescription);
            etDuration = findViewById(R.id.etDuration);
            etCapacity = findViewById(R.id.etCapacity);
            btnSubmit = findViewById(R.id.btnSubmit);
            btnBack = findViewById(R.id.btnBack);

            Intent intent = getIntent();
            id = intent.getStringExtra("id");
            courseId = intent.getStringExtra("courseId");
            dayOfWeek = intent.getStringExtra("dayOfWeek");

            if (id != null) {
                loadClassData(intent);
            }

            etClassDate.setOnClickListener(v -> showDatePickerDialog());

            btnSubmit.setOnClickListener(v -> {
                if (courseId != null && dayOfWeek != null) {
                    submitClassData();
                } else {
                    Toast.makeText(ClassFormActivity.this, "Course details not loaded yet. Please wait.", Toast.LENGTH_SHORT).show();
                }
            });

            btnBack.setOnClickListener(v -> {
                Intent backIntent = new Intent(ClassFormActivity.this, ClassListInCourseActivity.class);
                backIntent.putExtra("courseId", courseId);
                backIntent.putExtra("dayOfWeek", dayOfWeek);
                startActivity(backIntent);
            });
        } catch (Exception e) {
            Log.e("ClassFormActivity", "Error in onCreate: ", e);
        }
    }

    private void showDatePickerDialog() {
        try {
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    this,
                    (view, selectedYear, selectedMonth, selectedDay) -> {
                        selectedDate = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                        etClassDate.setText(selectedDate);
                    },
                    year, month, day
            );

            datePickerDialog.show();
        } catch (Exception e) {
            Log.e("ClassFormActivity", "Error in showDatePickerDialog: ", e);
        }
    }

    private boolean isDateValid(String selectedDate, String dayOfWeek) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        try {
            Date date = dateFormat.parse(selectedDate);

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            int dayOfWeekSelected = calendar.get(Calendar.DAY_OF_WEEK);

            int validDayOfWeek = mapDayOfWeekToCalendar(dayOfWeek);

            return dayOfWeekSelected == validDayOfWeek;
        } catch (ParseException e) {
            Log.e("ClassFormActivity", "Error in isDateValid: ", e);
            return false;
        }
    }

    private void loadClassData(Intent intent) {
        try {
            String teacherName = intent.getStringExtra("teacherName");
            String description = intent.getStringExtra("description");
            String date = intent.getStringExtra("date");
            int duration = intent.getIntExtra("duration", 0);
            int capacity = intent.getIntExtra("capacity", 0);
            List<String> participants = intent.getStringArrayListExtra("participants");

            if (teacherName != null) {
                etTeacherName.setText(teacherName);
            }

            if (description != null) {
                etDescription.setText(description);
            }

            if (date != null) {
                etClassDate.setText(date);
            } else {
                Toast.makeText(this, "Invalid or missing date", Toast.LENGTH_SHORT).show();
            }

            if (duration > 0) {
                etDuration.setText(String.valueOf(duration));
            } else {
                Toast.makeText(this, "Invalid or missing duration", Toast.LENGTH_SHORT).show();
            }

            if (capacity > 0) {
                etCapacity.setText(String.valueOf(capacity));
            } else {
                Toast.makeText(this, "Invalid or missing capacity", Toast.LENGTH_SHORT).show();
            }

            if (participants == null) {
                participants = new ArrayList<>();
            }

            String classId = intent.getStringExtra("id");
            if (classId != null) {
                getClassDetail(classId);
            }
        } catch (Exception e) {
            Log.e("ClassFormActivity", "Error in loadClassData: ", e);
        }
    }

    private void getClassDetail(String classId) {
        try {
            ApiService apiService = RetrofitClient.getApiService();
            Call<ClassModel> call = apiService.getClassById(classId);

            call.enqueue(new Callback<ClassModel>() {
                @Override
                public void onResponse(Call<ClassModel> call, Response<ClassModel> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        ClassModel classModel = response.body();
                        etTeacherName.setText(classModel.getTeacherName());
                        etDescription.setText(classModel.getDescription());
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                        etClassDate.setText(dateFormat.format(classModel.getDate()));
                        etDuration.setText(String.valueOf(classModel.getDuration()));
                        etCapacity.setText(String.valueOf(classModel.getCapacity()));
                    } else {
                        Log.e("ClassFormActivity", "Error in getClassDetail: " + response.message());
                        Toast.makeText(ClassFormActivity.this, "Failed to load class details", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ClassModel> call, Throwable t) {
                    Log.e("ClassFormActivity", "Error in getClassDetail: ", t);
                    Toast.makeText(ClassFormActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            Log.e("ClassFormActivity", "Error in getClassDetail: ", e);
        }
    }

    private int mapDayOfWeekToCalendar(String dayOfWeek) {
        try {
            switch (dayOfWeek.toLowerCase()) {
                case "sunday":
                    return Calendar.SUNDAY;
                case "monday":
                    return Calendar.MONDAY;
                case "tuesday":
                    return Calendar.TUESDAY;
                case "wednesday":
                    return Calendar.WEDNESDAY;
                case "thursday":
                    return Calendar.THURSDAY;
                case "friday":
                    return Calendar.FRIDAY;
                case "saturday":
                    return Calendar.SATURDAY;
                default:
                    throw new IllegalArgumentException("Invalid day of week: " + dayOfWeek);
            }
        } catch (Exception e) {
            Log.e("ClassFormActivity", "Error in mapDayOfWeekToCalendar: ", e);
            throw e;
        }
    }

    private void submitClassData() {
        try {
            if (selectedDate == null || etTeacherName.getText().toString().isEmpty() ||
                    etDuration.getText().toString().isEmpty() || etCapacity.getText().toString().isEmpty()) {
                Toast.makeText(this, "Please fill in all required fields", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!isDateValid(selectedDate, dayOfWeek)) {
                Toast.makeText(this, "Please select a valid " + dayOfWeek, Toast.LENGTH_SHORT).show();
                return;
            }

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            Date date;
            try {
                date = dateFormat.parse(selectedDate);
            } catch (ParseException e) {
                Log.e("ClassFormActivity", "Error in submitClassData: ", e);
                Toast.makeText(this, "Invalid date format", Toast.LENGTH_SHORT).show();
                return;
            }

            ClassModel classModel = new ClassModel();
            classModel.setTeacherName(etTeacherName.getText().toString());
            classModel.setDescription(etDescription.getText().toString());
            classModel.setDate(date);
            classModel.setDuration(Integer.parseInt(etDuration.getText().toString()));
            classModel.setCapacity(Integer.parseInt(etCapacity.getText().toString()));
            classModel.setYogaCourseId(courseId);

            ApiService apiService = RetrofitClient.getApiService();

            if (id == null) {
                Call<ClassModel> call = apiService.createClassInCourse(courseId, classModel);
                call.enqueue(new Callback<ClassModel>() {
                    @Override
                    public void onResponse(Call<ClassModel> call, Response<ClassModel> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(ClassFormActivity.this, "Class created successfully!", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Log.e("ClassFormActivity", "Error in createClassInCourse: " + response.message());
                            Toast.makeText(ClassFormActivity.this, "Failed to create class: " + response.message(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ClassModel> call, Throwable t) {
                        Log.e("ClassFormActivity", "Error in createClassInCourse: ", t);
                        Toast.makeText(ClassFormActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Call<ClassModel> call = apiService.updateClass(id, classModel);
                call.enqueue(new Callback<ClassModel>() {
                    @Override
                    public void onResponse(Call<ClassModel> call, Response<ClassModel> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(ClassFormActivity.this, "Class updated successfully!", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Log.e("ClassFormActivity", "Error in updateClass: " + response.message());
                            Toast.makeText(ClassFormActivity.this, "Failed to update class: " + response.message(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ClassModel> call, Throwable t) {
                        Log.e("ClassFormActivity", "Error in updateClass: ", t);
                        Toast.makeText(ClassFormActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        } catch (Exception e) {
            Log.e("ClassFormActivity", "Error in submitClassData: ", e);
        }
    }
}