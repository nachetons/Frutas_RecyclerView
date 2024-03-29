package com.tema4.tema4ejemplo1.Activities;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tema4.tema4ejemplo1.Adapter.FruitAdapter;
import com.tema4.tema4ejemplo1.Models.Fruit;
import com.tema4.tema4ejemplo2.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    // Declaramos las variables
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private FruitAdapter adapter;

    private List<Fruit> fruits;
    private int counter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fruits = this.getAllFruits();

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(this);
        // Observa como pasamos el activity, con this. Podríamos declarar
        // Activity o Context en el constructor y funcionaría pasando el mismo valor, this
        adapter = new FruitAdapter(fruits, R.layout.recycler_view_item, this, new FruitAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Fruit fruit, int position) {
                fruit.addQuantity(1);
                adapter.notifyItemChanged(position);
            }
        });

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        // No registramos para el menu contexto nada aquí, lo movemos al ViewHolder del adaptador
    }


   @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {


            case R.id.add_item:
                this.addFruit(0);

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private void addFruit (int position){
        // Rescatamos el número de frutas para saber en que posición insertaremos
        position = fruits.size();
        fruits.add(position, new Fruit("Add " + (++counter), "Fruit added by the user", R.drawable.plum_bg, R.mipmap.ic_plum, 0));
        adapter.notifyItemInserted(position);
        layoutManager.scrollToPosition(position);
    }
// Cogemos 7 frutas las cuales se mostraran al iniciar el main
    private List<Fruit> getAllFruits() {
        return new ArrayList<Fruit>() {{
            add(new Fruit("Strawberry", "Strawberry description", R.drawable.strawberry_bg, R.mipmap.ic_strawberry, 0));
            add(new Fruit("Orange", "Orange description", R.drawable.orange_bg, R.mipmap.ic_orange, 0));
            add(new Fruit("Apple", "Apple description", R.drawable.apple_bg, R.mipmap.ic_apple, 0));
            add(new Fruit("Banana", "Banana description", R.drawable.banana_bg, R.mipmap.ic_banana, 0));
            add(new Fruit("Cherry", "Cherry description", R.drawable.cherry_bg, R.mipmap.ic_cherry, 0));
            add(new Fruit("Pear", "Pear description", R.drawable.pear_bg, R.mipmap.ic_pear, 0));
            add(new Fruit("Raspberry", "Raspberry description", R.drawable.raspberry_bg, R.mipmap.ic_raspberry, 0));
        }};
    }
}