package in.varadhismartek.patashalaerp.DashboardModule.Assessment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import in.varadhismartek.Utils.Constant;
import in.varadhismartek.patashalaerp.AddWing.AddWingsModel;
import in.varadhismartek.patashalaerp.DashboardModule.DashBoardMenuActivity;
import in.varadhismartek.patashalaerp.R;
import in.varadhismartek.patashalaerp.Retrofit.APIService;
import in.varadhismartek.patashalaerp.Retrofit.ApiUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AssessmentAdapter extends RecyclerView.Adapter<AssessmentViewHolder> {
    private String recyclerTag;
    private ArrayList<AssesmentModel> arrayList;
    private ArrayList<AddWingsModel> addSportsModel;
    private ArrayList<AddWingsModel> addExamArrayList;
    private ArrayList<AssesmentModel> addCurricularList;

    private ArrayList<AssesmentModel> assesmentModels;
    private ArrayList<SectionSubjectModel> sectionSubjectModelsNew;
    private ArrayList<SectionSubjectModel> sectionSubjectModels;
    private ArrayList<AssesmentModel> arrayListSport;

    private Context mContext;
    private ImageView ivSendExam;
    private APIService apiService;
    private Dialog dialog;


    private String strCode, strSec, strDiv, strClass, strSub, strSubType, subStatus, strSports, strStatus;

    public AssessmentAdapter(ArrayList<AssesmentModel> arrayList, Context mContext, String recyclerTag) {
        this.mContext = mContext;
        this.arrayList = arrayList;
        this.recyclerTag = recyclerTag;
        apiService = ApiUtils.getAPIService();
    }

    public AssessmentAdapter(ArrayList<AddWingsModel> addExamArrayList, Context mContext,
                             ImageView ivSendExam, String recyclerTag) {

        this.addExamArrayList = addExamArrayList;
        this.mContext = mContext;
        this.ivSendExam = ivSendExam;
        this.recyclerTag = recyclerTag;

        apiService = ApiUtils.getAPIService();
        notifyDataSetChanged();

    }

    public AssessmentAdapter(String recyclerTag, Context mContext, ArrayList<AssesmentModel> addCurricularList) {
        this.mContext = mContext;
        this.addCurricularList = addCurricularList;
        this.recyclerTag = recyclerTag;
        apiService = ApiUtils.getAPIService();

    }
//subject

    public AssessmentAdapter(ArrayList<SectionSubjectModel> sectionSubjectModelsNew, String recyclerTag, Context mContext) {
        this.sectionSubjectModelsNew = sectionSubjectModelsNew;
        this.sectionSubjectModels = sectionSubjectModelsNew;
        this.recyclerTag = recyclerTag;
        this.mContext = mContext;
        apiService = ApiUtils.getAPIService();
        notifyDataSetChanged();

    }

    public AssessmentAdapter(Context mContext, ArrayList<AssesmentModel> assesmentModels, String recyclerTag) {
        this.mContext = mContext;
        this.assesmentModels = assesmentModels;
        this.arrayListSport = assesmentModels;
        this.recyclerTag = recyclerTag;
        apiService = ApiUtils.getAPIService();

        notifyDataSetChanged();
    }

    public AssessmentAdapter(Context mContext, String recyclerTag, ArrayList<AddWingsModel> addSportsModel) {
        this.mContext = mContext;
        this.recyclerTag = recyclerTag;
        this.addSportsModel = addSportsModel;
        apiService = ApiUtils.getAPIService();

        // notifyDataSetChanged();


    }


    @NonNull
    @Override
    public AssessmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (recyclerTag) {

            case Constant.RV_ASSESSMENT_GRADE_ROW:
                return new AssessmentViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_grade_row,
                        parent, false));
            case Constant.RV_EXAMS_ROW:
                return new AssessmentViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.select_module_row_card,
                        parent, false));

            case Constant.RV_EXAMS_BARRIER_ROW:
                return new AssessmentViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_exam_barriers_list,
                        parent, false));

            case Constant.RV_SUBJECT_ROW:
                return new AssessmentViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.select_subject_row,
                        parent, false));

            case Constant.RV_SUBJECT_ROW_CLASS:
                return new AssessmentViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.select_subject_row,
                        parent, false));


            case Constant.RV_CURRICULAR_ROW:
                return new AssessmentViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_grade_row,
                        parent, false));

            case Constant.RV_ASSESSMENT_SPORTS_ROW:
                return new AssessmentViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.select_module_row_card,
                        parent, false));

            case Constant.RV_ASSESSMENT_SPORTS_BARRIER:
                return new AssessmentViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.stop_barrier_row,
                        parent, false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull final AssessmentViewHolder holder, final int i) {
        switch (recyclerTag) {
            case Constant.RV_ASSESSMENT_SPORTS_BARRIER:

                holder.tvSportName.setText(arrayListSport.get(i).getSportName());
                int num = (int) Double.parseDouble(arrayListSport.get(i).getMaxPlayers());
                int num2 = (int) Double.parseDouble(arrayListSport.get(i).getMentors());
                int num3 = (int) Double.parseDouble(arrayListSport.get(i).getSupportPlayers());

                holder.tvMaxPlayer.setText(String.valueOf(num));
                holder.tvMentorNo.setText(String.valueOf(num2));
                holder.tvSupportPlayer.setText(String.valueOf(num3));
                holder.tvRoleName.setText(arrayListSport.get(i).getMentorPosition_name());

                break;

            case Constant.RV_ASSESSMENT_SPORTS_ROW:
                holder.check_box.setText(addSportsModel.get(i).getWingsName());
                boolean flagNew = addSportsModel.get(i).isWingsSelected();

                if (flagNew) {
                    holder.check_box.setBackgroundColor(Color.parseColor("#72d548"));
                    holder.check_box.setTextColor(Color.WHITE);

                } else {
                    holder.check_box.setBackgroundColor(Color.WHITE);
                    holder.check_box.setTextColor(Color.BLACK);

                }

                holder.check_box.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        strSports = holder.check_box.getText().toString();
                        String status = String.valueOf(addSportsModel.get(i).isWingsSelected());
                        if (status.equalsIgnoreCase("true")) {
                            strStatus = "false";
                        } else {
                            strStatus = "true";
                        }

                        JSONObject objectSecdtion = new JSONObject();
                        JSONObject object = new JSONObject();
                        try {


                            object.put("sports_name", strSports);
                            object.put("status", strStatus);
                            objectSecdtion.put(i + "", object);

                            Log.i("objectSecdtion***", "" + objectSecdtion);
                        } catch (JSONException je) {

                        }

                        apiService.updateSportsByStatus(Constant.SCHOOL_ID, objectSecdtion, Constant.EMPLOYEE_BY_ID)
                                .enqueue(new Callback<Object>() {
                                    @Override
                                    public void onResponse(Call<Object> call, Response<Object> response) {

                                        if (response.isSuccessful()) {
                                            Log.i("sport_update***", "" + response.body());

                                            ExamActivity examActivity = (ExamActivity) mContext;
                                            examActivity.loadFragment(Constant.ADD_SPORT, null);
                                           /* Intent intent = new Intent(mContext, ExamActivity.class);
                                            intent.putExtra("Assessement_Option", "Sport");
                                            mContext.startActivity(intent);
                                            ((Activity) mContext).finish();*/
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<Object> call, Throwable t) {

                                    }
                                });
                    }
                });

                holder.check_box.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        openDialogForUpdateSport(holder.check_box.getText().toString());
                        notifyDataSetChanged();

                        return false;
                    }
                });

                break;
            case Constant.RV_CURRICULAR_ROW:
                holder.tv_grade.setText(addCurricularList.get(i).getName());
                System.out.println("mmm**" + addCurricularList.get(i).getMarks());
                holder.tv_minMarks.setVisibility(View.GONE);
                holder.tv_maxMarks.setText(addCurricularList.get(i).getMarks());
                if (i == 0) {
                    holder.ll_gradeRow.setBackgroundResource(R.color.marksRowColor);

                } else if (i % 2 != 0) {
                    holder.ll_gradeRow.setBackgroundResource(R.color.white);

                }

                holder.ll_gradeRow.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        openDialogForCurricularUpdate(holder.tv_grade.getText().toString(),
                                Long.parseLong(holder.tv_maxMarks.getText().toString()));
                        notifyDataSetChanged();
                        return false;
                    }
                });

                break;

            case Constant.RV_SUBJECT_ROW_CLASS:
                Log.i("typee***", "" + sectionSubjectModels.get(i).getSubType());
                holder.check_box.setText(sectionSubjectModels.get(i).getSubjectName());
                holder.tvsubCode.setText(sectionSubjectModels.get(i).getSubCode() + "\n ");
                holder.check_box.setBackgroundColor(Color.parseColor("#f59642"));
                holder.check_box.setTextColor(Color.WHITE);


                holder.check_box.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        System.out.println("code**" + sectionSubjectModels.get(i).getSubCode());

                        strCode = sectionSubjectModels.get(i).getSubCode();
                        strSec = sectionSubjectModels.get(i).getSectionName();
                        strDiv = sectionSubjectModels.get(i).getDivisionName();
                        strClass = sectionSubjectModels.get(i).getClassName();
                        strSub = sectionSubjectModels.get(i).getSubjectName();
                        strSubType = sectionSubjectModels.get(i).getSubType();

                        String strStatus = String.valueOf(sectionSubjectModels.get(i).isIsselect());
                        if (strStatus.equalsIgnoreCase("true")) {
                            subStatus = "false";
                        } else {
                            subStatus = "true";
                        }
                        openDialogForSubjectUpdate(strCode, strSec, strDiv, strClass, strSub, strSubType, subStatus);
                        return false;
                    }
                });


                break;
            case Constant.RV_SUBJECT_ROW:

                Log.i("typee***AAA**", "" + sectionSubjectModels.get(i).getSubType());
                System.out.print("code**" + sectionSubjectModelsNew.get(i).getSubjectName());
                holder.check_box.setText(sectionSubjectModelsNew.get(i).getSubjectName());
                holder.check_box.setBackgroundColor(Color.parseColor("#f59642"));
                holder.check_box.setTextColor(Color.WHITE);
                String strSubTypeValue = sectionSubjectModelsNew.get(i).getSubType();

                if (strSubTypeValue.length() > 0) {
                    String strFirstChar = String.valueOf(strSubTypeValue.charAt(0));
                    holder.tv_subType.setText(strFirstChar);
                }
                holder.tvsubCode.setText(sectionSubjectModels.get(i).getSubCode());
                holder.check_box.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {


                        strCode = sectionSubjectModelsNew.get(i).getSubCode();
                        strSec = sectionSubjectModelsNew.get(i).getSectionName();
                        strDiv = sectionSubjectModelsNew.get(i).getDivisionName();
                        strClass = sectionSubjectModelsNew.get(i).getClassName();
                        strSub = sectionSubjectModelsNew.get(i).getSubjectName();
                        strSubType = sectionSubjectModelsNew.get(i).getSubType();

                        String strStatus = String.valueOf(sectionSubjectModelsNew.get(i).isIsselect());
                        if (strStatus.equalsIgnoreCase("true")) {
                            subStatus = "false";
                        } else {
                            subStatus = "true";
                        }
                        openDialogForSubjectUpdate(strCode, strSec, strDiv, strClass, strSub, strSubType, subStatus);
                        return false;
                    }
                });


                break;

            case Constant.RV_ASSESSMENT_GRADE_ROW:

                holder.tv_grade.setText(arrayList.get(i).getGrade());
                holder.tv_minMarks.setText(arrayList.get(i).getMinMarks().toString());
                holder.tv_maxMarks.setText(arrayList.get(i).getMaxMarks().toString());

                if (i == 0) {
                    holder.ll_gradeRow.setBackgroundResource(R.color.marksRowColor);

                } else if (i % 2 != 0) {
                    holder.ll_gradeRow.setBackgroundResource(R.color.white);

                }

                break;

            case Constant.RV_EXAMS_ROW:
                final AddWingsModel addWingsModel = addExamArrayList.get(i);

                holder.check_box.setText(addExamArrayList.get(i).getWingsName());
                boolean flag = addWingsModel.isWingsSelected();

                if (flag) {
                    holder.check_box.setBackgroundColor(Color.parseColor("#72d548"));
                    holder.check_box.setTextColor(Color.WHITE);

                } else {
                    holder.check_box.setBackgroundColor(Color.WHITE);
                    holder.check_box.setTextColor(Color.BLACK);

                }

                holder.check_box.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String examType = addExamArrayList.get(i).getWingsName();
                        String examTypeStatus = String.valueOf(addExamArrayList.get(i).isWingsSelected());
                        if (examTypeStatus.equalsIgnoreCase("true")) {
                            examTypeStatus = "false";
                        } else {
                            examTypeStatus = "true";
                        }

                        JSONObject objectWings = new JSONObject();
                        try {

                            JSONObject exam = new JSONObject();
                            exam.put("name", examType);
                            exam.put("status", examTypeStatus);

                            objectWings.put(String.valueOf(1), exam);

                        } catch (JSONException je) {
                            je.printStackTrace();
                        }

                        Log.i("objectWings**", "" + objectWings);

                        apiService.updateExamByStatus(Constant.SCHOOL_ID, objectWings, Constant.EMPLOYEE_BY_ID)
                                .enqueue(new Callback<Object>() {
                                    @Override
                                    public void onResponse(Call<Object> call, Response<Object> response) {
                                        Log.i("RESPONSE**", "" + response.body());

                                        ExamActivity examActivity = (ExamActivity) mContext;
                                        examActivity.loadFragment(Constant.ADD_EXAMINATION, null);

                                        notifyDataSetChanged();

                                    }

                                    @Override
                                    public void onFailure(Call<Object> call, Throwable t) {

                                    }
                                });


                    }
                });


                holder.check_box.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        openDialogForUpdate(holder.check_box.getText().toString());
                        notifyDataSetChanged();

                        return false;
                    }
                });


                break;


            case Constant.RV_EXAMS_BARRIER_ROW:
                holder.tvExamType.setText(assesmentModels.get(i).getExamType());
                holder.tvDivision.setText(assesmentModels.get(i).getDivisionNmae());
                holder.tvClass.setText(assesmentModels.get(i).getClassName());
                holder.tvSubject.setText(assesmentModels.get(i).getSubjectName());
                holder.tvMin.setText(String.valueOf(assesmentModels.get(i).getMinMarks()));
                holder.tvMax.setText(String.valueOf(assesmentModels.get(i).getMaxMarks()));
                holder.tvDuration.setText(assesmentModels.get(i).getExamDuration() + " min ");

                break;
        }
    }

    private void openDialogForUpdateSport(final String oldname) {
        dialog = new Dialog(mContext);
        dialog.setContentView(R.layout.sport_update_dialog);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.show();

        final TextView oldName = dialog.findViewById(R.id.tv_oldname);
        final EditText et_new_name = dialog.findViewById(R.id.ed_newName);
        final TextView bt_cancel = dialog.findViewById(R.id.bt_cancel);
        final TextView bt_update = dialog.findViewById(R.id.bt_update);

        oldName.setText(oldname);
        et_new_name.setHint("New Sports Name");

        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });
        bt_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_new_name.getText().toString().isEmpty()) {
                    Toast.makeText(mContext, "New Name Can Not Be Empty", Toast.LENGTH_SHORT).show();

                } else {
//addSportsModel
                    for (int i = 0; i < addSportsModel.size(); i++) {
                        if (et_new_name.getText().toString().contains(addSportsModel.get(i).getWingsName())) {
                            Toast.makeText(mContext, " Data already exist", Toast.LENGTH_SHORT).show();
                        } else {
                            apiService.updateSportsByName(Constant.SCHOOL_ID, Constant.EMPLOYEE_BY_ID,
                                    oldName.getText().toString(), et_new_name.getText().toString())
                                    .enqueue(new Callback<Object>() {
                                        @Override
                                        public void onResponse(Call<Object> call, Response<Object> response) {
                                            if (response.isSuccessful()) {
                                                Log.i("res**", "" + response.body());
                                                dialog.dismiss();
                                                Toast.makeText(mContext, "Sports have update successfully", Toast.LENGTH_SHORT);
                                                ExamActivity examActivity = (ExamActivity) mContext;
                                                examActivity.loadFragment(Constant.ADD_SPORT, null);


                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<Object> call, Throwable t) {
                                            Log.i("res**tt", "" + t.toString());

                                        }
                                    });
                        }
                    }


                }
            }
        });

    }

    private void openDialogForCurricularUpdate(final String oldName, long oldmarks) {

        dialog = new Dialog(mContext);
        dialog.setContentView(R.layout.curricular_update_dialog);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.show();
        final TextView tvOldActivityName = dialog.findViewById(R.id.tv_oldname);
        // final TextView tv_oldmarks = dialog.findViewById(R.id.tv_oldmarks);
        final EditText edNewName = dialog.findViewById(R.id.ed_newName);
        final EditText edNewMarks = dialog.findViewById(R.id.ed_newmarks);

        tvOldActivityName.setText(oldName);
        edNewMarks.setText(String.valueOf(oldmarks));
        final TextView bt_cancel = dialog.findViewById(R.id.bt_cancel);
        final TextView bt_update = dialog.findViewById(R.id.bt_update);
        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });

        bt_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String newName = edNewName.getText().toString();
                final String new_Marks = edNewMarks.getText().toString();

                if (newName.isEmpty() || new_Marks.isEmpty()) {
                    Toast.makeText(mContext, "New Name and New Marks Can Not Be Empty", Toast.LENGTH_SHORT).show();

                } else {
                    apiService.updateCurricular(Constant.SCHOOL_ID, Constant.EMPLOYEE_BY_ID, oldName, newName, new_Marks, "true")
                            .enqueue(new Callback<Object>() {
                                @Override
                                public void onResponse(Call<Object> call, Response<Object> response) {
                                    Log.i("RES***", "" + response.body());
                                    Log.i("RES***", "" + response.code());
                                    JSONObject object = null;

                                    if (response.isSuccessful()) {
                                        Log.d("UPDATE_WINGS_SUCCESS", response.body().toString());
                                        dialog.dismiss();
                                        try {
                                            object = new JSONObject(new Gson().toJson(response.body()));
                                            String status = object.getString("status");

                                            if (status.equalsIgnoreCase("Success")) {
                                                Toast.makeText(mContext, "Curricular have update successfully ",
                                                        Toast.LENGTH_SHORT).show();
                                                notifyDataSetChanged();

                                               /*  ExamActivity examActivity =(ExamActivity)mContext;
                                                 examActivity.loadFragment(Constant.ADD_CURRICULAR,null);

*/
                                            }

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }


                                    } else {
                                        Log.d("UPDATE_WINGS_FAIL", String.valueOf(response.code()));
                                        if (String.valueOf(response.code()).equals("409")) {
                                            Toast.makeText(mContext, "Activity name already exists", Toast.LENGTH_SHORT).show();
                                            dialog.dismiss();
                                        }
                                        if (String.valueOf(response.code()).equals("404")) {
                                            Toast.makeText(mContext, "Old Activity name not exists", Toast.LENGTH_SHORT).show();
                                            dialog.dismiss();
                                        }

                                    }
                                }

                                @Override
                                public void onFailure(Call<Object> call, Throwable t) {

                                }
                            });
                }
            }
        });


    }

    private void openDialogForSubjectUpdate(String strCode, String strSec, final String strDiv,
                                            final String strClass, String strSub, final String strSubType, final String subStatus) {

        dialog = new Dialog(mContext);
        dialog.setContentView(R.layout.subject_update_dialog);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.show();
        final TextView subName = dialog.findViewById(R.id.tvOldSub);
        final EditText newSub = dialog.findViewById(R.id.ed_newsub);
        final EditText edCode = dialog.findViewById(R.id.subcode);
        final EditText subType = dialog.findViewById(R.id.ed_subtype);

        final TextView bt_cancel = dialog.findViewById(R.id.bt_cancel);
        final TextView bt_update = dialog.findViewById(R.id.bt_update);
        subName.setText(strSub);
        newSub.setHint("New Subject Name");
        edCode.setText(strCode);
        subType.setText(strSubType);
        final JSONArray jsonArray = new JSONArray();
        jsonArray.put(strSec);

        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });

        bt_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (newSub.equals("") || newSub.getText().toString().trim().isEmpty() || newSub.getText().toString().trim().equalsIgnoreCase("New Subject Name")) {
                    Toast.makeText(mContext, "New Name Can Not Be Empty", Toast.LENGTH_SHORT).show();

                } else {


                    apiService.updateSubject(Constant.SCHOOL_ID, strDiv, Constant.EMPLOYEE_BY_ID, strClass,
                            jsonArray, subName.getText().toString(), newSub.getText().toString(),
                            edCode.getText().toString(), subType.getText().toString(), subStatus)
                            .enqueue(new Callback<Object>() {
                                @Override
                                public void onResponse(Call<Object> call, Response<Object> response) {

                                    if (response.isSuccessful()) {
                                        dialog.dismiss();
                                        Toast.makeText(mContext, "Subject have update successfully", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(mContext, DashBoardMenuActivity.class);
                                        intent.putExtra("Fragment_Type", "Subject");
                                        mContext.startActivity(intent);
                                        ((Activity) mContext).finish();
                                    }


                                }

                                @Override
                                public void onFailure(Call<Object> call, Throwable t) {
                                    dialog.dismiss();
                                }
                            });
                }

            }
        });

    }

    private void openDialogForUpdate(final String oldName) {
        dialog = new Dialog(mContext);
        dialog.setContentView(R.layout.update_dialog);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.show();

        final TextView tv_updateTitle = dialog.findViewById(R.id.tv_updateTitle);
        final TextView tv_heading = dialog.findViewById(R.id.tv_heading);
        final TextView tv_heading_name = dialog.findViewById(R.id.tv_heading_name);
        final TextView et_new_name = dialog.findViewById(R.id.et_new_name);
        final TextView bt_cancel = dialog.findViewById(R.id.bt_cancel);
        final TextView bt_update = dialog.findViewById(R.id.bt_update);

        tv_updateTitle.setText("Update Exam Type");
        tv_heading.setText("Old Exam Type Name");
        tv_heading_name.setText(oldName);
        et_new_name.setHint("New Exam Type Name");


        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });

        bt_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String newName = et_new_name.getText().toString().trim();

                for (int i = 0; i < addExamArrayList.size(); i++) {
                    if (newName.equalsIgnoreCase(addExamArrayList.get(i).getWingsName().trim())) {

                        Toast.makeText(mContext, "Data already exist", Toast.LENGTH_SHORT).show();
                        et_new_name.setText("");
                    }
                }

                if (et_new_name.getText().toString().trim().isEmpty() || et_new_name.getText().toString().trim().equals("")) {
                    Toast.makeText(mContext, "Enter the exam type value", Toast.LENGTH_SHORT).show();
                } else {

                    apiService.updateExameByname(Constant.SCHOOL_ID,
                            oldName, newName, Constant.EMPLOYEE_BY_ID).enqueue(new Callback<Object>() {
                        @Override
                        public void onResponse(Call<Object> call, Response<Object> response) {

                            JSONObject object = null;

                            if (response.isSuccessful()) {
                                Log.d("UPDATE_WINGS_SUCCESS", response.body().toString());

                                try {
                                    object = new JSONObject(new Gson().toJson(response.body()));
                                    String status = object.getString("status");

                                    if (status.equalsIgnoreCase("Sucess")) {
                                        Toast.makeText(mContext, "Exam Type Successfully Update",
                                                Toast.LENGTH_SHORT).show();
                                        ExamActivity examActivity = (ExamActivity) mContext;
                                        examActivity.loadFragment(Constant.ADD_EXAMINATION, null);


                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }


                                dialog.dismiss();

                            } else {
                                Log.d("UPDATE_WINGS_FAIL", String.valueOf(response.code()));
                                if (String.valueOf(response.code()).equals("409")) {
                                    Toast.makeText(mContext, "Exam type name already exists", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                }
                                if (String.valueOf(response.code()).equals("404")) {
                                    Toast.makeText(mContext, "Old exam type name not exists", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                }

                            }

                        }

                        @Override
                        public void onFailure(Call<Object> call, Throwable t) {
                            Log.d("UPDATE_WINGS_EX", t.getMessage());

                        }
                    });
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        switch (recyclerTag) {

            case Constant.RV_ASSESSMENT_GRADE_ROW:
                return arrayList.size();

            case Constant.RV_EXAMS_ROW:
                return addExamArrayList.size();

            case Constant.RV_EXAMS_BARRIER_ROW:
                return assesmentModels.size();

            case Constant.RV_SUBJECT_ROW:
                return sectionSubjectModelsNew.size();
            case Constant.RV_SUBJECT_ROW_CLASS:
                return sectionSubjectModels.size();

            case Constant.RV_CURRICULAR_ROW:
                return addCurricularList.size();

            case Constant.RV_ASSESSMENT_SPORTS_ROW:
                return addSportsModel.size();

            case Constant.RV_ASSESSMENT_SPORTS_BARRIER:
                return arrayListSport.size();


        }

        return 0;
    }

    public void newValues(String s) {
        addExamArrayList.add(new AddWingsModel(s, true));
        notifyDataSetChanged();

    }
}
