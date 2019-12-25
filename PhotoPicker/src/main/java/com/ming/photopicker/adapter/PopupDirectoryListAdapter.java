package com.ming.photopicker.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.bumptech.glide.RequestManager;
import com.ming.photopicker.R;
import com.ming.photopicker.entity.PhotoDirectory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by donglua on 15/6/28.
 */
public class PopupDirectoryListAdapter extends BaseAdapter {


    private List<PhotoDirectory> directories = new ArrayList<>();
    private RequestManager glide;
    private int currentDirectoryPosition;

    public PopupDirectoryListAdapter(RequestManager glide, List<PhotoDirectory> directories) {
        this.directories = directories;
        this.glide = glide;
    }


    @Override
    public int getCount() {
        return directories.size();
    }


    @Override
    public PhotoDirectory getItem(int position) {
        return directories.get(position);
    }


    @Override
    public long getItemId(int position) {
        return directories.get(position).hashCode();
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            LayoutInflater mLayoutInflater = LayoutInflater.from(parent.getContext());
            convertView = mLayoutInflater.inflate(R.layout.photo_picker_item_directory, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.bindData(directories.get(position), position);
        return convertView;
    }

    public void setCurrentDirectoryPosition(int currentDirectoryPosition) {
        this.currentDirectoryPosition = currentDirectoryPosition;
        notifyDataSetChanged();
    }

    private class ViewHolder {

        public ImageView ivCover;
        public TextView tvName;
        public TextView tvCount;
        public View line;
        public RadioButton mRadiobtn;

        public ViewHolder(View rootView) {
            ivCover = (ImageView) rootView.findViewById(R.id.iv_dir_cover);
            tvName = (TextView) rootView.findViewById(R.id.tv_dir_name);
            tvCount = (TextView) rootView.findViewById(R.id.tv_dir_count);
            mRadiobtn = (RadioButton) rootView.findViewById(R.id.tv_picker_checkbox);
            line = rootView.findViewById(R.id.line);
        }

        public void bindData(PhotoDirectory directory, int position) {
            glide.load(directory.getCoverPath())
                    .thumbnail(0.1f)
                    .into(ivCover);
            tvName.setText(directory.getName());
            tvCount.setText(tvCount.getContext().getString(R.string.photo_picker_image_count, directory.getPhotos().size()));
            if (position == getCount() - 1) {
                line.setVisibility(View.GONE);
            } else {
                line.setVisibility(View.VISIBLE);
            }
            if (currentDirectoryPosition == position) {
                mRadiobtn.setChecked(true);
                mRadiobtn.setVisibility(View.VISIBLE);
            } else {
                mRadiobtn.setVisibility(View.GONE);
            }
        }
    }

}
