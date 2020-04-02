package in.varadhismartek.patashalaerp.DashboardModule.NoticeBoard;


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
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

import in.varadhismartek.Utils.Constant;
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
public class NoticeBoardFragment extends Fragment implements View.OnClickListener {

    ImageView iv_backBtn;
    RecyclerView recycler_view;
    ArrayList<NoticeBoardModel> noticeList;
    FloatingActionButton fab;

    APIService mApiServicePatashala;
    NoticeBoardAdapter adapter;
    DateConvertor convertor;

    public NoticeBoardFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notice_board, container, false);

        initViews(view);
        initListeners();
        setRecyclerView();
        getNoticeBoardAPI();

        return view;
    }

    private void setRecyclerView() {

        adapter = new NoticeBoardAdapter(noticeList, getActivity(), Constant.RV_NOTICEBOARD_ROW);
        recycler_view.setLayoutManager(new LinearLayoutManager(getActivity()));
        recycler_view.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

    private void initListeners() {
        iv_backBtn.setOnClickListener(this);
        fab.setOnClickListener(this);
    }

    private void initViews(View view) {

        mApiServicePatashala = ApiUtilsPatashala.getService();
        convertor = new DateConvertor();

        iv_backBtn = view.findViewById(R.id.iv_backBtn);
        recycler_view = view.findViewById(R.id.recycler_view);
        fab = view.findViewById(R.id.fab);

        noticeList = new ArrayList<>();
    }

    @Override
    public void onClick(View view) {

        assert getActivity()!=null;
        NoticeBoardActivity noticeBoardActivity = (NoticeBoardActivity) getActivity();

        switch (view.getId()){

            case R.id.iv_backBtn:
                getActivity().onBackPressed();
                break;

            case R.id.fab:
                noticeBoardActivity.loadFragment(Constant.NOTICEBOARD_CREATE_FRAGMENT, null);
                break;
        }
    }

    private void getNoticeBoardAPI(){

        Log.d("NOTICE_API_CALL", "CALLED");


        mApiServicePatashala.getNotice(Constant.SCHOOL_ID).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {

                if (response.isSuccessful()){
                    JSONObject object = null;
                    try {
                        object = new JSONObject(new Gson().toJson(response.body()));
                        String message = object.getString("message");
                        String status = object.getString("status");

                        if (status.equalsIgnoreCase("Success")){
                            Log.d("NOTICE_API_Success", message);
                            Log.d("NOTICE_API_Success", String.valueOf(response.code()));
                           // Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();

                            JSONObject jsonData = object.getJSONObject("data");

                            Iterator<String> key = jsonData.keys();

                            while (key.hasNext()){

                                String myKey = key.next();
                                Log.d("NOTICE_API_Key", myKey);

                                JSONObject keyData = jsonData.getJSONObject(myKey);

                                String notice_id = keyData.getString("notice_id");
                                String added_by_employee_firstname = keyData.getString("added_by_employee_firstname");
                                String notice_title = keyData.getString("notice_title");
                                String added_by = keyData.getString("added_by");
                                String notice_message = keyData.getString("notice_message");
                                String added_datetime = keyData.getString("added_datetime");
                                String added_by_employee_lastname = keyData.getString("added_by_employee_lastname");

                                Log.d("NOTICE_API_DATA",notice_id);

                                String addedDate = convertor.getDateByTZ_SSS(added_datetime);
                                String addedTime = convertor.getTimeByTZ_SSS(added_datetime);

                                noticeList.add(new NoticeBoardModel(notice_id,added_by_employee_firstname,notice_title,
                                        added_by, notice_message,addedDate,addedTime,added_by_employee_lastname));
                            }

                            adapter.notifyDataSetChanged();

                        }else {
                            Log.d("NOTICE_API_else", message);
                            Log.d("NOTICE_API_else", String.valueOf(response.code()));
                            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }else {
                    try {
                        assert response.errorBody()!=null;
                        JSONObject object = new JSONObject(response.errorBody().string());
                        String message = object.getString("message");
                        Log.d("NOTICE_API", message);
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
