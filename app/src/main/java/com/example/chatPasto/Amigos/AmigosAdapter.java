package com.example.chatPasto.Amigos;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.chatPasto.Mensajes.Mensajeria;
import com.example.chatPasto.R;

import java.util.List;

public class AmigosAdapter extends RecyclerView.Adapter<AmigosAdapter.HolderAmigos> {

    private List<Amigos> atributosList;
    private Context context;

    public AmigosAdapter(List<Amigos> atributosList, Context context){
        this.atributosList = atributosList;
        this.context = context;
    }

    @NonNull
    @Override
    public HolderAmigos onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_amigos,parent,false);
        return new AmigosAdapter.HolderAmigos(v);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderAmigos holder, final int position) {
        holder.imageView.setImageResource(R.drawable.ic_account_circle);
        holder.nombre.setText(atributosList.get(position).getNombre());
        holder.mensaje.setText(atributosList.get(position).getUltimoMensaje());
        holder.hora.setText(atributosList.get(position).getHora());

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, Mensajeria.class);
                i.putExtra("key_receptor", atributosList.get(position).getId());
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return atributosList.size();
    }

    static class HolderAmigos extends RecyclerView.ViewHolder{
        CardView cardView;
        ImageView imageView;
        TextView nombre;
        TextView mensaje;
        TextView hora;

        public HolderAmigos(@NonNull View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.cardViewAmigos);
            imageView = (ImageView) itemView.findViewById(R.id.fotoPerfilAmigo);
            nombre = (TextView) itemView.findViewById(R.id.nombreAmigo);
            mensaje = (TextView) itemView.findViewById(R.id.mensajeAmigo);
            hora = (TextView) itemView.findViewById(R.id.horaMensajeAmigo);
        }
    }
}
