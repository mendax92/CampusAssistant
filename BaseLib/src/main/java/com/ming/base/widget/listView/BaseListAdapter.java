package com.ming.base.widget.listView;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ming on 2017/2/8.
 * 基础list adapter
 */

public abstract class BaseListAdapter<T> extends BaseAdapter {

    protected Context mContext;

    protected List<T> mList;
    protected LayoutInflater inflater;

    public BaseListAdapter(Context context) {
        this.mContext = context;
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public T getItem(int position) {
        return mList == null ? null : mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressWarnings("unchecked")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (null == convertView) {
            convertView = inflater.inflate(getLayoutResource(), parent, false);
        }
        onBindView(position, convertView, this.getItem(position));
        return convertView;
    }

    public abstract int getLayoutResource();

    public abstract void onBindView(int position, View convertView, T t);

    @SuppressWarnings("unchecked")
    public <E extends View> E findView(View view, int id) {
        SparseArray<View> viewHolder = (SparseArray<View>) view.getTag();
        if (viewHolder == null) {
            viewHolder = new SparseArray<>();
            view.setTag(viewHolder);
        }
        View childView = viewHolder.get(id);
        if (childView == null) {
            childView = view.findViewById(id);
            viewHolder.put(id, childView);
        }
        return (E) childView;
    }

    public void setNewData(T[] list) {
        ArrayList<T> arrayList = new ArrayList<>(list.length);
        for (T t : list) {
            arrayList.add(t);
        }
        setNewData(arrayList);
    }

    public void setNewData(List<T> list) {
        this.mList = list;
        notifyDataSetChanged();
    }

    public List<T> getList() {
        return mList;
    }

    public void add(T t) {
        this.mList.add(t);
        notifyDataSetChanged();
    }

    public void addData(List<T> list) {
        this.mList.addAll(list);
        notifyDataSetChanged();
    }

    public void remove(int position) {
        this.mList.remove(position);
        notifyDataSetChanged();
    }

    public void clear() {
        this.mList.clear();
        notifyDataSetChanged();
    }

}
