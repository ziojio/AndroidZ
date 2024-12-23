package androidz.util;

import android.app.Activity;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import static androidx.core.content.ContextCompat.getSystemService;


public final class KeyboardUtil {

    public static void showSoftKeyboard(@NonNull View view) {
        showSoftKeyboard(view, true);
    }

    public static void showSoftKeyboard(@NonNull View view, boolean useWindowInsetsController) {
        if (useWindowInsetsController) {
            WindowInsetsControllerCompat windowController = ViewCompat.getWindowInsetsController(view);
            if (windowController != null) {
                windowController.show(WindowInsetsCompat.Type.ime());
                return;
            }
        }
        InputMethodManager imm = getSystemService(view.getContext(), InputMethodManager.class);
        if (imm != null) {
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    public static void hideSoftKeyboard(@NonNull View view) {
        hideSoftKeyboard(view, true);
    }

    public static void hideSoftKeyboard(@NonNull View view, boolean useWindowInsetsController) {
        if (useWindowInsetsController) {
            WindowInsetsControllerCompat windowController = ViewCompat.getWindowInsetsController(view);
            if (windowController != null) {
                windowController.hide(WindowInsetsCompat.Type.ime());
                return;
            }
        }
        InputMethodManager imm = getSystemService(view.getContext(), InputMethodManager.class);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    /**
     * 判断软键盘是否可见
     */
    public static boolean isSoftKeyboardVisible(@NonNull Activity activity) {
        View content = activity.findViewById(android.R.id.content);
        int height = content.getRootView().getHeight();
        Rect r = new Rect();
        content.getWindowVisibleDisplayFrame(r); // 获取应用的显示区域
        int heightDiff = height - (r.bottom - r.top); // 未判断状态栏的高度
        return heightDiff > height / 4;
    }

    /**
     * 点击输入框外隐藏软键盘，需重写 dispatchTouchEvent 处理点击事件
     */
    public static void hideSoftKeyboardByClick(@NonNull MotionEvent event, @NonNull View focusView) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (!isClickInView(focusView, event)) {
                hideSoftKeyboard(focusView);
            }
        }
    }

    private static boolean isClickInView(@NonNull View view, @NonNull MotionEvent event) {
        int[] l = {0, 0};
        view.getLocationInWindow(l);
        int left = l[0], top = l[1], bottom = top + view.getHeight(), right = left + view.getWidth();
        return event.getX() > left && event.getX() < right && event.getY() > top && event.getY() < bottom;
    }

}
