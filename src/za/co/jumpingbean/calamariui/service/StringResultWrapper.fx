/*
 * StringResultWrapper.fx
 *
 * Created on 24 Apr 2010, 9:18:30 PM
 */

package za.co.jumpingbean.calamariui.service;

/**
 * @author mark
 */

public class StringResultWrapper extends AbstractWrapper{
        public var result:String="";

        override public function reset(){
            super.reset();
            result="";
        }
}
