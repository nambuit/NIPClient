/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mcash.service.objects;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.Getter;

/**
 *
 * @author tadekayero
 */
@Getter 
@XmlRootElement(name = "FinancialInstitutions")
public class FinancialInstitutions {
    
    @XmlElement(name = "FinancialInstitutionCode")
    private FinancialInstitutionCode [] FinancialInstitutionCode;
}
