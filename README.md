# Punerator

It provides a list of pun suggestions for some input token. Example, for "pun": 

* prePUNderance (preponderance) 
* sPUNtaneous (spontaneous)

To build: 

    mvn package

To run on the word "pun": 

    mvn exec:java "-Dexec.args=pun"

Run with multiple words: 

    mvn exec:java "-Dexec.args=beard turtle"

Provide more suggestions (searches a larger dictionary), use ````--v````:
 
    mvn exec:java "-Dexec.args=--v beard turtle"

