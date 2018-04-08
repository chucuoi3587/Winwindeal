package vn.winwindeal.android.app;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.MenuItem;

import vn.winwindeal.android.app.fragment.ProductListFragment;

/**
 * Created by nhannguyen on 4/6/2018.
 */

public class HomeActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);
        initComponents();
    }

    private void initComponents() {
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setSelectedItemId(R.id.action_home);
        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_home:
//                        setFragment(Constant.ACTIVITY_FRAGMENT);
                        break;
                    case R.id.action_me:
//                        setFragment(Constant.CREATE_PROOF_PRIVATE_FRAGMENT);
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
        Fragment fragment = null;
        Bundle bundle = new Bundle();
        switch (pos) {
            case Constant.HOME_FRAGMENT:
                fragment = new ProductListFragment();
                break;
//            case Constant.CREATE_PROOF_PRIVATE_FRAGMENT:
//                fragment = new PrivateProofFragment();
//                break;
//            case Constant.CREATE_PROOF_PUBLIC_FRAGMENT:
//
//                break;
//            case Constant.SETTINGS_FRAGMENT:
//                fragment = new SettingsFragment();
//                break;
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
}
