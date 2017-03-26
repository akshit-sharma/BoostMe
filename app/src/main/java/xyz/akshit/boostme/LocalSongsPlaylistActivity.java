package xyz.akshit.boostme;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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

        ContentResolver musicResolver = getContentResolver();
        Uri musicUri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor musicCursor = musicResolver.query(musicUri, null, null, null, null);
        if(musicCursor!=null && musicCursor.moveToFirst()){

            //get columns
            int titleColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media.TITLE);
            int idColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media._ID);
            int artistColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media.ARTIST);
            int urlColomn = musicCursor.getColumnIndex(android.provider.MediaStore.Audio.Radio);
            //add songs to list
//            int thumbnail =  musicCursor.getColumnIndex(android.provider.MediaStore.Images.Thumbnails);
            do {
                long thisId = musicCursor.getLong(idColumn);
                String id  =   thisId+"";
                String thisTitle = musicCursor.getString(titleColumn);
                String thisArtist = musicCursor.getString(artistColumn);
//                songList.add(new Song(thisId, thisTitle, thisArtist));
//                Log.d("who is ","id"+ thisId+" this Title " + thisTitle);
                DummyContent dummyContent = new DummyContent();
                dummyContent.addItem(new DummyContent.DummyItem(id,thisTitle,thisArtist));


            }
            while (musicCursor.moveToNext());
        }




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
