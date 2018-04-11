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
        SpinnerObj[] typeObjs = new SpinnerObj[2];
        typeObjs[0] = new SpinnerObj(2, getResources().getString(R.string.staff_lbl));
        typeObjs[1] = new SpinnerObj(3, getResources().getString(R.string.customer_lbl));
        typeAdapter = new SpinnerAdapter(this, android.R.layout.simple_list_item_1, typeObjs);
        mTypeSpinner.setAdapter(typeAdapter);

        mDistrictSpinner = (Spinner) findViewById(R.id.districtSpinner);
        SpinnerObj[] districtObjs = new SpinnerObj[16];
        districtObjs[0] = new SpinnerObj(1, "Quận 1");
        districtObjs[1] = new SpinnerObj(2, "Quận 2");
        districtObjs[2] = new SpinnerObj(3, "Quận 3");
        districtObjs[3] = new SpinnerObj(4, "Quận 4");
        districtObjs[4] = new SpinnerObj(5, "Quận 5");
        districtObjs[5] = new SpinnerObj(6, "Quận 6");
        districtObjs[6] = new SpinnerObj(7, "Quận 7");
        districtObjs[7] = new SpinnerObj(8, "Quận 8");
        districtObjs[8] = new SpinnerObj(9, "Quận 9");
        districtObjs[9] = new SpinnerObj(10, "Quận 10");
        districtObjs[10] = new SpinnerObj(11, "Quận 11");
        districtObjs[11] = new SpinnerObj(12, "Quận 12");
        districtObjs[12] = new SpinnerObj(13, "Quận Bình Thạnh");
        districtObjs[13] = new SpinnerObj(14, "Quận Phú Nhuận");
        districtObjs[14] = new SpinnerObj(15, "Quận Gò Vấp");
        districtObjs[15] = new SpinnerObj(16, "Quận Tân Phú");
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
