package com.sam_chordas.android.stockhawk.rest;

import android.content.ContentProviderOperation;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import com.sam_chordas.android.stockhawk.data.QuoteColumns;
import com.sam_chordas.android.stockhawk.data.QuoteProvider;
import com.sam_chordas.android.stockhawk.model.QuoteHistoryData;

/**
 * Created by sam_chordas on 10/8/15.
 */
public class Utils {
    //Tag
    private static String LOG_TAG = Utils.class.getCanonicalName();

    //Api base url
    public static final String BASE_YAHOO_QUERY = "https://query.yahooapis.com/v1/public/yql?q=";


    public static boolean showPercent = true;

    /**
     * Generate url to get historical data for a specified stock symbol
     * example: https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20yahoo.finance.historicaldata%20where%20symbol%20%3D%20%22FB%22%20AND%20startDate%20%3D%20%222009-09-11%22%20AND%20endDate%20%3D%20%222012-09-11%22&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys
     * example query: select * from yahoo.finance.historicaldata where symbol = "FB" AND startDate = "2009-09-11" AND endDate = "2012-09-11"
     * @param symbol
     * @param startDate
     * @param endDate
     * @return
     */
    public static final String GenerateQueryForStockHistoricalData(String symbol,
                                                                   long startDate,
                                                                   long endDate) throws UnsupportedEncodingException{
        StringBuilder urlStringBuilder = new StringBuilder();

        // Base URL for the Yahoo query
        urlStringBuilder.append(BASE_YAHOO_QUERY);
        urlStringBuilder.append(URLEncoder.encode("select * from yahoo.finance.historicaldata where symbol = ", "UTF-8"));
        urlStringBuilder.append(URLEncoder.encode("\""+ symbol +"\"", "UTF-8"));
        urlStringBuilder.append(URLEncoder.encode(" AND ", "UTF-8"));
        urlStringBuilder.append(URLEncoder.encode(" startDate = \"" + ConvertTimeStampToDateOnlyString(startDate) + "\"", "UTF-8"));
        urlStringBuilder.append(URLEncoder.encode(" AND ", "UTF-8"));
        urlStringBuilder.append(URLEncoder.encode(" endDate = \"" + ConvertTimeStampToDateOnlyString(endDate) + "\"", "UTF-8"));

        // finalize the URL for the API query.
        //TODO: Add this if you need debug: urlStringBuilder.append("&format=json&diagnostics=true&env=store%3A%2F%2Fdatatables." + "org%2Falltableswithkeys&callback=");
        urlStringBuilder.append("&format=json&env=store%3A%2F%2Fdatatables." + "org%2Falltableswithkeys&callback=");

        return urlStringBuilder.toString();
    }

    /**
     * Convert json string to a list of content values
     * @param JSON
     * @return
     */
    public static ArrayList quoteJsonToContentVals(String JSON) {
        ArrayList<ContentProviderOperation> batchOperations = new ArrayList<>();
        JSONObject jsonObject = null;
        JSONArray resultsArray = null;
        try {
            jsonObject = new JSONObject(JSON);
            if (jsonObject != null && jsonObject.length() != 0) {
                jsonObject = jsonObject.getJSONObject("query");
                int count = Integer.parseInt(jsonObject.getString("count"));
                if (count == 1) {
                    jsonObject = jsonObject.getJSONObject("results").getJSONObject("quote");
                    batchOperations.add(buildBatchOperation(jsonObject));
                } else {
                    resultsArray = jsonObject.getJSONObject("results").getJSONArray("quote");

                    if (resultsArray != null && resultsArray.length() != 0) {
                        for (int i = 0; i < resultsArray.length(); i++) {
                            jsonObject = resultsArray.getJSONObject(i);
                            batchOperations.add(buildBatchOperation(jsonObject));
                        }
                    }
                }
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "String to JSON failed: " + e);
        } catch (Exception ex){
            //Catch all execptions. If anything goes wrong with parsing to JSON, we consider it a fail
        }
        return batchOperations;
    }

    /**
     * Convert a json string to a list of quote history data objects
     * @param JSON
     * @return
     */
    public static List<QuoteHistoryData> quoteJsonToQuoteHistoryData(String JSON) {
        ArrayList<QuoteHistoryData> historyDatas = new ArrayList<>();
        JSONObject jsonObject = null;
        JSONArray resultsArray = null;
        try {
            jsonObject = new JSONObject(JSON);
            if (jsonObject != null && jsonObject.length() != 0) {
                jsonObject = jsonObject.getJSONObject("query");
                int count = Integer.parseInt(jsonObject.getString("count"));
                if (count == 1) {
                    jsonObject = jsonObject.getJSONObject("results").getJSONObject("quote");
                    historyDatas.add(new QuoteHistoryData(jsonObject.toString()));
                } else {
                    resultsArray = jsonObject.getJSONObject("results").getJSONArray("quote");
                    if (resultsArray != null && resultsArray.length() != 0) {
                        for (int i = 0; i < resultsArray.length(); i++) {
                            jsonObject = resultsArray.getJSONObject(i);
                            historyDatas.add(new QuoteHistoryData(jsonObject.toString()));
                        }
                    }
                }
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "String to JSON failed: " + e);
        } catch (Exception ex){
            //Catch all execptions. If anything goes wrong with parsing to JSON, we consider it a fail
        }
        return historyDatas;
    }

    /**
     * Convert time stamp(long) to string format > yyyy-MM-dd
     * @param timeStamp
     * @return
     */
    public static final String ConvertTimeStampToDateOnlyString(long timeStamp){
        //Note: Need to use English locale as we need the date format to be exactly specified in API
        String dateString = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(new Date(timeStamp));
        return dateString;
    }

    public static String truncateBidPrice(String bidPrice) {
        bidPrice = String.format("%.2f", Float.parseFloat(bidPrice));
        return bidPrice;
    }

    public static String truncateChange(String change, boolean isPercentChange) {
        String weight = change.substring(0, 1);
        String ampersand = "";
        if (isPercentChange) {
            ampersand = change.substring(change.length() - 1, change.length());
            change = change.substring(0, change.length() - 1);
        }
        change = change.substring(1, change.length());
        double round = (double) Math.round(Double.parseDouble(change) * 100) / 100;
        change = String.format("%.2f", round);
        StringBuffer changeBuffer = new StringBuffer(change);
        changeBuffer.insert(0, weight);
        changeBuffer.append(ampersand);
        change = changeBuffer.toString();
        return change;
    }

    public static ContentProviderOperation buildBatchOperation(JSONObject jsonObject) {
        ContentProviderOperation.Builder builder = ContentProviderOperation.newInsert(
                QuoteProvider.Quotes.CONTENT_URI);
        try {
            String change = jsonObject.getString("Change");
            builder.withValue(QuoteColumns.SYMBOL, jsonObject.getString("symbol"));
            builder.withValue(QuoteColumns.BIDPRICE, truncateBidPrice(jsonObject.getString("Bid")));
            builder.withValue(QuoteColumns.PERCENT_CHANGE, truncateChange(
                    jsonObject.getString("ChangeinPercent"), true));
            builder.withValue(QuoteColumns.CHANGE, truncateChange(change, false));
            builder.withValue(QuoteColumns.ISCURRENT, 1);
            if (change.charAt(0) == '-') {
                builder.withValue(QuoteColumns.ISUP, 0);
            } else {
                builder.withValue(QuoteColumns.ISUP, 1);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return builder.build();
    }
}
