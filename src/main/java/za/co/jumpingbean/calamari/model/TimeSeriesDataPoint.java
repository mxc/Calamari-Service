/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package za.co.jumpingbean.calamari.model;

import java.sql.Timestamp;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import za.co.jumpingbean.calamari.support.TimestampAdapter;

/**
 *
 * @author mark
 */
@XmlRootElement
public class TimeSeriesDataPoint {
    
        private Timestamp date;
        private Integer value;
        private String name;

    /**
     * @return the date
     */
    public Timestamp getDate() {
        return date;
    }

    /**
     * @param date the date to set
     */
    @XmlJavaTypeAdapter( TimestampAdapter.class)
    public void setDate(Timestamp date) {
        this.date = date;
    }

    /**
     * @return the value
     */
    public Integer getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(Integer value) {
        this.value = value;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }


}
