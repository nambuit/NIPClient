/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mcash.wrapperobjects;
import lombok.Getter;
import lombok.Setter;
import mcash.service.objects.FinancialInstitutionCode;
/**
 *
 * @author dogor-Igbosuah
 */

@Getter @Setter
public class Pre_PaymentResponse {
    
    private String requestID;
    private String InstitutionCode;
    private String RequestorID;
    private String PayerPhoneNumber;
    private String PayerBVN;
    private String MerchantCode;
    private String MerchantName;
    private String MerchantPhoneNumber;
    private String Amount;
    private String FinancialInstitutions;
    private financialInstitutionCode [] financialInstitutionCode;
    private String ResponseCode;
    private String Name;
    private String accountNumber;
   private String hash;
   private String responseDescription;
}
