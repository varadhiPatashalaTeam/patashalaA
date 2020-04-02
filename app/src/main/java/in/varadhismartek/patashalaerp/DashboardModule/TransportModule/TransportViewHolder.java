package in.varadhismartek.patashalaerp.DashboardModule.TransportModule;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import in.varadhismartek.patashalaerp.R;

class TransportViewHolder extends RecyclerView.ViewHolder {

    //transport
    public TextView tv_vehicleId,tv_vehicleName,tv_vehicleReg,tv_vehicleType,tv_vehicleDetails;
    public LinearLayout ll_vehicle;

    // stop
    public TextView tv_StopNo,tv_StopName,tv_StopDistance,tv_ArrivalTime;
    public ImageView iv_StopImage;
    public LinearLayout ll_vehicleList;


    public TransportViewHolder(View itemView) {
        super(itemView);


        tv_vehicleId = itemView.findViewById(R.id.tv_vehicleId);
        tv_vehicleName = itemView.findViewById(R.id.tv_vehicleName);
        tv_vehicleReg = itemView.findViewById(R.id.tv_vehicleReg);
        tv_vehicleType = itemView.findViewById(R.id.tv_vehicleType);
        tv_vehicleDetails = itemView.findViewById(R.id.tv_vehicleDetails);
        ll_vehicle = itemView.findViewById(R.id.ll_vehicle);

        //vehicle stop details

        tv_StopNo = itemView.findViewById(R.id.tv_StopNo);
        tv_StopName = itemView.findViewById(R.id.tv_StopName);
        tv_StopDistance = itemView.findViewById(R.id.tv_StopDistance);
        tv_ArrivalTime = itemView.findViewById(R.id.tv_ArrivalTime);
        iv_StopImage = itemView.findViewById(R.id.iv_StopImage);
        ll_vehicleList = itemView.findViewById(R.id.ll_vehicleList);
    }
}
