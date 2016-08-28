public class Main {
    public static void main(String args[]) {
        if (args.length != 0) {
            int group = 32;
            Encode encode = new Encode(args[0], group);

            //summary
            int dataLength = encode.getData().length;
            int totalEncodedLength = encode.getDictionary().length + encode.getEncoded().length;
            int average = ((dataLength - totalEncodedLength) / totalEncodedLength) * 100;
            System.out.println("data: " + dataLength + " bytes");
            System.out.println("encoded: " + totalEncodedLength + " bytes");
            System.out.println("Compression average: " + average + "%");

            Decode decode = new Decode(
                    encode.getEncoded(),
                    encode.getDictionary(),
                    group,
                    encode.getBites4file());
            decode.makeFile("out/" + encode.getFileName());
        }


        //printBytes(encoded);
        //HashMap<Byte, List<Integer>> dictionary = createDictionary(data); //generate data structure.
        //List<File> output = generateOutput(dictionary); //converts the encoded into a binary file.
    }


    /**
    private static byte[] encode1(byte[] data) {
        List<Byte> resultList = new ArrayList<Byte>();
        boolean start = true;
        int maxDigitValue = 0;
        int counter = -1;
        String initialBit = "0";
        String currentByte = "";
        for (byte b : data) {
            //byte to string representation
            String representation = String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0');
            //System.out.println(representation);

            String[] bites = representation.split("");
            for (String bit : bites) {
                //the first bit defined if will start with 1 or 0.
                if (start) { //use a flag for get the first bit.
                    start = false;
                    initialBit = bit.equals(initialBit) ? "1" : "0";
                    currentByte += bit;
                } else {
                    if (initialBit.equals(bit)) {
                        counter++;
                    } else {
                        initialBit = bit;
                        currentByte += Integer.toBinaryString((byte) counter);
                        counter = 0;

                        if (currentByte.length() >= 8) {
                            String byteStr = currentByte.substring(0, 8);
                            int casted = Integer.parseInt(byteStr, 2);
                            resultList.add((byte) casted);
                            currentByte = currentByte.substring(8, currentByte.length()); //restart the byte
                        }
                    }
                }
            }
        }

        return toPrimitives(resultList);
    }
    private static List<File> generateOutput(HashMap<Byte, List<Integer>> dictionary) {
        List<File> files = new ArrayList<File>();
        for (byte b : dictionary.keySet()) {
            List<Integer> list = dictionary.get(b);
            byte[] bytes = new byte[list.size()];

            ListIterator<Integer> it = list.listIterator();
            while (it.hasNext()) bytes[it.nextIndex()] = it.next().byteValue();

            try {
                File f = new File(directory + String.valueOf(b));
                FileUtils.writeByteArrayToFile(f, bytes);
                files.add(f);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return files;
    }
    private static HashMap<Byte, List<Integer>> createDictionary(byte[] data) {
        HashMap<Byte, List<Integer>> dictionary = new HashMap<Byte, List<Integer>>();
        for (int i = 0; i < data.length; i++) {
            byte b = data[i];
            List<Integer> list;
            if (dictionary.containsKey(b)) {
                list = dictionary.get(b);
            } else {
                list = new ArrayList<Integer>();
                dictionary.put(b, list);
            }

            list.add(i);
        }

        return dictionary;
    }
     **/
}
