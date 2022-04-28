package srin.dev.g.mediaplayer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

import srin.dev.g.mediaplayer.Activities.PlayerActivity;

public class MainActivity extends AppCompatActivity {

    private static final int MY_PERMISSION_REQUEST_CODE = 1;
    ListView lsList;
    String items[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lsList = findViewById(R.id.lsList);

        runtTimePermission();
    }

    /**
     * Checking for permission
     * if permission granted then
     * display the medias in Storage
     */
    private void runtTimePermission() {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSION_REQUEST_CODE);
        }else {
            displaySong();
        }
    }

    private void displaySong() {

        final ArrayList<File> mySongs = findSong(Environment.getExternalStorageDirectory());
        items = new String[mySongs.size()];

        // adding all the files without the extension to ArrayList

        for (int i = 0 ; i< mySongs.size() ; i++) {
            items[i] = mySongs.get(i).getName().toString().replace(".mp3","").replace(".wav","");

        }
        // calling adapter and set it to listView
        CustomAdapter customAdapter = new CustomAdapter();
        lsList.setAdapter(customAdapter);

        // setting the item onClick listener
        lsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String songName = (String) lsList.getItemAtPosition(i);

                // calling intent to send song to play activity to play the song

                Intent intent = new Intent(MainActivity.this, PlayerActivity.class);
                intent.putExtra("songs", mySongs);
                intent.putExtra("songName", songName);
                intent.putExtra("pos", i);
                startActivity(intent);
            }
        });
    }

    public ArrayList<File> findSong(File file) {
        ArrayList<File> allSongs = new ArrayList<>();
        File[] files;
        files = file.listFiles();

        for (File singleFile: files) {

            // Adding directory to the Arraylist
            if (singleFile.isDirectory() && !singleFile.isHidden()) {
                allSongs.addAll(findSong(singleFile));
            }else {
                // Adding Single files to the ArrayList
                if (singleFile.getName().endsWith(".mp3")) {
                    allSongs.add(singleFile);
                }
            }
        }
        return allSongs;
    }

    class CustomAdapter  extends BaseAdapter{

        @Override
        public int getCount() {
            return items.length;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View v = getLayoutInflater().inflate(R.layout.song_layout, null);
            TextView song_name = v.findViewById(R.id.song_name);
            song_name.setSelected(true);
            song_name.setText(items[i]);
            return v;
        }
    }
}