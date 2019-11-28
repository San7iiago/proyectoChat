package com.example.chatPasto.ActividadDeUsuarios.ClasesComunicacion;

public class Usuario {

    private String id;
    private String nombreCompleto;
    private int estado;
    private String mensaje;
    private String hora;
    private String FotoPerfil;

    public Usuario(){

    }

    public Usuario(String id, String nombreCompleto, int estado, String hora, String fotoPerfil) {
        this.id = id;
        this.nombreCompleto = nombreCompleto;
        this.estado = estado;
        this.hora = hora;
        FotoPerfil = fotoPerfil;
    }

    public Usuario(String id, String nombreCompleto, String mensaje, String hora, String fotoPerfil) {
        this.id = id;
        this.nombreCompleto = nombreCompleto;
        this.mensaje = mensaje;
        this.hora = hora;
        FotoPerfil = fotoPerfil;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getFotoPerfil() {
        return FotoPerfil;
    }

    public void setFotoPerfil(String fotoPerfil) {
        FotoPerfil = fotoPerfil;
    }
}
