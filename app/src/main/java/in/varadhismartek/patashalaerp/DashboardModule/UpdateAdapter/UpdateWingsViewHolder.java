package in.varadhismartek.patashalaerp.DashboardModule.UpdateAdapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;

import in.varadhismartek.patashalaerp.R;


public class UpdateWingsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public CheckBox check_box;
    public UpdateWingsViewHolder(View itemView) {
        super(itemView);
        check_box=itemView.findViewById(R.id.check_box);
        itemView.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {

    }
}