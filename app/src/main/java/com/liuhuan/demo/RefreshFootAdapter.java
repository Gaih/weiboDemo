package com.liuhuan.demo;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Administrator on 2017/1/13.
 */

public class RefreshFootAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    //上拉加载更多
    public static final int  PULLUP_LOAD_MORE=0;
    //正在加载中
    public static final int  LOADING_MORE=1;
    //上拉加载更多状态-默认为0
    private int load_more_status=0;
    private LayoutInflater mInflater;
    private static final int TYPE_ITEM = 0;  //普通Item View
    private static final int TYPE_FOOTER = 1;  //顶部FootView
    public static OnItemClickListener mItemClickListener;
    private List<BlogInfo> mData;

    public RefreshFootAdapter(Context context,List<BlogInfo> data){
        this.mInflater=LayoutInflater.from(context);
        this.mData=data;
    }
    //点击响应
    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        this.mItemClickListener = itemClickListener;
    }
    public interface OnItemClickListener{
        void onItemClick(View view, int position);
    }
    /**
     * item显示类型
     * @param parent
     * @param viewType
     * @return
     */
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //进行判断显示类型，来创建返回不同的View
        if(viewType==TYPE_ITEM){
            View view=mInflater.inflate(R.layout.item_view,parent,false);
            //这边可以做一些属性设置，甚至事件监听绑定
            //view.setBackgroundColor(Color.RED);
            ItemViewHolder itemViewHolder=new ItemViewHolder(view);
            return itemViewHolder;
        }else if(viewType==TYPE_FOOTER){
            View foot_view=mInflater.inflate(R.layout.item_view_loadmore,parent,false);
            //这边可以做一些属性设置，甚至事件监听绑定
            //view.setBackgroundColor(Color.RED);
            FootViewHolder footViewHolder=new FootViewHolder(foot_view);
            return footViewHolder;
        }
        return null;
    }

    /**
     * 数据的绑定显示
     * @param holder
     * @param position
     */
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof ItemViewHolder) {
            ((ItemViewHolder)holder).mContent.setText(mData.get(position).getContent());
//            ((ItemViewHolder)holder).mPic.setBackground(mData.get(position).getContent());
            ((ItemViewHolder)holder).mTime.setText(mData.get(position).getPub_date());
            ((ItemViewHolder)holder).mName.setText(mData.get(position).getUser_id());

            holder.itemView.setTag(position);
        }else if(holder instanceof FootViewHolder){
            FootViewHolder footViewHolder=(FootViewHolder)holder;
            switch (load_more_status){
                case PULLUP_LOAD_MORE:
                    footViewHolder.foot_view_item_tv.setText("上拉加载更多...");
                    break;
                case LOADING_MORE:
                    footViewHolder.foot_view_item_tv.setText("正在加载更多数据...");
                    break;
            }
        }
    }

    /**
     * 进行判断是普通Item视图还是FootView视图
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        // 最后一个item设置为footerView
        if (position + 1 == getItemCount()) {
            return TYPE_FOOTER;
        } else {
            return TYPE_ITEM;
        }
    }
    @Override
    public int getItemCount() {
        return mData.size();
    }
    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public static class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mContent;
        private TextView mTime;
        private ImageButton mPic;
        private TextView mName;
        public ItemViewHolder(View itemView) {
            super(itemView);
            mContent = (TextView) itemView.findViewById(R.id.mContent);
            mPic = (ImageButton) itemView.findViewById(R.id.mPic);
            mTime = (TextView) itemView.findViewById(R.id.mTime);
            mName = (TextView) itemView.findViewById(R.id.mName);
            mTime.setOnClickListener(this);
            mContent.setOnClickListener(this);
            mPic.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mItemClickListener !=null){
                mItemClickListener.onItemClick(view,getPosition());
            }
        }
    }
    /**
     * 底部FootView布局
     */
    public static class FootViewHolder extends  RecyclerView.ViewHolder{
        private TextView foot_view_item_tv;
        public FootViewHolder(View view) {
            super(view);
            foot_view_item_tv=(TextView)view.findViewById(R.id.foot_view_item_tv);
        }
    }

    //添加数据
//    public void addItem(List<String> newDatas) {
//        //mTitles.add(position, data);
//        //notifyItemInserted(position);
//        newDatas.addAll(mTitles);
//        mTitles.removeAll(mTitles);
//        mTitles.addAll(newDatas);
//        notifyDataSetChanged();
//    }
//
//    public void addMoreItem(List<String> newDatas) {
//        mTitles.addAll(newDatas);
//        notifyDataSetChanged();
//    }

    /**
     * //上拉加载更多
     * PULLUP_LOAD_MORE=0;
     *  //正在加载中
     * LOADING_MORE=1;
     * //加载完成已经没有更多数据了
     * NO_MORE_DATA=2;
     * @param status
     */
    public void changeMoreStatus(int status){
        load_more_status=status;
        notifyDataSetChanged();
    }
}
