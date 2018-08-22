package pers.sanfen.android.swiperefreshview;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import pers.sanfen.android.swiperefreshview.widget.SwipeRefreshLayout;

public class MainActivity extends AppCompatActivity {

    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        final SwipeRefreshLayout refreshLayout = findViewById(R.id.refresh_view);
        RecyclerView recyclerView = findViewById(R.id.rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        final ItemAdapter itemAdapter = new ItemAdapter(this);
        recyclerView.setAdapter(itemAdapter);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        itemAdapter.setData(getRandomList());
                        itemAdapter.notifyDataSetChanged();
                        refreshLayout.setRefreshing(false);
                    }
                }, 3000);
            }
        });
        refreshLayout.setRefreshing(true);
    }

    private List<String> getRandomList() {
        List<String> s= new ArrayList<>();
        for (int i = 0 ; i < 50; i ++) {
            s.add("ITEM - " + (int)(Math.random() * 50));
        }
        return s;
    }
}
