package in.varadhismartek.patashalaerp.DashboardModule.Assessment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import in.varadhismartek.Utils.Constant;
import in.varadhismartek.patashalaerp.R;

public class ExamActivity extends AppCompatActivity implements View.OnClickListener {
    TextView tvExam, tvExamBarrriers, tv_grade, tv_sport, tv_sportBarrier;
    String Assessment_Type = "";
    LinearLayout linearLayoutSport, linearLayoutExam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam);

        linearLayoutExam = findViewById(R.id.ll_assessment_exam);
        linearLayoutSport = findViewById(R.id.ll_assessment_sport);

        linearLayoutExam.setVisibility(View.GONE);
        linearLayoutSport.setVisibility(View.GONE);

        tvExam = findViewById(R.id.tv_exam);
        tvExamBarrriers = findViewById(R.id.tv_examBarriers);
        tv_grade = findViewById(R.id.tv_grade);

        tv_sport = findViewById(R.id.tv_sport);
        tv_sportBarrier = findViewById(R.id.tv_sportBarrier);

        tv_grade.setOnClickListener(this);
        tvExam.setOnClickListener(this);
        tvExamBarrriers.setOnClickListener(this);

        tv_sport.setOnClickListener(this);
        tv_sportBarrier.setOnClickListener(this);

        Bundle b = getIntent().getExtras();
        if (b != null) {
            Assessment_Type = b.getString("Assessement_Option");
        }

        switch (Assessment_Type) {
            case "Exam":
                loadFragment(Constant.ADD_EXAMINATION, null);
                linearLayoutExam.setVisibility(View.VISIBLE);
                linearLayoutSport.setVisibility(View.GONE);
                break;

            case "Curricular":
                loadFragment(Constant.ADD_CURRICULAR, null);
                linearLayoutExam.setVisibility(View.GONE);
                linearLayoutSport.setVisibility(View.GONE);
                break;
            case "Sport":
                loadFragment(Constant.ADD_SPORT, null);
                linearLayoutExam.setVisibility(View.GONE);
                linearLayoutSport.setVisibility(View.VISIBLE);
                break;

            case "event":
                linearLayoutExam.setVisibility(View.GONE);
                linearLayoutSport.setVisibility(View.GONE);
                loadFragment(Constant.ADD_EVENT, null);
                break;
            case "holiday":
                linearLayoutExam.setVisibility(View.GONE);
                linearLayoutSport.setVisibility(View.GONE);
                //loadFragment(Constant.HOLIDAY_LIST, null);
                loadFragment(Constant.ADD_HOLIDAY, null);
                break;

            case "schedule":
                linearLayoutExam.setVisibility(View.GONE);
                linearLayoutSport.setVisibility(View.GONE);

                loadFragment(Constant.SCHEDULE_BARRIERS_LIST, null);
                //loadFragment(Constant.ADD_SCHEDULE_BARRIERS, null);
                break;

           /* case "House":
                linearLayoutExam.setVisibility(View.GONE);
                linearLayoutSport.setVisibility(View.GONE);
                loadFragment(Constant.ADD_HOUSES, null);
               // loadFragment(Constant.ADD_HOUSES_LIST, null);
                break;*/

        }

    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.tv_exam:
                tvExam.setBackgroundResource(R.color.green);
                tvExamBarrriers.setBackgroundResource(R.color.white);
                tv_grade.setBackgroundResource(R.color.white);
                tvExam.setTextColor(getResources().getColor(R.color.white));
                tvExamBarrriers.setTextColor(getResources().getColor(R.color.grey));
                tv_grade.setTextColor(getResources().getColor(R.color.grey));

                loadFragment(Constant.ADD_EXAMINATION, null);
                break;

            case R.id.tv_examBarriers:
                tvExam.setBackgroundResource(R.color.white);
                tvExamBarrriers.setBackgroundResource(R.color.green);
                tv_grade.setBackgroundResource(R.color.white);
                tvExam.setTextColor(getResources().getColor(R.color.grey));
                tvExamBarrriers.setTextColor(getResources().getColor(R.color.white));
                tv_grade.setTextColor(getResources().getColor(R.color.grey));

                loadFragment(Constant.EXAM_BARRIER_LIST, null);
                // loadFragment(Constant.ADD_EXAM_BARRIER, null);
                break;

            case R.id.tv_grade:
                tvExam.setBackgroundResource(R.color.white);
                tvExamBarrriers.setBackgroundResource(R.color.white);
                tv_grade.setBackgroundResource(R.color.green);
                tvExam.setTextColor(getResources().getColor(R.color.grey));
                tvExamBarrriers.setTextColor(getResources().getColor(R.color.grey));
                tv_grade.setTextColor(getResources().getColor(R.color.white));

                loadFragment(Constant.ASSESSMENT_GRADE, null);
                break;

            case R.id.tv_sport:
                tv_sport.setBackgroundResource(R.color.green);
                tv_sportBarrier.setBackgroundResource(R.color.white);
                tv_sport.setTextColor(getResources().getColor(R.color.white));
                tv_sportBarrier.setTextColor(getResources().getColor(R.color.grey));

                loadFragment(Constant.ADD_SPORT, null);
                break;

            case R.id.tv_sportBarrier:
                tv_sport.setBackgroundResource(R.color.white);
                tv_sportBarrier.setBackgroundResource(R.color.green);
                tv_sport.setTextColor(getResources().getColor(R.color.grey));
                tv_sportBarrier.setTextColor(getResources().getColor(R.color.white));

                loadFragment(Constant.ADD_SPORT_BARRIER, null);
                break;
        }
    }


    public void loadFragment(String fragmentString, Bundle bundle) {
        switch (fragmentString) {


            case Constant.ADD_EXAMINATION:
                callFragment(new Fragment_AddExamination(), Constant.ADD_EXAMINATION, null, null);
                break;

            case Constant.EXAM_BARRIER_LIST:
                callFragment(new Fragment_ExamBarriersList(), Constant.EXAM_BARRIER_LIST, null, null);
                break;

            case Constant.ADD_EXAM_BARRIER:
                callFragment(new Fragment_Exam_Barrier(), Constant.ADD_EXAM_BARRIER, null, null);
                break;

            case Constant.ASSESSMENT_GRADE:
                callFragment(new Fragment_AssessmentGrade(), Constant.ASSESSMENT_GRADE, null, null);
                break;

            case Constant.ADD_CURRICULAR:
                callFragment(new Fragment_Curricular(), Constant.ADD_CURRICULAR, null, null);
                break;


            case Constant.ADD_SPORT:
                callFragment(new Fragment_AddSport(), Constant.ADD_SPORT, null, null);
                break;

            case Constant.ADD_SPORT_BARRIER:
                callFragment(new Fragment_SportBarrier(), Constant.ADD_SPORT_BARRIER, null, null);
                break;

            case Constant.ADD_EVENT:
                callFragment(new Fragment_AddEvent(), Constant.ADD_EVENT, null, null);
                break;

            case Constant.ADD_HOLIDAY:
                callFragment(new Fragment_AddHoliday(), Constant.ADD_HOLIDAY, null, null);
                break;

            case Constant.HOLIDAY_LIST:
              //  callFragment(new Fragment_AddHoliday(), Constant.ADD_HOLIDAY, null, null);
                callFragment(new Fragment_HolidayList(), Constant.HOLIDAY_LIST, null, null);
                break;

            case Constant.SCHEDULE_BARRIERS_LIST:
                callFragment(new Fragment_ScheduleBarriersList(), Constant.SCHEDULE_BARRIERS_LIST, null, null);
                // callFragment(new Fragment_ScheduleBarriers(), Constant.ADD_SCHEDULE_BARRIERS, null, null);
                break;

            case Constant.ADD_SCHEDULE_BARRIERS:
                callFragment(new Fragment_ScheduleBarriers(), Constant.ADD_SCHEDULE_BARRIERS, null, null);
                break;

        }


    }


    private void callFragment(Fragment fragment, String tag, String addBackStack, Bundle bundle) {

        if (bundle != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .replace(R.id.container, fragment, tag)
                    .addToBackStack(addBackStack)
                    .commit();
            fragment.setArguments(bundle);

        } else {

            if (Constant.ADD_DIVISION_FRAGMENT.equals(tag)) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .replace(R.id.container, fragment, tag)
                        .commit();
            }

            getSupportFragmentManager().beginTransaction()
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .replace(R.id.container, fragment, tag)
                    .addToBackStack(addBackStack)
                    .commit();

        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(ExamActivity.this, AssessmentModuleActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


}
