package mfg637.crypt.mfg2.codec;

import java.security.DigestException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import static java.lang.Integer.min;

public class MFG2Fast extends MFG2Codec {
    public MFG2Fast(byte[] password) throws NoSuchAlgorithmException {
        super(password);
    }

    public byte[] encode(byte[] data) {
        byte[] hash = password.clone();
        int length = data.length;
        int hash_len = hash.length;
        byte[] reversed = this.reverse(data);
        byte[] encoded = new byte[length];
        int enc_size = 0;
        while (enc_size<length){
            int block_size = min(hash_len, length-enc_size);
            for (int i=0; i<block_size; i++){
                encoded[enc_size+i] = (byte) (reversed[enc_size+i] ^ hash[i]);
            }
            byte[] buffer = Arrays.copyOf(hash, hash_len+block_size);
            System.arraycopy(reversed, enc_size , buffer, hash_len, block_size);
            hash = md.digest(buffer);
            enc_size += block_size;
        }
        return encoded;
    }

    @Override
    public byte[] decode(byte[] code) {
        byte[] hash = password.clone();
        int length = code.length;
        byte[] reversed = new byte[length];
        int hash_len = hash.length;
        int dec_size = 0;
        while (dec_size<length){
            int block_size = min(hash.length, length-dec_size);
            for (int i=0; i<block_size; i++){
                reversed[dec_size+i] = (byte) (code[dec_size+i] ^ hash[i]);
            }
            byte[] buffer = Arrays.copyOf(hash, hash_len+block_size);
            System.arraycopy(reversed, dec_size , buffer, hash_len, block_size);
            hash = md.digest(buffer);
            dec_size += block_size;
        }
        return reverse(reversed);
    }
}
