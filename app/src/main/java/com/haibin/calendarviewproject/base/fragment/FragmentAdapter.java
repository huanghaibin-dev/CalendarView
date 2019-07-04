package com.haibin.calendarviewproject.base.fragment;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class FragmentAdapter extends FragmentPagerAdapter {
    private List<Fragment> mFragment = new ArrayList<>();
    private final FragmentManager mFragmentManager;
    private boolean mUpdateFlag;
    private Fragment mCurFragment;
    private String[] mTitles;

    public FragmentAdapter(FragmentManager fm) {
        super(fm);
        this.mFragmentManager = fm;
    }

    public boolean isUpdateFlag() {
        return mUpdateFlag;
    }

    public void setUpdateFlag(boolean mUpdateFlag) {
        this.mUpdateFlag = mUpdateFlag;
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }


    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        if (mUpdateFlag) {
            Fragment fragment = (Fragment) super.instantiateItem(container, position);
            String tag = fragment.getTag();
            FragmentTransaction transaction = mFragmentManager.beginTransaction();
            transaction.remove(fragment);
            fragment = getItem(position);
            if (!fragment.isAdded()) {
                transaction.add(container.getId(), fragment, tag)
                        .attach(fragment)
                        .commitAllowingStateLoss();
            }
            return fragment;
        }
        return super.instantiateItem(container, position);
    }

    public void reset(List<Fragment> fragments) {
        mFragment.clear();
        mFragment.addAll(fragments);
    }

    @Override
    public void setPrimaryItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        super.setPrimaryItem(container, position, object);
        if (object instanceof Fragment) {
            mCurFragment = (Fragment) object;
        }
    }

    public Fragment getCurFragment() {
        return mCurFragment;
    }

    public void reset(String[] titles) {
        this.mTitles = titles;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragment.get(position);
    }

    @Override
    public int getCount() {
        return mFragment.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles[position];
    }
}
