package in.varadhismartek.patashalaerp.TicketRaise;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import in.varadhismartek.Utils.Constant;
import in.varadhismartek.patashalaerp.GeneralClass.CircleImageView;
import in.varadhismartek.patashalaerp.GeneralClass.CustomSpinnerAdapter;
import in.varadhismartek.patashalaerp.GeneralClass.DateConvertor;
import in.varadhismartek.patashalaerp.R;
import in.varadhismartek.patashalaerp.Retrofit.APIService;
import in.varadhismartek.patashalaerp.Retrofit.ApiUtilsPatashala;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StaffViewTicketFragment extends Fragment implements View.OnClickListener {

    public static String SEARCH_ID = "";
    private RecyclerView rvDetails,attach_rv;
    private LinearLayout ll_bottom,ll_escalate,ll_escalateBtn, ll_buttons;
    private ImageView attach_btn,img_back;
    private CircleImageView profile_pic;
    private ArrayList<StaffTicketModel> chatList;

    private TextView tv_EscalateTicket,tv_close_ticket,tv_send,tv_cancel;
    private TextView tv_name,tv_empId,tv_dept,tv_date;
    private Spinner sp_Select_Level;
    private EditText ed_reply;
    private ImageView iv_send;
    private StaffTicketAdapter attachAdapter;

    private APIService mApiServicePatashala;
    private String ticketID = "",empCustomId = "";
    private StaffTicketAdapter ticketAdapter;
    private DateConvertor convertor;
    private TextView tv_ticket_id, tv_priority, tv_status_level, tv_issueType, tv_ticketType, tv_ticketTitle, tv_solved, tv_search;
    private static final int PICK_BY_CAMERA = 3;
    private static final int PICK_IMAGE_MULTIPLE = 2;
    private static final int RESULT_OK = -1;
    private ArrayList<Uri> mArrayUri;
    private ProgressDialog progressDialog;

    private String issue_title;

    private StaffTicketAdapter searchAdapter;
    private ArrayList<StaffTicketModel> searchList;

    View v;

    public StaffViewTicketFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_view_ticket, container, false);
        Constant.CurrentFragment= Constant.FRAGMENT_VIEWTICKET;

        initView(v);
        initListeners();
        getBundles();
        initSpinnerSelectLevel();
        initRecyclerViewForChat();
        initRecyclerViewAttachment();
        getTicketActivityAPI();

        return v;
    }

    private void initView(View v) {

        mApiServicePatashala = ApiUtilsPatashala.getService();
        convertor = new DateConvertor();
        progressDialog = new ProgressDialog(getActivity());

        img_back = v.findViewById(R.id.img_back);
        ed_reply = v.findViewById(R.id.ed_reply);
        iv_send = v.findViewById(R.id.iv_send);
        tv_send  = v.findViewById(R.id.tv_send_escelated);
        tv_cancel = v.findViewById(R.id.tv_cancel_escelate);
        attach_rv  = v.findViewById(R.id.rv_attach);
        profile_pic = v.findViewById(R.id.circleImageView);
        tv_name     = v.findViewById(R.id.tv_Name);
        tv_empId   = v.findViewById(R.id.tv_EmpId);
        tv_date    = v.findViewById(R.id.tv_Date);
        tv_dept     = v.findViewById(R.id.tv_Department);
        rvDetails     = v.findViewById(R.id.rv_details);
        sp_Select_Level = v.findViewById(R.id.sp_SelectLevel);
        tv_EscalateTicket = v.findViewById(R.id.tv_Escalate_Ticket);
        tv_close_ticket  = v.findViewById(R.id.close_ticket);
        attach_btn     = v.findViewById(R.id.attach_btn);
        tv_search     = v.findViewById(R.id.tv_search);

        ll_bottom  = v.findViewById(R.id.ll_bottom);
        ll_escalate  = v.findViewById(R.id.ll_escalate);
        ll_escalateBtn = v.findViewById(R.id.ll_escalateBtn);
        ll_buttons   = v.findViewById(R.id.ll_buttons);

        tv_ticket_id = v.findViewById(R.id.tv_ticket_id);
        tv_priority    = v.findViewById(R.id.tv_priority);
        tv_status_level = v.findViewById(R.id.tv_status_level);
        tv_issueType  = v.findViewById(R.id.tv_issueType);
        tv_ticketType = v.findViewById(R.id.tv_ticketType);
        tv_ticketTitle = v.findViewById(R.id.tv_ticketTitle);
        tv_solved = v.findViewById(R.id.tv_solved);

        chatList = new ArrayList<>();
        mArrayUri = new ArrayList<>();
        searchList = new ArrayList<>();
    }

    private void initListeners() {
        tv_EscalateTicket.setOnClickListener(this);
        tv_close_ticket.setOnClickListener(this);
        attach_btn.setOnClickListener(this);
        tv_cancel.setOnClickListener(this);
        tv_send.setOnClickListener(this);
        iv_send.setOnClickListener(this);
        img_back.setOnClickListener(this);
        tv_search.setOnClickListener(this);
    }

    private void getBundles() {
        Bundle bundle = getArguments();
        assert bundle!=null;
        ticketID = bundle.getString("ticket_id");
        empCustomId = bundle.getString("empCustomId");

        Constant.EMPLOYEE_EMP_CUSTOM_ID = empCustomId;
    }

    private void initSpinnerSelectLevel() {

        ArrayList<String> arrayList_sp_selectLevel = new ArrayList<>();

        arrayList_sp_selectLevel.add("Select Level");
        arrayList_sp_selectLevel.add("1");
        arrayList_sp_selectLevel.add("2");
        arrayList_sp_selectLevel.add("3");

        CustomSpinnerAdapter customSpinnerAdapter = new CustomSpinnerAdapter(getContext(),arrayList_sp_selectLevel);
        sp_Select_Level.setAdapter(customSpinnerAdapter);
        customSpinnerAdapter.notifyDataSetChanged();
    }

    private void initRecyclerViewForChat() {
        ticketAdapter = new StaffTicketAdapter(getActivity(), chatList, Constant.RV_TICKET_VIEW_CHAT_ROW);
        rvDetails.setLayoutManager(new LinearLayoutManager(getContext()));
        rvDetails.setAdapter(ticketAdapter);
        ticketAdapter.notifyDataSetChanged();

    }

    private void initRecyclerViewAttachment() {

        attachAdapter = new StaffTicketAdapter(mArrayUri, getActivity(), Constant.RV_TICKET_REPLY_ATTACHMENT);
        attach_rv.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        attach_rv.setAdapter(attachAdapter);
        attachAdapter.notifyDataSetChanged();

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.iv_send:
                sendTicketReplyAPI();

                break;
            case R.id.close_ticket:
                closeTicketIssuesAPI();

                break;

            case R.id.tv_Escalate_Ticket:

                ll_escalate.setVisibility(View.VISIBLE);
                ll_escalateBtn.setVisibility(View.VISIBLE);
                ll_buttons.setVisibility(View.GONE);

                break;

            case R.id.tv_cancel_escelate:

                ll_escalate.setVisibility(View.GONE);
                ll_escalateBtn.setVisibility(View.GONE);
                ll_buttons.setVisibility(View.VISIBLE);

                break;

            case R.id.tv_send_escelated:

                escalateTicketIssuesAPI();

                break;

            case R.id.attach_btn:
                setImagesInDialog();

                break;

            case R.id.img_back:
                getActivity().onBackPressed();
                break;

            case R.id.tv_search:

                getLevelEmployeeAPI();

                break;

        }
    }

    private void setImagesInDialog() {

        final Dialog dialog = new Dialog(getActivity());

        dialog.setContentView(R.layout.dialog_camera_gallery);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.flag_transparent);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();

        TextView tv_date = dialog.findViewById(R.id.tv_date);
        EditText et_gallery_title = dialog.findViewById(R.id.et_gallery_title);
        TextView tv_add = dialog.findViewById(R.id.tv_add);

        LinearLayout camera = dialog.findViewById(R.id.ll_camera);
        LinearLayout gallery = dialog.findViewById(R.id.ll_gallery);

        tv_date.setVisibility(View.GONE);
        et_gallery_title.setVisibility(View.GONE);
        tv_add.setVisibility(View.GONE);

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(getActivity(), "Camera Selected...", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                openCamera();
            }
        });

        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(getActivity(), "Gallery Selected...", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                openGallery();
            }
        });
    }

    private void openCamera() {

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, PICK_BY_CAMERA);
    }

    private void openGallery() {


        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_MULTIPLE);

        if (mArrayUri != null) {
            mArrayUri.clear();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            // When an Image is picked
            if (requestCode == PICK_IMAGE_MULTIPLE && resultCode == RESULT_OK
                    && null != data) {

                if (data.getData() != null) {

                    Uri mImageUri = data.getData();

                    mArrayUri.add(mImageUri);

                    attachAdapter.notifyDataSetChanged();

                } else {
                    if (data.getClipData() != null) {
                        ClipData mClipData = data.getClipData();


                        if (mClipData.getItemCount() <= 8) {
                            for (int i = 0; i < mClipData.getItemCount(); i++) {

                                ClipData.Item item = mClipData.getItemAt(i);
                                Uri uri = item.getUri();

                                mArrayUri.add(uri);

                               /* Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                                String path = getRealPathFromURI(getImageUri(getActivity(), bitmap));
                                imageFilePathList.add(path);*/

                            }

                            attachAdapter.notifyDataSetChanged();

                        } else {
                            Toast.makeText(getActivity(), "You can't select images more than 8", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            } else {
                Toast.makeText(getActivity(), "You haven't picked Image",
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_LONG)
                    .show();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    private void getTicketActivityAPI(){

        Log.d("GET_TICKET_ELSE", Constant.SCHOOL_ID+" "+ticketID);

        progressDialog.show();

        mApiServicePatashala.getTicketActivity(Constant.SCHOOL_ID, ticketID).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {

                if (chatList.size()>0)
                    chatList.clear();

                if (response.isSuccessful()){

                    try {
                        JSONObject object = new JSONObject(new Gson().toJson(response.body()));
                        String status1= object.getString("status");
                        String message1= object.getString("message");

                        if (status1.equalsIgnoreCase("Success")){

                            JSONObject jsonData = object.getJSONObject("data");

                            JSONObject ticketDetails = jsonData.getJSONObject("ticket_details");

                            Log.d("Ticket", ticketDetails.toString());

                            String raised_by_email = ticketDetails.getString("raised_by_email");
                            String school_id       = ticketDetails.getString("school_id");
                            String ticket_level = ticketDetails.getString("ticket_level");
                            String ticket_subject = ticketDetails.getString("ticket_subject");
                            String ticket_type = ticketDetails.getString("ticket_type");
                            String raised_by_employee_photo = ticketDetails.getString("raised_by_employee_photo");
                            String ticket_id = ticketDetails.getString("ticket_id");
                            String raised_by_middle_name = ticketDetails.getString("raised_by_middle_name");
                            String raised_by_last_name = ticketDetails.getString("raised_by_last_name");
                            String ticket_priority = ticketDetails.getString("ticket_priority");
                            String ticket_department = ticketDetails.getString("ticket_department");
                            issue_title = ticketDetails.getString("issue_title");
                            String raised_by_pan_no = ticketDetails.getString("raised_by_pan_no");
                            String raised_by = ticketDetails.getString("raised_by");
                            String ticket_status = ticketDetails.getString("ticket_status");
                            String raised_by_first_name = ticketDetails.getString("raised_by_first_name");
                            String raised_by_aadhar_no = ticketDetails.getString("raised_by_aadhar_no");
                            String raised_by_mobile = ticketDetails.getString("raised_by_mobile");
                            String ticket_datetime = ticketDetails.getString("ticket_datetime");

                            if (!raised_by_employee_photo.equals("")){
                                Glide.with(getActivity()).load(Constant.IMAGE_URL+raised_by_employee_photo).into(profile_pic);
                            }

                            tv_name.setText(raised_by_first_name);
                            tv_empId.setText(empCustomId );
                            tv_date.setText(convertor.getDateByTZ_SSS(ticket_datetime));
                            tv_dept.setText(ticket_department);
                            tv_ticket_id.setText(ticket_id);

                            tv_priority.setText(ticket_priority);
                            tv_status_level.setText(ticket_status+" ("+ticket_level+" )");

                            if (ticket_status.equalsIgnoreCase("Closed")){
                                ll_bottom.setVisibility(View.GONE);
                            }

                            tv_ticketType .setText(ticket_type);
                            tv_ticketTitle.setText(issue_title);
                            tv_solved .setText("");

                            JSONObject activityData = jsonData.getJSONObject("activity");

                            Iterator<String> key = activityData.keys();

                            while (key.hasNext()){

                                JSONObject keyData = activityData.getJSONObject(key.next());

                                String first_name = keyData.getString("first_name");
                                String employee = keyData.getString("employee");
                                String escalated_status = keyData.getString("escalated_status");
                                String employee_photo = keyData.getString("employee_photo");
                                String last_name = keyData.getString("last_name");
                                String ticket_message = keyData.getString("ticket_message");
                                String pan_no = keyData.getString("pan_no");
                                String middle_name = keyData.getString("middle_name");
                                String aadhar_no = keyData.getString("aadhar_no");
                                String email = keyData.getString("email");
                                String mobile = keyData.getString("mobile");
                                String response_datetime = keyData.getString("response_datetime");

                                String escalated_level = "";

                                if (!keyData.isNull("escalated_level")){
                                    escalated_level = keyData.getString("escalated_level");
                                }

                                JSONArray attachment = keyData.getJSONArray("attachment");

                                ArrayList<String> images = new ArrayList<>();

                                for (int i=0; i<attachment.length();i++){
                                    images.add(attachment.get(i).toString());
                                }

                                chatList.add(new StaffTicketModel(first_name,employee,escalated_status,employee_photo, last_name,
                                        ticket_message,pan_no,middle_name,aadhar_no,email,mobile,response_datetime,escalated_level,images));

                            }

                            ticketAdapter.notifyDataSetChanged();
                            progressDialog.dismiss();

                        }else {
                            Log.d("GET_TICKET_ELSE", response.code()+" "+message1);
                            progressDialog.dismiss();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }else {
                    try {
                        assert response.errorBody()!=null;
                        JSONObject object = new JSONObject(response.errorBody().string());
                        String message = object.getString("message");
                        Log.d("GET_TICKET_FAIL", message);
                        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();

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

    private void sendTicketReplyAPI(){

        if (ed_reply.getText().toString().trim().isEmpty()){
            Toast.makeText(getContext(),"Please enter a Message", Toast.LENGTH_SHORT).show();

        }else {

            progressDialog.show();

            String message = ed_reply.getText().toString();
            JSONArray jsonArray = new JSONArray();

            MultipartBody.Builder builder = new MultipartBody.Builder();
            builder.setType(MultipartBody.FORM);

            for (int i = 0; i< mArrayUri.size();i++){

                Bitmap bitmap = null;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), mArrayUri.get(i));

                } catch (IOException e) {
                    e.printStackTrace();
                }

                assert bitmap!=null;
                String path = getRealPathFromURI(getImageUri(getActivity(), bitmap));
                File f = new File(path);

                jsonArray.put("file"+i);
                builder.addFormDataPart("file"+i, f.getName(), RequestBody.create(MediaType.parse("multipart/form-data"), f));
            }

            builder.addFormDataPart("school_id",      Constant.SCHOOL_ID);
            builder.addFormDataPart("ticket_id",      ticketID);
            builder.addFormDataPart("ticket_message", message);
            builder.addFormDataPart("replied_employee_uuid",Constant.EMPLOYEE_ID);
            builder.addFormDataPart("attachments",       jsonArray.toString());

            MultipartBody requestBody = builder.build();

            mApiServicePatashala.sendTicketReplies(requestBody).enqueue(new Callback<Object>() {
                @Override
                public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {

                    if (response.isSuccessful()){
                        ed_reply.setText("");
                        mArrayUri.clear();
                        attachAdapter.notifyDataSetChanged();
                        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                        getTicketActivityAPI();

                    }else {
                        progressDialog.dismiss();
                        Toast.makeText(getActivity(), "Message not send", Toast.LENGTH_SHORT).show();
                    }

                    Log.d("response_success", "onResponse: "+response.raw());
                }

                @Override
                public void onFailure(@NonNull Call<Object> call, @NonNull Throwable t) {

                }
            });

        }
    }

    private void escalateTicketIssuesAPI(){

        String reply = ed_reply.getText().toString();

        if (reply.equals(""))
            Toast.makeText(getActivity(), "Reply message is required", Toast.LENGTH_SHORT).show();

        else if (sp_Select_Level.getSelectedItemPosition()==0)
            Toast.makeText(getActivity(), "Level is required", Toast.LENGTH_SHORT).show();

        else {

            progressDialog.show();

            String level = sp_Select_Level.getSelectedItem().toString();

            JSONArray jsonArray = new JSONArray();

            MultipartBody.Builder builder = new MultipartBody.Builder();
            builder.setType(MultipartBody.FORM);

            for (int i = 0; i< mArrayUri.size();i++){

                Bitmap bitmap = null;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), mArrayUri.get(i));

                } catch (IOException e) {
                    e.printStackTrace();
                }

                assert bitmap!=null;
                String path = getRealPathFromURI(getImageUri(getActivity(), bitmap));
                File f = new File(path);

                jsonArray.put("file"+i);
                builder.addFormDataPart("file"+i, f.getName(), RequestBody.create(MediaType.parse("multipart/form-data"), f));
            }

            builder.addFormDataPart("school_id",               Constant.SCHOOL_ID);
            builder.addFormDataPart("ticket_id",               ticketID);
            builder.addFormDataPart("escalated_message",       reply);
            builder.addFormDataPart("replied_employee_uuid",   Constant.EMPLOYEE_ID);
            builder.addFormDataPart("escalated_employee_uuid", SEARCH_ID);
            builder.addFormDataPart("escalated_level",         level);
            builder.addFormDataPart("attachments",       jsonArray.toString());

            MultipartBody requestBody = builder.build();

            mApiServicePatashala.escalateTicketIssues(requestBody).enqueue(new Callback<Object>() {
                @Override
                public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {

                    if (response.isSuccessful()){
                        ll_escalate.setVisibility(View.GONE);
                        ll_escalateBtn.setVisibility(View.GONE);
                        ll_buttons.setVisibility(View.VISIBLE);
                        Toast.makeText(getActivity(), "Escalated Success", Toast.LENGTH_SHORT).show();

                        ed_reply.setText("");
                        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                        getTicketActivityAPI();

                    }else {
                        Toast.makeText(getActivity(), "Escalated Not Success", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();

                    }
                }

                @Override
                public void onFailure(@NonNull Call<Object> call, @NonNull Throwable t) {

                }
            });

        }


    }

    private void getLevelEmployeeAPI(){

        if (sp_Select_Level.getSelectedItemPosition()==0){
            Toast.makeText(getActivity(), "Select Level First", Toast.LENGTH_SHORT).show();

        }else {
            progressDialog.show();

            String level = sp_Select_Level.getSelectedItem().toString().trim();

            mApiServicePatashala.getLevelEmployee(Constant.SCHOOL_ID,
                    issue_title, level).enqueue(new Callback<Object>() {
                @Override
                public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {

                    if (searchList.size()>0)
                        searchList.clear();

                    if (response.isSuccessful()){

                        try {

                            JSONObject object = new JSONObject(new Gson().toJson(response.body()));
                            String status = object.getString("status");
                            String message = object.getString("message");

                            if (status.equalsIgnoreCase("Success")){
                                Log.d("LEVEL_EMP_FAIL", response.code()+" "+message);

                                JSONObject jsonData = object.getJSONObject("data");

                                Iterator<String> key = jsonData.keys();

                                while (key.hasNext()){

                                    JSONObject keyData = jsonData.getJSONObject(key.next());


                                    String role = "", aadhar_no = "", last_name = "", middle_name = "", first_name = "",
                                            department = "", custom_employee_id = "", wing = "", employee_photo = "",
                                            phone_number = "", email = "", employee_uuid = "";

                                    role = keyData.getString("role");
                                    aadhar_no = keyData.getString("aadhar_no");
                                    last_name = keyData.getString("last_name");
                                    first_name = keyData.getString("first_name");
                                    department = keyData.getString("department");
                                    wing = keyData.getString("wing");
                                    employee_photo = keyData.getString("employee_photo");
                                    phone_number = keyData.getString("phone_number");
                                    email = keyData.getString("email");
                                    employee_uuid = keyData.getString("employee_uuid");

                                    if (!keyData.isNull("middle_name")){
                                        middle_name = keyData.getString("middle_name");

                                    }if (!keyData.isNull("custom_employee_id")){
                                        custom_employee_id = keyData.getString("custom_employee_id");

                                    }

                                    searchList.add(new StaffTicketModel(role, aadhar_no, last_name, middle_name, first_name,
                                            department, custom_employee_id, wing, employee_photo, phone_number, email, employee_uuid));

                                }

                                progressDialog.dismiss();
                                searchDialog();

                            }else {
                                Log.d("LEVEL_EMP_FAIL", response.code()+" "+message);
                                progressDialog.dismiss();
                                searchDialog();
                            }

                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }else {
                        try {
                            assert response.errorBody()!=null;
                            JSONObject object = new JSONObject(response.errorBody().string());
                            String message = object.getString("message");
                            Log.d("LEVEL_EMP_FAIL", message);
                            progressDialog.dismiss();
                            searchDialog();

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

    private void searchDialog(){

        Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.dialog_search_emp);
        //noinspection ConstantConditions
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        RecyclerView recycler_view_dialog = dialog.findViewById(R.id.rv_search);
        EditText et_search = dialog.findViewById(R.id.et_search);

        searchAdapter = new StaffTicketAdapter(getActivity(), searchList, dialog,tv_search, Constant.RV_TICKET_STAFF_SEARCH_RESULT);
        recycler_view_dialog.setLayoutManager(new LinearLayoutManager(getActivity()));
        recycler_view_dialog.setAdapter(searchAdapter);
        searchAdapter.notifyDataSetChanged();

        dialog.show();

        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                employeeFilter(editable.toString());
            }
        });

    }

    private void employeeFilter(String text) {

        ArrayList<StaffTicketModel> filteredStaffList = new ArrayList<>();
        for (StaffTicketModel item : searchList) {
            if (item.getFirst_name().toLowerCase().contains(text.toLowerCase())) {
                filteredStaffList.add(item);
            }
        }
        searchAdapter.employeeFilterList(filteredStaffList);
    }

    private void closeTicketIssuesAPI(){

        final Dialog d = new Dialog(getActivity());
        d.setContentView(R.layout.close_ticket_dialog);
        d.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        d.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        d.show();

        final EditText ed_issue = d.findViewById(R.id.et_issue);
        final EditText ed_solution=d.findViewById(R.id.et_solution);
        final RatingBar dialog_ratingBar=d.findViewById(R.id.dialod_rating_bar);
        dialog_ratingBar.setStepSize(1);
        final Button btn_close = d.findViewById(R.id.dialog_submit);
        final  Button btn_cancel=d.findViewById(R.id.dialog_cancel);

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d.dismiss();
            }
        });

        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ed_issue.getText().toString().isEmpty()){
                    Toast.makeText(getContext(),"Please enter issue", Toast.LENGTH_SHORT).show();

                } else if (ed_solution.getText().toString().isEmpty()){
                    Toast.makeText(getContext(),"Please enter Solution provided", Toast.LENGTH_SHORT).show();

                } else if (dialog_ratingBar.getRating()<1){
                    Toast.makeText(getContext(),"Please give rating minimum of one star", Toast.LENGTH_SHORT).show();

                }else {

                    Log.d("rating", dialog_ratingBar.getRating() + "");

                    String issueMessage = ed_issue.getText().toString();
                    String feedback = ed_solution.getText().toString();
                    String rating   = dialog_ratingBar.getRating() + "";

                    progressDialog.show();

                    mApiServicePatashala.closeTicketIssues(Constant.SCHOOL_ID,ticketID,Constant.EMPLOYEE_ID,issueMessage,
                            feedback, rating,"true", Constant.EMPLOYEE_ID).enqueue(new Callback<Object>() {
                        @Override
                        public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {

                            Log.d("response_success", "onResponse: "+response.raw());

                            if (response.isSuccessful()){
                                tv_status_level.setText("Closed");
                                ll_escalate.setVisibility(View.GONE);
                                ll_escalateBtn.setVisibility(View.GONE);
                                ll_buttons.setVisibility(View.GONE);
                                d.dismiss();
                                progressDialog.dismiss();

                            }else {
                                Toast.makeText(getActivity(), "Not Closed", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();

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
