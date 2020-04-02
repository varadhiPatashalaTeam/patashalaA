package in.varadhismartek.patashalaerp.SessionModule;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import in.varadhismartek.Utils.Constant;
import in.varadhismartek.patashalaerp.DashboardModule.UpdateActivity.UpdateSessionsActivity;
import in.varadhismartek.patashalaerp.DivisionModule.ClassSectionAndDivisionModel;
import in.varadhismartek.patashalaerp.R;
import in.varadhismartek.patashalaerp.Retrofit.APIService;
import in.varadhismartek.patashalaerp.Retrofit.ApiUtils;
import in.varadhismartek.patashalaerp.Retrofit.ApiUtilsPatashala;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SessionAdapter extends RecyclerView.Adapter<SessionViewHolder> {

    private Context mContext;
    private ArrayList<String> sessionList;
    private ArrayList<Integer> integerArrayList;
    private String recyclerTag;
    List<SessionModel> modelList = new ArrayList<>();
    Button bt_submit;

    int rowNumber = 1;

    private int date, month, year;
    private String currentDate;
    APIService apiService;
    String acdStartDate="", acdEndDate="", serialNo="", sessionFromDate="", sessionToDate="",workingDay="";
    Dialog dialog;

    public SessionAdapter(ArrayList<String> sessionList, Context mContext, String recyclerTag, Button bt_submit) {

        this.mContext = mContext;
        this.sessionList = sessionList;
        this.recyclerTag = recyclerTag;
        this.bt_submit = bt_submit;
    }

    public SessionAdapter(Context mContext, ArrayList<Integer> integerArrayList, String recyclerTag) {

        this.mContext = mContext;
        this.integerArrayList = integerArrayList;
        this.recyclerTag = recyclerTag;

        Calendar calendar = Calendar.getInstance();

        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        date = calendar.get(Calendar.DAY_OF_MONTH);

        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat simpleDate = new SimpleDateFormat("dd/MM/yyyy");
        currentDate = simpleDate.format(calendar.getTime());
        apiService = ApiUtilsPatashala.getService();

    }

    public SessionAdapter(List<SessionModel> modelList, Context mContext, String recyclerTag) {
        this.modelList = modelList;
        this.mContext = mContext;
        this.recyclerTag = recyclerTag;
        apiService = ApiUtilsPatashala.getService();

    }



    @NonNull
    @Override
    public SessionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        switch (recyclerTag) {

            case Constant.RV_SESSION_ROW:
                return new SessionViewHolder(LayoutInflater.from(mContext)
                        .inflate(R.layout.nested_session_date_row, parent, false));
            case Constant.RV_SESSION_ROW_NEW:
                return new SessionViewHolder(LayoutInflater.from(mContext)
                        .inflate(R.layout.nested_session_date_row, parent, false));
        }

        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull final SessionViewHolder holder, final int position) {

        switch (recyclerTag) {

            case Constant.RV_SESSION_ROW:

                holder.tv_fromDate.setText(modelList.get(position).getRespStartDate());
                holder.tv_toDate.setText(modelList.get(position).getRespEndDate());


                holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        acdStartDate = modelList.get(position).getAcd_from_date();
                        acdEndDate = modelList.get(position).getAcd_to_date();
                        serialNo = modelList.get(position).getSession_serial_no();
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

                        acdStartDate = modelList.get(position).getAcd_from_date();
                        acdEndDate = modelList.get(position).getAcd_to_date();
                        serialNo = modelList.get(position).getSession_serial_no();
                        workingDay = AddSessionFragment.edWorkDay.getText().toString();
                        sessionFromDate = holder.tv_fromDate.getText().toString();
                        sessionToDate = holder.tv_fromDate.getText().toString();

                        Log.i("workingDayAd**", "" + workingDay);

                        openDialogForUpdate(acdStartDate,acdEndDate,serialNo,workingDay,sessionFromDate,sessionToDate);
                        notifyDataSetChanged();
                        return false;
                    }
                });

                break;

            case Constant.RV_SESSION_ROW_NEW:

                holder.tv_toDate.setText(currentDate);
                holder.tv_fromDate.setText(currentDate);
                break;
        }

    }

    private void openDialogForUpdate(final String acdStartDate, final String acdEndDate, final String serialNo,
                                     final String workingDay, String sessionFromDate, String sessionToDate) {

        dialog = new Dialog(mContext);
        dialog.setContentView(R.layout.session_update_dialog);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.show();
        TextView tvSession =dialog.findViewById(R.id.tvSessionName);
        EditText edWorkingDay =dialog.findViewById(R.id.workday);
       final TextView tvSessionFDate =dialog.findViewById(R.id.tv_fromDate);
       final TextView tvSessionTDate =dialog.findViewById(R.id.tv_toDate);
        RelativeLayout relativeLayoutFromDate =dialog.findViewById(R.id.rl_fromDate);
        RelativeLayout relativeLayoutToDate =dialog.findViewById(R.id.rl_toDate);
        TextView tvAdd =dialog.findViewById(R.id.tv_add);

        Log.i("workingDayAd**", "" + acdStartDate+"**"+acdEndDate);
        tvSession.setText(acdStartDate+"-"+acdEndDate);
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
                Date date1 = null,date2=null;
                try {
                    date1 = sdf.parse(tvSessionFDate.getText().toString());
                     date2 = sdf.parse(tvSessionTDate.getText().toString());

                } catch (ParseException e) {
                    e.printStackTrace();
                }

                if (date1.compareTo(date2)>0){
                    Toast.makeText(mContext,"Please enter valid Date",Toast.LENGTH_SHORT).show();

                }else {

                    Log.i("workingDayAd**", "" + acdStartDate+"**"+acdEndDate+"**"
                            +serialNo+"**"+tvSessionFDate.getText().toString()+"***"
                            +tvSessionTDate.getText().toString());

                    apiService.upDateSession(Constant.SCHOOL_ID,acdStartDate,acdEndDate,serialNo,Constant.EMPLOYEE_BY_ID,
                            tvSessionFDate.getText().toString(),tvSessionTDate.getText().toString())
                            .enqueue(new Callback<Object>() {
                                @Override
                                public void onResponse(Call<Object> call, Response<Object> response) {
                                    Log.i("Update_responseSession",""+response.body());
                                    Log.i("Update_responseSession*",""+response.code());
                                }

                                @Override
                                public void onFailure(Call<Object> call, Throwable t) {

                                }
                            });
                }




            }
        });

    }

    @Override
    public int getItemCount() {
        switch (recyclerTag) {

            case Constant.RV_SESSION_ROW:
                return modelList.size();

            case Constant.RV_SESSION_ROW_NEW:
                return integerArrayList.size();

        }
        return 0;
    }
}






































  /*     holder.rl_fromDate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        DatePickerDialog dialog = new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

                                Calendar calendar = Calendar.getInstance();
                                calendar.set(Calendar.YEAR, i);
                                calendar.set(Calendar.MONTH, i1);
                                calendar.set(Calendar.DAY_OF_MONTH, i2);

                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                                String str_from_date = simpleDateFormat.format(calendar.getTime());
                                holder.tv_fromDate.setText(str_from_date);

                            }
                        }, date, month, year);

                        dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                        dialog.show();
                    }
                });

                holder.rl_toDate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        DatePickerDialog dialog = new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

                                Calendar calendar = Calendar.getInstance();
                                calendar.set(Calendar.YEAR, i);
                                calendar.set(Calendar.MONTH, i1);
                                calendar.set(Calendar.DAY_OF_MONTH, i2);

                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                                String str_to_date = simpleDateFormat.format(calendar.getTime());
                                holder.tv_toDate.setText(str_to_date);

                            }
                        }, date, month, year);

                        dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                        dialog.show();
                    }
                });*/