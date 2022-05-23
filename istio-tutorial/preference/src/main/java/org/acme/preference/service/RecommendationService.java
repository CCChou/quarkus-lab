package org.acme.preference.service;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@RegisterRestClient
public interface RecommendationService {

    @Path("/")
    @GET
    @Produces("text/plain")
    public String getRecommendation();

}
