loadData();
login();

// loginReload();
logout();
signUp();
createGame();

allGets();
// joinButton();

function getColumnsHtml(row) {
    return row.gameplayers.map(function(gameplayer) {
        return gameplayer.player.playerName;
    }).join(" vs ");
}


function getRowsHTMl(data) {
    var data = data.games;
    return data.map(function(row, i) {
        return "<li id=" + "dataAttr" + i + ">" + data[i].created + " " + getColumnsHtml(row) + "</li>"
    }).join("");
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


function makeLeaderBoard(data) {
    var players = new Object();
    var competitor;

    var data = data.games;
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
            i = i + 1;
            $('#row' +i).append("<td>" + key + "</td>");
            $('#row' + i).append("<td>" + z.scores + "</td>");
            $('#row' + i).append("<td>" + z.win + "</td>");
            $('#row' + i).append("<td>" + z.loss + "</td>");
            $('#row' + i).append("<td>" + z.tie + "</td>");
        });
    }
}

function showOutput(text) {
    $("#playerName").text(text);
}

function login() {
    $("#loginButton").click(function(event) {
        event.preventDefault();
        var form = event.target.form;
        loginPost(form);

    });
}

function loginPost(form) {
    $.post("/api/login",
        { username: form["username"].value,
            password: form["password"].value })
        .done(function( ) {
            console.log("Logged in!")
            // loadUser();
            loginReload()
            // allGets();
            joinButton();
        })
        .fail(function() {
            showOutput("You have to sign up first!");
        });
    }
 // load user and make  REJOIN button
 //    function loadUser() {
 //        $.getJSON("http://localhost:8080/api/games", function(data) {
 //           showOutput("Logged in as: " + data.player.playerName);
 //           console.log(data);
 //        });
 //    }



function loginReload() {
    $.getJSON("/api/games", function(data) {
    if(data.player != null) {
        showOutput("Logged in as: " + data.player.playerName);
        $("#login-form").hide();
    }
    var games = data.games;
    if(data.player != null) {
        for (var i = 0; i < games.length; i++) {
            games[i].gameplayers.map(function (x) {
                if (x.player["playerId"] == data.player["playerId"]) {
                    $("#dataAttr" + i).append("<button class='buttonsRejoin'" + " onclick=window.location.href='http://localhost:8080/web/game.html?gp=" + x.id + "'>Go to your game</button>")
                }
            });
        }
    }
    });
}

function logout() {
$("#logoutButton").click(function(event) {
    event.preventDefault();
    $.post("/api/logout")
        .done(function () {
            console.log("Logged out")
            showOutput("");
            $("#login-form").show();
            $(".buttonsRejoin").hide();
            $(".buttonJoinGame").hide();
        })
        .fail();
    });
}



function signUp() {
    $("#signupButton").click(function(event) {
        event.preventDefault();
        var form = event.target.form;
        $.post("/api/players",
            { username: form["username"].value,
                password: form["password"].value })
            .done(function( ) {
                console.log("You are signed up" )
                loginPost(form);
                // loadNewPlayer();
            })
            .fail(function( jqXHR, textStatus ) {
                showOutput("Error!");
            });
    });
}



function createGame() {
    $("#createGameButton").click(function(event) {
        event.preventDefault();
        $.post("/api/games")
            .done(function (data) {
                console.log("GamePlayerId " + data.gamePlayerId);
                window.location.href='/web/game.html?gp=' + data.gamePlayerId;
            })
            .fail(
                console.log("You failed!")
            );
    });
}



function joinButton(data) {
    $.getJSON("/api/games", function(data) {

        var mainPlayer = data.player.playerId;
        data.games.forEach(function (element, i) {
            element.gameplayers.forEach(function (x) {
                if (element.gameplayers.length == 1 && mainPlayer != x.player.playerId) {
                    $("#dataAttr" + i).append("<button class='buttonJoinGame' data-joinButton=" + element.id + ">" + "Join Game" + "</button>");
                }
            });
        });
        joinGame();
    });


}



function joinGame() {
    $(".buttonJoinGame").click(function(event) {
        event.preventDefault();
        var id = event.target.attributes[1].value;
        console.log(id);
        $.post("/api/game/"+id+"/players")
            .done(function(data) {
                console.log(data);
                window.location.href='/web/game.html?gp=' + data.gamePlayerId;
            })
            .fail();
    });
}

function allGets() {
    $.getJSON("/api/games", function(data_json) {
        var data = data_json;
        makeLeaderBoard(data);
        loginReload(data);
        joinButton(data);
        console.log(data);

    });
}

// $.ajax({
//     method: "POST",
//     contentType: "application/json; charset=utf-8",
//     url: "http://localhost:8080/api/games/players/15/ships",
//     data: JSON.stringify([{ "shipType": "destroyer", "shipLocation": ["A1", "B1", "C1"]},{ "shipType": "patrol boat", "shipLocation": ["H5", "H6"]}])
// })
