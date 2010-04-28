/*
 * AdminDisplay.fx
 *
 * Created on 24 Apr 2010, 1:55:36 PM
 */

package za.co.jumpingbean.calamariui.adminDisplay;

import javafx.scene.CustomNode;
import javafx.scene.Node;
import javafx.scene.control.TextBox;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import za.co.jumpingbean.calamariui.service.DataService;
import org.jfxtras.util.XMap;
import za.co.jumpingbean.calamariui.common.DisplaySelector;
import za.co.jumpingbean.calamariui.common.Logo;
import za.co.jumpingbean.calamariui.service.StringResultWrapper;
import javafx.geometry.HPos;
import javafx.scene.input.MouseEvent;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.scene.text.Font;
import javafx.scene.paint.Color;

/**
 * @author mark
 */

public class AdminDisplay extends CustomNode{

    //var location:String;
    def service=DataService{};
    public var displaySelector:DisplaySelector;
    var vbox:VBox;
    var squidLogFileLocationResult:StringResultWrapper=StringResultWrapper{result:""};
    var importFileResult:StringResultWrapper=StringResultWrapper{
            override var result="No import under way" on replace {
                if(result=="no import in progress")  {
                        adminPoller.stop();
                }
            };
    }
    var txtLogFileLocation:TextBox;

public function insertDisplaySelector(displaySelector:DisplaySelector){
    insert displaySelector after vbox.content[1]
}

public function removeDisplaySelector(displaySelector:DisplaySelector){
    delete displaySelector from vbox.content
}

    function getAdminInfo(){
        service.getAdminData("admin/settings/squidlogfolder",null,squidLogFileLocationResult);
    }

    function saveLogFileLocation(){
        service.saveAdminData(txtLogFileLocation.text,squidLogFileLocationResult)
    }

def adminPoller = Timeline{
    repeatCount: Timeline.INDEFINITE
    keyFrames: [
      KeyFrame{
                time: 20s
                canSkip:true;
                action: function(){
                   println("polling ....");
                            service.getImportStatus(importFileResult);
                   }
                }
    ]
}

    override protected function create () : Node {
        getAdminInfo();//populate text box
        def lblLogFileLocation=Label{
            text:"Log File Folder"
            font:Font{size:12 name: "Arial Bold"}
            textFill:Color.DARKGREEN;
    }

        txtLogFileLocation = TextBox{
            text:bind squidLogFileLocationResult.result with inverse;
            columns:45
        }

        def btnSave = Button{
            text:"Save Folder"
            onMouseClicked:function(event:MouseEvent){
                saveLogFileLocation();
            }
        }

        def logFileControls = HBox{
            spacing:10
            content: [
                        lblLogFileLocation,
                        txtLogFileLocation,
                        btnSave
                    ]
        }

        def btnImport:Button = Button{
            text:"Import Data"
            onMouseClicked:function(event:MouseEvent){
                service.startImport(importFileResult);
                adminPoller.play();
            }
        }

        def lblImportResult=Label{ text:bind importFileResult.result font:Font{size:12 name: "Arial Bold"} textFill:Color.DARKGREEN;};


        def importControls=HBox{
            spacing:10
            content: [
                       btnImport,
                       lblImportResult
                    ]
        }


        HBox{
                content:[
                            Logo{}
                            vbox =VBox{
                                nodeHPos:HPos.CENTER;
                                content: [
                                            logFileControls,
                                            importControls,
                                            //DisplaySelector{default:DisplaySelector.adminDisplay},
                                        ]
                            }
                ]
        }
    }
}

