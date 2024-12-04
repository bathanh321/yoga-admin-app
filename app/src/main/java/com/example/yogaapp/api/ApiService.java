package com.example.yogaapp.api;

import com.example.yogaapp.models.ClassModel;
import com.example.yogaapp.models.OrderModel;
import com.example.yogaapp.models.UserModel;
import com.example.yogaapp.models.YogaCourse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    // --- Auth Routes ---
    @POST("auth/register")
    Call<UserModel> register(@Body UserModel user);

    @POST("auth/login")
    Call<UserModel> login(@Body UserModel user);

    @POST("auth/logout")
    Call<Void> logout();

    @GET("auth/user")
    Call<UserModel> getUser();

    @GET("auth/getAll")
    Call<List<UserModel>> getAllUsers();

    // --- YogaCourse Routes ---
    @POST("course/create")
    Call<YogaCourse> createCourse(@Body YogaCourse course);

    @GET("course/getAll")
    Call<List<YogaCourse>> getAllYogaCourses();

    @GET("course/{id}")
    Call<YogaCourse> getYogaCourseById(@Path("id") String courseId);

    @PUT("course/{id}")
    Call<YogaCourse> updateCourse(@Path("id") String courseId, @Body YogaCourse course);

    @DELETE("course/{id}")
    Call<Void> deleteYogaCourse(@Path("id") String courseId);

    @POST("course/search")
    Call<List<YogaCourse>> searchYogaCourses(@Query("dayOfWeek") String dayOfWeek);

    // --- Class Routes ---
    @POST("class/{courseId}/create")
    Call<ClassModel> createClassInCourse(@Path("courseId") String courseId, @Body ClassModel classModel);

    @GET("class/{courseId}/getAll")
    Call<List<ClassModel>> getAllClassesInCourse(@Path("courseId") String courseId);

    @GET("class/getAll")
    Call<List<ClassModel>> getAllClasses();

    @GET("class/{classId}")
    Call<ClassModel> getClassById(@Path("classId") String classId);

    @PUT("class/{classId}")
    Call<ClassModel> updateClass(@Path("classId") String classId, @Body ClassModel classModel);

    @DELETE("class/{classId}")
    Call<Void> deleteClassById(@Path("classId") String classId);

    @POST("class/search")
    Call<List<ClassModel>> searchClass(@Query("teacherName") String teacherName);

    @GET("order/")
    Call<List<OrderModel>> getAllOrders();
}
