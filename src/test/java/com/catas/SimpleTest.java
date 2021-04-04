package com.catas;

import com.catas.glimmer.util.SSHUtil;
import com.catas.webssh.utils.LogUtil;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.*;

public class SimpleTest {

    @Test
    public void tset1(int a, int b) {
        Queue<Integer> priorityQueue = new PriorityQueue<>(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return 0;
            }
        });
        Queue<Integer> pQueue = new PriorityQueue<>(new Comparator<Integer>() {
            public int compare(Integer o1, Integer o2) {
                return o2 - o1;
            }
        });
        // Math.abs(2-2)
        priorityQueue.add(5);
        priorityQueue.add(4);
        priorityQueue.add(8);
        priorityQueue.add(2);

        while (!priorityQueue.isEmpty())
            System.out.println(priorityQueue.poll());


    }

    @Test
    void test7() {
        // SSHUtil sshUtil = new SSHUtil("127.0.0.1", "catas", "eminem");
        // String log = sshUtil.execCommand("ls -lh; ls -a");
        // System.out.println(log);
    }

    @Test
    void testPer() {
        // System.out.println("30".compareTo("30"));
        // String[] a = new String[]{"10", "20", "2", "5"};
        List<String> list = Arrays.asList("3", "20", "2", "5", "03", "45", "7");
        list.sort(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                int len1 = o1.length();
                int len2 = o2.length();
                int i = 0;
                while (i < len1 && i < len2) {
                    if (o1.charAt(i) > o2.charAt(i)) {
                        return 1;
                    } else if (o1.charAt(i) < o2.charAt(i)) {
                        return -1;
                    }
                    i++;
                }
                if (len1 == len2)
                    return 0;
                return len1 > len2 ? -1 : 1;

            }
        });
        System.out.println(list);
    }

    @Test
    void test8() {
        // int[] ints = new int[4];
        // Arrays.fill(ints, -1);
        // System.out.println(Arrays.toString(ints));
        // HashMap<Character, Integer> map = new HashMap<>(70);
        System.out.println(lengthOfLongestSubstring("abcabcbb"));
        System.out.println(lengthOfLongestSubstring("bbbbb"));
        System.out.println(lengthOfLongestSubstring("pwwkew"));
        System.out.println(lengthOfLongestSubstring("pp"));
        System.out.println(lengthOfLongestSubstring("a"));
        System.out.println(lengthOfLongestSubstring(""));
        System.out.println(lengthOfLongestSubstring("abbcasdWDFdasvac"));
    }

    public char firstUniqChar(String s) {
        int[] count = new int[26];
        for (int i=0; i<s.length(); i++) {
            int pos = s.charAt(i) - 'a';
            count[pos] ++;
        }
        for (int i=0; i<26; i++) {
            if (count[i] == 1)
                return (char) ('a' + i);
        }
        return ' ';
    }

    public int lengthOfLongestSubstring(String s) {
        int len = s.length();
        if (len <= 1) {
            return len;
        }
        int[] lastPos = new int[128];
        int max = 0;
        int curLen = 0;
        Arrays.fill(lastPos, -1);
        for (int i=0; i<len; i++) {
            char chr = s.charAt(i);
            if (lastPos[chr] == -1) {
                curLen ++;
            }else if (i - lastPos[chr] <= curLen) {
                curLen = i - lastPos[chr];
            }else {
                curLen ++;
            }
            lastPos[chr] = i;
            max = Math.max(curLen, max);
        }
        return max;
    }

    public int translateNum(int num) {
        String str = String.valueOf(num);
        int len = str.length();
        int[] dp = new int[len + 1];
        dp[0] = 1;
        dp[1] = 1;
        for (int i = 2; i < len+1; i++) {
            int index = i - 1;
            // int x = (str.charAt(index - 1) - '0') * 10 + str.charAt(index) - '0';
            int x = Integer.parseInt(str.substring(index - 1, index + 1));
            if (x < 26) {
                dp[i] = dp[i-2] + dp[i-1];
            }else {
                dp[i] = dp[i-1];
            }
        }
        return dp[len];
    }

    public String minNumber(int[] nums) {
        String[] strs = new String[nums.length];
        for (int i=0; i<nums.length; i++) {
            strs[i] = String.valueOf(nums[i]);
        }
        Arrays.sort(strs, new Comparator<String>() {
            public int compare(String o1, String o2) {
                return (o1 + o2).compareTo(o2 + o1);
            }
        });
        System.out.println(Arrays.toString(strs));
        return String.join("", strs);
    }

    public String[] permutation(String s) {
        char[] str = s.toCharArray();
        Set<String> list = new HashSet<>();
        boolean[] visited = new boolean[str.length];
        StringBuilder builder = new StringBuilder();

        for (int i=0; i<str.length; i++) {
            backTrace(list, str, visited, i, builder);
        }
        String[] res = new String[list.size()];
        list.toArray(res);
        return res;
    }

    public void backTrace(Set<String> list, char[] str, boolean[] visited, int index, StringBuilder builder) {
        char curChar = str[index];
        builder.append(curChar);
        if (builder.length() == str.length) {
            list.add(builder.toString());
            builder.deleteCharAt(builder.length() - 1);
            return;
        }
        visited[index] = true;
        for (int i=0; i < str.length; i++) {
            if (!visited[i]) {
                backTrace(list, str, visited, i, builder);
            }
        }

        visited[index] = false;
        builder.deleteCharAt(builder.length() - 1);
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
        "sss".toCharArray();
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

    @Test
    void testOrder() {
        System.out.println(Arrays.toString(spiralOrder(new int[][]{{1, 2, 3}, {5, 6, 7, 8}, {9, 10, 11, 12}})));
        System.out.println(Arrays.toString(spiralOrder(new int[][]{{1}})));
        System.out.println(Arrays.toString(spiralOrder(new int[][]{{1,2},{2,6},{3,8}})));
        System.out.println(Arrays.toString(spiralOrder(new int[][]{{}})));
    }

    public int[] spiralOrder(int[][] matrix) {
        int rows = matrix.length;
        int cols = matrix[0].length;
        int[] path = new int[rows * cols];
        travelEdge(matrix, 0, rows-1, 0, cols-1, path, 0);
        return path;
    }

    public void travelEdge(int[][] matrix, int rowStart, int rowEnd, int colStart, int colEnd, int[] path, int index) {
        if ((rowStart > rowEnd) || (colStart > colEnd)) {
            return;
        }
        // 第一行
        for (int j = colStart; j <= colEnd; j++) {
            path[index++] = matrix[rowStart][j];
            if (index == path.length) {
                return;
            }
        }
        // last column
        for (int i = rowStart + 1; i <= rowEnd; i++) {
            path[index++] = matrix[i][colEnd];
            if (index == path.length) {
                return;
            }
        }
        // 最后行
        for (int j = colEnd - 1; j >= colStart; j--) {
            path[index++] = matrix[rowEnd][j];
            if (index == path.length) {
                return;
            }
        }
        // first column
        for (int i = rowEnd - 1; i >= rowStart + 1; i--) {
            path[index++] = matrix[i][colStart];
            if (index == path.length) {
                return;
            }
        }

        travelEdge(matrix, rowStart+1, rowEnd-1, colStart+1, colEnd-1, path, index);
    }

    @Test
    void stackSeq() {
        LinkedList<Integer> list = new LinkedList<>();
        // Arrays.
        // Collections.s
        System.out.println(validateStackSequences(new int[]{1, 2, 3, 4, 5}, new int[]{4, 3, 5, 1, 2}));
    }

    public boolean validateStackSequences(int[] pushed, int[] popped) {
        int index = 0;
        Deque<Integer> stack = new LinkedList<>();
        // 判断数字是否再栈中
        Set<Integer> set = new HashSet<>();
        for (int item: popped) {
            if (!set.contains(item)) {
                // 当前数字不在栈中,入栈到它在为止
                while(index<pushed.length && pushed[index] != item) {
                    stack.push(pushed[index]);
                    set.add(pushed[index]);
                    index ++;
                }
                index ++;
            }else if (stack.peek() == item) {
                // 在栈顶
                stack.pop();
                continue;
            }else {
                return false;
            }
        }

        return true;
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