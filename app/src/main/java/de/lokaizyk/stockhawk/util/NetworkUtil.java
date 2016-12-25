package de.lokaizyk.stockhawk.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import de.lokaizyk.stockhawk.R;

/**
 * Created by lars on 23.12.16.
 */

public class NetworkUtil {

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

}
