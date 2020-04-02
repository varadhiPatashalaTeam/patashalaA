package in.varadhismartek.patashalaerp.TicketRaise;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;


import java.util.ArrayList;
import java.util.List;

import in.varadhismartek.Utils.Constant;
import in.varadhismartek.patashalaerp.GeneralClass.CircleImageView;
import in.varadhismartek.patashalaerp.GeneralClass.DateConvertor;
import in.varadhismartek.patashalaerp.R;

public class StaffTicketAdapter extends RecyclerView.Adapter<StaffTicketAdapter.ViewHolder> {

    private Context context;
    private List<StaffTicketModel> list;
    private List<StaffTicketModel> searchList;
    private ArrayList<Uri> imageList;
    private ArrayList<String> images;
    private String recyclerTag;
    private DateConvertor convertor;

    TextView tv_search;
    Dialog dialog;

    public StaffTicketAdapter(Context context, List<StaffTicketModel> list, String recyclerTag) {
        this.context = context;
        this.list = list;
        this.recyclerTag = recyclerTag;
        convertor = new DateConvertor();
    }

    public StaffTicketAdapter(ArrayList<Uri> imageList, Context context, String recyclerTag) {

        this.context = context;
        this.imageList = imageList;
        this.recyclerTag = recyclerTag;
    }

    public StaffTicketAdapter(Context context, String recyclerTag, ArrayList<String> images) {

        this.context = context;
        this.images = images;
        this.recyclerTag = recyclerTag;
    }

    public StaffTicketAdapter(Context context, ArrayList<StaffTicketModel> searchList, Dialog dialog,
                              TextView tv_search, String recyclerTag) {

        this.context = context;
        this.searchList = searchList;
        this.dialog = dialog;
        this.tv_search = tv_search;
        this.recyclerTag = recyclerTag;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        switch (recyclerTag){

            case Constant.RV_TICKET_STAFF_INBOX:
                return new ViewHolder(LayoutInflater.from(context)
                        .inflate(R.layout.ticket_inbox_row,parent,false));

            case Constant.RV_TICKET_STAFF_CREATE_ATTACHMENT:
                return new ViewHolder(LayoutInflater.from(context)
                        .inflate(R.layout.attachment_images,parent,false));

            case Constant.RV_TICKET_VIEW_CHAT_ROW:
                return new ViewHolder(LayoutInflater.from(context)
                        .inflate(R.layout.ticket_inbox_1d_row,parent,false));

            case Constant.RV_TICKET_CHAT_ATTACHMENT:
                return new ViewHolder(LayoutInflater.from(context)
                        .inflate(R.layout.reply_attachment_item,parent,false));

            case Constant.RV_TICKET_REPLY_ATTACHMENT:
                return new ViewHolder(LayoutInflater.from(context)
                        .inflate(R.layout.attachment_images,parent,false));


        }

        return null;


    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, @SuppressLint("RecyclerView") final int position) {

        switch (recyclerTag){

            case Constant.RV_TICKET_STAFF_INBOX:
                holder.tv_ticketNo.setText(list.get(position).getTicket_number());
                holder.tv_issueTitle.setText(list.get(position).getIssue_title());
                holder.tv_TicketLevel.setText(list.get(position).getTicket_level());
                holder.tv_TicketType.setText(list.get(position).getTicket_type());
                holder.tv_TicketDate.setText(list.get(position).getTicket_datetime().substring(0,10));
                holder.tv_TicketPriyority.setText(list.get(position).getTicket_priority());
                holder.tv_Subject.setText(list.get(position).getTicket_subject());
                holder.tv_department.setText(list.get(position).getTicket_department());

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        StaffTicketActivity staffTicketActivity =(StaffTicketActivity)context;

                        Bundle bundle = new Bundle();
                        bundle.putString("ticket_id",list.get(position).getTicket_id());
                        bundle.putString("empCustomId",list.get(position).getEmpCustomId());

                        staffTicketActivity.loadFragment(Constant.TICKET_STAFF_VIEW_FRAGMENT, bundle);
                    }
                });

                break;

            case Constant.RV_TICKET_STAFF_CREATE_ATTACHMENT:

                holder.img.setImageURI(imageList.get(position));
                holder.delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        imageList.remove(position);
                        notifyItemRemoved(position);
                        notifyDataSetChanged();
                    }
                });

                break;


            case Constant.RV_TICKET_VIEW_CHAT_ROW:

                if (Constant.EMPLOYEE_ID.equals(list.get(position).getEmployee())){
                    Log.d("lwdhjkd", "if");
                    holder.ll_right.setVisibility(View.GONE);
                    holder.ll_left.setVisibility(View.VISIBLE);

                }else {
                    Log.d("lwdhjkd", "if");
                    holder.ll_right.setVisibility(View.VISIBLE);
                    holder.ll_left.setVisibility(View.GONE);

                }

                if (!list.get(position).getEmployee_photo().equals("")){
                    Glide.with(context).load(Constant.IMAGE_URL+list.get(position)
                            .getEmployee_photo()).into(holder.civ_image);
                }

                holder.tv_Name.setText(list.get(position).getFirst_name());
                holder.tv_Description.setText(list.get(position).getTicket_message());

                String date = convertor.getDateByTZ_SSS(list.get(position).getResponse_datetime());
                String time = convertor.getTimeByTZ_SSS(list.get(position).getResponse_datetime());

                holder.tv_date_time.setText(date+" "+time);
                holder.tv_id_num.setText(Constant.EMPLOYEE_EMP_CUSTOM_ID);

                StaffTicketAdapter adapter = new StaffTicketAdapter(context,
                        Constant.RV_TICKET_CHAT_ATTACHMENT, list.get(position).getImages());
                holder.image_rv.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
                holder.image_rv.setAdapter(adapter);
                adapter.notifyDataSetChanged();

                break;

            case Constant.RV_TICKET_CHAT_ATTACHMENT:

                if (!images.get(position).equals("")){
                    Glide.with(context).load(Constant.IMAGE_URL+images.get(position)).into(holder.iv_attachment);
                }

                break;

            case Constant.RV_TICKET_REPLY_ATTACHMENT:

                holder.img.setImageURI(imageList.get(position));
                holder.delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        imageList.remove(position);
                        notifyItemRemoved(position);
                        notifyDataSetChanged();
                    }
                });

                break;

            case Constant.RV_TICKET_STAFF_SEARCH_RESULT:

                String firstName = searchList.get(position).getFirst_name();
                String lastName = searchList.get(position).getLast_name();

                holder.tv_name.setText(firstName+" "+lastName);
                holder.tv_id.setText(searchList.get(position).getCustom_employee_id());
                holder.tv_mobile.setVisibility(View.GONE);

                if (!searchList.get(position).getEmployee_photo().equalsIgnoreCase("")) {
                    Glide.with(context)
                            .load(Constant.IMAGE_URL+searchList.get(position).getEmployee_photo())
                            .into(holder.civ_image);
                }

                holder.ll_parent.setVisibility(View.GONE);
                holder.ll_staff.setVisibility(View.VISIBLE);
                holder.tv_dept_name.setText(searchList.get(position).getDepartment());
                holder.tv_role_name.setText(searchList.get(position).getRole());

                holder.ll_search_row.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(context, "Clicked "+position, Toast.LENGTH_SHORT).show();
                        tv_search.setText(searchList.get(position).getFirst_name());

                        StaffViewTicketFragment.SEARCH_ID =
                                searchList.get(position).getEmployee_uuid();

                        dialog.dismiss();
                    }
                });

                if(position == 0){
                    holder.ll_search_row.setBackgroundResource(R.color.white);

                }else if (position%2 != 0){
                    holder.ll_search_row.setBackgroundResource(R.color.orange_colorPrimary);

                }else {
                    holder.ll_search_row.setBackgroundResource(R.color.white);

                }

                break;

        }
    }

    @Override
    public int getItemCount() {
        switch (recyclerTag) {

            case Constant.RV_TICKET_STAFF_INBOX:
                return list.size();

            case Constant.RV_TICKET_STAFF_CREATE_ATTACHMENT:
                return imageList.size();

            case Constant.RV_TICKET_VIEW_CHAT_ROW:
                return list.size();

            case Constant.RV_TICKET_CHAT_ATTACHMENT:
                return images.size();

            case Constant.RV_TICKET_REPLY_ATTACHMENT:
                return imageList.size();

            case Constant.RV_TICKET_STAFF_SEARCH_RESULT:
                return searchList.size();


        }
        return 0;
    }

    public void employeeFilterList(ArrayList<StaffTicketModel> filteredStaffList) {
        searchList = filteredStaffList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_ticketNo,tv_issueTitle,tv_TicketLevel,tv_TicketType,tv_department,tv_TicketDate,tv_TicketPriyority,tv_Subject;

        ImageView img,delete;

        LinearLayout ll_left,ll_right;
        CircleImageView civ_image;
        TextView tv_Name, tv_date_time, tv_id_num, tv_Description;
        RecyclerView image_rv;

        ImageView iv_attachment;

        LinearLayout ll_search_row;
        TextView tv_name;
        LinearLayout ll_parent, ll_staff;
        TextView tv_class_name, tv_section_name, tv_dept_name, tv_role_name, tv_id, tv_mobile;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_ticketNo = itemView.findViewById(R.id.tv_ticketNo);
            tv_department = itemView.findViewById(R.id.tv_department);
            tv_issueTitle = itemView.findViewById(R.id.tv_issueTitle);
            tv_TicketLevel = itemView.findViewById(R.id.tv_TicketLevel);
            tv_TicketType= itemView.findViewById(R.id.tv_TicketType);
            tv_TicketDate = itemView.findViewById(R.id.tv_TicketDate);
            tv_TicketPriyority = itemView.findViewById(R.id.tv_TicketPriyority);
            tv_Subject=itemView.findViewById(R.id.tv_Subject);

            img = itemView.findViewById(R.id.img_agreement_copy1);
            delete = itemView.findViewById(R.id.btn_delete);

            ll_left = itemView.findViewById(R.id.ll_left);
            ll_right = itemView.findViewById(R.id.ll_right);
            civ_image = itemView.findViewById(R.id.civ_image);
            tv_Name = itemView.findViewById(R.id.tv_Name);
            tv_date_time = itemView.findViewById(R.id.tv_date_time);
            tv_id_num = itemView.findViewById(R.id.tv_id_num);
            tv_Description = itemView.findViewById(R.id.tv_Description);
            image_rv = itemView.findViewById(R.id.image_rv);

            iv_attachment = itemView.findViewById(R.id.iv_attachment);


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
