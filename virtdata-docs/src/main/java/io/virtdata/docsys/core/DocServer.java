package io.virtdata.docsys.core;

import io.virtdata.docsys.handlers.EndpointsHandler;
import io.virtdata.docsys.handlers.FavIconHandler;
import io.virtdata.docsys.metafs.fs.layerfs.LayerFS;
import io.virtdata.docsys.metafs.fs.renderfs.api.FileRenderer;
import io.virtdata.docsys.metafs.fs.renderfs.fs.RenderFS;
import io.virtdata.docsys.metafs.fs.renderfs.renderers.MarkdownProcessor;
import io.virtdata.docsys.metafs.fs.renderfs.renderers.MustacheProcessor;
import io.virtdata.docsys.metafs.fs.virtual.VirtFS;
import org.eclipse.jetty.server.AbstractConnector;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.resource.PathResource;
import org.eclipse.jetty.util.resource.Resource;
import org.glassfish.jersey.servlet.ServletContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.AccessMode;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class DocServer implements Runnable {

    private final static Logger logger = LoggerFactory.getLogger(DocServer.class);
    private final List<Path> basePaths = new ArrayList<>();
    private final List<Class> servletClasses = new ArrayList<>();
    private ServletContextHandler contextHandler;
    private ServletHolder servlets;
    private String bindHost = "localhost";
    private int bindPort = 12345;

    public DocServer withHost(String bindHost) {
        this.bindHost = bindHost;
        return this;
    }

    public DocServer withPort(int bindPort) {
        this.bindPort = bindPort;
        return this;
    }

    public DocServer addWebObject(Class... objects) {
        servletClasses.addAll(Arrays.asList(objects));
        String servletClasses = this.servletClasses
                .stream()
                .map(Class::getCanonicalName)
                .collect(Collectors.joining(","));

        getServlets().setInitParameter(
                "jersey.config.server.provider.classnames",
                servletClasses
        );
        return this;
    }

    private ServletContextHandler getContextHandler() {
        if (contextHandler == null) {
            contextHandler = new ServletContextHandler();
            contextHandler.setContextPath("/");
        }
        return contextHandler;
    }

    private ServletHolder getServlets() {
        if (servlets == null) {
            servlets = getContextHandler().addServlet(
                    ServletContainer.class,
                    "/*"
            );
        }
        return servlets;
    }

    public DocServer addPaths(Path... paths) {
        for (Path path : paths) {
            try {
                path.getFileSystem().provider().checkAccess(path, AccessMode.READ);
                this.basePaths.add(path);
            } catch (Exception e) {
                logger.error("Unable to access path " + path.toString());
                throw new RuntimeException(e);
            }
        }
        return this;
    }

    public void run() {

        //new InetSocketAddress("")
        Server server = new Server(bindPort);
        HandlerList handlers = new HandlerList();


        // TODO: Get auto endpoints running
//        List<DocSystemEndpoint> autoendpoints = EndpointLoader.load();
//        autoendpoints.forEach(e -> {
//            if (!servletClasses.contains(e)) servletClasses.add(e);
//        });
//
        if (this.servletClasses.size()>0) {
            logger.info("adding " + servletClasses.size() + " context handlers");
            handlers.addHandler(getContextHandler());
        } else {
            logger.info("No context handlers defined, not adding context container.");
        }


        //        // Debug
//        DebugListener debugListener = new DebugListener();
//        If needed to limit to local only
//        InetAccessHandler handler = new InetAccessHandler();
//        handler.include("127.0.0.1");
//        InetAccessHandler accessHandler;
//        ShutdownHandler shutdownHandler; // for easy recycles

        // Favicon
        FavIconHandler favIconHandler =
                new FavIconHandler(basePaths.get(0) + "/favicon.ico", false);
        handlers.addHandler(favIconHandler);

//        // Hook dynamic endpoints in process, not in fs layers
        EndpointsHandler endpointsHandler = new EndpointsHandler();
        handlers.addHandler(endpointsHandler);

        LayerFS layerfs = new LayerFS("layers");

        for (Path basePath : basePaths) {
            VirtFS vfs = new VirtFS(basePath, "virt:" + basePath.toString());
            RenderFS rfs = new RenderFS(vfs, "render:" + basePath.toString());

            MustacheProcessor mustache = new MustacheProcessor();
            MarkdownProcessor mdToHtml = new MarkdownProcessor();
            FileRenderer mustacheMarkdown = new FileRenderer("._md", ".md", false, mustache);
            FileRenderer toMdFragment = new FileRenderer("._md", ".mdf", false, mustache);
            FileRenderer markdownHtml = new FileRenderer(".md", "._html", false, mdToHtml);
            FileRenderer mustacheHtml = new FileRenderer("._html", ".html", false, mustache);
            FileRenderer mustacheJson = new FileRenderer("._json", ".json", false, mustache);
            rfs.addRenderers(mustacheMarkdown, mustacheHtml, mustacheJson, markdownHtml, toMdFragment);
            layerfs.addLayer(rfs);
        }

        Resource baseResource = new PathResource(layerfs.getRootPath());

        logger.info("Setting root path of server: " + baseResource.toString());
        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setDirAllowed(true);
        resourceHandler.setAcceptRanges(true);

        resourceHandler.setWelcomeFiles(new String[]{"index.html"});
        resourceHandler.setRedirectWelcome(true);
        resourceHandler.setBaseResource(baseResource);
        resourceHandler.setCacheControl("no-cache");
        handlers.addHandler(resourceHandler);

        // Show contexts
        DefaultHandler defaultHandler = new DefaultHandler();
        defaultHandler.setShowContexts(true);
        defaultHandler.setServeIcon(false);

        handlers.addHandler(defaultHandler);

        server.setHandler(handlers);
        for (Connector connector : server.getConnectors()) {
            if (connector instanceof AbstractConnector) {
                logger.info("Setting idle timeout for " + connector.toString() + " to 300,000ms");
                ((AbstractConnector) connector).setIdleTimeout(300000);
            }
        }
        try {
            server.start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
