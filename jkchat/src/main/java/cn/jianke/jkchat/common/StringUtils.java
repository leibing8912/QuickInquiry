package cn.jianke.jkchat.common;

import org.json.JSONObject;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @className: StringUtils
 * @classDescription: 字符串操作
 * @author: leibing
 * @createTime: 2016/08/30
 */
public class StringUtils {

	/**
	 * 判断是否为null或空字符串
	 * @author leibing
	 * @createTime 2016/08/30
	 * @lastModify 2016/08/30
	 * @param str
	 * @return
	 */
    public static boolean isEmpty(String str) {
        if (str == null || "".equals(str.trim()) || str.trim().equalsIgnoreCase("null")) {
            return true;
        }
        return false;
    }

	/**
	 * 判断是否不为null或不是空字符串
	 * @author leibing
	 * @createTime 2016/08/30
	 * @lastModify 2016/08/30
	 * @param str
	 * @return
	 */
    public static boolean isNotEmpty(String str){
		if (str == null || str.trim().equals("") || str.trim().equalsIgnoreCase("null"))
			return false;
		return true;
    }

	/**
	 * 根据类名获取对象实例
	 * @author leibing
	 * @createTime 2016/08/30
	 * @lastModify 2016/08/30
	 * @param className 类名
	 * @return
	 */
	public static Object getObject(String className){
		Object object = null;
		if(StringUtils.isNotEmpty(className)){
			try {
				object = Class.forName(className).newInstance();
			}catch(ClassNotFoundException cnf) {
			}
			catch(InstantiationException ie) {
			}
			catch(IllegalAccessException ia) {
			}
		}
		return object;
	}

	/**
	 * 字符串是否数字
	 * @author leibing
	 * @createTime 2016/11/17
	 * @lastModify 2016/11/17
	 * @param
	 * @return
	 */
	public static boolean strIsNum(String str){
		// 判断是否为空
		if (StringUtils.isEmpty(str))
			return false;
		// 去空格
		str = str.trim();
		// 匹配
		Pattern pattern = Pattern.compile("[0-9]*");
		Matcher isNum = pattern.matcher(str);
		if( !isNum.matches() ) {
			return false;
		}
		return true;
	}

	/**
	 * 将map数据转出为JSON字符串
	 * @author leibing
	 * @createTime 2017/2/20
	 * @lastModify 2017/2/20
	 * @param map 参数名-值
	 * @return JSON数据
	 * @return
	 */
	public static String mapToJson(Map<String,String> map ){
		JSONObject json = new JSONObject();
		try{
			for(Map.Entry<String,String> entry : map.entrySet()){
				json.put(entry.getKey(), entry.getValue());
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return json.toString();
	}
}
