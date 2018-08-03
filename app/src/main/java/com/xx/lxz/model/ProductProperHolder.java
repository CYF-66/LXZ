package com.xx.lxz.model;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.xx.lxz.R;
import com.xx.lxz.adapter.ModelRecyclerAdapter;
import com.xx.lxz.bean.ProductProper;
import com.xx.lxz.bean.RecyclerItemViewId;

import butterknife.BindView;

/**
 * Created by cyf on 2017/12/28.
 */
@RecyclerItemViewId(R.layout.layout_procuct_proper_item)
public class ProductProperHolder extends ModelRecyclerAdapter.ModelViewHolder<ProductProper> {
    @BindView(R.id.tv_proper_value)
    TextView tv_proper_value;//消息内容

    public int position;
    public ProductProper dataList;
    public ModelRecyclerAdapter adapter;

    public ProductProperHolder(View itemView) {
        super(itemView);
    }

//    @OnClick(R.id.tv_proper_value)
//    void click() {
//        if(dataList.isSeclect()){
//            dataList.setSeclect(true);
//        }
//
//        adapter.notifyDataSetChanged();
////        Intent intent=new Intent(ll_whole.getContext(),ProductDetailsActivity.class);
////        intent.putExtra("product_id",dataList.getProduct_id());
////        ll_whole.getContext().startActivity(intent);
//    }

    /**
     * 绑定我们的数据
     *
     * @param dataList    这是数据泛型，根据我们的bean类决定
     * @param adapter adapter 对象
     * @param context context对象
     * @param positon 当前位置
     */
    @Override
    public void convert(final ProductProper dataList, ModelRecyclerAdapter adapter, Context context, int positon) {
        this.position = positon;
        this.dataList = dataList;
        this.adapter = adapter;

        if(dataList.isSeclect()){
            tv_proper_value.setBackground(context.getResources().getDrawable(R.drawable.shape_bg_stroke_blue));
            tv_proper_value.setTextColor(context.getResources().getColor(R.color.low_blue));
        }else{
            if(dataList.getProperValue().equals("3月")||dataList.getProperValue().equals("9月")){
                tv_proper_value.setBackground(context.getResources().getDrawable(R.drawable.shape_bg_stroke_gray_low));
                tv_proper_value.setTextColor(context.getResources().getColor(R.color.qian_gray));
            }else{
                tv_proper_value.setBackground(context.getResources().getDrawable(R.drawable.shape_bg_stroke_gray));
                tv_proper_value.setTextColor(context.getResources().getColor(R.color.shen_gray));
            }
        }
        tv_proper_value.setText(dataList.getProperValue());
    }
}
