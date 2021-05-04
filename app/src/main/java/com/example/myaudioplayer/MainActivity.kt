package com.example.myaudioplayer

import android.annotation.SuppressLint
import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.View
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    var imageView:ImageView = findViewById(R.id.imageView)
    var songTitle: TextView = findViewById(R.id.songTitle)
    var currentIndex = 0
    val songs:ArrayList<Int> = ArrayList()
    var mMediaPlayer: MediaPlayer  = MediaPlayer.create(getApplicationContext(), songs.get(currentIndex))
    var mSeekBarTime: SeekBar = findViewById(R.id.seekBarTime)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        var play: ImageView = findViewById(R.id.play)
        var prev:ImageView = findViewById(R.id.prev)
        var next:ImageView = findViewById(R.id.next)
        var mSeekBarVol:SeekBar = findViewById(R.id.seekBarVol)
        val runnable:Runnable
        val mAudioManager: AudioManager =getSystemService(Context.AUDIO_SERVICE) as AudioManager

        songs.add(0, R.raw.furelise)
        songs.add(1, R.raw.gluboko)
        songs.add(2, R.raw.makeitright)
        songs.add(3, R.raw.regular)
        songs.add(4, R.raw.saveyourtears)


        val maxV = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
        val curV = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
        mSeekBarVol.setMax(maxV)
        mSeekBarVol.setProgress(curV)

        mSeekBarVol.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar:SeekBar, progress:Int, fromUser:Boolean) {
                mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0)
            }
            override fun onStartTrackingTouch(seekBar:SeekBar) {
            }
            override fun onStopTrackingTouch(seekBar:SeekBar) {
            }
        })

        play.setOnClickListener(object: View.OnClickListener {
            override fun onClick(v:View) {
                mSeekBarTime.setMax(mMediaPlayer.getDuration())
                if (mMediaPlayer != null && mMediaPlayer.isPlaying())
                {
                    mMediaPlayer.pause()
                    play.setImageResource(R.drawable.play_btn)
                }
                else
                {
                    mMediaPlayer.start()
                    play.setImageResource(R.drawable.pause_btn)
                }
                songNames()
            }
        })

        next.setOnClickListener(object: View.OnClickListener {
            override fun onClick(v:View) {
                if (mMediaPlayer != null)
                {
                    play.setImageResource(R.drawable.pause_btn)
                }
                if (currentIndex < songs.size - 1)
                {
                    currentIndex++
                }
                else
                {
                    currentIndex = 0
                }
                if (mMediaPlayer.isPlaying())
                {
                    mMediaPlayer.stop()
                }
                mMediaPlayer = MediaPlayer.create(getApplicationContext(), songs.get(currentIndex))
                mMediaPlayer.start()
                songNames()
            }
        })

        prev.setOnClickListener(object: View.OnClickListener {
            override fun onClick(v:View) {
                if (mMediaPlayer != null)
                {
                    play.setImageResource(R.drawable.pause_btn)
                }
                if (currentIndex > 0)
                {
                    currentIndex--
                }
                else
                {
                    currentIndex = songs.size - 1
                }
                if (mMediaPlayer.isPlaying())
                {
                    mMediaPlayer.stop()
                }
                mMediaPlayer = MediaPlayer.create(getApplicationContext(), songs.get(currentIndex))
                mMediaPlayer.start()
                songNames()
            }
        })

    }

    private fun songNames() {
        if (currentIndex === 0)
        {
            songTitle.setText("Fur Elise - Ludwig van Beethoven")
            imageView.setImageResource(R.drawable.furelise)
        }
        if (currentIndex === 1)
        {
            songTitle.setText("Глубоко - Монатик")
            imageView.setImageResource(R.drawable.gluboko)
        }
        if (currentIndex === 2)
        {
            songTitle.setText("Make It Right - BTS")
            imageView.setImageResource(R.drawable.makeitright)
        }
        if (currentIndex === 3)
        {
            songTitle.setText("Regular - NCT")
            imageView.setImageResource(R.drawable.regular)
        }
        if (currentIndex === 4)
        {
            songTitle.setText("Save Your Tears - The Weeknd")
            imageView.setImageResource(R.drawable.saveyourtears)
        }

        mMediaPlayer.setOnPreparedListener(object: MediaPlayer.OnPreparedListener {
            override fun onPrepared(mp:MediaPlayer) {
                mSeekBarTime.setMax(mMediaPlayer.getDuration())
                mMediaPlayer.start()
            }
        })

        mSeekBarTime.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar:SeekBar, progress:Int, fromUser:Boolean) {
                if (fromUser)
                {
                    mMediaPlayer.seekTo(progress)
                    mSeekBarTime.setProgress(progress)
                }
            }
            override fun onStartTrackingTouch(seekBar:SeekBar) {
            }
            override fun onStopTrackingTouch(seekBar:SeekBar) {
            }
        })

        Thread(object:Runnable {
            public override fun run() {
                while (mMediaPlayer != null)
                {
                    try
                    {
                        if (mMediaPlayer.isPlaying())
                        {
                            val message = Message()
                            message.what = mMediaPlayer.getCurrentPosition()
                            handler.sendMessage(message)
                            Thread.sleep(1000)
                        }
                    }
                    catch (e:InterruptedException) {
                        e.printStackTrace()
                    }
                }
            }
        }).start()
    }
    @SuppressLint("Handler Leak") var handler: Handler = object:Handler() {
        override fun handleMessage(msg:Message) {
            mSeekBarTime.setProgress(msg.what)
        }
    }
}