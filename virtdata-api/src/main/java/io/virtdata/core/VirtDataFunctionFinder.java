package io.virtdata.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class VirtDataFunctionFinder {
    public VirtDataFunctionFinder() {
    }

    public List<String> getFunctionNames() {
        ClassLoader cl = VirtDataFunctionResolver.class.getClassLoader();
        Enumeration<URL> resources;
        try {
            resources = ClassLoader.getSystemResources("META-INF/functions");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        List<URL> urls = new ArrayList<>();
        while (resources.hasMoreElements()) {
            urls.add(resources.nextElement());
        }
        InputStream funcstream = cl.getResourceAsStream("META-INF/functions");
        if (funcstream == null) {
            throw new RuntimeException("unable to find META-INF/functions.");
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(funcstream));
        List<String> names = reader.lines()
                .filter(Objects::nonNull)
                .filter(s -> !s.isEmpty())
                .map(String::trim)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
        return names;
    }
}