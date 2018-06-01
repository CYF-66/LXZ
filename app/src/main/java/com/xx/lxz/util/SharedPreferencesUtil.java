package com.xx.lxz.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Base64;
import android.util.Log;

import com.xx.lxz.config.GlobalConfig;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 缓存工具类
 * @author pc
 *
 */
public class SharedPreferencesUtil implements Serializable{
	
    private static final long serialVersionUID = 1L;

//    private static final String sharedPreferencesInfo = "lxz";

    private static Context myContext;

    private static SharedPreferences saveInfo;

    private static Editor saveEditor;

    private static SharedPreferencesUtil sharedPreferencesUtil = new SharedPreferencesUtil();

    public static void init(Context context) {
        myContext = context;
    }

    public static SharedPreferencesUtil getinstance(Context context) {
        myContext = context;
        if (saveInfo == null && myContext != null) {
            saveInfo = myContext.getSharedPreferences(GlobalConfig.CATEGORY_NAME_SHAREDPREFERENCE_KEY, Context.MODE_PRIVATE);
            saveEditor = saveInfo.edit();
        }
        return sharedPreferencesUtil;
    }

    public static SharedPreferencesUtil getInstance() {
        if (saveInfo == null && myContext != null) {
            saveInfo = myContext.getSharedPreferences(GlobalConfig.CATEGORY_NAME_SHAREDPREFERENCE_KEY, Context.MODE_PRIVATE);
            saveEditor = saveInfo.edit();
        }
        return sharedPreferencesUtil;
    }

    public boolean isContainKey(String key) {
        return saveInfo.contains(key);
    }

    public String getString(String key) {
        return saveInfo.getString(key, "");
    }

    public String getString(String key, String defaultValue) {
        return saveInfo.getString(key, defaultValue);
    }

    public long getlong(String key) {
        return saveInfo.getLong(key, 0);
    }

    public boolean setlong(String key, long time) {
        if (saveInfo.contains(key)) {
            saveEditor.remove(key);
        }
        saveEditor.putLong(key, time);
        return saveEditor.commit();
    }

    @SuppressWarnings("unchecked")
	public HashMap<String, String> getAll() {
        return (HashMap<String, String>) saveInfo.getAll();
    }

    public boolean setString(String key, String value) {
        if (saveInfo.contains(key)) {
            saveEditor.remove(key);
        }
        saveEditor.putString(key, value);
        return saveEditor.commit();
    }
    public boolean setString(String key, Serializable value) {
        if (saveInfo.contains(key)) {
            saveEditor.remove(key);
        }
//        saveEditor.putString(key, value);
        saveEditor.putString(key, value.toString());
        return saveEditor.commit();
    }

    public boolean setFloat(String key, float value) {
        if (saveInfo.contains(key)) {
            saveEditor.remove(key);
        }
        saveEditor.putFloat(key,value);
//        saveEditor.putString(key, value);
        return saveEditor.commit();
    }
    public float getFloat(String key) {
        return saveInfo.getFloat(key, 0);
    }
    public boolean setInt(String key, int value) {
        if (saveInfo.contains(key)) {
            saveEditor.remove(key);
        }
        saveEditor.putInt(key, value);
        return saveEditor.commit();
    }

    public int getInt(String key) {
        return saveInfo.getInt(key, 0);
    }

    public int getInt(String key, int defaultValue) {
        return saveInfo.getInt(key, defaultValue);
    }

    public boolean clearItem(String key) {
        saveEditor.remove(key);
        return saveEditor.commit();
    }

    public boolean setBoolean(String key, boolean value) {
        if (saveInfo.contains(key)) {
            saveEditor.remove(key);
        }
        saveEditor.putBoolean(key, value);
        return saveEditor.commit();
    }

    public boolean getBoolean(String key) {
        return saveInfo.getBoolean(key, false);
    }
    /**
     * 记住用户名和密码
     *
     * @param key  value
     * @return
     */
    public boolean keepLogin(String key, boolean value) {
        if (saveInfo.contains(key)) {
            saveEditor.remove(key);
        }
        saveEditor.putBoolean(key, value);
        return saveEditor.commit();
    }

    /**
     * 判断是否记住用户名和密码
     *
     * @param key
     * @return
     */
    public  boolean readIsKeepLogin(String key) {
        return saveInfo.getBoolean(key, false);
    }

    /**
     * 保存集合
     *
     * @param msg
     * @param context
     * @param sp_list
     */
    public static void saveStringList(Context context,
                                 List<String> sp_list,String key,String msg) {
        @SuppressWarnings("static-access")
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                "Product", context.MODE_PRIVATE);
        Editor editor = sharedPreferences.edit();
        sp_list = getStringList(context,key);
        if (null == sp_list) {
            sp_list = new ArrayList<String>();
        }
        sp_list.add(msg);
        Log.i("TEST", "保存成功！！！！sp_list=" + sp_list);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(sp_list);
            String strList = new String(Base64.encode(baos.toByteArray(),
                    Base64.DEFAULT));
            editor.putString(key, strList);
            editor.commit();
            Log.i("TEST", "保存成功！！！！");
            oos.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                baos.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取集合
     *
     * @param context
     */
    @SuppressWarnings({ "unchecked", "static-access" })
    public static List<String> getStringList(Context context,String key) {
        List<String> list;
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                "Product", context.MODE_PRIVATE);
        String message = sharedPreferences.getString(key, "");
        byte[] buffer = Base64.decode(message.getBytes(), Base64.DEFAULT);
        ByteArrayInputStream bais = new ByteArrayInputStream(buffer);
        try {
            ObjectInputStream ois = new ObjectInputStream(bais);
            list = (List<String>) ois.readObject();
            ois.close();
            Log.i("TEST", "取出成功！！！！");
            return list;
        } catch (StreamCorruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (EOFException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                bais.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return null;
    }
	
}
