package ist.meic.sirs.securechildlocator;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by GuilhermeM on 27/11/2016.
 */

public class TimeStamp {
    public static String getTime() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MINUTE, +2);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return df.format(c.getTime());
    }
}
