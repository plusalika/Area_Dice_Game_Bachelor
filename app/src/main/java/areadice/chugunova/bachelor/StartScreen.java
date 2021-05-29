 package areadice.chugunova.bachelor;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

public class StartScreen extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_screen);
    }

    public void onStart(View v){
        Intent intent = new Intent(this, Game.class);
        startActivity(intent);
        finish();
    }
}
