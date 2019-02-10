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
@Getter 
@Setter
public class Pre_PaymentRequest {
  
    private String InstitutionCode;
    private String RequestorID;
    private String PayerPhoneNumber;
    private String PayerBVN;
    private String MerchantCode;
    private String Amount;
    private String hash;
   
}
