package in.varadhismartek.patashalaerp.DashboardModule.Syllabus;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import in.varadhismartek.patashalaerp.R;


public class SyllabusViewHolder extends RecyclerView.ViewHolder {

    public TextView tv_class, tv_Subject, tv_ExamType,tv_SyllabusTitle;
   // public ImageView img_Attachment;
    public LinearLayout ll_row;

    public ImageView iv_attachment;

    public SyllabusViewHolder(@NonNull View itemView) {
        super(itemView);

        tv_class = itemView.findViewById(R.id.tv_class);
        tv_Subject = itemView.findViewById(R.id.tv_Subject);
        tv_ExamType = itemView.findViewById(R.id.tv_ExamType);
        tv_SyllabusTitle = itemView.findViewById(R.id.tv_SyllabusTitle);
      //  img_Attachment = itemView.findViewById(R.id.img_Attachment);
        ll_row = itemView.findViewById(R.id.ll_row);


        iv_attachment = itemView.findViewById(R.id.iv_attachment);
    }
}
