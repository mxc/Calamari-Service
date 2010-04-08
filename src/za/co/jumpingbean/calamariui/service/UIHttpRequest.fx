/*
 * UIHttpRequest.fx
 *
 * Created on 05 Apr 2010, 9:10:39 PM
 */

package za.co.jumpingbean.calamariui.service;

import javafx.io.http.HttpRequest;

/**
 * @author mark
 */

public class UIHttpRequest {
    
    public var location:String;
    public var parser:AbstractParser;
    public var onDone: function():Void = null;

    public function start(){
            def request:HttpRequest = HttpRequest{
                    location:location;
                    onDone:onDone;
                    onInput: function(is: java.io.InputStream) {
                     try {
                            parser.parse(is);
                         } finally {
                            is.close();
                        }
                    }
                    method: HttpRequest.GET
                }
            request.start();
        }

}
