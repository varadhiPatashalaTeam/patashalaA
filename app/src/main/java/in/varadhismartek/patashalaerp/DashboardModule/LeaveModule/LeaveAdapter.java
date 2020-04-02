package in.varadhismartek.patashalaerp.DashboardModule.LeaveModule;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import in.varadhismartek.Utils.Constant;
import in.varadhismartek.patashalaerp.DashboardModule.DashBoardMenuActivity;
import in.varadhismartek.patashalaerp.R;
import in.varadhismartek.patashalaerp.Retrofit.APIService;
import in.varadhismartek.patashalaerp.Retrofit.ApiUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LeaveAdapter extends RecyclerView.Adapter<LeaveViewHolder> {

    private ArrayList<LeaveModel> leaveList;
    private ArrayList<EmpLeaveModel> empLeaveList;
    private Context mContext;
    private String recyclerTag;
    private RefereshName refereshName;
    private APIService mApiService;
    private ProgressDialog progressDialog;
    //   private LeaveStatementFragment.UpdateChartData updateChartData;

    private String status;

    // ADMIN LEAVE LIST
 /*   LeaveAdapter(Context mContext, ArrayList<EmpLeaveModel> empLeaveList, String recyclerTag){
        this.empLeaveList   = empLeaveList;
        this.mContext    = mContext;
        this.recyclerTag = recyclerTag;
    }*/

    // for nested and barrier RV_LEAVE_BARRIER or leave statement
    public LeaveAdapter(ArrayList<LeaveModel> leaveList, Context mContext, String recyclerTag) {
        this.leaveList = leaveList;
        this.mContext = mContext;
        this.recyclerTag = recyclerTag;
    }

    // leave statement name
   /* LeaveAdapter(ArrayList<LeaveModel> leaveList, Context mContext, String recyclerTag, LeaveStatementFragment.UpdateChartData updateChartData){
        this.leaveList   = leaveList;
        this.mContext    = mContext;
        this.recyclerTag = recyclerTag;
        this.updateChartData = updateChartData;
    }
*/

    LeaveAdapter(ArrayList<LeaveModel> leaveList, Context mContext, String recyclerTag, RefereshName refereshName) {
        this.leaveList = leaveList;
        this.mContext = mContext;
        this.recyclerTag = recyclerTag;
        this.refereshName = refereshName;
        mApiService = ApiUtils.getAPIService();
        progressDialog = new ProgressDialog(mContext);
        status = "";
    }

    //RV_LEAVE_ADMIN_LIST
    public LeaveAdapter(Context mContext, ArrayList<LeaveModel> leaveModels, String recyclerTag) {
        this.mContext = mContext;
        this.leaveList = leaveModels;
        this.recyclerTag = recyclerTag;

    }


    @NonNull
    @Override
    public LeaveViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        switch (recyclerTag) {

            case Constant.RV_LEAVE_INBOX_LIST_UPPER:
                return new LeaveViewHolder(LayoutInflater.from(mContext)
                        .inflate(R.layout.leave_inbox_row, viewGroup, false));

            case Constant.RV_LEAVE_BARRIER:
                return new LeaveViewHolder(LayoutInflater.from(mContext)
                        .inflate(R.layout.select_module_row_card, viewGroup, false));

            case Constant.RV_SCHOOL_LEAVE_BARRIER:
                return new LeaveViewHolder(LayoutInflater.from(mContext)
                        .inflate(R.layout.school_leave_barrier_row, viewGroup, false));




           /* case Constant.RV_ADMIN_LEAVE_LIST:
                return new LeaveViewHolder(LayoutInflater.from(mContext)
                        .inflate(R.layout.leave_admin_upper_list, viewGroup, false));
*/
            //    case Constant.RV_ADMIN_LEAVE_NESTED_LIST:
            case Constant.RV_LEAVE_ADMIN_LIST:
                return new LeaveViewHolder(LayoutInflater.from(mContext)
                        .inflate(R.layout.leave_admin_nested_row, viewGroup, false));






          /*  case Constant.RV_LEAVE_STATEMENT:
                return new LeaveViewHolder(LayoutInflater.from(mContext)
                        .inflate(R.layout.statement_table_row, viewGroup, false));

            case Constant.RV_LEAVE_STATEMENT_NAME:
                return new LeaveViewHolder(LayoutInflater.from(mContext)
                        .inflate(R.layout.statement_type_row, viewGroup, false));


            */
        }

        //noinspection ConstantConditions RV_SCHOOL_LEAVE_BARRIER
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull final LeaveViewHolder holder, final int position) {


        switch (recyclerTag) {

            case Constant.RV_LEAVE_INBOX_LIST_UPPER:

                holder.tv_applied_date.setText(leaveList.get(position).getAppliedDate());
                holder.tv_leaveType.setText(leaveList.get(position).getLeavetype());
                holder.tv_fromDate.setText(leaveList.get(position).getFrom_date());
                holder.tv_toDate.setText(leaveList.get(position).getToDate());
                holder.tv_status.setText(leaveList.get(position).getStatus());

                String subject = "";
                if (leaveList.get(position).getSubject().length() > 20) {
                    subject = leaveList.get(position).getSubject().substring(0, 20);
                    Log.d("SUBJECT", subject);
                    holder.tv_subject.setText(subject + "...");
                } else {
                    holder.tv_subject.setText(leaveList.get(position).getSubject());
                }


                char type = leaveList.get(position).getLeavetype().charAt(0);
                holder.tv_shortName.setText(type + "L");

                switch (leaveList.get(position).getStatus()) {
                    case "Pending":
                        holder.iv_status_circle.setImageResource(R.drawable.ring_shape2);
                        break;
                    case "Cancelled":
                        holder.iv_status_circle.setImageResource(R.drawable.ring_shape2);
                        break;
                    case "Approved":
                        holder.iv_status_circle.setImageResource(R.drawable.ring_shape3);
                        break;
                    case "Rejected":
                        holder.iv_status_circle.setImageResource(R.drawable.ring_shape4);
                        break;
                }

                holder.ll_leave_row.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        final DashBoardMenuActivity leaveView = (DashBoardMenuActivity) mContext;
                        Bundle bundle = new Bundle();
                        bundle.putString("leaveID", leaveList.get(position).getLeaveID());
                        bundle.putString("EMPUUID", leaveList.get(position).getEmpID());
                        bundle.putString("EMPLOYEE", "ADMIN_LEAVE");
                        Log.i("leaveList..getEmpID()**", "" + leaveList.get(position).getEmpID()
                                + "**" + leaveList.get(position).getLeaveID());

                        leaveView.loadFragment(Constant.LEAVE_VIEW_FRAGMENT, bundle);


                    }
                });

                if (position == 0) {
                    holder.ll_leave_row.setBackgroundResource(R.color.trans_green);

                } else if (position % 2 != 0) {
                    holder.ll_leave_row.setBackgroundResource(R.color.white);

                }

                break;

            case Constant.RV_LEAVE_BARRIER:

                holder.check_box.setText(leaveList.get(position).getLeavetype());

                if (leaveList.get(position).isChecked()) {
                    holder.check_box.setBackgroundResource(R.color.barrier_green_colorPrimary);
                    holder.check_box.setTextColor(Color.WHITE);

                } else {

                    holder.check_box.setBackgroundResource(R.color.white);
                    holder.check_box.setTextColor(Color.BLACK);
                }

                holder.check_box.setChecked(leaveList.get(position).isChecked());

                holder.check_box.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                        if (b) {
                            holder.check_box.setBackgroundResource(R.color.barrier_green_colorPrimary);
                            holder.check_box.setTextColor(Color.WHITE);
                            holder.check_box.setChecked(true);
                            leaveList.get(position).setChecked(true);

                        } else {
                            holder.check_box.setBackgroundResource(R.color.white);
                            holder.check_box.setTextColor(Color.BLACK);
                            holder.check_box.setChecked(false);
                            leaveList.get(position).setChecked(false);

                        }

                        updateLeaveTypeStatusApi(leaveList.get(position).getLeavetype(), b);

                    }
                });

                holder.check_box.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        Log.d("Status Update", "Long Press");
                        updateLeaveBarrierNameAPI(leaveList.get(position).getLeavetype());

                        return false;
                    }
                });

                break;

            case Constant.RV_SCHOOL_LEAVE_BARRIER:


                holder.tv_leavetype.setText(leaveList.get(position).getLeavetype());
                holder.tv_leaveCode.setText(leaveList.get(position).getLeaveCode());
                holder.tv_leaveNo.setText(leaveList.get(position).getNoOfLeave());
                holder.tv_noticePeriod.setText(leaveList.get(position).getNoticePeriod());
                holder.tv_applyTimes.setText(leaveList.get(position).getAvailability());

                holder.ll_barrier_row.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        status = leaveList.get(position).getStatus();

                        openDialogForUpdate(leaveList.get(position).getLeavetype(), status);

                        return false;
                    }
                });


                break;

     /*       case Constant.RV_LEAVE_STATEMENT:

                holder.tv_leaveType.setText(leaveList.get(position).getLeavetype());
                holder.tv_totalLeave.setText(leaveList.get(position).getTotalLeave());
                holder.tv_usedLeave.setText(leaveList.get(position).getUsedLeave());
                holder.tv_leftLeave.setText(leaveList.get(position).getLeftLeave());

                break;

            case Constant.RV_LEAVE_STATEMENT_NAME:
                holder.tv_leaveType.setText(leaveList.get(position).getLeavetype());
                holder.view_color.setBackgroundResource(R.color.teacherPrimary);

                holder.ll_row.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        updateChartData.setData(leaveList.get(position).getTotalLeave(),leaveList.get(position).getUsedLeave(),
                                leaveList.get(position).getLeftLeave());
                    }
                });

                break;*/

            /*case Constant.RV_ADMIN_LEAVE_LIST:

                holder.tv_applied_date.setText(empLeaveList.get(position).getAppliedDate());

                LeaveAdapter adapter = new LeaveAdapter(empLeaveList.get(position).getLeaveList(),
                        mContext,Constant.RV_ADMIN_LEAVE_NESTED_LIST);
                holder.recycler_view.setLayoutManager(new LinearLayoutManager(mContext));
                holder.recycler_view.setAdapter(adapter);
                adapter.notifyDataSetChanged();

                break;*/

            case Constant.RV_LEAVE_ADMIN_LIST:

                holder.tv_name.setText(leaveList.get(position).getEmp_name());
                holder.tv_fromDate.setText(leaveList.get(position).getFrom_date());
                holder.tv_toDate.setText(leaveList.get(position).getFrom_date() + " To " + leaveList.get(position).getToDate());
                holder.tv_status.setText(leaveList.get(position).getStatus());


                if(!leaveList.get(position).getEmpPhoto().equals("")){

                    Glide.with(mContext).load(Constant.IMAGE_URL+leaveList.get(position).getEmpPhoto()).into(holder.civ_image);

                }

                Log.d("SUBJECTDate*",leaveList.get(position).getEmpPhoto());

                String subject1 = "";
                if (leaveList.get(position).getSubject().length() > 20) {
                    subject1 = leaveList.get(position).getSubject().substring(0, 20);
                    Log.d("SUBJECT", subject1);
                    holder.tv_subject.setText(subject1 + "...");
                } else {
                    holder.tv_subject.setText(leaveList.get(position).getSubject());
                }


                switch (leaveList.get(position).getStatus()) {
                    case "Pending":
                        holder.iv_status_circle.setImageResource(R.drawable.ring_shape2);
                        break;
                    case "Cancelled":
                        holder.iv_status_circle.setImageResource(R.drawable.ring_shape2);
                        break;
                    case "Approved":
                        holder.iv_status_circle.setImageResource(R.drawable.ring_shape3);
                        break;
                    case "Rejected":
                        holder.iv_status_circle.setImageResource(R.drawable.ring_shape4);
                        break;
                }

                if (position == 0) {
                    holder.ll_leave_row.setBackgroundResource(R.color.lightColorRow);

                } else if (position % 2 != 0) {
                    holder.ll_leave_row.setBackgroundResource(R.color.white);

                } else {
                    holder.ll_leave_row.setBackgroundResource(R.color.lightColorRow);

                }

                holder.ll_leave_row.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        final DashBoardMenuActivity leaveView = (DashBoardMenuActivity) mContext;
                        Bundle bundle = new Bundle();
                        Constant.EMPLOYEE_ID = leaveList.get(position).getEmpID();
                        bundle.putString("leaveID", leaveList.get(position).getLeaveID());
                        bundle.putString("EMPUUID", Constant.EMPLOYEE_ID);
                        bundle.putString("EMPLOYEE", "EMP_LEAVE");
                        Log.i("leaveList..getEmpID()**", "" + leaveList.get(position).getEmpID());
                        leaveView.loadFragment(Constant.LEAVE_VIEW_FRAGMENT, bundle);


                    }
                });

                break;


        }

    }

    @Override
    public int getItemCount() {

        switch (recyclerTag) {

            case Constant.RV_LEAVE_INBOX_LIST_UPPER:
                return leaveList.size();

            case Constant.RV_LEAVE_BARRIER:
                return leaveList.size();

            case Constant.RV_SCHOOL_LEAVE_BARRIER:
                return leaveList.size();

            case Constant.RV_LEAVE_STATEMENT:
                return leaveList.size();

            case Constant.RV_LEAVE_STATEMENT_NAME:
                return leaveList.size();

           /* case Constant.RV_ADMIN_LEAVE_LIST:
                return empLeaveList.size();

*/


            case Constant.RV_LEAVE_ADMIN_LIST:
                return leaveList.size();

        }
        return 0;
    }

    private void updateLeaveTypeStatusApi(String leaveType, boolean val) {

        JSONObject obj = new JSONObject();
        JSONObject objCount = new JSONObject();
        JSONObject objLeave = new JSONObject();

        try {

            obj.put("name", leaveType);
            obj.put("status", val + "");

            objCount.put("1", obj);
            objLeave.put("leaves", objCount);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d("MY_JSON_OBJ", objLeave.toString());


        progressDialog.show();
        progressDialog.setMessage("Please Wait");
        mApiService.updateSchoolLeaveStatus(Constant.SCHOOL_ID, objLeave, Constant.EMPLOYEE_BY_ID).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {

                if (response.isSuccessful()) {

                    Log.d("MY_JSON_OBJ_success", response.body().toString());

                    try {
                        JSONObject jsonObject = new JSONObject(new Gson().toJson(response.body()));

                        String status = jsonObject.getString("status");

                        if (status.equalsIgnoreCase("Success")) {

                            Toast.makeText(mContext, "Status Update Successful", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(mContext, "Status Update Failed", Toast.LENGTH_SHORT).show();

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {

                    Toast.makeText(mContext, "Status Update Failed", Toast.LENGTH_SHORT).show();

                }

                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                progressDialog.dismiss();

            }
        });

    }

    private void updateLeaveBarrierNameAPI(final String leavetype) {

        final Dialog dialog = new Dialog(mContext);

        dialog.setContentView(R.layout.dialog_update_leave_name);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.flag_transparent);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        dialog.show();

        TextView tv_old_name = dialog.findViewById(R.id.tv_old_name);
        final EditText et_new_name = dialog.findViewById(R.id.et_new_name);
        Button bt_cancel = dialog.findViewById(R.id.bt_cancel);
        Button bt_update = dialog.findViewById(R.id.bt_update);

        tv_old_name.setText(leavetype);

        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        bt_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String newName = et_new_name.getText().toString();

                if (newName.equals(""))
                    Toast.makeText(mContext, "New Name Is Required", Toast.LENGTH_SHORT).show();

                else {

                    mApiService.updateLeaveBarrier(Constant.SCHOOL_ID, leavetype, newName).enqueue(new Callback<Object>() {
                        @Override
                        public void onResponse(Call<Object> call, Response<Object> response) {

                            if (response.isSuccessful()) {
                                Log.d("Success Update1", response.body().toString());
                                refereshName.refresh();
                                dialog.dismiss();

                            } else {
                                Log.d("Success Update2", String.valueOf(response.code()));

                            }
                        }

                        @Override
                        public void onFailure(Call<Object> call, Throwable t) {
                            Log.d("Success Update2", t.getMessage());

                        }
                    });
                }
            }
        });
    }

    private void openDialogForUpdate(final String leaveType, String val) {

        Log.d("MY_DIALOG_DATA", leaveType + " " + val);

        final Dialog dialog = new Dialog(mContext);
        dialog.setContentView(R.layout.dialog_update_school_leave_barrier);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.flag_transparent);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();

        TextView tv_leavetype = dialog.findViewById(R.id.tv_leavetype);
        final EditText et_leaveCode = dialog.findViewById(R.id.et_leaveCode);
        final EditText et_leaveNum = dialog.findViewById(R.id.et_leaveNum);
        final EditText et_noticePeriod = dialog.findViewById(R.id.et_noticePeriod);
        final EditText et_applyTimes = dialog.findViewById(R.id.et_applyTimes);
        final TextView tv_status = dialog.findViewById(R.id.tv_status);
        Button bt_cancel = dialog.findViewById(R.id.bt_cancel);
        Button bt_update = dialog.findViewById(R.id.bt_update);

        tv_leavetype.setText(leaveType);

        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        if (val.equalsIgnoreCase("true")) {
            tv_status.setBackgroundResource(R.drawable.btn_round_shape_green);
            tv_status.setTextColor(Color.WHITE);
            tv_status.setText(val);

        } else {
            tv_status.setBackgroundResource(R.drawable.btn_round_shape_grey);
            tv_status.setTextColor(Color.BLACK);
            tv_status.setText(val);

        }

        tv_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (status.equalsIgnoreCase("true")) {
                    status = "false";
                    //tv_status.setBackgroundResource(R.drawable.btn_round_shape_grey);
                    //tv_status.setTextColor(Color.BLACK);
                    //tv_status.setText(status);

                } else {
                    status = "true";
                    //tv_status.setBackgroundResource(R.drawable.btn_round_shape_green);
                    //tv_status.setTextColor(Color.WHITE);
                    //tv_status.setText(status);
                }

                updateSchoolLeaveBarrierStatusAPI(leaveType, status, tv_status);
            }
        });

        bt_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String leaveCode = et_leaveCode.getText().toString();
                String noOfLeave = et_leaveNum.getText().toString();
                String noticePeriod = et_noticePeriod.getText().toString();
                String availability = et_applyTimes.getText().toString();

                if (leaveCode.equals(""))
                    Toast.makeText(mContext, "Leave Code Is Required", Toast.LENGTH_SHORT).show();

                else if (noOfLeave.equals(""))
                    Toast.makeText(mContext, "Leave/Year Is Required", Toast.LENGTH_SHORT).show();

                else if (noticePeriod.equals(""))
                    Toast.makeText(mContext, "Notice Period Is Required", Toast.LENGTH_SHORT).show();

                else if (availability.equals(""))
                    Toast.makeText(mContext, "Availability Is Required", Toast.LENGTH_SHORT).show();

                else {


                    mApiService.updateSchoolLeaveBarrierDetails(Constant.SCHOOL_ID, leaveType,
                            leaveCode, noOfLeave, noticePeriod, availability).enqueue(new Callback<Object>() {
                        @Override
                        public void onResponse(Call<Object> call, Response<Object> response) {

                            if (response.isSuccessful()) {

                                try {
                                    JSONObject jsonObject = new JSONObject(new Gson().toJson(response.body()));
                                    String status = jsonObject.getString("status");

                                    if (status.equalsIgnoreCase("Success")) {

                                        Toast.makeText(mContext, "Update successfully Input", Toast.LENGTH_SHORT).show();
                                        Log.d("Update_barrier_success", String.valueOf(response.code()));
                                        dialog.dismiss();
                                        refereshName.refresh();

                                    } else {
                                        Toast.makeText(mContext, "Invalid Input", Toast.LENGTH_SHORT).show();

                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            } else {

                                Log.d("Update_barrier_fail", String.valueOf(response.code()));
                            }
                        }

                        @Override
                        public void onFailure(Call<Object> call, Throwable t) {

                        }
                    });
                }
            }
        });

    }

    private void updateSchoolLeaveBarrierStatusAPI(String leaveType, final String status, final TextView tv_status) {

        //{"leave_types":{"1":{"name":"Permission Leave","status":"false"}}}

        JSONObject obj = new JSONObject();
        JSONObject objCount = new JSONObject();
        JSONObject objLeave = new JSONObject();

        try {

            obj.put("name", leaveType);
            obj.put("status", status);

            objCount.put("1", obj);
            objLeave.put("leave_types", objCount);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d("MY_JSON_OBJ", objLeave.toString());

        mApiService.updateSchoolLeaveBarrierStatus(Constant.SCHOOL_ID, objLeave, Constant.EMPLOYEE_BY_ID)
                .enqueue(new Callback<Object>() {
                    @Override
                    public void onResponse(Call<Object> call, Response<Object> response) {

                        if (response.isSuccessful()) {

                            try {
                                JSONObject jsonObject = new JSONObject(new Gson().toJson(response.body()));
                                String val = jsonObject.getString("status");

                                if (val.equalsIgnoreCase("Success")) {
                                    Toast.makeText(mContext, "Status update success", Toast.LENGTH_SHORT).show();
                                    refereshName.refresh();

                                    if (status.equalsIgnoreCase("true")) {
                                        tv_status.setBackgroundResource(R.drawable.btn_round_shape_green);
                                        tv_status.setTextColor(Color.WHITE);
                                        tv_status.setText(status);

                                    } else {
                                        tv_status.setBackgroundResource(R.drawable.btn_round_shape_grey);
                                        tv_status.setTextColor(Color.BLACK);
                                        tv_status.setText(status);

                                    }

                                } else {
                                    Toast.makeText(mContext, "Status update Failed", Toast.LENGTH_SHORT).show();

                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        } else {
                            Toast.makeText(mContext, "Status update Failed", Toast.LENGTH_SHORT).show();

                        }

                    }

                    @Override
                    public void onFailure(@NonNull Call<Object> call, @NonNull Throwable t) {

                    }
                });
    }
}
