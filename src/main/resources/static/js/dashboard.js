window.onload = function () {
    const userId = localStorage.getItem('userId');
    const username = localStorage.getItem('username');

    // 如果用户未登录，跳转到登录页面
    if (!userId) {
        window.location.href = "login.html";
        return;
    }

    // 显示当前登录用户名
    document.getElementById('welcomeMessage').innerText = `Hi, ${username}！`;

    fetchBills(userId);  // 获取并显示当前登录用户的账单
};

// 获取用户的账单
function fetchBills(userId, searchQuery = '') {
    let url = `http://localhost:8080/api/bills/user/${userId}`;

    // 如果有查询条件，附加到URL
    if (searchQuery) {
        url += `?name=${searchQuery}`;
    }

    fetch(url)  // 获取当前用户的账单
        .then(response => response.json())
        .then(data => {
            const billTable = document.getElementById("billTable").getElementsByTagName('tbody')[0];
            billTable.innerHTML = "";  // 清空表格内容

            data.forEach(bill => {
                const row = billTable.insertRow();
                const formattedDate = formatDate(bill.consumeTime);

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
    fetchBills(userId, searchQuery);  // 带查询条件调用fetchBills
});

// 清除查询并恢复所有账单
document.getElementById("clearSearchButton").addEventListener("click", function () {
    // 清空搜索框
    document.getElementById("billSearch").value = "";

    const userId = localStorage.getItem('userId');
    fetchBills(userId);  // 恢复所有账单
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
                    fetchBills(localStorage.getItem('userId'));  // 重新加载账单
                } else {
                    alert("账单删除失败");
                }
            })
            .catch(error => console.error("Error deleting bill:", error));
    }
}

// 编辑账单
function editBill(billId) {
    window.location.href = `edit-bill.html?billId=${billId}`;
}

// 退出登录功能
document.getElementById("logoutButton").addEventListener("click", function () {
    localStorage.removeItem('userId');
    localStorage.removeItem('username');
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