package vn.winwindeal.android.app;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import vn.winwindeal.android.app.model.Product;
import vn.winwindeal.android.app.network.DataLoader;
import vn.winwindeal.android.app.util.CommonUtil;
import vn.winwindeal.android.app.util.DialogUtil;
import vn.winwindeal.android.app.webservice.AddProductWS;
import vn.winwindeal.android.app.webservice.EditProductWS;

/**
 * Created by nhannguyen on 4/9/2018.
 */

public class CreateEditProductActivity extends BaseActivity implements DataLoader.DataLoaderInterface, View.OnClickListener{

    Toolbar toolbar;
    private AddProductWS mAddProductWs;
    private EditProductWS mEditProductWs;
    private EditText mNameEdt, mCodeEdt,  mPriceEdt, mOriginEdt, mQuantityEdt, mDescriptionEdt;
    private ImageView mThumbnailImgv;
    private String mThumbnail = "";
    boolean isLock = false;
    boolean isEdit = false;
    Product product;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_edit_product_activity);
        initComponents();
    }

    private void initComponents() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("");

        mNameEdt = (EditText) findViewById(R.id.productNameEdt);
        mCodeEdt = (EditText) findViewById(R.id.productCodeEdt);
        mPriceEdt = (EditText) findViewById(R.id.productPriceEdt);
        mOriginEdt = (EditText) findViewById(R.id.productOriginEdt);
        mThumbnailImgv = (ImageView) findViewById(R.id.thumbnailImgv);
        mQuantityEdt = (EditText) findViewById(R.id.quantityEdt);
        mDescriptionEdt = (EditText) findViewById(R.id.descriptionEdt);

        product = getIntent().getParcelableExtra("product");
        if (product != null) {
            DisplayMetrics dMetrics = getResources().getDisplayMetrics();
            mThumbnailImgv.setLayoutParams(new LinearLayout.LayoutParams(dMetrics.widthPixels, (int) dMetrics.widthPixels * 3 / 4));
            isEdit = true;
            mNameEdt.setText(product.product_name);
            mCodeEdt.setText(product.code);
            mPriceEdt.setText(String.valueOf(product.price));
            mOriginEdt.setText(product.product_origin);
            mQuantityEdt.setText(String.valueOf(product.quantity));
            mDescriptionEdt.setText(product.description);
            Glide.with(CreateEditProductActivity.this).load(product.thumbnail).into((ImageView) findViewById(R.id.thumbnailImgv));
            if (product.is_deleted == 1) {
                mNameEdt.setEnabled(false);
                mCodeEdt.setEnabled(false);
                mPriceEdt.setEnabled(false);
                mOriginEdt.setEnabled(false);
                mThumbnailImgv.setEnabled(false);
                mQuantityEdt.setEnabled(false);
                mDescriptionEdt.setEnabled(false);
            }
        } else {
            isEdit = false;
        }

        mAddProductWs = new AddProductWS(this);
        mEditProductWs = new EditProductWS(this);
        mThumbnailImgv.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.product_header_menu, menu);
        if (product != null && product.is_deleted == 0) {
            if (!isEdit) {
                menu.findItem(R.id.action_delete).setVisible(false);
            }
        } else {
            menu.findItem(R.id.action_delete).setVisible(false);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_save:
                doSaveProduct();
                break;
            case R.id.action_delete:
                DialogUtil.showConfirmDialog(CreateEditProductActivity.this, null, getString(R.string.delete_product_warning), null, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String strName = mNameEdt.getText().toString().trim();
                        String strCode = mCodeEdt.getText().toString().trim();
                        String strOrigin = mOriginEdt.getText().toString().trim();
                        String strThumbnail = mThumbnail;
                        String strPrice = mPriceEdt.getText().toString().trim();
                        String description = mDescriptionEdt.getText().toString().trim();
                        int quantity = mQuantityEdt.getText().toString().trim().equals("") ? 0 : Integer.parseInt(mQuantityEdt.getText().toString().trim());
                        isLock = true;
                        mEditProductWs.doEditProduct(strCode, strName, !strPrice.equals("") ? Double.parseDouble(strPrice) : 0, strOrigin, strThumbnail, quantity, description, 1);
                        showLoading();
                    }
                }, null, true);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void doSaveProduct() {
        String strName = mNameEdt.getText().toString().trim();
        String strCode = mCodeEdt.getText().toString().trim();
        String strOrigin = mOriginEdt.getText().toString().trim();
        String strThumbnail = mThumbnail;
        String strPrice = mPriceEdt.getText().toString().trim();
        String description = mDescriptionEdt.getText().toString().trim();
        int quantity = mQuantityEdt.getText().toString().trim().equals("") ? 0 : Integer.parseInt(mQuantityEdt.getText().toString().trim());
        if (!strName.equals("") && !strCode.equals("") && !strOrigin.equals("") && !strThumbnail.equals("")) {
            isLock = true;
            if (!isEdit) {
                mAddProductWs.doAddProduct(strCode, strName, !strPrice.equals("") ? Double.parseDouble(strPrice) : 0, strOrigin, strThumbnail, quantity, description);
            } else {
                mEditProductWs.doEditProduct(strCode, strName, !strPrice.equals("") ? Double.parseDouble(strPrice) : 0, strOrigin, strThumbnail, quantity, description, 0);
            }
            showLoading();
        } else {

        }
    }

    @Override
    public void loadDataDone(int requestIndex, int resultCode, Object result) {
        hideLoading();
        switch (requestIndex) {
            case Constant.REQUEST_API_ADD_PRODUCT:
                DialogUtil.showWarningDialog(CreateEditProductActivity.this, null, getResources().getString(R.string.create_product_successfully), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setResult(RESULT_OK);
                        finish();
                    }
                }, Gravity.LEFT, false);
                break;
            case Constant.REQUEST_API_EDIT_PRODUCT:
                DialogUtil.showWarningDialog(CreateEditProductActivity.this, null, getResources().getString(R.string.edit_product_successfully), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setResult(RESULT_OK);
                        finish();
                    }
                }, Gravity.LEFT, false);
                break;
        }
    }

    @Override
    public void loadDataFail(int requestIndex, int resultCode, Object result) {
        hideLoading();
        switch (requestIndex) {
            case Constant.REQUEST_NETWORK_FAILED:
                isLock = true;
                DialogUtil.showNetworkDialogWarning(CreateEditProductActivity.this, new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        isLock = false;
                    }
                }, Gravity.LEFT, false);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.thumbnailImgv:
                CropImage.activity().setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(4, 3)
                        .setFixAspectRatio(true)
//                        .setMinCropResultSize(256,256)
                        .setMinCropResultSize(512,512)
                        .start(this);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    CropImage.ActivityResult result = CropImage.getActivityResult(data);
                    Uri resultUri = result.getUri();
                    String path = CommonUtil.getPathFromUri(CreateEditProductActivity.this, resultUri);
                    Log.d("Nhannatc", "Url location : " + path);
                    if (path != null && !path.equals("")) {
                        DisplayMetrics dMetrics = getResources().getDisplayMetrics();
                        mThumbnailImgv.setLayoutParams(new LinearLayout.LayoutParams(dMetrics.widthPixels, dMetrics.widthPixels));
                        CommonUtil.ValidateImageResolution(path, 1);
                        mThumbnail = path;
                        Bitmap bmp = BitmapFactory.decodeFile(path);
                        if (bmp != null) {
                            Log.d("Nhannatc", "Width : " + bmp.getWidth() + " == height : " + bmp.getHeight());
                            mThumbnailImgv.setImageBitmap(bmp);
                        }
                    }
                }
                break;
        }
    }
}
