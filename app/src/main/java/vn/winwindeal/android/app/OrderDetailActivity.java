package vn.winwindeal.android.app;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import vn.winwindeal.android.app.adapter.CartListAdapter;
import vn.winwindeal.android.app.adapter.OrderDetailAdapter;
import vn.winwindeal.android.app.model.Product;
import vn.winwindeal.android.app.model.UserInfo;
import vn.winwindeal.android.app.network.DataLoader;
import vn.winwindeal.android.app.util.CommonUtil;
import vn.winwindeal.android.app.util.FontUtil;
import vn.winwindeal.android.app.webservice.OrderDetailWS;
import vn.winwindeal.android.app.webservice.SearchUserWS;

/**
 * Created by nhannguyen on 4/26/2018.
 */

public class OrderDetailActivity extends BaseActivity implements DataLoader.DataLoaderInterface{

    Toolbar toolbar;
    private EditText mAddressEdt;
    private RecyclerView mRecyclerView;
    private CartListAdapter mAdapter;
    private ArrayList<Product> mProducts;
    private OrderDetailWS mOrderDetailWs;
    private SearchUserWS mSearchUserWs;
    UserInfo ui;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_activity);
        initComponents();
    }

    private void initComponents() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        SpannableString ss = new SpannableString(getResources().getString(R.string.order_detail_lbl));
        ss.setSpan(new RelativeSizeSpan(0.85f), 0, ss.length(), 0);
        ss.setSpan(FontUtil.getFontAssets(this, FontUtil.ROBOTO_MEDIUM), 0, ss.length(), 0);
        setTitle(ss);

        mProducts = new ArrayList<>();
        mAddressEdt = (EditText) findViewById(R.id.addressEdt);
        mAddressEdt.setEnabled(false);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        DividerItemDecoration dividerDecor = new DividerItemDecoration(OrderDetailActivity.this, DividerItemDecoration.VERTICAL);
        dividerDecor.setDrawable(CommonUtil.getResourceDrawable(OrderDetailActivity.this, R.drawable.divider));
        mRecyclerView.addItemDecoration(dividerDecor);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(OrderDetailActivity.this);
        mRecyclerView.setLayoutManager(layoutManager);
//        HashMap<String, String> map = GlobalSharedPreference.getProductOrder(this);
//        try {
//            JSONObject json = new JSONObject(map.get(Constant.ORDER));
//            JSONArray jsonArray = json.optJSONArray(Constant.JSON_TAG_ORDER);
//            for (int i = 0; i < jsonArray.length(); i++) {
//                Product p = new Product(jsonArray.optJSONObject(i));
//                mProducts.add(p);
//            }
//            mAdapter = new CartListAdapter(this, mProducts, new JSONObject(map.get(Constant.QUANTITY)));
//            mRecyclerView.setAdapter(mAdapter);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
        int orderId = getIntent().getIntExtra("order_id", -1);
        String address = getIntent().getStringExtra("address");
        mAddressEdt.setText(address);
        mOrderDetailWs = new OrderDetailWS(this);
        mOrderDetailWs.doSearchOrder(orderId);
        mSearchUserWs = new SearchUserWS(this);

//        mSearchUserWs.doSearch(null, jsonArray.put(GlobalSharedPreference.getUserInfo(this).user_id));
        showLoading();

        findViewById(R.id.confirmOrderBtn).setVisibility(View.GONE);
    }

    @Override
    public void loadDataDone(int requestIndex, int resultCode, Object result) {
        hideLoading();
        switch (requestIndex) {
            case Constant.REQUEST_API_ORDER_DETAIL:
                JSONArray jarray = ((JSONObject) result).optJSONArray("data");
                if (jarray != null && jarray.length() > 0) {
                    for (int i =0 ; i < jarray.length(); i++) {
                        Product p = new Product(jarray.optJSONObject(i));
                        mProducts.add(p);
                    }
                    OrderDetailAdapter adapter = new OrderDetailAdapter(OrderDetailActivity.this, mProducts);
                    mRecyclerView.setAdapter(adapter);
                }
            break;
        }
    }

    @Override
    public void loadDataFail(int requestIndex, int resultCode, Object result) {
        hideLoading();
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
}
