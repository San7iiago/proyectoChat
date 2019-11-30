package com.example.chatPasto;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.chatPasto.Amigos.Friends;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class Update extends AppCompatActivity {

    private static final String IP_ACTUALIZAR = "http://192.168.0.40/chatPasto/Actualizar_DATA.php";
    private static final String IP_OBTENER = "http://192.168.0.40/chatPasto/Obtener_DATA.php?id=";

    private EditText user;
    private EditText password;
    private EditText nombre;
    private EditText apellidos;
    private EditText txtFechaDeNacimiento;
    private long fechaDeNacimiento;
    private EditText correo;
    private EditText telefono;
    private Button actualizar;

    private RadioButton rdHombre;
    private RadioButton rdMujer;

    private VolleyRP volley;
    private RequestQueue mRequest;

    private static String ID_USER;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        volley = VolleyRP.getInstance(this);
        mRequest = volley.getRequestQueue();

        user = (EditText) findViewById(R.id.uUserRegister);
        password = (EditText) findViewById(R.id.uPasswordRegistro);
        nombre = (EditText) findViewById(R.id.uNombreRegistro);
        apellidos = (EditText) findViewById(R.id.uApellidosRegistro);
        txtFechaDeNacimiento = (EditText) findViewById(R.id.uFechaDeNacimiento);
        correo = (EditText) findViewById(R.id.uCorreoRegistro);
        telefono = (EditText) findViewById(R.id.uTelefonoRegistro);

        rdHombre = (RadioButton) findViewById(R.id.URDhombre);
        rdMujer = (RadioButton) findViewById(R.id.URDmujer);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarU);

        ID_USER = Preferences.obtenerPreferenceString(this, Preferences.PREFERENCE_USUARIO_LOGIN);
        System.out.println(ID_USER);
        obtenerDatos();

        /*Intent intent = this.getIntent();
        Bundle extra = intent.getExtras();*/

        actualizar = (Button) findViewById(R.id.buttonActualizar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        actualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String genero = "";

                if (rdHombre.isChecked()) genero = "hombre";
                else if (rdMujer.isChecked()) genero = "mujer";

                actualizarWebService(
                        getStringET(user).trim(),
                        getStringET(password).trim(),
                        getStringET(nombre).trim(),
                        getStringET(apellidos).trim(),
                        getStringET(txtFechaDeNacimiento).trim(),
                        getStringET(correo).trim(),
                        getStringET(telefono).trim(),
                        genero);
            }
        });
    }

    public void fecha(View view) {
        final Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(Update.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int mes, int dia) {
                Calendar calendarResultado = Calendar.getInstance();
                calendarResultado.set(Calendar.YEAR,year);
                calendarResultado.set(Calendar.MONTH,mes);
                calendarResultado.set(Calendar.DAY_OF_MONTH,dia);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                Date date = calendarResultado.getTime();
                String fechaDeNacimientoTexto = simpleDateFormat.format(date);
                System.out.println(fechaDeNacimientoTexto);
                fechaDeNacimiento = date.getTime();
                txtFechaDeNacimiento.setText(fechaDeNacimientoTexto);
            }
        },calendar.get(Calendar.YEAR)-18,calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void actualizarWebService(final String usuario, String contrasena, String nombre, String apellido, String fechaNacimiento, String correo, String numero, String genero){

        if(!usuario.isEmpty() &&
                !contrasena.isEmpty() &&
                !nombre.isEmpty() &&
                !apellido.isEmpty() &&
                !fechaNacimiento.isEmpty() &&
                !correo.isEmpty() &&
                !numero.isEmpty()
        ) {

            HashMap<String, String> hashMapToken = new HashMap<>();
            hashMapToken.put("id", usuario);
            hashMapToken.put("nombre", nombre);
            hashMapToken.put("apellidos", apellido);
            hashMapToken.put("fecha_nacimiento", fechaNacimiento);
            hashMapToken.put("correo", correo);
            hashMapToken.put("genero", genero);
            hashMapToken.put("telefono", numero);
            hashMapToken.put("password", contrasena);

            JsonObjectRequest solicitud = new JsonObjectRequest(Request.Method.POST, IP_ACTUALIZAR, new JSONObject(hashMapToken), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject datos) {
                    try {
                        String estado = datos.getString("resultado");
                        if (estado.equalsIgnoreCase("El usuario se actualizó correctamente")) {
                            Toast.makeText(Update.this, estado, Toast.LENGTH_SHORT).show();
                            finish();
                            startActivity(new Intent(Update.this, Friends.class));
                            /*String Token = FirebaseInstanceId.getInstance().getToken();
                            if(Token!=null){
                                if((""+Token.charAt(0)).equalsIgnoreCase("{")) {
                                    JSONObject js = new JSONObject(Token);//SOLO SI LES APARECE {"token":"...."} o "asdasdas"
                                    String tokenRecortado = js.getString("token");
                                    //SubirToken(usuario,tokenRecortado);
                                }else{
                                    //SubirToken(usuario,Token);
                                }
                            }
                            else Toast.makeText(Registro.this,"El token es nulo",Toast.LENGTH_SHORT).show();
*/
                        } else {
                            Toast.makeText(Update.this, estado, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        Toast.makeText(Update.this, "No se pudo actualizar. JSONError. "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        System.out.println(e.getMessage());
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(Update.this, "No se pudo actualizar. Error response. "+error.getMessage(), Toast.LENGTH_SHORT).show();
                    System.out.println(error.getMessage());
                }
            });
            VolleyRP.addToQueue(solicitud, mRequest, this, volley);
        }else{
            Toast.makeText(this, "Por favor llene todos los campos", Toast.LENGTH_SHORT).show();
        }
    }

    private void obtenerDatos(){
        JsonObjectRequest solicitud = new JsonObjectRequest(IP_OBTENER+ID_USER,null, new Response.Listener<JSONObject>(){
            @Override
            public void onResponse(JSONObject datos) {
                try {
                    String usuario = datos.getString("datos");
                    JSONObject jsonObject = new JSONObject(usuario);
                    user.setText(jsonObject.getString("id"));
                    password.setText(jsonObject.getString("password"));
                    nombre.setText(jsonObject.getString("nombre"));
                    apellidos.setText(jsonObject.getString("apellidos"));
                    txtFechaDeNacimiento.setText(jsonObject.getString("fecha_de_nacimiento"));
                    correo.setText(jsonObject.getString("correo"));
                    telefono.setText(jsonObject.getString("telefono"));
                    if(jsonObject.getString("genero").equals("hombre")){
                        rdHombre.setChecked(true);
                    }else{
                        rdMujer.setChecked(true);
                    }
                } catch (JSONException e) {
                    Toast.makeText(Update.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                    System.out.println(e.getMessage());
                }
            }
        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Update.this,"Ocurrio un error, por favor contactese con el administrador",Toast.LENGTH_SHORT).show();
            }
        });
        VolleyRP.addToQueue(solicitud,mRequest,this,volley);
    }

    private String getStringET(EditText e){
        return e.getText().toString();
    }
}
