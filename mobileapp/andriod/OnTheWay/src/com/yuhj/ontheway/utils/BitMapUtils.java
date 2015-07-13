package com.yuhj.ontheway.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class BitMapUtils {

	public BitMapUtils() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 * @param data
	 * @param reqWidth
	 * @param reqHeight
	 * @return
	 */
	public static Bitmap decodeBitmap(byte[] data, int reqWidth, int reqHeight) {
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;// ���������ڴ�ռ�
		BitmapFactory.decodeByteArray(data, 0, data.length, options);
		options.inSampleSize = calculateInSampleSize(options, reqWidth,
				reqHeight);
		options.inPurgeable = true;// �����з��������ʱ����Ҫ����
		options.inInputShareable = false;
		options.inJustDecodeBounds = false;// ���ʵ�ʵ�ͼƬ����
		return BitmapFactory.decodeByteArray(data, 0, data.length, options);
	}

	/**
	 * ����ͼƬ�Ŀ�߽���ѹ��.
	 * 
	 * @param options
	 * @param reqWidth
	 * @param reqHeight
	 * @return
	 */
	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;
		if (height > reqHeight || width > reqWidth) {
			if (width > height) {
				inSampleSize = Math.round((float) height / (float) reqHeight);
			} else {
				inSampleSize = Math.round((float) width / (float) reqWidth);
			}
		}
		return inSampleSize;
	}
}
