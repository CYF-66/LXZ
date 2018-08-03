package com.xx.lxz.model;

import android.content.Context;
import android.content.res.ColorStateList;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.TextAppearanceSpan;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xx.lxz.R;
import com.xx.lxz.adapter.ModelRecyclerAdapter;
import com.xx.lxz.bean.Msg;
import com.xx.lxz.bean.RecyclerItemViewId;
import com.xx.lxz.bean.RefreshModel;
import com.xx.lxz.bean.RefreshtEvent;
import com.xx.lxz.config.GlobalConfig;
import com.xx.lxz.util.LogUtil;
import com.xx.lxz.util.StringUtil;

import org.simple.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by cyf on 2017/12/28.
 */
@RecyclerItemViewId(R.layout.layout_msg)
public class MsgHolder extends ModelRecyclerAdapter.ModelViewHolder<Msg.Data> {
    @BindView(R.id.ll_whole)
    LinearLayout ll_whole;//整个item

    @BindView(R.id.tv_title)
    TextView tv_title;//消息标题
    @BindView(R.id.tv_time)
    TextView tv_time;//消息时间
    @BindView(R.id.tv_content)
    TextView tv_content;//消息内容

    public int position;
    public Msg.Data dataList;
    public ModelRecyclerAdapter adapter;

    public MsgHolder(View itemView) {
        super(itemView);
    }

    @OnClick(R.id.ll_item)
    void skipToDetails() {
//        Intent intent=new Intent(ll_whole.getContext(),ProductDetailsActivity.class);
//        intent.putExtra("product_id",dataList.getProduct_id());
//        ll_whole.getContext().startActivity(intent);
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
    public void convert(final Msg.Data dataList, ModelRecyclerAdapter adapter, Context context, int positon) {
        this.position = positon;
        this.dataList = dataList;
        this.adapter = adapter;


        String content=dataList.getContent();
        String islinkmsg=dataList.getIslinkmsg();
        if(TextUtils.isEmpty(islinkmsg)){
            tv_content.setText(content);
        }else{
            if(dataList.getIslinkmsg().equals("1")){
                content=content+"立即申请";
                SpannableString msp = null;
                //创建一个 SpannableString对象
                msp = new SpannableString(content);

                msp.setSpan(new UnderlineSpan(), content.length()-4, content.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                //设置字体（依次包括字体名称，字体大小，字体样式，字体颜色，链接颜色）
                ColorStateList csllink = ColorStateList.valueOf(context.getResources().getColor(R.color.blue));
                ColorStateList csl = ColorStateList.valueOf(context.getResources().getColor(R.color.blue));

                int dp14 = StringUtil.dip2px(context, 14);
                msp.setSpan(new TextAppearanceSpan("monospace",android.graphics.Typeface.NORMAL, dp14, csl, csllink), content.length()-4, content.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                msp.setSpan(new ClickableSpan() {
                    @Override
                    public void onClick(View widget) {
                        RefreshModel refreshModel = new RefreshModel();
                        refreshModel.setActive(GlobalConfig.ACTIVE_SKIPTO);
                        refreshModel.setPosition(GlobalConfig.REFRESHPOSITIO_MSG);
                        refreshModel.setDataPosition(position);
                        refreshModel.setId(dataList.getId());
                        EventBus.getDefault().post(
                                new RefreshtEvent(refreshModel));
                        LogUtil.d("TEST","立即申请");
                    }
                },content.length()-4, content.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                tv_content.setText(msp);
                tv_content.setMovementMethod(LinkMovementMethod.getInstance());
            }else{
                tv_content.setText(content);
            }
        }

        tv_time.setText(dataList.getCrtdate());
        tv_title.setText(dataList.getTitle());

    }
}
