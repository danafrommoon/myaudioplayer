package com.example.myaudioplayer

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.View
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class MainActivity : AppCompatActivity() {

    lateinit var imageView: ImageView
    lateinit var songTitle : TextView
    var currentIndex = 0
    val songs:ArrayList<Int> = ArrayList()
    lateinit var mMediaPlayer: MediaPlayer
    lateinit var mSeekBarTime: SeekBar
    private val CHANNEL_ID = "channel_id_example_01"
    private val notificationId = 101


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        createNotificationChannel()

        songs.add(0, R.raw.furelise)
        songs.add(1, R.raw.gluboko)
        songs.add(2, R.raw.makeitright)
        songs.add(3, R.raw.regular)
        songs.add(4, R.raw.saveyourtears)

        mMediaPlayer = MediaPlayer.create(getApplicationContext(), songs.get(currentIndex))

        imageView = findViewById(R.id.imageView)
        songTitle = findViewById(R.id.songTitle)
        mSeekBarTime = findViewById(R.id.seekBarTime)

        var play: ImageView = findViewById(R.id.play)
        var prev:ImageView = findViewById(R.id.prev)
        var next:ImageView = findViewById(R.id.next)
        var mSeekBarVol:SeekBar = findViewById(R.id.seekBarVol)
        val runnable:Runnable
        val mAudioManager: AudioManager =getSystemService(AUDIO_SERVICE) as AudioManager



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

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val name = "Notification title"
            val descriptionText = "Notification Description"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val  notificationManager : NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun songNames() {
        if (currentIndex === 0)
        {
            songTitle.setText("Fur Elise - Ludwig van Beethoven")
            imageView.setImageResource(R.drawable.furelise)

            val intent = Intent(this,MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }

            val pendingIntent: PendingIntent = PendingIntent.getActivity(this,0,intent,0)

            val bitmap = BitmapFactory.decodeResource(applicationContext.resources, R.drawable.furelise)
            val bitmapLargeIcon = BitmapFactory.decodeResource(applicationContext.resources, R.drawable.furelise)

            val builder = NotificationCompat.Builder(this, CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .setContentTitle("Fur Elise - Ludwig van Beethoven")
                    .setContentText("now playing . . .")
                    .setLargeIcon(bitmapLargeIcon)
                    .setStyle(NotificationCompat.BigPictureStyle().bigPicture(bitmap))
                    .setContentIntent(pendingIntent)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)

            with(NotificationManagerCompat.from(this)){
                notify(notificationId, builder.build())
            }
        }
        if (currentIndex === 1)
        {
            songTitle.setText("Глубоко - Монатик")
            imageView.setImageResource(R.drawable.gluboko)

            val intent = Intent(this,MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }

            val pendingIntent:PendingIntent = PendingIntent.getActivity(this,0,intent,0)

            val bitmap = BitmapFactory.decodeResource(applicationContext.resources, R.drawable.gluboko)
            val bitmapLargeIcon = BitmapFactory.decodeResource(applicationContext.resources, R.drawable.gluboko)

            val builder = NotificationCompat.Builder(this, CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .setContentTitle("Глубоко - Монатик")
                    .setContentText("now playing . . .")
                    .setLargeIcon(bitmapLargeIcon)
                    .setStyle(NotificationCompat.BigPictureStyle().bigPicture(bitmap))
                    .setContentIntent(pendingIntent)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)

            with(NotificationManagerCompat.from(this)){
                notify(notificationId, builder.build())
            }
        }
        if (currentIndex === 2)
        {
            songTitle.setText("Make It Right - BTS")
            imageView.setImageResource(R.drawable.makeitright)

            val intent = Intent(this,MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }

            val pendingIntent:PendingIntent = PendingIntent.getActivity(this,0,intent,0)

            val bitmap = BitmapFactory.decodeResource(applicationContext.resources, R.drawable.makeitright)
            val bitmapLargeIcon = BitmapFactory.decodeResource(applicationContext.resources, R.drawable.makeitright)

            val builder = NotificationCompat.Builder(this, CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .setContentTitle("Make It Right - BTS")
                    .setContentText("now playing . . .")
                    .setLargeIcon(bitmapLargeIcon)
                    .setStyle(NotificationCompat.BigPictureStyle().bigPicture(bitmap))
                    .setContentIntent(pendingIntent)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)

            with(NotificationManagerCompat.from(this)){
                notify(notificationId, builder.build())
            }
        }
        if (currentIndex === 3)
        {
            songTitle.setText("Regular - NCT")
            imageView.setImageResource(R.drawable.regular)

            val intent = Intent(this,MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }

            val pendingIntent:PendingIntent = PendingIntent.getActivity(this,0,intent,0)

            val bitmap = BitmapFactory.decodeResource(applicationContext.resources, R.drawable.regular)
            val bitmapLargeIcon = BitmapFactory.decodeResource(applicationContext.resources, R.drawable.regular)

            val builder = NotificationCompat.Builder(this, CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .setContentTitle("Regular - NCT")
                    .setContentText("now playing . . .")
                    .setLargeIcon(bitmapLargeIcon)
                    .setStyle(NotificationCompat.BigPictureStyle().bigPicture(bitmap))
                    .setContentIntent(pendingIntent)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)

            with(NotificationManagerCompat.from(this)){
                notify(notificationId, builder.build())
            }
        }
        if (currentIndex === 4)
        {
            songTitle.setText("Save Your Tears - The Weeknd")
            imageView.setImageResource(R.drawable.saveyourtears)

            val intent = Intent(this,MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }

            val pendingIntent:PendingIntent = PendingIntent.getActivity(this,0,intent,0)

            val bitmap = BitmapFactory.decodeResource(applicationContext.resources, R.drawable.saveyourtears)
            val bitmapLargeIcon = BitmapFactory.decodeResource(applicationContext.resources, R.drawable.saveyourtears)

            val builder = NotificationCompat.Builder(this, CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .setContentTitle("Save Your Tears - The Weeknd")
                    .setContentText("now playing . . .")
                    .setLargeIcon(bitmapLargeIcon)
                    .setStyle(NotificationCompat.BigPictureStyle().bigPicture(bitmap))
                    .setContentIntent(pendingIntent)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)

            with(NotificationManagerCompat.from(this)){
                notify(notificationId, builder.build())
            }
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