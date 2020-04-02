package in.varadhismartek.patashalaerp.DashboardModule.LeaveModule;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

import in.varadhismartek.Utils.Constant;
import in.varadhismartek.patashalaerp.DashboardModule.DashBoardMenuActivity;
import in.varadhismartek.patashalaerp.GeneralClass.CustomSpinnerAdapter;
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
public class AdminLeaveListFragment extends Fragment {

    RecyclerView recycler_view;
    APIService mApiService;
    DateConvertor convertor;

    ArrayList<EmpLeaveModel> empLeaveList;
    ArrayList<EmpLeaveModel> empLeaveListNew;
    LeaveAdapter adapter;
    ArrayList<LeaveModel> list;
    ArrayList<LeaveModel> leaveModels;

    ArrayList<String> employeeNameList = new ArrayList<>();
    ArrayList<String> spnStatus = new ArrayList<>();
    ArrayList<EmpLeaveModel> employeeList;
    Spinner spn_employee, spn_Status;
    ArrayList<LeaveModel> leaveModelsNew = new ArrayList<>();

    String empFname = "", empLname = "", empUUId, empPhoneNo, empAdhaarNo, empDept;
    FloatingActionButton fab;
    ImageView iv_backBtn;

    public AdminLeaveListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_admin_leave_list, container, false);

        initViews(view);
        //setRecyclerView();
        getAllEmployeeLeaveList();
        // getAllEmployeeAPI();


        spnStatus.clear();
        spnStatus.add("-- Status--");
        spnStatus.add("Approved");
        spnStatus.add("Rejected");
        spnStatus.add("Pending");


        ArrayAdapter<String> adp = new ArrayAdapter<String>
                (getActivity(), android.R.layout.simple_spinner_item, spnStatus);
        spn_Status.setAdapter(adp);


        spn_Status.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String notifyStatus = spn_Status.getSelectedItem().toString();

                if (!notifyStatus.equals("-- Status--")) {
                    filterLeave(notifyStatus);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        return view;
    }

    private void filterLeave(String notifyStatus) {

        if (leaveModels.size() > 0) {
            leaveModelsNew.clear();

            for (int i = 0; i < leaveModels.size(); i++) {
                if (notifyStatus.contains(leaveModels.get(i).getStatus())) {

                    leaveModelsNew.add(new LeaveModel(
                            leaveModels.get(i).getEmp_name(),
                            leaveModels.get(i).getFrom_date(),
                            leaveModels.get(i).getEmpPhoto(),
                            leaveModels.get(i).getStatus(),
                            leaveModels.get(i).getLeavetype(),
                            leaveModels.get(i).getEmpID(),
                            leaveModels.get(i).getToDate(),
                            leaveModels.get(i).getSchoolId(),
                            leaveModels.get(i).getSubject(),
                            leaveModels.get(i).getLeaveID()
                    ));
                }

            }
            setRecyclerViewNew(leaveModelsNew);
        }


    }

    private void setRecyclerViewNew(ArrayList<LeaveModel> leaveModelsNew) {
        adapter = new LeaveAdapter(getActivity(), leaveModelsNew, Constant.RV_LEAVE_ADMIN_LIST);
        recycler_view.setLayoutManager(new LinearLayoutManager(getActivity()));
        recycler_view.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }


    private void getAllEmployeeAPI() {
        mApiService.getAllEmployeeList(Constant.SCHOOL_ID).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if (response.isSuccessful()) {
                    try {
                        employeeNameList.clear();
                        employeeList.clear();
                        JSONObject object = new JSONObject(new Gson().toJson(response.body()));
                        String status = object.getString("status");

                        if (status.equalsIgnoreCase("Success")) {

                            JSONObject jsonData = object.getJSONObject("data");
                            Log.d("ADMIN_API_DATA", jsonData.toString());

                            Iterator<String> key = jsonData.keys();

                            while (key.hasNext()) {

                                String myKey = key.next();
                                Log.d("EMPLKEY", myKey);

                                JSONObject keyData = jsonData.getJSONObject(myKey);
                                Log.d("EMPKEYDATA", keyData.toString());


                                empUUId = keyData.getString("employee_uuid");
                                empFname = keyData.getString("first_name");
                                empLname = keyData.getString("last_name");
                                empPhoneNo = keyData.getString("phone_number");
                                empAdhaarNo = keyData.getString("adhaar_card_no");
                                empDept = keyData.getString("department_name");
                                String empName = empFname + " " + empLname;
                                employeeNameList.add(empName);

                                employeeList.add(new EmpLeaveModel(empFname, empLname, empUUId, empPhoneNo, empAdhaarNo, empDept));
                            }

                            CustomSpinnerAdapter customSpinnerAdapter = new CustomSpinnerAdapter(getActivity(), employeeNameList);
                            spn_employee.setAdapter(customSpinnerAdapter);


                        }

                    } catch (JSONException je) {

                    }
                }


            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {

            }
        });
        spn_employee.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String empName = spn_employee.getItemAtPosition(position).toString();
                int empOreder = spn_employee.getSelectedItemPosition();


                System.out.println("empName***" + empName + "***" + empOreder + "***" + employeeList.size());
                for (int i = 0; i < employeeList.size(); i++) {
                    if (empOreder == i) {
                        String strEmpId = employeeList.get(i).getEmpFname();
                        String strEmpId2 = employeeList.get(i).getEmpUUId();
                        System.out.println("empName2***" + strEmpId + "***" + strEmpId2 + "***" + employeeList.get(i));


                    }
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    private void initViews(View view) {

        mApiService = ApiUtils.getAPIService();
        convertor = new DateConvertor();
        iv_backBtn = view.findViewById(R.id.iv_backBtn);
        spn_employee = view.findViewById(R.id.spn_employee);
        spn_Status = view.findViewById(R.id.spn_Status);
        recycler_view = view.findViewById(R.id.recycler_view);
        fab = view.findViewById(R.id.fab);
        fab.setVisibility(View.GONE);

        iv_backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DashBoardMenuActivity leaveView = (DashBoardMenuActivity) getActivity();
                leaveView.loadFragment(Constant.LEAVE_DASHBOARD_FRAGMENT, null);


            }
        });

        empLeaveList = new ArrayList<>();
        leaveModels = new ArrayList<>();
        employeeList = new ArrayList<>();
        empLeaveListNew = new ArrayList<>();

    }

    private void getAllEmployeeLeaveList() {

        if (empLeaveList.size() > 0)
            empLeaveList.clear();

        mApiService.getAllEmployeeLeaveDetails(Constant.SCHOOL_ID).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {

                if (response.isSuccessful()) {

                    try {
                        leaveModels.clear();
                        JSONObject object = new JSONObject(new Gson().toJson(response.body()));
                        String status = object.getString("status");

                        if (status.equalsIgnoreCase("Success")) {

                            JSONObject jsonData = object.getJSONObject("data");
                            Log.d("ADMIN_API_DATA", jsonData.toString());

                            Iterator<String> key = jsonData.keys();

                            while (key.hasNext()) {

                                String myKey = key.next();
                                Log.d("ADMIN_KEY", myKey);

                                JSONObject keyData = jsonData.getJSONObject(myKey);
                                Log.d("ADMIN_API_KEY_DATA", keyData.toString());

                                String employee_name = keyData.getString("employee_name");
                                String applied_datetime = keyData.getString("applied_datetime");
                                String from_date = keyData.getString("from_date");
                                String employee_photo = keyData.getString("employee_photo");
                                String leave_status = keyData.getString("leave_status");
                                String leave_type = keyData.getString("leave_type");
                                String employee_uuid = keyData.getString("employee_uuid");
                                String to_date = keyData.getString("to_date");
                                String school_id = keyData.getString("school_id");
                                String subject = keyData.getString("subject");
                                String leave_id = keyData.getString("leave_id");

                                String appliedDate = convertor.getDateByTZ_SSS(applied_datetime);
                                String fromDate = convertor.getDateByTZ(from_date);
                                String toDate = convertor.getDateByTZ(to_date);

                                if (empLeaveList.size() > 0) {

                                    boolean b = true;

                                    for (int i = 0; i < empLeaveList.size(); i++) {

                                        if (empLeaveList.get(i).getAppliedDate().equals(appliedDate)) {

                                            b = false;

                                            empLeaveList.get(i).getLeaveList().add(new LeaveModel(employee_name, fromDate, employee_photo, leave_status,
                                                    leave_type, employee_uuid, toDate, school_id, subject, leave_id));
                                        }

                                    }

                                    if (b) {

                                        list = new ArrayList<>();
                                        list.add(new LeaveModel(employee_name, fromDate, employee_photo, leave_status,
                                                leave_type, employee_uuid, toDate, school_id, subject, leave_id));

                                        empLeaveList.add(new EmpLeaveModel(appliedDate, list));
                                    }

                                } else {
                                    list = new ArrayList<>();
                                    list.add(new LeaveModel(employee_name, fromDate, employee_photo, leave_status,
                                            leave_type, employee_uuid, toDate, school_id, subject, leave_id));

                                    empLeaveList.add(new EmpLeaveModel(appliedDate, list));

                                }

                                leaveModels.add(new LeaveModel(employee_name, fromDate, employee_photo, leave_status,
                                        leave_type, employee_uuid, toDate, school_id, subject, leave_id));
                            }

                            setRecyclerViewList(leaveModels);
                            adapter.notifyDataSetChanged();

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                } else {
                    try {
                        assert response.errorBody() != null;
                        JSONObject object = new JSONObject(response.errorBody().string());
                        String message = object.getString("message");
                        Log.d("ADMIN_API", message);
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

    private void setRecyclerViewList(ArrayList<LeaveModel> leaveModels) {
        Gson gson = new Gson();
        System.out.println("gson*****" + gson.toJson(leaveModels));
        adapter = new LeaveAdapter(getActivity(), leaveModels, Constant.RV_LEAVE_ADMIN_LIST);
        recycler_view.setLayoutManager(new LinearLayoutManager(getActivity()));
        recycler_view.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }


}
