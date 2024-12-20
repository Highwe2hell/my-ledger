document.getElementById("addBillButton").addEventListener("click", function () {
    const name = document.getElementById("name").value;
    const consumeTime = document.getElementById("consume_time").value;
    const amount = document.getElementById("amount").value;

    const userId = localStorage.getItem('userId');  // 从localStorage获取当前登录用户的ID

    const newBill = {
        name: name,
        consumeTime: consumeTime, // 这里传递的是字符串格式的日期时间
        amount: amount,
        user: {id: userId}  // 使用当前用户的ID
    };

    fetch("http://localhost:8080/api/bills/create", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(newBill)
    })
        .then(response => {
            if (response.ok) {
                alert("账单添加成功");
                window.location.href = "dashboard.html";  // 跳转到账单列表页面
            } else {
                alert("账单添加失败");
            }
        })
        .catch(error => console.error("Error adding bill:", error));
});
