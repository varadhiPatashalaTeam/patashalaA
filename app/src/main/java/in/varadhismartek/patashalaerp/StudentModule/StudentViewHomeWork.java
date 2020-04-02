package in.varadhismartek.patashalaerp.StudentModule;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import in.varadhismartek.Utils.Constant;
import in.varadhismartek.patashalaerp.R;
import in.varadhismartek.patashalaerp.Retrofit.APIService;
import in.varadhismartek.patashalaerp.Retrofit.ApiUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StudentViewHomeWork extends Fragment implements View.OnClickListener {
    String strHWClass = "", strHWSection = "", strHWhomeTitle = "", strHWSubject = "", strHWFName = "", strHWLName = "", strHWDOB = "",
            strHWbirthCertificate = "", strHWPercentage = "", strHWDesc = "", strHWStudentId = "", strHWHomeworkId = "", strAssignDate = "", strReportDate = "",strReportNote="";
    TextView tvSave;
    TextView tvAssignerName, tvHomeTitle, tvDesc, tvSubject, tvClass, tvAssignDate, tvReportDate,tvTitle;

    EditText edNote,edPercentage;
    APIService apiService;
    public StudentViewHomeWork() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_student_homework_view, container, false);

        initView(view);
        initListener();
        getStringBundle();
        return view;
    }

    private void initListener() {
        tvSave.setOnClickListener(this);
    }

    private void initView(View view) {

        apiService = ApiUtils.getAPIService();
        tvAssignerName = view.findViewById(R.id.tv_assignerName);
        tvHomeTitle = view.findViewById(R.id.tv_work_title);
        tvTitle = view.findViewById(R.id.tvTitle);
        tvDesc = view.findViewById(R.id.tv_description);
        tvClass = view.findViewById(R.id.tv_class);
        tvSubject = view.findViewById(R.id.tv_subject);
        tvAssignDate = view.findViewById(R.id.tv_fromDate);
        tvReportDate = view.findViewById(R.id.tv_toDate);
        tvSave = view.findViewById(R.id.tvSave);
        edPercentage = view.findViewById(R.id.ed_percentage);
        edNote = view.findViewById(R.id.ed_note);

        tvTitle.setText("Homework");
        tvSave.setVisibility(View.GONE);
    }

    private void getStringBundle() {
        Bundle bundle = getArguments();


        if (bundle != null) {
            strHWClass = bundle.getString("strClass");
            strHWSection = bundle.getString("strSection");
            strHWhomeTitle = bundle.getString("strhomeTitle");
            strHWSubject = bundle.getString("strSubject");
            strHWFName = bundle.getString("strFName");
            strHWLName = bundle.getString("strLName");
            strHWDOB = bundle.getString("strDOB");
            strHWbirthCertificate = bundle.getString("strbirthCertificate");
            strHWPercentage = bundle.getString("strPercentage");
            strHWDesc = bundle.getString("strDesc");
            strHWStudentId = bundle.getString("strStudentId");
            strHWHomeworkId = bundle.getString("strHomeworkId");
            strAssignDate = bundle.getString("assignDate");
            strReportDate = bundle.getString("reportDate");
            strReportNote = bundle.getString("reportNote");


            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
            String convertAssignDate = "", convertReportDate = "";

            try {
                Calendar c = Calendar.getInstance();
                Calendar c1 = Calendar.getInstance();
                c.setTime(sdf1.parse(strAssignDate));
                c1.setTime(sdf1.parse(strReportDate));
                c.add(Calendar.DATE, 1);
                c1.add(Calendar.DATE, 1);
                convertAssignDate = sdf1.format(c.getTime());  // dt is now the new date
                convertReportDate = sdf1.format(c.getTime());  // dt is now the new date

            } catch (ParseException e) {
                e.printStackTrace();
            }

            Log.i("HomeWork_UUID**", "" + strHWClass);
            tvAssignerName.setText(strHWFName + strHWLName);
            tvHomeTitle.setText(strHWhomeTitle);
            tvDesc.setText(strHWDesc);
            edPercentage.setText(strHWPercentage+ "% homework have completed");
            edNote.setText(strReportNote);
            edNote.setEnabled(false);
            edPercentage.setEnabled(false);

            tvSubject.setText("Subject : " + strHWSubject);
            tvClass.setText("Class : " + strHWClass);
            tvAssignDate.setText("Assigned Date :\n " + convertAssignDate);
            tvReportDate.setText("Report Date :\n" + convertReportDate);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvSave:
                if (edPercentage.getText().toString().isEmpty() || edNote.getText().toString().isEmpty()){
                    Toast.makeText(getActivity(),"Fill up all field",Toast.LENGTH_SHORT).show();
                }else {
                    studentHomeworkReport();
                }
                break;
        }
    }

    private void studentHomeworkReport() {
        apiService.submitStudentHomework(Constant.SCHOOL_ID,
                strHWClass,strHWSection, strHWSubject,edPercentage.getText().toString(),
                edNote.getText().toString(),strHWStudentId,strHWHomeworkId).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if (response.isSuccessful()){
                    Log.i("SAVE***",""+response.body());
                    Log.i("SAVE***",""+response.code());
                    Log.i("SAVE***",""+strHWStudentId);
                    Log.i("SAVE***",""+strHWHomeworkId);
                    Toast.makeText(getActivity(),"Report have submit successfully",Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {

            }
        });

    }
}
