package com.asocialfingers.mp_list.Frontend.Beginning;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.asocialfingers.mp_list.Backend.Database.DBHelper;
import com.asocialfingers.mp_list.Backend.Database.UserDao;
import com.asocialfingers.mp_list.Backend.Database.UserDetails;
import com.asocialfingers.mp_list.Frontend.Login.LoginActivity;
import com.asocialfingers.mp_list.Frontend.Main.MainActivity;
import com.asocialfingers.mp_list.R;

public class PrologueActivity extends AppCompatActivity {

    private DBHelper dbHelper;
    private ImageView _logo;
    private UserDao userDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prologue);

        userDao = new UserDao(this);
        dbHelper = new DBHelper(this);

        methodCleaner();
    }

    private void methodCleaner() {

        utils();
        timerTask();

    }

    private void utils() {

        _logo = (ImageView) findViewById(R.id.imgLogo);

    }

    private void timerTask() {
        new CountDownTimer(3000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                Thread timer = new Thread() {
                    public void run() {
                        try {
                            sleep(3000);
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                        }
                    }
                };
                timer.start();
            }

            @Override
            public void onFinish() {
                Animation animFadeIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
                _logo.startAnimation(animFadeIn);

                new CountDownTimer(3000, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        Thread timer = new Thread() {
                            public void run() {
                                try {
                                    sleep(3000);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                } finally {
                                }
                            }
                        };
                        timer.start();
                    }

                    @Override
                    public void onFinish() {

                        if (dbHelper.getUsernameData().isEmpty()) {
                            Intent i = new Intent(PrologueActivity.this, LoginActivity.class);
                            startActivity(i);
                        } else {
                            Intent i = new Intent(PrologueActivity.this, MainActivity.class);
                            startActivity(i);
                        }

                    }
                }.start();

            }
        }.start();
    }

}
