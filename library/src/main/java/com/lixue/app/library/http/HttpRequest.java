package com.lixue.app.library.http;

import com.lixue.app.library.http.okhttp.OkHttpManager;

import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by enlong on 2017/1/22.
 */

public abstract class HttpRequest {
    private String url;
    private HashMap<String, String> entry;
    public static final int METHOD_GET = 1;
    public static final int METHOD_POST = 2;
    private int curMethod = METHOD_POST;

    public HttpRequest(String utr) {
        this.url = url;
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


    public String encodeParameters(HashMap entry) {

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
        URL url = new URL(tmpurl.toString());

//        // 如果是wap连接
//        if (NetUtil.isCmwap(context)) {
//            url = new URL(pUrl);
//            request.addHeader("X-Online-Host", urlt);
//
//        } else {
//            url = new URL(tmpurl.toString());
//        }

        request.url(url);
        Response response = OkHttpManager.getInstance().excute(request.build());

        return new  RequestResult(response);
    }


    public RequestResult doTask() throws IOException {
        if (this.curMethod == METHOD_GET) {
            return requestByGet(url);
        } else {
            return requestByPost(url);
        }

    }

    private RequestResult requestByPost(String url) {
        return null;
    }
}
