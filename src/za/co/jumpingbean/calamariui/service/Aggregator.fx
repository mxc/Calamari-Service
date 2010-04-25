/*
 * Aggregator.fx
 *
 * Created on 18 Apr 2010, 5:39:11 PM
 */

package za.co.jumpingbean.calamariui.service;

import java.util.HashMap;
import za.co.jumpingbean.calamariui.model.SquidLogRecord;
import javafx.reflect.FXLocal;
import javafx.reflect.FXVarMember;
import java.math.BigDecimal;
import java.math.MathContext;
/**
 * @author mark
 */

public class Aggregator {

    public def map:HashMap = new HashMap();
    public var grouping:String[];

    public function getValueSequence():SquidLogRecord[]{
        def records = (map.values());
        for (record in records){
            record as SquidLogRecord;
        }
    }

    public function addRecord(record:SquidLogRecord){
        //var xmap = ReflectionCache.getVariableMap(record.getJFXClass());
        var tmpString:String="";
        //Build our hash key.
        def ctx:FXLocal.Context = FXLocal.getContext();
        def cls:FXLocal.ClassType = ctx.findClass("za.co.jumpingbean.calamariui.model.SquidLogRecord");
        def fxCurrentObj = new FXLocal.ObjectValue(record,cls);
        def newRecord = SquidLogRecord{hits:0; bytes:0, bytesKB:new BigDecimal(0,new MathContext(3))};
        def fxNewRecordObj = new FXLocal.ObjectValue(newRecord,cls);
        for (key in grouping) {
                def variable:FXVarMember = cls.getVariable(Utils.toClassCamelCase(key));
                def fxVal = variable.getValue(fxCurrentObj);
                variable.setValue(fxNewRecordObj,fxVal);
                def stringVal = fxVal.getValueString();
                tmpString = "{tmpString}{stringVal}";
        }

        if (map.get(tmpString)!=null){
            var tmpRecord = (map.get(tmpString) as SquidLogRecord);
            tmpRecord.bytes=tmpRecord.bytes+record.bytes;
            tmpRecord.bytesKB=tmpRecord.bytesKB.add(record.bytesKB);
            tmpRecord.hits=tmpRecord.hits+record.hits;
        }else{
            newRecord.hits=record.hits;
            newRecord.bytesKB=record.bytesKB;
            newRecord.bytesKB=record.bytesKB;
            map.put(tmpString,newRecord);
        }
    }
}

