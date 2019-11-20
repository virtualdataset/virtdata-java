package io.virtdata.docsys.core;

import io.virtdata.annotations.Service;
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
@Path("/")
public class DocsysDynamicService implements WebServiceObject {
    private final static Logger logger = LoggerFactory.getLogger(DocsysDynamicService.class);
    private DocsInfo docPathInfos;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("namespaces")
    public Map<String, Set<java.nio.file.Path>> getPaths() {
        return loadDocPaths().getPathMap();
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("markdown.csv")
    public String getMarkdownList() {
        StringBuilder sb = new StringBuilder();
        for (java.nio.file.Path path : loadDocPaths().getPaths()) {
            PathWalker.findAll(path).forEach(f -> {
                java.nio.file.Path relative = path.relativize(f);
                sb.append(path.relativize(f).toString());
            });
        }
        return sb.toString();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("list")
    public List<String> listFiles() {
        List<String> list = new ArrayList<>();
        for (java.nio.file.Path path : loadDocPaths().getPaths()) {
            PathWalker.findAll(path).forEach(f -> {
                java.nio.file.Path relative = path.relativize(f);
                list.add(relative.toString());
            });
        }
        return list;
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path(value="markdown/{pathspec: .*}")
    public String getFile(@PathParam("pathspec") String pathspec) {

        pathspec = URLDecoder.decode(pathspec,StandardCharsets.UTF_8);
        for (java.nio.file.Path path : loadDocPaths().getPaths()) {
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
        throw new RuntimeException("Unable to find any valid file at '"+ pathspec + "'");
    }

    private DocsInfo loadDocPaths() {
        if (this.docPathInfos == null ) {
            this.docPathInfos = DocsysPathLoader.loadDynamicPaths();
        }
        return this.docPathInfos;
    }

    @GET
    @Path("reload")
    public void reload() {
        this.docPathInfos = null;
    }


}
