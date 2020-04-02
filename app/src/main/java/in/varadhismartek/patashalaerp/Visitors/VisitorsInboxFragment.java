package in.varadhismartek.patashalaerp.Visitors;


import android.app.DatePickerDialog;
import android.app.ProgressDialog;
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
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;


import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

import in.varadhismartek.Utils.Constant;
import in.varadhismartek.patashalaerp.R;
import in.varadhismartek.patashalaerp.Retrofit.APIService;
import in.varadhismartek.patashalaerp.Retrofit.ApiUtilsPatashala;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class VisitorsInboxFragment extends Fragment implements View.OnClickListener {

    private ImageView img_back;
    private FloatingActionButton Fab_Button;
    private RecyclerView recyclerView;
    private VisitorsAdapter adapter;
    private ArrayList<VisitorModel> visitorList;
    TextView tv_date;

    private APIService mAPIservicePatashala;
    private ProgressDialog progressDialog;

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    int year, month, day;

    public VisitorsInboxFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_visitiors_inbox, container, false);

        initViews(view);
        initListeners();
        getCurrentDate();
        getSchoolVisitorAPI();


        return view;
    }

    private void getCurrentDate() {

        Calendar calendar = Calendar.getInstance();

        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

    }

    private void setRecyclerView() {

        adapter = new VisitorsAdapter(visitorList, getActivity(), Constant.RV_VISITORS_INBOX_ROW);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
    }

    private void initListeners() {

        img_back.setOnClickListener(this);
        Fab_Button.setOnClickListener(this);
        tv_date.setOnClickListener(this);
    }

    private void initViews(View view) {

        mAPIservicePatashala = ApiUtilsPatashala.getService();
        progressDialog = new ProgressDialog(getActivity());

        img_back = view.findViewById(R.id.img_back);
        Fab_Button = view.findViewById(R.id.Fab_Button);
        recyclerView = view.findViewById(R.id.recyclerView);
        tv_date = view.findViewById(R.id.tv_date);

        visitorList = new ArrayList<>();
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.img_back:
                getActivity().onBackPressed();
                break;

            case R.id.Fab_Button:

                VisitiorsActivity visitiorsActivity = (VisitiorsActivity) getActivity();
                visitiorsActivity.loadFragment(Constant.FRAGMENT_ADD_VISITIORS, null);

                break;

            case R.id.tv_date:

                DatePickerDialog dialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

                        Calendar calendar = Calendar.getInstance();
                        calendar.set(Calendar.YEAR, i);
                        calendar.set(Calendar.MONTH, i1);
                        calendar.set(Calendar.DAY_OF_MONTH, i2);

                        String date = dateFormat.format(calendar.getTime());

                        tv_date.setText(date);

                        //getSchoolVisitorCountAPI(date);
                    }
                }, year, month, day);

                dialog.show();

                break;
        }
    }

    private void getSchoolVisitorAPI(){

        progressDialog.show();

        Log.d("visitior_input", Constant.SCHOOL_ID);

        mAPIservicePatashala.getSchoolVisitor(Constant.SCHOOL_ID).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {

                if (visitorList.size()>0)
                    visitorList.clear();

                if (response.isSuccessful()){

                    try{
                        JSONObject object = new JSONObject(new Gson().toJson(response.body()));
                        String message = object.getString("message");
                        String status1 = object.getString("status");

                        if (status1.equalsIgnoreCase("Success")){
                            Log.d("visitior_success", response.code() + " " + message+" "+response.body());

                            JSONObject jsonData = object.getJSONObject("data");

                            Iterator<String> key = jsonData.keys();

                            while (key.hasNext()){

                                JSONObject keyData = jsonData.getJSONObject(key.next());

                                String entry_time = "", added_employeeid = "", visitor_id = "", entry_date = "", added_by_first_name = ""
                                        , purpose = "", visitor_uuid = "", staff_id = "", added_datetime = "", visitor_name = ""
                                        , visitor_photo = "", added_by_last_name = "";

                                entry_time = keyData.getString("entry_time");
                                added_employeeid = keyData.getString("added_employeeid");
                                visitor_id = keyData.getString("visitor_id");
                                entry_date = keyData.getString("entry_date");
                                added_by_first_name = keyData.getString("added_by_first_name");
                                purpose = keyData.getString("purpose");
                                visitor_uuid = keyData.getString("visitor_uuid");
                                staff_id = keyData.getString("staff_id");
                                added_datetime = keyData.getString("added_datetime");
                                visitor_name = keyData.getString("visitor_name");
                                visitor_photo = keyData.getString("visitor_photo");
                                added_by_last_name = keyData.getString("added_by_last_name");

                                visitorList.add(new VisitorModel(entry_time, added_employeeid, visitor_id, entry_date, added_by_first_name
                                        ,purpose, visitor_uuid, staff_id, added_datetime, visitor_name, visitor_photo, added_by_last_name ));

                            }

                            progressDialog.dismiss();
                            setRecyclerView();

                            Log.d("visitior_success_list", response.code() + " list " + visitorList.size());

                        }else {
                            Log.d("visitior_fail", response.code() + " " + message);
                            progressDialog.dismiss();
                            setRecyclerView();
                        }

                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }else {
                    try {
                        assert response.errorBody()!=null;
                        JSONObject object = new JSONObject(response.errorBody().string());
                        String message = object.getString("message");
                        Log.d("visitior_fail", message);
                        progressDialog.dismiss();
                        setRecyclerView();

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
