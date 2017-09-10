package co.vikm.dolarpy;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import co.vikm.dolarpy.core.IKRequestManager;
import co.vikm.dolarpy.json.DolarPy;
import co.vikm.dolarpy.json.DolarPyItem;

public class MainActivity extends AppCompatActivity {

    private ListView dolarpy_listview;
    private ProgressBar progress_bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dolarpy_listview = (ListView) findViewById(R.id.dolarpy_listview);
        progress_bar = (ProgressBar) findViewById(R.id.progress_bar);

        //fetch
        IKRequestManager.request("/", DolarPy.class, null, false, this, new IKRequestManager.IKRequestManagerListener() {
            @Override
            public void onIKRequestManagerFinished(@Nullable Object jsonItem, @Nullable String error) {
                if (error != null){
                    Log.e("ERROR", error);
                }else {
                    if (jsonItem != null){
                        DolarPy dolar_data = (DolarPy) jsonItem;

                        progress_bar.setVisibility(View.GONE);
                        dolarpy_listview.setVisibility(View.VISIBLE);

                        updateAdapter(dolar_data.dolarPy);
                    }
                }
            }
        });



    }

    public void updateAdapter(List<DolarPyItem> dolarPy) {

        DolarPyAdpater adpater = new DolarPyAdpater(this,dolarPy);
        dolarpy_listview.setAdapter(adpater);

    }

    private class DolarPyAdpater extends ArrayAdapter<DolarPyItem> {

        public DolarPyAdpater(Context context, List<DolarPyItem> list){
            super(context,0,list);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View returnView;
            if (convertView != null) {
                returnView = convertView;
            } else {
                LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                returnView = inflater.inflate(R.layout.dolarpy_item, parent, false);
            }

            final DolarPyItem item = getItem(position);

            final TextView compra_label = (TextView) returnView.findViewById(R.id.compra_label);
            final TextView venta_label = (TextView) returnView.findViewById(R.id.venta_label);
            final TextView name_label = (TextView) returnView.findViewById(R.id.name_label);

            compra_label.setText("$ " + String.valueOf(item.compra));
            venta_label.setText("$ " + String.valueOf(item.venta));
            name_label.setText(item.name);

            return returnView;
        }
    }

}
