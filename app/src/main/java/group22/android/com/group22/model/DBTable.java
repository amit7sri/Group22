package group22.android.com.group22.model;

/**
 * Created by amko0l on 3/7/2017.
 */

public class DBTable{
    public static String tablename = "Name_ID_Age_Sex";
    public static final String timestamp = "timestamp";
    public static final String x_values = "xvalues";
    public static final String y_values = "yvalues";
    public static final String z_values = "zvalues";

    public static String getTablename(){
        return tablename;
    }

    public static void setTablename(String name){
        tablename = name;
    }
}
