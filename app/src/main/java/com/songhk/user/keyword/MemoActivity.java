package com.songhk.user.keyword;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MemoActivity extends AppCompatActivity {
    Intent it;
    int id, isMemo = 0;
    TextView memo_word;
    EditText memo_edit;
    Button btn_save;

    DBController dbController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo);

        dbController = new DBController(getApplicationContext(), "Keyword.db", null, 1);
        it = getIntent();

        memo_word = (TextView)findViewById(R.id.memo_word);
        memo_edit = (EditText)findViewById(R.id.memo_edit);
        btn_save = (Button)findViewById(R.id.btn_save);

        id = it.getIntExtra("id",0);
        memo_word.setText(it.getStringExtra("word"));

        if(dbController.selectMemo(id) != null){
            btn_save.setText("수정");
            isMemo = 1;
            memo_edit.setText(dbController.selectMemo(id));
        }

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isMemo==0){ //새 메모면 저장
                    dbController.insertMemo(id,memo_edit.getText().toString());
                    Toast.makeText(getApplicationContext(),"메모를 저장하였습니다.",Toast.LENGTH_SHORT).show();
                }else{ //아니면 수정
                    dbController.updateMemo(id,memo_edit.getText().toString());
                    Toast.makeText(getApplicationContext(),"메모를 수정하였습니다.",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void back(View v){
        finish();
    }
}
