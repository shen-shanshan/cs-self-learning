import java.util.*;

public class Solution_350 {

    /*方法一：哈希表
    1.由于同一个数字在两个数组中都可能出现多次，因此需要用哈希表存储每个数字出现的次数。
      对于一个数字，其在交集中出现的次数等于该数字在两个数组中出现次数的最小值。
    2.首先遍历第一个数组，并在哈希表中记录第一个数组中的每个数字以及对应出现的次数。
      然后遍历第二个数组，对于第二个数组中的每个数字，如果在哈希表中存在这个数字，则：
      将该数字添加到答案，并减少哈希表中该数字出现的次数。
    3.为了降低空间复杂度，首先遍历较短的数组并在哈希表中记录每个数字以及对应出现的次数，然后遍历较长的数组得到交集。
    */

    /*public int[] intersect(int[] nums1, int[] nums2) {
        // 将较短的数组存入 map 中（节省空间）
        if (nums1.length > nums2.length) {
            return intersect(nums2, nums1);
        }
        Map<Integer, Integer> map = new HashMap<Integer, Integer>();
        for (int num : nums1) {
            // getOrDefault：获取指定 key 对应的 value，如果找不到 key ，则返回设置的默认值。
            int count = map.getOrDefault(num, 0) + 1;
            map.put(num, count);
        }
        // 新建用于返回的数组
        int[] intersection = new int[nums1.length];
        int index = 0;
        for (int num : nums2) {
            int count = map.getOrDefault(num, 0);
            if (count > 0) {
                intersection[index++] = num;
                count--;
                if (count > 0) {
                    map.put(num, count);
                } else {
                    map.remove(num);
                }
            }
        }
        *//* Arrays.copyOfRange(T[ ] original,int from,int to):
        将一个原始的数组 original，从下标 from开始复制，复制到上标 to，生成一个新的数组。
        注意这里包括下标 from，不包括上标 to。
        比利用循环复制数组效率要高得多。
        *//*
        return Arrays.copyOfRange(intersection, 0, index);
    }*/

    /*方法二：双指针
    1.首先对两个数组进行排序，然后使用两个指针遍历两个数组。
    2.初始时，两个指针分别指向两个数组的头部。
      每次比较两个指针指向的两个数组中的数字，如果两个数字不相等，则将指向较小数字的指针右移一位。
      如果两个数字相等，将该数字添加到答案，并将两个指针都右移一位。
      当至少有一个指针超出数组范围时，遍历结束。
    */

    public int[] intersect(int[] nums1, int[] nums2) {
        // 分别对两个数组排序
        Arrays.sort(nums1);
        Arrays.sort(nums2);
        // 新建用于返回的数组
        int len = nums1.length > nums2.length ? nums1.length : nums2.length;
        int[] res = new int[len];
        // 双指针遍历
        int i = 0;
        int j = 0;
        int k = 0;
        while (i < nums1.length && j < nums2.length) {
            if (nums1[i] < nums2[j]) {
                i++;
            } else if (nums1[i] > nums2[j]) {
                j++;
            } else {
                res[k++] = nums1[i];
                i++;
                j++;
            }
        }
        return Arrays.copyOfRange(res, 0, k);
    }

    public static void main(String[] args) {
        int[] nums1 = {4, 9, 5};
        int[] nums2 = {9, 4, 9, 8, 4}; // 4, 9

        int[] nums3 = {1, 2, 2, 1};
        int[] nums4 = {2, 2}; // 2, 2

        Solution_350 s = new Solution_350();
        int[] res = s.intersect(nums1, nums2);
        for (int x : res) {
            System.out.println(x);
        }
    }
}
