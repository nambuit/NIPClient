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
public class optinoptoutRequest {

    private String requestorID;
    private String sessionID;
    private String merchantcode;
    private String payerphonenumber;
    private String reason;
    private String institutioncode;
    private String optin;
    private String hash;

}
