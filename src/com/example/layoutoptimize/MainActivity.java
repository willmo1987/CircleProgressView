package com.example.layoutoptimize;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.example.layoutoptimize.CircleProgressView.CirclePrgressMode;

public class MainActivity extends ActionBarActivity implements OnClickListener {

	private CircleProgressView circleView;
	private Button startButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		circleView = (CircleProgressView) findViewById(R.id.circleView);
		startButton = (Button) findViewById(R.id.startDraw);
		startButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		circleView.startDrawProgress(CirclePrgressMode.CIRCLE_PROGRESS_MODE_DYNAMIC, 75);
	}

}
