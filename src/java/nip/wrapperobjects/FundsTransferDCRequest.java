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
@XmlRootElement(name = "FundsTransferDCRequest")
public class FundsTransferDCRequest {
    
    private String requestID;
    private String nameEnquiryRef;
    private String destinationInstitutionCode;
    private String beneficiaryAccountName;
    private String beneficiaryAccountNumber;
    private String beneficiaryBankVerificationNumber;
    private String beneficiaryKYCLevel;
    private String originatorAccountName;
    private String originatorAccountNumber;
    private String originatorBankVerificationNumber;
    private String originatorKYCLevel;
    private String transactionLocation;
    private String PaymentReference;
    private String narration;
    private String amount;
    private String hash;
    private String ChannelCode;
    private String InstitutionCode;
    
}
