package com.sam_chordas.android.stockhawk.model;

import java.util.Comparator;

/**
 * Created by bundee on 8/24/16.
 * For ascending sort
 */
public class QuoteHistoryDataComparator implements Comparator<QuoteHistoryData> {
    @Override
    public int compare(QuoteHistoryData lhs, QuoteHistoryData rhs) {
        Long lhsDate = lhs.getDate();
        Long rhsDate = rhs.getDate();
        return lhsDate.compareTo(rhsDate);
    }
}
