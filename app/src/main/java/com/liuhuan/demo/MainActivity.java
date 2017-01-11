package com.liuhuan.demo;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,View.OnClickListener {

    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private MyRecycleViewAdapter myRecycleViewAdapter;
    private List<String> datas = new ArrayList<>();
    private Spinner mSpinner;
//    private ArrayAdapter<String> adapter;
//    private static final String[] m=
//            {"收藏","屏蔽","取消关注","举报","取消"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
            this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        recycleInit();
    }


    private void recycleInit() {
        Button add = (Button) findViewById(R.id.button1);
        Button delete = (Button) findViewById(R.id.button2);
        add.setOnClickListener(this);
        delete.setOnClickListener(this);

        datas.add("之前大胜李世石的AlphaGo化名Master再次挑战人类，结果以60连胜的战绩横扫比赛，其中也包括现今棋力最强的柯洁。近日，柯洁接受采访坦言只剩震撼。柯洁表示，跟机器人对抗失败是非常正常的事情，现阶段AI依然有破绽，但是最终会战胜人类。作为人类，只要拼搏过了，输也没有遗憾。你认同柯洁的预言么？");
        datas.add("【教育部：大中小学教材一律改为“十四年抗战”】为落实中央关于纪念中国抗日战争暨世界反法西斯战争胜利70周年有关精神，加强爱国主义教育，教育部组织历史专家进行了认真研究，对教材修改工作进行了全面部署，日前基础教育二司又专门发函对中小学地方教材修订提出了要求。具体内容戳图看！");
        datas.add("Nokia 6 的诞生离不开这块铝合金，从打磨到成品需要耗时 12 小时，经过跌落测试得出，Nokia 从 1 米掉落百次以上也仅仅留下轻微划痕");

        mRecyclerView = (RecyclerView)findViewById(R.id.mRecycleView);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        myRecycleViewAdapter = new MyRecycleViewAdapter(datas);
        mRecyclerView.setAdapter(myRecycleViewAdapter);
        mRecyclerView.addItemDecoration(new MyDecoration(this,MyDecoration.VERTICAL_LIST));
        myRecycleViewAdapter.setOnItemClickListener(new MyRecycleViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(final View view, int position) {
                view.animate()
                        .translationZ(15f)
                        .setDuration(300)
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                view.animate()
                                        .translationZ(1f)
                                        .setDuration(500)
                                        .start();
                            }
                        }).start();
            }
        });
        
    }

    public void  add(View view){
        datas.add("添加的测试微博~~~~~~~demo");
        int position  = datas.size();
        if (position > 0){
            myRecycleViewAdapter.notifyDataSetChanged();
        }
    }
    public void  delete(View view){
        int position  = datas.size();
        if (position >  0){
            datas.remove(position - 1);
            myRecycleViewAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button1 :
                add(view);
                break;
            case R.id.button2 :
                delete(view);
                break;

        }
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
