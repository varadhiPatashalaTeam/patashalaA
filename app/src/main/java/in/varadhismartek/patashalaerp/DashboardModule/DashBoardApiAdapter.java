package in.varadhismartek.patashalaerp.DashboardModule;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import in.varadhismartek.Utils.Constant;
import in.varadhismartek.patashalaerp.DashboardModule.TransportModule.RouteList;
import in.varadhismartek.patashalaerp.R;

public class DashBoardApiAdapter extends RecyclerView.Adapter<DashBoardApiViewHolder> {


    ArrayList<DashBoardApiModel> dashBoardApiModelArrayList;
    ArrayList<RouteList> routeListArrayList;
    Context mContext;
    String tag;


    public DashBoardApiAdapter(ArrayList<DashBoardApiModel> dashBoardApiModelArrayList, Context mContext, String tag) {
        this.dashBoardApiModelArrayList = dashBoardApiModelArrayList;
        this.mContext = mContext;
        this.tag = tag;
    }



    @NonNull
    @Override
    public DashBoardApiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (tag) {

            case Constant.RV_NOTICEBOARD_ROW:
                return new DashBoardApiViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.dashboard_notice_row, parent, false));


            case Constant.RV_TRANSPORT_BUS_LIST_ROW:
                return new DashBoardApiViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.bus_details_row, parent, false));

            case Constant.RV_BIRTHDAY_ROW:
                return new DashBoardApiViewHolder(LayoutInflater.from(mContext)
                        .inflate(R.layout.birthday_dashboard_row, parent, false));

            case Constant.RV_SCHEDULE_ROW_FRONT:
                return new DashBoardApiViewHolder(LayoutInflater.from(mContext)
                        .inflate(R.layout.events_schedule_dashboard_row, parent, false));



            default: return null;


        }
    }

    @Override
    public void onBindViewHolder(@NonNull DashBoardApiViewHolder holder, int position) {

        switch (tag){

            case Constant.RV_NOTICEBOARD_ROW:

                holder.verticalMarqueeTextView.setText(dashBoardApiModelArrayList.get(position).getNotice_message());
                holder.verticalMarqueeTextView.setMovementMethod(new ScrollingMovementMethod());

                break;


            case Constant.RV_TRANSPORT_BUS_LIST_ROW:


                holder.tvVehicleRouteNumber.setText("R-"+dashBoardApiModelArrayList.get(position).getRoute_number());
                holder.tvVehicleNumber.setText(dashBoardApiModelArrayList.get(position).getVehicle_id());
                holder.tvVehicleType.setText(dashBoardApiModelArrayList.get(position).getClass_of_vehicle());
                holder.tvVehichleCapacity.setText(dashBoardApiModelArrayList.get(position).getSeating_capacity());


                break;

            case Constant.RV_BIRTHDAY_ROW:

                Glide.with(mContext).load(Constant.IMAGE_URL+dashBoardApiModelArrayList.get(position).getImage()).into(holder.cvStaffImage);
                holder.tvStaffName.setText(dashBoardApiModelArrayList.get(position).getFirstName()+" "+dashBoardApiModelArrayList.get(position).getLastName() );
                holder.tvStaffID.setText(dashBoardApiModelArrayList.get(position).getCustomID());

                break;

            case Constant.RV_SCHEDULE_ROW_FRONT:
                holder.tv_subject.setText(dashBoardApiModelArrayList.get(position).getScheduleName());
                if (dashBoardApiModelArrayList.get(position).getScheduleType().equalsIgnoreCase("Exam")){
                    holder.date_displayer.setVisibility(View.VISIBLE);
                    holder.tv_examDate.setText(dashBoardApiModelArrayList.get(position).getExamTime());
                }
                holder.tv_event_title.setText(dashBoardApiModelArrayList.get(position).getScheduleTitle());
                holder.tv_class.setText(dashBoardApiModelArrayList.get(position).getClassName());
                holder.tv_section.setText(dashBoardApiModelArrayList.get(position).getSection());
                holder.tv_division.setText(dashBoardApiModelArrayList.get(position).getDivision());
                holder.tv_scheduleType.setText(dashBoardApiModelArrayList.get(position).getScheduleType());
                break;

        }

    }

    @Override
    public int getItemCount() {
        return dashBoardApiModelArrayList.size();
    }
}
