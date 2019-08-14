$(document).ready(function() {
    // $(".form_datetime").datetimepicker({format: "yyyy-MM-dd'T'HH:mm:ss.SSSz"});
    getLastPulse();
});

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
            alert("Ocorreu um erro.  Por favor, tente mais tarde")
        }
    });
}

function getFirstPulse() {
    $.ajax({
    // /beacon/2.0/chain/{chainIndex}/pulse/first
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
        url: "/beacon/2.0/pulse/time/previous/" + document.getElementById("input-datetime").value ,
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
        url: "/beacon/2.0/pulse/time/next/" + document.getElementById("input-datetime").value,
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

function atualizarRecord(data) {
    var pulse = data.pulse;

    var elementById = document.getElementById("input-datetime");
    elementById.value = pulse.timeStamp;

    var lista = '';

    lista += '<tr><td>URI:</td>';
    lista += '<td>' + pulse.uri + '</td></tr>';

    lista += '<tr><td>Version:</td>';
    lista += '<td>' + pulse.version + '</td></tr>';

    lista += '<tr><td>Cipher Suite:</td>';
    lista += '<td>' + pulse.cipherSuite + '</td></tr>';

    lista += '<tr><td>Period:</td>';
    lista += '<td>' + pulse.period + '</td></tr>';

    lista += '<tr><td>Certified Hash:</td>';
    lista += '<td>' + pulse.certificateId + '</td></tr>';

    lista += '<tr><td>Chain Index:</td>';
    lista += '<td>' + pulse.chainIndex + '</td></tr>';

    lista += '<tr><td>Pulse Index:</td>';
    lista += '<td>' + pulse.pulseIndex + '</td></tr>';

    lista += '<tr><td>Time:</td>';
    lista += '<td>' + pulse.timeStamp + '</td></tr>';

    lista += '<tr><td>Local Random Value:</td>';
    lista += '<td>' + pulse.localRandomValue + '</td></tr>';

    lista += '<tr><td>External Source Id:</td>';
    lista += '<td>' + pulse.external.sourceId + '</td></tr>';

    lista += '<tr><td>External Status Code:</td>';
    lista += '<td>' + pulse.external.statusCode + '</td></tr>';

    lista += '<tr><td>External Value:</td>';
    lista += '<td>' + pulse.external.value + '</td></tr>';

    lista += '<tr><td>Previous Output:</td>';
    lista += '<td>' + pulse.listValues[0].value + '</td></tr>';

    lista += '<tr><td>Hour:</td>';
    lista += '<td>' + pulse.listValues[1].value + '</td></tr>';

    lista += '<tr><td>Day:</td>';
    lista += '<td>' + pulse.listValues[2].value + '</td></tr>';

    lista += '<tr><td>Month:</td>';
    lista += '<td>' + pulse.listValues[3].value + '</td></tr>';

    lista += '<tr><td>Year:</td>';
    lista += '<td>' + pulse.listValues[4].value + '</td></tr>';

    lista += '<tr><td>Precommitment Value:</td>';
    lista += '<td>' + pulse.precommitmentValue + '</td></tr>';

    lista += '<tr><td>Signature:</td>';
    lista += '<td>' + pulse.signature + '</td></tr>';

    lista += '<tr><td>Output Value:</td>';
    lista += '<td>' + pulse.outputValue + '</td></tr>';

    lista += '<tr><td>Status:</td>';
    lista += '<td>' + pulse.statusCode + '</td></tr>';

    $('#table_pulse').html(lista);
}