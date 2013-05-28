var http = require('http'), 
  fs = require('fs');

http.createServer(function(req, res) {
  console.log("Request from: " + req.headers.referrer);
  respondOK(res);
}).listen(1338, 'localhost');
console.log('Server running at http://localhost:1338/');

var respondOK = function(res) {
  var fileStats = fs.statSync('./respondOK.xml');
  var readStream = fs.createReadStream('./respondOK.xml', {
    encoding : 'utf-8',
    autoClose : true,
    bufferSize : 1 * 1024,
    flags : 'r'
  });
  res.writeHead(200, {
    'Content-Length' : fileStats.size,
    'Content-Type' : 'text/xml'
  });
  readStream.on('data', function(data) {
    res.write(data);
  });

  readStream.on('end', function() {
    res.end();
    console.log('200 OK was send successfully');
  });
};
