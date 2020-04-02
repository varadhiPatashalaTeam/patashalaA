package in.varadhismartek.patashalaerp.DashboardModule.Assessment;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import in.varadhismartek.patashalaerp.R;


public class AssessmentViewHolder extends RecyclerView.ViewHolder {

    public LinearLayout ll_gradeRow;
    public TextView tv_minMarks, tv_maxMarks, tv_grade,tvsubCode;
    CheckBox check_box;
    public TextView tvExamType,tvDivision,tvClass,tvSubject,tvMin,tvMax,tvDuration,tv_subType;
    public TextView tvSportName,tvMaxPlayer,tvMentorNo,tvSupportPlayer,tvRoleName;


    public AssessmentViewHolder(@NonNull View itemView) {
        super(itemView);

        ll_gradeRow = itemView.findViewById(R.id.ll_gradeRow);
        tv_grade = itemView.findViewById(R.id.tv_grade);
        tv_minMarks = itemView.findViewById(R.id.tv_minMarks);
        tv_maxMarks = itemView.findViewById(R.id.tv_maxMarks);
        check_box = itemView.findViewById(R.id.check_box);

        tvExamType = itemView.findViewById(R.id.tv_examtype);
        tvDivision = itemView.findViewById(R.id.tv_division);
        tvClass = itemView.findViewById(R.id.tv_class);
        tvSubject = itemView.findViewById(R.id.tv_subject);
        tv_subType = itemView.findViewById(R.id.tv_subType);
        tvMin = itemView.findViewById(R.id.tv_min);
        tvMax = itemView.findViewById(R.id.tv_max);
        tvDuration = itemView.findViewById(R.id.tv_duration);

        tvSportName = itemView.findViewById(R.id.tv_sportname);
        tvMaxPlayer = itemView.findViewById(R.id.tv_maxplayer);
        tvMentorNo = itemView.findViewById(R.id.tv_mentorno);
        tvSupportPlayer = itemView.findViewById(R.id.tv_supportplayer);
        tvRoleName = itemView.findViewById(R.id.tv_rolename);
        tvsubCode = itemView.findViewById(R.id.tvsubCode);



    }
}
