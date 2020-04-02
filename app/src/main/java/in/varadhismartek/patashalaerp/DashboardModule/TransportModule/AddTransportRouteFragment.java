package in.varadhismartek.patashalaerp.DashboardModule.TransportModule;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

import in.varadhismartek.Utils.Constant;
import in.varadhismartek.Utils.Utilities;
import in.varadhismartek.patashalaerp.Birthday.BirthdayModel;
import in.varadhismartek.patashalaerp.GeneralClass.CustomSpinnerAdapter;
import in.varadhismartek.patashalaerp.R;
import in.varadhismartek.patashalaerp.Retrofit.APIService;
import in.varadhismartek.patashalaerp.Retrofit.ApiUtilsPatashala;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

public class AddTransportRouteFragment extends Fragment implements View.OnClickListener {
    Button btn_Submit;
    Spinner sp_BusList;
    ImageView iv_location, iv_stopImage;
    TextView ed_NoStop, tv_Plus, tv_Minus;
    EditText ed_latValue, ed_longValue, ed_stopDistance, ed_stopTime,
            ed_startHomePoint, ed_startSchoolPoint, ed_RouteNo, ed_StopName, ed_SchoolTime, ed_HomeTime;
    Switch sw_locationManually;

    String strSchoolTime, strHomeTime;
    String strSchoolPoint, strHomePoint;
    String strRouteNo, strStopNo, strStopName, strLatPoint, strLongPoint, strStopDis, strStopTime,
            strLocationCond = "true";
    private static String TAG = "";
    private static final int FROM_CAMERA = 1111;
    private static final int FROM_GALLERY = 2222;
    File stopImagelFile;
    APIService apiService;
    Utilities utilities;
    TimePickerDialog timePickerDialog;
    Calendar calendar;
    int currentHour;
    int currentMinute;
    String amPm;
    TextView tv_VehicleName, tv_VehicleSeat, tv_VehicleRegNo, tv_VehicleID;

    ArrayList<BirthdayModel> transportBusList = new ArrayList<>();
    String type_of_body, GPS_Details, seating_capacity, registration_no,
            class_of_vehicle, vehicle_uuid, vehicle_id, added_datetime;
    String strVName = "NA", strVSeat = "NA", strVUUID = "NA", strVID = "NA", strVRegNo = "NA";
    String route_uuid = "", route_number = "";

    int routeCount = 1;
    ImageView iv_backBtn;
    protected LocationManager locationManager;
    protected LocationListener locationListener;
    String lat;
    String provider;
    protected String latitude,longitude;

    public AddTransportRouteFragment() {
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_route, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);


        initViews(view);
        initListeners();
        getBusListAPI();

        return view;
    }

    private void getBusListAPI() {
        utilities.displayProgressDialog(getActivity(), "Please wait ...", false);
        transportBusList.clear();
        apiService.getSchoolBusList(Constant.SCHOOL_ID).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if (response.isSuccessful()) {
                    utilities.cancelProgressDialog();

                    try {

                        JSONObject object = new JSONObject(new Gson().toJson(response.body()));
                        String status = (String) object.get("status");

                        if (status.equalsIgnoreCase("success")) {
                            if (object.getJSONObject("data").toString().equals("{}")) {
                                utilities.cancelProgressDialog();
                                transportBusList.clear();

                            } else {
                                JSONObject jsonObject1 = object.getJSONObject("data");
                                Iterator<String> keys = jsonObject1.keys();

                                while (keys.hasNext()) {
                                    String key = keys.next();
                                    JSONObject jsonObjectValue = jsonObject1.getJSONObject(key);


                                    if (!jsonObjectValue.isNull("type_of_body")) {
                                        type_of_body = jsonObjectValue.getString("type_of_body");
                                    }
                                    if (!jsonObjectValue.isNull("GPS_Details")) {
                                        GPS_Details = jsonObjectValue.getString("GPS_Details");
                                    }
                                    if (!jsonObjectValue.isNull("seating_capacity")) {
                                        seating_capacity = jsonObjectValue.getLong("seating_capacity") + "";
                                    }
                                    if (!jsonObjectValue.isNull("registration_no")) {
                                        registration_no = jsonObjectValue.getString("registration_no");
                                    }
                                    if (!jsonObjectValue.isNull("class_of_vehicle")) {
                                        class_of_vehicle = jsonObjectValue.getString("class_of_vehicle");
                                    }
                                    if (!jsonObjectValue.isNull("vehicle_uuid")) {
                                        vehicle_uuid = jsonObjectValue.getString("vehicle_uuid");
                                    }
                                    if (!jsonObjectValue.isNull("vehicle_id")) {
                                        vehicle_id = jsonObjectValue.getString("vehicle_id");
                                    }

                                    if (!jsonObjectValue.isNull("added_datetime")) {
                                        added_datetime = jsonObjectValue.getString("added_datetime");
                                    }

                                    Log.i("jsonObjectValue", "" + jsonObjectValue);

                                    JSONArray jsonArray = jsonObjectValue.getJSONArray("routes");
                                    Log.i("jsonArray", "" + jsonArray.toString());

                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject routeJson = jsonArray.getJSONObject(i);

                                        if (routeJson.isNull("route_uuid")) {
                                            route_uuid = "NA";
                                        } else {
                                            route_uuid = routeJson.getString("route_uuid");
                                        }
                                        Log.i("route_uuid", "" + route_uuid);
                                        if (routeJson.isNull("route_number")) {
                                            route_number = "NA";
                                        } else {
                                            route_number = routeJson.getString("route_number");
                                        }
                                        Log.i("route_uuid12", "" + route_number);
                                    }


                                    transportBusList.add(new BirthdayModel(type_of_body, GPS_Details, seating_capacity, registration_no,
                                            class_of_vehicle, vehicle_uuid, vehicle_id, added_datetime));
                                }
                                setSpinner(transportBusList);

                            }
                        }
                    } catch (JSONException je) {
                        je.printStackTrace();
                        utilities.cancelProgressDialog();
                    }


                } else {
                    try {
                        assert response.errorBody() != null;
                        utilities.cancelProgressDialog();
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
                utilities.cancelProgressDialog();

            }
        });
    }

    private void setSpinner(final ArrayList<BirthdayModel> transportBusList) {

        ArrayList<String> vehicleList = new ArrayList<>();
        vehicleList.clear();
        vehicleList.add("Select vehicle");

        for (int i = 0; i < transportBusList.size(); i++) {
            vehicleList.add(transportBusList.get(i).getClass_of_vehicle().toString());
        }

        CustomSpinnerAdapter vehicleListAdapter = new CustomSpinnerAdapter(getActivity(), vehicleList, "");
        sp_BusList.setAdapter(vehicleListAdapter);
        sp_BusList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String strVehicleName = sp_BusList.getItemAtPosition(position).toString();
                int intVehiclPos = (int) sp_BusList.getItemIdAtPosition(position);

                Log.i("strVehicleName***", "" + strVehicleName + "**" + intVehiclPos);
                if (strVehicleName.equalsIgnoreCase("Select vehicle")) {
                    tv_VehicleName.setText(strVName);
                    tv_VehicleSeat.setText(strVSeat);
                    tv_VehicleRegNo.setText(strVRegNo);
                    tv_VehicleID.setText(strVID);

                } else {

                    strVName = transportBusList.get(intVehiclPos - 1).getClass_of_vehicle();
                    strVSeat = transportBusList.get(intVehiclPos - 1).getSeating_capacity();
                    strVUUID = transportBusList.get(intVehiclPos - 1).getVehicle_uuid();
                    strVID = transportBusList.get(intVehiclPos - 1).getVehicle_id();
                    strVRegNo = transportBusList.get(intVehiclPos - 1).getRegistration_no();

                    tv_VehicleName.setText(strVName);
                    tv_VehicleSeat.setText(strVSeat);
                    tv_VehicleRegNo.setText(strVRegNo);
                    tv_VehicleID.setText(strVID);

                    Constant.VEHICLE_ID = strVUUID;

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    private void initViews(View view) {


        apiService = ApiUtilsPatashala.getService();


        iv_backBtn = view.findViewById(R.id.iv_backBtn);
        tv_VehicleName = view.findViewById(R.id.tv_VehicleName);
        tv_VehicleSeat = view.findViewById(R.id.tv_VehicleSeat);
        tv_VehicleRegNo = view.findViewById(R.id.tv_VehicleRegNo);
        tv_VehicleID = view.findViewById(R.id.tv_VehicleID);

        btn_Submit = view.findViewById(R.id.btn_Submit);
        sp_BusList = view.findViewById(R.id.sp_BusList);

        iv_location = view.findViewById(R.id.iv_location);


        iv_stopImage = view.findViewById(R.id.iv_stopImage);
        sw_locationManually = view.findViewById(R.id.sw_locationManually);
        ed_StopName = view.findViewById(R.id.ed_StopName);
        ed_latValue = view.findViewById(R.id.ed_latValue);
        ed_longValue = view.findViewById(R.id.ed_longValue);
        ed_stopDistance = view.findViewById(R.id.ed_stopDistance);
        ed_stopTime = view.findViewById(R.id.ed_stopTime);

        ed_NoStop = view.findViewById(R.id.ed_NoStop);
        tv_Plus = view.findViewById(R.id.tv_Plus);
        tv_Minus = view.findViewById(R.id.tv_Minus);
        ed_startHomePoint = view.findViewById(R.id.ed_startHomePoint);
        ed_startSchoolPoint = view.findViewById(R.id.ed_startSchoolPoint);
        ed_RouteNo = view.findViewById(R.id.ed_RouteNo);

        ed_SchoolTime = view.findViewById(R.id.ed_SchoolTime);
        ed_HomeTime = view.findViewById(R.id.ed_HomeTime);

        calendar = Calendar.getInstance();
        currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        currentMinute = calendar.get(Calendar.MINUTE);

        ed_NoStop.setText(String.valueOf(routeCount));


        Log.i("strLocationCond", "" + strLocationCond);


        ed_SchoolTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                        if (hourOfDay >= 12) {
                            amPm = "PM";
                        } else {
                            amPm = "AM";
                        }
                        ed_SchoolTime.setText(String.format("%02d:%02d", hourOfDay, minutes));
                    }
                }, currentHour, currentMinute, false);

                timePickerDialog.show();
            }
        });


    }

    private void initListeners() {
        iv_backBtn.setOnClickListener(this);
        btn_Submit.setOnClickListener(this);
        iv_stopImage.setOnClickListener(this);
        ed_stopTime.setOnClickListener(this);
        ed_HomeTime.setOnClickListener(this);
        tv_Plus.setOnClickListener(this);
        tv_Minus.setOnClickListener(this);
        iv_location.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.iv_location:

                break;


            case R.id.iv_backBtn:
                Intent intent = new Intent(getActivity(),TransportActivity.class);
                getActivity().startActivity(intent);
                getActivity().finish();
                break;

            case R.id.tv_Plus:

                if (routeCount < 5) {
                    routeCount = routeCount + 1;
                    ed_NoStop.setText(String.valueOf(routeCount));

                }


                break;

            case R.id.tv_Minus:
                if (routeCount > 1) {
                    routeCount = routeCount - 1;
                    ed_NoStop.setText(String.valueOf(routeCount));
                    // View viewBook = getLayoutInflater().inflate(R.layout.layout_book_row, null);
                    // ll_stopRoute.removeViewAt(routeCount - 1);


                }
                break;

            case R.id.ed_stopTime:
                openClockTime();
                break;

            case R.id.ed_HomeTime:
                openClockTimeHome();
                break;

            case R.id.iv_stopImage:
                stopImageAttach();
                break;


            case R.id.btn_Submit:

                if (ed_RouteNo.getText().toString().isEmpty() || ed_RouteNo.getText().toString().equals(null)) {
                    Toast.makeText(getActivity(), "Enter route no", Toast.LENGTH_SHORT).show();
                } else if (ed_startSchoolPoint.getText().toString().isEmpty() || ed_startSchoolPoint.getText().toString().equals(null)) {
                    Toast.makeText(getActivity(), "Enter start point from school", Toast.LENGTH_SHORT).show();
                } else if (ed_SchoolTime.getText().toString().isEmpty() || ed_SchoolTime.getText().toString().equals(null)) {
                    Toast.makeText(getActivity(), "Select time to start point from school", Toast.LENGTH_SHORT).show();
                } else if (ed_startHomePoint.getText().toString().isEmpty() || ed_startHomePoint.getText().toString().equals(null)) {
                    Toast.makeText(getActivity(), "Enter start point from home", Toast.LENGTH_SHORT).show();
                } else if (ed_HomeTime.getText().toString().isEmpty() || ed_HomeTime.getText().toString().equals(null)) {
                    Toast.makeText(getActivity(), "Select time to start point from home", Toast.LENGTH_SHORT).show();
                } else if (ed_StopName.getText().toString().isEmpty() || ed_StopName.getText().toString().equals(null)) {
                    Toast.makeText(getActivity(), "Enter stop name ", Toast.LENGTH_SHORT).show();
                } else if (ed_latValue.getText().toString().isEmpty() || ed_latValue.getText().toString().equals(null)) {
                    Toast.makeText(getActivity(), "Enter latitude point ", Toast.LENGTH_SHORT).show();
                } else if (ed_longValue.getText().toString().isEmpty() || ed_longValue.getText().toString().equals(null)) {
                    Toast.makeText(getActivity(), "Enter longitude point", Toast.LENGTH_SHORT).show();
                } else if (ed_stopDistance.getText().toString().isEmpty() || ed_stopDistance.getText().toString().equals(null)) {
                    Toast.makeText(getActivity(), "Enter stop distance  ", Toast.LENGTH_SHORT).show();
                } else if (ed_stopTime.getText().toString().isEmpty() || ed_stopTime.getText().toString().equals(null)) {
                    Toast.makeText(getActivity(), "Enter stop approx time", Toast.LENGTH_SHORT).show();
                } else if (stopImagelFile == null) {
                    Toast.makeText(getActivity(), "Attach stop image", Toast.LENGTH_SHORT).show();
                } else {


                    Log.i("strLocationCond1", "" + strLocationCond);

                    strRouteNo = ed_RouteNo.getText().toString().trim();
                    strSchoolPoint = ed_startSchoolPoint.getText().toString().trim();
                    strHomePoint = ed_startHomePoint.getText().toString().trim();
                    strStopNo = ed_NoStop.getText().toString().trim();
                    strSchoolTime = ed_SchoolTime.getText().toString().trim();
                    strHomeTime = ed_HomeTime.getText().toString().trim();

                    strStopName = ed_StopName.getText().toString().trim();
                    strLatPoint = ed_latValue.getText().toString().trim();
                    strLongPoint = ed_longValue.getText().toString().trim();
                    strStopDis = ed_stopDistance.getText().toString().trim();
                    strStopTime = ed_stopTime.getText().toString().trim();


                    addVehicleRouteAPI();
                }


                break;
        }
    }

    private void openClockTimeHome() {
        timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                if (hourOfDay >= 12) {
                    amPm = "PM";
                } else {
                    amPm = "AM";
                }
                ed_HomeTime.setText(String.format("%02d:%02d", hourOfDay, minutes));
            }
        }, currentHour, currentMinute, false);

        timePickerDialog.show();
    }

    private void openClockTime() {

        timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                if (hourOfDay >= 12) {
                    amPm = "PM";
                } else {
                    amPm = "AM";
                }
                ed_stopTime.setText(String.format("%02d:%02d", hourOfDay, minutes));
            }
        }, currentHour, currentMinute, false);

        timePickerDialog.show();
    }

    private void addVehicleRouteAPI() {


        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);


        JSONObject objectStop = new JSONObject();
        try {
            JSONArray jsonArray = new JSONArray();
            JSONObject object2 = new JSONObject();
            JSONObject object = new JSONObject();


            object.put("enter_location_manually", strLocationCond);
            object.put("latitude", strLatPoint.trim());
            object.put("longitude", strLongPoint.trim());
            object.put("stop_name", strStopName.trim());
            object.put("stop_distance", strStopDis.trim());
            object.put("approx_stop_time", strStopTime);


            object.put("stop_image", "stop_image" + strStopDis);
            builder.addFormDataPart("stop_image" + strStopDis, stopImagelFile.getName(), RequestBody.create(MediaType.parse("multipart/form-data"), stopImagelFile));

            jsonArray.put(object);
            object2.put("1", jsonArray);
            objectStop.put("stop", object2);


        } catch (JSONException je) {
            je.printStackTrace();
        }


        Log.i("objectStop", "" + objectStop);
        Log.i("Constant.VEHICLE_ID", "" + Constant.VEHICLE_ID);

        //Constant.VEHICLE_ID = "5387bfcd-80cc-4821-a721-30a7d0b5ff35";


        builder.addFormDataPart("school_id", Constant.SCHOOL_ID);
        builder.addFormDataPart("route_number", strRouteNo);
        builder.addFormDataPart("vehicle_uuid", Constant.VEHICLE_ID);
        builder.addFormDataPart("vehicle_start_point_towards_school", strSchoolPoint.trim());
        builder.addFormDataPart("vehicle_start_time_towards_school", strSchoolTime);
        builder.addFormDataPart("vehicle_start_point_towards_home", strHomePoint.trim());
        builder.addFormDataPart("vehicle_start_time_towards_home", strHomeTime);
        builder.addFormDataPart("number_of_stops", strStopNo.trim());
        builder.addFormDataPart("added_employeeid", Constant.EMPLOYEE_BY_ID);

        builder.addFormDataPart("stops", objectStop.toString());


        MultipartBody requestBody = builder.build();
        utilities.displayProgressDialog(getActivity(), "Loading ...", false);
        apiService.addVehicleRoute(requestBody).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if (response.isSuccessful()) {
                    try {
                        utilities.cancelProgressDialog();
                        JSONObject object = new JSONObject(new Gson().toJson(response.body()));
                        String status = (String) object.get("status");

                        if (status.equalsIgnoreCase("success")) {
                            Toast.makeText(getActivity(), "Route Added Successfully", Toast.LENGTH_SHORT).show();

                            ed_RouteNo.setText("");
                            ed_startSchoolPoint.setText("");
                            ed_startHomePoint.setText("");
                            ed_NoStop.setText("1");
                            ed_SchoolTime.setText("");
                            ed_HomeTime.setText("");

                            ed_StopName.setText("");
                            ed_latValue.setText("");
                            ed_longValue.setText("");
                            ed_stopDistance.setText("");
                            ed_stopTime.setText("");


                            JSONObject jsonObject = object.getJSONObject("data");

                            if (!jsonObject.isNull("route_uuid")) {
                                String route_uuid = jsonObject.getString("route_uuid");

                            }


                        }

                    } catch (JSONException je) {
                        je.printStackTrace();
                    }
                } else {
                    try {
                        assert response.errorBody() != null;
                        utilities.cancelProgressDialog();

                        JSONObject object = new JSONObject(response.errorBody().string());
                        String message = object.getString("message");
                        Log.d("Trans_addd*44555", message);
                        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.d("Trans_addd*44555", e.getMessage());
                    }

                }

            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Log.i("ERROR***", "" + t.toString() + t.getMessage());
                utilities.cancelProgressDialog();

            }
        });


    }

    private void stopImageAttach() {
        final Dialog dialog = new Dialog(getActivity());

        dialog.setContentView(R.layout.dialog_camera_gallery);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.flag_transparent);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();

        LinearLayout camera = dialog.findViewById(R.id.ll_camera);
        LinearLayout gallery = dialog.findViewById(R.id.ll_gallery);
        TextView tv_date = dialog.findViewById(R.id.tv_date);
        TextView tv_add = dialog.findViewById(R.id.tv_add);
        EditText et_gallery_title = dialog.findViewById(R.id.et_gallery_title);

        tv_date.setVisibility(View.GONE);
        tv_add.setVisibility(View.GONE);
        et_gallery_title.setVisibility(View.GONE);

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, FROM_CAMERA);
                dialog.dismiss();
            }
        });

        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent galleryIntent = new Intent();
                galleryIntent.setType("image/*");
                galleryIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(galleryIntent, "Select Image"), FROM_GALLERY);
                dialog.dismiss();
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == FROM_GALLERY && resultCode == RESULT_OK) {

            if (data.getData() != null) {

                Uri mImageUri = data.getData();
                setImages(mImageUri);
            }

        } else if (requestCode == FROM_CAMERA && resultCode == RESULT_OK) {

            Bitmap photo = (Bitmap) data.getExtras().get("data");
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            photo.compress(Bitmap.CompressFormat.PNG, 100, bytes);
            Bitmap bmp = Bitmap.createScaledBitmap(photo, 50, 60, true);
            String path = MediaStore.Images.Media.insertImage(getActivity().getContentResolver(), bmp, "Title", null);
            Uri filePathUri = Uri.parse(path);

            setImages(filePathUri);


        }
    }

    private void setImages(Uri mImageUri) {
        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), mImageUri);

        } catch (IOException e) {
            e.printStackTrace();
        }
        assert bitmap != null;
        String path = getRealPathFromURI(getImageUri(getActivity(), bitmap));

        Log.i("Path**", "" + path);
        iv_stopImage.setImageURI(mImageUri);
        stopImagelFile = new File(path);

    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.PNG, 20, bytes);
        Bitmap bmp = Bitmap.createScaledBitmap(inImage, 50, 60, true);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), bmp, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }


}
