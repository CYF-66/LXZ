package com.xx.lxz.adapter;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xx.lxz.bean.RecyclerItemViewId;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

/**
 * RecyclerView 通用适配器
 * Created by cyf on 2017/12/28.
 */

public class ModelRecyclerAdapter<T> extends RecyclerView.Adapter<ModelRecyclerAdapter.ModelViewHolder> {

    private OnItemClickListener mListener ;
    private OnItemLongClickListener mLongListener ;

    protected Context mContext;

    public void setOnItemClickListener(OnItemClickListener mListener) {
        this.mListener = mListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener mLongListener) {
        this.mLongListener = mLongListener;
    }

    /**
     * 通过注释的方式加入的布局item的layoutId
     */
    protected int mLayoutId;

    /**
     * viewholder的实现类类名
     */
    private Class<? extends ModelViewHolder> viewHolderClass;
    /**
     * 数据 即我们的任何类型的bean
     */
    protected List<T> mDatas = new ArrayList<>();

    /**
     * 实例化adapter
     * @param viewHolderClass
     */
    public ModelRecyclerAdapter(Class<ModelViewHolder> viewHolderClass) {
        this.viewHolderClass = viewHolderClass;
        this.mLayoutId = viewHolderClass.getAnnotation(RecyclerItemViewId.class)
                .value();
    }

    /**
     * 实例化adapter
     * @param viewHolderClass
     * @param Datas
     */
    public ModelRecyclerAdapter(Class<ModelViewHolder> viewHolderClass, List<T> Datas) {
        this.viewHolderClass = viewHolderClass;
        this.mLayoutId = viewHolderClass.getAnnotation(RecyclerItemViewId.class)//获取我们的layoutid，我们的类注释后面的部分
                .value();
        mDatas = Datas;
    }

    /**
     * 创建viewhold，绑定控件
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public ModelViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        ModelViewHolder viewHolder = null;
        if (mContext == null)
            mContext = parent.getContext();

        try {
            View converView = LayoutInflater.from(parent.getContext()).inflate(mLayoutId, parent, false);
            viewHolder = viewHolderClass.getConstructor(View.class).newInstance(converView);
            ButterKnife.bind(viewHolder, converView);//将viewhodler于我们的view绑定起来
        } catch (Exception e) {
            e.printStackTrace();
        }
        return viewHolder;
    }

    /**
     * 更新数据
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(final ModelViewHolder holder, int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null){
                    mListener.onItemClick(v,holder.getPosition(),getItemId(holder.getPosition()));
                }
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mLongListener != null){
                    return mLongListener.onItemLongClick(v,holder.getPosition(),getItemId(holder.getPosition()));
                }
                return false;
            }
        });
        holder.convert(mDatas.get(position), this, mContext, position);//这里更新数据
    }

    /**
     * 获取子条目数目
     * @return
     */
    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    /**
     * 添加子条目到指定位置
     * @param positon
     * @param data
     */
    public void add(int positon, T data) {
        mDatas.add(positon, data);
        notifyItemInserted(positon);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                notifyDataSetChanged();
            }
        }, 500);

    }

    /**
     * 添加子条目
     * @param data
     */
    public void add(T data) {
        mDatas.add(data);
        notifyDataSetChanged();

    }

    /**
     * 先清空数据，再添加数据
     * @param data
     */
    public void add(List<T> data) {
        mDatas.clear();
        mDatas.addAll(data);
        notifyDataSetChanged();
    }

    /**
     * 移除子条目
     * @param position
     */
    public void remove(int position) {
        mDatas.remove(position);
        notifyItemRemoved(position);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                notifyDataSetChanged();
            }
        }, 500);
    }

    /**
     * 替换子条目
     * @param position
     * @param data
     */
    public void replace(int position, T data) {
        mDatas.remove(position);
        mDatas.add(position == 0 ? position : position - 1, data);
        notifyDataSetChanged();
    }

    /**
     * 更新单个子条目
     * @param position
     * @param data
     */
    public void update(int position, T data) {
        mDatas.set(position,data);
        notifyItemChanged(position);
//        mDatas.remove(position);
//        mDatas.add(position == 0 ? position : position - 1, data);
//        notifyDataSetChanged();
    }

    /**
     * 直接添加数据
     * @param datas
     */
    public void addAll(List<T> datas) {
        mDatas.addAll(datas);
        notifyDataSetChanged();
    }

    /**
     * 清空数据
     */
    public void clear() {
        mDatas.clear();
        notifyDataSetChanged();
    }

    /**
     * 返回数据
     * @return
     */
    public List<T> getItems() {
        return mDatas;
    }

    /**
     * hodler抽象类，支持任何数据类型
     *
     * @param <T>
     */
    public static abstract class ModelViewHolder<T> extends RecyclerView.ViewHolder {

        public ModelViewHolder(View itemView) {
            super(itemView);
        }

        /**
         * 这个是我们真正在实际使用的类中的绑定数据的方法
         *
         * @param item    bean类型
         * @param adapter adpter对象
         * @param context context对象
         * @param positon 位置
         */
        public abstract void convert(T item, ModelRecyclerAdapter adapter, Context context, int positon);
    }

    public interface OnItemClickListener{
        void onItemClick(View view, int position, long id) ;

    }
    public interface OnItemLongClickListener{
        boolean onItemLongClick(View view, int position, long id) ;

    }
}
