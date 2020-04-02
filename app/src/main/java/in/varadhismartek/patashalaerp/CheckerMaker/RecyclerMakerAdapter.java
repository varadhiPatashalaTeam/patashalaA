package in.varadhismartek.patashalaerp.CheckerMaker;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;

import in.varadhismartek.Utils.Constant;
import in.varadhismartek.patashalaerp.AddDepartment.Data;
import in.varadhismartek.patashalaerp.DivisionModule.AddDivisionActivity;
import in.varadhismartek.patashalaerp.R;
import in.varadhismartek.patashalaerp.Retrofit.APIService;
import in.varadhismartek.patashalaerp.Retrofit.ApiUtils;
import in.varadhismartek.patashalaerp.SelectModules.SelectModuleLandingActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RecyclerMakerAdapter extends RecyclerView.Adapter<RecyclerViewHolder> {
    Context context;
    ArrayList<Data> dataArrayList;
    String rvType;
    Data data;
    ImageView imageViewSave;
    APIService mApiService;
    String moduleName;

    public RecyclerMakerAdapter(Context context, ArrayList<Data> maker_choice, String rvType) {
        this.context = context;
        this.dataArrayList = maker_choice;
        this.rvType = rvType;
        mApiService = ApiUtils.getAPIService();
        notifyDataSetChanged();
    }

    public RecyclerMakerAdapter(Context context, ArrayList<Data> module_choice,
                                String rvType, ImageView imageViewSave) {
        this.context = context;
        this.dataArrayList = module_choice;
        this.rvType = rvType;
        this.imageViewSave = imageViewSave;
        mApiService = ApiUtils.getAPIService();
        notifyDataSetChanged();

    }


    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        switch (rvType) {
            case Constant.RV_MAKER_TYPE:
                return new RecyclerViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.chip_layout, parent, false));

            case Constant.RV_CHECKER_TYPE:
                return new RecyclerViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.chip_layout, parent, false));
            case Constant.SELECTED_FRAG:
                return new RecyclerViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.card_module_checker_maker, parent, false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerViewHolder holder, final int position) {
        Log.d("arraySize1", "" + dataArrayList.size());


        switch (rvType) {
            case Constant.RV_MAKER_TYPE:
                // holder.checkBox.setText(maker_choice.get(position).getMake());

                final Data data1 = dataArrayList.get(position);
                holder.chip_txt.setText(data1.getChecker_name());
                holder.close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dataArrayList.remove(position);
                        notifyDataSetChanged();
                    }
                });
                break;
            case Constant.RV_CHECKER_TYPE:

                final Data data = dataArrayList.get(position);
                holder.chip_txt.setText(data.getChecker_name());
                holder.close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dataArrayList.remove(position);
                        notifyDataSetChanged();
                    }
                });

                break;

            case Constant.SELECTED_FRAG:
                final Data data2 = dataArrayList.get(position);
                holder.module.setText(data2.getSpin());


                holder.maker.setText(data2.getMake());
                holder.checker.setText(data2.getCheck());

                imageViewSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (dataArrayList.size() > 0) {
                           /* final AddDivisionActivity addDivisionActivity = (AddDivisionActivity) context;
                            addDivisionActivity.loadFragment(Constant.ADD_DIVISION_FRAGMENT, null);*/

                            Intent intent= new Intent(context,AddDivisionActivity.class);
                            intent.putExtra("BARRIERS_TYPE", "DIVISION_BARRIER");
                            context.startActivity(intent);

                        } else {
                            Toast.makeText(context, "Please Add Maker Checker", Toast.LENGTH_SHORT).show();

                        }

                    }
                });
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                         moduleName = holder.module.getText().toString();

                        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                        builder1.setMessage("Do you want to delete maker & checker");
                        builder1.setCancelable(false);

                        builder1.setPositiveButton(
                                "Yes",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(final DialogInterface dialog, int id) {

                                        mApiService.deleteMakerChecker(Constant.SCHOOL_ID, moduleName,
                                                Constant.EMPLOYEE_BY_ID).enqueue(new Callback<Object>() {
                                            @Override
                                            public void onResponse(Call<Object> call, Response<Object> response) {
                                                Log.i("MAKER_RES_DELETE", "" + response.body());
                                                Toast.makeText(context, "Selected Module deleted successfully",
                                                        Toast.LENGTH_SHORT).show();
                                                dialog.cancel();

                                                Intent intent = new Intent(context, AddDivisionActivity.class);
                                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                intent.putExtra("BARRIERS_TYPE" ,"MAKER_CHECKER_BARRIER");
                                                context.startActivity(intent);

                                                /*final AddDivisionActivity addDivisionActivity = (AddDivisionActivity) context;
                                                addDivisionActivity.loadFragment(Constant.MAKER_CHECKER_FRAGMENT, null);
*/
                                            }

                                            @Override
                                            public void onFailure(Call<Object> call, Throwable t) {
                                                Log.i("MAKER_RES_DELETE", "" + t.toString());
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
                });
                break;

        }
    }

    @Override
    public int getItemCount() {
        return dataArrayList.size();
    }

}
