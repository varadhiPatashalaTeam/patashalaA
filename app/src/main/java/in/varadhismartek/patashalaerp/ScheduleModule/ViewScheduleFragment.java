package in.varadhismartek.patashalaerp.ScheduleModule;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import in.varadhismartek.Utils.Constant;
import in.varadhismartek.patashalaerp.DashboardModule.Homework.MyModel;
import in.varadhismartek.patashalaerp.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class ViewScheduleFragment extends Fragment implements View.OnClickListener {

    ImageView iv_backBtn, iv_attachment;
    TextView tv_event_title, tv_event_by, tv_date, tv_time, tv_timing, tv_fromDate, tv_toDate,tv_subject,
            tv_message, tv_title, tv_division,tv_class,tv_section;
    LinearLayout ll_timing, ll_add_attachment;
    FloatingActionButton fab_button;

    RecyclerView rv_exam_schedule;
    TextView tv_schedule;

    ArrayList<String> list;
    MyScheduleModel myScheduleModel;
    ArrayList<MyNewModel> myList;

    String scheduleType = "";

    public ViewScheduleFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_view_schedule, container, false);

        initViews(view);
        initListeners();
        getExatrBundle();

        //setRecyclerView();

        return view;
    }

    private void getExatrBundle() {

        Bundle bundle = getArguments();
        myScheduleModel = (MyScheduleModel) bundle.getSerializable("myScheduleList");

        scheduleType = myScheduleModel.getScheduleType();

        myList = myScheduleModel.getMyList();
        Log.d("myList",myList.size()+"");

        switch (scheduleType){
            case "Exam":
                setRecyclerViewExam();
                tv_schedule.setText("Exam Schedule");
                tv_message.setText(myScheduleModel.getMyList().get(0).getScheduleModelList().get(0).getMessage());

                break;

            case "Event":
                setRecyclerViewEvent();
                tv_schedule.setText("Event Schedule");
                tv_message.setText(myScheduleModel.getMyList().get(0).getMessage());

                break;

            case "Holiday":
                setRecyclerViewHoliday();
                tv_schedule.setText("Holiday Schedule");
                tv_message.setText(myScheduleModel.getMyList().get(0).getMessage());

                break;
        }

        tv_event_title.setText(myScheduleModel.getExamName());
        tv_fromDate.setText(myScheduleModel.getFrom_date());
        tv_date.setText(myScheduleModel.getTo_date());
        tv_toDate.setText(myScheduleModel.getTo_date());
        tv_division.setText(myScheduleModel.getDivisionName());






    }

    @SuppressLint("SetTextI18n")
    private void setStrings(ScheduleModel scheduleModel) {

        assert scheduleModel!=null;

        tv_time.setText(scheduleModel.getTime());
        tv_title.setText("Title:  "+scheduleModel.getEventTitle());
        tv_class.setText(scheduleModel.getClassess());
        tv_section.setText(scheduleModel.getSection());
        tv_event_by.setText(scheduleModel.getAddedFirstName());
        tv_subject.setText("Subject:  "+scheduleModel.getSubject());


        if (!scheduleModel.getImage().equalsIgnoreCase("")){
            Glide.with(getActivity()).load(Constant.IMAGE_URL+scheduleModel.getImage()).into(iv_attachment);
        }

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat monthFormat = new SimpleDateFormat("dd-MMM-yyyy");

        switch (scheduleModel.getScheduleType()){

            case "Exam":
                ll_timing.setVisibility(View.VISIBLE);
                ll_add_attachment.setVisibility(View.GONE);
                rv_exam_schedule.setVisibility(View.VISIBLE);
                tv_subject.setVisibility(View.VISIBLE);

                break;

            case "Event":
                ll_timing.setVisibility(View.VISIBLE);
                ll_add_attachment.setVisibility(View.VISIBLE);
                rv_exam_schedule.setVisibility(View.GONE);
                tv_title.setVisibility(View.VISIBLE);

                break;

            case "Holiday":
                ll_timing.setVisibility(View.GONE);
                ll_add_attachment.setVisibility(View.VISIBLE);
                rv_exam_schedule.setVisibility(View.GONE);

                break;
        }
    }

    private void setRecyclerViewExam() {
        ScheduleAdapter adapter = new ScheduleAdapter(getActivity(), Constant.RV_SCHEDULE_VIEW_EXAM_ROW, myList);
        rv_exam_schedule.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv_exam_schedule.setAdapter(adapter);
    }

    private void setRecyclerViewHoliday() {
        ScheduleAdapter adapter = new ScheduleAdapter(getActivity(), Constant.RV_HOLIDAY_NESTED_LIST, myList);
        rv_exam_schedule.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        rv_exam_schedule.setAdapter(adapter);
    }

    private void setRecyclerViewEvent() {
        ScheduleAdapter adapter = new ScheduleAdapter(getActivity(), Constant.RV_EVENT_NESTED_LIST, myList);
        rv_exam_schedule.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        rv_exam_schedule.setAdapter(adapter);
    }

    private void initListeners() {
        iv_backBtn.setOnClickListener(this);
        fab_button.setOnClickListener(this);

    }

    private void initViews(View view) {
        iv_backBtn     = view.findViewById(R.id.iv_backBtn);
        fab_button     = view.findViewById(R.id.fab_button);
        iv_attachment  = view.findViewById(R.id.iv_attachment);
        tv_event_title = view.findViewById(R.id.tv_event_title);
        tv_event_by    = view.findViewById(R.id.tv_event_by);
        tv_date        = view.findViewById(R.id.tv_date);
        tv_time        = view.findViewById(R.id.tv_time);
        tv_timing      = view.findViewById(R.id.tv_timing);
        tv_fromDate    = view.findViewById(R.id.tv_fromDate);
        tv_toDate      = view.findViewById(R.id.tv_toDate);
        tv_message      = view.findViewById(R.id.tv_message);
        tv_title      = view.findViewById(R.id.tv_title);
        tv_division      = view.findViewById(R.id.tv_division);
        tv_class      = view.findViewById(R.id.tv_class);
        tv_section      = view.findViewById(R.id.tv_section);
        tv_subject      = view.findViewById(R.id.tv_subject);

        tv_schedule      = view.findViewById(R.id.tv_schedule);
        rv_exam_schedule = view.findViewById(R.id.rv_exam_schedule);


        ll_add_attachment    = view.findViewById(R.id.ll_add_attachment);
        ll_timing    = view.findViewById(R.id.ll_timing);

        list = new ArrayList<>();

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.iv_backBtn:
                getActivity().onBackPressed();
                break;


        }
    }
}