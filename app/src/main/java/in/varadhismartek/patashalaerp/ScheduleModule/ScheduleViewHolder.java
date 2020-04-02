package in.varadhismartek.patashalaerp.ScheduleModule;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import in.varadhismartek.patashalaerp.R;


public class ScheduleViewHolder extends RecyclerView.ViewHolder {

    // THIS IS FOR RV_LAYOUT_SCHEDULE_ROW_FRONT
    TextView tv_today;
    RecyclerView recycler_view;

    // THIS IS FOR RV_SCHEDULE_ROW_FRONT
    CardView ll_schedule_row;
    TextView tv_event_type, tv_division, tv_fromDate, tv_toDate, tv_event_title;
    RelativeLayout rl_fromDate, rl_toDate;
    ImageView iv_event_image;

    // THIS IS FOR RV_SCHEDULE_DIVISION_ROW
    ImageView iv_close;
    TextView tv_division_name;

    // THIS IS FOR RV_SCHEDULE_ATTACHMENT_ROW
    ImageView iv_attach, iv_cancel;

    // THIS IS FOR  RV_SCHEDULE_VIEW_EXAM_ROW
    TextView tv_classSection, tv_subject, tv_date, tv_time;

    // Constant.RV_ADD_SCHEDULE_CLASS_CHIP:
    TextView tv_className;

    // Constant.RV_ADD_SCHEDULE_ADD_AGENDA:
    TextView tv_fromTime,tv_toTime, tv_agenda_title, tv_description;
    LinearLayout ll_agenda_row;

    // Constant.RV_ADD_SCHEDULE_EXAM_DATE_TIME:
    TextView tv_add_date, tv_add_time,tv_Class,tv_Section;

    RecyclerView rcv_section;

    public ScheduleViewHolder(@NonNull View itemView) {
        super(itemView);

        tv_today = itemView.findViewById(R.id.tv_today);
        recycler_view = itemView.findViewById(R.id.recycler_view);

        tv_event_title = itemView.findViewById(R.id.tv_event_title);
        iv_event_image = itemView.findViewById(R.id.iv_event_image);
        ll_schedule_row = itemView.findViewById(R.id.ll_schedule_row);
        tv_event_type = itemView.findViewById(R.id.tv_event_type);
        tv_division = itemView.findViewById(R.id.tv_division);
        tv_fromDate = itemView.findViewById(R.id.tv_fromDate);
        tv_toDate = itemView.findViewById(R.id.tv_toDate);
        rl_fromDate = itemView.findViewById(R.id.rl_fromDate);
        rl_toDate = itemView.findViewById(R.id.rl_toDate);

      //  iv_close = itemView.findViewById(R.id.iv_close);
        tv_division_name = itemView.findViewById(R.id.tv_division_name);

        iv_attach = itemView.findViewById(R.id.iv_attach);
        iv_cancel = itemView.findViewById(R.id.iv_cancel);

        tv_classSection = itemView.findViewById(R.id.tv_classSection);
        tv_subject = itemView.findViewById(R.id.tv_subject);
        tv_date = itemView.findViewById(R.id.tv_date);
        tv_time = itemView.findViewById(R.id.tv_time);

        tv_className = itemView.findViewById(R.id.tv_className);

        tv_fromTime = itemView.findViewById(R.id.tv_fromTime);
        tv_toTime = itemView.findViewById(R.id.tv_toTime);
        tv_agenda_title = itemView.findViewById(R.id.tv_agenda_title);
        tv_description = itemView.findViewById(R.id.tv_description);
        ll_agenda_row = itemView.findViewById(R.id.ll_agenda_row);

        tv_add_date = itemView.findViewById(R.id.tv_add_date);
        tv_add_time = itemView.findViewById(R.id.tv_add_time);
        rcv_section = itemView.findViewById(R.id.rcv_section);
    }
}
