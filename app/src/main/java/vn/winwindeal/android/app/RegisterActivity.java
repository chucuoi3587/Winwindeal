package vn.winwindeal.android.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import vn.winwindeal.android.app.network.DataLoader;
import vn.winwindeal.android.app.util.DialogUtil;
import vn.winwindeal.android.app.util.FontUtil;
import vn.winwindeal.android.app.webservice.RegisterWS;

/**
 * Created by nhannguyen on 4/6/2018.
 */

public class RegisterActivity extends BaseActivity implements View.OnClickListener, DataLoader.DataLoaderInterface{

    Toolbar toolbar;
    private EditText mEmailEdt, mUserNameEdt, mPasswordEdt, mAddressEdt, mPhoneEdt;
    private RegisterWS mRegisterWs;
    boolean isLock = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);
        initComponents();
    }

    private void initComponents() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        SpannableString ss = new SpannableString(getResources().getString(R.string.create_account_lbl));
        ss.setSpan(new RelativeSizeSpan(0.85f), 0, ss.length(), 0);
        ss.setSpan(FontUtil.getFontAssets(this, FontUtil.ROBOTO_MEDIUM), 0, ss.length(), 0);
        setTitle(ss);

        mEmailEdt = (EditText) findViewById(R.id.emailEdt);
        mUserNameEdt = (EditText) findViewById(R.id.userNameEdt);
        mPasswordEdt = (EditText) findViewById(R.id.passEdt);
        mAddressEdt = (EditText) findViewById(R.id.addressEdt);
        mPhoneEdt = (EditText) findViewById(R.id.phoneEdt);
        mRegisterWs = new RegisterWS(this);
        findViewById(R.id.registerBtn).setOnClickListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        if (isLock) {
            return;
        }
        switch (view.getId()) {
            case R.id.registerBtn:
                String strEmail = mEmailEdt.getText().toString().trim();
                String strUserName = mUserNameEdt.getText().toString().trim();
                String strPass = mPasswordEdt.getText().toString().trim();
                String strAddress = mAddressEdt.getText().toString().trim();
                String strPhone = mPhoneEdt.getText().toString().trim();
                if (!strUserName.equals("") && !strEmail.equals("") && !strPass.equals("") &&!strAddress.equals("")&&!strPhone.equals("")) {
                    mRegisterWs.doRegister(strEmail, strUserName, strPass, strAddress);
                    showLoading();
                }
                break;
        }
    }

    @Override
    public void loadDataDone(int requestIndex, int resultCode, Object result) {
        hideLoading();
        switch (requestIndex) {
            case Constant.REQUEST_API_REGISTER:
                DialogUtil.showWarningDialog(RegisterActivity.this, "", getResources().getString(R.string.register_successfully),
                        new View.OnClickListener(){

                            @Override
                            public void onClick(View view) {
                                finish();
                            }
                        }, Gravity.LEFT, false);
                break;
        }
    }

    @Override
    public void loadDataFail(int requestIndex, int resultCode, Object result) {
        hideLoading();

    }
}
