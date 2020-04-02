package in.varadhismartek.patashalaerp.DashboardModule.Assessment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import in.varadhismartek.patashalaerp.DashboardModule.DashboardActivity;
import in.varadhismartek.patashalaerp.R;

public class AssessmentModuleActivity extends AppCompatActivity implements View.OnClickListener {
    LinearLayout linearLayoutExam,linearLayoutCurricular,linearLayoutSport,ll_questionbank,ll_house,ll_schedule;
    ImageView iv_backBtn;
    TextView tvTitle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_module);
        linearLayoutExam = findViewById(R.id.ll_exam);
        linearLayoutCurricular = findViewById(R.id.ll_curricular);
        linearLayoutSport = findViewById(R.id.ll_sport);
        ll_questionbank = findViewById(R.id.ll_questionbank);
        ll_questionbank.setVisibility(View.GONE);
        ll_house = findViewById(R.id.ll_house);
        ll_schedule = findViewById(R.id.ll_schedule);
        iv_backBtn = findViewById(R.id.iv_backBtn);
        tvTitle = findViewById(R.id.tvTitle);
        tvTitle.setText("Assesment");
        ll_house.setVisibility(View.GONE);

        linearLayoutExam.setOnClickListener(this);
        linearLayoutCurricular.setOnClickListener(this);
        linearLayoutSport.setOnClickListener(this);
        ll_questionbank.setOnClickListener(this);
        ll_house.setOnClickListener(this);
        ll_schedule.setOnClickListener(this);
        iv_backBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.iv_backBtn:
                onBackPressed();
                break;
            case R.id.ll_exam:
                Intent intent = new Intent(AssessmentModuleActivity.this, ExamActivity.class);
                intent.putExtra("Assessement_Option","Exam");
                startActivity(intent);
                break;
            case R.id.ll_curricular:
                Intent intent_curricular = new Intent(AssessmentModuleActivity.this, ExamActivity.class);
                intent_curricular.putExtra("Assessement_Option","Curricular");
                startActivity(intent_curricular);
                break;
            case R.id.ll_sport:
                Intent intent_sport = new Intent(AssessmentModuleActivity.this, ExamActivity.class);
                intent_sport.putExtra("Assessement_Option","Sport");
                startActivity(intent_sport);
                break;
            case R.id.ll_questionbank:
                Intent intent_ques_bank = new Intent(AssessmentModuleActivity.this, ExamActivity.class);
                intent_ques_bank.putExtra("Assessement_Option","event");
                startActivity(intent_ques_bank);
                break;
            case R.id.ll_house:
                Intent intent_house = new Intent(AssessmentModuleActivity.this, ExamActivity.class);
                intent_house.putExtra("Assessement_Option","holiday");
                startActivity(intent_house);
                break;

                case R.id.ll_schedule:
                Intent intent_schedule = new Intent(AssessmentModuleActivity.this, ExamActivity.class);
                    intent_schedule.putExtra("Assessement_Option","schedule");
                startActivity(intent_schedule);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent= new Intent(AssessmentModuleActivity.this, DashboardActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
