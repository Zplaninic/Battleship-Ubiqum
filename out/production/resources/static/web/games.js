$(function() {

    // display text in the output area
    function showOutput(text) {
        $("#output").text(text);
    }

    // load and display JSON sent by server for /players

    function loadData() {
        $.get("/api/games")
            .done(function (data) {
                retu
                // showOutput(JSON.stringify(data, null, 2));
            })
            .fail(function (jqXHR, textStatus) {
                showOutput("Failed: " + textStatus);
            });
    }

    loadData();
});