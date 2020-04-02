package in.varadhismartek.patashalaerp.LoginAndRegistation;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import in.varadhismartek.Utils.Constant;
import in.varadhismartek.Utils.Utilities;
import in.varadhismartek.patashalaerp.R;
import in.varadhismartek.patashalaerp.Retrofit.APIService;
import in.varadhismartek.patashalaerp.Retrofit.ApiUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SchoolListAdapter extends RecyclerView.Adapter<SchoolListAdapter.SchoolViewHolder> {
    Context context;
    ArrayList<SchoolModel> schoolModels;
    Utilities utilities;

    String department = "", last_name = "", first_name = "", role = "", adhaar_card_no = "",
            phone_number = "", employee_uuid = "", wing = "", custom_employee_id = "", photo = "";
    String strSchoolID, strSchoolNo, strSchoolPocName, strSchoolLogo;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    APIService apiService;
    Dialog dialogSchool;

    public SchoolListAdapter(Context context, ArrayList<SchoolModel> schoolModels, Dialog dialogSchool) {
        this.context = context;
        this.schoolModels = schoolModels;
        this.dialogSchool = dialogSchool;
        apiService = ApiUtils.getAPIService();

        notifyDataSetChanged();

    }

    @NonNull
    @Override
    public SchoolViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SchoolViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.school_list_details, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final SchoolViewHolder holder, final int position) {
        String schoolLogo = schoolModels.get(position).getStrSchoolLogo();
        String strImageUrl = "http://13.233.109.165:8000/media/";
        if (schoolLogo.isEmpty() || schoolLogo.equalsIgnoreCase("") || schoolLogo.equals(null)) {

        } else {
            Picasso.with(context).
                    load(strImageUrl + schoolLogo)
                    .placeholder(R.drawable.patashala_logo)
                    .resize(90, 70)
                    .into(holder.iv_schoolLogo);

        }

        holder.tv_orgName.setText(schoolModels.get(position).getOrganisationName());
        holder.tv_schoolName.setText(schoolModels.get(position).getSchoolName());
        holder.tv_classFrom.setText("From : " + schoolModels.get(position).getClassFrom());
        holder.tv_classTo.setText("To : " + schoolModels.get(position).getClassTo());

        holder.iv_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.iv_select.setVisibility(View.GONE);
                holder.iv_select_g.setVisibility(View.VISIBLE);


                strSchoolLogo = schoolModels.get(position).getStrSchoolLogo();

                strSchoolID = schoolModels.get(position).getStrSchoolID();
                strSchoolNo = schoolModels.get(position).getStrSchoolNo();
                strSchoolPocName = schoolModels.get(position).getStrSchoolPocName();

                utilities.displayProgressDialog(context, "process ...", false);
                Log.i("response11", "" + strSchoolID+"***" + strSchoolNo+"**"+strSchoolPocName);



              apiService.getEmployeeID(strSchoolID, strSchoolNo, strSchoolPocName).
                enqueue(new Callback<Object>() {
                    @Override
                    public void onResponse(Call<Object> call, Response<Object> response) {
                        Log.i("response1134", "" +response.raw()+response.code()+response.body());
                        utilities.cancelProgressDialog();
                        if (response.isSuccessful()) {
                            try {
                                JSONObject jsonObject = new JSONObject(new Gson().toJson(response.body()));

                                String strStatus = jsonObject.getString(Constant.STATUS);
                                String str = jsonObject.getString("message");

                                Log.i("response1189", "" + strStatus+str);
                                Log.i("response1189", "" + jsonObject.toString());


                                Log.i("response1189", "" + response.body() + str);
                                JSONObject jsonData= jsonObject.getJSONObject("data");

                                Log.i("jsonData456", "" + jsonData.toString());

                                department = jsonData.getString("department");
                                last_name = jsonData.getString("last_name");
                                first_name = jsonData.getString("first_name");
                                role = jsonData.getString("role");
                                adhaar_card_no = jsonData.getString("adhaar_card_no");
                                phone_number = jsonData.getString("phone_number");
                                employee_uuid = jsonData.getString("employee_uuid");
                                wing = jsonData.getString("wing");

                                Log.i("departmentNEW**", "" + department + last_name + first_name + role + employee_uuid + phone_number + wing);

                                if (jsonObject.getJSONObject("data").isNull("photo")) {
                                    photo = "NA";

                                } else {
                                    photo = jsonObject.getJSONObject("data").getString("photo");

                                }

                                if (jsonObject.getJSONObject("data").isNull("custom_employee_id")) {
                                    custom_employee_id = "NA";

                                } else {
                                    custom_employee_id = jsonObject.getJSONObject("data").getString("custom_employee_id");
                                }


                                Log.i("departmentNEW**photo", "" + photo + custom_employee_id);


                                Constant.SCHOOL_LOGO = strSchoolLogo;
                                Constant.SCHOOL_ID = strSchoolID;
                                Constant.POC_NAME = strSchoolPocName;
                                Constant.POC_Mobile_Number = strSchoolNo;
                                Constant.EMPLOYEE_BY_ID = employee_uuid;

                                Constant.EMPLOYEE_FNAME = first_name;
                                Constant.EMPLOYEE_LNAME = last_name;
                                Constant.EMPLOYEE_ADDHAR = adhaar_card_no;
                                Constant.EMPLOYEE_ROLE = role;
                                Constant.EMPLOYEE_DEPARTMENT = department;
                                Constant.EMPLOYEE_WING = wing;
                                Constant.EMPLOYEE_EMP_CUSTOM_ID = custom_employee_id;
                                Constant.EMPLOYEE_PHOTO = photo;
                                Log.i("photo8888888888", "" + photo);

                                preferences = context.getSharedPreferences("login_pref", Context.MODE_PRIVATE);
                                editor = preferences.edit();

                                editor.putString(Constant.SCHOOL_ID, Constant.SCHOOL_ID);
                                editor.putString(Constant.EMPLOYEE_BY_ID, Constant.EMPLOYEE_BY_ID);
                                editor.putString(Constant.POC_NAME, Constant.POC_NAME);
                                editor.putString(Constant.POC_Mobile_Number, Constant.POC_Mobile_Number);
                                editor.putString(Constant.SCHOOL_LOGO, Constant.SCHOOL_LOGO);

                                editor.putString(Constant.EMPLOYEE_FNAME, Constant.EMPLOYEE_FNAME);
                                editor.putString(Constant.EMPLOYEE_LNAME, Constant.EMPLOYEE_LNAME);
                                editor.putString(Constant.EMPLOYEE_ADDHAR, Constant.EMPLOYEE_ADDHAR);
                                editor.putString(Constant.EMPLOYEE_ROLE, Constant.EMPLOYEE_ROLE);
                                editor.putString(Constant.EMPLOYEE_DEPARTMENT, Constant.EMPLOYEE_DEPARTMENT);
                                editor.putString(Constant.EMPLOYEE_WING, Constant.EMPLOYEE_WING);
                                editor.putString(Constant.EMPLOYEE_EMP_CUSTOM_ID, Constant.EMPLOYEE_EMP_CUSTOM_ID);
                                editor.putString(Constant.EMPLOYEE_PHOTO, Constant.EMPLOYEE_PHOTO);

                                editor.apply();

                                Constant.SCHOOL_ID = preferences.getString(Constant.SCHOOL_ID, "");
                                Constant.EMPLOYEE_BY_ID = preferences.getString(Constant.EMPLOYEE_BY_ID, "");
                                Constant.POC_NAME = preferences.getString(Constant.POC_NAME, "");
                                Constant.POC_Mobile_Number = preferences.getString(Constant.POC_Mobile_Number, "");
                                Constant.SCHOOL_LOGO = preferences.getString(Constant.SCHOOL_LOGO, "");

                                Constant.EMPLOYEE_FNAME = preferences.getString(Constant.EMPLOYEE_FNAME, "");
                                Constant.EMPLOYEE_LNAME = preferences.getString(Constant.EMPLOYEE_LNAME, "");
                                Constant.EMPLOYEE_ADDHAR = preferences.getString(Constant.EMPLOYEE_ADDHAR, "");
                                Constant.EMPLOYEE_ROLE = preferences.getString(Constant.EMPLOYEE_ROLE, "");
                                Constant.EMPLOYEE_DEPARTMENT = preferences.getString(Constant.EMPLOYEE_DEPARTMENT, "");
                                Constant.EMPLOYEE_WING = preferences.getString(Constant.EMPLOYEE_WING, "");
                                Constant.EMPLOYEE_PHOTO = preferences.getString(Constant.EMPLOYEE_PHOTO, "");


                                Log.i("EMPLOYEE_PHOTOAAA", "" + Constant.EMPLOYEE_PHOTO);


                                saveDataOnPregerence(str);


                            } catch (JSONException j) {
                                j.printStackTrace();

                            }
                        }

                        else {
                            try {
                                assert response.errorBody()!=null;
                                utilities.cancelProgressDialog();
                                JSONObject object = new JSONObject(response.errorBody().string());
                                String str = object.getString("message");
                                Log.i("ADMIN_API_A", str);

                               // saveDataOnPregerence(str);

                            }catch (Exception e){
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
        });

    }


    private void saveDataOnPregerence(String str) {
        switch (str){
            case "Employee details":
                checkLastFilledBarreries();

                break;
        }

    }

    private void checkLastFilledBarreries() {
        apiService.checkLastfilledBarriers(Constant.SCHOOL_ID).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                Log.i("response***1", "" + response.body() + "***" + response.code());
                if (response.isSuccessful()) {


                    try {
                        JSONObject json1 = (new JSONObject(new Gson().toJson(response.body())));
                        String status = (String) json1.get("status");
                       String str = json1.getString("message");

                        Log.i("response***LAD", "" + response.body() + "***" + response.code());
                        if (status.equalsIgnoreCase("Success")) {

                            if (str.equalsIgnoreCase("All barriers are filled")){
                                Log.i("BARRIERS***1","SET_1");
                                dialogSchool.dismiss();

                                LoginScreenActivity loginScreenActivity2 = (LoginScreenActivity)context;
                                Constant.poc_mob = Constant.POC_Mobile_Number;

                                assert loginScreenActivity2 != null;
                                loginScreenActivity2.loadFragment(Constant.FOURTH_MPIN_FRAGMENT, null);

                            }
                            else{

                                Toast.makeText(context, ""+str, Toast.LENGTH_SHORT).show();
                            }




                        }
                    } catch (JSONException je) {

                        try {
                            assert response.errorBody()!=null;
                            JSONObject object = new JSONObject(response.errorBody().string());
                            String str = object.getString("message");
                            Log.i("checkFilledBarier", str);

                            // saveDataOnPregerence(str);

                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }

                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {

            }
        });
    }





    @Override
    public int getItemCount() {
        return schoolModels.size();
    }






    public class SchoolViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_orgName, tv_schoolName, tv_classFrom, tv_classTo;
        public ImageView iv_schoolLogo, iv_select, iv_select_g;

        public SchoolViewHolder(View itemView) {
            super(itemView);

            iv_schoolLogo = itemView.findViewById(R.id.iv_schoolLogo);
            iv_select = itemView.findViewById(R.id.iv_select);
            iv_select_g = itemView.findViewById(R.id.iv_select_g);
            tv_orgName = itemView.findViewById(R.id.tv_orgName);
            tv_schoolName = itemView.findViewById(R.id.tv_schoolName);
            tv_classFrom = itemView.findViewById(R.id.tv_classFrom);
            tv_classTo = itemView.findViewById(R.id.tv_classTo);
        }
    }
}
