package com.catas;

import org.apache.commons.collections.EnumerationUtils;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Array;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

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
    void testLRU() {

        System.out.println(leastInterval(new char[]{'A','A','A','B','B','B'}, 2));
        System.out.println(leastInterval(new char[]{'A','A','A','B','B','B'}, 0));
        System.out.println(leastInterval(new char[]{'A','A','A','B','B','B'}, 1));
        System.out.println(leastInterval(new char[]{'A','A','A','A','A','A','B','C','D','E','F','G'}, 2));
    }


    public int leastInterval(char[] tasks, int n) {
        int len = tasks.length;
        if (n == 0)
            return len;

        Map<Character, Integer> map = new ConcurrentHashMap<>();


        for (char chr: tasks) {
            map.put(chr, map.getOrDefault(chr, 0) + 1);
        }

        int count = len;
        while (!map.isEmpty()) {
            int curSize = n + 1;

            for (Map.Entry<Character, Integer> curItem : map.entrySet()) {
                Character chr = curItem.getKey();
                Integer curCount = curItem.getValue();
                if (curCount == 1)
                    map.remove(chr);
                else
                    map.put(chr, curCount - 1);

                curSize--;
                if (curSize == 0)
                    break;
            }
            if (!map.isEmpty())
                count += curSize;
        }

        return count;
    }

    @Test
    void testSubArray() {
        System.out.println(Arrays.asList(subarraySum(new int[]{1,1,2,-1,1,2,-2,-1,2,1,-1,1}, 3)));
    }

    public int subarraySum(int[] nums, int k) {

        Map<Integer, Integer> map = new HashMap<>();
        map.put(0, 1);

        int[] sumList = new int[nums.length];
        int sum = 0;

        for (int i=0; i<nums.length; i++) {
            sum += nums[i];
            sumList[i] = sum;
            map.put(sum, map.getOrDefault(sum, 0) + 1);
        }

        int res = 0;
        for (int i=0; i<nums.length; i++) {
            int curSum = sumList[i];
            if (map.containsKey(curSum - k))
                res += map.get(curSum - k);
        }

        return res;
    }


    @Test
    void testPal() {
        System.out.println(maximalRectangle(new char[][]{{'1', '0', '1', '0', '0'}, {'1', '0', '1', '1', '1'}, {'1', '1', '1', '1', '1'}, {'1', '0', '0', '1', '0'}}));
    }

    public int maximalRectangle(char[][] matrix) {
        int row = matrix.length;
        int col = matrix[0].length;
        int maxArea = 0;
        if (row == 0) {
            return 0;
        }
        int[] height = new int[col];
        for (int i=0; i<row; i++) {
            for (int j=0; j<col; j++) {
                height[j] = matrix[i][j] == '0' ? 0 : height[j] + 1;
            }
            maxArea = Math.max(maxArea, maxHistgram(height));
        }

        return maxArea;
    }

    public int maxHistgram(int[] height) {
        Deque<Integer> stack = new LinkedList<>();
        int[] temp = new int[height.length + 2];
        int res = 0;

        for (int i=1; i<temp.length-1; i++) {
            temp[i] = height[i-1];
        }

        for (int i=0; i<temp.length; i++) {
            int curHeight = temp[i];

            while (!stack.isEmpty() && curHeight < temp[stack.peek()]) {
                int lastPop = stack.pop();
                int curArea = temp[lastPop] * (i - stack.peek() - 1);
                res = Math.max(res, curArea);
            }
            stack.push(i);
        }

        return res;
    }

    public String minWindow(String s, String t) {
        int[] countS = new int[128];
        int[] countT = new int[128];
        long hashS = 0, hashT = 0;

        for (int i=0; i<t.length(); i++) {
            char chr = t.charAt(i);
            countT[chr] = 1;
            hashT ^= 1L << (chr - 'a');
        }

        for (int j=0; j<s.length(); j++) {
            char chr = s.charAt(j);
            countS[chr] += 1;
            hashS ^= 1L << (chr - 'a');
        }

        for (int k = 0; k<128; k++) {
            if (countT[k] == 1 && countS[k] == 0)
                return "";
        }

        int left = 0;
        int right = s.length() - 1;

        while (true) {
            char chr = s.charAt(left);
            countS[chr] --;
            if (countS[chr] <= 0 && countT[chr] == 1) {
                break;
            }
            left ++;
        }
        while(true) {
            char chr = s.charAt(right);
            countS[chr] --;
            if (countS[chr] <=0 && countT[chr] == 1){
                break;
            }
            right --;
        }

        return s.substring(left, right + 1);
    }

    public boolean canJump(int[] nums) {
        int len = nums.length;
        boolean[] memo = new boolean[len];
        Arrays.fill(memo, true);
        return jump(nums, memo, len-1);
    }

    public boolean jump(int[] nums, boolean[] memo, int last) {
        if (last == 0) {
            return true;
        }
        if (!memo[last])
            return false;

        int val = nums[last];
        for (int i=last-1; i>=0; i--) {
            if (nums[i] >= last-i) {
                if (jump(nums, memo, i))
                    return true;
                else
                    memo[i] = false;
            }
        }
        return false;
    }

    public int trap(int[] height) {
        // 单调栈
        int len = height.length;
        if (len == 0) {
            return 0;
        }

        int sum = 0;
        Deque<Integer> stack = new LinkedList<>();
        for(int i=0; i<len; i++) {
            while (!stack.isEmpty() && height[i] > height[stack.peek()]) {
                int lastPopped = stack.pop();
                while (!stack.isEmpty() && height[stack.peek()] == height[lastPopped]) {
                    stack.pop();
                }
                if (!stack.isEmpty()) {
                    int topVal = stack.peek();
                    int deltaHeight = Math.min(height[i], height[topVal]) - height[lastPopped];
                    int width = i - topVal - 1;
                    int addArea = width * deltaHeight;
                    sum += addArea;
                }
            }

            stack.push(i);
        }
        return sum;
    }

    public List<List<String>> groupAnagrams(String[] strs) {
        // int n = strs.length;
        List<List<String>> res = new ArrayList<>();
        Map<String, List<String>> map = new HashMap<>();
        for (String s: strs) {
            char[] chars = s.toCharArray();
            Arrays.sort(chars);
            String hashCode = String.valueOf(chars);
            if(!map.containsKey(hashCode)) {
                map.put(hashCode, new ArrayList<>(Arrays.asList(s)));
            }else {
                map.get(hashCode).add(s);
            }
        }

        for(String key: map.keySet()) {
            res.add(map.get(key));
        }
        return res;
    }

    public int search(int[] nums, int target) {
        if (nums.length == 1) {
            return nums[0] == target ? 0 : -1;
        }
        // 找到最小值下标
        int min = findMin(nums);


        if (target <= nums[nums.length-1]){
            return binarySearch(nums, target, min, nums.length-1);
        }else if (target >= nums[0]){
            return binarySearch(nums, target, 0, min-1);
        }else {
            return -1;
        }
    }

    public int findMin(int[] nums) {
        int first = nums[0];
        int start = 0, end = nums.length-1;
        if (nums[start] <= nums[end]) {
            return start;
        }

        while(start < end) {
            int mid = (start + end) / 2;
            if (nums[mid] >= first) {
                start = mid + 1;
            }else {
                end = mid;
            }
        }
        return start;
    }

    public int binarySearch(int[] nums, int target, int start, int end) {
        if (start > end) {
            return -1;
        }
        if (start == end) {
            return nums[start] == target ? start : -1;
        }
        int mid = (start + end) / 2;
        if (nums[mid] >= target) {
            return binarySearch(nums, target, start, mid);
        }else {
            return binarySearch(nums, target, mid+1, end);
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
