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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class SignUp extends AppCompatActivity {
    EditText Name,Email,Phone ,Address,Password,confirm_password;
    Button sign_up;
    TextView alr;
    String  emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    String MobilePattern = "[0-9]{10}";
    String PasswordPattern="[a-zA-Z0-9\\!\\@\\#\\$]{8,24}";
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        Name=(EditText)findViewById(R.id.name_signup);
        Email=(EditText)findViewById(R.id.email_signup);
        Phone=(EditText)findViewById(R.id.phone_signup);
        Address=(EditText)findViewById(R.id.address_signup);
        Password=(EditText)findViewById(R.id.password_signup);
        confirm_password=(EditText)findViewById(R.id.confirm_password);
        sign_up=(Button) findViewById(R.id.signup_btn);
        alr=(TextView)findViewById(R.id.link_signup);
        getSupportActionBar().hide();

        alr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i= new Intent(SignUp.this,Login.class);
                startActivity(i);
                finish();
            }
        });
        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String st_name=Name.getText().toString().trim();
                String st_email=Email.getText().toString().trim();
                String st_phone=Phone.getText().toString().trim();
                String st_address=Address.getText().toString().trim();
                String st_password=Password.getText().toString().trim();
                String st_cpassword=confirm_password.getText().toString().trim();
                if(st_name.isEmpty() || st_email.isEmpty() || st_phone.isEmpty() || st_address.isEmpty()|| st_password.isEmpty()||st_cpassword.isEmpty())
                {
                    Toast.makeText(SignUp.this, "Please Enter Values  ", Toast.LENGTH_SHORT).show();
                }
                else {
                    if(st_email.matches(emailPattern) && st_phone.matches(MobilePattern) & st_password.matches(PasswordPattern) )
                    {
                        registerMe();
                    }
                    else
                    {
                        Toast.makeText(SignUp.this, " Please Fill Correct Details", Toast.LENGTH_SHORT).show();
                    }}


            }
        });
    }

    private void registerMe() {
        progressDialog= new ProgressDialog(SignUp.this);
        progressDialog.setMessage("Please Wait");
        progressDialog.show();

        final String name = Name.getText().toString();
        final String email = Email.getText().toString();
        final String phone = Phone.getText().toString();
        final String address=Address.getText().toString();
        final String password = Password.getText().toString();
        final String confirmPassword=confirm_password.getText().toString();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RegisterInterface.REGIURL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        RegisterInterface api = retrofit.create(RegisterInterface.class);

        Call <String> call = api.getUserRegi(name,email,phone,address,password,confirmPassword);

        call.enqueue(new Callback <String>() {
            @Override
            public void onResponse(Call<String> call, Response <String> response) {
                Log.i("Responsestring", response.body().toString());
                Toast.makeText(SignUp.this, "value" + response, Toast.LENGTH_SHORT).show();

                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        progressDialog.dismiss();
                        Log.i("onSuccess", response.body().toString());
                        String jsonresponse = response.body().toString();

                        try {
                            parseRegData(jsonresponse);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

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

    private void parseRegData(String response) throws JSONException {

        JSONObject jsonObject = new JSONObject(response);
        if (jsonObject.optString("status").equals("true")){

            Toast.makeText(SignUp.this, "Registered Successfully!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(SignUp.this,Login.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            this.finish();
        }else {


        }
    }
}