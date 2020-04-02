package in.varadhismartek.patashalaerp.DashboardModule.Syllabus;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;



import java.util.ArrayList;

import in.varadhismartek.Utils.Constant;
import in.varadhismartek.patashalaerp.GeneralClass.DateConvertor;
import in.varadhismartek.patashalaerp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SyllabusViewFragment extends Fragment {

    ImageView img_back;
    TextView tv_examname, tv_SyllabusTitle, tv_addedBy, tv_addedDate,
            tv_addedTime, tv_description, tv_class, tv_section;

    RecyclerView rv_attachment;
    Toolbar toolbar;

    public SyllabusViewFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_syllabus_view, container, false);

        initViews(view);
        initListeners();
        getBungels();

        toolbar.setBackgroundResource(R.drawable.toolbar_syllabus);
        /*switch (Constant.audience_type){

            case "Staff":
                toolbar.setBackgroundResource(R.drawable.toolbar_syllabus_staff);

                break;

            case "Parent":
                toolbar.setBackgroundResource(R.drawable.toolbar_syllabus_parent);
                break;
        }*/

        return view;
    }

    private void getBungels() {

        Bundle bundle = getArguments();
        SyllabusModel syllabusModel = (SyllabusModel) bundle.getSerializable("syllabus_list");

        if (syllabusModel!=null){

            tv_examname.setVisibility(View.GONE);
            tv_SyllabusTitle.setText(syllabusModel.getSyllabus_title());
            tv_addedBy.setText(syllabusModel.getAdded_by());
            tv_description.setText(syllabusModel.getDescription());
            tv_class.setText(syllabusModel.getClasses());
            tv_section.setText(syllabusModel.getSections());

            DateConvertor convertor = new DateConvertor();

            String addedDate = convertor.getDateByTZ_SSS(syllabusModel.getAdded_datetime());

            tv_addedDate.setText(addedDate);
            String addedTime = convertor.getTimeByTZ_SSS(syllabusModel.getAdded_datetime());
            tv_addedTime.setText(addedTime);

            setRecyclerView(syllabusModel.getImageList());

        }
    }

    private void initListeners() {

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getActivity().onBackPressed();
            }
        });

    }

    private void initViews(View view) {

        img_back = view.findViewById(R.id.img_back);
        toolbar = view.findViewById(R.id.toolbar);
        tv_examname = view.findViewById(R.id.tv_examname);
        tv_SyllabusTitle = view.findViewById(R.id.tv_SyllabusTitle);
        tv_addedBy = view.findViewById(R.id.tv_addedBy);
        tv_addedDate = view.findViewById(R.id.tv_addedDate);
        tv_addedTime = view.findViewById(R.id.tv_addedTime);
        tv_description = view.findViewById(R.id.tv_description);
        tv_class = view.findViewById(R.id.tv_class);
        tv_section = view.findViewById(R.id.tv_section);
        rv_attachment = view.findViewById(R.id.rv_attachment);
    }

    private void setRecyclerView(ArrayList<String> imageList){

        Log.d("imageListsize", String.valueOf(imageList.size()));

        SyllabusAdapter adapter  =new SyllabusAdapter(getActivity(), imageList, Constant.RV_SYLLABUS_VIEW_ATTACHMENT);
        rv_attachment.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        rv_attachment.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

}
