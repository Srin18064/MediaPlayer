package srin.dev.g.mediaplayer.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

import srin.dev.g.mediaplayer.R;

public class PlayerActivity extends AppCompatActivity {

    TextView tvSong, tvSongStart,tvSongStop;
    SeekBar musicBar;
    Button btnPlayPause, btnNext, btnPrevious, btnForward, btnRewind;
    ImageView imageView;
    String songName, sName;
    int position;
    static MediaPlayer player;
    ArrayList<File> mySongs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        initView();

        // Checking song is Playing or Not
        if (player != null) {
            //we will start the media player if currently there no songs in it
            player.start();
            player.release();
        }

        // getting the required details from the Intent
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        mySongs = (ArrayList) bundle.getIntegerArrayList("songs");
        sName = intent.getStringExtra("songName");
        position = bundle.getInt("pos");

        //Extracting the file name from the Arraylist
        Uri uri = Uri.parse(mySongs.get(position).toString());
        songName = mySongs.get(position).getName();
        tvSong.setText(songName);

        //passing the path to the media player
        player = MediaPlayer.create(getApplicationContext(),uri);
        player.start();

        // method to get current media and time
        songEndTime();

    }

    private void songEndTime() {
        String endTime = createDuration(player.getDuration());
        tvSongStop.setText(endTime);
    }

    private String createDuration(int duration) {
        String time = "";
        int min = duration/ 1000 / 60;
        int sec = duration / 1000 % 60;

        time = time + min + ":";

        if (sec <= 0) {
            time += "0";

        }
        time += sec;
        return time;
    }

    private void initView() {
        tvSong = findViewById(R.id.tvSong);
        tvSongStart = findViewById(R.id.tvSongStart);
        tvSongStop = findViewById(R.id.tvSongStop);

        btnForward = findViewById(R.id.btnForward);
        btnRewind = findViewById(R.id.btnRewind);
        btnNext = findViewById(R.id.btnNext);
        btnPrevious = findViewById(R.id.brnPrevious);
        btnPlayPause = findViewById(R.id.btnPlayPause);

        musicBar = findViewById(R.id.musicBar);
        imageView = findViewById(R.id.imageView2);
    }
}