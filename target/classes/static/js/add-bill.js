// 获取添加账单按钮元素，并为其添加点击事件监听器
document.getElementById("addBillButton").addEventListener("click", function () {
    // 获取表单中账单名称输入框的值
    const name = document.getElementById("name").value;
    // 获取表单中消费时间输入框的值
    const consumeTime = document.getElementById("consume_time").value;
    // 获取表单中金额输入框的值
    const amount = document.getElementById("amount").value;

    // 从localStorage中获取当前登录用户的ID
    const userId = localStorage.getItem('userId');

    // 构建新的账单对象
    const newBill = {
        name: name, // 账单名称
        consumeTime: consumeTime, // 消费时间，这里传递的是字符串格式的日期时间
        amount: amount, // 金额
        user: { id: userId } // 使用当前用户的ID
    };

    // 发送POST请求到后端API创建新账单
    fetch("http://localhost:8080/api/bills/create", {
        method: "POST", // 请求方法为POST
        headers: {
            "Content-Type": "application/json" // 设置请求头，指定发送的数据格式为JSON
        },
        body: JSON.stringify(newBill) // 将账单对象转换为JSON字符串作为请求体
    })
        .then(response => {
            // 处理响应
            if (response.ok) {
                // 如果请求成功
                alert("账单添加成功"); // 弹出提示框显示成功信息
                window.location.href = "dashboard.html"; // 跳转到账单列表页面
            } else {
                // 如果请求失败
                alert("账单添加失败"); // 弹出提示框显示失败信息
            }
        })
        .catch(error => {
            // 处理请求过程中发生的错误
            console.error("Error adding bill:", error); // 在控制台输出错误信息
        });
});
