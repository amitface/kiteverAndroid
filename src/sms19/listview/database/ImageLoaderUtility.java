package sms19.listview.database;

import com.kitever.android.R;
import android.content.Context;
import android.graphics.Bitmap;

import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.FIFOLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

/**
 * THis class is used to intialize the image loader and load the image
 * 
 * @author Kalpit Seksaria
 * 
 */
public class ImageLoaderUtility {

	private ImageLoader imageLoader;

	private int emptyDrawableID = R.drawable.ic_launcher;

	private int failDrawableID = R.drawable.ic_launcher;

	public ImageLoaderUtility(Context c) {
		imageLoader = ImageLoader.getInstance();
		initImageLoader(c);

	}

	private void initImageLoader(Context c) {
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(c).threadPoolSize(5).threadPriority(Thread.MIN_PRIORITY + 2).memoryCacheSize(5 * 1024 * 1024)
				.memoryCache(new FIFOLimitedMemoryCache(5 * 1024 * 1024)).denyCacheImageMultipleSizesInMemory().discCacheFileNameGenerator(new HashCodeFileNameGenerator()).build();

		imageLoader.init(config);
	}

	public DisplayImageOptions getImageOptions() {

		DisplayImageOptions imageOptions = new DisplayImageOptions.Builder().showImageOnFail(failDrawableID).showImageOnLoading(emptyDrawableID)
				.showImageForEmptyUri(emptyDrawableID).cacheInMemory(true).cacheOnDisc(true).displayer(new FadeInBitmapDisplayer(500)).bitmapConfig(Bitmap.Config.RGB_565)
				.imageScaleType(ImageScaleType.EXACTLY).build();
		return imageOptions;
	}

	public ImageLoader getImageLoader() {
		return imageLoader;
	}

	public void setEmptyDrawableID(int emptyDrawableID) {
		this.emptyDrawableID = emptyDrawableID;
	}

	public void setFailDrawableID(int failDrawableID) {
		this.failDrawableID = failDrawableID;
	}

}
