package vn.winwindeal.android.app;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import vn.winwindeal.android.app.adapter.OrderListAdapter;
import vn.winwindeal.android.app.model.Product;
import vn.winwindeal.android.app.model.UserInfo;
import vn.winwindeal.android.app.util.CommonUtil;

public class OrderActivity extends BaseActivity implements View.OnClickListener{

    Toolbar toolbar;
    private EditText mAddressEdt;
    private RecyclerView mRecyclerView;
    private OrderListAdapter mAdapter;
    private ArrayList<Product> mProducts;
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

        mProducts = new ArrayList<>();
        mAddressEdt = (EditText) findViewById(R.id.addressEdt);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        DividerItemDecoration dividerDecor = new DividerItemDecoration(OrderActivity.this, DividerItemDecoration.VERTICAL);
        dividerDecor.setDrawable(CommonUtil.getResourceDrawable(OrderActivity.this, R.drawable.divider));
        mRecyclerView.addItemDecoration(dividerDecor);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(OrderActivity.this);
        mRecyclerView.setLayoutManager(layoutManager);
        HashMap<String, String> map = GlobalSharedPreference.getProductOrder(this);
        try {
            JSONObject json = new JSONObject(map.get(Constant.ORDER));
            JSONArray jsonArray = json.optJSONArray(Constant.JSON_TAG_ORDER);
            for (int i = 0; i < jsonArray.length(); i++) {
                Product p = new Product(jsonArray.optJSONObject(i));
                mProducts.add(p);
            }
            mAdapter = new OrderListAdapter(this, mProducts, new JSONObject(map.get(Constant.QUANTITY)));
            mRecyclerView.setAdapter(mAdapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ui = GlobalSharedPreference.getUserInfo(this);
        if (ui.address != null && !ui.address.equals("")) {
            mAddressEdt.setText(ui.address);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (mAdapter.isEdit) {
                    new SaveOrderAsyncTask().execute();
                } else {
                    finish();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
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
            GlobalSharedPreference.saveProductOrder(OrderActivity.this, map);
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

                break;
        }
    }
}
