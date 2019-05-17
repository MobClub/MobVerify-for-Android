package com.mob.mobverify.demo;

import android.view.View;
import android.widget.Toast;

import com.mob.mobverify.ui.component.VerifyCommonButton;

public class SuccessActivity extends BaseActivity {
	private static final String TAG = "SuccessActivity";
	private VerifyCommonButton toHomepageBtn;

	@Override
	protected int getContentViewId() {
		return R.layout.activity_success;
	}

	@Override
	protected void getTitleStyle(TitleStyle titleStyle) {
		titleStyle.titleResName = "mob_verify_demo_success_title_one_key_login";
	}

	@Override
	protected void onViewCreated() {
		initView();
	}

	@Override
	protected void onViewClicked(View v) {
		int id = v.getId();
		if (id == toHomepageBtn.getId()) {
			finish();
		}
	}

	private void initView() {
		toHomepageBtn = findViewById(R.id.mob_verify_demo_success_to_homepage_btn);
		toHomepageBtn.setOnClickListener(this);
	}
}
