/*
 * AbstractParser.fx
 *
 * Created on 05 Apr 2010, 8:36:56 PM
 */

package za.co.jumpingbean.calamariui.service;

import java.io.InputStream;
import javafx.data.pull.PullParser;
import javafx.data.pull.Event;
import java.lang.Exception;

/**
 * @author mark
 */

public abstract class AbstractParser {
    
    public abstract function onEvent(event:Event):Void;


    public function parse(input:InputStream){
        var parser =PullParser {
            documentType: PullParser.XML;
            input:input
            onEvent: onEvent
        }
        parser.parse();
    }

}
