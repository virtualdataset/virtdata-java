package io.virtdata.coretesting;

import io.virtdata.autodoctypes.DocFuncData;
import io.virtdata.core.VirtDataDocs;
import io.virtdata.finders.FunctionFinderService;
import org.junit.Test;

import java.util.List;

public class VirtDataDocsIntegratedTest {

    @Test
    public void testGetAllNames() {
        List<FunctionFinderService.Path> allNames = VirtDataDocs.getAllNames();
        System.out.println("Found " + allNames.size() + " documented function names.");
    }

    @Test
    public void testGetAllDocs() {
        List<DocFuncData> allDocs = VirtDataDocs.getAllDocs();
        System.out.println("Found " + allDocs.size() + " documentation classes names.");

    }
}