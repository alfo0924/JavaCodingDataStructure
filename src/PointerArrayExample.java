public class PointerArrayExample {
    public static void main(String[] args) {
        // 宣告一個整數指標陣列,大小為5
        Integer[] pointerArray = new Integer[5];

        // 初始化陣列元素
        pointerArray[0] = 10;
        pointerArray[1] = 20;
        pointerArray[2] = 30;
        pointerArray[3] = 40;
        pointerArray[4] = 50;

        // 輸出陣列中的所有元素
        System.out.println("陣列中的所有元素:");
        for (Integer value : pointerArray) {
            System.out.println(value);
        }
        System.out.println();

        // 計算陣列中所有元素的總和
        int sum = 0;
        for (Integer value : pointerArray) {
            sum += value;
        }
        System.out.println("陣列元素的總和: " + sum);
        System.out.println();

        // 找出陣列中的最大值
        int max = Integer.MIN_VALUE;
        for (Integer value : pointerArray) {
            if (value > max) {
                max = value;
            }
        }
        System.out.println("陣列中的最大值: " + max);
    }
}