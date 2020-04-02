package in.varadhismartek.patashalaerp.DashboardModule.UpdateAdapter;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import in.varadhismartek.Utils.Constant;
import in.varadhismartek.patashalaerp.DashboardModule.UpdateActivity.UpdateDivisionActivity;
import in.varadhismartek.patashalaerp.DashboardModule.UpdateActivity.UpdateSessionsActivity;
import in.varadhismartek.patashalaerp.DivisionModule.ClassDivisionHolder;
import in.varadhismartek.patashalaerp.DivisionModule.ClassSectionAndDivisionModel;
import in.varadhismartek.patashalaerp.R;
import in.varadhismartek.patashalaerp.Retrofit.APIService;
import in.varadhismartek.patashalaerp.Retrofit.ApiUtils;
import in.varadhismartek.patashalaerp.Retrofit.ApiUtilsPatashala;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateDivisionAdapter extends RecyclerView.Adapter<ClassDivisionHolder> {


    private ArrayList<ClassSectionAndDivisionModel> arrayList;
    private ArrayList<String> list;
    private Context mContext;
    private ImageView iv_sendAddDivision, iv_sendAddSubmit;
    private ArrayList<String> checkedArrayList;
    private ArrayList<String> uncheckedArrayList;
    private String recyclerTag;
    private Button bt_select_all, button_added;
    Dialog dialog;

// sessions

    private ArrayList<String> sessionList;
    private ArrayList<Integer> integerArrayList;
    List<ClassSectionAndDivisionModel> modelList = new ArrayList<>();
    Button bt_submit;
    int rowNumber = 1;
    private int date, month, year;
    private String currentDate;
    APIService apiService;
    String acdStartDate = "", acdEndDate = "", serialNo = "", sessionFromDate = "", sessionToDate = "", workingDay = "";

    private ArrayList<ClassSectionAndDivisionModel> classList,modelArrayList;

    //Sections
    private Button btnSave;

    private APIService mApiService;

    public UpdateDivisionAdapter(ArrayList<ClassSectionAndDivisionModel> arrayList, Context mContext,
                                 Button button_added, String recyclerTag, Button bt_select_all) {
        this.arrayList = arrayList;
        this.mContext = mContext;
        this.button_added = button_added;
        this.recyclerTag = recyclerTag;
        this.bt_select_all = bt_select_all;
        checkedArrayList = new ArrayList<>();
        uncheckedArrayList = new ArrayList<>();

        mApiService = ApiUtils.getAPIService();

    }

    public UpdateDivisionAdapter(List<ClassSectionAndDivisionModel> modelList, Context mContext, String recyclerTag) {
        this.modelList = modelList;
        this.mContext = mContext;
        this.recyclerTag = recyclerTag;
        apiService = ApiUtilsPatashala.getService();

    }

    public UpdateDivisionAdapter(Context mContext, ArrayList<ClassSectionAndDivisionModel> classList, String recyclerTag) {
        this.mContext = mContext;
        this.classList = classList;
        this.recyclerTag = recyclerTag;
        checkedArrayList = new ArrayList<>();
        uncheckedArrayList = new ArrayList<>();

    }

    public UpdateDivisionAdapter(Context mContext, ArrayList<ClassSectionAndDivisionModel> modelArrayList, Button btnSave, String recyclerTag) {
        this.mContext = mContext;
        this.recyclerTag = recyclerTag;
        this.modelArrayList = modelArrayList;
        this.btnSave = btnSave;

        mApiService = ApiUtils.getAPIService();
    }

    @NonNull
    @Override
    public ClassDivisionHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        switch (recyclerTag) {

            case Constant.RV_DIVISION_CARD:
                return new ClassDivisionHolder(LayoutInflater.from(mContext)
                        .inflate(R.layout.select_module_row_card, parent, false));

            case Constant.CLASS_ROW:
                return new ClassDivisionHolder(LayoutInflater.from(mContext)
                        .inflate(R.layout.select_module_row_card, parent, false));

            case Constant.RV_SECTION_ROW:
                return new ClassDivisionHolder(LayoutInflater.from(mContext)
                        .inflate(R.layout.section_item_card, parent, false));


            case Constant.RV_SESSION_ROW:
                return new ClassDivisionHolder(LayoutInflater.from(mContext)
                        .inflate(R.layout.nested_session_date_row, parent, false));
        }

        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull final ClassDivisionHolder holder, final int i) {
        switch (recyclerTag) {

            case Constant.RV_SECTION_ROW:

                int size = modelArrayList.get(i).getListSection().size();

                holder.tv_class.setText(modelArrayList.get(i).getClassName());

                StringBuffer sb = new StringBuffer();

                for (int j = 0; j < size; j++) {

                    sb.append(modelArrayList.get(i).getListSection().get(j) + " ");

                    Log.d("MY_ASCII_adapter", modelArrayList.get(i).getListSection().get(j));

                }

                holder.tv_section.setText(sb.toString());

                btnSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        JSONObject classObject = new JSONObject();

                        for (int k = 0; k < modelArrayList.size(); k++) {

                            String classValue = modelArrayList.get(k).getClassName();

                            JSONArray array = new JSONArray();
                            JSONObject jsonObject = new JSONObject();

                            for (int j = 0; j < modelArrayList.get(k).getListSection().size(); j++) {

                                String strSec = modelArrayList.get(k).getListSection().get(j);

                                array.put(strSec);

                            }

                            try {
                                jsonObject.put("sections", array);
                                jsonObject.put("order", k + 1);


                                classObject.put(classValue, jsonObject);


                            } catch (JSONException je) {

                            }
                        }


                        Log.d("OBJECT_ARR", Constant.SCHOOL_ID);
                        Log.d("OBJECT_ARR", Constant.DIVISION_NAME);
                        Log.d("OBJECT_ARR", Constant.EMPLOYEE_BY_ID);
                        Log.d("OBJECT_ARR", "" + classObject);

                        mApiService.addClassSections(Constant.SCHOOL_ID, Constant.DIVISION_NAME, classObject,
                                Constant.EMPLOYEE_BY_ID).enqueue(new Callback<Object>() {
                            @Override
                            public void onResponse(Call<Object> call, Response<Object> response) {
                                Log.i("OBJECT_ARR**RESPONSE", "" + response.body());
                                Toast.makeText(mContext, "Class and Section successfully Save",
                                        Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailure(Call<Object> call, Throwable t) {

                            }
                        });

                    }
                });

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String className = holder.tv_class.getText().toString();

                        JSONArray array = new JSONArray();
                        JSONObject jsonClass = new JSONObject();

                        for (int j = 0; j < modelArrayList.get(i).getListSection().size(); j++) {

                            String strSec = modelArrayList.get(i).getListSection().get(j);
                            array.put(strSec);
                        }


                        try {
                            String secData = (String) array.get(array.length() - 1);
                            System.out.println("jsonObject**vdata" + secData);
                            JSONArray array1 = new JSONArray();
                            array1.put(secData);
                            jsonClass.put(className, array1);
                        } catch (JSONException je) {

                        }


                        openDialog(jsonClass);// for delete
                    }
                });

                break;


            case Constant.CLASS_ROW:
                holder.check_box.setText(classList.get(i).getName());
                boolean classFlag = classList.get(i).isChecked();

                if (classFlag) {
                    holder.check_box.setBackgroundColor(Color.parseColor("#52b155"));
                    holder.check_box.setTextColor(Color.WHITE);
                    checkedArrayList.add(classList.get(i).getName());
                    Log.d("AaCheckedArrayList", "" + checkedArrayList.size());

                } else {
                    holder.check_box.setBackgroundColor(Color.WHITE);
                    holder.check_box.setTextColor(Color.BLACK);
                    checkedArrayList.remove(classList.get(i).isChecked());
                    Log.d("AaCheckedArrayList1", "" + checkedArrayList.size());

                }

                break;


            case Constant.RV_DIVISION_CARD:

                holder.check_box.setText(arrayList.get(i).getName());
                boolean flag = arrayList.get(i).isChecked();

                if (flag) {
                    holder.check_box.setBackgroundColor(Color.parseColor("#52b155"));
                    holder.check_box.setTextColor(Color.WHITE);
                    checkedArrayList.add(arrayList.get(i).getName());
                    Log.d("AaCheckedArrayList", "" + checkedArrayList.size());

                } else {
                    holder.check_box.setBackgroundColor(Color.WHITE);
                    holder.check_box.setTextColor(Color.BLACK);
                    checkedArrayList.remove(arrayList.get(i).isChecked());
                    Log.d("AaCheckedArrayList1", "" + checkedArrayList.size());

                }

                holder.check_box.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                        if (!arrayList.get(i).isChecked()) {
                            checkedArrayList.add(holder.check_box.getText().toString());
                            arrayList.get(i).setChecked(true);

                            Log.d("Checked", "" + checkedArrayList.size());
                            holder.check_box.setBackgroundColor(Color.parseColor("#52b155"));
                            holder.check_box.setTextColor(Color.WHITE);
                        } else {
                            checkedArrayList.remove(holder.check_box.getText().toString());
                            arrayList.get(i).setChecked(false);
                            Log.d("Unchecked", "" + checkedArrayList.size());
                            holder.check_box.setBackgroundColor(Color.WHITE);
                            holder.check_box.setTextColor(Color.BLACK);
                        }

                        Log.d("CheckedItem", "" + checkedArrayList.size());
                        String divisionName = arrayList.get(i).getName();
                        String divisionStatus = String.valueOf(arrayList.get(i).isChecked());

                        Log.d("CheckedItem", "" + checkedArrayList.size());
                        Log.d("moduleName", "" + divisionName);
                        Log.d("moduleStatus", "" + divisionStatus);
                        JSONArray jsonDivision = new JSONArray();
                        JSONObject jsonObject = new JSONObject();
                        try {

                            jsonObject.put("division", divisionName);
                            jsonObject.put("status", divisionStatus);
                            jsonDivision.put(jsonObject);
                        } catch (JSONException je) {

                        }

                        Log.d("jsonDivision", "" + jsonDivision);
                        divisionStatusUpdate(jsonDivision);

                    }
                });


                bt_select_all.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String status = bt_select_all.getText().toString();
                        if (status.equalsIgnoreCase("Select All")) {

                            if (uncheckedArrayList.size() > 0) {
                                uncheckedArrayList.clear();
                            }

                            bt_select_all.setText("Unselect All");
                            bt_select_all.setBackgroundResource(R.drawable.add_back);
                            bt_select_all.setTextColor(Color.WHITE);

                            if (checkedArrayList.size() > 0)
                                checkedArrayList.clear();

                            for (int i = 0; i < arrayList.size(); i++) {
                                arrayList.get(i).setChecked(true);
                                checkedArrayList.add(arrayList.get(i).getName());

                                Log.d("asdasd", "" + checkedArrayList.size());

                            }
                            notifyDataSetChanged();

                        } else if (status.equalsIgnoreCase("Unselect All")) {

                            bt_select_all.setText("Select All");
                            bt_select_all.setBackgroundResource(R.drawable.btn_round_shape_grey);
                            bt_select_all.setTextColor(mContext.getResources().getColor(R.color.colorBlack));

                            for (int i = 0; i < arrayList.size(); i++) {

                                arrayList.get(i).setChecked(false);

                            }

                            if (checkedArrayList.size() > 0)
                                checkedArrayList.clear();

                            notifyDataSetChanged();

                        }
                    }
                });

                button_added.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (uncheckedArrayList.size() > 0) {
                            uncheckedArrayList.clear();
                        }

                        if (checkedArrayList.size() > 0) {
                            checkedArrayList.clear();
                        }

                        for (ClassSectionAndDivisionModel module : arrayList) {

                            if (!module.isChecked()) {
                                uncheckedArrayList.add(module.getName());
                            } else if (module.isChecked()) {

                                checkedArrayList.add(module.getName());
                            }

                        }


                        JSONArray array = new JSONArray();
                        for (int i = 0; i < checkedArrayList.size(); i++) {
                            array.put(checkedArrayList.get(i));
                        }

                        JSONObject obj = new JSONObject();
                        try {
                            obj.put("data", array);

                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }


                        mApiService.addDivision(Constant.SCHOOL_ID, array.toString(), Constant.EMPLOYEE_BY_ID)
                                .enqueue(new Callback<Object>() {
                                    @Override
                                    public void onResponse(Call<Object> call, Response<Object> response) {

                                        if (response.isSuccessful()) {
                                            Toast.makeText(mContext, "Added Successfully", Toast.LENGTH_SHORT).show();


                                        } else {

                                            Log.d("DIVISION_FAIL", "" + response.body());
                                        }

                                    }

                                    @Override
                                    public void onFailure(Call<Object> call, Throwable t) {
                                        Log.d("DIVISION_Exception", t.getMessage());

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




            case Constant.RV_SESSION_ROW:

                holder.tv_fromDate.setText(modelList.get(i).getRespStartDate());
                holder.tv_toDate.setText(modelList.get(i).getRespEndDate());


                holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        acdStartDate = modelList.get(i).getAcd_from_date();
                        acdEndDate = modelList.get(i).getAcd_to_date();
                        serialNo = modelList.get(i).getSession_serial_no();
                        sessionFromDate = holder.tv_fromDate.getText().toString();
                        sessionToDate = holder.tv_fromDate.getText().toString();
                        Log.i("Delete**", "" + acdStartDate + "**" + acdEndDate + "**"
                                + serialNo + "**" + sessionFromDate + "***" + sessionToDate);


                        apiService.deleteSessions(Constant.SCHOOL_ID, acdStartDate, acdEndDate, Constant.EMPLOYEE_BY_ID,
                                serialNo, sessionFromDate, sessionToDate).enqueue(new Callback<Object>() {
                            @Override
                            public void onResponse(Call<Object> call, Response<Object> response) {
                                Log.i("RESPONSE_DELETE*", "" + response.body());
                                Log.i("RESPONSE_DELETE*", "" + response.code());
                            }

                            @Override
                            public void onFailure(Call<Object> call, Throwable t) {

                            }
                        });

                    }
                });

                holder.linearLayout.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {

                        acdStartDate = modelList.get(i).getAcd_from_date();
                        acdEndDate = modelList.get(i).getAcd_to_date();
                        serialNo = modelList.get(i).getSession_serial_no();
                        workingDay = UpdateSessionsActivity.edWorkDay.getText().toString();
                        sessionFromDate = holder.tv_fromDate.getText().toString();
                        sessionToDate = holder.tv_fromDate.getText().toString();

                        Log.i("workingDayAd**", "" + workingDay);

                        openDialogForUpdate(acdStartDate, acdEndDate, serialNo, workingDay, sessionFromDate, sessionToDate);
                        notifyDataSetChanged();
                        return false;
                    }
                });

                break;


        }

    }


    private void openDialog(final JSONObject jsonClass) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(mContext);
        builder1.setMessage("Do you want to delete Sections");

        builder1.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(final DialogInterface dialog, int id) {

                mApiService.deleteClassSections(Constant.SCHOOL_ID, jsonClass,
                        Constant.EMPLOYEE_BY_ID).enqueue(new Callback<Object>() {
                    @Override
                    public void onResponse(Call<Object> call, Response<Object> response) {
                        Log.i("MAKER_RES_DELETE", "" + response.body());
                        Toast.makeText(mContext, "Section have deleted successfully",
                                Toast.LENGTH_SHORT).show();
                        dialog.cancel();

                        Intent intent = new Intent(mContext, UpdateDivisionActivity.class);
                        mContext.startActivity(intent);
                        ((Activity)mContext).finish();


                    }

                    @Override
                    public void onFailure(Call<Object> call, Throwable t) {
                        // Log.i("MAKER_RES_DELETE", "" + t.toString());
                        dialog.cancel();
                    }
                });

            }
        });

        builder1.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();

    }

    //session

    private void openDialogForUpdate(final String acdStartDate, final String acdEndDate, final String serialNo,
                                     final String workingDay, String sessionFromDate, String sessionToDate) {

        dialog = new Dialog(mContext);
        dialog.setContentView(R.layout.session_update_dialog);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.show();
        TextView tvSession = dialog.findViewById(R.id.tvSessionName);
        EditText edWorkingDay = dialog.findViewById(R.id.workday);
        final TextView tvSessionFDate = dialog.findViewById(R.id.tv_fromDate);
        final TextView tvSessionTDate = dialog.findViewById(R.id.tv_toDate);
        RelativeLayout relativeLayoutFromDate = dialog.findViewById(R.id.rl_fromDate);
        RelativeLayout relativeLayoutToDate = dialog.findViewById(R.id.rl_toDate);
        TextView tvAdd = dialog.findViewById(R.id.tv_add);

        Log.i("workingDayAd**", "" + acdStartDate + "**" + acdEndDate);
        tvSession.setText(acdStartDate + "-" + acdEndDate);
        edWorkingDay.setText(workingDay);
        tvSessionFDate.setText(sessionFromDate);
        tvSessionTDate.setText(sessionToDate);
        relativeLayoutFromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerDialog dialog = new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

                        Calendar calendar = Calendar.getInstance();
                        calendar.set(Calendar.YEAR, i);
                        calendar.set(Calendar.MONTH, i1);
                        calendar.set(Calendar.DAY_OF_MONTH, i2);

                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                        SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy", Locale.getDefault());

                        String strStartDate = simpleDateFormat.format(calendar.getTime());

                        Log.d("CHECK_DATE", strStartDate);

                        tvSessionFDate.setText(strStartDate);
                        Date date2 = new Date();
                        try {
                            date2 = simpleDateFormat.parse(strStartDate);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }


                    }
                }, year, month, date);

                dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                dialog.show();
            }
        });


        relativeLayoutToDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

                        Calendar calendar = Calendar.getInstance();
                        calendar.set(Calendar.YEAR, i);
                        calendar.set(Calendar.MONTH, i1);
                        calendar.set(Calendar.DAY_OF_MONTH, i2);

                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                        SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy", Locale.getDefault());

                        String strTotDate = simpleDateFormat.format(calendar.getTime());

                        Log.d("CHECK_DATE", strTotDate);

                        tvSessionTDate.setText(strTotDate);
                        Date date2 = new Date();
                        try {
                            date2 = simpleDateFormat.parse(strTotDate);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }


                    }
                }, year, month, date);

                dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                dialog.show();
            }
        });

        tvAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date date1 = null, date2 = null;
                try {
                    date1 = sdf.parse(tvSessionFDate.getText().toString());
                    date2 = sdf.parse(tvSessionTDate.getText().toString());

                } catch (ParseException e) {
                    e.printStackTrace();
                }

                if (date1.compareTo(date2) > 0) {
                    Toast.makeText(mContext, "Please enter valid Date", Toast.LENGTH_SHORT).show();

                } else {

                    Log.i("workingDayAd**", "" + acdStartDate + "**" + acdEndDate + "**"
                            + serialNo + "**" + tvSessionFDate.getText().toString() + "***"
                            + tvSessionTDate.getText().toString());

                    apiService.upDateSession(Constant.SCHOOL_ID, acdStartDate, acdEndDate, serialNo, Constant.EMPLOYEE_BY_ID,
                            tvSessionFDate.getText().toString(), tvSessionTDate.getText().toString())
                            .enqueue(new Callback<Object>() {
                                @Override
                                public void onResponse(Call<Object> call, Response<Object> response) {
                                    Log.i("Update_responseSession", "" + response.body());
                                    Log.i("Update_responseSession*", "" + response.code());
                                }

                                @Override
                                public void onFailure(Call<Object> call, Throwable t) {

                                }
                            });
                }


            }
        });

    }


    private void divisionStatusUpdate(JSONArray jsonDivision) {
        mApiService.updateDivisionStatus(Constant.SCHOOL_ID, jsonDivision, Constant.EMPLOYEE_BY_ID)
                .enqueue(new Callback<Object>() {
                    @Override
                    public void onResponse(Call<Object> call, Response<Object> response) {
                        Log.i("RESPONSE**", "" + response.body());
                        notifyDataSetChanged();
                    }

                    @Override
                    public void onFailure(Call<Object> call, Throwable t) {

                    }
                });
    }

    private void openDialogForUpdate(final String division) {
        dialog = new Dialog(mContext);
        dialog.setContentView(R.layout.update_delete_dialog);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        EditText etOldDivision = dialog.findViewById(R.id.etOldRole);
        TextView tvTitle = dialog.findViewById(R.id.tvTitle);
        tvTitle.setText("Division");
        final EditText newDivision = dialog.findViewById(R.id.etNewRole);
        Button updateDivision = dialog.findViewById(R.id.btnUpdateRole);
        final Button deleteDivision = dialog.findViewById(R.id.btnDeleteRole);

        etOldDivision.setText(division);
        etOldDivision.setFocusable(false);
        etOldDivision.setFocusableInTouchMode(false);


        updateDivision.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String updateDivision = newDivision.getText().toString();
                if (!updateDivision.equals("")) {
                    updateDivisionApi(division, updateDivision);

                }

            }
        });


        deleteDivision.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                //deleteRoleApi(role);

            }
        });


        dialog.show();
    }

    private void updateDivisionApi(String division, String updateDivision) {

        mApiService.updateDivisionName(Constant.SCHOOL_ID, division, updateDivision, Constant.EMPLOYEE_BY_ID)
                .enqueue(new Callback<Object>() {
                    @Override
                    public void onResponse(Call<Object> call, Response<Object> response) {
//                dialog.dismiss();

                        if (response.isSuccessful()) {
                            try {
                                JSONObject json1 = (new JSONObject(new Gson().toJson(response.body())));
                                String status = (String) json1.get("status");

                                if (status.equalsIgnoreCase("Success")) {
                                    Toast.makeText(mContext, "Update Successfully", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                    Intent intent = new Intent(mContext, UpdateDivisionActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    mContext.startActivity(intent);


                                }
                            } catch (JSONException je) {

                            }
                        } else {
                            dialog.dismiss();
                            if (String.valueOf(response.code()).equals("409")) {
                                Toast.makeText(mContext, "Role name already exists", Toast.LENGTH_SHORT).show();

                            }
                            if (String.valueOf(response.code()).equals("404")) {
                                Toast.makeText(mContext, "Old role name does not exists", Toast.LENGTH_SHORT).show();

                            }

                        }


                        Log.d("updatedRoles", "" + response.body());

                    }

                    @Override
                    public void onFailure(Call<Object> call, Throwable t) {
                        dialog.dismiss();
                    }
                });
    }

    @Override
    public int getItemCount() {
        switch (recyclerTag) {
            case Constant.RV_DIVISION_CARD:
                return arrayList.size();
            case Constant.RV_SESSION_ROW:
                return modelList.size();

            case Constant.CLASS_ROW:
                return classList.size();

            case Constant.RV_SECTION_ROW:
                return modelArrayList.size();

        }
        return 0;
    }


}
