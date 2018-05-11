/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nip.service.objects;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.Setter;

/**
 *
 * @author wumoru
 */
@Setter
@XmlRootElement(name = "TSQuerySingleRequest")
public class TSQuerySingleRequest {
    
    @XmlElement(name = "SourceInstitutionCode")
    private String SourceInstitutionCode;
    
    @XmlElement(name = "ChannelCode")
    private String ChannelCode;
    
    @XmlElement(name = "SessionID")
    private String SessionID;
    
}
