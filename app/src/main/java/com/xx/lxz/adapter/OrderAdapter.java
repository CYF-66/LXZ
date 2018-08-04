package com.xx.lxz.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xx.lxz.R;
import com.xx.lxz.activity.my.PayDetailsActivity;
import com.xx.lxz.bean.OrderDTO;
import com.xx.lxz.bean.RefreshModel;
import com.xx.lxz.bean.RefreshtEvent;
import com.xx.lxz.config.GlobalConfig;

import org.simple.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Zhenhua on 2017/5/22 17:30.
 * @email zhshan@ctrip.com
 */

public class OrderAdapter extends RecyclerView.Adapter {
    private Context context;
    private List<OrderDTO> mContent;
    private static String orderType;

    private static final int TYPE_HEADER = 1;
    private static final int TYPE_CONTENT = 2;
    private static final int TYPE_FOOTER = 3;

    private List<Integer> mContentIndex = new ArrayList<>();
    //用于记录当前内容是隐藏还是显示
    private SparseBooleanArray mBooleanMap;

    public OrderAdapter(Context context, List<OrderDTO> data,String orderType) {
        this.context = context;
        this.mContent = data;
        this.orderType=orderType;
        mBooleanMap = new SparseBooleanArray();
    }
    /**
     * 是否为头布局
     * @param position
     * @return
     */
    private boolean isContentView(int position){
        return mContentIndex.contains(new Integer(position));
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_HEADER:
                return new ContentViewHolder(LayoutInflater.from(context).inflate(R.layout.adapter_order_item, parent, false));
            case TYPE_CONTENT:
                return new ContentViewHolder(LayoutInflater.from(context).inflate(R.layout.adapter_order_item, parent, false));
            case TYPE_FOOTER:
                return new FooterViewHolder(LayoutInflater.from(context).inflate(R.layout.adapter_orderdetails_item, parent, false));
            default:
                return new ContentViewHolder(LayoutInflater.from(context).inflate(R.layout.adapter_order_item, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ContentViewHolder) {
            position=getContentRealCount(position);

            ((ContentViewHolder) holder).ll_pay_record.setOnClickListener(null);
//            holder.tvClassName.setText(mContent.get(position).className);
            ((ContentViewHolder) holder).ll_pay_record.setTag(position);
            ((ContentViewHolder) holder).tv_pay_ime.setTag(position);
            ((ContentViewHolder) holder).tv_phone_type.setText(mContent.get(position).getContentDates().getProduct_name());
            ((ContentViewHolder) holder).tv_zu_date.setText(mContent.get(position).getContentDates().getStart_date());
            ((ContentViewHolder) holder).tv_current_rent_money.setText(mContent.get(position).getContentDates().getRepaytotal());
            ((ContentViewHolder) holder).tv_orderNum.setText("订单号:"+mContent.get(position).getContentDates().getBid());

            String outOfRate=mContent.get(position).getContentDates().getExpectday();
            String bookType=mContent.get(position).getContentDates().getBook_type();
            if(TextUtils.isEmpty(bookType)){
                ((ContentViewHolder) holder).tv_zu.setText("租");
            }else if(bookType.equals("1")){
                    ((ContentViewHolder) holder).tv_zu.setText("售");
            }else{
                ((ContentViewHolder) holder).tv_zu.setText("租");
            }
            if(TextUtils.isEmpty(outOfRate)){
                outOfRate="0";
            }
            ((ContentViewHolder) holder).tv_out_off_days.setText(outOfRate);
            ((ContentViewHolder) holder).tv_out_off_fee.setText(mContent.get(position).getContentDates().getProduct_rate());
            String state=mContent.get(position).getContentDates().getState();
            switch (state){
                case "1"://待还中
                    ((ContentViewHolder) holder).ll_dec1.setVisibility(View.VISIBLE);
                    ((ContentViewHolder) holder).ll_dec2.setVisibility(View.GONE);
                    ((ContentViewHolder) holder).rl_content.setBackground(context.getResources().getDrawable(R.mipmap.order_bg));
                    ((ContentViewHolder) holder).iv_state.setBackground(context.getResources().getDrawable(R.mipmap.order_state_usual));
                    break;
                case "2"://已逾期
                    ((ContentViewHolder) holder).ll_dec1.setVisibility(View.VISIBLE);
                    ((ContentViewHolder) holder).ll_dec2.setVisibility(View.GONE);
                    ((ContentViewHolder) holder).rl_content.setBackground(context.getResources().getDrawable(R.mipmap.order_bg));
                    ((ContentViewHolder) holder).iv_state.setBackground(context.getResources().getDrawable(R.mipmap.order_state_outof));
                    break;
                case "3"://已结清
                    ((ContentViewHolder) holder).btn_payoff.setVisibility(View.GONE);
                    ((ContentViewHolder) holder).ll_dec1.setVisibility(View.VISIBLE);
                    ((ContentViewHolder) holder).ll_dec2.setVisibility(View.GONE);
                    ((ContentViewHolder) holder).rl_content.setBackground(context.getResources().getDrawable(R.mipmap.order_bg));
                    ((ContentViewHolder) holder).iv_state.setBackground(context.getResources().getDrawable(R.mipmap.order_state_payoff));
                    break;
                case "4"://审核中
                    ((ContentViewHolder) holder).btn_payoff.setVisibility(View.GONE);
                    ((ContentViewHolder) holder).ll_dec1.setVisibility(View.GONE);
                    ((ContentViewHolder) holder).ll_dec2.setVisibility(View.VISIBLE);
                    ((ContentViewHolder) holder).iv_state.setBackground(context.getResources().getDrawable(R.mipmap.order_state_checking));
                    ((ContentViewHolder) holder).rl_content.setBackground(context.getResources().getDrawable(R.mipmap.order_bg_check));
                    ((ContentViewHolder) holder).tv_reason.setVisibility(View.GONE);
                    ((ContentViewHolder) holder).tv_pay_ime.setVisibility(View.GONE);
                    ((ContentViewHolder) holder).tv_state_desp.setText("订单在审核中,请你耐心等待");
                    ((ContentViewHolder) holder).tv_zu_date.setText(mContent.get(position).getContentDates().getCrtdate());
                    break;
                case "5"://审核拒绝
                    ((ContentViewHolder) holder).ll_dec1.setVisibility(View.GONE);
                    ((ContentViewHolder) holder).btn_payoff.setVisibility(View.GONE);
                    ((ContentViewHolder) holder).ll_dec2.setVisibility(View.VISIBLE);
                    ((ContentViewHolder) holder).rl_content.setBackground(context.getResources().getDrawable(R.mipmap.order_bg_refused));
                    ((ContentViewHolder) holder).iv_state.setBackground(context.getResources().getDrawable(R.mipmap.order_state_refused));
                    ((ContentViewHolder) holder).tv_reason.setVisibility(View.VISIBLE);
                    ((ContentViewHolder) holder).tv_pay_ime.setVisibility(View.GONE);
                    ((ContentViewHolder) holder).tv_reason.setText("审核拒绝原因:");
                    ((ContentViewHolder) holder).tv_state_desp.setText("由于你的征信信用过低,我司拒绝您的这次申请");
                    break;
                case "8"://待支付
                    ((ContentViewHolder) holder).ll_dec1.setVisibility(View.GONE);
                    ((ContentViewHolder) holder).btn_payoff.setVisibility(View.GONE);
                    ((ContentViewHolder) holder).ll_dec2.setVisibility(View.VISIBLE);
                    ((ContentViewHolder) holder).tv_reason.setVisibility(View.GONE);
                    ((ContentViewHolder) holder).tv_pay_ime.setVisibility(View.VISIBLE);
                    ((ContentViewHolder) holder).rl_content.setBackground(context.getResources().getDrawable(R.mipmap.order_statue_to_pay));
                    ((ContentViewHolder) holder).iv_state.setBackground(context.getResources().getDrawable(R.mipmap.order_state_to_pay));
                    ((ContentViewHolder) holder).tv_state_desp.setText("待付意外保障服务费350元");
                    ((ContentViewHolder) holder).tv_zu_date.setText(mContent.get(position).getContentDates().getCrtdate());
                    break;
            }
            ((ContentViewHolder) holder).ll_pay_record.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = (int) view.getTag();

                    boolean isOpen = mBooleanMap.get(position);

                    if(isOpen){
                        ((ContentViewHolder) holder).iv_arrow.setBackground(context.getResources().getDrawable(R.mipmap.arrow_down));
                    }else{
                        ((ContentViewHolder) holder).iv_arrow.setBackground(context.getResources().getDrawable(R.mipmap.arrow_up));
                    }
                    mBooleanMap.put(position, !isOpen);
                    notifyDataSetChanged();
                }
            });

            ((ContentViewHolder) holder).tv_pay_ime.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = (int) view.getTag();
                    String bid=mContent.get(position).getContentDates().getBid();
                    RefreshModel refreshMode = new RefreshModel();
                    refreshMode.setActive(GlobalConfig.ACTIVE_PAY);
                    refreshMode.setPosition(GlobalConfig.REFRESHPOSITIO_ORDER_CHECK);
                    refreshMode.setDataPosition(Integer.parseInt(bid));
                    EventBus.getDefault().post(
                            new RefreshtEvent(refreshMode));

//                    Intent intent=new Intent(context, PayDetailsActivity.class);
//                    context.startActivity(intent);

                }
            });

        } else if (holder instanceof HeaderViewHolder){

        } else if (holder instanceof FooterViewHolder) {
            //根据position获取position对应的内容所在head
            final int contentId = getContentDetailsOfClass(position);
            //获取改内容head所在的position位置
            final int headOfPosition = mContentIndex.get(contentId);
            //根据当前位置position和head布局的position，计算当前内容在head中的位置
            int contentIndex = position - headOfPosition - 1;
            position=contentIndex;

            ((FooterViewHolder) holder).tv_pay.setOnClickListener(null);
//            holder.tvClassName.setText(mContent.get(position).className);

            ((FooterViewHolder) holder).tv_pay.setTag(position);
            ((FooterViewHolder) holder).tv_order_state.setText(mContent.get(contentId).getContentDates().getPaymentList().get(position).getState_name());
            ((FooterViewHolder) holder).tv_current_rent_money.setText(mContent.get(contentId).getContentDates().getPaymentList().get(position).getTotalmoney());
            ((FooterViewHolder) holder).tv_payoff_date.setText(mContent.get(contentId).getContentDates().getPaymentList().get(position).getDate());
            ((FooterViewHolder) holder).tv_qi.setText("第"+mContent.get(contentId).getContentDates().getPaymentList().get(position).getCurphase()+"期");
            int state=mContent.get(contentId).getContentDates().getPaymentList().get(position).getState();
            switch (state){//1：已还  2：待还 3：逾期 4：尚未开始
                case 1:
                    ((FooterViewHolder) holder).tv_pay.setVisibility(View.VISIBLE);
                    break;
                case 2:
                    ((FooterViewHolder) holder).tv_pay.setVisibility(View.VISIBLE);
                    break;
                case 3:
                    ((FooterViewHolder) holder).tv_pay.setVisibility(View.VISIBLE);
                    break;
                case 4:
                    ((FooterViewHolder) holder).tv_pay.setVisibility(View.GONE);
                    break;
                case 5:
                    ((FooterViewHolder) holder).tv_pay.setVisibility(View.GONE);
                    break;
            }
            ((FooterViewHolder) holder).tv_pay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = (int) view.getTag();


                     //测试支付
                    Intent intent=new Intent(context, PayDetailsActivity.class);
                    intent.putExtra("pid",mContent.get(contentId).getContentDates().getPaymentList().get(position).getPid());
                    intent.putExtra("bid",mContent.get(contentId).getContentDates().getBid());
                    context.startActivity(intent);
                }
            });

        }
    }

    /**
     * 获取position是第几个头布局
     * @param position
     * @return
     */
    private int getContentRealCount(int position){
        return mContentIndex.indexOf(new Integer(position));
    }

    /**
     * 根据value获取所属的key
     * @return
     */
    private int getContentDetailsOfClass(int position){

        for (int i = 0; i < mContentIndex.size(); i++) {
            if(mContentIndex.get(i) > position){
                return i-1;
            }
        }
        return mContentIndex.size() - 1;
    }


    @Override
    public int getItemCount() {
//        return data.length;
        mContentIndex.clear();
        int count = 0;
        int contentSize = getContentCount();
        for (int i = 0; i < contentSize; i++) {
            if(i != 0){
                count++;
            }
            mContentIndex.add(new Integer(count));

            count += getContentDetailsForContent(i);
        }
        return count + 1;
    }

    /**
     * 获取订单数
     * @return
     */
    public int getContentCount() {
        return mContent.size();
    }

    /**
     * 根据订单获取租金缴纳记录数据
     * @param headerPosition
     * @return
     */
    public int getContentDetailsForContent(int headerPosition) {

        int count = mContent.get(headerPosition).getDetailsDates().size();

        //这里是控制显示隐藏内容的部分
        if (!mBooleanMap.get(headerPosition)) {
            count = 0;
        }
        return count;
    }

    @Override
    public int getItemViewType(int position) {
//        switch (data[position]) {
////            case "header":
////                return TYPE_HEADER;
////            case "content":
////                return TYPE_CONTENT;
////            case "footer":
////                return TYPE_FOOTER;
////            default:
////                return TYPE_CONTENT;
////        }
        if(isContentView(position)){
            return TYPE_CONTENT;
        }else{
            return TYPE_FOOTER;
        }
    }

    /***********************Multiple Types of ViewHolders****************************/
    public static class HeaderViewHolder extends RecyclerView.ViewHolder{

        public HeaderViewHolder(View itemView) {
            super(itemView);
        }
    }

    /**
     * 订单布局
     */
    public static class ContentViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_orderNum;//订单号
        public TextView tv_phone_type;//手机类型
        public TextView tv_zu_date;//租赁日期
        public TextView tv_current_rent_money;//本期租金
        public TextView tv_out_off_days;//逾期天数
        public TextView tv_out_off_fee;//逾期费
        public TextView tv_bian1;//
        public TextView tv_bian2;//
        public TextView tv_bian3;//
        public TextView tv_reason;//
        public TextView tv_zu;//标识
        public TextView tv_state_desp;//审核订单描述
        public TextView tv_pay_ime;//立即支付
        public Button btn_payoff;//结清账单
        public LinearLayout ll_pay_record;//租金缴纳记录 详情
        public LinearLayout ll_dec1;//描述1
        public LinearLayout ll_dec2;//描述2
        public RelativeLayout rl_content;//订单容器
        public ImageView iv_arrow;
        public ImageView iv_state;//订单状态显示图片
        public ContentViewHolder(View itemView) {
            super(itemView);
            tv_orderNum = (TextView)itemView.findViewById(R.id.tv_orderNum);
            tv_phone_type = (TextView)itemView.findViewById(R.id.tv_phone_type);
            tv_zu_date = (TextView)itemView.findViewById(R.id.tv_zu_date);
            tv_current_rent_money = (TextView)itemView.findViewById(R.id.tv_current_rent_money);
            tv_out_off_days = (TextView)itemView.findViewById(R.id.tv_out_off_days);
            tv_out_off_fee = (TextView)itemView.findViewById(R.id.tv_out_off_fee);
            tv_bian1 = (TextView)itemView.findViewById(R.id.tv_bian1);
            tv_bian2 = (TextView)itemView.findViewById(R.id.tv_bian2);
            tv_bian3 = (TextView)itemView.findViewById(R.id.tv_bian3);
            tv_pay_ime = (TextView)itemView.findViewById(R.id.tv_pay_ime);
            tv_reason = (TextView)itemView.findViewById(R.id.tv_reason);
            tv_zu = (TextView)itemView.findViewById(R.id.tv_zu);
            tv_state_desp = (TextView)itemView.findViewById(R.id.tv_state_desp);
            btn_payoff = (Button)itemView.findViewById(R.id.btn_payoff);
            ll_pay_record = (LinearLayout)itemView.findViewById(R.id.ll_pay_record);
            ll_dec1 = (LinearLayout)itemView.findViewById(R.id.ll_dec1);
            ll_dec2 = (LinearLayout)itemView.findViewById(R.id.ll_dec2);
            iv_arrow = (ImageView)itemView.findViewById(R.id.iv_arrow);
            iv_state = (ImageView)itemView.findViewById(R.id.iv_state);
            rl_content = (RelativeLayout)itemView.findViewById(R.id.rl_content);

            if(orderType.equals("0")){//已完成
                ll_pay_record.setVisibility(View.VISIBLE);
                tv_bian1.setText("每期租金(元)");
                tv_bian2.setText("租赁周期");
                tv_bian3.setText("逾期率");
            }else if(orderType.equals("1")){//待还
                ll_pay_record.setVisibility(View.VISIBLE);
                tv_bian1.setText("本期租金(元)");
                tv_bian2.setText("逾期天数(天)");
                tv_bian3.setText("逾期费");
            }else{//审核
                ll_pay_record.setVisibility(View.GONE);
                tv_bian1.setText("每期租金(元)");
                tv_bian2.setText("租赁周期");
                tv_bian3.setText("逾期率");
            }
        }
    }

    /**
     * 订单对应的租金缴纳记录布局
     */
    public static class FooterViewHolder extends RecyclerView.ViewHolder {

        public TextView tv_order_state;//订单状态
        public TextView tv_qi;//多少期
        public ImageView iv_phone_img;//手机图片
        public TextView tv_ti_msg;//提示信息
        public TextView tv_current_rent_money;//本期租金
        public TextView tv_payoff_date;//结算日期
        public Button tv_pay;//结算
        public FooterViewHolder(View itemView) {
            super(itemView);
            tv_order_state = (TextView)itemView.findViewById(R.id.tv_order_state);
            tv_qi = (TextView)itemView.findViewById(R.id.tv_qi);
            tv_ti_msg = (TextView)itemView.findViewById(R.id.tv_ti_msg);
            tv_current_rent_money = (TextView)itemView.findViewById(R.id.tv_current_rent_money);
            tv_payoff_date = (TextView)itemView.findViewById(R.id.tv_payoff_date);
            iv_phone_img = (ImageView)itemView.findViewById(R.id.iv_phone_img);
            tv_pay = (Button)itemView.findViewById(R.id.tv_pay);
        }
    }
}
