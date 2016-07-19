package io.virtdata.mappers.generators;

import org.testng.annotations.Test;

import java.io.FileWriter;
import java.io.IOException;

@Test
public class LogNormalDistTest {

    @Test(enabled=false)
    public void testLogNormalVisually() throws IOException {
        LogNormalAdapter lna = new LogNormalAdapter(0,1.0);
        int size = 4000;
        double[] ds = new double[size];
        for (int i = 0; i < size; i++) {
            Double value = lna.apply(i);
            ds[i]=value;
            if ((i%100)==0) {
                System.out.println("i:" +i +", v:" + value);
                System.out.flush();
            }
        }
        FileWriter fw = new FileWriter("/tmp/data.csv");
        for (double d : ds) {
            fw.write(String.valueOf(d)+"\n");
        }
        fw.close();
    }

}