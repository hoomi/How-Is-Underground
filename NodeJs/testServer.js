var http = require('http'), 
  fs = require('fs');

http.createServer(function(req, res) {
  var url = req.url;
  console.log(req.url);
  if (url === '/200') {
    respondOK(res);
  } else if (url === '/403') {
    respondForbidden(res);
  } else if (url === '/500') {
    respondInternalSError(res);
  } else {
    respondNotFound(res);
  }
}).listen(1337, '127.0.0.1');
console.log('Server running at http://127.0.0.1:1337/');

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

var respondForbidden = function (res) {
  res.writeHead(403, {'Content-Type': 'text/plain'});
  res.end('Access to this website is forbidden\n');
};

var respondNotFound = function (res) {
  res.writeHead(404, {'Content-Type': 'text/plain'});
  res.end('Could not find this website\n');
};

var respondInternalSError= function (res) {
  res.writeHead(500, {'Content-Type': 'text/plain'});
  res.end('An internal server error\n');
};