import static com.alee.utils.MathUtils.min;

public class DesEncryptor {
    static int[] bytes8ToBits64(byte[] byteArray) {
        //将byte[8]转换为int[64],把每个byte的每个bit拆分成8个int(1或0)
        byte[] input = new byte[8];
        System.arraycopy(byteArray, 0, input, 0, min(byteArray.length, 8));
        int[] inputBlock = new int[64];
        for (int i = 0; i < input.length; ++i)
            for (int j = 0; j < 8; ++j)
                inputBlock[8 * i + j] = (input[i] & (1 << (7 - j))) != 0 ? 1 : 0;
        return inputBlock;
    }

    static byte[] bits64ToBytes8(int[] bitArray) {
        //将int[64]转换为byte[8],每个int算一个bit,每8个int组合成一个byte
        byte[] bytes = new byte[8];
        for (int i = 0; i < 8; i++)
            for (int j = 0; j < 8; j++)
                bytes[i] += bitArray[i * 8 + j] << (7 - j);
        return bytes;
    }

    static int[] initialPermute(int[] input) {
        final int[] IP = {58, 50, 42, 34, 26, 18, 10, 2,
                60, 52, 44, 36, 28, 20, 12, 4,
                62, 54, 46, 38, 30, 22, 14, 6,
                64, 56, 48, 40, 32, 24, 16, 8,
                57, 49, 41, 33, 25, 17, 9, 1,
                59, 51, 43, 35, 27, 19, 11, 3,
                61, 53, 45, 37, 29, 21, 13, 5,
                63, 55, 47, 39, 31, 23, 15, 7};
        int[] output = new int[64];
        for (int i = 0; i < IP.length; ++i)
            output[i] = input[IP[i] - 1];
        return output;
    }

    static int[] inverseInitialPermute(int[] input) {
        final int[] IP_1 = {
                40, 8, 48, 16, 56, 24, 64, 32,
                39, 7, 47, 15, 55, 23, 63, 31,
                38, 6, 46, 14, 54, 22, 62, 30,
                37, 5, 45, 13, 53, 21, 61, 29,
                36, 4, 44, 12, 52, 20, 60, 28,
                35, 3, 43, 11, 51, 19, 59, 27,
                34, 2, 42, 10, 50, 18, 58, 26,
                33, 1, 41, 9, 49, 17, 57, 25
        };
        int[] output = new int[64];
        for (int i = 0; i < IP_1.length; ++i)
            output[i] = input[IP_1[i] - 1];
        return output;
    }

    static int[] f(int[] R, int[] K) {
        int[] resultOfE = E(R);
        for (int i = 0; i < resultOfE.length; ++i)
            resultOfE[i] = resultOfE[i] ^ K[i];
        return P(S(resultOfE));
    }

    static int[] S(int[] input) {
        int[][] inputS = new int[8][6];
        for (int i = 0; i < 8; ++i)
            System.arraycopy(input, i * 6, inputS[i], 0, 6);
        final int[][][] S =
                {
                        {
                                {14, 4, 13, 1, 2, 15, 11, 8, 3, 10, 6, 12, 5, 9, 0, 7},
                                {0, 15, 7, 4, 14, 2, 13, 1, 10, 6, 12, 11, 9, 5, 3, 8},
                                {4, 1, 14, 8, 13, 6, 2, 11, 15, 12, 9, 7, 3, 10, 5, 0},
                                {15, 12, 8, 2, 4, 9, 1, 7, 5, 11, 3, 14, 10, 0, 6, 13}
                        },
                        {
                                {15, 1, 8, 14, 6, 11, 3, 4, 9, 7, 2, 13, 12, 0, 5, 10},
                                {3, 13, 4, 7, 15, 2, 8, 14, 12, 0, 1, 10, 6, 9, 11, 5},
                                {0, 14, 7, 11, 10, 4, 13, 1, 5, 8, 12, 6, 9, 3, 2, 15},
                                {13, 8, 10, 1, 3, 15, 4, 2, 11, 6, 7, 12, 0, 5, 14, 9}
                        },
                        {
                                {10, 0, 9, 14, 6, 3, 15, 5, 1, 13, 12, 7, 11, 4, 2, 8},
                                {13, 7, 0, 9, 3, 4, 6, 10, 2, 8, 5, 14, 12, 11, 15, 1},
                                {13, 6, 4, 9, 8, 15, 3, 0, 11, 1, 2, 12, 5, 10, 14, 7},
                                {1, 10, 13, 0, 6, 9, 8, 7, 4, 15, 14, 3, 11, 5, 2, 12}
                        },
                        {
                                {7, 13, 14, 3, 0, 6, 9, 10, 1, 2, 8, 5, 11, 12, 4, 15},
                                {13, 8, 11, 5, 6, 15, 0, 3, 4, 7, 2, 12, 1, 10, 14, 9},
                                {10, 6, 9, 0, 12, 11, 7, 13, 15, 1, 3, 14, 5, 2, 8, 4},
                                {3, 15, 0, 6, 10, 1, 13, 8, 9, 4, 5, 11, 12, 7, 2, 14}
                        },
                        {
                                {2, 12, 4, 1, 7, 10, 11, 6, 8, 5, 3, 15, 13, 0, 14, 9},
                                {14, 11, 2, 12, 4, 7, 13, 1, 5, 0, 15, 10, 3, 9, 8, 6},
                                {4, 2, 1, 11, 10, 13, 7, 8, 15, 9, 12, 5, 6, 3, 0, 14},
                                {11, 8, 12, 7, 1, 14, 2, 13, 6, 15, 0, 9, 10, 4, 5, 3}
                        },
                        {
                                {12, 1, 10, 15, 9, 2, 6, 8, 0, 13, 3, 4, 14, 7, 5, 11},
                                {10, 15, 4, 2, 7, 12, 9, 5, 6, 1, 13, 14, 0, 11, 3, 8},
                                {9, 14, 15, 5, 2, 8, 12, 3, 7, 0, 4, 10, 1, 13, 11, 6},
                                {4, 3, 2, 12, 9, 5, 15, 10, 11, 14, 1, 7, 6, 0, 8, 13}
                        },
                        {
                                {4, 11, 2, 14, 15, 0, 8, 13, 3, 12, 9, 7, 5, 10, 6, 1},
                                {13, 0, 11, 7, 4, 9, 1, 10, 14, 3, 5, 12, 2, 15, 8, 6},
                                {1, 4, 11, 13, 12, 3, 7, 14, 10, 15, 6, 8, 0, 5, 9, 2},
                                {6, 11, 13, 8, 1, 4, 10, 7, 9, 5, 0, 15, 14, 2, 3, 12}
                        },
                        {
                                {13, 2, 8, 4, 6, 15, 11, 1, 10, 9, 3, 14, 5, 0, 12, 7},
                                {1, 15, 13, 8, 10, 3, 7, 4, 12, 5, 6, 11, 0, 14, 9, 2},
                                {7, 11, 4, 1, 9, 12, 14, 2, 0, 6, 10, 13, 15, 3, 5, 8},
                                {2, 1, 14, 7, 4, 10, 8, 13, 15, 12, 9, 0, 3, 5, 6, 11}
                        }
                };
        int[][] outputS = new int[8][4];
        for (int i = 0; i < S.length; ++i) {
            int rowNumber = (inputS[i][0] << 1) + inputS[i][5];
            int columnNumber = (inputS[i][1] << 3) + (inputS[i][2] << 2) + (inputS[i][3] << 1) + inputS[i][4];
            int t = S[i][rowNumber][columnNumber];
            for (int j = 0; j < 4; ++j)
                outputS[i][j] = (t & (8 >> j)) != 0 ? 1 : 0;
        }
        int[] output = new int[32];
        for (int i = 0; i < outputS.length; ++i)
            System.arraycopy(outputS[i], 0, output, 4 * i, 4);
        return output;
    }

    static int[] P(int[] input) {
        int[] output = new int[32];
        final int[] bitSelectionTable = {
                16, 7, 20, 21,
                29, 12, 28, 17,
                1, 15, 23, 26,
                5, 18, 31, 10,
                2, 8, 24, 14,
                32, 27, 3, 9,
                19, 13, 30, 6,
                22, 11, 4, 25
        };
        for (int i = 0; i < output.length; ++i)
            output[i] = input[bitSelectionTable[i] - 1];
        return output;
    }

    static int[] E(int[] R) {
        int[] output = new int[48];
        final int[] bitSelectionTable = {
                32, 1, 2, 3, 4, 5,
                4, 5, 6, 7, 8, 9,
                8, 9, 1, 11, 12, 13,
                12, 13, 1, 15, 16, 17,
                16, 17, 1, 19, 20, 21,
                20, 21, 2, 23, 24, 25,
                24, 25, 2, 27, 28, 29,
                28, 29, 3, 31, 32, 1};
        for (int i = 0; i < output.length; ++i)
            output[i] = R[bitSelectionTable[i] - 1];
        return output;

    }

    static int[][] KS(int[] key) {
        final int[][] PC1 = {
                {
                        57, 49, 41, 33, 25, 17, 9,
                        1, 58, 50, 42, 34, 26, 18,
                        10, 2, 59, 51, 43, 35, 27,
                        19, 11, 3, 60, 52, 44, 36
                },
                {
                        63, 55, 47, 39, 31, 23, 15,
                        7, 62, 54, 46, 38, 30, 22,
                        14, 6, 61, 53, 45, 37, 29,
                        21, 13, 5, 28, 20, 12, 4
                }
        };
        final int[] PC2 = {
                14, 17, 11, 24, 1, 5,
                3, 28, 15, 6, 21, 10,
                23, 19, 12, 4, 26, 8,
                16, 7, 27, 20, 13, 2,
                41, 52, 31, 37, 47, 55,
                30, 40, 51, 45, 33, 48,
                44, 49, 39, 56, 34, 53,
                46, 42, 50, 36, 29, 32
        };
        int[][] C = new int[17][28];
        int[][] D = new int[17][28];
        int[][] K = new int[16][48];
        for (int i = 0; i < PC1[0].length; ++i) {
            C[0][i] = key[PC1[0][i] - 1];
            D[0][i] = key[PC1[1][i] - 1];
        }

        int[] leftShiftTable = {1, 1, 2, 2, 2, 2, 2, 2, 1, 2, 2, 2, 2, 2, 2, 1};
        for (int i = 0; i < leftShiftTable.length; ++i) {
            int leftShift = leftShiftTable[i];
            for (int j = 0; j < C[i].length; ++j) {
                int originPosition = (j + leftShift) % C[i].length;
                C[i + 1][j] = C[i][originPosition];
                D[i + 1][j] = D[i][originPosition];
            }
            int[] CD = new int[56];
            System.arraycopy(C[i + 1], 0, CD, 0, C[i + 1].length);
            System.arraycopy(D[i + 1], 0, CD, C[i + 1].length, D[i + 1].length);
            for (int k = 0; k < K[i].length; ++k)
                K[i][k] = CD[PC2[k] - 1];
        }
        return K;
    }

    static byte[] encrypt(byte[] input, byte[] key) {
        //加密主流程
        int[] inputBitArray = bytes8ToBits64(input);
        int[] keyBitArray = bytes8ToBits64(key);
        int[][] K = KS(keyBitArray);
        inputBitArray = initialPermute(inputBitArray);
        int[][] L = new int[17][32];
        System.arraycopy(inputBitArray, 0, L[0], 0, 32);
        int[][] R = new int[17][32];
        System.arraycopy(inputBitArray, 32, R[0], 0, 32);
        for (int i = 0; i < 16; i++) {
            System.arraycopy(R[i], 0, L[i + 1], 0, 32);
            R[i + 1] = f(R[i], K[i]);
            for (int j = 0; j < R[i + 1].length; j++)
                R[i + 1][j] = L[i][j] ^ R[i + 1][j];
        }
        int[] preoutput = new int[64];
        System.arraycopy(R[16], 0, preoutput, 0, 32);
        System.arraycopy(L[16], 0, preoutput, 32, 32);
        preoutput = inverseInitialPermute(preoutput);
        return bits64ToBytes8(preoutput);
    }

    static byte[] decrypt(byte[] input, byte[] key) {
        //解密主流程
        int[] inputBitArray = bytes8ToBits64(input);
        int[] keyBitArray = bytes8ToBits64(key);
        int[][] K = KS(keyBitArray);
        inputBitArray = initialPermute(inputBitArray);
        int[][] L = new int[17][32];
        System.arraycopy(inputBitArray, 0, L[0], 0, 32);
        int[][] R = new int[17][32];
        System.arraycopy(inputBitArray, 32, R[0], 0, 32);
        for (int i = 0; i < 16; i++) {
            System.arraycopy(R[i], 0, L[i + 1], 0, 32);
            R[i + 1] = f(R[i], K[K.length - 1 - i]);
            for (int j = 0; j < R[i + 1].length; j++)
                R[i + 1][j] = L[i][j] ^ R[i + 1][j];
        }
        int[] preoutput = new int[64];
        System.arraycopy(R[16], 0, preoutput, 0, 32);
        System.arraycopy(L[16], 0, preoutput, 32, 32);
        preoutput = inverseInitialPermute(preoutput);
        return bits64ToBytes8(preoutput);
    }

}
