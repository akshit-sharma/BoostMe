package xyz.akshit.boostme;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by akshi on 3/26/2017.
 */

public class SmallServerAsyncTask extends AsyncTask<Void, Void, String> {

    private int port = 8080;
    private Context context;
    private FileNameWithListener fileNameWithListener;
    private InetAddress clientAdd;
    private String contentURI;
    private String fileName;

    public SmallServerAsyncTask(Context context, FileNameWithListener fileNameWithListener) {
        this.context = context;
        this.fileNameWithListener = fileNameWithListener;
    }

    @Override
    protected String doInBackground(Void... params) {
        InputStream inputStream = null;
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            while (true) {
                Socket client = serverSocket.accept();

                clientAdd = client.getInetAddress();

                byte[] input_buffer = new byte[4 * 1024];

                inputStream = client.getInputStream();
                inputStream.read(input_buffer);

                String input_text;

                boolean first = true;

                input_text = "";
                for (byte in_buffer :
                        input_buffer) {
                    if (in_buffer == (byte) ',') {
                        if (first) {
                            fileName = input_text;
                            first = false;
                            input_text = "";
                        } else {
                            contentURI = input_text;
                            input_text = "";
                        }
                    } else {
                        input_text += (char) in_buffer;
                    }
                }
                fileNameWithListener.setFile_name(clientAdd,fileName,contentURI);
            }
            //return input_text;
        } catch (IOException e) {
            Log.e("MainServerAsyncTask", e.getMessage());
            return null;
        }finally {
            if(inputStream!=null){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    protected void onPostExecute(String result) {
        fileNameWithListener.setFile_name(clientAdd,fileName,contentURI);
    }

    public interface FileNameWithListener{
        public void setFile_name(InetAddress address,String file_name, String contentURI);
    }

}
