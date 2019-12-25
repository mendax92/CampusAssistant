package com.ming.photopicker.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ming.photopicker.R;
import com.ming.photopicker.adapter.PhotoPagerAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by donglua on 15/6/21.
 */
public class ImagePagerFragment extends Fragment {

    protected static final String TAG = "ImagePagerFragment";

    public final static String ARG_PATH = "PATHS";
    public final static String ARG_CURRENT_ITEM = "ARG_CURRENT_ITEM";

    public final static String ARG_SELECT_PATH = "SELECT_PATHS";

    public final static String ARG_SHOW_SELECTED = "SHOW_SELECTED";

    public final static String EXTRA_COUNT = "count";

    private ArrayList<String> paths;
    private ArrayList<String> selectedPaths;
    private ArrayList<String> thumbnails;
    private ViewPager mViewPager;
    private PhotoPagerAdapter mPagerAdapter;
    private View checkedView;

    private int currentItem = 0;
    private boolean showSelected = false;
    private int maxCount;
    private OnCheckedListener onCheckedListener;


    protected static ImagePagerFragment newInstance(List<String> paths, List<String> selectedPaths, int currentItem, int maxCount, boolean showSelect) {

        ImagePagerFragment f = new ImagePagerFragment();

        Bundle args = new Bundle();
        if (paths != null) {
            args.putStringArray(ARG_PATH, paths.toArray(new String[paths.size()]));
        }
        args.putInt(ARG_CURRENT_ITEM, currentItem);
        args.putInt(EXTRA_COUNT, maxCount);
        if (selectedPaths != null) {
            args.putStringArray(ARG_SELECT_PATH, selectedPaths.toArray(new String[selectedPaths.size()]));
        }
        args.putBoolean(ARG_SHOW_SELECTED, showSelect);

        f.setArguments(args);

        return f;
    }


    public static ImagePagerFragment newInstance(List<String> paths, int currentItem) {

        return newInstance(paths, null, currentItem, 0, false);
    }

    public void setPhotos(List<String> paths, List<String> thumbnails, int currentItem) {
        this.paths.clear();
        this.paths.addAll(paths);
        if (thumbnails != null) {
            this.thumbnails.addAll(thumbnails);
        }
        this.currentItem = currentItem;
        mViewPager.setCurrentItem(currentItem);
        mViewPager.getAdapter().notifyDataSetChanged();
    }


    public void setPhotos(List<String> paths, int currentItem) {
        setPhotos(paths, null, currentItem);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        paths = new ArrayList<>();
        selectedPaths = new ArrayList<>();
        thumbnails = new ArrayList<>();

        Bundle bundle = getArguments();

        if (bundle != null) {
            String[] pathArr = bundle.getStringArray(ARG_PATH);
            paths.clear();
            if (pathArr != null) {

                paths = new ArrayList<>(Arrays.asList(pathArr));
            }
            pathArr = bundle.getStringArray(ARG_SELECT_PATH);
            selectedPaths.clear();
            if (pathArr != null) {

                selectedPaths = new ArrayList<>(Arrays.asList(pathArr));
            }
            maxCount = bundle.getInt(EXTRA_COUNT);
            currentItem = bundle.getInt(ARG_CURRENT_ITEM);
            showSelected = bundle.getBoolean(ARG_SHOW_SELECTED);
        }

        mPagerAdapter = new PhotoPagerAdapter(Glide.with(this), paths, thumbnails);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.photo_picker_fragment_image_pager, container, false);
        initView(rootView);

        return rootView;
    }

    protected void initView(View rootView) {
        mViewPager = (ViewPager) rootView.findViewById(R.id.vp_photos);
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setCurrentItem(currentItem);
        mViewPager.setOffscreenPageLimit(3);


        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (!selectedPaths.isEmpty()) {
                    String path = paths.get(getCurrentItem());
                    checkedView.setSelected(!TextUtils.isEmpty(path) && selectedPaths.contains(path));
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        View bottomView = rootView.findViewById(R.id.iv_bottom_view);
        if (showSelected) {
            checkedView = rootView.findViewById(R.id.iv_check_layout);
            checkedView.setOnClickListener(onSelectListener);
            bottomView.setVisibility(View.VISIBLE);
        } else {
            bottomView.setVisibility(View.GONE);
        }
        if (!selectedPaths.isEmpty() && selectedPaths.contains(paths.get(currentItem))) {
            checkedView.setSelected(true);
        }
    }

    private View.OnClickListener onSelectListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String path = paths.get(getCurrentItem());
            if (!TextUtils.isEmpty(path)) {
                if (selectedPaths.contains(path)) {
                    selectedPaths.remove(path);
                    checkedView.setSelected(false);
                    if (onCheckedListener != null) {
                        onCheckedListener.onChecked(false, path);
                    }
                } else {
                    if (selectedPaths.size() >= maxCount) {
                        Toast.makeText(getActivity(), getString(R.string.photo_picker_over_max_count_tips, maxCount),
                                Toast.LENGTH_LONG).show();
                        return;
                    }
                    selectedPaths.add(path);
                    checkedView.setSelected(true);
                    if (onCheckedListener != null) {
                        onCheckedListener.onChecked(true, path);
                    }
                }
            }
        }
    };


    public ViewPager getViewPager() {
        return mViewPager;
    }


    public ArrayList<String> getPaths() {
        return paths;
    }


    public int getCurrentItem() {
        return mViewPager.getCurrentItem();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        paths.clear();
        paths = null;

        if (mViewPager != null) {
            mViewPager.setAdapter(null);
        }
    }

    public ArrayList<String> getSelectedPaths() {
        return selectedPaths == null ? new ArrayList<String>() : selectedPaths;
    }

    public boolean isShowSelected() {
        return showSelected;
    }

    public int getMaxCount() {
        return maxCount;
    }

    public void setOnCheckedListener(OnCheckedListener onCheckedListener) {
        this.onCheckedListener = onCheckedListener;
    }

    public interface OnCheckedListener {
        void onChecked(boolean checked, String selectPath);
    }
}
