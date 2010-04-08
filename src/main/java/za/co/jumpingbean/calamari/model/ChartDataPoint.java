package za.co.jumpingbean.calamari.model;

import javax.xml.bind.annotation.XmlRootElement;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author mark
 */
@XmlRootElement
public class ChartDataPoint {
        private String name;
        private Integer hits;
        private Integer bytes;

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

    /**
     * @return the hits
     */
    public Integer getHits() {
        return hits;
    }

    /**
     * @param hits the hits to set
     */
    public void setHits(Integer hits) {
        this.hits = hits;
    }

    /**
     * @return the bytes
     */
    public Integer getBytes() {
        return bytes;
    }

    /**
     * @param bytes the bytes to set
     */
    public void setBytes(Integer bytes) {
        this.bytes = bytes;
    }
}
