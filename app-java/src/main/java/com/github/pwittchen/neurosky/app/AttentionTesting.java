package com.github.pwittchen.neurosky.app;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.github.pwittchen.neurosky.library.NeuroSky;
import com.github.pwittchen.neurosky.library.exception.BluetoothNotEnabledException;
import com.github.pwittchen.neurosky.library.listener.ExtendedDeviceMessageListener;
import com.github.pwittchen.neurosky.library.message.enums.BrainWave;
import com.github.pwittchen.neurosky.library.message.enums.Signal;
import com.github.pwittchen.neurosky.library.message.enums.State;
import java.util.Locale;
import java.util.Set;

//MainActivity類別實作AppCompatActivity 介面
public class AttentionTesting extends AppCompatActivity {

    private final static String LOG_TAG = "NeuroSky";
    public static String test ="神奇專注力數值";
    private NeuroSky neuroSky;

    @BindView(R.id.tv_state) TextView tvState;
    @BindView(R.id.tv_attention) TextView tvAttention;
    @BindView(R.id.tv_meditation) TextView tvMeditation;
//    @BindView(R.id.tv_blink) TextView tvBlink;
//    @BindView(R.id.attentionResult) TextView attentionResult;
//  TextView aR=(TextView) findViewById(R.id.attentionResult);



    //執行的程式碼在這
    //覆寫AppCompatActivity 的run()方法
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //super:存取父類別的成員
        //一旦一個 Activity 物件開始執行，Android 系統就會呼叫該物件的 onCreate() 方法
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        //路徑：src/main/res/layout/activity_attention_testing.xml
        setContentView(R.layout.activity_attention_testing);
//    Toast.makeText(AttentionTestIntro.this,"Firebase connection sucess",Toast.LENGTH_LONG).show();
        ButterKnife.bind(this);

        neuroSky = createNeuroSky();
    }

    //開始測試
    @Override protected void onResume() {

        super.onResume();
        if (neuroSky != null && neuroSky.isConnected()) {
            neuroSky.start();
        }
    }

    //停止測試
    @Override protected void onPause() {
        super.onPause();
        if (neuroSky != null && neuroSky.isConnected()) {

            neuroSky.stop();
        }
    }

    @NonNull private NeuroSky createNeuroSky() {

        return new NeuroSky(new ExtendedDeviceMessageListener() {
            @Override public void onStateChange(State state) {
                handleStateChange(state);
            }

            @Override public void onSignalChange(Signal signal) {
                handleSignalChange(signal);
            }

            @Override public void onBrainWavesChange(Set<BrainWave> brainWaves) {
                handleBrainWavesChange(brainWaves);
            }
        });
    }
    //STATE_CHANGE: 1(正在連線) 2(練線成功) 0(停止連線)
    private void handleStateChange(final State state) {
        if (neuroSky != null && state.equals(State.CONNECTED)) {
            neuroSky.start();
        }

        tvState.setText(state.toString());
        Log.d(LOG_TAG, state.toString());
    }

    //將變化的數值丟到介面上
    //它這邊回是一個物件，從上面傳進來的（但找不到初始化的地方，猜在很裡面的地方ExtendedDeviceMessageListener）

    private void handleSignalChange(final Signal signal) {
        //src:libary/src/main/java/message/enums/Signal

        switch (signal) {
            case ATTENTION:
//        Log.v("attention",ATTENTION);
                //將得到的值回傳到xml檔對應的id
                int temp=signal.getTotal_value()/signal.getCount_value();
                tvAttention.setText(getFormattedMessage("專注力數值: %d", signal));
                //add
                signal.setCount_value(signal.getCount_value()+1);
                signal.setTotal_value((signal.getTotal_value()+signal.getValue()));
                test=String.valueOf(signal.getTotal_value()/signal.getCount_value());
                Log.d("總專注力數值",String.valueOf(signal.getTotal_value()));
                Log.d("總讀取次數",String.valueOf(signal.getCount_value()));
                Log.d("平均專注力",String.valueOf(signal.getTotal_value()/signal.getCount_value()));

                break;
            case MEDITATION:
                tvMeditation.setText(getFormattedMessage("冥想數值: %d", signal));
                break;
//     case BLINK:
//        tvBlink.setText(getFormattedMessage("blink: %d", signal));
//        break;
        }

//腦波儀的ATTENTION、MEDITATION
//    Log.d(LOG_TAG, String.format("%s: %d", signal.toString(), signal.getValue()));
//    Log.d("測試",String.valueOf(signal.getTotal_value()));
//    Log.d("測2",String.valueOf(signal.getCount_value()));

    }

    //messageFormat是「meditation: %d」 /signal是「MEDITATION」
    private String getFormattedMessage(String messageFormat, Signal signal) {
        return String.format(Locale.getDefault(), messageFormat, signal.getValue());
    }







    //腦波儀a b 那些波不重要，而且我根本沒帶也有欸
    private void handleBrainWavesChange(final Set<BrainWave> brainWaves) {
        //for(:) 取出brainWaves的所有元素
        //
        for (BrainWave brainWave : brainWaves) {
//        Log.d(LOG_TAG, String.format("%s: %d ", brainWave.toString(), brainWave.getValue()));

        }
    }




    @OnClick(R.id.btn_connect) void connect() {
        try {
            neuroSky.connect();
        } catch (BluetoothNotEnabledException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.d(LOG_TAG, e.getMessage());
        }
    }

    @OnClick(R.id.btn_start_monitoring) void startMonitoring() {
        neuroSky.start();
    }

    @OnClick(R.id.btn_stop_monitoring) void stopMonitoring() {
        neuroSky.stop();
//傳值到下一頁
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.activity_attention_test_alert);

        dialog.show(); //顯示對話框

//        Intent intent = new Intent();
//        intent.putExtra("attention", test);
//        intent.setClass(AttentionTesting.this , AttentionTestResult.class);
//        startActivity(intent);
    }

//    @OnClick(R.id.btn_disconnect) void disconnect() {
//        neuroSky.disconnect();
//    }

}