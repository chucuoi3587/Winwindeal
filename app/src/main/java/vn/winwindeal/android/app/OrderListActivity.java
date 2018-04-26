package vn.winwindeal.android.app;

import android.content.Intent;
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

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import vn.winwindeal.android.app.adapter.OrderListAdapter;
import vn.winwindeal.android.app.model.Order;
import vn.winwindeal.android.app.model.UserInfo;
import vn.winwindeal.android.app.network.DataLoader;
import vn.winwindeal.android.app.util.CommonUtil;
import vn.winwindeal.android.app.util.FontUtil;
import vn.winwindeal.android.app.webservice.SearchOrderWS;

/**
 * Created by nhannguyen on 4/24/2018.
 */

public class OrderListActivity extends BaseActivity implements DataLoader.DataLoaderInterface{

    Toolbar toolbar;
    private SearchOrderWS mSearchWs;
    private ArrayList<Order> mOrders;
    private RecyclerView mRecyclerView;
    private OrderListAdapter mAdapter;
    private UserInfo ui;

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

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
//        DividerItemDecoration dividerDecor = new DividerItemDecoration(OrderListActivity.this, DividerItemDecoration.VERTICAL);
//        dividerDecor.setDrawable(CommonUtil.getResourceDrawable(OrderListActivity.this, R.drawable.divider));
//        mRecyclerView.addItemDecoration(dividerDecor);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(OrderListActivity.this);
        mRecyclerView.setLayoutManager(layoutManager);

        ui = GlobalSharedPreference.getUserInfo(this);

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
                JSONArray jarray = ((JSONObject) result).optJSONArray("data");
                if (mOrders == null) {
                    mOrders = new ArrayList<>();
                }
                if (jarray != null && jarray.length() > 0) {
                    for (int i = 0; i < jarray.length(); i++) {
                        Order o = new Order(jarray.optJSONObject(i));
                        if (o != null) {
                            mOrders.add(o);
                        }
                    }
                    renderData();
                }
                break;
        }
    }

    @Override
    public void loadDataFail(int requestIndex, int resultCode, Object result) {
        hideLoading();
    }

    private void renderData() {
        if (mAdapter == null) {
            mAdapter = new OrderListAdapter(OrderListActivity.this, mOrders, ui.user_type, mListener);
            mRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.notifyDataSetChanged();
        }
    }

    private OrderListAdapter.itemClickListener mListener = new OrderListAdapter.itemClickListener() {
        @Override
        public void onItemClickListener(int position) {
            Order od = mOrders.get(position);
            Intent intent = new Intent(OrderListActivity.this, OrderDetailActivity.class);
            intent.putExtra("order_id", od.order_id);
            intent.putExtra("address", od.address);
            startActivity(intent);
         }

        @Override
        public void onPhoneClickListener(int position) {

        }
    };
}
