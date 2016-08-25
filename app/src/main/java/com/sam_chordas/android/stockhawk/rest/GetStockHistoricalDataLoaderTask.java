package com.sam_chordas.android.stockhawk.rest;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;
import com.sam_chordas.android.stockhawk.model.Quote;
import com.sam_chordas.android.stockhawk.model.QuoteHistoryData;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

/**
 * Created by bundee on 8/22/16.
 */
public class GetStockHistoricalDataLoaderTask extends AsyncTaskLoader<AsyncTaskLoaderResult<List<QuoteHistoryData>>> {
    //Tag
    private static final String TAG = GetStockHistoricalDataLoaderTask.class.getCanonicalName();

    //Static
    public static final int DEFAULT_DAYS_OF_HISTORICAL_DATA = 10;

    //Variables
    private Quote mQuote;
    private int mDaysOfData;

    /**
     * Constructor
     * @param context
     */
    public GetStockHistoricalDataLoaderTask(Context context,
                                            Quote quote) {
        super(context);

        mQuote = quote;
        mDaysOfData = DEFAULT_DAYS_OF_HISTORICAL_DATA;
    }

    /**
     * Constructor 2
     * @param context
     * @param quote
     * @param daysOfData
     */
    public GetStockHistoricalDataLoaderTask(Context context,
                                            Quote quote,
                                            int daysOfData) {
        super(context);

        mQuote = quote;
        mDaysOfData = daysOfData;
    }

    @Override
    public AsyncTaskLoaderResult<List<QuoteHistoryData>> loadInBackground() {
        long todayInMili = (new Date()).getTime();
        long daysAgoInMili = todayInMili - (1000 * 60 * 60 * 24 * mDaysOfData);
        try {
            String url = Utils.GenerateQueryForStockHistoricalData(mQuote.getSymbol(), daysAgoInMili, todayInMili);

            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(url)
                    .get()
                    .build();

            //Block http call
            Response response = client.newCall(request).execute();

            if(response.isSuccessful()){
                String jsonResult = response.body().string();

                return new AsyncTaskLoaderResult<>(null, Utils.quoteJsonToQuoteHistoryData(jsonResult));
            }else{
                return new AsyncTaskLoaderResult<>(new Exception(response.message()), null);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return new AsyncTaskLoaderResult<>(e, null);
        } catch (IOException e) {
            e.printStackTrace();
            return new AsyncTaskLoaderResult<>(e, null);
        }
    }

    public int getDaysOfData() {
        return mDaysOfData;
    }
}
