package io.virtdata.docsys.core;

import io.virtdata.annotations.Service;
import io.virtdata.docsys.api.WebServiceObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

// TODO: MUltiple service objects don't work together yet in web
// TODO: Testing doesn't pick up packaged service files
// TODO: investigate how other bundled services work in debugging
// TODO: See if deferring to maven always will help
@Service(WebServiceObject.class)
@Singleton
@Path("/docs")
public class DocPathInfoService implements WebServiceObject {
    private final static Logger logger = LoggerFactory.getLogger(DocPathInfoService.class);

    private Map<String, Set<java.nio.file.Path>> docPathInfos;

    //private static Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("namespaces")
    public Map<String, Set<java.nio.file.Path>> getPaths() {
        return loadDocPaths();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("list")
    public List<String> listFiles() {
        List<String> list = new ArrayList<>();
        Map<String, Set<java.nio.file.Path>> paths = loadDocPaths();

        for (Set<java.nio.file.Path> pathSet : paths.values()) {
            pathSet.forEach(p -> {
                    PathWalker.findAll(p).forEach(f -> {
                        java.nio.file.Path relative = p.relativize(f);
                        list.add(relative.toString());
                    });
            });
        }

        return list;
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("file/{pathspec: .*}")
    public String getFile(@PathParam("pathspec") String pathspec) {
        for (Set<java.nio.file.Path> pathset : this.loadDocPaths().values()) {
            for (java.nio.file.Path path : pathset) {
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
        }
        throw new RuntimeException("Unable to find any valid file at '"+ pathspec + "'");
    }

    private Map<String, Set<java.nio.file.Path>> loadDocPaths() {
        if (this.docPathInfos == null || this.docPathInfos.size()==0) {
            this.docPathInfos = DocPathLoader.load();
        }
        return this.docPathInfos;
    }

    @GET
    @Path("reload")
    public void reload() {
        this.docPathInfos = null;
    }


}
