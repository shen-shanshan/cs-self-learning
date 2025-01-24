import java.util.ArrayList;
import java.util.List;

public class Solution {
    public List<List<Integer>> subsets(int[] nums) {
        List<List<Integer>> ans = new ArrayList<>();
        backtrack(nums,0,new ArrayList<>(),ans);
        return ans;
    }

    public void backtrack(int[] nums,int index,List<Integer> cur,List<List<Integer>> ans){
        // base case
        if(index == nums.length){
            ans.add(new ArrayList<>(cur));
            return;
        }
        // 直接跳过当前数
        backtrack(nums,index+1,cur,ans);
        // 将当前数加入结果
        cur.add(nums[index]);
        backtrack(nums,index+1,cur,ans);
        // 回溯
        cur.remove(cur.size()-1);
    }
}
