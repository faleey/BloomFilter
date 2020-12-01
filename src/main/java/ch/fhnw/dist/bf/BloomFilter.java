package ch.fhnw.dist.bf;

import java.nio.charset.Charset;
import java.util.BitSet;
import java.util.Set;

import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;

/**
 * Implementation on bloom-filter with Murmur3_128
 */
public class BloomFilter {
    Charset charset = Charset.defaultCharset();
    BitSet bitSet;
    int n, k, m;

    public BloomFilter(int n, int k, int m){
        this.n = n;
        this.k = k;
        this.m = m;

        bitSet = new BitSet(m);
    }

    public void addAll(Set<String> list){
        for (String i : list) {
            add(i);
        }
    }

    public void add(String s){
        for (int i : getTrueBits(s)) {
            bitSet.set(i);
        }
    }

    public boolean contains(String s){
        for (int i : getTrueBits(s)) {
            if(!bitSet.get(i))
                return false;
        }

        return true;
    }

    private int[] getTrueBits(String s){
        int[] result = new int[k];

        for(int i = 0; i < k; i++){
            result[i] = Math.abs(Hashing.murmur3_128(i).hashString(s, charset).asInt()) % m;
        }

        return result;
    }
}
