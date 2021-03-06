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
@XmlRootElement(name = "Account")
public class Account {
   @XmlElement(name = "AccountNumber")
   private String AccountNumber;
   
   @XmlElement(name = "AccountName")
   private String AccountName;
   
   @XmlElement(name = "Kyc")
   private String Kyc;
   
   @XmlElement(name = "BankVerificationNumber")
   private String BankVerificationNumber;
   
   @XmlElement(name = "MaximumTransactionAmount")
   private String MaximumTransactionAmount;
   
  @XmlElement(name = "DeferredSettlement")
   private String DeferredSettlement;
}
