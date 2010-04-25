/*
 * SquidLofRecordListWrapper.fx
 *
 * Created on 18 Apr 2010, 10:22:28 AM
 */

package za.co.jumpingbean.calamariui.service;

import za.co.jumpingbean.calamariui.model.SquidLogRecord;


/**
 * @author mark
 */

public class SquidLogRecordListWrapper  extends AbstractWrapper{
        public var list:SquidLogRecord[];

        override public function reset():Void{
                super.reset();
                list=[];
        }
}
