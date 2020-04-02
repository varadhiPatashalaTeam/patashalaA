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

import in.varadhismartek.patashalaerp.BarriersPage;
import in.varadhismartek.patashalaerp.DashboardModule.DashboardActivity;
import in.varadhismartek.patashalaerp.R;

public class BottomNavigationThreeActivity extends AppCompatActivity implements View.OnClickListener {

    LinearLayout llFirstItem,llSecondItem,llThirdItem,llForthItem;
    ImageView ivChat;
    TextView tvChat;
    View viewThree;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_navigation_three);


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


        ivChat = findViewById(R.id.ivThirdItem);
        tvChat = findViewById(R.id.tvThirdItem);
        viewThree = findViewById(R.id.viewThree);
        ivChat.setImageDrawable(getResources().getDrawable(R.drawable.ic_chat_filled));
        tvChat.setTextColor(getResources().getColor(R.color.orange_colorPrimaryDark));
        viewThree.setVisibility(View.VISIBLE);

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


            case R.id.llForthItem:
                BarriersPage barriersPage = new BarriersPage(this);
                barriersPage.openOTPDialog(this);
                break;
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);

    }
}
