package co.vikm.dolarpy;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import co.vikm.dolarpy.core.IKRequestManager;
import co.vikm.dolarpy.json.DolarPy;

public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //fetch
        IKRequestManager.request("/", DolarPy.class, null, false, this, new IKRequestManager.IKRequestManagerListener() {
            @Override
            public void onIKRequestManagerFinished(@Nullable Object jsonItem, @Nullable String error) {
                if (error != null){
                    Log.e("TAG", "ERROR");
                }else {
                    if (jsonItem != null){
                        DolarPy dolar_data = (DolarPy) jsonItem;

                        Log.e("data", String.valueOf(dolar_data.dolarPy.get(0).venta));
                    }
                }
            }
        });
    }
}
