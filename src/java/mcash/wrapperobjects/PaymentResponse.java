/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mcash.wrapperobjects;
import lombok.Getter;
import lombok.Setter;
/**
 *
 * @author dogor-Igbosuah
 */
@Getter @Setter
public class PaymentResponse {
    private String requestID;
     private String InstitutionCode;
    private String SessionID;
    private String RequestorID;
    private String PayerPhoneNumber;
    private String PayerBVN;
    private String PayerName;
    private String FinancialInstitutionCode;
    private String Telco;
    private String Amount;
    private String MerchantCode;
    private String MandateCode;
    private String ReferenceCode;
    private String MerchantPhoneNumber;
    private String Fee;
    private String Name;
    private String accountNumber;
    private String FISpecificInformation;
    private String ResponseCode;    
    private String hash;
    private String responseDescription;
}
