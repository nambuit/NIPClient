/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mcash.wrapperobjects;


import javax.xml.bind.annotation.XmlRootElement;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author r16user
 */

@Getter @Setter
@XmlRootElement(name = "FundsTransferDCRequest")
public class RegisterMerchantResponse {
    
    private String requestID;
    private String merchantCode;
    private String sessionID;
    private String hash;
    private String responseCode;
    private String responseDescription;

}
