/*
 * AggregateControl.fx
 *
 * Created on 18 Apr 2010, 5:29:10 PM
 */

package za.co.jumpingbean.calamariui.tabularDisplay;

import javafx.scene.CustomNode;
import javafx.scene.Node;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import za.co.jumpingbean.calamariui.service.Aggregator;
import javafx.util.Sequences;
import javafx.geometry.HPos;
import javafx.scene.Group;
import org.jfxtras.reflect.ReflectionCache;
import za.co.jumpingbean.calamariui.model.SquidLogRecord;
import javafx.scene.layout.Flow;
import za.co.jumpingbean.calamariui.common.ExpandImage;
import javafx.geometry.VPos;
import za.co.jumpingbean.calamariui.common.DataLoadingIndicator;


/**
 * The aggregation control.
 * @author mark
 */

public class AggregateControl extends CustomNode {
    //public-init var table:XSwingTable;
    public-init var tableDisplay:TabularDisplay;

    var list:CheckBox[];

    override protected function create () : Node {
        
        //build a list of all possible variable names.
        var columns:String[];
        def map =ReflectionCache.getVariableMap(SquidLogRecord{}.getJFXClass());
        def tmpColumnVars = map.getKeys();
        for (tmpVar in tmpColumnVars){
            //skip the measures which make no sense to aggregate on
            if (tmpVar=="hits" or tmpVar=="bytes" or tmpVar=="bytesKB") continue;
            insert tmpVar as String into columns;
        }

        var checkboxButtonsContainer =VBox{ spacing:10 nodeHPos:HPos.CENTER vpos:VPos.CENTER}//overall layout container for checkboxes and buttons
        var checkboxBox = Flow{ width: bind tableDisplay.width-350 hgap:10 vgap:10}; //layout for the checkboxes
        var btnGetData:Button = Button{
            text:"Get Summary"
            onMouseClicked:function (event:MouseEvent){
                    this.tableDisplay.startIndicator(DataLoadingIndicator.AGGREGATING);
                    //Get selected aggregate columns.
                    def agg= Aggregator{};
                    for (tmpCheckbox in list){
                        if (tmpCheckbox.selected){
                              insert tmpCheckbox.id into agg.grouping;
                        }
                    }
                    // Check for 0 result. If 0 then aggregations have
                    //been cleared and therefore show detail again.
                    if (sizeof agg.grouping !=0 and agg.grouping!=null){
                        for (data in tableDisplay.logEntries.list){
                            agg.addRecord(data);
                        }
                        //display.scene
                        //Hide columns that are'nt part of the aggregation.
                        tableDisplay.showingAggregate=true;
                        tableDisplay.hideColumns = columns;
                        for (tmp in agg.grouping) delete tmp from tableDisplay.hideColumns;
                        tableDisplay.tableData=agg.getValueSequence();
                    }else{
                        tableDisplay.showingAggregate=false;
                        tableDisplay.hideColumns=["elapsed","parameters","serverInfo","peerStatusPeerHost","method","codeStatus","hits","accessDate","bytesKB"];
                        tableDisplay.tableData=tableDisplay.logEntries.list;
                    }
                    tableDisplay.stopIndicator();
                }
        }
        
        var btnClear = Button{
            text:"Clear"
            onMouseClicked:function (event:MouseEvent){
                    for (tmpCheckbox in list){
                        tmpCheckbox.selected=false;
                    }
                    tableDisplay.showingAggregate=false;
                    tableDisplay.hideColumns=["elapsed","parameters","serverInfo","peerStatusPeerHost","method","codeStatus","hits","accessDate","bytesKB"];
                    tableDisplay.tableData=tableDisplay.logEntries.list;
            }
        };

        var buttonGroup = HBox{
            spacing:10
            content:[
                            btnGetData,
                            btnClear
                    ]
        };

        //Create check boxes
        for (column in columns){
            if (column!="hits" and column!="bytes" and column!="elapsed"){
            var checkbox = CheckBox{
                text:column;
                id:column
            }
            insert checkbox into list;
            insert checkbox into checkboxBox.content;
            }
        };

        //insert label into controls.content;
        insert checkboxBox into checkboxButtonsContainer.content;
        insert buttonGroup into checkboxButtonsContainer.content;


        var parent:VBox;//placeholder to enable easy access to content graph
        var group = Group{
                content:[
                         parent=VBox{
                                nodeHPos:HPos.LEFT
                                spacing:10
                                content: [
                                          ExpandImage{
                                            text:"Select Summary Level"
                                            onMouseClicked:function (mouseEvent:MouseEvent){
                                            if (Sequences.indexOf(parent.content,checkboxButtonsContainer)==-1){
                                                mouseEvent.source.rotate=90.0;
                                                insert checkboxButtonsContainer into parent.content;
                                            }else{
                                                mouseEvent.source.rotate=0.0;
                                                delete checkboxButtonsContainer from parent.content;
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
