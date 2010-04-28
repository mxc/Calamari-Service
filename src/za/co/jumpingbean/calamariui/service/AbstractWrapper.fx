/*
 * AbstractWrapper.fx
 *
 * Created on 25 Apr 2010, 8:28:18 AM
 */

package za.co.jumpingbean.calamariui.service;

/**
 * @author mark
 */

public class AbstractWrapper {
        public var done:Boolean=false;
        public var error:Boolean=false;
        public var errorMessage:String="";
        public var processing=false;

        public function reset():Void{
            done=false;
            error=false;
            processing=false;
            errorMessage="";
        }

}
