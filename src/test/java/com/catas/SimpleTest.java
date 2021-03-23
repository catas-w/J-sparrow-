package com.catas;

import com.catas.webssh.utils.LogUtil;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Arrays;

public class SimpleTest {

    @Test
    public void tset1() {
        Byte a =23;
        // Integer.par
        // Arrays.sort;
        System.out.println(Integer.toString(5, 2));
    }

    @Test
    public void test2() {
        double v = 1.5e-4;
        long pow = (long) Math.pow(3, 3);
        "ssss".substring(1);
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

    @Test
    public void test3() {
        System.out.println(myPow(2, -2));
    }

    public double myPow(double x, int n) {
        if(n == 0)
            return 1;
        if (n == 1)
            return x;

        double res;
        if (n > 0) {
            res = myPow(x, n/2);
            res *= res;
            if (n % 2 == 1)
                res *= 2;
            return res;
        }else {
            res = myPow(x, -n/2);
            res *= res;
            if (-n % 2 == 1)
                res *= 2;
            return 1 / res;
        }
    }

    @Test
    public void isMatch() {
        System.out.println(reMatch("abc", "a*"));
        System.out.println(reMatch("aa", "a*"));
        System.out.println(reMatch("ab", ".*"));
        System.out.println(reMatch("aab", "c*a*b"));
        System.out.println(reMatch("ab", ".*c"));
        System.out.println(reMatch("mississippi", "mis*is*p*."));
    }

    public boolean reMatch(String str, String pattern) {
        if (str.length()==0 && pattern.length()==0) {
            return true;
        }
        if (str.length()!=0 && pattern.length()==0) {
            return false;
        }
        // 若 pattern 下一个字符是 *
        if (pattern.length() > 1 && pattern.charAt(1) == '*') {
            // 若当前字符匹配
            if (str.length()>0 && (str.charAt(0) == pattern.charAt(0) || pattern.charAt(0) == '.')) {
                return reMatch(str, pattern.substring(2))
                        || reMatch(str.substring(1), pattern.substring(2))
                        || reMatch(str.substring(1), pattern);
            }else {
                // 当前字符不匹配
                return reMatch(str, pattern.substring(2));
            }

        }
        if (str.length()>0 && (str.charAt(0) == pattern.charAt(0) ||  pattern.charAt(0) == '.')){
            return reMatch(str.substring(1), pattern.substring(1));
        }else {
            return false;
        }
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