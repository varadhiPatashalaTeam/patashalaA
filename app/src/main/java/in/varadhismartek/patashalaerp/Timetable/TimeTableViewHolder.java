package in.varadhismartek.patashalaerp.Timetable;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import in.varadhismartek.patashalaerp.R;

public class TimeTableViewHolder extends RecyclerView.ViewHolder {
    public TextView tvPeriods,tvDays,tvSub;
    public LinearLayout ll_main;
    public LinearLayout linear_textView;
    public RecyclerView recyclerView_2;


    public TimeTableViewHolder(View itemView) {
        super(itemView);
        tvPeriods = itemView.findViewById(R.id.tv_period_no);
        tvDays = itemView.findViewById(R.id.tv_days);
        ll_main = itemView.findViewById(R.id.ll_main);
        linear_textView = itemView.findViewById(R.id.linear_textView);
        tvSub = itemView.findViewById(R.id.tvSub);
        recyclerView_2 = itemView.findViewById(R.id.recyclerView_2);
    }
}
