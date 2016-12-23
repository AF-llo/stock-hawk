package de.lokaizyk.stockhawk.util;

import android.databinding.BindingAdapter;
import android.os.Build;
import android.view.View;

/**
 * Created by lars on 23.12.16.
 */

public class ViewBinding {

    @BindingAdapter("bind:backgroundDrawable")
    public static void setBackgroundDrawable(View view, int drawableId) {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.JELLY_BEAN){
            view.setBackgroundDrawable(view.getContext().getResources().getDrawable(drawableId));
        }else {
            view.setBackground(view.getContext().getResources().getDrawable(drawableId));
        }
    }

}
