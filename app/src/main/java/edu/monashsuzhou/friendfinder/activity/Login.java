package edu.monashsuzhou.friendfinder.activity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import org.litepal.LitePal;

import java.io.IOException;
import java.util.List;

import edu.monashsuzhou.friendfinder.R;

import edu.monashsuzhou.friendfinder.MainActivity;
import edu.monashsuzhou.friendfinder.litepalbean.DatabaseHelper;
import edu.monashsuzhou.friendfinder.litepalbean.MiniStudent;
import edu.monashsuzhou.friendfinder.litepalbean.StudentProfile;
import edu.monashsuzhou.friendfinder.util.HttpUtil;
import edu.monashsuzhou.friendfinder.util.LoadingDialog;
import edu.monashsuzhou.friendfinder.util.MD5Util;
import edu.monashsuzhou.friendfinder.util.SharedPreferencesUtils;

public class Login extends AppCompatActivity
        implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private EditText et_account;
    private EditText et_password;
    private Button mLoginBtn;
    private Button signUpBtn;
    private CheckBox checkBox_rem;
    private CheckBox checkBox_skip;
    private ImageView iv_see_password;

    private LoadingDialog mLoadingDialog; //显示正在加载的对话框

    private Toolbar toolbar;
    private static int id = -1;

    //测试数据：email为2@2，密码为1，经加密为c4ca4238a0b923820dcc509a6f75849b


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.toolbar_items, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_home) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        //house
        toolbar = findViewById(R.id.login_toolbar);
        setSupportActionBar(toolbar);

        initViews();
        setupEvents();
        initData();
        //initDatabase();

    }
    private void initDatabase(){
        SQLiteDatabase db = LitePal.getDatabase();
        DatabaseHelper dh = new DatabaseHelper();

        StudentProfile sp = new StudentProfile();
        sp.setStudentId(2);
        sp.setFirstName("zhp");
        dh.insertStudent(sp);


        MiniStudent ms_1 = new MiniStudent();
        ms_1.setStudentid(2);
        ms_1.setLongtude(10);
        ms_1.setLatitude(10);
        ms_1.setFirstname("my");
        dh.insertMatchingStudent(ms_1);

        ms_1 = new MiniStudent();
        ms_1.setStudentid(3);
        ms_1.setLongtude(15);
        ms_1.setLatitude(15);
        ms_1.setFirstname("cool");
        dh.insertFriend(ms_1,10,10);

        ms_1 = new MiniStudent();
        ms_1.setStudentid(4);
        ms_1.setLongtude(9);
        ms_1.setLatitude(10);
        ms_1.setFirstname("cool");
        dh.insertFriend(ms_1,10,10);

        ms_1 = new MiniStudent();
        ms_1.setStudentid(5);
        ms_1.setLongtude(15);
        ms_1.setLatitude(20);
        ms_1.setFirstname("cool");
        dh.insertFriend(ms_1,10,10);

        ms_1 = new MiniStudent();
        ms_1.setStudentid(6);
        ms_1.setLongtude(21);
        ms_1.setLatitude(30);
        ms_1.setFirstname("cool");
        dh.insertFriend(ms_1,10,10);

        MiniStudent ms_2 = new MiniStudent();
        ms_2.setStudentid(3);
        ms_2.setLongtude(50);
        ms_2.setLatitude(120);
        ms_2.setFirstname("not cool");
        dh.insertMatchingStudent (ms_2);

    }

    private void initData() {


        //判断用户是否是第一次登陆
        if (firstLogin()) {
            checkBox_rem.setChecked(false);//取消记住密码的复选框
            checkBox_skip.setChecked(false);//取消自动登录的复选框
        }
        //判断是否记住密码
        if (remenberPassword()) {
            checkBox_rem.setChecked(true);//勾选记住密码
            setTextAccountAndPassword();//把密码和账号输入到输入框中
        } else {
            setTextAccount();//把用户账号放到输入账号的输入框中
        }

        //判断是否自动登录
        if (skipLogin()) {
            checkBox_skip.setChecked(true);
            login();//去登录就可以

        }
    }

    /**
     * 把本地保存的数据设置数据到输入框中
     */
    public void setTextAccountAndPassword() {
        et_account.setText("" + getLocalAccount());
        et_password.setText("" + getLocalPassword());
    }

    /**
     * 设置数据到输入框中
     */
    public void setTextAccount() {
        et_account.setText("" + getLocalAccount());
    }


    /**
     * 获得保存在本地的用户名
     */
    public String getLocalAccount() {
        //获取SharedPreferences对象，使用自定义类的方法来获取对象
        SharedPreferencesUtils helper = new SharedPreferencesUtils(this, "setting");
        String account = helper.getString("account");
        return account;
    }


    /**
     * 获得保存在本地的密码
     */
    public String getLocalPassword() {
        //获取SharedPreferences对象，使用自定义类的方法来获取对象
        SharedPreferencesUtils helper = new SharedPreferencesUtils(this, "setting");
        String password = helper.getString("password");
        return password;

    }

    /**
     * 判断是否自动登录
     */
    private boolean skipLogin() {
        //获取SharedPreferences对象，使用自定义类的方法来获取对象
        SharedPreferencesUtils helper = new SharedPreferencesUtils(this, "setting");
        boolean skipLogin = helper.getBoolean("skipLogin", false);
        return skipLogin;
    }

    /**
     * 判断是否记住密码
     */
    private boolean remenberPassword() {
        //获取SharedPreferences对象，使用自定义类的方法来获取对象
        SharedPreferencesUtils helper = new SharedPreferencesUtils(this, "setting");
        boolean remenberPassword = helper.getBoolean("remenberPassword", false);
        return remenberPassword;
    }


    private void initViews() {
        mLoginBtn = (Button) findViewById(R.id.btn_login);
        signUpBtn = (Button) findViewById(R.id.btn_sub);
        et_account = (EditText) findViewById(R.id.et_account);
        et_password = (EditText) findViewById(R.id.et_password);
        checkBox_rem = (CheckBox) findViewById(R.id.checkBox_rem);
        checkBox_skip = (CheckBox) findViewById(R.id.checkBox_skip);
        iv_see_password = (ImageView) findViewById(R.id.iv_see_password);
    }

    private void setupEvents() {
        mLoginBtn.setOnClickListener(this);
        signUpBtn.setOnClickListener(this);
        checkBox_rem.setOnCheckedChangeListener(this);
        checkBox_skip.setOnCheckedChangeListener(this);
        iv_see_password.setOnClickListener(this);

    }

    /**
     * 判断是否是第一次登陆
     */
    private boolean firstLogin() {
        //获取SharedPreferences对象，使用自定义类的方法来获取对象
        SharedPreferencesUtils helper = new SharedPreferencesUtils(this, "setting");
        boolean first = helper.getBoolean("first", true);
        if (first) {
            //创建一个ContentVa对象（自定义的）设置不是第一次登录，,并创建记住密码和自动登录是默认不选，创建账号和密码为空
            helper.putValues(new SharedPreferencesUtils.ContentValue("first", false),
                    new SharedPreferencesUtils.ContentValue("remenberPassword", false),
                    new SharedPreferencesUtils.ContentValue("skipLogin", false),
                    new SharedPreferencesUtils.ContentValue("account", ""),
                    new SharedPreferencesUtils.ContentValue("password", ""));
            return true;
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                loadUseraccount();    //无论如何保存一下用户名
                login(); //登陆
                break;
            case R.id.iv_see_password:
                setPasswordVisibility();    //改变图片并设置输入框的文本可见或不可见
                break;
            case R.id.btn_sub:
                jumpToSubscriptionPage();
                break;

        }
    }

    /**
     * 跳转到subscription 页面
     */
    private void jumpToSubscriptionPage() {
        startActivity(new Intent(this, Subscription.class));
    }


    /**
     * 获得服务器端的密码
     */
    public String getServerPassword() {
        String account = getAccount();
        if(!account.isEmpty() && account.contains("@")) {
            String info = null;
            try {
                info = HttpUtil.get("Profile", "findByEmail/" + account);
            } catch (IOException e) {
                e.printStackTrace();
            }
            JSONArray profList = JSON.parseArray(info);
            if(profList.size() == 0){
                //没有这个用户
                return null;
            }else{
                //查到了用户
                JSONObject prof = profList.getJSONObject(0);
                String pswd = prof.getString("password");
                id = prof.getInteger("studentId");
                return pswd;
            }
        }
        return null;
    }



    /**
     * 登录
     * 检查客户端密码pswd_c是否等于服务器端密码pswd_s，相同就能登录成功，否则登录失败
     */
    private void login() {
        //先做一些基本的判断，比如输入的用户命为空，密码为空，网络不可用多大情况，都不需要去链接服务器了，而是直接返回提示错误
        if (getAccount().isEmpty()){
            showToast("你输入的账号为空！");
            return;
        }

        if (getPassword().isEmpty()){
            showToast("你输入的密码为空！");
            return;
        }
        //登录一般都是请求服务器来判断密码是否正确，要请求网络，要子线程
        showLoading();//显示加载框
        Thread loginRunnable = new Thread() {

            @Override
            public void run() {
                super.run();
                setLoginBtnClickable(false);//点击登录后，设置登录按钮不可点击状态


                //睡眠2秒
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                //判断账号和密码，输入的密码经MD5加密后是否和服务器端存储的一致
                if (MD5Util.GetMD5Code(getPassword()).equals(getServerPassword())) {
                    showToast("登录成功");
                    loadCheckBoxState(checkBox_rem, checkBox_skip);//记录下当前用户记住密码和自动登录的状态;

                    startActivity(new Intent(Login.this, MainActivity.class));
                    finish();//关闭页面
                } else {
                    showToast("输入的登录账号或密码不正确");
                }

                setLoginBtnClickable(true);  //这里解放登录按钮，设置为可以点击
                hideLoading();//隐藏加载框
            }
        };
        loginRunnable.start();


    }


    /**
     * 保存用户账号
     */
    public void loadUseraccount() {
        if (!getAccount().equals("") || !getAccount().equals("e-mail")) {
            SharedPreferencesUtils helper = new SharedPreferencesUtils(this, "setting");
            helper.putValues(new SharedPreferencesUtils.ContentValue("account", getAccount()));
        }

    }

    /**
     * 设置密码可见和不可见的相互转换
     */
    private void setPasswordVisibility() {
        if (iv_see_password.isSelected()) {
            iv_see_password.setSelected(false);
            //密码不可见
            et_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

        } else {
            iv_see_password.setSelected(true);
            //密码可见
            et_password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        }

    }

    /**
     * 获取账号
     */
    public String getAccount() {
        return et_account.getText().toString().trim();//去掉空格
    }

    /**
     * 获取密码
     */
    public String getPassword() {
        return et_password.getText().toString().trim();//去掉空格
    }


    /**
     * 保存用户选择“记住密码”和“自动登陆”的状态
     */
    public void loadCheckBoxState(CheckBox checkBox_rem, CheckBox checkBox_skip) {

        //获取SharedPreferences对象，使用自定义类的方法来获取对象
        SharedPreferencesUtils helper = new SharedPreferencesUtils(this, "setting");

        //如果设置自动登录
        if (checkBox_skip.isChecked()) {
            //创建记住密码和自动登录是都选择,保存密码数据
            helper.putValues(
                    new SharedPreferencesUtils.ContentValue("remenberPassword", true),
                    new SharedPreferencesUtils.ContentValue("skipLogin", true),
                    new SharedPreferencesUtils.ContentValue("password", getPassword()));

        } else if (!checkBox_rem.isChecked()) { //如果没有保存密码，那么自动登录也是不选的
            //创建记住密码和自动登录是默认不选,密码为空
            helper.putValues(
                    new SharedPreferencesUtils.ContentValue("remenberPassword", false),
                    new SharedPreferencesUtils.ContentValue("skipLogin", false),
                    new SharedPreferencesUtils.ContentValue("password", ""));
        } else if (checkBox_rem.isChecked()) {   //如果保存密码，没有自动登录
            //创建记住密码为选中和自动登录是默认不选,保存密码数据
            helper.putValues(
                    new SharedPreferencesUtils.ContentValue("remenberPassword", true),
                    new SharedPreferencesUtils.ContentValue("skipLogin", false),
                    new SharedPreferencesUtils.ContentValue("password", getPassword()));
        }
    }

    /**
     * 是否可以点击登录按钮
     *
     * @param clickable
     */
    public void setLoginBtnClickable(boolean clickable) {
        mLoginBtn.setClickable(clickable);

    }


    /**
     * 显示加载的进度款
     */
    public void showLoading() {
        if (mLoadingDialog == null) {
            mLoadingDialog = new LoadingDialog(this, getString(R.string.loading), false);
        }
        mLoadingDialog.show();
    }


    /**
     * 隐藏加载的进度框
     */
    public void hideLoading() {
        if (mLoadingDialog != null) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mLoadingDialog.hide();
                }
            });

        }
    }


    /**
     * CheckBox点击时的回调方法 ,不管是勾选还是取消勾选都会得到回调
     *
     * @param buttonView 按钮对象
     * @param isChecked  按钮的状态
     */
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (buttonView == checkBox_rem) {  //记住密码选框发生改变时
            if (!isChecked) {   //如果取消“记住密码”，那么同样取消自动登陆
                checkBox_skip.setChecked(false);
            }
        } else if (buttonView == checkBox_skip) {   //自动登陆选框发生改变时
            if (isChecked) {   //如果选择“自动登录”，那么同样选中“记住密码”
                checkBox_rem.setChecked(true);
            }
        }
    }


    /**
     * 监听回退键
     */
    @Override
    public void onBackPressed() {
        if (mLoadingDialog != null) {
            if (mLoadingDialog.isShowing()) {
                mLoadingDialog.cancel();
            } else {
                finish();
            }
        } else {
            finish();
        }

    }

    /**
     * 页面销毁前回调的方法
     */
    protected void onDestroy() {
        if (mLoadingDialog != null) {
            mLoadingDialog.cancel();
            mLoadingDialog = null;
        }
        super.onDestroy();
    }


    public void showToast(String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(Login.this, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }



    public static int getCurrentId(){
        return id;
    }

    public static void setCurrentId(int _id) {
        id = _id;
    }

}