package com.krl.staticecg;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.krl.data.DataDescription;
import com.krl.data.DataSegment;


public class ECGSurfaceView extends SurfaceView implements
		SurfaceHolder.Callback, Runnable {
	private static final String TAG = "ECGSurfaceView";
	boolean mbLoop = false;
	SurfaceHolder mSurfaceHolder = null;
	int nMillStep = 50; // 30ms刷新一次
	Paint mPaint = null;
	float mperMM = 0.0f;
	float mcx = 0.0f;
	float mcy = 0.0f;
	int mTopOffset = 0;
	int[][] mBuffer = null;
	Bitmap mGrid = null;
	String[] mLead = new String[] { "I", "II", "III", "aVR", "aVL", "aVF",
			"V1", "V2", "V3", "V4", "V5", "V6" };
	public int nLastPos = 0;
	public int nStartPos = 0;
	public int nEndPos = 0;
	public DataSegment mDataSegment = null;
	public int mScreenWidth = 0;
	public int nWidth = 0;
	public int nHeight = 0;

	public void init_canvas_param(DisplayMetrics metrics, DataDescription desc,
			int[][] buffer) {
		Log.e(TAG,"init_canvas_param");
		mperMM = (float) (metrics.densityDpi * 10 / 254);
		mcx = mperMM * 25.0f / desc.nSamplingFrequency;
		mcy = (float) (mperMM * 5.0f * desc.GetMvPerSample());
		nLastPos = 0;
		nStartPos = 0;
		nEndPos = 0;
		mBuffer = buffer;
		Canvas canvas = this.mSurfaceHolder.lockCanvas();
		if (canvas != null) {
			canvas.drawColor(Color.WHITE);
			this.mSurfaceHolder.unlockCanvasAndPost(canvas);
		}
	}

	public ECGSurfaceView(Context context, AttributeSet attrs) {
		super(context, attrs);
		Log.e(TAG,"ECGSurfaceView");
		// TODO 自动生成的构造函数存根
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setColor(Color.BLUE);
		this.mSurfaceHolder = this.getHolder();
		this.mSurfaceHolder.addCallback(this);
		this.setFocusable(true);
		mbLoop = true;
	}

	@Override
	public void run() {
		// TODO 自动生成的方法存根
		while (mbLoop) {
			try {
				Thread.sleep(nMillStep);
			} catch (Exception e) {
				Thread.currentThread().interrupt();
			}
			synchronized (this.mSurfaceHolder) {
				this.paint();
			}
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
		// TODO 自动生成的方法存根
		Log.e(TAG, "surfaceChanged");
	}

	@Override
	public void surfaceCreated(SurfaceHolder arg0) {
		// TODO 自动生成的方法存根
		Log.e(TAG, "surfaceCreated");
		drawBackground();
		new Thread(this).start();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder arg0) {
		// TODO 自动生成的方法存根
		Log.e(TAG, "surfaceDestroyed");
		mbLoop = false;
	}

	private void drawBackground() {
		nWidth = getWidth();
		nHeight = getHeight();
		mScreenWidth = (int) (nWidth / mcx);
		DataDescription desc = mDataSegment.mDesc;
		float nPerHeight = nHeight / desc.nLeadCount;
		mGrid = Bitmap.createBitmap(nWidth, nHeight, Bitmap.Config.ARGB_8888);
		Canvas cv = new Canvas();
		cv.setBitmap(mGrid);
		cv.drawColor(Color.WHITE);
		Paint p = new Paint();
		p.setAntiAlias(true);
		p.setColor(Color.argb(30, 255, 100, 0));
		float fgridwidth = mperMM * 5.0f;
		int nRow = (int) (nHeight / fgridwidth);
		int nCol = (int) (nWidth / fgridwidth);
		int bottom = (int) (nRow * fgridwidth);
		int right = (int) (nCol * fgridwidth);
		mTopOffset = (int) ((nHeight - bottom) / 2.0f);
		bottom += mTopOffset;
		int tmp = 0, i = 0;
		i = 0;
		while (true) {
			tmp = (int) (i * mperMM) + mTopOffset;
			if (tmp >= bottom) {
				break;
			}
			cv.drawLine(0, tmp, right, tmp, p);
			i++;
		}
		i = 0;
		while (true) {
			tmp = (int) (i * mperMM);
			if (tmp >= right) {
				break;
			}
			cv.drawLine(tmp, 0, tmp, bottom, p);
			i++;
		}
		p.setColor(Color.argb(80, 255, 100, 0));
		i = 0;
		while (true) {
			tmp = (int) (i * fgridwidth) + mTopOffset;
			if (tmp >= bottom) {
				break;
			}
			cv.drawLine(0, tmp, right, tmp, p);
			i++;
		}
		i = 0;
		while (true) {
			tmp = (int) (i * fgridwidth);
			if (tmp >= right) {
				break;
			}
			cv.drawLine(tmp, 0, tmp, bottom, p);
			i++;
		}
		cv.drawLine(0, bottom, right, bottom, p);
		cv.drawLine(right, mTopOffset, right, bottom, p);
		// p.setTextSize(20.0f);
		// int pMV = (int) (mperMM * 5.0f);
		// int signw = (int) (mperMM * 2.0f);
		// int w = signw / 3;
		// int base = 0;
		// p.setColor(Color.argb(255, 255, 0, 0));
		// for (i = 0; i < 12; i++) {
		// base = (int) (i * nPerHeight + nPerHeight / 2.0f) + mTopOffset;
		// cv.drawLine(0, base, w, base, p);
		// cv.drawLine(w, base - pMV, w, base, p);
		// cv.drawLine(w, base - pMV, w * 2, base - pMV, p);
		// cv.drawLine(w * 2, base - pMV, w * 2, base, p);
		// cv.drawLine(w * 2, base, signw, base, p);
		// cv.drawText(mLead[i], signw, base, p);
		// }
		// p.setColor(Color.argb(150, 200, 0, 0));
		// p.setTextSize(40.0f);
		// cv.drawText("25mm/s 5mm/mV", right - p.measureText("25mm/s 5mm/mV")
		// - signw, 40 + mTopOffset, p);

		Rect showRect = new Rect(0, 0, nWidth, nHeight);
		Canvas canvas = mSurfaceHolder.lockCanvas(showRect);
		if (null != canvas) {
			p.setColor(Color.argb(150, 200, 0, 0));
			canvas.drawBitmap(mGrid, showRect, showRect, p);
		}
		mSurfaceHolder.unlockCanvasAndPost(canvas);
	}

	public void paint() {
//		Log.e(TAG, "paint 1");
		nWidth = getWidth();
		nHeight = getHeight();
		mScreenWidth = (int) (nWidth / mcx);
		if (this.mSurfaceHolder == null || mDataSegment == null
				|| mBuffer == null) {
			return;
		}
//		Log.e(TAG, "paint 5");
		DataDescription desc = mDataSegment.mDesc;
		float nPerHeight = nHeight / desc.nLeadCount;
		// if (mGrid == null) {
		// mGrid = Bitmap.createBitmap(nWidth, nHeight,
		// Bitmap.Config.ARGB_8888);
		// Canvas cv = new Canvas();
		// cv.setBitmap(mGrid);
		// cv.drawColor(Color.WHITE);
		// Paint p = new Paint();
		// p.setAntiAlias(true);
		// p.setColor(Color.argb(30, 255, 100, 0));
		// float fgridwidth = mperMM * 5.0f;
		// int nRow = (int) (nHeight / fgridwidth);
		// int nCol = (int) (nWidth / fgridwidth);
		// int bottom = (int) (nRow * fgridwidth);
		// int right = (int) (nCol * fgridwidth);
		// mTopOffset = (int) ((nHeight - bottom) / 2.0f);
		// bottom += mTopOffset;
		// int tmp = 0, i = 0;
		// i = 0;
		// while (true) {
		// tmp = (int) (i * mperMM) + mTopOffset;
		// if (tmp >= bottom) {
		// break;
		// }
		// cv.drawLine(0, tmp, right, tmp, p);
		// i++;
		// }
		// i = 0;
		// while (true) {
		// tmp = (int) (i * mperMM);
		// if (tmp >= right) {
		// break;
		// }
		// cv.drawLine(tmp, 0, tmp, bottom, p);
		// i++;
		// }
		// p.setColor(Color.argb(80, 255, 100, 0));
		// i = 0;
		// while (true) {
		// tmp = (int) (i * fgridwidth) + mTopOffset;
		// if (tmp >= bottom) {
		// break;
		// }
		// cv.drawLine(0, tmp, right, tmp, p);
		// i++;
		// }
		// i = 0;
		// while (true) {
		// tmp = (int) (i * fgridwidth);
		// if (tmp >= right) {
		// break;
		// }
		// cv.drawLine(tmp, 0, tmp, bottom, p);
		// i++;
		// }
		// cv.drawLine(0, bottom, right, bottom, p);
		// cv.drawLine(right, mTopOffset, right, bottom, p);
		// p.setColor(Color.argb(150, 200, 0, 0));
		// // p.setTextSize(20.0f);
		// // int pMV = (int) (mperMM * 5.0f);
		// // int signw = (int) (mperMM * 2.0f);
		// // int w = signw / 3;
		// // int base = 0;
		// // p.setColor(Color.argb(255, 255, 0, 0));
		// // for (i = 0; i < 12; i++) {
		// // base = (int) (i * nPerHeight + nPerHeight / 2.0f) + mTopOffset;
		// // cv.drawLine(0, base, w, base, p);
		// // cv.drawLine(w, base - pMV, w, base, p);
		// // cv.drawLine(w, base - pMV, w * 2, base - pMV, p);
		// // cv.drawLine(w * 2, base - pMV, w * 2, base, p);
		// // cv.drawLine(w * 2, base, signw, base, p);
		// // cv.drawText(mLead[i], signw, base, p);
		// // }
		// // p.setColor(Color.argb(150, 200, 0, 0));
		// // p.setTextSize(40.0f);
		// // cv.drawText("25mm/s 5mm/mV", right -
		// p.measureText("25mm/s 5mm/mV")
		// // - signw, 40 + mTopOffset, p);
		// }
//		Log.e(TAG, "mScreenWidth:" + mScreenWidth);
//		Log.e(TAG, "nLastPos:" + nLastPos);
//		Log.e(TAG, "nEndPos:" + nEndPos);
//		Log.e(TAG, "nStartPos:" + nStartPos);
		int remain = Math.min(mScreenWidth - nLastPos, nEndPos - nStartPos);
		if (remain <= 0)
			return;
		Rect dst = new Rect((int) (nLastPos * mcx), 0, (int) ((nLastPos
				+ remain + 50) * mcx), nHeight);
		Canvas canvas = this.mSurfaceHolder.lockCanvas(dst);
		if (canvas == null)
			return;
		canvas.drawBitmap(mGrid, dst, dst, mPaint);
		// 画心电图
		int tX = 0, tY = 0, bX = 0, bY = 0;
		int curY = 0, i = 0, j = 0;
		for (i = DataDescription.LeadIndex_I; i <= DataDescription.LeadIndex_V6; i++) {
			curY = (int) (nPerHeight * i + nPerHeight / 2.0f);
			for (j = nStartPos; (j - nStartPos) < remain && j < nEndPos - 1; j++) {
				tX = (int) ((nLastPos + j - nStartPos) * mcx);
				bX = (int) ((nLastPos + j - nStartPos + 1) * mcx);
				tY = (int) (curY - mBuffer[i][j % mScreenWidth] * mcy)
						+ mTopOffset;
				bY = (int) (curY - mBuffer[i][(j + 1) % mScreenWidth] * mcy)
						+ mTopOffset;
				canvas.drawLine(tX, tY, bX, bY, mPaint);
			}
		}
		nLastPos += (j - nStartPos);
		if (nLastPos >= mScreenWidth) {
			nLastPos = 0;
		}
		nStartPos = j;
		this.mSurfaceHolder.unlockCanvasAndPost(canvas);
	}
}
