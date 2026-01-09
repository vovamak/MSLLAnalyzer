package com.aflt.order.service;

import com.aflt.order.entity.PurchaseOrder;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class PurchaseOrderExcelParserService {
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    public List<PurchaseOrder> parseExcelFile(MultipartFile file) throws Exception {
        List<PurchaseOrder> purchaseOrders = new ArrayList<>();

        try (InputStream inputStream = file.getInputStream()) {
            Workbook workbook = WorkbookFactory.create(inputStream);
            Sheet sheet = workbook.getSheetAt(0);

            // Пропускаем заголовок (первую строку)
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) {
                    continue;
                }

                PurchaseOrder order = parseRowToPurchaseOrder(row);

                // Проверяем обязательные поля перед добавлением
                if (order.getOrderNo() != null && !order.getOrderNo().trim().isEmpty() &&
                        order.getPartNo() != null && !order.getPartNo().trim().isEmpty()) {
                    purchaseOrders.add(order);
                }
            }

            workbook.close();
        }

        return purchaseOrders;
    }

    private PurchaseOrder parseRowToPurchaseOrder(Row row) {
        PurchaseOrder order = new PurchaseOrder();

        // Order No. (столбец 0)
        order.setOrderNo(getCellValueAsString(row.getCell(0)));

        // Ext. Order No. (столбец 1)
        order.setExtOrderNo(getCellValueAsString(row.getCell(1)));

        // Date (столбец 2)
        String dateStr = getCellValueAsString(row.getCell(2));
        if (dateStr != null && !dateStr.trim().isEmpty()) {
            try {
                order.setDate(LocalDate.parse(dateStr.trim(), dateFormatter));
            } catch (Exception e) {
                System.err.println("Error parsing order date: " + dateStr);
            }
        }

        // Part No. (столбец 3)
        order.setPartNo(getCellValueAsString(row.getCell(3)));

        // Backorder (столбец 4)
        String backorderStr = getCellValueAsString(row.getCell(4));
        if (backorderStr != null && !backorderStr.trim().isEmpty()) {
            try {
                order.setBackorder(Integer.parseInt(backorderStr.trim()));
            } catch (NumberFormatException e) {
                order.setBackorder(0);
            }
        }

        // Shipment No. (столбец 5)
        order.setShipmentNo(getCellValueAsString(row.getCell(5)));

        // Target Date (столбец 6)
        String targetDateStr = getCellValueAsString(row.getCell(6));
        if (targetDateStr != null && !targetDateStr.trim().isEmpty()) {
            try {
                order.setTargetDate(LocalDate.parse(targetDateStr.trim(), dateFormatter));
            } catch (Exception e) {
                System.err.println("Error parsing target date: " + targetDateStr);
            }
        }

        // Confirmed Date (столбец 7)
        String confirmedDateStr = getCellValueAsString(row.getCell(7));
        if (confirmedDateStr != null && !confirmedDateStr.trim().isEmpty()) {
            try {
                order.setConfirmedDate(LocalDate.parse(confirmedDateStr.trim(), dateFormatter));
            } catch (Exception e) {
                System.err.println("Error parsing confirmed date: " + confirmedDateStr);
            }
        }

        // Prio. (столбец 8)
        order.setPriority(getCellValueAsString(row.getCell(8)));

        // Responsible (столбец 9)
        order.setResponsible(getCellValueAsString(row.getCell(9)));

        // Created By (столбец 10)
        order.setCreatedBy(getCellValueAsString(row.getCell(10)));

        return order;
    }

    private String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return null;
        }

        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getLocalDateTimeCellValue().toLocalDate().format(dateFormatter);
                } else {
                    double num = cell.getNumericCellValue();
                    if (num == Math.floor(num)) {
                        return String.valueOf((int) num);
                    } else {
                        return String.valueOf(num);
                    }
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                try {
                    return cell.getStringCellValue();
                } catch (Exception e) {
                    return String.valueOf(cell.getNumericCellValue());
                }
            case BLANK:
                return "";
            default:
                return "";
        }
    }
}
