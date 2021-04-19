package com.example.customnotification;

import androidx.appcompat.app.AppCompatActivity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.customnotification.Service.OnClearFromRecentService;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements Playable{
TextView title;
ImageButton playButton;

NotificationManager notificationManager;
List<Track> tracks;
int position=0;
boolean isPlaying=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        title=findViewById(R.id.titleTextViewId);
        playButton=findViewById(R.id.playButtonId);
        populateTrack();
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            createChannel();
            registerReceiver(broadcastReceiver,new IntentFilter("TRACKS_TRACKS"));
            startService(new Intent(getBaseContext(), OnClearFromRecentService.class));

        }

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPlaying){
                    onTrackPause();
                }else {
                    onTrackPlay();
                }


//                CreateNotification.createNotification(MainActivity.this,tracks.get(1),R.drawable.ic_baseline_pause_24,
//                        1,tracks.size()-1);
            }
        });

    }
private void createChannel(){
    if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
        NotificationChannel channel=new NotificationChannel(CreateNotification.CHANNEL_ID,
                "KOD Dev",NotificationManager.IMPORTANCE_LOW);

        notificationManager=getSystemService(NotificationManager.class);
        if (notificationManager!=null){
            notificationManager.createNotificationChannel(channel);
        }

    }
}
    private void populateTrack(){
        tracks=new ArrayList<>();
        tracks.add(new Track("Track 1","artist 1",R.drawable.images));
        tracks.add(new Track("Track 2","artist 2",R.drawable.images1));
        tracks.add(new Track("Track 3","artist 3",R.drawable.images2));
        tracks.add(new Track("Track 4","artist 4",R.drawable.images3));
    }

    BroadcastReceiver broadcastReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
        String action=intent.getExtras().getString("actionName");
        switch (action){
            case CreateNotification.ACTION_PREVIOUS:
                onTrackPrevious();
                break;
            case CreateNotification.ACTION_PLAY:
                if (isPlaying){
                    onTrackPause();
                }else {
                    onTrackPlay();
                }
                break;
            case CreateNotification.ACTION_NEXT:
                onTrackNext();
                break;
        }
        }
    };


    @Override
    public void onTrackPrevious() {
        position--;
        CreateNotification.createNotification(MainActivity.this,tracks.get(position),
                R.drawable.ic_baseline_pause_24,position,tracks.size()-1);
        title.setText(tracks.get(position).getTitle());
    }

    @Override
    public void onTrackPlay() {

        CreateNotification.createNotification(MainActivity.this,tracks.get(position),
                R.drawable.ic_baseline_pause_24,position,tracks.size()-1);
        playButton.setImageResource(R.drawable.ic_baseline_pause_24);
        title.setText(tracks.get(position).getTitle());
        isPlaying=true;
    }

    @Override
    public void onTrackPause() {
        CreateNotification.createNotification(MainActivity.this,tracks.get(position),
                R.drawable.ic_baseline_play_arrow_24,position,tracks.size()-1);
        playButton.setImageResource(R.drawable.ic_baseline_play_arrow_24);
        title.setText(tracks.get(position).getTitle());
        isPlaying=false;
    }

    @Override
    public void onTrackNext() {
        position++;
        CreateNotification.createNotification(MainActivity.this,tracks.get(position),
                R.drawable.ic_baseline_pause_24,position,tracks.size()-1);
        title.setText(tracks.get(position).getTitle());
    }

//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
//            notificationManager.cancelAll();
//        }
//        unregisterReceiver(broadcastReceiver);
//    }
}