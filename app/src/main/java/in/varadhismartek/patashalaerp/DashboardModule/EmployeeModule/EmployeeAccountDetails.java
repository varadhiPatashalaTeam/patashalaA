package in.varadhismartek.patashalaerp.DashboardModule.EmployeeModule;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import in.varadhismartek.Utils.Constant;
import in.varadhismartek.patashalaerp.GeneralClass.CircleImageView;
import in.varadhismartek.patashalaerp.R;
import in.varadhismartek.patashalaerp.Retrofit.APIService;
import in.varadhismartek.patashalaerp.Retrofit.ApiUtilsPatashala;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EmployeeAccountDetails extends Fragment implements View.OnClickListener {
    APIService apiServicePataShala;
    String employeeID="NA",employeeImage="NA",employeeName="NA",employeeDept="NA",employeeDesignation="NA";
    String accountStatus, employee_uuid, employee_image="", employee_lastname, ESI_number="NA",
            bank_name="NA", account_number="NA", salary="NA", employee_firstname="NA", employee_customid="NA",
            employee_role="NA", branch_name="NA", PF_number="NA", IFSC_code="NA", account_holder_name="NA";
    TextView tv_EmployeeName,tv_EmployeeID,tv_Department,tv_Designation;
    TextView tvTitle;
    ImageView iv_backBtn;
    CircleImageView civ_EmployeeImage;
    TextView tv_GrossSalary,tv_AccountHolderName,tv_BankName,tv_BranchName,tv_AccountNo,tv_IFSCCode,tv_ESINo,tv_PFNo;

    public EmployeeAccountDetails() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.employee_account_iew, container, false);
        initView(view);
        initListener();
        getBundle();
        getEmployeeAccountDetails();
        return view;
    }

    private void getBundle() {
        Bundle bundle = getArguments();

        employeeID = bundle.getString("EMPUUID");
        employeeImage = bundle.getString("EMP_IMAGE");

        employeeName = bundle.getString("EMP_NAME");
        employeeDept = bundle.getString("EMP_DEPT");
        employeeDesignation = bundle.getString("EMP_DESIGNATION");

        Constant.EMPLOYEE_ID = employeeID;
        Log.i("Constant*EMPLOYEE_ID1", Constant.EMPLOYEE_ID);


    }
    private void initView(View view) {
        apiServicePataShala = ApiUtilsPatashala.getService();

        tvTitle = view.findViewById(R.id.tvTitle);
        iv_backBtn = view.findViewById(R.id.iv_backBtn);
        civ_EmployeeImage = view.findViewById(R.id.civ_EmployeeImage);
        tv_EmployeeName = view.findViewById(R.id.tv_EmployeeName);
        tv_EmployeeID = view.findViewById(R.id.tv_EmployeeID);
        tv_Department = view.findViewById(R.id.tv_Department);
        tv_Designation = view.findViewById(R.id.tv_Designation);

        tv_GrossSalary = view.findViewById(R.id.tv_GrossSalary);
        tv_AccountHolderName = view.findViewById(R.id.tv_AccountHolderName);
        tv_BankName = view.findViewById(R.id.tv_BankName);
        tv_BranchName = view.findViewById(R.id.tv_BranchName);
        tv_AccountNo = view.findViewById(R.id.tv_AccountNo);
        tv_IFSCCode = view.findViewById(R.id.tv_IFSCCode);
        tv_ESINo = view.findViewById(R.id.tv_ESINo);
        tv_PFNo = view.findViewById(R.id.tv_PFNo);
        tvTitle.setText("Employee Account Detail");


    }

    private void initListener() {
        iv_backBtn.setOnClickListener(this);

    }

    private void getEmployeeAccountDetails() {
        apiServicePataShala.getEmployeeAccountDetails(Constant.SCHOOL_ID, Constant.EMPLOYEE_ID)
                .enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if (response.isSuccessful()) {
                    try {
                        JSONObject object = new JSONObject(new Gson().toJson(response.body()));
                        String status = object.getString("status");
                        String message = object.getString("message");

                        if (status.equalsIgnoreCase("Success")) {

                            JSONArray jsonData = object.getJSONArray("data");
                            for (int i = 0; i < jsonData.length(); i++) {
                                JSONObject jsonObject = jsonData.getJSONObject(i);

                                if (!jsonObject.isNull("status")) {
                                    accountStatus = jsonObject.getString("status");
                                }
                                if (!jsonObject.isNull("employee_uuid")) {
                                    employee_uuid = jsonObject.getString("employee_uuid");
                                }
                                if (!jsonObject.isNull("employee_image")) {
                                    employee_image = jsonObject.getString("employee_image");
                                }
                                if (!jsonObject.isNull("employee_lastname")) {
                                    employee_lastname = jsonObject.getString("employee_lastname");
                                }
                                if (!jsonObject.isNull("ESI_number")) {
                                    ESI_number = jsonObject.getString("ESI_number");
                                }

                                if (!jsonObject.isNull("bank_name")) {
                                    bank_name = jsonObject.getString("bank_name");
                                }
                                if (!jsonObject.isNull("account_number")) {
                                    account_number = jsonObject.getString("account_number");
                                }
                                if (!jsonObject.isNull("salary")) {
                                    salary = jsonObject.getString("salary");
                                }
                                if (!jsonObject.isNull("employee_firstname")) {
                                    employee_firstname = jsonObject.getString("employee_firstname");
                                }
                                if (!jsonObject.isNull("employee_customid")) {
                                    employee_customid = jsonObject.getString("employee_customid");
                                }
                                if (!jsonObject.isNull("employee_role")) {
                                    employee_role = jsonObject.getString("employee_role");
                                }
                                if (!jsonObject.isNull("branch_name")) {
                                    branch_name = jsonObject.getString("branch_name");
                                }
                                if (!jsonObject.isNull("PF_number")) {
                                    PF_number = jsonObject.getString("PF_number");
                                }
                                if (!jsonObject.isNull("IFSC_code")) {
                                    IFSC_code = jsonObject.getString("IFSC_code");
                                }
                                if (!jsonObject.isNull("account_holder_name")) {
                                    account_holder_name = jsonObject.getString("account_holder_name");
                                }


                            }

                            tv_EmployeeName.setText(employee_firstname+" "+employee_lastname);
                            tv_EmployeeID.setText(employee_uuid);
                            tv_Department.setText(employee_role);
                            tv_Designation.setText(employee_role);

                            tv_GrossSalary.setText(salary);
                            tv_AccountHolderName.setText(account_holder_name);
                            tv_BankName.setText(bank_name);
                            tv_BranchName.setText(branch_name);
                            tv_AccountNo.setText(account_number);
                            tv_IFSCCode.setText(IFSC_code);
                            tv_ESINo.setText(ESI_number);
                            tv_PFNo.setText(PF_number);
                            Glide.with(getActivity()).load(Constant.IMAGE_URL + employeeImage).into(civ_EmployeeImage);


                        }
                    } catch (JSONException je) {
                        je.printStackTrace();
                    }

                } else {
                    try {
                        assert response.errorBody() != null;
                        JSONObject object = new JSONObject(response.errorBody().string());
                        String message = object.getString("message");
                        Log.d("ADMIN_API", message);
                        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();

                        tv_EmployeeName.setText(employeeName);
                        tv_EmployeeID.setText(employeeID);
                        tv_Department.setText(employeeDept);
                        tv_Designation.setText(employeeDesignation);


                        tv_GrossSalary.setText(salary);
                        tv_AccountHolderName.setText(account_holder_name);
                        tv_BankName.setText(bank_name);
                        tv_BranchName.setText(branch_name);
                        tv_AccountNo.setText(account_number);
                        tv_IFSCCode.setText(IFSC_code);
                        tv_ESINo.setText(ESI_number);
                        tv_PFNo.setText(PF_number);
                        Glide.with(getActivity()).load(Constant.IMAGE_URL + employeeImage).into(civ_EmployeeImage);


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


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_backBtn:
                getActivity().onBackPressed();
                break;
        }
    }
}
