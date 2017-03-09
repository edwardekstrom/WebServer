WebServer
=========

Usage: java -cp WebServer.jar web.Server <port> <web root> <threads limit>

To test PUT run:  curl -H "Content-Type: text/plane" -H "Content-Length: 55" -X POST -d "This should write (overwrite) a new (an existing) file." http://localhost:<port where the server is running>/newDir/newFile.txt

Code that I created will be surrounded with with

<comment symbol for language> **************** Start code by Edward Ekstrom ****************
<the code>
<comment sybmol for language> **************** End code by Edward Ekstrom ****************

The rest of the code is by Igor Bogomolov.  I forked his repo.