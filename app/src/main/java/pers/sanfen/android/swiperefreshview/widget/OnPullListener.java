package pers.sanfen.android.swiperefreshview.widget;

import android.view.View;

public interface OnPullListener {
    void onPulling(View headview);
    void onCanRefreshing(View headview);
    void onRefreshing(View headview);
}
