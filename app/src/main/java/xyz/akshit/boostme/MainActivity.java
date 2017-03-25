package xyz.akshit.boostme;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button initiateButton, joinButton;
    private View rootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rootView = (View) findViewById(R.id.activity_main);

        initiateButton = (Button) findViewById(R.id.initiate_button);
        joinButton = (Button) findViewById(R.id.join_button);

        initiateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(MainActivity.this, LocalSongsPlaylistActivity.class);
                startActivity(myIntent);
            }
        });

        joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO
                //calls QR scanner
                Snackbar.make(rootView, "Goto scan QR here", Snackbar.LENGTH_SHORT).show();
            }
        });

    }
}
