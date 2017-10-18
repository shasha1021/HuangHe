package bzu.edu.cn.huanghe.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import org.apache.http.Header;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.UnsupportedEncodingException;

import bzu.edu.cn.huanghe.R;
import bzu.edu.cn.huanghe.constant.UrlConst;
import bzu.edu.cn.huanghe.utils.JsonResult;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*
        侧滑部分
        */
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //这句话创建toolbar的关键
        setSupportActionBar(toolbar);
        //浮动按钮的事件
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        //DrawerLayout事件的监听器
        //创建左侧toolbar按钮并且关联导航栏
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        //左边滑出来的那个菜单,一个容器组件
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }
    //按下back键时,执行此方法
    //返回按钮的监听
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }




    //当导航项被点击时的回调,处理点击事件，改变item的选中状态
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_historysend) {
            // Handle the  action
        } else if (id == R.id.nav_updatepassword) {

        } else if (id == R.id.nav_aboutour) {

        } else if (id == R.id.nav_quitlogin) {
            //注销登录点击事件处理
            //弹出一个提示框
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setMessage("确认离开吗？");
            builder.setTitle("小提示");
            builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    //1.调用网络进行登录
                    AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
                    RequestParams params = new RequestParams();

                    //url:   parmas：请求时携带的参数信息   responseHandler：是一个匿名内部类接受成功过失败
                    String url = UrlConst.LOGOUT;
                    asyncHttpClient.post(url, null, new AsyncHttpResponseHandler() {

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                            //statusCode:状态码    headers：头信息  responseBody：返回的内容，返回的实体

                            //判断状态码
                            if(statusCode == 200){
                                //获取结果
                                try {
                                    String result = new String(responseBody,"utf-8");
                                    //Toast.makeText(LoginActivity.this, result, Toast.LENGTH_LONG).show();
                                    Gson gson = new Gson();
                                    JsonResult jsonResult = gson.fromJson(result, JsonResult.class);
                                    //Toast.makeText(LoginActivity.this, jsonResult.getMessage(), Toast.LENGTH_LONG).show();




                                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                                    SharedPreferences sp = MainActivity.this.getSharedPreferences("data", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sp.edit();
                                    editor.putBoolean("autoLogin", false);
                                    editor.commit();
                                    startActivity(intent);
                                } catch (UnsupportedEncodingException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers,
                                              byte[] responseBody, Throwable error) {
                        }
                    });

                }
            });
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.create().show();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
