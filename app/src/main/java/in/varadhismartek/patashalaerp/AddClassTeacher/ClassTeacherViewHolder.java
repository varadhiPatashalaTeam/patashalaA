package in.varadhismartek.patashalaerp.AddClassTeacher;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import in.varadhismartek.patashalaerp.GeneralClass.CircleImageView;
import in.varadhismartek.patashalaerp.R;


public class ClassTeacherViewHolder extends RecyclerView.ViewHolder {

    CircleImageView civ_image;
    LinearLayout ll_search_row;
    TextView tv_name;
    LinearLayout ll_parent, ll_staff;
    TextView tv_class_name, tv_section_name, tv_dept_name, tv_role_name, tv_id, tv_mobile;

    TextView tv_classSection, tv_teacherName, tv_teacherId;
    ImageView iv_action;
    LinearLayout ll_row;

    public ClassTeacherViewHolder(@NonNull View itemView) {
        super(itemView);

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

        tv_classSection = itemView.findViewById(R.id.tv_classSection);
        tv_teacherName = itemView.findViewById(R.id.tv_teacherName);
        tv_teacherId = itemView.findViewById(R.id.tv_teacherId);
        iv_action = itemView.findViewById(R.id.iv_action);
        ll_row = itemView.findViewById(R.id.ll_row);
    }
}
