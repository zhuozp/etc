package com.gibbon.etc.demo;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.gibbon.etc.annotation.Background;
import com.gibbon.etc.annotation.UiThread;
import com.gibbon.etc.apt.annotation.ViewById;
import com.gibbon.etc.apt.annotation.DownloadMoreListener;
import com.gibbon.etc.apt.annotation.RefreshListner;
import com.gibbon.etc.inject.ViewInjector;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


/**
 * @author zhipeng.zhuo
 * @date 2020-03-13
 */
public class AptAndAspectBothActivity extends FragmentActivity {

    @ViewById
    RecyclerView recyclerView;
    @ViewById
    SwipeRefreshLayout refreshView;

    SimpleListAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atp_asepct_both);
        ViewInjector.inject(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SimpleListAdapter();
        recyclerView.setAdapter(adapter);

        mockData();
    }

    private void mockData() {
        List<String> datas = new ArrayList<>();
        datas.add(String.valueOf(new Random()));
        datas.add(String.valueOf(new Random()));
        datas.add(String.valueOf(new Random()));
        datas.add(String.valueOf(new Random()));
        datas.add(String.valueOf(new Random()));
        datas.add(String.valueOf(new Random()));
        datas.add(String.valueOf(new Random()));
        datas.add(String.valueOf(new Random()));
        datas.add(String.valueOf(new Random()));
        datas.add(String.valueOf(new Random()));
        adapter.addList(datas);
    }

    int i = 1;
    private void mockHeadData() {
        adapter.addHeader("header" + i++);
    }

    // 到倒数第五就可以加载更多了
    @DownloadMoreListener(loadPosition = 5)
    void recyclerView() {
        mockData();
    }

    @RefreshListner()
    @Background
    void refreshView() {
        // TODO, in other thread, such as downloading res
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        stopRefresh();
    }

    @UiThread
    void stopRefresh() {
        refreshView.setRefreshing(false);
        mockHeadData();
    }

    public static class SimpleListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private List<String> datas;

        public SimpleListAdapter() {
        }

        public void addHeader(String head) {
            if (this.datas == null) {
                this.datas = new ArrayList<>();
            }

            this.datas.add(0, head);
            notifyDataSetChanged();
        }

        public void addList(List<String> datas) {
            if (this.datas != null) {
                this.datas.addAll(datas);
            } else {
                this.datas = datas;
            }

            notifyDataSetChanged();
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

            TextView textView = new TextView(viewGroup.getContext());
            return new VH(textView);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
            String text = datas.get(position);
            VH vh = (VH) viewHolder;
            vh.textView.setText(text);
        }

        @Override
        public int getItemCount() {
            return datas != null ? datas.size() : 0;
        }

        static class VH extends RecyclerView.ViewHolder {
            public TextView textView;
            public VH(@NonNull View itemView) {
                super(itemView);
                textView = (TextView) itemView;
            }
        }
    }
}
