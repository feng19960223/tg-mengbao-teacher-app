package com.turingoal.mengbao.teacher.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.turingoal.mengbao.teacher.ui.fragment.PhotoFragment;

import java.util.ArrayList;

/**
 * 班级相册详情adapter
 */

public class PicturesViewPagerAdapter extends FragmentPagerAdapter {
    private ArrayList<PhotoFragment> fragments;

    public PicturesViewPagerAdapter(final FragmentManager fragmentManager, final ArrayList<PhotoFragment> fragments) {
        super(fragmentManager);
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(final int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments == null ? 0 : fragments.size();
    }
}
