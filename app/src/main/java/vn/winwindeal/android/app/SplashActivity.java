package vn.winwindeal.android.app;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import vn.winwindeal.android.app.BaseActivity;
import vn.winwindeal.android.app.model.UserInfo;

/**
 * Created by nhannguyen on 4/5/2018.
 */

public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);
        if (android.os.Build.VERSION.SDK_INT >= 21){
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.fade_in_fade_out);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                UserInfo ui = GlobalSharedPreference.getUserInfo(SplashActivity.this);
                if (ui.user_id != null && ui.user_id != -1) {
                    Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
                    intent.putExtra(Constant.SELECTED_PAGE, Constant.HOME_FRAGMENT);
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        findViewById(R.id.splashLogo).startAnimation(anim);
    }
}
