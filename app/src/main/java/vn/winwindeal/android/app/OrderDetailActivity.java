package vn.winwindeal.android.app;

import android.content.Intent;
import android.net.Uri;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import vn.winwindeal.android.app.adapter.CartListAdapter;
import vn.winwindeal.android.app.adapter.OrderDetailAdapter;
import vn.winwindeal.android.app.model.Order;
import vn.winwindeal.android.app.model.Product;
import vn.winwindeal.android.app.model.UserInfo;
import vn.winwindeal.android.app.network.DataLoader;
import vn.winwindeal.android.app.util.CommonUtil;
import vn.winwindeal.android.app.util.DialogUtil;
import vn.winwindeal.android.app.util.FontUtil;
import vn.winwindeal.android.app.webservice.EditOrderWS;
import vn.winwindeal.android.app.webservice.OrderDetailWS;
import vn.winwindeal.android.app.webservice.SearchUserWS;

/**
 * Created by nhannguyen on 4/26/2018.
 */

public class OrderDetailActivity extends BaseActivity implements DataLoader.DataLoaderInterface, View.OnClickListener{

    Toolbar toolbar;
    private EditText mAddressEdt;
    private TextView mTotalTv;
    private RecyclerView mRecyclerView;
    private CartListAdapter mAdapter;
    private ArrayList<Product> mProducts;
    private Order mOrder;
    private OrderDetailWS mOrderDetailWs;
    private EditOrderWS mEditOrderWs;
    private int mOrderStt = -1;
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

        mTotalTv = (TextView) findViewById(R.id.totalTv);
        mTotalTv.setTypeface(FontUtil.getFontAssets(this, FontUtil.ROBOTO_MEDIUM));
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
        ui = GlobalSharedPreference.getUserInfo(this);
        int orderId = getIntent().getIntExtra("order_id", -1);
        mOrderDetailWs = new OrderDetailWS(this);
        mOrderDetailWs.doSearchOrder(orderId);
        mEditOrderWs = new EditOrderWS(this);

//        mSearchUserWs.doSearch(null, jsonArray.put(GlobalSharedPreference.getUserInfo(this).user_id));
        showLoading();

        findViewById(R.id.confirmOrderBtn).setVisibility(View.GONE);
        findViewById(R.id.newLayout).setOnClickListener(this);
        findViewById(R.id.processingLayout).setOnClickListener(this);
        findViewById(R.id.doneLayout).setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (ui.user_type == 1 || ui.user_type == 2) {
            getMenuInflater().inflate(R.menu.order_detail_header_menu, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void loadDataDone(int requestIndex, int resultCode, Object result) {
        hideLoading();
        switch (requestIndex) {
            case Constant.REQUEST_API_ORDER_DETAIL:
                JSONArray jarray = ((JSONObject) result).optJSONObject("data").optJSONArray("details");
                if (jarray != null && jarray.length() > 0) {
                    for (int i =0 ; i < jarray.length(); i++) {
                        Product p = new Product(jarray.optJSONObject(i));
                        mProducts.add(p);
                    }
                    OrderDetailAdapter adapter = new OrderDetailAdapter(OrderDetailActivity.this, mProducts);
                    mRecyclerView.setAdapter(adapter);
                }
                mOrder = new Order(((JSONObject) result).optJSONObject("data").optJSONObject("info"));
                mAddressEdt.setText(mOrder.address);
                if (ui.user_type == 1 || ui.user_type == 2) {
                    findViewById(R.id.statusLayout).setVisibility(View.VISIBLE);
                    ((TextView) findViewById(R.id.newLayout).findViewById(R.id.sttTv)).setText(getString(R.string.order_stt_new));
                    ((TextView) findViewById(R.id.processingLayout).findViewById(R.id.sttTv)).setText(getString(R.string.order_stt_processing));
                    ((TextView) findViewById(R.id.doneLayout).findViewById(R.id.sttTv)).setText(getString(R.string.order_stt_done));
                    if (mOrder.order_status_id == 1) {
                        findViewById(R.id.doneLayout).setEnabled(false);
                        findViewById(R.id.doneLayout).setAlpha(0.4f);
                    } else if (mOrder.order_status_id == 2) {
                        findViewById(R.id.newLayout).setEnabled(false);
                        findViewById(R.id.newLayout).setAlpha(0.4f);
                    } else {
                        findViewById(R.id.newLayout).setEnabled(false);
                        findViewById(R.id.processingLayout).setEnabled(false);
                        findViewById(R.id.newLayout).setAlpha(0.4f);
                        findViewById(R.id.processingLayout).setAlpha(0.4f);
                    }
                    changeSttLayout(mOrder.order_status_id);
                }
                mTotalTv.setText(String.format(getString(R.string.total_price_lbl), CommonUtil.parseNumberToString(mOrder.total_money, "###.##")));
            break;
            case Constant.REQUEST_API_ORDER_EDIT:
                String message = ((JSONObject) result).optString("message", "");
                if (!message.equals("failed")) {
                    DialogUtil.showWarningDialog(OrderDetailActivity.this, null, getString(R.string.update_user_successfully), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            setResult(RESULT_OK);
                            finish();
                        }
                    }, Gravity.LEFT, false);
                } else {
                    String strResult = "";
                    JSONArray jsonArray = ((JSONObject) result).optJSONObject("data").optJSONArray("products");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        String mess = jsonArray.optJSONObject(i).optJSONObject("result").optString("mess", "");
                        if (mess.equals("product_not_found")) {
                            if (strResult.equals("")) {
                                strResult = jsonArray.optJSONObject(i).optString("name", "");
                            } else {
                                strResult += ", " + jsonArray.optJSONObject(i).optString("name", "");
                            }
                        }
                    }
                    if (!strResult.equals("")) {
                        strResult += " " + getString(R.string.update_order_failed);
                        DialogUtil.showWarningDialog(OrderDetailActivity.this, null, strResult, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                finish();
                            }
                        }, Gravity.LEFT, false);
                    }
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
            case R.id.action_save:
                if (mOrderStt != mOrder.order_status_id) {
                    showLoading();
                    new UpdateOrderAsyncTask().execute();
                }
                break;
            case R.id.action_phone:
                if (mOrder != null) {
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + mOrder.phone));
                    startActivity(intent);
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void changeSttLayout(int status) {
        switch (mOrderStt) {
            case 1:
                ((ImageView) findViewById(R.id.newLayout).findViewById(R.id.radioIcon)).setImageResource(R.drawable.radio_off);
                ((TextView) findViewById(R.id.newLayout).findViewById(R.id.sttTv)).setTextColor(CommonUtil.getColor(this, R.color.colorSubTextView));
                break;
            case 2:
                ((ImageView) findViewById(R.id.processingLayout).findViewById(R.id.radioIcon)).setImageResource(R.drawable.radio_off);
                ((TextView) findViewById(R.id.processingLayout).findViewById(R.id.sttTv)).setTextColor(CommonUtil.getColor(this, R.color.colorSubTextView));
                break;
            case 3:
                ((ImageView) findViewById(R.id.doneLayout).findViewById(R.id.radioIcon)).setImageResource(R.drawable.radio_off);
                ((TextView) findViewById(R.id.doneLayout).findViewById(R.id.sttTv)).setTextColor(CommonUtil.getColor(this, R.color.colorSubTextView));
                break;
        }
        mOrderStt = status;
        switch (status) {
            case 1:
                ((ImageView) findViewById(R.id.newLayout).findViewById(R.id.radioIcon)).setImageResource(R.drawable.radio_red);
                ((TextView) findViewById(R.id.newLayout).findViewById(R.id.sttTv)).setTextColor(CommonUtil.getColor(this, R.color.colorRed));
                break;
            case 2:
                ((ImageView) findViewById(R.id.processingLayout).findViewById(R.id.radioIcon)).setImageResource(R.drawable.radio_green);
                ((TextView) findViewById(R.id.processingLayout).findViewById(R.id.sttTv)).setTextColor(CommonUtil.getColor(this, R.color.colorGreenPhone));
                break;
            case 3:
                ((ImageView) findViewById(R.id.doneLayout).findViewById(R.id.radioIcon)).setImageResource(R.drawable.radio_blue);
                ((TextView) findViewById(R.id.doneLayout).findViewById(R.id.sttTv)).setTextColor(CommonUtil.getColor(this, R.color.colorDarkBlue));
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.newLayout:
                changeSttLayout(1);
                break;
            case R.id.processingLayout:
                changeSttLayout(2);
                break;
            case R.id.doneLayout:
                changeSttLayout(3);
                break;
        }
    }

    class UpdateOrderAsyncTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            JSONArray jarray = new JSONArray();
            for (Product p : mProducts) {
                JSONObject json = p.parseToJson();
                jarray.put(json);
            }
            mEditOrderWs.doEditOrder(mOrder.address, mOrder.phone, jarray, mOrder.order_id, mOrderStt);
            return null;
        }
    }
}
