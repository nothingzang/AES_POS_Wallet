package com.example.aes_pos_wallet.utils;

import android.annotation.TargetApi;
import android.os.Build;

import com.example.aes_pos_wallet.activitys.BaseActivity;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ActivityManager {

    public static BaseActivity get() {
        return activities.get(activities.keySet().toArray()[activities.keySet().size() - 1]);
    }


    /**
     * 存放activity的列表
     */
    public static HashMap<Class<?>, BaseActivity> activities = new LinkedHashMap<>();

    /**
     * 添加Activity
     *
     * @param activity
     */
    public static void addActivity(BaseActivity activity, Class<?> clz) {
        activities.put(clz, activity);
    }

    /**
     * 判断一个Activity 是否存在
     *
     * @param clz
     * @return
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static <T extends BaseActivity> boolean isActivityExist(Class<T> clz) {
        boolean res;
        BaseActivity activity = getActivity(clz);
        if (activity == null) {
            res = false;
        } else {
            if (activity.isFinishing() || activity.isDestroyed()) {
                res = false;
            } else {
                res = true;
            }
        }

        return res;
    }

    /**
     * 获得指定activity实例
     *
     * @param clazz Activity 的类对象
     * @return
     */
    public static <T extends BaseActivity> T getActivity(Class<T> clazz) {
        return (T) activities.get(clazz);
    }

    /**
     * 移除activity,代替finish
     *
     * @param activity
     */
    public static void removeActivity(BaseActivity activity) {
        if (activities.containsValue(activity)) {
            activities.remove(activity.getClass());
        }
    }

    /**
     * 移除activity,代替finish
     *
     * @param clazz
     */
    public static <T extends BaseActivity> void exit(Class<T> clazz) {
        T activity = getActivity(clazz);
        if (activity == null) return;
        if (activities.containsValue(activity)) {
            if (!activity.isFinishing()) {
                activity.finish();
            }
            activities.remove(activity.getClass());
        }
    }

    /**
     * 移除所有的Activity
     */
    public static synchronized void removeAllActivity() {
        if (activities != null && activities.size() > 0) {
            Map<Class<?>, BaseActivity> map = new ConcurrentHashMap<Class<?>, BaseActivity>();
            map.putAll(activities);
            for (Map.Entry<Class<?>, BaseActivity> entry : map.entrySet()) {
                Class key = entry.getKey();
                if (!map.get(key).isFinishing()) {
                    map.get(key).finish();
                }
            }
            activities.clear();
        }
    }


}
