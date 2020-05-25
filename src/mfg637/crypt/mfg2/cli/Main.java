package mfg637.crypt.mfg2.cli;

import mfg637.codec.BytesCodec;
import mfg637.crypt.mfg2.codec.MFG2Codec;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.DigestException;
import java.security.NoSuchAlgorithmException;

public class Main {

    private enum Mode{NULL, ENCODE, DECODE}

    public static void main(String[] args) {
        BytesCodec mfg2 = null;
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
                case "-p":
                    i++;
                    try {
                        byte[] password = args[i].getBytes(StandardCharsets.UTF_8);
                        mfg2 = new MFG2Codec(password);
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                        return;
                    }
                    break;
            }
            i++;
        }
        if (mfg2 != null){
            byte[] output;
            switch (mode){
                case ENCODE:
                    try {
                        output = mfg2.encode(System.in.readAllBytes());
                    } catch (DigestException | IOException | CloneNotSupportedException e) {
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
        }

    }
}
