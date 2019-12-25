package com.ming.photopicker;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ActionProvider;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.ming.photopicker.entity.Photo;
import com.ming.photopicker.event.OnItemCheckListener;
import com.ming.photopicker.fragment.ImageChoosePagerFragment;
import com.ming.photopicker.fragment.PhotoPickerFragment;
import com.ming.photopicker.widget.DoneActionProvider;

import java.util.ArrayList;
import java.util.List;

public class PhotoPickerActivity extends AppCompatActivity {

    private static final String TAG = "PhotoPickerActivity";

    private PhotoPickerFragment pickerFragment;
    private ImageChoosePagerFragment imagePagerFragment;
    private DoneActionProvider doneActionProvider;

    private int maxCount = PhotoPicker.DEFAULT_MAX_COUNT;

    private boolean showGif = false;
    private int columnNumber = PhotoPicker.DEFAULT_COLUMN_NUMBER;
    private ArrayList<String> originalPhotos = null;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        boolean showCamera = getIntent().getBooleanExtra(PhotoPicker.EXTRA_SHOW_CAMERA, true);
        boolean showGif = getIntent().getBooleanExtra(PhotoPicker.EXTRA_SHOW_GIF, false);
        boolean previewEnabled = getIntent().getBooleanExtra(PhotoPicker.EXTRA_PREVIEW_ENABLED, true);

        setShowGif(showGif);

        setContentView(R.layout.photo_picker_activity_photo_picker);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        setTitle(R.string.photo_picker_title);

        ActionBar actionBar = getSupportActionBar();

        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            actionBar.setElevation(25);
        }

        maxCount = getIntent().getIntExtra(PhotoPicker.EXTRA_MAX_COUNT, PhotoPicker.DEFAULT_MAX_COUNT);
        columnNumber = getIntent().getIntExtra(PhotoPicker.EXTRA_GRID_COLUMN, PhotoPicker.DEFAULT_COLUMN_NUMBER);
        originalPhotos = getIntent().getStringArrayListExtra(PhotoPicker.EXTRA_ORIGINAL_PHOTOS);

        pickerFragment = (PhotoPickerFragment) getSupportFragmentManager().findFragmentByTag("tag");
        if (pickerFragment == null) {
            pickerFragment = PhotoPickerFragment
                    .newInstance(showCamera, showGif, previewEnabled, columnNumber, maxCount, originalPhotos);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, pickerFragment, "tag")
                    .commit();
            getSupportFragmentManager().executePendingTransactions();
        }

        pickerFragment.getPhotoGridAdapter().setOnItemCheckListener(new OnItemCheckListener() {
            @Override
            public boolean onItemCheck(int position, Photo photo, final int selectedItemCount) {
                pickerFragment.updatePreview(selectedItemCount);
                if (doneActionProvider != null) {
                    doneActionProvider.setEnabled(selectedItemCount > 0);
                }
                if (maxCount <= 1) {
                    List<String> photos = pickerFragment.getPhotoGridAdapter().getSelectedPhotos();
                    if (!photos.contains(photo.getPath())) {
                        photos.clear();
                        pickerFragment.getPhotoGridAdapter().notifyDataSetChanged();
                    }
                    return true;
                }

                if (selectedItemCount > maxCount) {
                    Toast.makeText(getActivity(), getString(R.string.photo_picker_over_max_count_tips, maxCount),
                            Toast.LENGTH_LONG).show();
                    return false;
                }
                if (doneActionProvider != null) {
                    doneActionProvider.setTitle(selectedItemCount > 0 ? getString(R.string.photo_picker_done_with_count, selectedItemCount, maxCount) : getString(R.string.photo_picker_done));
                }
                return true;
            }
        });
    }

    public void addImagePagerFragment(final ImageChoosePagerFragment imagePagerFragment) {
        this.imagePagerFragment = imagePagerFragment;

        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .add(R.id.container, this.imagePagerFragment)
                .addToBackStack(null)
                .commit();
        fm.registerFragmentLifecycleCallbacks(new FragmentManager.FragmentLifecycleCallbacks() {
            @Override
            public void onFragmentDetached(FragmentManager fm, Fragment f) {
                Log.i(TAG, "f:" + f);
                if (f == pickerFragment) {
                    fm.unregisterFragmentLifecycleCallbacks(this);
                } else if (f == imagePagerFragment) {
                    ArrayList<String> selectedPaths = imagePagerFragment.getSelectedPaths();
                    pickerFragment.updatePreview(selectedPaths.size());
                    pickerFragment.getPhotoGridAdapter().setSelectedPhotoPaths(selectedPaths);
                    invalidateToolbar();
                    fm.unregisterFragmentLifecycleCallbacks(this);
                }
            }
        }, true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.clear();
        getMenuInflater().inflate(R.menu.photo_picker_menu_picker, menu);
        MenuItem menuItem = menu.findItem(R.id.done);
        ActionProvider provider = MenuItemCompat.getActionProvider(menuItem);
        if (provider == null || !(provider instanceof DoneActionProvider)) {
            return false;
        }
        doneActionProvider = (DoneActionProvider) provider;
        ArrayList<String> selectedPhotos = pickerFragment.getSelectedPhotoPaths();
        if (selectedPhotos != null && selectedPhotos.size() > 0) {
            doneActionProvider.setEnabled(true);
            doneActionProvider.setTitle(
                    getString(R.string.photo_picker_done_with_count, selectedPhotos.size(), maxCount));
        } else {
            doneActionProvider.setTitle(R.string.photo_picker_done);
            doneActionProvider.setEnabled(false);
        }
        doneActionProvider.setOnMenuClickListener(new DoneActionProvider.OnMenuClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                ArrayList<String> selectedPhotos = pickerFragment.getSelectedPhotoPaths();
                intent.putStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS, selectedPhotos);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            super.onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public PhotoPickerActivity getActivity() {
        return this;
    }

    public boolean isShowGif() {
        return showGif;
    }

    public void setShowGif(boolean showGif) {
        this.showGif = showGif;
    }

    public ImageChoosePagerFragment getImagePagerFragment() {
        return imagePagerFragment;
    }

    public void invalidateToolbar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.invalidateOptionsMenu();
            actionBar.setTitle(R.string.photo_picker_title);
        }
    }
}
