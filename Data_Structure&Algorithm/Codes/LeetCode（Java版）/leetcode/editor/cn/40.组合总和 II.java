package editor.cn;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

// 40.组合总和 II
class Solution40 {

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {

        List<List<Integer>> ans = new LinkedList<>();

        List<Integer> path = new LinkedList<>();

        public List<List<Integer>> combinationSum2(int[] candidates, int target) {
            Arrays.sort(candidates);
            backtrack(candidates,target,0,0);
            return ans;
        }

        public void backtrack(int[] arr,int target,int index,int sum){
            // 终止条件
            if(sum == target){
                ans.add(new LinkedList<>(path));
                return;
            }

            // 遍历
            for (int i = index; i < arr.length; i++) {
                // 剪枝
                if(sum+arr[i] > target){
                    break;
                }

                // 对每一个树层去重
                if(i > index && arr[i] == arr[i-1]){
                    continue;
                }

                path.add(arr[i]);
                sum += arr[i];
                // 递归
                backtrack(arr,target,i+1,sum);
                // 回溯
                sum -= arr[i];
                path.remove(path.size()-1);
            }
        }
    }
    //leetcode submit region end(Prohibit modification and deletion)

}