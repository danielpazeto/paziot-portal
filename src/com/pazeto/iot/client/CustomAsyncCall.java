package com.pazeto.iot.client;

import java.util.logging.Logger;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;

public abstract class CustomAsyncCall<T> implements AsyncCallback<T> {

	private static final Logger LOG = Logger.getLogger(CustomAsyncCall.class
			.getName());

	/** Call the service method using cb as the callback. */
	protected abstract void callService(AsyncCallback<T> cb);

	private PopupPanel popup;

	public CustomAsyncCall() {
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

	public void go(int retryCount) {
		showLoadingMessage();
		execute(retryCount);
	}

	public void go() {
		go(0);
	}

	public void goWithoutStatusDialog(int retryCount) {
		// showLoadingMessage();
		execute(retryCount);
	}

	private void execute(final int retriesLeft) {
		callService(new AsyncCallback<T>() {
			public void onFailure(Throwable t) {
				if (retriesLeft <= 0) {
					hideLoadingMessage();
					CustomAsyncCall.this.onFailure(t);
				} else {
					execute(retriesLeft - 1);
				}
			}

			public void onSuccess(T result) {
				hideLoadingMessage();
				CustomAsyncCall.this.onSuccess(result);
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
