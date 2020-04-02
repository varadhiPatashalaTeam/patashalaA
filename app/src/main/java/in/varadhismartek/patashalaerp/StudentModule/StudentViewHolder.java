package in.varadhismartek.patashalaerp.StudentModule;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import in.varadhismartek.patashalaerp.GeneralClass.CircleImageView;
import in.varadhismartek.patashalaerp.R;

public class StudentViewHolder extends RecyclerView.ViewHolder {
    public TextView tvStudentName,tvClass,tvSection,tvDOB,tvCertificate;
    public CircleImageView studentImage;
    public LinearLayout linearLayoutStudent;

    public  TextView leaveType,subject,toDate,fromDate,message;
    //StudentAttendance
    public TextView tvIdNumber,tvName,btnPresent,btnAbsent,tv_att_Class,tv_att_Section,tvStatus;
    public ImageView ivProfilePicture,ll_attendance;
    public  LinearLayout ll_present,ll_leave_row;
    public StudentViewHolder(View itemView) {
        super(itemView);
        tvStatus = itemView.findViewById(R.id.tvStatus);
        tvStudentName = itemView.findViewById(R.id.tv_name);
        tvClass = itemView.findViewById(R.id.tv_class);
        tvSection = itemView.findViewById(R.id.tv_section);
        tvDOB = itemView.findViewById(R.id.tv_dob);
        tvCertificate = itemView.findViewById(R.id.tv_certificateno);
        studentImage = itemView.findViewById(R.id.civEmpImage);
        linearLayoutStudent = itemView.findViewById(R.id.ll_student);

        leaveType= itemView.findViewById(R.id.leave_type);
        subject= itemView.findViewById(R.id.subject);
        toDate= itemView.findViewById(R.id.to_date);
        fromDate= itemView.findViewById(R.id.from_date);
        message= itemView.findViewById(R.id.message);

        tv_att_Class= itemView.findViewById(R.id.tv_att_Class);
        tv_att_Section= itemView.findViewById(R.id.tv_att_Section);
        ll_present= itemView.findViewById(R.id.ll_present);
        tvIdNumber= itemView.findViewById(R.id.tvIdNumber);
        tvName= itemView.findViewById(R.id.tvName);
        btnPresent= itemView.findViewById(R.id.btnPresent);
        btnAbsent= itemView.findViewById(R.id.btnAbsent);
        ll_attendance= itemView.findViewById(R.id.ll_attendance);
        ivProfilePicture= itemView.findViewById(R.id.ivProfilePicture);
        ll_leave_row= itemView.findViewById(R.id.ll_leave_row);


    }
}
