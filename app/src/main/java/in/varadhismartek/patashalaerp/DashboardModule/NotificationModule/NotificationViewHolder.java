package in.varadhismartek.patashalaerp.DashboardModule.NotificationModule;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import in.varadhismartek.patashalaerp.GeneralClass.CircleImageView;
import in.varadhismartek.patashalaerp.R;


class NotificationViewHolder extends RecyclerView.ViewHolder {

    //notification upper list inbox
    TextView tv_applied_date;
    RecyclerView recycler_view;

    //notification nested list inbox
    TextView tv_shortName, tv_message,tv_addedBy , tv_sendTo ,tv_status, tv_title;
    ImageView iv_status;
    LinearLayout ll_row;

    TextView tv_leavestype,tv_dayscount,tv_month,tv_pending;

    LinearLayout linearlayout_Scedule;

    CheckBox check_box_sub;

    //for search result
    CircleImageView civ_image;
    LinearLayout ll_search_row;
    TextView tv_name;
    LinearLayout ll_parent, ll_staff;
    TextView tv_class_name, tv_section_name, tv_dept_name, tv_role_name, tv_id, tv_mobile;

    public NotificationViewHolder(@NonNull View itemView) {
        super(itemView);

        //notification upper list inbox
        tv_applied_date = itemView.findViewById(R.id.tv_applied_date);
        recycler_view = itemView.findViewById(R.id.recycler_view);

        //notification nested list inbox
        tv_shortName = itemView.findViewById(R.id.tv_shortName);
        tv_message = itemView.findViewById(R.id.tv_message);
        tv_addedBy = itemView.findViewById(R.id.tv_addedBy);
        tv_sendTo = itemView.findViewById(R.id.tv_sendTo);
        iv_status = itemView.findViewById(R.id.iv_status);
        tv_status = itemView.findViewById(R.id.tv_status);
        ll_row = itemView.findViewById(R.id.ll_row);
        tv_title = itemView.findViewById(R.id.tv_title);


       // tv_leavestype = itemView.findViewById(R.id.tv_leavetype);

        check_box_sub = itemView.findViewById(R.id.check_box_sub);

        //for search result
        ll_search_row = itemView.findViewById(R.id.ll_search_row);
        tv_name = itemView.findViewById(R.id.tv_name);
        ll_parent = itemView.findViewById(R.id.ll_parent);
        ll_staff = itemView.findViewById(R.id.ll_staff);
        tv_class_name = itemView.findViewById(R.id.tv_class_name);
        tv_section_name = itemView.findViewById(R.id.tv_section_name);
        tv_dept_name = itemView.findViewById(R.id.tv_dept_name);
        tv_role_name = itemView.findViewById(R.id.tv_role_name);
        tv_id = itemView.findViewById(R.id.tv_id);
        tv_mobile = itemView.findViewById(R.id.tv_mobile);
        civ_image = itemView.findViewById(R.id.civ_image);
    }
}
