package in.varadhismartek.patashalaerp.DashboardModule.TransportModule;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageView;

import in.varadhismartek.Utils.Constant;
import in.varadhismartek.patashalaerp.R;

public class TransportActivity extends AppCompatActivity implements View.OnClickListener {
    /*TextView tv_Vehicle,tv_Route;
    ImageView iv_backBtn;*/

    CardView  cvAddBus,cvAddRoute,cvAssignDriver,cvBusIssues,cvParentIssues,cvAddExpenses,cvDriverPerformance,cvAttendance,
            cvNotification,cvTripDetails,cvBusPerformance,cvCctv,cvInventory;

    ImageView iv_backBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transport);




        initViews();
        initListners();

       /* tv_Vehicle=  findViewById(R.id.tv_Vehicle);
        tv_Route=  findViewById(R.id.tv_Route);
        iv_backBtn=  findViewById(R.id.iv_backBtn);


        //loadFragment(Constant.ADD_VEHICLE_FRAGMENT, null);
        loadFragment(Constant.TRANSPORT_INBOX_FRAGMENT, null);

        tv_Route.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_Vehicle.setBackgroundResource(R.color.white);
                tv_Route.setBackground(getResources().getDrawable(R.drawable.btn_round_shape_green));
                tv_Route.setTextColor(getResources().getColor(R.color.white));
                tv_Vehicle.setTextColor(getResources().getColor(R.color.grey));
                loadFragment(Constant.ADD_ROUTE_FRAGMENT, null);
            }
        });

        tv_Vehicle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                tv_Vehicle.setBackground(getResources().getDrawable(R.drawable.btn_round_shape_green));
                tv_Route.setBackgroundResource(R.color.white);
                tv_Route.setTextColor(getResources().getColor(R.color.grey));
                tv_Vehicle.setTextColor(getResources().getColor(R.color.white));

                loadFragment(Constant.TRANSPORT_INBOX_FRAGMENT, null);
            }
        });
        iv_backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });*/
    }

    private void initListners() {
        /*cvAddBus,cvAddRoute,cvAssignDriver,cvBusIssues,cvParentIssues,cvAddExpenses,cvDriverPerformance,cvAttendance,
                cvNotification,cvTripDetails,cvBusPerformance,cvCctv,cvInventory*/

        cvAddBus.setOnClickListener(this);
        cvAddRoute.setOnClickListener(this);
        cvAssignDriver.setOnClickListener(this);
        cvBusIssues.setOnClickListener(this);
        cvParentIssues.setOnClickListener(this);
        cvAddExpenses.setOnClickListener(this);
        cvDriverPerformance.setOnClickListener(this);
        cvAttendance.setOnClickListener(this);
        cvNotification.setOnClickListener(this);
        cvTripDetails.setOnClickListener(this);
        cvBusPerformance.setOnClickListener(this);
        cvCctv.setOnClickListener(this);
        cvInventory.setOnClickListener(this);
        iv_backBtn.setOnClickListener(this);

    }

    private void initViews() {

        cvAddBus=  findViewById(R.id.cvAddBus);
        cvAddRoute=  findViewById(R.id.cvAddRoute);
        cvAssignDriver=  findViewById(R.id.cvAssignDriver);
        cvBusIssues=  findViewById(R.id.cvBusIssues);
        cvParentIssues=  findViewById(R.id.cvParentIssues);
        cvAddExpenses=  findViewById(R.id.cvAddExpenses);
        cvDriverPerformance=  findViewById(R.id.cvDriverPerformance);
        cvAttendance=  findViewById(R.id.cvAttendance);
        cvNotification=  findViewById(R.id.cvNotification);
        cvTripDetails=  findViewById(R.id.cvTripDetails);
        cvBusPerformance=  findViewById(R.id.cvBusPerformance);
        cvCctv=  findViewById(R.id.cvCctv);
        cvInventory=  findViewById(R.id.cvInventory);
        iv_backBtn=  findViewById(R.id.iv_backBtn);
    }

    @Override
    public void onClick(View v) {
        Intent in = new Intent(TransportActivity.this, TransportLandingActivity.class);

        switch (v.getId()){

            case R.id.cvAddBus:

                in.putExtra("FRAGMENT_TAG", Constant.TRANSPORT_INBOX_FRAGMENT);
                startActivity(in);
               // callToast();
                break;
            case R.id.cvAddRoute:
                in.putExtra("FRAGMENT_TAG", Constant.ADD_ROUTE_FRAGMENT);
                startActivity(in);
                //callToast();
                break;
            case R.id.cvAssignDriver:
                callToast();
                break;
            case R.id.cvBusIssues:
                callToast();
                break;
            case R.id.cvParentIssues:
                callToast();
                break;
            case R.id.cvAddExpenses:
                callToast();
                break;
            case R.id.cvDriverPerformance:
                callToast();
                break;
            case R.id.cvAttendance:
                callToast();
                break;
            case R.id.cvNotification:
                callToast();
                break;
            case R.id.cvTripDetails:
                callToast();
                break;
            case R.id.cvBusPerformance:
                callToast();
                break;
            case R.id.cvCctv:
                callToast();
                break;
            case R.id.cvInventory:
                callToast();
                break;

            case R.id.iv_backBtn:
                finish();
                break;



        }



    }

    private void callToast() {
        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Under Development", Snackbar.LENGTH_SHORT);
        snackbar.show();
    }

    public void loadFragment(String fragmentString, Bundle bundle) {

        switch (fragmentString) {

            case Constant.TRANSPORT_INBOX_FRAGMENT:
                callFragment(new TransportInboxFragment(), Constant.TRANSPORT_INBOX_FRAGMENT, null, null);
                break;

            case Constant.ADD_VEHICLE_FRAGMENT:
                callFragment(new AddVehicleFragment(), Constant.ADD_VEHICLE_FRAGMENT, null, null);
                break;

            case Constant.TRANSPORT_DETAILS_FRAGMENT:
                callFragment(new TransportVechileDetails(), Constant.TRANSPORT_DETAILS_FRAGMENT, null, bundle);
                break;


            case Constant.ADD_ROUTE_FRAGMENT:
                callFragment(new AddTransportRouteFragment(), Constant.ADD_ROUTE_FRAGMENT, null, null);
                break;





        }
    }


    private void callFragment(Fragment fragment, String tag, String addBackStack, Bundle bundle) {

        if (bundle != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .replace(R.id.container, fragment, tag)
                    .addToBackStack(addBackStack)
                    .commit();
            fragment.setArguments(bundle);

        } else {

            if (Constant.TRANSPORT_INBOX_FRAGMENT.equals(tag)) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .replace(R.id.container, fragment, tag)
                        .commit();
            } else {
                getSupportFragmentManager().beginTransaction()
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .replace(R.id.container, fragment, tag)
                        .addToBackStack(addBackStack)
                        .commit();
            }
        }
    }
}
