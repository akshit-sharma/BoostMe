package xyz.akshit.boostme;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

import xyz.akshit.boostme.dummy.DummyContent;

import static android.net.wifi.p2p.WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION;
import static android.net.wifi.p2p.WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION;
import static android.net.wifi.p2p.WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION;
import static android.net.wifi.p2p.WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION;

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
    ClientCodeRunner clientCodeRunner;
    private final IntentFilter intentFilter = new IntentFilter();
    public static final String TAG = "wifidirectdemo";
    private BroadcastReceiver receiver = null;
    private boolean isWifiP2pEnabled = false;
    private boolean retryChannel = false;
    WifiP2pManager mManager;
    WifiP2pManager.Channel mChannel;
    BroadcastReceiver mReceiver;
    IntentFilter mIntentFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_local_songs_playlist);


        mManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        mChannel = mManager.initialize(this, getMainLooper(), null);
        mReceiver = new WiFiDirectBroadcastReceiver(mManager, mChannel);
        WifiManager wm = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        String ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());

        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(WIFI_P2P_STATE_CHANGED_ACTION);
        mIntentFilter.addAction(WIFI_P2P_PEERS_CHANGED_ACTION);
        mIntentFilter.addAction(WIFI_P2P_CONNECTION_CHANGED_ACTION);
        mIntentFilter.addAction(WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
        mManager.discoverPeers(mChannel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onFailure(int reasonCode) {

            }
        });




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

            int fullpath =  musicCursor.getColumnIndex((MediaStore.Audio.Media.DATA));
            //add songs to list
//            int thumbnail =  musicCursor.getColumnIndex(android.provider.MediaStore.Images.Thumbnails);
            do {
                long thisId = musicCursor.getLong(idColumn);
                String id  =   thisId+"";
                String thisTitle = musicCursor.getString(titleColumn);
                String thisArtist = musicCursor.getString(artistColumn);
                String urlColomn = musicCursor.getString(fullpath);
//                Log.d("localSongs",urlColomn);
//
//       songList.add(new Song(thisId, thisTitle, thisArtist));
//                Log.d("who is ","id"+ thisId+" this Title " + thisTitle);
                DummyContent dummyContent = new DummyContent();
                dummyContent.addItem(new DummyContent.DummyItem(id,thisTitle,urlColomn));


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
    public void onListFragmentInteraction(DummyContent.DummyItem item) throws IOException {

      String hostIP  = SharedDataStructure.hostIP;
        String songName = item.content;
        String contentURI = item.details;
        Log.d("localPlaylistactivity", "ip  song name and uri"+hostIP +"  "+songName+"  "+contentURI );
        MediaPlayer mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setDataSource(contentURI);
        mediaPlayer.prepare();
        mediaPlayer.start();

       clientCodeRunner = new ClientCodeRunner(this, hostIP, 8888, contentURI);
        clientCodeRunner.start();

    }
    class ClientCodeRunner extends Thread {

        private Context myContext;
        private String host;
        private int port;
        private String songuri;

        ClientCodeRunner(Context myContext, String host, int port,String cURI){
            this.myContext = myContext;
            this.host = host;
            this.port = port;
            this.songuri = cURI;
        }

        @Override
        public void run() {
            super.run();

            this.clientCode();
        }

        public void clientCode() {
            Context context = myContext.getApplicationContext();
            int len;
            Socket socket = new Socket();
            byte buf[] = new byte[1024];

            try {
                /**
                 * Create a client socket with the host,
                 * dataPort, and timeout information.
                 */
                socket.bind(null);
                socket.connect((new InetSocketAddress(host, port)), 500);

                /**
                 * Create a byte stream from a JPEG file and pipe it to the output stream
                 * of the socket. This data will be retrieved by the server device.
                 */
                Log.d("MAinAct","Sending file");
                OutputStream outputStream = socket.getOutputStream();
                ContentResolver cr = context.getContentResolver();
                File myFile = new File(songuri);
                InputStream inputStream = null;
                inputStream = new FileInputStream(myFile);//(Uri.parse("DCIM/Camera/IMG_20160724_110133.jpg"));
                while ((len = inputStream.read(buf)) != -1) {
                    outputStream.write(buf, 0, len);
                }
                outputStream.close();
                inputStream.close();
            } catch (FileNotFoundException e) {
                //catch logic
                e.printStackTrace();
            } catch (IOException e) {
                //catch logic
                e.printStackTrace();
            }

            /**
             * Clean up any open sockets when done
             * transferring or if an exception occurred.
             */ finally {
                if (socket != null) {
                    if (socket.isConnected()) {
                        try {
                            Log.d("song ", "send_______ completed");
                            socket.close();
                        } catch (IOException e) {
                            //catch logic
                        }
                    }
                }
                LocalSongsPlaylistActivity.this.clientCodeRunner = null;
            }

        }
    }







//    class ClientCodeRunner extends AsyncTask<Void, Void, Void> {
//
//        private String songName, contentURI;
//        private Context myContext;
//        private String host;
//        private int port;
//        int len;
//        Socket socket;
//        byte buf[];
//
//        ClientCodeRunner(Context myContext, String host, int port,
//                         String songName, String contentURI){
//            this.myContext = myContext;
//            this.host = host;
//            this.port = port;
//            this.songName = songName;
//            this.contentURI = contentURI;
//        }
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//
//            myContext = myContext.getApplicationContext();
//            int len;
//            socket = new Socket();
//            buf = new byte[1024];
//
//            try {
//                socket.bind(null);
//                socket.connect((new InetSocketAddress(host, port)), 500);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//
//        @Override
//        protected void onPostExecute(Void aVoid) {
//            super.onPostExecute(aVoid);
//
//            if (socket != null) {
//                if (socket.isConnected()) {
//                    try {
//                        socket.close();
//                    } catch (IOException e) {
//                        //catch logic
//                        e.printStackTrace();
//                    }
//                }
//            }
//
//        }
//
//        @Override
//        protected Void doInBackground(Void... voids) {
//            try {
//                Log.d("MAinAct","Sending file");
//                OutputStream outputStream = socket.getOutputStream();
//                ContentResolver cr = myContext.getContentResolver();
//                String toSend = songName+","+contentURI;
//                byte[] sendBuffer = toSend.getBytes();
//                outputStream.write(sendBuffer);
//                outputStream.close();
//            } catch (FileNotFoundException e) {
//                //catch logic
//                e.printStackTrace();
//            } catch (IOException e) {
//                //catch logic
//                e.printStackTrace();
//            }
//            return null;
//        }
//    }

}
