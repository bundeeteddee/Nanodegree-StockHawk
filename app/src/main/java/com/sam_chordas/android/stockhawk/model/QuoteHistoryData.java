package com.sam_chordas.android.stockhawk.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by bundee on 8/22/16.
 */
public class QuoteHistoryData implements Parcelable{
    //Tag
    protected final static String TAG = QuoteHistoryData.class.getCanonicalName();

    //JSON Keys
    private static final String KEY_SYMBOL = "Symbol";
    private static final String KEY_DATE = "Date";
    private static final String KEY_OPEN = "Open";
    private static final String KEY_HIGH = "High";
    private static final String KEY_LOW = "Low";
    private static final String KEY_CLOSE = "Close";
    private static final String KEY_VOLUMNE = "Volume";
    private static final String KEY_ADJ_CLOSE = "Adj_Close";

    //Variables
    private String mSymbol;
    private long mDate;
    private float mOpen;
    private float mHigh;
    private float mLow;
    private float mClose;
    private int mVolumne;

    /**
     * Constructor from json
     * @param jsonString
     */
    public QuoteHistoryData(String jsonString){
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            mSymbol = jsonObject.getString(KEY_SYMBOL);
            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Date date = formatter.parse(jsonObject.getString(KEY_DATE));
            mDate = date.getTime();
            mOpen = Float.parseFloat(jsonObject.getString(KEY_OPEN));
            mHigh = Float.parseFloat(jsonObject.getString(KEY_HIGH));
            mLow = Float.parseFloat(jsonObject.getString(KEY_LOW));
            mClose = Float.parseFloat(jsonObject.getString(KEY_CLOSE));
            mVolumne = jsonObject.getInt(KEY_VOLUMNE);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    /**
     * Constructor for Parcelable
     * @param in
     */
    protected QuoteHistoryData(Parcel in) {
        mSymbol = in.readString();
        mDate = in.readLong();
        mOpen = in.readFloat();
        mHigh = in.readFloat();
        mLow = in.readFloat();
        mClose = in.readFloat();
        mVolumne = in.readInt();
    }

    public static final Creator<QuoteHistoryData> CREATOR = new Creator<QuoteHistoryData>() {
        @Override
        public QuoteHistoryData createFromParcel(Parcel in) {
            return new QuoteHistoryData(in);
        }

        @Override
        public QuoteHistoryData[] newArray(int size) {
            return new QuoteHistoryData[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mSymbol);
        dest.writeLong(mDate);
        dest.writeFloat(mOpen);
        dest.writeFloat(mHigh);
        dest.writeFloat(mLow);
        dest.writeFloat(mClose);
        dest.writeInt(mVolumne);
    }

    public float getClose() {
        return mClose;
    }

    public long getDate() {
        return mDate;
    }

    public float getHigh() {
        return mHigh;
    }

    public float getLow() {
        return mLow;
    }

    public float getOpen() {
        return mOpen;
    }

    public String getSymbol() {
        return mSymbol;
    }

    public int getVolumne() {
        return mVolumne;
    }

}
