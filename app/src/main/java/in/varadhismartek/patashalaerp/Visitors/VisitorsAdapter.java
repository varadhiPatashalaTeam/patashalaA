package in.varadhismartek.patashalaerp.Visitors;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;


import java.util.ArrayList;

import in.varadhismartek.Utils.Constant;
import in.varadhismartek.patashalaerp.GeneralClass.CircleImageView;
import in.varadhismartek.patashalaerp.R;

public class VisitorsAdapter extends RecyclerView.Adapter<VisitorsAdapter.ViewHolder> {

    private ArrayList<VisitorModel> visitorList, searchList;
    private Context context;
    private String recyclerTag;
    private TextView tv_search,tv_roles,tv_department;
    private Dialog dialog;

    public VisitorsAdapter(ArrayList<VisitorModel> visitorList, Context context, String recyclerTag) {

        this.context = context;
        this.visitorList = visitorList;
        this.recyclerTag = recyclerTag;

    }

    public VisitorsAdapter(Context context, ArrayList<VisitorModel> searchList, Dialog dialog,
                           TextView tv_search, TextView tv_department, TextView tv_roles, String recyclerTag) {

        this.context = context;
        this.searchList = searchList;
        this.recyclerTag = recyclerTag;
        this.tv_roles = tv_roles;
        this.dialog = dialog;
        this.tv_department = tv_department;
        this.tv_search = tv_search;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        switch (recyclerTag){

            case Constant.RV_VISITORS_INBOX_ROW:
                return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_visitors_row, viewGroup, false));

            case Constant.RV_NOTIFICATION_STAFF_SEARCH_RESULT:
                return new ViewHolder(LayoutInflater.from(context)
                        .inflate(R.layout.layout_search_result, viewGroup, false));
        }

        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int i) {

        switch (recyclerTag){

            case Constant.RV_VISITORS_INBOX_ROW:

                if (!visitorList.get(i).getVisitor_photo().equalsIgnoreCase("")){
                    Glide.with(context)
                            .load(Constant.IMAGE_URL+visitorList.get(i).getVisitor_photo())
                            .into(holder.civ_visitor);
                }

                holder.visitorName.setText(visitorList.get(i).getVisitor_name());
                holder.visitorId.setText(visitorList.get(i).getVisitor_id());
                holder.tv_staffName.setText(visitorList.get(i).getAdded_by_first_name());
                holder.tv_purpose.setText(visitorList.get(i).getPurpose());
                holder.tv_entryTime.setText(visitorList.get(i).getEntry_time());
                holder.tv_entryDate.setText(visitorList.get(i).getEntry_date());


                holder.ll_row.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        VisitiorsActivity visitiorsActivity = (VisitiorsActivity) context;
                        Bundle bundle = new Bundle();
                        bundle.putString("visitorId", visitorList.get(i).getVisitor_uuid());
                        visitiorsActivity.loadFragment(Constant.FRAGMENT_VIEW_VISITIORS, bundle);
                    }
                });

                break;

            case Constant.RV_NOTIFICATION_STAFF_SEARCH_RESULT:

                String firstName = searchList.get(i).getFirst_name();
                String lastName = searchList.get(i).getLast_name();

                holder.tv_name.setText(firstName+" "+lastName);
                holder.tv_id.setText(searchList.get(i).getCustom_employee_id());
                holder.tv_mobile.setVisibility(View.GONE);

                if (!searchList.get(i).getPhoto().equalsIgnoreCase("")) {
                    Glide.with(context)
                            .load(Constant.IMAGE_URL+searchList.get(i).getPhoto())
                            .into(holder.civ_image);
                }

                holder.ll_parent.setVisibility(View.GONE);
                holder.ll_staff.setVisibility(View.VISIBLE);
                holder.tv_dept_name.setText(searchList.get(i).getDepartment());
                holder.tv_role_name.setText(searchList.get(i).getRole());

                holder.ll_search_row.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(context, "Clicked "+i, Toast.LENGTH_SHORT).show();
                        tv_search.setText(searchList.get(i).getFirst_name());
                        tv_department.setText(searchList.get(i).getDepartment());
                        tv_roles.setText(searchList.get(i).getRole());

                        AddVisitorsFragment.EMPLOYEE_ID = searchList.get(i).getEmployeeID();
                        AddVisitorsFragment.DEPARTMENT = searchList.get(i).getDepartment();
                        AddVisitorsFragment.ROLE = searchList.get(i).getRole();

                        dialog.dismiss();
                    }
                });

                if(i == 0){
                    holder.ll_search_row.setBackgroundResource(R.color.white);

                }else if (i%2 != 0){
                    holder.ll_search_row.setBackgroundResource(R.color.orange_colorPrimary);

                }else {
                    holder.ll_search_row.setBackgroundResource(R.color.white);

                }

                break;
        }

    }

    @Override
    public int getItemCount() {

        switch (recyclerTag){

            case Constant.RV_VISITORS_INBOX_ROW:
                return visitorList.size();

            case Constant.RV_NOTIFICATION_STAFF_SEARCH_RESULT:
                return searchList.size();


        }

        return 0;
    }


    public void empFilter(ArrayList<VisitorModel> filteredStaffList) {
        searchList = filteredStaffList;
        notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        CircleImageView civ_visitor;
        LinearLayout ll_row;
        TextView visitorName, visitorId, tv_staffName, tv_purpose, tv_entryTime, tv_entryDate;

        //for search result
        CircleImageView civ_image;
        LinearLayout ll_search_row;
        TextView tv_name;
        LinearLayout ll_parent, ll_staff;
        TextView tv_class_name, tv_section_name, tv_dept_name, tv_role_name, tv_id, tv_mobile;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            civ_visitor = itemView.findViewById(R.id.civ_visitor);
            visitorName = itemView.findViewById(R.id.visitorName);
            visitorId = itemView.findViewById(R.id.visitorId);
            tv_staffName = itemView.findViewById(R.id.tv_staffName);
            tv_purpose = itemView.findViewById(R.id.tv_purpose);
            tv_entryTime = itemView.findViewById(R.id.tv_entryTime);
            tv_entryDate = itemView.findViewById(R.id.tv_entryDate);
            ll_row = itemView.findViewById(R.id.ll_row);

            ll_search_row = itemView.findViewById(R.id.ll_search_row);
            tv_name = itemView.findViewById(R.id.tv_name);
            ll_parent = itemView.findViewById(R.id.ll_parent);
            ll_staff = itemView.findViewById(R.id.ll_staff);
            tv_class_name = itemView.findViewById(R.id.tv_class_name);
            tv_section_name = itemView.findViewById(R.id.tv_section_name);
            tv_dept_name = itemView.findViewById(R.id.tv_dept_name);
            tv_role_name = itemView.findViewById(R.id.tv_role_name);
            tv_id = itemView.findViewById(R.id.tv_id);
            tv_mobile = itemView.findViewById(R.id.tv_mobile);
            civ_image = itemView.findViewById(R.id.civ_image);
        }
    }
}



