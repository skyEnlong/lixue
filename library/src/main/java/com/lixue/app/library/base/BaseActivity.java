package com.lixue.app.library.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.lixue.app.library.http.ResJsonString;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by enlong on 2017/1/20.
 */

public class BaseActivity extends AppCompatActivity implements Subscriber<ResJsonString>{
    protected List<Subscription> subscription = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(null != subscription && subscription.size() > 0){
            for(Subscription s : subscription){
                s.cancel();
            }
            subscription.clear();
            subscription = null;
        }
    }

    @Override
    public void onSubscribe(Subscription s) {
        this.subscription.add(s);
    }

    @Override
    public void onNext(ResJsonString o) {
        ////to do update
    }

    @Override
    public void onError(Throwable t) {
        showMsg(t.getMessage());
    }

    @Override
    public void onComplete() {

    }

    public void showMsg(String str){
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }

}
