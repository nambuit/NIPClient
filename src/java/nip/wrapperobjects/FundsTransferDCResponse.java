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
 * @author r16user
 */
@Getter @Setter
@XmlRootElement(name = "FundsTransferDCResponse")
public class FundsTransferDCResponse {
 
    private String requestID;
    private String nibssSessionID;
    private String destinationInstitutionCode;
    private String beneficiaryAccountNumber;
    private String responseCode;
    private String responseDescription;
    private String hash;
    private String ChannelCode;
    private String InstitutionCode;
    
}
