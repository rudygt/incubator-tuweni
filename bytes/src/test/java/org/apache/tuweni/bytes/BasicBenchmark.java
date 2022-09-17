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

import static org.apache.tuweni.bytes.Bytes.fromHexString;

@State(Scope.Benchmark)
public class BasicBenchmark {

    public static void main(String[] args) throws RunnerException {
        Options options = new OptionsBuilder()
                .include(BasicBenchmark.class.getSimpleName())
                .build();
        new Runner(options).run();
    }

    static final int DATA_SET_SAMPLE_SIZE = 1024;

    Bytes[] random_values;

    @Setup
    public void setup() {

        Random random = new Random();
        random_values = new Bytes[DATA_SET_SAMPLE_SIZE];
        for (int i = 0; i < DATA_SET_SAMPLE_SIZE; i++) {
            long value = random.nextLong();
            random_values[i] = fromHexString(String.format("%032x", Math.abs(value)));
        }

    }

    @Benchmark
    public void toHexOriginal(Blackhole bh) {
        for (Bytes s : random_values) {
            bh.consume(s.toHexString());
        }
    }

    @Benchmark
    public void toHexCandidate(Blackhole bh) {
        for (Bytes s : random_values) {
            bh.consume(s.toFastHexString());
        }
    }

    @Benchmark
    public void toHexByteBufferCandidate(Blackhole bh) {
        for (Bytes s : random_values) {
            bh.consume(s.toFastHexByteBufferString());
        }
    }

}
