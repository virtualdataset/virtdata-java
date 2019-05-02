module io.virtdata.userlibs {
    requires org.slf4j;
    requires io.virtdata.api;
    requires io.virtdata.annotations;

    requires transitive io.virtdata.lib.basics;
    requires transitive io.virtdata.lib.curves4;
    requires transitive io.virtdata.lib.realer;
    requires transitive io.virtdata.lib.realdata;

    uses io.virtdata.services.FunctionFinderService;
    uses io.virtdata.services.ModuleDataService;

    exports io.virtdata.apps.docsapp;
    exports io.virtdata.apps.valuesapp;


}