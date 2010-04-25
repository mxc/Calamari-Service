/*
 * TabularDataDisplay.fx
 *
 * Created on 18 Apr 2010, 11:14:01 AM
 */

package za.co.jumpingbean.calamariui.tabularDisplay;

import javafx.scene.CustomNode;
import javafx.scene.Node;
import za.co.jumpingbean.calamariui.service.DataService;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import za.co.jumpingbean.calamariui.service.SquidLogRecordListWrapper;
import java.util.GregorianCalendar;
import javafx.scene.layout.VBox;
import javafx.geometry.HPos;
import javafx.util.Sequences;
import javafx.scene.layout.HBox;
import org.jfxtras.ext.swing.XSwingTable;
import org.jfxtras.ext.swing.table.ObjectSequenceTableModel;
import org.jfxtras.ext.swing.table.Row;
import org.jfxtras.ext.swing.table.DateCell;
import za.co.jumpingbean.calamariui.model.SquidLogRecord;
import org.jfxtras.ext.swing.table.StringCell;
import org.jfxtras.ext.swing.table.IntegerCell;
import org.jfxtras.ext.swing.table.Cell;
import java.awt.event.MouseAdapter;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableColumn;
import java.util.Comparator;
import javax.swing.table.JTableHeader;
import java.awt.event.MouseEvent;
import za.co.jumpingbean.calamariui.service.Utils;
import javafx.reflect.FXLocal;
import javafx.reflect.FXValue;
import javafx.scene.control.Label;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.geometry.VPos;
import org.jfxtras.ext.swing.table.BigDecimalCell;
import java.math.BigDecimal;
import za.co.jumpingbean.calamariui.common.DataLoadingIndicator;
import za.co.jumpingbean.calamariui.common.Poller;
import za.co.jumpingbean.calamariui.common.Logo;
import za.co.jumpingbean.calamariui.common.DateCriteriaControls;
import org.jfxtras.util.StringUtil;
import za.co.jumpingbean.calamariui.common.DisplaySelector;
import za.co.jumpingbean.calamariui.common.ErrorLabel;
import javafx.scene.paint.Color;

/**
 * @author mark
 */


public def reportUserDetail="userDetail";
public def reportDomainDetail="domainDetail";
public def reportContentTypeDetail="contentTypeDetail";
public def reportAll="details";



public class TabularDisplay extends CustomNode,Poller{

def service:DataService= DataService{};
public-read def logEntries = SquidLogRecordListWrapper{};//reference to untransformed data
public var tableData:SquidLogRecord[]; //the data to dispaly - may or may not be transformed
public var startDate:GregorianCalendar;// holds data start date
public var endDate:GregorianCalendar;// holds data end date
var sortOrderFlag:Boolean = true; // flag to change sort order of columns (asc/desc)
public var showingAggregate:Boolean=false;
var vbox:VBox; //reference to vbox in header for insertion of data loading
def dataLoadingIndicator=DataLoadingIndicator{};
var display:VBox; //for access to the scene graph
public var width:Number;
public var height:Number;
public var reportType:String;//set to determine the report to collect data for.
public var reportParameter:String;//prarmeter placeholder
var reportTitle:String;//placeholder for title
var controls:HBox;//rference to controls hbox to get height.
var errorLabel:Label;

//this is the list of columns to hide by default
public var hideColumns:String[]=["elapsed","parameters","serverInfo","peerStatusPeerHost","method","codeStatus","hits","accessDate","bytesKB"] on replace{
            table.tableModel=getModel();
};


//Determines when we can stop the poller timeline object.
var pollerDone=false on replace oldvalue{
            if (pollerDone==true){
                tabularPoller.stop();
                this.tableData=logEntries.list;
                stopIndicator();
                println("stoping poller...");
                var errorMessage;
                if (logEntries.errorMessage.length()>50)
                    errorMessage="{logEntries.errorMessage.substring(0,50)}..."
                else errorMessage=logEntries.errorMessage;
                if (logEntries.error) insertErrorMessage(errorMessage);
            }
};


def tabularPoller = Timeline{
    repeatCount: Timeline.INDEFINITE
    keyFrames: [
      KeyFrame{
                time: 1s
                canSkip:true;
                action: function(){
                   println("polling ....");
                    if (not logEntries.processing) {
                            if (reportType==reportUserDetail) service.getUserDetails(startDate, endDate,reportParameter,logEntries);
                            if (reportType==reportDomainDetail) service.getDomainDetails(startDate, endDate,reportParameter,logEntries);
                            if (reportType==reportContentTypeDetail) service.getContentTypeDetails(startDate, endDate,reportParameter,logEntries);
                            if (reportType==reportAll) service.getDetails(startDate, endDate,logEntries);
                            logEntries.processing=true;
                     } else if (logEntries.done){
                                pollerDone = true;
                    }
                }
              }
    ]
}

def table:XSwingTable = XSwingTable{
       width:bind width;
       height:bind height-controls.height-100//make sure not off scene
       tableModel: getModel();

}


//this function is to avoid compile time errors related to bound functions and is called by getTableModel()
public function getModel(){
        var model = ObjectSequenceTableModel{
                   override public function transformEntry (entry : Object) : Row {
                        var record = entry as SquidLogRecord;
                        var cells:Cell[];
                        if (Sequences.indexOf(hideColumns,"accessDate")==-1) insert { StringCell { value: Utils.getDateFromTimestamp(record.accessDate).toString(); editable:false }} into cells;
                        if (Sequences.indexOf(hideColumns,"serverInfo")==-1) insert { StringCell { value: record.serverInfo editable:false}}  into cells;
                        if (Sequences.indexOf(hideColumns,"elapsed")==-1) insert { IntegerCell { value: record.elapsed editable:false}}  into cells;
                        if (Sequences.indexOf(hideColumns,"remoteHost")==-1)insert { StringCell { value: record.remoteHost editable:false}}  into cells;
                        if (Sequences.indexOf(hideColumns,"codeStatus")==-1) insert { StringCell {value: record.codeStatus editable:false}}  into cells;
                        if (Sequences.indexOf(hideColumns,"bytes")==-1) insert { IntegerCell{value:record.bytes editable:false}}  into cells;
                        if (Sequences.indexOf(hideColumns,"bytesKB")==-1) insert {BigDecimalCell{value:record.bytesKB editable:false}}  into cells;
                        if (Sequences.indexOf(hideColumns,"method")==-1) insert { StringCell {value:record.method editable:false}}  into cells;
                        if (Sequences.indexOf(hideColumns,"parameters")==-1) insert { StringCell {value:record.parameters editable:false}}  into cells;
                        if (Sequences.indexOf(hideColumns,"rfc931")==-1) insert { StringCell {value:record.rfc931 editable:false}}  into cells;
                        if (Sequences.indexOf(hideColumns,"peerStatusPeerHost")==-1) insert { StringCell {value:record.peerStatusPeerHost editable:false}}  into cells;
                        if (Sequences.indexOf(hideColumns,"contentType")==-1) insert { StringCell {value:record.contentType editable:false}}  into cells;
                        if (Sequences.indexOf(hideColumns,"domain")==-1) insert { StringCell {value:record.domain editable:false}}  into cells;
                        if (Sequences.indexOf(hideColumns,"hits")==-1) insert { IntegerCell {value:record.hits editable:false}}  into cells;
                        var row = Row{ cells: cells};
                        return row;
                   }
                   sequence: bind tableData;
                   columnLabels: getLabels();
                   }
        return model;
}

function getLabels():String[]{
                var columnHeaders:String[];
                if (Sequences.indexOf(hideColumns,"accessDate")==-1) insert "Access Date" into columnHeaders;
                if (Sequences.indexOf(hideColumns,"serverInfo")==-1) insert "Server Info"  into columnHeaders;
                if (Sequences.indexOf(hideColumns,"elapsed")==-1) insert "Elapsed"  into columnHeaders;
                if (Sequences.indexOf(hideColumns,"remoteHost")==-1)insert "Remote Host"  into columnHeaders;
                if (Sequences.indexOf(hideColumns,"codeStatus")==-1) insert "Code Status" into columnHeaders;
                if (Sequences.indexOf(hideColumns,"bytes")==-1) insert "Bytes"  into columnHeaders;
                if (Sequences.indexOf(hideColumns,"bytesKB")==-1) insert "KiloBytes" into columnHeaders;
                if (Sequences.indexOf(hideColumns,"method")==-1) insert "Method"  into columnHeaders;
                if (Sequences.indexOf(hideColumns,"parameters")==-1) insert "Parameters"  into columnHeaders;
                if (Sequences.indexOf(hideColumns,"rfc931")==-1) insert "User"  into columnHeaders;
                if (Sequences.indexOf(hideColumns,"peerStatusPeerHost")==-1) insert "Peer Status Peer Host"  into columnHeaders;
                if (Sequences.indexOf(hideColumns,"contentType")==-1) insert "Content Type"  into columnHeaders;
                if (Sequences.indexOf(hideColumns,"domain")==-1) insert "Domain"  into columnHeaders;
                if (Sequences.indexOf(hideColumns,"hits")==-1) insert "Hits"  into columnHeaders;
                return columnHeaders
}


function insertErrorMessage(message:String){
    errorLabel=Label{text:message textFill:Color.RED font:Font.font("Verdana",FontWeight.BOLD,15)};
    insert errorLabel into vbox.content;
}

function removeErrorMessage(){
    delete errorLabel from vbox.content
}

public function insertDisplaySelector(displaySelector:DisplaySelector){
    insert displaySelector after vbox.content[4]
}

public function removeDisplaySelector(displaySelector:DisplaySelector){
    delete displaySelector from vbox.content
}

override protected function create () : Node {

        var header:JTableHeader = table.getJTable().getTableHeader();
        header.addMouseListener(columnSorter{ table:table});
         display = VBox{
                nodeHPos:HPos.LEFT
                spacing:20
                content:[
                 controls=HBox{
                     spacing:20
                     content: [
                                Logo{},
                          vbox= VBox{
                                spacing:10
                                nodeHPos:HPos.CENTER;
                                content:[
                                DateCriteriaControls{
                                    startDate: bind startDate with inverse
                                    endDate:bind endDate with inverse
                                    display:this
                                },
                                ReportSelectorControl{
                                    tableDisplay:this
                                }
                                ShowHideColumnControl{
                                    tableDisplay: this
                                },
                                AggregateControl{
                                    tableDisplay:this
                                },
                                //errorLabel
                                //DisplaySelector{ default:DisplaySelector.tableDisplay},
                                ]
                                }
                    ]
                    },
                    //VBox{
                    //     nodeHPos:HPos.CENTER
                    //     width:bind width
                    //     content:[
                    //      HBox{
                    //           spacing:10
                    //           content:[
                                          Label{text: bind reportTitle  font:Font.font("Verdana",FontWeight.BOLD,15) vpos:VPos.CENTER},
                                          Label{text: bind getRecordCount() font:Font.font("Verdana",FontWeight.BOLD,12) vpos:VPos.CENTER},
                      //                 ]
                      //  }
                      //  ]
                   // }
                    table
                    ]
        }
}

//Get record to display above table
bound function getRecordCount(){
    return "Total Records: {sizeof tableData}"
}

//This function is called to start the poller when new data is fetched from server
override public function startPoller(){
                pollerDone=false;
                logEntries.reset();
                tableData=[];
                removeErrorMessage();
                startIndicator(DataLoadingIndicator.LOADING);
                dataLoadingIndicator.text=dataLoadingIndicator.LOADING;
                tabularPoller.play();
}

public function startIndicator(text:String){
         reportTitle="{StringUtil.camelToTitleCase(reportType)} from {Utils.formatDatePrettyPrint(startDate)} to {Utils.formatDatePrettyPrint(endDate)} for parameter {reportParameter}";
         dataLoadingIndicator.text=text;
         dataLoadingIndicator.start();
         insert dataLoadingIndicator after vbox.content[4];
}


public  function stopIndicator(){
            dataLoadingIndicator.stop();
            delete dataLoadingIndicator from vbox.content;
    }
}

//Handle sorting of columns
class  columnSorter extends MouseAdapter{

    var table:XSwingTable;
    var display:TabularDisplay;

    override public function mouseClicked(event:MouseEvent):Void {
         display.startIndicator(DataLoadingIndicator.SORTING);
         
         def colModel:TableColumnModel = table.getJTable().getColumnModel();
         def columnModelIndex = colModel.getColumnIndexAtX(event.getX());
         def column:TableColumn = colModel.getColumn(columnModelIndex);
         var sortColumn = Utils.toClassCamelCase(column.getHeaderValue().toString());
         if (sortColumn=="kiloBytes") {
                 sortColumn="bytesKB";
         }//not lekker
         else if (sortColumn=="user") sortColumn="rfc931";//also not lekker
         var data = Sequences.sort(tableData,Comparator{
             override public function equals (item: Object) : Boolean {
                    if (item==null) return false;
                    if (item==this) return true;
                    return false;
             }
             //Use refelction to retrieve value. 
             override public function compare (item1 : Object, item2 : Object) : Integer {
                def ctx:FXLocal.Context = FXLocal.getContext();
                def cls:FXLocal.ClassType = ctx.findClass("za.co.jumpingbean.calamariui.model.SquidLogRecord");
                def fxItem1 = new FXLocal.ObjectValue(item1,cls);
                def fxItem2 = new FXLocal.ObjectValue(item2,cls);
                def variable = cls.getVariable(Utils.toClassCamelCase(sortColumn));
                def fxVal1:FXValue = variable.getValue(fxItem1);
                def fxVal2:FXValue = variable.getValue(fxItem2);
                 if (sortColumn=="hits" or sortColumn=="bytes" or sortColumn=="elapsed"){
                            def tmpItem1 = Integer.parseInt(fxVal1.getValueString());
                            def tmpItem2 = Integer.parseInt(fxVal2.getValueString());
                            return tmpItem1.compareTo(tmpItem2);
                 } else if (sortColumn=="bytesKB"){
                            def tmpItem1 = new BigDecimal(fxVal1.getValueString());
                            def tmpItem2 = new BigDecimal(fxVal2.getValueString());
                            return tmpItem1.compareTo(tmpItem2);
                 }
                 else if (sortColumn=="accessDate"){
                            println(fxVal1.getValueString());
                            def tmpItem1 = Utils.toDate(fxVal1.getValueString());
                            def tmpItem2 = Utils.toDate(fxVal2.getValueString());
                            return tmpItem1.compareTo(tmpItem2);
                 }else{
                            def tmpItem1 = fxVal1.getValueString();
                            def tmpItem2 = fxVal2.getValueString();
                            return tmpItem1.compareTo(tmpItem2);
                 }
              }
        }) as SquidLogRecord[];
        //Bit of a hack to change sort order. User will need to click on column to twice potentially to get required order
        //asc or desc. Avoids having to track previous order for each column.
        if (sortOrderFlag){
            sortOrderFlag=false;
            tableData = reverse data;
        }else{
            sortOrderFlag=true;
            tableData = data;
        }
        display.stopIndicator();
    }
}

