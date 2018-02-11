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

function makeLeaderBoard() {
    var players = new Object();
    var competitor;


    $.getJSON("http://localhost:8080/api/games", function(data) {
        data.map(function(x) {
            x.gameplayers.map(function(y) {
                competitor = y.player.playerName;
                players[competitor] = [{scores: 0, win: 0, loss: 0, tie: 0}];
            });
        });
        data.map(function(x) {
            x.gameplayers.map(function(y) {
                competitor = y.player.playerName;
                if(((competitor in players) == true) && (y.score != "No score")) {
                    var score = Number(y.score);
                    players[competitor].map(function(j) {
                        j.scores += score
                    });
                }
            });
        });
        data.map(function(x) {
            x.gameplayers.map(function(y) {
                competitor = y.player.playerName;
                players[competitor].map(function(j) {
                    if(y.score == "1") {
                        j.win += 1;
                    } else if (y.score == "0.5") {
                        j.tie += 1;
                    } else if (y.score == "0") {
                        j.loss += 1
                    }
                });
            })
        });

        var header = ["Name", "Total", "Win", "Loss", "Tie"];
        for (var rows = 0; rows <= (Object.keys(players).length); rows++) {
            $('#leaderBoardTable').append("<tr id=" + 'row' + rows + " ></tr>");
                if (rows == 0) {
                    for(var j= 0; j < 5; j++) {
                        $('#row' + rows).append("<td>" + header[j] + "</td>");
                    }
                }
        }


        var i = 0;
        for (key in players) {
            players[key].map(function(z) {
                console.log(Object.keys(z));
                i = i + 1;
                console.log(i)
                        $('#row' +i).append("<td>" + key + "</td>");
                        $('#row' + i).append("<td>" + z.scores + "</td>");
                        $('#row' + i).append("<td>" + z.win + "</td>");
                        $('#row' + i).append("<td>" + z.loss + "</td>");
                        $('#row' + i).append("<td>" + z.tie + "</td>");
            });
        }
    });
}
makeLeaderBoard();