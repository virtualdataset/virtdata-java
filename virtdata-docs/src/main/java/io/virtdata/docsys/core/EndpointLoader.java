package io.virtdata.docsys.core;

import io.virtdata.docsys.api.DocSystemEndpoint;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

public class EndpointLoader {
    private final static List<DocSystemEndpoint> endpoints = new ArrayList<>();

    public static List<DocSystemEndpoint> load() {
        ServiceLoader<DocSystemEndpoint> loader = ServiceLoader.load(DocSystemEndpoint.class);
        loader.forEach(endpoints::add);
        return endpoints;
    }
}
