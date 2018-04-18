package com.example.janaf.plae;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class MultiViewTypeAdapter extends RecyclerView.Adapter {

    //Global
    private int type;
    Context mContext;
    private ArrayList<Game> myGameList;
    private ArrayList<Reservation> myReservationList;
    Date d1;
    Date d2;

    //Firebase table reservas -> Profile_User
    DatabaseReference dataReserve = FirebaseDatabase.getInstance().getReference("reservas");
    DatabaseReference dataGames = FirebaseDatabase.getInstance().getReference("jogos");

    String id;
    private FirebaseAuth mAuth;
    String game1;

    //Main_User
    private String hour;
    private ArrayList<String> hours;
    private String date;
    private Double price = 0.00;

    private final String TAG = "Firebase";

    //for recycler view from profile_user
    public static class ProfileAdapter extends RecyclerView.ViewHolder {

        CardView cardView;
        protected TextView txtName, txtPrice, txtDate, txtHour;
        protected ImageView img1;

        public ProfileAdapter(View itemView) {
            super(itemView);
            this.txtDate = itemView.findViewById(R.id.date);
            this.txtHour = itemView.findViewById(R.id.hour);
            this.txtPrice = itemView.findViewById(R.id.price);
            this.txtName = itemView.findViewById(R.id.game_name);
            this.img1 = itemView.findViewById(R.id.delete);
            this.cardView = (CardView) itemView.findViewById(R.id.my_reserves);
        }
    }

    //for recycer view from main_user
    public static class MainAdapter extends RecyclerView.ViewHolder {

        CardView cardView;

        protected TextView txtName;
        protected ImageView imgGame, imgOcuppied;
        protected Button btn;


        public MainAdapter(View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.game_name_user);
            imgGame = itemView.findViewById(R.id.image_game);
            imgOcuppied = itemView.findViewById(R.id.image_ocuppied);
            btn = itemView.findViewById(R.id.btn);
            this.cardView = (CardView) itemView.findViewById(R.id.main1);
        }
    }
    //for recycer view from reserves
    public static class ReserverAdapter extends RecyclerView.ViewHolder {

        CardView cardView;

        protected TextView txtName;
        protected ImageView imgOcuppied;
        protected Button btn;


        public ReserverAdapter(View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.game_name_hour);
            imgOcuppied = itemView.findViewById(R.id.image_ocuppied1);
            btn = itemView.findViewById(R.id.btn1);
            this.cardView = (CardView) itemView.findViewById(R.id.reserve1);
        }
    }

    public static class AdminAdapter extends RecyclerView.ViewHolder {

        CardView cardView;

        protected TextView txtName;
        protected ImageView btn;


        public AdminAdapter(View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.game_name_admin1);
            btn = itemView.findViewById(R.id.delete1);
            this.cardView = (CardView) itemView.findViewById(R.id.main2);
        }
    }

    public static class ConsultAdapter extends RecyclerView.ViewHolder {

        CardView cardView;
        protected TextView txtName, txtPrice, txtDate;

        public ConsultAdapter(View itemView) {
            super(itemView);
            this.txtDate = itemView.findViewById(R.id.date);
            this.txtPrice = itemView.findViewById(R.id.price);
            this.txtName = itemView.findViewById(R.id.game_name);
            this.cardView = (CardView) itemView.findViewById(R.id.profit);
        }
    }

    //constructor
    public MultiViewTypeAdapter(int type, Context context, ArrayList<Game> myGameList, ArrayList<Reservation> myReservationList, String hour, String date, ArrayList<String> hours, String game1) {
        this.type = type;
        this.mContext = context;
        this.myGameList = myGameList;
        this.myReservationList = myReservationList;
        this.hour = hour;
        this.date = date;
        this.hours = hours;
        this.game1 = game1;
    }

    //layout card view
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
        switch (viewType) {
            case 0: //Recycler view Profile
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_my_reserves, parent, false);
                return new ProfileAdapter(view);
            case 1: //Recycler view Main_user
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_game_user, parent, false);
                return new MainAdapter(view);
            case 2://Recycler view Reserves
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_reserve, parent, false);
                return new ReserverAdapter(view);
            case 3://Recycler view Main Admin
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_admin_main, parent, false);
                return new AdminAdapter(view);
            case 4://Recycler view Consult
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_consult, parent, false);
                return new ConsultAdapter(view);
        }
        return null;
    }

    @Override
    public int getItemViewType(int position) {

        switch (type) {
            case 0:
                return 0;
            case 1:
                return 1;
            case 2:
                return 2;
            case 3:
                return 3;
            case 4:
                return 4;
            default:
                return -1;
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int listPosition) {
            switch (type) {
                case 0:
                    Reservation reservation = myReservationList.get(listPosition);
                    String d = reservation.getData();
                    final String h = reservation.getHora();

                    dateTime(d, h);

                    SimpleDateFormat df = new SimpleDateFormat("dd-mm-yyyy");
                    SimpleDateFormat df1 = new SimpleDateFormat("HH:mm");

                    try {
                        if(d2.before(df.parse(date)) && d1.before(df1.parse(hour))){

                        }
                        else {
                            String nameG = reservation.getNome_jogo();
                            ((ProfileAdapter) holder).txtName.setText(nameG);
                            String price2 = getPrice(nameG) + "€";
                            ((ProfileAdapter) holder).txtPrice.setText(price2);
                            String h1 = h + "H";
                            ((ProfileAdapter) holder).txtDate.setText(d);
                            ((ProfileAdapter) holder).txtHour.setText(h1);
                            id = reservation.getId();
                            Log.d(TAG, "hour" + h);
                            ((ProfileAdapter) holder).img1.setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
                                    if(h.equals(hour)){

                                    }
                                    else{
                                        dataReserve.child(id).removeValue();
                                        Intent i = new Intent(mContext, Profile_User.class);
                                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        mContext.startActivity(i);
                                    }
                                }
                            });
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    break;
                case 1:
                        final Game game = myGameList.get(listPosition);
                        String name = game.getNome_jogo();
                        ((MainAdapter) holder).txtName.setText(name);
                        String img = game.getIcon();
                        int i = getId(img, R.drawable.class);
                        ((MainAdapter) holder).imgGame.setImageResource(i);
                        boolean ocuppied = isOcuppied(name);
                        if (ocuppied == true) {
                            ((MainAdapter) holder).imgOcuppied.setImageResource(R.drawable.ocuppied);
                        } else {
                            ((MainAdapter) holder).imgOcuppied.setImageResource(R.drawable.free);

                            ((MainAdapter) holder).btn.setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
                                    Intent intent = new Intent(v.getContext(), Reserves.class);
                                    intent.putExtra("game", myGameList.get(listPosition).getNome_jogo());
                                    intent.putExtra("date", date);
                                    v.getContext().startActivity(intent);
                                }
                            });
                        }
                    break;
                case 2:
                    final String time1 = hours.get(listPosition).toString();
                    boolean ocuppied1 = Hour(time1);
                    final String time = time1 +"h";
                    ((ReserverAdapter)holder).txtName.setText(time);
                    if (ocuppied1 == true) {
                        ((ReserverAdapter) holder).imgOcuppied.setImageResource(R.drawable.ocuppied);
                    } else {
                        ((ReserverAdapter) holder).imgOcuppied.setImageResource(R.drawable.free);
                        ((ReserverAdapter)holder).btn.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                String key = dataReserve.push().getKey();

                                    Reservation r1 = new Reservation(date, hour,
                                            time1, key, game1);
                                    dataReserve.child(key).setValue(r1);

                            }
                        });
                    }
                    break;
                case 3:
                    final Game g = myGameList.get(listPosition);
                    final String n = g.getNome_jogo();
                    ((AdminAdapter) holder).txtName.setText(n);
                    final String k = g.getId();
                    ((AdminAdapter)holder).btn.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            dataGames.child(k).removeValue();
                            eliminateReserve(n);
                            Intent x = new Intent(v.getContext(),Main_Admin.class);
                            v.getContext().startActivity(x);
                        }
                    });
                    break;
                case 4:
                    final Game g1 = myGameList.get(listPosition);
                    String n1 = g1.getNome_jogo();
                    ((ConsultAdapter) holder).txtName.setText(n1);
                    String p = g1.getPreco();
                    Double f= Double.parseDouble(p);
                    getProfit(f, n1);
                    ((ConsultAdapter) holder).txtDate.setText(date);
                    String total1 = String.valueOf(price);
                    String total = total1 + "€";
                    ((ConsultAdapter) holder).txtPrice.setText(total);
                    break;
            }
        }

    @Override
    public int getItemCount() {
        if(type==0) {
            return myReservationList.size();
        }
        if(type == 1){
            return myGameList.size();
        }
        if(type == 2) {
            return hours.size();
        }
        return myGameList.size();
    }

    public String getPrice(String name){
        for(int i =0; i<myGameList.size(); i++){
            Game g = myGameList.get(i);
            if(g.getNome_jogo().equals(name)){
                return g.getPreco();
            }
        }
        return null;
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

    public boolean Hour(String h) {
        for (int i = 0; i < myReservationList.size(); i++) {
            Reservation reservation = myReservationList.get(i);
            String ho = reservation.getHora();
            if (ho.equals(h)) {
                return true;
            }
        }
        return false;
    }

        public boolean isOcuppied (String name){
            for (int i = 0; i < myReservationList.size(); i++) {
                Reservation reservation = myReservationList.get(i);
                String ho = reservation.getHora();
                String n = reservation.getNome_jogo();
                if (ho.equals(hour) && n.equals(name)) {
                    return true;
                }
            }
            return false;
        }

        public void dateTime(String d, String h){
            SimpleDateFormat df = new SimpleDateFormat("dd-mm-yyyy");
            SimpleDateFormat df1 = new SimpleDateFormat("HH:mm");

            try {
                d1 = df1.parse(h);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            try {
                d2 = df.parse(d);
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }

        public void eliminateReserve(String n){
            for (int i = 0; i < myReservationList.size(); i++) {
                Reservation reservation = myReservationList.get(i);
                String name = reservation.getNome_jogo();
                String key = reservation.getId();
                if (name.equals(n)) {
                    dataReserve.child(key).removeValue();
                }
            }
        }

        public void getProfit(Double p, String n){
            for (int i = 0; i < myReservationList.size(); i++) {
                Reservation reservation = myReservationList.get(i);
                String name = reservation.getNome_jogo();
                if (name.equals(n)) {
                    price=price + p;
                }
            }
        }
    }
