import java.util.HashMap;
import java.util.List;

/**
 * Created by oscar on 25/08/2016.

 */
class Utils {
    static String byte2String(byte b, boolean fillData) {
        String B = Integer.toBinaryString(b & 0xFF);
        return fillData ? String.format("%8s", B).replace(' ', '0') : B;
    }

    static byte string2Byte(String byteStr) {
        int casted = Integer.parseInt(byteStr, 2);
        return (byte) casted;
    }

    static void printHashMap(HashMap<String, Byte> hashMap) {
        for (String key: hashMap.keySet()){
            String value = hashMap.get(key).toString();
            System.out.println(key + " " + value);
        }
    }

    static void printBytes(byte[] data) {
        for (byte b : data) System.out.println(byte2String(b, true));
    }

    static  <K,V> HashMap<V,K> swapMap(HashMap<K,V> map) {
        HashMap<V,K> rev = new HashMap<>();
        for(HashMap.Entry<K,V> entry : map.entrySet())
            rev.put(entry.getValue(), entry.getKey());

        return rev;
    }

    static byte[] toPrimitives(List<Byte> oBytes) {
        Byte[] result = new Byte[oBytes.size()];
        result = oBytes.toArray(result);
        return toPrimitives(result);
    }

    private static byte[] toPrimitives(Byte[] oBytes) {
        byte[] bytes = new byte[oBytes.length];
        for(int i = 0; i < oBytes.length; i++) {
            bytes[i] = oBytes[i];
        }

        return bytes;
    }

    static boolean areBytesEquals(byte[] a, byte[] b) {
        boolean result = a.length == b.length;
        if (result) {
            for (int i = 0; i < a.length; i ++) {
                result = result && (a[i] == b[i]);
            }
        }

        return result;
    }
}
