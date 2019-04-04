package io.virtdata.core;

import io.virtdata.processors.types.DocFuncData;
import org.testng.annotations.Test;

import java.util.List;

@Test
public class VirtDataDocsIntegratedTest {

    @Test
    public void testGetAllNames() {
        List<String> allNames = VirtDataDocs.getAllNames();
        System.out.println("Found " + allNames.size() + " documented function names.");
    }

    @Test
    public void testGetAllDocs() {
        List<DocFuncData> allDocs = VirtDataDocs.getAllDocs();
        System.out.println("Found " + allDocs.size() + " documentation classes names.");

    }
}