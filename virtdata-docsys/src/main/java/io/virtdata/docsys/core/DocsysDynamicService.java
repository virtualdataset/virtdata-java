package io.virtdata.docsys.core;

import io.virtdata.annotations.Service;
import io.virtdata.docsys.api.Docs;
import io.virtdata.docsys.api.DocsInfo;
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
@Path("/")
public class DocsysDynamicService implements WebServiceObject {
    private final static Logger logger = LoggerFactory.getLogger(DocsysDynamicService.class);

    private DocsInfo docsinfo;
    private DocsInfo enabled;
    private DocsInfo disabled;
    private AtomicLong version = new AtomicLong(System.nanoTime());

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
     * @param enable A set of namespaces to enable, or no provided value to enable all namespaces
     * @return A view of the namespaces known to this service
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("namespaces")
    public Map<String, Map<String, Set<java.nio.file.Path>>> getPaths(
            @QueryParam("enable") String enable,
            @QueryParam("reload") boolean reload
    ) {
        if (reload) {
            reload();
        }
        load();
        if (enable != null && !enable.isEmpty()) {
            Set<String> enabled = new HashSet<>(List.of(enable.split("[, ;]")));
            this.disabled = new Docs().merge(docsinfo);
            this.enabled = disabled.remove(enabled);
        } else {
            this.enabled = new Docs().merge(docsinfo);
            this.disabled = new Docs().asDocsInfo();
        }

        Map<String, Map<String, Set<java.nio.file.Path>>> namespaces = new HashMap<>();
        namespaces.put("enabled", enabled.getPathMap());
        namespaces.put("disabled", disabled.getPathMap());
        return namespaces;
    }

    /**
     * @return Provide a list of all files from all enabled namespaces.
     */
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("allfiles.csv")
    public String getAllfilesList(@QueryParam("reload") boolean reload) {
        if (reload) {
            reload();
        }
        load();
        StringBuilder sb = new StringBuilder();
        for (java.nio.file.Path path : enabled.getPaths()) {
            PathWalker.findAll(path).forEach(f -> {
                sb.append(path.relativize(f).toString()).append("\n");
            });
        }
        return sb.toString();
    }

    /**
     * @return Provide a lit of all files from all enabled namespaces
     * where the file path ends with '.md'
     */
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("markdown.csv")
    public String getMarkdownList(@QueryParam("reload") boolean reload) {
        if (reload) {
            reload();
        }
        load();
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
        load();
        List<String> list = new ArrayList<>();
        for (java.nio.file.Path path : enabled.getPaths()) {
            PathWalker.findAll(path).forEach(f -> {
                java.nio.file.Path relative = path.relativize(f);
                list.add(relative.toString());
            });
        }
        return list;
    }

    /**
     * @param pathspec the path as known to the manifest
     * @return The contents of a file
     */
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path(value = "markdown/{pathspec: .*}")
    public String getFile(@PathParam("pathspec") String pathspec) {

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

    private void load() {
        if (this.docsinfo == null) {
            this.docsinfo = DocsysPathLoader.loadDynamicPaths();
            version.set(System.nanoTime());
        }
    }

    @GET
    @Path("reload")
    public void reload() {
        this.enabled = null;
        this.disabled = null;
        this.docsinfo = null;
        load();
    }


}
