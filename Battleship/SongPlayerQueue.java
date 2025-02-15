package Model;

import java.net.URL;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

/**
 * @author Rachel Benincasa
 * @author Cole Hersh 
 * This class plays songs in order from when they occur It
 * then notifies the GameView when a song is done
 */
public class SongPlayerQueue extends Observable {
	private MediaPlayer mediaPlayer;
	private Queue<String> queue = new LinkedBlockingQueue<String>();

	/**
	 * plays the song associated with the specified filename.
	 * 
	 * @param resourcePath - a string that is the file
	 */
	public void playSong(String resourcePath) {
		URL mediaUrl = getClass().getResource(resourcePath);
		if (mediaUrl == null) {
			System.out.println(resourcePath + " not found");
			return;
		}
		String mediaStringUrl = mediaUrl.toExternalForm();
		if (queue.isEmpty()) {
			queue.add(mediaStringUrl);
			playSongIfThereIsOneToPlay();
		} else {
			queue.add(mediaStringUrl);
		}

	}

	/**
	 * returns if the songs are done paying
	 * 
	 * @return a boolean that is true if the queue is empty and the media player is
	 *         idle
	 */
	public boolean isOver() {
		return mediaPlayer == null
				|| (queue.size() == 0 && !mediaPlayer.getStatus().equals(MediaPlayer.Status.PLAYING));
	}

	private void changeState() {
		notifyObservers(this);
	}

	private void playSongIfThereIsOneToPlay() {
		System.out.println(queue);
		if (!queue.isEmpty() && (mediaPlayer == null || !mediaPlayer.getStatus().equals(MediaPlayer.Status.PLAYING))) {
			String curr = queue.peek();
			Media media = new Media(curr);
			mediaPlayer = new MediaPlayer(media);
			mediaPlayer.setOnEndOfMedia(new Waiter());
			mediaPlayer.play();
		}
	}

	private class Waiter implements Runnable {
		public void run() {
			queue.poll();
			mediaPlayer.dispose();
			mediaPlayer = null;
			playSongIfThereIsOneToPlay();
			changeState();

		}
	}

}