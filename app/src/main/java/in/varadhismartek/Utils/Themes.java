package in.varadhismartek.Utils;

import android.app.Activity;

import in.varadhismartek.patashalaerp.R;

/**
 * Created by varadhi on 2/10/18.
 */

public class Themes {

    public static void onActivityCreateSetTheme(Activity activity, String themeColor)
    {
        switch (themeColor)
        {
            case "THEME_ORANGE":
                activity.setTheme(R.style.AppTheme_orange);
                break;
            case "THEME_GREEN":
                activity.setTheme(R.style.AppTheme_green);
                break;

        }
    }
}
