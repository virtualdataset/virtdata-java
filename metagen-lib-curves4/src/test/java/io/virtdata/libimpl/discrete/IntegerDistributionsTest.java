package io.virtdata.libimpl.discrete;

import io.virtdata.api.DataMapper;
import io.virtdata.core.DataMapperFunctionMapper;
import io.virtdata.core.ResolvedFunction;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class IntegerDistributionsTest {

    @Test
    public void testResolveFunction() throws Exception {
        IntegerDistributions mrs = new IntegerDistributions();
        Optional<ResolvedFunction> resolved = mrs.resolveFunction("mapto_binomial(8,0.5)");
        assertThat(resolved).isPresent();
        ResolvedFunction f = resolved.get();
        DataMapper<Object> mapper = DataMapperFunctionMapper.map(f.getFunctionObject());
        Object o = mapper.get(234234);
        assertThat(o).isNotNull();
        assertThat(o).isOfAnyClassIn(Long.class);
    }

    @Test
    public void testGetDataMapperNames() throws Exception {
        IntegerDistributions mrs = new IntegerDistributions();
        List<String> names = mrs.getDataMapperNames();
        assertThat(names).contains("mapto_binomial");
    }

}