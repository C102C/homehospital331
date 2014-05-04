package com.krl.data;

public class DataProcess
{
	public static Boolean MoveAverage(int [] dst, int [] src, int size, int window)
	{
		int i = 0, n = window * 2, m = window * 2 + 1;
		if (size < m)
		{
			dst = src;
			return false;
		}
		double s = 0;
		for (i = 0; i < m; i++)
		{
			s += src[i];
			dst[i] = 0;
		}
		n = size - window;
		for (i = window; i < n; i++)
		{
			dst[i] = (int) (src[i] - s / m);
			s -= src[i - window];
			s += src[i + window];
		}
		for (; i < size; i++)
		{
			dst[i] = 0;
		}
		return true;
	}

	// ¹ýÂË50Hz¸ÉÈÅ
	public static double Filter50HzDisturbance(double [] pX, double [] pY)
	{
		final int i = 2;
		pY[i] = pX[i] - 1.900 * pX[i - 1] + pX[i - 2] + 1.845 * pY[i - 1] - 0.94 * pY[i - 2];
		return pY[i];
	}
}
