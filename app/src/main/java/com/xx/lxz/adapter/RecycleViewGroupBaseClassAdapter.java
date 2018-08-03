package com.xx.lxz.adapter;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * recycleview实现分组效果的基类
 * @param <C>
 * @param <S>
 * author cyf
 */
public abstract class RecycleViewGroupBaseClassAdapter<C extends RecyclerView.ViewHolder, S extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_CONTENT = 1;
    private static final int TYPE_BOTTOM = 2;

    private List<Integer> mHeaderIndex = new ArrayList<>();
    /**
     * 是否为头布局
     * @param position
     * @return
     */
    private boolean isHeaderView(int position){
        return mHeaderIndex.contains(new Integer(position));
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == TYPE_HEADER){
            //head布局
            return onCreateHeaderViewHolder(parent, viewType);
        }else {
            //内容布局
            return onCreateContentViewHolder(parent, viewType);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        //如果是head布局
        if(isHeaderView(position)){     //head布局填充
            onBindHeaderViewHolder((C)holder, getHeadRealCount(position));
            return ;
        }else {//内容填充
            //根据position获取position对应的内容所在head
            int contentId = getContentOfClass(position);
            //获取改内容head所在的position位置
            int headOfPosition = mHeaderIndex.get(contentId);
            //根据当前位置position和head布局的position，计算当前内容在head中的位置
            int contentIndex = position - headOfPosition - 1;
            onBindContentViewHolder((S)holder, contentId, contentIndex);
        }
    }

    @Override
    public int getItemViewType(int position) {

        if(isHeaderView(position)){
            return TYPE_HEADER;
        }else{
            return TYPE_CONTENT;
        }
    }

    /**
     * 条目的总数量
     * @return
     */
    @Override
    public int getItemCount() {
        mHeaderIndex.clear();
        int count = 0;
        int headSize = getHeadersCount();
        for (int i = 0; i < headSize; i++) {
            if(i != 0){
                count++;
            }
            mHeaderIndex.add(new Integer(count));

            count += getContentCountForHeader(i);
        }
        return count + 1;
    }

    /**
     * 获取position是第几个头布局
     * @param position
     * @return
     */
    private int getHeadRealCount(int position){
        return mHeaderIndex.indexOf(new Integer(position));
    }

    /**
     * 根据value获取所属的key
     * @return
     */
    private int getContentOfClass(int position){

        for (int i = 0; i < mHeaderIndex.size(); i++) {
            if(mHeaderIndex.get(i) > position){
                return i-1;
            }
        }
        return mHeaderIndex.size() - 1;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {

        final RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if(layoutManager instanceof GridLayoutManager){
            GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
            GridLayoutManager.SpanSizeLookup spanSizeLookup = gridLayoutManager.getSpanSizeLookup();
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup(){

                @Override
                public int getSpanSize(int position) {

                    int viewType = getItemViewType(position);
                    if(viewType == TYPE_HEADER){
                        return ((GridLayoutManager) layoutManager).getSpanCount();
                    }
                    return 1;
                }
            });
        }
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder){
        int position = holder.getLayoutPosition();
        if (isHeaderView(position))
        {
            ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();

            if (lp != null && lp instanceof StaggeredGridLayoutManager.LayoutParams)
            {

                StaggeredGridLayoutManager.LayoutParams p =
                        (StaggeredGridLayoutManager.LayoutParams) lp;

                p.setFullSpan(true);
            }
        }
    }

    /**
     * 头布局的总数
     * @return
     */
    public abstract int getHeadersCount();

    /**
     * 头布局对应内容的总数（也就是改头布局里面有多少条item）
     * @param headerPosition 第几个头布局
     * @return
     */
    public abstract int getContentCountForHeader(int headerPosition);

    /**
     * 创建头布局
     * @param parent
     * @param viewType
     * @return
     */
    public abstract C onCreateHeaderViewHolder(ViewGroup parent, int viewType);

    /**
     * 创建内容布局
     * @param parent
     * @param viewType
     * @return
     */
    public abstract S onCreateContentViewHolder(ViewGroup parent, int viewType);

    /**
     * 填充头布局的数据
     * @param holder
     * @param position
     */
    public abstract void onBindHeaderViewHolder(C holder, int position);

    /**
     * 填充
     * @param holder
     * @param HeaderPosition
     * @param ContentPositionForHeader
     */
    public abstract void onBindContentViewHolder(S holder, int HeaderPosition, int ContentPositionForHeader);

}