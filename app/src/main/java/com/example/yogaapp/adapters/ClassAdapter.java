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

import com.example.yogaapp.ClassFormActivity;
import com.example.yogaapp.R;
import com.example.yogaapp.api.ApiService;
import com.example.yogaapp.api.RetrofitClient;
import com.example.yogaapp.models.ClassModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClassAdapter extends RecyclerView.Adapter<ClassAdapter.ClassViewHolder> {

    private List<ClassModel> classes;
    private Context context;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
    private String courseId;
    private String dayOfWeek;

    public ClassAdapter(List<ClassModel> classes, Context context, String courseId, String dayOfWeek) {
        this.classes = classes;
        this.context = context;
        this.courseId = courseId;
        this.dayOfWeek = dayOfWeek;
    }

    public void setCourseDetails(String courseId, String dayOfWeek) {
        this.courseId = courseId;
        this.dayOfWeek = dayOfWeek;
    }

    @NonNull
    @Override
    public ClassViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_class, parent, false);
        return new ClassViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClassViewHolder holder, int position) {
        ClassModel classModel = classes.get(position);
        String formattedDate = classModel.getDate() != null ? dateFormat.format(classModel.getDate()) : "";
        holder.tvTeacherName.setText("Teacher: " + classModel.getTeacherName());
        holder.tvDescription.setText("Description: " + classModel.getDescription());
        holder.tvDate.setText("Date: " + formattedDate);
        holder.tvDuration.setText("Duration: " + classModel.getDuration() + " min");
        holder.tvCapacity.setText("Capacity: " + classModel.getCapacity());

        Log.d("ClassAdapter", "Course ID: " + courseId);
        Log.d("ClassAdapter", "Day of Week: " + dayOfWeek);

        holder.btnEdit.setOnClickListener(v -> {
            Intent intent = new Intent(context, ClassFormActivity.class);
            intent.putExtra("id", classModel.getId());
            intent.putExtra("teacherName", classModel.getTeacherName());
            intent.putExtra("description", classModel.getDescription());
            intent.putExtra("date", formattedDate);
            intent.putExtra("duration", classModel.getDuration());
            intent.putExtra("capacity", classModel.getCapacity());
            intent.putStringArrayListExtra("participants", new ArrayList<>(classModel.getParticipants()));
            intent.putExtra("yogaCourseId", classModel.getYogaCourseId());
            intent.putExtra("courseId", courseId);
            intent.putExtra("dayOfWeek", dayOfWeek);
            context.startActivity(intent);
        });

        holder.btnDelete.setOnClickListener(v -> {
            showDeleteConfirmationDialog(classModel.getId(), position);
        });
    }

    @Override
    public int getItemCount() {
        return classes.size();
    }

    public static class ClassViewHolder extends RecyclerView.ViewHolder {
        TextView tvTeacherName, tvDescription, tvDate, tvDuration, tvCapacity;
        Button btnEdit, btnDelete;

        public ClassViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTeacherName = itemView.findViewById(R.id.tvTeacherName);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvDuration = itemView.findViewById(R.id.tvDuration);
            tvCapacity = itemView.findViewById(R.id.tvCapacity);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }

    private void showDeleteConfirmationDialog(String classId, int position) {
        new AlertDialog.Builder(context)
                .setTitle("Confirm Delete")
                .setMessage("Are you sure you want to delete this class?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    deleteClass(classId, position);
                })
                .setNegativeButton("Cancel", (dialog, which) -> {
                    dialog.dismiss();
                })
                .create()
                .show();
    }

    private void deleteClass(String classId, int position) {
        ApiService apiService = RetrofitClient.getApiService();
        Call<Void> call = apiService.deleteClassById(classId);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()){
                    Toast.makeText(context, "Class deleted!", Toast.LENGTH_SHORT).show();
                    classes.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, classes.size());
                } else {
                    Toast.makeText(context, "Failed to delete class: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(context, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}