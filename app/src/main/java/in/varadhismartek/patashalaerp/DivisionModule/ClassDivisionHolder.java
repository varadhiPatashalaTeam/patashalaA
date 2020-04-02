package in.varadhismartek.patashalaerp.DivisionModule;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import in.varadhismartek.patashalaerp.R;

public class ClassDivisionHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public CheckBox check_box;
    public TextView text_view, tv_section, tv_class;

    public LinearLayout linearLayout,linearLayoutSection;
    public RelativeLayout rl_fromDate, rl_toDate;
    public TextView tv_fromDate, tv_toDate;


    public ClassDivisionHolder(@NonNull View itemView) {
        super(itemView);

        check_box  = itemView.findViewById(R.id.check_box);

        //sessions

        linearLayout = itemView.findViewById(R.id.date_display);
        rl_fromDate = itemView.findViewById(R.id.rl_fromDate);
        rl_toDate = itemView.findViewById(R.id.rl_toDate);
        tv_fromDate = itemView.findViewById(R.id.tv_fromDate);
        tv_toDate = itemView.findViewById(R.id.tv_toDate);
//        linearLayout.setOnClickListener(this);


       /* text_view  = itemView.findViewById(R.id.text_view);
        tv_class   = itemView.findViewById(R.id.tv_class);
        tv_section = itemView.findViewById(R.id.tv_section);*/

       //sections
        linearLayoutSection  = itemView.findViewById(R.id.layout_section);
        //  text_view  = itemView.findViewById(R.id.text_view);

        tv_class   = itemView.findViewById(R.id.tv_class);
        tv_section = itemView.findViewById(R.id.tv_section);
    }

    @Override
    public void onClick(View v) {

    }
}
