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
@XmlRootElement(name = "Merchant")
public class Merchant {
    @XmlElement(name = "RequestID")
    private String RequestID;
    
    @XmlElement(name = "MerchantCode")
    private String MerchantCode;
    
    @XmlElement(name = "MerchantName") 
    private String MerchantName;
    
    @XmlElement(name = "ContactName")
    private String ContactName;
    
    @XmlElement(name = "PhoneNumber")
    private String PhoneNumber;
    
    @XmlElement(name = "EmailAddress")
    private String EmailAddress;
    
   @XmlElement(name = "GPSLocation")
    private String GPSLocation;
    
    @XmlElement(name = "GroupCode")
    private String GroupCode;
    
    @XmlElement(name = "GroupName")
    private String GroupName;

    
     @XmlElement(name = "Account")
    private Account Account;

    
    @XmlElement(name = "PhysicalAddress")
    private PhysicalAddress PhysicalAddress;
     
}
