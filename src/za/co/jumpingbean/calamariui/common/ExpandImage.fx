/*
 * ExpandImage.fx
 *
 * Created on 20 Apr 2010, 8:20:59 PM
 */

package za.co.jumpingbean.calamariui.common;

import javafx.scene.CustomNode;
import java.lang.UnsupportedOperationException;
import javafx.scene.Node;
import javafx.scene.shape.Polygon;
import javafx.scene.paint.Color;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.text.Font;
import javafx.scene.shape.Line;


/**
 * @author mark
 */

public class ExpandImage extends CustomNode{

    public var text:String;
    var label:Label;

    override protected function create () : Node {
    HBox{
        spacing:10
        hpos:HPos.CENTER
        vpos:VPos.CENTER
        nodeVPos:VPos.CENTER
        content: [
            Polygon {
                    scaleX:0.5
                    scaleY:0.5
                    points: [142.0,  126.0, 113.0, 108.0, 111.0, 143.0]
                    fill: Color.web("#337799")
            },
            label = Label{
                text:text;
                font:Font{size:12 name: "Arial Bold"}
                textFill:Color.DARKGREEN;
            },
            Line{
                startX:label.boundsInLocal.maxX+10
                startY:label.boundsInLocal.maxY
                endX: boundsInLocal.maxX+650
                endY:label.boundsInLocal.maxY
            }
       ]
   }
  }
}
