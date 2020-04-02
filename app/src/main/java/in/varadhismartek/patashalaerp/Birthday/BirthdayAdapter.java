package in.varadhismartek.patashalaerp.Birthday;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import in.varadhismartek.Utils.Constant;
import in.varadhismartek.patashalaerp.DashboardModule.TransportModule.TransportActivity;
import in.varadhismartek.patashalaerp.R;

public class BirthdayAdapter extends RecyclerView.Adapter<BirthdayViewHolder> {

    String recyclerTag;
    Context context;
    ArrayList<BirthdayModel> list,transportBusList;

    public BirthdayAdapter(ArrayList<BirthdayModel> list, Context context, String recyclerTag ) {
        this.recyclerTag = recyclerTag;
        this.context = context;
        this.list = list;
        this.transportBusList = list;
    }

    @NonNull
    @Override
    public BirthdayViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        switch (recyclerTag){

            case Constant.RV_BIRTHDAY_ROW:
                return new BirthdayViewHolder(LayoutInflater.from(context)
                        .inflate(R.layout.birthday_card_layout, parent, false));

                case Constant.RV_TRANSPORT_BUS_LIST_ROW:
                return new BirthdayViewHolder(LayoutInflater.from(context)
                        .inflate(R.layout.trans_bus_list_card_layout, parent, false));
        }

        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull BirthdayViewHolder holder, final int position) {

        switch (recyclerTag){
            case Constant.RV_TRANSPORT_BUS_LIST_ROW:
                //tv_vehicleId,tv_vehicleName,tv_vehicleReg,tv_vehicleType,tv_vehicleDetails;
                String vehicleUUID =transportBusList.get(position).getVehicle_uuid();

                holder.tv_vehicleId.setText(transportBusList.get(position).getCustomID());
                holder.tv_vehicleName.setText(transportBusList.get(position).getClass_of_vehicle());
                holder.tv_vehicleReg.setText(transportBusList.get(position).getRegistration_no());
                holder.tv_vehicleType.setText(transportBusList.get(position).getType_of_body());
                holder.tv_vehicleDetails.setText(transportBusList.get(position).getGPS_Details());

                holder.ll_vehicle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle bundle = new Bundle();
                        bundle.putString("VEHICLE_ID",transportBusList.get(position).getVehicle_id());
                        bundle.putString("VEHICLE_UUID",transportBusList.get(position).getVehicle_uuid());
                       // TransportActivity transportActivity = (TransportActivity)context;
                       // transportActivity.loadFragment(Constant.TRANSPORT_DETAILS_FRAGMENT,bundle);
                    }
                });
                break;

            case Constant.RV_BIRTHDAY_ROW:

                String firstName = list.get(position).getFirstName();
                String lastName = list.get(position).getLastName();

                holder.tv_name.setText(firstName+" "+lastName);
                holder.tv_id.setText(list.get(position).getCustomID());


                String date = list.get(position).getBirthDate();

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat dateFormatOut = new SimpleDateFormat("dd-MMMM");

                try {
                    Date input = dateFormat.parse(date);
                    String output = dateFormatOut.format(input);

                    holder.tv_date.setText(output);


                } catch (ParseException e) {
                    e.printStackTrace();
                }

                if (!list.get(position).getImage().equals("")){
                    Glide.with(context).load(Constant.IMAGE_URL+list.get(position).getImage()).into(holder.iv_image);
                }


                break;
        }
    }

    @Override
    public int getItemCount() {

        switch (recyclerTag){

            case Constant.RV_BIRTHDAY_ROW:
                return list.size();

                case Constant.RV_TRANSPORT_BUS_LIST_ROW:
                return transportBusList.size();
        }
        return 0;
    }
}
