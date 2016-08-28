import org.apache.commons.io.IOUtils;

import java.io.*;
import java.util.*;

/**
 * Created by oscar on 25/08/2016.

 */
class Encode {
    private File file;
    private int bites4file = 8;
    private int groups = 2;
    private byte[] data;
    private byte[] encoded;
    private byte[] dictionary;

    Encode(String file, int groups) {
        this.groups = groups;
        data = getBytes(file); //read file.
        init(data);
    }

    String getFileName() {
        return file.getName();
    }
    byte[] getData() {
        return data;
    }
    byte[] getEncoded() {
        return encoded;
    }
    byte[] getDictionary() {
        return dictionary;
    }
    int getBites4file() { return bites4file; };

    private void init(byte[] data) {
        HashMap<String, Integer> review = new HashMap<>();
        List<String> dictionary = new ArrayList<>();
        List<Integer> codedFile = new ArrayList<>();

        //get dictionary and stats.
        for (int i = 0; i < data.length; i++) {
            String key = ""; //do the key in group of bytes
            for (int j = 0; j < groups; j++) {
                if (i + j < data.length) key += Utils.byte2String(data[i + j], true);
            }

            if (review.containsKey(key)) {
                review.put(key, review.get(key) + 1);
            } else {
                review.put(key, 0);
                dictionary.add(key);
            }

            codedFile.add(dictionary.indexOf(key)); //if index is more than 255 some data will delete.
            i += (groups - 1);
        }

        //get how many bites are necessary for the file.
        bites4file = Utils.getBites4Size(dictionary.size());
        List<Byte> codedData = getCodedData(codedFile, bites4file);

        //limit of 1 byte for the dictionary.
        int dictionarySize = dictionary.size();
        if (dictionarySize > 255)
            System.out.println("Warning: dictionary size can't be over 255, current size: " + dictionarySize);

        this.dictionary = dictionary2ByteArray(dictionary);
        encoded = Utils.toPrimitives(codedData);
    }

    private List<Byte> getCodedData(List<Integer> codedFile, int bites4file) {
        List<Byte> result = new ArrayList<>();
        String byteStr = "";
        String auxBitesStr = "";
        for (int d: codedFile) {
            String bitsStr = auxBitesStr + Utils.byte2String(Utils.intToByteArray(d), bites4file);
            int bits4Byte = 8 - byteStr.length(); //number of bits for get a byte.
            int endIndex = bits4Byte > bitsStr.length() ? bitsStr.length() : bits4Byte;
            byteStr += bitsStr.substring(0, endIndex);
            auxBitesStr = bitsStr.substring(endIndex, bitsStr.length());

            if (byteStr.length() >= 8) {
                result.add(Utils.string2Byte(byteStr));
                byteStr = "";
            }
        }

        //completing the rest of the data
        if (!"".equals(byteStr)) {
            byteStr = String.format("%-8s", byteStr).replace(' ', '0');
            result.add(Utils.string2Byte(byteStr));
        }

        return result;
    }

    private byte[] dictionary2ByteArray(List<String> dictionary) {
        List<Byte> result = new ArrayList<>();
        for (String key: dictionary){
            for (int i = 0; i < groups; i++) {
                if (key.length() >= (i * 8) + 8) {
                    String byteStr = key.substring(i * 8, (i * 8) + 8);
                    result.add(Utils.string2Byte(byteStr));
                }
            }
        }

        return Utils.toPrimitives(result);
    }

    private byte[] getBytes(String path) {
        file = new File(path);
        try {
            InputStream io = new FileInputStream(file);
            return IOUtils.toByteArray(io);
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + file.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new byte[] {};
    }
}
