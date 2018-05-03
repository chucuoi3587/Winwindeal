package vn.winwindeal.android.app;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import vn.winwindeal.android.app.adapter.CartListAdapter;
import vn.winwindeal.android.app.model.Product;
import vn.winwindeal.android.app.model.UserInfo;
import vn.winwindeal.android.app.network.DataLoader;
import vn.winwindeal.android.app.util.CommonUtil;
import vn.winwindeal.android.app.util.DialogUtil;
import vn.winwindeal.android.app.util.FontUtil;
import vn.winwindeal.android.app.webservice.AddOrderWS;
import vn.winwindeal.android.app.webservice.SearchUserWS;

public class CartActivity extends BaseActivity implements View.OnClickListener, DataLoader.DataLoaderInterface{

    Toolbar toolbar;
    private EditText mAddressEdt;
    private TextView mTotalTv;
    private RecyclerView mRecyclerView;
    private CartListAdapter mAdapter;
    private ArrayList<Product> mProducts;
    private AddOrderWS mAddOrderWs;
    private SearchUserWS mSearchUserWs;
    private double mTotal;
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
        DividerItemDecoration dividerDecor = new DividerItemDecoration(CartActivity.this, DividerItemDecoration.VERTICAL);
        dividerDecor.setDrawable(CommonUtil.getResourceDrawable(CartActivity.this, R.drawable.divider));
        mRecyclerView.addItemDecoration(dividerDecor);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(CartActivity.this);
        mRecyclerView.setLayoutManager(layoutManager);
        HashMap<String, String> map = GlobalSharedPreference.getProductOrder(this);
        try {
            JSONObject json = new JSONObject(map.get(Constant.ORDER));
            JSONObject jsonQtt = new JSONObject(map.get(Constant.QUANTITY));
            JSONArray jsonArray = json.optJSONArray(Constant.JSON_TAG_ORDER);
            for (int i = 0; i < jsonArray.length(); i++) {
                Product p = new Product(jsonArray.optJSONObject(i));
                mProducts.add(p);
                mTotal += p.price * jsonQtt.getInt(String.valueOf(p.product_id));
            }
            mAdapter = new CartListAdapter(this, mProducts, jsonQtt, mListener);
            mRecyclerView.setAdapter(mAdapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mTotalTv = (TextView) findViewById(R.id.totalTv);
        mTotalTv.setTypeface(FontUtil.getFontAssets(this, FontUtil.ROBOTO_MEDIUM));
        mTotalTv.setText(String.format(getString(R.string.total_price_lbl), CommonUtil.parseNumberToString(mTotal, "###.##")));
        mAddOrderWs = new AddOrderWS(this);
        mSearchUserWs = new SearchUserWS(this);
        JSONArray jsonArray = new JSONArray();
        mSearchUserWs.doSearch(null, jsonArray.put(GlobalSharedPreference.getUserInfo(this).user_id), null);
        showLoading();

        findViewById(R.id.confirmOrderBtn).setOnClickListener(this );
    }

    private CartListAdapter.CartListQuantityListener mListener = new CartListAdapter.CartListQuantityListener() {
        @Override
        public void onChangeQuantity(double price, boolean isMinus) {
            if (isMinus) {
                mTotal -= price;
            } else {
                mTotal += price;
            }
            mTotalTv.setText(String.format(getString(R.string.total_price_lbl), CommonUtil.parseNumberToString(mTotal, "###.##")));
        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (mAdapter != null && mAdapter.isEdit) {
                    new SaveOrderAsyncTask().execute();
                } else {
                    finish();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void loadDataDone(int requestIndex, int resultCode, Object result) {
        hideLoading();
        switch (requestIndex) {
            case Constant.REQUEST_API_ORDER_ADD:
                String message = ((JSONObject) result).optString("message", "");
                if (message.equals("successful")) {
                    DialogUtil.showWarningDialog(CartActivity.this, null, getString(R.string.add_order_successfully), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            GlobalSharedPreference.clearProductOrder(CartActivity.this);
                            Intent intent = new Intent(CartActivity.this, HomeActivity.class);
                            startActivity(intent);
                            finishAffinity();
                        }
                    }, Gravity.LEFT, false);
                } else {
                    DialogUtil.showWarningDialog(CartActivity.this, null, getString(R.string.add_order_failed), null, Gravity.LEFT, false);
                }
                break;
            case Constant.REQUEST_API_SEARCH_USER:
                if (result instanceof JSONObject) {
                    JSONArray jarray = ((JSONObject) result).optJSONArray("data");
                    if (jarray != null && jarray.length() > 0) {
                        for (int i = 0; i < jarray.length(); i++) {
                            ui = new UserInfo(jarray.optJSONObject(i));
                        }
                    }
                    if (ui.address != null && !ui.address.equals("")) {
                        mAddressEdt.setText(ui.address);
                    }
                }
                break;
        }
    }

    @Override
    public void loadDataFail(int requestIndex, int resultCode, Object result) {
        hideLoading();
    }

    class SaveOrderAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            ArrayList<Product> products = mAdapter.getProducts();
            JSONArray jarray = new JSONArray();
            for (Product p : products) {
                JSONObject json = p.parseToJson();
                jarray.put(json);
            }
            JSONObject json = new JSONObject();
            try {
                json.accumulate(Constant.JSON_TAG_ORDER, jarray);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            HashMap<String, String> map = new HashMap<>();
            map.put(Constant.ORDER, json.toString());
            map.put(Constant.QUANTITY, mAdapter.getQuantityJson().toString());
            GlobalSharedPreference.saveProductOrder(CartActivity.this, map);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            finish();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.confirmOrderBtn:
                String address = mAddressEdt.getText().toString();
                if (mProducts != null && !mProducts.isEmpty()) {
                    if (!address.equals("")) {
                        showLoading();
                        new CreatOrderAsyncTask().execute();
                    } else {
                        DialogUtil.showWarningDialog(CartActivity.this, null, getString(R.string.address_warning), null, Gravity.LEFT, false);
                    }
                }
                break;
        }
    }

    class CreatOrderAsyncTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            JSONObject jsonQty = mAdapter.getQuantityJson();
            ArrayList<Product> products = mAdapter.getProducts();
            JSONArray jarray = new JSONArray();
            for (Product p : products) {
                try {
                    p.quantity = jsonQty.getInt(String.valueOf(p.product_id));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                JSONObject json = p.parseToJson();
                jarray.put(json);
            }
            String address = mAddressEdt.getText().toString();
            mAddOrderWs.doAddOrder(address, ui.phone, jarray);
            return null;
        }
    }
}
