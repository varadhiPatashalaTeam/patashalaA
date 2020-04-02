package in.varadhismartek.patashalaerp.Retrofit;



/**
 * Created by varadhi on 10/3/18.
 */

public class ApiUtils {



    private ApiUtils() {}

    public static final String BASE_URL = "http://13.127.95.246:8000/school/";
    //public static final String BASE_URL = "https://varadhinfotech.in/school/";


    public static APIService getAPIService() {

        return ApiClient.getClient(BASE_URL).create(APIService.class);
    }



}
