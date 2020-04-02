package in.varadhismartek.patashalaerp.Timetable;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import in.varadhismartek.Utils.Constant;
import in.varadhismartek.patashalaerp.R;

class TimeTableAdapter extends RecyclerView.Adapter<TimeTableViewHolder> {
    Context context;
    ArrayList<PeriodTableModel> tableModels = new ArrayList<>();
    ArrayList<String> daysArrayList = new ArrayList<>();
    ArrayList<Integer> countValue = new ArrayList<>();
    ArrayList<PeriodTableModel> classSectionTimeTable;
    ArrayList<TimetableDetails> timetableDetails;
    String rvType;
    int Counter = 0;
    String dayName;
    String strDay;
    public TimeTableAdapter(Context context, ArrayList<PeriodTableModel> periodTableModels, String rvType) {
        this.context = context;
        this.tableModels = periodTableModels;
        this.rvType = rvType;
        notifyDataSetChanged();
    }

 /*   public TimeTableAdapter(ArrayList<String> dayArrayList, Context context, String rvType) {
        this.daysArrayList = dayArrayList;
        this.context=context;
        this.rvType= rvType;
        notifyDataSetChanged();

    }*/

    public TimeTableAdapter(String rvType, ArrayList<Integer> countValue,
                            Context context) {
        this.rvType = rvType;
        this.countValue = countValue;
        this.context = context;
        notifyDataSetChanged();
    }

    public TimeTableAdapter(ArrayList<PeriodTableModel> classSectionTimeTable, String day,
                            Context context, String rvType) {
        this.classSectionTimeTable = classSectionTimeTable;
        this.strDay = day;
        this.context = context;
        this.rvType = rvType;
        notifyDataSetChanged();

    }

    public TimeTableAdapter(Context context,String rvType,
                            ArrayList<TimetableDetails> timetableDetails) {
        this.context = context;

        this.rvType = rvType;
        this.timetableDetails = timetableDetails;
        notifyDataSetChanged();

    }



    @NonNull
    @Override
    public TimeTableViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (rvType) {
            case Constant.RV_TIMEYABLE_PERIODS:
                return new TimeTableViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.periods_rows, parent, false));

            case Constant.RV_TIMEYABLE_DAYS:
                return new TimeTableViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.dynamic_textview, parent, false));

            case Constant.RV_TIMEYABLE_TEXTVIEW:
                return new TimeTableViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.dynamic_textview, parent, false));

        case Constant.RV_TIMETABLE_NESTED_ROW:
        return new TimeTableViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.dynamic_textview, parent, false));
    }
        return null;
}

    @Override
    public void onBindViewHolder(@NonNull TimeTableViewHolder holder, final int position) {
        switch (rvType) {

            case Constant.RV_TIMEYABLE_TEXTVIEW:

                for (int i = 0; i < countValue.size(); i++) {
                    System.out.println("counterValue**" + i + "===" + countValue.get(position));
                    TextView tv = new TextView(context); // Prepare textview object programmatically
                    LinearLayout.LayoutParams Params1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT);
                    tv.setLayoutParams(Params1);
                    Params1.setMargins(10, 10, 10, 10);
                    tv.setBackgroundResource(R.drawable.spinnerbackground_two);
                    tv.setText("A+");
                    tv.setGravity(Gravity.CENTER);

                    holder.linear_textView.addView(tv); // Add to your ViewGroup using this method
                }
                holder.linear_textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(context, "Click" + position, Toast.LENGTH_SHORT).show();
                    }
                });


                break;


            case Constant.RV_TIMEYABLE_PERIODS:
                String strType = tableModels.get(position).getType_of();
                String strStartTime = tableModels.get(position).getStart_time();
                if (strType.equalsIgnoreCase("Prayer")) {
                    holder.tvPeriods.setText("Prayer" + "\n" + strStartTime);
                    holder.tvPeriods.setTextColor(Color.parseColor("#FFFFFF"));
                    holder.ll_main.setBackgroundColor(Color.parseColor("#FF0000"));
                } else if (strType.equalsIgnoreCase("Period")) {
                    holder.tvPeriods.setText(strStartTime);
                    holder.tvPeriods.setTextColor(Color.parseColor("#52b155"));
                    holder.ll_main.setBackgroundColor(Color.parseColor("#FFFFFF"));

                } else if (strType.equalsIgnoreCase("Break")) {
                    holder.tvPeriods.setText("Break" + "\n" + strStartTime);
                    holder.tvPeriods.setTextColor(Color.parseColor("#FFFFFF"));
                    holder.ll_main.setBackgroundColor(Color.parseColor("#097db7"));
                } else if (strType.equalsIgnoreCase("Lunch")) {
                    holder.tvPeriods.setText("Lunch" + "\n" + strStartTime);
                    holder.tvPeriods.setTextColor(Color.parseColor("#FFFFFF"));
                    holder.ll_main.setBackgroundColor(Color.parseColor("#f2790d"));
                }
                break;

            case Constant.RV_TIMEYABLE_DAYS:

                holder.tvSub.setText(strDay);

                TimeTableAdapter adapter = new TimeTableAdapter(context, Constant.RV_TIMETABLE_NESTED_ROW,
                        classSectionTimeTable.get(position).getTimetableDetails());

                holder.recyclerView_2.setLayoutManager(new LinearLayoutManager(context));
                holder.recyclerView_2.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                break;

            case Constant.RV_TIMETABLE_NESTED_ROW:
                holder.tvSub.setText(timetableDetails.get(position).getSubject() + "\n"
                        + timetableDetails.get(position).getStrStart_time());
                holder.recyclerView_2.setVisibility(View.GONE);
                break;
        }


    }

    @Override
    public int getItemCount() {
        switch (rvType) {
            case Constant.RV_TIMEYABLE_PERIODS:
                return tableModels.size();
            case Constant.RV_TIMEYABLE_DAYS:
                return classSectionTimeTable.size();
            //  return daysArrayList.size();
            case Constant.RV_TIMEYABLE_TEXTVIEW:
                return countValue.size();
            case Constant.RV_TIMETABLE_NESTED_ROW:
                return timetableDetails.size();
        }
        return 0;
    }
}
