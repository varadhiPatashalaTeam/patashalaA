package in.varadhismartek.patashalaerp.SelectModules;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;

import in.varadhismartek.patashalaerp.R;


/**
 * Created by varadhi on 25/9/18.
 */

public class SelectModuleViewHolder extends RecyclerView.ViewHolder {
    CheckBox check_box;
    public SelectModuleViewHolder(View itemView) {
        super(itemView);
        check_box=itemView.findViewById(R.id.check_box);


    }
}
