// Compiled by ClojureScript 1.9.473 {}
goog.provide('adzerk.boot_reload.reload');
goog.require('cljs.core');
goog.require('clojure.string');
goog.require('goog.Uri');
goog.require('goog.async.DeferredList');
goog.require('goog.net.jsloader');
adzerk.boot_reload.reload.page_uri = (function adzerk$boot_reload$reload$page_uri(){
return (new goog.Uri(window.location.href));
});
adzerk.boot_reload.reload.ends_with_QMARK_ = (function adzerk$boot_reload$reload$ends_with_QMARK_(s,pat){
return cljs.core._EQ_.call(null,pat,cljs.core.subs.call(null,s,(cljs.core.count.call(null,s) - cljs.core.count.call(null,pat))));
});
adzerk.boot_reload.reload.reload_page_BANG_ = (function adzerk$boot_reload$reload$reload_page_BANG_(){
return window.location.reload();
});
adzerk.boot_reload.reload.normalize_href_or_uri = (function adzerk$boot_reload$reload$normalize_href_or_uri(href_or_uri){
var uri = (new goog.Uri(href_or_uri));
return adzerk.boot_reload.reload.page_uri.call(null).resolve(uri).getPath();
});
/**
 * Produce the changed goog.Uri iff the (relative) path is different
 *   compared to the content of changed (a string). Return nil otherwise.
 */
adzerk.boot_reload.reload.changed_uri = (function adzerk$boot_reload$reload$changed_uri(href_or_uri,changed){
if(cljs.core.truth_(href_or_uri)){
var path = adzerk.boot_reload.reload.normalize_href_or_uri.call(null,href_or_uri);
var temp__4657__auto__ = cljs.core.first.call(null,cljs.core.filter.call(null,((function (path){
return (function (p1__8573_SHARP_){
return adzerk.boot_reload.reload.ends_with_QMARK_.call(null,adzerk.boot_reload.reload.normalize_href_or_uri.call(null,p1__8573_SHARP_),path);
});})(path))
,changed));
if(cljs.core.truth_(temp__4657__auto__)){
var changed__$1 = temp__4657__auto__;
return goog.Uri.parse(changed__$1);
} else {
return null;
}
} else {
return null;
}
});
adzerk.boot_reload.reload.reload_css = (function adzerk$boot_reload$reload$reload_css(changed){
var sheets = document.styleSheets;
var seq__8578 = cljs.core.seq.call(null,cljs.core.range.call(null,(0),sheets.length));
var chunk__8579 = null;
var count__8580 = (0);
var i__8581 = (0);
while(true){
if((i__8581 < count__8580)){
var s = cljs.core._nth.call(null,chunk__8579,i__8581);
var temp__4657__auto___8582 = (sheets[s]);
if(cljs.core.truth_(temp__4657__auto___8582)){
var sheet_8583 = temp__4657__auto___8582;
var temp__4657__auto___8584__$1 = adzerk.boot_reload.reload.changed_uri.call(null,sheet_8583.href,changed);
if(cljs.core.truth_(temp__4657__auto___8584__$1)){
var href_uri_8585 = temp__4657__auto___8584__$1;
sheet_8583.ownerNode.href = href_uri_8585.makeUnique().toString();
} else {
}
} else {
}

var G__8586 = seq__8578;
var G__8587 = chunk__8579;
var G__8588 = count__8580;
var G__8589 = (i__8581 + (1));
seq__8578 = G__8586;
chunk__8579 = G__8587;
count__8580 = G__8588;
i__8581 = G__8589;
continue;
} else {
var temp__4657__auto__ = cljs.core.seq.call(null,seq__8578);
if(temp__4657__auto__){
var seq__8578__$1 = temp__4657__auto__;
if(cljs.core.chunked_seq_QMARK_.call(null,seq__8578__$1)){
var c__7930__auto__ = cljs.core.chunk_first.call(null,seq__8578__$1);
var G__8590 = cljs.core.chunk_rest.call(null,seq__8578__$1);
var G__8591 = c__7930__auto__;
var G__8592 = cljs.core.count.call(null,c__7930__auto__);
var G__8593 = (0);
seq__8578 = G__8590;
chunk__8579 = G__8591;
count__8580 = G__8592;
i__8581 = G__8593;
continue;
} else {
var s = cljs.core.first.call(null,seq__8578__$1);
var temp__4657__auto___8594__$1 = (sheets[s]);
if(cljs.core.truth_(temp__4657__auto___8594__$1)){
var sheet_8595 = temp__4657__auto___8594__$1;
var temp__4657__auto___8596__$2 = adzerk.boot_reload.reload.changed_uri.call(null,sheet_8595.href,changed);
if(cljs.core.truth_(temp__4657__auto___8596__$2)){
var href_uri_8597 = temp__4657__auto___8596__$2;
sheet_8595.ownerNode.href = href_uri_8597.makeUnique().toString();
} else {
}
} else {
}

var G__8598 = cljs.core.next.call(null,seq__8578__$1);
var G__8599 = null;
var G__8600 = (0);
var G__8601 = (0);
seq__8578 = G__8598;
chunk__8579 = G__8599;
count__8580 = G__8600;
i__8581 = G__8601;
continue;
}
} else {
return null;
}
}
break;
}
});
adzerk.boot_reload.reload.reload_img = (function adzerk$boot_reload$reload$reload_img(changed){
var images = document.images;
var seq__8606 = cljs.core.seq.call(null,cljs.core.range.call(null,(0),images.length));
var chunk__8607 = null;
var count__8608 = (0);
var i__8609 = (0);
while(true){
if((i__8609 < count__8608)){
var s = cljs.core._nth.call(null,chunk__8607,i__8609);
var temp__4657__auto___8610 = (images[s]);
if(cljs.core.truth_(temp__4657__auto___8610)){
var image_8611 = temp__4657__auto___8610;
var temp__4657__auto___8612__$1 = adzerk.boot_reload.reload.changed_uri.call(null,image_8611.src,changed);
if(cljs.core.truth_(temp__4657__auto___8612__$1)){
var href_uri_8613 = temp__4657__auto___8612__$1;
image_8611.src = href_uri_8613.makeUnique().toString();
} else {
}
} else {
}

var G__8614 = seq__8606;
var G__8615 = chunk__8607;
var G__8616 = count__8608;
var G__8617 = (i__8609 + (1));
seq__8606 = G__8614;
chunk__8607 = G__8615;
count__8608 = G__8616;
i__8609 = G__8617;
continue;
} else {
var temp__4657__auto__ = cljs.core.seq.call(null,seq__8606);
if(temp__4657__auto__){
var seq__8606__$1 = temp__4657__auto__;
if(cljs.core.chunked_seq_QMARK_.call(null,seq__8606__$1)){
var c__7930__auto__ = cljs.core.chunk_first.call(null,seq__8606__$1);
var G__8618 = cljs.core.chunk_rest.call(null,seq__8606__$1);
var G__8619 = c__7930__auto__;
var G__8620 = cljs.core.count.call(null,c__7930__auto__);
var G__8621 = (0);
seq__8606 = G__8618;
chunk__8607 = G__8619;
count__8608 = G__8620;
i__8609 = G__8621;
continue;
} else {
var s = cljs.core.first.call(null,seq__8606__$1);
var temp__4657__auto___8622__$1 = (images[s]);
if(cljs.core.truth_(temp__4657__auto___8622__$1)){
var image_8623 = temp__4657__auto___8622__$1;
var temp__4657__auto___8624__$2 = adzerk.boot_reload.reload.changed_uri.call(null,image_8623.src,changed);
if(cljs.core.truth_(temp__4657__auto___8624__$2)){
var href_uri_8625 = temp__4657__auto___8624__$2;
image_8623.src = href_uri_8625.makeUnique().toString();
} else {
}
} else {
}

var G__8626 = cljs.core.next.call(null,seq__8606__$1);
var G__8627 = null;
var G__8628 = (0);
var G__8629 = (0);
seq__8606 = G__8626;
chunk__8607 = G__8627;
count__8608 = G__8628;
i__8609 = G__8629;
continue;
}
} else {
return null;
}
}
break;
}
});
adzerk.boot_reload.reload.reload_js = (function adzerk$boot_reload$reload$reload_js(changed,p__8632){
var map__8635 = p__8632;
var map__8635__$1 = ((((!((map__8635 == null)))?((((map__8635.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__8635.cljs$core$ISeq$)))?true:false):false))?cljs.core.apply.call(null,cljs.core.hash_map,map__8635):map__8635);
var on_jsload = cljs.core.get.call(null,map__8635__$1,new cljs.core.Keyword(null,"on-jsload","on-jsload",-395756602),cljs.core.identity);
var js_files = cljs.core.filter.call(null,((function (map__8635,map__8635__$1,on_jsload){
return (function (p1__8630_SHARP_){
return adzerk.boot_reload.reload.ends_with_QMARK_.call(null,p1__8630_SHARP_,".js");
});})(map__8635,map__8635__$1,on_jsload))
,changed);
if(cljs.core.seq.call(null,js_files)){
goog.net.jsloader.loadMany(cljs.core.clj__GT_js.call(null,cljs.core.map.call(null,((function (js_files,map__8635,map__8635__$1,on_jsload){
return (function (p1__8631_SHARP_){
return goog.Uri.parse(p1__8631_SHARP_).makeUnique();
});})(js_files,map__8635,map__8635__$1,on_jsload))
,js_files)),({"cleanupWhenDone": true})).addCallbacks(((function (js_files,map__8635,map__8635__$1,on_jsload){
return (function() { 
var G__8637__delegate = function (_){
return on_jsload.call(null);
};
var G__8637 = function (var_args){
var _ = null;
if (arguments.length > 0) {
var G__8638__i = 0, G__8638__a = new Array(arguments.length -  0);
while (G__8638__i < G__8638__a.length) {G__8638__a[G__8638__i] = arguments[G__8638__i + 0]; ++G__8638__i;}
  _ = new cljs.core.IndexedSeq(G__8638__a,0);
} 
return G__8637__delegate.call(this,_);};
G__8637.cljs$lang$maxFixedArity = 0;
G__8637.cljs$lang$applyTo = (function (arglist__8639){
var _ = cljs.core.seq(arglist__8639);
return G__8637__delegate(_);
});
G__8637.cljs$core$IFn$_invoke$arity$variadic = G__8637__delegate;
return G__8637;
})()
;})(js_files,map__8635,map__8635__$1,on_jsload))
,((function (js_files,map__8635,map__8635__$1,on_jsload){
return (function (e){
return console.error("Load failed:",e.message);
});})(js_files,map__8635,map__8635__$1,on_jsload))
);

if(cljs.core.truth_((window["jQuery"]))){
return jQuery(document).trigger("page-load");
} else {
return null;
}
} else {
return null;
}
});
adzerk.boot_reload.reload.reload_html = (function adzerk$boot_reload$reload$reload_html(changed){
var page_path = adzerk.boot_reload.reload.page_uri.call(null).getPath();
var html_path = (cljs.core.truth_(adzerk.boot_reload.reload.ends_with_QMARK_.call(null,page_path,"/"))?[cljs.core.str.cljs$core$IFn$_invoke$arity$1(page_path),cljs.core.str.cljs$core$IFn$_invoke$arity$1("index.html")].join(''):page_path);
if(cljs.core.truth_(adzerk.boot_reload.reload.changed_uri.call(null,html_path,changed))){
return adzerk.boot_reload.reload.reload_page_BANG_.call(null);
} else {
return null;
}
});
adzerk.boot_reload.reload.group_log = (function adzerk$boot_reload$reload$group_log(title,things_to_log){
console.groupCollapsed(title);

var seq__8644_8648 = cljs.core.seq.call(null,things_to_log);
var chunk__8645_8649 = null;
var count__8646_8650 = (0);
var i__8647_8651 = (0);
while(true){
if((i__8647_8651 < count__8646_8650)){
var t_8652 = cljs.core._nth.call(null,chunk__8645_8649,i__8647_8651);
console.log(t_8652);

var G__8653 = seq__8644_8648;
var G__8654 = chunk__8645_8649;
var G__8655 = count__8646_8650;
var G__8656 = (i__8647_8651 + (1));
seq__8644_8648 = G__8653;
chunk__8645_8649 = G__8654;
count__8646_8650 = G__8655;
i__8647_8651 = G__8656;
continue;
} else {
var temp__4657__auto___8657 = cljs.core.seq.call(null,seq__8644_8648);
if(temp__4657__auto___8657){
var seq__8644_8658__$1 = temp__4657__auto___8657;
if(cljs.core.chunked_seq_QMARK_.call(null,seq__8644_8658__$1)){
var c__7930__auto___8659 = cljs.core.chunk_first.call(null,seq__8644_8658__$1);
var G__8660 = cljs.core.chunk_rest.call(null,seq__8644_8658__$1);
var G__8661 = c__7930__auto___8659;
var G__8662 = cljs.core.count.call(null,c__7930__auto___8659);
var G__8663 = (0);
seq__8644_8648 = G__8660;
chunk__8645_8649 = G__8661;
count__8646_8650 = G__8662;
i__8647_8651 = G__8663;
continue;
} else {
var t_8664 = cljs.core.first.call(null,seq__8644_8658__$1);
console.log(t_8664);

var G__8665 = cljs.core.next.call(null,seq__8644_8658__$1);
var G__8666 = null;
var G__8667 = (0);
var G__8668 = (0);
seq__8644_8648 = G__8665;
chunk__8645_8649 = G__8666;
count__8646_8650 = G__8667;
i__8647_8651 = G__8668;
continue;
}
} else {
}
}
break;
}

return console.groupEnd();
});
/**
 * Perform heuristics to check if a non-shimmed DOM is available
 */
adzerk.boot_reload.reload.has_dom_QMARK_ = (function adzerk$boot_reload$reload$has_dom_QMARK_(){
return (typeof window !== 'undefined') && (typeof window.document !== 'undefined') && (typeof window.document.documentURI !== 'undefined');
});
adzerk.boot_reload.reload.reload = (function adzerk$boot_reload$reload$reload(changed,opts){
var changed_STAR_ = cljs.core.map.call(null,(function (p1__8669_SHARP_){
return [cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"asset-host","asset-host",-899289050).cljs$core$IFn$_invoke$arity$1(opts)),cljs.core.str.cljs$core$IFn$_invoke$arity$1(p1__8669_SHARP_)].join('');
}),cljs.core.map.call(null,(function (p__8675){
var map__8676 = p__8675;
var map__8676__$1 = ((((!((map__8676 == null)))?((((map__8676.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__8676.cljs$core$ISeq$)))?true:false):false))?cljs.core.apply.call(null,cljs.core.hash_map,map__8676):map__8676);
var canonical_path = cljs.core.get.call(null,map__8676__$1,new cljs.core.Keyword(null,"canonical-path","canonical-path",-1891747854));
var web_path = cljs.core.get.call(null,map__8676__$1,new cljs.core.Keyword(null,"web-path","web-path",594590673));
if(cljs.core._EQ_.call(null,"file:",(function (){var G__8678 = window;
var G__8678__$1 = (((G__8678 == null))?null:G__8678.location);
if((G__8678__$1 == null)){
return null;
} else {
return G__8678__$1.protocol;
}
})())){
return canonical_path;
} else {
return web_path;
}
}),changed));
adzerk.boot_reload.reload.group_log.call(null,"Reload",changed_STAR_);

adzerk.boot_reload.reload.reload_js.call(null,changed_STAR_,opts);

if(cljs.core.truth_(adzerk.boot_reload.reload.has_dom_QMARK_.call(null))){
var G__8679 = changed_STAR_;
adzerk.boot_reload.reload.reload_html.call(null,G__8679);

adzerk.boot_reload.reload.reload_css.call(null,G__8679);

adzerk.boot_reload.reload.reload_img.call(null,G__8679);

return G__8679;
} else {
return null;
}
});

//# sourceMappingURL=reload.js.map