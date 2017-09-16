/*
*   Copyright 2016 jshook
*   Licensed under the Apache License, Version 2.0 (the "License");
*   you may not use this file except in compliance with the License.
*   You may obtain a copy of the License at
*
*       http://www.apache.org/licenses/LICENSE-2.0
*
*   Unless required by applicable law or agreed to in writing, software
*   distributed under the License is distributed on an "AS IS" BASIS,
*   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
*   See the License for the specific language governing permissions and
*   limitations under the License.
*/

package io.virtdata;

import io.virtdata.api.DataMapper;
import io.virtdata.core.AllDataMapperLibraries;
import io.virtdata.core.Bindings;
import io.virtdata.core.BindingsTemplate;
import org.testng.annotations.Test;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class ComposerLibraryTest {

//    @Test
//    public void testTypeConversionChain() {
//        BindingsTemplate bt = new BindingsTemplate(AllDataMapperLibraries.getInverseCumulativeDensity());
//        bt.addFieldBinding("mod_to_string", "compose Mod(3) ; Suffix('0000000000') -> String");
//        Bindings bindings = bt.resolveBindings();
//        Object[] all = bindings.getAll(5);
//        assertThat(all).isNotNull();
//        assertThat(all.length).isEqualTo(1);
//        Object o = all[0];
//        assertThat(o.getClass()).isEqualTo(String.class);
//        assertThat((String) o).isEqualTo("20000000000");
//        // RandomToByteBuffer(1048576) ; ToString()
//    }
//


    @Test(enabled=true)
    public void testArgumentMatchingViaMainLib() {
        BindingsTemplate bt = new BindingsTemplate(AllDataMapperLibraries.get());
        bt.addFieldBinding("param","RandomLineToString(data/variable_words.txt)");
        Bindings bindings = bt.resolveBindings();
        Object[] all = bindings.getAll(5);
        assertThat(all).isNotNull();
        assertThat(all.length).isEqualTo(1);
        Object o = all[0];
        assertThat(o.getClass()).isEqualTo(String.class);
    }

    @Test(enabled=true)
    public void testChainedTypeResolutionForLong() {
        BindingsTemplate bt = new BindingsTemplate(AllDataMapperLibraries.get());
        bt.addFieldBinding("longchain", "compose CycleRange(123456789) ; Div(3); Mod(7) -> long");
        Bindings bindings = bt.resolveBindings();
    }

    @Test(enabled=true)
    public void testChainedTypeResolutionForWithInternalLong() {
        BindingsTemplate bt = new BindingsTemplate(AllDataMapperLibraries.get());
        bt.addFieldBinding("longchain", "compose HashRange(1234,6789) -> long; Mod(3) -> int;");
        Bindings bindings = bt.resolveBindings();
        Object n1 = bindings.getAll(123)[0];
        assertThat(n1).isOfAnyClassIn(Integer.class);
    }

    @Test(enabled=true)
    public void testChainedTypeResolutionForInt() {
        BindingsTemplate bt = new BindingsTemplate(AllDataMapperLibraries.get());
        bt.addFieldBinding("intchain", "compose ToInt() ; CycleRange(123456789) ; Div(3) ; Mod(7) -> int");
        Bindings bindings = bt.resolveBindings();
    }

    @Test
    public void testStringConversion() {
        BindingsTemplate bt = new BindingsTemplate(AllDataMapperLibraries.get());
        bt.addFieldBinding("phone","compose HashRange(1000000000,9999999999); ToString() -> String");
        Bindings bindings = bt.resolveBindings();
    }

    @Test
    public void testPrefixSuffix() {
        BindingsTemplate bt = new BindingsTemplate(AllDataMapperLibraries.get());
        bt.addFieldBinding("solr_query","compose HashRange(1000000000,9999999999); ToString(); Prefix(before); Suffix(after) -> String");
        Bindings bindings = bt.resolveBindings();
    }

    // TODO: Fix this test
    @Test(enabled=false)
    public void testTypeCoercionWhenNeeded() {
        BindingsTemplate bt = new BindingsTemplate(AllDataMapperLibraries.get());
        bt.addFieldBinding("mod_to_string", "compose Mod(3) ; Suffix('0000000000') -> String");
        Bindings bindings = bt.resolveBindings();
        Object[] all = bindings.getAll(5);
        assertThat(all).isNotNull();
        assertThat(all.length).isEqualTo(1);
        Object o = all[0];
        assertThat(o.getClass()).isEqualTo(String.class);
        assertThat((String) o).isEqualTo("20000000000");
    }

    // TODO: Fix this test
    @Test(enabled=false)
    public void testBasicRange() {
        BindingsTemplate bt = new BindingsTemplate(AllDataMapperLibraries.get());
        bt.addFieldBinding("phone","HashRange(1000000000, 9999999999)");
        Bindings bindings = bt.resolveBindings();
    }

    @Test
    public void testUUIDChain() {
        Optional<DataMapper<Object>> dm =
                AllDataMapperLibraries.get()
                        .getDataMapper("compose Mod(1000); ToHashedUUID() -> UUID");
        assertThat(dm).isPresent();
        Object o = dm.get().get(5L);
        assertThat(o).isEqualTo(UUID.fromString("3df498b1-9568-4584-96fd-76f6081da01a"));
    }

    @Test
    public void testNormalDoubleAdd() {
        Optional<DataMapper<String>> dm =
                AllDataMapperLibraries.get()
                        .getStringDataMapper("compose normal(0.0,5.0); Add(5.0) -> double");
        assertThat(dm).isPresent();
    }

    @Test
    public void testDistInCompose() {
        Optional<DataMapper<String>> dm =
                AllDataMapperLibraries.get()
                .getStringDataMapper("compose Hash(); uniform_integer(0,100); ToString() -> String");
        assertThat(dm).isPresent();
        String s = dm.get().get(5L);
        assertThat(s).isNotEmpty();
        assertThat(s).isEqualTo("78");
    }

    @Test
    public void testComposeSingleFuncTypeCoercion() {
        Optional<DataMapper<Object>> longMapper =
                AllDataMapperLibraries.get()
                        .getDataMapper("compose uniform_integer(1,10) -> long");
        assertThat(longMapper).isPresent();
        Object l = longMapper.get().get(23L);
        assertThat(l).isNotNull();
        assertThat(l.getClass()).isEqualTo(Long.class);

        Optional<DataMapper<Object>> intMapper =
                AllDataMapperLibraries.get()
                        .getDataMapper("compose uniform_integer(1,123) -> int");
        assertThat(intMapper).isPresent();
        Object i = intMapper.get().get(23L);
        assertThat(i).isNotNull();
        assertThat(i.getClass()).isEqualTo(Integer.class);

    }
}