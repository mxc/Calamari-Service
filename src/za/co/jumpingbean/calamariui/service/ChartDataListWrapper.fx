/*
 * ListWrapper.fx
 *
 * Created on 07 Apr 2010, 7:39:12 PM
 */

package za.co.jumpingbean.calamariui.service;

import javafx.scene.chart.PieChart;


/**
 * @author mark
 */

public class ChartDataListWrapper extends AbstractWrapper{
        public var list:PieChart.Data[];

       override public function reset():Void{
            super.reset();
            list=[];
        }
}
