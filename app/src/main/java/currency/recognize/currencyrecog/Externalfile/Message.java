package currency.recognize.currencyrecog.Externalfile;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 25/03/2018.
 */

public class Message {
    String from,type,message;
    Date date;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Message(String from, String type, String message, String datetime) {
        this.from = from;
        this.type = type;
        this.message = message;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            this.date=sdf.parse(datetime);
        } catch (ParseException e) {
            Log.i("ASK",e.getMessage());
        }

    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
