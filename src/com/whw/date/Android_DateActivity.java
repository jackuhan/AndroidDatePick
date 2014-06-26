package com.whw.date;

import java.util.Calendar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

public class Android_DateActivity extends Activity implements OnClickListener {
	/** Called when the activity is first created. */
	private Button button;
	private TextView textview;
	private Dialog mdialog;
	private Calendar calendar = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main);

		initUi();
		button.setOnClickListener(this);
	}

	private void initUi() {
		button = (Button) findViewById(R.id.button);
		textview = (TextView) findViewById(R.id.textview);
	}

	@Override
	public void onClick(View v) {
		showDialog(0);// 日期弹出框
		int SDKVersion = Android_DateActivity.this.getSDKVersionNumber();// 获取系统版本
		System.out.println("SDKVersion = " + SDKVersion);
		DatePicker dp = findDatePicker((ViewGroup) mdialog.getWindow()
				.getDecorView());// 设置弹出年月日
		if (dp != null) {
            // 设置弹出年，隐藏月和日，getChildAt(0)为年，getChildAt(1)为月，getChildAt(2)为日
			if (SDKVersion < 11) {
				((ViewGroup) dp.getChildAt(0)).getChildAt(1).setVisibility(
						View.GONE);
			} else if (SDKVersion > 14) {
				((ViewGroup) ((ViewGroup) dp.getChildAt(0)).getChildAt(0))
						.getChildAt(1).setVisibility(View.GONE);
			}
		}

	}

	@Override
	protected Dialog onCreateDialog(int id) { // 对应上面的showDialog(0);//日期弹出框
		mdialog = null;
		switch (id) {
		case 0:
			calendar = Calendar.getInstance();
			mdialog = new CustomerDatePickerDialog(this,//CustomerDatePickerDialog--DatePickerDialog互换
					new CustomerDatePickerDialog.OnDateSetListener() {//CustomerDatePickerDialog--DatePickerDialog互换
						@Override
						public void onDateSet(DatePicker view, int year,
								int monthOfYear, int dayOfMonth) {
							textview.setText(year + "-" + (monthOfYear + 1));
						}
					}, calendar.get(Calendar.YEAR),
					calendar.get(Calendar.MONTH),
					calendar.get(Calendar.DAY_OF_MONTH));
			break;
		}
		return mdialog;
	}

    /**
     * 发现标题栏也要改，通过查看DatePickerDialog源码，需要自定义并实现onDateChanged方法才可实现
     * CustomerDatePickerDialog--DatePickerDialog互换
     *  */
    class CustomerDatePickerDialog extends DatePickerDialog {

        public CustomerDatePickerDialog(Context context,
                                        OnDateSetListener callBack, int year, int monthOfYear,
                                        int dayOfMonth) {
            super(context, callBack, year, monthOfYear, dayOfMonth);
        }

        @Override
        public void onDateChanged(DatePicker view, int year, int month, int day) {
            super.onDateChanged(view, year, month, day);
            mdialog.setTitle(year + "年");
        }
    }

	/**
	 * 从当前Dialog中查找DatePicker子控件
	 * 
	 * @param group
	 * @return
	 */
	private DatePicker findDatePicker(ViewGroup group) {
		if (group != null) {
			for (int i = 0, j = group.getChildCount(); i < j; i++) {
				View child = group.getChildAt(i);
				if (child instanceof DatePicker) {
					return (DatePicker) child;
				} else if (child instanceof ViewGroup) {
					DatePicker result = findDatePicker((ViewGroup) child);
					if (result != null)
						return result;
				}
			}
		}
		return null;
	}

	/**
	 * 获取系统SDK版本
	 * 
	 * @return
	 */
	public static int getSDKVersionNumber() {
		int sdkVersion;
		try {
			sdkVersion = Integer.valueOf(android.os.Build.VERSION.SDK);
		} catch (NumberFormatException e) {
			sdkVersion = 0;
		}
		return sdkVersion;
	}
}