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
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.example.chatPasto.Preferences;
import com.example.chatPasto.Internet.SolicitudesJson;
import com.example.chatPasto.R;
import com.example.chatPasto.VolleyRP;

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

    private static final String IP_MENSAJE = "http://kevinandroidkap.pe.hu/ArchivosPHP/Enviar_Mensajes.php";

    private VolleyRP volley;
    private RequestQueue mRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mensajes);
        mensajeDeTextos = new ArrayList<>();

        EMISOR = Preferences.obtenerPreferenceString(this, Preferences.PREFERENCE_USUARIO_LOGIN);
        Intent i = getIntent();
        Bundle bundle = i.getExtras();
        if (bundle != null) {
            RECEPTOR = bundle.getString("key_receptor");//
        }

        volley = VolleyRP.getInstance(this);
        mRequest = volley.getRequestQueue();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        bTEnviarMensaje = (Button) findViewById(R.id.bTenviarMensaje);
        eTEscribirMensaje = (EditText) findViewById(R.id.eTEsribirMensaje);

        rv = (RecyclerView) findViewById(R.id.rvMensajes);
        LinearLayoutManager lm = new LinearLayoutManager(this);
        rv.setLayoutManager(lm);

        adapter = new MensajeriaAdapter(mensajeDeTextos,this);
        rv.setAdapter(adapter);

        bTEnviarMensaje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mensaje = eTEscribirMensaje.getText().toString().trim();//"   hola  " => "hola"
                if(!mensaje.isEmpty() && !RECEPTOR.isEmpty()){
                    MENSAJE_ENVIAR = mensaje;
                    MandarMensaje();
                    CreateMensaje(mensaje,"00:00",1);
                    eTEscribirMensaje.setText("");
                }
            }
        });

        eTEscribirMensaje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                rv.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE)).isActive()){
                            setScrollbarChat();
                        }else{
                            rv.postDelayed(this,100);
                        }
                    }
                },100);

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
                String horaParametros[] = hora.split("\\,");
                String emisor = intent.getStringExtra("key_emisor_PHP");//xD
                if(emisor.equals(RECEPTOR)){
                    CreateMensaje(mensaje,horaParametros[0],2);
                }
            }
        };

        SolicitudesJson s = new SolicitudesJson() {
            @Override
            public void solicitudCompletada(JSONObject j) {
                try {
                    JSONArray js = j.getJSONArray("resultado");
                    for(int i=0;i<js.length();i++){
                        JSONObject jo = js.getJSONObject(i);
                        String menasje = jo.getString("mensaje");
                        String hora = jo.getString("hora_del_mensaje").split(",")[0];
                        int tipoMenasje = jo.getInt("tipo_mensaje");
                        CreateMensaje(menasje,hora,tipoMenasje);
                    }

                } catch (JSONException e) {
                    Toast.makeText(Mensajeria.this, "Ocurrio un error al descomprimir los mensajes", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void solicitudErronea() {

            }
        };
        HashMap<String,String> hs = new HashMap<>();
        hs.put("emisor",EMISOR);
        hs.put("receptor",RECEPTOR);
        s.solicitudJsonPOST(this,SolicitudesJson.URL_GET_ALL_MENSAJES_USUARIO,hs);

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