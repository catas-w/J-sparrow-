package com.catas;

import com.catas.webssh.utils.LogUtil;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

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

    @Test
    void testIsNum() {
        // System.out.println(isNumber("233"));
        // System.out.println(isNumber("2.33"));
        // System.out.println(isNumber("233e3"));
        // System.out.println(isNumber("+100"));
        // System.out.println(isNumber("-100"));
        // System.out.println(isNumber("+0123"));
        // System.out.println(isNumber("2.3e3"));
        // System.out.println(isNumber("2a3.2"));
        // System.out.println(isNumber("2.3.3"));
        // System.out.println(isNumber("23e3.3"));
        System.out.println(isNumber(".1"));
        System.out.println(isNumber(". 1"));
        System.out.println(isNumber(" 1.e-3"));
        System.out.println(isNumber(" e9"));
    }

    public boolean isNumber(String s) {
        s = s.strip();
        int len = s.length();
        if (len == 0) {
            return false;
        }
        int pos = 0;
        int next = scanInteger(s, pos);
        if (next == len && next > pos) {
            // 扫描数字后且指针到头
            return true;
        }
        if (s.charAt(next) == '.') {
            next ++;
            int nextDot = scanUnsignInteger(s, next); // 数组越界风险
            if (nextDot == len) {
                // 数组到头
                return ! (nextDot == next);
            }
            next = nextDot;
        }
        if (s.charAt(next) == 'e' || s.charAt(next) == 'E') {
            next ++;
            int nextE = scanInteger(s, next);
            if (next == nextE) {
                // e 后面直接到头
                return false;
            }
            next = nextE;
        }
        return next == len;
    }

    public int scanUnsignInteger(String s, int pos) {
        // 扫描无符号整数
        int p = pos;
        while(p < s.length() && s.charAt(p) >= '0' && s.charAt(p) <= '9') {
            p ++;
        }
        return p;
    }

    public int scanInteger(String s, int pos) {
        if (pos == s.length()) {
            return pos;
        }
        if (s.charAt(pos) == '+' || s.charAt(pos) == '-') {
            pos++;
        }
        return scanUnsignInteger(s, pos);
    }

    @Test
    void testExchange() {

        System.out.println(Arrays.toString(exchange(new int[]{1, 2, 3, 4, 5})));
        System.out.println(Arrays.toString(exchange(new int[]{1, 2})));
        System.out.println(Arrays.toString(exchange(new int[]{1, 1})));
        System.out.println(Arrays.toString(exchange(new int[]{2,2,2})));
        System.out.println(Arrays.toString(exchange(new int[]{})));
        System.out.println(Arrays.toString(exchange(new int[]{1})));
        System.out.println(Arrays.toString(exchange(new int[]{2,0,0,1})));
        System.out.println(Arrays.toString(exchange(new int[]{2,2,21,1,1})));
    }

    public int[] exchange(int[] nums) {
        int head = 0;
        int tail = nums.length - 1;
        while (head < tail) {
            while (head <= tail && head < nums.length && isOdd(nums[head])) {
                head ++;
            }
            while (tail >= head && tail >= 0 && !isOdd(nums[tail])) {
                tail --;
            }
            if (tail > head) {
                int temp = nums[head];
                nums[head] = nums[tail];
                nums[tail] = temp;
            }
        }
        return nums;
    }

    public boolean isOdd(int x) {
        return (x % 2) == 1;
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