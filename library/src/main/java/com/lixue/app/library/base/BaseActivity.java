package com.lixue.app.library.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

/**
 * Created by enlong on 2017/1/20.
 */

public class BaseActivity extends AppCompatActivity implements Subscriber<String>{
    protected Subscription subscription;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(null != subscription) subscription.cancel();
    }

    @Override
    public void onSubscribe(Subscription s) {
        this.subscription = s;
    }

    @Override
    public void onNext(String o) {
        ////to do update
    }

    @Override
    public void onError(Throwable t) {
        Toast.makeText(this, t.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onComplete() {

    }
}
