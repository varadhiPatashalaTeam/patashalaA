package in.varadhismartek.patashalaerp.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import in.varadhismartek.patashalaerp.R;

/**
 * Created by varadhi on 6/10/18.
 */

public class RecyclerViewHolder extends RecyclerView.ViewHolder {
    CheckBox check_box;

    //----------------------------Checker&Maker Adpater--------
    TextView chip_txt;
    ImageView close;
    LinearLayout linear;
    TextView module,checker,maker;

    public TextView tvStudentCount,tvStudent,tvTeacherName,tvCaptionName,tvViceCaptionName,tvHouseName;
    LinearLayout llHouseAddress,ll_updatehouse;
public LinearLayout llCap;

    public RecyclerViewHolder(View itemView) {
        super(itemView);
        check_box=itemView.findViewById(R.id.check_box);

        //---------------------------Checker&Maker-----------------
        chip_txt=itemView.findViewById(R.id.chip_txt);
        close=itemView.findViewById(R.id.close);
        linear=itemView.findViewById(R.id.linear);
        module=itemView.findViewById(R.id.module);
        checker=itemView.findViewById(R.id.checker);
        maker=itemView.findViewById(R.id.maker);

// house
        tvStudentCount    = itemView.findViewById(R.id.tvStudentCount);
        tvStudent         = itemView.findViewById(R.id.tvStudent);
        tvTeacherName     = itemView.findViewById(R.id.tvTeacherName);
        tvCaptionName     = itemView.findViewById(R.id.tvCaptionName);
        tvViceCaptionName = itemView.findViewById(R.id.tvViceCaptionName);
        llHouseAddress    = itemView.findViewById(R.id.llHouseAddress);
        ll_updatehouse    = itemView.findViewById(R.id.ll_updatehouse);
        tvHouseName       = itemView.findViewById(R.id.tvHouseName);
        llCap       = itemView.findViewById(R.id.ll_cap);



    }
}
