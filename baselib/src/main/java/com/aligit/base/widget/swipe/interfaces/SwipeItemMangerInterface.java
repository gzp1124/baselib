package com.aligit.base.widget.swipe.interfaces;

import com.aligit.base.widget.swipe.util.Attributes;

import java.util.List;

/**
 * <pre>
 * author : gzp1124
 * time   : 2020/6/19
 * desc   :
 * </pre>
 */
public interface SwipeItemMangerInterface {

    void openItem(int position);

    void closeItem(int position);

    void closeAllExcept(SwipeLayout layout);

    void closeAllItems();

    List<Integer> getOpenItems();

    List<SwipeLayout> getOpenLayouts();

    void removeShownLayouts(SwipeLayout layout);

    boolean isOpen(int position);

    Attributes.Mode getMode();

    void setMode(Attributes.Mode mode);
}
