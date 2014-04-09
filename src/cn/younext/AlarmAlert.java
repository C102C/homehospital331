package cn.younext;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class AlarmAlert extends Activity {
	String text;
	String tishi;
	TextView alarmalert_tishi;
	TextView alarmalert_text;
	Button closeBtn;
	OnClickListener btnClick;

	MediaPlayer mMediaPlayer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.alarmalert);

		Bundle extra = getIntent().getExtras();
		if (extra != null) {
			text = extra.getString("text");
			tishi = extra.getString("tishi");

		}
		// audio=(AudioManager)getSystemService(AUDIO_SERVICE);

		// audio.setStreamVolume(AudioManager.STREAM_ALARM,
		// audio.getMode(), 0);

		mMediaPlayer = new MediaPlayer();
		mMediaPlayer = MediaPlayer.create(AlarmAlert.this, R.raw.yingtao);
		mMediaPlayer.start();

		alarmalert_tishi = (TextView) findViewById(R.id.alarmalert_tishi);
		alarmalert_text = (TextView) findViewById(R.id.alarmalert_text);
		closeBtn = (Button) findViewById(R.id.alarmalert_closeBtn);
		alarmalert_tishi.setText(tishi);
		alarmalert_text.setText(text);

		btnClick = new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (v == closeBtn) {
					mMediaPlayer.stop();
					AlarmAlert.this.finish();

				} else {

				}

			}

		};
		closeBtn.setOnClickListener(btnClick);

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_MENU) {

			mMediaPlayer.stop();
			AlarmAlert.this.finish();

			return true;

		}
		return super.onKeyDown(keyCode, event);
	}
}