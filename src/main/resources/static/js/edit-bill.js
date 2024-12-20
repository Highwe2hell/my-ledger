const urlParams = new URLSearchParams(window.location.search);
const billId = urlParams.get('billId');

// 获取账单信息
fetch(`http://localhost:8080/api/bills/${billId}`)
    .then(response => response.json())
    .then(bill => {
        document.getElementById('name').value = bill.name;
        document.getElementById('consume_time').value = bill.consumeTime.substring(0, 16); // 只保留到分钟
        document.getElementById('amount').value = bill.amount;
    })
    .catch(error => console.error("Error fetching bill:", error));

// 更新账单
document.getElementById('updateBillButton').addEventListener('click', function () {
    const name = document.getElementById('name').value;
    const consumeTime = document.getElementById('consume_time').value;
    const amount = document.getElementById('amount').value;

    const updatedBill = {
        name: name,
        consumeTime: consumeTime,
        amount: amount
    };

    fetch(`http://localhost:8080/api/bills/update/${billId}`, {
        method: "PUT",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(updatedBill)
    })
        .then(response => {
            if (response.ok) {
                alert("账单更新成功");
                window.location.href = "dashboard.html";  // 跳转到账单页面
            } else {
                alert("账单更新失败");
            }
        })
        .catch(error => console.error("Error updating bill:", error));
});