package androidz.util;

import android.os.SystemClock;
import android.view.View;

import androidx.annotation.NonNull;


public final class ClickUtil {

    public static boolean isSingleClick(@NonNull View view) {
        return isSingleClick(view, 1000);
    }

    /**
     * @param duration The duration (in millisecond) of debouncing.
     */
    public static boolean isSingleClick(@NonNull View view, int duration) {
        Object time = view.getTag(R.id.view_tag_click_time);
        long lastClickTime = time == null ? 0 : (long) time;
        long nowClickTime = SystemClock.uptimeMillis();
        view.setTag(R.id.view_tag_click_time, nowClickTime);
        return nowClickTime - lastClickTime > duration;
    }

    public static abstract class OnSingleClickListener implements View.OnClickListener {
        private long duration = 1000;
        private boolean enabled = true;
        private final Runnable ENABLE_AGAIN = () -> enabled = true;

        public OnSingleClickListener() {
        }

        public OnSingleClickListener(long duration) {
            this.duration = duration;
        }

        @Override
        public final void onClick(View v) {
            if (enabled) {
                enabled = false;
                v.postDelayed(ENABLE_AGAIN, duration);
                onSingleClick(v);
            }
        }

        public abstract void onSingleClick(View v);
    }
}
