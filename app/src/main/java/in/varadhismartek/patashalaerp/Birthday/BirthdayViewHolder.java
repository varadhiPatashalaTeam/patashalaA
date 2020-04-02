package in.varadhismartek.patashalaerp.Birthday;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import in.varadhismartek.patashalaerp.R;


class BirthdayViewHolder extends RecyclerView.ViewHolder {

    TextView tv_name,tv_id,tv_date;
    ImageView iv_image;

    //transport
    public TextView tv_vehicleId,tv_vehicleName,tv_vehicleReg,tv_vehicleType,tv_vehicleDetails;
    public LinearLayout ll_vehicle;

    public BirthdayViewHolder(@NonNull View itemView) {
        super(itemView);

        tv_name = itemView.findViewById(R.id.tv_name);
        tv_id = itemView.findViewById(R.id.tv_id);
        tv_date = itemView.findViewById(R.id.tv_date);
        iv_image = itemView.findViewById(R.id.iv_image);


        tv_vehicleId = itemView.findViewById(R.id.tv_vehicleId);
        tv_vehicleName = itemView.findViewById(R.id.tv_vehicleName);
        tv_vehicleReg = itemView.findViewById(R.id.tv_vehicleReg);
        tv_vehicleType = itemView.findViewById(R.id.tv_vehicleType);
        tv_vehicleDetails = itemView.findViewById(R.id.tv_vehicleDetails);
        ll_vehicle = itemView.findViewById(R.id.ll_vehicle);
    }
}
