package com.lixue.app.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lixue.app.R;

import antistatic.spinnerwheel.AbstractWheel;
import antistatic.spinnerwheel.OnWheelScrollListener;
import antistatic.spinnerwheel.adapters.AbstractWheelTextAdapter;

/**
 * Created by workEnlong on 2015/2/9.
 * 统一的滚轮选择dialog，最大支持三个滚轮，可支持一位小数
 */
public class CommonWheelDialog extends Dialog {
    protected Context mContext;
    private Button btnSure;
    private Button btnCancel;
    private TextView title;
    private LinearLayout baseView;

    public AbstractWheel wheelLeft;
    public AbstractWheel wheelCenter;
    public AbstractWheel wheelRight;

    public AbstractWheelTextAdapter adapterLeft;
    public AbstractWheelTextAdapter adapterMid;
    public AbstractWheelTextAdapter adapterRight;

    private TextView dot_view;
    private OnWheelSelecetInterface mOnWheelSlecetInterface;

    private TextView lableLeft;
    private TextView lableRight;

    public interface OnWheelSelecetInterface
    {
        public void onValuesSelect(int[] indexs, String[] values);

    }

    public CommonWheelDialog(Context mContext){
        super(mContext, R.style.DialogMainFullScreen);
        this.mContext = mContext;
        setCanceledOnTouchOutside(true);


        baseView =  (LinearLayout) LayoutInflater.from(mContext).inflate(
                R.layout.lib_wheel_select_layout, null);
        setContentView(baseView, new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        getWindow().setWindowAnimations(
                R.style.path_popwindow_anim_enterorout_window);
        setWidthAndHeight(mContext.getResources().getDisplayMetrics().widthPixels,
                0) ;
        setGravity(Gravity.BOTTOM);

        initDialog();
    }

    public void addOnWheelScrollListener(OnWheelScrollListener mOnWheelScrollListener){
        wheelLeft.addScrollingListener(mOnWheelScrollListener);
        wheelCenter.addScrollingListener(mOnWheelScrollListener);
        wheelRight.addScrollingListener(mOnWheelScrollListener);
    }

    private void setWidthAndHeight(int width, int height) {
        Window win = getWindow();
        WindowManager.LayoutParams ll = win.getAttributes();
        if(0 != width){

            ll.width = width;
        }

        if(0 != height){

            ll.height = height;
        }
        win.setAttributes(ll);
    }

    private void initDialog() {
        // TODO Auto-generated method stub
        btnCancel = (Button) baseView.findViewWithTag("cancel");
        btnSure = (Button) baseView.findViewWithTag("ok");

        title = (TextView) baseView.findViewWithTag("title");
        dot_view = (TextView) baseView.findViewWithTag("dot_view");

        wheelLeft = (AbstractWheel) baseView.findViewById(R.id.common_wheel_left);
        wheelCenter = (AbstractWheel) baseView.findViewById(R.id.common_wheel_mid);
        wheelRight = (AbstractWheel) baseView.findViewById(R.id.common_wheel_right);

        wheelLeft.setItemScaleSytle(true);
        wheelCenter.setItemScaleSytle(true);
        wheelRight.setItemScaleSytle(true);

        wheelLeft.setShowSplitLine(true);
        wheelCenter.setShowSplitLine(true);
        wheelRight.setShowSplitLine(true);

        lableLeft = (TextView) baseView.findViewById(R.id.label_left);
        lableRight = (TextView) baseView.findViewById(R.id.label_right);


        wheelLeft.addScrollingListener(new OnWheelScrollListener() {
            @Override
            public void onScrollingStarted(AbstractWheel wheel) {
                wheelLeft.invalidateItemsLayout(false);
            }

            @Override
            public void onScrollingFinished(AbstractWheel wheel) {
                wheelLeft.invalidateItemsLayout(false);
            }
        });

        wheelCenter.addScrollingListener(new OnWheelScrollListener() {
            @Override
            public void onScrollingStarted(AbstractWheel wheel) {
                wheelCenter.invalidateItemsLayout(false);
            }

            @Override
            public void onScrollingFinished(AbstractWheel wheel) {
                wheelCenter.invalidateItemsLayout(false);
            }
        });


        wheelRight.addScrollingListener(new OnWheelScrollListener() {
            @Override
            public void onScrollingStarted(AbstractWheel wheel) {
                wheelRight.invalidateItemsLayout(false);
            }

            @Override
            public void onScrollingFinished(AbstractWheel wheel) {
                wheelRight.invalidateItemsLayout(false);
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                cancel();
            }
        });

        btnSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if(null != mOnWheelSlecetInterface){
                    String[] values = new String[3];
                    int[] index = new int[3];

                    if(null !=  adapterLeft){
                        values[0] = adapterLeft.getItemText(wheelLeft.getCurrentItem()).toString();
                        index[0] = wheelLeft.getCurrentItem();
                    }

                    if(null !=  adapterMid){
                        values[1] = adapterMid.getItemText(wheelCenter.getCurrentItem()).toString();
                        index[1] = wheelCenter.getCurrentItem();
                    }

                    if(null !=  adapterRight){
                        index[2] = wheelRight.getCurrentItem();
                        values[2] = adapterRight.getItemText(wheelRight.getCurrentItem()).toString();
                    }
                    mOnWheelSlecetInterface.onValuesSelect(index, values);

                }
            }
        });

    }

    /**
     * 设置当前索引， 必须先初始化适配器{@link #setAdapters}
     * @param index0
     * @param index1
     * @param index2
     * @param isAnima
     */
    public void setCurDatas(int index0, int index1, int index2, boolean isAnima){
        wheelLeft.setCurrentItem(index0, isAnima);
        wheelCenter.setCurrentItem(index1, isAnima);
        wheelRight.setCurrentItem(index2, isAnima);
    }


    /**
     * 设置适配器
     * @param adapterLeft
     * @param adapterMid
     * @param adapterRight
     */
    public void setAdapters(AbstractWheelTextAdapter adapterLeft,
                            AbstractWheelTextAdapter adapterMid, AbstractWheelTextAdapter adapterRight){
        this.adapterLeft = adapterLeft;
        this.adapterMid = adapterMid;
        this.adapterRight = adapterRight;

        if(null != adapterLeft){
            wheelLeft.setVisibility(View.VISIBLE);
            ((View)(wheelLeft.getParent())).setVisibility(View.VISIBLE);
            adapterLeft.setAbstractWheel(wheelLeft);
        }else{
            wheelLeft.setVisibility(View.GONE);
            ((View)(wheelLeft.getParent())).setVisibility(View.GONE);
        }
        wheelLeft.setViewAdapter(adapterLeft);

        if(null != adapterMid){
            wheelCenter.setVisibility(View.VISIBLE);
            adapterMid.setAbstractWheel(wheelCenter);
            ((View)(wheelCenter.getParent())).setVisibility(View.VISIBLE);
        }else{
            wheelCenter.setVisibility(View.GONE);
            ((View)(wheelCenter.getParent())).setVisibility(View.GONE);
        }

        wheelCenter.setViewAdapter(adapterMid);

        if(null != adapterRight){
            wheelRight.setVisibility(View.VISIBLE);
            adapterRight.setAbstractWheel(wheelRight);
            ((View)(wheelRight.getParent())).setVisibility(View.VISIBLE);
        }else{
            wheelRight.setVisibility(View.GONE);
            ((View)(wheelRight.getParent())).setVisibility(View.GONE);
        }
        wheelRight.setViewAdapter(adapterRight);

    }

    public void setDotViewShow(boolean isShow){
        setDotViewShow(isShow, ".");
    }

    /**
     * 显示并设置中间分隔符，
     * @param isShow
     * @param txt
     */
    public void setDotViewShow(boolean isShow, String txt){
        int visible = (isShow) ? View.VISIBLE : View.GONE;
        dot_view.setVisibility(visible);
        dot_view.setText(txt);
        dot_view.setTextColor(mContext.getResources().getColor(R.color.black_2));
        dot_view.setBackgroundColor(Color.WHITE);
    }

    public void setGravity(int gravity){
        Window win = getWindow();
        WindowManager.LayoutParams ll = win.getAttributes();
        ll.gravity = gravity;
        win.setAttributes(ll);
    }


    /**
     * 初始化单个适配器
     * @param context
     * @param adapter
     */
    public static void initBaseAdapterView(Context context, AbstractWheelTextAdapter adapter){
        if(null == adapter) return;
        adapter
                .setItemResource(R.layout.wheel_common_text_layout);
        adapter.setItemTextResource(R.id.text);
        adapter.setTextColor(context.getResources().getColor(R.color.black_2));
        adapter.setSelectionTextColor(context.getResources().getColor(R.color.green));
        adapter.setTextSize(14);
        adapter.setSelectionTextSize((int)(19));
    }


    @Override
    public void setTitle(CharSequence title_string) {
        // TODO Auto-generated method stub
        this.title.setText(title_string);
    }

    @Override
    public void show(){
        super.show();
        baseView.invalidate();
    }

    /**
     * 设置选择回调
     * @param onWheelSlecetInterface
     */
    public void setOnWheelSelecetListener(OnWheelSelecetInterface onWheelSlecetInterface){
       this.mOnWheelSlecetInterface = onWheelSlecetInterface;
    }

    /**
     *显示适配器的label
     * @param params
     */
    public void setLables(String... params){
        lableLeft.setText(params[0]);
        lableRight.setText(params[1]);
    }
}
