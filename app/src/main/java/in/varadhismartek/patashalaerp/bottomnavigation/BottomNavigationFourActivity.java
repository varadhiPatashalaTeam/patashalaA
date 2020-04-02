package in.varadhismartek.patashalaerp.bottomnavigation;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import in.varadhismartek.patashalaerp.DashboardModule.DashboardActivity;
import in.varadhismartek.patashalaerp.R;

public class BottomNavigationFourActivity extends AppCompatActivity implements View.OnClickListener {

    LinearLayout llFirstItem,llSecondItem,llThirdItem,llForthItem;
    ImageView ivBarriers;
    TextView tvBarriers;
    View viewFour;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_navigation_four);

        initViews();
        initListners();
    }


    private void initListners() {

        llFirstItem.setOnClickListener(this);
        llSecondItem.setOnClickListener(this);
        llThirdItem.setOnClickListener(this);
        llForthItem.setOnClickListener(this);

    }

    private void initViews() {

        llFirstItem = findViewById(R.id.llFirstItem);
        llSecondItem = findViewById(R.id.llSecondItem);
        llThirdItem = findViewById(R.id.llThirdItem);
        llForthItem = findViewById(R.id.llForthItem);

        ivBarriers = findViewById(R.id.ivForthItem);
        tvBarriers = findViewById(R.id.tvForthItem);
        viewFour = findViewById(R.id.viewFour);
        ivBarriers.setImageDrawable(getResources().getDrawable(R.drawable.ic_setting_filled));
        tvBarriers.setTextColor(getResources().getColor(R.color.orange_colorPrimaryDark));
        viewFour.setVisibility(View.VISIBLE);


    }
    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //finish();
                        finishAffinity();
                        System.exit(0);
                        //super.onBackPressed();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.llFirstItem:

                Intent intentFirst = new Intent(this, DashboardActivity.class);
                intentFirst.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intentFirst);

                break;

            case R.id.llSecondItem:
                Intent intentSecond = new Intent(this, BottomNavigationTwoActivity.class);
                intentSecond.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intentSecond);
                break;

            case R.id.llThirdItem:
                Intent intentThreee = new Intent(this, BottomNavigationThreeActivity.class);
                intentThreee.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intentThreee);
                break;


        }

    }

    @Override
    public void onPause() {
        super.onPause();
        overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);

    }
}
