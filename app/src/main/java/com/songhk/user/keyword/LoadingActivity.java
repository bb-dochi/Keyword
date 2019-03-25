package com.songhk.user.keyword;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class LoadingActivity extends Activity {

    ProgressBar androidProgressBar;
    int progressStatusCounter = 0;
    TextView textView;
    Handler progressHandler = new Handler();
    String line;
    SharedPreferences pref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        androidProgressBar = (ProgressBar) findViewById(R.id.horizontal_progress_bar);
        textView = (TextView) findViewById(R.id.textView);

        //어플 첫 실행 체크
        pref = getSharedPreferences("isFirst", Activity.MODE_PRIVATE);
        boolean first = pref.getBoolean("isFirst", false);
        if(first==false){
            Log.d("Is first Time?", "first");

            Toast.makeText(this,"단어를 다운 받는 중입니다. 기다려주세요.",Toast.LENGTH_LONG).show();
            //txt파일 >>> database
            getData();

        }else{//첫실행이 아닐때
            Log.d("Is first Time?", "not first");
            androidProgressBar.setVisibility(View.GONE);
            textView.setVisibility(View.GONE);
            startLoading();
        }
    }

    public void startLoading(){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent it = new Intent(getBaseContext(),MainActivity.class);
                startActivity(it);
                finish();
            }
        },2000);
    }

    public void getData(){

        new Thread(new Runnable() {
            public void run() {
                while (progressStatusCounter < 100) {
                    DBController dbController = new DBController(getApplicationContext(), "Keyword.db", null, 1);
                    try{
                        // 데이터 파일 읽기 raw 폴더의 keyword.txt
                        BufferedReader bfRead = new BufferedReader(new InputStreamReader(getResources().openRawResource(R.raw.keyword)));

                        // 한줄씩 NULL이 아닐때까지 읽어 rLine 배열에 넣는다
                        int line_count = 0;
                        while ((line = bfRead.readLine()) != null){
                            dbController.insert(line);
                            line_count++;

                            //읽는 수 만큼 ProgressBar 증가
                            progressStatusCounter += 1;
                            progressHandler.post(new Runnable() {
                                public void run() {
                                    androidProgressBar.setProgress(progressStatusCounter);
                                    //Status update in textview
                                    textView.setText(progressStatusCounter + "/" + androidProgressBar.getMax()); }});
                        }
                        bfRead.close();

                        //데이터를 다 받아야 첫 실행 한걸로 인정
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putBoolean("isFirst",true);
                        editor.commit();

                        //메인으로 넘어가기
                        Intent it = new Intent(getBaseContext(),MainActivity.class);
                        startActivity(it);
                        finish();
                    }catch(Exception e){
                        e.printStackTrace();;
                    }
                }
            }
        }).start();
    }
}
