package in.varadhismartek.patashalaerp.Retrofit;

public class ApiUtilsPatashala {

    private ApiUtilsPatashala() {
    }


    public static final String PATASHALA_BASE_URL = "http://13.127.95.246:8000/patashala/";
    //public static final String PATASHALA_BASE_URL = "https://varadhinfotech.in/patashala/";

    public static APIService getService() {

        return ApiClientPataShala.getClient(PATASHALA_BASE_URL).create(APIService.class);
    }


}
