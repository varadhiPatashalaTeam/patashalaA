package in.varadhismartek.patashalaerp.StudentModule;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import in.varadhismartek.patashalaerp.R;


public class FeesViewHolder extends RecyclerView.ViewHolder {

    RecyclerView recycler_view;
    TextView tv_month, tv_pendingFee, tv_totalFee;
    LinearLayout ll_row;
    ImageView iv_arrow;

    LinearLayout ll_back;
    TextView tv_feesType, tv_amount, tv_dueDate, tv_status;

    public FeesViewHolder(@NonNull View itemView) {
        super(itemView);

        recycler_view = itemView.findViewById(R.id.recycler_view);
        tv_month      = itemView.findViewById(R.id.tv_month);
        tv_pendingFee = itemView.findViewById(R.id.tv_pendingFee);
        tv_totalFee   = itemView.findViewById(R.id.tv_totalFee);
        ll_row        = itemView.findViewById(R.id.ll_row);
        iv_arrow      = itemView.findViewById(R.id.iv_arrow);

        ll_back     = itemView.findViewById(R.id.ll_back);
        tv_feesType = itemView.findViewById(R.id.tv_feesType);
        tv_amount   = itemView.findViewById(R.id.tv_amount);
        tv_dueDate  = itemView.findViewById(R.id.tv_dueDate);
        tv_status  = itemView.findViewById(R.id.tv_status);

    }
}
