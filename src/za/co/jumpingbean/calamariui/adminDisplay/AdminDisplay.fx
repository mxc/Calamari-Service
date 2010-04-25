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

/**
 * @author mark
 */

public class AdminDisplay extends CustomNode{

    //var location:String;
    def service=DataService{};
    public var displaySelector:DisplaySelector;
    var vbox:VBox;
    var result:StringResultWrapper=StringResultWrapper{result:""};
    var txtLogFileLocation:TextBox;

public function insertDisplaySelector(displaySelector:DisplaySelector){
    insert displaySelector after vbox.content[1]
}

public function removeDisplaySelector(displaySelector:DisplaySelector){
    delete displaySelector from vbox.content
}

    function getAdminInfo(){
        service.getAdminData("admin/settings/squidlogfolder",null,result);
    }

    function saveLogFileLocation(){
        service.saveAdminData(txtLogFileLocation.text,result)
    }


    override protected function create () : Node {
        getAdminInfo();
        def lblLogFileLocation=Label{
            text:"Log File Folder"
        }

        txtLogFileLocation = TextBox{
            text:bind result.result with inverse;
            columns:45
        }

        def btnSave = Button{
            text:"Save Folder"
            onMouseClicked:function(event:MouseEvent){
                saveLogFileLocation();
            }

        }

        def btnImport = Button{
            text:"Import Data"
        }

        var inputContainer = HBox{
            spacing:10
            content: [
                        lblLogFileLocation,
                        txtLogFileLocation,
                        btnSave
                    ]
        }

        HBox{
                content:[
                            Logo{}
                            vbox =VBox{
                                nodeHPos:HPos.CENTER;
                                content: [
                                            inputContainer,
                                            btnImport,
                                            //DisplaySelector{default:DisplaySelector.adminDisplay},
                                        ]
                            }
                ]
        }
    }
}

