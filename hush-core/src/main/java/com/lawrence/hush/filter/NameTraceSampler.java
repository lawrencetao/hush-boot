package com.lawrence.hush.filter;

import org.springframework.cloud.sleuth.Sampler;
import org.springframework.cloud.sleuth.Span;
import org.springframework.cloud.sleuth.sampler.SamplerProperties;

import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * sleuth收集策略
 */
public class NameTraceSampler implements Sampler {
    private final AtomicInteger counter = new AtomicInteger(0);
    private final BitSet sampleDecisions;
    private final SamplerProperties configuration;

    private static Map<String, String> nameStrategy;
    static {
        nameStrategy = new HashMap<>();
        nameStrategy.put("send-metrics", "");
        nameStrategy.put("gather-metrics", "");
        nameStrategy.put("rxjava", "");
    }

    public NameTraceSampler(SamplerProperties configuration) {
        int outOf100 = (int)(configuration.getPercentage() * 100.0F);
        this.sampleDecisions = randomBitSet(100, outOf100, new Random());
        this.configuration = configuration;
    }

    @Override
    public boolean isSampled(Span span) {
        String name = span.getName();

        // 当name为指定值时, 当前span不收集到zipkin
        if (nameStrategy.containsKey(name)) {

            return false;
        }

        // 按百分比收集
        if (this.configuration.getPercentage() != 0.0F && span != null) {
            if (this.configuration.getPercentage() == 1.0F) {
                return true;
            } else {
                synchronized(this) {
                    int i = this.counter.getAndIncrement();
                    boolean result = this.sampleDecisions.get(i);
                    if (i == 99) {
                        this.counter.set(0);
                    }

                    return result;
                }
            }
        } else {
            return false;
        }
    }

    static BitSet randomBitSet(int size, int cardinality, Random rnd) {
        BitSet result = new BitSet(size);
        int[] chosen = new int[cardinality];

        int i;
        for(i = 0; i < cardinality; ++i) {
            chosen[i] = i;
            result.set(i);
        }

        for(; i < size; ++i) {
            int j = rnd.nextInt(i + 1);
            if (j < cardinality) {
                result.clear(chosen[j]);
                result.set(i);
                chosen[j] = i;
            }
        }

        return result;
    }
}
