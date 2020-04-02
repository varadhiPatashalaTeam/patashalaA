package in.varadhismartek.patashalaerp.DashboardModule.TransportModule;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import in.varadhismartek.Utils.Constant;
import in.varadhismartek.Utils.Utilities;
import in.varadhismartek.patashalaerp.GeneralClass.CustomSpinnerAdapter;
import in.varadhismartek.patashalaerp.R;
import in.varadhismartek.patashalaerp.Retrofit.APIService;
import in.varadhismartek.patashalaerp.Retrofit.ApiUtilsPatashala;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

public class AddVehicleFragment extends Fragment implements View.OnClickListener {
    APIService apiService;
    Utilities utilities;

    private static String TAG = "";
    private static final int FROM_CAMERA = 1111;
    private static final int FROM_GALLERY = 2222;
    private String Tag = "Addvehicle";

    LinearLayout ll_first, ll_Second, ll_Third, ll_Fourth, ll_Fifth, ll_Six, ll_Seven,ll_Eight;
    RelativeLayout rl_first, rl_Second, rl_Third, rl_Fourth, rl_Fifth, rl_Six, rl_Seven,rl_Eight;
    ImageView ll_imageUp, ll_imageDown;

    private ArrayList<String> vehicletype = new ArrayList<>();
    private ArrayList<String> bodytype = new ArrayList<>();
    private Spinner vehicletypespin, bodytypespin;
    String str_vehicle_type = "", str_body_type = "";

    ImageView iv_vehicle1, iv_vehicle2, iv_vehicle3, iv_vehicle4,iv_backBtn;

    File vehicleFile, insuranceFile, vehicleAllFile, loanFile;
    private String str_ed_regNo = "", str_ed_GpsDetails = "", str_ed_ChasisNo = "NA", str_ed_EngineNo = "NA", str_ed_makerName = "NA",
            str_ed_ModelNo = "NA", str_ed_ManuYear = "0", str_ed_SeatCapacity = "0", str_ed_RegAutho = "NA", str_ed_RegState = "NA",
            str_ed_RegisteredDate = "NA", str_ed_PurchaseDate = "NA", str_ed_Ser_TFee = "0", str_ed_Ser_RFee = "0",
            str_ed_Ser_NextKM = "0", str_ed_Ser_NextDay = "0", str_ed_Ser_NextRenewalDate = "NA",
            str_ed_Pre_Owner_Cost = "NA";

    String str_ed_Insurance_Type = "NA", str_ed_Insurance_No = "NA", str_ed_Insurance_Date = "NA", str_vehicle_id = "",
            str_ed_Insurance_ReNewDate = "NA",
            str_ed_Insurance_NextReNewDate = "NA", str_ed_Agent_ID = "NA", str_ed_Agent_Name = "NA",
            str_ed_Agent_CompanyName = "NA", str_ed_Agent_No = "0", str_ed_Agent_Insur_No = "0", str_ed_Other_Insur_RcNo = "NA",
            str_ed_Other_Insur_NOCNo = "NA", str_ed_Poll_Cer_No = "NA";

    String str_ed_Poll_Cer_RegDate = "NA", str_ed_Poll_Cer_RenewDate = "NA",
            str_ed_Fit_Cer_no, str_ed_Fit_Cer_RegDate,

    str_ed_Fit_Cer_RenewDate = "NA", str_ed_Tax_No = "NA", str_ed_Tax_RegDate = "NA",
            str_ed_Tax_Amount = "NA", str_sw_Insurance, str_sw_Fitness, str_sw_Veh_Pur,
            str_sw_OtherExp, str_sw_Poll, str_sw_Service;

    RecyclerView recyclerView, otherdocuments, recyclerfinancial, recyclervehiclefitness;

    TextView btnsave, tv_addRoute;

    EditText ed_regNo, ed_Name, ed_GpsDetails, vehicle_id;

    EditText ed_ChasisNo, ed_EngineNo, ed_makerName, ed_ModelNo, ed_ManuYear, ed_SeatCapacity, ed_RegAutho, ed_RegState, ed_RegisteredDate, ed_PurchaseDate, ed_PreOwnerName, ed_Pre_Owner_PurDate, ed_Pre_Owner_Cost, ed_Pre_Owner_Remarks;


    Switch sw_Service, sw_Insurance, sw_Poll, sw_Fitness, sw_Tax, sw_OtherExp, sw_Veh_Pur;
    EditText ed_Ser_TFee, ed_Ser_RFee, ed_Ser_NextKM, ed_Ser_NextDay, ed_Ser_NextRenewalDate;

    EditText ed_Insurance_Type, ed_Insurance_No, ed_Insurance_Date, ed_Insurance_ReNewDate, ed_Insurance_NextReNewDate;
    EditText ed_Agent_ID, ed_Agent_Name, ed_Agent_CompanyName, ed_Agent_No, ed_Agent_Insur_No;

    EditText ed_Other_Insur_No, ed_Other_Insur_RcNo, ed_Other_Insur_NOCNo,
            ed_Poll_Cer_No, ed_Poll_Cer_RegDate, ed_Poll_Cer_RenewDate, ed_Fit_Cer_no,
            ed_Fit_Cer_RegDate, ed_Fit_Cer_RenewDate, ed_Tax_No, ed_Tax_RegDate, ed_Tax_Amount;


    EditText ed_Veh_Pur_Price, ed_Veh_Pur_BankName, ed_Veh_Pur_LoanAccountNo,
            ed_Veh_Pur_LoanAmount, ed_Veh_Pur_LoanInstRate, ed_Veh_Pur_DownPay, ed_Veh_Pur_RegDate, ed_Veh_Pur_Date, ed_Veh_Pur_LoanEndDate,
            ed_Veh_Pur_LoanEMI;

    EditText ed_Other_BankName, ed_Other_MainAmount, ed_Other_Reparing_No, ed_Other_Remarks;

    Calendar calendar;
    Date date1;
    String endYear, str_toDate, currentDate;
    private int year, month, date;
    public AddVehicleFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_vehicle, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        initViews(view);
        initListeners();
        return view;

    }

    private void initListeners() {
        rl_first.setOnClickListener(this);
        rl_Second.setOnClickListener(this);
        rl_Third.setOnClickListener(this);
        rl_Fourth.setOnClickListener(this);
        rl_Fifth.setOnClickListener(this);
        rl_Six.setOnClickListener(this);
        rl_Seven.setOnClickListener(this);
        rl_Eight.setOnClickListener(this);
        btnsave.setOnClickListener(this);
        tv_addRoute.setOnClickListener(this);
        iv_backBtn.setOnClickListener(this);


        ed_RegisteredDate.setOnClickListener(this);
        ed_PurchaseDate.setOnClickListener(this);
        ed_Pre_Owner_PurDate.setOnClickListener(this);
        ed_Ser_NextRenewalDate.setOnClickListener(this);
        ed_Insurance_Date.setOnClickListener(this);
        ed_Insurance_ReNewDate.setOnClickListener(this);
        ed_Insurance_NextReNewDate.setOnClickListener(this);
        ed_Poll_Cer_RegDate.setOnClickListener(this);
        ed_Poll_Cer_RenewDate.setOnClickListener(this);

        ed_Fit_Cer_RegDate.setOnClickListener(this);
        ed_Fit_Cer_RenewDate.setOnClickListener(this);

        ed_Tax_RegDate.setOnClickListener(this);
        ed_Veh_Pur_RegDate.setOnClickListener(this);
        ed_Veh_Pur_Date.setOnClickListener(this);
        ed_Veh_Pur_LoanEndDate.setOnClickListener(this);


        iv_vehicle1.setOnClickListener(this);
        iv_vehicle2.setOnClickListener(this);
        iv_vehicle3.setOnClickListener(this);
        iv_vehicle4.setOnClickListener(this);


    }

    private void initViews(View v) {
        apiService = ApiUtilsPatashala.getService();

        iv_backBtn = v.findViewById(R.id.iv_backBtn);
        tv_addRoute = v.findViewById(R.id.tv_addRoute);
        btnsave = v.findViewById(R.id.btnsave);
        ll_first = v.findViewById(R.id.ll_first);
        ll_Second = v.findViewById(R.id.ll_Second);
        ll_Third = v.findViewById(R.id.ll_Third);
        ll_Fourth = v.findViewById(R.id.ll_Fourth);
        ll_Fifth = v.findViewById(R.id.ll_fifth);
        ll_Six = v.findViewById(R.id.ll_Six);
        ll_Seven = v.findViewById(R.id.ll_Seven);
        ll_Eight = v.findViewById(R.id.ll_Seven);


        rl_first = v.findViewById(R.id.rl_first);
        rl_Second = v.findViewById(R.id.rl_Second);
        rl_Third = v.findViewById(R.id.rl_Third);
        rl_Fourth = v.findViewById(R.id.rl_Fourth);
        rl_Fifth = v.findViewById(R.id.rl_fifth);
        rl_Six = v.findViewById(R.id.rl_six);
        rl_Seven = v.findViewById(R.id.rl_Seven);
        rl_Eight = v.findViewById(R.id.rl_Seven);

        ll_imageUp = v.findViewById(R.id.img_drop_down_up);
        ll_imageDown = v.findViewById(R.id.img_drop_down);
        vehicletypespin = v.findViewById(R.id.vehicle_type);

        vehicletypespin = v.findViewById(R.id.vehicle_type);
        bodytypespin = v.findViewById(R.id.bodytype_id);
        vehicle_id = v.findViewById(R.id.vehicle_id);


        ed_regNo = v.findViewById(R.id.ed_regNo);
        ed_Name = v.findViewById(R.id.ed_Name);
        ed_GpsDetails = v.findViewById(R.id.ed_GpsDetails);


        ed_ChasisNo = v.findViewById(R.id.ed_ChasisNo);
        ed_EngineNo = v.findViewById(R.id.ed_EngineNo);
        ed_makerName = v.findViewById(R.id.ed_makerName);
        ed_ModelNo = v.findViewById(R.id.ed_ModelNo);
        ed_ManuYear = v.findViewById(R.id.ed_ManuYear);
        ed_SeatCapacity = v.findViewById(R.id.ed_SeatCapacity);
        ed_RegAutho = v.findViewById(R.id.ed_RegAutho);
        ed_RegState = v.findViewById(R.id.ed_RegState);
        ed_RegisteredDate = v.findViewById(R.id.ed_RegisteredDate);
        ed_PurchaseDate = v.findViewById(R.id.ed_PurchaseDate);
        ed_PreOwnerName = v.findViewById(R.id.ed_PreOwnerName);
        ed_Pre_Owner_PurDate = v.findViewById(R.id.ed_Pre_Owner_PurDate);
        ed_Pre_Owner_Cost = v.findViewById(R.id.ed_Pre_Owner_Cost);
        ed_Pre_Owner_Remarks = v.findViewById(R.id.ed_Pre_Owner_Remarks);


        ed_Ser_TFee = v.findViewById(R.id.ed_Ser_TFee);
        ed_Ser_RFee = v.findViewById(R.id.ed_Ser_RFee);
        ed_Ser_NextKM = v.findViewById(R.id.ed_Ser_NextKM);
        ed_Ser_NextDay = v.findViewById(R.id.ed_Ser_NextDay);
        ed_Ser_NextRenewalDate = v.findViewById(R.id.ed_Ser_NextRenewalDate);
        sw_Service = v.findViewById(R.id.sw_Service);

        ed_Insurance_Type = v.findViewById(R.id.ed_Insurance_Type);
        ed_Insurance_No = v.findViewById(R.id.ed_Insurance_No);
        ed_Insurance_Date = v.findViewById(R.id.ed_Insurance_Date);
        ed_Insurance_ReNewDate = v.findViewById(R.id.ed_Insurance_ReNewDate);
        ed_Insurance_NextReNewDate = v.findViewById(R.id.ed_Insurance_NextReNewDate);
        sw_Insurance = v.findViewById(R.id.sw_Insurance);

        ed_Agent_ID = v.findViewById(R.id.ed_Agent_ID);
        ed_Agent_Name = v.findViewById(R.id.ed_Agent_Name);
        ed_Agent_CompanyName = v.findViewById(R.id.ed_Agent_CompanyName);
        ed_Agent_No = v.findViewById(R.id.ed_Agent_No);
        ed_Agent_Insur_No = v.findViewById(R.id.ed_Agent_Insur_No);


        ed_Other_Insur_No = v.findViewById(R.id.ed_Other_Insur_No);
        ed_Other_Insur_RcNo = v.findViewById(R.id.ed_Other_Insur_RcNo);
        ed_Other_Insur_NOCNo = v.findViewById(R.id.ed_Other_Insur_NOCNo);
        ed_Poll_Cer_No = v.findViewById(R.id.ed_Poll_Cer_No);
        ed_Poll_Cer_RegDate = v.findViewById(R.id.ed_Poll_Cer_RegDate);
        ed_Poll_Cer_RenewDate = v.findViewById(R.id.ed_Poll_Cer_RenewDate);
        sw_Poll = v.findViewById(R.id.sw_Poll);
        ed_Fit_Cer_no = v.findViewById(R.id.ed_Fit_Cer_no);
        ed_Fit_Cer_RegDate = v.findViewById(R.id.ed_Fit_Cer_RegDate);
        ed_Fit_Cer_RenewDate = v.findViewById(R.id.ed_Fit_Cer_RenewDate);
        ed_Tax_RegDate = v.findViewById(R.id.ed_Tax_RegDate);
        ed_Tax_No = v.findViewById(R.id.ed_Tax_No);
        ed_Tax_Amount = v.findViewById(R.id.ed_Tax_Amount);
        sw_Tax = v.findViewById(R.id.sw_Tax);
        sw_Veh_Pur = v.findViewById(R.id.sw_Veh_Pur);
        sw_Fitness = v.findViewById(R.id.sw_Fitness);


        ed_Veh_Pur_Price = v.findViewById(R.id.ed_Veh_Pur_Price);
        ed_Veh_Pur_BankName = v.findViewById(R.id.ed_Veh_Pur_BankName);
        ed_Veh_Pur_LoanAccountNo = v.findViewById(R.id.ed_Veh_Pur_LoanAccountNo);
        ed_Veh_Pur_LoanAmount = v.findViewById(R.id.ed_Veh_Pur_LoanAmount);
        ed_Veh_Pur_LoanInstRate = v.findViewById(R.id.ed_Veh_Pur_LoanInstRate);
        ed_Veh_Pur_DownPay = v.findViewById(R.id.ed_Veh_Pur_DownPay);
        ed_Veh_Pur_RegDate = v.findViewById(R.id.ed_Veh_Pur_RegDate);
        ed_Veh_Pur_Date = v.findViewById(R.id.ed_Veh_Pur_Date);
        ed_Veh_Pur_LoanEndDate = v.findViewById(R.id.ed_Veh_Pur_LoanEndDate);
        ed_Veh_Pur_LoanEMI = v.findViewById(R.id.ed_Veh_Pur_LoanEMI);


        sw_OtherExp = v.findViewById(R.id.sw_OtherExp);
        ed_Other_BankName = v.findViewById(R.id.ed_Other_BankName);
        ed_Other_MainAmount = v.findViewById(R.id.ed_Other_MainAmount);
        ed_Other_Reparing_No = v.findViewById(R.id.ed_Other_Reparing_No);
        ed_Other_Remarks = v.findViewById(R.id.ed_Other_Remarks);


        iv_vehicle1 = v.findViewById(R.id.iv_vehicle1);
        iv_vehicle2 = v.findViewById(R.id.iv_vehicle2);
        iv_vehicle3 = v.findViewById(R.id.iv_vehicle3);
        iv_vehicle4 = v.findViewById(R.id.iv_vehicle4);


        vehicletype.clear();
        bodytype.clear();

        vehicletype.add("Select");
        vehicletype.add("BUS");
        vehicletype.add("MINI BUS");


        bodytype.add("Select");
        bodytype.add("NEW");
        bodytype.add("SECOND TYPE");

        Calendar calendar = Calendar.getInstance();

        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        date = calendar.get(Calendar.DAY_OF_MONTH);


        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd");
        currentDate = simpleDate.format(calendar.getTime());


        ed_RegisteredDate.setText(currentDate);
        ed_PurchaseDate.setText(currentDate);
        ed_Pre_Owner_PurDate.setText(currentDate);
        ed_Ser_NextRenewalDate.setText(currentDate);
        ed_Insurance_ReNewDate.setText(currentDate);
        ed_Insurance_Date.setText(currentDate);
        ed_Insurance_NextReNewDate.setText(currentDate);
        ed_Poll_Cer_RegDate.setText(currentDate);
        ed_Poll_Cer_RenewDate.setText(currentDate);
        ed_Fit_Cer_RegDate.setText(currentDate);
        ed_Fit_Cer_RenewDate.setText(currentDate);
        ed_Tax_RegDate.setText(currentDate);
        ed_Veh_Pur_RegDate.setText(currentDate);
        ed_Veh_Pur_Date.setText(currentDate);
        ed_Veh_Pur_LoanEndDate.setText(currentDate);


        CustomSpinnerAdapter customSpinnerAdaptervehicle = new CustomSpinnerAdapter(getActivity(), vehicletype, "#717071");
        vehicletypespin.setAdapter(customSpinnerAdaptervehicle);
        vehicletypespin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                str_vehicle_type = parent.getItemAtPosition(position).toString();
                Log.d(Tag, str_vehicle_type);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        CustomSpinnerAdapter customSpinnerAdapterbody = new CustomSpinnerAdapter(getActivity(), bodytype, "#717071");
        bodytypespin.setAdapter(customSpinnerAdapterbody);
        bodytypespin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                str_body_type = parent.getItemAtPosition(position).toString();

                Log.d(Tag, str_body_type);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_backBtn:
                getActivity().onBackPressed();
                break;

                case R.id.tv_addRoute:
               TransportActivity transportActivity =(TransportActivity)getActivity();
               //transportActivity.loadFragment(Constant.ADD_ROUTE_FRAGMENT,null);
                break;

            case R.id.iv_vehicle1:
                TAG = "VEHICLE_IMAGE";

                setImagesInDialog();

                break;
            case R.id.iv_vehicle2:
                TAG = "INSURANCE_IMAGE";
                setImagesInDialog();
                break;
            case R.id.iv_vehicle3:
                TAG = "VEHICLE_ALL_IMAGE";
                setImagesInDialog();
                break;
            case R.id.iv_vehicle4:
                TAG = "LOAN_IMAGE";
                setImagesInDialog();
                break;

            case R.id.ed_RegisteredDate:
                openCalenderDate("TAG1");

                break;
            case R.id.ed_PurchaseDate:
                openCalenderDate("TAG2");

                break;
            case R.id.ed_Pre_Owner_PurDate:
                openCalenderDate("TAG3");
                break;
            case R.id.ed_Ser_NextRenewalDate:
                openCalenderDate("TAG4");
                break;
            case R.id.ed_Insurance_ReNewDate:
                openCalenderDate("TAG5");
                break;

            case R.id.ed_Insurance_Date:
                openCalenderDate("TAG15");
                break;
            case R.id.ed_Insurance_NextReNewDate:
                openCalenderDate("TAG6");
                break;
            case R.id.ed_Poll_Cer_RegDate:
                openCalenderDate("TAG7");
                break;
            case R.id.ed_Poll_Cer_RenewDate:
                openCalenderDate("TAG8");
                break;
            case R.id.ed_Fit_Cer_RegDate:
                openCalenderDate("TAG9");
                break;
            case R.id.ed_Fit_Cer_RenewDate:
                openCalenderDate("TAG10");
                break;
            case R.id.ed_Tax_RegDate:
                openCalenderDate("TAG11");
                break;
            case R.id.ed_Veh_Pur_RegDate:
                openCalenderDate("TAG12");
                break;
            case R.id.ed_Veh_Pur_Date:
                openCalenderDate("TAG13");
                break;
            case R.id.ed_Veh_Pur_LoanEndDate:
                openCalenderDate("TAG14");
                break;


            case R.id.rl_first:
                if (ll_first.getVisibility() == View.VISIBLE) {
                    ll_first.setVisibility(View.GONE);

                } else {
                    ll_first.setVisibility(View.VISIBLE);

                }
                break;
            case R.id.rl_Second:
                if (ll_Second.getVisibility() == View.VISIBLE) {
                    ll_Second.setVisibility(View.GONE);
                } else {
                    ll_Second.setVisibility(View.VISIBLE);
                }

                break;
            case R.id.rl_Third:
                if (ll_Third.getVisibility() == View.VISIBLE) {
                    ll_Third.setVisibility(View.GONE);
                } else {
                    ll_Third.setVisibility(View.VISIBLE);
                }

                break;

            case R.id.rl_Fourth:
                if (ll_Fourth.getVisibility() == View.VISIBLE) {
                    ll_Fourth.setVisibility(View.GONE);
                } else {
                    ll_Fourth.setVisibility(View.VISIBLE);
                }

                break;

            case R.id.rl_fifth:
                if (ll_Fifth.getVisibility() == View.VISIBLE) {
                    ll_Fifth.setVisibility(View.GONE);
                } else {
                    ll_Fifth.setVisibility(View.VISIBLE);
                }

                break;

            case R.id.rl_six:
                if (ll_Six.getVisibility() == View.VISIBLE) {
                    ll_Six.setVisibility(View.GONE);
                } else {
                    ll_Six.setVisibility(View.VISIBLE);
                }

                break;

            case R.id.rl_Seven:
                if (ll_Seven.getVisibility() == View.VISIBLE) {
                    ll_Seven.setVisibility(View.GONE);
                } else {
                    ll_Seven.setVisibility(View.VISIBLE);
                }

                break;

                case R.id.rl_Eight:
                if (ll_Eight.getVisibility() == View.VISIBLE) {
                    ll_Eight.setVisibility(View.GONE);
                } else {
                    ll_Eight.setVisibility(View.VISIBLE);
                }

                break;

            case R.id.btnsave:


                if (!ed_SeatCapacity.getText().toString().trim().isEmpty()) {
                    str_ed_SeatCapacity = ed_SeatCapacity.getText().toString().trim();
                } else {
                    str_ed_SeatCapacity = "0";
                }


                if (!ed_ManuYear.getText().toString().trim().isEmpty()) {
                    str_ed_ManuYear = ed_ManuYear.getText().toString().trim();
                } else {
                    str_ed_ManuYear = "0";
                }


                if (!ed_Ser_TFee.getText().toString().trim().isEmpty()) {
                    str_ed_Ser_TFee = ed_Ser_TFee.getText().toString().trim();
                } else {
                    str_ed_Ser_TFee = "0";
                }

                if (!ed_Ser_RFee.getText().toString().trim().isEmpty()) {
                    str_ed_Ser_RFee = ed_Ser_RFee.getText().toString().trim();
                } else {
                    str_ed_Ser_RFee = "0";
                }

                if (!ed_Ser_NextKM.getText().toString().trim().isEmpty()) {
                    str_ed_Ser_NextKM = ed_Ser_NextKM.getText().toString().trim();
                } else {
                    str_ed_Ser_NextKM = "0";
                }

                if (!ed_Ser_NextDay.getText().toString().trim().isEmpty()) {
                    str_ed_Ser_NextDay = ed_Ser_NextDay.getText().toString().trim();
                } else {
                    str_ed_Ser_NextDay = "0";
                }

                if (!ed_Agent_No.getText().toString().trim().isEmpty()) {
                    str_ed_Agent_No = ed_Agent_No.getText().toString().trim();
                } else {
                    str_ed_Agent_No = "0";
                }

                if (!ed_Agent_Insur_No.getText().toString().trim().isEmpty()) {
                    str_ed_Agent_Insur_No = ed_Agent_Insur_No.getText().toString().trim();
                } else {
                    str_ed_Agent_Insur_No = "0";
                }


                str_vehicle_id = vehicle_id.getText().toString().trim();
                str_ed_Insurance_Date = ed_Insurance_Date.getText().toString().trim();
                str_ed_Insurance_ReNewDate = ed_Insurance_ReNewDate.getText().toString().trim();
                str_ed_Insurance_NextReNewDate = ed_Insurance_NextReNewDate.getText().toString().trim();


                str_ed_Agent_ID = ed_Agent_ID.getText().toString().trim();
                str_ed_Agent_Name = ed_Agent_Name.getText().toString().trim();
                str_ed_Agent_CompanyName = ed_Agent_CompanyName.getText().toString().trim();


                str_ed_Other_Insur_RcNo = ed_Other_Insur_RcNo.getText().toString().trim();
                str_ed_Other_Insur_NOCNo = ed_Other_Insur_NOCNo.getText().toString().trim();
                str_ed_Poll_Cer_No = ed_Poll_Cer_No.getText().toString().trim();
                str_ed_Poll_Cer_RegDate = ed_Poll_Cer_RegDate.getText().toString().trim();
                str_ed_Poll_Cer_RenewDate = ed_Poll_Cer_RenewDate.getText().toString().trim();

                str_ed_Fit_Cer_no = ed_Fit_Cer_no.getText().toString().trim();
                str_ed_Fit_Cer_RegDate = ed_Fit_Cer_RegDate.getText().toString().trim();
                str_ed_Fit_Cer_RenewDate = ed_Fit_Cer_RenewDate.getText().toString().trim();


                str_ed_Tax_No = ed_Tax_No.getText().toString().trim();
                str_ed_Tax_RegDate = ed_Tax_RegDate.getText().toString().trim();
                str_ed_Tax_Amount = ed_Tax_Amount.getText().toString().trim();

                str_ed_regNo = ed_regNo.getText().toString().trim();
                str_ed_GpsDetails = ed_GpsDetails.getText().toString().trim();
                str_ed_ChasisNo = ed_ChasisNo.getText().toString().trim();
                str_ed_EngineNo = ed_EngineNo.getText().toString().trim();
                str_ed_makerName = ed_makerName.getText().toString().trim();
                str_ed_ModelNo = ed_ModelNo.getText().toString().trim();

                str_ed_RegAutho = ed_RegAutho.getText().toString().trim();
                str_ed_RegState = ed_RegState.getText().toString().trim();
                str_ed_RegisteredDate = ed_RegisteredDate.getText().toString().trim();
                str_ed_PurchaseDate = ed_PurchaseDate.getText().toString().trim();
                str_ed_Pre_Owner_Cost = ed_Pre_Owner_Cost.getText().toString().trim();


                str_ed_Ser_NextRenewalDate = ed_Ser_NextRenewalDate.getText().toString().trim();

                str_sw_Service = "true";
                str_sw_Insurance = "true";
                str_sw_Poll = "true";
                str_sw_Fitness = "true";
                str_sw_Veh_Pur = "true";
                str_sw_OtherExp = "true";

                if (str_vehicle_id.isEmpty() || str_vehicle_id.equals(null)) {
                    Toast.makeText(getActivity(), "select Vehicle id ", Toast.LENGTH_SHORT).show();

                } else if (str_vehicle_type.equals(null) || str_vehicle_type.equals("") || str_vehicle_type.equalsIgnoreCase("Select") ||
                        str_body_type.equals(null) || str_body_type.equals("") || str_body_type.equalsIgnoreCase("Select")) {
                    Toast.makeText(getActivity(), "select Vehicle type & Type of body", Toast.LENGTH_SHORT).show();
                } else if (vehicleFile == null) {
                    Toast.makeText(getActivity(), "Attached Vehicle Image", Toast.LENGTH_SHORT).show();

                } else if (insuranceFile == null) {
                    Toast.makeText(getActivity(), "Attached Insurance Image", Toast.LENGTH_SHORT).show();
                } else if (vehicleAllFile == null) {
                    Toast.makeText(getActivity(), "Attached other vehicle Image", Toast.LENGTH_SHORT).show();
                } else if (loanFile == null) {
                    Toast.makeText(getActivity(), "Attached Loan Image", Toast.LENGTH_SHORT).show();
                } else {
                    addVehicleAPI();
                }
                break;

        }
    }

    private void setImagesInDialog() {
        final Dialog dialog = new Dialog(getActivity());

        dialog.setContentView(R.layout.dialog_camera_gallery);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.flag_transparent);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();

        LinearLayout camera = dialog.findViewById(R.id.ll_camera);
        LinearLayout gallery = dialog.findViewById(R.id.ll_gallery);
        TextView tv_date = dialog.findViewById(R.id.tv_date);
        TextView tv_add = dialog.findViewById(R.id.tv_add);
        EditText et_gallery_title = dialog.findViewById(R.id.et_gallery_title);

        tv_date.setVisibility(View.GONE);
        tv_add.setVisibility(View.GONE);
        et_gallery_title.setVisibility(View.GONE);

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, FROM_CAMERA);
                dialog.dismiss();
            }
        });

        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent galleryIntent = new Intent();
                galleryIntent.setType("image/*");
                galleryIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(galleryIntent, "Select Image"), FROM_GALLERY);
                dialog.dismiss();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == FROM_GALLERY && resultCode == RESULT_OK) {

            if (data.getData() != null) {

                Uri mImageUri = data.getData();
                setImages(mImageUri);
            }

        } else if (requestCode == FROM_CAMERA && resultCode == RESULT_OK) {

            Bitmap photo = (Bitmap) data.getExtras().get("data");
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            photo.compress(Bitmap.CompressFormat.PNG, 100, bytes);
            Bitmap bmp = Bitmap.createScaledBitmap(photo, 50, 60, true);
            String path = MediaStore.Images.Media.insertImage(getActivity().getContentResolver(), bmp, "Title", null);
            Uri filePathUri = Uri.parse(path);

            setImages(filePathUri);


        }
    }

    private void setImages(Uri mImageUri) {
        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), mImageUri);

        } catch (IOException e) {
            e.printStackTrace();
        }
        assert bitmap != null;
        String path = getRealPathFromURI(getImageUri(getActivity(), bitmap));

        switch (TAG) {

            case "VEHICLE_IMAGE":

                iv_vehicle1.setImageURI(mImageUri);
                vehicleFile = new File(path);

                break;

            case "INSURANCE_IMAGE":
                iv_vehicle2.setImageURI(mImageUri);
                insuranceFile = new File(path);

                break;

            case "VEHICLE_ALL_IMAGE":
                iv_vehicle3.setImageURI(mImageUri);
                vehicleAllFile = new File(path);

                break;

            case "LOAN_IMAGE":
                iv_vehicle4.setImageURI(mImageUri);
                loanFile = new File(path);

                break;
        }
    }


    public void addVehicleDocment() {



        JSONObject jsonObject = new JSONObject();
        try {
            String cameraIP = "127.0.0.1:8000";

            JSONArray jsonArray = new JSONArray();
            jsonArray.put(cameraIP);
            jsonObject.put("cameras", jsonArray);

        } catch (JSONException je) {

        }

        Log.i("jsonObject**", "" + jsonObject);
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);


        builder.addFormDataPart("school_id", Constant.SCHOOL_ID);
        builder.addFormDataPart("vehicle_uuid", Constant.VEHICLE_ID);

        builder.addFormDataPart("vehicle_image_0", vehicleFile.getName(), RequestBody.
                create(MediaType.parse("multipart/form-data"), vehicleFile));

        builder.addFormDataPart("vehicle_insurance_document_0", insuranceFile.getName(), RequestBody.
                create(MediaType.parse("multipart/form-data"), insuranceFile));

        builder.addFormDataPart("vehicle_all_documents_0", vehicleAllFile.getName(), RequestBody.
                create(MediaType.parse("multipart/form-data"), vehicleAllFile));

        builder.addFormDataPart("vehicle_loan_documents_0", loanFile.getName(), RequestBody.
                create(MediaType.parse("multipart/form-data"), loanFile));

        builder.addFormDataPart("camera_ip", jsonObject.toString());

        MultipartBody requestBody = builder.build();

        apiService.addVehicleDocument(requestBody).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if (response.isSuccessful()) {
                    try {
                        utilities.cancelProgressDialog();
                        JSONObject object = new JSONObject(new Gson().toJson(response.body()));
                        String status = (String) object.get("status");

                        if (status.equalsIgnoreCase("success")) {
                            Toast.makeText(getActivity(), "Vehicle Document attached Successfully", Toast.LENGTH_SHORT).show();

                               /* JSONObject jsonObject = object.getJSONObject("data");

                                if (!jsonObject.isNull("vehicle_uuid")) {
                                    Toast.makeText(getActivity(), "Vehicle Added Successfully", Toast.LENGTH_SHORT).show();
                                    Constant.VEHICLE_ID = jsonObject.getString("vehicle_uuid");

                                    addVehicleDocment();
                                }*/

                        }

                    } catch (JSONException je) {
                        je.printStackTrace();
                    }
                } else {
                    try {
                        assert response.errorBody() != null;
                        utilities.cancelProgressDialog();
                        JSONObject object = new JSONObject(response.errorBody().string());
                        String message = object.getString("message");
                        Log.d("Trans_addd*44555", message);
                        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {

            }
        });


    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.PNG, 20, bytes);
        Bitmap bmp = Bitmap.createScaledBitmap(inImage, 50, 60, true);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), bmp, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }


    private String openCalenderDate(final String tag1) {


        DatePickerDialog dialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

                calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, i);
                calendar.set(Calendar.MONTH, i1);
                calendar.set(Calendar.DAY_OF_MONTH, i2);

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy", Locale.getDefault());


                endYear = yearFormat.format(calendar.getTime());
                str_toDate = simpleDateFormat.format(calendar.getTime());
                Log.d("CHECK_DATE", str_toDate);
                date1 = new Date();
                try {
                    date1 = simpleDateFormat.parse(str_toDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }


                switch (tag1) {
                    case "TAG1":
                        ed_RegisteredDate.setText(str_toDate);
                        break;

                    case "TAG2":
                        ed_PurchaseDate.setText(str_toDate);
                        break;
                    case "TAG3":
                        ed_Pre_Owner_PurDate.setText(str_toDate);
                        break;
                    case "TAG4":
                        ed_Ser_NextRenewalDate.setText(str_toDate);
                        break;
                    case "TAG5":
                        ed_Insurance_ReNewDate.setText(str_toDate);
                        break;
                    case "TAG15":
                        ed_Insurance_Date.setText(str_toDate);
                        break;
                    case "TAG6":
                        ed_Insurance_NextReNewDate.setText(str_toDate);
                        break;
                    case "TAG7":
                        ed_Poll_Cer_RegDate.setText(str_toDate);
                        break;
                    case "TAG8":
                        ed_Poll_Cer_RenewDate.setText(str_toDate);

                        break;
                    case "TAG9":
                        ed_Fit_Cer_RegDate.setText(str_toDate);
                        break;
                    case "TAG10":
                        ed_Fit_Cer_RenewDate.setText(str_toDate);
                        break;
                    case "TAG11":
                        ed_Tax_RegDate.setText(str_toDate);
                        break;
                    case "TAG12":
                        ed_Veh_Pur_RegDate.setText(str_toDate);
                        break;
                    case "TAG13":
                        ed_Veh_Pur_Date.setText(str_toDate);
                        break;
                    case "TAG14":
                        ed_Veh_Pur_LoanEndDate.setText(str_toDate);
                        break;

                }
                //    ed_RegisteredDate.setText(str_toDate);


            }
        }, year, month, date);

        dialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        dialog.show();
        return str_toDate;
    }

    private void addVehicleAPI() {


        utilities.displayProgressDialog(getActivity(), "Loading...", false);
        Log.i("str_ed_Ser_TFee***", "" + str_ed_Ser_TFee + str_ed_Ser_NextKM);

        apiService.addVehicle(Constant.SCHOOL_ID, str_vehicle_type, str_vehicle_id, str_ed_regNo, str_body_type,
                str_sw_Service, str_sw_Insurance, str_sw_Poll, str_sw_Fitness, str_sw_Veh_Pur, str_sw_OtherExp,
                Constant.EMPLOYEE_BY_ID,

                str_ed_GpsDetails, str_ed_ChasisNo, str_ed_EngineNo, str_ed_makerName, str_ed_ModelNo, str_ed_ManuYear, str_ed_SeatCapacity, str_ed_RegAutho,
                str_ed_RegState, str_ed_RegisteredDate, str_ed_PurchaseDate, str_ed_Pre_Owner_Cost

                , str_ed_Ser_TFee, str_ed_Ser_RFee, str_ed_Ser_NextKM, str_ed_Ser_NextDay, str_ed_Ser_NextRenewalDate,

                str_ed_Insurance_Type, str_ed_Insurance_No, str_ed_Insurance_Date, str_ed_Insurance_ReNewDate, str_ed_Insurance_NextReNewDate

                , str_ed_Agent_ID, str_ed_Agent_Name, str_ed_Agent_CompanyName, str_ed_Agent_No, str_ed_Agent_Insur_No
                , str_ed_Other_Insur_RcNo, str_ed_Other_Insur_NOCNo, str_ed_Poll_Cer_No, str_ed_Poll_Cer_RegDate, str_ed_Poll_Cer_RenewDate

                , str_ed_Fit_Cer_no, str_ed_Fit_Cer_RegDate, str_ed_Fit_Cer_RenewDate
                , str_ed_Tax_No, str_ed_Tax_RegDate, str_ed_Tax_Amount
        )

                .enqueue(new Callback<Object>() {
                    @Override
                    public void onResponse(Call<Object> call, Response<Object> response) {

                        if (response.isSuccessful()) {
                            try {
                                utilities.cancelProgressDialog();
                                JSONObject object = new JSONObject(new Gson().toJson(response.body()));
                                String status = (String) object.get("status");

                                if (status.equalsIgnoreCase("success")) {
                                    JSONObject jsonObject = object.getJSONObject("data");

                                    if (!jsonObject.isNull("vehicle_uuid")) {
                                        Toast.makeText(getActivity(), "Vehicle Added Successfully", Toast.LENGTH_SHORT).show();
                                        Constant.VEHICLE_ID = jsonObject.getString("vehicle_uuid");
                                        System.out.println("Constant.VEHICLE_ID" + Constant.VEHICLE_ID);
                                        addVehicleDocment();
                                    }

                                }


                            } catch (JSONException je) {
                                je.printStackTrace();
                            }
                        }
                        try {
                            assert response.errorBody() != null;
                            utilities.cancelProgressDialog();
                            JSONObject object = new JSONObject(response.errorBody().string());
                            String message = object.getString("message");
                            Log.d("Trans_addd*44", message);
                            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onFailure(Call<Object> call, Throwable t) {
                        System.out.println("ReErrors*" + t.toString());
                        utilities.cancelProgressDialog();

                    }
                });

    }
}
