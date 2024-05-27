package com.bezkoder.spring.files.upload.controller;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

import com.dynamsoft.dbr.*;

public class ImageDecoding {

	public static Map<String,String> extract(InputStream stream,String licenseKey) {
		
		
		if ( stream == null) {
			System.out.println("Input  straem is null ....................");
		}else {
			System.out.println("Input  straem is not null");
		}

		Map<String,String> map = new HashMap<>();
		TextResult[] results = null;
		String[] barCodeData = null;
		try {

			BarcodeReader.initLicense(licenseKey);
		//	BarcodeReader dbr = BarcodeReader.getInstance();
			BarcodeReader dbr = new BarcodeReader();

			if (dbr == null) {
				throw new Exception("Get BarCode Instance Failed.");
			}
			System.out.println("before");
			results = dbr.decodeFileInMemory(stream, "");
			System.out.println("after");
			System.out.println("res :" + results.length);

			
			
			if (results != null && results.length > 0) {
				for (int i = 0; i < results.length; i++) {
					TextResult result = results[i];
					String s = result.barcodeText;
					//System.out.println(i +"->"+ s);
					barCodeData = s.split("\n");
					map = parseBarcodeText(s);
					break;
				}
				
			} else {
				System.out.println("No BarCode data detected.");
			}

			dbr.recycle();
		} catch (BarcodeReaderException ex) {
			System.out.println(ex.getMessage());
			ex.printStackTrace();
			
		} catch (IOException ex) {
			System.out.println(ex.getMessage());
			ex.printStackTrace();
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
			ex.printStackTrace();
		}

		return map;
	}
	
	 private static Map<String, String> parseBarcodeText(String barcodeText) {
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
}
