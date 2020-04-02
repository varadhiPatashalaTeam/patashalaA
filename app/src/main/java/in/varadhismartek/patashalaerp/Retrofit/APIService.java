package in.varadhismartek.patashalaerp.Retrofit;


import com.google.gson.JsonElement;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;


/**
 * Created by varadhi on 10/10/2019.
 */

public interface APIService {


  @FormUrlEncoded
  @POST("school_forgot_password")
  Call<JsonElement> getOtpToLogin(@Field("mobile_number")String mobile_number);


  @FormUrlEncoded
  @POST("school_login_with_otp")
  Call<Object> schoolLoginWithOtp(@Field("mobile_number") String mobile_number,
                                  @Field("otp_password")String otp_password);


    @FormUrlEncoded
    @POST("get_school_details")
    Call<Object> get_school_details(
            @Field("mobile_number") String mobile_number,
            @Field("school_id") String school_id);


  @FormUrlEncoded
    @POST("check_last_filled_school_barrier")
    Call<Object> checkLastfilledBarriers(
            @Field("school_id") String school_id);



  @FormUrlEncoded
  @POST("set_the_barriers_and_employee")
  Call<Object> getEmployeeID(
          @Field("school_id") String school_id,
          @Field("poc_mobile_number") String poc_mobile_number,
          @Field("poc_name") String poc_name
  );


    /******************************** select modules******************/

    @FormUrlEncoded
    @POST("get_school_modules_list")
    Call<Object> getModuleList(
            @Field("school_id") String school_id);


    @FormUrlEncoded
    @POST("add_school_modules")
    Call<Object> addSchoolModules(
            @Field("school_id") String school_id,
            @Field("modules_name") JSONObject modules_name,
            @Field("added_employeeid") String added_employeeid);


    @FormUrlEncoded
    @POST("update_school_modules_status")
    Call<Object> updateModuleStatus(
            @Field("school_id") String school_id,
            @Field("modules_name") JSONObject modules_name,
            @Field("updated_employeeid") String updated_employeeid);


    // wings modules

    @FormUrlEncoded
    @POST("get_school_wings")
    Call<Object> gettingWings(
            @Field("school_id") String school_id

    );

    @FormUrlEncoded
    @POST("add_school_wings")
    Call<Object> addWings(@Field("school_id") String school_id,
                          @Field("wings_names") JSONObject jsonObject,
                          @Field("added_employeeid") String added_employeeid);

    @FormUrlEncoded
    @POST("update_school_wing_name")
    Call<Object> updateSchoolWingName(
            @Field("school_id") String school_id,
            @Field("old_wing_name") String old_wing_name,
            @Field("new_wing_name") String new_wing_name,
            @Field("updated_employeeid") String updated_employeeid

    );


    @FormUrlEncoded
    @POST("update_school_wings_status")
    Call<Object> updateWingsStatus(@Field("school_id") String school_id,
                                   @Field("wings_names") JSONObject jsonObject,
                                   @Field("updated_employeeid") String added_employeeid);

// department module

    @FormUrlEncoded
    @POST("get_school_department")
    Call<Object> gettingDept(
            @Field("school_id") String mobileNumber,
            @Field("wings_name") JSONObject wing_name

    );


    @FormUrlEncoded
    @POST("add_school_department")
    Call<Object> addingDept(
            @Field("school_id") String school_id,
            @Field("wing_name") String wing_name,
            @Field("departments_name") JSONObject departments_name,
            @Field("added_employeeid") String added_employeeid
    );

    @FormUrlEncoded
    @POST("update_school_department_name")
    Call<Object> updateDeptName(
            @Field("school_id") String mobileNumber,
            @Field("wing_name") String wingName,
            @Field("old_department_name") String oldDeptName,
            @Field("new_department_name") String newDeptName,
            @Field("updated_employeeid") String updated_employeeid
    );

    @FormUrlEncoded
    @POST("update_school_departments_status")
    Call<Object> updateDeptStatus(
            @Field("school_id") String school_id,
            @Field("wing_name") String wing_name,
            @Field("departments_name") JSONObject departments_name,
            @Field("updated_employeeid") String added_employeeid
    );


// roles module

    @FormUrlEncoded
    @POST("get_school_roles")
    Call<Object> gettingRoles(
            @Field("school_id") String school_id,
            @Field("wings_name") JSONObject wingName,
            @Field("departments_name") JSONObject deptName);


    @FormUrlEncoded
    @POST("add_school_roles")
    Call<Object> addWingRoles(
            @Field("school_id") String school_id,
            @Field("wing_name") String wingName,
            @Field("department_name") String deptName,
            @Field("roles_name") JSONObject roles,
            @Field("added_employeeid") String employeeid
    );


    @FormUrlEncoded
    @POST("update_school_role_name")
    Call<Object> updateRolesName(
            @Field("school_id") String school_id,
            @Field("wing_name") String wingName,
            @Field("department_name") String deptName,
            @Field("old_role_name") String oldRole,
            @Field("new_role_name") String newRole,
            @Field("updated_employeeid") String updated_employeeid
    );


    @FormUrlEncoded
    @POST("update_school_roles_status")
    Call<Object> updateRolesStatus(
            @Field("school_id") String school_id,
            @Field("wing_name") String wingName,
            @Field("department_name") String deptName,
            @Field("roles_name") JSONObject roles_name,
            @Field("updated_employeeid") String updated_employeeid
    );


//school barrier

    @FormUrlEncoded
    @POST("update_school_student_barrier")
    Call<Object> addStudentBarrier(@Field("school_id") String school_id,
                                   @Field("default_student_admission_no") String default_admission_no,
                                   @Field("minimum_percentage_required") String minimum_percentage_required,
                                   @Field("father_qualification") String father_qualification,
                                   @Field("mother_qualification") String mother_qualification,
                                   @Field("food_facility") String food_facility,
                                   @Field("transport_facility") String transport_facility,
                                   @Field("no_of_guardians") String no_of_guardians,
                                   @Field("updated_employee_id") String updated_employee_id
    );

    @FormUrlEncoded
    @POST("get_school_student_barrier")
    Call<Object> getstatusStudentBarriers(
            @Field("school_id") String get_school_id);


//staff barriers

    @FormUrlEncoded
    @POST("get_school_staff_barrier")
    Call<Object> getStaffBarriers(
            @Field("school_id") String get_school_id);

    @FormUrlEncoded
    @POST("add_school_staff_barrier")
    Call<Object> addStaffBarrier(
            @Field("school_id") String schoolId,
            @Field("default_teacher_admission_no") String default_teacher_admission_no,
            @Field("added_by_employeeid") String added_by_employeeid);


    @FormUrlEncoded
    @POST("update_school_staff_barrier")
    Call<Object> updateStaffBarriers(
            @Field("school_id") String get_school_id,
            @Field("updated_teacher_admission_no") String updated_teacher_admission_no,
            @Field("updated_by_employeeid") String updated_by_employeeid);

    //Division

    @FormUrlEncoded
    @POST("get_divisions")
    Call<Object> getDivisions(
            @Field("school_id") String school_id);

    @FormUrlEncoded
    @POST("add_division")
    Call<Object> addDivision(
            @Field("school_id") String school_id,
            @Field("data") String data,
            @Field("employee_uuid") String employee_uuid);

    @FormUrlEncoded
    @POST("update_division_name")
    Call<Object> updateDivisionName(
            @Field("school_id") String school_id,
            @Field("division_old_name") String old_name,
            @Field("division_new_name") String new_name,
            @Field("employee_uuid") String employee_uuid
    );

    @FormUrlEncoded
    @POST("update_division_status")
    Call<Object> updateDivisionStatus(
            @Field("school_id") String school_id,
            @Field("divisions") JSONArray divisions,
            @Field("employee_uuid") String employee_uuid);


//maker checker


    @FormUrlEncoded
    @POST("add_school_maker_and_checker")
    Call<Object> addMakerChecker(
            @Field("school_id") String school_id,
            @Field("module_name") String module_name,
            @Field("added_employeeid") String added_employeeid,
            @Field("makers") JSONObject maker,
            @Field("checkers") JSONObject checkers
    );

    @FormUrlEncoded
    @POST("get_school_maker_and_checker")
    Call<Object> getMakerChecker(
            @Field("school_id") String school_id
    );


    @FormUrlEncoded
    @POST("delete_school_maker_and_checker")
    Call<Object> deleteMakerChecker(
            @Field("school_id") String school_id,
            @Field("module_name") String module_name,
            @Field("deleting_employeeid") String deleting_employeeid
    );

    //Class Module


    @FormUrlEncoded
    @POST("get_school_classes_sections")
    Call<Object> getClassSections(
            @Field("school_id") String school_id,
            @Field("divisions") JSONObject division
    );


    @FormUrlEncoded
    @POST("add_classes_sections")
    Call<Object> addClassSections(
            @Field("school_id") String school_id,
            @Field("division_name") String division,
            @Field("classes_sections") JSONObject classSection,
            @Field("added_employeeid") String employeeID
    );


    @FormUrlEncoded
    @POST("del_classes_sections")
    Call<Object> deleteClassSections(
            @Field("school_id") String school_id,
            @Field("classes_sections") JSONObject classSection,
            @Field("deleting_employeeid") String employeeID
    );

    //sessions
    @FormUrlEncoded
    @POST("add_school_sessions")
    Call<Object> addSession(
            @Field("school_id") String school_id,
            @Field("academic_start_date") String fdate,
            @Field("academic_end_date") String todate,
            @Field("weekly_working_days") String workingday,
            @Field("sessions") JSONObject sessions,
            @Field("added_employeeid") String empID
    );

    @FormUrlEncoded
    @POST("get_school_academic_years")
    Call<Object> getAcademicYear(
            @Field("school_id") String school_id

    );

    @FormUrlEncoded
    @POST("get_school_sessions")
    Call<Object> getSessions(
            @Field("school_id") String school_id,
            @Field("academic_start_date") String startDate,
            @Field("academic_end_date") String toDate
    );


    @FormUrlEncoded
    @POST("delete_school_sessions")
    Call<Object> deleteSessions(
            @Field("school_id") String school_id,
            @Field("academic_start_date") String startDate,
            @Field("academic_end_date") String endDate,
            @Field("session_serial_no") String serialNo,
            @Field("added_employeeid") String empId,
            @Field("session_from_date") String fromdate,
            @Field("session_to_date") String todate
    );

    @FormUrlEncoded
    @POST("update_school_sessions")
    Call<Object> upDateSession(
            @Field("school_id") String school_id,
            @Field("academic_start_date") String startDate,
            @Field("academic_end_date") String endDate,
            @Field("weekly_working_days") String workday,
            @Field("added_employeeid") String empId,
            @Field("session_from_date") String fromdate,
            @Field("session_to_date") String todate
    );

    //Assessment -> Grade


    @FormUrlEncoded
    @POST("add_grade_barrier")
    Call<Object> addGradeBarrier(
            @Field("school_id") String school_id,
            @Field("grade_data") JSONObject grade_data,
            @Field("employee_uuid") String employee_uuid,
            @Field("academic_start_date") String academic_start_date
    );

    @FormUrlEncoded
    @POST("get-status-grade-barrier")
    Call<Object> getGradeBarrier(
            @Field("school_id") String school_id,
            @Field("academic_start_date") String academic_start_date
    );

    @FormUrlEncoded
    @POST("update_grade_barrier")
    Call<Object> updateGradeBarrier(
            @Field("school_id") String school_id,
            @Field("grade_data") JSONObject grade_data,
            @Field("employee_uuid") String employee_uuid,
            @Field("academic_start_date") String academic_start_date
    );

    /**********************************************Homework Barriers***********************************/

    @FormUrlEncoded
    @POST("get_homework_barrier")
    Call<Object> getHomeworkBarrier(
            @Field("school_id") String school_id
    );

    @FormUrlEncoded
    @POST("add_homework_barrier")
    Call<Object> addHomeworkBarrier(
            @Field("school_id") String school_id,
            @Field("division") String division,
            @Field("homework_status") String homework_status,
            @Field("no_of_homeworks") String no_of_homeworks,
            @Field("added_employeeid") String added_employeeid
    );



    @FormUrlEncoded
    @POST("update_homework_barrier")
    Call<Object> updateHomeworkBarrier(
            @Field("school_id") String school_id,
            @Field("division") String division,
            @Field("no_of_homeworks") String no_of_homeworks,
            @Field("added_employeeid") String added_employeeid
    );

    @FormUrlEncoded
    @POST("update_school_homework_barrier_status")
    Call<Object> updateSchoolHomeworkBarrierStatus(
            @Field("school_id") String school_id,
            @Field("barrier_status") String barrier_status,
            @Field("employee_uuid") String employee_uuid
    );

    @FormUrlEncoded
    @POST("update_divisions_homework_barrier_status")
    Call<Object> updateDivisionsHomeworkBarrierStatus(
            @Field("school_id") String school_id,
            @Field("added_employeeid") String added_employeeid,
            @Field("data") String data
    );

    //Exam type Module

    @FormUrlEncoded
    @POST("get_exam_type")
    Call<Object> getExamList(
            @Field("school_id") String school_id
    );


    @FormUrlEncoded
    @POST("add_exam_type")
    Call<Object> addExamType(
            @Field("school_id") String school_id,
            @Field("exam_type") JSONArray exam_type,
            @Field("employee_uuid") String empId
    );


    @FormUrlEncoded
    @POST("update_exam_type_name")
    Call<Object> updateExameByname(
            @Field("school_id") String school_id,
            @Field("old_name") String old_name,
            @Field("new_name") String new_name,
            @Field("employee_uuid") String empId
    );


    @FormUrlEncoded
    @POST("update_exam_type_status")
    Call<Object> updateExamByStatus(
            @Field("school_id") String school_id,
            @Field("exam_type") JSONObject exam_type,
            @Field("employee_uuid") String empId
    );

    //exam barriers


    @FormUrlEncoded
    @POST("get-status-exam-barrier")
    Call<Object> getExamBarriers(
            @Field("school_id") String school_id,
            @Field("exam_type") String exam_type,
            @Field("division") String division,
            @Field("class") String classType
    );


    @FormUrlEncoded
    @POST("add_exam_barrier")
    Call<Object> addExamBarrier(
            @Field("school_id") String school_id,
            @Field("exam_type") String exam_type,
            @Field("division") String division,
            @Field("class") String classType,
            @Field("subject") String subject,
            @Field("min_marks") String min_marks,
            @Field("max_marks") String max_marks,
            @Field("exam_duration") String exam_duration,
            @Field("added_by_uuid") String added_by_uuid

    );



// Add subject


    @FormUrlEncoded
    @POST("get_subjects")// for barriers
    Call<Object> getSubjectNew(
            @Field("school_id") String school_id,
            @Field("division") String division,
            @Field("class") String classType,
            @Field("exam_type") String exam_type
    );


    @FormUrlEncoded
    @POST("get_subjects")
    Call<Object> getSubject(
            @Field("school_id") String school_id,
            @Field("division") String division,
            @Field("class") String classType

    );

  @FormUrlEncoded
  @POST("get_subjects")
  Call<Object> getSubjectByClass(
          @Field("school_id") String school_id,
          @Field("class") String classType

  );


    @FormUrlEncoded
    @POST("add_subjects")
    Call<Object> addSubject(
            @Field("school_id") String school_id,
            @Field("division") String division,
            @Field("class") String classType,
            @Field("data") JSONObject data,
            @Field("employee_uuid") String employee_uuid

    );


    @FormUrlEncoded
    @POST("update_subject")
    Call<Object> updateSubject(
            @Field("school_id") String school_id,
            @Field("division") String division,
            @Field("employee_uuid") String employee_uuid,
            @Field("class") String classType,
            @Field("sections") JSONArray data,
            @Field("old_subject") String old_subject,
            @Field("new_subject") String new_subject,
            @Field("new_subject_code") String new_subject_code,
            @Field("new_subject_type") String new_subject_type,
            @Field("new_status") String new_status

    );
    //Curricular


    @FormUrlEncoded
    @POST("get-status-co-curricular-activities")
    Call<Object> getCurricular(
            @Field("school_id") String school_id
    );


    @FormUrlEncoded
    @POST("add_co_curricular_activities")
    Call<Object> addCurricular(
            @Field("school_id") String school_id,
            @Field("data") JSONObject data,
            @Field("added_employeeid") String added_employeeid
    );

    @FormUrlEncoded
    @POST("update_co_curricular_activities")
    Call<Object> updateCurricular(
            @Field("school_id") String school_id,
            @Field("added_employeeid") String added_employeeid,
            @Field("old_activity_name") String old_activity_name,
            @Field("new_activity_name") String new_activity_name,
            @Field("new_marks") String new_marks,
            @Field("new_status") String new_status
    );

    /****************************************sports***************************************/

    @FormUrlEncoded
    @POST("get_sports")
    Call<Object> getSports(@Field("school_id") String school_id);

    @FormUrlEncoded
    @POST("add_sports")
    Call<Object> addSports(
            @Field("school_id") String school_id,
            @Field("sports") JSONArray sports,
            @Field("added_employeeid") String added_employeeid
    );

    @FormUrlEncoded
    @POST("update_sports_name")
    Call<Object> updateSportsByName(
            @Field("school_id") String school_id,
            @Field("added_employeeid") String empid,
            @Field("old_sports_name") String old_sports_name,
            @Field("new_sports_name") String new_sports_name
    );

    @FormUrlEncoded
    @POST("update_sports_status")
    Call<Object> updateSportsByStatus(
            @Field("school_id") String school_id,
            @Field("data") JSONObject datasports,
            @Field("added_employeeid") String added_employeeid
    );

    @FormUrlEncoded
    @POST("get_sports_barrier")
    Call<Object> getSportBarriers(
            @Field("school_id") String school_id
    );

    @FormUrlEncoded
    @POST("update_sports_barrier")
    Call<Object> updateAddSportBarriers(
            @Field("school_id") String school_id,
            @Field("added_employeeid") String added_employeeid,
            @Field("data") JSONObject data
    );

    /****************************************House module**********************************/


    @FormUrlEncoded
    @POST("get_school_houses")
    Call<Object> getHouseList(
            @Field("school_id") String school_id
    );


    @FormUrlEncoded
    @POST("update_houses")
    Call<Object> updateHouse(
            @Field("school_id") String school_id,
            @Field("house_name") String house_name,
            @Field("house_teacher_mentor_id") String house_teacher_mentor_id,
            @Field("house_captain_id") String house_captain_id,
            @Field("house_vice_captain_id") String house_vice_captain_id,
            @Field("house_color") String house_color,
            @Field("added_employeeid") String added_employeeid,
            @Field("house_uuid") String house_uuid
    );
    @FormUrlEncoded
    @POST("create_houses")
    Call<Object> AddHouse(
            @Field("school_id") String school_id,
            @Field("house_name") String house_name,

            @Field("house_color") String house_color,
            @Field("added_employeeid") String added_employeeid

    );

    @FormUrlEncoded
    @POST("get_house_student_list")
    Call<Object> get_house_student_list(
            @Field("school_id") String school_id,
            @Field("house_uuid") String house_uuid

    );



    /*****************************************Homework Module********************************/

    @FormUrlEncoded
    @POST("get_school_homework_list")
    Call<Object> getHomeWorkBySchool(
            @Field("school_id") String school_id
    );

    @FormUrlEncoded
    @POST("get_class_homework_list")
    Call<Object> getHomeWorkByClass(
            @Field("school_id") String school_id,
            @Field("classes") String classes
    );

    @POST("create_homework")
    Call<Object> createHomework(
            @Body RequestBody file

    );

    @FormUrlEncoded
    @POST("get_homework_details")
    Call<Object> getHomwWorkDetails(
            @Field("school_id") String school_id,
            @Field("homework_uuid") String homework_uuid

    );

    @FormUrlEncoded
    @POST("get_teacher_homework_list")
    Call<Object> get_teacher_homework_list(
            @Field("school_id") String school_id,
            @Field("employee_uuid") String employee_uuid

    );

    @FormUrlEncoded
    @POST("get_particular_homework_report_details")
    Call<Object> get_particular_homework_report_details(
            @Field("school_id") String school_id,
            @Field("homework_uuid") String homework_uuid
    );


    /*****************************************Teacher Module Homework list*****************/

    @FormUrlEncoded
    @POST("get_school_homework_report_list")
    Call<Object> getHomeworkListTeacher(
            @Field("school_id") String school_id
    );


    @FormUrlEncoded
    @POST("get_class_homework_report_list")
    Call<Object> getHomeworkListTeacherClass(
            @Field("school_id") String school_id,
            @Field("classes") String classes

    );


    @FormUrlEncoded
    @POST("get_student_homework_list")
    Call<Object> HomeworkListByClassSection(
            @Field("school_id") String school_id,
            @Field("classes") String classes,
            @Field("section") String section

    );

    /***************************************Student module**********************************/


     @FormUrlEncoded
     @POST("add_student_attendance")
     Call<Object> addStudentAttendance(
             @Field("school_id") String school_id,
             @Field("student_id") String student_id,
             @Field("division") String division,
             @Field("class") String classes,
             @Field("section") String section,
             @Field("date") String currentDate,
             @Field("attendance_status") String attendance_status,
             @Field("session_serial_no") String session_serial_no,
             @Field("academic_start_date") String academic_start_date,
             @Field("academic_end_date") String academic_end_date,
             @Field("session_from_date") String session_from_date,
             @Field("session_to_date") String session_to_date,
             @Field("added_by") String added_by
     );

    @FormUrlEncoded
    @POST("get_class_section_wise_student_attendance")
    Call<Object> getStudentAttendanceClassBy(
            @Field("school_id") String school_id,
            @Field("class") String classes,
            @Field("section") String section,
            @Field("date") String date
    );

  @FormUrlEncoded
  @POST("get_student_leave")
  Call<Object> getAllStudentLeave(
          @Field("school_id") String school_id,
          @Field("employee_uuid") String employee_uuid
  );

    @FormUrlEncoded
    @POST("get_student_leave")
    Call<Object> getStudentLeave(
            @Field("student_uuid") String school_id
    );

    @FormUrlEncoded
    @POST("get_student_monthly_attendance")
    Call<Object> getStudentMonthlyAttendance(
            @Field("school_id") String school_id,
            @Field("student_id") String student_id
    );



    @FormUrlEncoded
    @POST("get_student_homework_report_list")
    Call<Object> getHomeworkStudent(
            @Field("school_id") String school_id,
            @Field("classes") String classes,
            @Field("section") String section,
            @Field("student_uuid") String student_uuid

    );


        @FormUrlEncoded
    @POST("submit_student_homework")
    Call<Object> submitStudentHomework(
            @Field("school_id") String school_id,
            @Field("classes") String classes,
            @Field("section") String section,
            @Field("subject") String subject,

            @Field("completion_percent") String completion_percent,
            @Field("description") String description,
            @Field("student_uuid") String student_uuid,
            @Field("homework_uuid") String homework_uuid

    );

  //--------------------Syllabus----------------------------------

  @FormUrlEncoded
  @POST("get_school_syllabus")
  Call<Object> getSchoolSyllabus(
          @Field("school_id") String school_id
  );

  @FormUrlEncoded
  @POST("get_class_syllabus")
  Call<Object> getClassSyllabus(
          @Field("school_id") String school_id,
          @Field("classes") String classes,
          @Field("sections") String sections
  );

  @POST("add_school_syllabus")
  Call<Object> addSchoolSyllabus(@Body RequestBody file);


  //-------------------------------Subject Api----------------------------------

  @FormUrlEncoded
  @POST("get_class_subjects_school")
  Call<Object> getClassSubjectsSchool(
          @Field("school_id") String school_id
  );

  @FormUrlEncoded
  @POST("get_exam_type")
  Call<Object> getExamType(
          @Field("school_id") String school_id
  );

  /***********************Gallery module************************************/

    @FormUrlEncoded
    @POST("getting_school_gallery")
    Call<Object> getGallery(
            @Field("school_id") String school_id
    );

  @FormUrlEncoded
  @POST("getting_school_full_gallery")
  Call<Object> gettingSchoolFullGallery(
          @Field("school_id") String school_id,
          @Field("gallery_id") String gallery_id
  );

  @POST("creating_school_gallery")
  Call<Object> addGallery(@Body RequestBody file);


    @FormUrlEncoded
    @POST("delete_school_gallery")
    Call<Object> deleteSchoolGallery(
            @Field("school_id") String school_id,
            @Field("employee_id") String employee_id,
            @Field("gallery_id") String gallery_id,
            @Field("first_name") String first_name,
            @Field("last_name") String last_name,
            @Field("phone_no") String phone_no,
            @Field("adhaar_card_no") String adhaar_card_no
    );

    /*****************************************Question Bank*****************************/

    @FormUrlEncoded
    @POST("get_school_question_bank")
    Call<Object> getQuestionBank(
            @Field("school_id") String school_id
    );

    @FormUrlEncoded
    @POST("get_class_question_bank")
    Call<Object> getQuestionBankBysubject(
            @Field("school_id") String school_id,
            @Field("classes") String classes,
            @Field("sections") String sections,
            @Field("subject") String subject
    );

    @FormUrlEncoded
    @POST("delete_question_bank")
    Call<Object> deleteQuestionBank(
            @Field("school_id") String school_id,
            @Field("question_bank_id") String questionbankId
    );



    @POST("add_question_bank")
    Call<Object> addQuestionBank(@Body RequestBody file);

    /****************************************-Notification***************************************/

    @FormUrlEncoded
    @POST("get_notification_admin")
    Call<Object> get_NotificationAdmin(
            @Field("school_id") String school_id
    );

    @Multipart
    @POST("create_notification")
    Call<Object> createNotificationDepartment(
            @Part("school_id") RequestBody school_id,
            @Part("added_employeeid") RequestBody added_employeeid,
            @Part("send_to") RequestBody send_to,
            @Part("send_time") RequestBody send_time,
            @Part("send_date") RequestBody send_date,
            @Part("notification_message") RequestBody notification_message,
            @Part("departments") RequestBody departments,
            @Part MultipartBody.Part notification_attachment
    );

    @Multipart
    @POST("create_notification")
    Call<Object> createNotificationDivision(
            @Part("school_id") RequestBody school_id,
            @Part("added_employeeid") RequestBody added_employeeid,
            @Part("send_to") RequestBody send_to,
            @Part("send_time") RequestBody send_time,
            @Part("send_date") RequestBody send_date,
            @Part("notification_message") RequestBody notification_message,
            @Part("divisions") RequestBody divisions,
            @Part MultipartBody.Part notification_attachment
    );

    @Multipart
    @POST("create_notification")
    Call<Object> createNotificationRoles(
            @Part("school_id") RequestBody school_id,
            @Part("added_employeeid") RequestBody added_employeeid,
            @Part("send_to") RequestBody send_to,
            @Part("send_time") RequestBody send_time,
            @Part("send_date") RequestBody send_date,
            @Part("notification_message") RequestBody notification_message,
            @Part("roles") RequestBody roles,
            @Part MultipartBody.Part notification_attachment
    );

    @Multipart
    @POST("create_notification")
    Call<Object> createNotificationClasses(
            @Part("school_id") RequestBody school_id,
            @Part("added_employeeid") RequestBody added_employeeid,
            @Part("send_to") RequestBody send_to,
            @Part("send_time") RequestBody send_time,
            @Part("send_date") RequestBody send_date,
            @Part("notification_message") RequestBody notification_message,
            @Part("classes") RequestBody classes,
            @Part MultipartBody.Part notification_attachment

    );

    @FormUrlEncoded
    @POST("create_notification")
    Call<Object> createNotificationParents(
            @Field("school_id") String school_id,
            @Field("added_employeeid") String added_employeeid,
            @Field("send_to") String send_to,
            @Field("send_time") String send_time,
            @Field("send_date") String send_date,
            @Field("notification_message") String notification_message,
            @Field("parents") String parents
    );



    @Multipart
    @POST("create_notification")
    Call<Object> createNotificationStaff(
            @Part("school_id") RequestBody school_id,
            @Part("added_employeeid") RequestBody added_employeeid,
            @Part("send_to") RequestBody send_to,
            @Part("send_time") RequestBody send_time,
            @Part("send_date") RequestBody send_date,
            @Part("notification_message") RequestBody notification_message,
            @Part("staffs") RequestBody staffs,
            @Part("notification_title") RequestBody notification_title,
            @Part MultipartBody.Part notification_attachment
    );

    @Multipart
    @POST("create_notification")
    Call<Object> createNotificationStudents(
            @Part("school_id") RequestBody school_id,
            @Part("added_employeeid") RequestBody added_employeeid,
            @Part("send_to") RequestBody send_to,
            @Part("send_time") RequestBody send_time,
            @Part("send_date") RequestBody send_date,
            @Part("notification_message") RequestBody notification_message,
            @Part("students") RequestBody students,
            @Part("notification_title") RequestBody notification_title,
            @Part MultipartBody.Part notification_attachment
    );

    @Multipart
    @POST("update_notification")
    Call<Object> updateNotification(
            @Part("school_id") RequestBody school_id,
            @Part("added_employeeid") RequestBody added_employeeid,
            @Part("notification_id") RequestBody notification_id,
            @Part("new_send_time") RequestBody new_send_time,
            @Part("new_send_date") RequestBody new_send_date,
            @Part("new_notification_message") RequestBody new_notification_message,
            @Part("new_status") RequestBody new_status,
            //@Field("new_notification_title") String new_notification_title,
            @Part MultipartBody.Part new_notification_attachment

    );

    @Multipart
    @POST("update_notification_admin")
    Call<Object> updateNotificationAdmin(
            @Part("school_id") RequestBody school_id,
            @Part("notification_id") RequestBody notification_id,
            @Part("new_send_time") RequestBody new_send_time,
            @Part("new_send_date") RequestBody new_send_date,
            @Part("new_notification_message") RequestBody new_notification_message,
            @Part("new_status") RequestBody new_status,
            //@Part("new_notification_title") String new_notification_title,
            @Part MultipartBody.Part new_notification_attachment,
            @Part("approved_employeeid") RequestBody approved_employeeid,
            @Part("approver_comment") RequestBody approver_comment,
            @Part("approver_status") RequestBody approver_status
    );


  @FormUrlEncoded
  @POST("update_notification_admin")
  Call<Object> updateNotificationAdminWithOutImage(
          @Field("school_id") String school_id,
          @Field("notification_id") String notification_id,
          @Field("new_send_time") String new_send_time,
          @Field("new_send_date") String new_send_date,
          @Field("new_notification_message") String new_notification_message,
          @Field("new_status") String new_status,
          //@Part("new_notification_title") String new_notification_title,
          @Field("approved_employeeid") String approved_employeeid,
          @Field("approver_comment") String approver_comment,
          @Field("approver_status") String approver_status
  );
    /***************************************Leave Barrier Api**************************************/
    @FormUrlEncoded
    @POST("get_leave_barrier")
    Call<Object> getLeaveBarrier(
            @Field("school_id") String school_id
    );

    @FormUrlEncoded
    @POST("add_leave_barrier")
    Call<Object> addLeaveBarrier(
            @Field("school_id") String school_id,
            @Field("leavetype") JSONObject leavetype,
            @Field("added_employeeid") String added_employeeid


    );

    @FormUrlEncoded
    @POST("update_leave_barrier")
    Call<Object> updateLeaveBarrier(
            @Field("school_id") String school_id,
            @Field("old_leave_type") String old_leave_type,
            @Field("new_leave_type") String new_leave_type
    );

    /******************** school leave barrier*********************************/


    @FormUrlEncoded
    @POST("get_school_leave_barrier_details")
    Call<Object> getSchoolLeaveBarrierDetails(
            @Field("school_id") String school_id
    );


    @FormUrlEncoded
    @POST("add_school_leave_barrier_details")
    Call<Object> addSchoolLeaveBarrierDetails(
            @Field("school_id") String school_id,
            @Field("leave_code") String leave_code,
            @Field("leaves_per_year") String leaves_per_year,
            @Field("notice_period") String notice_period,
            @Field("added_employeeid") String added_employeeid,
            @Field("leave_id") String leave_id,
            @Field("leave_type") String leave_type,
            @Field("availability") String availability
    );

    @FormUrlEncoded
    @POST("update_school_leave_status")
    Call<Object> updateSchoolLeaveStatus(
            @Field("school_id") String school_id,
            @Field("leave_type") JSONObject leave_type,
            @Field("updated_employeeid") String updated_employeeid
    );


    @FormUrlEncoded
    @POST("update_school_leave_barrier_details")
    Call<Object> updateSchoolLeaveBarrierDetails(
            @Field("school_id") String school_id,
            @Field("leave_type") String leave_type,
            @Field("new_leave_code") String new_leave_code,
            @Field("new_leaves_per_year") String new_leaves_per_year,
            @Field("new_notice_period") String new_notice_period,
            @Field("availability") String availability
    );

    @FormUrlEncoded
    @POST("update_school_leave_barrier_status")
    Call<Object> updateSchoolLeaveBarrierStatus(
            @Field("school_id") String school_id,
            @Field("leave_type") JSONObject leave_type,
            @Field("added_employeeid") String added_employeeid

    );


    /************************************ Employee ***************************************/

    @FormUrlEncoded
    @POST("get_school_all_employee_lists")
    Call<Object> getAllEmployeeList(
            @Field("school_id") String school_id
    );


    //B-2 1.1234567


    @FormUrlEncoded
    @POST("get_employee_leave_statement")
    Call<Object> getEmployeeLeaveStatement(
            @Field("school_id") String school_id,
            @Field("employee_uuid") String employee_uuid
    );


    @FormUrlEncoded
    @POST("apply_employee_leave")
    Call<Object> applyEmployeeLeave(
            @Field("school_id") String school_id,
            @Field("employee_uuid") String employee_uuid,
            @Field("leave_type") String leave_type,
            @Field("from_date") String from_date,
            @Field("to_date") String to_date,
            @Field("message") String message,
            @Field("subject") String subject,
            @Field("door_no") String door_no,
            @Field("locality") String locality,
            @Field("landmark") String landmark,
            @Field("pincode") String pincode,
            @Field("city") String city,
            @Field("state") String state,
            @Field("country") String country,
            @Field("contact_number") String contact_number
    );

    @FormUrlEncoded
    @POST("get_employee_all_leave_details")
    Call<Object> getEmployeeAllLeaveDetails(
            @Field("school_id") String school_id,
            @Field("employee_uuid") String employee_uuid
    );

    @FormUrlEncoded
    @POST("get_all_employee_leave_details")
    Call<Object> getAllEmployeeLeaveDetails(
            @Field("school_id") String school_id
    );


    @FormUrlEncoded
    @POST("get_employee_leave_full_details")
    Call<Object> getEmployeeLeaveFullDetails(
            @Field("school_id") String school_id,
            @Field("employee_uuid") String employee_uuid,
            @Field("leave_id") String leave_id
    );


    @FormUrlEncoded
    @POST("approved_employee_leave")
    Call<Object> approvedEmployeeLeave(
            @Field("school_id") String school_id,
            @Field("employee_uuid") String employee_uuid,
            @Field("approved_by_employee_uuid") String approved_by_employee_uuid,
            @Field("comment") String comment,
            @Field("leave_status") String leave_status,
            @Field("leave_id") String leave_id
    );

    @FormUrlEncoded
    @POST("canceling_employee_leave")
    Call<Object> cancelingEmployeeLeave(
            @Field("school_id") String school_id,
            @Field("employee_uuid") String employee_uuid,
            @Field("leave_id") String leave_id
    );


    /*******************************Notice Board****************************/

    @FormUrlEncoded
    @POST("get_notice")
    Call<Object> getNotice(
            @Field("school_id") String school_id
    );

    @FormUrlEncoded
    @POST("add_notice")
    Call<Object> addNotice(
            @Field("school_id") String school_id,
            @Field("notice_title") String notice_title,
            @Field("notice_message") String notice_message,
            @Field("added_employeeid") String added_employeeid
    );

    @FormUrlEncoded
    @POST("update_notice")
    Call<Object> updateNotice(
            @Field("school_id") String school_id,
            @Field("notice_id") String notice_id,
            @Field("notice_message") String notice_message,
            @Field("notice_title") String notice_title,
            @Field("added_employeeid") String added_employeeid
    );

    @FormUrlEncoded
    @POST("delete_notice")
    Call<Object> deleteNotice(
            @Field("school_id") String school_id,
            @Field("notice_id") String notice_id,
            @Field("deleted_employeeid") String deleted_employeeid
    );

    /****************************Student List******************************/
    @FormUrlEncoded
    @POST("get_all_students_list")
    Call<Object> getStudentAll(
            @Field("school_id") String school_id
    );

    @FormUrlEncoded
    @POST("get_class_and_section_students_list")
    Call<Object> getStudentAllByClassSection(
            @Field("school_id") String school_id,
            @Field("classes") String classes,
            @Field("section") String section
    );

    @FormUrlEncoded
    @POST("get_student_personal_details")
    Call<Object> getStudentPersonalDetails(
            @Field("school_id") String school_id,
            @Field("first_name") String first_name,
            @Field("last_name") String last_name,
            @Field("birth_certificate_number") String birth_certificate_number,
            @Field("dob") String dob
    );

    @FormUrlEncoded
    @POST("get_student_parents_details")
    Call<Object> getStudentParentsDetails(
            @Field("school_id") String school_id,
            @Field("first_name") String first_name,
            @Field("last_name") String last_name,
            @Field("birth_certificate_number") String birth_certificate_number,
            @Field("dob") String dob
    );

    @FormUrlEncoded
    @POST("get_student_guardian_details")
    Call<Object> getStudentGuardianDetails(
            @Field("school_id") String school_id,
            @Field("first_name") String first_name,
            @Field("last_name") String last_name,
            @Field("birth_certificate_number") String birth_certificate_number,
            @Field("dob") String dob
    );


    /****************************************fees******************************************/

    @FormUrlEncoded
    @POST("create_fee_structure")
    Call<Object> createFeeStructure(
            @Field("school_id") String school_id,
            @Field("added_employeeid") String added_employeeid,
            @Field("academic_start_date") String academic_start_date,
            @Field("data") JSONObject feeData
    );

    @FormUrlEncoded
    @POST("get-status-fee-structure")
    Call<Object> getFeeStructure(
            @Field("school_id") String school_id,
            @Field("academic_start_date") String academic_start_date
    );

    @FormUrlEncoded
    @POST("update_fee_structure")
    Call<Object> updateFeeStructure(
            @Field("school_id") String school_id,
            @Field("academic_start_date") String academic_start_date,
            @Field("added_employeeid") String added_employeeid,
            @Field("new_status") String new_status,
            @Field("session_serial_no") String session_serial_no,
            @Field("old_fee_type") String old_fee_type,
            @Field("new_fee_type") String new_fee_type,
            @Field("new_installment") String new_installment,
            @Field("new_due_date") String new_due_date,
            @Field("new_fee_code") String new_fee_code
    );

    @FormUrlEncoded
    @POST("add_fee_in_classes")
    Call<Object> addfeeByClass(
            @Field("school_id") String school_id,
            @Field("added_employeeid") String added_employeeid,
            @Field("academic_start_date") String academic_start_date,
            @Field("session_serial_no") String session_serial_no,
            @Field("fee_type") String fee_type,
            @Field("class") String classes,
            @Field("total_fee") String total_fee
    );

    @FormUrlEncoded
    @POST("get-status-fee-in-classes")
    Call<Object> getFeeInClass(@Field("school_id") String school_id,
                               @Field("academic_start_date") String academic_start_date);

    /**********************************Attendance************************/
/* http://13.233.109.165:8000/school/get_employee_attendance_statistics
 http://13.233.109.165:8000/school/get_monthly_employee_attendance
 http://13.233.109.165:8000/school/get_employee_attendance_department_wise
 http://13.233.109.165:8000/school/get_employee_attendance_date_wise*/


    @FormUrlEncoded
    @POST("get_monthly_employee_attendance")
    Call<Object> getEmpAttendance(@Field("school_id") String school_id,
                               @Field("employee_id") String employee_id);


    @FormUrlEncoded
    @POST("getting_employee_personal_details")
    Call<Object> gettingEmployeePersonalDetails(
            @Field("school_id") String school_id,
            @Field("first_name") String first_name,
            @Field("last_name") String last_name,
            @Field("phone_no") String phone_no,
            @Field("adhaar_card_no") String adhaar_card_no
    );

    @FormUrlEncoded
    @POST("get_employee_attendance_statistics")
    Call<Object> getAttendanceByDate(@Field("school_id") String school_id,
                                  @Field("date") String dateValue);

    @FormUrlEncoded
    @POST("get_employee_attendance_department_wise")
    Call<Object> getAttendanceByDepartment(@Field("school_id") String school_id,
                                  @Field("date") String dateValue);

    /*******************************Schedule********************************/

    @FormUrlEncoded
    @POST("get-status-exams")
    Call<Object> getStatusExam(
            @Field("school_id") String school_id,
            @Field("session_serial_no") String session_serial_no,
            @Field("academic_start_date") String academic_start_date,
            @Field("academic_end_date") String academic_end_date
    );

    @FormUrlEncoded
    @POST("get_status_events")
    Call<Object> getStatusEvents(
            @Field("school_id") String school_id,
            @Field("session_serial_no") String session_serial_no,
            @Field("academic_start_date") String academic_start_date,
            @Field("academic_end_date") String academic_end_date
    );

    @FormUrlEncoded
    @POST("get-status-holidays")
    Call<Object> getStatusHolidays(
            @Field("school_id") String school_id,
            @Field("session_serial_no") String session_serial_no,
            @Field("academic_start_date") String academic_start_date,
            @Field("academic_end_date") String academic_end_date
    );

    /************************************Visitor*****************/
    @FormUrlEncoded
    @POST("get_school_visitor")
    Call<Object> getVisitor(
            @Field("school_id") String school_id);


    /*****************************Time table*************************/

    @FormUrlEncoded
    @POST("get_arranged_timetable_barrier")
    Call<Object> getPeriods(
            @Field("school_id") String school_id,
            @Field("division") String division,
            @Field("session_serial_no") String session_serial_no,
            @Field("academic_start_date") String academic_start_date
    );
//http://13.233.109.165:8000/patashala/get_class_section_timetable

/*school_id:8f82a5f5-3441-49eb-b67a-c113668f9140
class:LKG
section:A
academic_start_date:2019-12-29
session_serial_no:1*/
  @FormUrlEncoded
  @POST("get_class_section_timetable")
  Call<Object> getClassSectionTimetable(
          @Field("school_id") String school_id,
          @Field("class") String classes,
          @Field("section") String section,
          @Field("session_serial_no") String session_serial_no,
          @Field("academic_start_date") String academic_start_date
  );
  //----------------------------------Class Teacher-----------------------------------

  @FormUrlEncoded
  @POST("add_class_teacher")
  Call<Object> addClassTeacher(
          @Field("school_id") String school_id,
          @Field("added_employeeid") String added_employeeid,
          @Field("class") String classes,
          @Field("section") String section,
          @Field("teacher_uuid") String teacher_uuid
  );


  @FormUrlEncoded
  @POST("get_class_teachers")
  Call<Object> getClassTeachers(
          @Field("school_id") String school_id
  );


  @FormUrlEncoded
  @POST("update_class_teacher")
  Call<Object> updateClassTeacher(
          @Field("school_id") String school_id,
          @Field("added_employeeid") String added_employeeid,
          @Field("class") String className,
          @Field("section") String section,
          @Field("new_teacher_uuid") String new_teacher_uuid
  );


  //----------------------------------Birthday-----------------------------------

  @FormUrlEncoded
  @POST("get_employee_birthday")
  Call<Object> getEmployeeBirthday(
          @Field("school_id") String school_id,
          @Field("department") String department
  );

  @FormUrlEncoded
  @POST("get_student_birthday")
  Call<Object> getStudentBirthday(
          @Field("school_id") String school_id,
          @Field("class") String className,
          @Field("section") String section
  );

  @Multipart
  @POST("create_schedule")
  Call<Object> create_schedule(
          @Part("school_id") RequestBody school_id,
          @Part("added_employeeid") RequestBody added_employeeid,
          @Part("academic_start_date") RequestBody academic_start_date,
          @Part("academic_end_date") RequestBody academic_end_date,
          @Part("session_serial_no") RequestBody session_serial_no,
          @Part("division") RequestBody division,
          @Part("schedule_type") RequestBody schedule_type,
          @Part("schedule_title") RequestBody schedule_title,
          @Part("from_date") RequestBody from_date,
          @Part("to_date") RequestBody to_date,
          @Part("note") RequestBody note,
          @Part MultipartBody.Part schedule_attachment
  );

  @Multipart
  @POST("create_holidays")
  Call<Object> createHoliday1(
          @Part("school_id") RequestBody school_id,
          @Part("session_serial_no") RequestBody session_serial_no,
          @Part("schedule_type") RequestBody schedule_type,
          @Part("holiday_type") RequestBody holiday_type,
          @Part("holiday_name") RequestBody holiday_name,
          @Part("division") RequestBody division,
          @Part("classes") RequestBody classes,
          @Part("from_date") RequestBody from_date,
          @Part("to_date") RequestBody to_date,
          @Part("added_employeeid") RequestBody added_employeeid,
          @Part("academic_start_date") RequestBody academic_start_date,
          @Part("academic_end_date") RequestBody academic_end_date,
          @Part("message") RequestBody message,
          @Part MultipartBody.Part image
  );


  @POST("create_holidays")
  Call<Object> createHoliday(@Body RequestBody file);




  @FormUrlEncoded
  @POST("get-status-schedule")
  Call<Object> getScheduleBarriers(
          @Field("school_id") String school_id,
          @Field("session_serial_no") String session_serial_no,
          @Field("academic_start_date") String academic_start_date,
          @Field("academic_end_date") String academic_end_date

  );


  @FormUrlEncoded
  @POST("delete_schedule")
  Call<Object> delete_schedule(
          @Field("school_id") String school_id,
          @Field("deleted_employeeid") String deleted_employeeid,
          @Field("session_serial_no") String session_serial_no,
          @Field("academic_start_date") String academic_start_date,
          @Field("academic_end_date") String academic_end_date,
          @Field("from_date") String from_date,
          @Field("to_date") String to_date,
          @Field("schedule_type") String schedule_type

  );
  @FormUrlEncoded
  @POST("get_holiday_types")
  Call<Object>getHolidayBarrier(@Field("school_id") String school_id);


/****************************Transport Module*****************************/
//http://13.233.109.165:8000/patashala/get_transport_barrier
@FormUrlEncoded
  @POST("get_school_bus_list")
  Call<Object> getSchoolBusList(@Field("school_id") String school_id);


@FormUrlEncoded
  @POST("get_school_bus_full_detils")
  Call<Object> getSchoolBusDetails(@Field("school_id") String school_id,
                                @Field("vehicle_uuid")String vehicle_uuid);

  @FormUrlEncoded
  @POST("get_employee_acount_details")
  Call<Object> getEmployeeAccountDetails(
          @Field("school_id") String school_id,
          @Field("employee_uuid") String employee_uuid
  );

  @FormUrlEncoded
  @POST("get_employee_payslip")
  Call<Object> getEmployeePaySlipDetails(
          @Field("school_id") String school_id,
          @Field("employee_uuid") String employee_uuid
  );

  @GET("get_employee_payslip/{school_id}/{employee_uuid}/{year}/{month}")
  Call<Object> getEmployeePaySlip(
          @Path("school_id") String school_id,
          @Path("employee_uuid") String employee_uuid,
          @Path("year") String year,
          @Path("month") String month


  );
//@GET("group/{id}/users")

//http://13.233.109.165:8000/patashala/get_employee_payslip_barrier
  //http://13.233.109.165:8000/patashala/get_employee_payslip/scid/ecid/ye/mo
  //school_id:90a1dde7-038d-4d21-8369-a7a8ae25a331
  //employee_uuid:012081f8-bea3-4caa-b763-cee65ded0690


  //--------------------------Fees ----------------------------------

  @FormUrlEncoded
  @POST("get_student_fees_payments_details")
  Call<Object> getStudentFeesPaymentsDetails(
          @Field("school_id") String school_id,
          @Field("student_uuid") String student_uuid,
          @Field("academic_start_date") String academic_start_date,
          @Field("academic_end_date") String academic_end_date
  );


  /*---------------------DASHBOARD API---------------------------------*/


  @FormUrlEncoded
  @POST("get_exams_result")
  Call<Object> getExamsResult(
          @Field("school_id") String school_id,
          @Field("page_no") String page_no);
  @FormUrlEncoded
  @POST("get_employee_birthday")
  Call<Object> getEmployeeBirthday(
          @Field("school_id") String school_id
  );
  @FormUrlEncoded
  @POST("get_school_visitor_count")
  Call<Object> getSchoolVisitorCount(
          @Field("school_id") String school_id,
          @Field("added_datetime") String added_datetime
  );
  @FormUrlEncoded
  @POST("dashboard_student_attendance")
  Call<Object> dashboardStudentAttendance(
          @Field("school_id") String school_id
  );
  @FormUrlEncoded
  @POST("dashboard_employee_attendance")
  Call<Object> dashboardEmployeeAttendance(
          @Field("school_id") String school_id
  );

  @FormUrlEncoded
  @POST("get_pending_student_list")
  Call<Object> getPendingStudentList(
          @Field("school_id") String school_id,
          @Field("date") String date
  );

  //-------------------------------Visitors------------------------ get_school_visitor_details

  @FormUrlEncoded
  @POST("get_school_visitor")
  Call<Object> getSchoolVisitor(
          @Field("school_id") String school_id
  );

  @POST("add_visitor")
  Call<Object> addVisitor(@Body RequestBody file);

  @FormUrlEncoded
  @POST("get_school_visitor_details")
  Call<Object> getSchoolVisitorDetails(
          @Field("school_id") String school_id,
          @Field("visitor_uuid") String visitor_uuid
  );

  @FormUrlEncoded
  @POST("get_school_tickets_statistics")
  Call<Object> getSchoolTicketsStatistics(
          @Field("school_id") String school_id
  );
  @FormUrlEncoded
  @POST("get_school_fees_statistics")
  Call<Object> getSchoolFeesStatistics(
          @Field("school_id") String school_id
  );

    @FormUrlEncoded
    @POST("get_levels_ticket_list")
    Call<Object> getEmployeeTicketList(
            @Field("school_id") String school_id
    );

  @FormUrlEncoded
  @POST("get_ticket_activity")
  Call<Object> getTicketActivity(
          @Field("school_id") String school_id,
          @Field("ticket_id") String ticket_id
  );


    @POST("send_ticket_replies")
    Call<Object> sendTicketReplies(@Body RequestBody file);

    @POST("escalate_ticket_issues")
    Call<Object> escalateTicketIssues(@Body RequestBody file);

    @FormUrlEncoded
    @POST("get_level_employee")
    Call<Object> getLevelEmployee(
            @Field("school_id") String school_id,
            @Field("module_name") String module_name,
            @Field("level_number") String level_number
    );

  @FormUrlEncoded
  @POST("close_ticket_issues")
  Call<Object> closeTicketIssues(
          @Field("school_id") String school_id,
          @Field("ticket_id") String ticket_id,
          @Field("raised_by_employee_uuid") String raised_by_employee_uuid,
          @Field("issues_description") String issues_description,
          @Field("feedback_message") String feedback_message,
          @Field("feedback_rating") String feedback_rating,
          @Field("issue_resolved_status") String issue_resolved_status,
          @Field("resolved_employee_uuid") String resolved_employee_uuid
  );


  @FormUrlEncoded
  @POST("add_transport_barrier")
  Call<Object> addTransportBarrier(
          @Field("school_id") String school_id,
          @Field("vehicle_default_id") String vehicle_default_id,
          @Field("speed_limit") String speed_limit,
          @Field("max_no_of_stops_per_route") String max_no_of_stops_per_route,
          @Field("time_before_reaching_school") String time_before_reaching_school,
          @Field("time_after_leaving_school") String time_after_leaving_school,
          @Field("added_employeeid") String added_employeeid
  );


  @FormUrlEncoded
  @POST("get_transport_barrier")
  Call<Object> getTransportBarrier(
          @Field("school_id") String school_id
  );

  @FormUrlEncoded
  @POST("update_transport_barrier")
  Call<Object> updateTransportBarrier(
          @Field("school_id") String school_id,
          @Field("vehicle_default_id") String vehicle_default_id,
          @Field("speed_limit") String speed_limit,
          @Field("max_no_of_stops_per_route") String max_no_of_stops_per_route,
          @Field("time_before_reaching_school") String time_before_reaching_school,
          @Field("time_after_leaving_school") String time_after_leaving_school,
          @Field("added_employeeid") String added_employeeid
  );




  @FormUrlEncoded
  @POST("add_vehicle")
  Call<Object> addVehicle(

          @Field("school_id") String school_id,
          @Field("class_of_vehicle") String class_of_vehicle,
          @Field("vehicle_id") String vehicle_id,
          @Field("registration_certificate_no") String registration_certificate_no,
          @Field("type_of_body") String type_of_body,
          @Field("send_service_alert") String send_service_alert,
          @Field("send_insurance_renewal_alert") String send_insurance_renewal_alert,
          @Field("send_pc_renewal_alert") String send_pc_renewal_alert,
          @Field("send_fc_renewal_alert") String send_fc_renewal_alert,
          @Field("loan_details") String loan_details,
          @Field("other_expenses") String other_expenses,
          @Field("added_employeeid") String added_employeeid,

          @Field("GPS_Details") String GPS_Details,
          @Field("chassis_number") String chassis_number,
          @Field("engine_number") String engine_number,
          @Field("maker_name") String maker_name,
          @Field("model_number") String model_number,
          @Field("manufacturing_year") String manufacturing_year,
          @Field("seating_capacity") String seating_capacity,
          @Field("registering_authority") String registering_authority,
          @Field("registering_state") String registering_state,
          @Field("registered_date") String registered_date,
          @Field("purchase_date") String purchase_date,
          @Field("purchase_cost") String purchase_cost,

          @Field("free_service") String free_service,
          @Field("remaining_service") String remaining_service,
          @Field("next_service_in_kms") String next_service_in_kms,
          @Field("next_service_in_days") String next_service_in_days,
          @Field("last_servicing_date") String last_servicing_date,

          @Field("insurance_type") String insurance_type,
          @Field("insurance_number") String insurance_number,
          @Field("insurance_date") String insurance_date,
          @Field("renewed_date") String renewed_date,
          @Field("next_renewal_date") String next_renewal_date,

          @Field("agent_id") String agent_id,
          @Field("agent_name") String agent_name,
          @Field("insurance_company_name") String insurance_company_name,
          @Field("agent_contact_number") String agent_contact_number,
          @Field("insurance_company_contact_number") String insurance_company_contact_number,

          @Field("rc_number") String rc_number,
          @Field("NOC_number") String NOC_number,
          @Field("pollution_certificate_no") String pollution_certificate_no,
          @Field("pollution_certification_issue_date") String pollution_certification_issue_date,
          @Field("pollution_renewal_date") String pollution_renewal_date,

          @Field("fitness_certificate_no") String fitness_certificate_no,
          @Field("fitness_certification_issue_date") String fitness_certification_issue_date,
          @Field("fitness_renewal_date") String fitness_renewal_date,

          @Field("tax_permit_no") String tax_permit_no,
          @Field("tax_payable_date") String tax_payable_date,
          @Field("tax_permit_amount") String tax_permit_amount


  );


  @POST("add_vehicle_documents")
  Call<Object> addVehicleDocument(@Body RequestBody file);


  @POST("add_route")
  Call<Object> addVehicleRoute(@Body RequestBody file);


  @FormUrlEncoded
  @POST("get_route_details")
  Call<Object> getRouteDetails(
          @Field("school_id") String school_id,
          @Field("route_uuid") String route_uuid
  );

  @FormUrlEncoded
  @POST("dashboard_schedule")
  Call<Object> dashboard_schedule(
          @Field("school_id") String school_id,
          @Field("date") String date
  );


  //----------------------------------- add schedule -------------------------

  @FormUrlEncoded
  @POST("get_holiday_types")
  Call<Object> getHolidayTypes(
          @Field("school_id") String school_id
  );

  @FormUrlEncoded
  @POST("get_event_types")
  Call<Object> getEventTypes(
          @Field("school_id") String school_id
  );

  @FormUrlEncoded
  @POST("get_subjects")
  Call<Object> getSubjects(
          @Field("school_id") String school_id,
          @Field("division") String division,
          @Field("class") String className
  );

  @FormUrlEncoded
  @POST("create_exams")
  Call<Object> createExams(
          @Field("school_id") String school_id,
          @Field("session_serial_no") String session_serial_no,
          @Field("from_date") String from_date,
          @Field("to_date") String to_date,
          @Field("schedule_type") String schedule_type,
          @Field("division") String division,
          @Field("classes") String classes,
          @Field("section") String section,
          @Field("exam_type") String exam_type,
          @Field("exam_date") String exam_date,
          @Field("message") String message,
          @Field("subject") String subject,
          @Field("start_time") String start_time,
          @Field("added_employeeid") String added_employeeid,
          @Field("academic_start_date") String academic_start_date,
          @Field("academic_end_date") String academic_end_date
  );

  @FormUrlEncoded
  @POST("create_events")
  Call<Object> createEvents(
          @Field("school_id") String school_id,
          @Field("session_serial_no") String session_serial_no,
          @Field("from_date") String from_date,
          @Field("to_date") String to_date,
          @Field("schedule_type") String schedule_type,
          @Field("division") String division,
          @Field("classes") JSONObject classes,
          @Field("event_type") String event_type,
          @Field("event_title") String event_title,
          @Field("message") String message,
          @Field("added_employeeid") String added_employeeid,
          @Field("academic_start_date") String academic_start_date,
          @Field("academic_end_date") String academic_end_date
  );

  @FormUrlEncoded
  @POST("create_holidays")
  Call<Object> createHolidays(
          @Field("school_id") String school_id,
          @Field("session_serial_no") String session_serial_no,
          @Field("from_date") String from_date,
          @Field("to_date") String to_date,
          @Field("schedule_type") String schedule_type,
          @Field("division") String division,
          @Field("classes") JSONObject classes,
          @Field("holiday_type") String holiday_type,
          @Field("holiday_name") String holiday_name,
          @Field("message") String message,
          @Field("added_employeeid") String added_employeeid,
          @Field("academic_start_date") String academic_start_date,
          @Field("academic_end_date") String academic_end_date
  );

}
