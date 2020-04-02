package in.varadhismartek.patashalaerp.DashboardModule.NotificationModule;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

import in.varadhismartek.Utils.Constant;
import in.varadhismartek.patashalaerp.DashboardModule.DashBoardMenuActivity;
import in.varadhismartek.patashalaerp.GeneralClass.CustomSpinnerAdapter;
import in.varadhismartek.patashalaerp.GeneralClass.DateConvertor;
import in.varadhismartek.patashalaerp.R;
import in.varadhismartek.patashalaerp.Retrofit.APIService;
import in.varadhismartek.patashalaerp.Retrofit.ApiUtilsPatashala;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationInboxFragment extends Fragment implements View.OnClickListener {
    Spinner spn_notification, spn_Status;
    RecyclerView rv_myHistory;

    EditText edSearch;
    FloatingActionButton fab_button;

    ArrayList<String> spnList;
    ArrayList<String> spnStatus;

    String selectedItem = "";
    private APIService mApiService;

    ArrayList<EmpNotificationModel> empNotificationList;
    DateConvertor convertor;
    NotificationAdapter adapter;
    ImageView img_back;
    TextView tvNorecords;


    public NotificationInboxFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notification_inbox, container, false);

        initViews(view);
        initListeners();
        getNotificationAPI();
        initRecyclerviewHistory();


        callNotificationAPI();

        return view;
    }

    private void initListeners() {

        fab_button.setOnClickListener(this);
        edSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                //  filter(editable.toString());
            }
        });


    }

    private void initViews(View view) {

        mApiService = ApiUtilsPatashala.getService();
        convertor = new DateConvertor();

        tvNorecords = view.findViewById(R.id.tvNorecords);
        rv_myHistory = view.findViewById(R.id.rv_history);
        img_back = view.findViewById(R.id.img_back);
        spn_notification = view.findViewById(R.id.spn_notification);
        spn_Status = view.findViewById(R.id.spn_status);
        fab_button = view.findViewById(R.id.fab_button);
        edSearch = view.findViewById(R.id.ed_search);

        spnList = new ArrayList<>();
        spnStatus = new ArrayList<>();
        empNotificationList = new ArrayList<>();
        spnStatus.clear();


        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });



        callStatusSpinner();


    }

    private void callStatusSpinner() {

        spnStatus.add("-- Status--");
        spnStatus.add("Approved");
        spnStatus.add("Rejected");
        spnStatus.add("Pending");

        CustomSpinnerAdapter customSpinnerAdapter = new CustomSpinnerAdapter(getActivity(),spnStatus);
        spn_Status.setAdapter(customSpinnerAdapter);


        spn_Status.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String notifyStatus = spn_Status.getSelectedItem().toString();

                if (!notifyStatus.equals("-Status-") && !selectedItem.isEmpty()) {
                    getNoficationByFilter(notifyStatus);
                }
                initRecyclerviewHistory();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setSpinnerNotification() {

        spnList.add(0, "-- Select --");

        CustomSpinnerAdapter customSpinnerAdapter = new CustomSpinnerAdapter(getActivity(), spnList);
        spn_notification.setAdapter(customSpinnerAdapter);
        customSpinnerAdapter.notifyDataSetChanged();

        spn_notification.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {


                if (i == 0) {
                    selectedItem = "";
                    tvNorecords.setVisibility(View.VISIBLE);
                    rv_myHistory.setVisibility(View.GONE);

                } else {
                    selectedItem = spn_notification.getSelectedItem().toString();
                    tvNorecords.setVisibility(View.GONE);
                    rv_myHistory.setVisibility(View.VISIBLE);
                    callNotificationAPI();
                }

                Log.i("*selectedItem**", "" + selectedItem);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private void initRecyclerviewHistory() {

        Log.i("empNotificationList",""+empNotificationList.size());


        adapter = new NotificationAdapter(empNotificationList, getActivity(), Constant.RV_NOTIFICATION_INBOX_ROW);
        rv_myHistory.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv_myHistory.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

    /*************************Filter By Status*********************************************/
    private void getNoficationByFilter(final String notifyStatus) {
        if (empNotificationList.size() > 0)
            empNotificationList.clear();

        mApiService.get_NotificationAdmin(Constant.SCHOOL_ID).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {

                if (response.isSuccessful()) {

                    try {
                        JSONObject object = new JSONObject(new Gson().toJson(response.body()));

                        String status1 = object.getString("status");

                        if (status1.equalsIgnoreCase("Success")) {

                            JSONObject jsonData = object.getJSONObject("data");
                            Log.d("GET_API_DATA", jsonData.toString());

                            JSONObject keyData = jsonData.getJSONObject(selectedItem);

                            if (keyData.equals("{}")) {
                                Log.d("GET_API_KEY_DATA_if", keyData.toString());

                            } else {
                                Log.d("GET_API_KEY_DATA_else", keyData.toString());

                                Iterator<String> key = keyData.keys();

                                while (key.hasNext()) {

                                    String myKey = key.next();
                                    Log.d("MY_KEY", myKey);

                                    JSONObject finalData = keyData.getJSONObject(myKey);
                                    Log.d("FINAL_DATA", finalData.toString());
                                    String approver_status = finalData.getString("approver_status");

                                    boolean status = finalData.getBoolean("status");
                                    String notification_attachment = finalData.getString("notification_attachment");
                                    String message = finalData.getString("message");
                                    String send_date = finalData.getString("send_date");
                                    String notification_id = finalData.getString("notification_id");
                                    String added_datetime = finalData.getString("added_datetime");
                                    String added_employee_last_name = finalData.getString("added_employee_last_name");
                                    String title = "";
                                    if (!finalData.isNull("title")) {
                                        title = finalData.getString("title");
                                    }

                                    String added_employee_first_name = finalData.getString("added_employee_first_name");
                                    String send_time = finalData.getString("send_time");
                                    String added_employeeid = finalData.getString("added_employeeid");
                                    String send_to = finalData.getString("send_to");


                                    ArrayList<String> listSend = new ArrayList<>();
                                    switch (selectedItem) {

                                        case "divisions":

                                            JSONArray arrayDivision = finalData.getJSONArray("division");
                                            for (int i = 0; i < arrayDivision.length(); i++) {
                                                String divisionData = arrayDivision.getString(i);
                                                listSend.add(divisionData);
                                                Log.i("divisionData***", "" + divisionData);
                                            }

                                            break;

                                    }


                                    String addedDate = convertor.getDateByTZ_SSS(added_datetime);
                                    String addedTime = convertor.getTimeByTZ_SSS(added_datetime);

                                    if (approver_status.equals(notifyStatus)) {

                                        if (empNotificationList.size() > 0) {

                                            boolean b = true;

                                            for (int i = 0; i < empNotificationList.size(); i++) {

                                                if (empNotificationList.get(i).getApplyDate().equals(addedDate)) {
                                                    b = false;

                                                    empNotificationList.get(i).getNotificationList()
                                                            .add(new NotificationModel(status, notification_id,
                                                                    title, message, addedDate, addedTime,
                                                                    notification_attachment, send_to, send_date, send_time, approver_status,
                                                                    added_employee_first_name, added_employee_last_name, listSend));
                                                }
                                            }

                                            if (b) {
                                                ArrayList<NotificationModel> myList = new ArrayList<>();
                                                myList.add(new NotificationModel(status, notification_id,
                                                        title, message, addedDate, addedTime, notification_attachment,
                                                        send_to, send_date, send_time, approver_status,
                                                        added_employee_first_name, added_employee_last_name, listSend));

                                                empNotificationList.add(new EmpNotificationModel(addedDate, myList));
                                            }

                                        } else {

                                            ArrayList<NotificationModel> myList = new ArrayList<>();
                                            myList.add(new NotificationModel(status, notification_id,
                                                    title,
                                                    message, addedDate, addedTime, notification_attachment, send_to, send_date, send_time,
                                                    approver_status, added_employee_first_name, added_employee_last_name, listSend));

                                            empNotificationList.add(new EmpNotificationModel(addedDate, myList));
                                        }
                                    }
                                }
                                adapter.notifyDataSetChanged();
                            }

                        } else {
                            Log.d("GET_API_FAIL", String.valueOf(response.code()));

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    Log.d("GET_API_FAILED", String.valueOf(response.code()));
                    Log.d("GET_API_FAILED", response.message());

                }
            }

            @Override
            public void onFailure(@NonNull Call<Object> call, @NonNull Throwable t) {

            }
        });

    }


    private void getNotificationAPI() {

        if (spnList.size() > 0)
            spnList.clear();

        Log.d("GET_NOT_SIZE", String.valueOf(spnList.size()));


        mApiService.get_NotificationAdmin(Constant.SCHOOL_ID).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {

                if (response.isSuccessful()) {

                    try {
                        JSONObject object = new JSONObject(new Gson().toJson(response.body()));

                        String status = object.getString("status");

                        if (status.equalsIgnoreCase("Success")) {

                            JSONObject jsonData = object.getJSONObject("data");
                            Log.d("GET_NOT_DATA", jsonData.toString());

                            Iterator<String> key = jsonData.keys();

                            while (key.hasNext()) {

                                String myKey = key.next();
                                Log.d("GET_NOT_KEY", myKey);

                                spnList.add(myKey);
                            }

                            setSpinnerNotification();

                        } else {
                            Log.d("GET_NOT_FAIL", String.valueOf(response.code()));

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    Log.d("GET_NOT_FAILED", String.valueOf(response.code()));
                    Log.d("GET_NOT_FAILED", response.message());

                }
            }

            @Override
            public void onFailure(@NonNull Call<Object> call, @NonNull Throwable t) {

            }
        });
    }


    /*********************************Data Display by Selected to spinner value************************/
    private void callNotificationAPI() {

/*
        if (selectedItem.equals("")) {
            Log.d("GET_API_FAILED", selectedItem + "  Isssssssssss");

        } else {*/

        if (empNotificationList.size() > 0)
            empNotificationList.clear();

        mApiService.get_NotificationAdmin(Constant.SCHOOL_ID).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {

                if (response.isSuccessful()) {

                    try {
                        JSONObject object = new JSONObject(new Gson().toJson(response.body()));

                        String status1 = object.getString("status");

                        if (status1.equalsIgnoreCase("Success")) {

                            JSONObject jsonData = object.getJSONObject("data");
                            Log.d("GET_API_DATA_0", selectedItem);
                            Log.d("GET_API_DATA", jsonData.toString());


                            JSONObject keyData = jsonData.getJSONObject(selectedItem);

                            if (keyData.equals("{}")) {
                                Log.d("GET_API_KEY_DATA_if", keyData.toString());

                            } else {
                                Log.d("GET_API_KEY_DATA_else", keyData.toString());

                                Iterator<String> key = keyData.keys();

                                while (key.hasNext()) {

                                    String myKey = key.next();
                                    Log.d("MY_KEY", myKey);

                                    JSONObject finalData = keyData.getJSONObject(myKey);
                                    Log.d("FINAL_DATA", finalData.toString());
                                    String approver_status = finalData.getString("approver_status");

                                    boolean status = finalData.getBoolean("status");
                                    String notification_attachment = finalData.getString("notification_attachment");
                                    String message = finalData.getString("message");
                                    String send_date = finalData.getString("send_date");
                                    String notification_id = finalData.getString("notification_id");
                                    String added_datetime = finalData.getString("added_datetime");
                                    String added_employee_last_name = finalData.getString("added_employee_last_name");

                                    String added_employee_first_name = finalData.getString("added_employee_first_name");
                                    String send_time = finalData.getString("send_time");
                                    String added_employeeid = finalData.getString("added_employeeid");
                                    String send_to = finalData.getString("send_to");
                                    String title = "";
                                    if (!finalData.isNull("title")) {
                                        title = finalData.getString("title");
                                    }


                                    ArrayList<String> listSend = new ArrayList<>();
                                    switch (selectedItem) {

                                        case "divisions":
                                            listSend.clear();
                                            JSONArray arrayDivision = finalData.getJSONArray("division");
                                            for (int i = 0; i < arrayDivision.length(); i++) {
                                                String divisionData = arrayDivision.getString(i);
                                                listSend.add(divisionData);
                                                Log.i("divisionData***", "" + divisionData);
                                            }

                                            break;

                                        case "Students":
                                            listSend.clear();
                                            JSONArray arrayStudents = finalData.getJSONArray("students");
                                            for (int i = 0; i < arrayStudents.length(); i++) {
                                                JSONObject studentJson = arrayStudents.getJSONObject(i);
                                                String strudentName = studentJson.getString("student_fist_name") +" "
                                                        +studentJson.getString("student_last_name");
                                                listSend.add(strudentName);
                                                Log.i("strudentName***", "" + strudentName);
                                            }

                                            break;

                                        case "departments":
                                            listSend.clear();
                                            JSONArray arrayDeoartment = finalData.getJSONArray("departments");
                                            for (int i = 0; i < arrayDeoartment.length(); i++) {
                                                String departmentData = arrayDeoartment.getString(i);
                                                listSend.add(departmentData);
                                                Log.i("departmentData***", "" + departmentData);
                                            }
                                            break;


                                        case "staffs":

                                            listSend.clear();
                                            JSONArray arrayStaffs = finalData.getJSONArray("staffs");
                                            for (int i = 0; i < arrayStaffs.length(); i++) {
                                                JSONObject staffJson = arrayStaffs.getJSONObject(i);
                                                String employeeName = staffJson.getString("employee_fist_name") +" "
                                                        +staffJson.getString("employee_last_name");
                                                listSend.add(employeeName);
                                                Log.i("employeeName***", "" + employeeName);
                                            }
                                            break;


                                        case "classes":

                                            listSend.clear();
                                            JSONArray arrayClass = finalData.getJSONArray("class");
                                            HashSet<String> hashSet = new HashSet<String>();


                                            for (int i = 0; i < arrayClass.length(); i++) {
                                                JSONObject classJson = arrayClass.getJSONObject(i);
                                                Iterator<String> classKey = classJson.keys();
                                                while (classKey.hasNext()){
                                                    String className = classKey.next();
                                                    Log.i("className*****",""+className);

                                                    listSend.add(className);

                                                    hashSet.addAll(listSend);
                                                    listSend.clear();
                                                    listSend.addAll(hashSet);

                                                }

                                            }

                                            break;

                                    }

                                    String addedDate = convertor.getDateByTZ_SSS(added_datetime);
                                    String addedTime = convertor.getTimeByTZ_SSS(added_datetime);

                                    if (empNotificationList.size() > 0) {

                                        boolean b = true;

                                        for (int i = 0; i < empNotificationList.size(); i++) {

                                            if (empNotificationList.get(i).getApplyDate().equals(addedDate)) {
                                                b = false;

                                                empNotificationList.get(i).getNotificationList()
                                                        .add(new NotificationModel(status, notification_id,
                                                                title, message, addedDate, addedTime,
                                                                notification_attachment, send_to, send_date,
                                                                send_time, approver_status,
                                                                added_employee_first_name, added_employee_last_name, listSend));
                                            }
                                        }

                                        if (b) {
                                            ArrayList<NotificationModel> myList = new ArrayList<>();
                                            myList.add(new NotificationModel(status, notification_id, title,
                                                    message, addedDate, addedTime, notification_attachment,
                                                    send_to, send_date, send_time, approver_status,
                                                    added_employee_first_name, added_employee_last_name, listSend));

                                            empNotificationList.add(new EmpNotificationModel(addedDate, myList));
                                        }

                                    } else {

                                        ArrayList<NotificationModel> myList = new ArrayList<>();
                                        myList.add(new NotificationModel(status, notification_id, title,
                                                message, addedDate, addedTime, notification_attachment,
                                                send_to, send_date, send_time, approver_status,
                                                added_employee_first_name, added_employee_last_name, listSend));

                                        empNotificationList.add(new EmpNotificationModel(addedDate, myList));
                                    }

                                }
                                adapter.notifyDataSetChanged();
                            }

                        } else {
                            Log.d("GET_API_FAIL", String.valueOf(response.code()));

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    Log.d("GET_API_FAILED", String.valueOf(response.code()));
                    Log.d("GET_API_FAILED", response.message());

                }
            }

            @Override
            public void onFailure(@NonNull Call<Object> call, @NonNull Throwable t) {

            }
        });

        // }

    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.fab_button:

                NotificationActivity notificationActivity = (NotificationActivity) getActivity();
                assert notificationActivity != null;
                notificationActivity.loadFragment(Constant.NOTIFICATION_CREATE_FRAGMENT, null);
        }
    }

}
