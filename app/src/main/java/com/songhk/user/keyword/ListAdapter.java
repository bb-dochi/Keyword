package com.songhk.user.keyword;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ListAdapter extends BaseAdapter {
    ArrayList<ListWordItem> wordlistArray = new ArrayList<ListWordItem>();

    public ListAdapter(ArrayList<ListWordItem> wordlistArray) {
        this.wordlistArray = wordlistArray;
    }

    @Override
    public int getCount() {
        return wordlistArray.size();
    }

    @Override
    public ListWordItem getItem(int position) {
        return wordlistArray.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void delItem(Context context, int position){
        DBController dbController = new DBController(context, "Keyword.db", null, 1);
        //db에서 즐겨찾기 삭제
        dbController.addMark(wordlistArray.get(position).getId());

        //어댑터에서 삭제
        wordlistArray.remove(position);
        notifyDataSetChanged();
        Toast.makeText(context,"즐겨찾기에서 삭제되었습니다.",Toast.LENGTH_SHORT).show();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View v = convertView;
        final Context context = parent.getContext();
        if (v == null) {
            LayoutInflater vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.wordlist, null);
        }

        //위젯 참조
        TextView list_word = (TextView)v.findViewById(R.id.list_word);
        final ImageView list_mark = (ImageView)v.findViewById(R.id.list_mark);

        //데이터 참조
        list_word.setText(wordlistArray.get(position).getWord());
        list_mark.setImageResource(R.drawable.star_full);

        list_mark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delItem(context,position);
            }
        });

        return v;
    }
}
