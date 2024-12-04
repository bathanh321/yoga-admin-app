package com.example.yogaapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.yogaapp.api.ApiService;
import com.example.yogaapp.api.RetrofitClient;
import com.example.yogaapp.models.YogaCourse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CourseFormActivity extends AppCompatActivity {
    private Spinner dayOfWeekSpinner;
    private TimePicker timePickerStart, timePickerEnd;
    private EditText etPrice, etLocation;
    private RadioGroup radioGroupClassType;
    private Button btnSave, btnBack;

    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_form);

        dayOfWeekSpinner = findViewById(R.id.spinnerDays);
        timePickerStart = findViewById(R.id.timePickerStart);
        timePickerEnd = findViewById(R.id.timePickerEnd);
        etPrice = findViewById(R.id.etPrice);
        radioGroupClassType = findViewById(R.id.radioGroupClassType);
        etLocation = findViewById(R.id.etLocation);
        btnSave = findViewById(R.id.btnSave);
        btnBack = findViewById(R.id.btnBack);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.days_of_week_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dayOfWeekSpinner.setAdapter(adapter);

        Intent intent = getIntent();
        if (intent.hasExtra("id")) {
            id = intent.getStringExtra("id");
            loadCourseData(intent);
        }

        btnSave.setOnClickListener(v -> saveCourse());

        btnBack.setOnClickListener(view -> {
            Intent intent1 = new Intent(CourseFormActivity.this, CourseListActivity.class);
            startActivity(intent1);
        });
    }

    private void loadCourseData(Intent intent) {
        String courseTime = intent.getStringExtra("courseTime");
        if (courseTime == null || !courseTime.contains(" - ")) {
            Toast.makeText(this, "Invalid course time format", Toast.LENGTH_SHORT).show();
            return;
        }

        String[] times = courseTime.split(" - ");
        if (times.length < 2) {
            Toast.makeText(this, "Invalid course time format", Toast.LENGTH_SHORT).show();
            return;
        }

        String[] startTime = times[0].split(":");
        String[] endTime = times[1].split(":");

        if (startTime.length < 2 || endTime.length < 2) {
            Toast.makeText(this, "Invalid start or end time format", Toast.LENGTH_SHORT).show();
            return;
        }

        timePickerStart.setHour(Integer.parseInt(startTime[0]));
        timePickerStart.setMinute(Integer.parseInt(startTime[1]));
        timePickerEnd.setHour(Integer.parseInt(endTime[0]));
        timePickerEnd.setMinute(Integer.parseInt(endTime[1]));

        dayOfWeekSpinner.setSelection(((ArrayAdapter) dayOfWeekSpinner.getAdapter())
                .getPosition(intent.getStringExtra("dayOfWeek")));

        etPrice.setText(String.valueOf(intent.getDoubleExtra("pricePerClass", 0.0)));
        etLocation.setText(intent.getStringExtra("location"));

        String classType = intent.getStringExtra("classType");
        for (int i = 0; i < radioGroupClassType.getChildCount(); i++) {
            RadioButton rb = (RadioButton) radioGroupClassType.getChildAt(i);
            if (rb.getText().toString().equals(classType)) {
                rb.setChecked(true);
                Log.d("RadioButton", "Checking RadioButton: " + rb.getText().toString().trim());
                break;
            }
        }
    }


    private void saveCourse() {
        String dayOfWeek = dayOfWeekSpinner.getSelectedItem().toString();
        int startHour = timePickerStart.getHour();
        int startMinute = timePickerStart.getMinute();
        String startTime = String.format("%02d:%02d", startHour, startMinute);

        int endHour = timePickerEnd.getHour();
        int endMinute = timePickerEnd.getMinute();
        String endTime = String.format("%02d:%02d", endHour, endMinute);
        String pricePerClass = etPrice.getText().toString();
        int selectedClassTypeId = radioGroupClassType.getCheckedRadioButtonId();
        RadioButton selectedClassType = findViewById(selectedClassTypeId);
        String classType = selectedClassType.getText().toString();
        String location = etLocation.getText().toString();

        if (dayOfWeek.isEmpty() || startTime.isEmpty() || endTime.isEmpty() || pricePerClass.isEmpty() || classType.isEmpty() || location.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }


        YogaCourse course = new YogaCourse(null, dayOfWeek, startTime + " - " + endTime, Double.parseDouble(pricePerClass), classType, location, new ArrayList<>());
        ApiService apiService = RetrofitClient.getApiService();


        if(id == null){
            retrofit2.Call<YogaCourse> call = apiService.createCourse(course);
            call.enqueue(new Callback<YogaCourse>() {
                @Override
                public void onResponse(Call<YogaCourse> call, Response<YogaCourse> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(CourseFormActivity.this, "Course created successfully!", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(CourseFormActivity.this, "Failed to create course: " + response.message(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<YogaCourse> call, Throwable t) {
                    Toast.makeText(CourseFormActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Call<YogaCourse> call = apiService.updateCourse(id, course);
            call.enqueue(new Callback<YogaCourse>() {
                @Override
                public void onResponse(Call<YogaCourse> call, Response<YogaCourse> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(CourseFormActivity.this, "Course updated successfully!", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(CourseFormActivity.this, "Failed to update course: " + response.message(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<YogaCourse> call, Throwable t) {
                    Toast.makeText(CourseFormActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


}
