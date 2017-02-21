package cn.jianke.jkchat.data.Shareperferences;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import java.lang.ref.WeakReference;

/**
 * @className: PatientShareperferences
 * @classDescription: 就诊人轻量储存数据
 * @author: leibing
 * @createTime: 2017/2/21
 */
public class PatientShareperferences {
    // 就诊人
    private final static String PATIENT_PREF = "patient";
    // 就诊人id
    private final static String PATIENT_ID = "patientId";
    // 就诊人名称
    private final static String PATIENT_NAME = "name";
    // 就诊人性别
    private final static String PATIENT_SEX = "sex";
    // 就诊人年龄
    private final static String PATIENT_AGE = "age";
    // 就诊人SharedPreferences
    private SharedPreferences mPatientSp;
    // sington
    private static PatientShareperferences instance;

    /**
     * Construction
     * @author leibing
     * @createTime 2017/2/21
     * @lastModify 2017/2/21
     * @param
     * @return
     */
    private PatientShareperferences(Context context){
        WeakReference<Context> mContextWeakRef = new WeakReference<Context>(context);
        if (mContextWeakRef != null && mContextWeakRef.get() != null){
            mPatientSp = mContextWeakRef.get().getSharedPreferences(PATIENT_PREF,
                    Activity.MODE_PRIVATE);
        }
    }

    /**
     * sington
     * @author leibing
     * @createTime 2017/2/21
     * @lastModify 2017/2/21
     * @param
     * @return
     */
    public static PatientShareperferences getInstance(Context context){
        if (instance == null){
            synchronized(PatientShareperferences.class){
                if (instance == null)
                    instance = new PatientShareperferences(context);
            }
        }
        return instance;
    }

    /**
     * 获取就诊人id
     * @author leibing
     * @createTime 2017/2/21
     * @lastModify 2017/2/21
     * @param
     * @return
     */
    public String getPatientId(){
        synchronized (instance) {
            if (mPatientSp != null) {
                return mPatientSp.getString(PATIENT_ID, null);
            }
            return null;
        }
    }

    /**
     * 获取就诊人名称
     * @author leibing
     * @createTime 2017/2/21
     * @lastModify 2017/2/21
     * @param
     * @return
     */
    public String getPatientName(){
        synchronized (instance) {
            if (mPatientSp != null) {
                return mPatientSp.getString(PATIENT_NAME, null);
            }
            return null;
        }
    }

    /**
     * 获取就诊人性别
     * @author leibing
     * @createTime 2017/2/21
     * @lastModify 2017/2/21
     * @param
     * @return
     */
    public String getPatientSex(){
        synchronized (instance) {
            if (mPatientSp != null) {
                return mPatientSp.getString(PATIENT_SEX, null);
            }
            return null;
        }
    }

    /**
     * 获取就诊人年龄
     * @author leibing
     * @createTime 2017/2/21
     * @lastModify 2017/2/21
     * @param
     * @return
     */
    public String getPatientAge(){
        synchronized (instance) {
            if (mPatientSp != null) {
                return mPatientSp.getString(PATIENT_AGE, null);
            }
            return null;
        }
    }
}