package com.bezkoder.spring.files.upload.controller;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.imageio.ImageIO;
import java.util.HashMap;
import java.util.Map;

import com.dynamsoft.dbr.*;

class ImageData {
    public byte[] bytes;
    public int width;
    public int height;
    public int stride;
    public int format;
}

public class ImageBytesBarcodeReader {

    private ImageData cvtToImageData(File f) throws IOException {
    	
    	System.out.println("cvtToImageData");
    	
    	 // InputStream targetStream = new ByteArrayInputStream(f);
      //  BufferedImage in = ImageIO.read(targetStream);
        BufferedImage in = ImageIO.read(f);
        
        BufferedImage image = new BufferedImage(in.getWidth(), in.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D graphics = image.createGraphics();
        graphics.drawImage(in, null, 0, 0);
        graphics.dispose();

        int stride = ((24 * image.getWidth() + 31) / 32) * 4;

        ByteArrayOutputStream output = new ByteArrayOutputStream(image.getHeight() * stride);
        Raster raster = image.getRaster().createChild(0, 0, image.getWidth(), image.getHeight(), 0, 0, new int[]{2, 1, 0});
        byte[] row = new byte[stride];

        for (int i = 0; i < image.getHeight(); i++) {
            row = (byte[]) raster.getDataElements(0, i, image.getWidth(), 1, row);
            output.write(row);
        }

        ImageData imgData = new ImageData();
        imgData.width = image.getWidth();
        imgData.height = image.getHeight();
        imgData.bytes = output.toByteArray();
        imgData.stride = stride;
        imgData.format = EnumImagePixelFormat.IPF_BGR_888;
System.out.println("2");
        return imgData;
    }

    public String decodeImageFileBytes(File is) throws BarcodeReaderException, IOException {
    	
    	
    	
    	System.out.println("decodeImageFileBytes");
        ImageData imgData = cvtToImageData(is);
        System.out.println("getting reader");
        BarcodeReader dbr = BarcodeReader.getInstance();
        System.out.println("444");
        TextResult[] results = dbr.decodeBuffer(imgData.bytes, imgData.width, imgData.height, imgData.stride, imgData.format, "");
        System.out.println("5555");
        if (results != null && results.length > 0) {
        //    return parseBarcodeText(results[0].barcodeText);
        	System.out.println(results[0].barcodeText);
            return parseBarcodeData(results[0].barcodeText);
        } else {
            System.out.println("No data detected.");
            return "No data detected.";
        }
    }

    private Map<String, String> parseBarcodeText(String barcodeText) {
        try {
            String[] lines = barcodeText.split("\n");
            Map<String, String> parsedData = new HashMap<>();
            parsedData.put("IDNo", lines[1]);
            parsedData.put("DOB", lines[2]);
            parsedData.put("Sex", lines[3]);
            parsedData.put("IssueDate", lines[4]);
            parsedData.put("Name", lines[6]);
            parsedData.put("Address", lines[7]);
            parsedData.put("PlaceOfBirth", lines[8]);
            return parsedData;
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }
    
    
    private String parseBarcodeData(String barcodeText) {
     return barcodeText ;
    }
}
