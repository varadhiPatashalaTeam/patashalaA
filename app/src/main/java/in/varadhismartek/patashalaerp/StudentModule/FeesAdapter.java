package in.varadhismartek.patashalaerp.StudentModule;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.ArrayList;

import in.varadhismartek.Utils.Constant;
import in.varadhismartek.patashalaerp.R;

public class FeesAdapter extends RecyclerView.Adapter<FeesViewHolder> {

    private Context mContext;
    private String recyclerTag;
    private ArrayList<FeesMonthModel> feesMonthList;
    private ArrayList<FeesDetailsModel> feesDetailsList;

    public FeesAdapter(ArrayList<FeesMonthModel> feesMonthList, Context mContext, String recyclerTag) {
        this.mContext = mContext;
        this.recyclerTag = recyclerTag;
        this.feesMonthList = feesMonthList;
    }

    public FeesAdapter(Context mContext, ArrayList<FeesDetailsModel> feesDetailsList, String recyclerTag) {
        this.mContext = mContext;
        this.recyclerTag = recyclerTag;
        this.feesDetailsList = feesDetailsList;
    }

    @NonNull
    @Override
    public FeesViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        switch (recyclerTag){

            case Constant.RV_FEES_HISTORY_ROW:
                return new FeesViewHolder(LayoutInflater.from(mContext)
                        .inflate(R.layout.layout_fees_history_row, viewGroup, false));

            case Constant.RV_FEES_NESTED_CARD_ROW:
                return new FeesViewHolder(LayoutInflater.from(mContext)
                        .inflate(R.layout.fees_status_card, viewGroup, false));

        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull final FeesViewHolder holder, final int i) {

        switch (recyclerTag){

            case Constant.RV_FEES_HISTORY_ROW:

                FeesAdapter adapter = new FeesAdapter(mContext, feesMonthList.get(i).getFeesDetailsList(), Constant.RV_FEES_NESTED_CARD_ROW);
                holder.recycler_view.setLayoutManager(new GridLayoutManager(mContext, 2));
                holder.recycler_view.setAdapter(adapter);
                adapter.notifyDataSetChanged();

                holder.tv_month.setText(feesMonthList.get(i).getMonth());
                holder.tv_pendingFee.setText(feesMonthList.get(i).getPending_total());
                holder.tv_totalFee.setText(feesMonthList.get(i).getUpcoming_total());

                holder.ll_row.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (holder.recycler_view.getVisibility()== View.VISIBLE){
                            holder.recycler_view.setVisibility(View.GONE);
                            holder.iv_arrow.setImageResource(R.drawable.ic_keyboard_arrow_down);

                        }else {
                            holder.recycler_view.setVisibility(View.VISIBLE);
                            holder.iv_arrow.setImageResource(R.drawable.ic_keyboard_arrow_up);

                        }
                    }
                });

                break;

            case Constant.RV_FEES_NESTED_CARD_ROW:

                holder.tv_feesType.setText(feesDetailsList.get(i).getFee_type());
                holder.tv_amount.setText(feesDetailsList.get(i).getAmount_due());
                holder.tv_dueDate.setText(feesDetailsList.get(i).getDue_date().substring(0,10));
                holder.tv_status.setText(feesDetailsList.get(i).getPaid_status());

                switch (feesDetailsList.get(i).getPaid_status()){

                    case "Pending":
                        holder.ll_back.setBackgroundResource(R.color.yello_gold);
                        break;
                    case "Paid":
                        holder.ll_back.setBackgroundResource(R.color.green);
                        break;
                }


                holder.ll_back.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        final Dialog dialog = new Dialog(mContext);
                        dialog.setContentView(R.layout.dialog_fees_details);
                        dialog.getWindow().setBackgroundDrawableResource(R.drawable.flag_transparent);
                        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        dialog.show();

                        TextView tv_feesType = dialog.findViewById(R.id.tv_feesType);
                        TextView tv_addedDate = dialog.findViewById(R.id.tv_addedDate);
                        TextView tv_status = dialog.findViewById(R.id.tv_status);
                        ImageView iv_status_circle = dialog.findViewById(R.id.iv_status_circle);

                        TextView tv_dueAmount = dialog.findViewById(R.id.tv_dueAmount);
                        TextView tv_fromDate = dialog.findViewById(R.id.tv_fromDate);
                        TextView tv_dueDate = dialog.findViewById(R.id.tv_dueDate);
                        TextView tv_penaltyAmount = dialog.findViewById(R.id.tv_penaltyAmount);
                        TextView tv_penaltyFrom = dialog.findViewById(R.id.tv_penaltyFrom);
                        TextView tv_penaltyTo = dialog.findViewById(R.id.tv_penaltyTo);
                        TextView tv_netAmount = dialog.findViewById(R.id.tv_netAmount);
                        TextView tv_close = dialog.findViewById(R.id.tv_close);

                        tv_feesType.setText(feesDetailsList.get(i).getFee_type());
                        tv_addedDate.setText(feesDetailsList.get(i).getAdded_datetime().substring(0,10));
                        tv_status.setText(feesDetailsList.get(i).getPaid_status());

                        tv_dueAmount.setText(feesDetailsList.get(i).getAmount_due());
                        tv_fromDate.setText(feesDetailsList.get(i).getFrom_date().substring(0,10));
                        tv_dueDate.setText(feesDetailsList.get(i).getDue_date().substring(0,10));
                        tv_penaltyAmount.setText(feesDetailsList.get(i).getPenalty_amount());
                        tv_netAmount.setText(feesDetailsList.get(i).getNet_fees());

                        if (!feesDetailsList.get(i).getPenalty_from().equals("")) {
                            tv_penaltyFrom.setText(feesDetailsList.get(i).getPenalty_from().substring(0, 10));
                        }
                        if (!feesDetailsList.get(i).getPenalty_to().equals("")) {
                            tv_penaltyTo.setText(feesDetailsList.get(i).getPenalty_to().substring(0, 10));
                        }

                        switch (feesDetailsList.get(i).getPaid_status()){

                            case "Pending":
                                iv_status_circle.setBackgroundResource(R.drawable.circle_yellow);
                                break;
                            case "Paid":
                                iv_status_circle.setBackgroundResource(R.drawable.circle_bg);
                                break;
                        }

                        tv_close.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                dialog.dismiss();
                            }
                        });
                    }
                });

                break;

        }
    }

    @Override
    public int getItemCount() {

        switch (recyclerTag){

            case Constant.RV_FEES_HISTORY_ROW:
                return feesMonthList.size();

            case Constant.RV_FEES_NESTED_CARD_ROW:
                return feesDetailsList.size();
        }
        return 0;
    }
}
