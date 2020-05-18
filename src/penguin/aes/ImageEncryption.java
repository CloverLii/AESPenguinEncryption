package penguin.aes;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class ImageEncryption {
	private final static String imageName = "Image-Assignment3.bmp";
	private final static String keyStr = "770A8A65DA156D24EE2A093277530142";
	private final static int HEADER_LENGTH = 138;	// examine header length by GHex on Ubuntu
	private static byte[] header; // store the header of original image
	private static byte[] originContent; // store the content of image
	
	/*
	 * Read image to byte[] and record the header separately
	 */
	public static void readImage(String imgName) {
		try {
			File file = new File(imgName);
			 if(!file.exists()) { 
				 System.out.println("Image " + imgName + "does not exist!");
			 }
			 FileInputStream fs = new FileInputStream(file);	        
			// store header of original image for further use 
			 header = new byte[HEADER_LENGTH];
			 fs.read(header, 0, HEADER_LENGTH);
			 // read image content to byte array for encryption
			 originContent = new byte[fs.available()];
			 fs.read(originContent);
	        	// close FileInputStream when finished
	         fs.close(); 
		}catch(IOException e) {
			e.printStackTrace();
		}		 
	}
    /*
     *  Encrypt .bmp image to .jpg image with AES
     */
    public static byte [] encrypt(byte[] inputByte, String transformation) throws IllegalBlockSizeException, BadPaddingException{
    		Cipher cipher = null;
    		try {
    			// create instance of cipher
    	        cipher = Cipher.getInstance(transformation);
    	        // create AES 128bit key using given string
    	        Key key = new SecretKeySpec(keyStr.getBytes(), "AES");
    	        // update to unlimited
    	        System.out.println( "***** max allowed key length is: " + Cipher.getMaxAllowedKeyLength("AES"));
    	        // initialize the ciper and set mode
    	        cipher.init(Cipher.ENCRYPT_MODE, key);

    		}catch(NoSuchAlgorithmException e) {
    			e.printStackTrace();
    		}catch(NoSuchPaddingException e) {
    			e.printStackTrace();
    		}catch(InvalidKeyException e) {
    			e.printStackTrace();
    		} 
	    return cipher.doFinal(inputByte);
    }
    
    /*
     * Save encrypted .jpg image to input direction with new name
     */
    public static void saveImage( byte[] encryptedContent, String newImgName){
        try{
        		FileOutputStream fos = new FileOutputStream(newImgName);
        		// Appending the header of original image to make encrypted image available 
        		fos.write(header);
        		fos.write(encryptedContent);
        		fos.flush();
        		// close FileOutPutStream when finished
        		fos.close(); 
        }catch(IOException e) {
        		e.printStackTrace();
        }		   
    }
    
	public static void main(String[] args) throws Exception 
    {
		readImage(imageName);
        saveImage(encrypt(originContent, "AES"), "penguin_ecb.jpg");       
        saveImage(encrypt(originContent, "AES/CBC/PKCS5Padding"), "penguin_cbc.jpg");
        saveImage(encrypt(originContent, "AES/CFB/PKCS5Padding"), "penguin_cfb.jpg"); 
    }	
}
