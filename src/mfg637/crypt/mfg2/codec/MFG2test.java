package mfg637.crypt.mfg2.codec;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import static java.lang.Integer.min;

public class MFG2test extends MFG2Codec implements TestableCodec {
    public MFG2test(byte[] password) throws NoSuchAlgorithmException {
        super(password);
    }

    @Override
    public byte[] reversing(byte[] input) {
        return reverse(input);
    }

    public byte[] decrypt(byte[] input) throws CloneNotSupportedException {
        byte[] hash = password.clone();
        int length = input.length;
        byte[] output = new byte[length];
        int dec_size = 0;
        while (dec_size<length){
            int block_size = min(hash.length, length-dec_size);
            for (int i=0; i<block_size; i++){
                output[dec_size+i] = (byte) (input[dec_size+i] ^ hash[i]);
            }
            if (dec_size==0)
                md.reset();
            md.update(Arrays.copyOfRange(output, dec_size, dec_size+block_size));
            MessageDigest md_copy = (MessageDigest) md.clone();
            hash = md_copy.digest();
            dec_size += block_size;
        }
        return output;
    }
}
