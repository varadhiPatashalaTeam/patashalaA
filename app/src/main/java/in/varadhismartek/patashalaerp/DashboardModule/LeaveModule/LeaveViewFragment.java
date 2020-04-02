package in.varadhismartek.patashalaerp.DashboardModule.LeaveModule;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import in.varadhismartek.Utils.Constant;
import in.varadhismartek.patashalaerp.DashboardModule.DashBoardMenuActivity;
import in.varadhismartek.patashalaerp.DashboardModule.EmployeeModule.EmployeeActivity;
import in.varadhismartek.patashalaerp.GeneralClass.CircleImageView;
import in.varadhismartek.patashalaerp.GeneralClass.DateConvertor;
import in.varadhismartek.patashalaerp.R;
import in.varadhismartek.patashalaerp.Retrofit.APIService;
import in.varadhismartek.patashalaerp.Retrofit.ApiUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class LeaveViewFragment extends Fragment implements View.OnClickListener {

    ImageView ivBack;
    TextView empName,empId,tvAppliedDate,tvApplyTime,tvLeaveType,tvStatus, tvLeaveSub, tvLeaveMsg, tvFullAdd, tvContact, tvComment;
    TextView from_date_textview,to_date_textview;
    CircleImageView civEmpImage;
    EditText etAddComment;
    Button btApprove, btReject;
    String leaveId,employeeID,ClickBy;
    String country, applied_datetime, email, pincode, employee_first_name, state, phone_no, comment,
            door_no, employee_last_name, role, leave_id, subject,message, city, approved_by, leave_status="",
            leave_type, department, employee_uuid, to_date, school_id, wing, landmark, employee_photo, from_date, locality;
    Long leave_time_contact_number;

    APIService mApiService;

    ImageView iv_status_circle;

    public LeaveViewFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_leave_view, container, false);

        initViews(view);
        initListeners();
        getBundleValue();

        return view;
    }

    private void setStringValues() {

        DateConvertor dateConvertor = new DateConvertor();

        String appliedDate = dateConvertor.getDateByTZ_SSS(applied_datetime);
        String fromDate = dateConvertor.getDateByTZ(from_date);
        String toDate = dateConvertor.getDateByTZ(to_date);
        String appliedTime = dateConvertor.getTimeByTZ_SSS(applied_datetime);

        empName.setText(employee_first_name+" "+ employee_last_name);
        empId.setText(employee_uuid);
        tvLeaveType.setText(leave_type);
        tvAppliedDate.setText(appliedDate);
        tvApplyTime.setText(appliedTime);
        tvLeaveSub.setText(subject);
        tvLeaveMsg.setText(message);
        from_date_textview.setText(fromDate);
        to_date_textview.setText(toDate);
        tvStatus.setText(leave_status);
        tvContact.setText(leave_time_contact_number+"");

        if (!employee_photo.isEmpty() ||
                !employee_photo.equals("")) {
            Picasso.with(getActivity())
                    .load(Constant.IMAGE_LINK + employee_photo)
                    // .error(R.drawable.user_icon)
                    .into(civEmpImage);
        } else {
            civEmpImage.setBackgroundResource(R.drawable.patashala_logo);
        }


        String fullAddress = door_no+", "+locality+", near "+landmark+", "+city+", "+state+", "+country+"-"+pincode;

        tvFullAdd.setText(fullAddress);

        switch (leave_status){

            case "Pending":


                iv_status_circle.setImageResource(R.drawable.ring_shape2);

                break;

            case "Approved":
                iv_status_circle.setImageResource(R.drawable.ring_shape3);

                break;

            case "Rejected":
                iv_status_circle.setImageResource(R.drawable.ring_shape4);


                break;

            case "Cancelled":
                iv_status_circle.setImageResource(R.drawable.ring_shape2);

                break;
        }

    }

    private void getBundleValue() {

        Bundle bundle = getArguments();
        leaveId = bundle.getString("leaveID");
        employeeID= bundle.getString("EMPUUID");
        ClickBy= bundle.getString("EMPLOYEE");

        System.out.println("ClickBy***"+ClickBy+employeeID);

        Constant.EMPLOYEE_ID =employeeID;

        if (ClickBy.equals("2")){
            btApprove.setVisibility(View.VISIBLE);
            btReject.setVisibility(View.VISIBLE);


        }else if (ClickBy.equals("ADMIN_LEAVE")){
            btApprove.setVisibility(View.INVISIBLE);
            btReject.setText("Cancel");

         }else if (ClickBy.equals("EMP_LEAVE")){
            btApprove.setVisibility(View.VISIBLE);
            btReject.setVisibility(View.VISIBLE);


        }/*else if (ClickBy.equals("3")){
            btApprove.setVisibility(View.GONE);
            btReject.setVisibility(View.GONE);

        }
        else if (ClickBy.equals("0")){
            btApprove.setVisibility(View.GONE);
            btReject.setVisibility(View.GONE);

        }*/

        Constant.EMPLOYEE_ID= employeeID;
        getEmployeeLeaveFullDetailsAPI();
    }

    private void initListeners() {
        ivBack.setOnClickListener(this);
        btApprove.setOnClickListener(this);
        btReject.setOnClickListener(this);

    }

    private void initViews(View view) {

        mApiService = ApiUtils.getAPIService();

        ivBack = view.findViewById(R.id.ivBack);
        civEmpImage = view.findViewById(R.id.civEmpImage);
        empName = view.findViewById(R.id.empname);
        empId = view.findViewById(R.id.empId);
        tvAppliedDate = view.findViewById(R.id.tvAppliedDate);
        tvApplyTime = view.findViewById(R.id.tvApplyTime);
        tvLeaveType = view.findViewById(R.id.tvLeaveType);
        tvStatus = view.findViewById(R.id.tvStatus);
        iv_status_circle = view.findViewById(R.id.iv_status_circle);
        tvLeaveSub = view.findViewById(R.id.tvLeaveSub);
        tvLeaveMsg = view.findViewById(R.id.tvLeaveMsg);
        tvFullAdd = view.findViewById(R.id.tvFullAdd);
        tvContact = view.findViewById(R.id.tvContact);
        from_date_textview = view.findViewById(R.id.from_date_textview);
        to_date_textview = view.findViewById(R.id.to_date_textview);
        tvComment = view.findViewById(R.id.tvComment);
        etAddComment = view.findViewById(R.id.etAddComment);
        btApprove = view.findViewById(R.id.btApprove);
        btReject = view.findViewById(R.id.btReject);


    }

    @Override
    public void onClick(View view) {

        assert getActivity() != null;

        switch (view.getId()){

            case R.id.btApprove:

                leaveApproveRejectAPI("Approved","");
                break;


            case R.id.btReject:
                leaveApproveRejectAPI("Rejected","");
                break;

            case R.id.ivBack:
                getActivity().onBackPressed();
                   /* DashBoardMenuActivity leaveActivity = (DashBoardMenuActivity) getActivity();
                    leaveActivity.loadFragment(Constant.LEAVE_DASHBOARD_FRAGMENT, null);
               */

                break;

            case R.id.bt_cancel:

                if (leave_status.equalsIgnoreCase("Pending")){

                    //noinspection ConstantConditions
                    if (!leave_status.equals(""))
                        cancelingEmployeeLeaveAPI();

                }else {

                    Toast.makeText(getActivity(), "Status Changed By Admin", Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }

    private void getEmployeeLeaveFullDetailsAPI(){

        Log.d("FULL_DETAIL_API", Constant.SCHOOL_ID+" "+Constant.EMPLOYEE_ID+" "+leaveId);

        mApiService.getEmployeeLeaveFullDetails(Constant.SCHOOL_ID, Constant.EMPLOYEE_ID,leaveId).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {

                if (response.isSuccessful()){

                    try {
                        JSONObject object = new JSONObject(new Gson().toJson(response.body()));

                        String status = object.getString("status");

                        if (status.equalsIgnoreCase("Success")){

                            JSONObject jsonData = object.getJSONObject("data");
                            Log.d("FULL_DATA", jsonData.toString());

                            Iterator<String> key = jsonData.keys();

                            while (key.hasNext()){

                                String myKey = key.next();
                                Log.d("FULL_API_KEYS", myKey);

                                JSONObject keyData = jsonData.getJSONObject(myKey);
                                Log.d("FULL_DETAIL_KEY_DATA", keyData.toString());

                                country = keyData.getString("country");
                                applied_datetime = keyData.getString("applied_datetime");
                                email = keyData.getString("email");
                                pincode = keyData.getString("pincode");
                                employee_first_name = keyData.getString("employee_first_name");
                                state = keyData.getString("state");
                                phone_no = keyData.getString("phone_no");
                                //comment = keyData.getString("comment");
                                door_no = keyData.getString("door_no");
                                employee_last_name = keyData.getString("employee_last_name");
                                role = keyData.getString("role");
                                leave_id = keyData.getString("leave_id");
                                subject = keyData.getString("subject");
                                message = keyData.getString("message");
                                city = keyData.getString("city");
                                //approved_by = keyData.getString("approved_by");
                                leave_status = keyData.getString("leave_status");
                                leave_type = keyData.getString("leave_type");
                                department = keyData.getString("department");
                                employee_uuid = keyData.getString("employee_uuid");
                                to_date = keyData.getString("to_date");
                                school_id = keyData.getString("school_id");
                                wing = keyData.getString("wing");
                                landmark = keyData.getString("landmark");
                                if (keyData.isNull("employee_photo")){
                                    employee_photo = "";

                                }else {
                                    employee_photo = keyData.getString("employee_photo");

                                }

                                from_date = keyData.getString("from_date");
                                leave_time_contact_number = keyData.getLong("leave_time_contact_number");
                                locality = keyData.getString("locality");

                            }

                            setStringValues();

                        }else {
                            Toast.makeText(getActivity(), response.message(), Toast.LENGTH_SHORT).show();
                            Log.d("FULL_DETAIL_API", response.message()+" "+response.code());

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<Object> call, @NonNull Throwable t) {

            }
        });

    }

    private void cancelingEmployeeLeaveAPI(){

        mApiService.cancelingEmployeeLeave(Constant.SCHOOL_ID,Constant.EMPLOYEE_ID,leaveId).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {

                if (response.isSuccessful()){
                    Log.d("MY_CANCEL_API", response.message());
                    Toast.makeText(getActivity(), "Canceled Successfully", Toast.LENGTH_SHORT).show();
                    getEmployeeLeaveFullDetailsAPI();

                }else {
                    Log.d("MY_CANCEL_API", response.message());
                    Toast.makeText(getActivity(), response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Log.d("MY_CANCEL_API", t.getMessage());

            }
        });
    }

    private void leaveApproveRejectAPI(final String leaveStatus, final String comment){

        mApiService.approvedEmployeeLeave(Constant.SCHOOL_ID,Constant.EMPLOYEE_ID,Constant.EMPLOYEE_BY_ID,
                comment,leaveStatus,leaveId).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {

                if (response.isSuccessful()){

                    try {
                        JSONObject object = new JSONObject(new Gson().toJson(response.body()));
                        String message = object.getString("message");
                        Log.d("Approved_api", message);
                        Toast.makeText(getActivity(), "Leave"+ leaveStatus +"successfully", Toast.LENGTH_SHORT).show();
                        getActivity().getFragmentManager().popBackStack();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }else {

                    try {
                        assert response.errorBody() != null;
                        JSONObject obj = new JSONObject(response.errorBody().string());
                        String message = obj.getString("message");
                        Log.d("Approved_api", message);
                        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();

                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {

            }
        });

    }
}
