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
@XmlRootElement(name = "TransactionStatusRequest")
public class TransactionStatusRequest {
    @XmlElement(name = "SessionID")
   private String SessionID;
   
   @XmlElement(name = "RequestorID")
   private String RequestorID;
   
   @XmlElement(name = "MerchantCode")
   private String MerchantCode;
   
   @XmlElement(name = "ReferenceCode")
   private String ReferenceCode;
   
   @XmlElement(name = "PayerPhoneNumber")
   private String PayerPhoneNumber;
    
}
