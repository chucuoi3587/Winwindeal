package vn.winwindeal.android.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import vn.winwindeal.android.app.model.Product;
import vn.winwindeal.android.app.util.CommonUtil;
import vn.winwindeal.android.app.util.FontUtil;

public class ProductDetailActivity extends BaseActivity implements View.OnClickListener{

    Toolbar toolbar;
    private Product mProduct;
    private ImageView mThumbnail;
    private TextView mNameTv, mPriceTv, mDescriptionTv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_detail_activity);
        initComponents();
    }

    private void initComponents() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("");
        mProduct = getIntent().getParcelableExtra("product");
        mThumbnail = (ImageView) findViewById(R.id.thumbnailImgv);
        mNameTv = (TextView) findViewById(R.id.nameTv);
        mPriceTv = (TextView) findViewById(R.id.priceTv);
        mDescriptionTv = (TextView) findViewById(R.id.descriptionTv);

        mNameTv.setTypeface(FontUtil.getFontAssets(this, FontUtil.ROBOTO_MEDIUM));
        mNameTv.setText(mProduct.product_name);
        if (!mProduct.description.equals("null") && !mProduct.description.equals("")) {
            mDescriptionTv.setText(mProduct.description);
        }
        if (mProduct.price > 0) {
            mPriceTv.setText(String.valueOf(mProduct.price));
        } else {
            mPriceTv.setText(getResources().getString(R.string.price_call));
        }
        Glide.with(this).load(mProduct.thumbnail).into(mThumbnail);

        findViewById(R.id.orderBtn).setOnClickListener(this);
        findViewById(R.id.addToCartLayout).setOnClickListener(this);
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
            case R.id.addToCartLayout:
                CommonUtil.addProductToCart(ProductDetailActivity.this, mProduct);
                finish();
                break;
            case R.id.orderBtn:
                CommonUtil.addProductToCart(ProductDetailActivity.this, mProduct);
                Intent intent = new Intent(ProductDetailActivity.this, CartActivity.class);
                startActivity(intent);
                break;
        }
    }
}
