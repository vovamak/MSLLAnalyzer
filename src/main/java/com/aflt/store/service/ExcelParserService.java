package com.aflt.store.service;

import com.aflt.store.entity.StockAFLT;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class ExcelParserService {

    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    public List<StockAFLT> parseExcelFile(MultipartFile file) throws Exception {
        List<StockAFLT> stocks = new ArrayList<>();

        try (InputStream inputStream = file.getInputStream()) {
            Workbook workbook = WorkbookFactory.create(inputStream);
            Sheet sheet = workbook.getSheetAt(0); // Берем первый лист

            // Пропускаем заголовок (первую строку)
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) {
                    continue;
                }

                StockAFLT stock = parseRowToStock(row);

                // Проверяем обязательные поля перед добавлением
                if (stock.getPartNumber() != null && !stock.getPartNumber().trim().isEmpty() &&
                        stock.getQuantity() != null) {
                    stocks.add(stock);
                }
            }

            workbook.close();
        }

        return stocks;
    }

    private StockAFLT parseRowToStock(Row row) {
        StockAFLT stock = new StockAFLT();

        // PARTNO (столбец 0)
        Cell partNoCell = row.getCell(0);
        if (partNoCell != null) {
            stock.setPartNumber(getCellValueAsString(partNoCell));
        }

        // EXPIRE_DATE (столбец 1) - может быть пустым
        Cell expireDateCell = row.getCell(1);
        if (expireDateCell != null) {
            String dateStr = getCellValueAsString(expireDateCell);
            if (dateStr != null && !dateStr.trim().isEmpty()) {
                try {
                    // Преобразуем строку в LocalDate
                    LocalDate expireDate = LocalDate.parse(dateStr.trim(), dateFormatter);
                    stock.setExpireDate(expireDate);
                } catch (Exception e) {
                    System.err.println("Error parsing date: " + dateStr);
                    stock.setExpireDate(null);
                }
            }
        }

        // QTY (столбец 2)
        Cell qtyCell = row.getCell(2);
        if (qtyCell != null) {
            try {
                stock.setQuantity(getCellValueAsDouble(qtyCell));
            } catch (Exception e) {
                // Если не число, пытаемся преобразовать строку
                String qtyStr = getCellValueAsString(qtyCell);
                if (qtyStr != null && !qtyStr.trim().isEmpty()) {
                    try {
                        stock.setQuantity(Double.parseDouble(qtyStr.trim()));
                    } catch (NumberFormatException ex) {
                        stock.setQuantity(0.0);
                    }
                } else {
                    stock.setQuantity(0.0);
                }
            }
        }

        // MEASURE_UNIT (столбец 3)
        Cell measureUnitCell = row.getCell(3);
        if (measureUnitCell != null) {
            stock.setMeasureUnit(getCellValueAsString(measureUnitCell));
        }

        // STATION (столбец 4)
        Cell stationCell = row.getCell(4);
        if (stationCell != null) {
            stock.setStation(getCellValueAsString(stationCell));
        }

        // STORE (столбец 5)
        Cell storeCell = row.getCell(5);
        if (storeCell != null) {
            stock.setStore(getCellValueAsString(storeCell));
        }

        // OWNER (столбец 6)
        Cell ownerCell = row.getCell(6);
        if (ownerCell != null) {
            stock.setOwner(getCellValueAsString(ownerCell));
        }

        return stock;
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
                    // Если это дата, форматируем как строку
                    return cell.getLocalDateTimeCellValue().toLocalDate().format(dateFormatter);
                } else {
                    // Если это число, преобразуем в строку без десятичных, если это целое
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
                return cell.getCellFormula();
            case BLANK:
                return "";
            default:
                return "";
        }
    }

    private Double getCellValueAsDouble(Cell cell) {
        if (cell == null) {
            return 0.0;
        }

        switch (cell.getCellType()) {
            case NUMERIC:
                return cell.getNumericCellValue();
            case STRING:
                try {
                    return Double.parseDouble(cell.getStringCellValue().trim());
                } catch (NumberFormatException e) {
                    return 0.0;
                }
            default:
                return 0.0;
        }
    }
}
