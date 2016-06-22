package com.pazeto.iot.client.services;

import gwt.material.design.client.ui.MaterialLoader;

import com.google.gwt.user.client.rpc.AsyncCallback;

public abstract class CustomAsyncCall<T> implements AsyncCallback<T> {

    /** Call the service method using cb as the callback. */
    protected abstract void callService(AsyncCallback<T> cb);

    public CustomAsyncCall() {
    }

    public void execute(int retryCount) {
        showLoadingMessage();
        executeTask((long) retryCount);
    }

    public void execute() {
        execute(0);
    }

    public void executeWithoutSpinner() {
        executeWithoutSpinner(0);
    }

    public void executeWithoutSpinner(int retryCount) {
        executeTask(retryCount);
    }

    @Override
    public void onFailure(Throwable caught) {
        // TODO Auto-generated method stub

    }

    private void executeTask(final long retriesLeft) {
        callService(new AsyncCallback<T>() {
            public void onFailure(Throwable t) {
                if (retriesLeft <= 0) {
                    hideLoadingMessage();
                    CustomAsyncCall.this.onFailure(t);
                } else {
                    executeTask(retriesLeft - 1);
                }
            }

            public void onSuccess(T result) {
                hideLoadingMessage();
                CustomAsyncCall.this.onSuccess(result);
            }
        });
    }

    private void hideLoadingMessage() {
        MaterialLoader.showLoading(false);
    }

    private void showLoadingMessage() {
        MaterialLoader.showLoading(true);
    }
}
