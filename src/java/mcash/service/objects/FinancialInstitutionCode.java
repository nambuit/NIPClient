/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mcash.service.objects;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;
import lombok.Getter;
import lombok.Setter;


/**
 *
 * @author tadekayero
 */
@Setter @Getter
@XmlRootElement(name = "FinancialInstitutionCode")
public class FinancialInstitutionCode {
    
   @XmlAttribute(name = "Name")
    private String Name;
   
    @XmlAttribute(name = "accountNumber")
    private String accountNumber;
    
    @XmlAttribute(name = "FISpecificInformation")
    private String FISpecificInformation;
    
    @XmlAttribute(name = "SecondFactorAuthCode")
    private String SecondFactorAuthCode;
    
    @XmlValue
    private String FinancialInstitutionCode;

}
