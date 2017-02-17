package cn.jianke.jkchat.httprequest;

import java.io.IOException;
import java.util.Map;
import java.util.Stack;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @className: JkChatRequest
 * @classDescription: 健客聊天数据请求
 * @author: leibing
 * @createTime: 2017/2/17
 */
public class JkChatRequest {
    // sington
    private static JkChatRequest instance;
    // OkHttpClient实例
    private OkHttpClient mOkHttpClient;
    // Call堆栈
    private static Stack<Call> mCallStack;

    /**
     * Constructor
     * @author leibing
     * @createTime 2017/2/17
     * @lastModify 2017/2/17
     * @param
     * @return
     */
    private JkChatRequest(){
        mOkHttpClient = new OkHttpClient();
    }

    /**
     * sington
     * @author leibing
     * @createTime 2017/2/17
     * @lastModify 2017/2/17
     * @param
     * @return
     */
    public static JkChatRequest getInstance(){
        if (instance == null){
            synchronized (JkChatRequest.class){
                instance = new JkChatRequest();
            }
        }

        return instance;
    }

    /**
     * 添加Call到堆栈
     * @author leibing
     * @createTime 2017/2/17
     * @lastModify 2017/2/17
     * @param mCall Call实例
     * @return
     */
    public void addCall(Call mCall){
        if (mCall == null)
            return;
        if(mCallStack == null){
            mCallStack = new Stack<Call>();
        }
        mCallStack.add(mCall);
    }

    /**
     * 从堆栈移除Call
     * @author leibing
     * @createTime 2017/2/17
     * @lastModify 2017/2/17
     * @param mCall Call实例
     * @return
     */
    public void removeCall(Call mCall){
        if (mCallStack != null && mCall != null){
            mCallStack.remove(mCall);
        }
    }

    /**
     * 取消所有的请求
     * @author leibing
     * @createTime 2017/2/17
     * @lastModify 2017/2/17
     * @param
     * @return
     */
    public void cancelAllRequest(){
        if (mCallStack == null)
            return;
        // 遍历取消请求
        for (int i=0;i<mCallStack.size();i++){
            Call mCall = mCallStack.get(i);
            mCall.cancel();
        }
    }

    /**
     * 异步get请求数据
     * @author leibing
     * @createTime 2017/2/17
     * @lastModify 2017/2/17
     * @param requestUrl 请求url
     * @param mCallBack 回调
     * @return
     */
    public void getAsynHttp(String requestUrl, final ResponseCallBack mCallBack){
        // 判断OkHttpClient是否为空，若为空则重新实例化
        if (mOkHttpClient == null)
            mOkHttpClient = new OkHttpClient();
        // 构造请求Builder
        Request.Builder requestBuilder = new Request.Builder().url(requestUrl);
        // 生成请求
        Request request = requestBuilder.build();
        // 生成Call对象
        final Call mCall= mOkHttpClient.newCall(request);
        // 添加Call到堆栈
        addCall(mCall);
        // 开始异步请求
        mCall.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // 从堆栈移除Call
                removeCall(mCall);
                // 请求失败回调
                if (mCallBack != null)
                    mCallBack.onFailure(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                // 从堆栈移除Call
                removeCall(mCall);
                // 请求成功回调
                if (mCallBack != null)
                    mCallBack.onSuccess(response);
            }
        });
    }

    /**
     * 异步post请求数据
     * @author leibing
     * @createTime 2017/2/17
     * @lastModify 2017/2/17
     * @param requestUrl 请求url
     * @param paramMap 参数map
     * @param mCallBack 回调
     * @return
     */
    public void postAsynHttp(String requestUrl, Map<String,String> paramMap,
                             final ResponseCallBack mCallBack){
        // 判断OkHttpClient是否为空，若为空则重新实例化
        if (mOkHttpClient == null)
            mOkHttpClient = new OkHttpClient();
        // 生成构建
        FormBody.Builder mBuild = new FormBody.Builder();
        // 遍历参数map
        for (String key: paramMap.keySet()){
            mBuild.add(key, paramMap.get(key));
        }
        // 生成FromBody
        FormBody formBody = mBuild.build();
        // 生成请求
        Request request = new Request.Builder()
                .url(requestUrl)
                .post(formBody)
                .build();
        // 生成Call对象
        final Call mCall = mOkHttpClient.newCall(request);
        // 添加Call到堆栈
        addCall(mCall);
        // 开始请求
        mCall.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // 从堆栈移除Call
                removeCall(mCall);
                // 请求失败回调
                if (mCallBack != null)
                    mCallBack.onFailure(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                // 从堆栈移除Call
                removeCall(mCall);
                // 请求成功回调
                if (mCallBack != null)
                    mCallBack.onSuccess(response);
            }
        });
    }

    /**
     * @interfaceName: ResponseCallBack
     * @interfaceDescription: 响应回调
     * @author: leibing
     * @createTime: 2017/2/17
     */
    public interface ResponseCallBack{
        // 请求成功
        void onSuccess(Response response);
        // 请求失败
        void onFailure(Exception e);
    }
}
