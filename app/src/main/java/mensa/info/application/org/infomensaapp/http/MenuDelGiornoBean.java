package mensa.info.application.org.infomensaapp.http;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Giuseppe Grosso on 12/11/15.
 */
public class MenuDelGiornoBean implements Parcelable
{

    private String symbol;
    private double askValue;
    private double bidValue;

    public MenuDelGiornoBean() {}

    public MenuDelGiornoBean(String symbol, double askValue, double bidValue) {
        this.symbol = symbol;
        this.askValue = askValue;
        this.bidValue = bidValue;
    }

    // get and set method

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        // We write the stock information in the parcel
        dest.writeString(symbol);
        dest.writeDouble(askValue);
        dest.writeDouble(bidValue);
    }

    public static final Creator<MenuDelGiornoBean> CREATOR = new Creator<MenuDelGiornoBean>() {

        @Override
        public MenuDelGiornoBean createFromParcel(Parcel source) {
            MenuDelGiornoBean bean = new MenuDelGiornoBean();
            bean.setSymbol(source.readString());
            bean.setAskValue(source.readDouble());
            bean.setBidValue(source.readDouble());
            return bean;
        }

        @Override
        public MenuDelGiornoBean[] newArray(int size) {
            return new MenuDelGiornoBean[0];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    public String getSymbol()
    {
        return symbol;
    }

    public void setSymbol(String symbol)
    {
        this.symbol = symbol;
    }

    public double getAskValue()
    {
        return askValue;
    }

    public void setAskValue(double askValue)
    {
        this.askValue = askValue;
    }

    public double getBidValue()
    {
        return bidValue;
    }

    public void setBidValue(double bidValue)
    {
        this.bidValue = bidValue;
    }

}