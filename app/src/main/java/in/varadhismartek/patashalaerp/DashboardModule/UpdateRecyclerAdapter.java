package in.varadhismartek.patashalaerp.DashboardModule;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import in.varadhismartek.Utils.Constant;
import in.varadhismartek.patashalaerp.AddDepartment.Data;
import in.varadhismartek.patashalaerp.R;
import in.varadhismartek.patashalaerp.Retrofit.APIService;
import in.varadhismartek.patashalaerp.Retrofit.ApiUtils;
import in.varadhismartek.patashalaerp.DashboardModule.UpdateActivity.UpdateDepartmentActivity;
import in.varadhismartek.patashalaerp.DashboardModule.UpdateActivity.UpdateRolesActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateRecyclerAdapter extends RecyclerView.Adapter<UpdateRecyclerViewHolder> {
    ArrayList<Data> dataArrayList;
    Context context;
    Data data;
    String tag;
    APIService mApiService;
    in.varadhismartek.patashalaerp.Adapters.RecyclerAdapter recyclerAdapter;
    ProgressDialog mProgressDialog;
    Dialog dialog;


    public UpdateRecyclerAdapter() {
        dataArrayList = new ArrayList<>();
        mApiService = ApiUtils.getAPIService();

    }

    public UpdateRecyclerAdapter(Context context, ArrayList<Data> dataArrayList, String tag) {
        this.context = context;
        this.dataArrayList = dataArrayList;
        this.tag = tag;
        mApiService = ApiUtils.getAPIService();

    }


    @Override
    public UpdateRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        switch (tag) {

            case Constant.BARRIERS_FRAG:
                return new UpdateRecyclerViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.recycler_card, parent, false));

            case Constant.DEPARTMENT_FRAG:
                return new UpdateRecyclerViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.recycler_card, parent, false));



        }
        return null;
    }


    @Override
    public void onBindViewHolder(final UpdateRecyclerViewHolder holder, final int position) {
        //This is for if you the view it will change the color
        Log.d("arraySize1", "" + dataArrayList.size());


        switch (tag) {

            case Constant.BARRIERS_FRAG:

                holder.check_box.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        dataArrayList.get(position).setSelect(isChecked);
                        holder.check_box.setChecked(isChecked);
                        if (isChecked) {
                            holder.check_box.setBackgroundColor(Color.parseColor("#52b155"));
                            holder.check_box.setTextColor(Color.WHITE);
                        } else {

                            holder.check_box.setBackgroundColor(Color.WHITE);
                            holder.check_box.setTextColor(Color.BLACK);
                        }
                    }
                });

                //This is for Whatever click the values that are Selectable

                data = dataArrayList.get(position);
                holder.check_box.setText(data.getName());
                holder.check_box.setChecked(data.isSelect());
                if (data.isSelect()) {
                    holder.check_box.setBackgroundColor(Color.parseColor("#52b155"));
                    holder.check_box.setTextColor(Color.WHITE);
                    Log.d("Added", "onBindViewHolder: ");
                } else {
                    holder.check_box.setBackgroundColor(Color.WHITE);
                    holder.check_box.setTextColor(Color.BLACK);
                }


                holder.check_box.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        OpenDialogUpdateRoles(holder.check_box.getText().toString());
                        return true;
                    }
                });

                holder.check_box.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String status;
                        if (holder.check_box.isChecked()) {
                            status = "true";
                        } else {
                            status = "false";
                        }
                        String deptName = holder.check_box.getText().toString();

                        JSONObject objectSize = new JSONObject();
                        JSONObject objectDept = new JSONObject();
                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("name", deptName);
                            jsonObject.put("status", status);
                            objectSize.put("1",jsonObject);
                            objectDept.put("roles", objectSize);

                        } catch (JSONException je) {

                        }
                        Log.d("asdf", ""+ Constant.wingName);
                        Log.d("asdf", ""+  Constant.deptName);
                        Log.d("asdf", ""+objectDept);
                        updateRolesStatus(Constant.wingName,Constant.deptName,objectDept);
                    }
                });
                break;


            case Constant.DEPARTMENT_FRAG:

                Log.d("asdf", dataArrayList.size() + "");

                holder.check_box.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        dataArrayList.get(position).setSelect(isChecked);
                        holder.check_box.setChecked(isChecked);
                        if (isChecked) {
                            holder.check_box.setBackgroundColor(Color.parseColor("#52b155"));
                            holder.check_box.setTextColor(Color.WHITE);
                        } else {

                            holder.check_box.setBackgroundColor(Color.WHITE);
                            holder.check_box.setTextColor(Color.BLACK);
                        }
                    }
                });

                //This is for Whatever click the values that are Selectable

                data = dataArrayList.get(position);
                holder.check_box.setText(data.getName());
                holder.check_box.setChecked(data.isSelect());
                if (data.isSelect()) {
                    holder.check_box.setBackgroundColor(Color.parseColor("#52b155"));
                    holder.check_box.setTextColor(Color.WHITE);
                    Log.d("Added", "onBindViewHolder: ");
                } else {
                    holder.check_box.setBackgroundColor(Color.WHITE);
                    holder.check_box.setTextColor(Color.BLACK);
                }


                holder.check_box.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        Log.i("DEPARTMENT_UP", "UPDATE");
                        OpenDialogUpdateDepartment(dataArrayList, position, holder.check_box,
                                holder.check_box.getText().toString());
                        return true;
                    }
                });

                holder.check_box.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String status;
                        if (holder.check_box.isChecked()) {
                            status = "true";
                        } else {
                            status = "false";
                        }
                        String deptName = holder.check_box.getText().toString();

                        JSONObject objectSize = new JSONObject();
                        JSONObject objectDept = new JSONObject();
                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("name", deptName);
                            jsonObject.put("status", status);
                            objectSize.put("1",jsonObject);
                            objectDept.put("departments", objectSize);

                        } catch (JSONException je) {

                        }
                        updateDepartmentStatus(Constant.wingName,objectDept);

                    }
                });


                break;

        }


    }

    private void updateRolesStatus(String wingName, String deptName, JSONObject objectDept) {
        mApiService.updateRolesStatus(Constant.SCHOOL_ID,wingName,deptName,objectDept,Constant.EMPLOYEE_BY_ID)
                .enqueue(new Callback<Object>() {
                    @Override
                    public void onResponse(Call<Object> call, Response<Object> response) {
                        if (response.isSuccessful()){
                            Log.i("Roles_Status",""+response.body());
                        }
                    }

                    @Override
                    public void onFailure(Call<Object> call, Throwable t) {

                    }
                });
    }

    private void updateDepartmentStatus(String wingName, JSONObject objectDept) {
        mApiService.updateDeptStatus(Constant.SCHOOL_ID,wingName,objectDept,Constant.EMPLOYEE_BY_ID)
                .enqueue(new Callback<Object>() {
                    @Override
                    public void onResponse(Call<Object> call, Response<Object> response) {
                        Log.i("STATUS_UPDATE",""+response.body());
                    }

                    @Override
                    public void onFailure(Call<Object> call, Throwable t) {

                    }
                });



    }
    private void OpenDialogUpdateDepartment(final ArrayList<Data> dataArrayList, final int position,
                                            final CheckBox checkBox, final String dept) {

        dialog = new Dialog(context);
        dialog.setContentView(R.layout.update_delete_dialog);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        EditText etOldDept = dialog.findViewById(R.id.etOldRole);
        final EditText newOldDept = dialog.findViewById(R.id.etNewRole);
        Button updateRole = dialog.findViewById(R.id.btnUpdateRole);
        final Button deleteRole = dialog.findViewById(R.id.btnDeleteRole);
        TextView tvTitle =dialog.findViewById(R.id.tvTitle);
        tvTitle.setText("Department");


        etOldDept.setText(dept);
        etOldDept.setFocusable(false);
        etOldDept.setFocusableInTouchMode(false);


        updateRole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String updateDept = newOldDept.getText().toString();
                if (!updateDept.equals("")) {
                    updateDeptApi(checkBox, dept, updateDept);
                    notifyDataSetChanged();

                }

            }
        });


        deleteRole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });


        dialog.show();


    }

    private void updateDeptApi(final CheckBox checkBox, String dept, final String updaDept) {


        mApiService.updateDeptName(Constant.SCHOOL_ID, Constant.wingName, dept, updaDept, Constant.EMPLOYEE_BY_ID)
                .enqueue(new Callback<Object>() {
                    @Override
                    public void onResponse(Call<Object> call, Response<Object> response) {

                        try {
                            Log.d("updatedDept", "" + response.body());
                            if (response.isSuccessful()) {
                                Toast.makeText(context, "Updated Successfully", Toast.LENGTH_SHORT).show();
                                JSONObject json1 = null;
                                json1 = (new JSONObject(new Gson().toJson(response.body())));
                                String status = (String) json1.get("status");
                                String message = (String) json1.get("message");
                                if (status.equalsIgnoreCase("Success")) {
                                    dialog.dismiss();
                                    checkBox.setText(updaDept);
                                    notifyDataSetChanged();
                                    Intent intent = new Intent(context, UpdateDepartmentActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    context.startActivity(intent);
                                }


                            } else {
                                Log.d("UPDATE_WINGS_FAIL", String.valueOf(response.code()));
                                if (String.valueOf(response.code()).equals("409")) {
                                    Toast.makeText(context, "Department name already exists", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                }
                                if (String.valueOf(response.code()).equals("404")) {
                                    Toast.makeText(context, "Old wing name not exists", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                }

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        notifyDataSetChanged();
                        dialog.dismiss();
                    }

                    @Override
                    public void onFailure(Call<Object> call, Throwable t) {
                        dialog.dismiss();
                    }
                });


    }

    private void OpenDialogUpdateRoles(final String role) {
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.update_delete_dialog);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        EditText etOldRole = dialog.findViewById(R.id.etOldRole);
        final EditText newRole = dialog.findViewById(R.id.etNewRole);
        Button updateRole = dialog.findViewById(R.id.btnUpdateRole);
        final Button deleteRole = dialog.findViewById(R.id.btnDeleteRole);
        TextView tvTitle =dialog.findViewById(R.id.tvTitle);
        tvTitle.setText("Roles");
        etOldRole.setText(role);
        etOldRole.setFocusable(false);
        etOldRole.setFocusableInTouchMode(false);


        updateRole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String updateRole = newRole.getText().toString();
                if (!updateRole.equals("")) {
                    updateRolesApi(role, updateRole);

                }

            }
        });


        deleteRole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                //deleteRoleApi(role);

            }
        });


        dialog.show();


    }

    private void updateRolesApi(final String role, String updateRole) {

        mApiService.updateRolesName(Constant.SCHOOL_ID, Constant.wingName, Constant.deptName, role,
                updateRole, Constant.EMPLOYEE_BY_ID).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
//                dialog.dismiss();

                if (response.isSuccessful()) {
                    try {
                        JSONObject json1 = (new JSONObject(new Gson().toJson(response.body())));
                        String status = (String) json1.get("status");

                        if (status.equalsIgnoreCase("Success")) {
                            Toast.makeText(context,"Update Successfully",Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                            Intent intent = new Intent(context, UpdateRolesActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);


                        }
                    } catch (JSONException je) {

                    }
                }
                else {
                    dialog.dismiss();
                    if (String.valueOf(response.code()).equals("409")) {
                        Toast.makeText(context, "Role name already exists", Toast.LENGTH_SHORT).show();

                    }
                    if (String.valueOf(response.code()).equals("404")) {
                        Toast.makeText(context, "Old role name does not exists", Toast.LENGTH_SHORT).show();

                    }

                }



                Log.d("updatedRoles", "" + response.body());

            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                dialog.dismiss();
            }
        });


    }


    @Override
    public int getItemCount() {
        return dataArrayList.size();
    }


    public void newValues(String s) {
        dataArrayList.add(new Data(s, true));
        notifyDataSetChanged();
    }

}
