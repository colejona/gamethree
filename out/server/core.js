// Compiled by ClojureScript 1.10.520 {:target :nodejs}
goog.provide('server.core');
goog.require('cljs.core');
server.core.node$module$express = require('express');
server.core.node$module$socket$io = require('socket.io');
server.core.node$module$path = require('path');
cljs.core.enable_console_print_BANG_.call(null);
cljs.core.println.call(null,"Starting server...");
server.core.app = server.core.node$module$express.call(null);
server.core.server = (function (){var port = (function (){var or__4131__auto__ = process.env.PORT;
if(cljs.core.truth_(or__4131__auto__)){
return or__4131__auto__;
} else {
return (5000);
}
})();
return server.core.app.listen(port,((function (port){
return (function (){
return cljs.core.println.call(null,["Example app listening on port ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(port),"!"].join(''));
});})(port))
);
})();
server.core.io = server.core.node$module$socket$io.call(null,server.core.server);
server.core.serve_client = (function server$core$serve_client(req,resp){
return resp.sendFile(server.core.node$module$path.resolve([cljs.core.str.cljs$core$IFn$_invoke$arity$1(__dirname),"../../../build/index.html"].join('')));
});
server.core.app.get("/",server.core.serve_client);
server.core.io.set("origins","*:*");
/**
 * Sets up a new socket connection.
 */
server.core.setup_connection = (function server$core$setup_connection(socket){
return cljs.core.println.call(null,"Totally setting up a new socket connection.");
});
server.core.io.on("connection",server.core.setup_connection);

//# sourceMappingURL=core.js.map
