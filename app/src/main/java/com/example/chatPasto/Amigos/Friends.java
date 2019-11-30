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
import com.example.chatPasto.Login;
import com.example.chatPasto.R;
import com.example.chatPasto.Update;
import com.example.chatPasto.VolleyRP;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Friends extends AppCompatActivity {
    private RecyclerView rv;
    private List<Amigos> aList;
    private AmigosAdapter amigosAdapter;

    private VolleyRP volley;
    private RequestQueue mRequest;

    private static final String URL_GET_ALL_USERS = "http://192.168.0.40/chatPasto/Amigos_GETALL.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        aList = new ArrayList<>();

        rv = (RecyclerView) findViewById(R.id.rvAmigos);
        LinearLayoutManager lm = new LinearLayoutManager(this);
        rv.setLayoutManager(lm);

        volley = VolleyRP.getInstance(this);
        mRequest = volley.getRequestQueue();

        amigosAdapter = new AmigosAdapter(aList, this);
        rv.setAdapter(amigosAdapter);

        SolicitudJSON();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_usuario, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.opActualizarDatos:
                startActivity(new Intent(this, Update.class));
                finish();
                return true;
            case R.id.opCerrarSesion:
                startActivity(new Intent(this, Login.class));
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void agregarAmigo(String id, int fotoPerfil, String nombre, String ultimoMensaje, String hora){
        Amigos amigo = new Amigos();
        amigo.setId(id);
        amigo.setFotoPerfil(fotoPerfil);
        amigo.setNombre(nombre);
        amigo.setUltimoMensaje(ultimoMensaje);
        amigo.setHora(hora);
        aList.add(amigo);
        amigosAdapter.notifyDataSetChanged();
    }

    public void SolicitudJSON(){
        JsonObjectRequest solicitud = new JsonObjectRequest(URL_GET_ALL_USERS,null, new Response.Listener<JSONObject>(){
            @Override
            public void onResponse(JSONObject datos) {
                try {
                    String todosUsuarios = datos.getString("resultado");
                    JSONArray jsonArray = new JSONArray(todosUsuarios);
                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        agregarAmigo(jsonObject.getString("id"), R.drawable.ic_account_circle, jsonObject.getString("nombre"), "mensaje", "00:00");
                    }
                } catch (JSONException e) {
                    Toast.makeText(Friends.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Friends.this,"Ocurrio un error, por favor contactese con el administrador",Toast.LENGTH_SHORT).show();
            }
        });
        VolleyRP.addToQueue(solicitud,mRequest,this,volley);
    }
}
