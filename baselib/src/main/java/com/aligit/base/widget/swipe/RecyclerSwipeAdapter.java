package com.aligit.base.widget.swipe;

import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.aligit.base.widget.swipe.implments.SwipeItemMangerImpl;
import com.aligit.base.widget.swipe.interfaces.SwipeAdapterInterface;
import com.aligit.base.widget.swipe.interfaces.SwipeItemMangerInterface;
import com.aligit.base.widget.swipe.util.Attributes;
import com.aligit.base.widget.swipe.implments.SwipeItemMangerImpl;
import com.aligit.base.widget.swipe.interfaces.SwipeAdapterInterface;
import com.aligit.base.widget.swipe.interfaces.SwipeItemMangerInterface;
import com.aligit.base.widget.swipe.interfaces.SwipeLayout;
import com.aligit.base.widget.swipe.util.Attributes;

import java.util.List;

/**
 * <pre>
 * author : gzp1124
 * time   : 2020/6/19
 * desc   :
 * </pre>
 */
public abstract class RecyclerSwipeAdapter<VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH>
        implements SwipeItemMangerInterface, SwipeAdapterInterface {

    public SwipeItemMangerImpl mItemManger = new SwipeItemMangerImpl(this);

    @Override
    public abstract VH onCreateViewHolder(ViewGroup parent, int viewType);

    @Override
    public abstract void onBindViewHolder(VH viewHolder, final int position);

    @Override
    public void notifyDatasetChanged() {
        super.notifyDataSetChanged();
    }

    @Override
    public void openItem(int position) {
        mItemManger.openItem(position);
    }

    @Override
    public void closeItem(int position) {
        mItemManger.closeItem(position);
    }

    @Override
    public void closeAllExcept(SwipeLayout layout) {
        mItemManger.closeAllExcept(layout);
    }

    @Override
    public void closeAllItems() {
        mItemManger.closeAllItems();
    }

    @Override
    public List<Integer> getOpenItems() {
        return mItemManger.getOpenItems();
    }

    @Override
    public List<SwipeLayout> getOpenLayouts() {
        return mItemManger.getOpenLayouts();
    }

    @Override
    public void removeShownLayouts(SwipeLayout layout) {
        mItemManger.removeShownLayouts(layout);
    }

    @Override
    public boolean isOpen(int position) {
        return mItemManger.isOpen(position);
    }

    @Override
    public Attributes.Mode getMode() {
        return mItemManger.getMode();
    }

    @Override
    public void setMode(Attributes.Mode mode) {
        mItemManger.setMode(mode);
    }
}
