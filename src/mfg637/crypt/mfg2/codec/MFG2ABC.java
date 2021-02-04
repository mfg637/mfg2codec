package mfg637.crypt.mfg2.codec;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public abstract class MFG2ABC {
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
    public MFG2ABC(byte[] password) throws NoSuchAlgorithmException {
        md = MessageDigest.getInstance("SHA-512");
        this.password = md.digest(password);
    }
}
