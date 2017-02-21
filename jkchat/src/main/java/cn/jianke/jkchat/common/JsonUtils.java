package cn.jianke.jkchat.common;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.ArrayList;
import java.util.List;

/**
 * @className: JsonUtils
 * @classDescription: json工具类
 * @author: leibing
 * @createTime: 2017/2/21
 */
public class JsonUtils {

    /**
     * List 转为 JSON
     * @author leibing
     * @createTime 2017/2/21
     * @lastModify 2017/2/21
     * @param list 列表
     * @return
     */
    public static <T> String list2Json(ArrayList<T> list) {
        if(null != list && list.size() > 0){
            Gson gson = new Gson();
            return gson.toJson(list);
        }
        return "";
    }

    /**
     * JSON 转换为 List
     * @author leibing
     * @createTime 2017/2/21
     * @lastModify 2017/2/21
     * @param jsonStr json字符串
     * @return
     */
    public static <T> List<T> json2List(String jsonStr){
        if (StringUtils.isNotEmpty(jsonStr)) {
            Gson gson = new Gson();
            List<T> list = gson.fromJson(jsonStr, new TypeToken<ArrayList<T>>(){}.getType());
            return list;
        }
        return null;
    }
}
