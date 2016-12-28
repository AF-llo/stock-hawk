package de.lokaizyk.stockhawk.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import java.util.Locale;

import de.lokaizyk.stockhawk.R;

/**
 * Created by lars on 23.12.16.
 */

public class DeviceUtil {

    public static boolean isConnected(Context context) {
        if (context == null) {
            return false;
        }
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    public static void noNetworkToast(Context context) {
        Toast.makeText(context, context.getString(R.string.no_network), Toast.LENGTH_SHORT).show();
    }

    public static boolean isRTL(Locale locale) {
        final int directionality = Character.getDirectionality(locale.getDisplayName().charAt(0));
        return directionality == Character.DIRECTIONALITY_RIGHT_TO_LEFT ||
                directionality == Character.DIRECTIONALITY_RIGHT_TO_LEFT_ARABIC;
    }

    public static boolean isTablet(Context context) {
        if (context == null) {
            return false;
        }
        return context.getResources().getBoolean(R.bool.is_tablet);
    }

    public static boolean isLandscape(Context context) {
        if (context == null) {
            return false;
        }
        return context.getResources().getBoolean(R.bool.is_landscape);
    }

}
