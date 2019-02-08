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
 * @author tadekayero
 */

@Getter 
@XmlRootElement(name = "SearchResult")
public class SearchResult {
    
   @XmlElement(name = "ResultStatus")
    private String ResultStatus; 
   
   @XmlElement(name = "BvnSearchResult")
    private BvnSearchResult BvnSearchResult; 
   
   
}
