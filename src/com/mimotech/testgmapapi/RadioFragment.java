package com.mimotech.testgmapapi;

import java.io.IOException;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import com.actionbarsherlock.app.SherlockFragment;

public class RadioFragment extends SherlockFragment implements OnClickListener {
	private String tag = this.getClass().getSimpleName();
	private View viewMainFragment;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		this.viewMainFragment = inflater.inflate(R.layout.radio_fragment,
				container, false);
		//setContentView(R.layout);

        initializeUIElements();

        initializeMediaPlayer();
		
		
		Log.d(tag,"onCreateView");
		return viewMainFragment;
	}

	public void onViewCreated(View view, Bundle savedInstanceState) {
		Log.d(tag, "onViewCreated");
		super.onViewCreated(view, savedInstanceState);
	}

	public void onStart() {
		super.onStart();
		Log.d(tag, "onStart");

	}

	public void onActivityCreated(final Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Log.d(tag, "onActivityCreated");

	}

	public void reloadViewAfterRequestTaskComplete() {
		Log.d(tag, "reloadViewAfterRequestTaskComplete");

	}
	
	
    private ProgressBar playSeekBar;

    private Button buttonPlay;

    private Button buttonStopPlay;

    private MediaPlayer player;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
    }

    private void initializeUIElements() {

        playSeekBar = (ProgressBar) viewMainFragment.findViewById(R.id.progressBar1);
        playSeekBar.setMax(100);
        playSeekBar.setVisibility(View.INVISIBLE);

        buttonPlay = (Button) viewMainFragment.findViewById(R.id.buttonPlay);
        buttonPlay.setOnClickListener(this);

        buttonStopPlay = (Button) viewMainFragment.findViewById(R.id.buttonStopPlay);
        buttonStopPlay.setEnabled(false);
        buttonStopPlay.setOnClickListener(this);

    }

    public void onClick(View v) {
        if (v == buttonPlay) {
            startPlaying();
        } else if (v == buttonStopPlay) {
            stopPlaying();
        }
    }

    private void startPlaying() {
        buttonStopPlay.setEnabled(true);
        buttonPlay.setEnabled(false);

        playSeekBar.setVisibility(View.VISIBLE);
        try{
            player.prepareAsync();
        }
        catch(IllegalStateException e){
        	e.printStackTrace();
        }

        player.setOnPreparedListener(new OnPreparedListener() {

            public void onPrepared(MediaPlayer mp) {
                player.start();
            }
        });

    }

    private void stopPlaying() {
        if (player.isPlaying()) {
            player.stop();
            player.release();
            initializeMediaPlayer();
        }

        buttonPlay.setEnabled(true);
        buttonStopPlay.setEnabled(false);
        playSeekBar.setVisibility(View.INVISIBLE);
    }

    private void initializeMediaPlayer() {
        player = new MediaPlayer();
        try {
            player.setDataSource("http://122.155.16.48:8955");
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        player.setOnBufferingUpdateListener(new OnBufferingUpdateListener() {

            public void onBufferingUpdate(MediaPlayer mp, int percent) {
                playSeekBar.setSecondaryProgress(percent);
                Log.i("Buffering", "" + percent);
            }
        });
    }

    @Override
	public void onPause() {
        super.onPause();
        if (player.isPlaying()) {
            player.stop();
        }
    }

}
