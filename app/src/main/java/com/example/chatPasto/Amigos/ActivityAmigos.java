package com.example.chatPasto.Amigos;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import com.example.chatPasto.Login;
import com.example.chatPasto.Preferences;
import com.example.chatPasto.R;
import com.example.chatPasto.Update;
import com.example.chatPasto.VolleyRP;

public class ActivityAmigos extends AppCompatActivity {

    private RecyclerView rv;
    private List<AmigosAtributos> atributosList;
    private AmigosAdapter adapter;

    private VolleyRP volley;
    private RequestQueue mRequest;

    private static final String URL_GET_ALL_USUARIOS = "http://192.168.0.40/chatPasto/Amigos_GETALL.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amigos);
        setTitle("Amigos");

        volley = VolleyRP.getInstance(this);
        mRequest = volley.getRequestQueue();

        atributosList = new ArrayList<>();

        rv = (RecyclerView) findViewById(R.id.amigosRecyclerView);
        LinearLayoutManager lm = new LinearLayoutManager(this);
        rv.setLayoutManager(lm);

        adapter = new AmigosAdapter(atributosList,this);
        rv.setAdapter(adapter);
        SolicitudJSON();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_amigos,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.opActualizarDatos:
                startActivity(new Intent(this, Update.class));
                finish();
                return true;
            case R.id.opCerrarSesion:
                Preferences.savePreferenceBoolean(ActivityAmigos.this,false,Preferences.PREFERENCE_ESTADO_BUTTON_SESION);
                Intent i = new Intent(ActivityAmigos.this, Login.class);
                startActivity(i);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void agregarAmigo(int fotoDePerfil, String nombre, String ultimoMensaje, String hora,String id){
        AmigosAtributos amigosAtributos = new AmigosAtributos();
        amigosAtributos.setFotoDePerfil(fotoDePerfil);
        amigosAtributos.setNombre(nombre);
        amigosAtributos.setUltimoMensaje(ultimoMensaje);
        amigosAtributos.setHora(hora);
        amigosAtributos.setId(id);
        atributosList.add(amigosAtributos);
        adapter.notifyDataSetChanged();
    }

    public void SolicitudJSON(){
        JsonObjectRequest solicitud = new JsonObjectRequest(URL_GET_ALL_USUARIOS,null, new Response.Listener<JSONObject>(){
            @Override
            public void onResponse(JSONObject datos) {
                try {
                    String TodosLosDatos = datos.getString("resultado");
                    String TodosLosUsuariosQueTienenToken = datos.getString("usuariosConTokens");
                    JSONArray jsonArray = new JSONArray(TodosLosDatos);
                    JSONArray jsUserTokens = new JSONArray(TodosLosUsuariosQueTienenToken);
                    String NuestroUsuario = Preferences.obtenerPreferenceString(ActivityAmigos.this,Preferences.PREFERENCE_USUARIO_LOGIN);
                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject js = jsonArray.getJSONObject(i);
                        if(!NuestroUsuario.equals(js.getString("id"))){
                            for(int k=0;k<jsUserTokens.length();k++){
                                JSONObject UsuarioConTokens = jsUserTokens.getJSONObject(k);
                                if(js.getString("id").equals(UsuarioConTokens.getString("id"))){
                                    agregarAmigo(R.drawable.user,js.getString("nombre"),"mensaje "+i,"00:00",js.getString("id"));
                                }
                            }
                        }
                    }
                } catch (JSONException e) {
                    Toast.makeText(ActivityAmigos.this,"Ocurrio un error al descomponer el JSON",Toast.LENGTH_SHORT).show();
                }
            }
        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ActivityAmigos.this,"Ocurrio un error, por favor contactese con el administrador",Toast.LENGTH_SHORT).show();
            }
        });
        VolleyRP.addToQueue(solicitud,mRequest,this,volley);
    }

}
