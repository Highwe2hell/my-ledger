// 解析URL参数
const urlParams = new URLSearchParams(window.location.search);
// 获取名为'billId'的参数值
const billId = urlParams.get('billId');

// 获取账单信息
fetch(`http://localhost:8080/api/bills/${billId}`)
    .then(response => response.json())
    .then(bill => {
        // 将获取到的账单信息填充到HTML表单中
        document.getElementById('name').value = bill.name;
        // 只保留到分钟
        document.getElementById('consume_time').value = bill.consumeTime.substring(0, 16);
        document.getElementById('amount').value = bill.amount;
    })
    .catch(error => console.error("Error fetching bill:", error));

// 更新账单
document.getElementById('updateBillButton').addEventListener('click', function () {
    // 从表单中获取更新后的账单信息
    const name = document.getElementById('name').value;
    const consumeTime = document.getElementById('consume_time').value;
    const amount = document.getElementById('amount').value;

    // 创建包含更新信息的账单对象
    const updatedBill = {
        name: name,
        consumeTime: consumeTime,
        amount: amount
    };

    // 发送PUT请求更新账单
    fetch(`http://localhost:8080/api/bills/update/${billId}`, {
        method: "PUT",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(updatedBill)
    })
        .then(response => {
            // 根据响应结果给出反馈并跳转页面
            if (response.ok) {
                alert("账单更新成功");
                window.location.href = "dashboard.html";  // 跳转到账单页面
            } else {
                alert("账单更新失败");
            }
        })
        .catch(error => console.error("Error updating bill:", error));
});
