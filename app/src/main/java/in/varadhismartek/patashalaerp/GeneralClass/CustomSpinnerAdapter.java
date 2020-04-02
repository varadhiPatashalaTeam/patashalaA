package in.varadhismartek.patashalaerp.GeneralClass;

import android.support.v4.app.FragmentActivity;
import android.support.v4.content.res.ResourcesCompat;
import android.view.Gravity;

import in.varadhismartek.patashalaerp.R;

import android.content.Context;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * Created by varad_000 on 12/20/2017.
 */

public class CustomSpinnerAdapter extends BaseAdapter implements SpinnerAdapter {

    Context mContext;
    ArrayList<String> arrayList;
    String colorTag = "";

    public CustomSpinnerAdapter(Context mContext, ArrayList<String> arrayList) {
        this.mContext=mContext;
        this.arrayList = arrayList;
    }

    public CustomSpinnerAdapter(Context mContext, ArrayList<String> arrayList, String colorTag) {
        this.mContext=mContext;
        this.arrayList = arrayList;
        this.colorTag = colorTag;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return (long)position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView txt = new TextView(mContext);
        try {

            Typeface externalFont= ResourcesCompat.getFont(mContext, R.font.baumans);
            txt.setTypeface(externalFont);
            txt.setGravity(Gravity.CENTER);
            //txt.setGravity(Gravity.NO_GRAVITY);
            txt.setPadding(8, 8, 8, 8);
            txt.setTextSize(14);
            txt.setText(arrayList.get(position));
            //txt.setTypeface(txt.getTypeface(), Typeface.NORMAL);
            //txt.setTextColor(Color.parseColor("#FFFFFF"));
            txt.setTextColor(mContext.getResources().getColor(R.color.hintColor));

            if (colorTag.equals("orange"))
                txt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_drop_down_circle_orange_24dp, 0);
            else if(colorTag.equals("Blue"))
                txt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_drop_down_circle_blue_24dp, 0);
            else
                txt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_drop_down_circle_orange_24dp, 0);


        }catch (Exception e){

        }

        try {
            Field popup = Spinner.class.getDeclaredField("mPopup");
            popup.setAccessible(true);
            // Get private mPopup member variable and try cast to ListPopupWindow
            android.widget.ListPopupWindow popupWindow = (android.widget.ListPopupWindow) popup.get(parent);
            int height = arrayList.size() * 80;
            if (height < 500){
                popupWindow.setHeight(height);
            }else {
                popupWindow.setHeight(500);
            }
            // Set popupWindow height to 500px
        }
        catch (NoClassDefFoundError | ClassCastException | NoSuchFieldException | IllegalAccessException e) {
            // silently fail...
        }
        return  txt;
    }
    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        TextView txt = new TextView(mContext);
        Typeface externalFont= ResourcesCompat.getFont(mContext, R.font.baumans);
        txt.setTypeface(externalFont);
        txt.setPadding(8, 20, 8, 8);
        txt.setTextSize(15);
        txt.setGravity(Gravity.CENTER_VERTICAL);
        txt.setGravity(Gravity.NO_GRAVITY);
        txt.setText(arrayList.get(position));
        // txt.setTextColor(Color.parseColor("#FFFF"));
        //txt.setTypeface(txt.getTypeface(), Typeface.NORMAL);
        txt.setTextColor(mContext.getResources().getColor(R.color.hintColor));
        return  txt;
    }
}
