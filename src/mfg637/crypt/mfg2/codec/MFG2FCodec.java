package mfg637.crypt.mfg2.codec;

import mfg637.codec.FilesCodec;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class MFG2FCodec extends MFG2ABC implements FilesCodec {
    protected byte[] crypt(byte[] input, byte[] hash, int block_size){
        byte[] out_buffer = new byte[block_size];
        for (int i=0; i<block_size; i++){
            out_buffer[i] = (byte) (input[i] ^ hash[i]);
        }
        return out_buffer;
    }


    public MFG2FCodec(byte[] password) throws NoSuchAlgorithmException {
        super(password);
    }

    @Override
    public void encode(RandomAccessFile input_file, FileOutputStream output_file)
            throws IOException, CloneNotSupportedException
    {
        byte[] hash = password.clone();
        long file_size = input_file.length();
        long default_block_size = hash.length;
        boolean first_block = true;
        for (
                long current_position = file_size - default_block_size, prev_position = file_size;
                ;
                prev_position=current_position,
                        current_position=Math.max(current_position - default_block_size, 0)
        ){
            int block_size = (int)(prev_position - current_position);
            input_file.seek(current_position);
            byte[] src_buffer = new byte[block_size];
            input_file.read(src_buffer, 0, block_size);
            byte[] reversed = this.reverse(src_buffer);
            byte[] out_buffer = crypt(reversed, hash, block_size);
            output_file.write(out_buffer, 0, block_size);
            if (first_block) {
                md.reset();
                first_block = false;
            }
            md.update(Arrays.copyOf(reversed, block_size));
            MessageDigest md_copy = (MessageDigest) md.clone();
            hash = md_copy.digest();
            if (current_position == 0)
                break;
        }
    }

    @Override
    public void decode(File input_file, RandomAccessFile output_file)
            throws IOException, CloneNotSupportedException
    {
        byte[] hash = password.clone();
        long file_size = input_file.length();
        output_file.setLength(file_size);
        long default_block_size = hash.length;
        //byte[] out_buffer = new byte[(int) default_block_size];
        boolean first_block = true;
        FileInputStream input_file_reader = new FileInputStream(input_file);
        for (
                long current_position = file_size - default_block_size, prev_position = file_size;
                ;
                prev_position=current_position,
                        current_position=Math.max(current_position - default_block_size, 0)
        ){
            int block_size = (int)(prev_position - current_position);
            output_file.seek(current_position);
            byte[] src_buffer;
            src_buffer = input_file_reader.readNBytes(block_size);
            byte[] reversed = crypt(src_buffer, hash, block_size);
            byte[] output = reverse(reversed);
            output_file.write(output, 0, block_size);
            if (first_block) {
                md.reset();
                first_block = false;
            }
            md.update(Arrays.copyOf(reversed, block_size));
            MessageDigest md_copy = (MessageDigest) md.clone();
            hash = md_copy.digest();
            if (current_position == 0)
                break;
        }
    }
}
