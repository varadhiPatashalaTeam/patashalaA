package in.varadhismartek.patashalaerp.DashboardModule.LeaveModule;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import in.varadhismartek.patashalaerp.GeneralClass.CircleImageView;
import in.varadhismartek.patashalaerp.R;


public class LeaveViewHolder extends RecyclerView.ViewHolder {

    CheckBox check_box;

    TextView tv_leavetype, tv_leaveCode, tv_leaveNo, tv_noticePeriod, tv_applyTimes;
    LinearLayout ll_barrier_row;

    //teacher inbox
    TextView tv_applied_date, tv_leaveType, tv_status, tv_fromDate, tv_toDate, tv_subject, tv_shortName;
    ImageView iv_status_circle;
    LinearLayout ll_leave_row;

    //statement
    TextView tv_totalLeave, tv_usedLeave, tv_leftLeave;

    //statement name
    LinearLayout ll_row;
    View view_color;

    // admin_leave_list
    RecyclerView recycler_view;

    // admin nested
    CircleImageView civ_image;
    TextView tv_name;


    public LeaveViewHolder(@NonNull View view) {
        super(view);

        check_box = view.findViewById(R.id.check_box);


        tv_leavetype = view.findViewById(R.id.tv_leavetype);
        tv_leaveCode = view.findViewById(R.id.tv_leaveCode);
        tv_leaveNo = view.findViewById(R.id.tv_leaveNo);
        tv_noticePeriod = view.findViewById(R.id.tv_noticePeriod);
        tv_applyTimes = view.findViewById(R.id.tv_applyTimes);
        ll_barrier_row = view.findViewById(R.id.ll_barrier_row);

        //teacher inbox
        tv_applied_date = view.findViewById(R.id.tv_applied_date);
        tv_leaveType = view.findViewById(R.id.tv_leaveType);
        tv_status = view.findViewById(R.id.tv_status);
        tv_fromDate = view.findViewById(R.id.tv_fromDate);
        tv_toDate = view.findViewById(R.id.tv_toDate);
         iv_status_circle = view.findViewById(R.id.iv_status_circle);
        tv_subject = view.findViewById(R.id.tv_subject);
        tv_shortName = view.findViewById(R.id.tv_shortName);
        ll_leave_row = view.findViewById(R.id.ll_leave_row);

        // statement
     /*   tv_totalLeave = view.findViewById(R.id.tv_totalLeave);
        tv_usedLeave = view.findViewById(R.id.tv_usedLeave);
        tv_leftLeave = view.findViewById(R.id.tv_leftLeave);
*/
        //statement name
        ll_row = view.findViewById(R.id.ll_row);
        view_color = view.findViewById(R.id.view_color);

        // admin_leave_list
        recycler_view = view.findViewById(R.id.recycler_view);

        // admin nested
        civ_image = view.findViewById(R.id.civ_image);
        tv_name = view.findViewById(R.id.tv_name);




    }
}
