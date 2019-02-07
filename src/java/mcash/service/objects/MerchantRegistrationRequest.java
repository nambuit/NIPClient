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
 * @author tadekayero
 */
@Getter @Setter
@XmlRootElement(name = "MerchantRegistrationRequest")
public class MerchantRegistrationRequest {
    

    @XmlElement(name = "Header")
    private Header Header;
    
    @XmlElement(name = "Merchant")
    private Merchant [] Merchant;

    
   
}
