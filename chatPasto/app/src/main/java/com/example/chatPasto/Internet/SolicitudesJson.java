package com.example.chatPasto.Internet;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.HashMap;

import com.example.chatPasto.VolleyRP;

public abstract class SolicitudesJson {

    public final static String URL_GET_ALL_DATOS = "http://kevinandroidkap.pe.hu/ArchivosPHP/Datos_GETALL.php?id=";
    public final static String URL_GET_ALL_MENSAJES_USUARIO = "http://kevinandroidkap.pe.hu/ArchivosPHP/Mensajes_GETID.php";
    public final static String URL_ENVIAR_SOLICITUD = "http://kevinandroidkap.pe.hu/ArchivosPHP/Solicitudes_ENVIAR.php";
    public final static String URL_CANCELAR_SOLICITUD = "http://kevinandroidkap.pe.hu/ArchivosPHP/Solicitudes_CANCELAR.php";
    public final static String URL_ACEPTAR_SOLICITUD = "http://kevinandroidkap.pe.hu/ArchivosPHP/Solicitudes_ACEPTAR.php";
    public final static String URL_ELIMINAR_USUARIO = "http://kevinandroidkap.pe.hu/ArchivosPHP/Solicitudes_ELIMINAR.php";
    public static final String IP_TOKEN_UPLOAD = "http://kevinandroidkap.pe.hu/ArchivosPHP/Token_INSERTandUPDATE.php";
    public static final String URL_SUBIR_FOTO = "http://kevinandroidkap.pe.hu/ArchivosPHP/Imagen_Actualizar.php";

    public abstract void solicitudCompletada(JSONObject j);
    public abstract void solicitudErronea();

    public SolicitudesJson(){}

    public void solicitudJsonGET(Context c,String URL){
        JsonObjectRequest solicitud = new JsonObjectRequest(URL,null, new Response.Listener<JSONObject>(){
            @Override
            public void onResponse(JSONObject datos) {
                solicitudCompletada(datos);
            }
        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                solicitudErronea();
            }
        });
        VolleyRP.addToQueue(solicitud,VolleyRP.getInstance(c).getRequestQueue(),c,VolleyRP.getInstance(c));
    }

    public void solicitudJsonPOST(Context c, String URL, HashMap h){
        JsonObjectRequest solicitud = new JsonObjectRequest(Request.Method.POST,URL,new JSONObject(h), new Response.Listener<JSONObject>(){
            @Override
            public void onResponse(JSONObject datos) {
                solicitudCompletada(datos);
            }
        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                solicitudErronea();
            }
        });
        VolleyRP.addToQueue(solicitud,VolleyRP.getInstance(c).getRequestQueue(),c,VolleyRP.getInstance(c));
    }

}
