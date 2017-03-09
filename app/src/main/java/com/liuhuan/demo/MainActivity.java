package com.liuhuan.demo;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    //    private MyRecycleViewAdapter myRecycleViewAdapter;
    private RefreshFootAdapter mRefreshFootAdapter;
    private ArrayList<BlogInfo> datas = new ArrayList<>();
    private Spinner mSpinner;
    private SwipeRefreshLayout mSwipe;
    private int lastVisibleItem;
    private String jsonStr;
    private static final int SUCC = 0;//获取图片成功的标识
    private static final int FAIL = 1;//获取图片成功的标识

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        recycleInit();
    }

    private void init() {
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
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {//此方法在ui线程运行
            switch (msg.what) {
                case SUCC:
                    mRefreshFootAdapter.notifyDataSetChanged();
                    break;
                case FAIL:
                    Toast.makeText(MainActivity.this, "无更新", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    private void recycleInit() {

        new Thread(runnable).start();
        //下拉刷新

        mRecyclerView = (RecyclerView) findViewById(R.id.mRecycleView);

        mLinearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

//        myRecycleViewAdapter = new MyRecycleViewAdapter(datas);
        mRefreshFootAdapter = new RefreshFootAdapter(this, datas);
        mRecyclerView.setAdapter(mRefreshFootAdapter);
        mRecyclerView.addItemDecoration(new MyDecoration(this, MyDecoration.VERTICAL_LIST));
        mSwipe = (SwipeRefreshLayout) findViewById(R.id.mSwipe);
        mSwipe.setColorSchemeResources(R.color.google_blue, R.color.google_green, R.color.google_red, R.color.google_yellow);
        mSwipe.setProgressViewOffset(false, 0, (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources()
                        .getDisplayMetrics()));

        mSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        new Thread(runnable).start();
                        //数据重新加载完成后，提示数据发生改变，并且设置现在不在刷新
//                        mRefreshFootAdapter.notifyDataSetChanged();
                        mSwipe.setRefreshing(false);
                    }
                }, 4000);

//                mSwipe.setRefreshing(false);
            }
        });
        mRefreshFootAdapter.setOnItemClickListener(new RefreshFootAdapter.OnItemClickListener() {
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
                switch (view.getId()) {

                    case R.id.mPic:
                        Toast.makeText(MainActivity.this, "头像", Toast.LENGTH_SHORT).show();
                        ImageButton mPic = (ImageButton) view.findViewById(R.id.mPic);
                        mPic.setBackground(getResources().getDrawable(R.drawable.ic_launcher));
                        break;
                    case R.id.mContent:
                        Toast.makeText(MainActivity.this, "内容", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.mTime:
                        TextView mTime = (TextView) view.findViewById(R.id.mTime);
                        mTime.setText(new Date() + "");
                }
            }
        });

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == mRefreshFootAdapter.getItemCount()) {
                    mRefreshFootAdapter.changeMoreStatus(RefreshFootAdapter.LOADING_MORE);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            List<String> newDatas = new ArrayList<String>();
                            for (int i = 0; i < 5; i++) {
                                int index = i + 1;
                                BlogInfo blogInfo = new BlogInfo();
                                blogInfo.setContent("新添加的"+index);
                                blogInfo.setPub_date(new Date().toString());
                                blogInfo.setUser_id("The XXX");
                                datas.add(blogInfo);
                            }
//                            mRefreshFootAdapter.addMoreItem(newDatas);
                            mRefreshFootAdapter.changeMoreStatus(RefreshFootAdapter.PULLUP_LOAD_MORE);
                        }
                    }, 1000);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = mLinearLayoutManager.findLastVisibleItemPosition();
            }
        });
    }

    @Override
    public void onClick(View view) {

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

    Runnable runnable = new Runnable() {

        @Override
        public void run() {//run()在新的线程中运行
            try {
                URL url = new URL("http://192.168.1.157:81/index.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(10000);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                conn.setDoOutput(true);


                // 输出返回结果
                InputStream input = conn.getInputStream();
                int resLen = 0;
                byte[] res = new byte[1024];
                StringBuilder sb = new StringBuilder();
                while ((resLen = input.read(res)) != -1) {
                    sb.append(new String(res, 0, resLen));
                }
                jsonStr = sb.toString();
                //String转换成JSON
                //Json的解析类对象
                Log.d("数据库数据：", jsonStr);
                JsonParser parser = new JsonParser();
                //将JSON的String 转成一个JsonArray对象
                JsonArray jsonArray = parser.parse(jsonStr).getAsJsonArray();

                Gson gson = new Gson();
//                ArrayList<BlogInfo> userBeanList = new ArrayList<>();
                datas.clear();

                //加强for循环遍历JsonArray
                for (JsonElement user : jsonArray) {
                    //使用GSON，直接转成Bean对象
                    BlogInfo userBean = gson.fromJson(user, BlogInfo.class);
                    Log.d("blog:", "info:" + userBean.getContent());
                    datas.add(0, userBean);
                }
                mHandler.obtainMessage(SUCC, datas).sendToTarget();//向ui线程发送SUCCESS标识和数据


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
}
