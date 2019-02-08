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
 * @author dogor-Igbosuah
 */
@Getter 
@XmlRootElement(name = "OptInOptOutRequest")

public class OptInOptOutRequest {
    
  
    @XmlElement(name = "SessionID")
    private String SessionID;
    @XmlElement(name = "RequestorID")
    private String RequestorID;
    @XmlElement(name = "MerchantCode") 
    private String MerchantCode;
    @XmlElement(name = "PayerPhoneNumber")
    private String PayerPhoneNumber;
    @XmlElement(name = "Reason")
    private String Reason;
    @XmlElement(name = "OptIn") //Array
    private String OptIn; //Array
    
}
