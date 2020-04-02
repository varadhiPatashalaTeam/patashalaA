package in.varadhismartek.patashalaerp.SessionModule;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import in.varadhismartek.patashalaerp.R;


public class SessionViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

   /* TextView tvSessionName, tv_minus, tv_noSession,tv_plus;
    RecyclerView recycler_view;
    ImageView iv_down,iv_up;
    LinearLayout ll_showHide, ll_NoOfSession;
*/
   LinearLayout linearLayout;
    RelativeLayout rl_fromDate, rl_toDate;
    TextView tv_fromDate, tv_toDate;

    public SessionViewHolder(@NonNull View itemView) {
        super(itemView);

        /*tvSessionName = itemView.findViewById(R.id.tvSessionName);
        tv_minus = itemView.findViewById(R.id.tv_minus);
        tv_noSession = itemView.findViewById(R.id.tv_noSession);
        tv_plus = itemView.findViewById(R.id.tv_plus);
        recycler_view = itemView.findViewById(R.id.recycler_view);
        iv_down = itemView.findViewById(R.id.iv_down);
        iv_up = itemView.findViewById(R.id.iv_up);
        ll_showHide = itemView.findViewById(R.id.ll_showHide);
        ll_NoOfSession = itemView.findViewById(R.id.ll_NoOfSession);*/

        // this is for nested
        linearLayout = itemView.findViewById(R.id.date_display);
        rl_fromDate = itemView.findViewById(R.id.rl_fromDate);
        rl_toDate = itemView.findViewById(R.id.rl_toDate);
        tv_fromDate = itemView.findViewById(R.id.tv_fromDate);
        tv_toDate = itemView.findViewById(R.id.tv_toDate);

        linearLayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

    }
}
