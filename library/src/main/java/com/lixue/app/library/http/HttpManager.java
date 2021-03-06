package com.lixue.app.library.http;

import org.json.JSONException;
import org.json.JSONObject;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.io.IOException;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by enlong on 2017/1/22.
 */

public class HttpManager {
    private static HttpManager instance;
    private Object lock;

    public static HttpManager getInstance() {
        if (instance == null) {
            synchronized (HttpManager.class) {
                if (instance == null) {
                    instance = new HttpManager();
                }
            }
        }
        return instance;
    }


    /**
     * call back in background
     * @param request
     * @param handler
     */
    public void doTaskInBackGround(final HttpRequest request, final IHttpResponse handler) {
        Flowable.create(new FlowableOnSubscribe<ResJsonString>() {
            @Override
            public void subscribe(final FlowableEmitter<ResJsonString> subscriber) throws Exception {
                dealWithSubscriber(request, subscriber);
            }
        }, BackpressureStrategy.LATEST)
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<ResJsonString>() {
                    @Override
                    public void onSubscribe(Subscription s) {

                    }

                    @Override
                    public void onNext(ResJsonString s) {
                        if (null != handler) handler.onResponse(s.requestUrl, s.resultString);
                    }

                    @Override
                    public void onError(Throwable t) {
                        if (null != handler) handler.onErr(t.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }


    /**
     * call back in background
     * @param request
     * @param handler
     */
    public void doTaskInMainThread(final HttpRequest request, final IHttpResponse handler) {
        Flowable.create(new FlowableOnSubscribe<ResJsonString>() {
            @Override
            public void subscribe(final FlowableEmitter<ResJsonString> subscriber) throws Exception {
                dealWithSubscriber(request, subscriber);
            }
        }, BackpressureStrategy.LATEST)
                .observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ResJsonString>() {
                    @Override
                    public void onSubscribe(Subscription s) {

                    }

                    @Override
                    public void onNext(ResJsonString s) {
                        if (null != handler) handler.onResponse(s.requestUrl, s.resultString);
                    }

                    @Override
                    public void onError(Throwable t) {
                        if (null != handler) handler.onErr(t.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    private void dealWithSubscriber(final HttpRequest request, FlowableEmitter<ResJsonString> subscriber) throws IOException, JSONException {
        RequestResult result = request.doTask();
        boolean isValid = checkTokenValid(result);
        if (isValid || !request.isNeedToken) {
            String s = result.asString();
            JSONObject jo = new JSONObject(s);

            int code = jo.getInt("status");
            if (code != 200) {
                subscriber.onError(new Exception(jo.getString("msg")));
            } else {
            }

            ResJsonString data = new ResJsonString();
            data.requestUrl = request.getUrl();
            data.resultString = jo.getString("data");
            subscriber.onNext(data);
        } else {
            //// TODO: 2017/1/22 to refhresh token

            //then do request again
            result = request.doTask();
            String s = result.asString();
            JSONObject jo = new JSONObject(s);

            int code = jo.getInt("status");
            if (code != 200) {
                subscriber.onError(new Exception(jo.getString("msg")));
            } else {

            }
            ResJsonString data = new ResJsonString();
            data.requestUrl = request.getUrl();
            data.resultString = jo.getString("data");
            subscriber.onNext(data);
        }

        subscriber.onComplete();
    }

    /**
     * do rquest, call back in ui thread
     * @param request
     */
    public void doTask(final HttpRequest request, final Subscriber<ResJsonString> s) {
        Flowable.create(new FlowableOnSubscribe<ResJsonString>() {
            @Override
            public void subscribe(FlowableEmitter<ResJsonString> subscriber) throws Exception {
                dealWithSubscriber(request, subscriber);
            }
        }, BackpressureStrategy.BUFFER).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(s);


    }

    /**
     * check token
     * @param result
     * @return
     */
    private synchronized boolean checkTokenValid(RequestResult result) {
        boolean isValid = true;
        if (result.getStatusCode() == 200) {
            isValid = true;
        } else {
            //// TODO: 2017/1/22  update token ,
           isValid = false;

        }

        return isValid;
    }


}
