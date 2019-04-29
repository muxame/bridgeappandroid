package net.bridgeint.app.Animation;

import android.view.View;

/**
 * Created by DeviceBee on 8/11/2017.
 */

public class ViewProxy {
    public static float getAlpha(View view) {
        if (ViewAnimation.NEED_PROXY) {
            return ViewAnimation.wrap(view).getAlpha();
        } else {
            return view.getAlpha();
        }
    }

    public static void setAlpha(View view, float alpha) {
        if (ViewAnimation.NEED_PROXY) {
            ViewAnimation.wrap(view).setAlpha(alpha);
        } else {
            view.setAlpha(alpha);
        }
    }

    public static float getPivotX(View view) {
        if (ViewAnimation.NEED_PROXY) {
            return ViewAnimation.wrap(view).getPivotX();
        } else {
            return view.getPivotX();
        }
    }

    public static void setPivotX(View view, float pivotX) {
        if (ViewAnimation.NEED_PROXY) {
            ViewAnimation.wrap(view).setPivotX(pivotX);
        } else {
            view.setPivotX(pivotX);
        }
    }

    public static float getPivotY(View view) {
        if (ViewAnimation.NEED_PROXY) {
            return ViewAnimation.wrap(view).getPivotY();
        } else {
            return view.getPivotY();
        }
    }

    public static void setPivotY(View view, float pivotY) {
        if (ViewAnimation.NEED_PROXY) {
            ViewAnimation.wrap(view).setPivotY(pivotY);
        } else {
            view.setPivotY(pivotY);
        }
    }

    public static float getRotation(View view) {
        if (ViewAnimation.NEED_PROXY) {
            return ViewAnimation.wrap(view).getRotation();
        } else {
            return view.getRotation();
        }
    }

    public static void setRotation(View view, float rotation) {
        if (ViewAnimation.NEED_PROXY) {
            ViewAnimation.wrap(view).setRotation(rotation);
        } else {
            view.setRotation(rotation);
        }
    }

    public static float getRotationX(View view) {
        if (ViewAnimation.NEED_PROXY) {
            return ViewAnimation.wrap(view).getRotationX();
        } else {
            return view.getRotationX();
        }
    }

    public void setRotationX(View view, float rotationX) {
        if (ViewAnimation.NEED_PROXY) {
            ViewAnimation.wrap(view).setRotationX(rotationX);
        } else {
            view.setRotationX(rotationX);
        }
    }

    public static float getRotationY(View view) {
        if (ViewAnimation.NEED_PROXY) {
            return ViewAnimation.wrap(view).getRotationY();
        } else {
            return view.getRotationY();
        }
    }

    public void setRotationY(View view, float rotationY) {
        if (ViewAnimation.NEED_PROXY) {
            ViewAnimation.wrap(view).setRotationY(rotationY);
        } else {
            view.setRotationY(rotationY);
        }
    }

    public static float getScaleX(View view) {
        if (ViewAnimation.NEED_PROXY) {
            return ViewAnimation.wrap(view).getScaleX();
        } else {
            return view.getScaleX();
        }
    }

    public static void setScaleX(View view, float scaleX) {
        if (ViewAnimation.NEED_PROXY) {
            ViewAnimation.wrap(view).setScaleX(scaleX);
        } else {
            view.setScaleX(scaleX);
        }
    }

    public static float getScaleY(View view) {
        if (ViewAnimation.NEED_PROXY) {
            return ViewAnimation.wrap(view).getScaleY();
        } else {
            return view.getScaleY();
        }
    }

    public static void setScaleY(View view, float scaleY) {
        if (ViewAnimation.NEED_PROXY) {
            ViewAnimation.wrap(view).setScaleY(scaleY);
        } else {
            view.setScaleY(scaleY);
        }
    }

    public static int getScrollX(View view) {
        if (ViewAnimation.NEED_PROXY) {
            return ViewAnimation.wrap(view).getScrollX();
        } else {
            return view.getScrollX();
        }
    }

    public static void setScrollX(View view, int value) {
        if (ViewAnimation.NEED_PROXY) {
            ViewAnimation.wrap(view).setScrollX(value);
        } else {
            view.setScrollX(value);
        }
    }

    public static int getScrollY(View view) {
        if (ViewAnimation.NEED_PROXY) {
            return ViewAnimation.wrap(view).getScrollY();
        } else {
            return view.getScrollY();
        }
    }

    public static void setScrollY(View view, int value) {
        if (ViewAnimation.NEED_PROXY) {
            ViewAnimation.wrap(view).setScrollY(value);
        } else {
            view.setScrollY(value);
        }
    }

    public static float getTranslationX(View view) {
        if (ViewAnimation.NEED_PROXY) {
            return ViewAnimation.wrap(view).getTranslationX();
        } else {
            return view.getTranslationX();
        }
    }

    public static void setTranslationX(View view, float translationX) {
        if (ViewAnimation.NEED_PROXY) {
            ViewAnimation.wrap(view).setTranslationX(translationX);
        } else {
            view.setTranslationX(translationX);
        }
    }

    public static float getTranslationY(View view) {
        if (ViewAnimation.NEED_PROXY) {
            return ViewAnimation.wrap(view).getTranslationY();
        } else {
            return view.getTranslationY();
        }
    }

    public static void setTranslationY(View view, float translationY) {
        if (ViewAnimation.NEED_PROXY) {
            ViewAnimation.wrap(view).setTranslationY(translationY);
        } else {
            view.setTranslationY(translationY);
        }
    }

    public static float getX(View view) {
        if (ViewAnimation.NEED_PROXY) {
            return ViewAnimation.wrap(view).getX();
        } else {
            return view.getX();
        }
    }

    public static void setX(View view, float x) {
        if (ViewAnimation.NEED_PROXY) {
            ViewAnimation.wrap(view).setX(x);
        } else {
            view.setX(x);
        }
    }

    public static float getY(View view) {
        if (ViewAnimation.NEED_PROXY) {
            return ViewAnimation.wrap(view).getY();
        } else {
            return view.getY();
        }
    }

    public static void setY(View view, float y) {
        if (ViewAnimation.NEED_PROXY) {
            ViewAnimation.wrap(view).setY(y);
        } else {
            view.setY(y);
        }
    }

    public static Object wrap(View view) {
        if (ViewAnimation.NEED_PROXY) {
            return ViewAnimation.wrap(view);
        } else {
            return view;
        }
    }
}
