package com.tema4.tema4ejemplo1.Adapter;

import android.app.Activity;
import android.graphics.Typeface;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.tema4.tema4ejemplo1.Models.Fruit;
import com.tema4.tema4ejemplo2.R;


import java.util.List;

public class FruitAdapter extends RecyclerView.Adapter<FruitAdapter.ViewHolder> {

    private List<Fruit> fruits;
    private int layout;
    private Activity activity;
    private OnItemClickListener listener;

    // Pasamos el activity en vez del context, ya que nos hará falta para poder inflar en context menu
    public FruitAdapter(List<Fruit> fruits, int layout, Activity activity, OnItemClickListener listener) {
        this.fruits = fruits;
        this.layout = layout;
        this.activity = activity;
        this.listener = listener;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(activity).inflate(layout, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(fruits.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return fruits.size();
    }

    // Implementamos las interfaces OnCreateContextMenuListener y OnMenuItemClickListener
    // para hacer uso del context menu en RecyclerView, y sobreescribimos los métodos
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {

        public TextView textViewName;
        public TextView textViewDescription;
        public TextView textViewQuantity;
        public ImageView imageViewBackground;

        public ViewHolder(View itemView) {
            super(itemView);
            textViewName = (TextView) itemView.findViewById(R.id.TexViewName);
            textViewDescription = (TextView) itemView.findViewById(R.id.TextViewDescripcion);
            textViewQuantity = (TextView) itemView.findViewById(R.id.TextViewQuantity);
            imageViewBackground = (ImageView) itemView.findViewById(R.id.imageViewBackground);
            // Añadimos al view el listener para el context menu, en vez de hacerlo en
            // el activity mediante el método registerForContextMenu
            itemView.setOnCreateContextMenuListener(this);
        }

        public void bind(final Fruit fruit, final OnItemClickListener listener) {

            this.textViewName.setText(fruit.getName());
            this.textViewDescription.setText(fruit.getDescription());
            this.textViewQuantity.setText(fruit.getQuantity() + "");
            // Lógica aplicada para la limitación de la cantidad en cada elemento fruta
            if (fruit.getQuantity() == Fruit.LIMIT_QUANTITY) {
                textViewQuantity.setTextColor(ContextCompat.getColor(activity, R.color.colorAccent));
                textViewQuantity.setTypeface(null, Typeface.BOLD);
            } else {
                textViewQuantity.setTextColor(ContextCompat.getColor(activity, R.color.colorPrimary));
                textViewQuantity.setTypeface(null, Typeface.NORMAL);
            }
            // Cargamos la imagen con Picasso
            imageViewBackground.setImageResource(fruit.getImgBackground());
            //Si se hace con Picasso
           // Picasso.with(activity).load(fruit.getImgBackground()).fit().into(this.imageViewBackground);
            // Añadimos el listener click para cada elemento fruta
            this.imageViewBackground.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(fruit, getAdapterPosition());
                }
            });
        }

        // Sobreescribimos onCreateContextMenu, dentro del ViewHolder,
        // en vez de hacerlo en el activity
        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
            // Recogemos la posición con el método getAdapterPosition
            Fruit fruitSelected = fruits.get(this.getAdapterPosition());
            // Establecemos título e icono para cada elemento, mirando en sus propiedades
            contextMenu.setHeaderTitle(fruitSelected.getName());
            contextMenu.setHeaderIcon(fruitSelected.getImgIcon());
            // Inflamos el menú
            MenuInflater inflater = activity.getMenuInflater();
            inflater.inflate(R.menu.context_menu, contextMenu);
            // Por último, añadimos uno por uno, el listener onMenuItemClick para
            // controlar las acciones en el contextMenu, anteriormente lo manejábamos
            // con el método onContextItemSelected en el activity
            for (int i = 0; i < contextMenu.size(); i++)
                contextMenu.getItem(i).setOnMenuItemClickListener(this);
        }

        // Sobreescribimos onMenuItemClick, dentro del ViewHolder,
        // en vez de hacerlo en el activity bajo el nombre onContextItemSelected
        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            // No obtenemos nuestro objeto info
            // porque la posición la podemos rescatar desde getAdapterPosition
            switch (menuItem.getItemId()) {

                case R.id.delete_item:
                    // Observa que como estamos dentro del adaptador, podemos acceder
                    // a los métodos propios de él como notifyItemRemoved o notifyItemChanged
                    fruits.remove(getAdapterPosition());
                    notifyItemRemoved(getAdapterPosition());
                    return true;
                case R.id.reset_itemq:
                    fruits.get(getAdapterPosition()).resetQuantity();
                    notifyItemChanged(getAdapterPosition());
                    return true;
                default:
                    return false;
            }
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Fruit fruit, int position);
    }
}
