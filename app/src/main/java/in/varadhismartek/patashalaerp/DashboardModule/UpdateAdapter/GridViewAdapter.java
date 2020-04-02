package in.varadhismartek.patashalaerp.DashboardModule.UpdateAdapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import in.varadhismartek.patashalaerp.DashboardModule.Assessment.AssessmentModuleActivity;
import in.varadhismartek.patashalaerp.DashboardModule.DashboardSettingActivity;
import in.varadhismartek.patashalaerp.DashboardModule.Homework.HomeWorkActivity;
import in.varadhismartek.patashalaerp.DashboardModule.UpdateActivity.UpdateClassActivity;
import in.varadhismartek.patashalaerp.DashboardModule.UpdateActivity.UpdateDepartmentActivity;
import in.varadhismartek.patashalaerp.DashboardModule.UpdateActivity.UpdateDivisionActivity;
import in.varadhismartek.patashalaerp.DashboardModule.UpdateActivity.UpdateMakerCheckerActivity;
import in.varadhismartek.patashalaerp.DashboardModule.UpdateActivity.UpdateRolesActivity;
import in.varadhismartek.patashalaerp.DashboardModule.UpdateActivity.UpdateSectionActivity;
import in.varadhismartek.patashalaerp.DashboardModule.UpdateActivity.UpdateSelelectModuleActivity;
import in.varadhismartek.patashalaerp.DashboardModule.UpdateActivity.UpdateSessionsActivity;
import in.varadhismartek.patashalaerp.DashboardModule.UpdateActivity.UpdateStaffBarriersActivity;
import in.varadhismartek.patashalaerp.DashboardModule.UpdateActivity.UpdateWingsActivity;
import in.varadhismartek.patashalaerp.DashboardModule.extraUtils.ModuleModel;
import in.varadhismartek.patashalaerp.R;


public class GridViewAdapter extends BaseAdapter {
    private List<ModuleModel> dataList;
    Context mContext;

    public GridViewAdapter(Context mContext, List<ModuleModel> datas, int page) {
        this.mContext = mContext;
        dataList = new ArrayList<>();
        int start = page * DashboardSettingActivity.item_grid_num;
        int end = start + DashboardSettingActivity.item_grid_num;
        while ((start < datas.size()) && (start < end)) {
            dataList.add(datas.get(start));
            start++;
        }
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int i) {
        return dataList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View itemView, ViewGroup viewGroup) {
        ViewHolder mHolder;

        if (itemView == null) {

            mHolder = new ViewHolder();
            itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_gridview, viewGroup, false);
            mHolder.iv_img =  itemView.findViewById(R.id.iv_img);
            mHolder.tv_text =  itemView.findViewById(R.id.tv_text);
            mHolder.rlModules =  itemView.findViewById(R.id.rlModules);
            itemView.setTag(mHolder);

        } else {
            mHolder = (ViewHolder) itemView.getTag();
        }
        ModuleModel bean = dataList.get(i);
        Log.i("Beans***",""+bean+bean.getName());

        if (bean != null) {
            mHolder.iv_img.setImageResource(dataList.get(i).getImage());
            mHolder.tv_text.setText(dataList.get(i).getName());

            mHolder.rlModules.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String strBarrierName= dataList.get(i).getName();

                    switch (strBarrierName){


                        case "Module":
                            Intent intent1 = new Intent(mContext, UpdateSelelectModuleActivity.class);
                            mContext.startActivity(intent1);
                            break;

                        case "Wings":
                            Intent intent2 = new Intent(mContext, UpdateWingsActivity.class);
                            mContext.startActivity(intent2);
                            break;
                        case "Department":
                            Intent intent3 = new Intent(mContext, UpdateDepartmentActivity.class);
                            mContext.startActivity(intent3);
                            break;

                        case "Roles":
                            Intent intent4 = new Intent(mContext, UpdateRolesActivity.class);
                            mContext.startActivity(intent4);
                            break;

                        case "Staff Barrier's":
                            Intent intent5 = new Intent(mContext, UpdateStaffBarriersActivity.class);
                            mContext.startActivity(intent5);
                            break;

                        case "Maker & Checker":
                            Intent intent11 = new Intent(mContext, UpdateMakerCheckerActivity.class);
                            mContext.startActivity(intent11);
                            break;

                        case "Division":
                            Intent intent10 = new Intent(mContext, UpdateDivisionActivity.class);
                            mContext.startActivity(intent10);
                            break;

                        case "Class":
                            Intent intent6 = new Intent(mContext, UpdateClassActivity.class);
                            mContext.startActivity(intent6);
                            break;

                        case "Section":
                            Intent intent7 = new Intent(mContext, UpdateSectionActivity.class);
                            mContext.startActivity(intent7);
                            break;

                        case "Session":
                            Intent intent8 = new Intent(mContext, UpdateSessionsActivity.class);
                            mContext.startActivity(intent8);
                            break;

                        case "Homework":
                            Intent intent9 = new Intent(mContext, HomeWorkActivity.class);
                            intent9.putExtra("BARRIERS_TYPE","HomeWork_Barrier");
                            mContext.startActivity(intent9);
                            ((Activity)mContext).finish();
                            break;

                    }


                }
            });
        }
        return itemView;
    }



    private class ViewHolder {
        private ImageView iv_img;
        private TextView tv_text;
        private RelativeLayout rlModules;
    }
}
