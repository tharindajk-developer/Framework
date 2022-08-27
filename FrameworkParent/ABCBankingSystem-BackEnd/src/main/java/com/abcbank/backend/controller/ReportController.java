/*
 *ABC Bank 2022
 */
package com.abcbank.backend.controller;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.abcbank.backend.service.ReportService;


/*
 *tharinda.jayamal@gmail.com
 */
@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/report")
public class ReportController {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private ReportService reportService;

	@Value("${transaction.report.path}")
	private String transactionPath;

	@GetMapping("/transaction/{userId}")
	public ResponseEntity<byte[]> getSmbPerformanceReport(@PathVariable(value = "userId") String userId)
			throws Exception {

		try {
			log.info("Creating transaction report");

			String outputhFilePath = null;
			String pattern = "ddMMyyyy_HHmmSS";
			SimpleDateFormat format = new SimpleDateFormat(pattern);
			outputhFilePath = transactionPath + userId + format.format(new Date()) + ".pdf";
			byte[] outArray = reportService.generatedPDF(userId, outputhFilePath);

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_PDF);
			headers.setContentDispositionFormData(outputhFilePath, outputhFilePath);
			headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
			ResponseEntity<byte[]> response = new ResponseEntity<>(outArray, headers, HttpStatus.OK);
			return response;
		} catch (Exception e) {
			log.error("Error occured when creating report ", e);
		}
		return null;
	}
}
