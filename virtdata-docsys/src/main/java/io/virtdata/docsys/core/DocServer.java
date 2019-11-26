package io.virtdata.docsys.core;

import io.virtdata.docsys.DocsysDefaultAppPath;
import io.virtdata.docsys.api.Docs;
import io.virtdata.docsys.api.WebServiceObject;
import io.virtdata.docsys.handlers.FavIconHandler;
import org.eclipse.jetty.server.*;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.resource.JarResource;
import org.eclipse.jetty.util.resource.PathResource;
import org.eclipse.jetty.util.resource.Resource;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;
import org.glassfish.jersey.server.model.ResourceMethod;
import org.glassfish.jersey.servlet.ServletContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRegistration;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.AccessMode;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class DocServer implements Runnable {

    private final static Logger logger = LoggerFactory.getLogger(DocServer.class);
    private final List<Path> basePaths = new ArrayList<>();
    private final List<Class> servletClasses = new ArrayList<>();
    private ServletContextHandler contextHandler;
    private ServletHolder servletHolder;
    private HandlerList handlers;

    private String bindScheme = "http";
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

    public DocServer withURL(String urlSpec) {
        try {
            URL url = new URL(urlSpec);
            this.bindPort = url.getPort();
            this.bindHost = url.getHost();
            if (url.getPath() != null && url.getPath().isEmpty()) {
                throw new UnsupportedOperationException("You may not specify a path for the hosting URL.");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    public DocServer withScheme(String scheme) {
        this.bindScheme = scheme;
        return this;
    }

    private void addWebObject(Class... objects) {
        servletClasses.addAll(Arrays.asList(objects));
//        String servletClasses = this.servletClasses
//                .stream()
//                .map(Class::getCanonicalName)
//                .collect(Collectors.joining(","));
//
//        getServletHolder().setInitParameter(
//                "jersey.config.server.provider.classnames",
//                servletClasses
//        );
//        return this;
    }

    private void loadDynamicEndpoints() {
        List<WebServiceObject> serviceObjects = WebObjectLoader.loadWebServiceObjects();
        for (WebServiceObject serviceObject : serviceObjects) {
            logger.info("Adding web service object: " + serviceObject.toString());
            this.addWebObject(serviceObject.getClass());
        }
        logger.debug("Loaded " + this.servletClasses.size() + " root resources.");

    }

    private ServletContextHandler getContextHandler() {
        if (contextHandler == null) {
            contextHandler = new ServletContextHandler();
            contextHandler.setContextPath("/*");
        }
        return contextHandler;
    }

    private ServletHolder getServletHolder() {
        if (servletHolder == null) {
            servletHolder = getContextHandler().addServlet(
                    ServletContainer.class,
                    "/apps"
            );
            servletHolder.setInitOrder(0);
        }
        return servletHolder;
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
        handlers = new HandlerList();

        if (this.basePaths.size() == 0 && this.servletClasses.size() == 0) {
            logger.warn("No service endpoints or doc paths have been added. Loading dynamically.");
        }

//        RewriteHandler rh = new RewriteHandler();
//        rh.addRule(new RedirectRegexRule("/","/docs/"));
//        handlers.addHandler(rh);

        // Favicon
        for (Path basePath : basePaths) {
            Path icon = basePath.resolve("/favicon.ico");
            if (Files.exists(icon)) {
                FavIconHandler favIconHandler = new FavIconHandler(basePaths.get(0) + "/favicon.ico", false);
                handlers.addHandler(favIconHandler);
                break;
            }
        }

        if (basePaths.size()==0) {
            Docs docs = new Docs();
            // Load static path contexts which are published within the runtime.
            docs.merge(DocsysPathLoader.loadStaticPaths());

            // If none claims the "docsys-app" namespace, then install the
            // default static copy of the docs app
            if (!docs.getPathMap().containsKey("docsys-app")) {
                docs.merge(new DocsysDefaultAppPath().getDocs());
            }
            basePaths.addAll(docs.getPaths());
        }

        for (Path basePath : basePaths) {
            logger.info("Adding path to server: " + basePath.toString());
            ResourceHandler resourceHandler = new ResourceHandler();
            resourceHandler.setDirAllowed(true);
            resourceHandler.setAcceptRanges(true);

            resourceHandler.setWelcomeFiles(new String[]{"index.html"});
//            resourceHandler.setRedirectWelcome(true);
            Resource baseResource = new PathResource(basePath);

            if (basePath.toUri().toString().startsWith("jar:")) {
                try {
                    baseResource=JarResource.newResource(basePath.toUri());
                } catch (MalformedURLException e) {
                    throw new RuntimeException(e);
                }
            }

            resourceHandler.setBaseResource(baseResource);
            resourceHandler.setCacheControl("no-cache");
            handlers.addHandler(resourceHandler);

        }

//        ResourceConfig statusResourceCfg = new ResourceConfig(DocServerStatusEndpoint.class);
//        statusResourceCfg.property("server", this);
//        ServletContainer statusResourceContainer = new ServletContainer(statusResourceCfg);
//        ServletHolder statusResourceServletHolder = new ServletHolder(statusResourceContainer);
//        getContextHandler().addServlet(statusResourceServletHolder, "/_");

        logger.info("adding " + servletClasses.size() + " context handlers...");
        loadDynamicEndpoints();


        ResourceConfig rc = new ResourceConfig();
        rc.property("server", this);

        ServletContainer container = new ServletContainer(rc);
        ServletHolder servlets = new ServletHolder(container);
        String classnames = this.servletClasses
                .stream()
                .map(Class::getCanonicalName)
                .collect(Collectors.joining(","));
        rc.property(ServerProperties.PROVIDER_CLASSNAMES,classnames);
//        servlets.setInitParameter(ServerProperties.PROVIDER_CLASSNAMES,
//                classnames
//        );
        ServletContextHandler sch = new ServletContextHandler();
        sch.setContextPath("/*");
        sch.addServlet(servlets,"/*");

        handlers.addHandler(sch);

        // Show contexts
        DefaultHandler defaultHandler = new DefaultHandler();
        defaultHandler.setShowContexts(true);
        defaultHandler.setServeIcon(false);

        handlers.addHandler(defaultHandler);


        server.setHandler(handlers);
        for (Connector connector : server.getConnectors()) {
            if (connector instanceof AbstractConnector) {
                logger.debug("Setting idle timeout for " + connector.toString() + " to 300,000ms");
                ((AbstractConnector) connector).setIdleTimeout(300000);
            }
        }
        try {
            ServerConnector sc = null;
            if (bindScheme.equals("http")) {
                sc = new ServerConnector(server);
                sc.setPort(bindPort);
                sc.setHost(bindHost);
                server.setConnectors(new Connector[]{sc});
            } else if (bindScheme.equals("https")) {
                throw new UnsupportedOperationException("TLS is not supported yet");
            }

            server.start();
            logger.info("Started documentation server at http://" + bindHost + ":" + bindPort + "/");
            server.join();
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            e.printStackTrace(System.out);
            System.exit(2);
        }

    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Class servletClass : this.servletClasses) {
            sb.append("- ").append(servletClass.getCanonicalName()).append("\n");
            ResourceConfig rc = new ResourceConfig(servletClass);
            Set<org.glassfish.jersey.server.model.Resource> resources = rc.getResources();
            for (org.glassfish.jersey.server.model.Resource resource : resources) {
                sb.append("resource name:").append(resource.getName()).append("\n");
                List<ResourceMethod> resourceMethods = resource.getResourceMethods();
                for (ResourceMethod resourceMethod : resourceMethods) {
                    String rm = resourceMethod.toString();
                    sb.append("rm:").append(rm);
                }
            }
        }
        sb.append("- - - -\n");
        for (Handler handler : handlers.getHandlers()) {
            String summary = handlerSummary(handler);
            sb.append(summary == null ? "NULL SUMMARY" : summary);
            sb.append("\n");
        }
        return sb.toString();
    }

    private String handlerSummary(Handler handler) {
        StringBuilder sb = new StringBuilder();
        sb.append("----> handler type ").append(handler.getClass().getSimpleName()).append("\n");

        if (handler instanceof ResourceHandler) {
            ResourceHandler h = (ResourceHandler) handler;
            sb.append(" base resource: ").append(h.getBaseResource().toString())
                    .append("\n");
            sb.append(h.dump());
        } else if (handler instanceof ServletContextHandler) {
            ServletContextHandler h = (ServletContextHandler) handler;
            sb.append(h.dump()).append("\n");
            h.getServletContext().getServletRegistrations().forEach(
                    (k, v) -> {
                        sb.append("==> servlet type ").append(k).append("\n");
                        sb.append(getServletSummary(v)).append("\n");
                    }
            );
            sb.append("context path:").append(h.getContextPath());
        } else if (handler instanceof DefaultHandler) {
            DefaultHandler h = (DefaultHandler) handler;
            sb.append(h.dump());
        }
        return sb.toString();
    }

    private String getServletSummary(ServletRegistration v) {
        return v.getClassName() + "('" + v.getName() + "')" + v.getInitParameters().keySet().stream().map(
                k -> k + "=" + v.getInitParameters().get(k)).collect(Collectors.joining(","));
    }
}
