package penguin.aes;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.Key;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class Temp {

	public static void main(String[] args) throws Exception 
    {
        File f = new File("Image-Assignment3.bmp");
        if(!f.exists()) { 
            System.out.println("Make sure tux.bmp exists in the current folder");
        }

        FileInputStream fs = new FileInputStream(f);
        
        int HEADER_LENGTH = 138;  // 14 byte bmp header
        byte header [] = new byte[HEADER_LENGTH];
        fs.read(header, 0, HEADER_LENGTH);
       
        
        byte[] content = new byte[fs.available()];
        fs.read(content);
        
        writeToFile(header, 
                    encrypt(content, "AES"), 
                    "tux_ecb.bmp");
       
        
        writeToFile(header, 
                    encrypt(content, "AES/CBC/PKCS5Padding"), 
                    "tux_cbc.bmp");
         
        fs.close();        
    }
    
    public static byte [] encrypt(byte [] input, String transformation) throws Exception
    {
        Cipher cipher = Cipher.getInstance(transformation);
        Key secretKey = new SecretKeySpec("I don't have CAT".getBytes(), "AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        return cipher.doFinal(input);        
    }
    
    public static void writeToFile(byte[] header, byte [] content, String fileToWrite) throws Exception
    {
        FileOutputStream fos = new FileOutputStream(fileToWrite);
        fos.write(header);
        fos.write(content);
        fos.flush();
        fos.close();    
    }

}
