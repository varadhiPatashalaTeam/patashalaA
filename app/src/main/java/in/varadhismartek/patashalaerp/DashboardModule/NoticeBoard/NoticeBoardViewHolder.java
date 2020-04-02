package in.varadhismartek.patashalaerp.DashboardModule.NoticeBoard;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import in.varadhismartek.patashalaerp.R;


public class NoticeBoardViewHolder extends RecyclerView.ViewHolder {

    public TextView tv_title, tv_message, tv_dateView, tv_shortName;
    LinearLayout ll_row;

    public NoticeBoardViewHolder(@NonNull View itemView) {
        super(itemView);

        tv_title = itemView.findViewById(R.id.tv_title);
        tv_message = itemView.findViewById(R.id.tv_message);
        tv_dateView = itemView.findViewById(R.id.tv_dateView);
        tv_shortName = itemView.findViewById(R.id.tv_shortName);
        ll_row = itemView.findViewById(R.id.ll_row);
    }
}
