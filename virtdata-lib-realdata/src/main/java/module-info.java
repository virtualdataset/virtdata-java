module io.virtdata.lib.realdata {
    requires io.virtdata.annotations;
    provides io.virtdata.services.ModuleDataService
            with io.virtdata.realdata.ModuleDataService;
}