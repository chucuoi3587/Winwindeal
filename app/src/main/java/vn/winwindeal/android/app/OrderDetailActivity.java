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
import android.view.View;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import vn.winwindeal.android.app.adapter.CartListAdapter;
import vn.winwindeal.android.app.model.Product;
import vn.winwindeal.android.app.model.UserInfo;
import vn.winwindeal.android.app.util.CommonUtil;
import vn.winwindeal.android.app.util.FontUtil;
import vn.winwindeal.android.app.webservice.AddOrderWS;
import vn.winwindeal.android.app.webservice.OrderDetailWS;
import vn.winwindeal.android.app.webservice.SearchUserWS;

/**
 * Created by nhannguyen on 4/26/2018.
 */

public class OrderDetailActivity extends BaseActivity {

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
        SpannableString ss = new SpannableString(getResources().getString(R.string.cart_lbl));
        ss.setSpan(new RelativeSizeSpan(0.85f), 0, ss.length(), 0);
        ss.setSpan(FontUtil.getFontAssets(this, FontUtil.ROBOTO_MEDIUM), 0, ss.length(), 0);
        setTitle(ss);

        mProducts = new ArrayList<>();
        mAddressEdt = (EditText) findViewById(R.id.addressEdt);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        DividerItemDecoration dividerDecor = new DividerItemDecoration(OrderDetailActivity.this, DividerItemDecoration.VERTICAL);
        dividerDecor.setDrawable(CommonUtil.getResourceDrawable(OrderDetailActivity.this, R.drawable.divider));
        mRecyclerView.addItemDecoration(dividerDecor);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(OrderDetailActivity.this);
        mRecyclerView.setLayoutManager(layoutManager);
        HashMap<String, String> map = GlobalSharedPreference.getProductOrder(this);
        try {
            JSONObject json = new JSONObject(map.get(Constant.ORDER));
            JSONArray jsonArray = json.optJSONArray(Constant.JSON_TAG_ORDER);
            for (int i = 0; i < jsonArray.length(); i++) {
                Product p = new Product(jsonArray.optJSONObject(i));
                mProducts.add(p);
            }
            mAdapter = new CartListAdapter(this, mProducts, new JSONObject(map.get(Constant.QUANTITY)));
            mRecyclerView.setAdapter(mAdapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mOrderDetailWs = new OrderDetailWS(this);
        mSearchUserWs = new SearchUserWS(this);
        JSONArray jsonArray = new JSONArray();
        mSearchUserWs.doSearch(null, jsonArray.put(GlobalSharedPreference.getUserInfo(this).user_id));
        showLoading();

        findViewById(R.id.confirmOrderBtn).setVisibility(View.GONE);
    }
}
