package in.varadhismartek.patashalaerp.DashboardModule.FeesModule;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;

import in.varadhismartek.Utils.Constant;
import in.varadhismartek.patashalaerp.GeneralClass.CustomSpinnerAdapter;
import in.varadhismartek.patashalaerp.R;
import in.varadhismartek.patashalaerp.Retrofit.APIService;
import in.varadhismartek.patashalaerp.Retrofit.ApiUtils;
import in.varadhismartek.patashalaerp.Retrofit.ApiUtilsPatashala;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Field;

import static android.support.constraint.Constraints.TAG;

public class FeeStructureAdapter extends RecyclerView.Adapter<FeeViewHolder> {
    private Context context;
    ArrayList<FeesModle> feesModles;
    String rvType;
    APIService apiService,mApiService;

    String  str_toDate="", startYear = "", endYear = "", sDate = "", eDate = "",strSelectSession="",SubfromDate="";
    ArrayList<String>  spinnerList= new ArrayList<>();
    ArrayList<String>spinnerDateList= new ArrayList<>();
    Spinner spn_academicyear;
    TextView edFeeDueDate;
    private int year, month, date;

    public FeeStructureAdapter(ArrayList<FeesModle> feesModles, Context context, String rvType) {
        this.feesModles =feesModles;
        this.context =context;
        this.rvType = rvType;

        apiService = ApiUtils.getAPIService();
        mApiService = ApiUtilsPatashala.getService();

    }

    @NonNull
    @Override
    public FeeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        switch (rvType) {

            case Constant.RV_FEES_ROW:
                return new FeeViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_fees_row,
                        parent, false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull FeeViewHolder holder, final int position) {
        switch (rvType) {
            case Constant.RV_FEES_ROW:
                int serialNo = (int) Double.parseDouble(feesModles.get(position).getStrSerialNo());
                if (serialNo==0){
                    holder.tvSerialNo.setText("0");
                }else {
                    holder.tvSerialNo.setText(String.valueOf(serialNo));
                }


                holder.tvFeeType.setText(feesModles.get(position).getStrFeeType());
                holder.tvFeeCode.setText(feesModles.get(position).getSteFeeCode());
                holder.tvInstallment.setText(feesModles.get(position).getStrInstallment());
                holder.tvFeeDueDate.setText(feesModles.get(position).getStrDueDate());
                holder.linearLayoutFees.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String strFeesType,strFeeCode,strInstallment,strDueDate;

                        int strSerialNo=(int) Double.parseDouble(feesModles.get(position).getStrSerialNo());
                        strFeesType= feesModles.get(position).getStrFeeType();
                        strFeeCode=feesModles.get(position).getSteFeeCode();
                        strInstallment=feesModles.get(position).getStrInstallment();
                        strDueDate=feesModles.get(position).getStrDueDate();

                        updateDialogPop(strSerialNo,strFeesType,strFeeCode,strInstallment,strDueDate);
                    }
                });
                break;
        }

    }

    private void updateDialogPop(final int strSerialNo, final String strFeesType, String strFeeCode, String strInstallment, String strDueDate) {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_update_fees_structure);

        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.show();
        final EditText edFeeType = dialog.findViewById(R.id.et_fee_type);
        final EditText edFeeCode = dialog.findViewById(R.id.et_fee_code);
        final EditText edInstallment = dialog.findViewById(R.id.et_installment);
        edFeeDueDate = dialog.findViewById(R.id.tv_due_date);
        final Button btnCancel = dialog.findViewById(R.id.bt_cancel);
        final Button btnUpdate = dialog.findViewById(R.id.bt_update);
        spn_academicyear = dialog.findViewById(R.id.spn_acadmicyear);
        edFeeDueDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateDialog();
            }
        });



        mApiService.getAcademicYear(Constant.SCHOOL_ID).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {

                if (response.isSuccessful()) {
                    Log.d("SESSION**AYEAR", "onResponse: " + response.body());
                    try {
                        JSONObject object = new JSONObject(new Gson().toJson(response.body()));
                        String status = (String) object.get("status");

                        if (status.equalsIgnoreCase("success")) {
                            JSONObject jsonObject1 = object.getJSONObject("data");
                            JSONObject jsonObject2 = jsonObject1.getJSONObject("Academic_years");

                            Iterator<String> keys = jsonObject2.keys();
                            while (keys.hasNext()) {
                                String key = keys.next();
                                JSONObject jsonObjectValue = jsonObject2.getJSONObject(key);
                                String resStartDate = jsonObjectValue.getString("start_date");
                                String resEndDate = jsonObjectValue.getString("end_date");


                                SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
                                SimpleDateFormat formatterDate = new SimpleDateFormat("yyyy-MM-dd");
                                SimpleDateFormat formatterYear = new SimpleDateFormat("yyyy");

                                try {
                                    Date fromYear = formater.parse(resStartDate);
                                    Date toYear = formater.parse(resEndDate);

                                    sDate = formatterDate.format(fromYear);
                                    eDate = formatterDate.format(toYear);

                                    startYear = formatterYear.format(fromYear);
                                    endYear = formatterYear.format(toYear);

                                    String selectedDate = sDate + " - " + eDate;
                                    String selectedYear = startYear + " - " + endYear;

                                    spinnerList.add(selectedYear);
                                    spinnerDateList.add(selectedDate);

                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }


                            }
                            setSpinner();


                        }
                    } catch (JSONException je) {

                    }
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {

            }
        });


        edFeeType.setText(strFeesType);
        edFeeCode.setText(strFeeCode);
        edInstallment.setText(strInstallment);
        edFeeDueDate.setText(strDueDate);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                apiService.updateFeeStructure(Constant.SCHOOL_ID,Constant.ACADEMIC_YEAR,Constant.EMPLOYEE_BY_ID,
                        "true", String.valueOf(strSerialNo),strFeesType,edFeeType.getText().toString(),
                        edInstallment.getText().toString(),edFeeDueDate.getText().toString(),
                        edFeeCode.getText().toString()).enqueue(new Callback<Object>() {
                    @Override
                    public void onResponse(Call<Object> call, Response<Object> response) {
                        if (response.isSuccessful()){
                            Log.i("UPDATE_FEES1",""+response.body()+"****"+response.code());

                            dialog.dismiss();
                            Intent intent = new Intent(context, FeesDetailsActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);
                        }

                    }

                    @Override
                    public void onFailure(Call<Object> call, Throwable t) {

                    }
                });

            }
        });
    }

    private void dateDialog() {
        DatePickerDialog dialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                Calendar calendar;;
                calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, i);
                calendar.set(Calendar.MONTH, i1);
                calendar.set(Calendar.DAY_OF_MONTH, i2);

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy", Locale.getDefault());

                endYear = yearFormat.format(calendar.getTime());
                str_toDate = simpleDateFormat.format(calendar.getTime());
                Log.d("CHECK_DATE", str_toDate);
                Date date1 = new Date();
                try {
                    date1 = simpleDateFormat.parse(str_toDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                edFeeDueDate.setText(str_toDate);
            }
        }, year, month, date);

        dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        dialog.show();
    }

    private void setSpinner() {
        CustomSpinnerAdapter adapter = new CustomSpinnerAdapter(context, spinnerList);
        spn_academicyear.setAdapter(adapter);
        spn_academicyear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String strAcadmicYear = spn_academicyear.getItemAtPosition(position).toString();
                if (!strAcadmicYear.equalsIgnoreCase("Select Academic year")){
                    int pos = parent.getSelectedItemPosition();


                    strSelectSession = spinnerDateList.get(pos);
                    String str_sessionName = spn_academicyear.getSelectedItem().toString();
                    System.out.println("str_sessionName**1**" + spinnerDateList.get(pos)+"****"+str_sessionName);
                    //SubtoDate = strSelectSession.substring(13);

                    SubfromDate = strSelectSession.substring(0, Math.min(strSelectSession.length(), 10));
                    Constant.ACADEMIC_YEAR =SubfromDate;
                    Log.d(TAG, "onResponse:getsession " + SubfromDate + "***" );

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    @Override
    public int getItemCount() {
        switch (rvType) {
            case Constant.RV_FEES_ROW:
                return feesModles.size();
        }

        return 0;
    }
}