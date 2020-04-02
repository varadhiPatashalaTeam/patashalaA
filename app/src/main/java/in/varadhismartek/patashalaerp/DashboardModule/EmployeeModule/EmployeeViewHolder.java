package in.varadhismartek.patashalaerp.DashboardModule.EmployeeModule;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import in.varadhismartek.patashalaerp.GeneralClass.CircleImageView;
import in.varadhismartek.patashalaerp.R;

public class EmployeeViewHolder extends RecyclerView.ViewHolder {
    public LinearLayout llEmployee;
    public CircleImageView studentImage;
    public TextView tvEmpName,tvRole,tvDept,tvStatus,tvGender,tvEmployeeName,tv_mobileNo,tv_AadharNo,tvEmpId;

    //teacher inbox
    TextView tv_applied_date, tv_leaveType, tv_status, tv_fromDate, tv_toDate, tv_subject, tv_shortName;
    ImageView iv_status_circle;
    LinearLayout ll_leave_row,ll_mobile_aadhar;
    RelativeLayout rl_inbox_row;
    TextView tv_class, tv_section,tv_subject_home, tv_work_title, tv_description, tv_assign_date, tv_submit_date;
    public EmployeeViewHolder(View itemView) {
        super(itemView);
        ll_mobile_aadhar =itemView.findViewById(R.id.ll_mobile_aadhar);
        tv_mobileNo =itemView.findViewById(R.id.tv_mobileNo);
        tv_AadharNo =itemView.findViewById(R.id.tv_AadharNo);
        tvEmpName =itemView.findViewById(R.id.tv_name);
        tvGender =itemView.findViewById(R.id.tv_gender);
        tvRole =itemView.findViewById(R.id.tv_roles);
        tvDept =itemView.findViewById(R.id.tv_dept);
        tvStatus =itemView.findViewById(R.id.tv_status);
        llEmployee =itemView.findViewById(R.id.ll_emp_row);
        tvEmployeeName =itemView.findViewById(R.id.empName);
        tvEmpId =itemView.findViewById(R.id.tvEmpId);

        studentImage = itemView.findViewById(R.id.civEmpImage);

        //teacher inbox
        tv_applied_date = itemView.findViewById(R.id.tv_applied_date);
        tv_leaveType = itemView.findViewById(R.id.tv_leaveType);
        tv_status = itemView.findViewById(R.id.tv_status);
        tv_fromDate = itemView.findViewById(R.id.tv_fromDate);
        tv_toDate = itemView.findViewById(R.id.tv_toDate);
        iv_status_circle = itemView.findViewById(R.id.iv_status_circle);
        tv_subject = itemView.findViewById(R.id.tv_subject);
        tv_shortName = itemView.findViewById(R.id.tv_shortName);
        ll_leave_row = itemView.findViewById(R.id.ll_leave_row);

        //homework
        tv_class       = itemView.findViewById(R.id.tv_class);
        tv_subject_home       = itemView.findViewById(R.id.tv_subject);
        tv_section     = itemView.findViewById(R.id.tv_section);
        tv_work_title  = itemView.findViewById(R.id.tv_work_title);
        tv_description = itemView.findViewById(R.id.tv_description);
        tv_assign_date = itemView.findViewById(R.id.tv_assign_date);
        tv_submit_date = itemView.findViewById(R.id.tv_submit_date);
        rl_inbox_row   = itemView.findViewById(R.id.rl_inbox_row);
    }
}
