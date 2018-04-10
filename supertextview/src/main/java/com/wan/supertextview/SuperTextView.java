package com.wan.supertextview;

import android.animation.LayoutTransition;
import android.content.Context;
import android.content.res.TypedArray;
import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.Timer;
import java.util.TimerTask;


/**
 * Created by xen on 2018/4/7 0007.
 */

public class SuperTextView extends AppCompatTextView {
    private Context context;

    private String text="";
    private int count=1;
    private String signal;
    private int typeStartTime,typeTime;
    private boolean openAudio;
    private MediaPlayer player;
    public SuperTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        this.context = context;
        TypedArray typearray = context.obtainStyledAttributes(attrs,R.styleable.SuperTextView);
        text = typearray.getString(R.styleable.SuperTextView_text);
        signal = typearray.getString(R.styleable.SuperTextView_typeSignal);
        if (signal==null){
            signal="";
        }
        typeStartTime = typearray.getInt(R.styleable.SuperTextView_typeStartTime,400);
        typeTime = typearray.getInt(R.styleable.SuperTextView_typeTime,500);
        openAudio = typearray.getBoolean(R.styleable.SuperTextView_OpenTypeAudio,false);
        setText(text);
        typearray.recycle();

    }

    public void startShow(){

        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                post(new Runnable() {
                    @Override
                    public void run() {
                        if (text.length()!=0){

                            if (count!=text.length()+1){
                                openAudio(openAudio);
                                setText(text.substring(0,count)+signal);
                                postInvalidate();
                                count++;
                            }else{
                                setText(text.substring(0,count-1));
                                player.pause();
                                player.stop();
                                player.release();
                                postInvalidate();
                                count=1;
                                setVisibility(GONE);
                                timer.cancel();
                            }
                        }

                    }
                });
            }
        },typeStartTime,typeTime);
    }

    private void openAudio(boolean openAudio) {
        if (openAudio){
            player = MediaPlayer.create(context,R.raw.type);


            player.start();
            player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {

                    player.start();
                    player.setLooping(true);
                }
            });
        }
    }

    /**
     *
     * @param typeStartTime 开始打印的延迟时间（经过多长的时间开始打印）
     * @param typeTime 打印每个字的间隔时间
     */
    public void startShow(@NonNull int typeStartTime, @NonNull int typeTime){
        this.typeTime = typeTime;
        this.typeStartTime = typeStartTime;
        startShow();
    }

    /**
     *
     * @param v 根布局的实例（通过findviewbyid找到）
     * @param duration 经过多少秒消失
     */
    public void hide(View v,int duration){

        ViewGroup viewgroup =(ViewGroup)v;
        LayoutTransition transition = new LayoutTransition();

        transition.setDuration(duration);
        viewgroup.setLayoutTransition(transition);
    }
}