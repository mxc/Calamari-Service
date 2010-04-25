/*
 * DataLoading.fx
 *
 * Created on 17 Apr 2010, 2:24:19 PM
 */

package za.co.jumpingbean.calamariui.common;

import javafx.scene.CustomNode;
import javafx.scene.Node;
import javafx.scene.Group;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.HBox;
import javafx.scene.control.Label;
import javafx.scene.effect.Glow;
import javafx.animation.Timeline;
import javafx.animation.Interpolator;

/**
 * @author mark
 */

    public def LOADING:String="LOADING DATA ....";
    public def SORTING:String="SORTING DATA ....";

public class DataLoadingIndicator extends CustomNode{

    var glow:Float;

    public var text:String=LOADING;

    def blurAnim = Timeline {
        repeatCount: Timeline.INDEFINITE
        autoReverse: true
        keyFrames: [
            at(0s) { glow => 0.0 }
            at(0.6s) { glow => 1.0 tween Interpolator.EASEBOTH }
        ]
    };

    public function start(){
       blurAnim.play();
    }

    public function stop(){
       blurAnim.stop();
    }

    override protected function create () : Node {
        Group{
            content: [
                        HBox{
                        content:[
                                ProgressIndicator {
                                    progress: -1
                                },
                                Label{
                                    text:bind text
                                    effect:Glow{
                                      level:bind glow
                                    }

                                }

                        ]
                        }
             ]
        }
    }
}
