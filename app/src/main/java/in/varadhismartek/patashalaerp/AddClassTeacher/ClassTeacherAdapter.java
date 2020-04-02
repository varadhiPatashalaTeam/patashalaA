package in.varadhismartek.patashalaerp.AddClassTeacher;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

import in.varadhismartek.Utils.Constant;
import in.varadhismartek.patashalaerp.R;
import in.varadhismartek.patashalaerp.Retrofit.APIService;
import in.varadhismartek.patashalaerp.Retrofit.ApiUtils;
import in.varadhismartek.patashalaerp.Retrofit.ApiUtilsPatashala;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClassTeacherAdapter extends RecyclerView.Adapter<ClassTeacherViewHolder> {

    private ArrayList<ClassTeacherModel> searchList;
    private ArrayList<ClassTeacherModel> teacherList;
    private ArrayList<ClassTeacherModel> upDateSearchList;
    private Context mContext;
    private String recyclerTag;
    private TextView tv_search;
    private Dialog dialog;
    private ProgressDialog progressDialog;
    private APIService mApiService;
    private APIService mApiServicePatashala;
    private ClassTeacherAdapter classTeacherAdapter;
    private RecyclerView rv_search;
    private Dialog myDialog;
    private String class_Name = "";
    private String section_name = "";
    private AddClassTeacherFragment.MyRefreshPage myRefreshPage;
    private ArrayList<ClassTeacherModel> classTeacherArrayList = new ArrayList<>();
    private static String MY_ID = "";

    public ClassTeacherAdapter(ArrayList<ClassTeacherModel> searchList, Context mContext,
                               String recyclerTag, TextView tv_search, Dialog dialog) {

        this.searchList = searchList;
        this.mContext = mContext;
        this.recyclerTag = recyclerTag;
        this.tv_search = tv_search;
        this.dialog = dialog;
        progressDialog = new ProgressDialog(mContext);

    }

    public ClassTeacherAdapter(ArrayList<ClassTeacherModel> teacherList, Context mContext, String recyclerTag,
                               AddClassTeacherFragment.MyRefreshPage myRefreshPage) {

        this.teacherList = teacherList;
        this.mContext = mContext;
        this.recyclerTag = recyclerTag;
        this.myRefreshPage = myRefreshPage;

        progressDialog = new ProgressDialog(mContext);
        mApiService = ApiUtils.getAPIService();

    }

    public ClassTeacherAdapter(ArrayList<ClassTeacherModel> upDateSearchList, Context mContext,
                               String recyclerTag, Dialog dialog, String class_Name, String section_name,
                               AddClassTeacherFragment.MyRefreshPage myRefreshPage) {

        this.upDateSearchList = upDateSearchList;
        this.mContext = mContext;
        this.recyclerTag = recyclerTag;
        this.dialog = dialog;
        this.class_Name = class_Name;
        this.section_name = section_name;
        this.myRefreshPage = myRefreshPage;
        mApiServicePatashala = ApiUtilsPatashala.getService();
        progressDialog = new ProgressDialog(mContext);

    }

    @NonNull
    @Override
    public ClassTeacherViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        switch (recyclerTag) {

            case Constant.RV_CLASS_TEACHER_SEARCH_ROW:
                return new ClassTeacherViewHolder(LayoutInflater.from(mContext)
                        .inflate(R.layout.layout_search_result, viewGroup, false));

            case Constant.RV_CLASS_TEACHER_LIST:
                return new ClassTeacherViewHolder(LayoutInflater.from(mContext)
                        .inflate(R.layout.layout_class_teacher_row, viewGroup, false));

            case Constant.RV_CLASS_UPDATE_TEACHER_SEARCH_ROW:
                return new ClassTeacherViewHolder(LayoutInflater.from(mContext)
                        .inflate(R.layout.layout_search_result, viewGroup, false));

        }

        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull  ClassTeacherViewHolder holder, final int position) {

        switch (recyclerTag) {

            case Constant.RV_CLASS_TEACHER_SEARCH_ROW:

                final String firstName = searchList.get(position).getFirst_name();
                final String lastName = searchList.get(position).getLast_name();

                holder.tv_name.setText(firstName + " " + lastName);
                holder.tv_id.setText(searchList.get(position).getEmployeeID());
                holder.tv_mobile.setVisibility(View.GONE);

                if (!searchList.get(position).getPhoto().equalsIgnoreCase("")) {
                    Glide.with(mContext)
                            .load(Constant.IMAGE_URL + searchList.get(position).getPhoto())
                            .into(holder.civ_image);
                }

                holder.ll_parent.setVisibility(View.GONE);
                holder.ll_staff.setVisibility(View.VISIBLE);
                holder.tv_dept_name.setText(searchList.get(position).getDepartment());
                holder.tv_role_name.setText(searchList.get(position).getRole());

                holder.ll_search_row.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                       // Toast.makeText(mContext, "Clicked " + position, Toast.LENGTH_SHORT).show();
                        tv_search.setText(firstName + " " + lastName);

                        AddClassTeacherFragment.SEARCH_ID =
                                searchList.get(position).getEmployeeID();

                        dialog.dismiss();
                    }
                });

                if (position == 0) {
                    holder.ll_search_row.setBackgroundResource(R.color.white);

                } else if (position % 2 != 0) {
                    holder.ll_search_row.setBackgroundResource(R.color.trans_green);

                } else {
                    holder.ll_search_row.setBackgroundResource(R.color.white);

                }

                break;

            case Constant.RV_CLASS_TEACHER_LIST:

                final String className = teacherList.get(position).getClassName();
                final String sectionName = teacherList.get(position).getSectionName();

                holder.tv_classSection.setText(className + "(" + sectionName + ")");
                holder.tv_teacherName.setText(teacherList.get(position).getTeacher_first_name());
                holder.tv_teacherId.setText(teacherList.get(position).getTeacher_custom_id());

                holder.iv_action.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        myDialog = new Dialog(mContext);
                        myDialog.setContentView(R.layout.dialog_search_emp);
                        myDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                        myDialog.show();

                        EditText et_search = myDialog.findViewById(R.id.et_search);
                        rv_search = myDialog.findViewById(R.id.rv_search);

                        classTeacherAdapter = new ClassTeacherAdapter(classTeacherArrayList, mContext,
                                Constant.RV_CLASS_UPDATE_TEACHER_SEARCH_ROW, myDialog,className, sectionName,myRefreshPage);
                        rv_search.setLayoutManager(new LinearLayoutManager(mContext));
                        rv_search.setAdapter(classTeacherAdapter);
                        classTeacherAdapter.notifyDataSetChanged();

                        getStaffListAPI();

                        et_search.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                            }

                            @Override
                            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                            }

                            @Override
                            public void afterTextChanged(Editable editable) {

                                teacherFilter(editable.toString());
                            }
                        });

                    }
                });

                if (position == 0) {
                    holder.ll_row.setBackgroundResource(R.color.white);

                } else if (position % 2 != 0) {
                    holder.ll_row.setBackgroundResource(R.color.trans_green);

                } else {

                    holder.ll_row.setBackgroundResource(R.color.white);
                }

                break;

            case Constant.RV_CLASS_UPDATE_TEACHER_SEARCH_ROW:

                String first = upDateSearchList.get(position).getFirst_name();
                String last = upDateSearchList.get(position).getLast_name();

                holder.tv_name.setText(first + " " + last);
                holder.tv_id.setText(upDateSearchList.get(position).getEmployeeID());
                holder.tv_mobile.setVisibility(View.GONE);

                if (!upDateSearchList.get(position).getPhoto().equalsIgnoreCase("")) {
                    Glide.with(mContext)
                            .load(Constant.IMAGE_URL + upDateSearchList.get(position).getPhoto())
                            .into(holder.civ_image);
                }

                holder.ll_parent.setVisibility(View.GONE);
                holder.ll_staff.setVisibility(View.VISIBLE);
                holder.tv_dept_name.setText(upDateSearchList.get(position).getDepartment());
                holder.tv_role_name.setText(upDateSearchList.get(position).getRole());

                holder.ll_search_row.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                       // Toast.makeText(mContext, "Clicked " + position, Toast.LENGTH_SHORT).show();

                        MY_ID = upDateSearchList.get(position).getEmployeeID();

                        progressDialog.show();

                        mApiServicePatashala.updateClassTeacher(Constant.SCHOOL_ID, Constant.EMPLOYEE_BY_ID,
                                class_Name, section_name, MY_ID).enqueue(new Callback<Object>() {
                            @Override
                            public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {

                               if (response.isSuccessful()){
                                   String message = null;
                                   try {
                                       JSONObject object = new JSONObject(new
                                               Gson().toJson(response.body()));

                                       message = object.getString("message");
                                       Log.d("Update_class_teacher", object+"");
                                       progressDialog.dismiss();
                                       notifyDataSetChanged();
                                       myRefreshPage.refreshPage();
                                       dialog.dismiss();
                                       Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                                   } catch (JSONException e) {
                                       e.printStackTrace();
                                   }


                               }else {
                                   try {
                                       assert response.errorBody() != null;
                                       JSONObject object = new JSONObject(response.errorBody().string());
                                       String message = object.getString("message");
                                       Log.d("Update_class_teacher", message);
                                       Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                                       progressDialog.dismiss();

                                   } catch (Exception e) {
                                       e.printStackTrace();
                                   }
                               }
                            }

                            @Override
                            public void onFailure(@NonNull Call<Object> call, @NonNull Throwable t) {

                            }
                        });
                    }
                });

                if (position == 0) {
                    holder.ll_search_row.setBackgroundResource(R.color.white);

                } else if (position % 2 != 0) {
                    holder.ll_search_row.setBackgroundResource(R.color.trans_green);

                } else {
                    holder.ll_search_row.setBackgroundResource(R.color.white);

                }

                break;
        }
    }

    @Override
    public int getItemCount() {

        switch (recyclerTag) {
            case Constant.RV_CLASS_TEACHER_SEARCH_ROW:
                return searchList.size();

            case Constant.RV_CLASS_TEACHER_LIST:
                return teacherList.size();

            case Constant.RV_CLASS_UPDATE_TEACHER_SEARCH_ROW:
                return upDateSearchList.size();
        }

        return 0;
    }


    public void StafffilterList(ArrayList<ClassTeacherModel> filteredStaffList) {
        searchList = filteredStaffList;
        notifyDataSetChanged();
    }

    private void teacherFilter(String text) {

        ArrayList<ClassTeacherModel> filteredStaffList = new ArrayList<>();
        for (ClassTeacherModel item : searchList) {
            if (item.getFirst_name().toLowerCase().contains(text.toLowerCase())) {
                filteredStaffList.add(item);
            }
        }
        classTeacherAdapter.StafffilterList(filteredStaffList);
    }


    private void getStaffListAPI() {

        classTeacherArrayList.clear();
        progressDialog.show();

        mApiService.getAllEmployeeList(Constant.SCHOOL_ID).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {

                if (response.isSuccessful()) {

                    try {
                        JSONObject object = new JSONObject(new Gson().toJson(response.body()));
                        String status1 = object.getString("status");
                        String message1 = object.getString("message");

                        Log.d("STUDENT_LIST_Succ", response.code() + " " + message1);

                        if (status1.equalsIgnoreCase("Success")) {

                            JSONObject jsonData = object.getJSONObject("data");

                            Iterator<String> key = jsonData.keys();

                            while (key.hasNext()) {

                                JSONObject keyData = jsonData.getJSONObject(key.next());

                                String first_name = keyData.getString("first_name");
                                String custom_employee_id = "";
                                if (!keyData.isNull("custom_employee_id")) {
                                    custom_employee_id = keyData.getString("custom_employee_id");
                                }
                                String phone_number = keyData.getString("phone_number");
                                String added_datetime = keyData.getString("added_datetime");
                                String sex = keyData.getString("sex");
                                String last_name = keyData.getString("last_name");
                                String adhaar_card_no = keyData.getString("adhaar_card_no");
                                String photo = keyData.getString("employee_photo");
                                String wing_name = keyData.getString("wing_name");
                                String role = keyData.getString("role");
                                //String employee_status = keyData.getString("employee_status");
                                String employee_uuid = keyData.getString("employee_uuid");
                                String employee_deleted = keyData.getString("employee_deleted");
                                String department_name = keyData.getString("department_name");
                                //String date_of_joining = keyData.getString("date_of_joining");

                                classTeacherArrayList.add(new ClassTeacherModel(photo, first_name, last_name, department_name,
                                        role, employee_uuid));

                            }

                            classTeacherAdapter.notifyDataSetChanged();
                            progressDialog.dismiss();
                            Log.d("STUDENT_LIST_SIZE", " " + classTeacherArrayList.size());


                        } else {
                            Log.d("STUDENT_LIST_ELSE", response.code() + " " + message1);
                            classTeacherAdapter.notifyDataSetChanged();
                            progressDialog.dismiss();

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    try {
                        assert response.errorBody() != null;
                        JSONObject object = new JSONObject(response.errorBody().string());
                        String message = object.getString("message");
                        Log.d("STUDENT_LIST_FAIL", message);
                        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                        classTeacherAdapter.notifyDataSetChanged();
                        progressDialog.dismiss();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<Object> call, @NonNull Throwable t) {

            }
        });
    }
}
