package com.example.janaf.plae;

import android.content.Intent;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.R.layout.simple_expandable_list_item_1;

public class Reserves extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{

    //Firebase
    private FirebaseAuth mAuth;
    private final String TAG = "Firebase";
    DatabaseReference dataUsers = FirebaseDatabase.getInstance().getReference("pessoas");
    DatabaseReference dataReserve = FirebaseDatabase.getInstance().getReference("reservas");
    DatabaseReference dataGames = FirebaseDatabase.getInstance().getReference("jogos");

    //Layout
    Toolbar toolbar;
    BottomNavigationView bottomNavigationView;
    String game_names[];
    private String game_name;
    String d;
    TextView balance, description, name_g, date;
    private String friendlyName;
    ImageView img1;

    private RecyclerView rv;
    String[] hours;

    //ListView
    private List<String> listGames;
    private ArrayList<Game> listGames1;
    private ArrayList<Reservation> listReservations;
    private ListView lg;
    private ListView ld;
    ArrayAdapter<String> arrayAdapter;
    private List<String> dates;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserves);

        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        listReservations = new ArrayList<>();
        listGames1 = new ArrayList<Game>();


        lg = (ListView) findViewById(R.id.name_game_list);
        ld = (ListView) findViewById(R.id.name_game_list);

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(0);
        menuItem.setChecked(true);

        //recycle view
        rv = (RecyclerView) findViewById(R.id.reserving);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(this));

        Resources res = getResources();
        hours = res.getStringArray(R.array.hours_array);

        balance = (TextView) findViewById(R.id.balenceR);
        description = (TextView) findViewById(R.id.description);
        name_g = (TextView) findViewById(R.id.game_name1);
        img1 = (ImageView) findViewById(R.id.game_img);
        date = (TextView) findViewById(R.id.date11);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            game_name = extras.getString("game");
            d = extras.getString("date");
        }
        else{
            Calendar c = Calendar.getInstance();;
            SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
            d = df.format(c.getTime());
            description.setText("Descrição");
            name_g.setText("Nome do Jogo");
        }

        final ArrayList<String> stringHours = new ArrayList<String>(Arrays.asList(hours));

        Log.d(TAG, "d" + d + "/ng" + game_name);

        name_g.setText(game_name);
        date.setText(d);

        mAuth = FirebaseAuth.getInstance();

        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            friendlyName = user.getEmail();
        }

        //balance + firebase
        dataUsers.orderByChild("email").equalTo(friendlyName).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    User u = postSnapshot.getValue(User.class);
                    String i = u.getSaldo() + "€";
                    balance.setText(i);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        //search by game_name to set description
        dataGames.orderByChild("nome_jogo").equalTo(game_name).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Game g = postSnapshot.getValue(Game.class);
                    Log.d(TAG, g.getNome_jogo());
                    description.setText(g.getDescricao());
                    String img = g.getIcon();
                    int i = getId(img, R.drawable.class);
                    img1.setImageResource(i);
                    listGames1.add(g);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        //firebase reserves
        dataReserve.orderByChild("nome_jogo").equalTo(game_name).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Reservation reservation = postSnapshot.getValue(Reservation.class);
                    Log.d(TAG, reservation.getEmail());
                    if(reservation.getData().equals(d)) {
                        listReservations.add(reservation);
                    }
                }
                MultiViewTypeAdapter adapter = new MultiViewTypeAdapter(2,getApplicationContext(),listGames1,listReservations,friendlyName,d,stringHours,game_name);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), OrientationHelper.VERTICAL, false);
                rv.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_user,menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.logout:
                logout();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void logout() {
        // Fazer o logout
        mAuth.signOut();
        // Chamar primeira atividade (login)
        Intent i = new Intent(getApplicationContext(), Login.class);
        startActivity(i);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.reserve:

                break;
            case R.id.profile:
                Intent j = new Intent(getApplicationContext(), Profile_User.class);
                startActivity(j);
                break;
            case R.id.home:
                Intent k = new Intent(getApplicationContext(), Main_User.class);
                startActivity(k);
                break;
        }
        return true;
    }

    public static int getId(String resourceName, Class<?> c) {
        try {
            Field idField = c.getDeclaredField(resourceName);
            return idField.getInt(idField);
        } catch (Exception e) {
            throw new RuntimeException("No resource ID found for: "
                    + resourceName + " / " + c, e);
        }
    }

    public void showList(View v){
        lg = (ListView) findViewById(R.id.name_game_list);

        listGames = new ArrayList<>();
        lg.setVisibility(View.VISIBLE);

        dataGames.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Game game = postSnapshot.getValue(Game.class);
                    String name = game.getNome_jogo();
                    listGames.add(name);
                    Log.d(TAG, "nome jogo: " + listGames);
                }
                arrayAdapter = new ArrayAdapter<String>(getApplicationContext(),R.layout.list, R.id.list_content, listGames);
                lg.setAdapter(arrayAdapter);

                lg.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                    public void onItemClick(AdapterView<?> parent, View view, int j, long id){
                        Intent intent = new Intent(getApplicationContext(),Reserves.class);
                        intent.putExtra("game", listGames.get(j));
                        intent.putExtra("date", d);
                        startActivity(intent);
                    }
                });
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void showListDate(View v) {
        ld.setVisibility(View.VISIBLE);

        dates = new ArrayList<>();

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        Log.d(TAG,"data2" + df.format(cal.getTime()));
        dates.add(d);

        for (int i = 1; i < 8; i++) {
            cal = Calendar.getInstance();
            cal.add(Calendar.DATE, i);
            Date todate1 = cal.getTime();
            dates.add(df.format(todate1));

        }
        arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.list, R.id.list_content, dates);
        lg.setAdapter(arrayAdapter);

        lg.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int j, long id) {
                Intent intent = new Intent(getApplicationContext(), Reserves.class);
                intent.putExtra("game", game_name);
                intent.putExtra("date", dates.get(j));
                startActivity(intent);
            }
        });
    }
}
