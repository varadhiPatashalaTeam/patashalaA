package in.varadhismartek.patashalaerp.DashboardModule.EmployeeModule;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import in.varadhismartek.Utils.Constant;
import in.varadhismartek.patashalaerp.GeneralClass.CircleImageView;
import in.varadhismartek.patashalaerp.R;
import in.varadhismartek.patashalaerp.Retrofit.APIService;
import in.varadhismartek.patashalaerp.Retrofit.ApiUtils;
import in.varadhismartek.patashalaerp.Retrofit.ApiUtilsPatashala;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_EmployeeView extends Fragment implements View.OnClickListener {
    TextView tvTitle, tvNoRecords;
    LinearLayout tv_attendance;
    LinearLayout ll_employee;
    String empID, FName, LName, AdhaarNo, PhoneNo;
    APIService mApiService;


    TextView tv_firstName, tv_middleName, tv_lastName, tv_gender, tv_blood, tv_caste, tv_birthPlace, tv_dob, tv_nationality, tv_doJoin, tv_married, tv_childStatus,
            tv_mobile, tv_email;
    TextView tv_fatherName, tv_fatherOcc, tv_motherName, tv_motherOcc, tv_spouseName, tv_spouseOcc, tv_guardName, tv_guardOcc;
    TextView tv_address1, tv_landmark1, tv_contact1, tv_address2, tv_landmark2, tv_contact2;
    TextView tv_schoolNameX, tv_schoolBoardX, tv_schoolPercentX, tv_schoolPassingX, tv_schoolStateX;
    TextView tv_schoolNameXII, tv_schoolBoardXII, tv_schoolPercentXII, tv_schoolPassingXII, tv_schoolStateXII;
    TextView tv_ugUniversityName, tv_ugCollageName, tv_ugName, tv_ugBranch, tv_ugPercent, tv_ugPassingYear;
    TextView tv_pgUniversity, tv_pgCollage, tv_pgName, tv_pgBranch, tv_pgPercent, tv_pgPassingYear;
    TextView tv_phdUniversity, tv_phdCollage, tv_phdName, tv_phdBranch, tv_phdPercent, tv_phdPassingYear;
    TextView tv_bedUniversity, tv_bedCollage, tv_bedName, tv_bedBranch, tv_bedPercent, tv_bedPassingYear;
    TextView tv_height, tv_weight, tv_shortEye, tv_longEye, tv_colorVision, tv_commentEye, tv_teethProb, tv_commentTeeth, tv_rightEarF,
            tv_leftEarF, tv_hearingDiff, tv_commentEar, tv_anyDis, tv_commentDisability;

    RecyclerView recycler_view, rv_docs, rv_achievement, rv_sports, rv_child;
    ImageView iv_backBtn, ivClose, ivDownload;
    CircleImageView civ_image;
    TextView tv_name, tv_id, tv_dept, tv_role, tv_className, tv_sectionName;
    LinearLayout ll_rowDarkStaff, ll_rowDarkParent, ll_staffPersonal, ll_healthStudent, ll_addressStaff,
            ll_spouse, ll_guard;
    LinearLayout ll_personal, ll_academic, ll_health, ll_docs;
    LinearLayout ll_personal_view, ll_academic_view, ll_health_view, ll_docs_view;

    APIService mApiServicePatashala;
    ProgressDialog progressDialog;
    ProfileAdapter adapter;
    ProfileAdapter adapterDocs;
    ProfileAdapter adapterAchievement;
    ProfileAdapter adapterSports;
    ProfileAdapter adapterChild;
    ArrayList<ProfileModel> workExpList;
    ArrayList<ProfileModel> docsList;
    ArrayList<ProfileModel> achivementList;
    ArrayList<ProfileModel> sportsList;
    ArrayList<ProfileModel> childList;
    LinearLayout tvLeave, tv_homework, tv_account, ll_PaySlip, ll_IdCard;
    String empName, empId, empDept, empDesignation, empImage,empProfilePic;
    Dialog dialog = null;
    String spouse_designation, mothers_first_name, blood_group, spouse_email, weight, present_locality_name,
            wing, spouse_phone_no, pan_card_no, spouse_last_name, present_state, fathers_designation,
            present_street, driving_license_pic_back = "", permanent_contact_number, child_status,
            adhaar_card_pic_back = "", voter_id_pic_back = "", adhaar_card_no, present_country,
            pan_card_pic_front = "", permanent_door_no, permanent_pincode, present_pincode, nationality,
            short_eye_vision, spouse_organisation, permanent_landmark_name, dob, role, marriage_date,
            present_door_no, driving_license_no, spouse_first_name, long_eye_vision, voter_id_pic_front = "",
            birth_place, pan_card_pic_back = "", middle_name, passport_pic_front = "", sex,
            mothers_mobile_no, email, fathers_occupation, first_name, last_name, spouse_occupation,
            adhaar_card_pic_front = "", present_landmark_name, caste, present_contact_number, permanent_city,
            voter_id_no, permanent_country, height, fathers_first_name, phone_number, permanent_street,
            mothers_occupation, present_city, permanent_locality_name, passport_no, permanent_state,
            employee_photo;

    String employee_uuid, passport_pic_back, fathers_middle_name, driving_license_pic_front = "",
            marital_status, mothers_organisation, community, department, mothers_last_name, fathers_last_name;

    String child_DOB, child_name;





    public Fragment_EmployeeView() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_employee_view, container, false);

        initViews(view);
        initListener();

        gettingEmployeePersonalDetailsAPI();

        setRecyclerView();
        setRecyclerViewDocs();
        setRecyclerViewSports();
        setRecyclerViewAchievement();
        setRecyclerViewChild();
        return view;
    }

    private void setRecyclerView() {

        adapter = new ProfileAdapter(workExpList, getActivity(), Constant.RV_PROFILE_WORK_EXPERIENCE);
        recycler_view.setLayoutManager(new LinearLayoutManager(getActivity()));
        recycler_view.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void setRecyclerViewDocs() {

        adapterDocs = new ProfileAdapter(docsList, getActivity(), Constant.RV_PROFILE_DOCUMENTS);
        rv_docs.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv_docs.setAdapter(adapterDocs);
        adapterDocs.notifyDataSetChanged();
    }

    private void setRecyclerViewAchievement() {

        adapterAchievement = new ProfileAdapter(achivementList, getActivity(), Constant.RV_PROFILE_ACHIEVEMENT);
        rv_achievement.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv_achievement.setAdapter(adapterAchievement);
        adapterAchievement.notifyDataSetChanged();
    }

    private void setRecyclerViewSports() {

        adapterSports = new ProfileAdapter(sportsList, getActivity(), Constant.RV_PROFILE_SPORTS_ROW);
        rv_sports.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv_sports.setAdapter(adapterSports);
        adapterSports.notifyDataSetChanged();
    }

    private void setRecyclerViewChild() {

        adapterChild = new ProfileAdapter(childList, getActivity(), Constant.RV_PROFILE_CHILDREN);
        rv_child.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv_child.setAdapter(adapterChild);
        adapterChild.notifyDataSetChanged();
    }

    private void initListener() {
        ll_academic.setOnClickListener(this);
        ll_personal.setOnClickListener(this);
        iv_backBtn.setOnClickListener(this);
        tv_attendance.setOnClickListener(this);
        ll_health.setOnClickListener(this);
        ll_docs.setOnClickListener(this);
        tvLeave.setOnClickListener(this);
        tv_homework.setOnClickListener(this);
        tv_account.setOnClickListener(this);
        ll_PaySlip.setOnClickListener(this);
        ll_IdCard.setOnClickListener(this);
    }


    private void gettingEmployeePersonalDetailsAPI() {

        //   progressDialog.show();
        Log.i("fragmentType**45", "" + empID + "**" + FName + "***" + LName + "*" +
                AdhaarNo + "***" + PhoneNo);


        mApiService.gettingEmployeePersonalDetails(Constant.SCHOOL_ID, FName, LName,
                PhoneNo, AdhaarNo).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {
                Log.i("fragmentType**45", "" + response.code() + "**" + response.body());
                if (response.isSuccessful()) {

                    try {

                        tvNoRecords.setVisibility(View.GONE);
                        ll_employee.setVisibility(View.VISIBLE);

                        JSONObject object = new JSONObject(new Gson().toJson(response.body()));
                        String status = object.getString("status");
                        String message = object.getString("message");
                        Log.i("fragmentType**666", "" + object.toString());
                        if (status.equalsIgnoreCase("Success")) {

                            JSONObject jsonData = object.getJSONObject("data");
                            JSONObject empData = jsonData.getJSONObject("employee_data");


                            if (!empData.isNull("employee_uuid")) {
                                employee_uuid = empData.getString("employee_uuid");
                            }
                            if (!empData.isNull("passport_pic_back")) {
                                passport_pic_back = empData.getString("passport_pic_back");
                            }
                            if (!empData.isNull("fathers_middle_name")) {
                                fathers_middle_name = empData.getString("fathers_middle_name");
                            }
                            if (!empData.isNull("driving_license_pic_front")) {
                                driving_license_pic_front = empData.getString("driving_license_pic_front");
                            }
                            if (!empData.isNull("marital_status")) {
                                marital_status = empData.getString("marital_status");
                            }
                            if (!empData.isNull("mothers_organisation")) {
                                mothers_organisation = empData.getString("mothers_organisation");
                            }
                            if (!empData.isNull("community")) {
                                community = empData.getString("community");
                            }
                            if (!empData.isNull("department")) {
                                department = empData.getString("department");
                            }
                            if (!empData.isNull("mothers_last_name")) {
                                mothers_last_name = empData.getString("mothers_last_name");
                            }
                            if (!empData.isNull("fathers_last_name")) {
                                fathers_last_name = empData.getString("fathers_last_name");
                            }
                            if (!empData.isNull("spouse_designation")) {
                                spouse_designation = empData.getString("spouse_designation");
                            }
                            if (!empData.isNull("mothers_first_name")) {
                                mothers_first_name = empData.getString("mothers_first_name");
                            }
                            if (!empData.isNull("blood_group")) {
                                blood_group = empData.getString("blood_group");
                            }
                            if (!empData.isNull("spouse_email")) {
                                spouse_email = empData.getString("spouse_email");
                            }
                            if (!empData.isNull("weight")) {
                                weight = empData.getString("weight");
                            }
                            if (!empData.isNull("present_locality_name")) {
                                present_locality_name = empData.getString("present_locality_name");
                            }
                            if (!empData.isNull("wing")) {
                                wing = empData.getString("wing");
                            }
                            if (!empData.isNull("spouse_phone_no")) {
                                spouse_phone_no = empData.getString("spouse_phone_no");
                            }
                            if (!empData.isNull("pan_card_no")) {
                                pan_card_no = empData.getString("pan_card_no");
                            }
                            if (!empData.isNull("spouse_last_name")) {
                                spouse_last_name = empData.getString("spouse_last_name");
                            }
                            if (!empData.isNull("present_state")) {
                                present_state = empData.getString("present_state");
                            }
                            if (!empData.isNull("fathers_designation")) {
                                fathers_designation = empData.getString("fathers_designation");
                            }
                            if (!empData.isNull("present_street")) {
                                present_street = empData.getString("present_street");
                            }
                            if (!empData.isNull("driving_license_pic_back")) {
                                driving_license_pic_back = empData.getString("driving_license_pic_back");
                            }
                            if (!empData.isNull("permanent_contact_number")) {
                                permanent_contact_number = empData.getString("permanent_contact_number");
                            }
                            if (!empData.isNull("child_status")) {
                                child_status = empData.getString("child_status");
                            }
                            if (!empData.isNull("adhaar_card_pic_back")) {
                                adhaar_card_pic_back = empData.getString("adhaar_card_pic_back");
                            }
                            if (!empData.isNull("voter_id_pic_back")) {
                                voter_id_pic_back = empData.getString("voter_id_pic_back");
                            }
                            if (!empData.isNull("adhaar_card_no")) {
                                adhaar_card_no = empData.getString("adhaar_card_no");
                            }
                            if (!empData.isNull("present_country")) {
                                present_country = empData.getString("present_country");
                            }
                            if (!empData.isNull("pan_card_pic_front")) {
                                pan_card_pic_front = empData.getString("pan_card_pic_front");
                            }
                            if (!empData.isNull("permanent_door_no")) {
                                permanent_door_no = empData.getString("permanent_door_no");
                            }
                            if (!empData.isNull("permanent_pincode")) {
                                permanent_pincode = empData.getString("permanent_pincode");
                            }
                            if (!empData.isNull("present_pincode")) {
                                present_pincode = empData.getString("present_pincode");
                            }
                            if (!empData.isNull("nationality")) {
                                nationality = empData.getString("nationality");
                            }
                            if (!empData.isNull("short_eye_vision")) {
                                short_eye_vision = empData.getString("short_eye_vision");
                            }
                            if (!empData.isNull("spouse_organisation")) {
                                spouse_organisation = empData.getString("spouse_organisation");
                            }

                            if (!empData.isNull("permanent_landmark_name")) {
                                permanent_landmark_name = empData.getString("permanent_landmark_name");
                            }
                            if (!empData.isNull("dob")) {
                                dob = empData.getString("dob");
                            }
                            if (!empData.isNull("role")) {
                                role = empData.getString("role");
                            }
                            if (!empData.isNull("marriage_date")) {
                                marriage_date = empData.getString("marriage_date");
                            }
                            if (!empData.isNull("present_door_no")) {
                                present_door_no = empData.getString("present_door_no");
                            }
                            if (!empData.isNull("driving_license_no")) {
                                driving_license_no = empData.getString("driving_license_no");
                            }
                            if (!empData.isNull("spouse_first_name")) {
                                spouse_first_name = empData.getString("spouse_first_name");
                            }
                            if (!empData.isNull("long_eye_vision")) {
                                long_eye_vision = empData.getString("long_eye_vision");
                            }
                            if (!empData.isNull("voter_id_pic_front")) {
                                voter_id_pic_front = empData.getString("voter_id_pic_front");
                            }
                            if (!empData.isNull("birth_place")) {
                                birth_place = empData.getString("birth_place");

                            }
                            if (!empData.isNull("pan_card_pic_back")) {
                                pan_card_pic_back = empData.getString("pan_card_pic_back");
                            }
                            if (!empData.isNull("middle_name")) {
                                middle_name = empData.getString("middle_name");
                            }
                            if (!empData.isNull("passport_pic_front")) {
                                passport_pic_front = empData.getString("passport_pic_front");
                            }
                            if (!empData.isNull("sex")) {
                                sex = empData.getString("sex");
                            }
                            if (!empData.isNull("mothers_mobile_no")) {
                                mothers_mobile_no = empData.getString("mothers_mobile_no");
                            }
                            if (!empData.isNull("email")) {
                                email = empData.getString("email");
                            }
                            if (!empData.isNull("fathers_occupation")) {
                                fathers_occupation = empData.getString("fathers_occupation");
                            }
                            if (!empData.isNull("first_name")) {
                                first_name = empData.getString("first_name");
                            }
                            if (!empData.isNull("last_name")) {
                                last_name = empData.getString("last_name");
                            }
                            if (!empData.isNull("spouse_occupation")) {
                                spouse_occupation = empData.getString("spouse_occupation");
                            }
                            if (!empData.isNull("adhaar_card_pic_front")) {
                                adhaar_card_pic_front = empData.getString("adhaar_card_pic_front");
                            }
                            if (!empData.isNull("present_landmark_name")) {
                                present_landmark_name = empData.getString("present_landmark_name");
                            }
                            if (!empData.isNull("caste")) {
                                caste = empData.getString("caste");
                            }

                            if (!empData.isNull("present_contact_number")) {
                                present_contact_number = empData.getString("present_contact_number");
                            }
                            if (!empData.isNull("permanent_city")) {
                                permanent_city = empData.getString("permanent_city");
                            }
                            if (!empData.isNull("voter_id_no")) {
                                voter_id_no = empData.getString("voter_id_no");
                            }
                            if (!empData.isNull("permanent_country")) {
                                permanent_country = empData.getString("permanent_country");
                            }
                            if (!empData.isNull("height")) {
                                height = empData.getString("height");
                            }
                            if (!empData.isNull("fathers_first_name")) {
                                fathers_first_name = empData.getString("fathers_first_name");
                            }
                            if (!empData.isNull("phone_number")) {
                                phone_number = empData.getString("phone_number");
                            }

                            if (!empData.isNull("date_of_joining")) {
                                String date_of_joining = empData.getString("date_of_joining");
                                //   tv_doJoin.setText(date_of_joining);
                            }

                            if (!empData.isNull("permanent_street")) {
                                permanent_street = empData.getString("permanent_street");
                            }
                            if (!empData.isNull("mothers_occupation")) {
                                mothers_occupation = empData.getString("mothers_occupation");
                            }
                            if (!empData.isNull("present_city")) {
                                present_city = empData.getString("present_city");
                            }
                            if (!empData.isNull("permanent_locality_name")) {
                                permanent_locality_name = empData.getString("permanent_locality_name");
                            }
                            if (!empData.isNull("passport_no")) {
                                passport_no = empData.getString("passport_no");
                            }
                            if (!empData.isNull("permanent_state")) {
                                permanent_state = empData.getString("permanent_state");
                            }
                            if (!empData.isNull("employee_photo")) {
                                employee_photo = empData.getString("employee_photo");
                            }


                            Log.d("Profile_api_success", response.code() + " " + message);

                            docsList.add(new ProfileModel("Adhar Card",
                                    adhaar_card_no, adhaar_card_pic_back, adhaar_card_pic_front));
                            docsList.add(new ProfileModel("Voter Card",
                                    voter_id_no, voter_id_pic_back, voter_id_pic_front));
                            docsList.add(new ProfileModel("Pan Card",
                                    pan_card_no, pan_card_pic_back, pan_card_pic_front));
                            docsList.add(new ProfileModel("Driving Licence",
                                    driving_license_no, driving_license_pic_back, driving_license_pic_front));
                            docsList.add(new ProfileModel("Passport Card",
                                    passport_no, passport_pic_back, passport_pic_front));

                            empProfilePic = employee_photo;
                            adapterDocs.notifyDataSetChanged();

                            empName = first_name + " " + last_name;
                            empId = employee_uuid;
                            empDept = department;
                            empDesignation = role;
                            empImage = employee_photo;

                            tv_name.setText(first_name + " " + last_name);
                            tv_id.setText(employee_uuid);
                            tv_dept.setText(department);
                            tv_role.setText(role);

                            tv_firstName.setText(first_name);
                            tv_middleName.setText(middle_name);
                            tv_lastName.setText(last_name);
                            tv_gender.setText(sex);
                            tv_blood.setText(blood_group);
                            tv_caste.setText(caste);
                            tv_birthPlace.setText(birth_place);
                            tv_dob.setText(dob);
                            tv_nationality.setText(nationality);
                            tv_married.setText(marital_status);
                            tv_childStatus.setText(child_status);
                            tv_mobile.setText(phone_number);
                            tv_email.setText(email);

                            tv_fatherName.setText(fathers_first_name + " " + fathers_last_name);
                            tv_fatherOcc.setText(fathers_occupation);
                            tv_motherName.setText(mothers_first_name + " " + mothers_last_name);
                            tv_motherOcc.setText(mothers_occupation);
                            tv_spouseName.setText(spouse_first_name + " " + spouse_last_name);
                            tv_spouseOcc.setText(spouse_occupation);

                            tv_address1.setText(present_door_no + ", " + present_street + ", " + present_locality_name + ", " + present_city + ", " + present_state +
                                    ", " + present_country + "-" + present_pincode);
                            tv_landmark1.setText(present_landmark_name);
                            tv_contact1.setText(present_contact_number);
                            tv_address2.setText(permanent_door_no + ", " + permanent_street + ", " + permanent_locality_name + ", " + permanent_city + ", " + permanent_state +
                                    ", " + permanent_country + "-" + permanent_pincode);
                            tv_landmark2.setText(permanent_landmark_name);
                            tv_contact2.setText(permanent_contact_number);

                            tv_height.setText(height);
                            tv_weight.setText(weight);
                            tv_shortEye.setText(short_eye_vision);
                            tv_longEye.setText(long_eye_vision);

                            final String imageUrlg = "http://13.233.109.165:8000/media/";
                            assert getActivity() != null;
                            Glide.with(getActivity()).load(imageUrlg + employee_photo).into(civ_image);

                            JSONArray jsonArray = jsonData.getJSONArray("childeren_details");

                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject childObject = jsonArray.getJSONObject(i);

                                if (!childObject.isNull("child_DOB")) {
                                    child_DOB = childObject.getString("child_DOB");
                                }
                                if (!childObject.isNull("child_name")) {
                                    child_name = childObject.getString("child_name");
                                }


                                childList.add(new ProfileModel(child_name, child_DOB));

                            }

                            adapterChild.notifyDataSetChanged();
                            Log.d("Profile_api_success", response.code() + " " + message);


                        } else {
                            Log.d("Profile_api_else", response.code() + " " + message);


                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    try {
                        assert response.errorBody() != null;
                        JSONObject object = new JSONObject(response.errorBody().string());
                        String message = object.getString("message");
                        Log.d("ADMIN_API", message);
                        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                        tvNoRecords.setVisibility(View.VISIBLE);
                        ll_employee.setVisibility(View.GONE);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<Object> call, @NonNull Throwable t) {

                Log.d("safpjsaiofi", t.toString());
            }
        });

    }


    private void initViews(View view) {
        mApiService = ApiUtils.getAPIService();
        ll_PaySlip = view.findViewById(R.id.ll_PaySlip);
        ll_IdCard = view.findViewById(R.id.ll_IdCard);
        tv_attendance = view.findViewById(R.id.tv_attendance);
        tvLeave = view.findViewById(R.id.tv_leave);
        tv_homework = view.findViewById(R.id.tv_homework);
        ll_employee = view.findViewById(R.id.ll_employee);
        tvNoRecords = view.findViewById(R.id.tvNorecords);
        // tvTitle.setText("Employee Profile ");

        mApiServicePatashala = ApiUtilsPatashala.getService();
        //progressDialog = new ProgressDialog(getActivity());
        //progressDialog.setMessage("Please Wait");

        workExpList = new ArrayList<>();
        docsList = new ArrayList<>();
        achivementList = new ArrayList<>();
        sportsList = new ArrayList<>();
        childList = new ArrayList<>();

        recycler_view = view.findViewById(R.id.recycler_view);
        rv_docs = view.findViewById(R.id.rv_docs);
        rv_achievement = view.findViewById(R.id.rv_achievement);
        rv_sports = view.findViewById(R.id.rv_sports);
        rv_child = view.findViewById(R.id.rv_child);

        iv_backBtn = view.findViewById(R.id.iv_backBtn);
        civ_image = view.findViewById(R.id.civ_image);
        tv_name = view.findViewById(R.id.tv_name);
        tv_id = view.findViewById(R.id.tv_id);

        ll_rowDarkStaff = view.findViewById(R.id.ll_rowDarkStaff);
        ll_rowDarkParent = view.findViewById(R.id.ll_rowDarkParent);
        ll_healthStudent = view.findViewById(R.id.ll_healthStudent);
        ll_staffPersonal = view.findViewById(R.id.ll_staffPersonal);
        ll_addressStaff = view.findViewById(R.id.ll_addressStaff);
        ll_spouse = view.findViewById(R.id.ll_spouse);
        ll_guard = view.findViewById(R.id.ll_guard);

        tv_dept = view.findViewById(R.id.tv_dept);
        tv_role = view.findViewById(R.id.tv_role);
        tv_className = view.findViewById(R.id.tv_className);
        tv_sectionName = view.findViewById(R.id.tv_sectionName);

        tv_account = view.findViewById(R.id.tv_account);
        ll_personal = view.findViewById(R.id.ll_personal);
        ll_academic = view.findViewById(R.id.ll_academic);
        ll_health = view.findViewById(R.id.ll_health);
        ll_docs = view.findViewById(R.id.ll_docs);
        ll_personal_view = view.findViewById(R.id.ll_personal_view);
        ll_academic_view = view.findViewById(R.id.ll_academic_view);
        ll_health_view = view.findViewById(R.id.ll_health_view);
        ll_docs_view = view.findViewById(R.id.ll_docs_view);

        tv_firstName = view.findViewById(R.id.tv_firstName);
        tv_middleName = view.findViewById(R.id.tv_middleName);
        tv_lastName = view.findViewById(R.id.tv_lastName);
        tv_gender = view.findViewById(R.id.tv_gender);
        tv_blood = view.findViewById(R.id.tv_blood);
        tv_caste = view.findViewById(R.id.tv_caste);
        tv_birthPlace = view.findViewById(R.id.tv_birthPlace);
        tv_dob = view.findViewById(R.id.tv_dob);
        tv_nationality = view.findViewById(R.id.tv_nationality);
        tv_doJoin = view.findViewById(R.id.tv_doJoin);
        tv_married = view.findViewById(R.id.tv_married);
        tv_childStatus = view.findViewById(R.id.tv_childStatus);
        tv_mobile = view.findViewById(R.id.tv_mobile);
        tv_email = view.findViewById(R.id.tv_email);

        tv_fatherName = view.findViewById(R.id.tv_fatherName);
        tv_fatherOcc = view.findViewById(R.id.tv_fatherOcc);
        tv_motherName = view.findViewById(R.id.tv_motherName);
        tv_motherOcc = view.findViewById(R.id.tv_motherOcc);
        tv_spouseName = view.findViewById(R.id.tv_spouseName);
        tv_spouseOcc = view.findViewById(R.id.tv_spouseOcc);
        tv_guardName = view.findViewById(R.id.tv_guardName);
        tv_guardOcc = view.findViewById(R.id.tv_guardOcc);

        tv_address1 = view.findViewById(R.id.tv_address1);
        tv_landmark1 = view.findViewById(R.id.tv_landmark1);
        tv_contact1 = view.findViewById(R.id.tv_contact1);
        tv_address2 = view.findViewById(R.id.tv_address2);
        tv_landmark2 = view.findViewById(R.id.tv_landmark2);
        tv_contact2 = view.findViewById(R.id.tv_contact2);

        tv_schoolNameX = view.findViewById(R.id.tv_schoolNameX);
        tv_schoolBoardX = view.findViewById(R.id.tv_schoolBoardX);
        tv_schoolPercentX = view.findViewById(R.id.tv_schoolPercentX);
        tv_schoolPassingX = view.findViewById(R.id.tv_schoolPassingX);
        tv_schoolStateX = view.findViewById(R.id.tv_schoolStateX);

        tv_schoolNameXII = view.findViewById(R.id.tv_schoolNameXII);
        tv_schoolBoardXII = view.findViewById(R.id.tv_schoolBoardXII);
        tv_schoolPercentXII = view.findViewById(R.id.tv_schoolPercentXII);
        tv_schoolPassingXII = view.findViewById(R.id.tv_schoolPassingXII);
        tv_schoolStateXII = view.findViewById(R.id.tv_schoolStateXII);

        tv_ugUniversityName = view.findViewById(R.id.tv_ugUniversityName);
        tv_ugCollageName = view.findViewById(R.id.tv_ugCollageName);
        tv_ugName = view.findViewById(R.id.tv_ugName);
        tv_ugBranch = view.findViewById(R.id.tv_ugBranch);
        tv_ugPercent = view.findViewById(R.id.tv_ugPercent);
        tv_ugPassingYear = view.findViewById(R.id.tv_ugPassingYear);

        tv_pgUniversity = view.findViewById(R.id.tv_pgUniversity);
        tv_pgCollage = view.findViewById(R.id.tv_pgCollage);
        tv_pgName = view.findViewById(R.id.tv_pgName);
        tv_pgBranch = view.findViewById(R.id.tv_pgBranch);
        tv_pgPercent = view.findViewById(R.id.tv_pgPercent);
        tv_pgPassingYear = view.findViewById(R.id.tv_pgPassingYear);

        tv_phdUniversity = view.findViewById(R.id.tv_phdUniversity);
        tv_phdCollage = view.findViewById(R.id.tv_phdCollage);
        tv_phdName = view.findViewById(R.id.tv_phdName);
        tv_phdBranch = view.findViewById(R.id.tv_phdBranch);
        tv_phdPercent = view.findViewById(R.id.tv_phdPercent);
        tv_phdPassingYear = view.findViewById(R.id.tv_phdPassingYear);

        tv_bedUniversity = view.findViewById(R.id.tv_bedUniversity);
        tv_bedCollage = view.findViewById(R.id.tv_bedCollage);
        tv_bedName = view.findViewById(R.id.tv_bedName);
        tv_bedBranch = view.findViewById(R.id.tv_bedBranch);
        tv_bedPercent = view.findViewById(R.id.tv_bedPercent);
        tv_bedPassingYear = view.findViewById(R.id.tv_bedPassingYear);

        tv_height = view.findViewById(R.id.tv_height);
        tv_weight = view.findViewById(R.id.tv_weight);
        tv_shortEye = view.findViewById(R.id.tv_shortEye);
        tv_longEye = view.findViewById(R.id.tv_longEye);
        tv_colorVision = view.findViewById(R.id.tv_colorVision);
        tv_commentEye = view.findViewById(R.id.tv_commentEye);
        tv_teethProb = view.findViewById(R.id.tv_teethProb);
        tv_commentTeeth = view.findViewById(R.id.tv_commentTeeth);
        tv_rightEarF = view.findViewById(R.id.tv_rightEarF);
        tv_leftEarF = view.findViewById(R.id.tv_leftEarF);
        tv_hearingDiff = view.findViewById(R.id.tv_hearingDiff);
        tv_commentEar = view.findViewById(R.id.tv_commentEar);
        tv_anyDis = view.findViewById(R.id.tv_anyDis);
        tv_commentDisability = view.findViewById(R.id.tv_commentDisability);

        Bundle b = getArguments();
        if (b != null) {
            empID = b.getString("EMPUUID");
            FName = b.getString("FNAME");
            LName = b.getString("LNAME");
            AdhaarNo = b.getString("ADHAARNO");
            PhoneNo = b.getString("PHONENO");
        }

        Log.i("fragmentType**88", "" + empID + "**" + FName + "***" + LName + "*" + AdhaarNo + "***" + PhoneNo);

    }

    @Override
    public void onClick(View v) {
        EmployeeActivity employeeActivity = (EmployeeActivity) getActivity();
        Bundle bundle;
        switch (v.getId()) {
            case R.id.ll_PaySlip:
                ll_PaySlip.setBackgroundResource(R.drawable.border_rect);
                tv_account.setBackgroundResource(R.drawable.border);
                tvLeave.setBackgroundResource(R.drawable.border);
                tv_homework.setBackgroundResource(R.drawable.border);
                tv_attendance.setBackgroundResource(R.drawable.border);
                ll_academic.setBackgroundResource(R.drawable.border);
                ll_health.setBackgroundResource(R.drawable.border);
                ll_docs.setBackgroundResource(R.drawable.border);
                ll_health_view.setVisibility(View.GONE);
                ll_docs_view.setVisibility(View.GONE);
                ll_personal_view.setVisibility(View.GONE);
                tv_attendance.setVisibility(View.VISIBLE);

                bundle = new Bundle();
                bundle.putString("EMPUUID", empID);
                bundle.putString("EMP_NAME", empName);
                bundle.putString("EMP_DEPT", empDept);
                bundle.putString("EMP_DESIGNATION", empDesignation);
                bundle.putString("EMP_IMAGE", empImage);
                bundle.putString("EMPLOYEE", "EMP_PAT_LEAVE");
                //String empName, empId,  empDept, empDesignation;
                employeeActivity.loadFragment(Constant.EMPLOYEE_PAYSLIP, bundle);
                break;

            case R.id.tv_account:
                tv_account.setBackgroundResource(R.drawable.border_rect);
                ll_PaySlip.setBackgroundResource(R.drawable.border);
                tvLeave.setBackgroundResource(R.drawable.border);
                tv_homework.setBackgroundResource(R.drawable.border);
                tv_attendance.setBackgroundResource(R.drawable.border);
                ll_academic.setBackgroundResource(R.drawable.border);
                ll_health.setBackgroundResource(R.drawable.border);
                ll_docs.setBackgroundResource(R.drawable.border);
                ll_health_view.setVisibility(View.GONE);
                ll_docs_view.setVisibility(View.GONE);
                ll_personal_view.setVisibility(View.GONE);
                tv_attendance.setVisibility(View.VISIBLE);

                bundle = new Bundle();
                bundle.putString("EMPUUID", empID);
                bundle.putString("EMP_IMAGE", empImage);
                bundle.putString("EMP_NAME", empName);
                bundle.putString("EMP_DEPT", empDept);
                bundle.putString("EMP_DESIGNATION", empDesignation);
                bundle.putString("EMPLOYEE", "EMP_PAT_LEAVE");
                employeeActivity.loadFragment(Constant.EMPLOYEE_ACCOUNT_DETAILS, bundle);
                break;


            case R.id.tv_leave:
                tvLeave.setBackgroundResource(R.drawable.border_rect);
                tv_account.setBackgroundResource(R.drawable.border);
                ll_PaySlip.setBackgroundResource(R.drawable.border);
                tv_homework.setBackgroundResource(R.drawable.border);
                tv_attendance.setBackgroundResource(R.drawable.border);
                ll_academic.setBackgroundResource(R.drawable.border);
                ll_health.setBackgroundResource(R.drawable.border);
                ll_docs.setBackgroundResource(R.drawable.border);
                ll_health_view.setVisibility(View.GONE);
                ll_docs_view.setVisibility(View.GONE);
                ll_personal_view.setVisibility(View.GONE);
                tv_attendance.setVisibility(View.VISIBLE);

                bundle = new Bundle();
                bundle.putString("EMPUUID", empID);
                bundle.putString("EMPLOYEE", "EMP_PAT_LEAVE");
                employeeActivity.loadFragment(Constant.EMPLOYEE_LEAVE, bundle);
                break;

            case R.id.tv_homework:
                tv_homework.setBackgroundResource(R.drawable.border_rect);
                tv_account.setBackgroundResource(R.drawable.border);
                ll_PaySlip.setBackgroundResource(R.drawable.border);
                tvLeave.setBackgroundResource(R.drawable.border);
                tv_attendance.setBackgroundResource(R.drawable.border);
                ll_academic.setBackgroundResource(R.drawable.border);
                ll_health.setBackgroundResource(R.drawable.border);
                ll_docs.setBackgroundResource(R.drawable.border);

                ll_health_view.setVisibility(View.GONE);
                ll_docs_view.setVisibility(View.GONE);
                ll_personal_view.setVisibility(View.GONE);
                tv_attendance.setVisibility(View.VISIBLE);

                bundle = new Bundle();
                bundle.putString("EMPUUID", empID);
                bundle.putString("STAFF", "EMPLOYEE");
                employeeActivity.loadFragment(Constant.EMPLOYEE_HOMEWORK, bundle);
                break;


            case R.id.tv_attendance:
                tv_attendance.setBackgroundResource(R.drawable.border_rect);
                tv_account.setBackgroundResource(R.drawable.border);
                ll_PaySlip.setBackgroundResource(R.drawable.border);
                ll_academic.setBackgroundResource(R.drawable.border);
                tv_homework.setBackgroundResource(R.drawable.border);
                ll_health.setBackgroundResource(R.drawable.border);
                ll_docs.setBackgroundResource(R.drawable.border);
                ll_health_view.setVisibility(View.GONE);
                ll_docs_view.setVisibility(View.GONE);
                ll_personal_view.setVisibility(View.GONE);
                tv_attendance.setVisibility(View.VISIBLE);
                // EmployeeActivity employeeActivity = (EmployeeActivity) getActivity();
                bundle = new Bundle();
                bundle.putString("EMPUUID", empID);
                bundle.putString("FNAME", FName);
                bundle.putString("LNAME", LName);
                Constant.audience_type = "";
                employeeActivity.loadFragment(Constant.ATTENDANCE_FRAGMENT, bundle);

                break;
            case R.id.iv_backBtn:
                getActivity().onBackPressed();
                break;

            case R.id.ll_personal:
                ll_personal.setBackgroundResource(R.drawable.border_rect);
                tv_account.setBackgroundResource(R.drawable.border);
                ll_PaySlip.setBackgroundResource(R.drawable.border);
                tv_homework.setBackgroundResource(R.drawable.border);
                tv_attendance.setBackgroundResource(R.drawable.border);
                ll_academic.setBackgroundResource(R.drawable.border);
                ll_health.setBackgroundResource(R.drawable.border);
                ll_docs.setBackgroundResource(R.drawable.border);
                ll_personal_view.setVisibility(View.VISIBLE);
                ll_academic_view.setVisibility(View.GONE);
                ll_health_view.setVisibility(View.GONE);
                ll_docs_view.setVisibility(View.GONE);

               /* if (Constant.audience_type.equalsIgnoreCase("Parent"))
                    ll_academic_view.setVisibility(View.GONE);
*/

                break;

            case R.id.ll_academic:

                ll_personal.setBackgroundResource(R.drawable.border);
                tv_account.setBackgroundResource(R.drawable.border);
                ll_PaySlip.setBackgroundResource(R.drawable.border);
                ll_academic.setBackgroundResource(R.drawable.border_rect);
                tv_attendance.setBackgroundResource(R.drawable.border);
                tv_homework.setBackgroundResource(R.drawable.border);
                ll_health.setBackgroundResource(R.drawable.border);
                ll_docs.setBackgroundResource(R.drawable.border);
                ll_personal_view.setVisibility(View.GONE);
                ll_academic_view.setVisibility(View.VISIBLE);
                ll_health_view.setVisibility(View.GONE);
                ll_docs_view.setVisibility(View.GONE);
                /*if (Constant.audience_type.equalsIgnoreCase("Parent"))
                    ll_academic_view.setVisibility(View.GONE);*/
                break;

            case R.id.ll_health:
                tv_attendance.setBackgroundResource(R.drawable.border);
                tv_homework.setBackgroundResource(R.drawable.border);
                ll_PaySlip.setBackgroundResource(R.drawable.border);
                tv_account.setBackgroundResource(R.drawable.border);
                ll_personal.setBackgroundResource(R.drawable.border);
                ll_academic.setBackgroundResource(R.drawable.border);
                ll_health.setBackgroundResource(R.drawable.border_rect);
                ll_docs.setBackgroundResource(R.drawable.border);
                ll_personal_view.setVisibility(View.GONE);
                ll_academic_view.setVisibility(View.GONE);
                ll_health_view.setVisibility(View.VISIBLE);
                ll_docs_view.setVisibility(View.GONE);
                /*if (Constant.audience_type.equalsIgnoreCase("Parent"))
                    ll_academic_view.setVisibility(View.GONE);*/
                break;

            case R.id.ll_docs:
                tv_attendance.setBackgroundResource(R.drawable.border);
                ll_personal.setBackgroundResource(R.drawable.border);
                ll_PaySlip.setBackgroundResource(R.drawable.border);
                tv_account.setBackgroundResource(R.drawable.border);
                tv_homework.setBackgroundResource(R.drawable.border);
                ll_academic.setBackgroundResource(R.drawable.border);
                ll_health.setBackgroundResource(R.drawable.border);
                ll_docs.setBackgroundResource(R.drawable.border_rect);
                ll_personal_view.setVisibility(View.GONE);
                ll_academic_view.setVisibility(View.GONE);
                ll_health_view.setVisibility(View.GONE);
                ll_docs_view.setVisibility(View.VISIBLE);
               /* if (Constant.audience_type.equalsIgnoreCase("Parent"))
                    ll_academic_view.setVisibility(View.GONE);*/
                break;


            case R.id.ll_IdCard:
                generateIdCard();
                break;


        }
    }

    private void generateIdCard() {


        dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.id_card_layout);
        dialog.show();
        dialog.setCancelable(false);


        ImageView imageView = dialog.findViewById(R.id.qrImage);
        ImageView memberImage = dialog.findViewById(R.id.idStaffImage);
        TextView memberName = dialog.findViewById(R.id.staffName);
        TextView memberID = dialog.findViewById(R.id.staffId);
        TextView textViewCons = dialog.findViewById(R.id.idStaffDept);
        TextView textViewWard = dialog.findViewById(R.id.idStaffRole);
        ivDownload = dialog.findViewById(R.id.ivdownLoad);
        ivClose = dialog.findViewById(R.id.ivclose);


        if (empName.length() > 0) {

            memberName.setText("" + empName);

        }

        if (empID.length() > 0) {

            memberID.setText(empId.substring(0, 15));

        }

        if (empDept.length() > 0) {

            textViewCons.setText(empDept);

        }

        if (empDept.length() > 0) {

            textViewWard.setText(empDesignation);

        }

        if (empImage.length() > 0) {

            Glide.with(getActivity()).load(Constant.IMAGE_URL + empImage).into(memberImage);

        }

        ivDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Download Option Blocked,Due to Demo Login", Toast.LENGTH_SHORT).show();
            }
        });

        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        generateQRCode(imageView);


    }

    private void generateQRCode(ImageView imageView) {

        String memberDetailsQr = " Name " + empName + " EmployeeId " + empId + " Department " + empDept + " Designation " + empDesignation;

        try {
            Bitmap qrbitmap;
            qrbitmap = TextToImageEncode(memberDetailsQr);

            ByteArrayOutputStream bytes1 = new ByteArrayOutputStream();
            qrbitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes1);

            String qrpath = MediaStore.Images.Media.insertImage(getContext().getContentResolver(), qrbitmap, "QrScanner", null);

            Uri qrscannerpath = Uri.parse(qrpath);
            imageView.setImageBitmap(qrbitmap);
        } catch (WriterException e) {

            e.printStackTrace();

        }


    }

    Bitmap TextToImageEncode(String text) throws WriterException {


        BitMatrix bitMatrix;

        try {

            bitMatrix = new MultiFormatWriter().encode(text,
                    BarcodeFormat.DATA_MATRIX.QR_CODE,
                    500, 500, null
            );

        } catch (IllegalArgumentException Illegalargumentexception) {

            return null;
        }

        int bitMatrixWidth = bitMatrix.getWidth();

        int bitMatrixHeight = bitMatrix.getHeight();

        int[] pixels = new int[bitMatrixWidth * bitMatrixHeight];

        for (int y = 0; y < bitMatrixHeight; y++) {

            int offset = y * bitMatrixWidth;

            for (int x = 0; x < bitMatrixWidth; x++) {

                pixels[offset + x] = bitMatrix.get(x, y) ?
                        Color.BLACK : Color.WHITE;
            }
        }

        Bitmap bitmap = Bitmap.createBitmap(bitMatrixWidth, bitMatrixHeight, Bitmap.Config.ARGB_4444);

        bitmap.setPixels(pixels, 0, 500, 0, 0, bitMatrixWidth, bitMatrixHeight);

        return bitmap;

    }


}
