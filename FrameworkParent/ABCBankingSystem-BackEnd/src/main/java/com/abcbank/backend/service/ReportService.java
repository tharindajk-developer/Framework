/*
 *ABC Bank 2022
 */
package com.abcbank.backend.service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.abcbank.backend.entity.Transaction;
import com.abcbank.backend.entity.User;
import com.abcbank.backend.repository.UserRepository;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

/*
 * tharinda.jayamal@gmail.com
 */

@Service
public class ReportService {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Value("${transaction.report.template}")
	private String trReportTemplate;

	@Autowired
	private UserRepository userRepository;

	public byte[] generatedPDF(String userId, String outputhFilePath)
			throws DocumentException, IOException {

		User user = userRepository.findById(userId.trim()).get();

		if (user.getIndividualCustomer() != null) {
			log.info("Preparing transaction report for "
					+ user.getIndividualCustomer().getAccount().getAccountNo());
		} else {
			log.info("Preparing transaction report for "
					+ user.getCorporateCustomer().getAccount().getAccountNo());
		}
		OutputStream fos = new FileOutputStream(new File(outputhFilePath));

		PdfReader pdfReader = new PdfReader(trReportTemplate);
		PdfStamper pdfStamper = new PdfStamper(pdfReader, fos);

		if (user.getCorporateCustomer() != null) {
			for (int i = 1; i <= pdfReader.getNumberOfPages(); i++) {
				if (i == 1) {
					PdfContentByte pdfContentByte = pdfStamper
							.getOverContent(i);

					pdfContentByte.beginText();
					pdfContentByte.setFontAndSize(BaseFont.createFont(
							BaseFont.COURIER, BaseFont.CP1257,
							BaseFont.EMBEDDED), 10);

					pdfContentByte.setTextMatrix(102, 622);
					pdfContentByte.showText(user.getCorporateCustomer()
							.getAccount().getAccountNo());

					pdfContentByte.setTextMatrix(102, 601);
					pdfContentByte.showText(new SimpleDateFormat(
							"dd-MM-yyyy HH:mm").format(new Date()));

					pdfContentByte.setTextMatrix(102, 580);
					pdfContentByte.showText(user.getCorporateCustomer()
							.getName());

					int x = 102;
					int y = 557;

					if (user.getCorporateCustomer().getAccount()
							.getTransactions() != null) {

						pdfContentByte.setTextMatrix(x, y);
						pdfContentByte.setTextMatrix(x, y);
						pdfContentByte.showText("Reference" + "     " + "Amount"
								+ "   " + "From" + "            " + "To" + "             "
								+ "Date" + "               " + "Type");
						y -= 13;

						for (Transaction transaction : user
								.getCorporateCustomer().getAccount()
								.getTransactions()) {
							if (StringUtils.isEmpty(transaction.getFromAcc())) {
								transaction.setFromAcc("----Bank----");
							}
							if (StringUtils.isEmpty(transaction.getToAcc())) {
								transaction.setToAcc("----Bank----");
							}
							pdfContentByte.setTextMatrix(x, y);
							pdfContentByte.showText(transaction
									.getTransactionRef()
									+ "   "
									+ transaction.getTransactionAmount()
											.setScale(2, RoundingMode.CEILING)
									+ "   "
									+ transaction.getFromAcc()
									+ "   "
									+ transaction.getToAcc()
									+ "   "
									+ new SimpleDateFormat("dd-MM-yyyy HH:mm")
											.format(transaction
													.getTransactionDate())
									+ "   " + transaction.getTransactionType());
							y -= 13;
						}
					}

					pdfContentByte.endText();
				}

			}
		}

		if (user.getIndividualCustomer() != null) {
			for (int i = 1; i <= pdfReader.getNumberOfPages(); i++) {
				if (i == 1) {
					PdfContentByte pdfContentByte = pdfStamper
							.getOverContent(i);

					pdfContentByte.beginText();
					pdfContentByte.setFontAndSize(BaseFont.createFont(
							BaseFont.COURIER, BaseFont.CP1257,
							BaseFont.EMBEDDED), 10);

					pdfContentByte.setTextMatrix(102, 622);
					pdfContentByte.showText(user.getIndividualCustomer()
							.getAccount().getAccountNo());

					pdfContentByte.setTextMatrix(102, 601);
					pdfContentByte.showText(new SimpleDateFormat(
							"dd-MM-yyyy HH:mm").format(new Date()));

					pdfContentByte.setTextMatrix(102, 580);
					pdfContentByte.showText(user.getIndividualCustomer()
							.getFirstName()
							+ " "
							+ user.getIndividualCustomer().getLastName());

					int x = 102;
					int y = 557;

					if (user.getIndividualCustomer().getAccount()
							.getTransactions() != null) {

						pdfContentByte.setTextMatrix(x, y);
						pdfContentByte.showText("Reference" + "     " + "Amount"
								+ "   " + "From" + "            " + "To" + "             "
								+ "Date" + "               " + "Type");
						y -= 13;

						for (Transaction transaction : user
								.getIndividualCustomer().getAccount()
								.getTransactions()) {
							if (StringUtils.isEmpty(transaction.getFromAcc())) {
								transaction.setFromAcc("----Bank----");
							}
							if (StringUtils.isEmpty(transaction.getToAcc())) {
								transaction.setToAcc("----Bank----");
							}
							pdfContentByte.setTextMatrix(x, y);
							pdfContentByte.showText(transaction
									.getTransactionRef()
									+ "   "
									+ transaction.getTransactionAmount()
											.setScale(2, RoundingMode.CEILING)
									+ "   "
									+ transaction.getFromAcc()
									+ "   "
									+ transaction.getToAcc()
									+ "   "
									+ new SimpleDateFormat("dd-MM-yyyy HH:mm")
											.format(transaction
													.getTransactionDate())
									+ "   " + transaction.getTransactionType());
							y -= 13;
						}
					}

					pdfContentByte.endText();
				}

			}
		}

		pdfStamper.close();
		pdfReader.close();

		return readFile(outputhFilePath);
	}

	private byte[] readFile(String path) throws IOException {
		InputStream is = new FileInputStream(path);
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		int nRead;
		byte[] data = new byte[1024];
		while ((nRead = is.read(data, 0, data.length)) != -1) {
			buffer.write(data, 0, nRead);
		}
		buffer.flush();
		return buffer.toByteArray();
	}

}
