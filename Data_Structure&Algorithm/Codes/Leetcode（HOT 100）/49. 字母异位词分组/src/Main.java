import java.util.List;

public class Main {
    public static void main(String[] args) {
        String[] strs1 = {"eat", "tea", "tan", "ate", "nat", "bat"};
        String[] strs2 = {""};
        String[] strs3 = {"a"};

        Solution s = new Solution();

        List<List<String>> ans1 = s.groupAnagrams(strs1);
        List<List<String>> ans2 = s.groupAnagrams(strs2);
        List<List<String>> ans3 = s.groupAnagrams(strs3);

        print(ans1);
        print(ans2);
        print(ans3);
    }

    public static void print(List<List<String>> l) {
        for (List<String> i : l) {
            for (String j : i) {
                System.out.print(j + " ");
            }
            System.out.println();
        }
        System.out.println("-----------");
    }
}
