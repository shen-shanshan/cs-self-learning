package editor.cn;

// 383.赎金信
class Solution383 {

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {
        public boolean canConstruct(String ransomNote, String magazine) {

            int[] count = new int[26];

            // 统计 ransomNote 字符
            for (int i = 0; i < ransomNote.length(); i++) {
                int index = ransomNote.charAt(i) - 'a';
                count[index]++;
            }

            // 统计 magazine 字符
            for (int i = 0; i < magazine.length(); i++) {
                int index = magazine.charAt(i) - 'a';
                count[index]--;
            }

            for (int i = 0; i < 26; i++) {
                if (count[i] > 0) {
                    return false;
                }
            }

            return true;
        }
    }
    //leetcode submit region end(Prohibit modification and deletion)

}