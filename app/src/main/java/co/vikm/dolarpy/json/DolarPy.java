package co.vikm.dolarpy.json;

import android.os.Parcel;
import android.os.Parcelable;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

import java.util.List;

import co.vikm.dolarpy.core.IKRequestManager;

/**
 * Created by ivankoop on 9/9/17.
 */
@JsonObject
public class DolarPy extends IKRequestManager implements Parcelable {

    @JsonField
    public List<DolarPyItem> dolarPy;

    public DolarPy(){}

    protected DolarPy(Parcel in) {
        super(in);
        dolarPy = in.createTypedArrayList(DolarPyItem.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeTypedList(dolarPy);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<DolarPy> CREATOR = new Creator<DolarPy>() {
        @Override
        public DolarPy createFromParcel(Parcel in) {
            return new DolarPy(in);
        }

        @Override
        public DolarPy[] newArray(int size) {
            return new DolarPy[size];
        }
    };
}
