/*
 * ShowHideColumnControl.fx
 *
 * Created on 18 Apr 2010, 2:07:27 PM
 */

package za.co.jumpingbean.calamariui.tabularDisplay;

import javafx.scene.CustomNode;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.VBox;
import javafx.geometry.HPos;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
import javafx.util.Sequences;
import org.jfxtras.reflect.ReflectionCache;
import za.co.jumpingbean.calamariui.model.SquidLogRecord;
import za.co.jumpingbean.calamariui.common.ExpandImage;
import javafx.scene.layout.Flow;


/**
 * @author mark
 */

public class ShowHideColumnControl extends CustomNode{

    public var tableDisplay:TabularDisplay;

    override protected function create () : Node {
        var controls =VBox{ nodeHPos:HPos.LEFT spacing:10};
        var hbox = Flow{width: bind tableDisplay.width-350 hgap:10 vgap:10};
        def variables = ReflectionCache.getVariableSequence(SquidLogRecord{}.getJFXClass());
        for (variable in variables){
            var checkbox:CheckBox = CheckBox{
                text:variable.getName();
                onMouseClicked:function(event:MouseEvent){
                     //if we are in aggregate mode then ignore!
                     if (tableDisplay.showingAggregate) {
                           return;
                           //tableDisplay.tableData=tableDisplay.logEntries.list;
                           //tableDisplay.showingAggregate=false;
                    }
                   if (checkbox.selected==true){
                       if (Sequences.indexOf(tableDisplay.hideColumns,checkbox.text)!=-1){
                            delete checkbox.text from tableDisplay.hideColumns;
                       }
                   } else{
                           insert checkbox.text into tableDisplay.hideColumns;
                    }
                 }
                }
            if (Sequences.indexOf(tableDisplay.hideColumns,variable.getName())==-1){
                    checkbox.selected=true;
            }else {
                    checkbox.selected=false;
            }
            insert checkbox into hbox.content;
        };
        //insert label into controls.content;
        insert hbox into controls.content;


        var parent:VBox;//placeholder to enable easy access to content graph
        var group = Group{
                content:[
                         parent=VBox{
                                nodeHPos:HPos.LEFT
                                spacing:10
                                content: [
                                          ExpandImage{
                                            text:"Show/Hide Columns"
                                            onMouseClicked:function (mouseEvent:MouseEvent){
                                            if (Sequences.indexOf(parent.content,controls)==-1){
                                                mouseEvent.source.rotate=90.0;
                                                insert controls into parent.content;
                                            }else{
                                                mouseEvent.source.rotate=0.0;
                                                delete controls from parent.content;
                                            }
                                          }
                                          },
                                        ]
                            }
                     ]
              }
        return group;
    }
}
