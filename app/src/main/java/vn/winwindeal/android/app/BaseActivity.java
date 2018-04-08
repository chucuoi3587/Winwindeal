package vn.winwindeal.android.app;

import android.support.v7.app.AppCompatActivity;

import vn.winwindeal.android.app.customview.MyProgressDialog;

/**
 * Created by nhannguyen on 4/5/2018.
 */

public class BaseActivity extends AppCompatActivity {

    MyProgressDialog progressDialog;

    public void hideLoading() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    public boolean isLoading() {
        if (progressDialog == null) {
            return false;
        }
        return progressDialog.isShowing();
    }

    public void showLoading() {
        if (progressDialog == null) {
            progressDialog = new MyProgressDialog(this);
        }
        progressDialog.show();
    }
}
