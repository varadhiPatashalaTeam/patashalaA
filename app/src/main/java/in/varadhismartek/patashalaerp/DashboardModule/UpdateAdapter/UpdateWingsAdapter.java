package in.varadhismartek.patashalaerp.DashboardModule.UpdateAdapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import in.varadhismartek.Utils.Constant;
import in.varadhismartek.Utils.Utilities;
import in.varadhismartek.patashalaerp.AddWing.AddWingsModel;
import in.varadhismartek.patashalaerp.DashboardModule.BarrierSettingActivity;
import in.varadhismartek.patashalaerp.DashboardModule.UpdateActivity.UpdateRolesActivity;
import in.varadhismartek.patashalaerp.DashboardModule.UpdateActivity.UpdateWingsActivity;
import in.varadhismartek.patashalaerp.R;
import in.varadhismartek.patashalaerp.Retrofit.APIService;
import in.varadhismartek.patashalaerp.Retrofit.ApiUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class UpdateWingsAdapter extends RecyclerView.Adapter<UpdateWingsViewHolder> {

    public ArrayList<AddWingsModel> arrayList;
    Context mContext;
    private ArrayList<String> checkedWingsArrayList;
    private ArrayList<String> uncheckedWingsArrayList;
    private Button btn_Save;
    private APIService mApiService;
    String checkBoxValues;
    Dialog dialog;
    Utilities utilities;

    public UpdateWingsAdapter(ArrayList arrayList, Context mContext, Button btn_Save) {
        this.arrayList = arrayList;
        this.mContext = mContext;
        checkedWingsArrayList = new ArrayList<>();
        this.btn_Save = btn_Save;
        uncheckedWingsArrayList = new ArrayList<>();
        mApiService = ApiUtils.getAPIService();
    }

    @NonNull
    @Override
    public UpdateWingsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new UpdateWingsViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.select_module_row_card, parent, false));
    }


    @Override
    public void onBindViewHolder(@NonNull final UpdateWingsViewHolder holder, final int position) {
        final AddWingsModel addWingsModel = arrayList.get(position);
        Log.i("TEXT_NAME",""+addWingsModel.getWingsName());
        if (addWingsModel.getWingsName().length() <= 3) {
            holder.check_box.setText(addWingsModel.getWingsName().toUpperCase());
        } else {
            holder.check_box.setText(addWingsModel.getWingsName());
        }


        boolean flag = addWingsModel.isWingsSelected();

        if (flag) {
            holder.check_box.setBackgroundColor(Color.parseColor("#52b155"));
            holder.check_box.setTextColor(Color.WHITE);
            checkedWingsArrayList.add(arrayList.get(position).getWingsName());
            Log.d("AaCheckedArrayList", "" + checkedWingsArrayList.size());

        } else {
            holder.check_box.setBackgroundColor(Color.WHITE);
            holder.check_box.setTextColor(Color.BLACK);
            checkedWingsArrayList.remove(arrayList.get(position).getWingsName());
            Log.d("AaCheckedArrayList1", "" + checkedWingsArrayList.size());


        }


        holder.check_box.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (!addWingsModel.isWingsSelected()) {
                    checkBoxValues = holder.check_box.getText().toString();
                    checkedWingsArrayList.add(holder.check_box.getText().toString());
                    addWingsModel.setWingsSelected(true);

                    Log.d("Checked", "" + checkedWingsArrayList.size());
                    holder.check_box.setBackgroundColor(Color.parseColor("#52b155"));
                    holder.check_box.setTextColor(Color.WHITE);
                } else {
                    checkedWingsArrayList.remove(holder.check_box.getText().toString());
                    addWingsModel.setWingsSelected(false);
                    Log.d("Unchecked", "" + checkedWingsArrayList.size());
                    holder.check_box.setBackgroundColor(Color.WHITE);
                    holder.check_box.setTextColor(Color.BLACK);
                }
                Log.d("CheckedItem", "" + checkedWingsArrayList.size());


            }
        });
        holder.check_box.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strWings = arrayList.get(position).getWingsName();
                String strWingsStatus = String.valueOf(arrayList.get(position).isWingsSelected());
                JSONObject objectWings = new JSONObject();
                try {

                    JSONObject wings = new JSONObject();
                    wings.put("name", strWings);
                    wings.put("status", strWingsStatus);

                    objectWings.put(String.valueOf(1), wings);

                } catch (JSONException je) {

                }

                JSONObject objectMain = new JSONObject();
                try {
                    objectMain.put("wings", objectWings);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                System.out.println("objectMain**** " + objectMain);
                utilities.displayProgressDialog(mContext, "Please wait...", false);
                updateWingsByStatus(objectMain);


            }
        });

        holder.check_box.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Log.i("TEXT_ACT_VALUE",""+arrayList.get(position).getWingsName());
                openDialogForUpdate(arrayList.get(position).getWingsName());
                notifyDataSetChanged();

                return false;
            }
        });


        btn_Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (uncheckedWingsArrayList.size() > 0) {
                    uncheckedWingsArrayList.clear();
                }

                if (checkedWingsArrayList.size() > 0) {
                    checkedWingsArrayList.clear();
                }

                for (AddWingsModel module : arrayList) {

                    if (!module.isWingsSelected()) {
                        Log.d("sizea", module.getWingsName() + "");
                        uncheckedWingsArrayList.add(module.getWingsName());
                    } else if (module.isWingsSelected()) {
                        Log.d("sizeab", module.getWingsName() + "");

                        checkedWingsArrayList.add(module.getWingsName());
                    }

                }

                JSONArray array = new JSONArray();
                for (int i = 0; i < checkedWingsArrayList.size(); i++) {
                    array.put(checkedWingsArrayList.get(i));

                }

                JSONObject wings = new JSONObject();
                Log.i("checkedWings.size()**", "" + checkedWingsArrayList.size() + "**" + arrayList.size());

                for (int i = 0; i <= checkedWingsArrayList.size(); i++) {
                    JSONObject objWings = new JSONObject();
                    try {
                        objWings.put("name", array.get(i));
                        objWings.put("status", "true");
                        wings.put("" + i, objWings);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                JSONObject main = new JSONObject();
                try {
                    main.put("wings", wings);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Log.i("WINGS_MAIN", "" + main.toString()+"***"+ array.length());


                if (array.length() == 0) {
                    Toast.makeText(mContext, "Please add at least one wings", Toast.LENGTH_SHORT).show();

                } else {
                    utilities.displayProgressDialog(mContext, "Please wait...", false);
                    mApiService.addWings(Constant.SCHOOL_ID, main, Constant.EMPLOYEE_BY_ID).enqueue(new Callback<Object>() {
                        @Override
                        public void onResponse(Call<Object> call, Response<Object> response) {
                            if (response.isSuccessful()) {
                                utilities.cancelProgressDialog();
                                Log.d("Response", response.body().toString());
                                String sub = response.body().toString().substring(9, response.body().toString().length() - 2);
                                Log.d("shaaa", "" + sub);

                                if (response.body().toString() != null || response.body().toString().equals(sub)) {
                                    Toast.makeText(mContext, "Wings Save Successfully. ", Toast.LENGTH_SHORT).show();


                                }
                            } else {
                                try {
                                    assert response.errorBody() != null;
                                    utilities.cancelProgressDialog();
                                    JSONObject object = new JSONObject(response.errorBody().string());
                                    String message = object.getString("message");
                                    Log.d("ADMIN_API", message);
                                    Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
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

            }


        });


    }

    private void updateWingsByStatus(JSONObject objectMain) {
        Log.d("Response", "STATUSR" + Constant.SCHOOL_ID+ Constant.EMPLOYEE_BY_ID+"**"+ objectMain);


        mApiService.updateWingsStatus(Constant.SCHOOL_ID, objectMain, Constant.EMPLOYEE_BY_ID)
                .enqueue(new Callback<Object>() {
                    @Override
                    public void onResponse(Call<Object> call, Response<Object> response) {
                        if (response.isSuccessful()) {

                            Log.d("Response", "STATUSR" + response.body());
                            Toast.makeText(mContext, "Wings Status Update Successfully", Toast.LENGTH_SHORT).show();
                            utilities.cancelProgressDialog();
                        } else {

                        }


                    }

                    @Override
                    public void onFailure(Call<Object> call, Throwable t) {
                        utilities.cancelProgressDialog();
                    }
                });

    }

    private void openDialogForUpdate(final String oldName) {

        dialog = new Dialog(mContext);
        dialog.setContentView(R.layout.update_dialog);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.show();

        final TextView tv_heading = dialog.findViewById(R.id.tv_heading);
        final TextView tv_heading_name = dialog.findViewById(R.id.tv_heading_name);
        final TextView et_new_name = dialog.findViewById(R.id.et_new_name);
        final TextView bt_cancel = dialog.findViewById(R.id.bt_cancel);
        final TextView bt_update = dialog.findViewById(R.id.bt_update);

        tv_heading.setText("Old Wings Name");
        tv_heading_name.setText(oldName);
        et_new_name.setHint("New Wings Name");

        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });

        bt_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String newName = et_new_name.getText().toString();

                if (newName.equals("")) {
                    Toast.makeText(mContext, "New Name Can Not Be Empty", Toast.LENGTH_SHORT).show();

                } else {

                    mApiService.updateSchoolWingName(Constant.SCHOOL_ID,
                            oldName, newName, Constant.EMPLOYEE_BY_ID).enqueue(new Callback<Object>() {
                        @Override
                        public void onResponse(Call<Object> call, Response<Object> response) {

                            JSONObject object = null;

                            if (response.isSuccessful()) {
                                Log.d("UPDATE_WINGS_SUCCESS", response.body().toString());

                                try {
                                    object = new JSONObject(new Gson().toJson(response.body()));
                                    String status = object.getString("status");

                                    if (status.equals("Success")) {
                                        Toast.makeText(mContext, "Successfully updated the wing name in the school",
                                                Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(mContext, UpdateWingsActivity.class);
                                        mContext.startActivity(intent);
                                        ((Activity) mContext).finish();

                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }


                                dialog.dismiss();

                            } else {
                                Log.d("UPDATE_WINGS_FAIL", String.valueOf(response.code()));
                                if (String.valueOf(response.code()).equals("409")) {
                                    Toast.makeText(mContext, "Wing name already exists", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                }
                                if (String.valueOf(response.code()).equals("404")) {
                                    Toast.makeText(mContext, "Old wing name not exists", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                }

                            }

                        }

                        @Override
                        public void onFailure(Call<Object> call, Throwable t) {
                            Log.d("UPDATE_WINGS_EX", t.getMessage());

                        }
                    });
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return arrayList.size();
    }


    public void newValues(String s) {
        arrayList.add(new AddWingsModel(s, true));
        notifyDataSetChanged();
    }
}