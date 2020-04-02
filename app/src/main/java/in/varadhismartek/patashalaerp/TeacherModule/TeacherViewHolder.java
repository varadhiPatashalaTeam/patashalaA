package in.varadhismartek.patashalaerp.TeacherModule;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import in.varadhismartek.patashalaerp.R;

public class TeacherViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView tvName,tvTitle,tvDescription,tvClass,tvSection,tvStartDate,tvEndDate;
    public TeacherViewHolder(View itemView) {
        super(itemView);
        tvName = itemView.findViewById(R.id.tvName);
        tvTitle = itemView.findViewById(R.id.tv_work_title);
        tvDescription = itemView.findViewById(R.id.tv_description);
        tvClass = itemView.findViewById(R.id.tv_class);
        tvSection = itemView.findViewById(R.id.tv_section);
        tvStartDate = itemView.findViewById(R.id.tv_assign_date);
        tvEndDate = itemView.findViewById(R.id.tv_submit_date);

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

    }
}
