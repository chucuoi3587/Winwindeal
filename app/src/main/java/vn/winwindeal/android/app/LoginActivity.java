package vn.winwindeal.android.app;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONObject;

import vn.winwindeal.android.app.model.UserInfo;
import vn.winwindeal.android.app.network.DataLoader;
import vn.winwindeal.android.app.util.DialogUtil;
import vn.winwindeal.android.app.webservice.LoginWS;

/**
 * Created by nhannguyen on 4/5/2018.
 */

public class LoginActivity extends BaseActivity implements View.OnClickListener, DataLoader.DataLoaderInterface{

    private EditText mUnameEdt, mPassEdt;
    private LoginWS mLoginWs;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        initComponents();
    }

    private void initComponents() {
        mUnameEdt = (EditText) findViewById(R.id.unameEdt);
        mPassEdt = (EditText) findViewById(R.id.passEdt);
        findViewById(R.id.loginBtn).setOnClickListener(this);
//        findViewById(R.id.unameLayout).setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if (hasFocus) {
//                    v.setBackgroundResource(R.drawable.box_focus_border);
//                    findViewById(R.id.passLayout).setBackgroundResource(R.drawable.box_defocus_border);
//                } else {
//                    v.setBackgroundResource(R.drawable.box_defocus_border);
//                    findViewById(R.id.passLayout).setBackgroundResource(R.drawable.box_focus_border);
//                }
//            }
//        });
//        findViewById(R.id.passLayout).setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if (hasFocus) {
//                    v.setBackgroundResource(R.drawable.box_focus_border);
//                    findViewById(R.id.unameLayout).setBackgroundResource(R.drawable.box_defocus_border);
//                } else {
//                    v.setBackgroundResource(R.drawable.box_defocus_border);
//                }
//            }
//        });
        mPassEdt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_GO) {
                    doLogin();
                    return true;
                }
                return false;
            }
        });
        ((TextView) findViewById(R.id.registerTv)).setPaintFlags(((TextView) findViewById(R.id.registerTv)).getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        findViewById(R.id.registerTv).setOnClickListener(this);
        mLoginWs = new LoginWS(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.loginBtn:
                doLogin();
                break;
            case R.id.registerTv:
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void doLogin() {
        String strUname = mUnameEdt.getText().toString().trim();
        String strPass = mPassEdt.getText().toString().trim();
        String strError = "";
        if (strUname.equals("")) {
            strError = getResources().getString(R.string.username_empty_warning);
        }
        if (strPass.equals("")) {
            if (strError.equals("")) {
                strError = getResources().getString(R.string.password_empty_warning);
            } else {
                strError += "\n" + getResources().getString(R.string.password_empty_warning);
            }
        }
        if (!strError.equals("")) {
            DialogUtil.showWarningDialog(this, "", strError, null, Gravity.LEFT, false);
        } else {
            // login
            mLoginWs.doLogin(strUname, strPass);
            showLoading();

        }
    }

    @Override
    public void loadDataDone(int requestIndex, int resultCode, Object result) {
        hideLoading();
        switch (requestIndex) {
            case Constant.REQUEST_API_LOGIN:
                UserInfo ui = new UserInfo(((JSONObject) result).optJSONObject("data"));
                GlobalSharedPreference.login(LoginActivity.this, ui);
                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                intent.putExtra(Constant.SELECTED_PAGE, Constant.HOME_FRAGMENT);
                startActivity(intent);
                finish();
                break;
        }
    }

    @Override
    public void loadDataFail(int requestIndex, int resultCode, Object result) {
        hideLoading();
        switch (requestIndex) {
            case Constant.REQUEST_API_LOGIN:
                String message = ((JSONObject) result).optString("message");
                if (message.equals("password_wrong")) {
                    DialogUtil.showWarningDialog(LoginActivity.this, null, getResources().getString(R.string.login_fail),
                            null, Gravity.LEFT, false);
                }
                break;
        }
    }
}
