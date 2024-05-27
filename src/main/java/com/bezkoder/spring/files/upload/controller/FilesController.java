package com.bezkoder.spring.files.upload.controller;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import com.bezkoder.spring.files.upload.message.ResponseMessage;
import com.bezkoder.spring.files.upload.model.FileInfo;
import com.bezkoder.spring.files.upload.service.FilesStorageService;
import com.dynamsoft.dbr.BarcodeReader;
import com.dynamsoft.dbr.BarcodeReaderException;
import com.dynamsoft.dbr.TextResult;

import java.io.*;

@Controller
@CrossOrigin("http://localhost:8081")
public class FilesController {

	@PostMapping("/upload")
	@ResponseBody
	public Map<String, String> uploadFile(@RequestParam("file") MultipartFile file) {
		String message = "";
		try {
			InputStream targetStream = file.getInputStream();
			String licence = "t0068lQAAAI+8+gDAuGp0bPZJUsJO4+7A2YYitmW0pW0z+mEtrYakTpC5MjxK4pIcUQBzP1nsjInUeL4JJUSy1JPzdsJGamk=;t0068lQAAAASktBRHut/+UaPlM3gJ5ktSg9JqQz1Ujdn1ojspBUaPxFtrNPzICaNJyEYqwVW9vT28qRUx0XYSS6wOzE4i+jU=";

			Map<String, String> map = ImageDecoding.extract(targetStream, licence);
			map.forEach((key, value) -> System.out.println(key + " " + value));

			message = "Uploaded the file successfully: " + file.getOriginalFilename();
			// return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(map));
			return map;
			// return map;
		} catch (Exception e) {
			message = "Could not upload the file: " + file.getOriginalFilename() + ". Error: " + e.getMessage();
			Map<String, String> map = new HashMap<>();
			map.put("Error", "Could not upload the file");
			// return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new
			// ResponseMessage(message));
			return map;

		}
	}

	public static void main(String[] args) throws FileNotFoundException {

		File initialFile = new File("C:\\ComBank\\ID.png");
		InputStream targetStream = new FileInputStream(initialFile);

		System.out.println("1");
		String licence = "t0068lQAAAI+8+gDAuGp0bPZJUsJO4+7A2YYitmW0pW0z+mEtrYakTpC5MjxK4pIcUQBzP1nsjInUeL4JJUSy1JPzdsJGamk=;t0068lQAAAASktBRHut/+UaPlM3gJ5ktSg9JqQz1Ujdn1ojspBUaPxFtrNPzICaNJyEYqwVW9vT28qRUx0XYSS6wOzE4i+jU=";

		extract(targetStream, licence);

	}

	public static String[] extract(InputStream stream, String licence) {

		if (stream == null) {
			System.out.println("Input  straem is null ....................");
		} else {
			System.out.println("Input  straem is not null");
		}

		TextResult[] results = null;
		String[] barCodeData = null;
		try {

			// String licence
			// ="t0068lQAAAI+8+gDAuGp0bPZJUsJO4+7A2YYitmW0pW0z+mEtrYakTpC5MjxK4pIcUQBzP1nsjInUeL4JJUSy1JPzdsJGamk=;t0068lQAAAASktBRHut/+UaPlM3gJ5ktSg9JqQz1Ujdn1ojspBUaPxFtrNPzICaNJyEYqwVW9vT28qRUx0XYSS6wOzE4i+jU=";
			BarcodeReader.initLicense(licence);
			BarcodeReader dbr = BarcodeReader.getInstance();
			if (dbr == null) {
				throw new Exception("Get BarCode Instance Failed.");
			}
			// System.out.println("before");
			results = dbr.decodeFileInMemory(stream, "");
			// System.out.println("after");
			// System.out.println("res :" + results.length);

			Map<String, String> map = new HashMap<>();

			if (results != null && results.length > 0) {
				for (int i = 0; i < results.length; i++) {
					TextResult result = results[i];
					String s = result.barcodeText;
					// System.out.println(s);
					barCodeData = s.split("\n");
					map = parseBarcodeText(s);
					break;
				}

				// map.forEach((key, value) -> System.out.println(key + " " + value));

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

		return barCodeData;
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
			e.printStackTrace();
			return null;
		}
	}

}
