// 页面加载时执行的函数
window.onload = function () {
    // 从本地存储获取用户ID和用户名
    const userId = localStorage.getItem('userId');
    const username = localStorage.getItem('username');

    // 如果用户未登录，跳转到登录页面
    if (!userId) {
        window.location.href = "login.html";
        return;
    }

    // 显示欢迎信息和当前登录用户名
    document.getElementById('welcomeMessage').innerText = `欢迎使用 My Ledger, ${username}！`;

    // 获取并显示当前登录用户的账单
    fetchBills(userId);
};

// 获取用户的账单
function fetchBills(userId, searchQuery = '') {
    let url = `http://localhost:8080/api/bills/user/${userId}`;

    // 如果有查询条件，附加到URL
    if (searchQuery) {
        url += `?name=${searchQuery}`;
    }

    // 获取当前用户的账单
    fetch(url)
        .then(response => response.json())
        .then(data => {
            // 获取账单表格的tbody部分
            const billTable = document.getElementById("billTable").getElementsByTagName('tbody')[0];
            // 清空表格内容
            billTable.innerHTML = "";

            // 遍历每个账单并添加到表格中
            data.forEach(bill => {
                const row = billTable.insertRow();
                const formattedDate = formatDate(bill.consumeTime);

                // 填充账单行数据
                row.innerHTML = `
                            <td>${bill.name}</td>
                            <td>${formattedDate}</td>
                            <td>${bill.amount}</td>
                            <td>
                                <button class="action-btn" onclick="editBill(${bill.id})">编辑</button>
                                <button class="action-btn" onclick="deleteBill(${bill.id})">删除</button>
                            </td>
                        `;
            });
        })
        .catch(error => console.error("Error fetching bills:", error));
}

// 格式化消费时间为 'yyyy年mm月dd日 hh:mm' 格式
function formatDate(consumeTime) {
    const date = new Date(consumeTime);
    const year = date.getFullYear();
    const month = (date.getMonth() + 1).toString().padStart(2, '0');
    const day = date.getDate().toString().padStart(2, '0');
    const hours = date.getHours().toString().padStart(2, '0');
    const minutes = date.getMinutes().toString().padStart(2, '0');

    return `${year}年${month}月${day}日 ${hours}:${minutes}`;
}

// 点击添加账单跳转到添加页面
document.getElementById("addBillButton").addEventListener("click", function () {
    window.location.href = "add-bill.html";
});

// 查询账单名称
document.getElementById("searchButton").addEventListener("click", function () {
    const searchQuery = document.getElementById("billSearch").value.trim();
    const userId = localStorage.getItem('userId');
    // 带查询条件调用fetchBills
    fetchBills(userId, searchQuery);
});

// 清除查询并恢复所有账单
document.getElementById("clearSearchButton").addEventListener("click", function () {
    // 清空搜索框
    document.getElementById("billSearch").value = "";

    const userId = localStorage.getItem('userId');
    // 恢复所有账单
    fetchBills(userId);
});

// 删除账单
function deleteBill(billId) {
    if (confirm("确定要删除这个账单吗？")) {
        fetch(`http://localhost:8080/api/bills/delete/${billId}`, {
            method: "DELETE"
        })
            .then(response => {
                if (response.ok) {
                    alert("账单删除成功");
                    // 重新加载账单
                    fetchBills(localStorage.getItem('userId'));
                } else {
                    alert("账单删除失败");
                }
            })
            .catch(error => console.error("Error deleting bill:", error));
    }
}

// 编辑账单
function editBill(billId) {
    // 跳转到编辑账单页面
    window.location.href = `edit-bill.html?billId=${billId}`;
}

// 退出登录功能
document.getElementById("logoutButton").addEventListener("click", function () {
    // 移除本地存储中的用户信息
    localStorage.removeItem('userId');
    localStorage.removeItem('username');
    // 跳转到登录页面
    window.location.href = "login.html";
});

// 导出账单为 Excel 按钮
document.getElementById("exportBillsButton").addEventListener("click", function () {
    const userId = localStorage.getItem('userId');
    const searchQuery = document.getElementById("billSearch").value.trim();

    fetch(`http://localhost:8080/api/bills/export/${userId}?name=${searchQuery}`, {
        method: "GET",
        headers: {
            "Content-Type": "application/json",
        }
    })
        .then(response => {
            if (response.ok) {
                return response.blob();
            } else {
                alert("导出失败");
            }
        })
        .then(blob => {
            const url = URL.createObjectURL(blob);
            const a = document.createElement("a");
            const currentDate = new Date().toISOString().split('T')[0];
            const username = localStorage.getItem('username');
            a.href = url;
            a.download = `${username}_bills_${currentDate}.xlsx`;
            document.body.appendChild(a);
            a.click();
            document.body.removeChild(a);
        })
        .catch(error => console.error("Error exporting bills:", error));
});
