// 登录功能实现
document.getElementById("loginForm").addEventListener("submit", function (event) {
    event.preventDefault();
    const username = document.getElementById("username").value;
    const password = document.getElementById("password").value;

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
                window.location.href = "dashboard.html"; // 登录成功后跳转到主界面
            } else {
                document.getElementById("errorMessage").style.display = "block"; // 显示错误信息
            }
        })
        .catch(error => console.error("Error:", error));
});