// 登录功能实现
document.getElementById("loginForm").addEventListener("submit", function (event) {
    // 阻止表单默认提交行为
    event.preventDefault();

    // 获取用户名和密码输入值
    const username = document.getElementById("username").value;
    const password = document.getElementById("password").value;

    // 使用fetch进行POST请求以验证用户登录信息
    fetch("http://localhost:8080/api/users/login", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({username, password})
    })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                // 存储用户信息到 localStorage
                localStorage.setItem('userId', data.user.id);
                localStorage.setItem('username', data.user.username);

                // 登录成功后跳转到主界面
                window.location.href = "dashboard.html";
            } else {
                // 显示错误信息
                document.getElementById("errorMessage").style.display = "block";
            }
        })
        .catch(error => console.error("Error:", error));
});
