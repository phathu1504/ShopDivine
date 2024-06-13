package com.example.ASM.admin.export;

import com.example.ASM.Entity.Staff;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.usermodel.*;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;

public class StaffExcelExport extends AbstractExportter {

	private XSSFWorkbook workbook;
	private XSSFSheet sheet;

	public StaffExcelExport() {
		workbook = new XSSFWorkbook();
	}

	private void writeHeaderLine() {
		sheet = workbook.createSheet("Staff");
		XSSFRow row = sheet.createRow(0);

		XSSFCellStyle cellStyle = workbook.createCellStyle();
		XSSFFont font = workbook.createFont();
		font.setBold(true);
		font.setFontHeight(16);
		cellStyle.setFont(font);

		this.createCell(row, 0, "Staff ID", cellStyle);
		this.createCell(row, 1, "FullName", cellStyle);
		this.createCell(row, 2, "E-mail", cellStyle);
		this.createCell(row, 3, "Gender", cellStyle);
		this.createCell(row, 4, "Phone", cellStyle);
		this.createCell(row, 5, "Enabled", cellStyle);
		this.createCell(row, 6, "Address", cellStyle);
		this.createCell(row, 7, "Created Date", cellStyle);
	}

	private void createCell(XSSFRow row, int columnIndex, Object value, CellStyle cellStyle) {
		XSSFCell cell = row.createCell(columnIndex);
		sheet.autoSizeColumn(columnIndex);

		if (value instanceof Integer) {
			cell.setCellValue((Integer) value);
		} else if (value instanceof Boolean) {
			cell.setCellValue((Boolean) value);
		} else if (value instanceof Timestamp) {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			cell.setCellValue(dateFormat.format((Timestamp) value));
		} else {
			cell.setCellValue((String) value);
		}
		cell.setCellStyle(cellStyle);
	}

	private void writeDataLines(List<Staff> listUsers) {
		int rowIndex = 1;

		XSSFCellStyle cellStyle = workbook.createCellStyle();
		XSSFFont font = workbook.createFont();
		font.setFontHeight(12);
		cellStyle.setFont(font);

		for (Staff user : listUsers) {
			XSSFRow row = sheet.createRow(rowIndex++);
			int columnIndex = 0;

			createCell(row, columnIndex++, user.getStaffID(), cellStyle);
			createCell(row, columnIndex++, user.getFullName(), cellStyle);
			createCell(row, columnIndex++, user.getEmail(), cellStyle);
			createCell(row, columnIndex++, user.getGender(), cellStyle);
			createCell(row, columnIndex++, user.getPhone(), cellStyle);
			createCell(row, columnIndex++, user.isEnabled(), cellStyle);
			createCell(row, columnIndex++, user.getAddress(), cellStyle);
			createCell(row, columnIndex++, user.getCreateDate(), cellStyle);
		}
	}

	public void export(List<Staff> listUsers, HttpServletResponse response) throws IOException {
		super.setResponseHeader(response, "application/octet-stream", ".xlsx");

		this.writeHeaderLine();
		this.writeDataLines(listUsers);

		ServletOutputStream outputStream = response.getOutputStream();
		workbook.write(outputStream);
		workbook.close();
		outputStream.close();
	}
}
