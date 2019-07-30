package io.virtdata.docsys.handlers;

import io.virtdata.docsys.metafs.core.MetaFS;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("metafs")
public class MetaFsLayout {
    private final MetaFS metafs;

    public MetaFsLayout(MetaFS metafs) {
        this.metafs = metafs;
    }

    @GET
    @Path("layout")
    @Produces(MediaType.TEXT_PLAIN)
    public String getLayout() {
        return metafs.toString();
    }
}
