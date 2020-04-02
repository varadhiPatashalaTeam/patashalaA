package in.varadhismartek.patashalaerp.DashboardModule.NoticeBoard;


import android.app.Dialog;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONObject;

import in.varadhismartek.Utils.Constant;
import in.varadhismartek.patashalaerp.R;
import in.varadhismartek.patashalaerp.Retrofit.APIService;
import in.varadhismartek.patashalaerp.Retrofit.ApiUtilsPatashala;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NoticeBoardViewFragment extends Fragment implements View.OnClickListener {

    private ImageView ivBack;
    private TextView tv_name, tv_date, tv_time, tv_title, tv_message;
    private Button btn_delete, bt_update;
    private NoticeBoardModel noticeBoardModel;
    private APIService mApiServicePatashala;

    public NoticeBoardViewFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notice_board_view, container, false);

        initViews(view);
        initListeners();
        getBundles();

        return view;
    }

    private void getBundles() {

        Bundle bundle = getArguments();

        assert bundle != null;
        noticeBoardModel = (NoticeBoardModel) bundle.getSerializable("list");

        assert noticeBoardModel != null;
        Log.d("LiSTlsndn", noticeBoardModel.getNoticeID());
        setStrings();

    }

    private void setStrings() {

        tv_name.setText(noticeBoardModel.getFirstName());
        tv_date.setText(noticeBoardModel.getAddedDate());
        tv_time.setText(noticeBoardModel.getAddedTime());
        tv_title.setText(noticeBoardModel.getTitle());
        tv_message.setText(noticeBoardModel.getMessage());
    }

    private void initListeners(){

        ivBack.setOnClickListener(this);
        btn_delete.setOnClickListener(this);
        bt_update.setOnClickListener(this);
    }

    private void initViews(View view){

        mApiServicePatashala = ApiUtilsPatashala.getService();
        ivBack = view.findViewById(R.id.ivBack);
        tv_name = view.findViewById(R.id.tv_name);
        tv_date = view.findViewById(R.id.tv_date);
        tv_time = view.findViewById(R.id.tv_time);
        tv_title = view.findViewById(R.id.tv_title);
        tv_message = view.findViewById(R.id.tv_message);
        btn_delete = view.findViewById(R.id.btn_delete);
        bt_update = view.findViewById(R.id.bt_update);

    }

    @Override
    public void onClick(View view) {

        assert getActivity()!=null;

        switch (view.getId()){

            case R.id.ivBack:
                getActivity().onBackPressed();
                break;

            case R.id.btn_delete:
                openMyDialod("Delete");
                break;

            case R.id.bt_update:
                openMyDialod("Update");
                break;
        }
    }

    private void openMyDialod(String tag) {

        assert getActivity()!=null;

        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.dialog_notice_board);
        //noinspection ConstantConditions
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.flag_transparent);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();

        TextView tv_dialog_title = dialog.findViewById(R.id.tv_dialog_title);
        LinearLayout ll_delete = dialog.findViewById(R.id.ll_delete);
        LinearLayout ll_update = dialog.findViewById(R.id.ll_update);
        final EditText et_title = dialog.findViewById(R.id.et_title);
        final EditText et_message = dialog.findViewById(R.id.et_message);
        Button btn_no = dialog.findViewById(R.id.btn_no);
        Button bt_yes = dialog.findViewById(R.id.bt_yes);
        Button bt_cancel = dialog.findViewById(R.id.bt_cancel);
        Button bt_update = dialog.findViewById(R.id.bt_update);

        tv_dialog_title.setText(tag);

        if(tag.equalsIgnoreCase("Delete")){
            ll_delete.setVisibility(View.VISIBLE);
            ll_update.setVisibility(View.GONE);

        }else if(tag.equalsIgnoreCase("Update")){
            ll_delete.setVisibility(View.GONE);
            ll_update.setVisibility(View.VISIBLE);

        }

        btn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        bt_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // delete notice api
                Log.d("NOTICE_DELETE_INPUT", Constant.SCHOOL_ID + " " + noticeBoardModel.getAddedID() + " " + noticeBoardModel.getNoticeID());

                mApiServicePatashala.deleteNotice(Constant.SCHOOL_ID, noticeBoardModel.getNoticeID(),
                        noticeBoardModel.getAddedID()).enqueue(new Callback<Object>() {
                    @Override
                    public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {

                        if (response.isSuccessful()) {

                            try {
                                JSONObject object = new JSONObject(new Gson().toJson(response.body()));
                                String message = object.getString("message");
                                String status = object.getString("status");

                                if (status.equalsIgnoreCase("Success") || response.code() == 200) {
                                    Log.d("NOTICE_DELETE_SU", message);
                                    Log.d("NOTICE_DELETE_SU", String.valueOf(response.code()));
                                    Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();

                                    dialog.dismiss();
                                    assert getActivity() != null;
                                    getActivity().onBackPressed();

                                } else {
                                    Log.d("NOTICE_DELETE_ELSE", message);
                                    Log.d("NOTICE_DELETE_ELSE", String.valueOf(response.code()));
                                    Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();

                                }

                            } catch (Exception e) {

                                e.printStackTrace();
                            }


                        } else {
                            try {
                                assert response.errorBody() != null;
                                JSONObject object = new JSONObject(response.errorBody().string());
                                String message = object.getString("message");
                                Log.d("NOTICE_DELETE_FAIL", message);
                                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                    }

                    @Override
                    public void onFailure(@NonNull Call<Object> call, @NonNull Throwable t) {

                    }
                });
            }
        });

        bt_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (et_title.getText().toString().equals(""))
                    Toast.makeText(NoticeBoardViewFragment.this.getActivity(), "Title is required", Toast.LENGTH_SHORT).show();

                else if (et_message.getText().toString().equals(""))
                    Toast.makeText(NoticeBoardViewFragment.this.getActivity(), "Message is required", Toast.LENGTH_SHORT).show();

                else {

                    // update notice api
                    mApiServicePatashala.updateNotice(Constant.SCHOOL_ID, noticeBoardModel.getNoticeID(), et_message.getText().toString(),
                            et_title.getText().toString(), noticeBoardModel.getAddedID()).enqueue(new Callback<Object>() {
                        @Override
                        public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {

                            if (response.isSuccessful()) {

                                try {
                                    JSONObject object = new JSONObject(new Gson().toJson(response.body()));
                                    String message = object.getString("message");
                                    String status = object.getString("status");

                                    if (status.equalsIgnoreCase("Success") || response.code() == 200) {
                                        Log.d("NOTICE_UPDATE_SU", message);
                                        Log.d("NOTICE_UPDATE_SU", String.valueOf(response.code()));
                                        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();

                                        dialog.dismiss();
                                        assert getActivity() != null;
                                        getActivity().onBackPressed();

                                    } else {
                                        Log.d("NOTICE_UPDATE_ELSE", message);
                                        Log.d("NOTICE_UPDATE_ELSE", String.valueOf(response.code()));
                                        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();

                                    }

                                } catch (Exception e) {

                                    e.printStackTrace();
                                }

                            } else {
                                try {
                                    assert response.errorBody() != null;
                                    JSONObject object = new JSONObject(response.errorBody().string());
                                    String message = object.getString("message");
                                    Log.d("NOTICE_UPDATE_FAIL", message);
                                    Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                                } catch (Exception e) {
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
        });

    }
}
