package com.example.wx.inba.util;

import android.content.Context;
import android.database.DataSetObserver;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

public final class HeightListView extends ListView {

    private DataSetObserver mDataSetObserver; // The custom data set observer.

    public HeightListView(Context context) {
        this(context, null);
    }

    public HeightListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        createObserver();
    }

    public HeightListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        createObserver();
    }

    /**
     * Overrides to set a suitable measure spec.
     *
     * @param widthMeasureSpec  widthMeasureSpec, not changed.
     * @param heightMeasureSpec heightMeasureSpec replaced with a custom one.
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int spec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, spec);
    }

    /**
     * Sets the data behind this ListView.
     * <p>
     * Unregisters the observer of the pre adapter before which will be replaced.
     * After the new adapter is setted, registers a observer.
     *
     * @param adapter Set the new listView adapter
     */
    @Override
    public void setAdapter(ListAdapter adapter) {
        if (adapter == null) throw new NullPointerException();

        if (getAdapter() != null) {
            getAdapter().unregisterDataSetObserver(mDataSetObserver);
        }

        adapter.registerDataSetObserver(mDataSetObserver);
        super.setAdapter(adapter);
    }

    /**
     * Creates a custom data set change observer.
     */
    private void createObserver() {
        if (mDataSetObserver != null) return;
        mDataSetObserver = new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                onItemsChanged();
            }
        };
    }

    /**
     * Calculates the height of all sub items, includes divider height of each item.
     */
    private void onItemsChanged() {
        int count = getAdapter().getCount();
        if (count == 0) return;

        int height = 0;
        for (int i = 0; i < count; i++) {
            View v = getAdapter().getView(i, null, this);
            v.measure(0, 0);
            height += v.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = getLayoutParams();
        params.height = height + getDividerHeight() * (count - 1);
        setLayoutParams(params);
    }

    @Override
    public boolean isClickable() {
        return super.isClickable();
    }

    @Override
    public boolean isEnabled() {
        return super.isEnabled();
    }
}
