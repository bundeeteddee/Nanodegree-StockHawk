package com.sam_chordas.android.stockhawk.util;

import android.graphics.Color;
import android.util.TypedValue;

import java.util.List;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.AxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.application.EApplication;

/**
 * Created by bundee on 8/24/16.
 */
public final class ChartUtil {

    /**
     * Generate a line data set
     * @param entries
     * @return
     */
    public static final LineDataSet GenerateLineDataSet(List<Entry> entries){
        //Reusable typed value
        //Reference: http://stackoverflow.com/a/8780360/377844
        TypedValue typedValue = new TypedValue();

        LineDataSet set1 = new LineDataSet(entries, "");
        EApplication.getInstance().getResources().getValue(R.dimen.chart_linedataset_linewidth, typedValue, true);
        set1.setLineWidth(typedValue.getFloat());
        EApplication.getInstance().getResources().getValue(R.dimen.chart_linedataset_circle_radius, typedValue, true);
        set1.setCircleRadius(typedValue.getFloat());
        EApplication.getInstance().getResources().getValue(R.dimen.chart_linedataset_circle_hole_radius, typedValue, true);
        set1.setCircleHoleRadius(typedValue.getFloat());
        set1.setColor(R.color.primary_light);
        set1.setCircleColor(R.color.primary_dark);
        set1.setHighLightColor(R.color.accent);
        set1.setDrawValues(true);
        EApplication.getInstance().getResources().getValue(R.dimen.chart_linedataset_value_text_size, typedValue, true);
        set1.setValueTextSize(typedValue.getFloat());
        return set1;
    }

    /**
     * Setup a given chart, style it etc
     * @param chart
     * @param data
     * @param daysOfData
     * @param valueFormatter
     */
    public static final void SetupChart(LineChart chart,
                                        LineData data,
                                        int daysOfData,
                                        AxisValueFormatter valueFormatter){
        ((LineDataSet) data.getDataSetByIndex(0)).setCircleColorHole(R.color.primary_dark);

        // Description text
        chart.setDescription(String.format(EApplication.getInstance().getString(R.string.charting_closing_price_title), daysOfData));
        chart.setNoDataTextDescription(EApplication.getInstance().getString(R.string.charting_closing_price_no_data));

        // Disable grid background
        chart.setDrawGridBackground(false);

        // enable touch gestures
        chart.setTouchEnabled(true);

        // enable scaling and dragging
        chart.setDragEnabled(false);
        chart.setScaleEnabled(false);

        // if disabled, scaling can be done on x- and y-axis separately
        chart.setPinchZoom(false);
        chart.setBackgroundColor(EApplication.getInstance().getResources().getColor(android.R.color.transparent));

        // add data
        chart.setData(data);

        // get the legend (only possible after setting data)
        Legend l = chart.getLegend();
        l.setEnabled(true);
        l.setPosition(Legend.LegendPosition.BELOW_CHART_CENTER);

        chart.getAxisLeft().setEnabled(true);
        chart.getAxisLeft().setSpaceTop(EApplication.getInstance().getResources().getDimension(R.dimen.chart_linedataset_yaxis_spacing_top));
        chart.getAxisLeft().setSpaceBottom(EApplication.getInstance().getResources().getDimension(R.dimen.chart_linedataset_yaxis_spacing_bottom));
        chart.getAxisRight().setEnabled(false);

        //YAxis setup
        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setTextColor(ColorTemplate.getHoloBlue());
        leftAxis.setTextColor(Color.BLACK);

        //XAxis setup
        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        TypedValue typedValue = new TypedValue();
        EApplication.getInstance().getResources().getValue(R.dimen.chart_linedataset_xaxis_text_size, typedValue, true);
        xAxis.setTextSize(typedValue.getFloat());
        xAxis.setTextColor(Color.BLACK);
        xAxis.setDrawAxisLine(false);
        xAxis.setDrawGridLines(false);
        xAxis.setCenterAxisLabels(false);
        xAxis.setAxisMinValue(0f);
        xAxis.setGranularity(1f);
        xAxis.setValueFormatter(valueFormatter);
    }


}
