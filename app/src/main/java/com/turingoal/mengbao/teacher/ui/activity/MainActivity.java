package com.turingoal.mengbao.teacher.ui.activity;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.view.MenuItem;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.turingoal.common.app.TgApplication;
import com.turingoal.common.app.TgSystemHelper;
import com.turingoal.common.base.TgBaseActivity;
import com.turingoal.common.base.TgBaseFragment;
import com.turingoal.mengbao.teacher.R;
import com.turingoal.mengbao.teacher.constants.ConstantActivityPath;
import com.turingoal.mengbao.teacher.ui.fragment.FragmentFactory;

import butterknife.BindView;

/**
 * 主页
 */
@Route(path = ConstantActivityPath.MAIN)
public class MainActivity extends TgBaseActivity {
    @BindView(R.id.navigation)
    BottomNavigationView mNavigation; // 底部按钮
    private TgBaseFragment mFragment; // 当前Fragment

    @Override
    protected int getLayoutID() {
        return R.layout.activity_main;
    }

    @Override
    protected void initialized() {
        TgApplication.getTgUserPreferences().setClassId("c1");
        TgApplication.getTgUserPreferences().setUserId("u1");
        TgApplication.getTgUserPreferences().setSchoolId("s1");
        mFragment = FragmentFactory.createFragment(FragmentFactory.FRAGMENT_MAIN); // 默认选中项
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, mFragment).commit();
        mNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull final MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_main:
                        mFragment = FragmentFactory.createFragment(FragmentFactory.FRAGMENT_MAIN); // 主页
                        break;
                    case R.id.navigation_rank:
                        mFragment = FragmentFactory.createFragment(FragmentFactory.FRAGMENT_RANK); // 个人中心
                        break;
                    case R.id.navigation_personal:
                        mFragment = FragmentFactory.createFragment(FragmentFactory.FRAGMENT_PERSONAL); // 个人中心
                        break;
                    default:
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, mFragment).commit();
                return true;
            }
        });
    }

    /**
     * 点击返回按钮
     */
    @Override
    public void onBackPressed() {
        if (mNavigation.getSelectedItemId() != R.id.navigation_main) { // 先回到主页面，再退出
            mNavigation.setSelectedItemId(R.id.navigation_main);
        } else {
            TgSystemHelper.dbClickExit(this); // 再按一次退出系统
        }
    }
}
