package com.ming.base.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;
import android.view.KeyEvent;

import com.ming.base.fragment.BaseFragment;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 基本acitivity
 *
 * @author Edmin
 */
public class BaseActivity extends AppCompatActivity {

    private SparseArray<Set<OnActivityResultListener>> onActivityResultsWithRequestId;

    private Set<OnActivityResultListener> onActivityResults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityManager.getInstance().pushActivity(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ActivityManager.getInstance().bringToForeground();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (onActivityResultsWithRequestId != null) {
            onActivityResultsWithRequestId.clear();
        }
        if (onActivityResults != null) {
            onActivityResults.clear();
        }
        ActivityManager.getInstance().popActivity(this);
    }

    /**
     * 对fragment做事件分发
     *
     * @return
     */
    @Override
    public void onBackPressed() {
        FragmentManager fm = getSupportFragmentManager();
        List<Fragment> fragments = fm.getFragments();
        if (fragments != null && !fragments.isEmpty()) {
            for (Fragment fragment : fragments) {
                if (fragment == null) {
                    continue;
                }
                if (fragment instanceof BaseFragment && fragment.isVisible()) {
                    BaseFragment baseFragment = (BaseFragment) fragment;
                    // 只对根Fragment做判断，子Fragment已经在BaseFragment做了事件分发
                    boolean hasParent = baseFragment.getParent() != null;
                    if (!hasParent && baseFragment.isDispatchKeyEvent() && baseFragment.onBackPressed()) {
                        return;
                    }
                }
            }
        }
        super.onBackPressed();
    }

    /**
     * 对fragment做事件分发
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        FragmentManager fm = getSupportFragmentManager();
        List<Fragment> fragments = fm.getFragments();
        if (fragments != null && !fragments.isEmpty()) {
            for (Fragment fragment : fragments) {
                if (fragment == null) {
                    continue;
                }
                if (fragment instanceof BaseFragment && fragment.isVisible()) {
                    BaseFragment baseFragment = (BaseFragment) fragment;
                    // 只对根Fragment做判断，子Fragment已经在BaseFragment做了事件分发
                    boolean hasParent = baseFragment.getParent() != null;
                    if (!hasParent && baseFragment.isDispatchKeyEvent() && baseFragment.onKeyDown(keyCode, event)) {
                        return true;
                    }
                }
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 对fragment做事件分发
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        FragmentManager fm = getSupportFragmentManager();
        List<Fragment> fragments = fm.getFragments();
        if (fragments != null && !fragments.isEmpty()) {
            for (Fragment fragment : fragments) {
                if (fragment == null) {
                    continue;
                }
                if (fragment instanceof BaseFragment && fragment.isVisible()) {
                    BaseFragment baseFragment = (BaseFragment) fragment;
                    // 只对根Fragment做判断，子Fragment已经在BaseFragment做了事件分发
                    boolean hasParent = baseFragment.getParent() != null;
                    if (!hasParent && baseFragment.isDispatchKeyEvent() && baseFragment.onKeyUp(keyCode, event)) {
                        return true;
                    }
                }
            }
        }
        return super.onKeyUp(keyCode, event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Set<OnActivityResultListener> listeners = new HashSet<>();
        if (onActivityResults != null) {
            listeners.addAll(onActivityResults);
        }
        if (onActivityResultsWithRequestId != null) {
            for (int i = 0; i < onActivityResultsWithRequestId.size(); i++) {
                Set<OnActivityResultListener> set = onActivityResultsWithRequestId.valueAt(i);
                if (set == null) {
                    continue;
                }
                listeners.addAll(set);
            }
        }
        for (OnActivityResultListener listener : listeners) {
            listener.onActivityResult(requestCode, resultCode, data);
        }

    }

    /**
     * 增加onActivityResult回调
     *
     * @param requestCode 请求code
     * @param listener    监听
     */
    public void addOnActivityResultListener(int requestCode, OnActivityResultListener listener) {
        if (onActivityResultsWithRequestId == null) {
            onActivityResultsWithRequestId = new SparseArray<>();
        }
        Set<OnActivityResultListener> set = onActivityResultsWithRequestId.get(requestCode);
        if (set == null) {
            set = new HashSet<>();
            onActivityResultsWithRequestId.put(requestCode, set);
        }
        set.add(listener);
    }

    public void addOnActivityResultListener(OnActivityResultListener listener) {
        if (onActivityResults != null) {
            onActivityResults = new HashSet<>();
        }
        onActivityResults.add(listener);
    }

    public void removeOnActivityResultListener(OnActivityResultListener listener) {
        if (onActivityResultsWithRequestId != null) {
            for (int i = 0; i < onActivityResultsWithRequestId.size(); i++) {
                Set<OnActivityResultListener> set = onActivityResultsWithRequestId.valueAt(i);
                if (set != null && set.contains(listener)) {
                    set.remove(listener);
                    if (set.isEmpty()) {
                        onActivityResultsWithRequestId.removeAt(i);
                    }
                }
            }
        }
        if (onActivityResults != null) {
            onActivityResults.remove(listener);
        }
    }

    public void removeOnActivityResultListener(int requestCode) {
        if (onActivityResultsWithRequestId != null) {
            onActivityResultsWithRequestId.remove(requestCode);
        }
    }

    public interface OnActivityResultListener {
        void onActivityResult(int requestCode, int resultCode, Intent data);
    }
}
