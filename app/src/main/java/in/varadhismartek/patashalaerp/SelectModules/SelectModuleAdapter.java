package in.varadhismartek.patashalaerp.SelectModules;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import in.varadhismartek.Utils.Constant;
import in.varadhismartek.patashalaerp.AddWing.AddWingsActivity;
import in.varadhismartek.patashalaerp.DashboardModule.DashboardActivity;
import in.varadhismartek.patashalaerp.DashboardModule.UpdateActivity.UpdateSelelectModuleActivity;
import in.varadhismartek.patashalaerp.DivisionModule.AddDivisionActivity;
import in.varadhismartek.patashalaerp.R;
import in.varadhismartek.patashalaerp.Retrofit.APIService;
import in.varadhismartek.patashalaerp.Retrofit.ApiUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by varadhi on 25/9/18.
 */
public class SelectModuleAdapter extends RecyclerView.Adapter<SelectModuleViewHolder> {

    private SparseBooleanArray itemStateArray = new SparseBooleanArray();
    public ArrayList<SelectModuleModel> arrayList;
    private Context mContext;
    private ArrayList<String> checkedArrayList;
    private ArrayList<String> selectAllList;
    private Button btnSelectAll;
    private Button btn_Save;
    APIService mApiService;
    String update;
    boolean bSelect =false;
    String moduleStatus,updateModuleStatus;
    String strModuleName,updateModuleName;

    public SelectModuleAdapter(ArrayList arrayList, Context mContext, Button btn_SelectAll, Button btn_Save) {
        this.arrayList = arrayList;
        this.mContext = mContext;
        checkedArrayList = new ArrayList<>();
        this.btnSelectAll = btn_SelectAll;
        this.btn_Save = btn_Save;
        mApiService = ApiUtils.getAPIService();


    }

    public SelectModuleAdapter(ArrayList<SelectModuleModel> arrayList, Context mContext,
                               Button btn_selectAll, Button btn_Save, String update) {
        this.arrayList = arrayList;
        this.mContext = mContext;
        this.btnSelectAll = btn_selectAll;
        this.btn_Save = btn_Save;
        this.update = update;

        checkedArrayList = new ArrayList<>();
        mApiService = ApiUtils.getAPIService();

    }

    @NonNull
    @Override
    public SelectModuleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SelectModuleViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.select_module_row_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final SelectModuleViewHolder holder, final int position) {
        final SelectModuleModel adapterPojoClass = arrayList.get(position);
        boolean flag;
        switch (update) {
            case Constant.UPDATE:
                holder.check_box.setText(adapterPojoClass.getModuleNAme());
                System.out.println("name**"+adapterPojoClass.getModuleNAme());

                updateModuleName = arrayList.get(position).getModuleNAme();
                flag = adapterPojoClass.isModuleSelected();
                if (updateModuleName.equals("Payroll")||updateModuleName.equals("Library")||updateModuleName.equals("Notifications")
                        ||updateModuleName.equals("Timeline")||updateModuleName.equals("Chat")||updateModuleName.equals("Gallery")
                        ||updateModuleName.equals("Newsletters")||updateModuleName.equals("Visitors")||updateModuleName.equals("Transport")
                        ||updateModuleName.equals("Canteen")||updateModuleName.equals("Remainder Alerts")) {

                    if (flag) {
                        holder.check_box.setBackgroundColor(Color.parseColor("#52b155"));
                        holder.check_box.setTextColor(Color.WHITE);
                        checkedArrayList.add(arrayList.get(position).getModuleNAme());
                    }
                }else {
                    holder.check_box.setBackgroundColor(Color.YELLOW);
                    holder.check_box.setTextColor(Color.BLACK);
                    checkedArrayList.add(arrayList.get(position).getModuleNAme());
                }



                holder.check_box.setChecked(flag);
                holder.check_box.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                        String moduleName = arrayList.get(position).getModuleNAme();


                        if (moduleName.equals("Payroll")||moduleName.equals("Library")||moduleName.equals("Notifications")
                                ||moduleName.equals("Timeline")||moduleName.equals("Chat")||moduleName.equals("Gallery")
                                ||moduleName.equals("Newsletters")||moduleName.equals("Visitors")||moduleName.equals("Transport")
                                ||moduleName.equals("Canteen")||moduleName.equals("Remainder Alerts")) {

                            updateModuleStatus = String.valueOf(arrayList.get(position).isModuleSelected());

                            System.out.println("moduleName***"+moduleName+"**"+isChecked);
                            if (isChecked) {
                                updateModuleStatus = "true";
                                holder.check_box.setBackgroundColor(Color.parseColor("#52b155"));
                                holder.check_box.setTextColor(Color.WHITE);
                                updateAPI(moduleName,updateModuleStatus);
                            }
                            else  {
                                updateModuleStatus = "false";
                                holder.check_box.setBackgroundColor(Color.WHITE);
                                holder.check_box.setTextColor(Color.BLACK);
                                updateAPI(moduleName,updateModuleStatus);
                            }
                        }
                        else {
                            updateModuleStatus="true";
                            holder.check_box.setBackgroundColor(Color.YELLOW);
                            holder.check_box.setTextColor(Color.BLACK);
                            updateAPI(moduleName,updateModuleStatus);
                        }
                    }
                });

                btn_Save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        Log.i("ARRAYLIST", "b" + bSelect+selectAllList.size());
                        if(bSelect){
                            JSONObject objModule = new JSONObject();
                            for (int i = 0; i < selectAllList.size(); i++) {
                                JSONObject obj = new JSONObject();
                                try {
                                    obj.put("name", selectAllList.get(i));
                                    obj.put("status", "true");
                                    objModule.put("" + i, obj);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            JSONObject mainModule = new JSONObject();

                            try {
                                mainModule.put("modules", objModule);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            Log.d("addModule***", "" + mainModule);
                            mApiService.updateModuleStatus(Constant.SCHOOL_ID, mainModule, Constant.EMPLOYEE_BY_ID)
                                    .enqueue(new Callback<Object>() {
                                        @Override
                                        public void onResponse(Call<Object> call, Response<Object> response) {
                                            if (response.isSuccessful()) {
                                                bSelect=false;
                                                checkedArrayList.clear();
                                                Log.i("SELECT_MODULE", "" + response.body());
                                                Toast.makeText(mContext, "Module Update Successfully ", Toast.LENGTH_SHORT).show();

                                                Intent intent = new Intent(mContext, UpdateSelelectModuleActivity.class);
                                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                mContext.startActivity(intent);
                                            } else {
                                                Toast.makeText(mContext, "Select the module ", Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<Object> call, Throwable t) {
                                            Log.d("ADD_MODULE_EX", t.getMessage());

                                        }
                                    });

                        }
                        else {
                            Log.i("ARRAYLIST", "ccc" + checkedArrayList.size());
                            JSONObject objModule = new JSONObject();
                            for (int i = 0; i < checkedArrayList.size(); i++) {
                                JSONObject obj = new JSONObject();
                                try {
                                    obj.put("name", checkedArrayList.get(i));
                                    obj.put("status", "true");
                                    objModule.put("" + i, obj);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            JSONObject mainModule = new JSONObject();
                            try {
                                mainModule.put("modules", objModule);
                            }
                            catch (JSONException e)
                            {
                                e.printStackTrace();
                            }

                            mApiService.addSchoolModules(Constant.SCHOOL_ID, mainModule, Constant.EMPLOYEE_BY_ID)
                                    .enqueue(new Callback<Object>() {
                                        @Override
                                        public void onResponse(Call<Object> call, Response<Object> response) {
                                            if (response.isSuccessful()) {
                                                checkedArrayList.clear();
                                                Toast.makeText(mContext, "Module Save Successfully ", Toast.LENGTH_SHORT).show();

                                                Log.i("SELECT_MODULE", "" + response.body());
                                                Intent intent = new Intent(mContext, UpdateSelelectModuleActivity.class);
                                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                mContext.startActivity(intent);

                                            } else {
                                                Toast.makeText(mContext, "Select the module ", Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<Object> call, Throwable t) {
                                            Log.d("ADD_MODULE_EX", t.getMessage());

                                        }
                                    });

                        }


                    }


                });
                btnSelectAll.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {

                        bSelect = true;
                        selectAllList = new ArrayList<>();
                        selectAllList.clear();
                        String status = btnSelectAll.getText().toString().trim();
                        if (status.equalsIgnoreCase("Select All"))
                        {
                            btnSelectAll.setText("Unselect All");
                            btnSelectAll.setBackground(mContext.getResources().getDrawable(R.drawable.btn_round_shape_green));
                            btnSelectAll.setTextColor(Color.WHITE);
                            if (checkedArrayList.size() > 0)
                                checkedArrayList.clear();

                            for (int i = 0; i < arrayList.size(); i++) {
                                arrayList.get(i).setModuleSelected(true);
                                selectAllList.add(arrayList.get(i).getModuleNAme());
                                checkedArrayList.add(arrayList.get(i).getModuleNAme());
                                Log.d("asdasd", "" + checkedArrayList.size());

                            }
                            Toast.makeText(mContext, "All Module have Selected ", Toast.LENGTH_SHORT).show();


                            notifyDataSetChanged();

                        }
                        else if (status.equalsIgnoreCase("Unselect All"))
                        {

                            btnSelectAll.setText("Select All");
                            btnSelectAll.setBackground(mContext.getResources().getDrawable(R.drawable.btn_round_shape_grey));
                            btnSelectAll.setTextColor(mContext.getResources().getColor(R.color.colorBlack));

                            for (int i = 0; i < arrayList.size(); i++) {
                                arrayList.get(i).setModuleSelected(false);
                            }

                            Toast.makeText(mContext, "All Module have UnSelected ", Toast.LENGTH_SHORT).show();

                            if (checkedArrayList.size() > 0)
                                checkedArrayList.clear();


                            notifyDataSetChanged();

                        }

                    }
                });
                break;









            case Constant._NOT_UPDATE:

                holder.check_box.setText(adapterPojoClass.getModuleNAme());
                System.out.println("name**"+adapterPojoClass.getModuleNAme());

                strModuleName = arrayList.get(position).getModuleNAme();
                flag = adapterPojoClass.isModuleSelected();
                if (strModuleName.equals("Payroll")||strModuleName.equals("Library")||strModuleName.equals("Notifications")
                        ||strModuleName.equals("Timeline")||strModuleName.equals("Chat")||strModuleName.equals("Gallery")
                        ||strModuleName.equals("Newsletters")||strModuleName.equals("Visitors")||strModuleName.equals("Transport")
                        ||strModuleName.equals("Canteen")||strModuleName.equals("Remainder Alerts")) {

                    if (flag) {
                        holder.check_box.setBackgroundColor(Color.parseColor("#52b155"));
                        holder.check_box.setTextColor(Color.WHITE);
                        checkedArrayList.add(arrayList.get(position).getModuleNAme());
                    }
                }else {
                    holder.check_box.setBackgroundColor(Color.YELLOW);
                    holder.check_box.setTextColor(Color.BLACK);
                    checkedArrayList.add(arrayList.get(position).getModuleNAme());
                }


                holder.check_box.setChecked(flag);
                holder.check_box.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                        String moduleName = arrayList.get(position).getModuleNAme();


                        if (moduleName.equals("Payroll")||moduleName.equals("Library")||moduleName.equals("Notifications")
                                ||moduleName.equals("Timeline")||moduleName.equals("Chat")||moduleName.equals("Gallery")
                                ||moduleName.equals("Newsletters")||moduleName.equals("Visitors")||moduleName.equals("Transport")
                                ||moduleName.equals("Canteen")||moduleName.equals("Remainder Alerts")) {

                            moduleStatus = String.valueOf(arrayList.get(position).isModuleSelected());

                            if (isChecked) {
                                moduleStatus = "true";
                                holder.check_box.setBackgroundColor(Color.parseColor("#52b155"));
                                holder.check_box.setTextColor(Color.WHITE);
                                updateAPI(moduleName,moduleStatus);
                            }
                            else  {
                                moduleStatus = "false";
                                holder.check_box.setBackgroundColor(Color.WHITE);
                                holder.check_box.setTextColor(Color.BLACK);
                                updateAPI(moduleName,moduleStatus);
                            }
                        }
                        else {
                            moduleStatus="true";
                            holder.check_box.setBackgroundColor(Color.YELLOW);
                            holder.check_box.setTextColor(Color.BLACK);
                            updateAPI(moduleName,moduleStatus);
                        }
                    }
                });
                btn_Save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Log.i("ARRAYLIST", "b" + bSelect+selectAllList.size());
                        if(bSelect){
                            JSONObject objModule = new JSONObject();
                            for (int i = 0; i < selectAllList.size(); i++) {
                                JSONObject obj = new JSONObject();
                                try {
                                    obj.put("name", selectAllList.get(i));
                                    obj.put("status", "true");
                                    objModule.put("" + i, obj);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            JSONObject mainModule = new JSONObject();

                            try {
                                mainModule.put("modules", objModule);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            Log.d("addModule***", "" + mainModule);
                            mApiService.updateModuleStatus(Constant.SCHOOL_ID, mainModule, Constant.EMPLOYEE_BY_ID)
                                    .enqueue(new Callback<Object>() {
                                        @Override
                                        public void onResponse(Call<Object> call, Response<Object> response) {
                                            if (response.isSuccessful()) {
                                                bSelect=false;
                                                checkedArrayList.clear();
                                                Log.i("SELECT_MODULE", "" + response.body());

                                                Intent intent = new Intent(mContext, AddWingsActivity.class);
                                                mContext.startActivity(intent);
                                            } else {
                                                Toast.makeText(mContext, "Select the module ", Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<Object> call, Throwable t) {
                                            Log.d("ADD_MODULE_EX", t.getMessage());

                                        }
                                    });

                        }
                        else {
                            Log.i("ARRAYLIST", "ccc" + checkedArrayList.size());
                            JSONObject objModule = new JSONObject();
                            for (int i = 0; i < checkedArrayList.size(); i++) {
                                JSONObject obj = new JSONObject();
                                try {
                                    obj.put("name", checkedArrayList.get(i));
                                    obj.put("status", "true");
                                    objModule.put("" + i, obj);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            JSONObject mainModule = new JSONObject();
                            try {
                                mainModule.put("modules", objModule);
                            }
                            catch (JSONException e)
                            {
                                e.printStackTrace();
                            }

                            mApiService.addSchoolModules(Constant.SCHOOL_ID, mainModule, Constant.EMPLOYEE_BY_ID)
                                    .enqueue(new Callback<Object>() {
                                        @Override
                                        public void onResponse(Call<Object> call, Response<Object> response) {
                                            if (response.isSuccessful()) {
                                                checkedArrayList.clear();
                                                Log.i("SELECT_MODULE", "" + response.body());
                                                Intent intent = new Intent(mContext, AddWingsActivity.class);
                                                mContext.startActivity(intent);
                                            } else {
                                                Toast.makeText(mContext, "Select the module ", Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<Object> call, Throwable t) {
                                            Log.d("ADD_MODULE_EX", t.getMessage());

                                        }
                                    });

                        }

                    }


                });
                btnSelectAll.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        bSelect = true;
                        selectAllList = new ArrayList<>();
                        selectAllList.clear();
                        String status = btnSelectAll.getText().toString().trim();
                        if (status.equalsIgnoreCase("Select All"))
                        {
                            btnSelectAll.setText("Unselect All");
                            btnSelectAll.setBackground(mContext.getResources().getDrawable(R.drawable.btn_round_shape_green));
                            btnSelectAll.setTextColor(Color.WHITE);
                            if (checkedArrayList.size() > 0)
                                checkedArrayList.clear();

                            for (int i = 0; i < arrayList.size(); i++) {
                                arrayList.get(i).setModuleSelected(true);
                                selectAllList.add(arrayList.get(i).getModuleNAme());
                                checkedArrayList.add(arrayList.get(i).getModuleNAme());
                                Log.d("asdasd", "" + checkedArrayList.size());

                            }


                            notifyDataSetChanged();

                        }
                        else if (status.equalsIgnoreCase("Unselect All"))
                        {

                            btnSelectAll.setText("Select All");
                            btnSelectAll.setBackground(mContext.getResources().getDrawable(R.drawable.btn_round_shape_grey));
                            btnSelectAll.setTextColor(mContext.getResources().getColor(R.color.colorBlack));

                            for (int i = 0; i < arrayList.size(); i++) {
                                arrayList.get(i).setModuleSelected(false);
                            }

                            if (checkedArrayList.size() > 0)
                                checkedArrayList.clear();

                            notifyDataSetChanged();

                        }

                    }
                });
                break;
        }

    }

    private void updateAPI(String moduleName, String moduleStatus) {
        JSONObject jsonModulesName = new JSONObject();
        JSONObject jsonModules = new JSONObject();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("name", moduleName);
            jsonObject.put("status", moduleStatus);
            jsonModules.put("1", jsonObject);
            jsonModulesName.put("modules", jsonModules);
        } catch (JSONException je) { }
        System.out.println("jsonModulesName****nnn"+jsonModulesName);
        moduleStatusUpdate(jsonModulesName);
    }

    private void moduleStatusUpdate(JSONObject jsonModulesName) {

        mApiService.updateModuleStatus(Constant.SCHOOL_ID, jsonModulesName, Constant.EMPLOYEE_BY_ID).
                enqueue(new Callback<Object>() {
                    @Override
                    public void onResponse(Call<Object> call, Response<Object> response) {
                        Log.i("ModuleStatus", "" + response.body());
                    }

                    @Override
                    public void onFailure(Call<Object> call, Throwable t) {

                    }
                });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}