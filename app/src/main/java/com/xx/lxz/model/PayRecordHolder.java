package com.xx.lxz.model;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xx.lxz.R;
import com.xx.lxz.adapter.ModelRecyclerAdapter;
import com.xx.lxz.bean.PayRecord;
import com.xx.lxz.bean.RecyclerItemViewId;

import butterknife.BindView;

/**
 * Created by cyf on 2017/12/28.
 */
@RecyclerItemViewId(R.layout.adapter_payoff_record_item)
public class PayRecordHolder extends ModelRecyclerAdapter.ModelViewHolder<PayRecord.Data> {
    @BindView(R.id.tv_order_id)
    TextView tv_order_id;//订单id
    @BindView(R.id.tv_qi)
    TextView tv_qi;//多少期
    @BindView(R.id.iv_phone_img)
    ImageView iv_phone_img;//手机图片
    @BindView(R.id.tv_ti_msg)
    TextView tv_ti_msg;//提示信息
    @BindView(R.id.tv_current_rent_money)
    TextView tv_current_rent_money;//本期租金
    @BindView(R.id.tv_payoff_date)
    TextView tv_payoff_date;//结算日期

    public int position;
    public PayRecord.Data dataList;
    public ModelRecyclerAdapter adapter;

    public PayRecordHolder(View itemView) {
        super(itemView);
    }

    /**
     * 绑定我们的数据
     *
     * @param dataList    这是数据泛型，根据我们的bean类决定
     * @param adapter adapter 对象
     * @param context context对象
     * @param positon 当前位置
     */
    @Override
    public void convert(final PayRecord.Data dataList, ModelRecyclerAdapter adapter, Context context, int positon) {
        this.position = positon;
        this.dataList = dataList;
        this.adapter = adapter;

        tv_qi.setText("第"+dataList.getCurphase()+"期");
        tv_current_rent_money.setText(dataList.getRealymoney());
        tv_payoff_date.setText(dataList.getDate());
        tv_order_id.setText(dataList.getPid());
    }
}
