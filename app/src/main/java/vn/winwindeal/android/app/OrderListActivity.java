package vn.winwindeal.android.app;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.view.MenuItem;

import org.json.JSONObject;

import vn.winwindeal.android.app.model.UserInfo;
import vn.winwindeal.android.app.network.DataLoader;
import vn.winwindeal.android.app.util.FontUtil;
import vn.winwindeal.android.app.webservice.SearchOrderWS;

/**
 * Created by nhannguyen on 4/24/2018.
 */

public class OrderListActivity extends BaseActivity implements DataLoader.DataLoaderInterface{

    Toolbar toolbar;
    private SearchOrderWS mSearchWs;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_list_activity);
        initComponents();
    }

    private void initComponents() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        SpannableString ss = new SpannableString(getResources().getString(R.string.order_history));
        ss.setSpan(new RelativeSizeSpan(0.85f), 0, ss.length(), 0);
        ss.setSpan(FontUtil.getFontAssets(this, FontUtil.ROBOTO_MEDIUM), 0, ss.length(), 0);
        setTitle(ss);

        mSearchWs = new SearchOrderWS(this);
        UserInfo ui = GlobalSharedPreference.getUserInfo(this);
        mSearchWs.doSearchOrder(null, ui.user_id, null, null);
        showLoading();
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
    public void loadDataDone(int requestIndex, int resultCode, Object result) {
        hideLoading();
        switch (requestIndex) {
            case Constant.REQUEST_API_ORDER_SEARCH:

                break;
        }
    }

    @Override
    public void loadDataFail(int requestIndex, int resultCode, Object result) {
        hideLoading();
    }
}
