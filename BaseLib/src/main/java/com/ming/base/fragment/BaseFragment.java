package com.ming.base.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;

import com.ming.base.util.FragmentManagerUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ming on 2016/8/16.
 */
public class BaseFragment extends Fragment {

    private ArrayList<Fragment> children;

    private BaseFragment parent;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * 由FragmentActivity 将按键事件传递进来，再分发给添加进来的子Fragment
     *
     * @param keyCode
     * @param event
     * @return 返回true，将不再继续传递事件；返回false继续传递
     */
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        List<Fragment> childFragments = getChildrenFragments();
        if (childFragments != null) {
            for (int i = 0; i < childFragments.size(); i++) {
                Fragment child = childFragments.get(i);
                if (child != null && child instanceof BaseFragment) {
                    BaseFragment childBase = (BaseFragment) child;
                    if (childBase.isDispatchKeyEvent() && childBase.onKeyDown(keyCode, event)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 由FragmentActivity 将按键事件传递进来，再分发给添加进来的子Fragment
     *
     * @param keyCode
     * @param event
     * @return 返回true，将不再继续传递事件；返回false继续传递
     */
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        List<Fragment> childFragments = getChildrenFragments();
        if (childFragments != null) {
            for (int i = 0; i < childFragments.size(); i++) {
                Fragment child = childFragments.get(i);
                if (child != null && child instanceof BaseFragment) {
                    BaseFragment childBase = (BaseFragment) child;
                    if (childBase.isDispatchKeyEvent() && childBase.onKeyUp(keyCode, event)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 返回键处理
     *
     * @return
     */
    public boolean onBackPressed() {
        List<Fragment> childFragments = getChildrenFragments();
        if (childFragments != null) {
            for (int i = 0; i < childFragments.size(); i++) {
                Fragment child = childFragments.get(i);
                if (child != null && child instanceof BaseFragment) {
                    BaseFragment childBase = (BaseFragment) child;
                    if (childBase.isDispatchKeyEvent() && childBase.onBackPressed()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public void addFragment(int containerId, Fragment fragment, boolean addAnimation) {
        if (fragment == null) {
            return;
        }
        if (fragment instanceof BaseFragment) {
            ((BaseFragment) fragment).parent = this;
        }
        addChild(fragment);
        FragmentManagerUtils.addFragmentOnly(this, containerId, fragment, addAnimation);
    }

    public void replaceFragment(int containerId, Fragment fragment, boolean addAnimation) {
        if (fragment == null) {
            return;
        }
        if (fragment instanceof BaseFragment) {
            ((BaseFragment) fragment).parent = this;
        }
        addChild(fragment);
        FragmentManagerUtils.replaceFragmentOnly(this, containerId, fragment, addAnimation);
    }

    public void addChild(Fragment fragment) {
        if (children == null) {
            children = new ArrayList<>();
        }
        if (!children.contains(fragment)) {
            children.add(fragment);
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
    }

    /**
     * 关闭自己
     */
    protected boolean removeSelf() {
        boolean success = false;
        try {
            if (parent != null && parent.children != null) {
                parent.children.remove(this);
                parent.onChildRemove(this);
                if (parent.children.isEmpty()) {
                    parent.children = null;
                }
            }
            if (isVisible()) {
                FragmentManagerUtils.getRemoveFragmentTransaction(this).remove(this).commitAllowingStateLoss();
            }
            success = true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return success;
    }

    /**
     * 是否分发事件
     *
     * @return
     */
    public boolean isDispatchKeyEvent() {
        return false;
    }

    /**
     * 子fragment被remove时
     *
     * @param child
     */
    public void onChildRemove(Fragment child) {
    }

    /**
     * 获取父Fragment
     *
     * @return
     */
    public final BaseFragment getParent() {
        return parent;
    }

    /**
     * 获取子Fragment
     *
     * @return
     */
    public final List<Fragment> getChildrenFragments() {
        return children;
    }
}
