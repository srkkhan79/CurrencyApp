package currency.recognize.currencyrecog;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 08/03/2018.
 */

public class validationClass {
    String inputtext;

    public validationClass(String inputtext) {
        this.inputtext = inputtext;
    }
    public boolean checkemail()
    {
        String Emaiilid="^([\\w\\.\\-]+)@([\\w\\-]+)((\\.(\\w){2,3})+)$";
        Matcher Match= Pattern.compile(Emaiilid).matcher(inputtext);
        if(Match.matches()) {
            return true;
        }
        else
        {
            return  false;
        }
    }
    public boolean checkmobileno()
    {
        String regularexpress="^([7-9]{1})([0-9]{9})$";
        Matcher Match= Pattern.compile(regularexpress).matcher(inputtext);
        if(Match.matches()) {
            return true;
        }
        else
        {
            return  false;
        }
    }
}
