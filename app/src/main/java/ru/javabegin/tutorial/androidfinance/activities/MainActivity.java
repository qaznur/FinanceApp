package ru.javabegin.tutorial.androidfinance.activities;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import java.util.List;

import ru.javabegin.tutorial.androidfinance.R;
import ru.javabegin.tutorial.androidfinance.core.database.Initializer;
import ru.javabegin.tutorial.androidfinance.core.interfaces.Source;
import ru.javabegin.tutorial.androidfinance.core.interfaces.TreeNode;
import ru.javabegin.tutorial.androidfinance.core.objects.OperationType;
import ru.javabegin.tutorial.androidfinance.fragments.SprFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, SprFragment.OnListFragmentInteractionListener {

    private TreeNode selectedNode;
    private List<? extends TreeNode> list;

    private TextView toolbarTitle;
    private Toolbar toolbar;
    private ImageView backIcon;
    private TabLayout tabLayout;

    private SprFragment sprFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initToolbar();
        initNavigationDrawer(toolbar);
        initFloatingButton();
        initFragment();
        initTabs();

        list = Initializer.getSourceSync().getAll();
    }

    private void initTabs() {
        tabLayout = findViewById(R.id.tabs);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        list = Initializer.getSourceSync().getAll();
                        break;
                    case 1:
                        list = Initializer.getSourceSync().getList(OperationType.INCOME);
                        break;
                    case 2:
                        list = Initializer.getSourceSync().getList(OperationType.OUTCOME);
                        break;
                }
                sprFragment.updateData(list);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void initNavigationDrawer(Toolbar toolbar) {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void initFloatingButton() {
//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }

    private void initToolbar() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbarTitle = findViewById(R.id.toolbar_title);
        backIcon = findViewById(R.id.back_icon);
        backIcon.setVisibility(View.INVISIBLE);
        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedNode.getParent() == null) {
                    OperationType type = ((Source) selectedNode).getOperationType();
                    sprFragment.updateData(Initializer.getSourceSync().getList(type));
                    toolbarTitle.setText(R.string.sources);
                    backIcon.setVisibility(View.INVISIBLE);
                } else {
                    sprFragment.updateData(selectedNode.getParent().getChildren());
                    selectedNode = selectedNode.getParent();
                    toolbarTitle.setText(selectedNode.getName());
                    backIcon.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void initFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        sprFragment = new SprFragment();
        fragmentTransaction.replace(R.id.fragment_container, sprFragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_operations) {
            // Handle the camera action
        } else if (id == R.id.nav_storage) {

        } else if (id == R.id.nav_source) {

        } else if (id == R.id.nav_reports) {

        } else if (id == R.id.nav_settings) {

        } else if (id == R.id.nav_reference) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onItemClicked(TreeNode node) {
        this.selectedNode = node;
        if(selectedNode != null && selectedNode.hasChilds()) {
            toolbarTitle.setText(selectedNode.getName());
            backIcon.setVisibility(View.VISIBLE);
        } else {
            this.selectedNode = node.getParent();
        }
    }
}
