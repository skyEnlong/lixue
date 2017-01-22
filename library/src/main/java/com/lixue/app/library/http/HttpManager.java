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
        Flowable.create(new FlowableOnSubscribe<String>() {
            @Override
            public void subscribe(final FlowableEmitter<String> subscriber) throws Exception {
                dealWithSubscriber(request, subscriber);
            }
        }, BackpressureStrategy.LATEST)
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onSubscribe(Subscription s) {

                    }

                    @Override
                    public void onNext(String s) {
                        if (null != handler) handler.onResponse(s);
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
        Flowable.create(new FlowableOnSubscribe<String>() {
            @Override
            public void subscribe(final FlowableEmitter<String> subscriber) throws Exception {
                dealWithSubscriber(request, subscriber);
            }
        }, BackpressureStrategy.LATEST)
                .subscribeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onSubscribe(Subscription s) {

                    }

                    @Override
                    public void onNext(String s) {
                        if (null != handler) handler.onResponse(s);
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

    private void dealWithSubscriber(final HttpRequest request, FlowableEmitter<String> subscriber) throws IOException, JSONException {
        RequestResult result = request.doTask();
        boolean isValid = checkTokenValid(result);
        if (isValid) {
            String s = result.asString();
            JSONObject jo = new JSONObject(s);

            int code = jo.getInt("status");
            if (code != 200) {
                subscriber.onError(new Exception(jo.getString("msg")));
            } else {
            }
            subscriber.onNext(jo.getString("data"));
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
            subscriber.onNext(jo.getString("data"));
        }

        subscriber.onComplete();
    }

    /**
     * do rquest, call back in ui thread
     * @param request
     */
    public void doTask(final HttpRequest request, final Subscriber<String> s) {
        Flowable.create(new FlowableOnSubscribe<String>() {
            @Override
            public void subscribe(FlowableEmitter<String> subscriber) throws Exception {
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
            //// TODO: 2017/1/22
        }

        return isValid;
    }

    ;
}
