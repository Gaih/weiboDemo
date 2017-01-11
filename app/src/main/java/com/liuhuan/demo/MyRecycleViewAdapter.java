package com.liuhuan.demo;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Administrator on 2017/1/10.
 */

public class MyRecycleViewAdapter
        extends RecyclerView.Adapter<MyRecycleViewAdapter.ViewHolder> {


    private Context mContext;
    private List<String> mData;


    public MyRecycleViewAdapter(List<String> data) {
        mData = data;
    }

    public OnItemClickListener mItemClickListener;

    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        this.mItemClickListener = itemClickListener;
    }

    public interface OnItemClickListener{
        void onItemClick(View view, int position);
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyRecycleViewAdapter.ViewHolder holder, int position) {
        holder.textView.setText(mData.get(position)+position);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView textView;
        public Spinner mSpinner;
        public ViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.mContent);
            textView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mItemClickListener !=null){
                mItemClickListener.onItemClick(view,getPosition());
            }
        }
    }
}
