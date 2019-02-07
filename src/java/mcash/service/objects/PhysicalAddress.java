/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mcash.service.objects;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.Getter;
import lombok.Setter;
/**
 *
 * @author emusa
 */
 @Getter
@XmlRootElement(name = "PhysicalAddress")
public class PhysicalAddress {
    
   @XmlElement(name = "Street")
   private String Street;
   
   @XmlElement(name = "LGA")
   private String LGA;
   
   @XmlElement(name = "State")
   private String State;
}
  