/*
 * DatePicker.fx
 *
 * Created on 11-Apr-2010, 19:32:16
 */

package za.co.jumpingbean.calamariui.common;

import javafx.scene.CustomNode;
import java.lang.UnsupportedOperationException;
import javafx.scene.Node;
import javafx.scene.Group;
import org.jfxtras.scene.control.XPicker;
import org.jfxtras.scene.control.XPickerType;
import java.util.Calendar;
import java.util.GregorianCalendar;
import javafx.scene.layout.HBox;
import javafx.geometry.VPos;
import javafx.scene.layout.LayoutInfo;

/**
 * @author mark
 */

public class DatePicker extends CustomNode{

     var year:Integer;
     var month:Integer;
     var day:Integer;

     public var date:GregorianCalendar;

    //public bound function getDate():GregorianCalendar{
    //     date= new GregorianCalendar(year,month,day);
    // }

    override protected function create () : Node {
       year=date.get(Calendar.YEAR);
       month=date.get(Calendar.MONTH);
       day=date.get(Calendar.DATE);
       var daysInMonth=[1..31];
       Group {
           content: [
                        HBox{
                                spacing:5
                                nodeVPos:VPos.CENTER
                                content:[
                                     XPicker {
                                              pickerType: XPickerType.DROP_DOWN
                                              cyclic: true
                                              layoutInfo: LayoutInfo {
                                                  width: 80
                                              }
                                              items: [2007..2012]
                                              preset: date.get(Calendar.YEAR)-2007;
                                              onIndexChange:function (index:Integer){
                                                  year = 2007+index;
                                                  date.set(Calendar.YEAR,year);
                                              }
                                    },
                                    XPicker {
                                              pickerType: XPickerType.DROP_DOWN
                                              cyclic: true
                                              items: [ "January", "February", "March", "April", "May", "June",
                                                       "July", "August", "September", "October", "November", "December" ]
                                              preset: date.get(Calendar.MONTH);
                                              layoutInfo: LayoutInfo {
                                                  width: 100
                                              }
                                              onIndexChange:function(index:Integer){
                                                  date.set(Calendar.MONTH,index);
                                                  //month = index;
                                                  //var cal:Calendar = Calendar.getInstance();
                                                  //cal.set(Calendar.MONTH,index);
                                                  daysInMonth =[1.. date.getActualMaximum(Calendar.DAY_OF_MONTH)]
                                              }
                                    },
                                    XPicker {
                                              pickerType: XPickerType.DROP_DOWN
                                              cyclic: true
                                              layoutInfo: LayoutInfo {
                                                  width: 60
                                              }
                                              preset: date.get(Calendar.DATE)-1;
                                              items: bind daysInMonth;
                                              onIndexChange:function (index:Integer){
                                                  date.set(Calendar.DATE,index+1);
                                              }
                                    }
                                  ]
                        }
                   ]
       }
    }
}
