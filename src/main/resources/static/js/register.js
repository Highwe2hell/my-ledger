// 监听注册表单的提交事件
document.getElementById("registerForm").addEventListener("submit", function (event) {
    // 阻止默认的表单提交行为，以进行自定义处理
    event.preventDefault();
    // 获取用户名和密码输入框的值
    const username = document.getElementById("username").value;
    const password = document.getElementById("password").value;

    // 先隐藏错误消息
    document.getElementById("errorMessage").style.display = "none";

    // 使用fetch API向后端发送注册请求
    fetch("http://localhost:8080/api/users/register", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({username, password})
    })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                // 注册成功时的处理逻辑
                alert("注册成功！");
                // 重定向到登录页面
                window.location.href = "login.html";
            } else {
                // 显示错误消息
                document.getElementById("errorMessage").style.display = "block";
            }
        })
        .catch(error => {
            // 处理网络请求错误
            console.error("Error:", error);
            alert("注册请求失败，请稍后再试");
        });
});

// 监听登录按钮的点击事件，以实现页面跳转
document.getElementById("loginBtn").addEventListener("click", function () {
    // 重定向到登录页面
    window.location.href = "login.html";
});
