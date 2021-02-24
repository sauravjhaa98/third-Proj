package laptop.com.kachralelo;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface RegisterInterface {
    String REGIURL = "http://imsauravvv.xyz/webservices/";
    @FormUrlEncoded
    @POST("simpleregister.php")
    Call <String> getUserRegi(
            @Field("name") String name,
            @Field("email") String email,
            @Field("phone") String phone,
            @Field("address") String address,
            @Field("password") String  password,
            @Field("cpassword") String confirmPassword
    );

}
