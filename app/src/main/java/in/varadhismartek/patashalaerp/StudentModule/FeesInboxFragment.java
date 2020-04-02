package in.varadhismartek.patashalaerp.StudentModule;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

import in.varadhismartek.Utils.Constant;
import in.varadhismartek.patashalaerp.GeneralClass.CircleImageView;
import in.varadhismartek.patashalaerp.GeneralClass.CustomSpinnerAdapter;
import in.varadhismartek.patashalaerp.GeneralClass.DateConvertor;
import in.varadhismartek.patashalaerp.R;
import in.varadhismartek.patashalaerp.Retrofit.APIService;
import in.varadhismartek.patashalaerp.Retrofit.ApiUtils;
import in.varadhismartek.patashalaerp.Retrofit.ApiUtilsPatashala;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FeesInboxFragment extends Fragment implements View.OnClickListener {

    ImageView iv_backBtn;
    CircleImageView civ_image;
    TextView tv_student_name, tv_student_class, tv_student_section;
    Spinner spn_academic;
    FeesAdapter adapter;
    RecyclerView recycler_view;

    ArrayList<String> arrayList;
    ArrayList<String> academicList;
    ArrayList<FeesMonthModel> feesMonthList;
    DateConvertor convertor;
    ProgressDialog progressDialog;

    APIService mApiService;
    APIService mApiServicePatashala;
    String strStudentId="Undefine",strStudentSection="Undefine",strStudentClass="Undefine",strStudentName="Undefine",strStudentImage="";
    public FeesInboxFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fees_inbox, container, false);

        initViews(view);
        initListeners();
        setStrings();
        setRecyclerView();
        getAcademicYearAPI();

        return view;
    }

    private void setStrings() {

        assert getActivity()!= null;



    }

    private void setRecyclerView() {

        adapter = new FeesAdapter(feesMonthList, getActivity(), Constant.RV_FEES_HISTORY_ROW);
        recycler_view.setLayoutManager(new LinearLayoutManager(getActivity()));
        recycler_view.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void setSpinnerForAcademicYear(ArrayList<String> academicList){

        CustomSpinnerAdapter adapter = new CustomSpinnerAdapter(getActivity(),academicList ,Constant.audience_type);
        spn_academic.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        spn_academic.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                String fromDate = "";
                String toDate ="";

                if (i==0){
                    fromDate = "";
                    toDate ="";

                }else {

                    String[] date = spn_academic.getSelectedItem().toString().split("/");

                    fromDate = date[0];
                    toDate = date[1];

                }

                getStudentFeesPaymentsDetailsAPI(fromDate, toDate);


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }


    private void initListeners() {

        iv_backBtn.setOnClickListener(this);
    }

    private void initViews(View view) {

        mApiService = ApiUtils.getAPIService();
        mApiServicePatashala = ApiUtilsPatashala.getService();
        convertor = new DateConvertor();
        progressDialog = new ProgressDialog(getActivity());

        iv_backBtn = view.findViewById(R.id.iv_backBtn);
        recycler_view = view.findViewById(R.id.recycler_view);

        civ_image = view.findViewById(R.id.civ_image);
        tv_student_name = view.findViewById(R.id.tv_student_name);
        tv_student_class = view.findViewById(R.id.tv_student_class);
        tv_student_section = view.findViewById(R.id.tv_student_section);
        spn_academic = view.findViewById(R.id.spn_academic);

        arrayList = new ArrayList<>();
        academicList = new ArrayList<>();
        feesMonthList = new ArrayList<>();

        Bundle bundle = getArguments();
        if (bundle!= null){

             strStudentId = bundle.getString("STUDENT_ID");
             strStudentName = bundle.getString("STUDENT_NAME");
             strStudentClass = bundle.getString("STUDENT_CLASS");
             strStudentSection = bundle.getString("STUDENT_SECTION");
             strStudentImage = bundle.getString("STUDENT_IMAGE");

        }
        Log.i("StudentFees**",""+strStudentId+strStudentName+strStudentClass+strStudentSection+strStudentImage);
        tv_student_name.setText(strStudentName);
        tv_student_class.setText(strStudentClass);
        tv_student_section.setText(strStudentSection);
        Glide.with(getActivity()).load(Constant.IMAGE_URL+strStudentImage).into(civ_image);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.iv_backBtn:
                getActivity().onBackPressed();
                break;
        }
    }


    private void getAcademicYearAPI(){
        Log.d("jwhdlff",";jlfjdgfj;");

        if (academicList.size()>0){
            academicList.clear();
        }

        academicList.add(0,"-Academic Year-");

        progressDialog.show();
        mApiServicePatashala.getAcademicYear(Constant.SCHOOL_ID).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {

                if (response.isSuccessful()){
                    progressDialog.dismiss();
                    try {

                        JSONObject object = new JSONObject(new Gson().toJson(response.body()));
                        String message = object.getString("message");
                        String status1 = object.getString("status");

                        if (status1.equalsIgnoreCase("Success")){

                            JSONObject jsonData = object.getJSONObject("data");
                            Log.d("jwhdlff",jsonData.toString());

                            JSONObject academicYear = jsonData.getJSONObject("Academic_years");

                            Iterator<String> key = academicYear.keys();

                            while (key.hasNext()){

                                JSONObject years = academicYear.getJSONObject(key.next());

                                String start_date = years.getString("start_date");
                                String end_date = years.getString("end_date");

                                String from = start_date.substring(0,10);
                                String to   = end_date.substring(0,10);

                                Log.d("lakfblkas", start_date+" "+end_date);

                                academicList.add(from+"/"+to);
                            }

                            setSpinnerForAcademicYear(academicList);


                        }else {
                            progressDialog.dismiss();
                            Log.d("ldafhhlka", response.code()+" "+message);
                            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                            setSpinnerForAcademicYear(academicList);


                        }


                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }else {
                    try {
                        assert response.errorBody()!=null;
                        progressDialog.dismiss();
                        JSONObject object = new JSONObject(response.errorBody().string());
                        String message = object.getString("message");
                        Log.d("ADMIN_API", message);
                        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                        setSpinnerForAcademicYear(academicList);


                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onFailure(@NonNull Call<Object> call, @NonNull Throwable t) {
                progressDialog.dismiss();
            }
        });
    }

    private void getStudentFeesPaymentsDetailsAPI(String fromDate, String toDate){

        Log.d("INPUT_FEE_DATA",Constant.SCHOOL_ID+" "+strStudentId+" "+fromDate+" "+toDate);



        progressDialog.show();
        //mApiService.getStudentFeesPaymentsDetails("9ecf9e65-2ae5-40c7-9a1c-68bf6ade801f", "d0b307c4-69c2-43e7-b387-b534ee2dc22a", "2020-01-01", "2021-04-16").enqueue(new Callback<Object>() {
        mApiService.getStudentFeesPaymentsDetails(Constant.SCHOOL_ID, strStudentId, fromDate, toDate).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {

                if (feesMonthList.size()>0){
                    feesMonthList.clear();
                }

                if (response.isSuccessful()){

                    try {
                        progressDialog.dismiss();
                        JSONObject object = new JSONObject(new Gson().toJson(response.body()));
                        String message1 = object.getString("message");
                        String status1 = object.getString("status");

                        if (status1.equalsIgnoreCase("Success")){
                            Log.d("FEES_SUCCESS", response.code()+" "+message1);

                            JSONObject jsonData = object.getJSONObject("data");
                            Log.d("FEES_SUCCESS_DATA", jsonData.toString());

                            Iterator<String> key = jsonData.keys();

                            while (key.hasNext()){

                                String month = key.next();
                                Log.d("MONTH_NAME", month);

                                JSONObject keyData = jsonData.getJSONObject(month);
                                Log.d("FEES_KEY_DATA", keyData.toString());

                                String upcoming_total ="", paid_total = "", pending_total = "";

                                upcoming_total = keyData.getString("upcoming_total");
                                paid_total = keyData.getString("paid_total");
                                pending_total = keyData.getString("pending_total");

                                Log.d("upcoming_total**", upcoming_total);

                                ArrayList<FeesDetailsModel> feesDetailsList = new ArrayList<>();

                                if (keyData.getJSONObject("paid_fees").toString().equals("{}")){
                                    Log.d("paidFees", "paid fees");

                                }else {
                                    Log.d("MONTH_NAME_1", month);

                                    JSONObject paidFees = keyData.getJSONObject("paid_fees");
                                    Iterator<String> myKey = paidFees.keys();

                                    while (myKey.hasNext()){

                                        JSONObject paidData = paidFees.getJSONObject(myKey.next());

                                        String paid_status = "", added_datetime = "", penalty_to = "", fee_payment_id = "", penalty_from = ""
                                                , fee_type = "", due_date = "", amount_due = "", penalty_amount = "", from_date = "", net_fees = "";

                                        paid_status    = paidData.getString("paid_status");
                                        added_datetime = paidData.getString("added_datetime");
                                        fee_payment_id = paidData.getString("fee_payment_id");
                                        fee_type       = paidData.getString("fee_type");
                                        due_date       = paidData.getString("due_date");
                                        amount_due     = paidData.getString("amount_due");
                                        penalty_amount = paidData.getString("penalty_amount");
                                        from_date      = paidData.getString("from_date");
                                        net_fees       = paidData.getString("net_fees");

                                        if (!paidData.isNull("penalty_to")) {
                                            penalty_to = paidData.getString("penalty_to");
                                        }
                                        if (!paidData.isNull("penalty_from")) {
                                            penalty_from = paidData.getString("penalty_from");
                                        }

                                        feesDetailsList.add(new FeesDetailsModel(paid_status,added_datetime,penalty_to,fee_payment_id,penalty_from,
                                                fee_type,due_date,amount_due,penalty_amount,from_date,net_fees));
                                    }
                                }

                                if (keyData.getJSONObject("pending_fees").toString().equals("{}")){
                                    Log.d("pendingFees", "pending fees");

                                }else {
                                    Log.d("MONTH_NAME_2", month);

                                    JSONObject pendingFees = keyData.getJSONObject("pending_fees");
                                    Iterator<String> myKey = pendingFees.keys();

                                    while (myKey.hasNext()){

                                        JSONObject pendingData = pendingFees.getJSONObject(myKey.next());

                                        String paid_status = "", added_datetime = "", penalty_to = "", fee_payment_id = "", penalty_from = ""
                                                , fee_type = "", due_date = "", amount_due = "", penalty_amount = "", from_date = "", net_fees = "";

                                        paid_status    = pendingData.getString("paid_status");
                                        added_datetime = pendingData.getString("added_datetime");
                                        fee_payment_id = pendingData.getString("fee_payment_id");
                                        fee_type       = pendingData.getString("fee_type");
                                        due_date       = pendingData.getString("due_date");
                                        amount_due     = pendingData.getString("amount_due");
                                        penalty_amount = pendingData.getString("penalty_amount");
                                        from_date      = pendingData.getString("from_date");
                                        net_fees       = pendingData.getString("net_fees");

                                        if (!pendingData.isNull("penalty_to")) {
                                            penalty_to = pendingData.getString("penalty_to");
                                        }
                                        if (!pendingData.isNull("penalty_from")) {
                                            penalty_from = pendingData.getString("penalty_from");
                                        }

                                        feesDetailsList.add(new FeesDetailsModel(paid_status,added_datetime,penalty_to,fee_payment_id,penalty_from,
                                                fee_type,due_date,amount_due,penalty_amount,from_date,net_fees));
                                    }
                                }

                                if (keyData.getJSONObject("upcoming_fees").toString().equals("{}")){
                                    Log.d("upcomingFees", "upcoming fees");

                                }else {
                                    Log.d("MONTH_NAME_3", month);

                                    JSONObject upcomingFees = keyData.getJSONObject("upcoming_fees");
                                    Iterator<String> myKey = upcomingFees.keys();

                                    while (myKey.hasNext()){

                                        JSONObject upcomingData = upcomingFees.getJSONObject(myKey.next());

                                        String paid_status = "", added_datetime = "", penalty_to = "", fee_payment_id = "", penalty_from = ""
                                                , fee_type = "", due_date = "", amount_due = "", penalty_amount = "", from_date = "", net_fees = "";

                                        paid_status    = upcomingData.getString("paid_status");
                                        added_datetime = upcomingData.getString("added_datetime");
                                        fee_payment_id = upcomingData.getString("fee_payment_id");
                                        fee_type       = upcomingData.getString("fee_type");
                                        due_date       = upcomingData.getString("due_date");
                                        amount_due     = upcomingData.getString("amount_due");
                                        penalty_amount = upcomingData.getString("penalty_amount");
                                        from_date      = upcomingData.getString("from_date");
                                        net_fees       = upcomingData.getString("net_fees");

                                        if (!upcomingData.isNull("penalty_to")) {
                                            penalty_to = upcomingData.getString("penalty_to");
                                        }
                                        if (!upcomingData.isNull("penalty_from")) {
                                            penalty_from = upcomingData.getString("penalty_from");
                                        }

                                        feesDetailsList.add(new FeesDetailsModel(paid_status,added_datetime,penalty_to,fee_payment_id,penalty_from,
                                                fee_type,due_date,amount_due,penalty_amount,from_date,net_fees));
                                    }
                                }

                                feesMonthList.add(new FeesMonthModel(month,upcoming_total,paid_total,pending_total,feesDetailsList));
                            }

                            adapter.notifyDataSetChanged();


                        }else {
                            progressDialog.dismiss();
                            Log.d("FEES_ELSE", response.code()+" "+message1);
                            adapter.notifyDataSetChanged();

                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }else {
                    try {
                        assert response.errorBody()!=null;
                        progressDialog.dismiss();
                        JSONObject object = new JSONObject(response.errorBody().string());
                        String message = object.getString("message");
                        Log.d("ADMIN_API", message);
                        adapter.notifyDataSetChanged();


                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onFailure(@NonNull Call<Object> call, @NonNull Throwable t) {
                progressDialog.dismiss();
            }
        });
    }
}
