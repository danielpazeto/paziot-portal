package com.pazeto.iot.client;

import java.util.logging.Logger;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;

public abstract class CustomIotPazetoAsyncCall<T> implements AsyncCallback<T> {

	private static final Logger LOG = Logger
			.getLogger(CustomIotPazetoAsyncCall.class.getName());

	/** Call the service method using cb as the callback. */
	protected abstract void callService(AsyncCallback<T> cb);

	private PopupPanel popup;

	public CustomIotPazetoAsyncCall() {
		popup = new PopupPanel(false, true);

		Image img = new Image("res/loading_spinner.gif");
		img.setHeight("50px");
		img.setWidth("50px");

		HorizontalPanel hp = new HorizontalPanel();
		hp.add(img);
		Label lbWaiting = new Label("Carregando");
		lbWaiting.setStyleName("label-loading");
		hp.add(lbWaiting);

		popup.add(hp);
		popup.setGlassEnabled(true);
	}

	public void execute(int retryCount) {
		showLoadingMessage();
		executeTask((long) retryCount);
	}

	public void execute() {
		executeTask(0);
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
					CustomIotPazetoAsyncCall.this.onFailure(t);
				} else {
					executeTask(retriesLeft - 1);
				}
			}

			public void onSuccess(T result) {
				hideLoadingMessage();
				CustomIotPazetoAsyncCall.this.onSuccess(result);
			}
		});
	}

	private void hideLoadingMessage() {
		popup.hide();
	}

	private void showLoadingMessage() {
		popup.center();
	}
}
