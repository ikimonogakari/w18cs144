import java.security.*;
import java.io.*;
import java.util.*;
import java.lang.Math;

public class ComputeSHA {
    public static void main(String[] args) {
        // read file as byte array
        String filename = args[0];
        File input_file = new File(filename);
        FileInputStream fis;
        try {
            fis = new FileInputStream(input_file);
        } catch (FileNotFoundException ex) {
            System.out.println(ex);
            return;
        }
        byte[] buffer = new byte[(int) input_file.length()];
        try {
            fis.read(buffer);
            fis.close();
        } catch (IOException ex) {
            System.out.println(ex);
            return;
        }
        // calculate sha1
        MessageDigest sha;
        try {
            sha = MessageDigest.getInstance("SHA1");
        } catch (NoSuchAlgorithmException ex) {
            System.out.println(ex);
            return;
        }
        sha.update(buffer);
        byte[] res = sha.digest();
        StringBuilder sb = new StringBuilder();
        for (byte b: res) {
            sb.append(String.format("%02x",b));
        }
        System.out.println(sb.toString());
    }
}
