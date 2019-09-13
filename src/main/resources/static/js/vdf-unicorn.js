$(document).ready(function() {
    getCurrent();
    // setInterval(function(){ getCurrent(); }, 10000);
});

function getCurrent() {
    $.ajax({
        url: "/beacon/2.0/vdf/unicorn/current/",
        method: 'GET',
        dataType: 'json',
        contentType: 'application/json',
        success: function (data) {
            updateFields(data);
        },
        error: function (xhr, status) {
            alert("Ocorreu um erro.  Por favor, tente mais tarde")
        }
    });
}

function updateFields(data) {
    // var vdf = data.vdf;

    document.getElementById("spanStatus").textContent = data.status;
    document.getElementById("spanNextRunInMinutes").textContent = data.nextRunInMinutes;
    document.getElementById("spanStart").textContent = data.start;
    document.getElementById("spanEnd").textContent = data.end;
    document.getElementById("spanCurrentHash").textContent = data.currentHash;

    var x = document.getElementById("pNextSubmission");
    if (data.status === "Running"){
        x.style.display = "none";
    } else {
        x.style.display = "block";
    }

    var seedList = data.seedList;
    updateSeedTable(seedList)

}

function updateSeedTable(seedList) {
    var lista = '';
    seedList.forEach(function (seed) {
        // lista += '<tr><td style="word-break: break-word">' + seed.timestamp + '</td>';
        lista += '<tr>';
        lista += '<td style="word-break: break-word">' + seed.timeStamp + '</td>';
        lista += '<td style="word-break: break-word">' + seed.seed + '</td>';
        lista += '<td style="word-break: break-word">' + seed.description + '</td>';
        lista += '<td style="word-break: break-word">' + seed.uri + '</td>';
        lista += '<td style="word-break: break-word">' + seed.cumulativeHash + '</td>';
        lista += '</tr>';

    });

    $('#table_seed').html(lista);
}
