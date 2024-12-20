package xyz.hw2.myledger.controller;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import xyz.hw2.myledger.model.Bill;
import xyz.hw2.myledger.service.BillService;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/bills")
public class BillController {

    @Autowired
    private BillService billService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Bill>> getBillsByUserId(
            @PathVariable Long userId,
            @RequestParam(required = false) String name) {

        List<Bill> bills;
        if (name != null && !name.isEmpty()) {
            // 根据账单名称查询
            bills = billService.getBillsByUserIdAndName(userId, name);
        } else {
            // 查询所有账单
            bills = billService.getBillsByUserId(userId);
        }

        return new ResponseEntity<>(bills, HttpStatus.OK);
    }

    // 创建账单
    @PostMapping("/create")
    public ResponseEntity<Bill> createBill(@RequestBody Bill bill) {
        try {
            // 只传递用户 ID，后台根据 ID 查找用户
            Bill newBill = billService.createBill(bill.getUser().getId(), bill.getName(),
                    bill.getConsumeTime().toString(), bill.getAmount());
            return new ResponseEntity<>(newBill, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    // 删除账单
    @DeleteMapping("/delete/{billId}")
    public ResponseEntity<Void> deleteBill(@PathVariable Long billId) {
        billService.deleteBill(billId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // 根据账单 ID 查询账单
    @GetMapping("/{billId}")
    public ResponseEntity<Bill> getBillById(@PathVariable Long billId) {
        Optional<Bill> bill = billService.getBillById(billId);
        return bill.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // 更新账单
    @PutMapping("/update/{billId}")
    public ResponseEntity<Bill> updateBill(@PathVariable Long billId, @RequestBody Bill bill) {
        try {
            Bill updatedBill = billService.updateBill(billId, bill.getName(), bill.getConsumeTime().toString(), bill.getAmount());
            return new ResponseEntity<>(updatedBill, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/export/{userId}")
    public ResponseEntity<byte[]> exportBillsToExcel(@PathVariable Long userId,
                                                     @RequestParam(required = false) String name) throws IOException {
        List<Bill> bills;
        if (name != null && !name.isEmpty()) {
            // 如果提供了查询条件，按照账单名称查询
            bills = billService.getBillsByUserIdAndName(userId, name);
        } else {
            // 否则，导出所有账单
            bills = billService.getBillsByUserId(userId);
        }

        // 创建 Excel 工作簿
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Bills");

        // 创建表头
        Row headerRow = sheet.createRow(0);
        String[] columns = {"账单名称", "消费时间", "金额"};
        for (int i = 0; i < columns.length; i++) {
            headerRow.createCell(i).setCellValue(columns[i]);
        }

        // 填充数据
        int rowNum = 1;
        for (Bill bill : bills) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(bill.getName());
            row.createCell(1).setCellValue(bill.getConsumeTime().toString());  // 格式化时间
            row.createCell(2).setCellValue(bill.getAmount());
        }

        // 将工作簿内容写入 ByteArrayOutputStream
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        workbook.write(byteArrayOutputStream);
        workbook.close();

        // 设置 HTTP 响应头，以便浏览器下载 Excel 文件
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=bills.xlsx");
        headers.add("Content-Type", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

        // 返回 Excel 文件数据
        return ResponseEntity.ok()
                .headers(headers)
                .body(byteArrayOutputStream.toByteArray());
    }
}