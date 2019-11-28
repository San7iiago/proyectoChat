package com.example.chatPasto.ActividadDeUsuarios.Solicitudes;

import com.example.chatPasto.ActividadDeUsuarios.ClasesComunicacion.Usuario;

public class Solicitudes extends Usuario{

    public Solicitudes() {
    }

    public Solicitudes(String id, String nombreCompleto, int estado, String hora, String fotoPerfil) {
        super(id, nombreCompleto, estado, hora, fotoPerfil);
    }

}
