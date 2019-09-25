goog.provide('cljs.nodejs');
goog.require('cljs.core');
cljs.nodejs.require = require;
cljs.nodejs.process = process;
cljs.nodejs.enable_util_print_BANG_ = (function cljs$nodejs$enable_util_print_BANG_(){
cljs.core._STAR_print_newline_STAR_ = false;

cljs.core.set_print_fn_BANG_.call(null,(function() { 
var G__534__delegate = function (args){
return console.log.apply(console,cljs.core.into_array.call(null,args));
};
var G__534 = function (var_args){
var args = null;
if (arguments.length > 0) {
var G__535__i = 0, G__535__a = new Array(arguments.length -  0);
while (G__535__i < G__535__a.length) {G__535__a[G__535__i] = arguments[G__535__i + 0]; ++G__535__i;}
  args = new cljs.core.IndexedSeq(G__535__a,0,null);
} 
return G__534__delegate.call(this,args);};
G__534.cljs$lang$maxFixedArity = 0;
G__534.cljs$lang$applyTo = (function (arglist__536){
var args = cljs.core.seq(arglist__536);
return G__534__delegate(args);
});
G__534.cljs$core$IFn$_invoke$arity$variadic = G__534__delegate;
return G__534;
})()
);

cljs.core.set_print_err_fn_BANG_.call(null,(function() { 
var G__537__delegate = function (args){
return console.error.apply(console,cljs.core.into_array.call(null,args));
};
var G__537 = function (var_args){
var args = null;
if (arguments.length > 0) {
var G__538__i = 0, G__538__a = new Array(arguments.length -  0);
while (G__538__i < G__538__a.length) {G__538__a[G__538__i] = arguments[G__538__i + 0]; ++G__538__i;}
  args = new cljs.core.IndexedSeq(G__538__a,0,null);
} 
return G__537__delegate.call(this,args);};
G__537.cljs$lang$maxFixedArity = 0;
G__537.cljs$lang$applyTo = (function (arglist__539){
var args = cljs.core.seq(arglist__539);
return G__537__delegate(args);
});
G__537.cljs$core$IFn$_invoke$arity$variadic = G__537__delegate;
return G__537;
})()
);

return null;
});
