package in.varadhismartek.patashalaerp.DashboardModule.EmployeeModule;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.joanzapata.pdfview.PDFView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;

import in.varadhismartek.Utils.Constant;
import in.varadhismartek.Utils.Utilities;
import in.varadhismartek.patashalaerp.GeneralClass.CircleImageView;
import in.varadhismartek.patashalaerp.GeneralClass.CustomSpinnerAdapter;
import in.varadhismartek.patashalaerp.MainActivity;
import in.varadhismartek.patashalaerp.R;
import in.varadhismartek.patashalaerp.Retrofit.APIService;
import in.varadhismartek.patashalaerp.Retrofit.ApiUtilsPatashala;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EmployeePaySlipBarrier extends Fragment implements View.OnClickListener {
    APIService apiServicePataShala;
    String employeeID = "NA", employeeName = "NA", employeeDept = "NA", employeeDesignation = "NA", employeeImage = "NA";
    TextView tvTitle,tvPdfFile;
    ImageView iv_backBtn;
    TextView tv_EmployeeName, tv_EmployeeID, tv_Department, tv_Designation;
    Utilities utilities;
    CircleImageView civ_EmployeeImage;
    LinearLayout ll_pdfView;
    ArrayList<String> years = new ArrayList<String>();
    ArrayList<String> month = new ArrayList<String>();
    Spinner spn_year, spn_month;
    String strMonth = "", strYear = "", monthCount = "0";
    String monthValue;
    String pdfUrl="";


    public EmployeePaySlipBarrier() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.employee_payslip_view, container, false);
        initView(view);
        initListener();
        getBundle();
        // getEmployeePayslip();

        yearSpinner();
        monthSpinner();
        return view;
    }

    private void monthSpinner() {
        month.clear();
        month.add("Select Month");
        month.add("January");
        month.add("February");
        month.add("March");
        month.add("April");
        month.add("May");
        month.add("June");
        month.add("July");
        month.add("August");
        month.add("September");
        month.add("October");
        month.add("November");
        month.add("December");

        CustomSpinnerAdapter customSpinnerAdapter = new CustomSpinnerAdapter(getActivity(), month);
        spn_month.setAdapter(customSpinnerAdapter);
        spn_month.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position == 0) {
                    strMonth = "";
                } else {
                    strMonth = spn_month.getSelectedItem().toString();
                    int intMonth = spn_month.getSelectedItemPosition();
                    if ((intMonth == 10 || intMonth == 11 || intMonth == 12)) {
                        monthValue = String.valueOf(intMonth);
                    } else {
                        monthValue = monthCount + intMonth;
                    }
                    Log.i("Month**", "" + strMonth + monthValue);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    private void yearSpinner() {
        years.clear();
        years.add("Select Year");
        int thisYear = Calendar.getInstance().get(Calendar.YEAR);

        for (int i = 2010; i <= thisYear; i++) {
            years.add(String.valueOf(i));
        }
        CustomSpinnerAdapter customSpinnerAdapter = new CustomSpinnerAdapter(getActivity(), years);
        spn_year.setAdapter(customSpinnerAdapter);
        spn_year.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position == 0) {
                    strYear = "";
                    spn_month.setSelection(0);
                } else {
                    strYear = spn_year.getSelectedItem().toString();
                    spn_month.setSelection(0);
                    Log.i("YEAR**", "" + strYear);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void getBundle() {

    }

    private void initListener() {
        iv_backBtn.setOnClickListener(this);
        ll_pdfView.setOnClickListener(this);
    }

    private void initView(View view) {
        apiServicePataShala = ApiUtilsPatashala.getService();

        tvTitle = view.findViewById(R.id.tvTitle);
        tvPdfFile = view.findViewById(R.id.tvPdfFile);
        ll_pdfView = view.findViewById(R.id.ll_pdfView);
        spn_year = view.findViewById(R.id.spn_year);
        spn_month = view.findViewById(R.id.spn_month);
        iv_backBtn = view.findViewById(R.id.iv_backBtn);
        civ_EmployeeImage = view.findViewById(R.id.civ_EmployeeImage);
        tv_EmployeeName = view.findViewById(R.id.tv_EmployeeName);
        tv_EmployeeID = view.findViewById(R.id.tv_EmployeeID);
        tv_Department = view.findViewById(R.id.tv_Department);
        tv_Designation = view.findViewById(R.id.tv_Designation);
        tvTitle.setText("Employee Payslip Detail");

        Bundle bundle = getArguments();
        if (bundle != null) {
            employeeID = bundle.getString("EMPUUID");
            employeeName = bundle.getString("EMP_NAME");
            employeeDept = bundle.getString("EMP_DEPT");
            employeeDesignation = bundle.getString("EMP_DESIGNATION");
            employeeImage = bundle.getString("EMP_IMAGE");

            Constant.EMPLOYEE_ID = employeeID;
            Log.i("Constant*EMPLOYEE_ID1", Constant.EMPLOYEE_ID);

            tv_EmployeeName.setText(employeeName);
            tv_EmployeeID.setText(employeeID);
            tv_Department.setText(employeeDept);
            tv_Designation.setText(employeeDesignation);
            Glide.with(getActivity()).load(Constant.IMAGE_URL + employeeImage).into(civ_EmployeeImage);

        }


    }

    private void getEmployeePayslip() {
        utilities.displayProgressDialog(getActivity(), "Please wait...", false);
        apiServicePataShala.getEmployeePaySlipDetails(Constant.SCHOOL_ID, Constant.EMPLOYEE_ID).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if (response.isSuccessful()) {
                    utilities.cancelProgressDialog();
                    try {
                        JSONObject object = new JSONObject(new Gson().toJson(response.body()));
                        String status = object.getString("status");
                        String message = object.getString("message");

                        if (status.equalsIgnoreCase("Success")) {

                            JSONObject jsonData = object.getJSONObject("data");
                            String Total_salary = jsonData.getLong("Total_salary") + "";

                            JSONArray jsonArrayAllowance = object.getJSONArray("Allowance");
                            for (int i = 0; i < jsonArrayAllowance.length(); i++) {
                                JSONObject jsonAllowance = jsonArrayAllowance.getJSONObject(i);

                                String allowanceStatus, employee_payslip_id, added_datetime, employee_percent, pay_name, pay_amount, depends_on,
                                        employee_firstname, fixed_amount, employee_lastname, employer_percent, employee_uuid;

                                if (!jsonAllowance.isNull("status")) {
                                    allowanceStatus = jsonAllowance.getString("allowanceStatus");
                                }
                                if (!jsonAllowance.isNull("employee_payslip_id")) {
                                    employee_payslip_id = jsonAllowance.getString("employee_payslip_id");
                                }
                                if (!jsonAllowance.isNull("added_datetime")) {
                                    added_datetime = jsonAllowance.getString("added_datetime");
                                }
                                if (!jsonAllowance.isNull("employee_percent")) {
                                    employee_percent = jsonAllowance.getString("employee_percent");
                                }
                                if (!jsonAllowance.isNull("pay_name")) {
                                    pay_name = jsonAllowance.getString("pay_name");
                                }
                                if (!jsonAllowance.isNull("pay_amount")) {
                                    pay_amount = jsonAllowance.getString("pay_amount");
                                }
                                if (!jsonAllowance.isNull("depends_on")) {
                                    depends_on = jsonAllowance.getString("depends_on");
                                }
                                if (!jsonAllowance.isNull("employee_firstname")) {
                                    employee_firstname = jsonAllowance.getString("employee_firstname");
                                }
                                if (!jsonAllowance.isNull("fixed_amount")) {
                                    fixed_amount = jsonAllowance.getString("fixed_amount");
                                }
                                if (!jsonAllowance.isNull("employee_lastname")) {
                                    employee_lastname = jsonAllowance.getString("employee_lastname");
                                }
                                if (!jsonAllowance.isNull("employer_percent")) {
                                    employer_percent = jsonAllowance.getString("employer_percent");
                                }
                                if (!jsonAllowance.isNull("employee_uuid")) {
                                    employee_uuid = jsonAllowance.getString("employee_uuid");
                                }

                            }
                        }
                    } catch (JSONException je) {
                        je.printStackTrace();
                    }
                } else {
                    try {
                        assert response.errorBody() != null;
                        utilities.cancelProgressDialog();
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
                utilities.cancelProgressDialog();

            }
        });

    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_backBtn:
                getActivity().onBackPressed();
                break;

            case R.id.ll_pdfView:
                if ((strYear.equalsIgnoreCase("Select Year")) ||strYear.isEmpty() ||
                        (strMonth.equalsIgnoreCase("Select Month")) ||strMonth.isEmpty()){
                    Toast.makeText(getActivity(),"Select Valid Year & month ",Toast.LENGTH_SHORT).show();
                }else {

                    getEmployeeSalarySlip(strYear, monthValue);
                    //openPdfViewDialog(strMonth, strYear);
                }

                break;
        }
    }

    private void getEmployeeSalarySlip(final String strYear, String monthValue) {
        utilities.displayProgressDialog(getActivity(), "Please wait...", false);
        apiServicePataShala.getEmployeePaySlip(Constant.SCHOOL_ID, Constant.EMPLOYEE_ID, strYear, monthValue)
                .enqueue(new Callback<Object>() {
                    @Override
                    public void onResponse(Call<Object> call, Response<Object> response) {

                        Log.i("PDF FILE", "" + response.code() + response.raw() + response.body());
                        if (response.isSuccessful()) {
                            utilities.cancelProgressDialog();
                            Log.i("Emp Payslip3", ""+response.code()+response.body());

                        } else {
                            try {
                                assert response.errorBody() != null;
                                utilities.cancelProgressDialog();
                                JSONObject object = new JSONObject(response.errorBody().string());
                                String message = object.getString("message");
                                Log.i("Emp Payslip", ""+message);


                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        pdfUrl = "http://androhub.com/demo/demo.pdf";
                        tvPdfFile.setText(pdfUrl);
                        openPdfViewDialog(strMonth, strYear,pdfUrl);

                    }

                    @Override
                    public void onFailure(Call<Object> call, Throwable t) {
                        utilities.cancelProgressDialog();
                        Log.i("Emp Payslip1", t.toString());
                    }
                });

    }

    private void openPdfViewDialog(String strMonth, String strYear,final String pdfUrl) {

        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.pdf_view_layout);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.show();
        dialog.setCancelable(true);
        PDFView pdfView = dialog.findViewById(R.id.pdfview);
        WebView webview = dialog.findViewById(R.id.webview);
        LinearLayout ll_close = dialog.findViewById(R.id.ll_close);
        LinearLayout ll_download = dialog.findViewById(R.id.ll_download);
        TextView tv_payslipMonth = dialog.findViewById(R.id.tv_payslipMonth);

        webview.getSettings().setJavaScriptEnabled(true);

          //final String pdf = "https://github.github.com/training-kit/downloads/github-git-cheat-sheet.pdf";
        //String pdf = "http://www.adobe.com/devnet/acrobat/pdfs/pdf_open_parameters.pdf";
        webview.loadUrl("http://drive.google.com/viewerng/viewer?embedded=true&url=" + pdfUrl);


        tv_payslipMonth.setText("Payslip " + strMonth + " " + strYear);
        ll_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


          webview.getSettings().setPluginState(WebSettings.PluginState.ON);
        webview.setWebViewClient(new WebViewClient() {

            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                utilities.displayProgressDialog(getActivity(), "Loading..", false);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                utilities.cancelProgressDialog();

            }
        });

        ll_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                System.out.println("pdf File***"+pdfUrl);
                pdfSaveMethod(pdfUrl);
            }
        });




    }

    private void pdfSaveMethod(String pdfUrl) {
        new DownloadTask(getActivity(),pdfUrl);
    }

    //Check if internet is present or not




}
