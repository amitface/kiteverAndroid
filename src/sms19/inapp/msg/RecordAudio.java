package sms19.inapp.msg;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.MediaRecorder;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.kitever.android.R;

import java.io.File;
import java.io.IOException;

import sms19.inapp.msg.constant.GlobalData;
import sms19.inapp.single.chatroom.SingleChatRoomFrgament;
import sms19.listview.newproject.Home;

public class RecordAudio implements OnCompletionListener {
    private static final String LOG_TAG = RecordAudio.class.getName();
    private static String mFileName = null;
    Dialog dialogMapMain;
    private MediaRecorder mRecorder = null;
    int PlayRemember = 0;
    int count = 0;
    private long startTime = 0L;
    Button mStopButton = null;
    private Handler customHandler = new Handler();
    int secs, mins;
    long timeInMilliseconds = 0L;
    long timeSwapBuff = 0L;
    long updatedTime = 0L;
    private Button mPlayButton = null, mCancel, mSend;
    private MediaPlayer mPlayer = null;
    ProgressBar progress;
    String timer_text, fixed_timer;
    TextView mRecordTime, mRecordTimefixed, mRecordTimefixed1;
    LinearLayout send_layout;
    LinearLayout progress_bar_layout;
    LinearLayout seek_bar_layout;
    View view1;
    final Handler handler = new Handler();
    int totalDuration;
    SeekBar seekBar;
    Utilities utils;
    // Handler to update UI timer, progress bar etc,.
    private Handler mHandler = new Handler();
    int counter;

    public RecordAudio() {

    }

    private void onPlay(boolean start) {
        if (start) {
            startPlaying();
        } else {
            stopPlaying();
        }
    }

    private void startPlaying() {

        try {
            mPlayer = new MediaPlayer();
            try {
                mPlayer.setDataSource(mFileName);
                mPlayer.prepare();
                mPlayer.setOnCompletionListener(this); // Important
                mPlayer.setOnPreparedListener(new OnPreparedListener() {

                    @Override
                    public void onPrepared(MediaPlayer arg0) {
                        // TODO Auto-generated method stub
                        totalDuration = mPlayer.getDuration();

                        mRecordTimefixed1.setText(""
                                + utils.milliSecondsToTimer(totalDuration));
                        mPlayer.start();
                        seekBar.setMax(100); // Set the Maximum range of the

                        // set
                        // current progress to song's
                        seekBar.setProgress(0);// set current progress to
                        // song's
                        // Updating progress bar
                        updateProgressBar();
                    }
                });
            } catch (IOException e) {
                //Log.e(LOG_TAG, "prepare() failed");
            }

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    private void stopPlaying() {
        if (mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
        }

    }

    private void startRecording() {
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setOutputFile(mFileName);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mRecorder.prepare();
            //Log.e(LOG_TAG, "prepare() failed");
        } catch (IOException e) {
            //Log.e(LOG_TAG, "prepare() failed");
        }

        try {
            mRecorder.start();
        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void stopRecording() {
        try {
            mRecorder.stop();
            mRecorder.release();
            mRecorder = null;
        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public RecordAudio(Context con) {
        utils = new Utilities();
        // Listeners

        mFileName = GlobalData.VideoPath;
        File f = new File(mFileName);
        if (f.exists()) {
            if (f.isDirectory()) {

            } else {
                f.mkdirs();
            }

        } else {
            f.mkdirs();
        }

        mFileName += "/Ucaht_" + System.currentTimeMillis() + ".3gp";

    }

    public void ReSendalertDialog(final Context con) {

        try {

            final View view = LayoutInflater.from(con).inflate(
                    R.layout.ac_record_audio, null, false);

            if (dialogMapMain != null) {

                dialogMapMain.dismiss();

            }
            final LinearLayout record_layout = (LinearLayout) view
                    .findViewById(R.id.record_layout);
            final LinearLayout Stop_layout = (LinearLayout) view
                    .findViewById(R.id.Stop_layout);
            send_layout = (LinearLayout) view.findViewById(R.id.send_layout);
            progress_bar_layout = (LinearLayout) view
                    .findViewById(R.id.progress_bar_layout);
            seek_bar_layout = (LinearLayout) view
                    .findViewById(R.id.seek_bar_layout);
            final Button mRecordButton = (Button) view
                    .findViewById(R.id.resend);
            mRecordTime = (TextView) view.findViewById(R.id.time_exit);
            mRecordTimefixed = (TextView) view.findViewById(R.id.time_fixed);

            progress = (ProgressBar) view.findViewById(R.id.progressBar1);

            view1 = view;

            mRecordButton.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    // TODO Auto-generated method stub
                    record_layout.setVisibility(View.GONE);
                    Stop_layout.setVisibility(View.VISIBLE);
                    mStopButton = (Button) view.findViewById(R.id.stop_button);
                    // boolean mStartRecording = true;
                    // onRecord(mStartRecording);
                    mStopButton.setOnClickListener(sendClickListener);
                    startTime = SystemClock.uptimeMillis();
                    progress.setProgress(0);
                    progress.setMax(1069);

                    startRecording();
                    customHandler.postDelayed(updateTimerThread, 100);
                    count++;
                    handler.postDelayed(updatetimerThread, 100);
                    // mStartRecording = !mStartRecording;

                }

            });
            final Button mPlayButton = (Button) view.findViewById(R.id.ok);
            mPlayButton.setOnClickListener(new OnClickListener() {
                // boolean mStartPlaying = true;

                @Override
                public void onClick(View arg0) {
                    // TODO Auto-generated method stub
                    // onPlay(mStartPlaying);
                    dialogMapMain.dismiss();
                    // if (mStartPlaying) {
                    // mPlayButton.setText("Stop playing");
                    // } else {
                    // mPlayButton.setText("Start playing");
                    // }
                    // mStartPlaying = !mStartPlaying;
                }
            });
            dialogMapMain = new Dialog(con);
            dialogMapMain.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialogMapMain.setContentView(view);
            dialogMapMain.setCancelable(false);

            dialogMapMain.show();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    final Runnable updatetimerThread = new Runnable() {

        @Override
        public void run() {
            // TODO Auto-generated method stub
            counter++;
            // counter=progress.getProgress()+1;
            progress.setProgress(counter);

            handler.postDelayed(this, 100);
        }
    };

    final Runnable updateTimerThread = new Runnable() {

        @SuppressLint("NewApi")
        @Override
        public void run() {

            timeInMilliseconds = SystemClock.uptimeMillis() - startTime;

            updatedTime = timeSwapBuff + timeInMilliseconds;

            int secs = (int) (updatedTime / 1000);
            int mins = secs / 60;
            secs = secs % 60;

            // int milliseconds = (int) (updatedTime % 1000);
            mRecordTime.setText("" + mins + ":" + String.format("%02d", secs));
            customHandler.postDelayed(this, 100);
            Log.i(LOG_TAG, "" + counter);
            // where this is a Context

            if (mins == 2) {
                stopRecording();
                customHandler.removeCallbacks(updateTimerThread);
                handler.removeCallbacks(updatetimerThread);
                send_layout.setVisibility(View.VISIBLE);
                progress_bar_layout.setVisibility(View.GONE);
                seek_bar_layout.setVisibility(View.VISIBLE);
                mStopButton.setVisibility(View.GONE);
                mPlayButton = (Button) view1.findViewById(R.id.play);
                mPlayButton.setOnClickListener(sendClickListener1);
                mCancel = (Button) view1.findViewById(R.id.cancel);
                mSend = (Button) view1.findViewById(R.id.send);
                seekBar = (SeekBar) view1.findViewById(R.id.seekBar1);
                seekBar.setOnSeekBarChangeListener(seekBarOnSeekChangeListener);

                mRecordTimefixed1 = (TextView) view1
                        .findViewById(R.id.time_fixed_play);
                mSend.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        dialogMapMain.dismiss();
                        SingleChatRoomFrgament.recordfilePath = mFileName;
                        SingleChatRoomFrgament.RecordAudioHandler.sendEmptyMessage(0);
                    }
                });
                mCancel.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        // TODO Auto-generated method stub
                        dialogMapMain.dismiss();
                    }
                });

				/*
                 * try { //mPlayer.setDataSource(mFileName);
				 * 
				 * } catch (IOException e) { //Log.e(LOG_TAG, "prepare() failed");
				 * }
				 */
                // mPlayer.getDuration()

            }

        }

    };
    private final OnClickListener sendClickListener = new OnClickListener() {
        @SuppressLint("NewApi")
        @Override
        public void onClick(View v) {
            stopRecording();
            customHandler.removeCallbacks(updateTimerThread);
            handler.removeCallbacks(updatetimerThread);

            send_layout.setVisibility(View.VISIBLE);
            progress_bar_layout.setVisibility(View.GONE);
            seek_bar_layout.setVisibility(View.VISIBLE);
            mStopButton.setVisibility(View.GONE);
            mPlayButton = (Button) view1.findViewById(R.id.play);
            mPlayButton.setOnClickListener(sendClickListener1);
            mCancel = (Button) view1.findViewById(R.id.cancel);

            seekBar = (SeekBar) view1.findViewById(R.id.seekBar1);

            seekBar.setOnSeekBarChangeListener(seekBarOnSeekChangeListener);
            mRecordTimefixed1 = (TextView) view1
                    .findViewById(R.id.time_fixed_play);

            mSend = (Button) view1.findViewById(R.id.send);

            mSend.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    dialogMapMain.dismiss();
                    if (Home.currentFragment.equals("SingleChatRoomFrgament")) {
                        SingleChatRoomFrgament.recordfilePath = mFileName;
                        SingleChatRoomFrgament.RecordAudioHandler.sendEmptyMessage(0);
                    } else {
                        SingleChatRoomFrgament.recordfilePath = mFileName;
                        SingleChatRoomFrgament.RecordAudioHandler.sendEmptyMessage(0);
                    } // comment m
                }
            });
            mCancel.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    // TODO Auto-generated method stub
                    dialogMapMain.dismiss();
                }
            });
			/*
			 * mPlayer = new MediaPlayer(); try {
			 * mPlayer.setDataSource(mFileName);
			 * 
			 * } catch (IOException e) { //Log.e(LOG_TAG, "prepare() failed"); }
			 */

			/*
			 * long totalDuration = mPlayer.getDuration();
			 * mRecordTimefixed1.setText("" +
			 * utils.milliSecondsToTimer(totalDuration));
			 */
        }
    };
    private final OnClickListener sendClickListener1 = new OnClickListener() {
        boolean mStartPlaying = true;

        @Override
        public void onClick(View v) {
            onPlay(mStartPlaying);
            if (mStartPlaying) {
                mPlayButton.setBackgroundResource(R.drawable.pull);

                // mPlayButton.setText("Stop playing");
            } else {
				/*
				 * mPlayer = new MediaPlayer(); try {
				 * mPlayer.setDataSource(mFileName);
				 * 
				 * } catch (IOException e) { //Log.e(LOG_TAG, "prepare() failed");
				 * }
				 */

                mPlayButton.setBackgroundResource(R.drawable.push);

                if (mPlayer != null) {
                    mPlayer.stop();
                }
            }

            // mPlayButton.setText("Start playing");

            mStartPlaying = !mStartPlaying;
        }
    };

    /**
     * Update timer on seekbar
     */
    public void updateProgressBar() {
        Log.d("fdf", "Updating progress bar");
        mHandler.postDelayed(mUpdateTimeTask, 100);
    }

    /**
     * Background Runnable thread
     */
    private Runnable mUpdateTimeTask = new Runnable() {
        public void run() {

            long currentDuration = 0;
            if (mPlayer != null) {

                currentDuration = mPlayer.getCurrentPosition();
            }
            Log.d("fdf", "Updating progress bar");
            // Displaying Total Duration time
            // songTotalDurationLabel.setText(""+utils.milliSecondsToTimer(totalDuration));
            // Displaying time completed playing
            // songCurrentDurationLabel.setText(""+utils.milliSecondsToTimer(currentDuration));

            // Updating progress bar
            int progress = utils.getProgressPercentage(currentDuration,
                    totalDuration);
            // Log.d("Progress", ""+progress);
            seekBar.setProgress(progress);

            // Running this thread after 100 milliseconds
            mHandler.postDelayed(this, 100);
        }
    };
    SeekBar.OnSeekBarChangeListener seekBarOnSeekChangeListener = new SeekBar.OnSeekBarChangeListener() {
        /**
         * When user starts moving the progress handler
         * */

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            // TODO Auto-generated method stub
            mHandler.removeCallbacks(mUpdateTimeTask);
        }

        /**
         * When user stops moving the progress hanlder
         * */
        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            // TODO Auto-generated method stub
            mHandler.removeCallbacks(mUpdateTimeTask);
            // totalDuration = mPlayer.getDuration();
            int currentPosition = utils.progressToTimer(seekBar.getProgress(),
                    totalDuration);

            // forward or backward to certain seconds
            mPlayer.seekTo(currentPosition);

            // update timer progress again
            updateProgressBar();
        }

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress,
                                      boolean fromUser) {
            // TODO Auto-generated method stub

			/*
			 * if (fromUser) { mPlayer.seekTo(progress);
			 * seekBar.setProgress(progress); }
			 */

        }
    };

    @Override
    public void onCompletion(MediaPlayer arg0) {
        // TODO Auto-generated method stub
        if (mPlayer != null) {
            mPlayer.stop();
            mPlayer.release();
            mPlayer = null;
        }

        mPlayButton.setBackgroundResource(R.drawable.push);
        seekBar.setProgress(0);
        seekBar.setMax(100);
        mHandler.removeCallbacks(mUpdateTimeTask);
        Log.d(LOG_TAG, "complete");
        // set Progress bar values

    }

}