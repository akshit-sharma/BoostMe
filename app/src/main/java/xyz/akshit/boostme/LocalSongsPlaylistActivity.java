package xyz.akshit.boostme;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import xyz.akshit.boostme.dummy.DummyContent;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class LocalSongsPlaylistActivity extends AppCompatActivity implements LocalSongsFragment.OnListFragmentInteractionListener{

    View rootView;
    View localSongsFragmentHolder;
    Fragment listLocalSongs;
    FloatingActionButton fab;
    Button showOngoing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_local_songs_playlist);

        rootView = (View) findViewById(R.id.activity_local_songs_playlist);
        localSongsFragmentHolder = (View) findViewById(R.id.fullscreen_content_placeholder);
        fab = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        showOngoing = (Button) findViewById(R.id.showOnGoing);

        if(listLocalSongs==null){
            listLocalSongs = LocalSongsFragment.newInstance(1);
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(localSongsFragmentHolder.getId(),listLocalSongs);
            fragmentTransaction.commit();
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(rootView, "Show QR to scan here", Snackbar.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), QRCodeGenerator.class);
                startActivity(intent);
            }
        });

        showOngoing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(LocalSongsPlaylistActivity.this, NowPlayingActivity.class);
                startActivity(myIntent);
            }
        });


    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

    }


    @Override
    public void onListFragmentInteraction(DummyContent.DummyItem item) {
        // TODO
        Log.d("LSPA","define onListFragmentInteraction");
    }
}
