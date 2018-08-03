package com.xx.lxz.model;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xx.lxz.R;
import com.xx.lxz.adapter.ModelRecyclerAdapter;
import com.xx.lxz.bean.Addr;
import com.xx.lxz.bean.RecyclerItemViewId;
import com.xx.lxz.bean.RefreshModel;
import com.xx.lxz.bean.RefreshtEvent;
import com.xx.lxz.config.GlobalConfig;

import org.simple.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by cyf on 2017/12/28.
 */
@RecyclerItemViewId(R.layout.layout_address_item)
public class AddressHolder extends ModelRecyclerAdapter.ModelViewHolder<Addr.Data> {
    @BindView(R.id.ll_whole)
    LinearLayout ll_whole;//整个item

    @BindView(R.id.tv_name)
    TextView tv_name;//姓名
    @BindView(R.id.tv_phone)
    TextView tv_phone;//手机号
    @BindView(R.id.tv_address)
    TextView tv_address;//地址
    @BindView(R.id.tv_edit)
    TextView tv_edit;//编辑
    @BindView(R.id.tv_delete)
    TextView tv_delete;//删除

    @BindView(R.id.iv_select)
    ImageView iv_select;//默认图标

    public int position;
    public Addr.Data dataList;
    public ModelRecyclerAdapter adapter;

    public AddressHolder(View itemView) {
        super(itemView);
    }

    @OnClick(R.id.iv_select)
    void selectDefault() {

        if(dataList.isDefault()){
            dataList.setDefault(false);
        }else{
            dataList.setDefault(true);
        }
        adapter.notifyDataSetChanged();
//        Intent intent=new Intent(ll_whole.getContext(),AddAddressActivity.class);
//        intent.putExtra("product_id",dataList);
//        ll_whole.getContext().startActivity(intent);
    }
    @OnClick(R.id.tv_edit)
    void edit() {

        RefreshModel refreshModel = new RefreshModel();
        refreshModel.setActive(GlobalConfig.ACTIVE_SKIPTO);
        refreshModel.setPosition(GlobalConfig.REFRESHPOSITIO_ADDRESS);
        refreshModel.setDataPosition(position);
        EventBus.getDefault().post(
                new RefreshtEvent(refreshModel));
    }
    @OnClick(R.id.tv_delete)
    void delete() {
        RefreshModel refreshModel = new RefreshModel();
        refreshModel.setActive(GlobalConfig.ACTIVE_REFRESH);
        refreshModel.setPosition(GlobalConfig.REFRESHPOSITIO_ADDRESS);
        refreshModel.setDataPosition(position);
        EventBus.getDefault().post(
                new RefreshtEvent(refreshModel));
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
    public void convert(Addr.Data dataList, ModelRecyclerAdapter adapter, Context context, int positon) {
        this.position = positon;
        this.dataList = dataList;
        this.adapter = adapter;


        if(dataList.isDefault()){
            iv_select.setBackground(context.getResources().getDrawable(R.mipmap.img_add_select));
        }else{
            iv_select.setBackground(context.getResources().getDrawable(R.mipmap.img_add_unselect));
        }

        tv_name.setText(dataList.getName());
        tv_phone.setText(dataList.getPhone());
        tv_address.setText(dataList.getAddressArea()+dataList.getAddressDeatails());

    }
}
