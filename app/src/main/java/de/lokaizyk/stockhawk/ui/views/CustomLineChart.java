package de.lokaizyk.stockhawk.ui.views;

import android.content.Context;
import android.util.AttributeSet;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.List;

import de.lokaizyk.stockhawk.R;

/**
 * Created by lars on 27.12.16.
 */

public class CustomLineChart extends LineChart {
    public CustomLineChart(Context context) {
        super(context);
    }

    public CustomLineChart(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomLineChart(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setEntries(List<Entry> entries) {
        if (entries == null || entries.size() == 0) {
            return;
        }
        int color = getContext().getResources().getColor(R.color.colorAccent);
        LineData data = getData();
        if (data == null) {
            data = new LineData();
        } else {
            data.clearValues();
            invalidate();
        }

        setDescription(null);

        getXAxis().setEnabled(false);
        getAxisLeft().setTextColor(color);
        getAxisRight().setTextColor(color);
        getLegend().setTextColor(color);

        LineDataSet dataSet = new LineDataSet(entries, getContext().getString(R.string.stock_chart_label));
        dataSet.setDrawValues(false);
        dataSet.setDrawCircles(false);
        dataSet.setColor(color);
        data.addDataSet(dataSet);
        setData(data);
        invalidate();
    }

    public void setLabel(int labelId) {
        LineData data = getData();
        if (data != null) {
            String[] labels = data.getDataSetLabels();
            if (labels != null && labels.length > 0) {
                labels[0] = getContext().getString(labelId);
            }
        }
    }
}
