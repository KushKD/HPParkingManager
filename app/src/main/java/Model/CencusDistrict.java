package Model;

import java.io.Serializable;

/**
 * Created by kuush on 10/7/2016.
 */
public class CencusDistrict implements Serializable{


    private String Id;

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    private String Name;
}
