package in.varadhismartek.patashalaerp.TicketRaise;


import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.graphics.Color;
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
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;


import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import in.varadhismartek.Utils.Constant;
import in.varadhismartek.patashalaerp.GeneralClass.CustomSpinnerAdapter;
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
public class StaffInboxTicketFragment extends Fragment implements View.OnClickListener, DatePickerDialog.OnDateSetListener {

    private RecyclerView rv_TicketInbox;
    private List<StaffTicketModel> ticket_List;
    private Spinner sp_status;
    private ImageView imgSearch,img_back;
    private FloatingActionButton fabButton;
    private TextView tv_final_date;
    private LinearLayout ll_no_record;

    private Calendar yesterday= Calendar.getInstance();
    private Calendar tommorrow= Calendar.getInstance();

    private Calendar calendar= Calendar.getInstance();
    private Calendar current= Calendar.getInstance();
    private SimpleDateFormat mdformat = new SimpleDateFormat("dd/MM/yyyy ");

    private APIService mApiServicePatashala;
    private DateConvertor convertor;
    private StaffTicketAdapter rvInboxAdapter;

    private ProgressDialog progressDialog;

    private String str_tktType = "";
    private String MY_TAG = "tickets_list";

    public StaffInboxTicketFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_inbox_ticket, container, false);

        initViews(view);
        initListeners();
        initRecyclerviewTicketInbox();
        tommorrow.add(Calendar.DAY_OF_MONTH,1);
        yesterday.add(Calendar.DAY_OF_MONTH,-1);
        getTicketAPI();

        return view;

    }



    private void initRecyclerviewTicketInbox() {

        rvInboxAdapter = new StaffTicketAdapter(getContext(),ticket_List, Constant.RV_TICKET_STAFF_INBOX);
        rv_TicketInbox.setLayoutManager(new LinearLayoutManager(getContext()));
        rv_TicketInbox.setAdapter(rvInboxAdapter);
    }


    private void initListeners() {

        img_back.setOnClickListener(this);

    }

    private void initViews(View view) {

        mApiServicePatashala = ApiUtilsPatashala.getService();
        convertor = new DateConvertor();
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Please Wait...");

        rv_TicketInbox = view.findViewById(R.id.rv_Ticket_Inbox);
        fabButton = view.findViewById(R.id.Fab_Button);
        fabButton = view.findViewById(R.id.Fab_Button);
        img_back =view.findViewById(R.id.img_back);
        ll_no_record =view.findViewById(R.id.ll_no_record);

        ticket_List = new ArrayList<>();


    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){


            case R.id.img_back:
                try {
                    int count = getFragmentManager().getBackStackEntryCount();

                    if (count == 0) {
                        super.getActivity().onBackPressed();
                        //additional code
                    } else {
                        getFragmentManager().popBackStack();
                    }

                }catch (NullPointerException e)
                {e.printStackTrace();}
                break;



        }

    }
    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

        tv_final_date.setText(mdformat.format(datePicker.getDayOfMonth()+"/"+(datePicker.getMonth()+1)+"/"+datePicker.getYear()));
        //tv_final_date.setText(datePicker.getDayOfMonth()+"/"+(datePicker.getMonth()+1)+"/"+datePicker.getYear());

    }
    public void setDate(Calendar c){
        Log.d("current",c+"");
        String strDate =   mdformat.format(c.getTime());
        tv_final_date.setText(strDate);
    }


    private void getTicketAPI(){

        progressDialog.show();

        Log.d("My_inputs", str_tktType+" "+MY_TAG);

        mApiServicePatashala.getEmployeeTicketList(Constant.SCHOOL_ID).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {

                if (ticket_List.size()>0)
                    ticket_List.clear();

                if (response.isSuccessful()){

                    try {
                        JSONObject object = new JSONObject(new Gson().toJson(response.body()));

                        String status1 = object.getString("status");
                        String message1 = object.getString("message");

                        if (status1.equalsIgnoreCase("Success")){

                            Log.d("Ticket_inbox_succ", response.code()+" "+message1+object.toString());

                            JSONObject jsonData = object.getJSONObject("data");

                            JSONObject ticketData = jsonData.getJSONObject("tickets_list");

                                Iterator<String> key = ticketData.keys();

                                while (key.hasNext()){

                                    JSONObject keyData = ticketData.getJSONObject(key.next());

                                    String updated_datetime = "", ticket_datetime = "",ticket_number = "",ticket_subject = "",
                                            ticket_department = "",issue_title = "",ticket_priority = "",ticket_id = "",
                                            ticket_level = "",ticket_type = "",ticket_status = "",empCustomId="";

                                    ticket_datetime = keyData.getString("ticket_datetime");
                                    ticket_number = keyData.getString("ticket_number");
                                    ticket_department = keyData.getString("ticket_department");
                                    issue_title = keyData.getString("issue_title");
                                    ticket_priority = keyData.getString("ticket_priority");
                                    ticket_id = keyData.getString("ticket_id");
                                    ticket_level = keyData.getString("ticket_level");
                                    ticket_type = keyData.getString("ticket_type");
                                    ticket_status = keyData.getString("ticket_status");
                                    empCustomId = keyData.getString("employee_custom_employee_id");

                                    if (!keyData.isNull("updated_datetime")){
                                        updated_datetime = keyData.getString("updated_datetime");

                                    }if (!keyData.isNull("ticket_subject")){
                                        ticket_subject = keyData.getString("ticket_subject");

                                    }

                                    String addedDate = convertor.getDateByTZ_SSS(ticket_datetime);

                                    ticket_List.add(new StaffTicketModel(updated_datetime, ticket_datetime,ticket_number,ticket_subject,
                                            ticket_department,issue_title,ticket_priority,ticket_id, ticket_level,ticket_type, ticket_status,empCustomId,"" ));

                            }

                            rvInboxAdapter.notifyDataSetChanged();
                            progressDialog.dismiss();

                            if (ticket_List.size() == 0)
                                ll_no_record.setVisibility(View.VISIBLE);
                            else
                                ll_no_record.setVisibility(View.GONE);

                        }else {
                            Log.d("Ticket_inbox_else", response.code()+" "+message1);
                            rvInboxAdapter.notifyDataSetChanged();
                            progressDialog.dismiss();

                            if (ticket_List.size() == 0)
                                ll_no_record.setVisibility(View.VISIBLE);
                            else
                                ll_no_record.setVisibility(View.GONE);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }else {
                    try {
                        assert response.errorBody()!=null;
                        JSONObject object = new JSONObject(response.errorBody().string());
                        String message = object.getString("message");
                        Log.d("Ticket_inbox_fail", message+" "+response.code());
                        rvInboxAdapter.notifyDataSetChanged();
                        progressDialog.dismiss();

                        if (ticket_List.size() == 0)
                            ll_no_record.setVisibility(View.VISIBLE);
                        else
                            ll_no_record.setVisibility(View.GONE);

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
