package com.aligit.base.widget.swipe.interfaces;

/**
 * <pre>
 * author : gzp1124
 * time   : 2020/6/19
 * desc   :
 * </pre>
 */
public interface SwipeAdapterInterface {

    int getSwipeLayoutResourceId(int position);

    void notifyDatasetChanged();

}
