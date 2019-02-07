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
 * @author cahamefula
 */
@Getter @Setter
@XmlRootElement(name = "PaymentNotification")
public class PaymentNotification {
    
    @XmlElement(name = "SessionID")
   private String SessionID;
   
   @XmlElement(name = "PayerPhoneNumber")
   private String PayerPhoneNumber;
   
   @XmlElement(name = "PayerName")
   private String PayerName;
   
   @XmlElement(name = "MerchantCode")
   private String MerchantCode;
   
   @XmlElement(name = "MerchantName")
   private String MerchantName;
    
}
