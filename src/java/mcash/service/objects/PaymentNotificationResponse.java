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
@XmlRootElement(name = "PaymentNotificationResponse")
public class PaymentNotificationResponse {
    
    
    @XmlElement(name = "SessionID")
   private String SessionID;
   
   @XmlElement(name = "RequestorID")
   private String RequestorID;
   
   @XmlElement(name = "PayerPhoneNumber")
   private String PayerPhoneNumber;
   
   @XmlElement(name = "MerchantCode")
   private String MerchantCode;
   
   @XmlElement(name = "MerchantName")
   private String MerchantName;
    
   @XmlElement(name = "Amount")
   private String Amount;
   
   @XmlElement(name = "FinancialInstitutions")
   private String FinancialInstitutions;
   
   @XmlElement(name = "AccountNumber")
   private String AccountNumber;
   
   @XmlElement(name = "TransactionDate")
   private String TransactionDate;
   
   @XmlElement(name = "Passcode")
   private String Passcode;
   
   @XmlElement(name = "SecondFactorAuthCode")
   private String SecondFactorAuthCode;
    
   @XmlElement(name = "ReferenceCode")
   private String ReferenceCode;
   
   @XmlElement(name = "ResponseCode")
   private String ResponseCode;
   
   @XmlElement(name = "PayerBVN")
   private String PayerBVN;
   
   @XmlElement(name = "MandateReferenceNumber")
   private String MandateReferenceNumber;
   
   @XmlElement(name = "ProductCode")
   private String ProductCode;
    
}
