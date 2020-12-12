let openConnection = false;
const socket = new WebSocket('ws://localhost:50000/twitterWS'); // assuming you have your ws enpdoint there.

$(document).ready(function () {

    socket.addEventListener('open', function (event) {
        console.log("Connection opened");
        openConnection = true;
    });

    socket.addEventListener('message', function (event) {
        // event.data contains the tweets
        $("#resultsBlock").html(Mustache.render(template, event.data));

    });
    registerSearch();
    registerTemplate();
});

function registerSearch() {
    $("#search").submit(function (event) {
        event.preventDefault();
        if (openConnection) {
            socket.send($("#q").val());
        }
    });
}

function registerTemplate() {
    template = $("#template").html();
    Mustache.parse(template);
}