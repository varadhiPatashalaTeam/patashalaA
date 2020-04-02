package in.varadhismartek.patashalaerp.DashboardModule.Attendance;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import in.varadhismartek.Utils.Constant;
import in.varadhismartek.patashalaerp.DashboardModule.AttendanceList;
import in.varadhismartek.patashalaerp.DashboardModule.DashBoardMenuActivity;
import in.varadhismartek.patashalaerp.DashboardModule.EmployeeModule.EmployeeActivity;
import in.varadhismartek.patashalaerp.R;

public class AttendanceAdapter extends RecyclerView.Adapter<AttendanceViewHolder> {
    Context context;
    String rvType;
    ArrayList<EmpAttendanceModel> empAttendanceModels;

    public AttendanceAdapter(Context context, ArrayList<EmpAttendanceModel> empAttendanceModels, String rvType) {
        this.context = context;
        this.empAttendanceModels = empAttendanceModels;
        this.rvType = rvType;
    }

    @NonNull
    @Override
    public AttendanceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (rvType) {
            case Constant.RV_ADMIN_ATTENDANCE_LIST:
                return new AttendanceViewHolder(LayoutInflater.from(context)
                        .inflate(R.layout.attendance_inbox_row, parent, false));

            case Constant.RV_ADMIN_ATTENDANCE_LIST_STAFF:
                return new AttendanceViewHolder(LayoutInflater.from(context)
                        .inflate(R.layout.attendance_inbox_row, parent, false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull AttendanceViewHolder holder, final int position) {
        switch (rvType) {
            case Constant.RV_ADMIN_ATTENDANCE_LIST:
                holder.ll_department.setVisibility(View.GONE);
                holder.tvName.setText(empAttendanceModels.get(position).getEmployee_name());
                holder.tvTotalTime.setText(empAttendanceModels.get(position).getTotal_time());
                holder.tvLoginTime.setText(empAttendanceModels.get(position).getLogIn_time());
                holder.tvLogOutTime.setText(empAttendanceModels.get(position).getLogOut_time());
                holder.tvStatus.setText(empAttendanceModels.get(position).getEmpStatus());

                Log.i("EmpName**de1", "" + empAttendanceModels.get(position).getEmployee_department());

                holder.ll_leave_row.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DashBoardMenuActivity dashboard = (DashBoardMenuActivity) context;
                        String empID = empAttendanceModels.get(position).getEmployee_id();
                        String empName = empAttendanceModels.get(position).getEmployee_name();
                        String subStr[] = empName.split(" ");
                        String fName = subStr[0];
                        String lName = subStr[1];

                        System.out.println("fName**" + fName + "**" + lName);
                        Bundle bundle = new Bundle();
                        bundle.putString("EMPUUID", empID);
                        bundle.putString("FNAME", fName);
                        bundle.putString("LNAME", lName);

                        dashboard.loadFragment(Constant.ATTENDANCE_FRAGMENT, bundle);
                    }
                });
                break;

            case Constant.RV_ADMIN_ATTENDANCE_LIST_STAFF:

               if (!empAttendanceModels.get(position).getEmployee_department().isEmpty()){
                   holder.ll_department.setVisibility(View.VISIBLE);
                   holder.tv_department.setText(empAttendanceModels.get(position).getEmployee_department());

               }else {
                   holder.ll_department.setVisibility(View.GONE);

               }
                holder.tvName.setText(empAttendanceModels.get(position).getEmployee_name());

                holder.tvTotalTime.setText(empAttendanceModels.get(position).getTotal_time());
                holder.tvLoginTime.setText(empAttendanceModels.get(position).getLogIn_time());
                holder.tvLogOutTime.setText(empAttendanceModels.get(position).getLogOut_time());
                holder.tvStatus.setText(empAttendanceModels.get(position).getEmpStatus());
                String EmpName = empAttendanceModels.get(position).getEmployee_name();

                Log.i("EmpName**de", "" + empAttendanceModels.get(position).getEmployee_department());
                Log.i("EmpName**", "" + EmpName);
                String[] title = EmpName.split(" ");
                Log.i("EmpName**title", "" + title.length);

                if (title.length == 1) {
                    char a = title[0].charAt(0);
                    holder.tv_shortName.setText(String.valueOf(a));

                } else if (title.length == 2) {
                    char a = title[0].charAt(0);
                    char b = title[1].charAt(0);
                    Log.d("CHAR", String.valueOf(a));
                    holder.tv_shortName.setText(String.valueOf(a) + b);
                } else {
                    holder.tv_shortName.setText("");
                }


                holder.ll_leave_row.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AttendanceList dashboard = (AttendanceList) context;
                        Constant.EMPLOYEE_ID = empAttendanceModels.get(position).getEmployee_id();
                        String empID = empAttendanceModels.get(position).getEmployee_id();
                        String empName = empAttendanceModels.get(position).getEmployee_name();
                        String subStr[] = empName.split(" ");
                        String fName = subStr[0];
                        String lName = subStr[1];

                        System.out.println("fName**" + fName + "**" + lName);
                        Bundle bundle = new Bundle();
                        bundle.putString("EMPUUID", empID);
                        bundle.putString("FNAME", fName);
                        bundle.putString("LNAME", lName);

                        dashboard.loadFragment(Constant.ATTENDANCE_FRAGMENT, bundle);
                    }
                });
                break;
        }
    }

    @Override
    public int getItemCount() {
        switch (rvType) {
            case Constant.RV_ADMIN_ATTENDANCE_LIST:
                return empAttendanceModels.size();
            case Constant.RV_ADMIN_ATTENDANCE_LIST_STAFF:
                return empAttendanceModels.size();
        }
        return 0;

    }
}
