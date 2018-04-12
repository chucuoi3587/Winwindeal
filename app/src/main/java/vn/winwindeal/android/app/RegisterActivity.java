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
import android.widget.Spinner;

import org.json.JSONException;
import org.json.JSONObject;

import vn.winwindeal.android.app.adapter.SpinnerAdapter;
import vn.winwindeal.android.app.model.SpinnerObj;
import vn.winwindeal.android.app.network.DataLoader;
import vn.winwindeal.android.app.util.DialogUtil;
import vn.winwindeal.android.app.util.FontUtil;
import vn.winwindeal.android.app.webservice.RegisterWS;

/**
 * Created by nhannguyen on 4/6/2018.
 */

public class RegisterActivity extends BaseActivity implements View.OnClickListener, DataLoader.DataLoaderInterface{

    Toolbar toolbar;
    private EditText mEmailEdt, mPasswordEdt, mAddressEdt, mPhoneEdt;
    private RegisterWS mRegisterWs;
    private Spinner mTypeSpinner, mDistrictSpinner;
    boolean isLock = false;
    SpinnerAdapter typeAdapter;
    SpinnerAdapter districtAdapter;


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
        mPasswordEdt = (EditText) findViewById(R.id.passEdt);
        mAddressEdt = (EditText) findViewById(R.id.addressEdt);
        mPhoneEdt = (EditText) findViewById(R.id.phoneEdt);
        mRegisterWs = new RegisterWS(this);
        findViewById(R.id.registerBtn).setOnClickListener(this);

        mTypeSpinner = (Spinner) findViewById(R.id.typeSpinner);
        SpinnerObj[] typeObjs = new SpinnerObj[3];
        try {
            JSONObject rolesJson = new JSONObject(GlobalSharedPreference.getRoles(this));
            for (int i = 0; i <= 2; i++) {
                typeObjs[i] = new SpinnerObj(i + 1, rolesJson.optString(String.valueOf(i + 1)));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        typeAdapter = new SpinnerAdapter(this, android.R.layout.simple_list_item_1, typeObjs);
        mTypeSpinner.setAdapter(typeAdapter);

        mDistrictSpinner = (Spinner) findViewById(R.id.districtSpinner);
        SpinnerObj[] districtObjs = new SpinnerObj[17];
        JSONObject districtJson;
        try {
            districtJson = new JSONObject(GlobalSharedPreference.getDistricts(this));
            for (int i = 0; i <= 16; i++) {
                districtObjs[i] = new SpinnerObj(i, districtJson.optString(String.valueOf(i)));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        districtAdapter = new SpinnerAdapter(this, android.R.layout.simple_list_item_1, districtObjs);
        mDistrictSpinner.setAdapter(districtAdapter);
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
                String strPass = mPasswordEdt.getText().toString().trim();
                String strAddress = mAddressEdt.getText().toString().trim();
                String strPhone = mPhoneEdt.getText().toString().trim();
                SpinnerObj typeUserObj = (SpinnerObj) mTypeSpinner.getSelectedItem();
                SpinnerObj districtObj = (SpinnerObj) mDistrictSpinner.getSelectedItem();
                if (!strEmail.equals("") && !strPass.equals("") &&!strAddress.equals("")&&!strPhone.equals("")) {
                    mRegisterWs.doRegister(strEmail, strPass, strAddress, strPhone, typeUserObj.key, districtObj.key);
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
