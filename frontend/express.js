var express = require('express');
var app = express();
app.disable('x-powered-by');

app.set('port', process.env.PORT || 8000);

app.use(express.static(__dirname + '/app'));

app.all('/*', function(req, res, next) {
    // Just send the index.html for other files to support HTML5Mode
    res.sendFile('app/index.html', { root: __dirname });
});

app.listen(app.get('port'), function() {
    console.log("============================================");
    console.log("================== ACHTUNG =================");
    console.log("=Server started on -> http://localhost:" + app.get('port') + "=");
    console.log("===Press Ctrl-C to terminate the server!====");
    console.log("============================================");
});
