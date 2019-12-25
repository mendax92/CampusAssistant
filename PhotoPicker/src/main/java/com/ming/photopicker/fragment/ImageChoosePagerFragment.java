package com.ming.photopicker.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ActionProvider;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.ming.photopicker.PhotoPicker;
import com.ming.photopicker.R;
import com.ming.photopicker.widget.DoneActionProvider;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * Created by ming on 2017/2/23.
 */

public class ImageChoosePagerFragment extends ImagePagerFragment {

    private DoneActionProvider doneActionProvider;

    private ActionBar actionBar;

    private boolean inflateMenu = false;


    public static ImageChoosePagerFragment newInstance(List<String> paths, List<String> selectedPaths, int currentItem, int maxCount, boolean showSelect) {

        ImageChoosePagerFragment f = new ImageChoosePagerFragment();

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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    protected void initView(View rootView) {
        super.initView(rootView);

        actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        updateActionBarTitle();
        getViewPager().addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                updateActionBarTitle();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        setOnCheckedListener(new OnCheckedListener() {
            @Override
            public void onChecked(boolean checked, String selectPath) {
                setDoneTitle();
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (isShowSelected() && !inflateMenu) {
            menu.clear();
            inflater.inflate(R.menu.photo_picker_menu_picker, menu);
            MenuItem menuItem = menu.findItem(R.id.done);
            ActionProvider provider = MenuItemCompat.getActionProvider(menuItem);
            if (provider == null || !(provider instanceof DoneActionProvider)) {
                return;
            }
            doneActionProvider = (DoneActionProvider) provider;
            setDoneTitle();
            doneActionProvider.setOnMenuClickListener(new DoneActionProvider.OnMenuClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    ArrayList<String> selectedPhotos = getSelectedPaths();
                    if (selectedPhotos.isEmpty()) {
                        String path = getPaths().get(getCurrentItem());
                        if (TextUtils.isEmpty(path)) {
                            return;
                        }
                        selectedPhotos.add(path);
                    }
                    intent.putStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS, selectedPhotos);
                    getActivity().setResult(RESULT_OK, intent);
                    getActivity().finish();
                }
            });
            inflateMenu = true;
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void setDoneTitle() {
        if (getSelectedPaths().size() > 0) {
            doneActionProvider.setTitle(
                    getString(R.string.photo_picker_done_with_count, getSelectedPaths().size(), getMaxCount()));
        } else {
            doneActionProvider.setTitle(R.string.photo_picker_done);
        }
    }

    public void updateActionBarTitle() {
        if (actionBar != null) {
            actionBar.setTitle(getString(R.string.photo_picker_image_index, getCurrentItem() + 1, getPaths().size()));
        }
    }

    @Override
    public void setOnCheckedListener(OnCheckedListener onCheckedListener) {
        super.setOnCheckedListener(onCheckedListener);
    }
}
