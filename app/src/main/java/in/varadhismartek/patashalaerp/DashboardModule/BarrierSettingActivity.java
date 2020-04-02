package in.varadhismartek.patashalaerp.DashboardModule;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import in.varadhismartek.patashalaerp.DashboardModule.UpdateActivity.UpdateClassActivity;
import in.varadhismartek.patashalaerp.DashboardModule.UpdateActivity.UpdateSectionActivity;
import in.varadhismartek.patashalaerp.DashboardModule.UpdateActivity.UpdateSelelectModuleActivity;
import in.varadhismartek.patashalaerp.DashboardModule.UpdateActivity.UpdateMakerCheckerActivity;
import in.varadhismartek.patashalaerp.DashboardModule.UpdateActivity.UpdateSessionsActivity;
import in.varadhismartek.patashalaerp.DashboardModule.UpdateActivity.UpdateStaffBarriersActivity;
import in.varadhismartek.patashalaerp.R;
import in.varadhismartek.patashalaerp.DashboardModule.UpdateActivity.UpdateDepartmentActivity;
import in.varadhismartek.patashalaerp.DashboardModule.UpdateActivity.UpdateDivisionActivity;
import in.varadhismartek.patashalaerp.DashboardModule.UpdateActivity.UpdateRolesActivity;
import in.varadhismartek.patashalaerp.DashboardModule.UpdateActivity.UpdateWingsActivity;

public class BarrierSettingActivity extends AppCompatActivity implements View.OnClickListener {
    private Button buttonRoles, buttonDept, buttonWings, buttonDivision, buttonClass,
            buttonSection, buttonSession;
    private Button buttonSelectModule, buttonMakerChecker, buttonStaffBarriers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barrier_setting);

        buttonSelectModule = findViewById(R.id.selectModule);
        buttonMakerChecker = findViewById(R.id.maker_checker);
        buttonWings = findViewById(R.id.wings);
        buttonDept = findViewById(R.id.dept);
        buttonRoles = findViewById(R.id.roles);

        buttonDivision = findViewById(R.id.division);
        buttonClass = findViewById(R.id.buttonClass);
        buttonSection = findViewById(R.id.buttonSection);

        buttonSession = findViewById(R.id.buttonSession);
        buttonStaffBarriers = findViewById(R.id.staff_barriers);

        buttonSelectModule.setOnClickListener(this);
        buttonMakerChecker.setOnClickListener(this);
        buttonWings.setOnClickListener(this);
        buttonDept.setOnClickListener(this);
        buttonRoles.setOnClickListener(this);
        buttonDivision.setOnClickListener(this);
        buttonStaffBarriers.setOnClickListener(this);
        buttonClass.setOnClickListener(this);
        buttonSection.setOnClickListener(this);
        buttonSession.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.selectModule:
                Intent intentsModule = new Intent(BarrierSettingActivity.this, UpdateSelelectModuleActivity.class);
                startActivity(intentsModule);
                finish();
                break;
            case R.id.maker_checker:
                Intent intentmakerchecker = new Intent(BarrierSettingActivity.this, UpdateMakerCheckerActivity.class);
                startActivity(intentmakerchecker);
                finish();
                break;
            case R.id.wings:
                Intent intentWings = new Intent(BarrierSettingActivity.this, UpdateWingsActivity.class);
                startActivity(intentWings);
                finish();
                break;
            case R.id.dept:
                Intent intentDept = new Intent(BarrierSettingActivity.this, UpdateDepartmentActivity.class);
                startActivity(intentDept);
                finish();
                break;
            case R.id.roles:
                Intent intentRoles = new Intent(BarrierSettingActivity.this, UpdateRolesActivity.class);
                startActivity(intentRoles);
                finish();
                break;
            case R.id.division:
                Intent intentDivision = new Intent(BarrierSettingActivity.this, UpdateDivisionActivity.class);
                startActivity(intentDivision);
                finish();
                break;

            case R.id.buttonClass:
                Intent intentClass = new Intent(BarrierSettingActivity.this, UpdateClassActivity.class);
                startActivity(intentClass);
                break;

            case R.id.buttonSection:
                Intent intentSection = new Intent(BarrierSettingActivity.this, UpdateSectionActivity.class);
                startActivity(intentSection);


                break;
            case R.id.buttonSession:
                Intent intentSessions = new Intent(BarrierSettingActivity.this, UpdateSessionsActivity.class);
                startActivity(intentSessions);
                finish();
                break;
            case R.id.staff_barriers:
                Intent intentStaffBarrier = new Intent(BarrierSettingActivity.this, UpdateStaffBarriersActivity.class);
                startActivity(intentStaffBarrier);

                break;
        }
    }


}
