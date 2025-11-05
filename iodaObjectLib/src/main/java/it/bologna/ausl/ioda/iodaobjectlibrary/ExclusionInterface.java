/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.bologna.ausl.ioda.iodaobjectlibrary;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 *
 * @author gdm
 */

public interface ExclusionInterface {
    
    @JsonIgnore
    public void setTrue(boolean booleanValue);
    @JsonIgnore
    public void setFalse(boolean booleanValue);
    @JsonIgnore
    public Boolean getTrue();
    @JsonIgnore
    public Boolean getFalse();
    
    @JsonIgnore
    public void setTrue(Boolean booleanValue);
    @JsonIgnore
    public void setFalse(Boolean booleanValue);
}
