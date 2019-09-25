// Compiled by ClojureScript 1.10.520 {:target :nodejs}
goog.provide('server.core');
goog.require('cljs.core');
server.core.node$module$express = require('express');
cljs.core.enable_console_print_BANG_.call(null);
cljs.core.println.call(null,"Starting server...");
var app_526 = server.core.node$module$express.call(null);
app_526.get("/",((function (app_526){
return (function (req,res){
return res.send("Hello, world!");
});})(app_526))
);

app_526.listen((3000),((function (app_526){
return (function (){
return cljs.core.println.call(null,"Example app listening on port 3000!");
});})(app_526))
);

//# sourceMappingURL=core.js.map
