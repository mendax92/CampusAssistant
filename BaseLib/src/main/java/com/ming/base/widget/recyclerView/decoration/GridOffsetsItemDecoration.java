package com.ming.base.widget.recyclerView.decoration;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by ming on 2016/10/12.
 */

public class GridOffsetsItemDecoration extends RecyclerView.ItemDecoration {

    private int mVerticalSpace;
    private int mHorizontalSpace;

    public GridOffsetsItemDecoration(Context context, int dpOfVerticalSpace, int dpHorizontalSpace) {
        mVerticalSpace = context.getResources().getDimensionPixelOffset(dpOfVerticalSpace);
        mHorizontalSpace = context.getResources().getDimensionPixelOffset(dpHorizontalSpace);
    }

    public GridOffsetsItemDecoration(int verticalSpace, int horizontalSpace) {
        mVerticalSpace = verticalSpace;
        mHorizontalSpace = horizontalSpace;
    }

    public void setVerticalSpace(int verticalItemOffsets) {
        this.mVerticalSpace = verticalItemOffsets;
    }

    public void setHorizontalSpace(int horizontalItemOffsets) {
        this.mHorizontalSpace = horizontalItemOffsets;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

        int left = mVerticalSpace / 2;
        int right = left;
        int top = mHorizontalSpace / 2;
        int bottom = top;
        outRect.set(left, top, right, bottom);
    }
}
