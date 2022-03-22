import java.util.LinkedList;


public class AesEncryptor {
    static final int Nk = 4;
    static final int Nr = 10;
    static final int Nb = 4;

    static int[][] inputToState(byte[] in) {
        int[][] s = new int[4][4];
        for (int r = 0; r < 4; r++)
            for (int c = 0; c < 4; c++)
                s[r][c] = in[r + 4 * c] & 0xff;
        return s;
    }

    static byte[] stateToOutput(int[][] s) {
        byte[] out = new byte[16];
        for (int r = 0; r < 4; r++)
            for (int c = 0; c < 4; c++)
                out[r + 4 * c] = (byte) s[r][c];
        return out;
    }

    static int[][] subBytes(int[][] state) {
        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 4; j++)
                state[i][j] = sBox(state[i][j]);
        return state;
    }

    static int sBox(int in) {
        int[][] sBox = {
                {0x63, 0x7c, 0x77, 0x7b, 0xf2, 0x6b, 0x6f, 0xc5, 0x30, 0x01, 0x67, 0x2b, 0xfe, 0xd7, 0xab, 0x76},
                {0xca, 0x82, 0xc9, 0x7d, 0xfa, 0x59, 0x47, 0xf0, 0xad, 0xd4, 0xa2, 0xaf, 0x9c, 0xa4, 0x72, 0xc0},
                {0xb7, 0xfd, 0x93, 0x26, 0x36, 0x3f, 0xf7, 0xcc, 0x34, 0xa5, 0xe5, 0xf1, 0x71, 0xd8, 0x31, 0x15},
                {0x04, 0xc7, 0x23, 0xc3, 0x18, 0x96, 0x05, 0x9a, 0x07, 0x12, 0x80, 0xe2, 0xeb, 0x27, 0xb2, 0x75},
                {0x09, 0x83, 0x2c, 0x1a, 0x1b, 0x6e, 0x5a, 0xa0, 0x52, 0x3b, 0xd6, 0xb3, 0x29, 0xe3, 0x2f, 0x84},
                {0x53, 0xd1, 0x00, 0xed, 0x20, 0xfc, 0xb1, 0x5b, 0x6a, 0xcb, 0xbe, 0x39, 0x4a, 0x4c, 0x58, 0xcf},
                {0xd0, 0xef, 0xaa, 0xfb, 0x43, 0x4d, 0x33, 0x85, 0x45, 0xf9, 0x02, 0x7f, 0x50, 0x3c, 0x9f, 0xa8},
                {0x51, 0xa3, 0x40, 0x8f, 0x92, 0x9d, 0x38, 0xf5, 0xbc, 0xb6, 0xda, 0x21, 0x10, 0xff, 0xf3, 0xd2},
                {0xcd, 0x0c, 0x13, 0xec, 0x5f, 0x97, 0x44, 0x17, 0xc4, 0xa7, 0x7e, 0x3d, 0x64, 0x5d, 0x19, 0x73},
                {0x60, 0x81, 0x4f, 0xdc, 0x22, 0x2a, 0x90, 0x88, 0x46, 0xee, 0xb8, 0x14, 0xde, 0x5e, 0x0b, 0xdb},
                {0xe0, 0x32, 0x3a, 0x0a, 0x49, 0x06, 0x24, 0x5c, 0xc2, 0xd3, 0xac, 0x62, 0x91, 0x95, 0xe4, 0x79},
                {0xe7, 0xc8, 0x37, 0x6d, 0x8d, 0xd5, 0x4e, 0xa9, 0x6c, 0x56, 0xf4, 0xea, 0x65, 0x7a, 0xae, 0x08},
                {0xba, 0x78, 0x25, 0x2e, 0x1c, 0xa6, 0xb4, 0xc6, 0xe8, 0xdd, 0x74, 0x1f, 0x4b, 0xbd, 0x8b, 0x8a},
                {0x70, 0x3e, 0xb5, 0x66, 0x48, 0x03, 0xf6, 0x0e, 0x61, 0x35, 0x57, 0xb9, 0x86, 0xc1, 0x1d, 0x9e},
                {0xe1, 0xf8, 0x98, 0x11, 0x69, 0xd9, 0x8e, 0x94, 0x9b, 0x1e, 0x87, 0xe9, 0xce, 0x55, 0x28, 0xdf},
                {0x8c, 0xa1, 0x89, 0x0d, 0xbf, 0xe6, 0x42, 0x68, 0x41, 0x99, 0x2d, 0x0f, 0xb0, 0x54, 0xbb, 0x16}
        };
        int row = in >>> 4;
        int column = in & 0xf;
        return sBox[row][column];
    }

    static int[][] shiftRows(int[][] state) {
        int[] t = new int[4];
        for (int i = 1; i < 4; i++) {
            for (int j = 0; j < 4; j++)
                t[j] = state[i][(j + i) % 4];
            for (int j = 0; j < 4; j++)
                state[i][j] = t[j];
        }
        return state;
    }

    static int[][] mixColumns(int[][] state) {
        int[][] M = {
                {2, 3, 1, 1},
                {1, 2, 3, 1},
                {1, 1, 2, 3},
                {3, 1, 1, 2}};
        int[] tmp = new int[4];
        for (int j = 0; j < 4; j++) {
            //复制第j列到tmp
            for (int i = 0; i < 4; i++)
                tmp[i] = state[i][j];
            for (int i = 0; i < 4; i++) {
                state[i][j] = 0;
                for (int k = 0; k < 4; k++)
                    state[i][j] = (state[i][j] ^ multiply(M[i][k], tmp[k]));
            }
        }
        return state;
    }

    static int polynomialMultiply(int a, int b) {
        int result = 0;
        for (int i = 1; i != 256; i = i << 1) {
            int tmp = (i & b) * a;
            result = result ^ tmp;
        }
        return result;
    }

    static int polynomialModulo(int a, int b) {
        if (b == 0)
            throw new RuntimeException("除数为0");
        LinkedList<Integer> listA = new LinkedList<>();
        for (int i = a; i != 0; i = i / 2)
            listA.push(a % 2);
        LinkedList<Integer> listB = new LinkedList<>();
        for (int i = b; i != 0; i = i / 2)
            listB.push(b % 2);

        int i = a;
        while (listA.size() >= listB.size()) {
            i = i ^ (b << (listA.size() - listB.size()));
            listA = new LinkedList<>();
            for (int j = i; j != 0; j = j / 2)
                listA.push(j % 2);
        }

        return i;
    }

    static int multiply(int a, int b) {
        return polynomialModulo(polynomialMultiply(a, b), 0x11b);
    }

    static int[] rotWord(int[] in) {
        int a = in[0];
        for (int i = 0; i < 3; i++)
            in[i] = in[i + 1];
        in[3] = a;
        return in;
    }

    static int[] subWord(int[] in) {
        for (int i = 0; i < 4; i++)
            in[i] = sBox(in[i]);
        return in;
    }

    static int[][] rCon = {{0x00, 0x00, 0x00, 0x00},
            {0x01, 0x00, 0x00, 0x00},
            {0x02, 0x00, 0x00, 0x00},
            {0x04, 0x00, 0x00, 0x00},
            {0x08, 0x00, 0x00, 0x00},
            {0x10, 0x00, 0x00, 0x00},
            {0x20, 0x00, 0x00, 0x00},
            {0x40, 0x00, 0x00, 0x00},
            {0x80, 0x00, 0x00, 0x00},
            {0x1b, 0x00, 0x00, 0x00},
            {0x36, 0x00, 0x00, 0x00}};

    static int[][] expandKey(int[][] key) {
        int[][] w = new int[Nb * (Nr + 1)][4];
        System.arraycopy(key, 0, w, 0, Nk);
        for (int i = Nk; i < Nb * (Nr + 1); i++) {
            int temp = fourBytesToInt(w[i - 1]);
            int[] t = new int[4];
            System.arraycopy(w[i - 1], 0, t, 0, 4);
            if (i % Nk == 0) {
                t = rotWord(t);
                t = subWord(t);
                temp = fourBytesToInt(t) ^ fourBytesToInt(rCon[i / Nk]);

            } else if (Nk > 6 && i % Nk == 4)
                temp = fourBytesToInt(subWord(t));
            w[i] = intToFourBytes(fourBytesToInt(w[i - Nk]) ^ temp);
        }
        return w;
    }

    static int fourBytesToInt(int[] in) {
        int temp = 0;
        for (int i = 0; i < 4; i++)
            temp |= in[i] << (3 - i) * 8;
        return temp;
    }

    static int[] intToFourBytes(int i) {
        int[] out = new int[4];
        for (int j = 0; j < 4; j++)
            out[j] = (i >>> (3 - j) * 8) & 0xff;
        return out;
    }


    static int[][] addRoundKey(int[][] state, int[][] roundKeys, int round) {
        for (int c = 0; c < 4; c++) {
            int[] tmp = new int[4];
            //拷贝第c列
            for (int i = 0; i < 4; i++)
                tmp[i] = state[i][c];
            tmp = intToFourBytes(fourBytesToInt(tmp) ^ fourBytesToInt(roundKeys[round * Nb + c]));
            for (int i = 0; i < 4; i++)
                state[i][c] = tmp[i];
        }
        return state;
    }

    static byte[] encrypt(byte[] input, byte[] key) {
//        if (input.length != Nb * 4)
//            throw new RuntimeException("要加密的数据长度不是16字节");
//        if (key.length != Nk * 4)
//            throw new RuntimeException("密钥长度不是16字节");
        int[][] keyArray = new int[Nk][4];
        for (int i = 0; i < Nk; i++) {
            int[] temp = {key[i * 4] & 0xff, key[i * 4 + 1] & 0xff, key[i * 4 + 2] & 0xff, key[i * 4 + 3] & 0xff};
            keyArray[i] = temp;
        }

        int[][] state = inputToState(input);
        int[][] roundKeys = expandKey(keyArray);
        addRoundKey(state, roundKeys, 0);
        for (int round = 1; round < Nr; round++) {
            subBytes(state);
            shiftRows(state);
            mixColumns(state);
            addRoundKey(state, roundKeys, round);
        }
        subBytes(state);
        shiftRows(state);
        addRoundKey(state, roundKeys, Nr);

        return stateToOutput(state);
    }

    static int[][] invShiftRows(int[][] state) {
        int[] t = new int[4];
        for (int i = 1; i < 4; i++) {
            for (int j = 0; j < 4; j++)
                t[j] = state[i][(j - i + 4) % 4];
            for (int j = 0; j < 4; j++)
                state[i][j] = t[j];
        }
        return state;
    }

    static int invSBox(int in) {
        int[][] array = {
                {0x52, 0x09, 0x6a, 0xd5, 0x30, 0x36, 0xa5, 0x38, 0xbf, 0x40, 0xa3, 0x9e, 0x81, 0xf3, 0xd7, 0xfb},
                {0x7c, 0xe3, 0x39, 0x82, 0x9b, 0x2f, 0xff, 0x87, 0x34, 0x8e, 0x43, 0x44, 0xc4, 0xde, 0xe9, 0xcb},
                {0x54, 0x7b, 0x94, 0x32, 0xa6, 0xc2, 0x23, 0x3d, 0xee, 0x4c, 0x95, 0x0b, 0x42, 0xfa, 0xc3, 0x4e},
                {0x08, 0x2e, 0xa1, 0x66, 0x28, 0xd9, 0x24, 0xb2, 0x76, 0x5b, 0xa2, 0x49, 0x6d, 0x8b, 0xd1, 0x25},
                {0x72, 0xf8, 0xf6, 0x64, 0x86, 0x68, 0x98, 0x16, 0xd4, 0xa4, 0x5c, 0xcc, 0x5d, 0x65, 0xb6, 0x92},
                {0x6c, 0x70, 0x48, 0x50, 0xfd, 0xed, 0xb9, 0xda, 0x5e, 0x15, 0x46, 0x57, 0xa7, 0x8d, 0x9d, 0x84},
                {0x90, 0xd8, 0xab, 0x00, 0x8c, 0xbc, 0xd3, 0x0a, 0xf7, 0xe4, 0x58, 0x05, 0xb8, 0xb3, 0x45, 0x06},
                {0xd0, 0x2c, 0x1e, 0x8f, 0xca, 0x3f, 0x0f, 0x02, 0xc1, 0xaf, 0xbd, 0x03, 0x01, 0x13, 0x8a, 0x6b},
                {0x3a, 0x91, 0x11, 0x41, 0x4f, 0x67, 0xdc, 0xea, 0x97, 0xf2, 0xcf, 0xce, 0xf0, 0xb4, 0xe6, 0x73},
                {0x96, 0xac, 0x74, 0x22, 0xe7, 0xad, 0x35, 0x85, 0xe2, 0xf9, 0x37, 0xe8, 0x1c, 0x75, 0xdf, 0x6e},
                {0x47, 0xf1, 0x1a, 0x71, 0x1d, 0x29, 0xc5, 0x89, 0x6f, 0xb7, 0x62, 0x0e, 0xaa, 0x18, 0xbe, 0x1b},
                {0xfc, 0x56, 0x3e, 0x4b, 0xc6, 0xd2, 0x79, 0x20, 0x9a, 0xdb, 0xc0, 0xfe, 0x78, 0xcd, 0x5a, 0xf4},
                {0x1f, 0xdd, 0xa8, 0x33, 0x88, 0x07, 0xc7, 0x31, 0xb1, 0x12, 0x10, 0x59, 0x27, 0x80, 0xec, 0x5f},
                {0x60, 0x51, 0x7f, 0xa9, 0x19, 0xb5, 0x4a, 0x0d, 0x2d, 0xe5, 0x7a, 0x9f, 0x93, 0xc9, 0x9c, 0xef},
                {0xa0, 0xe0, 0x3b, 0x4d, 0xae, 0x2a, 0xf5, 0xb0, 0xc8, 0xeb, 0xbb, 0x3c, 0x83, 0x53, 0x99, 0x61},
                {0x17, 0x2b, 0x04, 0x7e, 0xba, 0x77, 0xd6, 0x26, 0xe1, 0x69, 0x14, 0x63, 0x55, 0x21, 0x0c, 0x7d}
        };
        int row = in >>> 4;
        int column = in & 0xf;
        return array[row][column];
    }

    static int[][] invSubBytes(int[][] state) {
        for (int i = 0; i < Nb; i++)
            for (int j = 0; j < 4; j++)
                state[i][j] = invSBox(state[i][j]);
        return state;
    }

    static int[][] invMixColumns(int[][] state) {
        int[][] M = {
                {0xe, 0xb, 0xd, 0x9},
                {0x9, 0xe, 0xb, 0xd},
                {0xd, 0x9, 0xe, 0xb},
                {0xb, 0xd, 0x9, 0xe}
        };
        int[] tmp = new int[4];
        for (int j = 0; j < 4; j++) {
            //复制第j列到tmp
            for (int i = 0; i < 4; i++)
                tmp[i] = state[i][j];
            for (int i = 0; i < 4; i++) {
                state[i][j] = 0;
                for (int k = 0; k < 4; k++)
                    state[i][j] = (state[i][j] ^ multiply(M[i][k], tmp[k]));
            }
        }
        return state;
    }

    static byte[] decrypt(byte[] input, byte[] key) {
//        if (input.length != Nb * 4)
//            throw new RuntimeException("要加密的数据长度不是16字节");
//        if (key.length != Nk * 4)
//            throw new RuntimeException("密钥长度不是16字节");
        int[][] keyArray = new int[Nk][Nk];
        for (int i = 0; i < Nk; i++) {
            int[] temp = {key[i * 4] & 0xff, key[i * 4 + 1] & 0xff, key[i * 4 + 2] & 0xff, key[i * 4 + 3] & 0xff};
            keyArray[i] = temp;
        }

        int[][] state = inputToState(input);
        int[][] roundKeys = expandKey(keyArray);
        addRoundKey(state, roundKeys, Nr);
        for (int round = Nr - 1; round > 0; round--) {
            invShiftRows(state);
            invSubBytes(state);
            addRoundKey(state, roundKeys, round);
            invMixColumns(state);
        }
        invShiftRows(state);
        invSubBytes(state);
        addRoundKey(state, roundKeys, 0);
        return stateToOutput(state);
    }
}
