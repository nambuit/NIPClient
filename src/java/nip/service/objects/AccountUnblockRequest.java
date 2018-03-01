/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nip.service.objects;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.Getter;
import lombok.Setter;
/**
 *
 * @author chijiokennamani
 */


@Getter @Setter 
@XmlRootElement(name = "AccountUnblockRequest")
public class AccountUnblockRequest {
    
  @XmlElement(name = "SessionID")
  private String SessionID;
  
  @XmlElement(name = "DestinationInstitutionCode") 
  private String DestinationInstitutionCode;
  
  @XmlElement(name = "ChannelCode") 
  private Integer ChannelCode;
  
   @XmlElement(name = "ReferenceCode")  
   private String ReferenceCode;
   
   @XmlElement(name = "TargetAccountName") 
   private String TargetAccountName;
   
   @XmlElement(name = "TargetBankVerificationNumber") 
   private String TargetBankVerificationNumber;
     
   @XmlElement(name = "TargetAccountNumber") 
   private String TargetAccountNumber;
  
   @XmlElement(name = "ReasonCode")  
   private String ReasonCode;
 
   @XmlElement(name = "Narration")  
   private String Narration;
}
