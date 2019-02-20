/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bvn.service.objects;


import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author dogor-Igbosuah
 */

@Getter @Setter

public class BvnMultipleResponse {
    
    private String ResponseCode; 
    
    private  BvnSingleResponse [] ValidationResponses;
    
}
