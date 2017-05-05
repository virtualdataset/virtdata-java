/*
*   Copyright 2017 jshook
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

package io.virtdata.core;

import org.testng.annotations.Test;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@Test
public class BindingsTest {

    @Test
    public void testMapResult() {
        Bindings bindings = new BindingsTemplate(AllDataMapperLibraries.get()) {{
            addFieldBinding("mod5","Mod(5)");
            addFieldBinding("mod7","Mod(7)");
        }}.resolveBindings();

        Map<String, Object> map = bindings.getLazyMap(12);
        assertThat(map.get("mod5")).isEqualTo(2L);
        assertThat(map.get("mod7")).isEqualTo(5L);
    }

    @Test
    public void testMapSetter() {
        Bindings bindings = new BindingsTemplate(AllDataMapperLibraries.get()) {{
            addFieldBinding("mod5","Mod(5)");
            addFieldBinding("mod7","Mod(7)");
        }}.resolveBindings();

        LinkedHashMap<String,Object> map = new LinkedHashMap<>();
        bindings.setMap(map,12);
        assertThat(map.size()).isEqualTo(2);
        assertThat(map).containsEntry("mod5",2L);
        assertThat(map).containsEntry("mod7",5L);
    }

    @Test
    public void testFieldSetter() {
        Bindings bindings = new BindingsTemplate(AllDataMapperLibraries.get()) {{
            addFieldBinding("mod5","Mod(5)");
            addFieldBinding("mod7","Mod(7)");
        }}.resolveBindings();

        final StringBuilder sb = new StringBuilder();
        Bindings.FieldSetter fs = new Bindings.FieldSetter() {
            @Override
            public void setField(String name, Object value) {
                sb.append(name).append("=").append(value).append(";");
            }
        };

        bindings.setFields(fs,12);
        assertThat(sb.toString()).isEqualTo("mod5=2;mod7=5;");
    }
}