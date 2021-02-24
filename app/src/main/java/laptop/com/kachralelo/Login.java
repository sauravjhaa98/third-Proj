package laptop.com.kachralelo;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class Login extends AppCompatActivity {
    TextView forgot_password,register;
    EditText email_edittext,password_edittext;
    Button login_button;
    String PasswordPattern="[a-zA-Z0-9\\!\\@\\#\\$]{8,24}";
    String  emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    ProgressDialog progressDialog;
    String Email,Password;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        email_edittext=(EditText)findViewById(R.id.email);
        password_edittext=(EditText)findViewById(R.id.password);
        login_button=(Button)findViewById(R.id.login_btn);
        forgot_password=(TextView)findViewById(R.id.forgot_password);
        register=(TextView)findViewById(R.id.link_signup);

        getSupportActionBar().hide();
        sharedPreferences=getSharedPreferences("login",MODE_PRIVATE);
        if(sharedPreferences.contains("username") && sharedPreferences.contains("password")){
            startActivity(new Intent(Login.this,WelcomeActivity.class));
            finish();   //finish current activity
        }

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i= new Intent(Login.this,SignUp.class) ;
                startActivity(i);
                finish();
            }
        });
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 Email=email_edittext.getText().toString().trim();
                 Password=password_edittext.getText().toString().trim();
                if (Email.isEmpty() && Password.isEmpty()){
                    Toast.makeText(Login.this, "Please Enter Values", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    if(Email.matches(emailPattern) && Password.matches(PasswordPattern)){
                        loginUser();
                    }
                    else
                    {
                        Toast.makeText(Login.this, " Please Enter Your Correct Details", Toast.LENGTH_SHORT).show();
                    }

                }


            }
        });

    }

    private void loginUser() {
        progressDialog= new ProgressDialog(Login.this);
        progressDialog.setMessage("PLease Wait");
        progressDialog.show();

        final String username = email_edittext.getText().toString().trim();
        final String password = password_edittext.getText().toString().trim();

        Retrofit retrofit= new Retrofit.Builder().baseUrl(LoginInterface.LOGINURL).addConverterFactory(ScalarsConverterFactory.create()).build();

        LoginInterface api = retrofit.create(LoginInterface.class);

        Call <String> call = api.getUserLogin(username,password);

        call.enqueue(new Callback <String>() {
            @Override
            public void onResponse(Call<String> call, Response <String> response) {
                Log.i("Responsestring", response.body().toString());

                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        progressDialog.dismiss();
                        SharedPreferences.Editor sp = sharedPreferences.edit();
                        sp.putString("username", Email);
                        sp.putString("password", Password);
                        sp.commit();
                        Log.i("onSuccess", response.body().toString());

                        String jsonresponse = response.body().toString();
                        parseLoginData(jsonresponse);

                    } else {
                        Log.i("onEmptyResponse", "Returned empty response");//Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });

    }

    private void parseLoginData(String response){
        try {
            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject.getString("status").equals("true")) {


                Toast.makeText(Login.this, "Login Successfully!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Login.this,WelcomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                this.finish();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}