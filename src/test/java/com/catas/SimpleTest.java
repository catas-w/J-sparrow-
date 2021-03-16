package com.catas;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

public class SimpleTest {

    @Test
    public void tset1() {
        Byte a =23;
        System.out.println(Integer.toString(5, 2));
    }

    @Test
    public void test2() {
        System.out.println(Arrays.toString(findRedundantConnection(new int[][]{{1, 2}, {1, 3}, {2, 3}})));
    }

    public int[] findRedundantConnection(int[][] edges) {
        int n = edges.length;
        UF unionSet = new UF(n);
        for (int[] item: edges) {
            if (unionSet.isSameRoot(item[0], item[1])) {
                return item;
            }else {
                unionSet.union(item[0], item[1]);
            }
        }
        Integer.bitCount(23);
        return null;
    }

    public int reverseBits(int n) {
        int res = 0;
        for (int i=0; i<32; i++) {
            res <<= 1;
            res |= (n|1);
            n |= 1;
        }
        return res;
    }
}


class UF {

    int[] rootNode;

    public UF(int n) {
        rootNode = new int[n+1];
        // 每个元素的根节点为自己
        for (int i=1; i<n+1; i++) {
            rootNode[i] = i;
        }
    }

    public boolean isSameRoot(int low, int high) {
        return rootNode[low] == rootNode[high];
    }

    public void union(int low, int high) {
        // 根与low 相同的全部指向 high
        int flag = rootNode[low];
        // if (rootNode[low] == rootNode[high])
        //     return;
        for (int i=1; i<rootNode.length; i++) {
            if (rootNode[i] == flag)
                rootNode[i] = rootNode[high];
        }
    }
}