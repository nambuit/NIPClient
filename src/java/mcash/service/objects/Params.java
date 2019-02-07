/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mcash.service.objects;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlElement;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author dogor-Igbosuah
 */

@Getter
@XmlRootElement(name = "Params")

public class Params {
    @XmlElement (name = "Param")
    private Param [] Param;
}
