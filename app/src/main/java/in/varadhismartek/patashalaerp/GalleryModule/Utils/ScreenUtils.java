package in.varadhismartek.patashalaerp.GalleryModule.Utils;

import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;

public class ScreenUtils {

    static int convertDpToPixel(Context context, int dp) {
        try {
            Resources r = context.getResources();
            return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return -1;
    }
}
