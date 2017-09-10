package co.vikm.dolarpy.json;

import android.os.Parcel;
import android.os.Parcelable;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

import co.vikm.dolarpy.core.IKRequestManager;

/**
 * Created by ivankoop on 9/9/17.
 */

@JsonObject
public class DolarPyItem extends IKRequestManager implements Parcelable {

    @JsonField
    public String name;

    @JsonField
    public int compra;

    @JsonField
    public int venta;

    public DolarPyItem(){}

    protected DolarPyItem(Parcel in) {
        super(in);
        name = in.readString();
        compra = in.readInt();
        venta = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(name);
        dest.writeInt(compra);
        dest.writeInt(venta);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<DolarPyItem> CREATOR = new Creator<DolarPyItem>() {
        @Override
        public DolarPyItem createFromParcel(Parcel in) {
            return new DolarPyItem(in);
        }

        @Override
        public DolarPyItem[] newArray(int size) {
            return new DolarPyItem[size];
        }
    };
}
