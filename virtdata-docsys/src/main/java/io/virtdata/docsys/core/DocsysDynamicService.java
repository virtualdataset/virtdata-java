package io.virtdata.docsys.core;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.jaxrs.annotation.JacksonFeatures;
import io.virtdata.annotations.Service;
import io.virtdata.docsys.api.DocNameSpace;
import io.virtdata.docsys.api.Docs;
import io.virtdata.docsys.api.DocNameSpacesBinder;
import io.virtdata.docsys.api.WebServiceObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Service(WebServiceObject.class)
@Singleton
@Path("/services/docs/")
public class DocsysDynamicService implements WebServiceObject {
    private final static Logger logger = LoggerFactory.getLogger(DocsysDynamicService.class);

    private DocNameSpacesBinder docsinfo;
    private DocNameSpacesBinder enabled;
    private DocNameSpacesBinder disabled;

    private AtomicLong version = new AtomicLong(System.nanoTime());
    private final Set<String> enables = new HashSet<>();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("version")
    public long getVersion() {
        return version.get();
    }

    /**
     * If no enable= parameter is provided, then this call simply provides a map of
     * namespaces which are enabled and disabled.
     *
     * @param enableParam A set of namespaces to enable, or no provided value to enable all namespaces
     * @return A view of the namespaces known to this service
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("namespaces")
    @JacksonFeatures(serializationEnable =  { SerializationFeature.INDENT_OUTPUT })
    public Map<String, Map<String, Set<java.nio.file.Path>>> getNamespaces(
            @QueryParam("enable") String enableParam,
            @QueryParam("reload") boolean reload
    ) {

        if (enableParam!=null && !enableParam.isEmpty()) {
            enables.clear();
            enables.addAll(List.of(enableParam.split("[, ;]")));
        }

        init(reload);
        enable(enables);

        return Map.of(
                "enabled",enabled.getPathMap(),
                "disabled",disabled.getPathMap()
        );
    }


    /**
     * @return Provide a list of all files from all enabled namespaces.
     */
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("allfiles.csv")
    public String getAllfilesList(@QueryParam("reload") boolean reload) {

        init(reload);

        StringBuilder sb = new StringBuilder();
        for (java.nio.file.Path path : enabled.getPaths()) {
            PathWalker.findAll(path).forEach(f -> {
                sb.append(path.relativize(f).toString()).append("\n");
            });
        }
        return sb.toString();
    }

    /**
     * @return Provide a list of all files from all enabled namespaces
     * where the file path ends with '.md'
     */
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("markdown.csv")
    public String getMarkdownList(@QueryParam("reload") boolean reload) {

        init(reload);

        StringBuilder sb = new StringBuilder();
        for (java.nio.file.Path path : enabled.getPaths()) {
            PathWalker.findAll(path).forEach(f -> {
                if (f.toString().endsWith(".md")) {
                    sb.append(path.relativize(f).toString()).append("\n");
                }
            });
        }
        return sb.toString();
    }

    /**
     * @return Provides a list of all files from all enabled namespaces as a JSON list.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("list")
    public List<String> listFiles(@QueryParam("reload") boolean reload) {
        init(reload);
        List<String> list = new ArrayList<>();
        for (java.nio.file.Path path : enabled.getPaths()) {
            PathWalker.findAll(path).forEach(f -> {
                java.nio.file.Path relative = path.relativize(f);
                list.add(relative.toString());
            });
        }
        return list;
    }

    @GET
    @Path("file")
    @Produces(MediaType.TEXT_PLAIN)
    public String getFileByPath(@QueryParam("path") String pathspec) {
        return getFile(pathspec);
    }

    /**
     * @param pathspec the path as known to the manifest
     * @return The contents of a file
     */
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path(value = "markdown/{pathspec:.*}")
    public String getFileInPath(@PathParam("pathspec") String pathspec) {
        return getFile(pathspec);
    }

    private String getFile(String pathspec) {
        pathspec = URLDecoder.decode(pathspec, StandardCharsets.UTF_8);
        for (java.nio.file.Path path : enabled.getPaths()) {
            java.nio.file.Path resolved = path.resolve(pathspec);
            if (Files.isDirectory(resolved)) {
                throw new RuntimeException("Path is a directory: '" + pathspec + "'");
            }
            if (Files.exists(resolved)) {
                try {
                    String content = Files.readString(resolved, StandardCharsets.UTF_8);
                    return content;
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        throw new RuntimeException("Unable to find any valid file at '" + pathspec + "'");
    }

    private void init(boolean reload) {
        if (reload) {
            this.enabled = null;
            this.disabled = null;
            this.docsinfo = null;
        }
        if (this.docsinfo == null) {
            this.docsinfo = DocsysPathLoader.loadDynamicPaths();
            version.set(System.nanoTime());
        }
        if (enabled==null || disabled==null) {
            enable(enables);
        }
    }

    private void enable(Set<String> enabled) {

        Set<String> toEnable = new HashSet<>();
        if (this.enables !=null) {
            toEnable.addAll(this.enables);
        }

        for (DocNameSpace nsinfo : docsinfo) {
            // add namespaces which are neither enabled nor disabled to the default group
            if (nsinfo.isEnabledByDefault()) {
                if (disabled!=null && disabled.getPathMap().containsKey(nsinfo.getNameSpace())) {
                    continue;
                }
                enables.add(nsinfo.getNameSpace());
            }
        }

        if (enabled.isEmpty()) { // Nothing is enabled or enabled by default, so enable everything
            this.enabled = new Docs().merge(docsinfo);
            this.disabled = new Docs().asDocsInfo();
        } else { // At least one thing was enabled by default, or previously enabled specifically
            this.disabled = new Docs().merge(docsinfo);
            this.enabled = disabled.remove(enabled);
        }
    }


}
