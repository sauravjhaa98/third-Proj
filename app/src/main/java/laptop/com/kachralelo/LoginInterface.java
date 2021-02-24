package laptop.com.kachralelo;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface LoginInterface {
    String LOGINURL = "http://imsauravvv.xyz/webservices/";
    @FormUrlEncoded
    @POST("simplelogin.php")
    Call <String> getUserLogin(

            @Field("email") String uname,
            @Field("password") String password
    );

}
