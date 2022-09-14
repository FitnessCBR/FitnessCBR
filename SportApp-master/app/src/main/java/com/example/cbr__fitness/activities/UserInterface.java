package com.example.cbr__fitness.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import com.example.cbr__fitness.R;
import com.example.cbr__fitness.logic.SharedPreferenceManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

/**
 * @author Jobst-Julius Bartels
 */

// Diese Klasse stellt die Aktivität des User-Interfaces dar.
public class UserInterface extends AppCompatActivity implements View.OnClickListener {

    // Attribute der Klasse.
    private TextView textUser;
    private ImageButton imgButtonCBR;
    private ImageButton imgButtonPlan;
    private ImageButton imgButtonSetts;
    private ImageButton imgButtonLogo;

    private NavController navController;

    @Override
    // OnClick Methode der Klasse.
    public void onClick(View v) {
        switch (v.getId()) {
        }
    }
    @Override
    // OnCreate Methode der Klasse.
    protected void onCreate(Bundle savedInstanceState) {

        // Layout.
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_interface);

        NavHostFragment fragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.navFragmentContainer);
        if (fragment != null) {
            System.out.println("NavController found");
            navController = fragment.getNavController();
        }

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);

        if (SharedPreferenceManager.getIsUserAdmin(this)) {
            System.out.println("SETTTING MENUS");
            Menu menu = bottomNavigationView.getMenu();
            menu.add(Menu.NONE, R.id.admin_nav, Menu.NONE, "Admin");
        }


        System.out.println("NAV CONTROLLER: " + navController.toString());

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        System.out.println(navController.getGraph().toString());
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        // Überschrift für die Aktivität wird bestimmt.
        //String msgTxt = "Hello " + MainActivity.userLogged.getUsername() + "! Start your workout here!";
        //textUser.setText(msgTxt);
    }
    //NESSESARY TO ENABLE A BACK BUTTON
    @Override
    public boolean onSupportNavigateUp() {
        boolean test = navController.navigateUp();
        return test;
    }
}