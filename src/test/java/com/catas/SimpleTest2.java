package com.catas;

import org.apache.commons.collections.EnumerationUtils;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Array;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class SimpleTest2 {


    @Test
    void testMergeSort() {
        Path path = Paths.get("D:\\PY_Projects\\SpringBootProjects\\sparrow\\log\\tmp", "log.txt");
        System.out.println(path.toString());
        System.out.println(path.toFile().getName());
        System.out.println(path.toFile().getPath());
        System.out.println(path.toFile().exists());

    }

    @Test
    void testPal() {
        HashSet<List<Integer>> hashSet = new HashSet<>();
        hashSet.add(new ArrayList<>(Arrays.asList(1,2,3)));
        hashSet.add(new ArrayList<>(Arrays.asList(1,2,3)));
        hashSet.add(new ArrayList<>(Arrays.asList(2,1,3)));
        for (List<Integer> list : hashSet) {
            System.out.println(list);
        }
    }

    @Test
    void testTreeSum() {
        int[] ints = new int[]{12, 4, 6, 12, 4};
        // Arrays.sort(ints, 1,4);
        Arrays.sort(ints, 1,5);
        System.out.println(Arrays.toString(ints));
    }

    public List<String> generateParenthesis(int n) {
        List<String> res = new ArrayList<>();
        getRes(res, new StringBuilder(), n, n, new LinkedList<Integer>());
        return res;
    }

    public void getRes(List<String> res, StringBuilder builder,int left, int right, Deque<Integer> stack) {
        if (left == 0 && right == 0) {
            res.add(builder.toString());
            return;
        }
        if (left > 0) {
            stack.push(0);
            builder.append("(");
            getRes(res, builder, left-1, right, stack);
            //回溯
            stack.pop();
            builder.deleteCharAt(builder.length()-1);
        }


        if (right > 0) {
            if (!stack.isEmpty() && stack.peek() == 0) {
                stack.pop();
                builder.append(")");
                getRes(res, builder, left, right-1, stack);
                //回溯
                stack.push(0);
                builder.deleteCharAt(builder.length()-1);
            }
        }

    }

    public List<List<Integer>> threeSum(int[] nums) {
        if (nums.length < 3) {
            return new ArrayList<>();
        }

        Set<List<Integer>> tempRes = new HashSet<>();
        for (int i=0; i<nums.length; i++) {
            twoSum(nums, i, tempRes);
        }
        return new ArrayList<>(tempRes);
    }

    public void twoSum(int[] nums, int curNum, Set<List<Integer>> res) {
        int target = -nums[curNum];
        Map<Integer, Integer> map = new HashMap<>();
        for (int i=0; i != curNum && i<nums.length; i++) {
            if (map.containsKey(nums[i])) {
                List<Integer> curRes = new ArrayList<>(Arrays.asList(nums[curNum], nums[i], map.get(nums[i])));
                Collections.sort(curRes);
                res.add(curRes);
            } else {
                map.put(target - nums[i], nums[i]);
            }
        }
    }

    public String longestPalindrome(String s) {
        int len = s.length();
        int[] dp = new int[len];
        dp[0] = 1;
        int maxPos = 0;
        Arrays.fill(dp, 1);

        int maxSamePos = 0;
        int[] allSame = new int[len];
        Arrays.fill(allSame, 1);
        for (int i=1; i<len; i++) {
            char chr = s.charAt(i);

            int counter0 = i - 2;
            if (counter0 >=0 && s.charAt(counter0) == chr) {
                // 三元序列
                dp[i] = 3;
            }

            if (chr == s.charAt(i-1)) {
                // 全同序列
                allSame[i] = allSame[i-1] + 1;
            }
            if (allSame[i] > allSame[maxSamePos]) {
                maxSamePos = i;
            }

            // 一般序列
            int counter = i - dp[i-1] - 1;
            if (counter >= 0 && s.charAt(counter) == chr) {
                dp[i] = Math.max(dp[i], dp[i-1] + 2);
            }

            if (dp[i] > dp[maxPos]) {
                maxPos = i;
            }
        }
        if (dp[maxPos] > dp[maxSamePos])
            return s.substring(maxPos - dp[maxPos] + 1, maxPos+1);
        else
            return s.substring(maxSamePos - allSame[maxSamePos] + 1, maxSamePos);
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
        double total = Math.pow(6, n);
        int[][] dp = new int[n][max + 1];

        for (int i=1; i<=6; i++) {
            dp[0][i] = 1;
        }
        for (int i=1; i<n; i++) {
            // 每一轮min=i+1, max=n*6
            for (int j=i+1; j<=max; j++) {
                for (int k=1; k<=6; k++) {
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
        System.out.println(Arrays.toString(maxSlidingWindow(new int[]{1, 3, -1, -3, 5, 3, 6, 7}, 3)));
    }

    public int[] maxSlidingWindow(int[] nums, int k) {
        if (nums.length == 0) {
            return new int[0];
        }
        // 单调队列
        Queue<Integer> queue = new LinkedList<>();
        Deque<Integer> maxQueue = new LinkedList<>();
        int[] res = new int[nums.length - k + 1];
        // 初始化队列
        for (int i=0; i<k; i++) {
            queue.add(nums[i]);
            while (!maxQueue.isEmpty() && nums[i] > maxQueue.peekLast()) {
                maxQueue.pollLast();
            }
            maxQueue.addLast(nums[i]);
        }
        // 滑动
        int index = 0;
        res[index++] = maxQueue.peekFirst();
        for (int i=k; i<nums.length; i++) {

            int val = queue.poll();
            if (val == maxQueue.peekFirst()) {
                maxQueue.pollFirst();
            }
            queue.add(nums[i]);
            while (!maxQueue.isEmpty() && nums[i] > maxQueue.peekLast()) {
                maxQueue.pollLast();
            }
            maxQueue.addLast(nums[i]);

            res[index++] = maxQueue.peekFirst();
        }
        return res;
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
