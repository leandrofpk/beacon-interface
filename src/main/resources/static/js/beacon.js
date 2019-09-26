$(document).ready(function() {
    getLastPulseReloadPage();
});

function getLastPulseReloadPage() {
    $.ajax({
        url: "/beacon/2.0/pulse/last",
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
        url: "/beacon/2.0/pulse/last",
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
        url: "/beacon/2.0/chain/1/pulse/first",
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
        url: "/beacon/2.0/pulse/time/previous/" + new Date(document.getElementById("input-datetime").value).getTime() ,
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
        url: "/beacon/2.0/pulse/time/next/" + new Date(document.getElementById("input-datetime").value).getTime(),
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

    updateHidden(pulse);

    var elementById = document.getElementById("input-datetime");
    elementById.value = pulse.timeStamp;

    var lista = '';

    lista += '<tr><td>URI:</td>';
    lista += '<td style="word-break: break-word">' + pulse.uri + '</td></tr>';

    lista += '<tr><td>Version:</td>';
    lista += '<td>' + pulse.version + '</td></tr>';

    lista += '<tr><td>Cipher Suite:</td>';
    lista += '<td>' + pulse.cipherSuite + '</td></tr>';

    lista += '<tr><td>Period:</td>';
    lista += '<td>' + pulse.period + '</td></tr>';

    lista += '<tr><td>Certified Hash:</td>';
    lista += '<td style="word-break: break-word">' + pulse.certificateId + '</td></tr>';

    lista += '<tr><td>Chain Index:</td>';
    lista += '<td>' + pulse.chainIndex + '</td></tr>';

    lista += '<tr><td>Pulse Index:</td>';
    lista += '<td>' + pulse.pulseIndex + '</td></tr>';

    lista += '<tr><td>Time:</td>';
    lista += '<td>' + pulse.timeStamp + '</td></tr>';

    lista += '<tr><td>Local Random Value:</td>';
    lista += '<td style="word-break: break-word">' + pulse.localRandomValue + '</td></tr>';

    lista += '<tr><td>External Source Id:</td>';
    lista += '<td style="word-break: break-word">' + pulse.external.sourceId + '</td></tr>';

    lista += '<tr><td>External Status Code:</td>';
    lista += '<td>' + pulse.external.statusCode + '</td></tr>';

    lista += '<tr><td>External Value:</td>';
    lista += '<td style="word-break: break-word">' + pulse.external.value + '</td></tr>';

    lista += '<tr><td>Previous Output:</td>';
    lista += '<td style="word-break: break-word">' + pulse.listValues[0].value + '</td></tr>';

    lista += '<tr><td>Hour:</td>';
    lista += '<td style="word-break: break-word">' + pulse.listValues[1].value + '</td></tr>';

    lista += '<tr><td>Day:</td>';
    lista += '<td style="word-break: break-word">' + pulse.listValues[2].value + '</td></tr>';

    lista += '<tr><td>Month:</td>';
    lista += '<td style="word-break: break-word">' + pulse.listValues[3].value + '</td></tr>';

    lista += '<tr><td>Year:</td>';
    lista += '<td style="word-break: break-word">' + pulse.listValues[4].value + '</td></tr>';

    lista += '<tr><td>Precommitment Value:</td>';
    lista += '<td style="word-break: break-word">' + pulse.precommitmentValue + '</td></tr>';

    lista += '<tr><td>Signature:</td>';
    lista += '<td style="word-break: break-word">' + pulse.signatureValue + '</td></tr>';

    lista += '<tr><td>Output Value:</td>';
    lista += '<td style="word-break: break-word">' + pulse.outputValue + '</td></tr>';

    lista += '<tr><td>Status:</td>';
    lista += '<td>' + pulse.statusCode + '</td></tr>';

    $('#table_pulse').html(lista);
}

function updateHidden(pulse) {
    document.getElementById("hour").dataset.uri = pulse.listValues[1].uri;
    document.getElementById("day").dataset.uri = pulse.listValues[2].uri;
    document.getElementById("month").dataset.uri = pulse.listValues[3].uri;
    document.getElementById("year").dataset.uri = pulse.listValues[4].uri;
}