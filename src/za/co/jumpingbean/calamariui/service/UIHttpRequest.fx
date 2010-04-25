/*
 * UIHttpRequest.fx
 *
 * Created on 05 Apr 2010, 9:10:39 PM
 */

package za.co.jumpingbean.calamariui.service;

import javafx.io.http.HttpRequest;
import java.lang.Exception;

/**
 * @author mark
 */

public class UIHttpRequest {
    
    public var location:String;
    public var parser:AbstractParser;
    public var onDone: function():Void = null;
    public var onException:function(ex:Exception):Void=null;
    public def request:HttpRequest = HttpRequest{};

    public function start(){
            request.location=location;
            request.onDone=onDone;
            request.onException=onException;
            request.onInput= function(is: java.io.InputStream) {
                 try {
                        parser.parse(is);
                     } finally {
                        is.close();
                    }
             }
             request.method=HttpRequest.GET;
            request.start();
        }
}
