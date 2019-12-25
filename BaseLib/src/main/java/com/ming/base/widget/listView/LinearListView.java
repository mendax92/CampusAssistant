package com.ming.base.widget.listView;

import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.support.v4.view.ViewConfigurationCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SoundEffectConstants;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;

import com.ming.base.R;

/**
 * An extension of a linear layout that supports the divider API of Android
 * 4.0+. You can populate this layout with data that comes from a
 * {@link ListAdapter}
 */
public class LinearListView extends IcsLinearLayout {

    public static final String TAG = "LinearListView";

    private static final int[] R_styleable_LinearListView = new int[]{
        /* 0 */android.R.attr.entries,
        /* 1 */R.attr.dividerThickness
    };

    private static final int LinearListView_entries = 0;
    private static final int LinearListView_dividerThickness = 1;

    private View mEmptyView;
    private ListAdapter mAdapter;
    private boolean mAreAllItemsSelectable;
    private OnItemClickListener mOnItemClickListener;
    private DataSetObserver mDataObserver = new DataSetObserver() {

        @Override
        public void onChanged() {
            setupChildren();
        }

        @Override
        public void onInvalidated() {
            setupChildren();
        }

    };

    private float mLastMotionX;
    private float mLastMotionY;

    private boolean isHorizonScrolling = false;

    public LinearListView(Context context) {
        this(context, null);
    }

    public LinearListView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs,
                R_styleable_LinearListView);

        // Use the thickness specified, zero being the default
        final int thickness = a.getDimensionPixelSize(
                LinearListView_dividerThickness, 0);
        if (thickness != 0) {
            setDividerThickness(thickness);
        }

        CharSequence[] entries = a.getTextArray(LinearListView_entries);
        if (entries != null) {
            setAdapter(new ArrayAdapter<CharSequence>(context,
                    android.R.layout.simple_list_item_1, entries));
        }

        a.recycle();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastMotionX = event.getRawX();
                mLastMotionY = event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:

                float moveX = event.getRawX();
                float moveY = event.getRawY();

                float deltaX = Math.abs(moveX - mLastMotionX);
                float deltaY = Math.abs(moveY - mLastMotionY);
                int touchSlop = ViewConfiguration.get(getContext()).getScaledPagingTouchSlop();
                if (deltaX > deltaY && Math.abs(deltaX) > touchSlop) {
                    isHorizonScrolling = true;
                }

                break;
            case MotionEvent.ACTION_UP:
                isHorizonScrolling = false;
                break;
        }
        return isHorizonScrolling || super.onInterceptTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                isHorizonScrolling = false;
                break;
        }

        return isHorizonScrolling || super.onTouchEvent(event);
    }

    /**
     * 判断是否在横向滑动
     *
     * @return true 滑动中，反之 false
     */
    public boolean isScrolling() {
        return isHorizonScrolling;
    }

    @Override
    public void setOrientation(int orientation) {
        if (orientation != getOrientation()) {
            int tmp = mDividerHeight;
            mDividerHeight = mDividerWidth;
            mDividerWidth = tmp;
        }
        super.setOrientation(orientation);
    }

    /**
     * Set the divider thickness size in pixel. That means setting the divider
     * height if the layout has an HORIZONTAL orientation and setting the
     * divider width otherwise.
     *
     * @param thickness The divider thickness in pixel.
     */
    public void setDividerThickness(int thickness) {
        if (getOrientation() == VERTICAL) {
            mDividerHeight = thickness;
        } else {
            mDividerWidth = thickness;
        }
        requestLayout();
    }

    public ListAdapter getAdapter() {
        return mAdapter;
    }

    /**
     * Sets the data behind this LinearListView.
     *
     * @param adapter The ListAdapter which is responsible for maintaining the data
     *                backing this list and for producing a view to represent an
     *                item in that data set.
     * @see #getAdapter()
     */
    public void setAdapter(ListAdapter adapter) {
        if (mAdapter != null) {
            mAdapter.unregisterDataSetObserver(mDataObserver);
        }

        mAdapter = adapter;

        if (mAdapter != null) {
            mAdapter.registerDataSetObserver(mDataObserver);
            mAreAllItemsSelectable = mAdapter.areAllItemsEnabled();
        }

        setupChildren();

    }

    /**
     * Interface definition for a callback to be invoked when an item in this
     * LinearListView has been clicked.
     */
    public interface OnItemClickListener {

        /**
         * Callback method to be invoked when an item in this LinearListView has
         * been clicked.
         * <p>
         * Implementers can call getItemAtPosition(position) if they need to
         * access the data associated with the selected item.
         *
         * @param parent   The LinearListView where the click happened.
         * @param view     The view within the LinearListView that was clicked (this
         *                 will be a view provided by the adapter)
         * @param position The position of the view in the adapter.
         * @param id       The row id of the item that was clicked.
         */
        void onItemClick(LinearListView parent, View view, int position, long id);
    }

    /**
     * Register a callback to be invoked when an item in this LinearListView has
     * been clicked.
     *
     * @param listener The callback that will be invoked.
     */
    public void setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    /**
     * @return The callback to be invoked with an item in this LinearListView has
     * been clicked, or null id no callback has been set.
     */
    public final OnItemClickListener getOnItemClickListener() {
        return mOnItemClickListener;
    }

    /**
     * Call the OnItemClickListener, if it is defined.
     *
     * @param view     The view within the LinearListView that was clicked.
     * @param position The position of the view in the adapter.
     * @param id       The row id of the item that was clicked.
     * @return True if there was an assigned OnItemClickListener that was
     * called, false otherwise is returned.
     */
    public boolean performItemClick(View view, int position, long id) {
        if (mOnItemClickListener != null) {
            playSoundEffect(SoundEffectConstants.CLICK);
            mOnItemClickListener.onItemClick(this, view, position, id);
            return true;
        }

        return false;
    }

    /**
     * Sets the view to show if the adapter is empty
     */
    public void setEmptyView(View emptyView) {
        mEmptyView = emptyView;

        final ListAdapter adapter = getAdapter();
        final boolean empty = ((adapter == null) || adapter.isEmpty());
        updateEmptyStatus(empty);
    }

    /**
     * When the current adapter is empty, the LinearListView can display a special
     * view call the empty view. The empty view is used to provide feedback to
     * the user that no data is available in this LinearListView.
     *
     * @return The view to show if the adapter is empty.
     */
    public View getEmptyView() {
        return mEmptyView;
    }

    /**
     * Update the status of the list based on the empty parameter. If empty is
     * true and we have an empty view, display it. In all the other cases, make
     * sure that the layout is VISIBLE and that the empty view is GONE (if
     * it's not null).
     */
    private void updateEmptyStatus(boolean empty) {
        if (empty) {
            if (mEmptyView != null) {
                mEmptyView.setVisibility(View.VISIBLE);
                setVisibility(View.GONE);
            } else {
                // If the caller just removed our empty view, make sure the list
                // view is visible
                setVisibility(View.VISIBLE);
            }
        } else {
            if (mEmptyView != null)
                mEmptyView.setVisibility(View.GONE);
            setVisibility(View.VISIBLE);
        }
    }

    private void setupChildren() {

        removeAllViews();

        updateEmptyStatus((mAdapter == null) || mAdapter.isEmpty());

        if (mAdapter == null) {
            return;
        }

        for (int i = 0; i < mAdapter.getCount(); i++) {
            View child = mAdapter.getView(i, null, this);
            if (mAreAllItemsSelectable || mAdapter.isEnabled(i)) {
                child.setOnClickListener(new InternalOnClickListener(i));
            }

            addView(child, i);
        }
    }

    /**
     * Internal OnClickListener that this view associate of each of its children
     * so that they can respond to OnItemClick listener's events. Avoid setting
     * an OnClickListener manually. If you need it you can wrap the child in a
     * simple {@link android.widget.FrameLayout}.
     */
    private class InternalOnClickListener implements OnClickListener {

        int mPosition;

        public InternalOnClickListener(int position) {
            mPosition = position;
        }

        @Override
        public void onClick(View v) {
            if ((mOnItemClickListener != null) && (mAdapter != null)) {
                mOnItemClickListener.onItemClick(LinearListView.this, v,
                        mPosition, mAdapter.getItemId(mPosition));
            }
        }
    }
}
