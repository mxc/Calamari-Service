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
public class ImportFile {
    private String fileName;
    private Timestamp importDate;
    private Long checksum;

    /**
     * @return the fileName
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * @param fileName the fileName to set
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**
     * @return the importDate
     */
    public Timestamp getImportDate() {
        return importDate;
    }

    /**
     * @param importDate the importDate to set
     */
    @XmlJavaTypeAdapter( TimestampAdapter.class)
    public void setImportDate(Timestamp importDate) {
        this.importDate = importDate;
    }

    /**
     * @return the checksum
     */
    public Long getChecksum() {
        return checksum;
    }

    /**
     * @param checksum the checksum to set
     */
    public void setChecksum(Long checksum) {
        this.checksum = checksum;
    }
}
