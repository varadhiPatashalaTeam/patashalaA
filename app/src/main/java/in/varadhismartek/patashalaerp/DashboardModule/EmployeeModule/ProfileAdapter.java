package in.varadhismartek.patashalaerp.DashboardModule.EmployeeModule;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import in.varadhismartek.Utils.Constant;
import in.varadhismartek.patashalaerp.R;


public class ProfileAdapter extends RecyclerView.Adapter<ProfileViewHolder> {

    private ArrayList<ProfileModel> profileList;
    private Context mContext;
    private String recyclerTag;

    public ProfileAdapter(ArrayList<ProfileModel> profileList, Context mContext, String recyclerTag) {
        this.profileList = profileList;
        this.mContext = mContext;
        this.recyclerTag = recyclerTag;
    }

    @NonNull
    @Override
    public ProfileViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        switch (recyclerTag) {

            case Constant.RV_PROFILE_WORK_EXPERIENCE:
                return new ProfileViewHolder(LayoutInflater.from(mContext)
                        .inflate(R.layout.layout_work_experience, viewGroup, false));

            case Constant.RV_PROFILE_DOCUMENTS:
                return new ProfileViewHolder(LayoutInflater.from(mContext)
                        .inflate(R.layout.layout_docs, viewGroup, false));

            case Constant.RV_PROFILE_SPORTS_ROW:
                return new ProfileViewHolder(LayoutInflater.from(mContext)
                        .inflate(R.layout.layout_sports_row, viewGroup, false));

            case Constant.RV_PROFILE_ACHIEVEMENT:
                return new ProfileViewHolder(LayoutInflater.from(mContext)
                        .inflate(R.layout.achievement_row, viewGroup, false));

            case Constant.RV_PROFILE_CHILDREN:
                return new ProfileViewHolder(LayoutInflater.from(mContext)
                        .inflate(R.layout.children_row, viewGroup, false));

            case Constant.RV_PROFILE_PARENTS:
                return new ProfileViewHolder(LayoutInflater.from(mContext)
                        .inflate(R.layout.parent_gaurdian_layout, viewGroup, false));

        }

        return null; // RV_PROFILE_PARENTS
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileViewHolder holder, int i) {

        final String imageUrlg = "http://13.233.109.165:8000/media/";

        switch (recyclerTag) {

            case Constant.RV_PROFILE_WORK_EXPERIENCE:
                holder.tv_organisation.setText(profileList.get(i).getOrganisation_name());
                holder.tv_place.setText(profileList.get(i).getOrganisation_place());
                holder.tv_contact.setText(profileList.get(i).getContact_no());
                holder.tv_roles.setText(profileList.get(i).getRole());
                holder.tv_responsibility.setText(profileList.get(i).getResponsibilty());
                holder.tv_expDays.setText(profileList.get(i).getNo_of_days_experience());
                holder.tv_startDate.setText(profileList.get(i).getExperience_from_date());
                holder.tv_endDate.setText(profileList.get(i).getExperience_to_date());

                if(profileList.get(i).getWork_experience_attachments().length() > 10){

                    int last = profileList.get(i).getWork_experience_attachments().length();
                    holder.tv_attach_doc.setText(profileList.get(i).getWork_experience_attachments().substring(last-10,last));

                }else
                    holder.tv_attach_doc.setText(profileList.get(i).getWork_experience_attachments());

                holder.tv_attach_doc.setTextColor(Color.parseColor("#ff00bcd4"));

                break;

            case Constant.RV_PROFILE_DOCUMENTS:

                holder.tv_docsName.setText(profileList.get(i).getDocsName());
                holder.tv_docsNo.setText(profileList.get(i).getDocsNo());

                Glide.with(mContext).load(imageUrlg+profileList.get(i).getPicTwo()).into(holder.iv_docsOne);
                Glide.with(mContext).load(imageUrlg+profileList.get(i).getPicOne()).into(holder.iv_docsTwo);

                break;

            case Constant.RV_PROFILE_SPORTS_ROW:

                holder.tv_sportsName.setText(profileList.get(i).getSportsName());
                holder.tv_teamName.setText(profileList.get(i).getTeamName());
                holder.tv_level.setText(profileList.get(i).getLevel());
                holder.tv_position.setText(profileList.get(i).getPosition());
                holder.tv_year.setText(profileList.get(i).getYear());
                holder.tv_location.setText(profileList.get(i).getLocation());

                break;

            case Constant.RV_PROFILE_ACHIEVEMENT:

                holder.tv_title.setText(profileList.get(i).getTitle());
                holder.tv_description.setText(profileList.get(i).getDescription());
                holder.tv_year.setText(profileList.get(i).getYear());

                break;

            case Constant.RV_PROFILE_CHILDREN:

                holder.tv_name.setText(profileList.get(i).getName());
                holder.tv_dob.setText(profileList.get(i).getDOB());

                break;

            case Constant.RV_PROFILE_PARENTS:

                break;

        }

        /*TextView tv_type,tv_aadharNo,tv_proofNo;
    ImageView iv_picOne,iv_picTwo,iv_picThree, iv_picFour;*/
    }

    @Override
    public int getItemCount() {

        switch (recyclerTag) {

            case Constant.RV_PROFILE_WORK_EXPERIENCE:
                return profileList.size();

            case Constant.RV_PROFILE_DOCUMENTS:
                return profileList.size();

            case Constant.RV_PROFILE_SPORTS_ROW:
                return profileList.size();

            case Constant.RV_PROFILE_ACHIEVEMENT:
                return profileList.size();

            case Constant.RV_PROFILE_CHILDREN:
                return profileList.size();

            case Constant.RV_PROFILE_PARENTS:
                return profileList.size();


        }
        return 0;
    }
}
