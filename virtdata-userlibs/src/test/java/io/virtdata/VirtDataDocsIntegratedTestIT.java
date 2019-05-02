package io.virtdata;

import io.virtdata.autodoctypes.DocFuncData;
import io.virtdata.core.VirtDataDocs;
import io.virtdata.services.FunctionFinderService;
import io.virtdata.util.ModuleInfo;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

public class VirtDataDocsIntegratedTestIT {

    @BeforeClass
    public static void showModuleInfo() {
        ModuleInfo.logModuleNamesDebug(VirtDataDocsIntegratedTestIT.class);
    }

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