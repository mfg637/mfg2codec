package mfg637.crypt.mfg2.codec;

import mfg637.codec.BytesCodec;
import mfg637.codec.Codec;

import java.security.DigestException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import static java.lang.Integer.min;

public class MFG2Codec implements BytesCodec {
    protected byte[] password;
    protected MessageDigest md;
    protected byte[] reverse(byte[] data){
        int length = data.length;
        byte[] reversed = new byte[length];
        int rev_len = length / 2;
        for (int i=0; i<rev_len; i++){
            reversed[i] = data[length - i - 1];
            reversed[length - i - 1] = data[i];
        }
        if (length%2==1){
            reversed[rev_len] = data[rev_len];
        }
        return reversed;
    }
    public MFG2Codec(byte[] password) throws NoSuchAlgorithmException {
        md = MessageDigest.getInstance("SHA-512");
        this.password = md.digest(password);
    }
    @Override
    public byte[] encode(byte[] data) throws CloneNotSupportedException {
        byte[] hash = password.clone();
        int length = data.length;
        byte[] reversed = this.reverse(data);
        byte[] encoded = new byte[length];
        int enc_size = 0;
        while (enc_size<length){
            int block_size = min(hash.length, length-enc_size);
            for (int i=0; i<block_size; i++){
                encoded[enc_size+i] = (byte) (reversed[enc_size+i] ^ hash[i]);
            }
            if (enc_size==0)
                md.reset();
            md.update(Arrays.copyOfRange(reversed, enc_size, enc_size+block_size));
            MessageDigest md_copy = (MessageDigest) md.clone();
            hash = md_copy.digest();
            enc_size += block_size;
        }
        return encoded;
    }

    @Override
    public byte[] decode(byte[] code) throws CloneNotSupportedException {
        byte[] hash = password.clone();
        int length = code.length;
        byte[] reversed = new byte[length];
        int dec_size = 0;
        while (dec_size<length){
            int block_size = min(hash.length, length-dec_size);
            for (int i=0; i<block_size; i++){
                reversed[dec_size+i] = (byte) (code[dec_size+i] ^ hash[i]);
            }
            if (dec_size==0)
                md.reset();
            md.update(Arrays.copyOfRange(reversed, dec_size, dec_size+block_size));
            MessageDigest md_copy = (MessageDigest) md.clone();
            hash = md_copy.digest();
            dec_size += block_size;
        }
        return reverse(reversed);
    }
}
