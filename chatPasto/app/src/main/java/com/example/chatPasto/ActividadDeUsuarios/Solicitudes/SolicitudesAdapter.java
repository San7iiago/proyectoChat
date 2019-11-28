package com.example.chatPasto.ActividadDeUsuarios.Solicitudes;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import com.example.chatPasto.R;

public class SolicitudesAdapter extends RecyclerView.Adapter<SolicitudesAdapter.solicitudesHolder> {

    private List<Solicitudes> listSolicitudes;
    private Context context;
    private FragmentSolicitudes f;

    public SolicitudesAdapter(List<Solicitudes> listSolicitudes, Context context,FragmentSolicitudes f){
        this.listSolicitudes = listSolicitudes;
        this.context = context;
        this.f = f;
    }

    @Override
    public solicitudesHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_solicitudes,parent,false);
        return new SolicitudesAdapter.solicitudesHolder(v);
    }

    @Override
    public void onBindViewHolder(solicitudesHolder holder, final int position) {
        Picasso.with(context).load(listSolicitudes.get(position).getFotoPerfil()).error(R.drawable.ic_account_circle).into(holder.fotoPerfil);
        holder.nombre.setText(listSolicitudes.get(position).getNombreCompleto());
        holder.hora.setText(listSolicitudes.get(position).getHora());

        switch (listSolicitudes.get(position).getEstado()){
            case 2:
                holder.cancelar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        f.cancelarSolicitud(listSolicitudes.get(position).getId());
                    }
                });
                holder.aceptar.setVisibility(View.GONE);
                break;
            case 3:

                holder.cancelar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        f.cancelarSolicitud(listSolicitudes.get(position).getId());
                    }
                });

                holder.aceptar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        f.aceptarSolicitud(listSolicitudes.get(position).getId());
                    }
                });


                holder.aceptar.setVisibility(View.VISIBLE);
                break;
        }

    }

    @Override
    public int getItemCount() {
        return listSolicitudes.size();
    }

    static class solicitudesHolder extends RecyclerView.ViewHolder{

        CardView cardView;
        ImageView fotoPerfil;
        TextView nombre;
        TextView hora;
        Button cancelar;
        Button aceptar;

        public solicitudesHolder(View itemView) {
            super(itemView);
            fotoPerfil = (ImageView) itemView.findViewById(R.id.fotoDePerfilSolicitud);
            nombre = (TextView) itemView.findViewById(R.id.nombreSolicitud);
            hora = (TextView) itemView.findViewById(R.id.horaSolicitud);
            cancelar = (Button) itemView.findViewById(R.id.cancelarSolicitud);
            aceptar = (Button) itemView.findViewById(R.id.AceptarSolicitud);
        }
    }



}

