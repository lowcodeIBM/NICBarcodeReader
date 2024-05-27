package com.bezkoder.spring.files.upload.controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import com.dynamsoft.dbr.*;

public class BarcodeReaderService {

    private ImageBytesBarcodeReader imageBytesReader;

    public BarcodeReaderService() {
        imageBytesReader = new ImageBytesBarcodeReader();
    }

    public String readBarcodesFromImageBytes(File b) {
        try {
            BarcodeReader.initLicense("t0068lQAAAI+8+gDAuGp0bPZJUsJO4+7A2YYitmW0pW0z+mEtrYakTpC5MjxK4pIcUQBzP1nsjInUeL4JJUSy1JPzdsJGamk=;t0068lQAAAASktBRHut/+UaPlM3gJ5ktSg9JqQz1Ujdn1ojspBUaPxFtrNPzICaNJyEYqwVW9vT28qRUx0XYSS6wOzE4i+jU=");
           System.out.println("Licence is OK");
            return imageBytesReader.decodeImageFileBytes(b);
        } catch (BarcodeReaderException | IOException ex) {
            ex.printStackTrace();
            return null;
        }catch(Exception e) {
        	e.printStackTrace();
        }
        System.out.println("Nnnnnnnnnnnnnnnnnnnnnnnnnn");
		return null;
    }
}
