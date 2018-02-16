function rowCreate(divId, rowId) {

    for (var rows = 0; rows < 11; rows++) {
        $(divId).append("<tr id=" + rowId + rows + " ></tr>");
    }
}
rowCreate("#gridShipTable", "rowShip");
rowCreate("#gridSalvoTable", "rowSalvo");
function columnCreate(rowId) {
    var letters = ["0", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J"];
    var numbers = ["0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"];
    for (var rows = 0; rows < 11; rows++, j++) {
        if(rows == 0) {
            for(var j= 0; j < 11; j++) {
                $(rowId + rows).append("<td class='grid' " + " >" + numbers[j] + " </td>");
            }
        }else {
            for (var i = 0; i < 11; i++) {
                if(i == 0) {
                    $(rowId + rows).append("<td  class='grid' " + " >" + letters[rows] + "</td>");
                }else {
                    $(rowId + rows).append("<td id="  + letters[rows] + i + " class=" + "grid" + "></td>");
                }
            }
        }
    }
}
columnCreate("#rowShip");
columnCreate("#rowSalvo");

// id=" + letters[j] + rows + "
// id=" + letters[i] + rows + "

var url = window.location.href;
var key_url;

function paramObj(search) {
    var obj = {};
    var reg = /(?:[?&]([^?&#=]+)(?:=([^&#]*))?)(?:#.*)?/g;

    search.replace(reg, function(match, param, val) {
        obj[decodeURIComponent(param)] = val === undefined ? "" : decodeURIComponent(val);
    });
    key_url = obj.gp;
    return key_url;

}
paramObj(url);

function loadJsonShipData(param) {
    $.getJSON("http://localhost:8080/api/game_view/" + param, function(data) {
        var playerCompetitor;
        var playerMain;
        var ships = data.ship;
        var gameplayers = data.gameplayers;
        var dataplayer = data.player



        ships.map(function(x) {
            x.locations.map(function(y) {
                $("#gridShipTable #" + y).css("background-color","blue")
               // document.getElementById(y).style.background = "blue";
            });
        });


        gameplayers.map(function(x){

            console.log(dataplayer)
                 if(x.player["playerId"] == dataplayer.playerId) {
                    playerMain = x.player.playerName + "(you)"
                    return playerMain;
                }else {
                    playerCompetitor = x.player.playerName;
                    return playerCompetitor;
                }
        });

        $("#title").append("<h3>" + playerMain + " vs " +  playerCompetitor + "</h3>");
    });
}

loadJsonShipData(key_url);

function loadJsonSalvoData(param) {
    $.getJSON("http://localhost:8080/api/game_view/" + param, function(data) {
        data.salvoes.map(function(x) {
            x.map(function(y) {
                console.log(y.turn);
                if(y.player_id == param) {
                    y.locations.map(function(z) {
                        console.log(z);
                        $("#gridSalvoTable #" + z).append("<div class='salvoPlayer'>" + "<span class='text'>"  + y.turn + "</span>" + "</div>");
                    })

                }
                else {
                    y.locations.map(function(z) {
                        console.log("nesto " + z);
                        $("#gridShipTable #" + z).append("<div class='salvoOpponent'>" + "<span class='text'>"  + y.turn + "</span>" + "</div>");
                    // .css({"background-color": "red", "text-align": "center"})
                    });
                }
            });
        });
    });

}

loadJsonSalvoData(key_url);

function logout() {
    $("#logoutButton").click(function(event) {
        event.preventDefault();
        $.post("/api/logout")
            .done(function () {
                window.location.replace("http://localhost:8080/web/games.html");
                console.log("Logged out");
            })
            .fail();
    });
}

logout();

