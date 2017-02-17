package com.lixue.app.library.http;

import android.content.Context;

import com.lixue.app.library.http.okhttp.OkHttpManager;
import com.lixue.app.library.util.NetUtil;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by enlong on 2017/1/22.
 */

public class HttpRequest {
    private String url;
    private HashMap<String, String> entry;
    public static final int METHOD_GET = 1;
    public static final int METHOD_POST = 2;
    private int curMethod = METHOD_POST;
    private Context mContext;
    public  boolean isNeedToken = true;

    public HttpRequest(Context mContext, String url) {
        this.url = url;
        this.mContext = mContext;
    }


    public String getUrl(){
        return url;
    }
    public void setEntry(HashMap<String, String> entry) {
        this.entry = entry;
    }

    public void addParams(String key, String value) {
        if (null == entry) {
            entry = new HashMap<>();
        }
        entry.put(key, value);
    }

    public void setMethod(int method) {
        this.curMethod = method;
    }


    public String encodeParameters(HashMap<String, String> entry) {

        if (null == entry) return null;

        StringBuffer buf = new StringBuffer();

        Set<Map.Entry<String, String>> es = entry.entrySet();
        int size = es.size();
        int index = 0;
        try {
            for (Map.Entry<String, String> e : es) {
                buf.append(URLEncoder.encode(e.getKey(), "UTF-8"))
                        .append("=")
                        .append(URLEncoder.encode(null == e.getValue() ? "" : e.getValue()));

                if (index != (size - 1)) {
                    buf.append("&");
                }

                index++;
            }

        } catch (Exception e) {
        }

        return buf.toString();

    }

    public RequestResult requestByGet(String urlPath) throws IOException {
        StringBuilder tmpurl = new StringBuilder();
        tmpurl.append(urlPath);
        if (urlPath.indexOf("?") < 0) {
            tmpurl.append("?");
        }
        String paraStr = encodeParameters(entry);
        tmpurl.append(paraStr);

        String urlt = tmpurl.toString();
        String pUrl = "https://10.0.0.172";
        urlt = urlt.replace("https://", "");
        pUrl = pUrl + urlt.substring(urlt.indexOf("/"));
        urlt = urlt.substring(0, urlt.indexOf("/"));

        Request.Builder request = new Request.Builder();
        // 新建一个URL对象
        URL url = null;

//        // 如果是wap连接
        if (NetUtil.isCmwap(mContext)) {
            url = new URL(pUrl);
            request.addHeader("X-Online-Host", urlt);

        } else {
            url = new URL(tmpurl.toString());
        }

        request.url(url);
        Response response = OkHttpManager.getInstance().excute(request.build());

        return new RequestResult(response);
    }


    public RequestResult doTask() throws IOException {
        if (this.curMethod == METHOD_GET) {
            return requestByGet(url);
        } else {
            return requestByPost(url);
        }

    }


    private RequestResult requestByPost(String urlPath) throws MalformedURLException {
        String paraStr = encodeParameters(entry);
         // 新建一个URL对象
        String pUrl = "https://10.0.0.172";
        String urlt = urlPath.toString();
        urlt = urlt.replace("https://", "");
        pUrl = pUrl + urlt.substring(urlt.indexOf("/"));
        urlt = urlt.substring(0, urlt.indexOf("/"));
        URL url;
        Request.Builder request = new Request.Builder();

        // 如果是wap连接
        if (NetUtil.isCmwap(mContext)) {
            url = new URL(pUrl);
            request.addHeader("X-Online-Host", urlt);

        } else {
            url = new URL(urlPath);
        }

        request.url(url);

        addHeader(request);

        request.post(RequestBody.create(
                MediaType.parse("application/x-www-form-urlencode; charset=utf-8"),
                paraStr));


        return null;
    }

    public void addHeader(Request.Builder request) {
        request.header("Cache-Control", "max-age=0");
        request.header("Content-Type","application/x-www-form-urlencode");
        request.header("Accept-Encoding", "gzip");
    }
}
