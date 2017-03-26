package xyz.akshit.boostme;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import xyz.akshit.boostme.dummy.SharedDummyContent;

public class NowPlayingActivity extends AppCompatActivity implements SharedMusicFragment.OnListFragmentInteractionListener{

    private View rootView;
    private ImageView prev_image, next_image, play_image;

    private View fragmentHolder;
    private Fragment listShareSongs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_now_playing);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        rootView = (View) findViewById(R.id.activity_now_playing);
        prev_image = (ImageView) findViewById(R.id.prev_image);
        play_image = (ImageView) findViewById(R.id.play_image);
        next_image = (ImageView) findViewById(R.id.next_image);

        fragmentHolder = (View) findViewById(R.id.shared_playlist_holder);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(rootView, "Show QR to scan here", Snackbar.LENGTH_SHORT).show();
                //Show QR here
                Intent intent = new Intent(getApplicationContext(), QRCodeGenerator.class);
                startActivity(intent);
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        prev_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(rootView, "Implement this", Snackbar.LENGTH_SHORT).show();
            }
        });

        next_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(rootView, "Implement this", Snackbar.LENGTH_SHORT).show();
            }
        });

        play_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(rootView, "Implement this", Snackbar.LENGTH_SHORT).show();
            }
        });


        if(listShareSongs==null){
            listShareSongs = SharedMusicFragment.newInstance(1);
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(fragmentHolder.getId(),listShareSongs);
            fragmentTransaction.commit();
        }

    }

    @Override
    public void onListFragmentInteraction(SharedDummyContent.SharedDummyItem item) {
        // TODO
        Log.d("NPA","Impliment onListFragmentInteraction");
    }
}
