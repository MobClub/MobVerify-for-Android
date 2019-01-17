package com.mob.mobverify.demo;

import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mob.mobverify.MobVerify;
import com.mob.mobverify.demo.util.Const;
import com.mob.mobverify.exception.VerifyException;
import com.mob.mobverify.gui.MobVerifyGui;
import com.mob.mobverify.gui.MobVerifyGuiCallback;
import com.mob.mobverify.gui.datatype.GuiVerifyResult;
import com.mob.mobverify.gui.view.component.VerifyCommonButton;

import java.util.ArrayList;

public class MainActivity extends Activity implements View.OnClickListener {
	private static final String TAG = "MainActivity";
	private ImageView logoIv;
	private VerifyCommonButton verifyBtn;
	private VerifyCommonButton reVerifyBtn;
	private TextView versionTv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		initView();
		checkPermissions();
	}

	@Override
	public void onClick(View view) {
		int id = view.getId();
		switch (id) {
			case R.id.mob_verify_demo_main_login: {
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
						if (t != null) {
							String errDetail = t.getMessage();
						}

						String msg = "errCode: " + errCode + "\nerrMsg: " + errMsg;
						Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
					}
				});
				break;
			}
			case R.id.mob_verify_demo_main_reverify: {
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
						if (t != null) {
							String errDetail = t.getMessage();
						}

						String msg = "errCode: " + errCode + "\nerrMsg: " + errMsg;
						Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
					}
				});
				break;
			}
		}
	}

	private void initView() {
		logoIv = findViewById(R.id.mob_verify_demo_main_logo);
		verifyBtn = findViewById(R.id.mob_verify_demo_main_login);
		reVerifyBtn = findViewById(R.id.mob_verify_demo_main_reverify);
		versionTv = findViewById(R.id.mob_verify_demo_main_version);
		versionTv.setText(MobVerify.getVersion());

		logoIv.setImageDrawable(getIcon());
		verifyBtn.setOnClickListener(this);
		reVerifyBtn.setOnClickListener(this);
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
}
