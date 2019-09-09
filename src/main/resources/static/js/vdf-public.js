$(document).ready(function() {
    getCurrent();
    setInterval(function(){ getCurrent(); }, 10000);
});

function getCurrent() {
    $.ajax({
        url: "/beacon/2.0/pulse/vdf/public/current/",
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

    var x = document.getElementById("pNextSubmission");
    if (vdf.status === "Running"){
        x.style.display = "none";
    } else {
        x.style.display = "block";
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