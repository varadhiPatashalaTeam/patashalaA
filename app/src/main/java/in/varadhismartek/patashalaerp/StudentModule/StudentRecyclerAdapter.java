package in.varadhismartek.patashalaerp.StudentModule;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;

import in.varadhismartek.Utils.Constant;
import in.varadhismartek.patashalaerp.DashboardModule.DashBoardMenuActivity;
import in.varadhismartek.patashalaerp.R;
import in.varadhismartek.patashalaerp.Retrofit.APIService;
import in.varadhismartek.patashalaerp.Retrofit.ApiUtils;
import in.varadhismartek.patashalaerp.Retrofit.ApiUtilsPatashala;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StudentRecyclerAdapter extends RecyclerView.Adapter<StudentViewHolder> {
    private ArrayList<StudentModel> allstudentList ,studentLeaveList,studentAttendanceModels;
    private Context context;
    private String rvType;
    private String strAttendanceStatus="";
    APIService apiPatashalService;
    public StudentRecyclerAdapter(ArrayList<StudentModel> studentModels, Context context, String rvType) {
        this.allstudentList =studentModels;
        this.context = context;
        this.rvType =rvType;
        apiPatashalService = ApiUtils.getAPIService();


    }

    public StudentRecyclerAdapter(Context context, ArrayList<StudentModel> studentLeaveList, String rvType) {
        this.context =context;
        this.studentLeaveList= studentLeaveList;
        this.rvType =rvType;
        notifyDataSetChanged();
    }

    public StudentRecyclerAdapter(String rvType, Context context, ArrayList<StudentModel> studentAttendanceModels) {
        this.rvType =rvType;
        this.context =context;
        this.studentAttendanceModels= studentAttendanceModels;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (rvType){
            case Constant.RV_STUDENT_ALL_LIST:
                return new StudentViewHolder(LayoutInflater.from(context).inflate(R.layout.student_row,parent,false));

             case Constant.RV_ADD_STUDENT_ATTENDANCE:
                return new StudentViewHolder(LayoutInflater.from(context).inflate(R.layout.student_attendance_row,parent,false));

                case Constant.RV_STUDENT_LEAVE_LIST:
                return new StudentViewHolder(LayoutInflater.from(context).inflate(R.layout.student_leave_row,parent,false));

                case Constant.RV_ALL_STUDENT_LEAVE_LIST:
                return new StudentViewHolder(LayoutInflater.from(context).inflate(R.layout.student_leave_list_row,parent,false));

                case Constant.RV_STUDENT_ATTENDANCE_LIST:
                return new StudentViewHolder(LayoutInflater.from(context).inflate(R.layout.student_attendance_row, parent, false));

        }
        return null;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull final StudentViewHolder holder, final int position) {
        switch (rvType){
            case Constant.RV_STUDENT_ATTENDANCE_LIST:

                holder.tvIdNumber.setText(studentAttendanceModels.get(position).getStudent_id());
                holder.tvName.setText(studentAttendanceModels.get(position).getStudent_first_name()+ " "+
                        studentAttendanceModels.get(position).getStudent_last_name());
                String attendanceStatus= studentAttendanceModels.get(position).getAttStatus();
               Log.i("attendanceStatus",""+attendanceStatus);
                String stuImage = studentAttendanceModels.get(position).getPhoto();
                holder.tv_att_Section.setVisibility(View.GONE);
                holder.tv_att_Class.setVisibility(View.GONE);

                if (!studentAttendanceModels.get(position).getPhoto().isEmpty() ||
                        !studentAttendanceModels.get(position).getPhoto().equals("")){
                    Picasso.with(context)
                            .load(Constant.IMAGE_LINK+stuImage)
                            .error(R.drawable.user_icon)
                            .into(holder.ivProfilePicture);
                }
                else {
                    holder.ivProfilePicture.setBackgroundResource(R.drawable.user_icon);
                }
               switch (attendanceStatus){
                   case "Present":
                       holder.btnPresent.setBackgroundResource(R.color.green);
                       holder.ll_attendance.setBackgroundResource(R.color.green);
                       holder.btnPresent.setTextColor(Color.WHITE);
                       break;
                   case "Absent":
                       holder.btnAbsent.setBackgroundResource(R.color.red);
                       holder.ll_attendance.setBackgroundResource(R.color.red);
                       holder.btnAbsent.setTextColor(Color.WHITE);
                       break;
               }

                break;


            case Constant.RV_STUDENT_LEAVE_LIST:
                Log.i("attendanceStatus111",""+studentLeaveList.size());
                holder.leaveType.setText(studentLeaveList.get(position).getLeave_type());
                holder.subject.setText(studentLeaveList.get(position).getSubject());
                holder.toDate.setText(studentLeaveList.get(position).getTo_date());
                holder.fromDate.setText(studentLeaveList.get(position).getFrom_date());
                String strMessage= studentLeaveList.get(position).getMessage();
                if (strMessage.length()> 50) {
                    String subStr2 = strMessage.substring(0,40);
                    holder.message.setText(subStr2);
                }else {
                    holder.message.setText(studentLeaveList.get(position).getMessage());
                }



                break;
            case Constant.RV_ALL_STUDENT_LEAVE_LIST:

                holder.leaveType.setText(studentLeaveList.get(position).getLeave_type());
                holder.subject.setText(studentLeaveList.get(position).getSubject());
                holder.toDate.setText(studentLeaveList.get(position).getTo_date());
                holder.fromDate.setText(studentLeaveList.get(position).getFrom_date());
                holder.message.setText(studentLeaveList.get(position).getMessage());
                holder.tvName.setText(studentLeaveList.get(position).getStudent_first_name());
                holder.tv_att_Class.setText(studentLeaveList.get(position).getStrClass()+"("+studentLeaveList.get(position).getStrSection()+")");

                String strMessage1= studentLeaveList.get(position).getMessage();
                if (strMessage1.length()> 50) {
                    String subStr2 = strMessage1.substring(0,40);
                    holder.message.setText(subStr2);
                }else {
                    holder.message.setText(studentLeaveList.get(position).getMessage());
                }


                if (position == 0) {
                    holder.ll_leave_row.setBackgroundResource(R.color.lightColorRow);

                } else if (position % 2 != 0) {
                    holder.ll_leave_row.setBackgroundResource(R.color.white);

                } else {
                    holder.ll_leave_row.setBackgroundResource(R.color.lightColorRow);

                }


                holder.ll_leave_row.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        final Dialog dialog = new Dialog(context);
                        dialog.setContentView(R.layout.dialog_teacher_view_student);
                        dialog.getWindow().setBackgroundDrawableResource(R.drawable.flag_transparent);
                        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        dialog.show();

                        TextView tv_leaveType = dialog.findViewById(R.id.tv_leaveType);
                        TextView tv_message = dialog.findViewById(R.id.tv_message);
                        TextView tv_firstName = dialog.findViewById(R.id.tv_firstName);
                        TextView tv_class = dialog.findViewById(R.id.tv_class);
                        TextView tv_section = dialog.findViewById(R.id.tv_section);
                        TextView tv_fromDate = dialog.findViewById(R.id.tv_fromDate);
                        TextView tv_toDate = dialog.findViewById(R.id.tv_toDate);
                        TextView tv_subject = dialog.findViewById(R.id.tv_subject);
                        Button bt_close = dialog.findViewById(R.id.bt_close);

                        tv_leaveType.setText(studentLeaveList.get(position).getLeave_type());
                        tv_message.setText(studentLeaveList.get(position).getMessage());
                        tv_firstName.setText(studentLeaveList.get(position).getStudent_first_name());
                        tv_class.setText(studentLeaveList.get(position).getStrClass());
                        tv_section.setText(studentLeaveList.get(position).getStrSection());
                        tv_fromDate.setText(studentLeaveList.get(position).getFrom_date());
                        tv_toDate.setText(studentLeaveList.get(position).getTo_date());
                        tv_subject.setText(studentLeaveList.get(position).getSubject());


                        bt_close.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                            }
                        });


                    }
                });


                break;



            case Constant.RV_ADD_STUDENT_ATTENDANCE:

                holder.tv_att_Section.setVisibility(View.VISIBLE);
                holder.tv_att_Class.setVisibility(View.VISIBLE);

                holder.tvName.setText(allstudentList.get(position).getStrFirstName() +" "+allstudentList.get(position).getStrLastName());
                holder.tv_att_Class.setText("Class : "+allstudentList.get(position).getStrClass());
                holder.tv_att_Section.setText("Section : "+allstudentList.get(position).getStrSection());

                String att_stuImage = allstudentList.get(position).getStrPhoto();

                if (!allstudentList.get(position).getStrPhoto().isEmpty() ||
                        !allstudentList.get(position).getStrPhoto().equals("")){
                    Picasso.with(context)
                            .load(Constant.IMAGE_LINK+att_stuImage)
                            .error(R.drawable.user_icon)
                            .into(holder.ivProfilePicture);
                }
                else {
//                    holder.studentImage.setBackgroundResource(R.drawable.user_icon);
                }

                holder.btnPresent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        strAttendanceStatus ="Present";

                        holder.btnPresent.setBackgroundResource(R.color.green);
                        holder.btnPresent.setTextColor(Color.WHITE);
                        holder.btnAbsent.setBackgroundResource(R.color.trans_gray);
                        holder.btnAbsent.setTextColor(Color.GRAY);


                        String strStudentId = allstudentList.get(position).getStrStudentID();
                        String strClass = allstudentList.get(position).getStrClass();
                        String strSection = allstudentList.get(position).getStrSection();
                        String strDivsion = allstudentList.get(position).getStrDivision();
                        String strDate = allstudentList.get(position).getStrCustomId();
                        String strSess_serialNo = allstudentList.get(position).getSession_serial_no();

                        String strAcademicStartDate = allstudentList.get(position).getAcdaemicFromDate();
                        String strAcademicEndDate = allstudentList.get(position).getAcdaemicToDate();
                        String strSessionFromDate = allstudentList.get(position).getSessionFromDate();
                        String strSessionToDate = allstudentList.get(position).getSessionToDate();

                        Log.i("Attend**",""+strStudentId+"**"+strDivsion+"**"+strClass+"**"+strSection+"**"+strDate);
                        Log.i("Attend**2",""+strAttendanceStatus+"**"+strSess_serialNo+"**"+
                                strAcademicStartDate+"**" +strAcademicEndDate+"**"+strSessionFromDate+"**"+
                                strSessionToDate.trim());


                        apiPatashalService.addStudentAttendance(
                                Constant.SCHOOL_ID,strStudentId.trim(),strDivsion.trim(),strClass.trim(),strSection.trim(),strDate.trim()
                                ,strAttendanceStatus,strSess_serialNo,strAcademicStartDate.trim(),strAcademicEndDate.trim(),
                                strSessionFromDate.trim(),strSessionToDate.trim(),Constant.EMPLOYEE_BY_ID
                        ).enqueue(new Callback<Object>() {

                            @Override
                            public void onResponse(Call<Object> call, Response<Object> response) {

                                if (response.isSuccessful()){

                                    Toast.makeText(context,"Student attendance added successfully",Toast.LENGTH_SHORT).show();
                                } else {
                                    try {
                                        assert response.errorBody() != null;
                                        JSONObject object = new JSONObject(response.errorBody().string());
                                        String message = object.getString("message");
                                        Log.d("create_API", message);
                                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<Object> call, Throwable t) {


                            }
                        });


                    }
                });


                holder.btnAbsent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        strAttendanceStatus ="Absent";
                        holder.btnAbsent.setBackgroundResource(R.color.red);
                        holder.btnAbsent.setTextColor(Color.WHITE);
                        holder.btnPresent.setBackgroundResource(R.color.trans_gray);
                        holder.btnPresent.setTextColor(Color.GRAY);

                        String strStudentId = allstudentList.get(position).getStrStudentID();
                        String strClass = allstudentList.get(position).getStrClass();
                        String strSection = allstudentList.get(position).getStrSection();
                        String strDivsion = allstudentList.get(position).getStrDivision();
                        String strDate = allstudentList.get(position).getStrCustomId();
                        String strSess_serialNo = allstudentList.get(position).getSession_serial_no();

                        String strAcademicStartDate = allstudentList.get(position).getAcdaemicFromDate();
                        String strAcademicEndDate = allstudentList.get(position).getAcdaemicToDate();
                        String strSessionFromDate = allstudentList.get(position).getSessionFromDate();
                        String strSessionToDate = allstudentList.get(position).getSessionToDate();

                        apiPatashalService.addStudentAttendance(
                                Constant.SCHOOL_ID,strStudentId.trim(),strDivsion.trim(),strClass.trim(),strSection.trim(),
                                strDate.trim(),strAttendanceStatus,strSess_serialNo,strAcademicStartDate.trim(),
                                strAcademicEndDate.trim(), strSessionFromDate.trim(),strSessionToDate.trim(),
                                Constant.EMPLOYEE_BY_ID).enqueue(new Callback<Object>() {

                            @Override
                            public void onResponse(Call<Object> call, Response<Object> response) {

                                if (response.isSuccessful()){

                                    Toast.makeText(context,"Student attendance added successfully",Toast.LENGTH_SHORT).show();
                                } else {
                                    try {
                                        assert response.errorBody() != null;
                                        JSONObject object = new JSONObject(response.errorBody().string());
                                        String message = object.getString("message");
                                        Log.d("create_API", message);
                                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<Object> call, Throwable t) {

                            }
                        });
                    }
                });
                break;



            case Constant.RV_STUDENT_ALL_LIST:
                holder.tvStatus.setVisibility(View.VISIBLE);
                holder.tvStudentName.setText("Name: "+allstudentList.get(position).getStrFirstName() +" "+allstudentList.get(position).getStrLastName());
                holder.tvStatus.setText(allstudentList.get(position).getStrStatus());
                holder.tvClass.setText("Class : "+allstudentList.get(position).getStrClass());
                holder.tvSection.setText("Section : "+allstudentList.get(position).getStrSection());
                holder.tvDOB.setText("DOB : "+allstudentList.get(position).getStrDOB());
                holder.tvCertificate.setText("Certificate No : "+allstudentList.get(position).getStrCertificateNo());
                String image_Value = allstudentList.get(position).getStrPhoto();

                if (!allstudentList.get(position).getStrPhoto().isEmpty() ||
                        !allstudentList.get(position).getStrPhoto().equals("")){
                    Picasso.with(context)
                            .load(Constant.IMAGE_LINK+image_Value)
                           // .error(R.drawable.ic_account)
                            .into(holder.studentImage);
                }
                else {
                    holder.studentImage.setBackgroundResource(R.drawable.ic_account);
                }


                holder.linearLayoutStudent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        StudentModuleActivity studentModuleActivity = (StudentModuleActivity)context;

                        Bundle bundle = new Bundle();
                        bundle.putString("FName", allstudentList.get(position).getStrFirstName());
                        bundle.putString("LName", allstudentList.get(position).getStrLastName());
                        bundle.putString("DOB", allstudentList.get(position).getStrDOB());
                        bundle.putString("CertificateNo", allstudentList.get(position).getStrCertificateNo());
                        bundle.putString("EEUID", allstudentList.get(position).getStrStudentID());

                        Log.i("STUDENTID***","SLF  "+allstudentList.get(position).getStrStudentID());

                        studentModuleActivity.loadFragment(Constant.STUDENT_DETAILS, bundle);


                    }
                });

                break;
        }
    }

    @Override
    public int getItemCount() {
        switch (rvType){
            case Constant.RV_STUDENT_ALL_LIST:
               return allstudentList.size();

               case Constant.RV_ADD_STUDENT_ATTENDANCE:
               return allstudentList.size();

               case Constant.RV_STUDENT_LEAVE_LIST:
               return studentLeaveList.size();

               case Constant.RV_ALL_STUDENT_LEAVE_LIST:
               return studentLeaveList.size();

               case Constant.RV_STUDENT_ATTENDANCE_LIST:
               return studentAttendanceModels.size();
        }
        return 0;
    }
}
