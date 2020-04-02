package in.varadhismartek.patashalaerp.DashboardModule;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import in.varadhismartek.Utils.Constant;
import in.varadhismartek.patashalaerp.GeneralClass.CircleImageView;
import in.varadhismartek.patashalaerp.LoginAndRegistation.LoginScreenActivity;
import in.varadhismartek.patashalaerp.MainActivity;
import in.varadhismartek.patashalaerp.R;
import in.varadhismartek.patashalaerp.Retrofit.APIService;
import in.varadhismartek.patashalaerp.Retrofit.ApiUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_SchoolProfile extends Fragment implements View.OnClickListener {

    private ImageView iv_banner, iv_backBtn, iv_logout;
    CircleImageView iv_logo;
    CircleImageView iv_pocImage;
    APIService apiService;
    TextView tvTitle, tv_schoolname, address, address1, pincode;
    private String school_banner_image = "", school_logo = "", schoolname = "";
    LinearLayout ll_twitter, ll_facebook, ll_google,llPhoneOne,llPhoneTwo,llPocMobile,llAddress,llMail,llPocEmail;
    WebView webView1;
    String Remarks, Website, FaceBook_Link, YouTube, Twitter, LinkedIN, Google_link;
    TextView poc_name, POC_designation, POC_email, POC_mobile;
    String POC_Designation = "", Poc_Name, POC_Mobile_Number, poc_image, POC_Email, Class_From, Class_To, Email_ID;
    TextView tv_year, tv_Registration, tv_organisation_name, tv_schoolId, tv_mobile, tv_phone, tvclass_from, tvclass_to, tv_email;

    String Address_Line_1, Address_Line_2, City, State, Pincode, Country, Trust_Registration_Number, Authorized_Mobile_Number;

    String school_id, Mobile_Number, Fixed_Landline_Number, organisation_name, Subscription_number_of_years,
            Organization_Registration_Number, Subscription_End_Date, Subscription_Start_Date;

    SharedPreferences pref, pref2;
    SharedPreferences.Editor editor, editor2;
    String Authorized_Email, Status,
            Agreed_Monthly_Charges, Monthly_Charges, Agreement_Number,
            Discount_ON_coupon, Agreement_Remarks,
            Agreement_Date;

    String completeAddress;

    TextView tvSchoolLink,tvShoolFacebook,tvSchoolGPlus,tvSchoolTwitter;

    public Fragment_SchoolProfile() {
    }

  /*  @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }*/

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_school_profile, container, false);
        //getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        pref = getActivity().getSharedPreferences("ActivityPREF", Context.MODE_PRIVATE);
        pref2 = getActivity().getSharedPreferences("SecurityStatus", Context.MODE_PRIVATE);
        editor = pref.edit();
        editor2 = pref2.edit();

        initview(view);

        getSchoolprofile();
        return view;
    }


    private void initview(View view) {
        apiService = ApiUtils.getAPIService();
        iv_backBtn = view.findViewById(R.id.iv_backBtn);
        iv_logout = view.findViewById(R.id.iv_logout);
        iv_banner = view.findViewById(R.id.iv_banner);
        iv_logo = view.findViewById(R.id.iv_logo);
        iv_pocImage = view.findViewById(R.id.iv_pocImage);
        tvTitle = view.findViewById(R.id.tvTitle);
        tv_schoolname = view.findViewById(R.id.tv_schoolname);
        address = view.findViewById(R.id.address);
        address1 = view.findViewById(R.id.address1);
        pincode = view.findViewById(R.id.pincode);
        ll_twitter = view.findViewById(R.id.ll_twitter);
        ll_facebook = view.findViewById(R.id.ll_facebook);
        ll_google = view.findViewById(R.id.ll_google);

        llPhoneOne = view.findViewById(R.id.llPhoneOne);
        llPhoneTwo = view.findViewById(R.id.llPhoneTwo);
        llPocMobile = view.findViewById(R.id.llPocMobile);
        llAddress = view.findViewById(R.id.llAddress);
        llMail = view.findViewById(R.id.llMail);
        llPocEmail = view.findViewById(R.id.llPocEmail);

        webView1 = view.findViewById(R.id.webView1);

        tv_email = view.findViewById(R.id.tv_email);
        tvclass_from = view.findViewById(R.id.tvclass_from);
        tvclass_to = view.findViewById(R.id.tvclass_to);
        tv_mobile = view.findViewById(R.id.tv_mobile);
        tv_phone = view.findViewById(R.id.tv_phone);
        poc_name = view.findViewById(R.id.poc_name);
        POC_designation = view.findViewById(R.id.POC_designation);
        POC_email = view.findViewById(R.id.POC_email);
        POC_mobile = view.findViewById(R.id.POC_mobile);
        tv_year = view.findViewById(R.id.tv_year);
        tv_Registration = view.findViewById(R.id.tv_Registration);
        tv_schoolId = view.findViewById(R.id.tv_schoolId);
        tv_organisation_name = view.findViewById(R.id.tv_organisation_name);

        /*tvSchoolLink,tvShoolFacebook,tvSchoolGPlus,tvSchoolTwitter*/

        tvSchoolLink = view.findViewById(R.id.tvSchoolLink);
        tvShoolFacebook = view.findViewById(R.id.tvSchoolFacebook);
        tvSchoolGPlus = view.findViewById(R.id.tvSchoolGPlus);
        tvSchoolTwitter = view.findViewById(R.id.tvSchoolTwitter);

        tvTitle.setText("School Profile");

        ll_twitter.setOnClickListener(this);
        ll_facebook.setOnClickListener(this);
        ll_google.setOnClickListener(this);
        iv_backBtn.setOnClickListener(this);
        iv_logout.setOnClickListener(this);
        llPhoneOne.setOnClickListener(this);
        llPhoneTwo.setOnClickListener(this);
        llPocMobile.setOnClickListener(this);
        llAddress.setOnClickListener(this);
        llMail.setOnClickListener(this);
        llPocEmail.setOnClickListener(this);
        tvSchoolLink.setOnClickListener(this);
        tvShoolFacebook.setOnClickListener(this);
        tvSchoolGPlus.setOnClickListener(this);
        tvSchoolTwitter.setOnClickListener(this);
    }

    private void getSchoolprofile() {
        Log.d("NOTICE_API_Success", Constant.POC_Mobile_Number + Constant.SCHOOL_ID);
        apiService.get_school_details(Constant.POC_Mobile_Number, Constant.SCHOOL_ID).enqueue(new Callback<Object>() {


            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if (response.isSuccessful()) {

                    JSONObject object = null;
                    try {
                        object = new JSONObject(new Gson().toJson(response.body()));
                        String message = object.getString("message");
                        String status = object.getString("status");

                        if (status.equalsIgnoreCase("Success")) {
                            Log.d("NOTICE_API_Success", object.toString());

                            JSONObject keyData = object.getJSONObject("data");


                            if (keyData.isNull("school_banner_image")) {
                                school_banner_image = "";
                            } else {
                                school_banner_image = keyData.getString("school_banner_image");
                            }

                            if (keyData.isNull("school_logo")) {
                                school_logo = "";
                            } else {
                                school_logo = keyData.getString("school_logo");
                            }


                            if (!school_banner_image.equals("") || !school_banner_image.isEmpty()) {
                                Picasso.with(getActivity())
                                        .load(Constant.IMAGE_LINK+school_banner_image)
                                        .resize(150, 150)
                                        .into(iv_banner);
                            } else {
                                //  holder.studentImage.setBackgroundResource(R.drawable.user_icon);
                            }

                            if (!school_logo.equals("") || !school_logo.isEmpty()) {
                                Glide.with(getActivity()).load(Constant.IMAGE_LINK+school_logo).into(iv_logo);

                            } else {
                                Glide.with(getActivity()).load(R.drawable.management_logo).into(iv_logo);

                            }

                            tv_schoolname.setText(keyData.getString("organisation_name"));


                            Status = keyData.getString("Status");


                            Email_ID = keyData.getString("school_Email_ID");
                            Class_From = keyData.getString("Class_From");
                            Class_To = keyData.getString("Class_To");


                            Agreed_Monthly_Charges = keyData.getString("Agreed_Monthly_Charges");
                            Agreement_Number = keyData.getString("Agreement_Number");
                            Discount_ON_coupon = keyData.getString("Discount_ON_coupon");
                            Agreement_Remarks = keyData.getString("Agreement_Remarks");
                            Agreement_Date = keyData.getString("Agreement_Date");

                            Monthly_Charges = keyData.getString("Monthly_Charges");
                            Trust_Registration_Number = keyData.getString("Trust_Registration_Number");
                            Authorized_Mobile_Number = keyData.getString("Authorized_Mobile_Number");
                            Authorized_Email = keyData.getString("Authorized_Email");


                            Subscription_number_of_years = keyData.getString("Subscription_number_of_years");
                            Subscription_Start_Date = keyData.getString("Subscription_Start_Date");
                            Subscription_End_Date = keyData.getString("Subscription_End_Date");
                            school_id = keyData.getString("school_id");
                            organisation_name = keyData.getString("organisation_name");
                            Organization_Registration_Number = keyData.getString("Organization_Registration_Number");
                            Mobile_Number = keyData.getString("school_Mobile_Number");
                            Fixed_Landline_Number = keyData.getString("Fixed_Landline_Number");


                            Remarks = keyData.getString("Remarks");

                            POC_Designation = keyData.getString("school_POC_Designation");
                            POC_Mobile_Number = keyData.getString("school_POC_Mobile_Number");
                            Poc_Name = keyData.getString("school_Poc_Name");
                            POC_Email = keyData.getString("school_Email_ID");
                            poc_image = keyData.getString("school_poc_image");


                            Address_Line_1 = keyData.getString("school_Address_Line_1");
                            Address_Line_2 = keyData.getString("school_Address_Line_2");
                            Country = keyData.getString("Country");
                            City = keyData.getString("school_City");
                            State = keyData.getString("school_State");
                            Pincode = keyData.getString("school_Pincode");

                            Website = keyData.getString("school_Website");
                            YouTube = keyData.getString("YouTube");
                            Twitter = keyData.getString("Twitter");
                            Google_link = keyData.getString("Google_link");
                            LinkedIN = keyData.getString("LinkedIN");
                            FaceBook_Link = keyData.getString("FaceBook_Link");

                            address.setText(Address_Line_1 + ", " + Address_Line_2);
                            address1.setText(City + ", " + State);
                            pincode.setText(Pincode + ", " + Country);

                            completeAddress = Address_Line_1 + ", " +Address_Line_2+", "+City + ", " + State+", "+Pincode + ", " + Country;

                            tv_Registration.setText(Organization_Registration_Number);
                            tv_year.setText(Subscription_number_of_years);
                            tv_organisation_name.setText(organisation_name);
                            tv_schoolId.setText(school_id);
                            tv_phone.setText(Fixed_Landline_Number);
                            tvclass_from.setText(Class_From+ " - "+Class_To);
                            tvclass_to.setText(Class_To);
                            tv_mobile.setText(Mobile_Number);
                            tv_email.setText(Email_ID);

                            poc_name.setText(Poc_Name);
                            POC_designation.setText(POC_Designation);
                            POC_email.setText(POC_Email);
                            POC_mobile.setText(POC_Mobile_Number);

                            tvSchoolLink.setText(""+Website);
                            tvSchoolGPlus.setText(""+Google_link);
                            tvSchoolTwitter.setText(""+Twitter);
                            tvShoolFacebook.setText(""+FaceBook_Link);



                            if (!poc_image.equals("") || !poc_image.isEmpty()) {
                               /* Picasso.with(getActivity())
                                        .load(strImageUrl+ poc_image)

                                        .into(iv_pocImage);*/

                                Glide.with(getActivity()).load(Constant.IMAGE_LINK+poc_image).into(iv_pocImage);
                            } else {
                                //  holder.studentImage.setBackgroundResource(R.drawable.user_icon);
                            }


                        }
                    } catch (JSONException je) {

                        Log.d("sdkfsdj", je.getMessage());
                        je.printStackTrace();

                    }

                } else {
                    try {
                        assert response.errorBody() != null;
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

            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_backBtn:
                getActivity().finish();
                break;
            case R.id.ll_twitter:
                System.out.println("FaceBook_Link**"+Twitter);
                FaceBook_Link ="https://wwww."+Twitter;
                openPdfViewDialog(FaceBook_Link);
                break;

            case R.id.ll_facebook:
                System.out.println("FaceBook_Link**"+FaceBook_Link);
                FaceBook_Link ="https://wwww."+FaceBook_Link;
                openPdfViewDialog(FaceBook_Link);
                break;

            case R.id.ll_google:
                System.out.println("FaceBook_Link**"+Google_link);
                FaceBook_Link ="https://wwww."+Google_link;
                openPdfViewDialog(FaceBook_Link);


                break;
            case R.id.iv_logout:
                logoutMethod();
                break;

            case R.id.llPhoneOne:
                openDialogForContact(tv_phone.getText().toString().trim());
                break;


            case R.id.llPhoneTwo:
                openDialogForContact(tv_mobile.getText().toString().trim());
                break;

            case R.id.llPocMobile:
                openDialogForContact(POC_mobile.getText().toString().trim());
                break;

            case R.id.llAddress:
                addressToMap();
                break;

            case R.id.llMail:
                startActivity(new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:"+tv_email.getText().toString())));

                break;

            case R.id.llPocEmail:
                startActivity(new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:"+POC_email.getText().toString())));
                break;

            case R.id.tvSchoolFacebook:
                openWebBrowser(tvShoolFacebook.getText().toString().trim());
                break;

            case R.id.tvSchoolGPlus:
                openWebBrowser(tvSchoolGPlus.getText().toString().trim());
                break;

            case R.id.tvSchoolLink:
                openWebBrowser(tvSchoolLink.getText().toString().trim());
                break;

            case R.id.tvSchoolTwitter:
                openWebBrowser(tvSchoolTwitter.getText().toString().trim());
                break;




        }
    }

    private void openWebBrowser(String url) {

        Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse(url));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setPackage("com.android.chrome");
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException ex) {
            // Chrome browser presumably not installed so allow user to choose instead
            intent.setPackage(null);
            startActivity(intent);
        }




    }

    private void addressToMap() {
        assert getActivity()!=null;

        Geocoder coder = new Geocoder(getActivity());
        List<Address> address;
        LatLng p1 = null;

        Log.d("Address", completeAddress);
        try {
            // May throw an IOException
            address = coder.getFromLocationName(completeAddress, 5);
            if (address != null) {

                Address location = address.get(0);
                p1 = new LatLng(location.getLatitude(), location.getLongitude() );

                Log.d("LatLong", +p1.latitude+" "+p1.longitude);
                String strUri = "http://maps.google.com/maps?q=loc:" + p1.latitude + "," + p1.longitude + " (" + tv_organisation_name.getText().toString() + ")";
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(strUri));
                intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
                startActivity(intent);

            }



        } catch (IOException ex) {

            ex.printStackTrace();
        }
    }

    private void openDialogForContact(final String number) {

        View view1 = getLayoutInflater().inflate(R.layout.contact_dialog, null);
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getActivity());
        bottomSheetDialog.setContentView(view1);
        ImageView ivCall = view1.findViewById(R.id.ivCall);
        ImageView ivMessage = view1.findViewById(R.id.ivMessage);
        ImageView ivWhatsApp = view1.findViewById(R.id.ivWhatsApp);

        ivCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:"+Uri.encode(number)));
                callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(callIntent);
                bottomSheetDialog.dismiss();
            }
        });

        ivMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent smsMsgAppVar = new Intent(Intent.ACTION_VIEW);
                smsMsgAppVar.setData(Uri.parse("sms:" +  number));
                smsMsgAppVar.putExtra("sms_body", "Write your own message");
                startActivity(smsMsgAppVar);
                bottomSheetDialog.dismiss();
            }
        });

        ivWhatsApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://api.whatsapp.com/send?phone=" +"+91-"+number;
                try {
                    PackageManager pm = getActivity().getPackageManager();
                    pm.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES);
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);
                } catch (PackageManager.NameNotFoundException e) {
                    Toast.makeText(getActivity(), "Whatsapp app not installed in your phone", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
                bottomSheetDialog.dismiss();
            }
        });
        bottomSheetDialog.show();


    }

    private void logoutMethod() {

        new android.app.AlertDialog.Builder(getActivity())
                .setMessage("Are you sure? Do you want logout.")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        editor.clear();
                        editor2.clear();
                        editor.apply();
                        editor2.apply();


                        Intent intent = new Intent(getActivity(), LoginScreenActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                        getActivity().startActivity(intent);
                        getActivity().finish();
                    }
                }).setNegativeButton("No", null).show();


    }

    private void openPdfViewDialog(String faceBook_Link) {
        System.out.println("faceBook_Link**"+faceBook_Link);
        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.pdf_view_layout);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.show();
        dialog.setCancelable(true);

        WebView webview = dialog.findViewById(R.id.webview);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.loadUrl(faceBook_Link);

        webview.getSettings().setPluginState(WebSettings.PluginState.ON);
        webview.setWebViewClient(new WebViewClient() {

            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                //utilities.displayProgressDialog(getActivity(), "Loading..", false);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                // utilities.cancelProgressDialog();

            }
        });

    }


 /*   @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.fragment_menu_items, menu);

        super.onCreateOptionsMenu(menu, inflater);
      //  MenuItem menuItemExpand = menu.findItem(R.id.fragment_menu_search);

       // menuItemExpand.setVisible(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }*/
}
