import java.util.Arrays;

public class Solution {
    public int findTheDistanceValue(int[] arr1, int[] arr2, int d) {
        int len1 = arr1.length;
        // 排序
        Arrays.sort(arr2);
        // 遍历 arr1，并统计满足条件的元素个数
        int count = 0;
        for (int i = 0; i < len1; i++) {
            int cur = arr1[i];
            // 二分查找
            if (isValid(cur, arr2, d)) {
                count++;
            }
        }
        return count;
    }

    public boolean isValid(int num,int[] arr, int d) {
        int left = 0;
        int right = arr.length-1;
        // 二分查找，在 arr 中找到与 num 最接近的数（取绝对值）
        while(left <= right){
            int mid = left + ((right-left)>>1);
            int cur = Math.abs(num-arr[mid]);
            if(cur <= d){
                return false;
            }
            // 若 cur > d，则继续找更小的距离
            if((mid-1 >= 0 && Math.abs(num-arr[mid-1]) > cur)
                    || mid+1 < arr.length && Math.abs(num-arr[mid+1]) < cur){
                // 在左边的坡上
                left = mid + 1;
            }else if(mid-1 >= 0 && Math.abs(num-arr[mid-1]) < cur){
                // 在右边的坡上
                right = mid - 1;
            }
        }
        return true;
    }
}
