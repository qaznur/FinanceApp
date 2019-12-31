package ru.javabegin.tutorial.androidfinance.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
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
import ru.javabegin.tutorial.androidfinance.core.impls.DefaultSource;
import ru.javabegin.tutorial.androidfinance.core.interfaces.Source;
import ru.javabegin.tutorial.androidfinance.core.interfaces.TreeNode;
import ru.javabegin.tutorial.androidfinance.core.objects.OperationType;
import ru.javabegin.tutorial.androidfinance.fragments.SprListFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, SprListFragment.OnListFragmentInteractionListener {

    private TreeNode selectedParentNode;
    private List<? extends TreeNode> list;

    private TextView toolbarTitle;
    private Toolbar toolbar;
    private ImageView backIcon;
    private ImageView addIcon;
    private TabLayout tabLayout;

    private SprListFragment sprListFragment;

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
                backIcon.setVisibility(View.INVISIBLE);
                toolbarTitle.setText(R.string.sources);

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
                sprListFragment.updateList(list);
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
                if (selectedParentNode.getParent() == null) {
                    sprListFragment.updateList(list);
                    toolbarTitle.setText(R.string.sources);
                    backIcon.setVisibility(View.INVISIBLE);
                } else {
                    sprListFragment.updateList(selectedParentNode.getParent().getChildren());
                    selectedParentNode = selectedParentNode.getParent();
                    toolbarTitle.setText(selectedParentNode.getName());
                }
            }
        });

        addIcon = findViewById(R.id.img_add_node);
        addIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DefaultSource source = new DefaultSource();
                if (selectedParentNode != null) {
                    source.setOperationType(((Source) selectedParentNode).getOperationType());
                }
                Intent intent = new Intent(MainActivity.this, EditSourceActivity.class);
                intent.putExtra(EditSourceActivity.NODE_OBJECT, source);
                startActivityForResult(intent, EditSourceActivity.REQUEST_NODE_ADD);
            }
        });
    }

    private void initFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        sprListFragment = new SprListFragment();
        fragmentTransaction.replace(R.id.fragment_container, sprListFragment);
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
        this.selectedParentNode = node;
        if (selectedParentNode.hasChilds()) {
            toolbarTitle.setText(selectedParentNode.getName());// показывает выбранную категорию
            backIcon.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == EditSourceActivity.REQUEST_NODE_EDIT) {
                if (data != null) {
                    TreeNode node = (TreeNode) data.getSerializableExtra(EditSourceActivity.NODE_OBJECT);
                    sprListFragment.updateNode(node);
                }
            }
        }
    }
}
