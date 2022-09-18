package org.apache.tuweni.bytes;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

@State(Scope.Benchmark)
public class FromHexBenchmark {

    public static void main(String[] args) throws RunnerException {
        Options options = new OptionsBuilder()
                .include(FromHexBenchmark.class.getSimpleName())
                .build();
        new Runner(options).run();
    }

    static final int DATA_SET_SAMPLE_SIZE = 1024;

    String[] random_values;

    private String getRandomHexString(int numchars){
        Random r = new Random();
        StringBuilder sb = new StringBuilder();
        while(sb.length() < numchars){
            sb.append(Integer.toHexString(r.nextInt()));
        }

        return sb.substring(0, numchars);
    }

    @Setup
    public void setup() {

        Random random = new Random();
        random_values = new String[DATA_SET_SAMPLE_SIZE];
        for (int i = 0; i < DATA_SET_SAMPLE_SIZE; i++) {
            int size = (random.nextInt(16) * 2 + 16);
            random_values[i] = getRandomHexString(size);
            if (i % 2 == 0) {
                System.out.println(random_values[i]);
                byte[] expected = BytesValues.fromRawHexString(random_values[i], -1, false);
                assertArrayEquals(expected, BytesValues.fromFastRawHexString(random_values[i], -1, false));
            }
        }

    }

    @Benchmark
    public void fromHexOriginal(Blackhole bh) {
        for (String s : random_values) {
            bh.consume(BytesValues.fromRawHexString(s, -1, false));
        }
    }

    @Benchmark
    public void fromHexCandidate(Blackhole bh) {
        for (String s : random_values) {
            bh.consume(BytesValues.fromFastRawHexString(s, -1, false));
        }
    }

}
