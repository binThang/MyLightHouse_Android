package com.ghsoft.android.lighthouse;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.androidquery.AQuery;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NoticeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private LayoutInflater inflater;
    List<NoticeData> data = Collections.emptyList();
    AQuery aQuery;


    // create constructor to innitilize context and data sent from MainActivity
    public NoticeAdapter(Context context, List<NoticeData> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
        aQuery = new AQuery(this.context);
    }

    // Inflate the layout when viewholder created
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_item, parent, false);
        MyHolder holder = new MyHolder(view);
        return holder;

    }

    // Bind data
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        // Get current position of item in recyclerview to bind data and assign values from list
        final MyHolder myHolder = (MyHolder) holder;
        NoticeData current = data.get(position);

        myHolder.main_text.setText(current.title);
        myHolder.sub_text.setText(current.date);
        myHolder.te_1.setText(current.con);

        myHolder.topview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               myHolder.img.performClick();
            }
        });
        myHolder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (myHolder.img.isChecked()) {
                    myHolder.img.setBackgroundResource(R.drawable.up);
                    myHolder.li_1.setVisibility(View.VISIBLE);
                } else {
                    myHolder.img.setBackgroundResource(R.drawable.down);
                    myHolder.li_1.setVisibility(View.GONE);
                }
            }
        });


    }


    // return total item from List
    @Override
    public int getItemCount() {
        return data.size();
    }


    class MyHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.main_text)
        TextView main_text;
        @BindView(R.id.sub_text)
        TextView sub_text;
        @BindView(R.id.img)
        ToggleButton img;
        @BindView(R.id.li_1)
        LinearLayout li_1;
        @BindView(R.id.te_1)
        TextView te_1;
        @BindView(R.id.topview)
        LinearLayout topview;

        public MyHolder(final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);


        }
    }
}