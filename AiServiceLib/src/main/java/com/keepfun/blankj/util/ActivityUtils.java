package com.keepfun.blankj.util;//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Build.VERSION;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import androidx.annotation.AnimRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import com.keepfun.blankj.util.Utils.ActivityLifecycleCallbacks;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class ActivityUtils {
    private ActivityUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    public static void addActivityLifecycleCallbacks(Activity activity, ActivityLifecycleCallbacks callbacks) {
        UtilsBridge.addActivityLifecycleCallbacks(activity, callbacks);
    }

    public static void removeActivityLifecycleCallbacks(Activity activity) {
        UtilsBridge.removeActivityLifecycleCallbacks(activity);
    }

    public static void removeActivityLifecycleCallbacks(Activity activity, ActivityLifecycleCallbacks callbacks) {
        UtilsBridge.removeActivityLifecycleCallbacks(activity, callbacks);
    }

    public static Activity getActivityByContext(Context context) {
        Activity activity = getActivityByContextInner(context);
        return !isActivityAlive(activity) ? null : activity;
    }

    private static Activity getActivityByContextInner(Context context) {
        if (context == null) {
            return null;
        } else {
            ArrayList list = new ArrayList();

            do {
                if (!(context instanceof ContextWrapper)) {
                    return null;
                }

                if (context instanceof Activity) {
                    return (Activity)context;
                }

                Activity activity = getActivityFromDecorContext(context);
                if (activity != null) {
                    return activity;
                }

                list.add(context);
                context = ((ContextWrapper)context).getBaseContext();
                if (context == null) {
                    return null;
                }
            } while(!list.contains(context));

            return null;
        }
    }

    private static Activity getActivityFromDecorContext(Context context) {
        if (context == null) {
            return null;
        } else {
            if (context.getClass().getName().equals("com.android.internal.policy.DecorContext")) {
                try {
                    Field mActivityContextField = context.getClass().getDeclaredField("mActivityContext");
                    mActivityContextField.setAccessible(true);
                    return (Activity)((WeakReference)mActivityContextField.get(context)).get();
                } catch (Exception var2) {
                }
            }

            return null;
        }
    }

    public static boolean isActivityExists(@NonNull String pkg, @NonNull String cls) {
        if (pkg == null) {
            throw new NullPointerException("Argument 'pkg' of type String (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else if (cls == null) {
            throw new NullPointerException("Argument 'cls' of type String (#1 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            Intent intent = new Intent();
            intent.setClassName(pkg, cls);
            PackageManager pm = Utils.getApp().getPackageManager();
            return pm.resolveActivity(intent, 0) != null && intent.resolveActivity(pm) != null && pm.queryIntentActivities(intent, 0).size() != 0;
        }
    }

    public static void startActivity(@NonNull Class<? extends Activity> clz) {
        if (clz == null) {
            throw new NullPointerException("Argument 'clz' of type Class<? extends Activity> (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            Context context = UtilsBridge.getTopActivityOrApp();
            startActivity((Context)context, (Bundle)null, context.getPackageName(), clz.getName(), (Bundle)null);
        }
    }

    public static void startActivity(@NonNull Class<? extends Activity> clz, @Nullable Bundle options) {
        if (clz == null) {
            throw new NullPointerException("Argument 'clz' of type Class<? extends Activity> (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            Context context = UtilsBridge.getTopActivityOrApp();
            startActivity((Context)context, (Bundle)null, context.getPackageName(), clz.getName(), (Bundle)options);
        }
    }

    public static void startActivity(@NonNull Class<? extends Activity> clz, @AnimRes int enterAnim, @AnimRes int exitAnim) {
        if (clz == null) {
            throw new NullPointerException("Argument 'clz' of type Class<? extends Activity> (#0 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            Context context = UtilsBridge.getTopActivityOrApp();
            startActivity((Context)context, (Bundle)null, context.getPackageName(), clz.getName(), (Bundle)getOptionsBundle(context, enterAnim, exitAnim));
            if (VERSION.SDK_INT < 16 && context instanceof Activity) {
                ((Activity)context).overridePendingTransition(enterAnim, exitAnim);
            }

        }
    }

    public static void startActivity(@NonNull Activity activity, @NonNull Class<? extends Activity> clz) {
        if (activity == null) {
            throw new NullPointerException("Argument 'activity' of type Activity (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else if (clz == null) {
            throw new NullPointerException("Argument 'clz' of type Class<? extends Activity> (#1 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            startActivity((Context)activity, (Bundle)null, activity.getPackageName(), clz.getName(), (Bundle)null);
        }
    }

    public static void startActivity(@NonNull Activity activity, @NonNull Class<? extends Activity> clz, @Nullable Bundle options) {
        if (activity == null) {
            throw new NullPointerException("Argument 'activity' of type Activity (#0 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else if (clz == null) {
            throw new NullPointerException("Argument 'clz' of type Class<? extends Activity> (#1 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            startActivity((Context)activity, (Bundle)null, activity.getPackageName(), clz.getName(), (Bundle)options);
        }
    }

    public static void startActivity(@NonNull Activity activity, @NonNull Class<? extends Activity> clz, View... sharedElements) {
        if (activity == null) {
            throw new NullPointerException("Argument 'activity' of type Activity (#0 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else if (clz == null) {
            throw new NullPointerException("Argument 'clz' of type Class<? extends Activity> (#1 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            startActivity((Context)activity, (Bundle)null, activity.getPackageName(), clz.getName(), (Bundle)getOptionsBundle(activity, sharedElements));
        }
    }

    public static void startActivity(@NonNull Activity activity, @NonNull Class<? extends Activity> clz, @AnimRes int enterAnim, @AnimRes int exitAnim) {
        if (activity == null) {
            throw new NullPointerException("Argument 'activity' of type Activity (#0 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else if (clz == null) {
            throw new NullPointerException("Argument 'clz' of type Class<? extends Activity> (#1 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            startActivity((Context)activity, (Bundle)null, activity.getPackageName(), clz.getName(), (Bundle)getOptionsBundle((Context)activity, enterAnim, exitAnim));
            if (VERSION.SDK_INT < 16) {
                activity.overridePendingTransition(enterAnim, exitAnim);
            }

        }
    }

    public static void startActivity(@NonNull Bundle extras, @NonNull Class<? extends Activity> clz) {
        if (extras == null) {
            throw new NullPointerException("Argument 'extras' of type Bundle (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else if (clz == null) {
            throw new NullPointerException("Argument 'clz' of type Class<? extends Activity> (#1 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            Context context = UtilsBridge.getTopActivityOrApp();
            startActivity((Context)context, (Bundle)extras, context.getPackageName(), clz.getName(), (Bundle)null);
        }
    }

    public static void startActivity(@NonNull Bundle extras, @NonNull Class<? extends Activity> clz, @Nullable Bundle options) {
        if (extras == null) {
            throw new NullPointerException("Argument 'extras' of type Bundle (#0 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else if (clz == null) {
            throw new NullPointerException("Argument 'clz' of type Class<? extends Activity> (#1 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            Context context = UtilsBridge.getTopActivityOrApp();
            startActivity(context, extras, context.getPackageName(), clz.getName(), options);
        }
    }

    public static void startActivity(@NonNull Bundle extras, @NonNull Class<? extends Activity> clz, @AnimRes int enterAnim, @AnimRes int exitAnim) {
        if (extras == null) {
            throw new NullPointerException("Argument 'extras' of type Bundle (#0 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else if (clz == null) {
            throw new NullPointerException("Argument 'clz' of type Class<? extends Activity> (#1 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            Context context = UtilsBridge.getTopActivityOrApp();
            startActivity(context, extras, context.getPackageName(), clz.getName(), getOptionsBundle(context, enterAnim, exitAnim));
            if (VERSION.SDK_INT < 16 && context instanceof Activity) {
                ((Activity)context).overridePendingTransition(enterAnim, exitAnim);
            }

        }
    }

    public static void startActivity(@NonNull Bundle extras, @NonNull Activity activity, @NonNull Class<? extends Activity> clz) {
        if (extras == null) {
            throw new NullPointerException("Argument 'extras' of type Bundle (#0 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else if (activity == null) {
            throw new NullPointerException("Argument 'activity' of type Activity (#1 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else if (clz == null) {
            throw new NullPointerException("Argument 'clz' of type Class<? extends Activity> (#2 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            startActivity((Context)activity, (Bundle)extras, activity.getPackageName(), clz.getName(), (Bundle)null);
        }
    }

    public static void startActivity(@NonNull Bundle extras, @NonNull Activity activity, @NonNull Class<? extends Activity> clz, @Nullable Bundle options) {
        if (extras == null) {
            throw new NullPointerException("Argument 'extras' of type Bundle (#0 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else if (activity == null) {
            throw new NullPointerException("Argument 'activity' of type Activity (#1 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else if (clz == null) {
            throw new NullPointerException("Argument 'clz' of type Class<? extends Activity> (#2 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            startActivity((Context)activity, (Bundle)extras, activity.getPackageName(), clz.getName(), (Bundle)options);
        }
    }

    public static void startActivity(@NonNull Bundle extras, @NonNull Activity activity, @NonNull Class<? extends Activity> clz, View... sharedElements) {
        if (extras == null) {
            throw new NullPointerException("Argument 'extras' of type Bundle (#0 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else if (activity == null) {
            throw new NullPointerException("Argument 'activity' of type Activity (#1 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else if (clz == null) {
            throw new NullPointerException("Argument 'clz' of type Class<? extends Activity> (#2 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            startActivity((Context)activity, (Bundle)extras, activity.getPackageName(), clz.getName(), (Bundle)getOptionsBundle(activity, sharedElements));
        }
    }

    public static void startActivity(@NonNull Bundle extras, @NonNull Activity activity, @NonNull Class<? extends Activity> clz, @AnimRes int enterAnim, @AnimRes int exitAnim) {
        if (extras == null) {
            throw new NullPointerException("Argument 'extras' of type Bundle (#0 out of 5, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else if (activity == null) {
            throw new NullPointerException("Argument 'activity' of type Activity (#1 out of 5, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else if (clz == null) {
            throw new NullPointerException("Argument 'clz' of type Class<? extends Activity> (#2 out of 5, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            startActivity((Context)activity, (Bundle)extras, activity.getPackageName(), clz.getName(), (Bundle)getOptionsBundle((Context)activity, enterAnim, exitAnim));
            if (VERSION.SDK_INT < 16) {
                activity.overridePendingTransition(enterAnim, exitAnim);
            }

        }
    }

    public static void startActivity(@NonNull String pkg, @NonNull String cls) {
        if (pkg == null) {
            throw new NullPointerException("Argument 'pkg' of type String (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else if (cls == null) {
            throw new NullPointerException("Argument 'cls' of type String (#1 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            startActivity((Context)UtilsBridge.getTopActivityOrApp(), (Bundle)null, pkg, cls, (Bundle)null);
        }
    }

    public static void startActivity(@NonNull String pkg, @NonNull String cls, @Nullable Bundle options) {
        if (pkg == null) {
            throw new NullPointerException("Argument 'pkg' of type String (#0 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else if (cls == null) {
            throw new NullPointerException("Argument 'cls' of type String (#1 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            startActivity((Context)UtilsBridge.getTopActivityOrApp(), (Bundle)null, pkg, cls, (Bundle)options);
        }
    }

    public static void startActivity(@NonNull String pkg, @NonNull String cls, @AnimRes int enterAnim, @AnimRes int exitAnim) {
        if (pkg == null) {
            throw new NullPointerException("Argument 'pkg' of type String (#0 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else if (cls == null) {
            throw new NullPointerException("Argument 'cls' of type String (#1 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            Context context = UtilsBridge.getTopActivityOrApp();
            startActivity((Context)context, (Bundle)null, pkg, cls, (Bundle)getOptionsBundle(context, enterAnim, exitAnim));
            if (VERSION.SDK_INT < 16 && context instanceof Activity) {
                ((Activity)context).overridePendingTransition(enterAnim, exitAnim);
            }

        }
    }

    public static void startActivity(@NonNull Activity activity, @NonNull String pkg, @NonNull String cls) {
        if (activity == null) {
            throw new NullPointerException("Argument 'activity' of type Activity (#0 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else if (pkg == null) {
            throw new NullPointerException("Argument 'pkg' of type String (#1 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else if (cls == null) {
            throw new NullPointerException("Argument 'cls' of type String (#2 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            startActivity((Context)activity, (Bundle)null, pkg, cls, (Bundle)null);
        }
    }

    public static void startActivity(@NonNull Activity activity, @NonNull String pkg, @NonNull String cls, @Nullable Bundle options) {
        if (activity == null) {
            throw new NullPointerException("Argument 'activity' of type Activity (#0 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else if (pkg == null) {
            throw new NullPointerException("Argument 'pkg' of type String (#1 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else if (cls == null) {
            throw new NullPointerException("Argument 'cls' of type String (#2 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            startActivity((Context)activity, (Bundle)null, pkg, cls, (Bundle)options);
        }
    }

    public static void startActivity(@NonNull Activity activity, @NonNull String pkg, @NonNull String cls, View... sharedElements) {
        if (activity == null) {
            throw new NullPointerException("Argument 'activity' of type Activity (#0 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else if (pkg == null) {
            throw new NullPointerException("Argument 'pkg' of type String (#1 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else if (cls == null) {
            throw new NullPointerException("Argument 'cls' of type String (#2 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            startActivity((Context)activity, (Bundle)null, pkg, cls, (Bundle)getOptionsBundle(activity, sharedElements));
        }
    }

    public static void startActivity(@NonNull Activity activity, @NonNull String pkg, @NonNull String cls, @AnimRes int enterAnim, @AnimRes int exitAnim) {
        if (activity == null) {
            throw new NullPointerException("Argument 'activity' of type Activity (#0 out of 5, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else if (pkg == null) {
            throw new NullPointerException("Argument 'pkg' of type String (#1 out of 5, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else if (cls == null) {
            throw new NullPointerException("Argument 'cls' of type String (#2 out of 5, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            startActivity((Context)activity, (Bundle)null, pkg, cls, (Bundle)getOptionsBundle((Context)activity, enterAnim, exitAnim));
            if (VERSION.SDK_INT < 16) {
                activity.overridePendingTransition(enterAnim, exitAnim);
            }

        }
    }

    public static void startActivity(@NonNull Bundle extras, @NonNull String pkg, @NonNull String cls) {
        if (extras == null) {
            throw new NullPointerException("Argument 'extras' of type Bundle (#0 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else if (pkg == null) {
            throw new NullPointerException("Argument 'pkg' of type String (#1 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else if (cls == null) {
            throw new NullPointerException("Argument 'cls' of type String (#2 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            startActivity((Context)UtilsBridge.getTopActivityOrApp(), (Bundle)extras, pkg, cls, (Bundle)null);
        }
    }

    public static void startActivity(@NonNull Bundle extras, @NonNull String pkg, @NonNull String cls, @Nullable Bundle options) {
        if (extras == null) {
            throw new NullPointerException("Argument 'extras' of type Bundle (#0 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else if (pkg == null) {
            throw new NullPointerException("Argument 'pkg' of type String (#1 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else if (cls == null) {
            throw new NullPointerException("Argument 'cls' of type String (#2 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            startActivity(UtilsBridge.getTopActivityOrApp(), extras, pkg, cls, options);
        }
    }

    public static void startActivity(@NonNull Bundle extras, @NonNull String pkg, @NonNull String cls, @AnimRes int enterAnim, @AnimRes int exitAnim) {
        if (extras == null) {
            throw new NullPointerException("Argument 'extras' of type Bundle (#0 out of 5, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else if (pkg == null) {
            throw new NullPointerException("Argument 'pkg' of type String (#1 out of 5, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else if (cls == null) {
            throw new NullPointerException("Argument 'cls' of type String (#2 out of 5, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            Context context = UtilsBridge.getTopActivityOrApp();
            startActivity(context, extras, pkg, cls, getOptionsBundle(context, enterAnim, exitAnim));
            if (VERSION.SDK_INT < 16 && context instanceof Activity) {
                ((Activity)context).overridePendingTransition(enterAnim, exitAnim);
            }

        }
    }

    public static void startActivity(@NonNull Bundle extras, @NonNull Activity activity, @NonNull String pkg, @NonNull String cls) {
        if (extras == null) {
            throw new NullPointerException("Argument 'extras' of type Bundle (#0 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else if (activity == null) {
            throw new NullPointerException("Argument 'activity' of type Activity (#1 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else if (pkg == null) {
            throw new NullPointerException("Argument 'pkg' of type String (#2 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else if (cls == null) {
            throw new NullPointerException("Argument 'cls' of type String (#3 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            startActivity((Context)activity, (Bundle)extras, pkg, cls, (Bundle)null);
        }
    }

    public static void startActivity(@NonNull Bundle extras, @NonNull Activity activity, @NonNull String pkg, @NonNull String cls, @Nullable Bundle options) {
        if (extras == null) {
            throw new NullPointerException("Argument 'extras' of type Bundle (#0 out of 5, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else if (activity == null) {
            throw new NullPointerException("Argument 'activity' of type Activity (#1 out of 5, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else if (pkg == null) {
            throw new NullPointerException("Argument 'pkg' of type String (#2 out of 5, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else if (cls == null) {
            throw new NullPointerException("Argument 'cls' of type String (#3 out of 5, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            startActivity((Context)activity, (Bundle)extras, pkg, cls, (Bundle)options);
        }
    }

    public static void startActivity(@NonNull Bundle extras, @NonNull Activity activity, @NonNull String pkg, @NonNull String cls, View... sharedElements) {
        if (extras == null) {
            throw new NullPointerException("Argument 'extras' of type Bundle (#0 out of 5, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else if (activity == null) {
            throw new NullPointerException("Argument 'activity' of type Activity (#1 out of 5, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else if (pkg == null) {
            throw new NullPointerException("Argument 'pkg' of type String (#2 out of 5, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else if (cls == null) {
            throw new NullPointerException("Argument 'cls' of type String (#3 out of 5, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            startActivity((Context)activity, (Bundle)extras, pkg, cls, (Bundle)getOptionsBundle(activity, sharedElements));
        }
    }

    public static void startActivity(@NonNull Bundle extras, @NonNull Activity activity, @NonNull String pkg, @NonNull String cls, @AnimRes int enterAnim, @AnimRes int exitAnim) {
        if (extras == null) {
            throw new NullPointerException("Argument 'extras' of type Bundle (#0 out of 6, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else if (activity == null) {
            throw new NullPointerException("Argument 'activity' of type Activity (#1 out of 6, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else if (pkg == null) {
            throw new NullPointerException("Argument 'pkg' of type String (#2 out of 6, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else if (cls == null) {
            throw new NullPointerException("Argument 'cls' of type String (#3 out of 6, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            startActivity((Context)activity, (Bundle)extras, pkg, cls, (Bundle)getOptionsBundle((Context)activity, enterAnim, exitAnim));
            if (VERSION.SDK_INT < 16) {
                activity.overridePendingTransition(enterAnim, exitAnim);
            }

        }
    }

    public static boolean startActivity(@NonNull Intent intent) {
        if (intent == null) {
            throw new NullPointerException("Argument 'intent' of type Intent (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            return startActivity((Intent)intent, (Context)UtilsBridge.getTopActivityOrApp(), (Bundle)null);
        }
    }

    public static boolean startActivity(@NonNull Intent intent, @Nullable Bundle options) {
        if (intent == null) {
            throw new NullPointerException("Argument 'intent' of type Intent (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            return startActivity(intent, UtilsBridge.getTopActivityOrApp(), options);
        }
    }

    public static boolean startActivity(@NonNull Intent intent, @AnimRes int enterAnim, @AnimRes int exitAnim) {
        if (intent == null) {
            throw new NullPointerException("Argument 'intent' of type Intent (#0 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            Context context = UtilsBridge.getTopActivityOrApp();
            boolean isSuccess = startActivity(intent, context, getOptionsBundle(context, enterAnim, exitAnim));
            if (isSuccess && VERSION.SDK_INT < 16 && context instanceof Activity) {
                ((Activity)context).overridePendingTransition(enterAnim, exitAnim);
            }

            return isSuccess;
        }
    }

    public static void startActivity(@NonNull Activity activity, @NonNull Intent intent) {
        if (activity == null) {
            throw new NullPointerException("Argument 'activity' of type Activity (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else if (intent == null) {
            throw new NullPointerException("Argument 'intent' of type Intent (#1 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            startActivity((Intent)intent, (Context)activity, (Bundle)null);
        }
    }

    public static void startActivity(@NonNull Activity activity, @NonNull Intent intent, @Nullable Bundle options) {
        if (activity == null) {
            throw new NullPointerException("Argument 'activity' of type Activity (#0 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else if (intent == null) {
            throw new NullPointerException("Argument 'intent' of type Intent (#1 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            startActivity((Intent)intent, (Context)activity, (Bundle)options);
        }
    }

    public static void startActivity(@NonNull Activity activity, @NonNull Intent intent, View... sharedElements) {
        if (activity == null) {
            throw new NullPointerException("Argument 'activity' of type Activity (#0 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else if (intent == null) {
            throw new NullPointerException("Argument 'intent' of type Intent (#1 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            startActivity((Intent)intent, (Context)activity, (Bundle)getOptionsBundle(activity, sharedElements));
        }
    }

    public static void startActivity(@NonNull Activity activity, @NonNull Intent intent, @AnimRes int enterAnim, @AnimRes int exitAnim) {
        if (activity == null) {
            throw new NullPointerException("Argument 'activity' of type Activity (#0 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else if (intent == null) {
            throw new NullPointerException("Argument 'intent' of type Intent (#1 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            startActivity((Intent)intent, (Context)activity, (Bundle)getOptionsBundle((Context)activity, enterAnim, exitAnim));
            if (VERSION.SDK_INT < 16) {
                activity.overridePendingTransition(enterAnim, exitAnim);
            }

        }
    }

    public static void startActivityForResult(@NonNull Activity activity, @NonNull Class<? extends Activity> clz, int requestCode) {
        if (activity == null) {
            throw new NullPointerException("Argument 'activity' of type Activity (#0 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else if (clz == null) {
            throw new NullPointerException("Argument 'clz' of type Class<? extends Activity> (#1 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            startActivityForResult((Activity)activity, (Bundle)null, activity.getPackageName(), clz.getName(), requestCode, (Bundle)null);
        }
    }

    public static void startActivityForResult(@NonNull Activity activity, @NonNull Class<? extends Activity> clz, int requestCode, @Nullable Bundle options) {
        if (activity == null) {
            throw new NullPointerException("Argument 'activity' of type Activity (#0 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else if (clz == null) {
            throw new NullPointerException("Argument 'clz' of type Class<? extends Activity> (#1 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            startActivityForResult((Activity)activity, (Bundle)null, activity.getPackageName(), clz.getName(), requestCode, (Bundle)options);
        }
    }

    public static void startActivityForResult(@NonNull Activity activity, @NonNull Class<? extends Activity> clz, int requestCode, View... sharedElements) {
        if (activity == null) {
            throw new NullPointerException("Argument 'activity' of type Activity (#0 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else if (clz == null) {
            throw new NullPointerException("Argument 'clz' of type Class<? extends Activity> (#1 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            startActivityForResult((Activity)activity, (Bundle)null, activity.getPackageName(), clz.getName(), requestCode, (Bundle)getOptionsBundle(activity, sharedElements));
        }
    }

    public static void startActivityForResult(@NonNull Activity activity, @NonNull Class<? extends Activity> clz, int requestCode, @AnimRes int enterAnim, @AnimRes int exitAnim) {
        if (activity == null) {
            throw new NullPointerException("Argument 'activity' of type Activity (#0 out of 5, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else if (clz == null) {
            throw new NullPointerException("Argument 'clz' of type Class<? extends Activity> (#1 out of 5, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            startActivityForResult((Activity)activity, (Bundle)null, activity.getPackageName(), clz.getName(), requestCode, (Bundle)getOptionsBundle((Context)activity, enterAnim, exitAnim));
            if (VERSION.SDK_INT < 16) {
                activity.overridePendingTransition(enterAnim, exitAnim);
            }

        }
    }

    public static void startActivityForResult(@NonNull Bundle extras, @NonNull Activity activity, @NonNull Class<? extends Activity> clz, int requestCode) {
        if (extras == null) {
            throw new NullPointerException("Argument 'extras' of type Bundle (#0 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else if (activity == null) {
            throw new NullPointerException("Argument 'activity' of type Activity (#1 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else if (clz == null) {
            throw new NullPointerException("Argument 'clz' of type Class<? extends Activity> (#2 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            startActivityForResult((Activity)activity, (Bundle)extras, activity.getPackageName(), clz.getName(), requestCode, (Bundle)null);
        }
    }

    public static void startActivityForResult(@NonNull Bundle extras, @NonNull Activity activity, @NonNull Class<? extends Activity> clz, int requestCode, @Nullable Bundle options) {
        if (extras == null) {
            throw new NullPointerException("Argument 'extras' of type Bundle (#0 out of 5, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else if (activity == null) {
            throw new NullPointerException("Argument 'activity' of type Activity (#1 out of 5, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else if (clz == null) {
            throw new NullPointerException("Argument 'clz' of type Class<? extends Activity> (#2 out of 5, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            startActivityForResult(activity, extras, activity.getPackageName(), clz.getName(), requestCode, options);
        }
    }

    public static void startActivityForResult(@NonNull Bundle extras, @NonNull Activity activity, @NonNull Class<? extends Activity> clz, int requestCode, View... sharedElements) {
        if (extras == null) {
            throw new NullPointerException("Argument 'extras' of type Bundle (#0 out of 5, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else if (activity == null) {
            throw new NullPointerException("Argument 'activity' of type Activity (#1 out of 5, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else if (clz == null) {
            throw new NullPointerException("Argument 'clz' of type Class<? extends Activity> (#2 out of 5, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            startActivityForResult(activity, extras, activity.getPackageName(), clz.getName(), requestCode, getOptionsBundle(activity, sharedElements));
        }
    }

    public static void startActivityForResult(@NonNull Bundle extras, @NonNull Activity activity, @NonNull Class<? extends Activity> clz, int requestCode, @AnimRes int enterAnim, @AnimRes int exitAnim) {
        if (extras == null) {
            throw new NullPointerException("Argument 'extras' of type Bundle (#0 out of 6, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else if (activity == null) {
            throw new NullPointerException("Argument 'activity' of type Activity (#1 out of 6, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else if (clz == null) {
            throw new NullPointerException("Argument 'clz' of type Class<? extends Activity> (#2 out of 6, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            startActivityForResult(activity, extras, activity.getPackageName(), clz.getName(), requestCode, getOptionsBundle((Context)activity, enterAnim, exitAnim));
            if (VERSION.SDK_INT < 16) {
                activity.overridePendingTransition(enterAnim, exitAnim);
            }

        }
    }

    public static void startActivityForResult(@NonNull Bundle extras, @NonNull Activity activity, @NonNull String pkg, @NonNull String cls, int requestCode) {
        if (extras == null) {
            throw new NullPointerException("Argument 'extras' of type Bundle (#0 out of 5, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else if (activity == null) {
            throw new NullPointerException("Argument 'activity' of type Activity (#1 out of 5, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else if (pkg == null) {
            throw new NullPointerException("Argument 'pkg' of type String (#2 out of 5, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else if (cls == null) {
            throw new NullPointerException("Argument 'cls' of type String (#3 out of 5, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            startActivityForResult((Activity)activity, (Bundle)extras, pkg, cls, requestCode, (Bundle)null);
        }
    }

    public static void startActivityForResult(@NonNull Bundle extras, @NonNull Activity activity, @NonNull String pkg, @NonNull String cls, int requestCode, @Nullable Bundle options) {
        if (extras == null) {
            throw new NullPointerException("Argument 'extras' of type Bundle (#0 out of 6, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else if (activity == null) {
            throw new NullPointerException("Argument 'activity' of type Activity (#1 out of 6, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else if (pkg == null) {
            throw new NullPointerException("Argument 'pkg' of type String (#2 out of 6, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else if (cls == null) {
            throw new NullPointerException("Argument 'cls' of type String (#3 out of 6, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            startActivityForResult(activity, extras, pkg, cls, requestCode, options);
        }
    }

    public static void startActivityForResult(@NonNull Bundle extras, @NonNull Activity activity, @NonNull String pkg, @NonNull String cls, int requestCode, View... sharedElements) {
        if (extras == null) {
            throw new NullPointerException("Argument 'extras' of type Bundle (#0 out of 6, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else if (activity == null) {
            throw new NullPointerException("Argument 'activity' of type Activity (#1 out of 6, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else if (pkg == null) {
            throw new NullPointerException("Argument 'pkg' of type String (#2 out of 6, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else if (cls == null) {
            throw new NullPointerException("Argument 'cls' of type String (#3 out of 6, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            startActivityForResult(activity, extras, pkg, cls, requestCode, getOptionsBundle(activity, sharedElements));
        }
    }

    public static void startActivityForResult(@NonNull Bundle extras, @NonNull Activity activity, @NonNull String pkg, @NonNull String cls, int requestCode, @AnimRes int enterAnim, @AnimRes int exitAnim) {
        if (extras == null) {
            throw new NullPointerException("Argument 'extras' of type Bundle (#0 out of 7, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else if (activity == null) {
            throw new NullPointerException("Argument 'activity' of type Activity (#1 out of 7, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else if (pkg == null) {
            throw new NullPointerException("Argument 'pkg' of type String (#2 out of 7, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else if (cls == null) {
            throw new NullPointerException("Argument 'cls' of type String (#3 out of 7, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            startActivityForResult(activity, extras, pkg, cls, requestCode, getOptionsBundle((Context)activity, enterAnim, exitAnim));
            if (VERSION.SDK_INT < 16) {
                activity.overridePendingTransition(enterAnim, exitAnim);
            }

        }
    }

    public static void startActivityForResult(@NonNull Activity activity, @NonNull Intent intent, int requestCode) {
        if (activity == null) {
            throw new NullPointerException("Argument 'activity' of type Activity (#0 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else if (intent == null) {
            throw new NullPointerException("Argument 'intent' of type Intent (#1 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            startActivityForResult((Intent)intent, (Activity)activity, requestCode, (Bundle)null);
        }
    }

    public static void startActivityForResult(@NonNull Activity activity, @NonNull Intent intent, int requestCode, @Nullable Bundle options) {
        if (activity == null) {
            throw new NullPointerException("Argument 'activity' of type Activity (#0 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else if (intent == null) {
            throw new NullPointerException("Argument 'intent' of type Intent (#1 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            startActivityForResult(intent, activity, requestCode, options);
        }
    }

    public static void startActivityForResult(@NonNull Activity activity, @NonNull Intent intent, int requestCode, View... sharedElements) {
        if (activity == null) {
            throw new NullPointerException("Argument 'activity' of type Activity (#0 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else if (intent == null) {
            throw new NullPointerException("Argument 'intent' of type Intent (#1 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            startActivityForResult(intent, activity, requestCode, getOptionsBundle(activity, sharedElements));
        }
    }

    public static void startActivityForResult(@NonNull Activity activity, @NonNull Intent intent, int requestCode, @AnimRes int enterAnim, @AnimRes int exitAnim) {
        if (activity == null) {
            throw new NullPointerException("Argument 'activity' of type Activity (#0 out of 5, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else if (intent == null) {
            throw new NullPointerException("Argument 'intent' of type Intent (#1 out of 5, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            startActivityForResult(intent, activity, requestCode, getOptionsBundle((Context)activity, enterAnim, exitAnim));
            if (VERSION.SDK_INT < 16) {
                activity.overridePendingTransition(enterAnim, exitAnim);
            }

        }
    }

    public static void startActivityForResult(@NonNull Fragment fragment, @NonNull Class<? extends Activity> clz, int requestCode) {
        if (fragment == null) {
            throw new NullPointerException("Argument 'fragment' of type Fragment (#0 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else if (clz == null) {
            throw new NullPointerException("Argument 'clz' of type Class<? extends Activity> (#1 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            startActivityForResult((Fragment)fragment, (Bundle)null, Utils.getApp().getPackageName(), clz.getName(), requestCode, (Bundle)null);
        }
    }

    public static void startActivityForResult(@NonNull Fragment fragment, @NonNull Class<? extends Activity> clz, int requestCode, @Nullable Bundle options) {
        if (fragment == null) {
            throw new NullPointerException("Argument 'fragment' of type Fragment (#0 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else if (clz == null) {
            throw new NullPointerException("Argument 'clz' of type Class<? extends Activity> (#1 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            startActivityForResult((Fragment)fragment, (Bundle)null, Utils.getApp().getPackageName(), clz.getName(), requestCode, (Bundle)options);
        }
    }

    public static void startActivityForResult(@NonNull Fragment fragment, @NonNull Class<? extends Activity> clz, int requestCode, View... sharedElements) {
        if (fragment == null) {
            throw new NullPointerException("Argument 'fragment' of type Fragment (#0 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else if (clz == null) {
            throw new NullPointerException("Argument 'clz' of type Class<? extends Activity> (#1 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            startActivityForResult((Fragment)fragment, (Bundle)null, Utils.getApp().getPackageName(), clz.getName(), requestCode, (Bundle)getOptionsBundle(fragment, sharedElements));
        }
    }

    public static void startActivityForResult(@NonNull Fragment fragment, @NonNull Class<? extends Activity> clz, int requestCode, @AnimRes int enterAnim, @AnimRes int exitAnim) {
        if (fragment == null) {
            throw new NullPointerException("Argument 'fragment' of type Fragment (#0 out of 5, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else if (clz == null) {
            throw new NullPointerException("Argument 'clz' of type Class<? extends Activity> (#1 out of 5, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            startActivityForResult((Fragment)fragment, (Bundle)null, Utils.getApp().getPackageName(), clz.getName(), requestCode, (Bundle)getOptionsBundle(fragment, enterAnim, exitAnim));
        }
    }

    public static void startActivityForResult(@NonNull Bundle extras, @NonNull Fragment fragment, @NonNull Class<? extends Activity> clz, int requestCode) {
        if (extras == null) {
            throw new NullPointerException("Argument 'extras' of type Bundle (#0 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else if (fragment == null) {
            throw new NullPointerException("Argument 'fragment' of type Fragment (#1 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else if (clz == null) {
            throw new NullPointerException("Argument 'clz' of type Class<? extends Activity> (#2 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            startActivityForResult((Fragment)fragment, (Bundle)extras, Utils.getApp().getPackageName(), clz.getName(), requestCode, (Bundle)null);
        }
    }

    public static void startActivityForResult(@NonNull Bundle extras, @NonNull Fragment fragment, @NonNull Class<? extends Activity> clz, int requestCode, @Nullable Bundle options) {
        if (extras == null) {
            throw new NullPointerException("Argument 'extras' of type Bundle (#0 out of 5, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else if (fragment == null) {
            throw new NullPointerException("Argument 'fragment' of type Fragment (#1 out of 5, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else if (clz == null) {
            throw new NullPointerException("Argument 'clz' of type Class<? extends Activity> (#2 out of 5, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            startActivityForResult(fragment, extras, Utils.getApp().getPackageName(), clz.getName(), requestCode, options);
        }
    }

    public static void startActivityForResult(@NonNull Bundle extras, @NonNull Fragment fragment, @NonNull Class<? extends Activity> clz, int requestCode, View... sharedElements) {
        if (extras == null) {
            throw new NullPointerException("Argument 'extras' of type Bundle (#0 out of 5, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else if (fragment == null) {
            throw new NullPointerException("Argument 'fragment' of type Fragment (#1 out of 5, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else if (clz == null) {
            throw new NullPointerException("Argument 'clz' of type Class<? extends Activity> (#2 out of 5, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            startActivityForResult(fragment, extras, Utils.getApp().getPackageName(), clz.getName(), requestCode, getOptionsBundle(fragment, sharedElements));
        }
    }

    public static void startActivityForResult(@NonNull Bundle extras, @NonNull Fragment fragment, @NonNull Class<? extends Activity> clz, int requestCode, @AnimRes int enterAnim, @AnimRes int exitAnim) {
        if (extras == null) {
            throw new NullPointerException("Argument 'extras' of type Bundle (#0 out of 6, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else if (fragment == null) {
            throw new NullPointerException("Argument 'fragment' of type Fragment (#1 out of 6, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else if (clz == null) {
            throw new NullPointerException("Argument 'clz' of type Class<? extends Activity> (#2 out of 6, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            startActivityForResult(fragment, extras, Utils.getApp().getPackageName(), clz.getName(), requestCode, getOptionsBundle(fragment, enterAnim, exitAnim));
        }
    }

    public static void startActivityForResult(@NonNull Bundle extras, @NonNull Fragment fragment, @NonNull String pkg, @NonNull String cls, int requestCode) {
        if (extras == null) {
            throw new NullPointerException("Argument 'extras' of type Bundle (#0 out of 5, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else if (fragment == null) {
            throw new NullPointerException("Argument 'fragment' of type Fragment (#1 out of 5, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else if (pkg == null) {
            throw new NullPointerException("Argument 'pkg' of type String (#2 out of 5, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else if (cls == null) {
            throw new NullPointerException("Argument 'cls' of type String (#3 out of 5, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            startActivityForResult((Fragment)fragment, (Bundle)extras, pkg, cls, requestCode, (Bundle)null);
        }
    }

    public static void startActivityForResult(@NonNull Bundle extras, @NonNull Fragment fragment, @NonNull String pkg, @NonNull String cls, int requestCode, @Nullable Bundle options) {
        if (extras == null) {
            throw new NullPointerException("Argument 'extras' of type Bundle (#0 out of 6, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else if (fragment == null) {
            throw new NullPointerException("Argument 'fragment' of type Fragment (#1 out of 6, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else if (pkg == null) {
            throw new NullPointerException("Argument 'pkg' of type String (#2 out of 6, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else if (cls == null) {
            throw new NullPointerException("Argument 'cls' of type String (#3 out of 6, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            startActivityForResult(fragment, extras, pkg, cls, requestCode, options);
        }
    }

    public static void startActivityForResult(@NonNull Bundle extras, @NonNull Fragment fragment, @NonNull String pkg, @NonNull String cls, int requestCode, View... sharedElements) {
        if (extras == null) {
            throw new NullPointerException("Argument 'extras' of type Bundle (#0 out of 6, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else if (fragment == null) {
            throw new NullPointerException("Argument 'fragment' of type Fragment (#1 out of 6, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else if (pkg == null) {
            throw new NullPointerException("Argument 'pkg' of type String (#2 out of 6, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else if (cls == null) {
            throw new NullPointerException("Argument 'cls' of type String (#3 out of 6, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            startActivityForResult(fragment, extras, pkg, cls, requestCode, getOptionsBundle(fragment, sharedElements));
        }
    }

    public static void startActivityForResult(@NonNull Bundle extras, @NonNull Fragment fragment, @NonNull String pkg, @NonNull String cls, int requestCode, @AnimRes int enterAnim, @AnimRes int exitAnim) {
        if (extras == null) {
            throw new NullPointerException("Argument 'extras' of type Bundle (#0 out of 7, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else if (fragment == null) {
            throw new NullPointerException("Argument 'fragment' of type Fragment (#1 out of 7, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else if (pkg == null) {
            throw new NullPointerException("Argument 'pkg' of type String (#2 out of 7, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else if (cls == null) {
            throw new NullPointerException("Argument 'cls' of type String (#3 out of 7, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            startActivityForResult(fragment, extras, pkg, cls, requestCode, getOptionsBundle(fragment, enterAnim, exitAnim));
        }
    }

    public static void startActivityForResult(@NonNull Fragment fragment, @NonNull Intent intent, int requestCode) {
        if (fragment == null) {
            throw new NullPointerException("Argument 'fragment' of type Fragment (#0 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else if (intent == null) {
            throw new NullPointerException("Argument 'intent' of type Intent (#1 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            startActivityForResult((Intent)intent, (Fragment)fragment, requestCode, (Bundle)null);
        }
    }

    public static void startActivityForResult(@NonNull Fragment fragment, @NonNull Intent intent, int requestCode, @Nullable Bundle options) {
        if (fragment == null) {
            throw new NullPointerException("Argument 'fragment' of type Fragment (#0 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else if (intent == null) {
            throw new NullPointerException("Argument 'intent' of type Intent (#1 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            startActivityForResult(intent, fragment, requestCode, options);
        }
    }

    public static void startActivityForResult(@NonNull Fragment fragment, @NonNull Intent intent, int requestCode, View... sharedElements) {
        if (fragment == null) {
            throw new NullPointerException("Argument 'fragment' of type Fragment (#0 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else if (intent == null) {
            throw new NullPointerException("Argument 'intent' of type Intent (#1 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            startActivityForResult(intent, fragment, requestCode, getOptionsBundle(fragment, sharedElements));
        }
    }

    public static void startActivityForResult(@NonNull Fragment fragment, @NonNull Intent intent, int requestCode, @AnimRes int enterAnim, @AnimRes int exitAnim) {
        if (fragment == null) {
            throw new NullPointerException("Argument 'fragment' of type Fragment (#0 out of 5, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else if (intent == null) {
            throw new NullPointerException("Argument 'intent' of type Intent (#1 out of 5, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            startActivityForResult(intent, fragment, requestCode, getOptionsBundle(fragment, enterAnim, exitAnim));
        }
    }

    public static void startActivities(@NonNull Intent[] intents) {
        if (intents == null) {
            throw new NullPointerException("Argument 'intents' of type Intent[] (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            startActivities((Intent[])intents, (Context)UtilsBridge.getTopActivityOrApp(), (Bundle)null);
        }
    }

    public static void startActivities(@NonNull Intent[] intents, @Nullable Bundle options) {
        if (intents == null) {
            throw new NullPointerException("Argument 'intents' of type Intent[] (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            startActivities(intents, UtilsBridge.getTopActivityOrApp(), options);
        }
    }

    public static void startActivities(@NonNull Intent[] intents, @AnimRes int enterAnim, @AnimRes int exitAnim) {
        if (intents == null) {
            throw new NullPointerException("Argument 'intents' of type Intent[] (#0 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            Context context = UtilsBridge.getTopActivityOrApp();
            startActivities(intents, context, getOptionsBundle(context, enterAnim, exitAnim));
            if (VERSION.SDK_INT < 16 && context instanceof Activity) {
                ((Activity)context).overridePendingTransition(enterAnim, exitAnim);
            }

        }
    }

    public static void startActivities(@NonNull Activity activity, @NonNull Intent[] intents) {
        if (activity == null) {
            throw new NullPointerException("Argument 'activity' of type Activity (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else if (intents == null) {
            throw new NullPointerException("Argument 'intents' of type Intent[] (#1 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            startActivities((Intent[])intents, (Context)activity, (Bundle)null);
        }
    }

    public static void startActivities(@NonNull Activity activity, @NonNull Intent[] intents, @Nullable Bundle options) {
        if (activity == null) {
            throw new NullPointerException("Argument 'activity' of type Activity (#0 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else if (intents == null) {
            throw new NullPointerException("Argument 'intents' of type Intent[] (#1 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            startActivities((Intent[])intents, (Context)activity, options);
        }
    }

    public static void startActivities(@NonNull Activity activity, @NonNull Intent[] intents, @AnimRes int enterAnim, @AnimRes int exitAnim) {
        if (activity == null) {
            throw new NullPointerException("Argument 'activity' of type Activity (#0 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else if (intents == null) {
            throw new NullPointerException("Argument 'intents' of type Intent[] (#1 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            startActivities((Intent[])intents, (Context)activity, getOptionsBundle((Context)activity, enterAnim, exitAnim));
            if (VERSION.SDK_INT < 16) {
                activity.overridePendingTransition(enterAnim, exitAnim);
            }

        }
    }

    public static void startHomeActivity() {
        Intent homeIntent = new Intent("android.intent.action.MAIN");
        homeIntent.addCategory("android.intent.category.HOME");
        homeIntent.setFlags(268435456);
        startActivity(homeIntent);
    }

    public static void startLauncherActivity() {
        startLauncherActivity(Utils.getApp().getPackageName());
    }

    public static void startLauncherActivity(@NonNull String pkg) {
        if (pkg == null) {
            throw new NullPointerException("Argument 'pkg' of type String (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            String launcherActivity = getLauncherActivity(pkg);
            if (!TextUtils.isEmpty(launcherActivity)) {
                startActivity(pkg, launcherActivity);
            }
        }
    }

    public static List<Activity> getActivityList() {
        return UtilsBridge.getActivityList();
    }

    public static String getLauncherActivity() {
        return getLauncherActivity(Utils.getApp().getPackageName());
    }

    public static String getLauncherActivity(@NonNull String pkg) {
        if (pkg == null) {
            throw new NullPointerException("Argument 'pkg' of type String (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else if (UtilsBridge.isSpace(pkg)) {
            return "";
        } else {
            Intent intent = new Intent("android.intent.action.MAIN", (Uri)null);
            intent.addCategory("android.intent.category.LAUNCHER");
            intent.setPackage(pkg);
            PackageManager pm = Utils.getApp().getPackageManager();
            List<ResolveInfo> info = pm.queryIntentActivities(intent, 0);
            return info != null && info.size() != 0 ? ((ResolveInfo)info.get(0)).activityInfo.name : "";
        }
    }

    public static List<String> getMainActivities() {
        return getMainActivities(Utils.getApp().getPackageName());
    }

    public static List<String> getMainActivities(@NonNull String pkg) {
        if (pkg == null) {
            throw new NullPointerException("Argument 'pkg' of type String (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            List<String> ret = new ArrayList();
            Intent intent = new Intent("android.intent.action.MAIN", (Uri)null);
            intent.setPackage(pkg);
            PackageManager pm = Utils.getApp().getPackageManager();
            List<ResolveInfo> info = pm.queryIntentActivities(intent, 0);
            int size = info.size();
            if (size == 0) {
                return ret;
            } else {
                for(int i = 0; i < size; ++i) {
                    ResolveInfo ri = (ResolveInfo)info.get(i);
                    if (ri.activityInfo.processName.equals(pkg)) {
                        ret.add(ri.activityInfo.name);
                    }
                }

                return ret;
            }
        }
    }

    public static void goHome() {
        Intent home = new Intent(Intent.ACTION_MAIN);
        home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        home.addCategory(Intent.CATEGORY_HOME);
        startActivity(home);
    }

    public static Activity getTopActivity() {
        return UtilsBridge.getTopActivity();
    }

    public static boolean isActivityAlive(Context context) {
        return isActivityAlive(getActivityByContext(context));
    }

    public static boolean isActivityAlive(Activity activity) {
        return activity != null && !activity.isFinishing() && (VERSION.SDK_INT < 17 || !activity.isDestroyed());
    }

    public static boolean isActivityExistsInStack(@NonNull Activity activity) {
        if (activity == null) {
            throw new NullPointerException("Argument 'activity' of type Activity (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            List<Activity> activities = UtilsBridge.getActivityList();
            Iterator var2 = activities.iterator();

            Activity aActivity;
            do {
                if (!var2.hasNext()) {
                    return false;
                }

                aActivity = (Activity)var2.next();
            } while(!aActivity.equals(activity));

            return true;
        }
    }

    public static boolean isActivityExistsInStack(@NonNull Class<? extends Activity> clz) {
        if (clz == null) {
            throw new NullPointerException("Argument 'clz' of type Class<? extends Activity> (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            List<Activity> activities = UtilsBridge.getActivityList();
            Iterator var2 = activities.iterator();

            Activity aActivity;
            do {
                if (!var2.hasNext()) {
                    return false;
                }

                aActivity = (Activity)var2.next();
            } while(!aActivity.getClass().equals(clz));

            return true;
        }
    }

    public static void finishActivity(@NonNull Activity activity) {
        if (activity == null) {
            throw new NullPointerException("Argument 'activity' of type Activity (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            finishActivity(activity, false);
        }
    }

    public static void finishActivity(@NonNull Activity activity, boolean isLoadAnim) {
        if (activity == null) {
            throw new NullPointerException("Argument 'activity' of type Activity (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            activity.finish();
            if (!isLoadAnim) {
                activity.overridePendingTransition(0, 0);
            }

        }
    }

    public static void finishActivity(@NonNull Activity activity, @AnimRes int enterAnim, @AnimRes int exitAnim) {
        if (activity == null) {
            throw new NullPointerException("Argument 'activity' of type Activity (#0 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            activity.finish();
            activity.overridePendingTransition(enterAnim, exitAnim);
        }
    }

    public static void finishActivity(@NonNull Class<? extends Activity> clz) {
        if (clz == null) {
            throw new NullPointerException("Argument 'clz' of type Class<? extends Activity> (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            finishActivity(clz, false);
        }
    }

    public static void finishActivity(@NonNull Class<? extends Activity> clz, boolean isLoadAnim) {
        if (clz == null) {
            throw new NullPointerException("Argument 'clz' of type Class<? extends Activity> (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            List<Activity> activities = UtilsBridge.getActivityList();
            Iterator var3 = activities.iterator();

            while(var3.hasNext()) {
                Activity activity = (Activity)var3.next();
                if (activity.getClass().equals(clz)) {
                    activity.finish();
                    if (!isLoadAnim) {
                        activity.overridePendingTransition(0, 0);
                    }
                }
            }

        }
    }

    public static void finishActivity(@NonNull Class<? extends Activity> clz, @AnimRes int enterAnim, @AnimRes int exitAnim) {
        if (clz == null) {
            throw new NullPointerException("Argument 'clz' of type Class<? extends Activity> (#0 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            List<Activity> activities = UtilsBridge.getActivityList();
            Iterator var4 = activities.iterator();

            while(var4.hasNext()) {
                Activity activity = (Activity)var4.next();
                if (activity.getClass().equals(clz)) {
                    activity.finish();
                    activity.overridePendingTransition(enterAnim, exitAnim);
                }
            }

        }
    }

    public static boolean finishToActivity(@NonNull Activity activity, boolean isIncludeSelf) {
        if (activity == null) {
            throw new NullPointerException("Argument 'activity' of type Activity (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            return finishToActivity(activity, isIncludeSelf, false);
        }
    }

    public static boolean finishToActivity(@NonNull Activity activity, boolean isIncludeSelf, boolean isLoadAnim) {
        if (activity == null) {
            throw new NullPointerException("Argument 'activity' of type Activity (#0 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            List<Activity> activities = UtilsBridge.getActivityList();
            Iterator var4 = activities.iterator();

            while(var4.hasNext()) {
                Activity act = (Activity)var4.next();
                if (act.equals(activity)) {
                    if (isIncludeSelf) {
                        finishActivity(act, isLoadAnim);
                    }

                    return true;
                }

                finishActivity(act, isLoadAnim);
            }

            return false;
        }
    }

    public static boolean finishToActivity(@NonNull Activity activity, boolean isIncludeSelf, @AnimRes int enterAnim, @AnimRes int exitAnim) {
        if (activity == null) {
            throw new NullPointerException("Argument 'activity' of type Activity (#0 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            List<Activity> activities = UtilsBridge.getActivityList();
            Iterator var5 = activities.iterator();

            while(var5.hasNext()) {
                Activity act = (Activity)var5.next();
                if (act.equals(activity)) {
                    if (isIncludeSelf) {
                        finishActivity(act, enterAnim, exitAnim);
                    }

                    return true;
                }

                finishActivity(act, enterAnim, exitAnim);
            }

            return false;
        }
    }

    public static boolean finishToActivity(@NonNull Class<? extends Activity> clz, boolean isIncludeSelf) {
        if (clz == null) {
            throw new NullPointerException("Argument 'clz' of type Class<? extends Activity> (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            return finishToActivity(clz, isIncludeSelf, false);
        }
    }

    public static boolean finishToActivity(@NonNull Class<? extends Activity> clz, boolean isIncludeSelf, boolean isLoadAnim) {
        if (clz == null) {
            throw new NullPointerException("Argument 'clz' of type Class<? extends Activity> (#0 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            List<Activity> activities = UtilsBridge.getActivityList();
            Iterator var4 = activities.iterator();

            while(var4.hasNext()) {
                Activity act = (Activity)var4.next();
                if (act.getClass().equals(clz)) {
                    if (isIncludeSelf) {
                        finishActivity(act, isLoadAnim);
                    }

                    return true;
                }

                finishActivity(act, isLoadAnim);
            }

            return false;
        }
    }

    public static boolean finishToActivity(@NonNull Class<? extends Activity> clz, boolean isIncludeSelf, @AnimRes int enterAnim, @AnimRes int exitAnim) {
        if (clz == null) {
            throw new NullPointerException("Argument 'clz' of type Class<? extends Activity> (#0 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            List<Activity> activities = UtilsBridge.getActivityList();
            Iterator var5 = activities.iterator();

            while(var5.hasNext()) {
                Activity act = (Activity)var5.next();
                if (act.getClass().equals(clz)) {
                    if (isIncludeSelf) {
                        finishActivity(act, enterAnim, exitAnim);
                    }

                    return true;
                }

                finishActivity(act, enterAnim, exitAnim);
            }

            return false;
        }
    }

    public static void finishOtherActivities(@NonNull Class<? extends Activity> clz) {
        if (clz == null) {
            throw new NullPointerException("Argument 'clz' of type Class<? extends Activity> (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            finishOtherActivities(clz, false);
        }
    }

    public static void finishOtherActivities(@NonNull Class<? extends Activity> clz, boolean isLoadAnim) {
        if (clz == null) {
            throw new NullPointerException("Argument 'clz' of type Class<? extends Activity> (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            List<Activity> activities = UtilsBridge.getActivityList();
            Iterator var3 = activities.iterator();

            while(var3.hasNext()) {
                Activity act = (Activity)var3.next();
                if (!act.getClass().equals(clz)) {
                    finishActivity(act, isLoadAnim);
                }
            }

        }
    }

    public static void finishOtherActivities(@NonNull Class<? extends Activity> clz, @AnimRes int enterAnim, @AnimRes int exitAnim) {
        if (clz == null) {
            throw new NullPointerException("Argument 'clz' of type Class<? extends Activity> (#0 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            List<Activity> activities = UtilsBridge.getActivityList();
            Iterator var4 = activities.iterator();

            while(var4.hasNext()) {
                Activity act = (Activity)var4.next();
                if (!act.getClass().equals(clz)) {
                    finishActivity(act, enterAnim, exitAnim);
                }
            }

        }
    }

    public static void finishAllActivities() {
        finishAllActivities(false);
    }

    public static void finishAllActivities(boolean isLoadAnim) {
        List<Activity> activityList = UtilsBridge.getActivityList();
        Iterator var2 = activityList.iterator();

        while(var2.hasNext()) {
            Activity act = (Activity)var2.next();
            act.finish();
            if (!isLoadAnim) {
                act.overridePendingTransition(0, 0);
            }
        }

    }

    public static void finishAllActivities(@AnimRes int enterAnim, @AnimRes int exitAnim) {
        List<Activity> activityList = UtilsBridge.getActivityList();
        Iterator var3 = activityList.iterator();

        while(var3.hasNext()) {
            Activity act = (Activity)var3.next();
            act.finish();
            act.overridePendingTransition(enterAnim, exitAnim);
        }

    }

    public static void finishAllActivitiesExceptNewest() {
        finishAllActivitiesExceptNewest(false);
    }

    public static void finishAllActivitiesExceptNewest(boolean isLoadAnim) {
        List<Activity> activities = UtilsBridge.getActivityList();

        for(int i = 1; i < activities.size(); ++i) {
            finishActivity((Activity)activities.get(i), isLoadAnim);
        }

    }

    public static void finishAllActivitiesExceptNewest(@AnimRes int enterAnim, @AnimRes int exitAnim) {
        List<Activity> activities = UtilsBridge.getActivityList();

        for(int i = 1; i < activities.size(); ++i) {
            finishActivity((Activity)activities.get(i), enterAnim, exitAnim);
        }

    }

    public static Drawable getActivityIcon(@NonNull Activity activity) {
        if (activity == null) {
            throw new NullPointerException("Argument 'activity' of type Activity (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            return getActivityIcon(activity.getComponentName());
        }
    }

    public static Drawable getActivityIcon(@NonNull Class<? extends Activity> clz) {
        if (clz == null) {
            throw new NullPointerException("Argument 'clz' of type Class<? extends Activity> (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            return getActivityIcon(new ComponentName(Utils.getApp(), clz));
        }
    }

    public static Drawable getActivityIcon(@NonNull ComponentName activityName) {
        if (activityName == null) {
            throw new NullPointerException("Argument 'activityName' of type ComponentName (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            PackageManager pm = Utils.getApp().getPackageManager();

            try {
                return pm.getActivityIcon(activityName);
            } catch (NameNotFoundException var3) {
                var3.printStackTrace();
                return null;
            }
        }
    }

    public static Drawable getActivityLogo(@NonNull Activity activity) {
        if (activity == null) {
            throw new NullPointerException("Argument 'activity' of type Activity (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            return getActivityLogo(activity.getComponentName());
        }
    }

    public static Drawable getActivityLogo(@NonNull Class<? extends Activity> clz) {
        if (clz == null) {
            throw new NullPointerException("Argument 'clz' of type Class<? extends Activity> (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            return getActivityLogo(new ComponentName(Utils.getApp(), clz));
        }
    }

    public static Drawable getActivityLogo(@NonNull ComponentName activityName) {
        if (activityName == null) {
            throw new NullPointerException("Argument 'activityName' of type ComponentName (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        } else {
            PackageManager pm = Utils.getApp().getPackageManager();

            try {
                return pm.getActivityLogo(activityName);
            } catch (NameNotFoundException var3) {
                var3.printStackTrace();
                return null;
            }
        }
    }

    private static void startActivity(Context context, Bundle extras, String pkg, String cls, @Nullable Bundle options) {
        Intent intent = new Intent();
        if (extras != null) {
            intent.putExtras(extras);
        }

        intent.setComponent(new ComponentName(pkg, cls));
        startActivity(intent, context, options);
    }

    private static boolean startActivity(Intent intent, Context context, Bundle options) {
        if (!isIntentAvailable(intent)) {
            Log.e("ActivityUtils", "intent is unavailable");
            return false;
        } else {
            if (!(context instanceof Activity)) {
                intent.addFlags(268435456);
            }

            if (options != null && VERSION.SDK_INT >= 16) {
                context.startActivity(intent, options);
            } else {
                context.startActivity(intent);
            }

            return true;
        }
    }

    private static boolean isIntentAvailable(Intent intent) {
        return Utils.getApp().getPackageManager().queryIntentActivities(intent, 65536).size() > 0;
    }

    private static boolean startActivityForResult(Activity activity, Bundle extras, String pkg, String cls, int requestCode, @Nullable Bundle options) {
        Intent intent = new Intent();
        if (extras != null) {
            intent.putExtras(extras);
        }

        intent.setComponent(new ComponentName(pkg, cls));
        return startActivityForResult(intent, activity, requestCode, options);
    }

    private static boolean startActivityForResult(Intent intent, Activity activity, int requestCode, @Nullable Bundle options) {
        if (!isIntentAvailable(intent)) {
            Log.e("ActivityUtils", "intent is unavailable");
            return false;
        } else {
            if (options != null && VERSION.SDK_INT >= 16) {
                activity.startActivityForResult(intent, requestCode, options);
            } else {
                activity.startActivityForResult(intent, requestCode);
            }

            return true;
        }
    }

    private static void startActivities(Intent[] intents, Context context, @Nullable Bundle options) {
        if (!(context instanceof Activity)) {
            Intent[] var3 = intents;
            int var4 = intents.length;

            for(int var5 = 0; var5 < var4; ++var5) {
                Intent intent = var3[var5];
                intent.addFlags(268435456);
            }
        }

        if (options != null && VERSION.SDK_INT >= 16) {
            context.startActivities(intents, options);
        } else {
            context.startActivities(intents);
        }

    }

    private static boolean startActivityForResult(Fragment fragment, Bundle extras, String pkg, String cls, int requestCode, @Nullable Bundle options) {
        Intent intent = new Intent();
        if (extras != null) {
            intent.putExtras(extras);
        }

        intent.setComponent(new ComponentName(pkg, cls));
        return startActivityForResult(intent, fragment, requestCode, options);
    }

    private static boolean startActivityForResult(Intent intent, Fragment fragment, int requestCode, @Nullable Bundle options) {
        if (!isIntentAvailable(intent)) {
            Log.e("ActivityUtils", "intent is unavailable");
            return false;
        } else if (fragment.getActivity() == null) {
            Log.e("ActivityUtils", "Fragment " + fragment + " not attached to Activity");
            return false;
        } else {
            if (options != null && VERSION.SDK_INT >= 16) {
                fragment.startActivityForResult(intent, requestCode, options);
            } else {
                fragment.startActivityForResult(intent, requestCode);
            }

            return true;
        }
    }

    private static Bundle getOptionsBundle(Fragment fragment, int enterAnim, int exitAnim) {
        Activity activity = fragment.getActivity();
        return activity == null ? null : ActivityOptionsCompat.makeCustomAnimation(activity, enterAnim, exitAnim).toBundle();
    }

    private static Bundle getOptionsBundle(Context context, int enterAnim, int exitAnim) {
        return ActivityOptionsCompat.makeCustomAnimation(context, enterAnim, exitAnim).toBundle();
    }

    private static Bundle getOptionsBundle(Fragment fragment, View[] sharedElements) {
        Activity activity = fragment.getActivity();
        return activity == null ? null : getOptionsBundle((Activity)activity, sharedElements);
    }

    private static Bundle getOptionsBundle(Activity activity, View[] sharedElements) {
        if (VERSION.SDK_INT < 21) {
            return null;
        } else if (sharedElements == null) {
            return null;
        } else {
            int len = sharedElements.length;
            if (len <= 0) {
                return null;
            } else {
                Pair<View, String>[] pairs = new Pair[len];

                for(int i = 0; i < len; ++i) {
                    pairs[i] = Pair.create(sharedElements[i], sharedElements[i].getTransitionName());
                }

                return ActivityOptionsCompat.makeSceneTransitionAnimation(activity, pairs).toBundle();
            }
        }
    }
}
