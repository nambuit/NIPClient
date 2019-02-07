/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mcash.service.objects;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;
import lombok.Getter;
import lombok.Setter;
/**
 *
 * @author dogor-Igbosuah
 */

@Getter @Setter
@XmlRootElement(name = "Param")
public class Param {
 
    @XmlAttribute(name = "name")
    private String Name;
    
    @XmlAttribute(name = "description")
    private String description;
    
    @XmlValue
    private String Param;
    
    
}
