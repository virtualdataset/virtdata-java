//open module io.virtdata.lib.basics {
module io.virtdata.lib.basics {

    requires io.virtdata.annotations;
    requires commons.csv;
    requires org.joda.time;
    requires essentials;
    requires mvel2;
    requires number.to.words;
    requires java.compiler;
    requires io.virtdata.api;

    opens lfsrmasks;

    opens io.virtdata.libbasics.shared.diagnostics;
    exports io.virtdata.libbasics.shared.diagnostics;
    opens io.virtdata.libbasics.shared.from_double.to_double;
    exports io.virtdata.libbasics.shared.from_double.to_double;
    opens io.virtdata.libbasics.shared.from_double.to_float;
    exports io.virtdata.libbasics.shared.from_double.to_float;
    opens io.virtdata.libbasics.shared.from_long.to_bigdecimal;
    exports io.virtdata.libbasics.shared.from_long.to_bigdecimal;
    opens io.virtdata.libbasics.shared.from_long.to_bigint;
    exports io.virtdata.libbasics.shared.from_long.to_bigint;
    opens io.virtdata.libbasics.shared.from_long.to_boolean;
    exports io.virtdata.libbasics.shared.from_long.to_boolean;
    opens io.virtdata.libbasics.shared.from_long.to_byte;
    exports io.virtdata.libbasics.shared.from_long.to_byte;
    opens io.virtdata.libbasics.shared.from_long.to_bytebuffer;
    exports io.virtdata.libbasics.shared.from_long.to_bytebuffer;
    opens io.virtdata.libbasics.shared.from_long.to_collection;
    exports io.virtdata.libbasics.shared.from_long.to_collection;
    opens io.virtdata.libbasics.shared.from_long.to_double;
    exports io.virtdata.libbasics.shared.from_long.to_double;
    opens io.virtdata.libbasics.shared.from_long.to_inetaddress;
    exports io.virtdata.libbasics.shared.from_long.to_inetaddress;
    opens io.virtdata.libbasics.shared.from_long.to_int;
    exports io.virtdata.libbasics.shared.from_long.to_int;
    opens io.virtdata.libbasics.shared.from_long.to_long;
    exports io.virtdata.libbasics.shared.from_long.to_long;
    opens io.virtdata.libbasics.shared.from_long.to_short;
    exports io.virtdata.libbasics.shared.from_long.to_short;
    opens io.virtdata.libbasics.shared.from_long.to_string;
    exports io.virtdata.libbasics.shared.from_long.to_string;
    opens io.virtdata.libbasics.shared.from_long.to_time_types;
    exports io.virtdata.libbasics.shared.from_long.to_time_types;
    opens io.virtdata.libbasics.shared.from_long.to_uuid;
    exports io.virtdata.libbasics.shared.from_long.to_uuid;
    opens io.virtdata.libbasics.shared.nondeterministic.to_int;
    exports io.virtdata.libbasics.shared.nondeterministic.to_int;
    opens io.virtdata.libbasics.shared.nondeterministic.to_long;
    exports io.virtdata.libbasics.shared.nondeterministic.to_long;
    opens io.virtdata.libbasics.shared.stateful;
    exports io.virtdata.libbasics.shared.stateful;
    opens io.virtdata.libbasics.shared.stateful.from_long;
    exports io.virtdata.libbasics.shared.stateful.from_long;
    opens io.virtdata.libbasics.shared.unary_int;
    exports io.virtdata.libbasics.shared.unary_int;
    opens io.virtdata.libbasics.shared.unary_string;
    exports io.virtdata.libbasics.shared.unary_string;
    opens io.virtdata.stathelpers.aliasmethod;
    exports io.virtdata.stathelpers.aliasmethod;
    exports io.virtdata.libbasics.shared;
    opens io.virtdata.libbasics.shared;

    exports io.virtdata.libbasics.shared.conversions.from_double;
    exports io.virtdata.libbasics.shared.conversions.from_float;
    exports io.virtdata.libbasics.shared.conversions.from_int;
    exports io.virtdata.libbasics.shared.conversions.from_long;
    exports io.virtdata.libbasics.shared.conversions.from_short;
    exports io.virtdata.libbasics.shared.conversions.from_string;
    opens io.virtdata.libbasics.shared.conversions.from_double;
    opens io.virtdata.libbasics.shared.conversions.from_float;
    opens io.virtdata.libbasics.shared.conversions.from_int;
    opens io.virtdata.libbasics.shared.conversions.from_long;
    opens io.virtdata.libbasics.shared.conversions.from_short;
    opens io.virtdata.libbasics.shared.conversions.from_string;

    exports io.virtdata.libbasics.shared.from_long.to_time_types.joda;
    opens io.virtdata.libbasics.shared.from_long.to_time_types.joda;

    exports io.virtdata.libbasics.shared.formatting;
    opens io.virtdata.libbasics.shared.formatting;

    exports io.virtdata.libbasics.shared.functionadapters;
    opens io.virtdata.libbasics.shared.functionadapters;

    exports io.virtdata.libbasics.shared.nondeterministic;
    opens io.virtdata.libbasics.shared.nondeterministic;

    provides io.virtdata.services.FunctionFinderService
            with io.virtdata.libbasics.LibraryFunctionFinder;
    provides io.virtdata.services.ModuleDataService
            with io.virtdata.libbasics.BasicsModuleDataService;

}