package in.varadhismartek.patashalaerp.ScheduleModule;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import in.varadhismartek.Utils.Constant;
import in.varadhismartek.patashalaerp.DashboardModule.Assessment.Fragment_ScheduleBarriersList;
import in.varadhismartek.patashalaerp.DashboardModule.DashboardActivity;
import in.varadhismartek.patashalaerp.DashboardModule.Homework.MyModel;
import in.varadhismartek.patashalaerp.R;
import in.varadhismartek.patashalaerp.Retrofit.APIService;
import in.varadhismartek.patashalaerp.Retrofit.ApiUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleViewHolder> {

    private Context mContext;
    private ArrayList<String> strList;
    private String recyclerTag;
    private ArrayList<MyScheduleModel> myScheduleList;
    private ArrayList<ScheduleModel> scheduleList, scheduleBarriesList;
    private String strAcdStartDate, strAcdEndDate;
    private String acd_startdate, acd_enddate, from_date, to_date, schedule_type, session_serial_no;
    ArrayList<MyNewModel> myModelsList;

    Fragment_ScheduleBarriersList.Refersh refersh;

    public ScheduleAdapter(Context mContext, ArrayList<String> strList, String recyclerTag) {
        this.mContext = mContext;
        this.strList = strList;
        this.recyclerTag = recyclerTag;

    }

    public ScheduleAdapter(ArrayList<MyScheduleModel> myScheduleList, Context mContext, String recyclerTag) {
        this.mContext = mContext;
        this.recyclerTag = recyclerTag;
        this.myScheduleList = myScheduleList;


    }

    public ScheduleAdapter(ArrayList<ScheduleModel> scheduleList, String recyclerTag, Context mContext) {
        this.mContext = mContext;
        this.recyclerTag = recyclerTag;
        this.scheduleList = scheduleList;
        this.scheduleBarriesList = scheduleList;
    }

    public ScheduleAdapter(ArrayList<ScheduleModel> scheduleList, String recyclerTag, Context mContext,
                           Fragment_ScheduleBarriersList.Refersh refersh) {

        this.mContext = mContext;
        this.recyclerTag = recyclerTag;
        this.scheduleBarriesList = scheduleList;
        this.refersh = refersh;
    }

    public ScheduleAdapter( Context mContext, String recyclerTag,ArrayList<MyNewModel> myModelsList) {
        this.mContext = mContext;
        this.recyclerTag = recyclerTag;
        this.myModelsList = myModelsList;

    }


    @NonNull
    @Override
    public ScheduleViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        switch (recyclerTag) {

            case Constant.RV_LAYOUT_SCHEDULE_ROW_FRONT:
                return new ScheduleViewHolder(LayoutInflater.from(mContext)
                        .inflate(R.layout.layout_schedule_front, viewGroup, false));

            case Constant.RV_SCHEDULE_ROW_FRONT:
                return new ScheduleViewHolder(LayoutInflater.from(mContext)
                        .inflate(R.layout.layout_schedule_row, viewGroup, false));

            case Constant.RV_SCHEDULE_BARRIER_LIST:
                return new ScheduleViewHolder(LayoutInflater.from(mContext)
                        .inflate(R.layout.layout_schedule_row, viewGroup, false));

            case Constant.RV_HOLIDAY_LIST:
                return new ScheduleViewHolder(LayoutInflater.from(mContext)
                        .inflate(R.layout.layout_schedule_row, viewGroup, false));

            case Constant.RV_SCHEDULE_LIST_ROW:
                return new ScheduleViewHolder(LayoutInflater.from(mContext)
                        .inflate(R.layout.layout_schedule_row, viewGroup, false));

            case Constant.RV_SCHEDULE_VIEW_EXAM_ROW:
                return new ScheduleViewHolder(LayoutInflater.from(mContext)
                        .inflate(R.layout.layout_view_schedule_row, viewGroup, false));

            case Constant.RV_EXAM_NESTED_LIST:
                return new ScheduleViewHolder(LayoutInflater.from(mContext)
                        .inflate(R.layout.exam_view_schedule_row, viewGroup, false));

            case Constant.RV_EVENT_NESTED_LIST:
                return new ScheduleViewHolder(LayoutInflater.from(mContext)
                        .inflate(R.layout.class_chip, viewGroup, false));

            case Constant.RV_HOLIDAY_NESTED_LIST:
                return new ScheduleViewHolder(LayoutInflater.from(mContext)
                        .inflate(R.layout.class_chip, viewGroup, false));

            case Constant.RV_ADD_SCHEDULE_EXAM_DATE_TIME:
                return new ScheduleViewHolder(LayoutInflater.from(mContext)
                        .inflate(R.layout.add_exam_row, viewGroup, false));

            case Constant.RV_ADD_SCHEDULE_CLASS_UPER:
                return new ScheduleViewHolder(LayoutInflater.from(mContext)
                        .inflate(R.layout.layout_class_section_row, viewGroup, false));

            case Constant.RV_ADD_NESTED_SECTION:
                return new ScheduleViewHolder(LayoutInflater.from(mContext)
                        .inflate(R.layout.chip_layout_orange, viewGroup, false));

        }

        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ScheduleViewHolder holder, final int i) {


        switch (recyclerTag) {


            case Constant.RV_LAYOUT_SCHEDULE_ROW_FRONT:

                String fromDate = myScheduleList.get(i).getDate();

                holder.tv_date.setText(fromDate);

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat monthFormat = new SimpleDateFormat("dd-MMM-yyyy");

                Date date1 = null;

                try {
                    date1 = simpleDateFormat.parse(fromDate);

                } catch (ParseException e) {
                    e.printStackTrace();
                }

                holder.tv_date.setText(monthFormat.format(date1).toString());

                ScheduleAdapter adapter = new ScheduleAdapter(myScheduleList.get(i).getScheduleModelList(),Constant.RV_SCHEDULE_ROW_FRONT, mContext);
                holder.recycler_view.setLayoutManager(new LinearLayoutManager(mContext));
                holder.recycler_view.setNestedScrollingEnabled(false);
                holder.recycler_view.setAdapter(adapter);

                break;

            case Constant.RV_SCHEDULE_ROW_FRONT:

                if (scheduleList.get(i).getSchedule_image().length()>0){
                    Glide.with(mContext)
                            .load(Constant.IMAGE_URL+scheduleList.get(i).getSchedule_image())
                            .into(holder.iv_event_image);
                }

                holder.tv_event_title.setText(scheduleList.get(i).getSchedule_title());
                holder.tv_division.setText(scheduleList.get(i).getDivision());
                holder.tv_fromDate.setText(scheduleList.get(i).getFrom_date().substring(0,10));
                holder.tv_toDate.setText(scheduleList.get(i).getTo_date().substring(0,10));
                holder.tv_event_type.setText(scheduleList.get(i).getSchedule_type());

                holder.ll_schedule_row.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        ScheduleActivity scheduleActivity = (ScheduleActivity) mContext;
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("ScheduleModel", scheduleList.get(i));
                        scheduleActivity.loadFragment(Constant.SCHEDULE_LIST_FRAGMENT, bundle);
                    }
                });


                break;



            case Constant.RV_SCHEDULE_BARRIER_LIST:
               // holder.tv_Class.setVisibility(View.GONE);
                holder.tv_Section.setVisibility(View.GONE);
                System.out.println("S_TODATE**" + scheduleBarriesList.get(i).getTo_date());
                if (scheduleBarriesList.get(i).getSchedule_image().length() > 0) {
                    Glide.with(mContext)
                            .load(Constant.IMAGE_LINK + scheduleBarriesList.get(i).getSchedule_image())
                            .into(holder.iv_event_image);
                }
                //holder.tv_Class.setText(myScheduleList.get(i).getTitle());

                holder.tv_event_title.setVisibility(View.VISIBLE);
                holder.tv_event_title.setText(scheduleBarriesList.get(i).getSchedule_title());
                holder.tv_division.setText(scheduleBarriesList.get(i).getStrdivision());
                holder.tv_fromDate.setText(scheduleBarriesList.get(i).getFrom_date());
                holder.tv_toDate.setText(scheduleBarriesList.get(i).getTo_date());
                holder.tv_event_type.setText(scheduleBarriesList.get(i).getSchedule_type());


                holder.ll_schedule_row.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        acd_startdate = scheduleBarriesList.get(i).getAcademic_start_date();
                        acd_enddate = scheduleBarriesList.get(i).getAcademic_end_date();
                        from_date = scheduleBarriesList.get(i).getFrom_date();
                        to_date = scheduleBarriesList.get(i).getTo_date();
                        schedule_type = scheduleBarriesList.get(i).getSchedule_type();
                        session_serial_no = scheduleBarriesList.get(i).getSession_serial_no();


                        SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

                        SimpleDateFormat outputFormate = new SimpleDateFormat("yyyy-MM-dd");

                        try {

                            Date fromYear = formater.parse(from_date);
                            Date toYear = formater.parse(to_date);


                            strAcdStartDate = outputFormate.format(fromYear);
                            strAcdEndDate = outputFormate.format(toYear);


                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                        builder.setMessage("Are you sure you want to Delete?")
                                .setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                        Log.d("ADMINDDDD1", "" + acd_startdate);
                                        Log.d("ADMINDDDD2", "" + acd_enddate);
                                        Log.d("ADMINDDDD2", "" + strAcdStartDate);
                                        Log.d("ADMINDDDD2", "" + strAcdEndDate);
                                        Log.d("ADMINDDDD2", "" + session_serial_no);

                                        APIService apiService = ApiUtils.getAPIService();


                                        apiService.delete_schedule(Constant.SCHOOL_ID,
                                                Constant.EMPLOYEE_BY_ID,
                                                session_serial_no,
                                                acd_startdate, acd_enddate,
                                                strAcdStartDate, strAcdEndDate, schedule_type
                                        ).enqueue(new Callback<Object>() {
                                            @Override
                                            public void onResponse(Call<Object> call, Response<Object> response) {

                                                 if (response.isSuccessful()) {
                                                     Log.d("ADMIN_API_Delete", "" + response.code() + response.body());
                                                     Toast.makeText(mContext, "Delete successfully", Toast.LENGTH_SHORT).show();
                                                    scheduleBarriesList.remove(i);
                                                    notifyDataSetChanged();
                                                    refersh.pageRefersh();
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
                                                Log.d("ADMIN_API_DeleteE", "" + t.getMessage());

                                            }
                                        });


                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.show();

                        return false;
                    }
                });
                break;

            case Constant.RV_HOLIDAY_LIST:
                System.out.println("S_TODATE**" + scheduleBarriesList.get(i).getTo_date());
                if (scheduleBarriesList.get(i).getSchedule_image().length() > 0) {
                    Glide.with(mContext)
                            .load(Constant.IMAGE_LINK + scheduleBarriesList.get(i).getSchedule_image())
                            .into(holder.iv_event_image);
                }
                holder.tv_Class.setText(scheduleBarriesList.get(i).getStrclasses());
                holder.tv_Section.setText(scheduleBarriesList.get(i).getStrsection());

                holder.tv_event_title.setText(scheduleBarriesList.get(i).getSchedule_title());
                holder.tv_division.setText(scheduleBarriesList.get(i).getStrdivision());
                holder.tv_fromDate.setText(scheduleBarriesList.get(i).getFrom_date());
                holder.tv_toDate.setText(scheduleBarriesList.get(i).getTo_date());
                holder.tv_event_type.setText(scheduleBarriesList.get(i).getSchedule_type());


                break;

            case Constant.RV_SCHEDULE_LIST_ROW:

                holder.tv_event_title.setText(myScheduleList.get(i).getExamName());
                holder.tv_division.setText(myScheduleList.get(i).getDivisionName());
                holder.tv_fromDate.setText(myScheduleList.get(i).getFrom_date().substring(0,10));
                holder.tv_toDate.setText(myScheduleList.get(i).getTo_date().substring(0,10));
                holder.tv_event_type.setText(myScheduleList.get(i).getScheduleType());

                holder.ll_schedule_row.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ScheduleActivity scheduleActivity = (ScheduleActivity) mContext;
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("myScheduleList", myScheduleList.get(i));
                        scheduleActivity.loadFragment(Constant.SCHEDULE_DETAILS_FRAGMENT, bundle);
                    }
                });

                break;

            case Constant.RV_SCHEDULE_VIEW_EXAM_ROW:

                holder.tv_date.setText(myModelsList.get(i).getDate());

                ScheduleAdapter adapterExam = new ScheduleAdapter(myModelsList.get(i).scheduleModelList,
                        Constant.RV_EXAM_NESTED_LIST,mContext);
                holder.recycler_view.setLayoutManager(new LinearLayoutManager(mContext));
                holder.recycler_view.setAdapter(adapterExam);

                break;

            case Constant.RV_EXAM_NESTED_LIST:

                Log.d("scheduleList_exam_1", scheduleList.size()+"");

                String className = scheduleList.get(i).getClassName();
                String section = scheduleList.get(i).getSection();

                holder.tv_classSection.setText(className+" (" +section+ ")");
                holder.tv_subject.setText(scheduleList.get(i).getSubject());
                holder.tv_date.setText(scheduleList.get(i).getStart_time());
                holder.tv_time.setText(scheduleList.get(i).getExam_duration());

                break;

            case Constant.RV_HOLIDAY_NESTED_LIST:
                holder.tv_className.setText(myModelsList.get(i).getClassName());

                break;

            case Constant.RV_EVENT_NESTED_LIST:
                holder.tv_className.setText(myModelsList.get(i).getClassName());

                break;

            case Constant.RV_ADD_SCHEDULE_EXAM_DATE_TIME:

                holder.tv_subject.setText(myScheduleList.get(i).getSubject());
                holder.tv_add_date.setText(myScheduleList.get(i).getDate());
                holder.tv_add_time.setText(myScheduleList.get(i).getTime());
                break;

            case Constant.RV_ADD_SCHEDULE_CLASS_UPER:

                holder.tv_className.setText(myScheduleList.get(i).getClassName());

                ScheduleAdapter scheduleAdapter = new ScheduleAdapter(mContext,
                        myScheduleList.get(i).getSectionList(), Constant.RV_ADD_NESTED_SECTION);
                holder.rcv_section.setLayoutManager(new GridLayoutManager(mContext, 2));
                holder.rcv_section.setAdapter(scheduleAdapter);

                break;

            case Constant.RV_ADD_NESTED_SECTION:
                holder.tv_division_name.setText(strList.get(i));

                break;




        }
    }

    @Override
    public int getItemCount() {

        switch (recyclerTag) {

            case Constant.RV_LAYOUT_SCHEDULE_ROW_FRONT:
                return myScheduleList.size();

            case Constant.RV_SCHEDULE_ROW_FRONT:
                return scheduleList.size();

            case Constant.RV_SCHEDULE_BARRIER_LIST:
                return scheduleBarriesList.size();

            case Constant.RV_HOLIDAY_LIST:
                return scheduleBarriesList.size();

            case Constant.RV_SCHEDULE_LIST_ROW:
                return myScheduleList.size();

            case Constant.RV_SCHEDULE_VIEW_EXAM_ROW:
                return myModelsList.size();

            case Constant.RV_EXAM_NESTED_LIST:
                return scheduleList.size();

            case Constant.RV_EVENT_NESTED_LIST:
                return myModelsList.size();

            case Constant.RV_HOLIDAY_NESTED_LIST:
                return myModelsList.size();

            case Constant.RV_ADD_SCHEDULE_EXAM_DATE_TIME:
                return myScheduleList.size();

            case Constant.RV_ADD_SCHEDULE_CLASS_UPER:
                return myScheduleList.size();

            case Constant.RV_ADD_NESTED_SECTION:
                return strList.size();


        }

        return 0;
    }

}
