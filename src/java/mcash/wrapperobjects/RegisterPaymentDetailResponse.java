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
@Getter
@Setter
public class RegisterPaymentDetailResponse {

    private String requestID;
    private String sessionID;
    private String PayBVN;
    private String merchantCode;
    private String merchantname;
    private String Amount;
    private String fee;
    private String institutioncode;
    private param[] params;
    private financialInstitutionCode[] financialInstitutionCode;
    private String responsecode;
    private String responseDescription;
    private String hash;

   
}
