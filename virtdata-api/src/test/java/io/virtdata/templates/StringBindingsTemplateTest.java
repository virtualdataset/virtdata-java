package io.virtdata.templates;

import io.virtdata.core.BindingsTemplate;
import org.testng.annotations.Test;

public class StringBindingsTemplateTest {

    @Test(expectedExceptions = RuntimeException.class, expectedExceptionsMessageRegExp = ".*not provided in the bindings: \\[two, three\\]")
    public void testUnqualifiedBindings() {
        BindingsTemplate bt1 = new BindingsTemplate();
        bt1.addFieldBinding("one", "Identity()");
        String template="{one} {two} {three}\n";
        StringBindingsTemplate sbt = new StringBindingsTemplate(template,bt1);
        StringBindings resolved = sbt.resolve();
    }

}