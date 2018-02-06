function rowCreate() {

    for (var rows = 0; rows < 11; rows++) {
        $("#gridTable").append("<div id=" + 'row' + rows + " ></div>");
    }
}
rowCreate();

function columnCreate() {
    var letters = ["0", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J"];
    var numbers = ["0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"];
    for (var rows = 0; rows < 11; rows++, j++) {
        if(rows == 0) {
            for(var j= 0; j < 11; j++) {
                $("#row" + rows).append("<div class='grid border border_right' " + " >" + numbers[j] + " </div>");
            }
        }else {
            for (var i = 0; i < 11; i++) {
                if(i == 0) {
                    $("#row" + rows).append("<div  class='grid border border_right' " + " >" + letters[rows] + "</div>");
                }else {
                    $("#row" + rows).append("<div id="  + letters[rows] + i + " class='grid border border_right'" + "></div>");
                }
            }
        }
    }
}
columnCreate();

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

function loadJsonData(param) {
    $.getJSON("http://localhost:8080/api/game_view/" + param, function(data) {
        var playerCompetitor;
        var playerMain;
        var ships = data.ship;
        var gameplayers = data.gameplayers;
        var dataplayer = data.player



        ships.map(function(x) {
            console.log(x.locations);
            x.locations.map(function(y) {
               document.getElementById(y).style.background = "blue";
            });
        });


        gameplayers.map(function(x){
            console.log(x.player.playerId);
                console.log(x.player.playerId);
                if(x.player.playerId == dataplayer.id) {
                    console.log("nesto"+dataplayer.id)
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

loadJsonData(key_url);

