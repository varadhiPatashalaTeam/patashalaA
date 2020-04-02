package in.varadhismartek.patashalaerp.DashboardModule;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import in.varadhismartek.Utils.Constant;
import in.varadhismartek.Utils.Utilities;
import in.varadhismartek.patashalaerp.BarriersPage;
import in.varadhismartek.patashalaerp.Birthday.BirthdayActivity;
import in.varadhismartek.patashalaerp.DashboardModule.NoticeBoard.NoticeBoardActivity;
import in.varadhismartek.patashalaerp.DashboardModule.TransportModule.TransportActivity;
import in.varadhismartek.patashalaerp.DashboardModule.TransportModule.TransportLandingActivity;
import in.varadhismartek.patashalaerp.DashboardModule.UpdateAdapter.FragmentDasboardAdapter;
import in.varadhismartek.patashalaerp.DashboardModule.extraUtils.ModuleModel;
import in.varadhismartek.patashalaerp.GalleryModule.GalleryActivity;
import in.varadhismartek.patashalaerp.GeneralClass.CircleImageView;
import in.varadhismartek.patashalaerp.GeneralClass.Mpin;
import in.varadhismartek.patashalaerp.GeneralClass.RingProgressBar;
import in.varadhismartek.patashalaerp.LoginAndRegistation.LoginScreenActivity;
import in.varadhismartek.patashalaerp.PieChart.ChartData;
import in.varadhismartek.patashalaerp.PieChart.PieChart;
import in.varadhismartek.patashalaerp.R;
import in.varadhismartek.patashalaerp.Retrofit.APIService;
import in.varadhismartek.patashalaerp.Retrofit.ApiUtils;
import in.varadhismartek.patashalaerp.Retrofit.ApiUtilsPatashala;
import in.varadhismartek.patashalaerp.ScheduleModule.ScheduleActivity;
import in.varadhismartek.patashalaerp.TicketRaise.StaffTicketActivity;
import in.varadhismartek.patashalaerp.Visitors.VisitiorsActivity;
import in.varadhismartek.patashalaerp.bottomnavigation.BottomNavigationThreeActivity;
import in.varadhismartek.patashalaerp.bottomnavigation.BottomNavigationTwoActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Fragment_Dashboard extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    Context context;

    APIService mApiService;
    ArrayList<String> name = new ArrayList<>();
    ArrayList<String> nameModule = new ArrayList<>();
    ImageView ivLogout, editSetting;
    CircleImageView iv_schoolLogo;
    boolean module_status = false;
    RecyclerView recyclerView;
    LinearLayout linearLayout;
    private List<ModuleModel> moduleList= new ArrayList<>();
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    CircleImageView ivSlideImage;

    LinearLayout llFirstItem,llSecondItem,llThirdItem,llForthItem,ll_pieChartFees;

    RingProgressBar ringProgressVisitor,ringProgressTicketOne,ringProgressTicketTwo,ringProgressTicketThree;
    int i = 0,k=0,j=0;

    WebView wbUrlOne,wbUrlTwo,wbUrlThree,wbUrlFour;


    // for otp
    Mpin mpin;
    ImageView iv_logo, iv_cancel,ivRefresh;
    Dialog dialog;
    Button btnOkOtp, btnResendOtp;
    TextView tv_OtpMobileNo;
    Utilities utilities;
    private String POC_Mobile_Number;
    ProgressDialog progressDialog;

    APIService mApiservicePatashala;
    ArrayList<String> imageList;
    DashBoardGalleryAdapter mAdapter;

    RecyclerView rcvDashboardGallery,rcvNoticeBoard,rcvDashboardBusDetails,rcvBirthday,rcvDashboardEventSchedule;
    private RecyclerView.LayoutManager mLayoutManager;
    String ringTag;
    int scrollCount = 0;
    ImageView ivHome,ivChat,ivNotification,ivBarriers;
    TextView tvHome,tvChat,tvNotification,tvBarriers,tvTotalFees,tvCollectedFees,tvPendingFees;
    View viewOne,viewTwo,viewThree,viewFour;




    ArrayList<DashBoardApiModel> dashBoardApiModelArrayListBus,dashBoardApiModelArrayListNotice;
    ArrayList<DashBoardApiModel> birthdayArrayList;

    int totalStduent = 0;
    int totalEmployeeAttendance = 0;
    PieChart pieChartStaff,pieChartStudent,pieChartFees;

    LinearLayout ll_pieChartStaff,ll_pieChartStudent;
    int totalVisitor=0;

    TextView tvLastVisitorName,tvLastVisitorNumber,tvLastVisitorPurpose,tvLastVisted;


    LinearLayout llBirthday,llVisitors,llNoticeBoard,llAttendance,llBusDetails,llCamera,llSchedule,llGallery,llExam,llTicketRaise;
    ArrayList<DashBoardApiModel> dashBoardApiModelsSchedule;


    LinearLayout llDataNotAvailableBirthday,llVistorsDataAvaialbleVisitors,llVistorsDataNotAvaialbleVisitors,
            llDataNotAvailableNoticeboard,llDataNotAvailableBus,llDataNotAvailableSchedule,llDataNotAvailableGallery,
            llExamDataAvaialble,llDataNotAvailableExam,llDataNotAvailableTicket,llTicketDataAvaialble;


    SharedPreferences.Editor editor, editor2;
    SharedPreferences pref, pref2;


    public Fragment_Dashboard() {
        // Required empty public constructor
    }


    public static Fragment_Dashboard newInstance(String param1, String param2) {
        Fragment_Dashboard fragment = new Fragment_Dashboard();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.new_dashboard, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        initViews(view);
        initListener();
        //getModule();
      //  getModuleListInSpinner(nameModule);
        getSchoolDetailsApi();
        getModuleListInSpinner(moduleList);
        Log.i("MOBILE***",""+ Constant.POC_Mobile_Number);
        Log.i("MOBILE***Logo",""+ Constant.SCHOOL_LOGO);
        Log.i("MOBILE***Logo1",""+ Constant.EMPLOYEE_FNAME);
        callDashboardApis();


        return view;
    }

    private void callDashboardApis() {

        progressDialog.show();
        progressDialog.setMessage("Loading");


        getEmployeeBirthdayAPI();
        getNoticeBoardAPI();
        dashboardStudentAttendanceAPI();
        dashboardEmployeeAttendanceAPI();
        getSchoolBusListAPI();
        getSchoolVisitorCountAPI();
        gettingSchoolGalleryAPI();
        getSchoolTicketsStatisticsAPI();
        getSchoolFeesStatisticsAPI();
        getSchoolScheduleEvents();
        getExamsResultAPI();



    }

    private void getSchoolScheduleEvents() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        String dateNew = simpleDateFormat.format(calendar.getTime());
        if(dashBoardApiModelsSchedule.size()>0){
            dashBoardApiModelsSchedule.clear();
        }
        mApiservicePatashala.dashboard_schedule(Constant.SCHOOL_ID,dateNew).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {


                Log.d("responseCodde", response.code()+"");
                if (response.isSuccessful()){
                    try{

                        JSONObject object = new JSONObject(new Gson().toJson(response.body()));
                        String message = object.getString("message");
                        String status = object.getString("status");

                        if (status.equalsIgnoreCase("Success")){
                            Log.d("db_schedule_success", response.code() + " " + message+" "+response.body());
                            JSONObject jsonData = object.getJSONObject("data");
                            if (!jsonData.isNull("events")) {
                                JSONArray eventsArray = jsonData.getJSONArray("events");
                                for (int i = 0; i<eventsArray.length(); i++) {
                                    JSONObject eventsObject = eventsArray.getJSONObject(i);
                                    String division = "", event_type = "", className = "", section = "", event_name = "";
                                    section    = eventsObject.getString("section");
                                    event_type = eventsObject.getString("event_type");
                                    division   = eventsObject.getString("division");
                                    className  = eventsObject.getString("class");
                                    event_name = eventsObject.getString("event_name");
                                    dashBoardApiModelsSchedule.add(new DashBoardApiModel("Event",event_type,event_name,
                                            className, section, division,""));
                                }
                            }
                            if (!jsonData.isNull("exams")) {
                                JSONArray examsArray = jsonData.getJSONArray("exams");
                                for (int i = 0; i<examsArray.length(); i++) {
                                    JSONObject examObject = examsArray.getJSONObject(i);
                                    String exam_date = "", exam_type = "", section = "", subject = "", division = "", className = "";
                                    exam_date = examObject.getString("exam_date");
                                    exam_type = examObject.getString("exam_type");
                                    section   = examObject.getString("section");
                                    subject   = examObject.getString("subject");
                                    division  = examObject.getString("division");
                                    className = examObject.getString("class");
                                    dashBoardApiModelsSchedule.add(new DashBoardApiModel("Exam",exam_type,subject,
                                            className, section, division,exam_date));
                                }
                            }
                            if (!jsonData.isNull("holidays")) {
                                JSONArray holidaysArray = jsonData.getJSONArray("holidays");
                                for (int i = 0; i<holidaysArray.length(); i++) {
                                    JSONObject examObject = holidaysArray.getJSONObject(i);
                                    String holiday_name = "", division = "", holiday_type = "", section = "", className = "";
                                    holiday_name = examObject.getString("holiday_name");
                                    division = examObject.getString("division");
                                    holiday_type   = examObject.getString("holiday_type");
                                    className = examObject.getString("class");
                                    section = examObject.getString("section");
                                    dashBoardApiModelsSchedule.add(new DashBoardApiModel("Holiday",holiday_name,holiday_type,
                                            className, section, division,""));
                                }
                            }
                            setScheduleToRecyclerView(dashBoardApiModelsSchedule);
                        }else {
                            Log.d("db_schedule_else", response.code() + " " + message);
                            progressDialog.dismiss();
                            llDataNotAvailableSchedule.setVisibility(View.VISIBLE);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                        llDataNotAvailableSchedule.setVisibility(View.VISIBLE);

                    }
                }else {
                    try {
                        assert response.errorBody()!=null;
                        JSONObject object = new JSONObject(response.errorBody().string());
                        String message = object.getString("message");
                        Log.d("db_schedule_fail", message);
                        progressDialog.dismiss();
                        llDataNotAvailableSchedule.setVisibility(View.VISIBLE);

                    }catch (Exception e){
                        e.printStackTrace();
                        llDataNotAvailableSchedule.setVisibility(View.VISIBLE);

                    }
                }
            }
            @Override
            public void onFailure(@NonNull Call<Object> call, @NonNull Throwable t) {
                llDataNotAvailableSchedule.setVisibility(View.VISIBLE);

            }
        });
    }

    private void setScheduleToRecyclerView(ArrayList<DashBoardApiModel> dashBoardApiModelArrayListSchedule) {


        rcvDashboardEventSchedule.setVisibility(View.VISIBLE);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL,false);
        rcvDashboardEventSchedule.setLayoutManager(linearLayoutManager);
        rcvDashboardEventSchedule.setHasFixedSize(true);
        DashBoardApiAdapter fragmentDasboardAdapter = new DashBoardApiAdapter(dashBoardApiModelArrayListSchedule,
                getActivity(),Constant.RV_SCHEDULE_ROW_FRONT);
        rcvDashboardEventSchedule.setAdapter(fragmentDasboardAdapter);
    }

    private void getSchoolTicketsStatisticsAPI(){

        mApiservicePatashala.getSchoolTicketsStatistics(Constant.SCHOOL_ID).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {

                if (response.isSuccessful()){

                    try{
                        JSONObject object = new JSONObject(new Gson().toJson(response.body()));
                        String message = object.getString("message");
                        String status1 = object.getString("status");

                        if (status1.equalsIgnoreCase("Success")){
                            Log.d("ticket_success", response.code() + " " + message+" "+response.body());

                            JSONObject jsonData = object.getJSONObject("data");

                            int total_number_of_tickets = jsonData.getInt("total_number_of_tickets");
                            int total_number_of_new_tickets = jsonData.getInt("total_number_of_new_tickets");
                            int total_number_of_closed_tickets = jsonData.getInt("total_number_of_closed_tickets");
                            int total_number_of_open_tickets = jsonData.getInt("total_number_of_open_tickets");


                            ringProgressTicketOne.setProgress(total_number_of_tickets);
                            ringProgressTicketTwo.setProgress(total_number_of_open_tickets);
                            ringProgressTicketThree.setProgress(total_number_of_closed_tickets);

                            JSONObject current_academic = jsonData.getJSONObject("current_academic");

                            String academic_start_date = current_academic.getString("academic_start_date");
                            String academic_end_date = current_academic.getString("academic_end_date");

                            Log.d("ticket_success_data", total_number_of_tickets+" " +academic_start_date);
                            llTicketDataAvaialble.setVisibility(View.VISIBLE);

                        }else {
                            Log.d("ticket_success_else", response.code() + " " + message);
                            progressDialog.dismiss();
                            llDataNotAvailableTicket.setVisibility(View.GONE);
                        }

                    }catch (Exception e){
                        e.printStackTrace();
                        llDataNotAvailableTicket.setVisibility(View.GONE);

                    }

                }else {
                    try {
                        assert response.errorBody()!=null;
                        JSONObject object = new JSONObject(response.errorBody().string());
                        String message = object.getString("message");
                        Log.d("ticket_success_fail", message);
                        progressDialog.dismiss();
                        llDataNotAvailableTicket.setVisibility(View.GONE);



                    }catch (Exception e){
                        e.printStackTrace();
                        llDataNotAvailableTicket.setVisibility(View.GONE);

                    }
                }

            }

            @Override
            public void onFailure(@NonNull Call<Object> call, @NonNull Throwable t) {
                llDataNotAvailableTicket.setVisibility(View.GONE);

            }
        });
    }

    private void getSchoolFeesStatisticsAPI(){

        mApiService.getSchoolFeesStatistics(Constant.SCHOOL_ID).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {

                if (response.isSuccessful()){

                    try{
                        JSONObject object = new JSONObject(new Gson().toJson(response.body()));
                        String message = object.getString("message");
                        String status1 = object.getString("status");

                        if (status1.equalsIgnoreCase("Success")){
                            Log.d("fees_success", response.code() + " " + message+" "+response.body());

                            JSONObject jsonData = object.getJSONObject("data");

                            if (!jsonData.getJSONObject("month_fees").toString().equals("{}")){

                                JSONObject month_fees = jsonData.getJSONObject("month_fees");

                                double pending_fees_day = month_fees.getDouble("pending_fees");
                                double collected_fees_day = month_fees.getDouble("collected_fees");
                                double total_fees_day = month_fees.getDouble("total_fees");


                            }

                            if (!jsonData.getJSONObject("day_fees").toString().equals("{}")){

                                JSONObject day_fees = jsonData.getJSONObject("day_fees");

                                int pending_fees_day = day_fees.getInt("pending_fees");
                                int collected_fees_day = day_fees.getInt("collected_fees");
                                int total_fees_day = day_fees.getInt("total_fees");
                                setChartFees(2000,4000,6000);




                            }

                            if (!jsonData.getJSONObject("week_fees").toString().equals("{}")){

                                JSONObject week_fees = jsonData.getJSONObject("week_fees");

                                double pending_fees_day = week_fees.getDouble("pending_fees");
                                double collected_fees_day = week_fees.getDouble("collected_fees");
                                double total_fees_day = week_fees.getDouble("total_fees");


                            }

                        }else {
                            Log.d("fees_else", response.code() + " " + message);
                            progressDialog.dismiss();
                        }

                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }else {
                    try {
                        assert response.errorBody()!=null;
                        JSONObject object = new JSONObject(response.errorBody().string());
                        String message = object.getString("message");
                        Log.d("fees_fail", message);
                        progressDialog.dismiss();


                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onFailure(@NonNull Call<Object> call, @NonNull Throwable t) {

            }
        });
    }

    private void getSchoolVisitorCountAPI(){

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        String dateNew = simpleDateFormat.format(calendar.getTime());

        Log.d("DateToday", dateNew);

        progressDialog.show();

        mApiservicePatashala.getSchoolVisitorCount(Constant.SCHOOL_ID, dateNew).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {

                if (response.isSuccessful()){

                    try {

                        JSONObject object = new JSONObject(new Gson().toJson(response.body()));
                        String status = object.getString("status");
                        String message = object.getString("message");
                        String total_visitor = object.getString("total_visitor");
                        totalVisitor = object.getInt("total_visitor");

                        Log.d("Visitor_api_result", response.code()+" "+message+" "+object.toString());

                        if (status.equalsIgnoreCase("Success")){

                            JSONObject jsonData = object.getJSONObject("data");

                            Iterator<String> key = jsonData.keys();
                            String whom_to_meet = "", visitor_name = "", visitor_photo = "", staff_name = "", purpose = "", school_id ="",
                                    visitor_address= "" , visitor_uuid = "", vistor_contact_no =  "";


                            while (key.hasNext()){

                                JSONObject keyData = jsonData.getJSONObject(key.next());


                                //whom_to_meet = keyData.getString("whom_to_meet");
                                visitor_name = keyData.getString("visitor_name");
                               // visitor_photo = keyData.getString("visitor_photo");
                                //staff_name = keyData.getString("staff_name");
                                purpose = keyData.getString("purpose");
                                //school_id = keyData.getString("school_id");
                                //visitor_address = keyData.getString("visitor_address");
                                //visitor_uuid = keyData.getString("visitor_uuid");
                                vistor_contact_no = keyData.getLong("vistor_contact_no")+"";


                                Log.d("visitor", " "+visitor_name+" "+vistor_contact_no+" "+purpose);


                            }
                            ringProgressVisitor.setVisibility(View.VISIBLE);
                            ringProgressVisitor.setProgress(totalVisitor);
                            tvLastVisitorName.setText(visitor_name);
                            tvLastVisitorNumber.setText(vistor_contact_no);
                            tvLastVisitorPurpose.setText(purpose);
                            tvLastVisted.setVisibility(View.VISIBLE);
                            llVistorsDataAvaialbleVisitors.setVisibility(View.VISIBLE);
                            llVistorsDataNotAvaialbleVisitors.setVisibility(View.GONE);
                            progressDialog.dismiss();

                        }else {

                            progressDialog.dismiss();
                            llVistorsDataAvaialbleVisitors.setVisibility(View.GONE);
                            llVistorsDataNotAvaialbleVisitors.setVisibility(View.VISIBLE);
                        }


                    }catch (Exception e){
                        e.printStackTrace();
                        llVistorsDataAvaialbleVisitors.setVisibility(View.GONE);
                        llVistorsDataNotAvaialbleVisitors.setVisibility(View.VISIBLE);
                    }

                }else {

                    try {
                        assert response.errorBody() != null;
                        JSONObject object = new JSONObject(response.errorBody().string());
                        String message = object.getString("message");
                        Log.d("Visitor_api_fail", message);
                        progressDialog.dismiss();
                        llVistorsDataAvaialbleVisitors.setVisibility(View.GONE);
                        llVistorsDataNotAvaialbleVisitors.setVisibility(View.VISIBLE);

                    } catch (Exception e) {
                        e.printStackTrace();
                        llVistorsDataAvaialbleVisitors.setVisibility(View.GONE);
                        llVistorsDataNotAvaialbleVisitors.setVisibility(View.VISIBLE);
                    }
                }

            }

            @Override
            public void onFailure(@NonNull Call<Object> call, @NonNull Throwable t) {
                llVistorsDataAvaialbleVisitors.setVisibility(View.GONE);
                llVistorsDataNotAvaialbleVisitors.setVisibility(View.VISIBLE);

            }
        });
    }

    private void gettingSchoolGalleryAPI(){

        mLayoutManager = new StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL);
        rcvDashboardGallery.setLayoutManager(mLayoutManager);

        // Initialize a new instance of RecyclerView Adapter instance
        mAdapter = new DashBoardGalleryAdapter(getActivity(), imageList);

        // Set the adapter for RecyclerView
        rcvDashboardGallery.setAdapter(mAdapter);

        progressDialog.show();

        mApiservicePatashala.getGallery(Constant.SCHOOL_ID).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {

                if (imageList.size()>0)
                    imageList.clear();

                if (response.isSuccessful()){

                    try {

                        JSONObject object = new JSONObject(new Gson().toJson(response.body()));
                        String status = object.getString("status");
                        String message = object.getString("message");

                        Log.d("Gallery_result", response.code()+" "+message);

                        if (status.equalsIgnoreCase("Success")){

                            JSONObject jsonData = object.getJSONObject("data");

                            JSONObject gallery_details = jsonData.getJSONObject("gallery_details");

                            Iterator<String> key = gallery_details.keys();

                            while (key.hasNext()){

                                JSONObject keyData = gallery_details.getJSONObject(key.next());

                                String gallery_id = "", added_by = "", title = "", description = "", employee_id = "", added_datetime ="";

                                gallery_id = keyData.getString("gallery_id");
                                added_by = keyData.getString("added_by");
                                title = keyData.getString("title");
                                description = keyData.getString("description");
                                employee_id = keyData.getString("employee_id");
                                added_datetime = keyData.getString("added_datetime");


                                if (!keyData.isNull("gallery_image")){
                                    JSONArray array = keyData.getJSONArray("gallery_image");

                                    for (int i=0; i<array.length(); i++){

                                        String image = array.get(i).toString();
                                        imageList.add(image);

                                    }
                                }
                            }

                            progressDialog.dismiss();
                            mAdapter.notifyDataSetChanged();
                            rcvDashboardGallery.setVisibility(View.VISIBLE);


                        }else {

                            progressDialog.dismiss();
                            mAdapter.notifyDataSetChanged();
                            llDataNotAvailableGallery.setVisibility(View.VISIBLE);



                        }


                    }catch (Exception e){
                        e.printStackTrace();
                        llDataNotAvailableGallery.setVisibility(View.VISIBLE);

                    }

                }else {

                    try {
                        assert response.errorBody() != null;
                        JSONObject object = new JSONObject(response.errorBody().string());
                        String message = object.getString("message");
                        Log.d("Gallery_fail", message);
                        progressDialog.dismiss();
                        mAdapter.notifyDataSetChanged();
                        llDataNotAvailableGallery.setVisibility(View.VISIBLE);


                    } catch (Exception e) {
                        e.printStackTrace();
                        llDataNotAvailableGallery.setVisibility(View.VISIBLE);

                    }
                }

            }

            @Override
            public void onFailure(@NonNull Call<Object> call, @NonNull Throwable t) {
                llDataNotAvailableGallery.setVisibility(View.VISIBLE);

            }
        });
    }

    private void getNoticeBoardAPI(){

        Log.d("NOTICE_API_CALL", "CALLED");
        dashBoardApiModelArrayListNotice = new ArrayList<>();

        progressDialog.show();
        mApiservicePatashala.getNotice(Constant.SCHOOL_ID).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {

                if (response.isSuccessful()){
                    try {
                        JSONObject object = new JSONObject(new Gson().toJson(response.body()));
                        String message = object.getString("message");
                        String status = object.getString("status");

                        if (status.equalsIgnoreCase("Success")){
                            Log.d("NoticeBoard", response.code()+" "+message);

                            JSONObject jsonData = object.getJSONObject("data");

                            Iterator<String> key = jsonData.keys();

                            while (key.hasNext()){

                                String myKey = key.next();
                                Log.d("NOTICE_API_Key", myKey);

                                JSONObject keyData = jsonData.getJSONObject(myKey);

                                String notice_id = keyData.getString("notice_id");
                                String notice_title = keyData.getString("notice_title");
                                String notice_message = keyData.getString("notice_message");

                                Log.d("NOTICE_API_DATA",notice_id+" "+notice_message);
                                dashBoardApiModelArrayListNotice.add(new DashBoardApiModel(notice_id,notice_title,notice_message));

                            }
                            setNoticeToRecyclerView(dashBoardApiModelArrayListNotice);

                            progressDialog.dismiss();



                        }else {
                            Log.d("NOTICE_API_else", message);
                            Log.d("NOTICE_API_else", String.valueOf(response.code()));
                            progressDialog.dismiss();
                            llDataNotAvailableNoticeboard.setVisibility(View.VISIBLE);


                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        llDataNotAvailableNoticeboard.setVisibility(View.VISIBLE);

                    }

                }else {
                    try {
                        assert response.errorBody()!=null;
                        JSONObject object = new JSONObject(response.errorBody().string());
                        String message = object.getString("message");
                        Log.d("NOTICE_API", message);
                        progressDialog.dismiss();
                        llDataNotAvailableNoticeboard.setVisibility(View.VISIBLE);



                    }catch (Exception e){
                        e.printStackTrace();
                        llDataNotAvailableNoticeboard.setVisibility(View.VISIBLE);

                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<Object> call, @NonNull Throwable t) {
                llDataNotAvailableNoticeboard.setVisibility(View.VISIBLE);

            }
        });
    }

    private void setNoticeToRecyclerView(ArrayList<DashBoardApiModel> apiModels) {

        rcvNoticeBoard.setVisibility(View.VISIBLE);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL,false);
        rcvNoticeBoard.setLayoutManager(linearLayoutManager);
        rcvNoticeBoard.setHasFixedSize(true);

        DashBoardApiAdapter fragmentDasboardAdapter = new DashBoardApiAdapter(apiModels,getActivity(),Constant.RV_NOTICEBOARD_ROW);
        rcvNoticeBoard.setAdapter(fragmentDasboardAdapter);
        rcvNoticeBoard.setNestedScrollingEnabled(false);


    }

    private void dashboardStudentAttendanceAPI(){

        mApiservicePatashala.dashboardStudentAttendance(Constant.SCHOOL_ID).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {

                if (response.isSuccessful()){

                    try {
                        JSONObject object = new JSONObject(new Gson().toJson(response.body()));
                        String message = object.getString("message");
                        String status = object.getString("status");
                        Log.d("AttApi", response.code() + " " + object);

                        if (status.equalsIgnoreCase("Success")) {

                            JSONObject jsonData = object.getJSONObject("data");

                            Log.d("studentAttendance", jsonData.toString());


                            String total_students = "", absent_students = "", present_students = "";

                            total_students   = jsonData.getInt("total_students")+"";
                            absent_students  = jsonData.getInt("absent_students")+"";
                            present_students = jsonData.getInt("present_students")+"";

                            Log.d("stdentAtten",present_students+" "+total_students );

                            setChartAndDataStudent(jsonData.getInt("present_students"),jsonData.getInt("absent_students"),jsonData.getInt("total_students"));

                            totalStduent = jsonData.getInt("total_students");






                        }else {
                            progressDialog.dismiss();
                        }

                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }else {
                    try {
                        assert response.errorBody()!=null;
                        JSONObject object = new JSONObject(response.errorBody().string());
                        String message = object.getString("message");
                        Log.d("stu_attendance_API", message);
                        progressDialog.dismiss();


                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(@NonNull Call<Object> call, @NonNull Throwable t) {

            }
        });
    }

    private void dashboardEmployeeAttendanceAPI(){

        mApiservicePatashala.dashboardEmployeeAttendance(Constant.SCHOOL_ID).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {

                if (response.isSuccessful()){

                    try {
                        JSONObject object = new JSONObject(new Gson().toJson(response.body()));
                        String message = object.getString("message");
                        String status = object.getString("status");
                        Log.d("NOTICE_API_Success", response.code() + " " + message);

                        if (status.equalsIgnoreCase("Success")) {

                            JSONObject jsonData = object.getJSONObject("data");

                            String absent_employees = "", total_employees = "",present_employees = "";

                            absent_employees   = jsonData.getInt("absent_employees")+"";
                            total_employees  = jsonData.getInt("total_employees")+"";
                            present_employees  = jsonData.getInt("present_employees")+"";
                            totalEmployeeAttendance = jsonData.getInt("total_employees");


                            setChartAndDataStaff(jsonData.getInt("present_employees"),jsonData.getInt("absent_employees"),jsonData.getInt("total_employees"));

                            Log.d("EmployeeAt", absent_employees+" "+total_employees+" "+present_employees);






                        }else {
                            progressDialog.dismiss();
                        }

                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }else {

                    try {
                        assert response.errorBody()!=null;
                        JSONObject object = new JSONObject(response.errorBody().string());
                        String message = object.getString("message");
                        Log.d("Emp_att", message);
                        progressDialog.dismiss();


                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(@NonNull Call<Object> call, @NonNull Throwable t) {

            }
        });
    }

    private void getSchoolBusListAPI(){

        dashBoardApiModelArrayListBus = new ArrayList<>();

        mApiservicePatashala.getSchoolBusList(Constant.SCHOOL_ID).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {

                if (response.isSuccessful()){

                    try {
                        JSONObject object = new JSONObject(new Gson().toJson(response.body()));
                        String message = object.getString("message");
                        String status = object.getString("status");
                       // Log.d("BUSLIST", response.code() + " " + message);

                        if (status.equalsIgnoreCase("Success")) {

                            JSONObject jsonData = object.getJSONObject("data");

                            Log.d("BUSLIST", jsonData + " " + message);


                            Iterator<String> key = jsonData.keys();

                            while (key.hasNext()){

                                JSONObject keyData = jsonData.getJSONObject(key.next());



                                String registration_no, seating_capacity, GPS_Details, school_id,added_datetime, vehicle_uuid, vehicle_id,
                                        class_of_vehicle,type_of_body, added_by, route_uuid="",route_number="";

                                registration_no = keyData.getString("registration_no");
                                //seating_capacity = keyData.getString("seating_capacity");
                                seating_capacity = "23";
                                GPS_Details = keyData.getString("GPS_Details");
                                school_id = keyData.getString("school_id");
                                added_datetime = keyData.getString("added_datetime");
                                vehicle_uuid = keyData.getString("vehicle_uuid");
                                vehicle_id = keyData.getString("vehicle_id");
                                class_of_vehicle = keyData.getString("class_of_vehicle");
                                type_of_body = keyData.getString("type_of_body");
                                added_by = keyData.getString("added_by");

                                if (!keyData.isNull("routes")){

                                    JSONArray routeArray = keyData.getJSONArray("routes");

                                    for (int i=0; i<routeArray.length(); i++){

                                        JSONObject routeData = routeArray.getJSONObject(i);

                                       route_uuid = routeData.getString("route_uuid");
                                       route_number = routeData.getString("route_number");

                                       Log.d("routeNumber", route_number);
                                    }

                                    route_number = "1";
                                    route_uuid = "sadh2743-sdfk48-sdfk3fsdf4435";

                                    dashBoardApiModelArrayListBus.add(new DashBoardApiModel(registration_no,seating_capacity,GPS_Details,vehicle_uuid,vehicle_id,class_of_vehicle,type_of_body,route_uuid,route_number));

                                }

                                setBusListRecyclerView(dashBoardApiModelArrayListBus);

                            }


                        }else {
                            progressDialog.dismiss();
                            llDataNotAvailableBus.setVisibility(View.VISIBLE);
                        }

                    }catch (Exception e){
                        e.printStackTrace();
                        llDataNotAvailableBus.setVisibility(View.VISIBLE);

                    }

                }else {

                    try {
                        assert response.errorBody()!=null;
                        JSONObject object = new JSONObject(response.errorBody().string());
                        String message = object.getString("message");
                        Log.d("Emp_att", message);
                        progressDialog.dismiss();
                        llDataNotAvailableBus.setVisibility(View.VISIBLE);



                    }catch (Exception e){
                        e.printStackTrace();
                        llDataNotAvailableBus.setVisibility(View.VISIBLE);

                    }

                }
            }

            @Override
            public void onFailure(@NonNull Call<Object> call, @NonNull Throwable t) {
                llDataNotAvailableBus.setVisibility(View.VISIBLE);

            }
        });
    }

    private void setBusListRecyclerView(ArrayList<DashBoardApiModel> apiModels) {


        rcvDashboardBusDetails.setVisibility(View.VISIBLE);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL,false);
        rcvDashboardBusDetails.setLayoutManager(linearLayoutManager);
        rcvDashboardBusDetails.setHasFixedSize(true);

        DashBoardApiAdapter fragmentDasboardAdapter = new DashBoardApiAdapter(apiModels,getActivity(),Constant.RV_TRANSPORT_BUS_LIST_ROW);
        rcvDashboardBusDetails.setAdapter(fragmentDasboardAdapter);


    }

    private void getPendingStudentListAPI(){

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        String dateNew = simpleDateFormat.format(calendar.getTime());

        Log.d("DateToday", dateNew);

        mApiservicePatashala.getPendingStudentList(Constant.SCHOOL_ID, dateNew).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {

                if (response.isSuccessful()){

                    try {
                        JSONObject object = new JSONObject(new Gson().toJson(response.body()));
                        String message = object.getString("message");
                        String status = object.getString("status");
                        Log.d("NOTICE_API_Success", response.code() + " " + message);

                        if (status.equalsIgnoreCase("Success")) {

                            JSONObject jsonData = object.getJSONObject("data");

                            JSONArray approvedArray = jsonData.getJSONArray("approved");

                            for (int i=0; i<approvedArray.length(); i++){

                                JSONObject approvedObject = approvedArray.getJSONObject(i);

                                String custom_student_id;

                                String submitted_timestamp = approvedObject.getString("submitted_timestamp");
                                String dob = approvedObject.getString("dob");
                                String adhar_id_number = approvedObject.getString("adhar_id_number");
                                String student_first_name = approvedObject.getString("student_first_name");
                                String division = approvedObject.getString("division");
                                String student_last_name = approvedObject.getString("student_last_name");
                                String student_uuid = approvedObject.getString("student_uuid");
                                String school_id = approvedObject.getString("school_id");
                                String student_photo = approvedObject.getString("student_photo");
                                String birth_certificate_number = approvedObject.getString("birth_certificate_number");
                                String gender = approvedObject.getString("gender");
                                String classes = approvedObject.getString("classes");

                                if (!approvedObject.isNull("student_photo")){
                                    custom_student_id = approvedObject.getString("custom_student_id");

                                }
                            }

                            progressDialog.dismiss();

                        }else {
                            progressDialog.dismiss();
                        }

                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }else {

                    try {
                        assert response.errorBody()!=null;
                        JSONObject object = new JSONObject(response.errorBody().string());
                        String message = object.getString("message");
                        Log.d("Emp_att", message);
                        progressDialog.dismiss();


                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(@NonNull Call<Object> call, @NonNull Throwable t) {

            }
        });
    }

    private void getEmployeeBirthdayAPI() {

        birthdayArrayList = new ArrayList<>();
        mApiservicePatashala.getEmployeeBirthday(Constant.SCHOOL_ID).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {

                if (response.isSuccessful()){

                    try {

                        JSONObject object = new JSONObject(new Gson().toJson(response.body()));
                        String status = object.getString("status");
                        String message = object.getString("message");

                        Log.d("Birthday_result", new Gson().toJson(response.body())+" "+message);

                        if (status.equalsIgnoreCase("Success")){

                            JSONArray jsonArray = object.getJSONArray("data");

                            for (int i = 0; i < jsonArray.length(); i++){

                                JSONObject arrayData = jsonArray.getJSONObject(i);

                                String employee_custom_id  = "",employee_fistname = "",employee_uuid = "",employee_lastname = "",
                                        employee_birthday = "",employee_image = "";

                                employee_custom_id = arrayData.getString("employee_custom_id");
                                employee_fistname  = arrayData.getString("employee_fistname");
                                employee_uuid      = arrayData.getString("employee_uuid");
                                employee_lastname  = arrayData.getString("employee_lastname");
                                employee_birthday  = arrayData.getString("employee_birthday");
                                employee_image     = arrayData.getString("employee_image");

                                birthdayArrayList.add(new DashBoardApiModel(employee_fistname,employee_lastname,employee_custom_id,
                                        employee_image, employee_uuid,employee_birthday));

                            }

                            setBirthDayRecyclerView(birthdayArrayList);
                            llDataNotAvailableBirthday.setVisibility(View.GONE);

                        }else {
                            llDataNotAvailableBirthday.setVisibility(View.VISIBLE);
                            rcvBirthday.setVisibility(View.GONE);
                            progressDialog.dismiss();
                        }


                    }catch (Exception e){
                        e.printStackTrace();
                        llDataNotAvailableBirthday.setVisibility(View.VISIBLE);
                        rcvBirthday.setVisibility(View.GONE);
                    }

                }else {

                    try {
                        llDataNotAvailableBirthday.setVisibility(View.VISIBLE);
                        rcvBirthday.setVisibility(View.GONE);
                        assert response.errorBody() != null;
                        JSONObject object = new JSONObject(response.errorBody().string());
                        String message = object.getString("message");
                        Log.d("Birthday_fail", message);
                        progressDialog.dismiss();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onFailure(@NonNull Call<Object> call, @NonNull Throwable t) {
                llDataNotAvailableBirthday.setVisibility(View.VISIBLE);
                rcvBirthday.setVisibility(View.GONE);
            }
        });
    }

    private void setBirthDayRecyclerView(ArrayList<DashBoardApiModel> birthdayArrayList) {

        rcvBirthday.setVisibility(View.VISIBLE);
        DashBoardApiAdapter dashBoardApiAdapter = new DashBoardApiAdapter(birthdayArrayList,getActivity(), Constant.RV_BIRTHDAY_ROW);
        rcvBirthday.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
        rcvBirthday.setAdapter(dashBoardApiAdapter);
        rcvBirthday.setOnFlingListener(null);
        PagerSnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(rcvBirthday);
    }

    private void getExamsResultAPI() {


        progressDialog.show();

        mApiservicePatashala.getExamsResult(Constant.SCHOOL_ID, "2").enqueue(new Callback<Object>() {
            @Override
            public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {

                if (response.isSuccessful()){

                    try {

                        JSONObject object = new JSONObject(new Gson().toJson(response.body()));
                        String status = object.getString("status");
                        String message = object.getString("message");
                        String total_pages = object.getString("total_pages");

                        Log.d("ExamResult_result", response.code()+" "+message+" "+object);

                        if (status.equalsIgnoreCase("Success")){

                           /* JSONArray jsonArray = object.getJSONArray("data");

                            for (int i = 0; i < jsonArray.length(); i++){

                                JSONObject arrayData = jsonArray.getJSONObject(i);

                            }*/

                           llDataNotAvailableExam.setVisibility(View.VISIBLE);

                        }else {

                            progressDialog.dismiss();
                            llDataNotAvailableExam.setVisibility(View.VISIBLE);

                        }


                    }catch (Exception e){
                        e.printStackTrace();
                        llDataNotAvailableExam.setVisibility(View.VISIBLE);

                    }

                }else {
                    try {
                        assert response.errorBody() != null;
                        JSONObject object = new JSONObject(response.errorBody().string());
                        String message = object.getString("message");
                        Log.d("ExamResult_fail", message);
                        progressDialog.dismiss();
                        llDataNotAvailableExam.setVisibility(View.VISIBLE);


                    } catch (Exception e) {
                        e.printStackTrace();
                        llDataNotAvailableExam.setVisibility(View.VISIBLE);

                    }
                }


            }

            @Override
            public void onFailure(@NonNull Call<Object> call, @NonNull Throwable t) {
                llDataNotAvailableExam.setVisibility(View.VISIBLE);

            }
        });





    }


    private void initListener() {

        ivLogout.setOnClickListener(this);
        editSetting.setOnClickListener(this);
        llFirstItem.setOnClickListener(this);
        llSecondItem.setOnClickListener(this);
        llThirdItem.setOnClickListener(this);
        llForthItem.setOnClickListener(this);
        ivSlideImage.setOnClickListener(this);



        /*llBirthday,llVisitors,llNoticeBoard,llAttendance,llBusDetails,llCamera,llSchedule,llGallery,llExam;*/

        llBirthday.setOnClickListener(this);
        llVisitors.setOnClickListener(this);
        llNoticeBoard.setOnClickListener(this);
        llAttendance.setOnClickListener(this);
        llBusDetails.setOnClickListener(this);
        llCamera.setOnClickListener(this);
        llSchedule.setOnClickListener(this);
        llGallery.setOnClickListener(this);
        llExam.setOnClickListener(this);
        ivRefresh.setOnClickListener(this);
        llTicketRaise.setOnClickListener(this);

    }

    private void initViews(View view) {

        pref = getActivity().getSharedPreferences("ActivityPREF", Context.MODE_PRIVATE);
        pref2 = getActivity().getSharedPreferences("SecurityStatus", Context.MODE_PRIVATE);
        editor = pref.edit();
        editor2 = pref2.edit();

        llFirstItem = view.findViewById(R.id.llFirstItem);
        llSecondItem = view.findViewById(R.id.llSecondItem);
        llThirdItem = view.findViewById(R.id.llThirdItem);
        llForthItem = view.findViewById(R.id.llForthItem);
        ringProgressVisitor = view.findViewById(R.id.ringProgressVisitor);
        dashBoardApiModelsSchedule = new ArrayList<>();
        ringProgressTicketOne = view.findViewById(R.id.ringProgressTicketOne);
        ringProgressTicketTwo = view.findViewById(R.id.ringProgressTicketTwo);
        ringProgressTicketThree = view.findViewById(R.id.ringProgressTicketThree);

        rcvDashboardGallery = view.findViewById(R.id.rcvDashboardGallery);

        rcvNoticeBoard = view.findViewById(R.id.rcvNoticeBoard);
        rcvDashboardBusDetails = view.findViewById(R.id.rcvDashboardBusDetails);
        rcvBirthday = view.findViewById(R.id.rcvBirthday);


        rcvDashboardEventSchedule = view.findViewById(R.id.rcvDashboardEventSchedule);

        rcvDashboardGallery.setNestedScrollingEnabled(false);
        progressDialog = new ProgressDialog(getActivity());
        imageList = new ArrayList<>();

        loadUrlIntoView(view);

        ivRefresh = view.findViewById(R.id.ivRefresh);





        //handleRingProgress("Visitor",50,100);



        nameModule.clear();
        mApiService = ApiUtils.getAPIService();
        mApiservicePatashala = ApiUtilsPatashala.getService();

        nameModule.add("School Profile");

        nameModule.add("Staff Directory");
        nameModule.add("Student Directory");
        nameModule.add("Staff Attendance");
        nameModule.add("Student Attendance");
        nameModule.add("Class Teacher");
        nameModule.add("Subject");
        nameModule.add("Leave");
        nameModule.add("Syllabus");

        nameModule.add("Homework");
        nameModule.add("Schedule");
        nameModule.add("School Activity Barriers");
        nameModule.add("Notification");
        nameModule.add("Notice Board");
        nameModule.add("Question Bank");
        nameModule.add("House");
        nameModule.add("Birthday");
        nameModule.add("Gallery");
        nameModule.add("Transport");

        moduleList.add(new ModuleModel(R.drawable.ic_assesment,"School Profile"));
        moduleList.add(new ModuleModel(R.drawable.ic_staff,"Staff Directory"));
        moduleList.add(new ModuleModel(R.drawable.ic_student,"Student Directory"));
        moduleList.add(new ModuleModel(R.drawable.ic_staff,"Staff Attendance"));
        moduleList.add(new ModuleModel(R.drawable.ic_student,"Student Attendance"));
        moduleList.add(new ModuleModel(R.drawable.ic_class_teacher,"Class Teacher"));
        moduleList.add(new ModuleModel(R.drawable.ic_subject,"Subject"));
        moduleList.add(new ModuleModel(R.drawable.ic_leave,"Leave"));
        moduleList.add(new ModuleModel(R.drawable.ic_syllabus,"Syllabus"));
        moduleList.add(new ModuleModel(R.drawable.ic_homework_new,"Homework"));
        moduleList.add(new ModuleModel(R.drawable.ic_schedule,"Schedule"));
        moduleList.add(new ModuleModel(R.drawable.ic_barrier,"School Activity Barriers"));
        moduleList.add(new ModuleModel(R.drawable.ic_notification,"Notification"));
        moduleList.add(new ModuleModel(R.drawable.ic_noticeboard,"Notice Board"));
        moduleList.add(new ModuleModel(R.drawable.ic_question_bank,"Question Bank"));
        moduleList.add(new ModuleModel(R.drawable.ic_house_menu,"House"));
        moduleList.add(new ModuleModel(R.drawable.ic_cake_new,"Birthday"));
        moduleList.add(new ModuleModel(R.drawable.ic_gallery_new,"Gallery"));
        moduleList.add(new ModuleModel(R.drawable.ic_visitor,"Visitor"));
        moduleList.add(new ModuleModel(R.drawable.ic_ticket_raise,"Ticket Raise"));
        moduleList.add(new ModuleModel(R.drawable.ic_bus,"Transport"));
       // moduleList.add(new ModuleModel(R.drawable.ic_timetable,"Fees"));



        ivSlideImage = view.findViewById(R.id.ivSlideImage);


       // nameModule.add("Fees");
        //nameModule.add("TimeTable");
        // nameModule.add("Visitor");
        // nameModule.add("Attendance");

        recyclerView = view.findViewById(R.id.rcv_ModuleList);
        ivLogout = view.findViewById(R.id.iv_logout);
        editSetting = view.findViewById(R.id.edit_setting);
        iv_schoolLogo = view.findViewById(R.id.iv_schoolLogo);

        linearLayout     = view.findViewById(R.id.linearLayout);
        linearLayout.setVisibility(View.GONE);

        if (Constant.SCHOOL_LOGO.isEmpty() || Constant.SCHOOL_LOGO.equalsIgnoreCase("")|| Constant.SCHOOL_LOGO.equals(null)){

        }else {
            Picasso.with(getActivity()).
                    load(Constant.IMAGE_LINK+Constant.SCHOOL_LOGO)
                    .placeholder(R.drawable.patashala_logo)
                    //.resize(50, 50)
                    .into(iv_schoolLogo);
           // Glide.with(getActivity()).load(Constant.IMAGE_LINK+Constant.SCHOOL_LOGO).into(iv_schoolLogo);

        }





        ivHome = view.findViewById(R.id.ivFirstItem);
        tvHome = view.findViewById(R.id.tvFirstItem);
        viewOne = view.findViewById(R.id.viewOne);
        ivHome.setImageDrawable(getResources().getDrawable(R.drawable.ic_home_filled));
        tvHome.setTextColor(getResources().getColor(R.color.orange_colorPrimaryDark));
        viewOne.setVisibility(View.VISIBLE);



        ll_pieChartStaff = view.findViewById(R.id.ll_pieChartStaff);
        pieChartStaff= new PieChart(getActivity());
        pieChartStaff.setTextColor(R.color.blue);
        pieChartStaff.setChartColor(R.color.blue);
        pieChartStaff.setAboutTextColor(R.color.blue);
        ll_pieChartStaff.addView(pieChartStaff);
        setChartAndDataStaff(0,0,0);


        ll_pieChartStudent = view.findViewById(R.id.ll_pieChartStudent);
        pieChartStudent= new PieChart(getActivity());
        pieChartStudent.setTextColor(R.color.blue);
        pieChartStudent.setChartColor(R.color.blue);
        pieChartStudent.setAboutTextColor(R.color.blue);
        ll_pieChartStudent.addView(pieChartStudent);
        setChartAndDataStudent(0,0,0);





        tvLastVisitorName = view.findViewById(R.id.tvLastVisterName);
        tvLastVisitorNumber = view.findViewById(R.id.tvLastVisterNumber);
        tvLastVisitorPurpose = view.findViewById(R.id.tvLastVisterPurpose);
        tvLastVisted = view.findViewById(R.id.tvLastVisted);




        /*llBirthday,llVisitors,llNoticeBoard,llAttendance,llBusDetails,llCamera,llSchedule,llGallery,llExam;*/


        llBirthday = view.findViewById(R.id.llBirthday);
        llVisitors = view.findViewById(R.id.llVisitors);
        llNoticeBoard = view.findViewById(R.id.llNoticeBoard);
        llAttendance = view.findViewById(R.id.llAttendance);
        llBusDetails = view.findViewById(R.id.llBusDetails);
        llCamera = view.findViewById(R.id.llCctv);
        llSchedule = view.findViewById(R.id.llSchedule);
        llGallery = view.findViewById(R.id.llGallery);
        llExam = view.findViewById(R.id.llExam);
        llTicketRaise = view.findViewById(R.id.llTicketRaise);


        tvTotalFees = view.findViewById(R.id.tvTotalFees);
        tvCollectedFees = view.findViewById(R.id.tvCollectedFees);
        tvPendingFees = view.findViewById(R.id.tvPendingFees);

        ll_pieChartFees = view.findViewById(R.id.ll_pieChartFees);
        pieChartFees= new PieChart(getActivity());
        pieChartFees.setTextColor(R.color.blue);
        pieChartFees.setChartColor(R.color.blue);
        pieChartFees.setAboutTextColor(R.color.blue);
        ll_pieChartFees.addView(pieChartFees);
        setChartFees(1000,3000,6000);

        llDataNotAvailableBirthday = view.findViewById(R.id.llDataNotAvailableBirthday);
        llVistorsDataAvaialbleVisitors = view.findViewById(R.id.llVistorsDataAvaialbleVisitors);
        llVistorsDataNotAvaialbleVisitors = view.findViewById(R.id.llDataNotAvailableVisitors);
        llDataNotAvailableNoticeboard = view.findViewById(R.id.llDataNotAvailableNoticeboard);
        llDataNotAvailableBus = view.findViewById(R.id.llDataNotAvailableBus);
        llDataNotAvailableSchedule = view.findViewById(R.id.llDataNotAvailableSchedule);
        llDataNotAvailableGallery = view.findViewById(R.id.llDataNotAvailableGallery);
        llDataNotAvailableExam = view.findViewById(R.id.llDataNotAvailableExam);
        llExamDataAvaialble = view.findViewById(R.id.llExamDataAvaialble);
        llDataNotAvailableTicket = view.findViewById(R.id.llDataNotAvailableTicket);
        llTicketDataAvaialble = view.findViewById(R.id.llTicketDataAvaialble);



    }

    private void setChartFees(int collected, int pending, int total) {

        int total1 = collected + pending + total;

        List<ChartData> data = new ArrayList<>();



        int onTimePercent = 0;
        int absentPercent = 0;
        int latePercent   = 0;

        if (collected == 0 && pending == 0 && total == 0) {
            absentPercent = 100;
            data.add(new ChartData("", absentPercent, Color.WHITE, Color.parseColor("#CAC6C6")));//

        }

        if (collected != 0) {
            latePercent = (collected * 100) / total1;
            data.add(new ChartData(latePercent+"%",latePercent , Color.WHITE, Color.parseColor("#52b155")));//green

        }

        if (pending != 0) {
            absentPercent = (pending * 100) / total1;
            data.add(new ChartData(absentPercent+"%", absentPercent, Color.WHITE,Color.parseColor("#ffd700")));//yellow

        }

        if (total != 0) {
            onTimePercent = (total * 100) / total1;
            data.add(new ChartData(onTimePercent+"%", onTimePercent, Color.WHITE, Color.parseColor("#f2790d")));//orange

        }




        tvTotalFees.setText("Total = \u20B9 "+total);
        tvCollectedFees.setText("Collected = \u20B9 "+collected);
        tvPendingFees.setText("Pending = \u20B9 "+pending);

        pieChartFees.setAboutChart(" ");


        pieChartFees.setChartData(data);
        pieChartFees.partitionWithPercent(true);
        pieChartFees.setAboutTextSize(20);
        pieChartFees.setCenterCircleColor(Color.WHITE);
    }

    private void setChartAndDataStaff(int present, int absent, int total) {

        int total1 = present+absent;

        List<ChartData> data = new ArrayList<>();

        int presentPercent = 0;

        int absentPercent = 0;


        if (present == 0 && absent == 0 && total1 == 0) {
            absentPercent = 100;
            data.add(new ChartData("", absentPercent, Color.WHITE, Color.parseColor("#CAC6C6")));//red

        }

        if (absent != 0) {
            absentPercent = (absent * 100) / total1;
            data.add(new ChartData(absentPercent+"%", absentPercent, Color.WHITE, Color.parseColor("#FD0202")));//red

        }

        if (present != 0) {
            presentPercent = (present * 100) / total1;
            data.add(new ChartData(presentPercent+"%", presentPercent, Color.WHITE, Color.parseColor("#72d548")));//green

        }




        pieChartStaff.setAboutChart( present +" / "+absent);


        pieChartStaff.setChartData(data);
        pieChartStaff.partitionWithPercent(true);
        pieChartStaff.setAboutTextSize(20);
        pieChartStaff.setCenterCircleColor(Color.WHITE);
    }

    private void setChartAndDataStudent(int present, int absent, int total) {

        int total1 = present+absent;

        List<ChartData> data = new ArrayList<>();

        int presentPercent = 0;

        int absentPercent = 0;


        if (present == 0 && absent == 0 && total1 == 0) {
            absentPercent = 100;
            data.add(new ChartData("", absentPercent, Color.WHITE, Color.parseColor("#CAC6C6")));//red

        }

        if (absent != 0) {
            absentPercent = (absent * 100) / total1;
            data.add(new ChartData(absentPercent+"%", absentPercent, Color.WHITE, Color.parseColor("#FD0202")));//red

        }

        if (present != 0) {
            presentPercent = (present * 100) / total1;
            data.add(new ChartData(presentPercent+"%", presentPercent, Color.WHITE, Color.parseColor("#72d548")));//green

        }




        pieChartStudent.setAboutChart( present+" / "+absent);


        pieChartStudent.setChartData(data);
        pieChartStudent.partitionWithPercent(true);
        pieChartStudent.setAboutTextSize(20);
        pieChartStudent.setCenterCircleColor(Color.WHITE);
    }

    private void loadUrlIntoView(View view) {

        wbUrlOne = view.findViewById(R.id.wbUrlOne);
        wbUrlTwo = view.findViewById(R.id.wbUrlTwo);
        wbUrlThree = view.findViewById(R.id.wbUrlThree);
        wbUrlFour = view.findViewById(R.id.wbUrlFour);



        wbUrlOne.loadUrl("http://121.58.202.110:8080/jpeg?cam=10");
        wbUrlOne.getSettings().setLoadWithOverviewMode(true);
        wbUrlOne.getSettings().setUseWideViewPort(true);
        wbUrlOne.setWebChromeClient(new WebChromeClient());

        wbUrlTwo.loadUrl("http://121.58.202.110:8080/jpeg?cam=9");
        wbUrlTwo.getSettings().setLoadWithOverviewMode(true);
        wbUrlTwo.getSettings().setUseWideViewPort(true);


        wbUrlThree.loadUrl("http://121.58.202.110:8080/jpeg?cam=8");
        wbUrlThree.getSettings().setLoadWithOverviewMode(true);
        wbUrlThree.getSettings().setUseWideViewPort(true);


        wbUrlFour.loadUrl("http://121.58.202.110:8080/jpeg?cam=7");
        wbUrlFour.getSettings().setLoadWithOverviewMode(true);
        wbUrlFour.getSettings().setUseWideViewPort(true);
    }

    private void getModuleListInSpinner(List<ModuleModel> nameModule) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

        FragmentDasboardAdapter fragmentDasboardAdapter = new FragmentDasboardAdapter(getActivity(), nameModule);
        recyclerView.setAdapter(fragmentDasboardAdapter);
        fragmentDasboardAdapter.notifyDataSetChanged();

    }

    @Override
    public void onClick(View v) {

        assert getActivity()!=null;

        switch (v.getId()) {

            case R.id.iv_logout:

                logoutMethod();
               /* Intent intent = new Intent(getActivity(), DashboardSettingActivity.class);
                startActivity(intent);*/
                break;
            case R.id.edit_setting:
                Intent intentEdit = new Intent(getActivity(), BarrierSettingActivity.class);
                startActivity(intentEdit);
                break;

            case R.id.llFirstItem:

                break;

            case R.id.llSecondItem:
                Intent intentSecond = new Intent(getActivity(), BottomNavigationTwoActivity.class);
                startActivity(intentSecond);
                break;

            case R.id.llThirdItem:
                Intent intenThird = new Intent(getActivity(), BottomNavigationThreeActivity.class);
                startActivity(intenThird);

                break;

            case R.id.llForthItem:

                BarriersPage barriersPage = new BarriersPage(getActivity());
                barriersPage.openOTPDialog(getActivity());
                //openOTPDialog();
                break;

            case R.id.ivSlideImage:
              //openTopSheetDialog();
                break;


            case R.id.llBirthday:

                Intent llBirthday = new Intent(getActivity(), BirthdayActivity.class);
                startActivity(llBirthday);

                break;

            case R.id.llNoticeBoard:

                Intent llNoticeBoard = new Intent(getActivity(), NoticeBoardActivity.class);
                startActivity(llNoticeBoard);

                break;

            case R.id.llSchedule:

                Intent llSchedule = new Intent(getActivity(), ScheduleActivity.class);
                startActivity(llSchedule);

                break;

            case R.id.llGallery:

                Intent llGallery = new Intent(getActivity(), GalleryActivity.class);
                startActivity(llGallery);

                break;

            case R.id.llVisitors:

                Intent llVisitors = new Intent(getActivity(), VisitiorsActivity.class);
                startActivity(llVisitors);

                break;


            case R.id.llAttendance:
                Intent llAttendance = new Intent(getActivity(), AttendanceList.class);
                startActivity(llAttendance);


                break;

            case R.id.ivRefresh:

                callDashboardApis();


                break;

            case R.id.llTicketRaise:

                Intent llTicketRaise = new Intent(getActivity(), StaffTicketActivity.class);
                startActivity(llTicketRaise);


                break;

            case R.id.llBusDetails:

                Intent llBusDetails = new Intent(getActivity(), TransportActivity.class);
                startActivity(llBusDetails);


                break;





        }
    }

    private void logoutMethod() {
        new android.app.AlertDialog.Builder(getActivity())
                .setMessage("Are you sure? Do you want logout.")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        editor.clear();
                        editor2.clear();
                        editor.apply();
                        editor2.apply();


                        Intent intent = new Intent(getActivity(), LoginScreenActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                        getActivity().startActivity(intent);
                        getActivity().finish();
                    }
                }).setNegativeButton("No", null).show();


    }
//For OTP functionality

    private void getSchoolDetailsApi() {
        mApiService.get_school_details(Constant.POC_Mobile_Number, Constant.SCHOOL_ID).enqueue(new Callback<Object>() {


            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if (response.isSuccessful()) {

                    JSONObject object = null;
                    try {
                        object = new JSONObject(new Gson().toJson(response.body()));
                        String message = object.getString("message");
                        String status = object.getString("status");

                        if (status.equalsIgnoreCase("Success")) {
                            Log.d("NOTICE_API_Success", object.toString());

                            JSONObject keyData = object.getJSONObject("data");


                            if (keyData.isNull("POC_Mobile_Number")) {
                                POC_Mobile_Number = "";
                            } else {
                                POC_Mobile_Number = keyData.getLong("POC_Mobile_Number")+"";
                                Constant.POC_Mobile_Number = POC_Mobile_Number;
                            }



                        }
                    } catch (JSONException je) {

                    }

                } else {
                    try {
                        assert response.errorBody() != null;
                        JSONObject object = new JSONObject(response.errorBody().string());
                        String message = object.getString("message");
                        Log.d("ADMIN_API", message);
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



    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onDestroy() {

        super.onDestroy();
        progressDialog.dismiss();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);

    }

    @Override
    public void onStop() {
        super.onStop();
        progressDialog.dismiss();
    }
}

