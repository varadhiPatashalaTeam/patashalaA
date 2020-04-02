package in.varadhismartek.patashalaerp.DashboardModule.EmployeeModule;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import in.varadhismartek.patashalaerp.DashboardModule.calenderView.GridAdapter;
import in.varadhismartek.patashalaerp.R;


public class AttendenceGridAdapter extends ArrayAdapter {

    private static final String TAG = GridAdapter.class.getSimpleName();
    private LayoutInflater mInflater;
    private List<Date> monthlyDates;
    private Calendar currentDate;
    private ArrayList<AttendanceModel> attendanceList;
    Context context;
    AttendenceCalander.MyDateClickListener myDateClickListener;

    public AttendenceGridAdapter(Context context, List<Date> monthlyDates, Calendar currentDate,
                                 ArrayList<AttendanceModel> attendanceList, AttendenceCalander.MyDateClickListener myDateClickListener) {
        super(context, R.layout.single_cell_layout);
        this.context = context;
        this.monthlyDates = monthlyDates;
        this.currentDate = currentDate;
        this.attendanceList = attendanceList;
        this.myDateClickListener = myDateClickListener;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Date mDate = monthlyDates.get(position);
        Calendar dateCal = Calendar.getInstance();
        dateCal.setTime(mDate);
        int dayValue = dateCal.get(Calendar.DAY_OF_MONTH);
        int displayMonth = dateCal.get(Calendar.MONTH) + 1;
        int displayYear = dateCal.get(Calendar.YEAR);
        int currentMonth = currentDate.get(Calendar.MONTH) + 1;
        int currentYear = currentDate.get(Calendar.YEAR);
        View view = convertView;

        if(view == null){
            view = mInflater.inflate(R.layout.attendance_cell_layout, parent, false);
        }

        TextView cellNumber = view.findViewById(R.id.calendar_date_id);
        RelativeLayout rl_calendar_circle = view.findViewById(R.id.rl_calendar_circle);

        if(displayMonth == currentMonth && displayYear == currentYear){

            Log.d("CLONE",currentMonth+" "+currentYear);

            view.setBackgroundColor(Color.parseColor("#FFFFFF"));

            final int clone_year  = dateCal.get(Calendar.YEAR);
            final int clone_month = dateCal.get(Calendar.MONTH)+1;
            final int clone_day   = dateCal.get(Calendar.DAY_OF_MONTH);
            int day_name = dateCal.get(Calendar.DAY_OF_WEEK);

            Calendar cal = Calendar.getInstance();

            int current_year  = cal.get(Calendar.YEAR);
            int current_month = cal.get(Calendar.MONTH)+1;
            int current_day   = cal.get(Calendar.DAY_OF_MONTH);

            if(current_year == clone_year && current_month == clone_month && current_day == clone_day){
                rl_calendar_circle.setBackgroundResource(R.drawable.button_shape_other);
                cellNumber.setTextColor(Color.WHITE);

            }

            if(day_name == 1){
                cellNumber.setTextColor(Color.RED);

            }

            view.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {

                    Toast.makeText(context,clone_day+"-"+clone_month+"-"+clone_year, Toast.LENGTH_SHORT).show();

                    String loginTime = "";
                    String logoutTime = "";
                    String totalTime = "";

                    for (int i = 0; i<attendanceList.size();i++){

                        if (attendanceList.get(i).getDate().equals(clone_day+"-"+clone_month+"-"+clone_year)){

                            loginTime  = attendanceList.get(i).getLogIn_time();
                            logoutTime = attendanceList.get(i).getLogOut_time();
                            totalTime  = attendanceList.get(i).getTotal_time();

                            Log.d("Time_IF", loginTime+" "+logoutTime+" "+totalTime);
                        }

                        Log.d("Time_Nor", loginTime+" "+logoutTime+" "+totalTime);

                        myDateClickListener.dateClick(loginTime, logoutTime, totalTime);
                    }

                    //call api for for get schedule for perticular date
                }
            });

        }else{
            view.setBackgroundColor(Color.parseColor("#F5F5F5"));
            view.setVisibility(View.GONE);

        }
        //Add day to calendar
        cellNumber.setText(String.valueOf(dayValue));
        //Add events to the calendar
        Calendar eventCalendar = Calendar.getInstance();

        // get the list from Api for attendance   date and status = present/absent/late

        Log.d("attendanceList_size", String.valueOf(attendanceList.size()));

        ArrayList<Date> dates  = new ArrayList<>();



        for (int i = 0; i<attendanceList.size();i++){

            SimpleDateFormat dateFormat1 = new SimpleDateFormat("dd-MM-yyyy");
            Date date = null;

            try {

                Log.i("AttendanceDate_",""+dateFormat1);

                date = dateFormat1.parse(attendanceList.get(i).getDate());
                dates.add(date);

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        for (int j = 0; j < attendanceList.size(); j++){

            eventCalendar.setTime(dates.get(j));

            if(dayValue == eventCalendar.get(Calendar.DAY_OF_MONTH) && displayMonth == eventCalendar.get(Calendar.MONTH) + 1
                    && displayYear == eventCalendar.get(Calendar.YEAR)){

                switch (attendanceList.get(j).getStatus()){

                    case "Present":
                        rl_calendar_circle.setBackgroundResource(R.drawable.ring_shape3);
                        cellNumber.setTextColor(Color.WHITE);
                        break;

                    case "Late":
                        rl_calendar_circle.setBackgroundResource(R.drawable.circle_yellow);
                        cellNumber.setTextColor(Color.WHITE);
                        break;

                    case "Absent":

                        rl_calendar_circle.setBackgroundResource(R.drawable.circle_red);
                        cellNumber.setTextColor(Color.WHITE);
                        break;

                }

            }
        }

        return view;

    }

    @Override
    public int getCount() {
        return monthlyDates.size();
    }

    @Override
    public Object getItem(int position) {
        return monthlyDates.get(position);
    }

    @Override
    public int getPosition( Object item) {
        return monthlyDates.indexOf(item);
    }


}