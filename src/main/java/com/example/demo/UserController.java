package com.example.demo;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;
import java.lang.NumberFormatException;
import java.lang.Float;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.BufferedInputStream;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.opencsv.CSVReader;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
class UserController {

	private final UserRepository repository;

	UserController(UserRepository repository) {
		this.repository = repository;
	}

	//frontend interface
	@GetMapping("/")
    ModelAndView home() {
        return new ModelAndView("index");
    }


	@GetMapping("/users")
	JSONObject all() {
		JSONObject result = new JSONObject();
		JSONArray userlist = new JSONArray();

		List<User> list = repository.findAllInRange(0f, 4000f);
		Iterator userIterator = list.iterator();
		while(userIterator.hasNext()) {
			User user = (User)userIterator.next();

			JSONObject userjson = new JSONObject();
			userjson.put("name", user.getName());
		    userjson.put("salary", String.format("%.02f", user.getSalary()));

		    userlist.add(userjson);
		}

		result.put("results", userlist);
		return result;
	}

	private ResponseEntity<?> processInputStream(InputStream istream) {
		try {
	    	InputStreamReader isReader = new InputStreamReader(istream);
	    	CSVReader csvReader = new CSVReader(isReader);
	        String[] header = csvReader.readNext();
	        if (header == null || header.length != 2) {
	        	return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid header format");
	        } else if (!header[0].trim().toLowerCase().equals("name") || 
	        		   !header[1].trim().toLowerCase().equals("salary")){
	        	return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid header fields");
	        }


	        String[] record = null;
	        List<User> userlist = new ArrayList<User>();
	        while ((record = csvReader.readNext()) != null) {
	        	userlist.add(new User(record[0], Float.parseFloat(record[1].trim())));

	        }

	        csvReader.close();

	        // write to database
	        repository.saveAll(userlist);

    	} catch (IOException e) {

    	} catch (NumberFormatException e) {
    		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid number format");
    	}

    	return ResponseEntity.status(HttpStatus.OK).body(null);
	}

	@PostMapping(value = "/users", consumes={"text/csv", "text/plain"})
    public ResponseEntity<?> uploadSimple(InputStream body) {
    	return processInputStream(body);
    }

    // for file uploads
    @PostMapping(value = "/users", consumes="multipart/form-data") 
    public ResponseEntity<?> fileUpload(@RequestParam MultipartFile file) throws IOException {
    	InputStream inStream = new BufferedInputStream(file.getInputStream());
    	return processInputStream(inStream);
    }

}