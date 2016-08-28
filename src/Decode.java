import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by oscar on 25/08/2016.

 */
class Decode {
    private byte[] decoded;
    private int groups, bites4file;
    private HashMap<String, Integer> dictionary;

    Decode(byte[] data, byte[] dictionary, int groups, int bites4file) {
        this.groups = groups;
        this.bites4file = bites4file;
        this.dictionary = getDictionaryStructure(dictionary);
        this.decoded = getDecodedFile(data);
    }

    File makeFile(String fileName) {
        File f = new File(fileName);
        try {
            FileUtils.writeByteArrayToFile(f, getDecoded());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return f;
    }

    byte[] getDecoded() {
        return decoded;
    }

    private byte[] getDecodedFile(byte[] data) {
        List<Byte> encoded = new ArrayList<>();
        HashMap<Integer, String> reverseDictionary = Utils.swapMap(dictionary);
        String keyStr = "";

        int currentBits = bites4file;
        for (int j = 0; j < data.length; j++) {
            byte b = data[j];
            String byteStr = Utils.byte2String(b, true);
            int beginIndex = 0;

            if (j == data.length - 1) {
                //remove garbage bits.
                String lastBit = byteStr.substring(byteStr.length() - 1);
                byteStr = byteStr.replaceFirst(lastBit + "*$", "");
            }

            while (beginIndex + currentBits <= byteStr.length()) {
                keyStr += byteStr.substring(beginIndex, beginIndex += currentBits);

                //group of bits gotten
                int key = Utils.string2Int(keyStr);
                String bytes = reverseDictionary.get(key);
                for (int i = 0; i < groups; i++) {
                    if (bytes.length() >= i * 8 + 8) {
                        String fileDecodedByte = bytes.substring(i * 8, i * 8 + 8);
                        encoded.add(Utils.string2Byte(fileDecodedByte));
                    }
                }

                currentBits = bites4file;
                keyStr = "";
            }

            keyStr += byteStr.substring(beginIndex, byteStr.length());
            currentBits = bites4file - keyStr.length();
        }

//        if (!"".equals(keyStr)) {
//            //rest of bits in the last byte, that is not useful for the moment.
//            System.out.println(keyStr);
//        }

        return Utils.toPrimitives(encoded);
    }

    private HashMap<String, Integer> getDictionaryStructure(byte[] dictionary) {
        HashMap<String, Integer> dictionaryStructure = new HashMap<>();
        int counter = 0;
        for (int i = 0; i < dictionary.length; i++) {
            String key = "";

            for (int j = 0; j < groups; j++) {
                if (i + j < dictionary.length) key += Utils.byte2String(dictionary[i + j], true);
            }

            dictionaryStructure.put(key, counter);
            i += (groups - 1);
            counter++;
        }

        return dictionaryStructure;
    }
}
