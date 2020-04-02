package in.varadhismartek.patashalaerp.DashboardModule.Homework;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import in.varadhismartek.Utils.Constant;
import in.varadhismartek.patashalaerp.DashboardModule.Assessment.QuestionBankModel;
import in.varadhismartek.patashalaerp.DashboardModule.DashBoardMenuActivity;
import in.varadhismartek.patashalaerp.R;
import in.varadhismartek.patashalaerp.Retrofit.APIService;
import in.varadhismartek.patashalaerp.Retrofit.ApiUtils;
import in.varadhismartek.patashalaerp.Retrofit.ApiUtilsPatashala;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomeworkAdapter extends RecyclerView.Adapter<HomeworkViewHolder> {

    private Context context;
    private List<HomeworkModel> homeworkModels;
    private String recyclerTag;
    private ArrayList<String> list;
    private ArrayList<MyModel> myModelList;
    private Homework_Interface homework_interface;
    ArrayList<String> QuestionList;

    private APIService mApiService;
    private ImageView ivSendNext;
    String currentDate;
    private ArrayList<HomeworkModel> homeworkModelsInbox;
    private ArrayList<HomeworkModel> bookHomeWorkList;
    private ArrayList<HomeworkModel> urlHomeWorkList;
    private ArrayList<HomeworkModel> attachmentHomeworkList;
    private ArrayList<HomeworkModel> homeworkReportModels;

    private ArrayList<QuestionBankModel> questionBankModels;

    public HomeworkAdapter(Context context, List<HomeworkModel> homeworkModels, String recyclerTag,
                           Homework_Interface homework_interface/*, ImageView ivSendNext*/) {
        this.context = context;
        this.homeworkModels = homeworkModels;
        this.recyclerTag = recyclerTag;
        this.homework_interface = homework_interface;
        //   this.ivSendNext =ivSendNext;
        this.mApiService = ApiUtils.getAPIService();
    }


    public HomeworkAdapter(ArrayList<MyModel> myModelList, Context context, String recyclerTag) {

        this.myModelList = myModelList;
        this.context = context;
        this.recyclerTag = recyclerTag;
    }


    public HomeworkAdapter(ArrayList<HomeworkModel> homeworkModelsInbox, Context context, String currentDate, String recyclerTag) {
        this.homeworkModelsInbox = homeworkModelsInbox;
        this.context = context;
        this.currentDate = currentDate;
        this.recyclerTag = recyclerTag;
    }

    public HomeworkAdapter(ArrayList<HomeworkModel> bookHomeWorkList, String recyclerTag, Context context) {
        this.bookHomeWorkList = bookHomeWorkList;
        this.attachmentHomeworkList = bookHomeWorkList;

        this.homeworkReportModels = bookHomeWorkList;
        this.recyclerTag = recyclerTag;
        this.context = context;

    }

    public HomeworkAdapter(Context context, ArrayList<HomeworkModel> bookHomeWorkList, String recyclerTag) {
        this.context = context;
        this.urlHomeWorkList = bookHomeWorkList;
        this.recyclerTag = recyclerTag;


    }

    public HomeworkAdapter(String recyclerTag, Context context, ArrayList<QuestionBankModel> questionBankModels) {
        this.recyclerTag = recyclerTag;
        this.context = context;
        this.questionBankModels = questionBankModels;
        mApiService = ApiUtilsPatashala.getService();
        notifyDataSetChanged();
    }

    public HomeworkAdapter(Context context, String recyclerTag, ArrayList<String> questionList) {
        //RV_QUESTION_BANK_IMAGE_ROW
        this.recyclerTag = recyclerTag;
        this.context = context;
        this.QuestionList = questionList;

    }


    @NonNull
    @Override
    public HomeworkViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        switch (recyclerTag) {

            case Constant.RV_QUESTION_BANK_ROW:
                return new HomeworkViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_question_row, parent, false));

            case Constant.RV_HOMEWORK_BARRIER:
                return new HomeworkViewHolder(LayoutInflater.from(context).inflate(R.layout.recyclerview_list_details, parent, false));

            case Constant.RV_HOMEWORK_BOOK:
                return new HomeworkViewHolder(LayoutInflater.from(context).inflate(R.layout.simple_text_layout, parent, false));

            case Constant.RV_HOMEWORK_URL:
                return new HomeworkViewHolder(LayoutInflater.from(context).inflate(R.layout.simple_text_layout, parent, false));

            case Constant.RV_HOMEWORK_VIEW_ATTACHMENT:
                return new HomeworkViewHolder(LayoutInflater.from(context).inflate(R.layout.attachment_image_row, parent, false));

            case Constant.RV_HOMEWORK_INBOX_ROW:
                return new HomeworkViewHolder(LayoutInflater.from(context)
                        .inflate(R.layout.layout_homework_inbox_row, parent, false));

            case Constant.RV_HOMEWORK_REPORT:
                return new HomeworkViewHolder(LayoutInflater.from(context)
                        .inflate(R.layout.layout_homework_report_row, parent, false));

            case Constant.RV_QUESTION_BANK_IMAGE_ROW:
                return new HomeworkViewHolder(LayoutInflater.from(context)
                        .inflate(R.layout.attachment_image_row, parent, false));


        }

        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull final HomeworkViewHolder holder, @SuppressLint("RecyclerView") final int position) {


        switch (recyclerTag) {

            case Constant.RV_HOMEWORK_BARRIER:
                final HomeworkModel homeworkModel = homeworkModels.get(position);
                holder.tvName.setText(homeworkModels.get(position).getDivisionClassName());
                holder.tvNumber.setText(String.valueOf(homeworkModels.get(position).getNoOfHomework()));

                if (homeworkModels.get(position).isCheked()) {

                    holder.cardRow.setBackgroundResource(R.color.barrier_green_colorPrimary);
                    holder.tvName.setTextColor(Color.WHITE);
                    holder.tvNumber.setTextColor(Color.WHITE);

                } else {

                    holder.cardRow.setBackgroundColor(Color.WHITE);
                    holder.tvName.setTextColor(Color.GRAY);
                    holder.tvNumber.setTextColor(Color.GRAY);
                }

                holder.check_box.setChecked(homeworkModels.get(position).isCheked());

                holder.check_box.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                        if (b) {
                            holder.cardRow.setBackgroundResource(R.color.barrier_green_colorPrimary);
                            holder.tvName.setTextColor(Color.WHITE);
                            holder.tvNumber.setTextColor(Color.WHITE);
                            homeworkModel.setCheked(true);


                        } else {
                            holder.cardRow.setBackgroundColor(Color.WHITE);
                            holder.tvName.setTextColor(Color.GRAY);
                            holder.tvNumber.setTextColor(Color.GRAY);
                            homeworkModel.setCheked(false);

                        }

                        JSONObject object = new JSONObject();
                        JSONObject obj = new JSONObject();
                        JSONObject objDivisions = new JSONObject();

                        try {
                            object.put("division", homeworkModels.get(position).getDivisionClassName());
                            object.put("status", String.valueOf(b));
                            obj.put("1", object);
                            objDivisions.put("divisions", obj);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Log.d("MyJSON", objDivisions.toString());

                        mApiService.updateDivisionsHomeworkBarrierStatus(Constant.SCHOOL_ID, Constant.CUSTOM_EMPLOYEE_ID,
                                objDivisions.toString()).enqueue(new Callback<Object>() {
                            @Override
                            public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {

                                if (response.isSuccessful()) {

                                    Log.d("MyJSON", String.valueOf(response.code()));
                                } else {
                                    Log.d("MyJSON", String.valueOf(response.code()));

                                }
                            }

                            @Override
                            public void onFailure(@NonNull Call<Object> call, @NonNull Throwable t) {
                                Log.d("MyJSON", t.getMessage());

                            }
                        });

                    }
                });


                holder.check_box.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        homework_interface.selectRow(homeworkModels.get(position).getDivisionClassName(),
                                homeworkModels.get(position).getNoOfHomework(), position);
                        return false;
                    }
                });


                break;


            case Constant.RV_HOMEWORK_BOOK:
                holder.tv_text.setText("Book Name: " + bookHomeWorkList.get(position).getBookName());
                //  holder.tv_author.setText("Book Author: " + bookHomeWorkList.get(position).getBookAuthor());
                holder.tv_pageno.setText("Book Page No: " + bookHomeWorkList.get(position).getBookPageNo());
                holder.tv_reference.setVisibility(View.GONE);
                holder.tv_author.setVisibility(View.GONE);

                // holder.tv_reference.setText("Book Reference: "+bookHomeWorkList.get(position).getBookCover());

                break;


            case Constant.RV_HOMEWORK_URL:
                holder.tv_author.setVisibility(View.GONE);
                holder.tv_pageno.setVisibility(View.GONE);
                holder.tvUrlLink.setVisibility(View.VISIBLE);
                holder.tvUrlLink.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });

                holder.tv_text.setText("Reference URL: " + urlHomeWorkList.get(position).getUrlReference());

                holder.tv_reference.setText("URL Description: " + urlHomeWorkList.get(position).getUrlDescription());
                break;


            case Constant.RV_HOMEWORK_VIEW_ATTACHMENT:
                String attachImage = attachmentHomeworkList.get(position).getStrAttachment();
                Log.i("attachImage***", "" + Constant.IMAGE_LINK + attachImage);

                if (!attachImage.equals("") || !attachImage.isEmpty()) {
                    Picasso.with(context).
                            load(Constant.IMAGE_LINK + attachImage)
                            .placeholder(R.drawable.patashala_logo)
                            .resize(100, 100)
                            .centerCrop()
                            .into(holder.iv_attach);

                } else {
                    holder.iv_attach.setImageResource(R.drawable.patashala_logo);
                }

                holder.iv_cancel.setVisibility(View.GONE);
                break;


            case Constant.RV_HOMEWORK_INBOX_ROW:

                System.out.println("Sub***" + homeworkModelsInbox.get(position).getSubject());
                holder.view_color.setBackgroundResource(R.drawable.circle_yellow);

                holder.tv_class.setText("Class: " + homeworkModelsInbox.get(position).getHomeClass());
                holder.tv_subject.setText("Subject: " + homeworkModelsInbox.get(position).getSubject());
                holder.tv_section.setText("Section: " + homeworkModelsInbox.get(position).getSection());
                holder.tv_work_title.setText("Homework Title: " + homeworkModelsInbox.get(position).getHomeworkTitle());
                holder.tv_description.setText("Description: " + homeworkModelsInbox.get(position).getDescription());
                holder.tv_assign_date.setText(homeworkModelsInbox.get(position).getStartDate());
                holder.tv_submit_date.setText(homeworkModelsInbox.get(position).getEndDate());

                holder.rl_inbox_row.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        final DashBoardMenuActivity homeWorkActivity = (DashBoardMenuActivity) context;
                        Bundle bundle = new Bundle();

                        bundle.putString("HOMEWORK_UUID", homeworkModelsInbox.get(position).getHomeworkId());
                        bundle.putString("STAFF", "ADMIN");
                        homeWorkActivity.loadFragment(Constant.HOMEWORK_VIEW_FRAGMENT, bundle);
                    }
                });

                break;


            case Constant.RV_QUESTION_BANK_ROW:

                holder.tv_class.setText("Class: " + questionBankModels.get(position).getClassName());
                holder.tv_section.setText("Section: " + questionBankModels.get(position).getSection());
                holder.tvSubject.setText("Subject: " + questionBankModels.get(position).getSubject());
                holder.tv_work_title.setText("Title: " + questionBankModels.get(position).getQuesBankTitle());
                holder.tv_description.setText("Description: " + questionBankModels.get(position).getDesc());
                String strDate = questionBankModels.get(position).getAddedDate();


                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");


                try {
                    Calendar c = Calendar.getInstance();
                    c.setTime(sdf.parse(strDate));
                    c.add(Calendar.DATE, 0);
                    String convertDate = sdf.format(c.getTime());  // dt is now the new date

                } catch (ParseException e) {
                    e.printStackTrace();
                }

                HomeworkAdapter questionBankImageAdapter = new HomeworkAdapter(context, Constant.RV_QUESTION_BANK_IMAGE_ROW,
                        questionBankModels.get(position).getQuestionBankAttachList());

                Log.i("ImageList_Size", "" + questionBankModels.get(position).getQuestionBankAttachList());
                holder.recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
                holder.recyclerView.setAdapter(questionBankImageAdapter);
                questionBankImageAdapter.notifyDataSetChanged();

                holder.linearLayoutQuestion.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        final String strQuestionId = questionBankModels.get(position).getQuesBankId();

                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setMessage("Are you sure you want to delete")
                                .setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                        mApiService.deleteQuestionBank(Constant.SCHOOL_ID,
                                                strQuestionId).enqueue(new Callback<Object>() {
                                            @Override
                                            public void onResponse(Call<Object> call, Response<Object> response) {
                                                Log.i("Res***", "" + response.body());

                                                notifyDataSetChanged();
                                                if (response.isSuccessful()) {
                                                    try {
                                                        JSONObject object = new JSONObject(new Gson().toJson(response.body()));
                                                        String status = (String) object.get("status");
                                                        if (status.equalsIgnoreCase("success")) {
                                                            Toast.makeText(context, "Question bank deleted successfully",
                                                                    Toast.LENGTH_SHORT).show();

                                                            Intent intent = new Intent(context, DashBoardMenuActivity.class);
                                                            intent.putExtra("Fragment_Type", "QUESTIONBANK_LIST");

                                                            context.startActivity(intent);


                                                        }
                                                    } catch (JSONException je) {

                                                    }


                                                }

                                            }

                                            @Override
                                            public void onFailure(Call<Object> call, Throwable t) {

                                            }
                                        });


                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.show();

                        return false;
                    }
                });


                break;


            case Constant.RV_QUESTION_BANK_IMAGE_ROW:

                if (!QuestionList.get(position).equals("")) {
                    Glide.with(context).load(Constant.IMAGE_LINK + QuestionList.get(position)).into(holder.iv_attach);
                    holder.iv_cancel.setVisibility(View.GONE);
                }

                break;

            case Constant.RV_HOMEWORK_REPORT:

                holder.tvSlNo.setText(String.valueOf(homeworkReportModels.get(position).getCount()));
                holder.tvHWStudentName.setText(homeworkReportModels.get(position).getStudent_first_name() + " "
                        + homeworkReportModels.get(position).getStudent_last_name());
                holder.tvHWComplete.setText(homeworkReportModels.get(position).getCompletion_percentage() + " % Completed");

                final String student_Name = homeworkReportModels.get(position).getStudent_first_name() + " "
                        + homeworkReportModels.get(position).getStudent_last_name();

                final String stuRole = homeworkReportModels.get(position).getRoll_no();
                final String stuDesc = homeworkReportModels.get(position).getRep_description();
                final String stuPercentage = homeworkReportModels.get(position).getCompletion_percentage();

                holder.ll_report_view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        reportViewDialog(student_Name, stuRole, stuDesc, stuPercentage);

                    }
                });


                break;

        }

    }

    private void reportViewDialog(String student_Name, String stuRole, String stuDesc, String stuPercentage) {

        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.report_view_details);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.show();
        dialog.setCancelable(true);
        TextView student_name= dialog.findViewById(R.id.student_name);
        TextView student_role= dialog.findViewById(R.id.student_role);
        TextView task_percentage= dialog.findViewById(R.id.task_percentage);
        TextView tv_reportDesc= dialog.findViewById(R.id.tv_reportDesc);
        LinearLayout ll_cancel= dialog.findViewById(R.id.ll_cancel);

        student_name.setText(student_Name);
        student_role.setText(stuRole);
        task_percentage.setText(stuPercentage);
        tv_reportDesc.setText(stuDesc);

        ll_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


    }

    @Override
    public int getItemCount() {

        switch (recyclerTag) {

            case Constant.RV_HOMEWORK_BARRIER:
                return homeworkModels.size();

            case Constant.RV_HOMEWORK_BOOK:
                return bookHomeWorkList.size();

            case Constant.RV_HOMEWORK_URL:
                return urlHomeWorkList.size();

            case Constant.RV_HOMEWORK_VIEW_ATTACHMENT:
                return attachmentHomeworkList.size();

            case Constant.RV_HOMEWORK_INBOX_ROW:
                return homeworkModelsInbox.size();

            case Constant.RV_QUESTION_BANK_ROW:
                return questionBankModels.size();

            case Constant.RV_QUESTION_BANK_IMAGE_ROW:
                return QuestionList.size();

            case Constant.RV_HOMEWORK_REPORT:
                return homeworkReportModels.size();
        }

        return 0;
    }

}
