function convert() {
    let formError = document.querySelector("#danger-info");

    if (!formError.classList.contains("d-none")) {
        formError.classList.add("d-none");
    }

    $.ajax({
        type: "POST",
        url: "/user",
        data: $("#form").serialize(),
        beforeSend: () => {
            $("#convert-to").val("Конвертируем ...");
        }
    }).done((data) => {
        updateHistory();
        $("#convert-to").val(data);
    }).fail((data) => {
        formError.classList.remove("d-none");
        formError.innerHTML = data.responseText;
    });
}

function updateHistory() {
    $.ajax({
        type: "POST",
        url: "/user/history",
    }).done((data) => {
        updateTable(data);
    });
}

function updateTable(data) {
    let el = $("#history-convert").children("tbody");
    let tableEl = [];

    if (data.length !== 0) {
        data.forEach(function (history) {
            tableEl.push($('<tr><td>'+history.fromCurrency+'</td><td>'+history.toCurrency+'</td><td>'+history.fromAmount+'</td><td>'+history.totalAmount+'</td><td>'+history.date+'</td></tr>'));
        })
    }

    el.html(tableEl);
}