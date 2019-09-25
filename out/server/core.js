// Compiled by ClojureScript 1.10.520 {:target :nodejs}
goog.provide('server.core');
goog.require('cljs.core');
server.core.node$module$express = require('express');
cljs.core.enable_console_print_BANG_.call(null);
cljs.core.println.call(null,"Starting server...");
var app_526 = server.core.node$module$express.call(null);
var port_527 = (function (){var or__4131__auto__ = process.env.PORT;
if(cljs.core.truth_(or__4131__auto__)){
return or__4131__auto__;
} else {
return (5000);
}
})();
app_526.get("/",((function (app_526,port_527){
return (function (req,res){
return res.send("Hello, world!");
});})(app_526,port_527))
);

app_526.listen(port_527,((function (app_526,port_527){
return (function (){
return cljs.core.println.call(null,["Example app listening on port ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(port_527),"!"].join(''));
});})(app_526,port_527))
);

//# sourceMappingURL=core.js.map
