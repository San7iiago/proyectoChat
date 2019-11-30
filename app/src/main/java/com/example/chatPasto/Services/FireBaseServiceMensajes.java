package com.example.chatPasto.Services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat.Builder;
import android.support.v4.content.LocalBroadcastManager;

import com.example.chatPasto.Preferences;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Random;

import com.example.chatPasto.Mensajes.Mensajeria;
import com.example.chatPasto.R;

public class FireBaseServiceMensajes extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        String mensaje = remoteMessage.getData().get("mensaje");
        String hora = remoteMessage.getData().get("hora");
        String cabezera = remoteMessage.getData().get("cabezera");
        String cuerpo = remoteMessage.getData().get("cuerpo");
        String receptor = remoteMessage.getData().get("receptor");
        Mensaje(mensaje,hora,receptor);
        String emisor = Preferences.obtenerPreferenceString(this, Preferences.PREFERENCE_USUARIO_LOGIN);
        if(emisor.equals(receptor)) {
            showNotification(cabezera, cuerpo);
        }
    }

    private void Mensaje(String mensaje,String hora,String receptor){
        Intent i = new Intent(Mensajeria.MENSAJE);
        i.putExtra("key_mensaje",mensaje);
        i.putExtra("key_hora",hora);
        i.putExtra("key_receptor",receptor);
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
        builder.setSmallIcon(R.drawable.ic_mood_black_24dp);
        builder.setTicker(cuerpo);
        builder.setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        Random random = new Random();

        notificationManager.notify(random.nextInt(),builder.build());

    }

}
