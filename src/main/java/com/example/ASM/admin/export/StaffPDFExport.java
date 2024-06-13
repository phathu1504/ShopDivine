package com.example.ASM.admin.export;

import com.example.ASM.Entity.Staff;
import com.lowagie.text.Font;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import jakarta.servlet.http.HttpServletResponse;

import java.awt.*;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

public class StaffPDFExport extends AbstractExportter {

	public void export(List<Staff> listUsers, HttpServletResponse response) throws IOException {
		super.setResponseHeader(response, "application/pdf", ".pdf");

		Document document = new Document(PageSize.A4);
		PdfWriter.getInstance(document, response.getOutputStream());

		document.open();

		Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
		font.setSize(18);
		font.setColor(Color.BLUE);

		Paragraph paragraph = new Paragraph("List of Staff", font);
		paragraph.setAlignment(Paragraph.ALIGN_CENTER);

		document.add(paragraph);

		PdfPTable table = new PdfPTable(8);
		table.setWidthPercentage(100f);
		table.setSpacingBefore(10);
		table.setWidths(new float[] { 3.0f, 10.0f, 10.0f, 4.0f, 10.0f, 3.5f, 10.0f, 15.0f });

		this.writeTableHeader(table);

		this.writeTableData(table, listUsers);

		document.add(table);

		document.close();
	}

	private void writeTableData(PdfPTable table, List<Staff> listUsers) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		for (Staff user : listUsers) {
			table.addCell(String.valueOf(user.getStaffID()));
			table.addCell(user.getFullName());
			table.addCell(user.getEmail());
			table.addCell(user.getGender());
			table.addCell(user.getPhone());
			table.addCell(user.isEnabled() ? "Male" : "Female");
			table.addCell(user.getAddress());
			table.addCell(user.getCreateDate() != null ? dateFormat.format(user.getCreateDate()) : "null");
		}
	}

	private void writeTableHeader(PdfPTable table) {
		PdfPCell cell = new PdfPCell();
		cell.setBackgroundColor(Color.BLUE);
		cell.setPadding(5);

		Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
		font.setColor(Color.WHITE);

		cell.setPhrase(new Phrase("ID", font));
		table.addCell(cell);

		cell.setPhrase(new Phrase("FullName", font));
		table.addCell(cell);

		cell.setPhrase(new Phrase("E-mail", font));
		table.addCell(cell);

		cell.setPhrase(new Phrase("Gender", font));
		table.addCell(cell);

		cell.setPhrase(new Phrase("Phone", font));
		table.addCell(cell);

		cell.setPhrase(new Phrase("Enabled", font));
		table.addCell(cell);

		cell.setPhrase(new Phrase("Address", font));
		table.addCell(cell);

		cell.setPhrase(new Phrase("Created date", font));
		table.addCell(cell);
	}
}
