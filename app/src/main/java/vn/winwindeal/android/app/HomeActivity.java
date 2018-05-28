package vn.winwindeal.android.app;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.MenuItem;
import android.view.View;

import vn.winwindeal.android.app.fragment.OrderFragment;
import vn.winwindeal.android.app.fragment.ProductListFragment;
import vn.winwindeal.android.app.fragment.SettingsFragment;
import vn.winwindeal.android.app.fragment.UsersFragment;
import vn.winwindeal.android.app.model.UserInfo;
import vn.winwindeal.android.app.util.DialogUtil;

/**
 * Created by nhannguyen on 4/6/2018.
 */

public class HomeActivity extends BaseActivity {

    private int mSeletedPage = -1;
    UserInfo ui;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);
        initComponents();
    }

    private void initComponents() {
        ui = GlobalSharedPreference.getUserInfo(this);
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        if (ui.user_type == 3) {
            // customer
            bottomNav.getMenu().removeItem(R.id.action_users);
        }
        bottomNav.setSelectedItemId(R.id.action_home);
        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_home:
                        setFragment(Constant.HOME_FRAGMENT);
                        break;
                    case R.id.action_users:
                        setFragment(Constant.USERS_MANAGE_FRAGMENT);
                        break;
                    case R.id.action_settings:
                        setFragment(Constant.SETTINGS_FRAGMENT);
                        break;
                    case R.id.action_task:
                        setFragment(Constant.ORDER_FRAGMENT);
                        break;
                }
                return true;
            }
        });
        int selectedPage = getIntent().getIntExtra(Constant.SELECTED_PAGE, -1);
        if (selectedPage != -1) {
            setFragment(selectedPage);
        } else {
            setFragment(Constant.HOME_FRAGMENT);
        }
    }

    private void setFragment(int pos) {
        if (mSeletedPage == pos) {
            return;
        }
        mSeletedPage = pos;
        Fragment fragment = null;
        Bundle bundle = new Bundle();
        switch (pos) {
            case Constant.HOME_FRAGMENT:
                fragment = new ProductListFragment();
                break;
            case Constant.USERS_MANAGE_FRAGMENT:
                fragment = new UsersFragment();
                break;
            case Constant.SETTINGS_FRAGMENT:
                fragment = new SettingsFragment();
                break;
            case Constant.ORDER_FRAGMENT:
                fragment = new OrderFragment();
                break;
        }
        if (fragment != null) {
            fragment.setArguments(bundle);
            FragmentManager fragmentManager = getSupportFragmentManager();
            Fragment curFragment = fragmentManager.findFragmentById(R.id.content_frame);
            if (curFragment != null) {
                fragmentManager.beginTransaction().remove(curFragment);
            }
            fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).addToBackStack(fragment.getTag()).commit();
        }
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        DialogUtil.showConfirmDialog(HomeActivity.this, null, getString(R.string.log_out_confirm_message), getString(R.string.yes_lbl), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishAffinity();
            }
        }, getString(R.string.no_lbl), false);
    }
}
