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

/**
 * 账单控制器类，处理与账单相关的HTTP请求
 */
@RestController
@RequestMapping("/api/bills")
public class BillController {

    @Autowired
    private BillService billService;

    /**
     * 根据用户ID查询账单
     * 如果提供了账单名称，则按照名称过滤
     *
     * @param userId 用户ID
     * @param name   账单名称，可选
     * @return 包含账单列表的ResponseEntity
     */
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

    /**
     * 创建新账单
     * 只传递用户ID，后台根据ID查找用户
     *
     * @param bill 包含账单信息的Bill对象
     * @return 包含新账单的 ResponseEntity
     */
    @PostMapping("/create")
    public ResponseEntity<Bill> createBill(@RequestBody Bill bill) {
        try {
            // 只传递用户ID，后台根据ID查找用户
            Bill newBill = billService.createBill(bill.getUser().getId(), bill.getName(),
                    bill.getConsumeTime().toString(), bill.getAmount());
            return new ResponseEntity<>(newBill, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * 删除指定账单
     *
     * @param billId 账单ID
     * @return 表示删除成功的ResponseEntity
     */
    @DeleteMapping("/delete/{billId}")
    public ResponseEntity<Void> deleteBill(@PathVariable Long billId) {
        billService.deleteBill(billId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * 根据账单ID查询账单
     *
     * @param billId 账单ID
     * @return 包含账单的ResponseEntity
     */
    @GetMapping("/{billId}")
    public ResponseEntity<Bill> getBillById(@PathVariable Long billId) {
        Optional<Bill> bill = billService.getBillById(billId);
        return bill.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * 更新账单信息
     *
     * @param billId 账单ID
     * @param bill   包含更新信息的Bill对象
     * @return 包含更新后账单的ResponseEntity
     */
    @PutMapping("/update/{billId}")
    public ResponseEntity<Bill> updateBill(@PathVariable Long billId, @RequestBody Bill bill) {
        try {
            Bill updatedBill = billService.updateBill(billId, bill.getName(), bill.getConsumeTime().toString(), bill.getAmount());
            return new ResponseEntity<>(updatedBill, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * 导出用户账单到Excel文件
     * 如果提供了账单名称，则只导出匹配的账单，否则导出所有账单
     *
     * @param userId 用户ID
     * @param name   账单名称，可选
     * @return 包含Excel文件数据的ResponseEntity
     */
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

        // 创建Excel工作簿
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

        // 将工作簿内容写入ByteArrayOutputStream
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        workbook.write(byteArrayOutputStream);
        workbook.close();

        // 设置HTTP响应头，以便浏览器下载Excel文件
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=bills.xlsx");
        headers.add("Content-Type", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

        // 返回Excel文件数据
        return ResponseEntity.ok()
                .headers(headers)
                .body(byteArrayOutputStream.toByteArray());
    }
}
