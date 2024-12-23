package androidz.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatDialog;

import static android.content.pm.ActivityInfo.SCREEN_ORIENTATION_LOCKED;


public final class LoadingDialog {

    @SuppressLint("StaticFieldLeak")
    private static InternalDialog dialog;


    public static void showLoading(@NonNull Activity activity) {
        showLoading(activity, new Options());
    }

    public static void showLoading(@NonNull Activity activity, @NonNull Options options) {
        hide();
        dialog = new InternalDialog(activity, options);
        dialog.show();
    }

    public static void hide() {
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
    }

    public static boolean isShowing() {
        if (dialog != null) {
            return dialog.isShowing();
        }
        return false;
    }

    public static final class InternalDialog extends AppCompatDialog {
        private final Options options;
        private final Activity activity;
        private int orientation = SCREEN_ORIENTATION_LOCKED;

        public InternalDialog(@NonNull Activity activity, @NonNull Options options) {
            super(activity);
            this.activity = activity;
            this.options = options;
            setOnShowListener(options.onShow);
            setOnDismissListener(options.onHide);
        }

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(options.loadingRes != 0 ? options.loadingRes : R.layout.loading_dialog);
            setCancelable(options.cancelable);

            TextView textView = findViewById(R.id.loading_message);
            if (textView != null) {
                if (options.message != null) {
                    textView.setText(options.message);
                } else if (options.messageRes != 0) {
                    textView.setText(options.messageRes);
                }
            }
        }

        @Override
        protected void onStart() {
            super.onStart();
            orientation = activity.getRequestedOrientation();
            if (orientation != SCREEN_ORIENTATION_LOCKED) {
                activity.setRequestedOrientation(SCREEN_ORIENTATION_LOCKED);
            }
        }

        @Override
        protected void onStop() {
            super.onStop();
            if (orientation != SCREEN_ORIENTATION_LOCKED) {
                activity.setRequestedOrientation(orientation);
                orientation = SCREEN_ORIENTATION_LOCKED;
            }
        }
    }

    public static class Options {
        /**
         * 自定义布局
         */
        @LayoutRes
        public int loadingRes;
        /**
         * 提示消息
         */
        @StringRes
        public int messageRes;
        public String message;
        public boolean cancelable;
        public OnShowListener onShow;
        public OnHideListener onHide;
    }

    public interface OnShowListener extends DialogInterface.OnShowListener {
        @Override
        default void onShow(DialogInterface dialog) {
            onShow();
        }

        void onShow();
    }

    public interface OnHideListener extends DialogInterface.OnDismissListener {
        @Override
        default void onDismiss(DialogInterface dialog) {
            onHide();
        }

        void onHide();
    }
}
