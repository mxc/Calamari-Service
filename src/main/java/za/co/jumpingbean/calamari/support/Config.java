/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package za.co.jumpingbean.calamari.support;

/**
 *
 * @author mark
 */
public enum Config {

    LOGFOLDER("squidLogFolder");
    
    
    private String name;
    
    private Config(String name){
        this.name=name;
    }


    public String getName(){
        return name;
    }

}
