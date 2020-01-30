package com.ghsoft.android.lighthouse;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.androidquery.AQuery;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{

    private Context context;
    private LayoutInflater inflater;
    List<MainData> data = Collections.emptyList();
    AQuery aQuery;
    private AdapterCallback mAdapterCallback;
    Handler handler = new Handler();

    // create constructor to initilize context and data sent from MainActivity
    public MainAdapter(Context context, List<MainData> data)
    {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
        aQuery = new AQuery(this.context);
        try
        {
            this.mAdapterCallback = ((AdapterCallback) context);
        } catch (ClassCastException e)
        {
            throw new ClassCastException("Activity must implement AdapterCallback.");
        }
    }

    // Inflate the layout when viewholder created
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = inflater.inflate(R.layout.spinlist_item, parent, false);
        MyHolder holder = new MyHolder(view);
        return holder;
    }

    // Bind data
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position)
    {
        // Get current position of item in recyclerview to bind data and assign values from list
        final MyHolder myHolder = (MyHolder) holder;
        final MainData current = data.get(position);

        myHolder.main_text.setText(current.text);

        if (current.color)
        {
            data.get(position).color = false;
            Typeface typeFace = Typeface.createFromAsset(context.getAssets(), "NotoSans-Medium.otf");
            myHolder.main_text.setTypeface(typeFace);
            myHolder.main_text.setTextColor(Color.parseColor("#0f0f0f"));
        }
        else
        {
            Typeface typeFace = Typeface.createFromAsset(context.getAssets(), "NotoSans-Regular.otf");
            myHolder.main_text.setTypeface(typeFace);
            myHolder.main_text.setTextColor(Color.parseColor("#303030"));
        }
        myHolder.main_text.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                data.get(position).color = true;
                notifyDataSetChanged();
                try
                {

                    mAdapterCallback.onMethodCallback(position, data.get(position).text, current.text_idx);

                } catch (ClassCastException exception)
                {
                }
            }
        });
    }


    // return total item from List
    @Override
    public int getItemCount()
    {
        return data.size();
    }


    class MyHolder extends RecyclerView.ViewHolder
    {

        @BindView(R.id.text)
        TextView main_text;

        public MyHolder(final View itemView)
        {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }

    interface AdapterCallback
    {
        void onMethodCallback(int pos, String name, String idx);
    }
}