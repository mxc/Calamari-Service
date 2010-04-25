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
public class SquidLogRecord {
        private String serverInfo;
        private Timestamp accessDate;
        private Integer elapsed;
        private String remoteHost;
        private String codeStatus;
        private Integer bytes;
        private String method;
        private String parameters;
        private String rfc931;
        private String peerStatusPeerHost;
        private String contentType;
        private String domain;
        private String checksum;

    /**
     * @return the serverInfo
     */
    public String getServerInfo() {
        return serverInfo;
    }

    /**
     * @param serverInfo the serverInfo to set
     */
    public void setServerInfo(String serverInfo) {
        this.serverInfo = serverInfo;
    }

    /**
     * @return the accessDate
     */
    public Timestamp getAccessDate() {
        return accessDate;
    }

    /**
     * @param accessDate the accessDate to set
     */
    @XmlJavaTypeAdapter( TimestampAdapter.class)
    public void setAccessDate(Timestamp accessDate) {
        this.accessDate = accessDate;
    }

    /**
     * @return the elapsed
     */
    public Integer getElapsed() {
        return elapsed;
    }

    /**
     * @param elapsed the elapsed to set
     */
    public void setElapsed(Integer elapsed) {
        this.elapsed = elapsed;
    }

    /**
     * @return the remoteHost
     */
    public String getRemoteHost() {
        return remoteHost;
    }

    /**
     * @param remoteHost the remoteHost to set
     */
    public void setRemoteHost(String remoteHost) {
        this.remoteHost = remoteHost;
    }

    /**
     * @return the codeStatus
     */
    public String getCodeStatus() {
        return codeStatus;
    }

    /**
     * @param codeStatus the codeStatus to set
     */
    public void setCodeStatus(String codeStatus) {
        this.codeStatus = codeStatus;
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

    /**
     * @return the method
     */
    public String getMethod() {
        return method;
    }

    /**
     * @param method the method to set
     */
    public void setMethod(String method) {
        this.method = method;
    }

    /**
     * @return the parameters
     */
    public String getParameters() {
        return parameters;
    }

    /**
     * @param parameters the parameters to set
     */
    public void setParameters(String parameters) {
        this.parameters = parameters;
    }

    /**
     * @return the rfc931
     */
    public String getRfc931() {
        return rfc931;
    }

    /**
     * @param rfc931 the rfc931 to set
     */
    public void setRfc931(String rfc931) {
        this.rfc931 = rfc931;
    }

    /**
     * @return the peerStatusPeerHost
     */
    public String getPeerStatusPeerHost() {
        return peerStatusPeerHost;
    }

    /**
     * @param peerStatusPeerHost the peerStatusPeerHost to set
     */
    public void setPeerStatusPeerHost(String peerStatusPeerHost) {
        this.peerStatusPeerHost = peerStatusPeerHost;
    }

    /**
     * @return the contentType
     */
    public String getContentType() {
        return contentType;
    }

    /**
     * @param contentType the contentType to set
     */
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    /**
     * @return the domain
     */
    public String getDomain() {
        return domain;
    }

    /**
     * @param domain the domain to set
     */
    public void setDomain(String domain) {
        this.domain = domain;
    }

    /**
     * @return the checksum
     */
    public String getChecksum() {
        return checksum;
    }

    /**
     * @param checksum the checksum to set
     */
    public void setChecksum(String checksum) {
        this.checksum = checksum;
    }

}
