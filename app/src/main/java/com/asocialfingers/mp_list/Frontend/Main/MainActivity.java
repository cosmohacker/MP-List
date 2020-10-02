package com.asocialfingers.mp_list.Frontend.Main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.asocialfingers.mp_list.Backend.Database.DBHelper;
import com.asocialfingers.mp_list.Backend.Database.UserDao;
import com.asocialfingers.mp_list.Backend.Database.UserDetails;
import com.asocialfingers.mp_list.Frontend.Login.LoginActivity;
import com.asocialfingers.mp_list.R;
import com.asocialfingers.mp_list.Frontend.Tabs.About.AboutFragment;
import com.asocialfingers.mp_list.Frontend.Tabs.Add.AddFragment;
import com.asocialfingers.mp_list.Frontend.Tabs.Home.HomeFragment;
import com.asocialfingers.mp_list.Frontend.Tabs.List.ListFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private String _nameSurnameHolder, _commentHolder, created_at, _currentTab;
    private static String TAG = MainActivity.class.getSimpleName();
    private TextView _nameSurnameCounter, _commentCounter;
    private AppBarConfiguration mAppBarConfiguration;
    private EditText _nameSurname, _comment;
    private NavigationView navigationView;
    private Button _login, _send, _logout;
    private ProgressDialog _pDialog;
    private UserDao userDao;
    private Dialog _customDialog;
    private DrawerLayout drawer;
    private ImageButton _close;
    private DBHelper dbHelper;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userDao = new UserDao(this);
        dbHelper = new DBHelper(this);

        methodCleaner();
    }

    private void methodCleaner() {
        utils();
        setSupportActionBar(toolbar);
        navigationControls();
        loadFragment(new HomeFragment());
        logout();
    }

    //region Utilities
    private void utils() {
        navigationView = findViewById(R.id.nav_view);
        drawer = findViewById(R.id.drawer_layout);
        _logout = findViewById(R.id.btnLogout);
        toolbar = findViewById(R.id.toolbar);
    }

    private void getTimeStamp() {
        Date calendar = Calendar.getInstance().getTime();
        created_at = calendar.toString();
    }

    //endregion

    private void logout() {

        _logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!dbHelper.getUsernameData().isEmpty()) {
                    int user_id = Integer.parseInt(dbHelper.getUserId());

                    userDao.deleteUser(user_id);

                }

                Intent i = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
            }
        });

    }

    //region Fragment Controls
    private void navigationControls() {
        mAppBarConfiguration = new AppBarConfiguration.Builder
                (R.id.homeTab, R.id.addTab, R.id.listTab, R.id.aboutTab)
                .setDrawerLayout(drawer)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        //navigationView.setItemTextColor(ColorStateList.valueOf(getColor(R.color.text_colors_dark)));
        //navigationView.setItemIconTintList(ColorStateList.valueOf(getColor(R.color.object_color_dark)));

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment;
                switch (item.getItemId()) {
                    case R.id.homeAction:
                        fragment = new HomeFragment();
                        toolbar.setTitle("Anasayfa");
                        loadFragment(fragment);
                        _currentTab = "Anasayfa";
                        return true;
                    case R.id.addAction:
                        fragment = new AddFragment();
                        toolbar.setTitle("Ekle");
                        loadFragment(fragment);
                        _currentTab = "Ekle";
                        return true;
                    case R.id.listAction:
                        fragment = new ListFragment();
                        toolbar.setTitle("Ara");
                        loadFragment(fragment);
                        _currentTab = "Ara";
                        return true;
                    case R.id.aboutAction:
                        fragment = new AboutFragment();
                        toolbar.setTitle("Hakkında");
                        loadFragment(fragment);
                        _currentTab = "Hakkında";
                        return true;
                }
                return false;
            }
        });

    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.nav_host_fragment, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
        drawer.closeDrawers();
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
    //endregion

}
