package vn.winwindeal.android.app.util;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.text.SpannableString;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import java.util.Locale;
import vn.winwindeal.android.app.R;

/**
 * Created by nhannguyen on 4/6/2018.
 */

public class DialogUtil {
    public static void showWarningDialog(Context context, String title, Object message, final View.OnClickListener listener, int messageGravity, boolean isCancellable) {
        final Dialog dialog = new Dialog(context);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(context.getResources().getColor(
                        R.color.transparent)));
        dialog.setContentView(R.layout.warning_popup);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        lp.copyFrom(window.getAttributes());
        // This makes the dialog take up the full width
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(lp);
        ((TextView) dialog.findViewById(R.id.titleTv)).setTypeface(FontUtil.getFontAssets(context, FontUtil.ROBOTO_MEDIUM));

        if (title != null && !title.equals("")) {
            ((TextView) dialog.findViewById(R.id.titleTv)).setText(title);
        } else {
            ((TextView) dialog.findViewById(R.id.titleTv)).setText(context.getResources().getString(R.string.app_name));
        }

        if (message != null/* && !message.equals("")*/) {
            if (message instanceof SpannableString) {
                ((TextView) dialog.findViewById(R.id.messageTv)).setText((SpannableString) message, TextView.BufferType.SPANNABLE);
            } else if (message instanceof String) {
                ((TextView) dialog.findViewById(R.id.messageTv)).setText((String) message);
            }
            ((TextView) dialog.findViewById(R.id.messageTv)).setGravity(messageGravity);
        }

        dialog.findViewById(R.id.okTv).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (listener != null) {
                    listener.onClick(v);
                }
                dialog.dismiss();
            }
        });

        dialog.setCancelable(isCancellable);
        dialog.show();
    }

    public static void showConfirmDialog(final Context context, String title, Object message, String possitiveBtnLabel, final View.OnClickListener possitiveButtonListener, String negativeBtnLabel, boolean isConfirmDelete) {
        final Dialog dialog = new Dialog(context);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(context.getResources().getColor(
                        R.color.transparent)));
        dialog.setContentView(R.layout.confirm_popup);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        lp.copyFrom(window.getAttributes());
        // This makes the dialog take up the full width
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(lp);
        ((TextView) dialog.findViewById(R.id.titleTv)).setTypeface(FontUtil.getFontAssets(context, FontUtil.ROBOTO_MEDIUM));

        if (title != null && !title.equals("")) {
            ((TextView) dialog.findViewById(R.id.titleTv)).setText(title);
        } else {
            ((TextView) dialog.findViewById(R.id.titleTv)).setText(context.getResources().getString(R.string.app_name));
        }

        if (message instanceof String && message != null && !message.equals("")) {
            ((TextView) dialog.findViewById(R.id.messageTv)).setText((String) message);
        } else if (message instanceof SpannableString) {
            ((TextView) dialog.findViewById(R.id.messageTv)).setText((SpannableString) message, TextView.BufferType.SPANNABLE);
        }

        dialog.findViewById(R.id.noTv).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                dialog.dismiss();
            }
        });
        dialog.findViewById(R.id.yesTv).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (possitiveButtonListener != null) {
                    possitiveButtonListener.onClick(v);
                }
                dialog.dismiss();
            }
        });
        if (possitiveBtnLabel != null && !possitiveBtnLabel.equals("")) {
            ((TextView) dialog.findViewById(R.id.yesTv)).setText(possitiveBtnLabel.toUpperCase(Locale.US));
        } else {
            ((TextView) dialog.findViewById(R.id.yesTv)).setText(context.getResources().getString(R.string.ok_lbl).toUpperCase(Locale.US));
        }
        if (negativeBtnLabel != null && !negativeBtnLabel.equals("")) {
            ((TextView) dialog.findViewById(R.id.noTv)).setText(negativeBtnLabel.toUpperCase(Locale.US));
        } else {
            ((TextView) dialog.findViewById(R.id.noTv)).setText(context.getResources().getString(R.string.cancel_lbl).toUpperCase(Locale.US));
        }

        if (isConfirmDelete) {
            ((TextView) dialog.findViewById(R.id.yesTv)).setTextColor(CommonUtil.getColor(context, R.color.colorRed));
        } else {
            ((TextView) dialog.findViewById(R.id.yesTv)).setTextColor(CommonUtil.getColor(context, R.color.colorPrimary));
        }
        dialog.setCancelable(false);
        dialog.show();
    }

    public static void showNetworkDialogWarning(Context context, final View.OnClickListener listener, int messageGravity, boolean isCancellable) {
        final Dialog dialog = new Dialog(context);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(context.getResources().getColor(
                        R.color.transparent)));
        dialog.setContentView(R.layout.warning_popup);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        lp.copyFrom(window.getAttributes());
        // This makes the dialog take up the full width
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(lp);
        ((TextView) dialog.findViewById(R.id.titleTv)).setTypeface(FontUtil.getFontAssets(context, FontUtil.ROBOTO_MEDIUM));

        ((TextView) dialog.findViewById(R.id.titleTv)).setText(context.getResources().getString(R.string.network_connection_title));

        ((TextView) dialog.findViewById(R.id.messageTv)).setText(context.getResources().getString(R.string.slow_network_warning));

        ((TextView) dialog.findViewById(R.id.okTv)).setText(context.getResources().getString(R.string.close_cap_lbl).toUpperCase(Locale.US));
        ((TextView) dialog.findViewById(R.id.okTv)).setTextColor(context.getResources().getColor(R.color.colorTextView));
        dialog.findViewById(R.id.okTv).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (listener != null) {
                    listener.onClick(v);
                }
                dialog.dismiss();
            }
        });

        dialog.setCancelable(isCancellable);
        dialog.show();
    }
}
