/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nip.service.objects;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.Getter;
import lombok.Setter;
/**
 *
 * @author chijiokennamani
 */
@Getter @Setter 
@XmlRootElement(name = "FinancialInstitutionListRequest")
public class FinancialInstitutionListRequest_1 {
    
    @XmlElement(name = "Header")
    private Header Header;
    
    @XmlElement(name = "Record")
    private Record [] Record;
}
