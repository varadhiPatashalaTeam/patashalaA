package in.varadhismartek.patashalaerp.DashboardModule.EmployeeModule;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import in.varadhismartek.Utils.Constant;
import in.varadhismartek.patashalaerp.DashboardModule.AttendanceList;
import in.varadhismartek.patashalaerp.DashboardModule.Homework.HomeworkModel;
import in.varadhismartek.patashalaerp.DashboardModule.LeaveModule.EmpLeaveModel;
import in.varadhismartek.patashalaerp.DashboardModule.LeaveModule.LeaveModel;
import in.varadhismartek.patashalaerp.DashboardModule.LeaveModule.RefereshName;
import in.varadhismartek.patashalaerp.R;
import in.varadhismartek.patashalaerp.Retrofit.APIService;
import in.varadhismartek.patashalaerp.StudentModule.StudentModuleActivity;

public class EmployeeAdapter extends RecyclerView.Adapter<EmployeeViewHolder> {
    private Context context;
    private ArrayList<EmpLeaveModel> allEmployeeList;
    private String rvType, currentDate;
    private ArrayList<LeaveModel> leaveList;

    private ArrayList<HomeworkModel> homeworkModelsInbox;

    public EmployeeAdapter(Context context, ArrayList<EmpLeaveModel> employeeList, String rvType) {
        this.context = context;
        this.allEmployeeList = employeeList;
        this.rvType = rvType;
    }

    public EmployeeAdapter(ArrayList<LeaveModel> leaveList, Context context, String rvType) {
        this.leaveList = leaveList;
        this.context = context;
        this.rvType = rvType;
    }


    public EmployeeAdapter(ArrayList<HomeworkModel> homeworkModelsInbox, Context context, String currentDate, String rvType) {
        this.homeworkModelsInbox = homeworkModelsInbox;
        this.context = context;
        this.currentDate = currentDate;
        this.rvType = rvType;
    }

    @NonNull
    @Override
    public EmployeeViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        switch (rvType) {
            case Constant.RV_EMPLOYEE_LIST:
                return new EmployeeViewHolder(LayoutInflater.from(context)
                        .inflate(R.layout.employees_row, viewGroup, false));


            case Constant.RV_EMPLOYEE_LIST_ATTENDANCE:
                return new EmployeeViewHolder(LayoutInflater.from(context)
                        .inflate(R.layout.employees_row, viewGroup, false));


            case Constant.RV_LEAVE_INBOX_LIST_UPPER:

                return new EmployeeViewHolder(LayoutInflater.from(context)
                        .inflate(R.layout.leave_inbox_row, viewGroup, false));

            case Constant.RV_EMP_HOMEWORK_ROW:
                return new EmployeeViewHolder(LayoutInflater.from(context)
                        .inflate(R.layout.layout_homework_inbox_row, viewGroup, false));

        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull EmployeeViewHolder holder, final int position) {
        switch (rvType) {
            case Constant.RV_EMPLOYEE_LIST:


                holder.tv_mobileNo.setText(allEmployeeList.get(position).getEmpPhoneNo());
                holder.tvEmpName.setText(allEmployeeList.get(position).getEmpFname() + " "
                        + allEmployeeList.get(position).getEmpLname());


                holder.tvGender.setText(allEmployeeList.get(position).getSex());
                holder.tvRole.setText(allEmployeeList.get(position).getRole());
                holder.tvDept.setText(allEmployeeList.get(position).getEmpDept());

                String image_Value = allEmployeeList.get(position).getEmployee_photo();
                String customEMPID = allEmployeeList.get(position).getCustom_EMPID();
               // holder.tvEmpId.setText(customEMPID);

                System.out.println("image_Value****" + image_Value+customEMPID);
                Glide.with(context)
                        .load(Constant.IMAGE_LINK + image_Value)
                        .error(R.drawable.management_logo)
                        .into(holder.studentImage);

                if(allEmployeeList.get(position).getEmployee_status().equalsIgnoreCase("Approved")){




                    Log.d("ApprovedList",allEmployeeList.get(position).getEmpFname());



                    holder.iv_status_circle.setBackground(context.getResources().getDrawable(R.drawable.ring_shape3));

                }



                holder.llEmployee.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        EmployeeActivity employeeActivity = (EmployeeActivity) context;
                        Bundle bundle = new Bundle();

                        bundle.putString("EMPUUID", allEmployeeList.get(position).getEmpUUId());
                        bundle.putString("FNAME", allEmployeeList.get(position).getEmpFname());
                        bundle.putString("LNAME", allEmployeeList.get(position).getEmpLname());
                        bundle.putString("ADHAARNO", allEmployeeList.get(position).getEmpAdhaarNo());
                        bundle.putString("PHONENO", allEmployeeList.get(position).getEmpPhoneNo());



                        Constant.EMPLOYEE_ID = allEmployeeList.get(position).getEmpUUId();
                        Log.d("EMPLOYEE_ID_EMP_ADA", Constant.EMPLOYEE_ID);

                        employeeActivity.loadFragment(Constant.EMPLOYEE_VIEW_FRAGMENT, bundle);


                    }
                });
                break;


            case Constant.RV_EMPLOYEE_LIST_ATTENDANCE:
                holder.ll_mobile_aadhar.setVisibility(View.GONE);
                holder.tvEmpName.setText("Name : " + allEmployeeList.get(position).getEmpFname() + " " + allEmployeeList.get(position).getEmpLname());
                holder.tvGender.setText(allEmployeeList.get(position).getSex());
                holder.tvRole.setText(allEmployeeList.get(position).getRole());
                holder.tvDept.setText(allEmployeeList.get(position).getEmpDept());
                holder.tvStatus.setText(allEmployeeList.get(position).getEmployee_status());

                String image_Value1 = allEmployeeList.get(position).getEmployee_photo();

                if (!allEmployeeList.get(position).getEmployee_photo().isEmpty() ||
                        !allEmployeeList.get(position).getEmployee_photo().equals("")) {
                    Picasso.with(context)
                            .load(Constant.IMAGE_LINK + image_Value1)
                            // .error(R.drawable.user_icon)
                            .into(holder.studentImage);
                } else {
                    //  holder.studentImage.setBackgroundResource(R.drawable.user_icon);
                }

                holder.llEmployee.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AttendanceList employeeActivity = (AttendanceList) context;
                        Bundle bundle = new Bundle();
                        bundle.putString("EMPUUID", allEmployeeList.get(position).getEmpUUId());
                        bundle.putString("FNAME", allEmployeeList.get(position).getEmpFname());
                        bundle.putString("LNAME", allEmployeeList.get(position).getEmpLname());
                        bundle.putString("ADHAARNO", allEmployeeList.get(position).getEmpAdhaarNo());
                        bundle.putString("PHONENO", allEmployeeList.get(position).getEmpPhoneNo());

                        //  bundle.putString("Fragment_Type", "EMPLOYEE_DETAILS");
                        employeeActivity.loadFragment(Constant.ATTENDANCE_FRAGMENT, bundle);


                    }
                });
                break;


            case Constant.RV_LEAVE_INBOX_LIST_UPPER:

                holder.tv_applied_date.setText(leaveList.get(position).getAppliedDate());
                holder.tv_leaveType.setText(leaveList.get(position).getLeavetype());
                holder.tv_fromDate.setText(leaveList.get(position).getFrom_date());
                holder.tv_toDate.setText(leaveList.get(position).getToDate());
                holder.tv_status.setText(leaveList.get(position).getStatus());

                String subject = "";
                if (leaveList.get(position).getSubject().length() > 20) {
                    subject = leaveList.get(position).getSubject().substring(0, 20);
                    Log.d("SUBJECTEE", subject);
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
                        Bundle bundle;
                        switch (Constant.CLICK_BY) {
                            case "STAFF":
                                final EmployeeActivity leaveView = (EmployeeActivity) context;

                                bundle = new Bundle();
                                bundle.putString("leaveID", leaveList.get(position).getLeaveID());
                                bundle.putString("EMPUUID", leaveList.get(position).getEmpID());
                                bundle.putString("EMPLOYEE", "2");
                                Log.d("EMPUUID", leaveList.get(position).getEmpID());

                                leaveView.loadFragment(Constant.LEAVE_VIEW_FRAGMENT, bundle);
                                break;


                            case "STUDENT":
                                final StudentModuleActivity studentLeave = (StudentModuleActivity) context;

                                bundle = new Bundle();
                                bundle.putString("leaveID", leaveList.get(position).getLeaveID());
                                bundle.putString("EMPUUID", leaveList.get(position).getEmpID());
                                bundle.putString("EMPLOYEE", "3");
                                Log.d("EMPUUID", leaveList.get(position).getEmpID());

                                studentLeave.loadFragment(Constant.LEAVE_VIEW_FRAGMENT, bundle);
                                break;
                                default:

                        }


                    }
                });

                if (position == 0) {
                    holder.ll_leave_row.setBackgroundResource(R.color.trans_green);

                } else if (position % 2 != 0) {
                    holder.ll_leave_row.setBackgroundResource(R.color.white);

                }

                break;

            case Constant.RV_EMP_HOMEWORK_ROW:

                System.out.println("Sub***" + homeworkModelsInbox.get(position).getSubject());

                holder.tv_class.setText("Class: " + homeworkModelsInbox.get(position).getHomeClass());
                holder.tv_subject.setText("Subject: " + homeworkModelsInbox.get(position).getSubject());
                holder.tv_section.setText("Section: " + homeworkModelsInbox.get(position).getSection());
                holder.tv_work_title.setText("Homework Title: " + homeworkModelsInbox.get(position).getHomeworkTitle());
                holder.tv_description.setText("Description: " + homeworkModelsInbox.get(position).getDescription());
                holder.tv_assign_date.setText(homeworkModelsInbox.get(position).getStartDate());
                holder.tv_submit_date.setText(homeworkModelsInbox.get(position).getEndDate());
                //  }
                holder.rl_inbox_row.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        final EmployeeActivity employeeActivity = (EmployeeActivity) context;
                        Bundle bundle = new Bundle();

                        bundle.putString("HOMEWORK_UUID", homeworkModelsInbox.get(position).getHomeworkId());
                        bundle.putString("STAFF", "EMPLOYEE");
                        employeeActivity.loadFragment(Constant.HOMEWORK_VIEW_FRAGMENT, bundle);
                    }
                });

                break;
        }


    }

    @Override
    public int getItemCount() {
        switch (rvType) {
            case Constant.RV_EMPLOYEE_LIST:
                return allEmployeeList.size();

            case Constant.RV_EMPLOYEE_ATTENDANCE_LIST:
                return allEmployeeList.size();

            case Constant.RV_EMPLOYEE_LIST_ATTENDANCE:
                return allEmployeeList.size();

            case Constant.RV_LEAVE_INBOX_LIST_UPPER:
                return leaveList.size();

            case Constant.RV_EMP_HOMEWORK_ROW:
                return homeworkModelsInbox.size();
        }
        return 0;
    }
}
