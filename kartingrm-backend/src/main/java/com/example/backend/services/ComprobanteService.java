package com.example.backend.services;

import com.example.backend.entities.Comprobante;
import com.example.backend.entities.Reserva;
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import jakarta.mail.internet.MimeMessage;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;
import java.util.stream.Stream;

@Service
public class ComprobanteService {

    @Autowired
    private JavaMailSender mailSender;


    private static final DateTimeFormatter FORMAT_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter FORMAT_HORA = DateTimeFormatter.ofPattern("HH:mm");


    public byte[] generarPdf(Reserva reserva) throws Exception {
        Document doc = new Document();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter.getInstance(doc, baos);
        doc.open();

        com.lowagie.text.Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
        String fechaFormato = reserva.getFechaHora().format(FORMAT_FECHA);
        String horaInicio = reserva.getFechaHora().format(FORMAT_HORA);
        String horaFin = reserva.getFechaHora()
                .plusMinutes(reserva.getTarifa().getDuracionTotalMinutos())
                .format(FORMAT_HORA);
        doc.add(new Paragraph("Comprobante de Reserva", titleFont));
        doc.add(new Paragraph("Reserva #" + reserva.getId()));
        doc.add(new Paragraph("Fecha de reserva: " + fechaFormato));
        doc.add(new Paragraph("Horario: " + horaInicio + " - " + horaFin));
        doc.add(new Paragraph("Cantidad de personas: " + reserva.getCantidadPersonas()));
        doc.add(new Paragraph(" "));

        PdfPTable table = new PdfPTable(7);
        table.setWidthPercentage(100);
        Stream.of("Cliente", "Tarifa", "Grupo", "Frecuencia", "Promo", "IVA", "Total")
                .forEach(col -> table.addCell(new PdfPCell(new Phrase(col))));

        for (Comprobante c : reserva.getComprobantes()) {
            table.addCell(c.getCliente().getNombre());
            table.addCell(String.valueOf(c.getTarifaBase()));
            table.addCell(String.valueOf(c.getDescuentoGrupo()));
            table.addCell(String.valueOf(c.getDescuentoFrecuente()));
            table.addCell(String.valueOf(c.getDescuentoEspecial()));
            table.addCell(String.valueOf(c.getIva()));
            table.addCell(String.valueOf(c.getTotal()));
        }

        doc.add(table);

        doc.add(new Paragraph(" "));
        doc.add(new Paragraph("Total Reserva: $" + reserva.getTotalConIva(),
                FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14)));

        doc.close();
        return baos.toByteArray();
    }


    public void enviarComprobantePorEmail(Reserva reserva) throws Exception {
        byte[] pdfBytes = generarPdf(reserva);
        byte[] excelBytes = generarExcel(reserva);

        for (Comprobante comp : reserva.getComprobantes()) {
            String destinatario = comp.getCliente().getEmail();
            enviarCorreo(destinatario, pdfBytes, excelBytes, reserva.getId());
        }
    }

    private void enviarCorreo(String destinatario, byte[] pdf, byte[] excel, Long reservaId) throws Exception {
        MimeMessage mensaje = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mensaje, true);

        helper.setTo(destinatario);
        helper.setSubject("Tu comprobante de reserva - KartingRM");
        helper.setText("Adjuntamos el comprobante de tu reserva en PDF y Excel. ¡Gracias por preferirnos!");

        helper.addAttachment("comprobante_" + reservaId + ".pdf", new ByteArrayResource(pdf));

        helper.addAttachment("comprobante_" + reservaId + ".xlsx", new ByteArrayResource(excel));

        mailSender.send(mensaje);
    }




    public byte[] generarExcel(Reserva reserva) throws Exception {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Comprobante");

        org.apache.poi.ss.usermodel.Row header = sheet.createRow(0);
        String[] columnas = {"Cliente", "Tarifa Base", "Descuento Grupo", "Descuento Frecuencia", "Descuento Especial", "IVA", "Total"};

        for (int i = 0; i < columnas.length; i++) {
            org.apache.poi.ss.usermodel.Cell cell = header.createCell(i);
            cell.setCellValue(columnas[i]);
        }

        int rowIdx = 1;
        for (Comprobante c : reserva.getComprobantes()) {
            org.apache.poi.ss.usermodel.Row row = sheet.createRow(rowIdx++);
            row.createCell(0).setCellValue(c.getCliente().getNombre());
            row.createCell(1).setCellValue(c.getTarifaBase());
            row.createCell(2).setCellValue(c.getDescuentoGrupo());
            row.createCell(3).setCellValue(c.getDescuentoFrecuente());
            row.createCell(4).setCellValue(c.getDescuentoEspecial());
            row.createCell(5).setCellValue(c.getIva());
            row.createCell(6).setCellValue(c.getTotal());
        }

        for (int i = 0; i < columnas.length; i++) {
            sheet.autoSizeColumn(i);
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        workbook.write(baos);
        workbook.close();

        return baos.toByteArray();
    }

}
