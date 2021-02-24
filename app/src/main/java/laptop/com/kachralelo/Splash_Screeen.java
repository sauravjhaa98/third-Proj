package laptop.com.kachralelo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;

public class Splash_Screeen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash__screeen); getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        Thread y=new Thread(){
            public void run(){
                try{
                    sleep(4000);
                }catch (Exception t){

                }
                finally {
                    {
                        Intent i=new Intent(Splash_Screeen.this,Login.class);
                        startActivity(i);
                        finish();
                    }
                }
            }
        };
        y.start();
    }
}