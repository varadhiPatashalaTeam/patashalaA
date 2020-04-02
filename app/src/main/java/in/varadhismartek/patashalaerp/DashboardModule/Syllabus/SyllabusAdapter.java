package in.varadhismartek.patashalaerp.DashboardModule.Syllabus;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;


import java.util.ArrayList;

import in.varadhismartek.Utils.Constant;
import in.varadhismartek.patashalaerp.R;

public class SyllabusAdapter extends RecyclerView.Adapter<SyllabusViewHolder> {

    private ArrayList<SyllabusModel> syllabusList;
    private ArrayList<String> imageList;
    private ArrayList<Uri> uriList;
    private Context context;
    private String recyclerTag;

    public SyllabusAdapter(ArrayList<SyllabusModel> syllabusList, Context context, String recyclerTag) {

        this.syllabusList = syllabusList;
        this.context = context;
        this.recyclerTag = recyclerTag;
    }

    public SyllabusAdapter(Context context, ArrayList<String> imageList, String recyclerTag) {

        this.imageList = imageList;
        this.context = context;
        this.recyclerTag = recyclerTag;
    }

    public SyllabusAdapter(Context context, String recyclerTag, ArrayList<Uri> uriList) {

        this.uriList = uriList;
        this.context = context;
        this.recyclerTag = recyclerTag;
    }

    @NonNull
    @Override
    public SyllabusViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        switch (recyclerTag){

            case Constant.RV_SYLLABUS_LIST:
                return new SyllabusViewHolder(LayoutInflater.from(context)
                        .inflate(R.layout.syllabus_inbox_row, viewGroup, false));//RV_SYLLABUS_VIEW_ATTACHMENT

            case Constant.RV_SYLLABUS_VIEW_ATTACHMENT:
                return new SyllabusViewHolder(LayoutInflater.from(context)
                        .inflate(R.layout.syllabus_attach_row, viewGroup, false));

            case Constant.RV_ADD_SYLLABUS_ATTACHMENT:
                return new SyllabusViewHolder(LayoutInflater.from(context)
                        .inflate(R.layout.syllabus_attach_row, viewGroup, false));

        }
        return null;//RV_ADD_SYLLABUS_ATTACHMENT
    }

    @Override
    public void onBindViewHolder(@NonNull SyllabusViewHolder holder, final int i) {

        switch (recyclerTag){

            case Constant.RV_SYLLABUS_LIST:

                holder.tv_class.setText(syllabusList.get(i).getClasses());
                holder.tv_Subject.setText(syllabusList.get(i).getSubject());
                holder.tv_ExamType.setText(syllabusList.get(i).getExam_type());
                holder.tv_SyllabusTitle.setText(syllabusList.get(i).getSyllabus_title());

                holder.ll_row.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        SyllabusActivity syllabusActivity = (SyllabusActivity) context;

                        Bundle bundle = new Bundle();
                        bundle.putSerializable("syllabus_list", syllabusList.get(i));
                        syllabusActivity.loadFragment(Constant.SYLLABUS_VIEW_FRAGMENT, bundle);

                    }
                });

                break;

            case Constant.RV_SYLLABUS_VIEW_ATTACHMENT:

                if (!imageList.get(i).equals("")){
                    Glide.with(context).load(Constant.IMAGE_LINK+imageList.get(i)).into(holder.iv_attachment);
                }

                break;

            case Constant.RV_ADD_SYLLABUS_ATTACHMENT:
                holder.iv_attachment.setImageURI(uriList.get(i));

                break;

        }
    }

    @Override
    public int getItemCount() {

        switch (recyclerTag){

            case Constant.RV_SYLLABUS_LIST:
                return syllabusList.size();

            case Constant.RV_SYLLABUS_VIEW_ATTACHMENT:
                return imageList.size();

            case Constant.RV_ADD_SYLLABUS_ATTACHMENT:
                return uriList.size();

        }
        return 0;
    }
}
