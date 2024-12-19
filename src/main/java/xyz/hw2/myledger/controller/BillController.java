package xyz.hw2.myledger.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import xyz.hw2.myledger.model.Bill;
import xyz.hw2.myledger.service.BillService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/bills")
public class BillController {

    @Autowired
    private BillService billService;

    // 获取用户的所有账单
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Bill>> getBillsByUserId(@PathVariable Long userId) {
        List<Bill> bills = billService.getBillsByUserId(userId);
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
}