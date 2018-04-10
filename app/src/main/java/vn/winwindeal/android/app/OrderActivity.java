package vn.winwindeal.android.app;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import vn.winwindeal.android.app.adapter.OrderListAdapter;
import vn.winwindeal.android.app.model.Product;

public class OrderActivity extends BaseActivity implements View.OnClickListener{

    Toolbar toolbar;
    private EditText mAddressEdt;
    private RecyclerView mLv;
    private OrderListAdapter mAdapter;
    private ArrayList<Product> mProducts;

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
        mAddressEdt = (EditText) findViewById(R.id.addressEdt);
        mLv = (RecyclerView) findViewById(R.id.recycler_view);
        String strOrder = GlobalSharedPreference.getProductOrder(this);
        try {
            JSONObject json = new JSONObject(strOrder);
            JSONArray jsonArray = json.optJSONArray(Constant.JSON_TAG_ORDER);
            mProducts = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                Product p = new Product(jsonArray.optJSONObject(i));
                mProducts.add(p);
            }
            mAdapter = new OrderListAdapter(this, mProducts);
            mLv.setAdapter(mAdapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }

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
        switch (view.getId()) {
            case R.id.confirmOrderBtn:

                break;
        }
    }
}
