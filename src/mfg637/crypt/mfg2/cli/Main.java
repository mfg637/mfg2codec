package mfg637.crypt.mfg2.cli;

import mfg637.codec.BytesCodec;
import mfg637.codec.FilesCodec;
import mfg637.crypt.mfg2.codec.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.security.DigestException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class Main {

    private enum Mode{NULL, ENCODE, DECODE, SELF_TEST}

    public static void main(String[] args) {
        BytesCodec mfg2 = null;
        FilesCodec mfg2f = null;
        String input_file = null, output_file = null;
        int i=0;
        Mode mode = Mode.NULL;
        while (i<args.length){
            switch (args[i]) {
                case "-d":
                    mode = Mode.DECODE;
                    break;
                case "-c":
                    mode = Mode.ENCODE;
                    break;
                case "-pb":
                    i++;
                    try {
                        byte[] password = args[i].getBytes(StandardCharsets.UTF_8);
                        mfg2 = new MFG2Codec(password);
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                        return;
                    }
                    break;
                case "-pf":
                    i++;
                    try {
                        byte[] password = args[i].getBytes(StandardCharsets.UTF_8);
                        mfg2f = new MFG2FCodec(password);
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                        return;
                    }
                    break;
                case "-pst":
                    i++;
                    try {
                        byte[] password = args[i].getBytes(StandardCharsets.UTF_8);
                        mfg2 = new MFG2test(password);
                        mfg2f = new MFG2Ftest(password);
                        mode = Mode.SELF_TEST;
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                        return;
                    }
                    break;
                case "-if":
                    i++;
                    input_file = args[i];
                    break;
                case "-of":
                    i++;
                    output_file = args[i];
                    break;
            }
            i++;
        }
        if (mode == Mode.SELF_TEST){
            TestableCodec mfg2_test = (TestableCodec) mfg2;
            TestableCodec mfg2f_test = (TestableCodec) mfg2f;
            System.out.println("Self-test mode activated");
            System.out.println("Reading data from stdin");
            byte[] input = new byte[0];
            try {
                input = System.in.readAllBytes();
            } catch (IOException e) {
                System.out.println("Error occurred while read stdin");
                e.printStackTrace();
            }
            System.out.println("test 1: reversing");
            byte[] mfg2_test_result, mfg2f_test_result;
            try {
                mfg2_test_result = mfg2_test.reversing(input);
                mfg2f_test_result = mfg2f_test.reversing(input);
                if (Arrays.equals(mfg2_test_result, mfg2f_test_result)){
                    System.out.println("Test passed");
                }else{
                    System.out.println("failure has occurred");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("test 2: encoding");
            try {
                mfg2_test_result = mfg2_test.encode(input);
                mfg2f_test_result = mfg2f_test.encode(input);
                if (Arrays.equals(mfg2_test_result, mfg2f_test_result)){
                    System.out.println("Test passed");
                }else{
                    System.out.println("failure has occurred");
                }
            } catch (IOException | CloneNotSupportedException e) {
                e.printStackTrace();
            }
            System.out.println("test 3: decoding");
            try {
                mfg2_test_result = mfg2_test.decode(input);
                mfg2f_test_result = mfg2f_test.decode(input);
                if (Arrays.equals(mfg2_test_result, mfg2f_test_result)){
                    System.out.println("Test passed");
                }else{
                    System.out.println("failure has occurred");
                }
            } catch (IOException | CloneNotSupportedException e) {
                e.printStackTrace();
            }
        } else if (mfg2 != null){
            byte[] output;
            switch (mode){
                case ENCODE:
                    try {
                        output = mfg2.encode(System.in.readAllBytes());
                    } catch (IOException | CloneNotSupportedException e) {
                        e.printStackTrace();
                        return;
                    }
                    try {
                        System.out.write(output);
                    } catch (IOException e) {
                        e.printStackTrace();
                        return;
                    }
                    break;
                case DECODE:
                    try {
                        output = mfg2.decode(System.in.readAllBytes());
                    } catch (IOException | CloneNotSupportedException e) {
                        e.printStackTrace();
                        return;
                    }
                    try {
                        System.out.write(output);
                    } catch (IOException e) {
                        e.printStackTrace();
                        return;
                    }
                    break;
            }
        }else if ((mfg2f != null) && (input_file != null) && (output_file != null)){
            switch (mode){
                case ENCODE:
                    try {
                        mfg2f.encode(new RandomAccessFile(input_file, "r"), new FileOutputStream(output_file));
                    } catch (IOException | CloneNotSupportedException e) {
                        e.printStackTrace();
                        return;
                    }
                    break;
                case DECODE:
                    try {
                        mfg2f.decode(new File(input_file), new RandomAccessFile(output_file, "rw"));
                    } catch (IOException | CloneNotSupportedException e) {
                        e.printStackTrace();
                        return;
                    }
                    break;
            }
        }

    }
}
