public class ThreeDimensionalPointerArrayExample {
    public static void main(String[] args) {
        // 宣告一個三維整數指標陣列
        Integer[][][] pointerArray3D = new Integer[2][3][4];

        // 初始化陣列元素
        pointerArray3D[0][0][0] = 10;
        pointerArray3D[0][0][1] = 11;
        pointerArray3D[0][0][2] = 12;
        pointerArray3D[0][0][3] = 13;

        pointerArray3D[0][1][0] = 20;
        pointerArray3D[0][1][1] = 21;
        pointerArray3D[0][1][2] = 22;
        pointerArray3D[0][1][3] = 23;

        pointerArray3D[0][2][0] = 30;
        pointerArray3D[0][2][1] = 31;
        pointerArray3D[0][2][2] = 32;
        pointerArray3D[0][2][3] = 33;

        pointerArray3D[1][0][0] = 40;
        pointerArray3D[1][0][1] = 41;
        pointerArray3D[1][0][2] = 42;
        pointerArray3D[1][0][3] = 43;

        pointerArray3D[1][1][0] = 50;
        pointerArray3D[1][1][1] = 51;
        pointerArray3D[1][1][2] = 52;
        pointerArray3D[1][1][3] = 53;

        pointerArray3D[1][2][0] = 60;
        pointerArray3D[1][2][1] = 61;
        pointerArray3D[1][2][2] = 62;
        pointerArray3D[1][2][3] = 63;

        // 輸出陣列中的所有元素
        System.out.println("陣列中的所有元素:");
        for (int i = 0; i < pointerArray3D.length; i++) {
            for (int j = 0; j < pointerArray3D[i].length; j++) {
                for (int k = 0; k < pointerArray3D[i][j].length; k++) {
                    System.out.println("pointerArray3D[" + i + "][" + j + "][" + k + "]: " + pointerArray3D[i][j][k]);
                }
            }
        }
        System.out.println();

        // 計算陣列中所有元素的總和
        int sum = 0;
        for (int i = 0; i < pointerArray3D.length; i++) {
            for (int j = 0; j < pointerArray3D[i].length; j++) {
                for (int k = 0; k < pointerArray3D[i][j].length; k++) {
                    sum += pointerArray3D[i][j][k];
                }
            }
        }
        System.out.println("陣列元素的總和: " + sum);
        System.out.println();

        // 找出陣列中的最大值
        int max = Integer.MIN_VALUE;
        for (int i = 0; i < pointerArray3D.length; i++) {
            for (int j = 0; j < pointerArray3D[i].length; j++) {
                for (int k = 0; k < pointerArray3D[i][j].length; k++) {
                    if (pointerArray3D[i][j][k] > max) {
                        max = pointerArray3D[i][j][k];
                    }
                }
            }
        }
        System.out.println("陣列中的最大值: " + max);
    }
}