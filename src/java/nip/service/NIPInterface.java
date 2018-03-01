/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nip.service;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.core.MediaType;

/**
 * REST Web Service
 *
 * @author Administrator
 */
@Path("NIPOutwardInterface")
public class NIPInterface {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of NIPInterface
     */
    public NIPInterface() {
    }

    /**
     * Retrieves representation of an instance of nip.service.NIPInterface
     * @return an instance of java.lang.String
     */
    @GET
    @Path("NameEnquiry")
    @Produces(MediaType.APPLICATION_JSON)
    public String getXml() {
     return "fggsgs";
    }

    /**
     * PUT method for updating or creating an instance of NIPInterface
     * @param content representation for the resource
     */
    @PUT
    @Consumes(MediaType.APPLICATION_XML)
    public void putXml(String content) {
    }
}
