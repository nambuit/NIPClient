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
public class financialInstitutionCode {

    private String name;
    private String accountnumber;
    private String FISpecificInformation;
    private String SecondFactorAuthCode;

}
