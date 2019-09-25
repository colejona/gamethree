goog.provide('cljs.nodejs');
goog.require('cljs.core');
cljs.nodejs.require = require;
cljs.nodejs.process = process;
cljs.nodejs.enable_util_print_BANG_ = (function cljs$nodejs$enable_util_print_BANG_(){
cljs.core._STAR_print_newline_STAR_ = false;

cljs.core.set_print_fn_BANG_.call(null,(function() { 
var G__536__delegate = function (args){
return console.log.apply(console,cljs.core.into_array.call(null,args));
};
var G__536 = function (var_args){
var args = null;
if (arguments.length > 0) {
var G__537__i = 0, G__537__a = new Array(arguments.length -  0);
while (G__537__i < G__537__a.length) {G__537__a[G__537__i] = arguments[G__537__i + 0]; ++G__537__i;}
  args = new cljs.core.IndexedSeq(G__537__a,0,null);
} 
return G__536__delegate.call(this,args);};
G__536.cljs$lang$maxFixedArity = 0;
G__536.cljs$lang$applyTo = (function (arglist__538){
var args = cljs.core.seq(arglist__538);
return G__536__delegate(args);
});
G__536.cljs$core$IFn$_invoke$arity$variadic = G__536__delegate;
return G__536;
})()
);

cljs.core.set_print_err_fn_BANG_.call(null,(function() { 
var G__539__delegate = function (args){
return console.error.apply(console,cljs.core.into_array.call(null,args));
};
var G__539 = function (var_args){
var args = null;
if (arguments.length > 0) {
var G__540__i = 0, G__540__a = new Array(arguments.length -  0);
while (G__540__i < G__540__a.length) {G__540__a[G__540__i] = arguments[G__540__i + 0]; ++G__540__i;}
  args = new cljs.core.IndexedSeq(G__540__a,0,null);
} 
return G__539__delegate.call(this,args);};
G__539.cljs$lang$maxFixedArity = 0;
G__539.cljs$lang$applyTo = (function (arglist__541){
var args = cljs.core.seq(arglist__541);
return G__539__delegate(args);
});
G__539.cljs$core$IFn$_invoke$arity$variadic = G__539__delegate;
return G__539;
})()
);

return null;
});
