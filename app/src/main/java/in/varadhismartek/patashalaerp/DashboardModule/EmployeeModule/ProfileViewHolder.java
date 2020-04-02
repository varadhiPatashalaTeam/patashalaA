package in.varadhismartek.patashalaerp.DashboardModule.EmployeeModule;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import in.varadhismartek.patashalaerp.R;


public class ProfileViewHolder extends RecyclerView.ViewHolder {

    TextView tv_organisation, tv_place, tv_contact, tv_roles, tv_responsibility, tv_expDays, tv_startDate, tv_endDate, tv_attach_doc;

    TextView tv_docsName, tv_docsNo;
    ImageView iv_docsOne, iv_docsTwo;

    TextView tv_sportsName, tv_teamName, tv_level, tv_position, tv_year, tv_location;

    TextView tv_title, tv_description;
    TextView tv_name, tv_dob;

    TextView tv_type,tv_aadharNo,tv_proofNo;
    ImageView iv_picOne,iv_picTwo,iv_picThree, iv_picFour;

    public ProfileViewHolder(@NonNull View itemView) {
        super(itemView);

        tv_organisation = itemView.findViewById(R.id.tv_organisation);
        tv_place = itemView.findViewById(R.id.tv_place);
        tv_contact = itemView.findViewById(R.id.tv_contact);
        tv_roles = itemView.findViewById(R.id.tv_roles);
        tv_responsibility = itemView.findViewById(R.id.tv_responsibility);
        tv_expDays = itemView.findViewById(R.id.tv_expDays);
        tv_startDate = itemView.findViewById(R.id.tv_startDate);
        tv_endDate = itemView.findViewById(R.id.tv_endDate);
        tv_attach_doc = itemView.findViewById(R.id.tv_attach_doc);

        tv_docsName = itemView.findViewById(R.id.tv_docsName);
        tv_docsNo = itemView.findViewById(R.id.tv_docsNo);
        iv_docsOne = itemView.findViewById(R.id.iv_docsOne);
        iv_docsTwo = itemView.findViewById(R.id.iv_docsTwo);

        tv_sportsName = itemView.findViewById(R.id.tv_sportsName);
        tv_teamName = itemView.findViewById(R.id.tv_teamName);
        tv_level = itemView.findViewById(R.id.tv_level);
        tv_position = itemView.findViewById(R.id.tv_position);
        tv_year = itemView.findViewById(R.id.tv_year);
        tv_location = itemView.findViewById(R.id.tv_location);

        tv_title = itemView.findViewById(R.id.tv_title);
        tv_description = itemView.findViewById(R.id.tv_description);

        tv_name = itemView.findViewById(R.id.tv_name);
        tv_dob = itemView.findViewById(R.id.tv_dob);

        tv_type = itemView.findViewById(R.id.tv_type);
        tv_name = itemView.findViewById(R.id.tv_name);
        tv_aadharNo = itemView.findViewById(R.id.tv_aadharNo);
        tv_proofNo = itemView.findViewById(R.id.tv_proofNo);
        iv_picOne = itemView.findViewById(R.id.iv_picOne);
        iv_picTwo = itemView.findViewById(R.id.iv_picTwo);
        iv_picThree = itemView.findViewById(R.id.iv_picThree);
        iv_picFour = itemView.findViewById(R.id.iv_picFour);

    }
}
