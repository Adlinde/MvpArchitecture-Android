package com.qingmei2.module_a.mvp.ui;

import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.annimon.stream.Optional;
import com.jakewharton.rxbinding2.view.RxView;
import com.qingmei2.module.base.BaseActivity;
import com.qingmei2.module.http.entity.UserInfo;
import com.qingmei2.module.image.GlideApp;
import com.qingmei2.module_a.R;
import com.qingmei2.module_a.databinding.ActivityHomeBinding;
import com.qingmei2.module_a.mvp.contract.HomeContract;
import com.qingmei2.module_a.mvp.presenter.HomePresenter;
import com.qingmei2.module_business.model.UserModel;
import com.qingmei2.module_business.router.RouterConstants;

import java.util.concurrent.TimeUnit;

@Route(path = RouterConstants.ModuleA.ACTIVITY_HOME)
public class HomeActivity extends BaseActivity<HomePresenter, ActivityHomeBinding> implements HomeContract.View {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_home;
    }

    @Autowired
    UserModel user;

    @Override
    protected void initView() {
        RxView.clicks(b.btnUserInfo)
                .throttleFirst(500, TimeUnit.MILLISECONDS)
                .subscribe(v -> presenter.requestUserInfo("qingmei2"));
    }

    @Override
    protected void inject() {
        super.inject();
        ARouter.getInstance().inject(this);
    }

    @Override
    protected void initData() {
        b.tvUserInfo.setText(Optional.ofNullable(user)
                .map(UserModel::toString)
                .map(s -> "跨module传递的数据:" + s)
                .orElse("跨module传递的数据为空"));
    }

    @Override
    public void onGetUserInfo(UserInfo userInfo) {
        b.tvUserInfo.setText("网络请求Login:" + userInfo.toString());
        GlideApp.with(this)
                .load(userInfo.getAvatarUrl())
                .circleCrop()
                .into(b.ivTest);
    }

    @Override
    public void onError(String errorMassage) {
        Toast.makeText(this, errorMassage, Toast.LENGTH_SHORT).show();
    }

}
