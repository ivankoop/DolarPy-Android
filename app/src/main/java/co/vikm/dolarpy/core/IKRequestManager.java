package co.vikm.dolarpy.core;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.bluelinelabs.logansquare.LoganSquare;
import com.bluelinelabs.logansquare.annotation.JsonObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

/**
 * Created by ivankoop on 9/9/17.
 */

@JsonObject
public class IKRequestManager implements Parcelable {

    public static String BASE_URL = "http://dolar.vikm.co";

    public interface IKRequestManagerListener {
        void onIKRequestManagerFinished(@Nullable Object jsonItem, @Nullable String error);
    }



    protected IKRequestManager(Parcel in) {}

    public IKRequestManager() {}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

    }

    public static final Creator<IKRequestManager> CREATOR = new Creator<IKRequestManager>() {
        @Override
        public IKRequestManager createFromParcel(Parcel in) {
            return new IKRequestManager(in);
        }

        @Override
        public IKRequestManager[] newArray(int size) {
            return new IKRequestManager[size];
        }
    };

    public static void request(@NonNull final String endpoint,
                               @NonNull final Class<? extends IKRequestManager> jsonClass,
                               @Nullable final Map<String,String> post,
                               @Nullable final boolean isList,
                               @Nullable final Activity activity,
                               @NonNull final IKRequestManagerListener listener) {

        final Handler handler = new Handler();

        if(activity!=null && !isNetworkAvailable(activity)) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    listener.onIKRequestManagerFinished(null, "DolarPy no está pudiendo obtener una conexión, compruebe la señal de su teléfono y si puede acceder a internet");
                }
            });
        }

        new Thread(new Runnable() {
            @Override
            public void run() {

                HttpURLConnection con = null;
                BufferedReader in = null;
                BufferedWriter out = null;
                String finalURL = BASE_URL + endpoint;


                String jsonString = "";

                try {

                    URL url = new URL(finalURL);
                    con = (HttpURLConnection) url.openConnection();

                    con.setReadTimeout(60000);
                    con.setConnectTimeout(10000);
                    con.setRequestProperty("Accept", "application/json");

                    if (post != null) {

                        con.setDoOutput(true);
                        con.setRequestMethod("POST");

                        String postString = "";
                        for (String key : post.keySet()) {
                            postString += "&" + key + "=" + post.get(key);
                        }

                        Log.d("enviado","(" + finalURL + "): " + postString);


                        out = new BufferedWriter(new OutputStreamWriter(new BufferedOutputStream(con.getOutputStream()), "UTF-8"));
                        out.write(postString);
                        out.flush();
                        out.close();

                    }

                    in = new BufferedReader(new InputStreamReader(con.getInputStream()));

                    String line = in.readLine();
                    while (line != null) {
                        jsonString += line;
                        line = in.readLine();
                    }

                    int maxLogSize = 1000;
                    for (int i = 0; i <= jsonString.length() / maxLogSize; i++) {
                        int start = i * maxLogSize;
                        int end = (i + 1) * maxLogSize;
                        end = end > jsonString.length() ? jsonString.length() : end;
                        Log.d("ivankoop", "recibido (" + finalURL + "): " + jsonString.substring(start, end));
                    }

                    if (isList){
                        final List<?> jsonItem = LoganSquare.parseList(jsonString, jsonClass);

                        if(jsonItem == null){

                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    listener.onIKRequestManagerFinished(null, "No se recuperaron datos");
                                }
                            });

                        } else {

                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    listener.onIKRequestManagerFinished(jsonItem, null);
                                }
                            });

                        }
                    }else {
                        final IKRequestManager jsonItem = LoganSquare.parse(jsonString, jsonClass);

                        if(jsonItem == null){

                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    listener.onIKRequestManagerFinished(null, "No se recuperaron datos");
                                }
                            });

                        } else {

                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    listener.onIKRequestManagerFinished(jsonItem, null);
                                }
                            });

                        }
                    }

                } catch (final IOException ioe){

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            listener.onIKRequestManagerFinished(null, "Hubo un error de conexión: " + ioe.getMessage());
                        }
                    });

                } catch (final Exception e) {

                    e.printStackTrace();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            listener.onIKRequestManagerFinished(null, "Hubo un error no catalogado: " + e.getMessage());
                        }
                    });

                } finally {
                    if (out != null) try {
                        out.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (in != null) try {
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (con != null) con.disconnect();
                }


            }
        }).start();

    }

    private static boolean isNetworkAvailable(Activity activity) {

        ConnectivityManager connectivityManager = (ConnectivityManager)activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;

    }
}

