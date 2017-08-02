package org.baseballbaedal.baseballbaedal.MainFragment.Delivery.Market;

import android.view.View;

public interface ScrollTabHolder {

	void adjustScroll(int scrollHeight, View view);

	void onScroll(View view, int firstVisibleItem, int visibleItemCount, int totalItemCount, int pagePosition);
}
