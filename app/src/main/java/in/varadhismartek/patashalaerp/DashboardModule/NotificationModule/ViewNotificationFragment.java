package in.varadhismartek.patashalaerp.DashboardModule.NotificationModule;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import in.varadhismartek.Utils.Constant;
import in.varadhismartek.patashalaerp.R;
import in.varadhismartek.patashalaerp.Retrofit.APIService;
import in.varadhismartek.patashalaerp.Retrofit.ApiUtilsPatashala;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewNotificationFragment extends Fragment implements View.OnClickListener {
    EditText et_title, et_message, et_comment;
    private ImageView iv_backBtn, iv_attach, iv_dropButton;
    private TextView tv_title, tv_message, tv_date, tv_time, tv_sendTo, tv_addedBy, tv_sendList;
    private Button bt_update, bt_update_emp;
    private NotificationModel notificationModel;

    private String currentDate = "";
    private String currentTime = "";

    int date = 0, month = 0, year = 0;
    final int REQUEST_PERMISSION_CODE = 1234;
    private File userProfile;
    private Dialog dialog;
    private APIService mApiServicePatashala;
    private LinearLayout ll_sendList,ll_notify_update;
    ArrayList<String> listSend = new ArrayList<>();


    public ViewNotificationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_view_notification, container, false);

        initViews(view);
        initListeners();
        getBundels();
        getCurrentDateAndTime();

        return view;
    }

    private void getBundels() {

        Bundle bundle = getArguments();
        assert bundle != null;
        notificationModel = (NotificationModel) bundle.getSerializable("data");

        setStrings();
    }

    private void getCurrentDateAndTime() {

        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleTimeFormat = new SimpleDateFormat("hh:mm aa");

        Calendar calendar = Calendar.getInstance();

        currentDate = simpleDateFormat.format(calendar.getTime());
        currentTime = simpleTimeFormat.format(calendar.getTime());

        Log.d("CURRENT_DATE_TIME", currentDate + " " + currentTime);

        date = calendar.get(Calendar.DAY_OF_MONTH);
        month = calendar.get(Calendar.MONTH);
        year = calendar.get(Calendar.YEAR);

        Log.d("CURRENT TIME", date + " " + month + " " + year);

    }

    private void setStrings() {

        assert notificationModel != null;

        final String imageUrlg = "http://13.233.109.165:8000/media/" + notificationModel.getAttachment();

        String title = notificationModel.getTitle();
        System.out.println("title***" + title);

       /* if (title.length()>30)
            tv_title.setText(title.substring(0,29)+"...");
        else
            tv_title.setText(title);*/

        if (title.length() == 0) {
            tv_title.setText("UnDefine");
        } else if (title.length() > 30)
            tv_title.setText(title.substring(0, 29) + "...");
        else
            tv_title.setText(title);

        tv_message.setText(notificationModel.getMessage());
        tv_date.setText(notificationModel.getSendDate());
        tv_time.setText(notificationModel.getSendTime());
        tv_sendTo.setText(notificationModel.getSendTo());
        String strNotifyStatus = notificationModel.getApproverStatus();

        System.out.println("NOTIFICATIONSTATUS**1"+notificationModel.getApproverStatus());


        tv_addedBy.setText(notificationModel.getFirstName() + " " + notificationModel.getLastName());
        assert getActivity() != null;
        Glide.with(getActivity()).load(imageUrlg).into(iv_attach);

        listSend = notificationModel.getListSend();
        Log.i("listSend**", "" + listSend.size());


        StringBuffer sb = new StringBuffer();

        for (int j = 0; j < listSend.size(); j++) {
            sb.append(listSend.get(j) + ", ");
        }


        if (listSend.size() == 0) {
            // tv_select.setText("-- Select --");

        } else
            tv_sendList.setText(sb.toString());

    }

    private void initListeners() {

        iv_backBtn.setOnClickListener(this);
        bt_update.setOnClickListener(this);
        bt_update_emp.setOnClickListener(this);
        iv_dropButton.setOnClickListener(this);
    }

    private void initViews(View view) {

        mApiServicePatashala = ApiUtilsPatashala.getService();
        iv_backBtn = view.findViewById(R.id.iv_backBtn);
        iv_dropButton = view.findViewById(R.id.iv_dropButton);
        ll_sendList = view.findViewById(R.id.ll_sendList);
        ll_notify_update = view.findViewById(R.id.ll_notify_update);

        tv_title = view.findViewById(R.id.tv_title);
        tv_message = view.findViewById(R.id.tv_message);
        tv_sendList = view.findViewById(R.id.tv_sendList);
        tv_date = view.findViewById(R.id.tv_date);
        tv_time = view.findViewById(R.id.tv_time);
        tv_sendTo = view.findViewById(R.id.tv_sendTo);
        tv_addedBy = view.findViewById(R.id.tv_addedBy);

        iv_attach = view.findViewById(R.id.iv_attach);
        bt_update = view.findViewById(R.id.bt_update);
        bt_update_emp = view.findViewById(R.id.bt_update_emp);
    }

    @Override
    public void onClick(View view) {


        assert getActivity() != null;

        switch (view.getId()) {

            case R.id.iv_backBtn:
                getActivity().onBackPressed();
                break;

            case R.id.bt_update:
                openDialogForUpdate("Admin");
                break;

            case R.id.bt_update_emp:
                openDialogForUpdate("Employee");
                break;

            case R.id.iv_dropButton:
                if (ll_sendList.getVisibility() == View.VISIBLE) {
                    ll_sendList.setVisibility(View.GONE);

                } else {
                    ll_sendList.setVisibility(View.VISIBLE);
                }
                break;
        }
    }


    //----------------------------------------ALL API_ HERE ADMIN----------------

    private void openDialogForUpdate(final String TAG) {

        assert getActivity() != null;

        dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.dialog_update_notification);
        //noinspection ConstantConditions
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.flag_transparent);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();

        Button bt_cancel = dialog.findViewById(R.id.bt_cancel);
        final Button bt_on_off = dialog.findViewById(R.id.bt_on_off);
        Button bt_update = dialog.findViewById(R.id.bt_update);
        Button bt_attachment = dialog.findViewById(R.id.bt_attachment);

        final LinearLayout ll_schedule_row = dialog.findViewById(R.id.ll_schedule_row);
        LinearLayout ll_date = dialog.findViewById(R.id.ll_date);
        LinearLayout ll_time = dialog.findViewById(R.id.ll_time);
        final TextView tv_date = dialog.findViewById(R.id.tv_date);
        final TextView tv_time = dialog.findViewById(R.id.tv_time);

        TextView tv_comment = dialog.findViewById(R.id.tv_comment);

        et_title = dialog.findViewById(R.id.et_title);
        et_message = dialog.findViewById(R.id.et_message);
        et_comment = dialog.findViewById(R.id.et_comment);

        et_message.setText(notificationModel.getMessage());
        et_title.setText(notificationModel.getTitle());

        if (TAG.equalsIgnoreCase("Admin")) {
            tv_comment.setVisibility(View.VISIBLE);
            et_comment.setVisibility(View.VISIBLE);
            bt_update.setText("Update");

        } else {
            tv_comment.setVisibility(View.GONE);
            et_comment.setVisibility(View.GONE);

        }

        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //dialog.dismiss();
                String strStatus = "Rejected";
                updateNotificationmethod(TAG, strStatus);
            }
        });

        bt_on_off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bt_on_off.getText().toString().equalsIgnoreCase("OFF")) {
                    bt_on_off.setBackgroundColor(Color.parseColor("#ff00bcd4"));
                    bt_on_off.setText("ON");
                    ll_schedule_row.setVisibility(View.VISIBLE);
                    currentDate = "";
                    currentTime = "";
                    Log.d("CURRENT_DATE_TIME_2", currentDate + " " + currentTime);
                    tv_date.setText("Date");
                    tv_time.setText("Time");


                } else {
                    bt_on_off.setBackgroundColor(Color.parseColor("#A0A0A0"));
                    bt_on_off.setText("OFF");
                    ll_schedule_row.setVisibility(View.GONE);
                    ViewNotificationFragment.this.getCurrentDateAndTime();
                    Log.d("CURRENT_DATE_TIME_1", currentDate + " " + currentTime);
                }
            }
        });

        ll_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatePickerDialog dialog = new DatePickerDialog(ViewNotificationFragment.this.getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

                        Calendar cal = Calendar.getInstance();
                        cal.set(Calendar.YEAR, i);
                        cal.set(Calendar.MONTH, i1);
                        cal.set(Calendar.DAY_OF_MONTH, i2);

                        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

                        currentDate = simpleDateFormat.format(cal.getTime());
                        Log.d("CURRENT_DATE_TIME_3", currentDate);
                        tv_date.setText(currentDate);
                    }
                }, year, month, date);
                dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                dialog.show();

            }
        });

        ll_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TimePickerDialog timePickerDialog = new TimePickerDialog(ViewNotificationFragment.this.getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {

                        Calendar calendar = Calendar.getInstance();
                        calendar.set(Calendar.HOUR, i);
                        calendar.set(Calendar.MINUTE, i1);

                        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm aa");

                        currentTime = simpleDateFormat.format(calendar.getTime());
                        Log.d("CURRENT_DATE_TIME_4", currentTime);
                        tv_time.setText(currentTime);

                    }
                }, 12, 12, false);

                timePickerDialog.show();

            }
        });

        bt_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String strStatus = "Approved";
                updateNotificationmethod(TAG, strStatus);
            }
        });

        bt_attachment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewNotificationFragment.this.initPermission();
            }
        });

    }

    private void updateNotificationmethod(String TAG, String strStatus) {

        String message = et_message.getText().toString();
        String comment = et_comment.getText().toString();

        if (et_title.getText().toString().equals("")) {
            Toast.makeText(ViewNotificationFragment.this.getActivity(), "Title To Is Required", Toast.LENGTH_SHORT).show();

        } else if (et_message.getText().toString().equals("")) {
            Toast.makeText(ViewNotificationFragment.this.getActivity(), "Message To Is Required", Toast.LENGTH_SHORT).show();

        } else if (currentDate.equals("")) {
            Toast.makeText(ViewNotificationFragment.this.getActivity(), "Date To Is Required", Toast.LENGTH_SHORT).show();

        } else if (currentTime.equals("")) {
            Toast.makeText(ViewNotificationFragment.this.getActivity(), "Time To Is Required", Toast.LENGTH_SHORT).show();

        } else {

            if (userProfile != null) {

                Log.d("lafkjasfj", Constant.SCHOOL_ID + " " + notificationModel.getNotificationID() + " " + Constant.EMPLOYEE_BY_ID);

                RequestBody schoolId = RequestBody.create(MediaType.parse("text/plain"), Constant.SCHOOL_ID);
                RequestBody notificationID = RequestBody.create(MediaType.parse("text/plain"), notificationModel.getNotificationID());
                RequestBody employeeId = RequestBody.create(MediaType.parse("text/plain"), Constant.EMPLOYEE_BY_ID);
                RequestBody newSendTime = RequestBody.create(MediaType.parse("text/plain"), currentTime);
                RequestBody newSendDate = RequestBody.create(MediaType.parse("text/plain"), currentDate);
                RequestBody newMessage = RequestBody.create(MediaType.parse("text/plain"), message);
                RequestBody app_comment = RequestBody.create(MediaType.parse("text/plain"), comment);
                //RequestBody approvar = RequestBody.create(MediaType.parse("text/plain"), "Pending");
                RequestBody approvar = RequestBody.create(MediaType.parse("text/plain"), strStatus);
                RequestBody status = RequestBody.create(MediaType.parse("text/plain"), "true");

                final RequestBody mFileProfile = RequestBody.create(MediaType.parse("image"), userProfile);
                final MultipartBody.Part fileToUploadProfilepicture = MultipartBody.Part.createFormData("new_notification_attachment", userProfile.getName(), mFileProfile);

                Log.d("Multipart", fileToUploadProfilepicture.toString() + "  " + userProfile.getName() + " " + mFileProfile);
                Log.d("UPDATE_API", currentDate + " " + currentTime);


                switch (TAG) {

                    case "Admin":
                        mApiServicePatashala.updateNotificationAdmin(schoolId, notificationID,
                                newSendTime, newSendDate, newMessage, status,
                                fileToUploadProfilepicture, employeeId, app_comment, approvar).enqueue(new Callback<Object>() {
                            @Override
                            public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {

                                if (response.isSuccessful()) {
                                    Log.d("UPDATE_API", String.valueOf(response.code()));
                                    dialog.dismiss();
                                    assert getActivity() != null;
                                    getActivity().onBackPressed();

                                } else {
                                    try {
                                        assert response.errorBody() != null;
                                        Log.d("UPDATE_API", response.code() + " " + response.message());

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }

                            @Override
                            public void onFailure(@NonNull Call<Object> call, @NonNull Throwable t) {

                            }
                        });
                        break;

                    case "Employee":

                        mApiServicePatashala.updateNotification(schoolId, employeeId, notificationID, newSendTime, newSendDate, newMessage,
                                status, fileToUploadProfilepicture).enqueue(new Callback<Object>() {
                            @Override
                            public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {

                                if (response.isSuccessful()) {
                                    Log.d("UPDATE_EMP_NOT", response.body().toString());

                                    dialog.dismiss();
                                    assert getActivity() != null;
                                    getActivity().onBackPressed();
                                } else {

                                    Log.d("UPDATE_EMP_NOT", String.valueOf(response.code()));

                                }
                            }

                            @Override
                            public void onFailure(@NonNull Call<Object> call, @NonNull Throwable t) {

                            }
                        });

                        break;
                }


            } else {

                switch (TAG) {

                    case "Admin":
                        mApiServicePatashala.updateNotificationAdminWithOutImage(Constant.SCHOOL_ID,
                                notificationModel.getNotificationID(),
                                currentTime, currentDate, message, "true",
                                Constant.EMPLOYEE_BY_ID, comment, strStatus).enqueue(new Callback<Object>() {
                            @Override
                            public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {

                                if (response.isSuccessful()) {
                                    Log.d("UPDATE_API12", String.valueOf(response.code()));
                                    dialog.dismiss();
                                    assert getActivity() != null;
                                    getActivity().onBackPressed();

                                } else {
                                    try {
                                        assert response.errorBody() != null;
                                        Log.d("UPDATE_API", response.code() + " " + response.message());

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }

                            @Override
                            public void onFailure(@NonNull Call<Object> call, @NonNull Throwable t) {

                            }
                        });
                        break;
                }

            }

        }
    }

    private void initPermission() {

        assert getActivity() != null;

        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            if (ContextCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                Toast.makeText(getActivity(), "Need To Permissions", Toast.LENGTH_SHORT).show();

                ActivityCompat.requestPermissions(getActivity(), new String[]{
                        Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_PERMISSION_CODE);

                getAttachmentMethod();
            }

        } else {
            getAttachmentMethod();
        }
    }

    private void getAttachmentMethod() {

        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 1111);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1111) {

            if (data != null) {

                Uri uri = data.getData();
                try {

                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                    String path = saveImage(bitmap);

                    userProfile = new File(path);

                    ImageView iv_attachment = dialog.findViewById(R.id.iv_attachment);
                    iv_attachment.setImageBitmap(bitmap);

                } catch (IOException e) {
                    e.printStackTrace();

                }
            } else {

            }

        }
    }

    public String saveImage(Bitmap myBitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File wallpaperDirectory = new File(String.valueOf(Environment.getExternalStorageDirectory()));
        // have the object build the directory structure, if needed.
        if (!wallpaperDirectory.exists()) {
            //noinspection ResultOfMethodCallIgnored
            wallpaperDirectory.mkdirs();
        }

        try {
            File f = new File(wallpaperDirectory, Calendar.getInstance()
                    .getTimeInMillis() + ".jpg");
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(getActivity(),
                    new String[]{f.getPath()},
                    new String[]{"image/jpeg"}, null);
            fo.close();
            Log.d("TAG", "File Saved::---&gt;" + f.getAbsolutePath());

            return f.getAbsolutePath();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return "";
    }
}
