/*
 * ErrorLabel.fx
 *
 * Created on 25 Apr 2010, 9:09:36 AM
 */

package za.co.jumpingbean.calamariui.common;

import javafx.scene.CustomNode;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.geometry.VPos;

/**
 * @author mark
 */

public class ErrorLabel extends CustomNode{

    var label:Label;
    
    override protected function create () : Node {
        label = Label{
            text: "";
            font:Font.font("Verdana",FontWeight.BOLD,15) vpos:VPos.CENTER
        }
    }

    public function updateErrorMessage(msg:String){
        label.text=msg;
    }

    //public var errorMessage:String;


}
