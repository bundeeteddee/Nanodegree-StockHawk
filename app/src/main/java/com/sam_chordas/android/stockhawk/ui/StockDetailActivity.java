package com.sam_chordas.android.stockhawk.ui;

import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.model.Quote;
import com.sam_chordas.android.stockhawk.model.QuoteHistoryData;
import com.sam_chordas.android.stockhawk.model.QuoteHistoryDataComparator;
import com.sam_chordas.android.stockhawk.rest.AsyncTaskLoaderResult;
import com.sam_chordas.android.stockhawk.rest.GetStockHistoricalDataLoaderTask;
import com.sam_chordas.android.stockhawk.util.ChartUtil;
import com.sam_chordas.android.stockhawk.util.IntentExtras;
import com.sam_chordas.android.stockhawk.util.NetworkUtil;

public class StockDetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<AsyncTaskLoaderResult<List<QuoteHistoryData>>>{
    //Tag
    protected static final String TAG = StockDetailActivity.class.getCanonicalName();

    //Bind
    @BindView(R.id.stock_details_container) CardView mCardView;
    @BindView(R.id.stock_symbol) TextView mSymbolText;
    @BindView(R.id.stock_bid_price) TextView mBidPriceText;
    @BindView(R.id.stock_bid_price_change) TextView mChangeText;
    @BindView(R.id.general_message) TextView mGeneralMessage;
    @BindView(R.id.stock_movement) ImageView mMovementIcon;
    @BindView(R.id.chart) LineChart mLineChart;
    @BindView(R.id.load_progress) ProgressBar mProgress;

    //Variables
    private Quote mQuote;
    private Unbinder mUnbinder;
    private Calendar mRefDay = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_detail);

        //Bind ui
        mUnbinder = ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Get data
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            mQuote = extras.getParcelable(IntentExtras.QUOTE);
        }else if(savedInstanceState != null){
            //From savedInstance
            mQuote = savedInstanceState.getParcelable(IntentExtras.QUOTE);
        }

        //Bind
        mSymbolText.setText(mQuote.getSymbol());
        mBidPriceText.setText(mQuote.getBidPrice());
        mChangeText.setText(mQuote.getChange() + " (" + mQuote.getPercentChange() +")");
        if(mQuote.isUp()){
            mMovementIcon.setImageResource(R.drawable.ic_price_up);
        }else{
            mMovementIcon.setImageResource(R.drawable.ic_price_down);
        }
        mLineChart.setNoDataText(null);

        //Set content description for cardview - stock details
        mCardView.setContentDescription(String.format(getString(R.string.cd_stock_details),
                mQuote.getSymbol(),
                mQuote.getBidPrice(),
                (mQuote.isUp() ? getString(R.string.price_up) : getString(R.string.price_down)),
                mQuote.getChange()));

        //Start loader
        getSupportLoaderManager().restartLoader(1, null, this).forceLoad();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(IntentExtras.QUOTE, mQuote);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mUnbinder.unbind();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<AsyncTaskLoaderResult<List<QuoteHistoryData>>> onCreateLoader(int id, Bundle args) {
        mProgress.setVisibility(View.VISIBLE);
        return new GetStockHistoricalDataLoaderTask(this, mQuote);
    }

    @Override
    public void onLoadFinished(Loader<AsyncTaskLoaderResult<List<QuoteHistoryData>>> loader, AsyncTaskLoaderResult<List<QuoteHistoryData>> data) {
        mProgress.setVisibility(View.GONE);

        if(data != null && data.getResult() != null){
            mGeneralMessage.setVisibility(View.GONE);
            LineData lineData = getData(data.getResult());

            //Setup chart
            ChartUtil.SetupChart(mLineChart,
                    lineData,
                    ((GetStockHistoricalDataLoaderTask) loader).getDaysOfData(),
                    new com.github.mikephil.charting.formatter.AxisValueFormatter() {
                private SimpleDateFormat mFormat = new SimpleDateFormat("MM/dd");

                @Override
                public String getFormattedValue(float value, AxisBase axis) {
                    //Clone ref calendar so we can manipulate it
                    Calendar refDay = (Calendar) mRefDay.clone();

                    //Add extra days
                    refDay.add(Calendar.DATE, (int)value);

                    //Return formatted X-value
                    return mFormat.format(new Date(refDay.getTimeInMillis()));
                }

                @Override
                public int getDecimalDigits() {
                    return 0;
                }
            });
        }else if(data.getError() != null){
            mGeneralMessage.setVisibility(View.VISIBLE);
            if(!NetworkUtil.IsConnected()) {
                mGeneralMessage.setText(R.string.network_toast);
            }else{
                mGeneralMessage.setText(data.getError().getMessage());
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<AsyncTaskLoaderResult<List<QuoteHistoryData>>> loader) {
        mProgress.setVisibility(View.GONE);
    }

    /**
     * Given an array of history data, construct a LineData for our charting
     * @param datas
     * @return
     */
    private LineData getData(List<QuoteHistoryData> datas) {
        ArrayList<Entry> yVals = new ArrayList<>();

        //Sort quote history data. ascending order based on date
        Collections.sort(datas, new QuoteHistoryDataComparator());

        //A reference to starting calendar day
        Calendar historyReference = null;
        for(QuoteHistoryData history: datas){
            //Date of history data
            Date historyDate = new Date(history.getDate());
            if(historyReference == null){
                historyReference = Calendar.getInstance();

                //Set this history date as the reference date / starting date, used as indices
                historyReference.setTime(historyDate);
            }

            long difference = historyDate.getTime() - historyReference.getTimeInMillis();
            long daysDifferent = 0;
            if(difference != 0){
                daysDifferent = difference / (24 * 60 * 60 * 1000);
            }

            yVals.add(new Entry(daysDifferent, history.getClose()));
        }

        // create a data object with the datasets
        LineData data = new LineData(ChartUtil.GenerateLineDataSet(yVals));

        //Set the global reference date
        mRefDay = (Calendar) historyReference.clone();

        return data;
    }
}
