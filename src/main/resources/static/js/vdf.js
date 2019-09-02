$(document).ready(function() {
    getCurrent();
    setInterval(function(){ getCurrent(); }, 5000);
});

function getCurrent() {
    $.ajax({
        url: "/vdf/current",
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
    var vdf = data.vdf;

    document.getElementById("spanStatus").textContent = vdf.status;
    document.getElementById("spanNextRunInMinutes").textContent = vdf.nextRunInMinutes;
    document.getElementById("spanStart").textContent = vdf.start;
    document.getElementById("spanEnd").textContent = vdf.end;
    document.getElementById("spanCurrentHash").textContent = vdf.currentHash;
    document.getElementById("spanOutput").textContent = vdf.output;

    var x = document.getElementById("pNextSubmission");
    if (vdf.status === "Closed"){
        x.style.display = "block";
    } else {
        x.style.display = "none";
    }

    var seedList = vdf.seedList;
    updateSeedTable(seedList)

}

function updateSeedTable(seedList) {
    var lista = '';
    seedList.forEach(function (seed) {
        // lista += '<tr><td style="word-break: break-word">' + seed.timestamp + '</td>';
        lista += '<tr>';
        lista += '<td style="word-break: break-word">' + seed.seed + '</td>';
        lista += '<td style="word-break: break-word">' + seed.uri + '</td></tr>';
    });

    $('#table_seed').html(lista);
}