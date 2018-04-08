package vn.winwindeal.android.app.customview;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import vn.winwindeal.android.app.R;

/**
 * Created by nhannguyen on 4/5/2018.
 */

public class MyProgressDialog extends AlertDialog{
    public MyProgressDialog(Context context) {
        super(context);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        setCancelable(false);
    }

    @Override
    public void show() {
        super.show();
        setContentView(R.layout.popup);
    }
}
