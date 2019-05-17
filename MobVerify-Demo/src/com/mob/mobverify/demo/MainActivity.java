package com.mob.mobverify.demo;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mob.mobverify.MobVerify;
import com.mob.mobverify.OperationCallback;
import com.mob.mobverify.datatype.LoginResult;
import com.mob.mobverify.demo.util.Const;
import com.mob.mobverify.exception.VerifyException;
import com.mob.mobverify.gui.MobVerifyGui;
import com.mob.mobverify.gui.MobVerifyGuiCallback;
import com.mob.mobverify.gui.datatype.GuiVerifyResult;
import com.mob.mobverify.gui.view.component.VerifyCommonButton;

import java.util.ArrayList;

public class MainActivity extends BaseActivity {
	private static final String TAG = "MainActivity";
	private ImageView logoIv;
	private VerifyCommonButton verifyBtn;
	private VerifyCommonButton oneKeyLoginBtn;
	private TextView versionTv;

	@Override
	protected int getContentViewId() {
		return R.layout.activity_main;
	}

	@Override
	protected void getTitleStyle(TitleStyle titleStyle) {
		titleStyle.showLeft = false;
	}

	@Override
	protected void onViewCreated() {

		initView();
		checkPermissions();
	}

	@Override
	protected void onViewClicked(View v) {
		int id = v.getId();
		switch (id) {
			case R.id.mob_verify_demo_main_verify: {
				verify();
				break;
			}
			case R.id.mob_verify_demo_main_one_key_login: {
				oneKeyLogin();
				break;
			}
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// 页面销毁时需要注销Callback，防止内存泄漏
		MobVerifyGui.unRegisterCallback();
	}

	private void initView() {
		logoIv = findViewById(R.id.mob_verify_demo_main_logo);
		verifyBtn = findViewById(R.id.mob_verify_demo_main_verify);
		oneKeyLoginBtn = findViewById(R.id.mob_verify_demo_main_one_key_login);
		versionTv = findViewById(R.id.mob_verify_demo_main_version);
		versionTv.setText(MobVerify.getVersion());

		logoIv.setImageDrawable(getIcon());
		verifyBtn.setOnClickListener(this);
		oneKeyLoginBtn.setOnClickListener(this);
	}

	/* 检查使用权限 */
	private void checkPermissions() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			try {
				PackageManager pm = getPackageManager();
				PackageInfo pi = pm.getPackageInfo(getPackageName(), PackageManager.GET_PERMISSIONS);
				ArrayList<String> list = new ArrayList<String>();
				for (String p : pi.requestedPermissions) {
					if (checkSelfPermission(p) != PackageManager.PERMISSION_GRANTED) {
						list.add(p);
					}
				}
				if (list.size() > 0) {
					String[] permissions = list.toArray(new String[list.size()]);
					if (permissions != null) {
						requestPermissions(permissions, 1);
					}
				}
			} catch (Throwable t) {
				t.printStackTrace();
			}
		}
	}

	private Drawable getIcon() {
		PackageManager pm = getPackageManager();
		try {
			ApplicationInfo ai = pm.getApplicationInfo(getPackageName(), 0);
			return ai.loadIcon(pm);
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}
		return getResources().getDrawable(R.drawable.ic_launcher);
	}

	private void verify() {
		MobVerifyGui.verify(this, MobVerifyGui.GuiType.LOGIN, Const.SMS_TEMP_CODE, new MobVerifyGuiCallback<GuiVerifyResult>() {
			@Override
			public void onComplete(GuiVerifyResult data) {
				if (data != null) {
					Log.d(TAG, data.toJSONString());
					String msg = "Phone: " + data.getPhone() + "\nVerified: " + data.isVerified() + "\nVerifyType: " + data.getVerifyType();
					Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();

					boolean isVerified = data.isVerified();
					if (isVerified) {
						// 验证通过，执行开发者自己的逻辑
					} else {
						// 验证不通过
					}
				}
			}

			@Override
			public void onFailure(VerifyException e) {
				// 验证失败
				Log.e(TAG, "Verify failed", e);
				// 错误码
				int errCode = e.getCode();
				// 错误信息
				String errMsg = e.getMessage();
				// 更详细的网络错误信息可以通过t查看，请注意：t有可能为null
				Throwable t = e.getCause();
				String errDetail = null;
				if (t != null) {
					errDetail = t.getMessage();
				}

				String msg = "errCode: " + errCode + "\nerrMsg: " + errMsg;
				if (!TextUtils.isEmpty(errDetail)) {
					msg += "\nerrDetail: " + errDetail;
				}
				Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
			}
		});
	}

	private void oneKeyLogin() {
		MobVerify.login(Const.SMS_TEMP_CODE, new OperationCallback<LoginResult>() {
			@Override
			public void onComplete(LoginResult data) {
				if (data != null) {
					Log.d(TAG, data.toJSONString());
					String msg = "Phone: " + data.getPhone() + "\nLoginType: " + data.getLoginType()
							+ "\nOpenId: " + data.getOpenId() + "\nEmail: " + data.getEmail() + "\nNickname: " + data.getNickName()
							+ "\nOperator: " + data.getOperator() + "\nUserIconUrl: " + data.getUserIconUrl() + "\nUserIconUr2: " + data.getUserIconUr2()
							+ "\nUserIconUl3: " + data.getUserIconUr3();
					Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();

					// 登录成功，执行开发者自己的逻辑
					gotoSuccessActivity();
				}
			}

			@Override
			public void onFailure(VerifyException e) {
				// 登录失败
				Log.e(TAG, "One key login failed", e);
				// 错误码
				int errCode = e.getCode();
				// 错误信息
				String errMsg = e.getMessage();
				// 更详细的网络错误信息可以通过t查看，请注意：t有可能为null
				Throwable t = e.getCause();
				String errDetail = null;
				if (t != null) {
					errDetail = t.getMessage();
				}

				String msg = "errCode: " + errCode + "\nerrMsg: " + errMsg;
				if (!TextUtils.isEmpty(errDetail)) {
					msg += "\nerrDetail: " + errDetail;
				}
				Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
			}
		});
	}

	private void gotoSuccessActivity() {
		Intent i = new Intent(this, SuccessActivity.class);
		startActivity(i);
	}

	private void reverify() {
		MobVerifyGui.verify(this, MobVerifyGui.GuiType.REVERIFY, Const.SMS_TEMP_CODE, new MobVerifyGuiCallback<GuiVerifyResult>() {
			@Override
			public void onComplete(GuiVerifyResult data) {
				if (data != null) {
					Log.d(TAG, data.toJSONString());
					String msg = "Phone: " + data.getPhone() + "\nVerified: " + data.isVerified() + "\nVerifyType: " + data.getVerifyType();
					Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();

					boolean isVerified = data.isVerified();
					if (isVerified) {
						// 验证通过，执行开发者自己的逻辑
					} else {
						// 验证不通过
					}
				}
			}

			@Override
			public void onFailure(VerifyException e) {
				// 验证失败
				Log.e(TAG, "Verify failed", e);
				// 错误码
				int errCode = e.getCode();
				// 错误信息
				String errMsg = e.getMessage();
				// 更详细的网络错误信息可以通过t查看，请注意：t有可能为null
				Throwable t = e.getCause();
				String errDetail = null;
				if (t != null) {
					errDetail = t.getMessage();
				}

				String msg = "errCode: " + errCode + "\nerrMsg: " + errMsg;
				if (!TextUtils.isEmpty(errDetail)) {
					msg += "\nerrDetail: " + errDetail;
				}
				Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
			}
		});
	}
}
