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
 * @author tadekayero
 */
@Getter @Setter
@XmlRootElement(name = "Header")
public class Header {
    
       @XmlElement(name = "InstitutionCode")
   private String InstitutionCode;
   
   @XmlElement(name = "TotalCount")
   private String TotalCount;
   
   @XmlElement(name = "ResponseCode")
   private String ResponseCode;
   
}
