package com.songhk.user.keyword;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class SaveWordActivity extends AppCompatActivity {
    ListView wordlist;
    DBController dbController;
    ListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_word);

        dbController = new DBController(getApplicationContext(), "Keyword.db", null, 1);
        adapter = new ListAdapter(dbController.getFavorite()); //즐겨찾기 목록 어댑터 세팅

        wordlist = (ListView)findViewById(R.id.wordlist);
        wordlist.setAdapter(adapter);

        wordlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListWordItem item = (ListWordItem)adapter.getItem(position);
                Intent it = new Intent(getApplicationContext(),MemoActivity.class);
                System.out.println("아이디"+item.getId()+"/단어"+item.getWord());
                it.putExtra("id",item.getId());
                it.putExtra("word",item.getWord());
                startActivity(it);
            }
        });

    }

    public void back(View v){
        finish();
    }
}
