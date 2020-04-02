package in.varadhismartek.patashalaerp.DashboardModule.NoticeBoard;


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

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import in.varadhismartek.Utils.Constant;
import in.varadhismartek.patashalaerp.GeneralClass.CircleImageView;
import in.varadhismartek.patashalaerp.R;
import in.varadhismartek.patashalaerp.Retrofit.APIService;
import in.varadhismartek.patashalaerp.Retrofit.ApiUtilsPatashala;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class NoticeBoardCreateFragment extends Fragment implements View.OnClickListener {

    private ImageView ivBack;
    private Button bt_submit;
    private CircleImageView civ_image;
    private TextView tv_name, tv_id, tv_date;
    private EditText et_title, et_message;

    String date = "";
    APIService mApiServicePatashala;

    public NoticeBoardCreateFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notice_board_create, container, false);

        initViews(view);
        initListeners();
        getCurrentDate();
        setStrings();

        return view;
    }

    private void getCurrentDate() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd_MM-yyyy");
        Calendar calendar = Calendar.getInstance();
        date = simpleDateFormat.format(calendar.getTime());

    }

    private void setStrings() {

        tv_name.setText(Constant.POC_NAME);
        tv_date.setText(date);
        tv_id.setText(Constant.EMPLOYEE_BY_ID);
    }


    private void initListeners() {

        ivBack.setOnClickListener(this);
        bt_submit.setOnClickListener(this);
    }

    private void initViews(View view) {

        mApiServicePatashala = ApiUtilsPatashala.getService();

        ivBack = view.findViewById(R.id.ivBack);
        bt_submit = view.findViewById(R.id.bt_submit);
        civ_image = view.findViewById(R.id.civ_image);
        tv_name = view.findViewById(R.id.tv_name);
        tv_id = view.findViewById(R.id.tv_id);
        tv_date = view.findViewById(R.id.tv_date);
        et_title = view.findViewById(R.id.et_title);
        et_message = view.findViewById(R.id.et_message);
    }

    @Override
    public void onClick(View view) {

        assert getActivity()!=null;

        switch (view.getId()){

            case R.id.ivBack:
                getActivity().onBackPressed();
                break;

            case R.id.bt_submit:

                String title = et_title.getText().toString();
                String message = et_message.getText().toString();

                if (title.equals("")){
                    Toast.makeText(getContext(), "Title Is Required", Toast.LENGTH_SHORT).show();

                }else if (message.equals("")){
                    Toast.makeText(getContext(), "Message Is Required", Toast.LENGTH_SHORT).show();

                }else {

                    addNoticeAPI(title,message);
                }

                break;
        }
    }

    private void addNoticeAPI(String title, String message) {

        mApiServicePatashala.addNotice(Constant.SCHOOL_ID,title,message,Constant.EMPLOYEE_BY_ID).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {

                if (response.isSuccessful()){

                    try {
                        JSONObject object = new JSONObject(new Gson().toJson(response.body()));
                        String message = object.getString("message");
                        String status = object.getString("status");

                        if (status.equalsIgnoreCase("Success")) {
                            Log.d("NOTICE_create_Success", message);
                            Log.d("NOTICE_create_Success", String.valueOf(response.code()));
                            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();

                            getActivity().onBackPressed();

                        } else {
                            Log.d("NOTICE_create_else", message);
                            Log.d("NOTICE_create_else", String.valueOf(response.code()));
                            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();

                        }

                    }catch (Exception e){

                        e.printStackTrace();
                    }

                }else {
                    try {
                        assert response.errorBody()!=null;
                        JSONObject object = new JSONObject(response.errorBody().string());
                        String message = object.getString("message");
                        Log.d("NOTICE_create_API", message);
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
