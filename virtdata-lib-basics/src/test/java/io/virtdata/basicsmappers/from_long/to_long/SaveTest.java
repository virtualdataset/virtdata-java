package io.virtdata.basicsmappers.from_long.to_long;

import io.virtdata.basicsmappers.from_long.to_string.NumberNameToString;
import io.virtdata.basicsmappers.stateful.Clear;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SaveTest {

    @Test(singleThreaded = true)
    public void testSaveAndLoadByGeneratedName() {
        new Clear().applyAsLong(0L);
        long l1=234L;
        long l2=123L;

        NumberNameToString numberNameToString = new NumberNameToString();
        String two34 = numberNameToString.apply(l1);
        assertThat(two34).isEqualTo("two hundred and thirty four");
        String one23 = numberNameToString.apply(l2);
        assertThat(one23).isEqualTo("one hundred and twenty three");

        Save save = new Save(numberNameToString);
        long lv1=save.applyAsLong(l1);
        assertThat(lv1).isEqualTo(l1);
        long lv2=save.applyAsLong(l2);
        assertThat(lv2).isEqualTo(l2);

        Load load = new Load(numberNameToString);
        long lv3 = load.applyAsLong(l1);
        assertThat(lv3).isEqualTo(l1);
        long lv4 = load.applyAsLong(l2);
        assertThat(lv4).isEqualTo(l2);



    }

}