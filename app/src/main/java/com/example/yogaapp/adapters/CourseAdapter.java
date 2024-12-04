package com.example.yogaapp.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yogaapp.ClassListInCourseActivity;
import com.example.yogaapp.CourseFormActivity;
import com.example.yogaapp.R;
import com.example.yogaapp.api.ApiService;
import com.example.yogaapp.api.RetrofitClient;
import com.example.yogaapp.models.YogaCourse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CourseViewHolder> {

    private List<YogaCourse> courses;
    private Context context;

    public CourseAdapter(List<YogaCourse> courses, Context context) {
        this.courses = courses;
        this.context = context;
    }

    @NonNull
    @Override
    public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_course, parent, false);
        return new CourseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseViewHolder holder, int position) {
        YogaCourse course = courses.get(position);
        holder.tvDayOfWeek.setText("Day: " + course.getDayOfWeek());
        holder.tvCourseTime.setText("Time: " + course.getCourseTime());
        holder.tvPricePerClass.setText("Price per Class: $" + course.getPricePerClass());
        holder.tvClassType.setText("Class Type: " + course.getClassType());
        holder.tvLocation.setText("Location: " + course.getLocation());

        holder.btnEdit.setOnClickListener(v -> {
            Intent intent = new Intent(context, CourseFormActivity.class);
            intent.putExtra("id", course.getId());
            intent.putExtra("dayOfWeek", course.getDayOfWeek());
            intent.putExtra("courseTime", course.getCourseTime());
            intent.putExtra("pricePerClass", course.getPricePerClass());
            intent.putExtra("classType", course.getClassType());
            intent.putExtra("location", course.getLocation());
            context.startActivity(intent);
        });

        holder.btnDelete.setOnClickListener(v -> {
            showDeleteConfirmationDialog(course.getId(), position);
        });

        holder.btnViewClass.setOnClickListener(v -> {
            Intent intent = new Intent(context, ClassListInCourseActivity.class);
            intent.putExtra("courseId", course.getId());
            intent.putExtra("dayOfWeek", course.getDayOfWeek());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return courses.size();
    }

    public static class CourseViewHolder extends RecyclerView.ViewHolder {
        TextView tvDayOfWeek, tvCourseTime, tvPricePerClass, tvClassType, tvLocation;
        Button btnEdit, btnDelete, btnViewClass;

        public CourseViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDayOfWeek = itemView.findViewById(R.id.tvDayOfWeek);
            tvCourseTime = itemView.findViewById(R.id.tvCourseTime);
            tvPricePerClass = itemView.findViewById(R.id.tvPrice);
            tvClassType = itemView.findViewById(R.id.tvClassType);
            tvLocation = itemView.findViewById(R.id.tvLocation);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            btnViewClass = itemView.findViewById(R.id.btnViewClass);
        }
    }

    private void showDeleteConfirmationDialog(String courseId, int position) {
        new AlertDialog.Builder(context)
                .setTitle("Confirm Delete")
                .setMessage("Are you sure you want to delete this course?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    deleteYogaCourse(courseId, position);
                })
                .setNegativeButton("Cancel", (dialog, which) -> {
                    dialog.dismiss();
                })
                .create()
                .show();
    }

    private void deleteYogaCourse(String courseId, int position) {
        ApiService apiService = RetrofitClient.getApiService();
        Call<Void> call = apiService.deleteYogaCourse(courseId);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(context, "Course deleted successfully!", Toast.LENGTH_SHORT).show();
                    courses.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, courses.size());
                } else {
                    Toast.makeText(context, "Failed to delete course: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(context, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
