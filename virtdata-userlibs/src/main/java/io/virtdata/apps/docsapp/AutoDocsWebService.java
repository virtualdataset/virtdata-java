package io.virtdata.apps.docsapp;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.jaxrs.annotation.JacksonFeatures;
import io.virtdata.annotations.Service;
import io.virtdata.core.VirtDataDocs;
import io.virtdata.docsys.api.WebServiceObject;
import io.virtdata.processors.DocFuncData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Service(WebServiceObject.class)
@Singleton
@Path("/services/virtdata/functions/")
public class AutoDocsWebService implements WebServiceObject {
    private final static Logger logger  = LogManager.getLogger(AutoDocsWebService.class);

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @JacksonFeatures(serializationEnable =  { SerializationFeature.INDENT_OUTPUT })
    @Path("names")
    public List<String> getAutoDocsNames() {
        return VirtDataDocs.getAllNames();
    }

    @GET
    @JsonFormat
    @JacksonFeatures(serializationEnable =  { SerializationFeature.INDENT_OUTPUT })
    @Produces(MediaType.APPLICATION_JSON)
    @Path("details")
    public List<DocFuncData> getAutoDocsDetails() {
        return VirtDataDocs.getAllDocs();
    }

}
