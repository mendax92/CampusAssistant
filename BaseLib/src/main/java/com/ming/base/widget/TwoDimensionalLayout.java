package com.ming.base.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.util.SparseIntArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.ming.base.R;
import com.ming.base.util.Log;
import com.ming.base.widget.recyclerView.BaseMultiItemQuickAdapter;
import com.ming.base.widget.recyclerView.BaseQuickAdapter;
import com.ming.base.widget.recyclerView.BaseViewHolder;
import com.ming.base.widget.recyclerView.listener.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by ming on 2016/12/5.
 * 二维布局
 */

public class TwoDimensionalLayout extends LinearLayout {

    private static final String TAG = "TwoDimensionalLayout";

    private static final int HEAD_ID = 0x7f0b0000;

    private BaseTableAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private LinearLayout headerLayout;
    private AdapterDataSetObserver mObserver;

    private Drawable headDecorationDrawable = null;

    private OnItemClick onItemClick;

    public TwoDimensionalLayout(Context context) {
        this(context, null);
    }

    public TwoDimensionalLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TwoDimensionalLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TwoDimensionalLayout);
        if (typedArray != null) {
            headDecorationDrawable = typedArray.getDrawable(R.styleable.TwoDimensionalLayout_headerDivider);
            typedArray.recycle();
        }
        setOrientation(VERTICAL);

        mRecyclerView = new RecyclerView(context);
        mRecyclerView.setOverScrollMode(OVER_SCROLL_NEVER);
        mRecyclerView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        headerLayout = new LinearLayout(context);
        headerLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        headerLayout.setOrientation(HORIZONTAL);

        addView(headerLayout);
        addView(mRecyclerView);
    }

    public void addOnScrollListener(RecyclerView.OnScrollListener listener) {
        mRecyclerView.addOnScrollListener(listener);
    }

    public void setAdapter(BaseTableAdapter adapter) {
        this.mAdapter = adapter;
        if (mAdapter != null) {
            mAdapter.headDecorationDrawable = headDecorationDrawable;
            mRecyclerView.addItemDecoration(mAdapter.offsetDecoration);
        }
        mRecyclerView.setAdapter(adapter);
        if (mAdapter != null && mObserver != null) {
            mAdapter.unregisterAdapterDataObserver(mObserver);
        }
        if (mObserver == null) {
            mObserver = new AdapterDataSetObserver();
            mAdapter.registerAdapterDataObserver(mObserver);
        }
    }

    public void setHeaderItemDecorationRes(@DrawableRes int drawableRes) {
        setHeaderItemDecoration(getResources().getDrawable(drawableRes));
    }

    public void setHeaderItemDecoration(final Drawable drawable) {
        this.headDecorationDrawable = drawable;
        if (mAdapter != null) {
            mAdapter.headDecorationDrawable = headDecorationDrawable;
        }
    }

    private void initRawHeader() {
        if (mAdapter == null) {
            return;
        }
        if (mAdapter.getItemCount() == 0) {
            headerLayout.removeAllViews();
            requestLayout();
            return;
        }
        headerLayout.removeAllViews();
        int column = mAdapter.columnSize + 1;
        ViewGroup.LayoutParams params;
        if (headDecorationDrawable != null) {
            headerLayout.setShowDividers(SHOW_DIVIDER_MIDDLE);
            headerLayout.setDividerDrawable(headDecorationDrawable);
        }
        Log.i(TAG, "column:" + column);
        for (int i = 0; i < column; i++) {
            View view = mAdapter.getRowHeadView(i, null, headerLayout);
            if (view == null) {
                view = new View(getContext());
            }
            Log.i(TAG, "init view:" + i);
            params = view.getLayoutParams();
            int width = ViewGroup.LayoutParams.WRAP_CONTENT;
            int height = ViewGroup.LayoutParams.WRAP_CONTENT;
            if (params != null) {
                width = params.width;
                height = params.height;
            }
            if (headDecorationDrawable != null) {
                int drawableHeight = headDecorationDrawable.getIntrinsicHeight();
                LayoutParams viewParams = new LayoutParams(width, height - drawableHeight);
                view.setLayoutParams(viewParams);
                LinearLayout layout = new LinearLayout(getContext());
                layout.setLayoutParams(new LayoutParams(width, height));
                layout.setOrientation(VERTICAL);
                layout.addView(view);
                if (headDecorationDrawable != null) {
                    layout.setShowDividers(SHOW_DIVIDER_END);
                    layout.setDividerDrawable(headDecorationDrawable);
                }
                view = layout;
            }
            LayoutParams linearLayout = new LayoutParams(width, height);
            if (width <= 0) {
                linearLayout.weight = 1;
            }
            view.setLayoutParams(linearLayout);
            view.setTag(HEAD_ID, i);
            view.setOnClickListener(headClickListener);
            Log.i(TAG, "add view:" + i);
            headerLayout.addView(view, i);
        }
        requestLayout();
    }

    private OnClickListener headClickListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            if (onItemClick != null && view.getTag(HEAD_ID) != null && view.getTag(HEAD_ID) instanceof Integer) {
                onItemClick.onRowHeadItemClick(view, (Integer) view.getTag(HEAD_ID));
            }
        }
    };

    private OnItemClickListener itemClickListener = new OnItemClickListener() {
        @Override
        public void SimpleOnItemClick(BaseQuickAdapter adapter, View view, int position) {
            Object obj = mAdapter.getItem(position);
            if (obj != null && obj instanceof BaseDataItem) {
                if (onItemClick == null) {
                    return;
                }
                BaseDataItem item = (BaseDataItem) obj;
                if (item.yPos == -1) {
                    onItemClick.onColumnHeadItemClick(view, item.xPos);
                } else if (item.item != null) {
                    onItemClick.onTableItemClick(view, item);
                }
            }
        }
    };

    private class AdapterDataSetObserver extends RecyclerView.AdapterDataObserver {

        @Override
        public void onChanged() {
            setupView();
        }
    }

    private void setupView() {
        Log.i(TAG, "setupView.");
        initRawHeader();
    }

    /**
     * 设置二维数组的大小
     *
     * @param rowSize
     * @param columnSize
     */
    public void setTwoDimensionalSize(int rowSize, int columnSize) {
        if (mAdapter.rowSize == rowSize && mAdapter.columnSize == columnSize) {
            return;
        }
        mAdapter.setTwoDimensionalSize(rowSize, columnSize);
        mRecyclerView.setHasFixedSize(true);
        final StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(columnSize + 1, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(manager);
    }

    public void setOnItemClick(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
        if (onItemClick != null) {
            mRecyclerView.addOnItemTouchListener(itemClickListener);
        } else {
            mRecyclerView.removeOnItemTouchListener(itemClickListener);
        }
    }

    public static abstract class BaseTableAdapter<T extends BaseDataItem> extends BaseMultiItemQuickAdapter<BaseDataItem> {

        private static final int COLUMN_HEADER = 0xFFFF;
        private static final int EMPTY_VIEW = 0xFFFE;

        private int rowSize;

        private int columnSize;

        private Drawable headDecorationDrawable = null;

        private SparseIntArray gridHeight = new SparseIntArray();

        private SparseIntArray rowOffset = new SparseIntArray();

        public BaseTableAdapter() {
            super(null);
        }

        public void setColumnHeaderItemType(@LayoutRes int layoutResId) {
            addItemType(COLUMN_HEADER, layoutResId);
        }

        @Override
        protected void convert(BaseViewHolder helper, BaseDataItem item, int position) {
            int type = getItemViewType(item);
            if (COLUMN_HEADER == type) {
                View view = helper.getConvertView();
                if (view != null) {
                    ViewGroup.LayoutParams params = view.getLayoutParams();
                    if (params.height > 0) {
                        gridHeight.put(item.xPos, params.height);
                    }
                    convertColumnHeadView(item.xPos, helper);
                }
            } else {
                View view = helper.getConvertView();
                if (view != null) {
                    ViewGroup.LayoutParams params = view.getLayoutParams();
                    int height = gridHeight.get(item.xPos);
                    if (height > 0) {
                        height = height * item.placeSize;
                        int width = ViewGroup.LayoutParams.MATCH_PARENT;
                        if (params == null) {
                            params = new ViewGroup.LayoutParams(width, height);
                        } else {
                            params.height = height;
                        }
                        view.setLayoutParams(params);
                    }
                    if (EMPTY_VIEW != type) {
                        convertContentView(helper, (T) item);
                    }
                }
            }
        }

        @Override
        protected BaseViewHolder onCreateDefViewHolder(final ViewGroup parent, int viewType) {
            if (EMPTY_VIEW == viewType) {
                View view = new View(mContext);
                return createBaseViewHolder(view);
            }
            return super.onCreateDefViewHolder(parent, viewType);
        }

        @Override
        protected int getItemViewType(BaseDataItem item) {
            if (item.yPos == -1) {
                return COLUMN_HEADER;
            } else if (item.item == null) {
                return EMPTY_VIEW;
            }
            return getContentViewType((T) item);
        }

        /**
         * 设置二维数组大小
         *
         * @param rows
         * @param columns
         */
        private void setTwoDimensionalSize(int rows, int columns) {
            this.rowSize = rows;
            this.columnSize = columns;
        }

        /**
         * @hide
         */
        @Override
        public void setNewData(List<BaseDataItem> data) {
            super.setNewData(data);
        }

        public void setData(List<T> data) {
            gridHeight.clear();
            rowOffset.clear();
            BaseDataItem[][] fillData = new BaseDataItem[rowSize][columnSize + 1];
            for (int i = 0; i < rowSize; i++) {
                // 列头部坐标以-1开始
                BaseDataItem columnHeader = new BaseDataItem();
                columnHeader.xPos = i;
                columnHeader.yPos = -1;
                columnHeader.placeSize = 1;
                fillData[i][0] = columnHeader;
            }
            if (data != null && !data.isEmpty()) {
                for (T item : data) {
                    fillData[item.xPos][item.yPos + 1] = item;
                }
            }
            // 填充list
            List<BaseDataItem> newData = new ArrayList<>();
            for (int i = 0; i < fillData.length; i++) {
                for (int j = 0; j < fillData[i].length; j++) {
                    BaseDataItem item = fillData[i][j];
                    if (item == null) {
                        item = getLastSpaceItem(i, i - 1, j, fillData);
                        if (item != null) {
                            continue;
                        }
                        item = new BaseDataItem();
                        item.xPos = i;
                        item.yPos = j;
                        item.placeSize = getEmptySpaceSize(i, i + 1, j, fillData);
                        fillData[i][j] = item;
                    }
                    newData.add(item);
                }
            }
            setNewData(newData);
        }

        private BaseDataItem getLastSpaceItem(int index, int i, int j, BaseDataItem[][] fillData) {
            BaseDataItem item = null;
            if (i >= 0) {
                if (fillData[i][j] == null) {
                    item = getLastSpaceItem(index, i - 1, j, fillData);
                } else if (index <= i - 1 + fillData[i][j].placeSize) {
                    return fillData[i][j];
                }
            }
            return item;
        }

        private int getEmptySpaceSize(int index, int i, int j, BaseDataItem[][] fillData) {
            int size;
            if (i >= 0 && i < fillData.length) {
                if (fillData[i][j] != null) {
                    return i - index;
                } else {
                    return getEmptySpaceSize(index, i + 1, j, fillData);
                }
            } else {
                size = fillData.length - index;
            }
            return size;
        }

        private RecyclerView.ItemDecoration offsetDecoration = new RecyclerView.ItemDecoration() {


            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                if (headDecorationDrawable == null) {
                    return;
                }
                final int childCount = parent.getChildCount();
                for (int i = 0; i < childCount; i++) {
                    View child = parent.getChildAt(i);
                    RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
                    int position = params.getViewLayoutPosition();
                    BaseDataItem item = getItem(position);
                    if (item.yPos == -1) {
                        int left = child.getLeft() - params.leftMargin;
                        int right = child.getRight() + params.rightMargin;
                        int top = child.getBottom() + params.bottomMargin;
                        int bottom = top + headDecorationDrawable.getIntrinsicHeight();
                        headDecorationDrawable.setBounds(left, top, right, bottom);
                        headDecorationDrawable.draw(c);
                        left = right;
                        top = child.getTop() + params.topMargin;
                        right = left + headDecorationDrawable.getIntrinsicWidth();
                        bottom = child.getBottom() + params.bottomMargin + headDecorationDrawable.getIntrinsicHeight();
                        headDecorationDrawable.setBounds(left, top, right, bottom);
                        headDecorationDrawable.draw(c);
                    }
                }
            }

            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                int position = ((RecyclerView.LayoutParams) view.getLayoutParams()).getViewLayoutPosition();
                // 由于RecyclerView对item都是进行均分布局，因此设置偏移量，头部不动偏移
                BaseDataItem item = getItem(position);
                if (item.yPos == -1) {
                    if (rowOffset.get(item.xPos) <= 0) {
                        ViewGroup.LayoutParams params = view.getLayoutParams();
                        int width = params == null ? 0 : params.width;
                        if (width > 0) {
                            int columnSize = BaseTableAdapter.this.columnSize;
                            int parentWidth = parent.getWidth();
                            if (parentWidth <= 0) {
                                parentWidth = parent.getMeasuredWidth();
                            }
                            int gridSize = parentWidth / (columnSize + 1);
                            int offset = gridSize - width;
                            rowOffset.put(item.xPos, offset);
                        }
                    }
                    if (headDecorationDrawable != null) {
                        outRect.set(0, 0, 0, headDecorationDrawable.getIntrinsicHeight());
                    }
                } else {
                    int offset = rowOffset.get(item.xPos);
                    if (offset > 0) {
                        /**
                         * yPos由于头部为-1，因此从-1开始，所以不是0开始
                         * 左偏移：-(offset - (offset / columnSize) * yPos)
                         * 右偏移：-(offset - (offset / columnSize) * yPos + 1)
                         */
                        int offsetWidth = offset / columnSize;
                        outRect.set(-(offset - offsetWidth * item.yPos), 0,
                                offset - offsetWidth * (item.yPos + 1), 0);
                    }
                }
            }
        };

        /**
         * @hide
         */
        @Override
        public void addData(BaseDataItem data) {
            super.addData(data);
        }

        /**
         * @hide
         */
        @Override
        public void addData(List<BaseDataItem> newData) {
            super.addData(newData);
        }

        /**
         * @hide
         */
        @Override
        public void add(int position, BaseDataItem item) {
            super.add(position, item);
        }

        /**
         * @hide
         */
        @Override
        public void addData(int position, BaseDataItem data) {
            super.addData(position, data);
        }

        /**
         * @hide
         */
        @Override
        public void addData(int position, List<BaseDataItem> data) {
            super.addData(position, data);
        }

        /**
         * @hide
         */
        @Override
        public void remove(int position) {
            super.remove(position);
        }

        public int getContentViewType(T item) {
            return 0;
        }

        public abstract void convertContentView(BaseViewHolder helper, T item);

        public abstract void convertColumnHeadView(int row, BaseViewHolder helper);

        public abstract View getRowHeadView(int column, View view, ViewGroup parent);
    }

    public static class BaseDataItem<T> {
        /**
         * X坐标
         */
        public int xPos;
        /**
         * Y坐标
         */
        public int yPos;
        /**
         * 占位大小
         */
        public int placeSize = 1;
        /**
         * item数据
         */
        public T item;
    }

    public interface OnItemClick {

        /**
         * 列头点击
         *
         * @param view
         * @param row
         */
        void onColumnHeadItemClick(View view, int row);

        /**
         * 行头点击
         *
         * @param view
         * @param column
         */
        void onRowHeadItemClick(View view, int column);

        /**
         * table内容点击
         *
         * @param view
         * @param item
         * @param <T>
         */
        <T> void onTableItemClick(View view, BaseDataItem<T> item);
    }
}
