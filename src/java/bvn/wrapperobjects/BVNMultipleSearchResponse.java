/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bvn.wrapperobjects;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author emusa
 */
@Getter
@Setter
public class BVNMultipleSearchResponse {

    private BvnSingleSearchResponse [] BVNMultipleSearchResponse;
    private String institutioncode;
    private String hash;
    private String responsecode;
    private String responsedescription;

}
