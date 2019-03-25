package com.songhk.user.keyword;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class NewWordActivity extends AppCompatActivity {
    TextView word;
    RelativeLayout layout_word;
    ImageView bookMark;
    int currentIndex;
    DBController dbController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_word);

        dbController = new DBController(getApplicationContext(), "Keyword.db", null, 1);

        word = (TextView)findViewById(R.id.word);
        layout_word = (RelativeLayout)findViewById(R.id.layout_word);
        bookMark = (ImageView)findViewById(R.id.bookMark);

        Toast toast = Toast.makeText(this,"충분히 생각하고 넘겨보세요!",Toast.LENGTH_LONG);
        toast.setGravity(Gravity.TOP,0,350);
        toast.show();

        setWord();
    }

    public void tabWord(View v){
        setWord();
    }

    public void setWord(){
        Random rnd = new Random();
        currentIndex = rnd.nextInt(5888); //0~5888까지 랜덤으로 (나중에 바꾸기)

        word.setText(dbController.select(currentIndex+1));

        //즐겨찾기 되어있던 단어면 채워진 별 이미지로 세팅
        if(dbController.isMarked(currentIndex+1)){
            bookMark.setImageResource(R.drawable.star_full);
        }else{
        bookMark.setImageResource(R.drawable.star_border);
    }
    }

    public void addMark(View v){ //즐겨찾기 추가 및 삭제
        if(dbController.isMarked(currentIndex+1)){
            bookMark.setImageResource(R.drawable.star_border);
        }else{
            Toast.makeText(this,"즐겨찾기에 추가하였습니다.",Toast.LENGTH_SHORT).show();
            bookMark.setImageResource(R.drawable.star_full);
        }

        dbController.addMark(currentIndex+1);
    }

    public void back(View v){
        finish();
    }
}
