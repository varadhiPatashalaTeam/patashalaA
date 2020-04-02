package in.varadhismartek.patashalaerp.SelectModules;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import in.varadhismartek.Utils.Constant;
import in.varadhismartek.Utils.Themes;
import in.varadhismartek.patashalaerp.AddDepartment.AddDepartmentActivity;
import in.varadhismartek.patashalaerp.AddRoles.RolesLandingActivity;
import in.varadhismartek.patashalaerp.AddWing.AddWingsActivity;
import in.varadhismartek.patashalaerp.AdmissionBarriers.AdmissionBarriersActivity;
import in.varadhismartek.patashalaerp.DashboardModule.DashboardActivity;
import in.varadhismartek.patashalaerp.DivisionModule.AddDivisionActivity;
import in.varadhismartek.patashalaerp.R;
import in.varadhismartek.patashalaerp.Retrofit.APIService;
import in.varadhismartek.patashalaerp.Retrofit.ApiUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SelectModuleLandingActivity extends AppCompatActivity {

    final String themeGreen = "THEME_GREEN";
    String strCheckModuleset = "";
    SharedPreferences pref = null, pref2, login_pref;
    boolean booleanStatus = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Themes.onActivityCreateSetTheme(this,themeGreen);
        setContentView(R.layout.activity_select_module_landing);
        //This method is to detect screen size and set orientation


        DisplayMetrics metrics = new DisplayMetrics();
        SelectModuleLandingActivity.this.getWindowManager().getDefaultDisplay().getMetrics(metrics);

        float yInches= metrics.heightPixels/metrics.ydpi;
        float xInches= metrics.widthPixels/metrics.xdpi;
        double diagonalInches = Math.sqrt(xInches*xInches + yInches*yInches);



 /*       if (diagonalInches>=6.5){
            // 6.5inch device or bigger set orientation to Landscape
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }else{
            // smaller device set orientation to portrait
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
*/

        pref = getSharedPreferences("ActivityPREF", Context.MODE_PRIVATE);
        pref2 = getSharedPreferences("SecurityStatus", Context.MODE_PRIVATE);

        booleanStatus = pref2.getBoolean("activity_executed", false);
        Constant.ACTIVE_PAGE = pref2.getString("ACTIVE_PAGE", "0");

        Constant.SCHOOL_ID = pref2.getString("SchoolId", "0");
        Constant.EMPLOYEE_BY_ID = pref2.getString("EmployeeId", "0");
        Constant.POC_NAME = pref2.getString("PocName", "0");
        Constant.POC_Mobile_Number = pref2.getString("PocMobileNo", "0");
        Constant.SCHOOL_LOGO = pref2.getString("SchoolLogo", "0");

        Constant.EMPLOYEE_PHOTO = pref2.getString("EmployeePhoto","" );
        Constant.EMPLOYEE_FNAME = pref2.getString("EmployeeFname","" );
        Constant. EMPLOYEE_LNAME= pref2.getString("EmployeeLname","" );
        Constant.EMPLOYEE_ADDHAR = pref2.getString("EmployeeAdhar","" );
        Constant.EMPLOYEE_EMP_CUSTOM_ID = pref2.getString("EmployeeEmpCusID","" );
        Constant.EMPLOYEE_DEPARTMENT = pref2.getString("EmployeeDept","" );
        Constant.EMPLOYEE_ROLE = pref2.getString("EmployeeRole","" );
        Constant.EMPLOYEE_WING = pref2.getString("EmployeeWing","" );


        Log.d("empId**ID4", "" +  Constant.EMPLOYEE_BY_ID+Constant.POC_Mobile_Number+Constant.SCHOOL_LOGO);

       checkLastFilledBarreries();
       // getSupportFragmentManager().beginTransaction().replace(R.id.selectModuleContainer, new ModuleListFragment()).commit();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }




    private void checkLastFilledBarreries() {
        APIService apiService = ApiUtils.getAPIService();
        apiService.checkLastfilledBarriers(Constant.SCHOOL_ID).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                Log.i("response***1", "" + response.body() + "***" + response.code());
                if (response.isSuccessful()) {


                    try {
                        JSONObject json1 = (new JSONObject(new Gson().toJson(response.body())));
                        String status = (String) json1.get("status");
                        strCheckModuleset = json1.getString("message");
                        Log.i("response***", "" + response.body() + "***" + response.code());
                        if (status.equalsIgnoreCase("Success")) {


                            if (strCheckModuleset.equalsIgnoreCase("All barriers are filled")){
                                Log.i("BARRIERS***1","SET_1");
                                Intent intent = new Intent(SelectModuleLandingActivity.this, DashboardActivity.class);
                                startActivity(intent);
                                finish();

                            }else if (strCheckModuleset.equalsIgnoreCase("sessions are not filled")){

                                Intent intent = new Intent(SelectModuleLandingActivity.this,AddDivisionActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.putExtra("BARRIERS_TYPE" ,"SESSIONS_BARRIER");
                                startActivity(intent);
                                finish();

                            }else if (strCheckModuleset.equalsIgnoreCase("Class and sections is not filled")){
                                Intent intent = new Intent(SelectModuleLandingActivity.this,AddDivisionActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.putExtra("BARRIERS_TYPE" ,"CLASS_SECTIONS_BARRIER");
                                startActivity(intent);
                                finish();

                            }else if (strCheckModuleset.equalsIgnoreCase("Divisions are not filled")){
                                Intent intent = new Intent(SelectModuleLandingActivity.this,AddDivisionActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.putExtra("BARRIERS_TYPE" ,"DIVISION_BARRIER");
                                startActivity(intent);
                                finish();

                            }else if (strCheckModuleset.equalsIgnoreCase("Staff barrier is not filled")){
                                Intent intent = new Intent(SelectModuleLandingActivity.this, AdmissionBarriersActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();

                            }else if (strCheckModuleset.equalsIgnoreCase("Maker and checker is not filled")){
                                Intent intent = new Intent(SelectModuleLandingActivity.this, AddDivisionActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                 intent.putExtra("BARRIERS_TYPE" ,"MAKER_CHECKER_BARRIER");
                                startActivity(intent);
                                finish();

                            }else if (strCheckModuleset.equalsIgnoreCase("Roles are not filled")){
                                Intent intent = new Intent(SelectModuleLandingActivity.this, RolesLandingActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                // intent.putExtra("BARRIERS_TYPE" ,"MAKER_CHECKER_BARRIER");
                                startActivity(intent);
                                finish();

                            }else if (strCheckModuleset.equalsIgnoreCase("Roles are not filled")){
                                Intent intent = new Intent(SelectModuleLandingActivity.this, RolesLandingActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                // intent.putExtra("BARRIERS_TYPE" ,"MAKER_CHECKER_BARRIER");
                                startActivity(intent);
                                finish();

                            }else if (strCheckModuleset.equalsIgnoreCase("Departments are not filled")){
                                Intent intent = new Intent(SelectModuleLandingActivity.this, AddDepartmentActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                // intent.putExtra("BARRIERS_TYPE" ,"MAKER_CHECKER_BARRIER");
                                startActivity(intent);
                                finish();

                            }else if (strCheckModuleset.equalsIgnoreCase("Wings are not filled")){
                                Intent intent = new Intent(SelectModuleLandingActivity.this, AddWingsActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                // intent.putExtra("BARRIERS_TYPE" ,"MAKER_CHECKER_BARRIER");
                                startActivity(intent);
                                finish();

                            }

                            else {
                                getSupportFragmentManager().beginTransaction().replace(R.id.selectModuleContainer,
                                        new ModuleListFragment()).commit();
                            }


                        }
                    } catch (JSONException je) {

                    }

                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {

            }
        });
    }
}
