package in.varadhismartek.patashalaerp.DashboardModule.Attendance;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import in.varadhismartek.patashalaerp.R;

public class AttendanceViewHolder extends RecyclerView.ViewHolder {
    public TextView tvName,tvLogOutTime,tvLoginTime,tvTotalTime,tvStatus,tv_shortName,tv_department;
    LinearLayout ll_leave_row,ll_department;
    public AttendanceViewHolder(View itemView) {
        super(itemView);
        tvName = itemView.findViewById(R.id.tv_name);
        tvLoginTime = itemView.findViewById(R.id.tv_inTime);
        tvLogOutTime = itemView.findViewById(R.id.tv_outtime);
        tvTotalTime = itemView.findViewById(R.id.tv_total_time);
        tvStatus = itemView.findViewById(R.id.tv_status);
        ll_leave_row = itemView.findViewById(R.id.ll_leave_row);
        tv_shortName = itemView.findViewById(R.id.tv_shortName);
        ll_department = itemView.findViewById(R.id.ll_department);
        tv_department = itemView.findViewById(R.id.tv_department);
    }
}
