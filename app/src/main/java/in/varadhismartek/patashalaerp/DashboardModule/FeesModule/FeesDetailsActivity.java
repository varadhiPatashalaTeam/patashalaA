package in.varadhismartek.patashalaerp.DashboardModule.FeesModule;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import in.varadhismartek.Utils.Constant;
import in.varadhismartek.patashalaerp.DashboardModule.DashboardActivity;
import in.varadhismartek.patashalaerp.R;

public class FeesDetailsActivity extends AppCompatActivity implements View.OnClickListener {
    TextView tvFeeStructure,tvAddFee;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.frame_container);



        setContentView(R.layout.fee_details);
        tvFeeStructure = findViewById(R.id.fee_structure);
        tvAddFee = findViewById(R.id.add_fee);
        tvFeeStructure.setOnClickListener(this);
        tvAddFee.setOnClickListener(this);

        getSupportFragmentManager().beginTransaction().
                replace(R.id.container, new Fragment_FeeList()).addToBackStack(null).commit();
    }




    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fee_structure:
                tvAddFee.setTextColor(Color.parseColor("#000000"));
                tvAddFee.setBackgroundColor(Color.parseColor("#FFFFFF"));

                tvFeeStructure.setTextColor(Color.parseColor("#ffffff"));
                tvFeeStructure.setBackgroundColor(Color.parseColor("#52b155"));

                getSupportFragmentManager().beginTransaction().
                        replace(R.id.container, new Fragment_FeeList()).addToBackStack(null).commit();

                break;

            case R.id.add_fee:
                tvFeeStructure.setTextColor(Color.parseColor("#000000"));
                tvFeeStructure.setBackgroundColor(Color.parseColor("#FFFFFF"));

                tvAddFee.setTextColor(Color.parseColor("#ffffff"));
                tvAddFee.setBackgroundColor(Color.parseColor("#52b155"));


                getSupportFragmentManager().beginTransaction().
                        replace(R.id.container, new Fragment_FeeBarriers()).addToBackStack(null).commit();
                break;
        }
    }



  /*  @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent= new Intent(FeesDetailsActivity.this, DashboardActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }*/
}
