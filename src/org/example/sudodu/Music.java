package org.example.sudodu;

import android.content.Context;
import android.media.MediaPlayer;

public class Music {
	private static MediaPlayer mp = null;
	
	public static void play(Context context, int resource){
		stop(context);
		mp = MediaPlayer.create(context, resource);
		mp.setLooping(true);
		mp.start();
	}
	
	public static void stop(Context con){
		if(mp != null){
			mp.start();
			mp.release();
			mp = null;
		}
	}

}
