package in.varadhismartek.patashalaerp.ClassAndSection;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import in.varadhismartek.patashalaerp.R;

public class ClassViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public CheckBox check_box;
    public TextView text_view, tv_section, tv_class;
    public LinearLayout linearLayoutSection;
    public ClassViewHolder(View itemView) {
        super(itemView);

        check_box  = itemView.findViewById(R.id.check_box);
        linearLayoutSection  = itemView.findViewById(R.id.layout_section);
      //  text_view  = itemView.findViewById(R.id.text_view);

        tv_class   = itemView.findViewById(R.id.tv_class);
        tv_section = itemView.findViewById(R.id.tv_section);
        itemView.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

    }
}
