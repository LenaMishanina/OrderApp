//package com.example.graduatework;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.appcompat.widget.Toolbar;
//
//import android.os.Bundle;
//import android.view.MenuItem;
//import android.view.View;
//
//import com.google.android.material.floatingactionbutton.FloatingActionButton;
//import com.google.android.material.navigation.NavigationView;
//import com.google.android.material.snackbar.Snackbar;
//
//public class Home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_home);
//        Toolbar toolbar=(Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(view) {
//            Snackbar.make()
//
//        };
//    }
//
//    @Override
//    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//        return false;
//    }
//
//    @Override
//    public void onPointerCaptureChanged(boolean hasCapture) {
//        super.onPointerCaptureChanged(hasCapture);
//    }
//}
//

package com.example.graduatework;

        import android.annotation.SuppressLint;
        import android.content.Intent;
        import android.os.Bundle;
        import com.example.graduatework.Common.Common;
        import com.example.graduatework.Common.ItemClickListener;
        import com.example.graduatework.adapter.MyAdapterMenu;
        import com.example.graduatework.database.Category;
        import com.example.graduatework.viewHolder.MenuViewHolder;
        import com.firebase.ui.database.FirebaseRecyclerOptions;
        import com.google.android.material.floatingactionbutton.FloatingActionButton;
        import com.google.android.material.navigation.NavigationView;
        import com.google.android.material.snackbar.Snackbar;
        import com.google.firebase.database.DataSnapshot;
        import com.google.firebase.database.DatabaseError;
        import com.google.firebase.database.DatabaseReference;
        import com.google.firebase.database.FirebaseDatabase;

        import com.firebase.ui.database.FirebaseRecyclerAdapter;
        import com.google.firebase.database.Query;
        import com.google.firebase.database.ValueEventListener;
        import com.squareup.picasso.Picasso;

        import androidx.annotation.NonNull;
        import androidx.appcompat.app.ActionBarDrawerToggle;

        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.Menu;
        import android.view.MenuItem;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.TextView;
        import android.widget.Toast;

        import androidx.appcompat.widget.Toolbar;
        import androidx.appcompat.app.AppCompatActivity;

        import androidx.core.view.GravityCompat;
        import androidx.drawerlayout.widget.DrawerLayout;
        import androidx.navigation.ui.AppBarConfiguration;
        import androidx.recyclerview.widget.LinearLayoutManager;
        import androidx.recyclerview.widget.RecyclerView;
        import androidx.recyclerview.widget.RecyclerView.LayoutManager;

        import java.util.ArrayList;


public class Home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private AppBarConfiguration appBarConfiguration;

    DatabaseReference database;

    TextView tvFullName;

    RecyclerView recyclerMenu;
    RecyclerView.LayoutManager layoutManager;

    MyAdapterMenu myAdapterMenu;
    ArrayList<Category> categoryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar=(Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Menu");
        setSupportActionBar(toolbar);

        database = FirebaseDatabase.getInstance().getReference("Category");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cartIntent = new Intent(Home.this, Cart.class);
                startActivity(cartIntent);

            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawerLayout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.openNav, R.string.closeNav);

        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navView = (NavigationView) findViewById(R.id.navView);
        navView.setNavigationItemSelectedListener(this);

        //set name
        View headerView = navView.getHeaderView(0);
        tvFullName =(TextView) headerView.findViewById(R.id.userName);
        tvFullName.setText(Common.currentUser.getName());

        //menu
        recyclerMenu = (RecyclerView) findViewById(R.id.recycleMenu);
        recyclerMenu.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recyclerMenu.setLayoutManager(layoutManager);



        final View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int itemPosition = recyclerMenu.getChildLayoutPosition(v);
                Category item = categoryList.get(itemPosition);
//                Toast.makeText(Home.this, item.getName(), Toast.LENGTH_SHORT).show();

                Log.v("CATEGORY NAME CLICK", item.getName());

                database.orderByChild("Name").equalTo(item.getName()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Log.v("I_M_HERE", "I M HERE");
                   //     Log.v("GET_KEY", snapshot.child(item.getName()).getRef().getKey());

                        String key = "no(";

                        for(DataSnapshot childSnapshot : snapshot.getChildren()){
                            Log.v("GET_KEY", childSnapshot.getKey());
                            key = childSnapshot.getKey();
                            Toast.makeText(Home.this, childSnapshot.getKey(), Toast.LENGTH_SHORT).show();
                        }

                        Log.v("KEYYYY CATEGORY", key);
                        Intent toFoodList=new Intent(Home.this, FoodList.class);
                        toFoodList.putExtra("CategoryId", key);
                        startActivity(toFoodList);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


            }
        };

        categoryList = new ArrayList<>();
        myAdapterMenu=new MyAdapterMenu(this,categoryList, onClickListener);
        recyclerMenu.setAdapter(myAdapterMenu);

        database.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Category category = dataSnapshot.getValue(Category.class);
                    categoryList.add(category);
                }
                myAdapterMenu.notifyDataSetChanged();
                //        Toast.makeText(Home.this, categoryList.get(0).getName(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void loadMenu(FirebaseRecyclerOptions<Category> options) {
  //      Toast.makeText(Home.this, "load menu 2", Toast.LENGTH_SHORT).show();

//        FirebaseRecyclerAdapter<Category, MenuViewHolder> adapter=new FirebaseRecyclerAdapter<Category, MenuViewHolder>(Category.class, MenuViewHolder.class, R.layout.menu_item,  category) {
        FirebaseRecyclerAdapter<Category, MenuViewHolder> adapter=new FirebaseRecyclerAdapter<Category, MenuViewHolder>(options) {

            @Override
            public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.menu_item, parent, false);

                return new MenuViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull MenuViewHolder holder, int position, @NonNull Category model) {
                holder.txtMenuName.setText(model.getName());
                Picasso.with(getBaseContext()).load(model.getImage()).into(holder.imageView);
                final Category clickItem = model;
                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Toast.makeText(Home.this, ""+clickItem.getName(), Toast.LENGTH_SHORT).show();
                    }
                });
            }

        };
        recyclerMenu.setAdapter(adapter);

    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_menu){

        }else if(id == R.id.nav_cart){
            Intent intent = new Intent(Home.this, Cart.class);
            startActivity(intent);
        }else if(id == R.id.nav_orders){
            Intent intent = new Intent(Home.this, OrderStatus.class);
            startActivity(intent);

        }else if(id == R.id.nav_logout){
            Intent intent = new Intent(Home.this, SignIn.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

        }

        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed(){
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawerLayout);
        if (drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
               return super.onOptionsItemSelected(item);
    }

}