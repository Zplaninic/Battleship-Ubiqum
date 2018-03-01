rowCreate("#gridShipTable", "rowShip");
rowCreate("#gridSalvoTable", "rowSalvo");

columnCreate("#rowShip");
columnCreate("#rowSalvo");


var url = window.location.href;
var key_url;

paramObj(url);

loadJsonData(key_url);

logout();



function rowCreate(divId, rowId) {

    for (var rows = 0; rows < 11; rows++) {
        $(divId).append("<div id=" + rowId + rows + " ></div>");
    }
}

function columnCreate(rowId) {
    var letters = ["0", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J"];
    var numbers = ["0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"];
    for (var rows = 0; rows < 11; rows++, j++) {
        if(rows == 0) {
            for(var j= 0; j < 11; j++) {
                $(rowId + rows).append("<div class='grid' " + " >" + numbers[j] + " </div>");
            }
        }else {
            for (var i = 0; i < 11; i++) {
                if(i == 0) {
                    $(rowId + rows).append("<div  class='grid' " + " >" + letters[rows] + "</div>");
                }else {
                    $(rowId + rows).append("<div id="  + letters[rows] + i + " class=" + "grid " + "ondrop=" + "drop(event)" +" ondragover=" + "allowDrop(event)"
                        + " data-occupied=" + "no" + " ></div>");
                }
            }
        }
    }
}

function paramObj(search) {
    var obj = {};
    var reg = /(?:[?&]([^?&#=]+)(?:=([^&#]*))?)(?:#.*)?/g;

    search.replace(reg, function(match, param, val) {
        obj[decodeURIComponent(param)] = val === undefined ? "" : decodeURIComponent(val);
    });
    key_url = obj.gp;
    return key_url;

}

function loadJsonShipData(param, data) {
        var playerCompetitor;
        var playerMain;
        var ships = data.ship;
        var gameplayers = data.gameplayers;
        var dataplayer = data.player;

        ships.map(function(x) {
            x.locations.map(function(y) {
                $("#gridShipTable #" + y).css("background-color","blue")
            });
        });


        gameplayers.map(function(x){
                 if(x.player["playerId"] == dataplayer.playerId) {
                    playerMain = x.player.playerName + "(you)"
                    return playerMain;
                }else {
                    playerCompetitor = x.player.playerName;
                    return playerCompetitor;
                }
        });

        $("#title").append("<h3>" + playerMain + " vs " +  playerCompetitor + "</h3>");

}



function loadJsonSalvoData(param, data) {
        data.salvoes.map(function(x) {
            x.map(function(y) {
                console.log(y.turn);
                if(y.player_id == param) {
                    y.locations.map(function(z) {
                        $("#gridSalvoTable #" + z).append("<div class='salvoPlayer'>" + "<span class='text'>"  + y.turn + "</span>" + "</div>");
                    })

                }
                else {
                    y.locations.map(function(z) {

                        $("#gridShipTable #" + z).append("<div class='salvoOpponent'>" + "<span class='text'>"  + y.turn + "</span>" + "</div>");
                    // .css({"background-color": "red", "text-align": "center"})
                    });
                }
            });
        });

}

function logout() {
    $("#logoutButton").click(function(event) {
        event.preventDefault();
        $.post("/api/logout")
            .done(function () {
                window.location.replace("/web/games.html");
                console.log("Logged out");
            })
            .fail();
    });
}

function loadJsonData(param) {
    $.getJSON("/api/game_view/" + param, function(data_json) {
        var data = data_json;
        loadJsonShipData(param, data);
        loadJsonSalvoData(param, data);
        createShips(data);

    })
}

function createShips(data) {

    var allships = {"destroyer": 3,
        "patrol_boat": 2,
        "submarine": 3,
        "battleship": 4,
        "aircraft_carrier": 5}

    console.log(allships);
    for (var property in allships) {
        $("#shipsDrag").append("<div id=" + property + allships[property] + " draggable="  + "true" +" ondragstart=" + "drag(event)"
            + " ondrop=" + "drop(event)" + "ondragleave=" + "dragLeave(event)" + "></div>")
        console.log(allships[property]);
        for(var i = 0; i<allships[property]; i++) {
            $("#" + property + allships[property]).append("<div class=" + property + i +"></div>")
        }

    }

}

function drag(ev) {
    ev.dataTransfer.setData("text", ev.target.id);
    var firstDiv = ev.target.offsetParent["id"];

    $("#" + ev.target.id).find("div").each(function(index) {
        var counter = Number(firstDiv[1]) + index;
        $('#' + firstDiv[0] + counter).attr('data-occupied', 'no');
    });
}

function allowDrop(ev) {
    ev.preventDefault();
}

function drop(ev) {
    ev.preventDefault();
    var data = ev.dataTransfer.getData("text");
    var firstDiv = ev.target["id"];

    $('#' + data).find("div").each(function() {
        var datAtr = $('#' + firstDiv).data("occupied");
        console.log(datAtr);
        if (datAtr == 'no') {
            ev.target.appendChild((document.getElementById(data)));
            $('#' + data).find("div").each(function(index) {
                var counter = Number(firstDiv[1]) + index;
                $('#' + firstDiv[0] + counter).attr('data-occupied', 'yes');
            });
        }
    });
    //
    // ev.target.appendChild((document.getElementById(data)));
    //
    // $('#' + data).find("div").each(function(index) {
    //     var counter = Number(firstDiv[1]) + index;
    //     $('#' + firstDiv[0] + counter).attr('data-occupied', 'yes');
    // });
}


// function dragLeave(ev) {
//     ev.preventDefault();
//     var data = ev.dataTransfer.getData("text");
//     console.log(data);
//     ev.target;
//     console.log(ev.target);
// }

