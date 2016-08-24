package com.sam_chordas.android.stockhawk.rest;

/**
 * Created by bundee on 8/23/16.
 * Wrapper for async task loader result so we can include errors if need to
 */
public class AsyncTaskLoaderResult<T> {

    private final T result;
    private final Exception error;

    /**
     * Constructor
     * @param error
     * @param result
     */
    public AsyncTaskLoaderResult(Exception error, T result) {
        this.error = error;
        this.result = result;
    }

    public Exception getError() {
        return error;
    }

    public T getResult() {
        return result;
    }
}
