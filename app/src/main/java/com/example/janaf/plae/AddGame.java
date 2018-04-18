package com.example.janaf.plae;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddGame extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private final String TAG = "Firebase";
    DatabaseReference dataGames = FirebaseDatabase.getInstance().getReference("jogos");

    String icon;
    String price;
    Button btn_v, btn_c, btn_u, btn_uc, btn_d;
    ImageView dados, dards, ping_p, bilhar, cartas1, play1, puzzle1, m;
    EditText nome_jogo1, descricao1;

    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_game);

        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        mAuth = FirebaseAuth.getInstance();

        descricao1 = (EditText) findViewById(R.id.description1);
        nome_jogo1 = (EditText) findViewById(R.id.game_name2);

        btn_d = (Button) findViewById(R.id.dois);
        btn_uc = (Button) findViewById(R.id.um_cinquenta);
        btn_u = (Button) findViewById(R.id.um);
        btn_c = (Button) findViewById(R.id.cinquenta);
        btn_v = (Button) findViewById(R.id.vinte);

        dados = (ImageView) findViewById(R.id.dados);
        dards = (ImageView) findViewById(R.id.dardos);
        ping_p = (ImageView) findViewById(R.id.ping);
        bilhar = (ImageView) findViewById(R.id.snooker);
        cartas1 = (ImageView) findViewById(R.id.cartas);
        play1 = (ImageView) findViewById(R.id.play);
        puzzle1 = (ImageView) findViewById(R.id.puzzle);
        m = (ImageView) findViewById(R.id.matrecos);

    }

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.admin_menu,menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.logout:
                logout();
                break;
            case R.id.consult:
                Intent k = new Intent(getApplicationContext(), Consult.class);
                startActivity(k);
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

    //add game
    public void click(View v){
        Intent j = new Intent(getApplicationContext(), Main_Admin.class);
        startActivity(j);
    }

    public void getElements(View v){
        final int newColor = getResources().getColor(R.color.colorSelected);
        final int pastColor = getResources().getColor(R.color.colorLetters);
        switch (v.getId()) {
            case R.id.cartas:
                icon = "cards";
                cartas1.setColorFilter(newColor, PorterDuff.Mode.SRC_ATOP);
                dards.setColorFilter(pastColor, PorterDuff.Mode.SRC_ATOP);
                play1.setColorFilter(pastColor, PorterDuff.Mode.SRC_ATOP);
                m.setColorFilter(pastColor, PorterDuff.Mode.SRC_ATOP);
                dados.setColorFilter(pastColor, PorterDuff.Mode.SRC_ATOP);
                ping_p.setColorFilter(pastColor, PorterDuff.Mode.SRC_ATOP);
                bilhar.setColorFilter(pastColor, PorterDuff.Mode.SRC_ATOP);
                puzzle1.setColorFilter(pastColor, PorterDuff.Mode.SRC_ATOP);
                break;
            case R.id.dados:
                icon = "dice";
                dados.setColorFilter(newColor, PorterDuff.Mode.SRC_ATOP);
                dards.setColorFilter(pastColor, PorterDuff.Mode.SRC_ATOP);
                play1.setColorFilter(pastColor, PorterDuff.Mode.SRC_ATOP);
                m.setColorFilter(pastColor, PorterDuff.Mode.SRC_ATOP);
                cartas1.setColorFilter(pastColor, PorterDuff.Mode.SRC_ATOP);
                ping_p.setColorFilter(pastColor, PorterDuff.Mode.SRC_ATOP);
                bilhar.setColorFilter(pastColor, PorterDuff.Mode.SRC_ATOP);
                puzzle1.setColorFilter(pastColor, PorterDuff.Mode.SRC_ATOP);
                break;
            case R.id.dardos:
                icon = "dardos";
                dards.setColorFilter(newColor, PorterDuff.Mode.SRC_ATOP);
                cartas1.setColorFilter(pastColor, PorterDuff.Mode.SRC_ATOP);
                play1.setColorFilter(pastColor, PorterDuff.Mode.SRC_ATOP);
                m.setColorFilter(pastColor, PorterDuff.Mode.SRC_ATOP);
                dados.setColorFilter(pastColor, PorterDuff.Mode.SRC_ATOP);
                ping_p.setColorFilter(pastColor, PorterDuff.Mode.SRC_ATOP);
                bilhar.setColorFilter(pastColor, PorterDuff.Mode.SRC_ATOP);
                puzzle1.setColorFilter(pastColor, PorterDuff.Mode.SRC_ATOP);
                break;
            case R.id.snooker:
                icon = "snooker";
                bilhar.setColorFilter(newColor, PorterDuff.Mode.SRC_ATOP);
                dards.setColorFilter(pastColor, PorterDuff.Mode.SRC_ATOP);
                play1.setColorFilter(pastColor, PorterDuff.Mode.SRC_ATOP);
                m.setColorFilter(pastColor, PorterDuff.Mode.SRC_ATOP);
                dados.setColorFilter(pastColor, PorterDuff.Mode.SRC_ATOP);
                ping_p.setColorFilter(pastColor, PorterDuff.Mode.SRC_ATOP);
                cartas1.setColorFilter(pastColor, PorterDuff.Mode.SRC_ATOP);
                puzzle1.setColorFilter(pastColor, PorterDuff.Mode.SRC_ATOP);
                break;
            case R.id.ping:
                icon = "ping_pong";
                ping_p.setColorFilter(newColor, PorterDuff.Mode.SRC_ATOP);
                dards.setColorFilter(pastColor, PorterDuff.Mode.SRC_ATOP);
                play1.setColorFilter(pastColor, PorterDuff.Mode.SRC_ATOP);
                m.setColorFilter(pastColor, PorterDuff.Mode.SRC_ATOP);
                dados.setColorFilter(pastColor, PorterDuff.Mode.SRC_ATOP);
                cartas1.setColorFilter(pastColor, PorterDuff.Mode.SRC_ATOP);
                bilhar.setColorFilter(pastColor, PorterDuff.Mode.SRC_ATOP);
                puzzle1.setColorFilter(pastColor, PorterDuff.Mode.SRC_ATOP);
                break;
            case R.id.puzzle:
                icon = "puzzle";
                puzzle1.setColorFilter(newColor, PorterDuff.Mode.SRC_ATOP);
                dards.setColorFilter(pastColor, PorterDuff.Mode.SRC_ATOP);
                play1.setColorFilter(pastColor, PorterDuff.Mode.SRC_ATOP);
                m.setColorFilter(pastColor, PorterDuff.Mode.SRC_ATOP);
                dados.setColorFilter(pastColor, PorterDuff.Mode.SRC_ATOP);
                ping_p.setColorFilter(pastColor, PorterDuff.Mode.SRC_ATOP);
                bilhar.setColorFilter(pastColor, PorterDuff.Mode.SRC_ATOP);
                cartas1.setColorFilter(pastColor, PorterDuff.Mode.SRC_ATOP);
                break;
            case R.id.matrecos:
                icon = "foosball";
                m.setColorFilter(newColor, PorterDuff.Mode.SRC_ATOP);
                dards.setColorFilter(pastColor, PorterDuff.Mode.SRC_ATOP);
                play1.setColorFilter(pastColor, PorterDuff.Mode.SRC_ATOP);
                cartas1.setColorFilter(pastColor, PorterDuff.Mode.SRC_ATOP);
                dados.setColorFilter(pastColor, PorterDuff.Mode.SRC_ATOP);
                ping_p.setColorFilter(pastColor, PorterDuff.Mode.SRC_ATOP);
                bilhar.setColorFilter(pastColor, PorterDuff.Mode.SRC_ATOP);
                puzzle1.setColorFilter(pastColor, PorterDuff.Mode.SRC_ATOP);
                break;
            case R.id.play:
                icon = "playstation";
                play1.setColorFilter(newColor, PorterDuff.Mode.SRC_ATOP);
                dards.setColorFilter(pastColor, PorterDuff.Mode.SRC_ATOP);
                cartas1.setColorFilter(pastColor, PorterDuff.Mode.SRC_ATOP);
                m.setColorFilter(pastColor, PorterDuff.Mode.SRC_ATOP);
                dados.setColorFilter(pastColor, PorterDuff.Mode.SRC_ATOP);
                ping_p.setColorFilter(pastColor, PorterDuff.Mode.SRC_ATOP);
                bilhar.setColorFilter(pastColor, PorterDuff.Mode.SRC_ATOP);
                puzzle1.setColorFilter(pastColor, PorterDuff.Mode.SRC_ATOP);
                break;
            case R.id.vinte:
                price = "0.20";
                btn_v.setBackgroundColor(newColor);
                btn_c.setBackgroundColor(pastColor);
                btn_u.setBackgroundColor(pastColor);
                btn_uc.setBackgroundColor(pastColor);
                btn_d.setBackgroundColor(pastColor);
                break;
            case R.id.cinquenta:
                price = "0.50";
                btn_c.setBackgroundResource(R.drawable.buttonselected);
                btn_v.setBackgroundResource(R.drawable.button);
                btn_u.setBackgroundResource(R.drawable.button);
                btn_uc.setBackgroundResource(R.drawable.button);
                btn_d.setBackgroundResource(R.drawable.button);
                break;
            case R.id.um:
                price = "1.00";
                btn_u.setBackgroundResource(R.drawable.buttonselected);
                btn_c.setBackgroundResource(R.drawable.button);
                btn_v.setBackgroundResource(R.drawable.button);
                btn_uc.setBackgroundResource(R.drawable.button);
                btn_d.setBackgroundResource(R.drawable.button);
                break;
            case R.id.um_cinquenta:
                price = "1.50";
                btn_uc.setBackgroundResource(R.drawable.buttonselected);
                btn_c.setBackgroundResource(R.drawable.button);
                btn_u.setBackgroundResource(R.drawable.button);
                btn_v.setBackgroundResource(R.drawable.button);
                btn_d.setBackgroundResource(R.drawable.button);
                break;
            case R.id.dois:
                price = "2.00";
                btn_d.setBackgroundResource(R.drawable.buttonselected);
                btn_c.setBackgroundResource(R.drawable.button);
                btn_u.setBackgroundResource(R.drawable.button);
                btn_uc.setBackgroundResource(R.drawable.button);
                btn_v.setBackgroundResource(R.drawable.button);
                break;
        }
    }

    public void addGame(View v){
        String key = dataGames.push().getKey();


        String descricao = descricao1.getText().toString();
        String nome_jogo = nome_jogo1.getText().toString();

        Game g = new Game(descricao, icon, key, nome_jogo, price);
        dataGames.child(key).setValue(g);

        Intent i = new Intent(getApplicationContext(), Main_Admin.class);
        startActivity(i);
    }
}
