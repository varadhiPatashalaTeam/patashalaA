package in.varadhismartek.patashalaerp.AddWing;

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
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

import in.varadhismartek.Utils.Constant;
import in.varadhismartek.patashalaerp.AddDepartment.AddDepartmentActivity;
import in.varadhismartek.patashalaerp.R;
import in.varadhismartek.patashalaerp.Retrofit.APIService;
import in.varadhismartek.patashalaerp.Retrofit.ApiUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by varadhi on 25/9/18.
 */

public class AddWingsAdapter extends RecyclerView.Adapter<AddWingsViewHolder> {

    public ArrayList<AddWingsModel> arrayList;
    private Context mContext;
    private ArrayList<String> checkedWingsArrayList;
    private ArrayList<String> uncheckedWingsArrayList;
    private ImageView btn_Save;
    private APIService mApiService;
    String checkBoxValues;


    public AddWingsAdapter(ArrayList arrayList, Context mContext, ImageView btn_Save) {
        this.arrayList = arrayList;
        this.mContext = mContext;
        checkedWingsArrayList = new ArrayList<>();
        this.btn_Save = btn_Save;
        uncheckedWingsArrayList = new ArrayList<>();
        mApiService = ApiUtils.getAPIService();
    }

    @NonNull
    @Override
    public AddWingsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AddWingsViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.select_module_row_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final AddWingsViewHolder holder, final int position) {
        final AddWingsModel addWingsModel = arrayList.get(position);

        holder.check_box.setText(addWingsModel.getWingsName());
        boolean flag = addWingsModel.isWingsSelected();

        if (flag) {
            holder.check_box.setBackgroundColor(Color.parseColor("#72d548"));
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
                     checkBoxValues= holder.check_box.getText().toString();
                    checkedWingsArrayList.add(holder.check_box.getText().toString());
                    addWingsModel.setWingsSelected(true);

                    Log.d("Checked", "" + checkedWingsArrayList.size());
                    holder.check_box.setBackgroundColor(Color.parseColor("#72d548"));
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

                Log.d("CheckedWings", "" + checkedWingsArrayList.size());
                Log.d("UnCheckedWings", "" + uncheckedWingsArrayList.size());


                JSONArray array = new JSONArray();
                for (int i = 0; i < checkedWingsArrayList.size(); i++) {
                    array.put(checkedWingsArrayList.get(i));

                }

                JSONObject wings = new JSONObject();
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

                mApiService.addWings(Constant.SCHOOL_ID, main, Constant.EMPLOYEE_BY_ID).enqueue(new Callback<Object>() {
                    @Override
                    public void onResponse(Call<Object> call, Response<Object> response) {
                        if (response.isSuccessful()) {
                            Log.d("Response", response.body().toString());
                            String sub = response.body().toString().substring(9, response.body().toString().length() - 2);
                            Log.d("shaaa", "" + sub);
                            if (response.body().toString() != null || response.body().toString().equals(sub)) {

                            } else if (response.body().toString().equals("Try again later")) {
                                Toast.makeText(mContext, "Data not sent", Toast.LENGTH_SHORT).show();
                            }
                        } else {

                        }


                    }

                    @Override
                    public void onFailure(Call<Object> call, Throwable t) {

                    }
                });

                if (checkedWingsArrayList.size() > 0) {

                    Intent intent = new Intent(mContext, AddDepartmentActivity.class);
                    mContext.startActivity(intent);
                } else {
                    Toast.makeText(mContext, "Please Add Wings", Toast.LENGTH_SHORT).show();

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
