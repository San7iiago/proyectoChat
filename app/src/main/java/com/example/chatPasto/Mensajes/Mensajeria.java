package com.example.chatPasto.Mensajes;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.example.chatPasto.R;
import com.example.chatPasto.VolleyRP;
import com.example.chatPasto.Preferences;

public class Mensajeria extends AppCompatActivity {

    public static final String MENSAJE = "MENSAJE";

    private BroadcastReceiver bR;

    private RecyclerView rv;
    private Button bTEnviarMensaje;
    private EditText eTEscribirMensaje;
    private List<MensajeDeTexto> mensajeDeTextos;
    private MensajeriaAdapter adapter;

    private String MENSAJE_ENVIAR = "";
    private String EMISOR = "";
    private String RECEPTOR;

    private static final String IP_MENSAJE = "https://androidchatpastuso.000webhostapp.com/ArchivosPHP/Enviar_Mensajes.php";

    private VolleyRP volley;
    private RequestQueue mRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mensajeria);
        mensajeDeTextos = new ArrayList<>();

        EMISOR = Preferences.obtenerPreferenceString(this, Preferences.PREFERENCE_USUARIO_LOGIN);
        Intent extras = getIntent();
        Bundle bundle = extras.getExtras();
        if(bundle!=null){
            RECEPTOR = bundle.getString("key_receptor");
        }
        volley = VolleyRP.getInstance(this);
        mRequest = volley.getRequestQueue();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        bTEnviarMensaje = (Button) findViewById(R.id.bTenviarMensaje);
        eTEscribirMensaje = (EditText) findViewById(R.id.eTEsribirMensaje);

        rv = (RecyclerView) findViewById(R.id.rvMensajes);
        LinearLayoutManager lm = new LinearLayoutManager(this);
        lm.setStackFromEnd(true);
        rv.setLayoutManager(lm);

        adapter = new MensajeriaAdapter(mensajeDeTextos,this);
        rv.setAdapter(adapter);

        bTEnviarMensaje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mensaje = validarCadena(eTEscribirMensaje.getText().toString());
                if(!mensaje.isEmpty() && !RECEPTOR.isEmpty()){
                    MENSAJE_ENVIAR = mensaje;
                    MandarMensaje();
                    CreateMensaje(mensaje,"00:00",1);
                    eTEscribirMensaje.setText("");
                }
            }
        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        setScrollbarChat();

        bR = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String mensaje = intent.getStringExtra("key_mensaje");
                String hora = intent.getStringExtra("key_hora");
                String receptor = intent.getStringExtra("key_receptor");
                if(receptor.equals(EMISOR)){
                    CreateMensaje(mensaje,hora,2);
                }
            }
        };

    }

    private String validarCadena(String cadena){
        for (int i=0; i<cadena.length(); i++){
            if (!(""+cadena.charAt(i)).equals(" ")){
                return cadena.substring(i,cadena.length());
            }

        }
        return "";
    }

    private void MandarMensaje(){
        HashMap<String,String> hashMapToken = new HashMap<>();
        hashMapToken.put("emisor",EMISOR);
        hashMapToken.put("receptor",RECEPTOR);
        hashMapToken.put("mensaje",MENSAJE_ENVIAR);

        JsonObjectRequest solicitud = new JsonObjectRequest(Request.Method.POST,IP_MENSAJE,new JSONObject(hashMapToken), new Response.Listener<JSONObject>(){
            @Override
            public void onResponse(JSONObject datos) {
                try {
                    Toast.makeText(Mensajeria.this,datos.getString("resultado"),Toast.LENGTH_SHORT).show();
                } catch (JSONException e){}
            }
        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Mensajeria.this,"Ocurrio un error",Toast.LENGTH_SHORT).show();
            }
        });
        VolleyRP.addToQueue(solicitud,mRequest,this,volley);
    }

    public void CreateMensaje(String mensaje,String hora,int tipoDeMensaje){
        MensajeDeTexto mensajeDeTextoAuxiliar = new MensajeDeTexto();
        mensajeDeTextoAuxiliar.setId("0");
        mensajeDeTextoAuxiliar.setMensaje(mensaje);
        mensajeDeTextoAuxiliar.setTipoMensaje(tipoDeMensaje);
        mensajeDeTextoAuxiliar.setHoraDelMensaje(hora);
        mensajeDeTextos.add(mensajeDeTextoAuxiliar);
        adapter.notifyDataSetChanged();
        setScrollbarChat();
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(bR);
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(bR,new IntentFilter(MENSAJE));
    }

    public void setScrollbarChat(){
        rv.scrollToPosition(adapter.getItemCount()-1);
    }

}
