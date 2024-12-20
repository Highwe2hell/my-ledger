document.getElementById("registerForm").addEventListener("submit", function (event) {
    event.preventDefault();
    const username = document.getElementById("username").value;
    const password = document.getElementById("password").value;

    // 先隐藏错误消息
    document.getElementById("errorMessage").style.display = "none";

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
                alert("注册成功！");
                window.location.href = "login.html";
            } else {
                // 显示错误消息
                document.getElementById("errorMessage").style.display = "block";
            }
        })
        .catch(error => {
            console.error("Error:", error);
            alert("注册请求失败，请稍后再试");
        });
});

document.getElementById("loginBtn").addEventListener("click", function () {
    window.location.href = "login.html";
});