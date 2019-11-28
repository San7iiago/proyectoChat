package com.example.chatPasto.ActividadDeUsuarios.Amigos;

import com.example.chatPasto.ActividadDeUsuarios.ClasesComunicacion.Usuario;

public class AmigosAtributos extends Usuario{

    private String type_mensaje;

    public AmigosAtributos() {
    }

    public AmigosAtributos(String id, String nombreCompleto, String mensaje, String hora, String fotoPerfil, String type_mensaje) {
        super(id, nombreCompleto, mensaje, hora, fotoPerfil);
        this.type_mensaje = type_mensaje;
    }

    public String getType_mensaje() {
        return type_mensaje;
    }

    public void setType_mensaje(String type_mensaje) {
        this.type_mensaje = type_mensaje;
    }
}
