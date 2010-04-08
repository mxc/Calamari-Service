/*
 * SquidLogDetail.fx
 *
 * Created on 05 Apr 2010, 8:31:15 PM
 */

package za.co.jumpingbean.calamariui.model;

import java.sql.Timestamp;

/**
 * @author mark
 */

public class SquidLogRecord {
        public var  serverInfo:String;
        public var accessDate:Timestamp ;
        public var elapsed:Integer;
        public var remoteHost:String;
        public var codeStatus:String;
        public var bytes:Integer;
        public var method:String;
        public var parameters:String;
        public var rfc931:String;
        public var peerStatusPeerHost:String;
        public var contentType:String;
        public var domain:String;

        override function toString():String{
            return "doamin:{domain},user:{rfc931},bytes{bytes}";
        }

}
