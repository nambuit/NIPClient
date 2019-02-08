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
 * @author emusa
 */
@Getter @Setter
public class RegisterPaymentDetailRequest {
    
    private String requestID;
    private String sessionID;
    private String payphonenumber;
    private String payerBVN;
    private String merchantcode;
    private String Amount;
    private String InstitutionCode;
    private String FinancialInstitutionCode;
    private String Accountnumber;
    private String hash;
    
  }
