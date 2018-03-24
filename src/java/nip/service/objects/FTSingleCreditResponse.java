/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nip.service.objects;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.Getter;


/**
 *
 * @author wumoru
 */
@Getter
@XmlRootElement(name = "FTSingleCreditResponse")
public class FTSingleCreditResponse {
    
    @XmlElement(name = "SessionID")
   private String SessionID;
   
   @XmlElement(name = "NameEnquiryRef")
   private String NameEnquiryRef;
   
   @XmlElement(name = "DestinationInstitutionCode") 
   private String DestinationInstitutionCode;
   
   @XmlElement(name = "ChannelCode")
   private String ChannelCode;
   
   @XmlElement(name = "BeneficiaryAccountName")
   private String BeneficiaryAccountName;
   
   @XmlElement(name = "BeneficiaryAccountNumber")
   private String BeneficiaryAccountNumber;
   
   @XmlElement(name = "BeneficiaryVerificationNumber")
   private String BeneficiaryVerificationNumber;
   
   @XmlElement(name = "BeneficiaryBankVerificationNumber")
   private String BeneficiaryBankVerificationNumber;
   
   @XmlElement(name = "BeneficiaryKYCLevel")
   private String BeneficiaryKYCLevel;
   
   @XmlElement(name = "OriginatorAccountName")
   private String OriginatorAccountName;
   
   @XmlElement(name = "OriginatorAccountNumber")
   private String OriginatorAccountNumber;
   
   @XmlElement(name = "OriginatorBankVerificationNumber")
   private String OriginatorBankVerificationNumber;
   
   @XmlElement(name = "OriginatorKYCLevel")
   private String OriginatorKYCLevel;
   
   @XmlElement(name = "TransactionLocation")
   private String TransactionLocation;
   
   @XmlElement(name = "Narration") 
   private String Narration;
   
   @XmlElement(name = "PaymentReference") 
   private String PaymentReference;
   
   @XmlElement(name = "Amount") 
   private String Amount;
   
   @XmlElement(name = "ResponseCode") 
   private String ResponseCode;
}
