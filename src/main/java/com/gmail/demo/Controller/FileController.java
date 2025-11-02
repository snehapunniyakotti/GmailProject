package com.gmail.demo.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gmail.demo.service.api.FileService;

@RestController
@RequestMapping("/file")
public class FileController {

	@Autowired
	private FileService fileService;

	@GetMapping("/download/{id}")
	public ResponseEntity<Resource> getfileById(@PathVariable Integer id) {
		try {
			System.out.println(" id in file controller  "+id);
			ResponseEntity<Resource> res = fileService.getFileById(id);
			System.out.println(" res in file download : "+res);
			return res;
		} catch (Exception e) {
			e.printStackTrace(); 
			return ResponseEntity.internalServerError().build();
		}
	}

}
