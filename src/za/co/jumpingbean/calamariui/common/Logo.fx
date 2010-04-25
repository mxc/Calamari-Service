/*
 * Logo.fx
 *
 * Created on 20 Apr 2010, 10:56:23 PM
 */

package za.co.jumpingbean.calamariui.common;

import javafx.scene.CustomNode;
import java.lang.UnsupportedOperationException;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.geometry.HPos;

/**
 * @author mark
 */

public class Logo extends CustomNode {

    public var x:Number;
    public var y:Number;

    override protected function create () : Node {
            ImageView{
                image:Image {url:"{__DIR__}logo.png" }
                layoutX:x;
                layoutY:y;
                scaleX:0.4
                scaleY:0.4

            }
    }


}
