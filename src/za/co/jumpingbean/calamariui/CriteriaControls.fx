/*
 * CriteriaControls.fx
 *
 * Created on 11-Apr-2010, 11:56:20
 */

package za.co.jumpingbean.calamariui;

import javafx.scene.CustomNode;
import java.lang.UnsupportedOperationException;
import javafx.scene.Node;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.LayoutInfo;
import javafx.scene.control.TextBox;
import org.jfxtras.scene.control.XCalendarPicker;
import java.util.GregorianCalendar;
import java.util.Calendar;
import javafx.scene.layout.VBox;
import javafx.scene.control.Button;
import javafx.geometry.VPos;
import javafx.scene.input.MouseEvent;
import javafx.geometry.HPos;
import org.jfxtras.scene.control.XCalendarPickerSkinLight;
import javafx.animation.Timeline;
import org.jfxtras.scene.control.XPicker;
import org.jfxtras.scene.control.XCalendarPickerSkinControls;
import javafx.scene.layout.Flow;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import org.jfxtras.scene.layout.XSpacer;
import org.jfxtras.scene.layout.Priority;


/**
 * @author mark
 */

public class CriteriaControls extends CustomNode {

    public var startDate:Calendar;
    public var endDate:Calendar;
    public var main:Main;

    override protected function create () : Node {
        var group =
        Group{
                content:[
                          VBox{
                                hpos: HPos.CENTER
                                spacing:10
                                layoutInfo: LayoutInfo{}
                                //vgap: 10
                                //hgap: 250
                                //vertical:true
                                content:[
                                            HBox{
                                                spacing:5
                                                nodeVPos:VPos.CENTER
                                                content:[
                                                        Label{text:"Start Date" font:Font.font("Verdana",FontWeight.BOLD,15) vpos:VPos.CENTER},
                                                        DatePicker{year:startDate.get(Calendar.YEAR); month:startDate.get(Calendar.MONTH); day:startDate.get(Calendar.DATE);}
                                                        XSpacer{w:220 hgrow:Priority.ALWAYS}
                                                        Label{text:"End Date" font:Font.font("Verdana",FontWeight.BOLD,15) vpos:VPos.CENTER},
                                                        DatePicker{year:endDate.get(Calendar.YEAR); month:endDate.get(Calendar.MONTH); day:endDate.get(Calendar.DATE);}
                                                    ]
                                            },
                                            HBox{
                                                    hpos:HPos.CENTER
                                                    content:[
                                                        Button{text:"Get Data"
                                                            onMouseClicked:function(event:MouseEvent){
                                                                println("dates udated - {startDate.toString()} {endDate.toString()}");
                                                                println("starting poller....");
                                                                main.startPoller();
                                                                }
                                                        }
                                                    ]
                                            }
                                        ]
                            }
                     ]
              }
           return group;
    }
}
