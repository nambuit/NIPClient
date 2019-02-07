/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mcash.service.objects;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author dogor-Igbosuah
 */
@Getter @Setter
@XmlRootElement(name = "OptInOptOutResponse")
public class OptInOptOutResponse {
    
    @XmlElement(name = "SessionID")
    private String SessionID;
    @XmlElement(name = "RequestorID")
    private String RequestorID;
    @XmlElement(name = "Message") //Array
    private String Message;  //Array
    @XmlElement(name = "ResponseCode")
    private String ResponseCode;
    
}
