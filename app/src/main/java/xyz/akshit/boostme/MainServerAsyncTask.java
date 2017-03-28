package xyz.akshit.boostme;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import xyz.akshit.boostme.dummy.SharedDummyContent;

/**
 * Created by akshi on 3/26/2017.
 */

public class MainServerAsyncTask extends AsyncTask<Void, Void, String> {

    private Context context;
//    private String file_name;

    public MainServerAsyncTask(Context context) {
        this.context = context;
    }

//    @Override
//    protected void onPreExecute() {
//        super.onPreExecute();
//
////        SmallServerAsyncTask ssat = new SmallServerAsyncTask(context, this);
////        ssat.execute();
//    }

    @Override
    protected String doInBackground(Void... params) {
        try {

            /**
             * Create a server socket and wait for client connections. This
             * call blocks until a connection is accepted from a client
             */
            ServerSocket serverSocket = new ServerSocket(8888);
            Socket client = serverSocket.accept();

            /**
             * If this code is reached, a client has connected and transferred data
             * Save the input stream from the client as a JPEG file
             */
            final File f = new File(Environment.getExternalStorageDirectory() + "/"
                    + context.getPackageName() + "/boostme" + System.currentTimeMillis()
                    + ".mp3");

            File dirs = new File(f.getParent());
            if (!dirs.exists())
                dirs.mkdirs();
            f.createNewFile();
            InputStream inputstream = client.getInputStream();
            OutputStream output = new FileOutputStream(f);
            copyFile(inputstream, output);
            serverSocket.close();
            return f.getAbsolutePath();
        } catch (IOException e) {
            Log.e("MainServerAsyncTask", e.getMessage());
            return null;
        }
    }

    private void copyFile(InputStream inputStream, OutputStream output) {
        try {
            try {
                byte[] buffer = new byte[4 * 1024]; // or other buffer size
                int read;

                while ((read = inputStream.read(buffer)) != -1) {
                    output.write(buffer, 0, read);
                }
                output.flush();
            } finally {
                output.close();
            }
        } catch (Exception e) {
            e.printStackTrace(); // handle exception, define IOException and others
        }
        finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Start activity that can handle the JPEG image
     */
    @Override
    protected void onPostExecute(String result) {
        if (result != null) {
            Intent intent = new Intent();
            Log.d("recieved ","File copied - " + result);
            intent.setAction(android.content.Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.parse("file://" + result), "image/*");
            context.startActivity(intent);
        }
    }

//    @Override
//    public void setFile_name(InetAddress address, String file_name, String contentURI) {
//        SharedDummyContent.SharedDummyItem item = new SharedDummyContent.SharedDummyItem(address, file_name, contentURI);
//        SharedDataStructure.addItem(item);
//    }

}
