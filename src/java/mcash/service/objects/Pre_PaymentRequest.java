/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mcash.service.objects;

import javax.xml.bind.annotation.XmlElement;

/**
 *
 * @author dogor-Igbosuah
 */
public class Pre_PaymentRequest {
   @XmlElement(name = "SessionID")
   private String SessionID;
   
   @XmlElement(name = "RequestorID")
   private String RequestorID;
   
   @XmlElement(name = "PayerPhoneNumber")
   private String PayerPhoneNumber;  
   
   @XmlElement(name = "PayerBVN")
   private String PayerBVN;
   
   @XmlElement(name = "MerchantCode")
   private String MerchantCode;
   
   @XmlElement(name = "Amount")
   private String Amount;
}
