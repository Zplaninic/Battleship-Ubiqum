$(function() {

    function getColumnsHtml(row) {
        return row.gameplayers.map(function(gameplayer) {
            return gameplayer.player.playerName;
        }).join(" ");
    }
    
    function getRowsHTMl(data) {
        return data.map(function(row, i) {
           return "<li>" + data[i].id + " " + data[i].created + " " + getColumnsHtml(row) + "</li>"
        }).join(" ");
    }

    function renderList(data) {
        var html = getRowsHTMl(data);
        document.getElementById("output").innerHTML = html;
    }


    function loadData() {
        $.get("/api/games")
            .done(function (data) {
                renderList(data);
            })
            .fail(function (jqXHR, textStatus) {
                showOutput("Failed: " + textStatus);
            });
    }

    loadData();
});