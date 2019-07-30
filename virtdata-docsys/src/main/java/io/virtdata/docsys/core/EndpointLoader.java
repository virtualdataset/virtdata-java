package io.virtdata.docsys.core;

import io.virtdata.docsys.api.DocPaths;
import io.virtdata.docsys.api.WebServiceObject;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

public class EndpointLoader {

    public static List<WebServiceObject> loadWebServiceObjects() {
        List<WebServiceObject> endpoints = new ArrayList<>();
        ServiceLoader<WebServiceObject> loader = ServiceLoader.load(WebServiceObject.class);
        loader.forEach(endpoints::add);
        return endpoints;
    }

    public static List<DocPaths> loadPathDescriptors() {
        List<DocPaths> docpaths = new ArrayList<>();
        ServiceLoader<DocPaths> loader = ServiceLoader.load(DocPaths.class);
        loader.forEach(docpaths::add);
        return docpaths;
    }
}
