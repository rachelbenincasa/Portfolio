package Model;

import java.net.URL;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

/**
 * @author Rachel Benincasa
 * @author Cole Hersh
 * This class plays a song and loops it
 */

public class SongPlayer {
	private MediaPlayer mediaPlayer;

	/**
	 * plays the song associated with the specified filename.
	 * @param resourcePath - the song file
	 */
	public void playSong(String resourcePath) {
		URL mediaUrl = getClass().getResource(resourcePath);
		if (mediaUrl == null) {
			System.out.println(resourcePath + " not found");
			return;
		}
		String mediaStringUrl = mediaUrl.toExternalForm();

		try {
			Media media = new Media(mediaStringUrl);
			mediaPlayer = new MediaPlayer(media);
			mediaPlayer.setOnEndOfMedia(new Waiter());
			mediaPlayer.play();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * plays the song associated with the media object
	 * @param media - the song as a media object
	 */
	public void playSong(Media media) {
		try {
			mediaPlayer = new MediaPlayer(media);
			mediaPlayer.setOnEndOfMedia(new Waiter());
			mediaPlayer.play();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Ends the current song.  Used to
	 * play a new one
	 */
	public void dispose() {
		if (mediaPlayer != null) {
			mediaPlayer.dispose();
			mediaPlayer = null;
		}
	}

	private class Waiter implements Runnable {
		public void run() {
			Media media = mediaPlayer.getMedia();
			mediaPlayer.dispose();
			mediaPlayer = null;
			playSong(media);
		}
	}
}