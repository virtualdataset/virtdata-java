package io.basics.virtdata.libraryimpl.composers;

import io.basics.virtdata.api.ValueType;
import io.basics.virtdata.api.specs.SpecData;
import io.basics.virtdata.api.specs.Specifier;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class MultiSpecData implements Specifier {
    private final LinkedList<SpecData> specDataList = new LinkedList<>();
    private String preamble;

    private MultiSpecData(String preamble, List<SpecData> specDataList) {
        this.preamble = preamble;
        this.specDataList.addAll(specDataList);
    }

    public static Optional<MultiSpecData> forOptionalSpec(String preamble, String multispec) {
        if (!multispec.startsWith(preamble)) {
            return Optional.empty();
        }
        multispec=multispec.substring(preamble.length());

        List<SpecData> specDataList = new ArrayList<>();
        String[] split = multispec.split("\\s*;\\s*");
        for (String s : split) {
            Optional<SpecData> sd = SpecData.forOptionalSpec(s);
            if (!sd.isPresent()) {
                return Optional.empty();
            }
            specDataList.add(sd.get());
        }
        MultiSpecData multiSpecData = new MultiSpecData(preamble, specDataList);
        return Optional.of(multiSpecData);
    }

    public Optional<ValueType> getResultType() {
        Optional<ValueType> resultType = specDataList.getLast().getResultType();
        return resultType;
    }

    @Override
    public String getCanonicalSpec() {
        return specDataList.stream()
                .map(SpecData::getCanonicalSpec)
                .collect(Collectors.joining(";"));
    }

    public List<SpecData> getSpecs() {
        return new LinkedList<SpecData>(specDataList);
    }

    public static MultiSpecData forSpec(String preamble, String multispec) {
        Optional<MultiSpecData> multiSpecData = forOptionalSpec(preamble,multispec);
        return multiSpecData.orElseThrow(() -> new RuntimeException("Unable to parse multispec: " + multispec));
    }
}
