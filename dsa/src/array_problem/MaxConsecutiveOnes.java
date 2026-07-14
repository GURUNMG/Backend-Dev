package array_problem;

public class MaxConsecutiveOnes {
    public static int findMaxConsecutiveOnes(int[] nums) {
        int count = 0;
        int current = 0;
        for(int i=0; i<nums.length; i++){
            if(nums[i] == 1) {
                current ++;
            } else {
                current = 0;
            }
            if(current > count) {
                count = current;
            }
        }
        return count;
    }

    public static void main(String[] args) {

        int[] arr = {1,1,0,1,1,1};
        int result = findMaxConsecutiveOnes(arr);
        System.out.println(result);
    }
}
