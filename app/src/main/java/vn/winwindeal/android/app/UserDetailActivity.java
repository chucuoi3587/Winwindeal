package vn.winwindeal.android.app;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;
import vn.winwindeal.android.app.adapter.SpinnerAdapter;
import vn.winwindeal.android.app.model.SpinnerObj;
import vn.winwindeal.android.app.model.UserInfo;
import vn.winwindeal.android.app.network.DataLoader;
import vn.winwindeal.android.app.util.CommonUtil;
import vn.winwindeal.android.app.util.DialogUtil;
import vn.winwindeal.android.app.util.FontUtil;
import vn.winwindeal.android.app.webservice.SearchUserWS;
import vn.winwindeal.android.app.webservice.UpdateUserWS;

public class UserDetailActivity extends BaseActivity implements DataLoader.DataLoaderInterface, View.OnClickListener{

    Toolbar toolbar;
    private UserInfo mUser;
    private SearchUserWS mSearchUserWs;
    private UpdateUserWS mUpdateUserWs;
    private CircleImageView mAvatar;
    private EditText mEmailEdt, mAddressEdt, mPhoneEdt;
    private Spinner mDistrictSpinner;
    private String mAvatarPath = "";
    SpinnerAdapter districtAdapter;
    boolean isLock = false;
    boolean isEditable = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_detail_activity);
        initComponents();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.product_header_menu, menu);
        menu.findItem(R.id.action_delete).setVisible(false);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_save:
                doUpdate();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initComponents() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        isEditable = getIntent().getBooleanExtra("is_editable", false);

        ((TextView) findViewById(R.id.accStatusTitle)).setTypeface(FontUtil.getFontAssets(this, FontUtil.ROBOTO_MEDIUM));
        ((TextView) findViewById(R.id.accStatus)).setTypeface(FontUtil.getFontAssets(this, FontUtil.ROBOTO_REGULAR));
        mAvatar = (CircleImageView) findViewById(R.id.avatarImgv);
        mEmailEdt = (EditText) findViewById(R.id.emailEdt);
        mAddressEdt = (EditText) findViewById(R.id.addressEdt);
        mPhoneEdt = (EditText) findViewById(R.id.phoneEdt);
        mDistrictSpinner = (Spinner) findViewById(R.id.districtSpinner);

        if (!isEditable) {
            mAvatar.setEnabled(false);
            mEmailEdt.setEnabled(false);
            mAddressEdt.setEnabled(false);
            mPhoneEdt.setEnabled(false);
            mDistrictSpinner.setEnabled(false);
        }

        mSearchUserWs = new SearchUserWS(this);
        mUpdateUserWs = new UpdateUserWS(this);
        mUser = getIntent().getParcelableExtra("user");
        if (mUser == null) {
            int userId = getIntent().getIntExtra("user_id", -1);
            if (userId != -1) {
                JSONArray jsonArray = new JSONArray();
                mSearchUserWs.doSearch(null, jsonArray.put(userId));
                showLoading();
            }
        }

        mAvatar.setOnClickListener(this);
        UserInfo curUser = GlobalSharedPreference.getUserInfo(this);
        if (curUser.user_type == 1) {

        } else {
            findViewById(R.id.statusLayout).setVisibility(View.GONE);
        }
        findViewById(R.id.sttToggle).setOnClickListener(this);
    }

    private void doUpdate() {
        String email = mEmailEdt.getText().toString().trim();
        String address = mAddressEdt.getText().toString().trim();
        String phone = mPhoneEdt.getText().toString().trim();
        int district_id = ((SpinnerObj) mDistrictSpinner.getSelectedItem()).key;
        if (!email.equals("") && !address.equals("") && !phone.equals("") && district_id > 0) {
            mUpdateUserWs.doUpdateUser(email, address, phone, district_id, mAvatarPath, mUser.user_id, mUser.status);
            showLoading();
        }
    }

    @Override
    public void loadDataDone(int requestIndex, int resultCode, final Object result) {
        hideLoading();
        switch (requestIndex) {
            case Constant.REQUEST_API_SEARCH_USER:
                if (result instanceof JSONObject) {
                    JSONArray jarray = ((JSONObject) result).optJSONArray("data");
                    if (jarray != null && jarray.length() > 0) {
                        for (int i = 0; i < jarray.length(); i++) {
                            mUser = new UserInfo(jarray.optJSONObject(i));
                        }
                    }
                    renderData();
                }
                break;
            case Constant.REQUEST_API_EDIT_USER:
                String message = ((JSONObject) result).optString("message");
                if (message.equals("uploaded is unsuccessful")) {
                    DialogUtil.showWarningDialog(UserDetailActivity.this, null, getString(R.string.update_user_failed), null, Gravity.LEFT, false);
                } else {
                    DialogUtil.showWarningDialog(UserDetailActivity.this, null, getString(R.string.update_user_successfully), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            UserInfo ui = new UserInfo(((JSONObject) result).optJSONArray("data").optJSONObject(0));
                            Intent intent = new Intent();
                            intent.putExtra("user", ui);
                            setResult(RESULT_OK, intent);
                            finish();
                        }
                    }, Gravity.LEFT,false);
                }
                break;
        }
    }

    @Override
    public void loadDataFail(int requestIndex, int resultCode, Object result) {
        hideLoading();
        switch (requestIndex) {
            case Constant.REQUEST_NETWORK_FAILED:
                isLock = true;
                DialogUtil.showNetworkDialogWarning(UserDetailActivity.this, new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        isLock = false;
                    }
                }, Gravity.LEFT, false);
                break;
            case Constant.REQUEST_API_EDIT_USER:

                break;
        }
    }

    private void renderData() {
        if (mUser != null) {
            if (findViewById(R.id.statusLayout).getVisibility() == View.VISIBLE) {
                if (mUser.status == 0) {
                    ((ImageView) findViewById(R.id.sttToggle)).setImageResource(R.drawable.toggle_off);
                    ((TextView) findViewById(R.id.accStatus)).setText(getString(R.string.inactive));
                } else {
                    ((ImageView) findViewById(R.id.sttToggle)).setImageResource(R.drawable.toggle_on);
                    ((TextView) findViewById(R.id.accStatus)).setText(getString(R.string.active));
                }
            }
            if (!mUser.avatar.equals("") && !mUser.avatar.equals("null")) {
                Glide.with(this).load(mUser.avatar).into(mAvatar);
            }
            mEmailEdt.setText(mUser.email);
            mAddressEdt.setText(mUser.address);
            mPhoneEdt.setText(mUser.phone);
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
            mDistrictSpinner.setSelection(mUser.district_id);
        }
    }

    @Override
    public void onClick(View v) {
        if (isLock) {
            return;
        }
        switch (v.getId()) {
            case R.id.avatarImgv:
                Intent intent = new Intent(UserDetailActivity.this, MyCropImageActivity.class);
                startActivityForResult(intent, Constant.REQUEST_GET_IMAGE_CHOOSER_INTENT);
                break;
            case R.id.sttToggle:
                if (mUser.status == 0) {
                    mUser.status = 1;
                    ((ImageView) findViewById(R.id.sttToggle)).setImageResource(R.drawable.toggle_on);
                    ((TextView) findViewById(R.id.accStatus)).setText(getString(R.string.active));
                    ((TextView) findViewById(R.id.accStatus)).setTextColor(CommonUtil.getColor(UserDetailActivity.this, R.color.colorDarkBlue));
                } else {
                    mUser.status = 0;
                    ((ImageView) findViewById(R.id.sttToggle)).setImageResource(R.drawable.toggle_off);
                    ((TextView) findViewById(R.id.accStatus)).setText(getString(R.string.inactive));
                    ((TextView) findViewById(R.id.accStatus)).setTextColor(CommonUtil.getColor(UserDetailActivity.this, R.color.colorSubTextView));
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Constant.REQUEST_GET_IMAGE_CHOOSER_INTENT:
                if (resultCode == RESULT_OK) {
                    mAvatarPath = data.getStringExtra("path");
                    Bitmap bmp = BitmapFactory.decodeFile(mAvatarPath);
                    if (bmp != null) {
                        mAvatar.setImageBitmap(bmp);
                    }
                }
                break;
        }
    }
}
