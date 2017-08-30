package com.mrwu.demo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.mrwu.demo.adapter.DemoAdapter;
import com.mrwu.demo.adapter.DemoAdapter.ViewHolder;
import com.mrwu.demo.bean.DemoBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivity extends Activity implements OnClickListener, OnItemClickListener {

    /**
     * 选择所有
     */
    private Button btnSelectAll;
    /**
     * ListView列表
     */
    private ListView lvListView;
    /**
     * 适配对象
     */
    private DemoAdapter adpAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 初始化视图
        initView();
        // 初始化控件
        initData();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        /*
         * 返回按钮
         */
        RelativeLayout btnPay = (RelativeLayout) findViewById(R.id.btnPay);
        btnPay.setOnClickListener(this);

        /*
         * 确定按钮
         */
        RelativeLayout btnAdd = (RelativeLayout) findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(this);

        /*
         * 清除所有
         */
        Button btnDelete = (Button) findViewById(R.id.btnDelete);
        btnDelete.setOnClickListener(this);

        btnSelectAll = (Button) findViewById(R.id.btnSelectAll);
        btnSelectAll.setOnClickListener(this);

        lvListView = (ListView) findViewById(R.id.lvListView);
        lvListView.setOnItemClickListener(this);
    }

    /**
     * 初始化视图
     */
    private void initData() {
        // 模拟假数据
        List<DemoBean> demoDatas = new ArrayList<DemoBean>();
        demoDatas.add(new DemoBean("凤梨", 30.5, false));
        demoDatas.add(new DemoBean("香蕉", 13.2, true));
        demoDatas.add(new DemoBean("苹果", 11.7, true));
        demoDatas.add(new DemoBean("榴莲", 26.1, true));
        adpAdapter = new DemoAdapter(this, demoDatas);
        lvListView.setAdapter(adpAdapter);
    }

    /**
     * 按钮点击事件
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnPay:// 支付
                /* 当点击支付的时候 */
                pay();
                break;
            case R.id.btnAdd:// 增加
                /* 当点击增加的时候 */
                add();
                break;
            case R.id.btnDelete:// 删除
                /* 当点击删除的时候 */
                delete();
                break;
            case R.id.btnSelectAll:// 全选
                /* 当点击全选的时候 */
                allSelect();
                break;
            default:
                break;
        }
    }

    /* 当ListView 子项点击的时候 */
    @Override
    public void onItemClick(AdapterView<?> listView, View itemLayout, int position, long id) {
        if (itemLayout.getTag() instanceof ViewHolder) {
            ViewHolder holder = (ViewHolder) itemLayout.getTag();
            // 会自动出发CheckBox的checked事件
            holder.cbCheck.toggle();
        }
    }

    /**
     * 增加
     **/
    private void add() {
        adpAdapter.add(new DemoBean("哈密瓜", 19.3, true));
        adpAdapter.notifyDataSetChanged();
    }

    /**
     * 删除
     **/
    private void delete() {
        /* 删除算法最复杂,拿到checkBox选择寄存map */
        Map<Integer, Boolean> map = adpAdapter.getCheckMap();
        // 获取当前的数据数量
        int count = adpAdapter.getCount();
        // 进行遍历
        for (int i = 0; i < count; i++) {
            // 因为List的特性,删除了2个item,则3变成2,所以这里要进行这样的换算,才能拿到删除后真正的position
            int position = i - (count - adpAdapter.getCount());
            if (map.get(i) != null && map.get(i)) {
                DemoBean bean = (DemoBean) adpAdapter.getItem(position);
                if (bean.isCanRemove()) {
                    adpAdapter.getCheckMap().remove(i);
                    adpAdapter.remove(position);
                } else {
                    map.put(position, false);
                }
            }
        }
        if (btnSelectAll.getText().toString().trim().equals("全不选")) {
            btnSelectAll.setText("全选");
        }
        adpAdapter.notifyDataSetChanged();
    }

    /**
     * 全选
     **/
    private void allSelect() {
        if (btnSelectAll.getText().toString().trim().equals("全选")) {
            // 所有项目全部选中
            adpAdapter.configCheckMap(true);
            adpAdapter.notifyDataSetChanged();
            btnSelectAll.setText("全不选");
        } else {
            // 所有项目全部不选中
            adpAdapter.configCheckMap(false);
            adpAdapter.notifyDataSetChanged();
            btnSelectAll.setText("全选");
        }
    }

    private List<DemoBean> isCheckList = new ArrayList<DemoBean>();

    private double total_fee;

    public void pay() {
        /* 拿到checkBox选择寄存map */
        Map<Integer, Boolean> map = adpAdapter.getCheckMap();
        // 获取当前的数据数量
        int count = adpAdapter.getCount();
        // 进行遍历
        for (int i = 0; i < count; i++) {
            if (map.get(i) != null && map.get(i)) {
                DemoBean bean = (DemoBean) adpAdapter.getItem(i);
                isCheckList.add(bean);
            }
        }
        if (isCheckList == null || isCheckList.size() == 0) {
            Toast.makeText(this, "没有需要支付的账单", Toast.LENGTH_SHORT).show();
            return;
        }
        /* 遍历列表 */
        for (DemoBean d : isCheckList) {
            if (d == null) continue;
            total_fee += d.getFee();
            System.out.println(d.toString());
        }
        System.out.println(total_fee);
        /* 所有项目全部不选中 */
        adpAdapter.configCheckMap(false);
        adpAdapter.notifyDataSetChanged();
        btnSelectAll.setText("全选");
        isCheckList.clear();
        total_fee = 0.0;
    }
}
