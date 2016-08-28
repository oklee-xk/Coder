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
    private int groups;
    private HashMap<String, Byte> dictionary;

    Decode(byte[] data, byte[] dictionary, int groups) {
        this.groups = groups;
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
        HashMap<Byte, String> reverseDictionary = Utils.swapMap(dictionary);
        for (byte b: data) {
            String bytes = reverseDictionary.get(b);

            for (int i = 0; i < groups; i++) {
                if (bytes.length() >= i * 8 + 8) {
                    String byteStr = bytes.substring(i * 8, i * 8 + 8);
                    encoded.add(Utils.string2Byte(byteStr));
                }
            }
        }

        return Utils.toPrimitives(encoded);
    }

    private HashMap<String, Byte> getDictionaryStructure(byte[] dictionary) {
        HashMap<String, Byte> dictionaryStructure = new HashMap<>();
        int counter = 0;
        for (int i = 0; i < dictionary.length; i++) {
            Byte value = (byte) counter;
            counter++;
            String key = "";

            for (int j = 0; j < groups; j++) {
                if (i + j < dictionary.length) key += Utils.byte2String(dictionary[i + j], true);
            }

            dictionaryStructure.put(key, value);
            i += (groups - 1);
        }

        return dictionaryStructure;
    }
}
