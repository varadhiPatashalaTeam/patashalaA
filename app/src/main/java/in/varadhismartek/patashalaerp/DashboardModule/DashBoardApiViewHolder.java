package in.varadhismartek.patashalaerp.DashboardModule;

import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import in.varadhismartek.patashalaerp.GeneralClass.CircleImageView;
import in.varadhismartek.patashalaerp.GeneralClass.VerticalMarqueeTextView;
import in.varadhismartek.patashalaerp.R;

class DashBoardApiViewHolder extends RecyclerView.ViewHolder {
    TextView verticalMarqueeTextView;
    TextView tvVehicleRouteNumber,tvVehicleNumber,tvVehicleType,tvVehichleCapacity;

    CircleImageView cvStaffImage;
    TextView tvStaffName,tvStaffID;

    TextView tv_event_title, tv_scheduleType, tv_division, tv_class, tv_section, tv_examDate,tv_fromDate,tv_toDate,tv_subject;
    LinearLayout date_displayer;

    public DashBoardApiViewHolder(View itemView) {
        super(itemView);
        verticalMarqueeTextView = itemView.findViewById(R.id.vtxMarquee);
        tvVehicleRouteNumber = itemView.findViewById(R.id.tvRouteNumber);
        tvVehicleNumber = itemView.findViewById(R.id.tvVehicleNumber);
        tvVehicleType = itemView.findViewById(R.id.tvVehicleType);
        tvVehichleCapacity = itemView.findViewById(R.id.tvVehicleCapacity);

        cvStaffImage=itemView.findViewById(R.id.cvStaffImage);
        tvStaffName=itemView.findViewById(R.id.tvStaffName);
        tvStaffID=itemView.findViewById(R.id.tvStaffID);


        tv_fromDate = itemView.findViewById(R.id.tv_fromDate);
        tv_toDate = itemView.findViewById(R.id.tv_toDate);
        tv_event_title = itemView.findViewById(R.id.tv_event_title);
        tv_scheduleType = itemView.findViewById(R.id.tv_scheduleType);
        tv_division = itemView.findViewById(R.id.tv_division);
        tv_class = itemView.findViewById(R.id.tv_class);
        tv_section = itemView.findViewById(R.id.tv_section);
        tv_examDate = itemView.findViewById(R.id.tv_examDate);
        tv_subject = itemView.findViewById(R.id.tv_subject);
        date_displayer = itemView.findViewById(R.id.date_displayer);

    }
}
