package in.varadhismartek.patashalaerp.DashboardModule.TransportModule;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import in.varadhismartek.Utils.Constant;
import in.varadhismartek.patashalaerp.R;

public class TransportAdapter extends RecyclerView.Adapter<TransportViewHolder> {

    ArrayList<RouteList> routeListArrayList;
    Context mContext;
    String tag;
    private int lastPosition = -1;


    public TransportAdapter(ArrayList<RouteList> routeListArrayList, Context mContext, String tag) {
        this.routeListArrayList = routeListArrayList;
        this.mContext = mContext;
        this.tag = tag;
    }


    @NonNull
    @Override
    public TransportViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (tag) {

            case Constant.RV_BIRTHDAY_ROW:
                return new TransportViewHolder(LayoutInflater.from(mContext)
                        .inflate(R.layout.birthday_card_layout, parent, false));

            case Constant.RV_TRANSPORT_BUS_LIST_ROW:
                return new TransportViewHolder(LayoutInflater.from(mContext)
                        .inflate(R.layout.trans_bus_list_card_layout, parent, false));

            case Constant.RV_VEHICLE_STOP_ROW:
                return new TransportViewHolder(LayoutInflater.from(mContext)
                        .inflate(R.layout.stop_card_list, parent, false));
        }

        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull TransportViewHolder holder, final int position) {

        switch (tag) {
            case Constant.RV_VEHICLE_STOP_ROW:

                holder.tv_StopNo.setText(routeListArrayList.get(position).getStopNumber());
                holder.tv_StopName.setText(routeListArrayList.get(position).getStopName());
                holder.tv_StopDistance.setText(routeListArrayList.get(position).getStopDistance());
                String strApproxTime = routeListArrayList.get(position).getApproxStopTime();

                String[] strSpliteTime = strApproxTime.split(":");
                String str1 = strSpliteTime[0];
                String str2 = strSpliteTime[1];
                holder.tv_ArrivalTime.setText(routeListArrayList.get(position).getApproxStopTime());


                if (!routeListArrayList.get(position).getStopImage().equals("")) {
                    Glide.with(mContext).load(Constant.IMAGE_URL + routeListArrayList.get(position).getStopImage())
                            .into(holder.iv_StopImage);
                }
                break;


            case Constant.RV_TRANSPORT_BUS_LIST_ROW:
                //tv_vehicleId,tv_vehicleName,tv_vehicleReg,tv_vehicleType,tv_vehicleDetails;
                String vehicleUUID = routeListArrayList.get(position).getVehicle_uuid();

                holder.tv_vehicleId.setText(routeListArrayList.get(position).getVehicle_id());
                holder.tv_vehicleName.setText(routeListArrayList.get(position).getClass_of_vehicle());
                holder.tv_vehicleReg.setText(routeListArrayList.get(position).getRegistration_no());
                holder.tv_vehicleType.setText(routeListArrayList.get(position).getType_of_body());
                holder.tv_vehicleDetails.setText(routeListArrayList.get(position).getGPS_Details());

                holder.ll_vehicle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle bundle = new Bundle();
                        bundle.putString("VEHICLE_ID", routeListArrayList.get(position).getVehicle_id());
                        bundle.putString("VEHICLE_UUID", routeListArrayList.get(position).getVehicle_uuid());
                        bundle.putSerializable("VEHICLE_ROUTELIST", routeListArrayList.get(position).getRouteLists());

                        Log.i("Route_list***", "" + routeListArrayList.get(position).getRouteLists().size());

                        TransportLandingActivity transportActivity = (TransportLandingActivity) mContext;
                        transportActivity.loadFragment(Constant.TRANSPORT_DETAILS_FRAGMENT, bundle);
                    }
                });

                setAnimation(holder.ll_vehicleList, position);
                break;
        }

    }

    @Override
    public int getItemCount() {
        switch (tag) {

            case Constant.RV_TRANSPORT_BUS_LIST_ROW:
                return routeListArrayList.size();

            case Constant.RV_VEHICLE_STOP_ROW:
                return routeListArrayList.size();
        }
        return 0;
    }
//MetV!kz@1087#
    private void setAnimation(View ll_vehicleList, int position) {
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.left_to_right);
            ll_vehicleList.startAnimation(animation);
            lastPosition = position;
        }
    }
}
