package array_problem;

import java.util.Arrays;

public class Shuffle {

    static void shuffle(int[] nums, int n) {

        int[] ans = new int[2 * n];
        int j = 0;
        for (int i = 0; i < n; i++) {
            ans[j++] = nums[i];
            ans[j++] = nums[i + n];
        }
        System.out.println(Arrays.toString(ans));
    }

    public static void main(String[] args) {

        int[] nums = {2,5,1,3,4,7};
        int n = 3;

        shuffle(nums, n);
    }
}
