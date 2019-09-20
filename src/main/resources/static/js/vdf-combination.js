$(document).ready(function() {
    getLastPulseReloadPage();
});

function getLastPulseReloadPage() {
    $.ajax({
        url: "/beacon/2.0/combination/last",
        method: 'GET',
        dataType: 'json',
        contentType: 'application/json',
        success: function (data) {
            atualizarRecord(data);
        }
    });
}

function getLastPulse() {
    $.ajax({
        url: "/beacon/2.0/combination/last",
        method: 'GET',
        dataType: 'json',
        contentType: 'application/json',
        success: function (data) {
            atualizarRecord(data);
        },
        error: function (xhr, status) {
            alert(xhr.status + '-' +xhr.responseText);
        }
    });
}

function getFirstPulse() {
    $.ajax({
        url: "/beacon/2.0/combination/first",
        method: 'GET',
        dataType: 'json',
        contentType: 'application/json',
        success: function (data) {
            atualizarRecord(data);
        },
        error: function (xhr) {
            alert(xhr.status + '-' +xhr.responseText);
        }
    });
}

function getPreviousPulse() {
    $.ajax({
        url: "/beacon/2.0/combination/previous/" + document.getElementById("input-datetime").value ,
        method: 'GET',
        dataType: 'json',
        contentType: 'application/json',
        success: function (data) {
            atualizarRecord(data);
        },
        error: function (xhr) {
            alert(xhr.status + '-' +xhr.responseText);
        }
    });
}

function getNextPulse() {
    $.ajax({
        url: "/beacon/2.0/combination/next/" + document.getElementById("input-datetime").value,
        method: 'GET',
        dataType: 'json',
        contentType: 'application/json',
        success: function (data) {
            atualizarRecord(data);
        },
        error: function (xhr) {
            alert(xhr.status + '-' +xhr.responseText);
        }
    });
}

function getByUri(button) {
   var uri = button.dataset.uri;

    if (uri!="null"){
        $.ajax({
            url: uri,
            method: 'GET',
            dataType: 'json',
            contentType: 'application/json',
            success: function (data) {
                atualizarRecord(data);
            },
            error: function (xhr) {
                alert(xhr.status + '-' +xhr.responseText);
            }
        });
    }
}

function atualizarRecord(data) {
    var pulse = data.pulse;

    var elementById = document.getElementById("input-datetime");
    elementById.value = pulse.timeStamp;

    var lista = '';

    lista += '<tr><td>Uri:</td>';
    lista += '<td>' + pulse.uri + '</td></tr>';

    lista += '<tr><td>Version:</td>';
    lista += '<td>' + pulse.version + '</td></tr>';

    lista += '<tr><td>Period:</td>';
    lista += '<td>' + pulse.period + '</td></tr>';

    lista += '<tr><td>Certified Hash:</td>';
    lista += '<td style="word-break: break-word">' + pulse.certificateId + '</td></tr>';

    lista += '<tr><td>Pulse Index:</td>';
    lista += '<td>' + pulse.pulseIndex + '</td></tr>';

    lista += '<tr><td>Time:</td>';
    lista += '<td>' + pulse.timeStamp + '</td></tr>';

    lista += '<tr><td>Combination:</td>';
    lista += '<td>' + pulse.combination + '</td></tr>';

    lista += '<tr><td>Vdf p:</td>';
    lista += '<td style="word-break: break-word">' + pulse.sloth.p + '</td></tr>';

    lista += '<tr><td>Vdf x:</td>';
    lista += '<td style="word-break: break-word">' + pulse.sloth.x + '</td></tr>';

    lista += '<tr><td>Vdf y:</td>';
    lista += '<td style="word-break: break-word">' + pulse.sloth.y + '</td></tr>';

    lista += '<tr><td>Vdf iterations:</td>';
    lista += '<td>' + pulse.sloth.iterations + '</td></tr>';

    lista += '<tr><td>Signature:</td>';
    lista += '<td style="word-break: break-word">' + pulse.signatureValue + '</td></tr>';

    lista += '<tr><td>Output Value :</td>';
    lista += '<td style="word-break: break-word">' + pulse.outputValue + '</td></tr>';

    $('#table_pulse').html(lista);

    updateSeedTable(pulse.seedList);
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