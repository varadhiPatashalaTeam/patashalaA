package in.varadhismartek.patashalaerp.DashboardModule.FeesModule;


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import in.varadhismartek.patashalaerp.R;


public class FeeViewHolder extends RecyclerView.ViewHolder {
    public TextView tvSerialNo,tvFeeType,tvFeeCode,tvInstallment,tvFeeDueDate;
    LinearLayout linearLayoutFees;
    public FeeViewHolder(View itemView) {
        super(itemView);
        tvSerialNo =itemView.findViewById(R.id.tvSerialno);
        tvFeeType =itemView.findViewById(R.id.tvfeeType);
        tvFeeCode =itemView.findViewById(R.id.tvFeeCode);
        tvInstallment =itemView.findViewById(R.id.tvInstallment);
        tvFeeDueDate =itemView.findViewById(R.id.tvDueDate);
        linearLayoutFees =itemView.findViewById(R.id.ll_fees);
    }
}

