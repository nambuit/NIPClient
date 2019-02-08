/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bvn.service.objects;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.Getter;

/**
 *
 * @author dogor-Igbosuah
 */

@Getter 
@XmlRootElement(name = "SearchResults")
public class SearchResults {
    
    @XmlElement(name = "RequestStatus")
    private String RequestStatus; 
    
    @XmlElement(name = "ResultList")
    private ResultList ResultList;
    
}
