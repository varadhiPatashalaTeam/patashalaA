package in.varadhismartek.patashalaerp.Visitors;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;


import org.json.JSONObject;

import java.util.Iterator;

import in.varadhismartek.Utils.Constant;
import in.varadhismartek.patashalaerp.GeneralClass.CircleImageView;
import in.varadhismartek.patashalaerp.GeneralClass.DateConvertor;
import in.varadhismartek.patashalaerp.R;
import in.varadhismartek.patashalaerp.Retrofit.APIService;
import in.varadhismartek.patashalaerp.Retrofit.ApiUtilsPatashala;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class ViewVisitorsFragment extends Fragment implements View.OnClickListener {


    private CircleImageView civ_visitorPic;
    private ImageView iv_visitIdCard, iv_signature,ivBack;
    private TextView tv_addEmpName, tv_staffName, tv_gender, tv_visitorId, tv_purpose, tv_staffAdhar, tv_staffPhone,
            tv_whomToMeet, tv_empAdhar,tv_department, tv_contact, tv_entryTime, tv_address, tv_addEmpId, tv_entryDate,
            tv_addedTime, tv_visitorName, tv_empPhone;

    private TextView tv_genPass;

    private APIService mAPIServicePatashala;
    private ProgressDialog progressDialog;
    private DateConvertor convertor;

    private String visitorId = "";

    private String whom_to_meet = "",added_by_employee_firstname = "", staff_first_name = "",gender = "",
            visitor_id = "",visitor_photo = "",contact_number = "",entry_time = "",
            address = "",entry_date = "",visitor_name = "";

    public ViewVisitorsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_view_visitors, container, false);

        initViews(view);
        initListeners();
        getBundles();

        getSchoolVisitorDetailsAPI();

        return view;
    }

    private void getBundles() {

        Bundle bundle = getArguments();

        visitorId = bundle.getString("visitorId");
    }

    private void initListeners() {

        tv_genPass.setOnClickListener(this);
        ivBack.setOnClickListener(this);
    }

    private void initViews(View view) {

        mAPIServicePatashala = ApiUtilsPatashala.getService();
        progressDialog = new ProgressDialog(getActivity());
        convertor = new DateConvertor();

        ivBack = view.findViewById(R.id.ivBack);
        civ_visitorPic = view.findViewById(R.id.civ_visitorPic);
        iv_visitIdCard = view.findViewById(R.id.iv_visitIdCard);
        iv_signature = view.findViewById(R.id.iv_signature);
        tv_addEmpName = view.findViewById(R.id.tv_addEmpName);
        tv_staffName = view.findViewById(R.id.tv_staffName);
        tv_gender = view.findViewById(R.id.tv_gender);
        tv_visitorId = view.findViewById(R.id.tv_visitorId);
        tv_purpose = view.findViewById(R.id.tv_purpose);
        tv_staffAdhar = view.findViewById(R.id.tv_staffAdhar);
        tv_staffPhone = view.findViewById(R.id.tv_staffPhone);
        tv_whomToMeet = view.findViewById(R.id.tv_whomToMeet);
        tv_empAdhar = view.findViewById(R.id.tv_empAdhar);
        tv_department = view.findViewById(R.id.tv_department);
        tv_contact = view.findViewById(R.id.tv_contact);
        tv_entryTime = view.findViewById(R.id.tv_entryTime);
        tv_address = view.findViewById(R.id.tv_address);
        tv_addEmpId = view.findViewById(R.id.tv_addEmpId);
        tv_entryDate = view.findViewById(R.id.tv_entryDate);
        tv_addedTime = view.findViewById(R.id.tv_addedTime);
        tv_visitorName = view.findViewById(R.id.tv_visitorName);
        tv_empPhone = view.findViewById(R.id.tv_empPhone);
        tv_genPass = view.findViewById(R.id.tv_genPass);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.ivBack:
                getActivity().onBackPressed();
                break;

            case R.id.tv_genPass:

                VisitiorsActivity visitiorsActivity = (VisitiorsActivity) getActivity();

                Bundle bundle = new Bundle();

                bundle.putString("added_by_employee_firstname",added_by_employee_firstname);
                bundle.putString("staff_first_name",staff_first_name);
                bundle.putString("gender",gender);
                bundle.putString("visitor_id",visitor_id);
                bundle.putString("visitor_photo",visitor_photo);
                bundle.putString("contact_number",contact_number);
                bundle.putString("entry_time",entry_time);
                bundle.putString("address",address);
                bundle.putString("entry_date",entry_date);
                bundle.putString("visitor_name",visitor_name);

                visitiorsActivity.loadFragment(Constant.FRAGMENT_VISITIORS_GEN_PASS, bundle);

                break;
        }
    }

    private void getSchoolVisitorDetailsAPI(){

        progressDialog.show();

        Log.d("visitorDetails_input", Constant.SCHOOL_ID+" "+ visitorId);

        mAPIServicePatashala.getSchoolVisitorDetails(Constant.SCHOOL_ID, visitorId).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {

                if (response.isSuccessful()){

                    try {

                        JSONObject object = new JSONObject(new Gson().toJson(response.body()));
                        String message = object.getString("message");
                        String status1 = object.getString("status");

                        if (status1.equalsIgnoreCase("Success")){
                            Log.d("visitorDetails_success", response.code() + " " + message+" "+response.body());

                            JSONObject jsonData = object.getJSONObject("data");

                            Iterator<String> key = jsonData.keys();

                            while (key.hasNext()){

                                JSONObject keyData = jsonData.getJSONObject(key.next());

                                added_by_employee_firstname = keyData.getString("added_by_employee_firstname");
                                staff_first_name = keyData.getString("staff_first_name");
                                gender = keyData.getString("gender");
                                visitor_id = keyData.getString("visitor_id");
                                String purpose = keyData.getString("purpose");
                                String staff_adhar_card_no = keyData.getString("staff_adhar_card_no");
                                String staff_phone_no = keyData.getString("staff_phone_no");
                                String staff_last_name = keyData.getString("staff_last_name");
                                String employee_adhaar_card_no = keyData.getString("employee_adhaar_card_no");
                                String department = keyData.getString("department");
                                visitor_photo = keyData.getString("visitor_photo");
                                String signature = keyData.getString("signature");
                                contact_number = keyData.getString("contact_number");
                                entry_time = keyData.getString("entry_time");
                                address = keyData.getString("address");
                                String added_employeeid = keyData.getString("added_employeeid");
                                entry_date = keyData.getString("entry_date");
                                String school_id = keyData.getString("school_id");
                                String visitor_idcard_photo = keyData.getString("visitor_idcard_photo");
                                String added_by_employee_lastname = keyData.getString("added_by_employee_lastname");
                                String visitor_uuid = keyData.getString("visitor_uuid");
                                String staff_id = keyData.getString("staff_id");
                                String added_datetime = keyData.getString("added_datetime");
                                visitor_name = keyData.getString("visitor_name");
                                String employee_phone_number = keyData.getString("employee_phone_number");

                                if (!keyData.isNull("whom_to_meet")){
                                    whom_to_meet = keyData.getString("whom_to_meet");
                                }

                                assert getActivity() !=null;

                                if (!visitor_photo.equalsIgnoreCase("")){
                                    Glide.with(getActivity()).load(Constant.IMAGE_URL+visitor_photo).into(civ_visitorPic);

                                }if (!visitor_idcard_photo.equalsIgnoreCase("")){
                                    Glide.with(getActivity()).load(Constant.IMAGE_URL+visitor_idcard_photo).into(iv_visitIdCard);

                                }if (!signature.equalsIgnoreCase("")){
                                    Glide.with(getActivity()).load(Constant.IMAGE_URL+signature).into(iv_signature);

                                }

                                String date = convertor.getDateByTZ(added_datetime);
                                String time = convertor.getTimeByTZ(added_datetime);

                                tv_addEmpName.setText(added_by_employee_firstname+" "+added_by_employee_lastname);
                                tv_staffName.setText(staff_first_name+" "+staff_last_name);
                                tv_gender.setText(gender);
                                tv_visitorId.setText(visitor_id);
                                tv_purpose.setText(purpose);
                                tv_staffAdhar.setText(staff_adhar_card_no);
                                tv_staffPhone.setText(staff_phone_no);
                                tv_whomToMeet.setText(whom_to_meet);
                                tv_empAdhar.setText(employee_adhaar_card_no);
                                tv_department.setText(department);
                                tv_contact.setText(contact_number);
                                tv_entryTime.setText(entry_time);
                                tv_address.setText(address);
                                tv_addEmpId.setText(added_employeeid);
                                tv_entryDate.setText(entry_date);
                                tv_addedTime.setText(date+" "+time);
                                tv_visitorName.setText(visitor_name);
                                tv_empPhone.setText(employee_phone_number);

                            }

                            progressDialog.dismiss();

                        }else {
                            Log.d("visitorDetails_else", response.code() + " " + message);
                            progressDialog.dismiss();
                        }

                    }catch (Exception e){
                        e.printStackTrace();
                        Log.d("visitorDetails_else", response.code() + " "+e);

                    }

                }else {

                    try {
                        assert response.errorBody()!=null;
                        JSONObject object = new JSONObject(response.errorBody().string());
                        String message = object.getString("message");
                        Log.d("visitorDetails_fail", message);
                        progressDialog.dismiss();


                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<Object> call, @NonNull Throwable t) {

            }
        });
    }

}
