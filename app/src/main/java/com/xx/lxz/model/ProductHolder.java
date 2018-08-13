package com.xx.lxz.model;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xx.lxz.R;
import com.xx.lxz.adapter.ModelRecyclerAdapter;
import com.xx.lxz.bean.Product;
import com.xx.lxz.bean.RecyclerItemViewId;

import net.arvin.selector.utils.PSGlideUtil;

import butterknife.BindView;

/**
 * Created by cyf on 2017/12/28.
 */
@RecyclerItemViewId(R.layout.layout_product_item)
public class ProductHolder extends ModelRecyclerAdapter.ModelViewHolder<Product.Data> {
    @BindView(R.id.ll_item)
    LinearLayout ll_item;//整个item
    @BindView(R.id.iv_img)
    ImageView iv_img;//图片地址
    @BindView(R.id.tv_phone_type)
    TextView tv_phone_type;//手机类型
    @BindView(R.id.tv_money)
    TextView tv_money;//每期多少钱

    public int position;
    public Product.Data dataList;
    public ModelRecyclerAdapter adapter;

    public ProductHolder(View itemView) {
        super(itemView);
    }

//    @OnClick(R.id.ll_item)
//    void skipToDetails() {
//        Intent intent=new Intent(ll_item.getContext(),ProductDetailsActivity.class);
//        intent.putExtra("product_id",dataList.getProduct_id());
//        intent.putExtra("type","1");
//        ll_item.getContext().startActivity(intent);
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
    public void convert(Product.Data dataList, ModelRecyclerAdapter adapter, Context context, int positon) {
        this.position = positon;
        this.dataList = dataList;
        this.adapter = adapter;
        PSGlideUtil.loadImage(context, dataList.getProduct_img(), iv_img);
        if(TextUtils.isEmpty(dataList.getProduct_deep())){
            tv_phone_type.setText(dataList.getProduct_name());
        }else{
            tv_phone_type.setText(dataList.getProduct_name()+"("+dataList.getProduct_deep()+")");
        }


        String totalMoney=dataList.getProduct_price();
        String eachInt=dataList.getProduct_leaseterm();
        String eachMoney="";
        String leaseunit="月";
        eachMoney=dataList.getEachprice();
//        eachMoney=String.format("%.2f", Double.parseDouble(eachMoney));
        if(dataList.getLeaseunit().equals("1")){
            leaseunit="月";
        }else if(dataList.getLeaseunit().equals("2")){
            leaseunit="周";
        }else{
            leaseunit="日";
        }
        tv_money.setText("￥"+eachMoney+"/"+leaseunit+"起");//￥558/月起
    }
}
