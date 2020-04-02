package in.varadhismartek.patashalaerp.TeacherModule;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import in.varadhismartek.Utils.Constant;
import in.varadhismartek.patashalaerp.R;
import in.varadhismartek.patashalaerp.StudentModule.StudentModuleActivity;

public class TeacherHomeworkAdapter extends RecyclerView.Adapter<TeacherViewHolder>{
    Context context;
    String rvType;
    ArrayList<TeacherHomeworkModle> teacherHomeworkModles;



    public TeacherHomeworkAdapter(ArrayList<TeacherHomeworkModle> teacherHomeworkModles,
                                  Context context, String rvType) {
        this.teacherHomeworkModles = teacherHomeworkModles;
        this.context =context;
        this.rvType = rvType;

    }

    @NonNull
    @Override
    public TeacherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        switch (rvType) {

            case Constant.RV_TEACHER_HOMEWORK_ROW:
                return new TeacherViewHolder(LayoutInflater.from(context).inflate(R.layout.teacher_homework_row,parent,false));


            case Constant.RV_STUDENT_HOMEWORK_ROW:
                return new TeacherViewHolder(LayoutInflater.from(context).inflate(R.layout.teacher_homework_row,parent,false));


        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull TeacherViewHolder holder, final int position) {
        switch (rvType){
            case Constant.RV_TEACHER_HOMEWORK_ROW:
                holder.tvName.setText("Assigner Name : "+teacherHomeworkModles.get(position).getStudentFName()
                        +" "+teacherHomeworkModles.get(position).getStudentLName());
                holder.tvTitle.setText("Home work Title : " +teacherHomeworkModles.get(position).getHomeTitle());
                holder.tvDescription.setText("Description: " +teacherHomeworkModles.get(position).getHomeDescription());
                holder.tvClass.setText("Class : " +teacherHomeworkModles.get(position).getHomeClass());
                holder.tvSection.setText("Section : " +teacherHomeworkModles.get(position).getHomeSection());
                holder.tvStartDate.setText(teacherHomeworkModles.get(position).getStartDate());

                System.out.println("AssignId***"+teacherHomeworkModles.get(position).getAssignerID());
                String submitDate = teacherHomeworkModles.get(position).getStartDate();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");


                try {
                    Calendar c = Calendar.getInstance();
                    c.setTime(sdf.parse(submitDate));
                    c.add(Calendar.DATE, 1);
                    String convertDate = sdf.format(c.getTime());  // dt is now the new date
                    holder.tvEndDate.setText(convertDate);

                } catch (ParseException e) {
                    e.printStackTrace();
                }

                break;

            case Constant.RV_STUDENT_HOMEWORK_ROW:
                holder.tvName.setText("Assigner Name : "+teacherHomeworkModles.get(position).getAssigerFName()
                        +" "+teacherHomeworkModles.get(position).getAssignerLName());
                holder.tvTitle.setText("Home work Title : " +teacherHomeworkModles.get(position).getHomeTitle());
                holder.tvDescription.setText("Description: " +teacherHomeworkModles.get(position).getHomeDescription());
                holder.tvClass.setText("Class : " +teacherHomeworkModles.get(position).getHomeClass());
                holder.tvSection.setText("Section : " +teacherHomeworkModles.get(position).getHomeSection());
                holder.tvStartDate.setText(teacherHomeworkModles.get(position).getStartDate());

                System.out.println("AssignId***"+teacherHomeworkModles.get(position).getAssignerID());
                String submitDate1 = teacherHomeworkModles.get(position).getStartDate();
                SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");


                try {
                    Calendar c = Calendar.getInstance();
                    c.setTime(sdf1.parse(submitDate1));
                    c.add(Calendar.DATE, 1);
                    String convertDate = sdf1.format(c.getTime());  // dt is now the new date
                    holder.tvEndDate.setText(convertDate);

                } catch (ParseException e) {
                    e.printStackTrace();
                }

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
/*

                        strClass = teacherHomeworkModles.get(position).getHomeClass();
                        strSection = teacherHomeworkModles.get(position).getHomeSection();
                        strhomeTitle = teacherHomeworkModles.get(position).getHomeTitle();
                        strSubject = teacherHomeworkModles.get(position).getHomeSubject();
                        strFName = teacherHomeworkModles.get(position).getStudentFName();
                        strLName = teacherHomeworkModles.get(position).getStudentLName();
                        strDOB = teacherHomeworkModles.get(position).getDOB();
                        strbirthCertificate = teacherHomeworkModles.get(position).getBirthCertificate();
                        strPercentage = teacherHomeworkModles.get(position).getCompletePercentage();
                        strDesc = teacherHomeworkModles.get(position).getHomeDescription();
                        strStudentId = teacherHomeworkModles.get(position).getStudentId();
                        strHomeworkId = teacherHomeworkModles.get(position).getHomeworkID();
*/


                        Bundle bundle = new Bundle();

                        bundle.putString("strClass", teacherHomeworkModles.get(position).getHomeClass());
                        bundle.putString("strSection", teacherHomeworkModles.get(position).getHomeSection());
                        bundle.putString("strhomeTitle", teacherHomeworkModles.get(position).getHomeTitle());
                        bundle.putString("strSubject", teacherHomeworkModles.get(position).getHomeSubject());
                        bundle.putString("strFName", teacherHomeworkModles.get(position).getStudentFName());
                        bundle.putString("strLName", teacherHomeworkModles.get(position).getStudentLName());
                        bundle.putString("strDOB", teacherHomeworkModles.get(position).getDOB());
                        bundle.putString("strbirthCertificate", teacherHomeworkModles.get(position).getBirthCertificate());
                        bundle.putString("strPercentage", teacherHomeworkModles.get(position).getCompletePercentage());
                        bundle.putString("strDesc", teacherHomeworkModles.get(position).getHomeDescription());
                        bundle.putString("strStudentId", teacherHomeworkModles.get(position).getStudentId());
                        bundle.putString("strHomeworkId", teacherHomeworkModles.get(position).getHomeworkID());
                        bundle.putString("assignDate", teacherHomeworkModles.get(position).getAssignDate());
                        bundle.putString("reportDate", teacherHomeworkModles.get(position).getReportDate());
                        bundle.putString("reportNote", teacherHomeworkModles.get(position).getNote());

                        StudentModuleActivity studentModuleActivity =(StudentModuleActivity) context;
                        studentModuleActivity.loadFragment(Constant.STUDENT_HOMEWORK_VIEW, bundle);


                    }
                });

                break;
        }

    }

    @Override
    public int getItemCount() {
        switch (rvType){
            case Constant.RV_TEACHER_HOMEWORK_ROW:
                return teacherHomeworkModles.size();

            case Constant.RV_STUDENT_HOMEWORK_ROW:
                return teacherHomeworkModles.size();
        }
        return  0;

    }
}
