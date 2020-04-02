package in.varadhismartek.patashalaerp.StudentModule;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;

import in.varadhismartek.Utils.Constant;
import in.varadhismartek.patashalaerp.DashboardModule.EmployeeModule.ProfileAdapter;
import in.varadhismartek.patashalaerp.DashboardModule.EmployeeModule.ProfileModel;
import in.varadhismartek.patashalaerp.GeneralClass.CircleImageView;
import in.varadhismartek.patashalaerp.R;
import in.varadhismartek.patashalaerp.Retrofit.APIService;
import in.varadhismartek.patashalaerp.Retrofit.ApiUtils;
import in.varadhismartek.patashalaerp.Retrofit.ApiUtilsPatashala;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StudentDetails_Fragment extends Fragment implements View.OnClickListener {

    String FName, LName, DOB, CertificateNo, StudentID;
    APIService apiService;

    TextView tv_gender, tv_blood_group, tv_cast, tv_dob;
    LinearLayout tv_attendance;

    TextView tv_firstName, tv_middleName, tv_lastName, tv_blood, tv_caste, tv_birthPlace, tv_nationality,
            tv_doJoin, tv_married, tv_childStatus, tv_mobile, tv_email;

    TextView tv_fatherName, tv_fatherOcc, tv_motherName, tv_motherOcc, tv_spouseName, tv_spouseOcc, tv_guardName, tv_guardOcc;
    TextView tv_address1, tv_landmark1, tv_contact1, tv_address2, tv_landmark2, tv_contact2;
    TextView tv_schoolNameX, tv_schoolBoardX, tv_schoolPercentX, tv_schoolPassingX, tv_schoolStateX;
    TextView tv_schoolNameXII, tv_schoolBoardXII, tv_schoolPercentXII, tv_schoolPassingXII, tv_schoolStateXII;
    TextView tv_ugUniversityName, tv_ugCollageName, tv_ugName, tv_ugBranch, tv_ugPercent, tv_ugPassingYear;
    TextView tv_pgUniversity, tv_pgCollage, tv_pgName, tv_pgBranch, tv_pgPercent, tv_pgPassingYear;
    TextView tv_phdUniversity, tv_phdCollage, tv_phdName, tv_phdBranch, tv_phdPercent, tv_phdPassingYear;
    TextView tv_bedUniversity, tv_bedCollage, tv_bedName, tv_bedBranch, tv_bedPercent, tv_bedPassingYear;
    TextView tv_height, tv_weight, tv_shortEye, tv_longEye, tv_colorVision, tv_commentEye, tv_teethProb, tv_commentTeeth,
            tv_rightEarF, tvNorecords, tv_leftEarF, tv_hearingDiff, tv_commentEar, tv_anyDis, tv_commentDisability;

    RecyclerView recycler_view, rv_docs, rv_achievement, rv_sports, rv_child;
    ImageView iv_backBtn;
    CircleImageView civ_image;
    String photo;
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
    LinearLayout tvLeave,tv_homework,tv_feesDetails;
    LinearLayout llStudentProfile;

    public StudentDetails_Fragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_student_details, container, false);

        initView(view);
        initListener();
        getStudentPersonalInfo();
        getStudentParentsDetailsAPI();
        //getStudentGuardianDetailsAPI();

        setRecyclerView();
        setRecyclerViewDocs();
        setRecyclerViewSports();
        setRecyclerViewAchievement();
        setRecyclerViewChild();
        return view;
    }


    private void initView(View view) {
        apiService = ApiUtils.getAPIService();

        mApiServicePatashala = ApiUtilsPatashala.getService();

        workExpList = new ArrayList<>();
        docsList = new ArrayList<>();
        achivementList = new ArrayList<>();
        sportsList = new ArrayList<>();
        childList = new ArrayList<>();

        tv_feesDetails = view.findViewById(R.id.tv_feesDetails);
        tv_attendance = view.findViewById(R.id.tv_attendance);
        llStudentProfile = view.findViewById(R.id.ll_student_profile);
        tvNorecords = view.findViewById(R.id.tvNorecords);

        tvLeave = view.findViewById(R.id.tv_leave);
        tv_homework = view.findViewById(R.id.tv_homework);
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

       /* tv_gender = view.findViewById(R.id.tv_gender);
        tv_blood_group = view.findViewById(R.id.tv_gender);
        tv_cast = view.findViewById(R.id.tv_gender);
        tv_dob = view.findViewById(R.id.tv_gender);

*/
        Bundle bundle = getArguments();

        if (bundle != null) {
            FName = bundle.getString("FName");
            LName = bundle.getString("LName");
            DOB = bundle.getString("DOB");
            CertificateNo = bundle.getString("CertificateNo");
            StudentID = bundle.getString("EEUID");


            Log.i("STUDENTID***", "SD" + StudentID);
            System.out.println("FName**" + FName + "**" + LName + "**" + DOB + "**" + CertificateNo);
        }

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
        tv_feesDetails.setOnClickListener(this);
    }

    private void getStudentPersonalInfo() {


        System.out.println("FName**22**" + FName + "**" + LName + "**" + DOB + "**" + CertificateNo);

        if (docsList.size() > 0)
            docsList.clear();

        apiService.getStudentPersonalDetails(Constant.SCHOOL_ID, FName, LName, CertificateNo, DOB)
                .enqueue(new Callback<Object>() {
                    @Override
                    public void onResponse(Call<Object> call, Response<Object> response) {

                        if (response.isSuccessful()) {

                            try {
                                llStudentProfile.setVisibility(View.VISIBLE);
                                tvNorecords.setVisibility(View.GONE);

                                JSONObject object = new JSONObject(new Gson().toJson(response.body()));
                                String status1 = object.getString("status");
                                String message = object.getString("message");

                                System.out.println("object***" + object.toString());
                                if (status1.equalsIgnoreCase("Success")) {
                                    String className = "", section = "";

                                    JSONObject jsonData = object.getJSONObject("student_data");

                                    String comments_on_disability = jsonData.getString("comments_on_disability");
                                    String gender = jsonData.getString("gender");
                                    String nationality = jsonData.getString("nationality");
                                    String subcaste = jsonData.getString("subcaste");
                                    //String teeth_sensivity = jsonData.getString("teeth_sensivity");

                                    String previous_school_marksheet_front_image = jsonData.getString("previous_school_marksheet_front_image");
                                    String previous_school_affiliated_to = jsonData.getString("previous_school_affiliated_to");
                                    String birth_certificate_number = jsonData.getString("birth_certificate_number");
                                    String mother_tongue = jsonData.getString("mother_tongue");
                                    String community = jsonData.getString("community");
                                    String transfer_certificate_number = jsonData.getString("transfer_certificate_number");
                                    String previous_school_name = jsonData.getString("previous_school_name");
                                    //String teeth_cavity = jsonData.getString("teeth_cavity");
                                    String long_eye_vision = jsonData.getString("long_eye_vision");
                                    String language_known = jsonData.getString("language_known");
                                    String religion = jsonData.getString("religion");
                                    String visual_disability = jsonData.getString("visual_disability");
                                    //String speech_disability = jsonData.getString("speech_disability");
                                    String height = jsonData.getString("height");

                                    if (jsonData.isNull("class")) {
                                        className = "NO Data";

                                    } else {
                                        className = jsonData.getString("class");

                                    }
                                    if (jsonData.isNull("section")) {
                                        section = "NO Data";

                                    } else {
                                        section = jsonData.getString("section");

                                    }


                                    String previous_school_marksheet_back_image = jsonData.getString("previous_school_marksheet_back_image");
                                    String house = jsonData.getString("house");
                                    //String physical_disability = jsonData.getString("physical_disability");
                                    String comments_on_eye = jsonData.getString("comments_on_eye");
                                    String previous_class_percentage = jsonData.getString("previous_class_percentage");
                                    String previous_school_contact_number = jsonData.getString("previous_school_contact_number");
                                    String birth_certificate_front_image = jsonData.getString("birth_certificate_front_image");
                                    String left_ear_frequency = jsonData.getString("left_ear_frequency");
                                    String short_eye_vision = jsonData.getString("short_eye_vision");
                                    String submitted_datetime = jsonData.getString("submitted_datetime");
                                    String previous_school_city = jsonData.getString("previous_school_city");
                                    String division = jsonData.getString("division");
                                    String date_of_birth = jsonData.getString("date_of_birth");
                                    String adhar_id_number_image_front = jsonData.getString("adhar_id_number_image_front");
                                    String middle_name = jsonData.getString("middle_name");
                                    String comments_on_ears = jsonData.getString("comments_on_ears");
                                    //String color_vision = jsonData.getString("color_vision");
                                    String transfer_certificate_front_image = jsonData.getString("transfer_certificate_front_image");
                                    String right_ear_freqency = jsonData.getString("right_ear_freqency");
                                    String last_name = jsonData.getString("last_name");
                                    String first_name = jsonData.getString("first_name");
                                    String adhaar_id_number = jsonData.getString("adhaar_id_number");
                                    String transfer_certificate_back_image = jsonData.getString("transfer_certificate_back_image");
                                    String previous_markscard_number = jsonData.getString("previous_markscard_number");
                                    String birth_certificate_back_image = jsonData.getString("birth_certificate_back_image");
                                    //String student_custom_id = jsonData.getString("student_custom_id");
                                    //String hearing_difficulty = jsonData.getString("hearing_difficulty");
                                    String birth_place = jsonData.getString("birth_place");
                                    String caste = jsonData.getString("caste");
                                     photo = jsonData.getString("photo");
                                    //String hearing_disability = jsonData.getString("hearing_disability");
                                    String previous_class = jsonData.getString("previous_class");
                                    String status = jsonData.getString("status");
                                    String student_deleted = jsonData.getString("student_deleted");
                                    String student_id = jsonData.getString("student_id");
                                    String comments_on_teeth = jsonData.getString("comments_on_teeth");
                                    String submitted_by = jsonData.getString("submitted_by");
                                    String weight = jsonData.getString("weight");

                                    //JSONArray siblingsArray = jsonData.getJSONArray("siblings");

                                    tv_name.setText(first_name + " " + last_name);
                                    tv_id.setText(student_id);
                                    tv_className.setText(className);
                                    tv_sectionName.setText(section);
                                    tv_dept.setText(className);
                                    tv_role.setText(section);

                                    tv_firstName.setText(first_name);
                                    tv_middleName.setText(middle_name);
                                    tv_lastName.setText(last_name);
                                    tv_gender.setText(gender);
                                    tv_caste.setText(caste);
                                    tv_birthPlace.setText(birth_place);
                                    tv_dob.setText(date_of_birth);
                                    tv_nationality.setText(nationality);


                                    tv_height.setText(height);
                                    tv_weight.setText(weight);
                                    tv_shortEye.setText(short_eye_vision);
                                    tv_longEye.setText(long_eye_vision);
                                    //tv_colorVision.setText();
                                    tv_commentEye.setText(comments_on_eye);
                                    //tv_teethProb.setText();
                                    tv_commentTeeth.setText(comments_on_teeth);
                                    tv_rightEarF.setText(right_ear_freqency);
                                    tv_leftEarF.setText(left_ear_frequency);
                                    //tv_hearingDiff.setText();
                                    tv_commentEar.setText(comments_on_ears);
                                    //tv_anyDis.setText(height);
                                    tv_commentDisability.setText(comments_on_disability);

                                    final String imageUrlg = "http://13.233.109.165:8000/media/";
                                    assert getActivity() != null;
                                    Glide.with(getActivity()).load(imageUrlg + photo).into(civ_image);

                                    docsList.add(new ProfileModel("Adhar Card", adhaar_id_number,
                                            adhar_id_number_image_front, adhar_id_number_image_front));

                                    docsList.add(new ProfileModel("Birth Certificate",
                                            birth_certificate_number, birth_certificate_back_image, birth_certificate_front_image));

                                    docsList.add(new ProfileModel("Previous School Marksheet",
                                            previous_markscard_number, previous_school_marksheet_back_image,
                                            previous_school_marksheet_front_image));

                                    adapterDocs.notifyDataSetChanged();
                                    Log.d("student_personal_suc", response.code() + "  " + message);


                                } else {

                                    Log.d("student_personal_else", response.code() + "  " + message);
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
                                Log.d("ADMIN_API", message);
                                // Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();

                                llStudentProfile.setVisibility(View.GONE);
                                tvNorecords.setVisibility(View.VISIBLE);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Object> call, Throwable t) {

                    }
                });


    }

    private void getStudentParentsDetailsAPI() {
        apiService.getStudentParentsDetails(Constant.SCHOOL_ID, FName, LName,
                CertificateNo, DOB).enqueue(new Callback<Object>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {

                if (response.isSuccessful()) {

                    try {

                        JSONObject object = new JSONObject(new Gson().toJson(response.body()));
                        String status = object.getString("status");
                        String message = object.getString("message");

                        if (status.equalsIgnoreCase("Success")) {

                            JSONObject jsonData = object.getJSONObject("data");

                            String parent_permanent_door_no = "", parent_present_street_name = "", parent_present_locality = "", parent_present_city = "",
                                    parent_present_state = "", parent_present_country = "", parent_present_pincode = "", parent_present_landmark = "",
                                    parent_present_contact_number = "";

                            String added_datetime = jsonData.getString("added_datetime");
                            String father_occupation = jsonData.getString("father_occupation");
                            String added_by = jsonData.getString("added_by");
                            String father_email_id = jsonData.getString("father_email_id");
                            String mother_last_name = jsonData.getString("mother_last_name");
                            String father_address_proof_number = jsonData.getString("father_address_proof_number");
                            String father_adharcard_front_image = jsonData.getString("father_adharcard_front_image");
                            String mother_date_of_birth = jsonData.getString("mother_date_of_birth");
                            String father_designation = jsonData.getString("father_designation");
                            String father_first_name = jsonData.getString("father_first_name");
                            String father_last_name = jsonData.getString("father_last_name");
                            String mother_current_address_proof_image_back = jsonData.getString("mother_current_address_proof_image_back");
                            String mother_middle_name = jsonData.getString("mother_middle_name");
                            String father_profile_image = jsonData.getString("father_profile_image");
                            String father_middle_name = jsonData.getString("father_middle_name");
                            String mother_designation = jsonData.getString("mother_designation");
                            String mother_current_address_proof_image_front = jsonData.getString("mother_current_address_proof_image_front");
                            String mother_profile_image = jsonData.getString("mother_profile_image");
                            String mother_address_proof_number = jsonData.getString("mother_address_proof_number");
                            String father_qualification = jsonData.getString("father_qualification");
                            String mother_qualification = jsonData.getString("mother_qualification");
                            String mother_aadhar_card_number = jsonData.getString("mother_aadhar_card_number");
                            String father_aadhar_card_number = jsonData.getString("father_aadhar_card_number");
                            String father_date_of_birth = jsonData.getString("father_date_of_birth");
                            String mother_first_name = jsonData.getString("mother_first_name");
                            String father_current_address_proof_image_back = jsonData.getString("father_current_address_proof_image_back");
                            String mother_aadhar_card_back_image = jsonData.getString("mother_aadhar_card_back_image");
                            String mother_aadhar_card_front_image = jsonData.getString("mother_aadhar_card_front_image");
                            String mother_email_id = jsonData.getString("mother_email_id");
                            String mother_mobile_number = jsonData.getString("mother_mobile_number");
                            String mother_occupation = jsonData.getString("mother_occupation");
                            String father_adharcard_back_image = jsonData.getString("father_adharcard_back_image");
                            String father_current_address_proof_image_front = jsonData.getString("father_current_address_proof_image_front");
                            String father_mobile_number = jsonData.getString("father_mobile_number");

                            if (!jsonData.isNull("parent_permanent_door_no")) {
                                parent_permanent_door_no = jsonData.getString("parent_permanent_door_no");
                            }
                            if (!jsonData.isNull("parent_present_locality")) {
                                parent_present_locality = jsonData.getString("parent_present_locality");
                            }
                            if (!jsonData.isNull("parent_present_landmark")) {
                                parent_present_landmark = jsonData.getString("parent_present_landmark");
                            }
                            if (!jsonData.isNull("parent_present_contact_number")) {
                                parent_present_contact_number = jsonData.getString("parent_present_contact_number");
                            }
                            if (!jsonData.isNull("parent_present_state")) {
                                parent_present_state = jsonData.getString("parent_present_state");
                            }
                            if (!jsonData.isNull("parent_present_street_name")) {
                                parent_present_street_name = jsonData.getString("parent_present_street_name");
                            }
                            if (!jsonData.isNull("parent_present_pincode")) {
                                parent_present_pincode = jsonData.getString("parent_present_pincode");
                            }
                            if (!jsonData.isNull("parent_present_city")) {
                                parent_present_city = jsonData.getString("parent_present_city");
                            }
                            if (!jsonData.isNull("parent_present_country")) {
                                parent_present_country = jsonData.getString("parent_present_country");
                            }

                            tv_fatherName.setText(father_first_name + " " + father_last_name);
                            tv_fatherOcc.setText(father_occupation);
                            tv_motherName.setText(mother_first_name + " " + mother_last_name);
                            tv_motherOcc.setText(mother_occupation);

                            tv_address1.setText(parent_permanent_door_no + ", " + parent_present_street_name + ", " + parent_present_locality + ", " +
                                    parent_present_city + ", " + parent_present_state + ", " + parent_present_country + "-" + parent_present_pincode);
                            tv_landmark1.setText(parent_present_landmark);
                            tv_contact1.setText(parent_present_contact_number);

                            Log.d("student_parent_suc", response.code() + "  " + message);

                        } else {
                            Log.d("student_parent_else", response.code() + "  " + message);
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
                        Log.d("ADMIN_API", message);
                        //   Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {

            }
        });
    }


    @Override
    public void onClick(View v) {
        StudentModuleActivity studentModuleActivity = (StudentModuleActivity) getActivity();
        Bundle bundle;
        switch (v.getId()) {
            case R.id.tv_feesDetails:
                tv_feesDetails.setBackgroundResource(R.drawable.border_rect);
                tv_homework.setBackgroundResource(R.drawable.border);
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
                bundle.putString("STUDENT_ID", StudentID);
                bundle.putString("STUDENT_NAME", tv_name.getText().toString());
                bundle.putString("STUDENT_CLASS", tv_className.getText().toString());
                bundle.putString("STUDENT_SECTION", tv_sectionName.getText().toString());
                bundle.putString("STUDENT_IMAGE", photo);

                Log.i("Student_ID**2**",""+StudentID+"**"+tv_className.getText().toString()+"**"+tv_sectionName.getText().toString());

                studentModuleActivity.loadFragment(Constant.FEES_INBOX_FRAGMENT, bundle);
                break;


                case R.id.tv_homework:
                tv_homework.setBackgroundResource(R.drawable.border_rect);
                    tv_feesDetails.setBackgroundResource(R.drawable.border);
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
                bundle.putString("STUDENTID", StudentID);
                bundle.putString("STUDENT_CLASS", tv_className.getText().toString());
                bundle.putString("STUDENT_SECTION", tv_sectionName.getText().toString());

                Log.i("Student_ID**2**",""+StudentID+"**"+tv_className.getText().toString()+"**"+tv_sectionName.getText().toString());

                studentModuleActivity.loadFragment(Constant.STU_HOMEWORK_LIST, bundle);
                break;


            case R.id.tv_leave:
                tvLeave.setBackgroundResource(R.drawable.border_rect);
                tv_feesDetails.setBackgroundResource(R.drawable.border);
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
                bundle.putString("STUDENTID", StudentID);

                studentModuleActivity.loadFragment(Constant.STUDENT_LEAVE, bundle);
                break;
            case R.id.tv_attendance:
                tv_attendance.setBackgroundResource(R.drawable.border_rect);
                tv_feesDetails.setBackgroundResource(R.drawable.border);
                tvLeave.setBackgroundResource(R.drawable.border);
                ll_academic.setBackgroundResource(R.drawable.border);
                ll_health.setBackgroundResource(R.drawable.border);
                ll_docs.setBackgroundResource(R.drawable.border);
                ll_health_view.setVisibility(View.GONE);
                ll_docs_view.setVisibility(View.GONE);
                ll_personal_view.setVisibility(View.GONE);
                tv_attendance.setVisibility(View.VISIBLE);
                bundle = new Bundle();
                bundle.putString("EMPUUID", StudentID);
                bundle.putString("FNAME", FName);
                bundle.putString("LNAME", LName);
                Constant.audience_type = "STUDENT";
                studentModuleActivity.loadFragment(Constant.ATTENDANCE_FRAGMENT, bundle);

                break;
            case R.id.iv_backBtn:
                getActivity().onBackPressed();
                break;

            case R.id.ll_personal:
                ll_personal.setBackgroundResource(R.drawable.border_rect);
                tv_feesDetails.setBackgroundResource(R.drawable.border);
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
                tv_feesDetails.setBackgroundResource(R.drawable.border);
                ll_academic.setBackgroundResource(R.drawable.border_rect);
                tv_attendance.setBackgroundResource(R.drawable.border);
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
                tv_feesDetails.setBackgroundResource(R.drawable.border);
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
                tv_feesDetails.setBackgroundResource(R.drawable.border);
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
        }
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
}

