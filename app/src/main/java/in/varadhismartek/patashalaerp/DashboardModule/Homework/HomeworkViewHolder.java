package in.varadhismartek.patashalaerp.DashboardModule.Homework;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import in.varadhismartek.patashalaerp.R;


public class HomeworkViewHolder extends RecyclerView.ViewHolder {

    public TextView tvName,tvNumber;
    CardView cardRow;
    CheckBox check_box;

    View view_color;
    RelativeLayout rl_inbox_row;
    TextView tv_class, tv_section,tv_subject, tv_work_title, tv_description, tv_assign_date, tv_submit_date;

    TextView tv_text,tvAddedDate,tvSubject;
    ImageView iv_attach,iv_cancel;
    public TextView tv_author,tv_pageno,tv_reference;
    public LinearLayout linearLayoutQuestion,ll_main_homeworkReport,ll_report_view;
    public RecyclerView recyclerView;
    // homework report
    TextView tvSlNo,tvHWStudentName,tvHWComplete,tvUrlLink;
    public HomeworkViewHolder(@NonNull View itemView) {
        super(itemView);

        tvName   = itemView.findViewById(R.id.tv_name);
        tvUrlLink   = itemView.findViewById(R.id.tvUrlLink);
        tvNumber = itemView.findViewById(R.id.tv_number);
        cardRow = itemView.findViewById(R.id.cardRow);
        check_box = itemView.findViewById(R.id.check_box);

        view_color     = itemView.findViewById(R.id.view_color);
        rl_inbox_row   = itemView.findViewById(R.id.rl_inbox_row);
        tv_class       = itemView.findViewById(R.id.tv_class);
        tv_subject       = itemView.findViewById(R.id.tv_subject);
        tv_section     = itemView.findViewById(R.id.tv_section);
        tv_work_title  = itemView.findViewById(R.id.tv_work_title);
        tv_description = itemView.findViewById(R.id.tv_description);
        tv_assign_date = itemView.findViewById(R.id.tv_assign_date);
        tv_submit_date = itemView.findViewById(R.id.tv_submit_date);
        tvAddedDate = itemView.findViewById(R.id.tv_added_date);
        tvSubject = itemView.findViewById(R.id.tv_subject);
        recyclerView = itemView.findViewById(R.id.recyclerView);
        linearLayoutQuestion = itemView.findViewById(R.id.ll_questionbank);
        ll_main_homeworkReport = itemView.findViewById(R.id.ll_main);
        // homework report
        tvSlNo = itemView.findViewById(R.id.slno);
        tvHWStudentName = itemView.findViewById(R.id.rep_stu_name);
        tvHWComplete = itemView.findViewById(R.id.rep_task_complete);
        ll_report_view = itemView.findViewById(R.id.ll_report_view);


        tv_text = itemView.findViewById(R.id.tv_text);
        tv_author = itemView.findViewById(R.id.tv_author);
        tv_pageno = itemView.findViewById(R.id.tv_pageno);
        tv_reference = itemView.findViewById(R.id.tv_reference);

        iv_attach = itemView.findViewById(R.id.iv_attach);
        iv_cancel = itemView.findViewById(R.id.iv_cancel);



    }
}
