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
 * @author r16user
 */

@Getter @Setter
public class RegisterMerchantRequest {
    
    private String requestID;
    private String merchantCode;
    private String merchantName;
    private String contactName;
    private String phoneNumber;
    private String emailAddress;
    private String Street;
    private String LGA;
    private String State;
    private String gpsLocation;
    private String groupCode;
    private String groupName;
    private String accountName;  
    private String accountNumber;
    private String InstitutionCode;
     private String kyc;
      private String BVN;
       private String maximumTransactionAmount;
        private String defferedSettlement;
     private String hash;
    
}
