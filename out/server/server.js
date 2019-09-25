// Compiled by ClojureScript 1.10.520 {:target :nodejs}
goog.provide('server.server');
goog.require('cljs.core');
server.server.node$module$express = require('express');
server.server.node$module$socket$io = require('socket.io');
server.server.node$module$path = require('path');
cljs.core.enable_console_print_BANG_.call(null);
cljs.core.println.call(null,"Starting server...");
server.server.app = server.server.node$module$express.call(null);
server.server.server = (function (){var port = (function (){var or__4131__auto__ = process.env.PORT;
if(cljs.core.truth_(or__4131__auto__)){
return or__4131__auto__;
} else {
return (5000);
}
})();
return server.server.app.listen(port,((function (port){
return (function (){
return cljs.core.println.call(null,["Example app listening on port ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(port),"!"].join(''));
});})(port))
);
})();
server.server.io = server.server.node$module$socket$io.call(null,server.server.server);
server.server.serve_client = (function server$server$serve_client(req,resp){
return resp.sendFile(server.server.node$module$path.resolve([cljs.core.str.cljs$core$IFn$_invoke$arity$1(__dirname),"../../../index.html"].join('')));
});
server.server.app.get("/",server.server.serve_client);
server.server.io.set("origins","*:*");
/**
 * Sets up a new socket connection.
 */
server.server.setup_connection = (function server$server$setup_connection(socket){
return cljs.core.println.call(null,"Totally setting up a new socket connection.");
});
server.server.io.on("connection",server.server.setup_connection);

//# sourceMappingURL=server.js.map
