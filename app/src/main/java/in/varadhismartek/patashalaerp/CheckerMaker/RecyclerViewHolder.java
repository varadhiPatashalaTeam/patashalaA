package in.varadhismartek.patashalaerp.CheckerMaker;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import in.varadhismartek.patashalaerp.R;


public class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public CheckBox check_box;

    //----------------------------Checker&Maker Adpater--------
    public TextView chip_txt;
    public ImageView close;
    public  LinearLayout linear;
    public  TextView module,checker,maker;


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
        itemView.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {

    }
}
