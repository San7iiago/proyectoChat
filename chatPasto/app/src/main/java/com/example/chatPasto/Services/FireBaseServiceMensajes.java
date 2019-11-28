package com.example.chatPasto.Services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat.Builder;
import android.support.v4.content.LocalBroadcastManager;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.greenrobot.eventbus.EventBus;

import java.util.Random;

import com.example.chatPasto.ActividadDeUsuarios.Amigos.AmigosAtributos;
import com.example.chatPasto.ActividadDeUsuarios.Amigos.EliminarFragmentAmigos;
import com.example.chatPasto.ActividadDeUsuarios.Solicitudes.SolicitudFragmentSolicitudes;
import com.example.chatPasto.ActividadDeUsuarios.Solicitudes.Solicitudes;
import com.example.chatPasto.ActividadDeUsuarios.Usuarios.AceptarSolicitudFragmentUsuarios;
import com.example.chatPasto.ActividadDeUsuarios.Usuarios.EliminarAmigoFragmentUsuarios;
import com.example.chatPasto.ActividadDeUsuarios.Usuarios.RecibirSolicitudAmistadFragmentUsuarios;
import com.example.chatPasto.ActividadDeUsuarios.Usuarios.SolicitudFragmentUsuarios;
import com.example.chatPasto.Preferences;
import com.example.chatPasto.Mensajes.Mensajeria;
import com.example.chatPasto.R;

public class FireBaseServiceMensajes extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        String type = remoteMessage.getData().get("type");
        String cabezera =  remoteMessage.getData().get("cabezera");
        String cuerpo =  remoteMessage.getData().get("cuerpo");
        switch (type){
            case "mensaje":
                String mensaje = remoteMessage.getData().get("mensaje");
                String hora = remoteMessage.getData().get("hora");
                String receptor = remoteMessage.getData().get("receptor");
                String emisorPHP = remoteMessage.getData().get("emisor");
                String emisor = Preferences.obtenerPreferenceString(this,Preferences.PREFERENCE_USUARIO_LOGIN);
                if(emisor.equals(receptor)){
                    Mensaje(mensaje,hora,emisorPHP);
                    showNotification(cabezera,cuerpo);
                }
                break;
            case "solicitud":
                String sub_tipo_solicitud = remoteMessage.getData().get("sub_type");
                String usuario_envio_solicitud;
                switch (sub_tipo_solicitud){
                    case "enviar_solicitud":
                        EventBus.getDefault().post(new Solicitudes(remoteMessage.getData().get("user_envio_solicitud"),
                                remoteMessage.getData().get("user_envio_solicitud_nombre"),
                                3,remoteMessage.getData().get("hora"),
                                remoteMessage.getData().get("imagen")));
                        EventBus.getDefault().post(new RecibirSolicitudAmistadFragmentUsuarios(remoteMessage.getData().get("user_envio_solicitud")));
                        usuario_envio_solicitud = remoteMessage.getData().get("user_envio_solicitud");
                        showNotification(cabezera,cuerpo);
                        break;
                    case "cancelar_solicitud":
                        EventBus.getDefault().post(new SolicitudFragmentUsuarios(remoteMessage.getData().get("user_envio_solicitud")));
                        EventBus.getDefault().post(new SolicitudFragmentSolicitudes(remoteMessage.getData().get("user_envio_solicitud")));
                        break;
                    case "aceptar_solicitud":
                        EventBus.getDefault().post(new AmigosAtributos(remoteMessage.getData().get("user_envio_solicitud"),
                                remoteMessage.getData().get("user_envio_solicitud_nombre"),
                                remoteMessage.getData().get("ultimoMensaje")==null ? "null":
                                    remoteMessage.getData().get("ultimoMensaje"),
                                remoteMessage.getData().get("hora_del_mensaje")==null ? "null" :
                                        remoteMessage.getData().get("hora_del_mensaje").split(",")[0],
                                remoteMessage.getData().get("imagen"),
                                remoteMessage.getData().get("type_mensaje")==null ? "null" :
                                    remoteMessage.getData().get("type_mensaje")));
                        EventBus.getDefault().post(new SolicitudFragmentSolicitudes(remoteMessage.getData().get("user_envio_solicitud")));
                        EventBus.getDefault().post(new AceptarSolicitudFragmentUsuarios(remoteMessage.getData().get("user_envio_solicitud")));
                        usuario_envio_solicitud = remoteMessage.getData().get("user_envio_solicitud");
                        showNotification(cabezera,cuerpo);
                        break;
                    case "eliminar_amigo":
                        EventBus.getDefault().post(new EliminarFragmentAmigos(remoteMessage.getData().get("user_envio_solicitud")));
                        EventBus.getDefault().post(new EliminarAmigoFragmentUsuarios(remoteMessage.getData().get("user_envio_solicitud")));
                        break;
                }
                break;
        }

    }

    private void Mensaje(String mensaje,String hora,String emisor){
        Intent i = new Intent(Mensajeria.MENSAJE);
        i.putExtra("key_mensaje",mensaje);
        i.putExtra("key_hora",hora);
        i.putExtra("key_emisor_PHP",emisor);
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(i);
    }

    private void showNotification(String cabezera, String cuerpo){
        Intent i = new Intent(this,Mensajeria.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,i,PendingIntent.FLAG_ONE_SHOT);

        Uri soundNotification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Builder builder = new Builder(this);
        builder.setAutoCancel(true);
        builder.setContentTitle(cabezera);
        builder.setContentText(cuerpo);
        builder.setSound(soundNotification);
        builder.setSmallIcon(R.drawable.ic_action_key);
        builder.setTicker(cuerpo);
        builder.setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        Random random = new Random();

        notificationManager.notify(random.nextInt(),builder.build());

    }


}
