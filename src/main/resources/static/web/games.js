$(function() {
    function getColumnsHtml(row) {
        return row.gameplayers.map(function(gameplayer) {
            return gameplayer.player.playerName;
        }).join(" ");
    }

    function getRowsHTMl(data) {
        var data = data.games;
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

$(function() {

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
                $("#login-form").hide();
                loadUser();
            })
            .fail(function() {
                showOutput("You have to sign up first!");
            });

    }

    function loadUser() {
        $.getJSON("http://localhost:8080/api/games", function(data) {
            console.log(data);
           showOutput("Logged in as: " + data.player.playerName);
        });
    }

    login();

    function loginReload() {
        $.getJSON("http://localhost:8080/api/games", function(data) {
            if(data.player != null) {
                showOutput("Logged in as: " + data.player.playerName);
            }

        });
    }

    loginReload();


    function logout() {
        $("#logoutButton").click(function(event) {
            event.preventDefault();
            $.post("/api/logout")
                .done(function () {
                    console.log("Logged out")
                    showOutput("");
                    $("#login-form").show();
                })
                .fail();
        });
    }

    logout();
    // $("#logoutButton").on('click', logout());

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
                    showOutput("You are already signed up!");
                });
        });
    }

    function loadNewPlayer() {
        $.getJSON("http://localhost:8080/rest/players", function(data) {
            console.log(data);
        });
    }

    signUp();
    // $("#signupButton").on('click', signUp());

    // loadNewPlayer();



});