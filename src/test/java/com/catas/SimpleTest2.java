package com.catas;

import org.apache.commons.collections.EnumerationUtils;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Array;
import java.util.*;

public class SimpleTest2 {


    @Test
    void testMergeSort() {
        Deque<Integer> dq = new LinkedList<>();
        // dq.poll();
        dq.offer(2);
        dq.offer(1);
        dq.offer(3);
        dq.add(4);
        dq.add(6);
        dq.add(5);
        dq.push(-1);
        dq.push(-2);
        System.out.println(dq.pop());
        System.out.println(dq.pop());
        System.out.println(dq.pop());
        int pow = (int) Math.pow(1, 5);
        // dq.pop()
    }

    @Test
    void testDices() {
        System.out.println(Arrays.toString(dicesProbability(2)));
    }

    public double[] dicesProbability(int n) {
        int min = n;
        int max = n * 6;
        double[] res = new double[max - min + 1];

        /*
          每一轮的组合数 f(n,s)=f(n-1,s-1)+f(n-1,s-2)+f(n-1,s-3)+f(n-1,s-4)+f(n-1,s-5)+f(n-1,s-6)
          p(n, s) = f(n, s) / 6^n
         */
        double total = Math.pow(n, 6);
        int[][] dp = new int[n][max + 1];

        for (int i=1; i<=6; i++) {
            dp[0][i] = 1;
        }
        for (int i=1; i<n; i++) {
            // 每一轮min=i+1, max=n*6
            for (int j=i+1; j<=max; j++) {
                for (int k=0; k<6; k++) {
                    dp[i][j] += (j-k >= 0) ? dp[i-1][j-k] : 0;
                }
            }
        }

        int index = 0;
        for (int i=min; i <= max; i++) {
            res[index++] = (double) (dp[n-1][i] / total);
        }
        return res;
    }


    int reversSort;

    void mergeSort(int[] nums, int[] temp, int left, int right) {
        if (left >= right) {
            return;
        }
        int mid = left + (right - left)/2;
        mergeSort(nums, temp, left, mid);
        mergeSort(nums, temp, mid + 1, right);
        merge(nums, temp, left, right, mid);
    }

    void merge(int[] nums, int[] temp, int left, int right, int mid) {
        int i = left;
        int j = mid + 1;
        int index = left;
        while (i <= mid && j <= right) {
            if (nums[i] > nums[j]) {
                // 逆序
                reversSort += mid - i + 1;
                temp[index++] = nums[j++];
            }else {
                temp[index++] = nums[i++];
            }
        }
        while (i <= mid) {
            temp[index++] = nums[i++];
        }
        while (j <= right) {
            temp[index++] = nums[j++];
        }

        for (int k = left; k <= right; k++) {
            nums[k] = temp[k];
        }
    }

    @Test
    void testSingNum() {
        // int[][] sequence = findContinuousSequence(9);
        // System.out.println(Arrays.deepToString(sequence));
        System.out.println(reverseWords("the sky is blue"));
        System.out.println(reverseWords("  hello world!  "));
        System.out.println(reverseWords("a good   example"));
    }

    public String reverseWords(String s) {
        String[] words = s.split(" +");
        if (words.length == 0) {
            return "";
        }
        int start = 0;
        int end = words.length - 1;
        while (start <= end) {
            // String prevWord = reverseWord(words[start]);
            // String latterWord = reverseWord(words[end]);
            String temp = words[start];
            words[start] = words[end];
            words[end] = temp;
            start ++;
            end --;
        }
        // "sss".trim();
        return String.join(" ", words).strip();
    }

    String reverseWord(String word) {
        if (word.length() == 0) {
            return "";
        }
        char[] list = new char[word.length()];
        int start = 0;
        int end = word.length() - 1;
        while (start <= end) {
            list[end] = word.charAt(start);
            list[start] = word.charAt(end);
            start ++;
            end --;
        }
        return String.valueOf(list);
    }

    public int[][] findContinuousSequence(int target) {
        List<int[]> resList = new ArrayList<>();
        int prev = 1;
        int latter = 2;
        int sum = prev;
        while (latter < target) {
            // 和大于目标值
            sum += latter;
            while (prev < latter && sum > target) {
                sum -= prev;
                prev ++;
            }
            if (sum == target) {
                // 添加结果
                int[] item = new int[latter - prev + 1];
                for (int i=prev; i<=latter; i++) {
                    item[i-prev] = i;
                }
                resList.add(item);
            }
            latter ++;
        }
        int[][] res = new int[resList.size()][];
        int index = 0;
        for (int[] item: resList) {
            res[index++] = item;
        }
        return res;
    }

    public int singleNumber(int[] nums) {
        int[] bitCount = new int[32];
        // 统计每个数字每个位的个数
        for (int n: nums) {
            int index = 0;
            int x = n;
            while (n > 0) {
                if ((n & 1) == 1) {
                    bitCount[index] += 1;
                }
                n >>= 1;
                index ++;
            }
        }
        int res = 0;
        for (int i = 31; i>=0; i--) {
            res <<= 1;
            if (bitCount[i] % 3 != 0)
                res |= 1;
        }
        return res;
    }

}
