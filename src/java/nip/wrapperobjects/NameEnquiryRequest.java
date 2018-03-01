/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nip.wrapperobjects;



import javax.xml.bind.annotation.XmlRootElement;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author wumoru
 */

@Getter @Setter
@XmlRootElement(name = "NameEnquiryRequest")
public class NameEnquiryRequest {
    
   
    private String requestID;
    

    private String destinationInstitutionCode;
    
    private String accountNumber;
    
    private String hash;
    

   
    
}
