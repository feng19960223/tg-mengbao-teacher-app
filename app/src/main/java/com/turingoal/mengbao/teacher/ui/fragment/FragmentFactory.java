package com.turingoal.mengbao.teacher.ui.fragment;

import com.turingoal.common.base.TgBaseFragment;

import java.util.HashMap;

/**
 * Fragment工厂
 */

public class FragmentFactory {
    public static final int FRAGMENT_MAIN = 0; // 主页
    public static final int FRAGMENT_RANK = 1; // 排行
    public static final int FRAGMENT_PERSONAL = 2; // 我的
    private static HashMap<Integer, TgBaseFragment> mFragments = null; //Fragment缓存

    private FragmentFactory() {
        throw new Error("Fragment工厂类不能实例化！");
    }

    /**
     * 根据类型创建Fragment
     */
    public static TgBaseFragment createFragment(final int type) {
        if (mFragments == null) {
            mFragments = new HashMap<>();
        }
        TgBaseFragment fragment = mFragments.get(type);
        if (fragment == null) {
            switch (type) {
                case FRAGMENT_MAIN:
                    fragment = new MainFragment(); // 主页
                    break;
                case FRAGMENT_RANK:
                    fragment = new RankFragment(); // 主页
                    break;
                case FRAGMENT_PERSONAL:
                    fragment = new PersonalFragment();  // 我的
                    break;
                default:
                    break;
            }
            if (fragment != null) {
                mFragments.put(type, fragment); // 如果新new了Fragment，加到缓存中
            }
        }
        return fragment;
    }

    /**
     * 清理数据缓存，退出的时候要清理重新加载
     */
    public static void fragmetnFactoryClearData() {
        mFragments = null;
    }
}
