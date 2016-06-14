package Model;

import java.io.Serializable;

/**
 * Created by kuush on 6/14/2016.
 */
public class Collection_Reports_Pojo implements Serializable {




    public String getTotalCollection() {
        return TotalCollection;
    }

    public void setTotalCollection(String totalCollection) {
        TotalCollection = totalCollection;
    }

    public String getTotalVehicles() {
        return TotalVehicles;
    }

    public void setTotalVehicles(String totalVehicles) {
        TotalVehicles = totalVehicles;
    }

    public String TotalCollection;
    public String TotalVehicles;
}
