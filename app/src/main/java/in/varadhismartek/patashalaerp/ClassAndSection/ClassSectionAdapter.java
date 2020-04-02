package in.varadhismartek.patashalaerp.ClassAndSection;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import in.varadhismartek.Utils.Constant;
import in.varadhismartek.patashalaerp.DivisionModule.AddDivisionActivity;
import in.varadhismartek.patashalaerp.DivisionModule.ClassSectionAndDivisionModel;
import in.varadhismartek.patashalaerp.R;
import in.varadhismartek.patashalaerp.Retrofit.APIService;
import in.varadhismartek.patashalaerp.Retrofit.ApiUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ClassSectionAdapter extends RecyclerView.Adapter<ClassViewHolder> {
    Context context;
    private ArrayList<ClassSectionAndDivisionModel> arrayList, newarrayList;
    private ArrayList<ClassSectionModel> modelArrayList;
    private ArrayList<String> list;
    private ImageView ivSendClass, iv_sendAddSubmit;
    private ArrayList<String> checkedArrayList;
    private ArrayList<String> uncheckedArrayList;
    private String recyclerTag;
    private Button buttonAdd, btnSave;
    private ArrayList<DivisionClassModel> divisionClassModels;
    private APIService mApiService;
    boolean b2 = true;

    public ClassSectionAdapter(ArrayList<ClassSectionAndDivisionModel> arrayListForClasses, Context context,
            /*Button buttonAdd,*/ ImageView iv_sendAddClasses, String recyclerTag) {
        this.context = context;
        this.arrayList = arrayListForClasses;
        //  this.buttonAdd =buttonAdd;
        this.ivSendClass = iv_sendAddClasses;
        this.recyclerTag = recyclerTag;
        checkedArrayList = new ArrayList<>();
        uncheckedArrayList = new ArrayList<>();

        newarrayList = new ArrayList<>();

        mApiService = ApiUtils.getAPIService();

    }

    public ClassSectionAdapter(Context context, ArrayList<ClassSectionModel> modelArrayList, Button btnSave, String recyclerTag) {

        this.context = context;
        this.recyclerTag = recyclerTag;
        this.modelArrayList = modelArrayList;
        this.btnSave = btnSave;

        mApiService = ApiUtils.getAPIService();
    }


    @NonNull
    @Override
    public ClassViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (recyclerTag) {
            case Constant.RV_CLASSES_CARD:
                return new ClassViewHolder(LayoutInflater.from(context)
                        .inflate(R.layout.select_module_row_card, parent, false));
            case Constant.RV_SECTION_ROW:
                return new ClassViewHolder(LayoutInflater.from(context)
                        .inflate(R.layout.section_item_card, parent, false));
        }
        return null;

    }

    @Override
    public void onBindViewHolder(@NonNull final ClassViewHolder holder, final int i) {
        switch (recyclerTag) {
            case Constant.RV_CLASSES_CARD:

                holder.check_box.setText(arrayList.get(i).getName());
                boolean flag1 = arrayList.get(i).isChecked();
                if (flag1) {
                    holder.check_box.setBackgroundColor(Color.parseColor("#72d548"));
                    holder.check_box.setTextColor(Color.WHITE);
                    checkedArrayList.add(arrayList.get(i).getName());
                    Log.d("AaCheckedArrayList", "" + checkedArrayList.size());

                } else {
                    holder.check_box.setBackgroundColor(Color.WHITE);
                    holder.check_box.setTextColor(Color.BLACK);
                    checkedArrayList.remove(arrayList.get(i).getName());
                    Log.d("AaCheckedArrayList1", "" + checkedArrayList.size());

                }


                break;
            case Constant.RV_SECTION_ROW:

                int size = modelArrayList.get(i).getListSection().size();


                holder.tv_class.setText(modelArrayList.get(i).getClassName());


                StringBuffer sb = new StringBuffer();

                for (int j = 0; j < size; j++) {

                    sb.append(modelArrayList.get(i).getListSection().get(j) + " ");

                    Log.d("MY_ASCII_adapter", modelArrayList.get(i).getListSection().get(j));

                }

                holder.tv_section.setText(sb.toString());

                btnSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        JSONObject classObject = new JSONObject();

                        for (int k = 0; k < modelArrayList.size(); k++) {

                            String classValue = modelArrayList.get(k).getClassName();

                            JSONArray array = new JSONArray();
                            JSONObject jsonObject = new JSONObject();

                            for (int j = 0; j < modelArrayList.get(k).getListSection().size(); j++) {

                                String strSec = modelArrayList.get(k).getListSection().get(j);

                                array.put(strSec);

                            }

                            try {
                                jsonObject.put("sections", array);
                                jsonObject.put("order", k + 1);


                                classObject.put(classValue, jsonObject);


                            } catch (JSONException je) {

                            }
                        }


                        Log.d("OBJECT_ARR", Constant.SCHOOL_ID);
                        Log.d("OBJECT_ARR", Constant.DIVISION_NAME);
                        Log.d("OBJECT_ARR", Constant.EMPLOYEE_BY_ID);
                        Log.d("OBJECT_ARR", "" + classObject);

                        mApiService.addClassSections(Constant.SCHOOL_ID, Constant.DIVISION_NAME, classObject,
                                Constant.EMPLOYEE_BY_ID).enqueue(new Callback<Object>() {
                            @Override
                            public void onResponse(Call<Object> call, Response<Object> response) {
                                Log.i("OBJECT_ARR**RESPONSE", "" + response.body());
                                Toast.makeText(context, "Class and Section successfully Save",
                                        Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailure(Call<Object> call, Throwable t) {

                            }
                        });

                    }
                });

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String className = holder.tv_class.getText().toString();

                        JSONArray array = new JSONArray();
                        JSONObject jsonClass = new JSONObject();

                        for (int j = 0; j < modelArrayList.get(i).getListSection().size(); j++) {

                            String strSec = modelArrayList.get(i).getListSection().get(j);
                            array.put(strSec);
                        }


                        try {
                            String secData = (String) array.get(array.length() - 1);
                            System.out.println("jsonObject**vdata" + secData);
                            JSONArray array1 = new JSONArray();
                            array1.put(secData);
                            jsonClass.put(className, array1);
                        } catch (JSONException je) {

                        }


                        openDialog(jsonClass);
                    }
                });

                break;

        }


    }

    private void openDialog(final JSONObject jsonClass) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
        builder1.setMessage("Do you want to delete Sections");

        builder1.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(final DialogInterface dialog, int id) {

                mApiService.deleteClassSections(Constant.SCHOOL_ID, jsonClass,
                        Constant.EMPLOYEE_BY_ID).enqueue(new Callback<Object>() {
                    @Override
                    public void onResponse(Call<Object> call, Response<Object> response) {
                        Log.i("MAKER_RES_DELETE", "" + response.body());
                        Toast.makeText(context, "Section have deleted successfully",
                                Toast.LENGTH_SHORT).show();
                        dialog.cancel();

                                /*final AddDivisionActivity addDivisionActivity = (AddDivisionActivity) context;
                                addDivisionActivity.loadFragment(Constant.ADD_SECTION_FRAGMENT, null);

*/
                        Intent intent = new Intent(context, AddDivisionActivity.class);
                        intent.putExtra("BARRIERS_TYPE", "CLASS_SECTIONS_BARRIER");
                        context.startActivity(intent);
                    }

                    @Override
                    public void onFailure(Call<Object> call, Throwable t) {
                        // Log.i("MAKER_RES_DELETE", "" + t.toString());
                        dialog.cancel();
                    }
                });

            }
        });

        builder1.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();

    }

    @Override
    public int getItemCount() {
        switch (recyclerTag) {
            case Constant.RV_CLASSES_CARD:
                return arrayList.size();
            // return divisionClassModels.size();

            case Constant.RV_SECTION_ROW:
                return modelArrayList.size();

        }
        return arrayList.size();

    }
}