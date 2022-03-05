// Compiled by ClojureScript 1.7.228 {}
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
var temp__4425__auto__ = cljs.core.first.call(null,cljs.core.filter.call(null,((function (path){
return (function (p1__7660_SHARP_){
return adzerk.boot_reload.reload.ends_with_QMARK_.call(null,adzerk.boot_reload.reload.normalize_href_or_uri.call(null,p1__7660_SHARP_),path);
});})(path))
,changed));
if(cljs.core.truth_(temp__4425__auto__)){
var changed__$1 = temp__4425__auto__;
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
var seq__7665 = cljs.core.seq.call(null,cljs.core.range.call(null,(0),sheets.length));
var chunk__7666 = null;
var count__7667 = (0);
var i__7668 = (0);
while(true){
if((i__7668 < count__7667)){
var s = cljs.core._nth.call(null,chunk__7666,i__7668);
var temp__4425__auto___7669 = (sheets[s]);
if(cljs.core.truth_(temp__4425__auto___7669)){
var sheet_7670 = temp__4425__auto___7669;
var temp__4425__auto___7671__$1 = adzerk.boot_reload.reload.changed_uri.call(null,sheet_7670.href,changed);
if(cljs.core.truth_(temp__4425__auto___7671__$1)){
var href_uri_7672 = temp__4425__auto___7671__$1;
sheet_7670.ownerNode.href = href_uri_7672.makeUnique().toString();
} else {
}
} else {
}

var G__7673 = seq__7665;
var G__7674 = chunk__7666;
var G__7675 = count__7667;
var G__7676 = (i__7668 + (1));
seq__7665 = G__7673;
chunk__7666 = G__7674;
count__7667 = G__7675;
i__7668 = G__7676;
continue;
} else {
var temp__4425__auto__ = cljs.core.seq.call(null,seq__7665);
if(temp__4425__auto__){
var seq__7665__$1 = temp__4425__auto__;
if(cljs.core.chunked_seq_QMARK_.call(null,seq__7665__$1)){
var c__7225__auto__ = cljs.core.chunk_first.call(null,seq__7665__$1);
var G__7677 = cljs.core.chunk_rest.call(null,seq__7665__$1);
var G__7678 = c__7225__auto__;
var G__7679 = cljs.core.count.call(null,c__7225__auto__);
var G__7680 = (0);
seq__7665 = G__7677;
chunk__7666 = G__7678;
count__7667 = G__7679;
i__7668 = G__7680;
continue;
} else {
var s = cljs.core.first.call(null,seq__7665__$1);
var temp__4425__auto___7681__$1 = (sheets[s]);
if(cljs.core.truth_(temp__4425__auto___7681__$1)){
var sheet_7682 = temp__4425__auto___7681__$1;
var temp__4425__auto___7683__$2 = adzerk.boot_reload.reload.changed_uri.call(null,sheet_7682.href,changed);
if(cljs.core.truth_(temp__4425__auto___7683__$2)){
var href_uri_7684 = temp__4425__auto___7683__$2;
sheet_7682.ownerNode.href = href_uri_7684.makeUnique().toString();
} else {
}
} else {
}

var G__7685 = cljs.core.next.call(null,seq__7665__$1);
var G__7686 = null;
var G__7687 = (0);
var G__7688 = (0);
seq__7665 = G__7685;
chunk__7666 = G__7686;
count__7667 = G__7687;
i__7668 = G__7688;
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
var seq__7693 = cljs.core.seq.call(null,cljs.core.range.call(null,(0),images.length));
var chunk__7694 = null;
var count__7695 = (0);
var i__7696 = (0);
while(true){
if((i__7696 < count__7695)){
var s = cljs.core._nth.call(null,chunk__7694,i__7696);
var temp__4425__auto___7697 = (images[s]);
if(cljs.core.truth_(temp__4425__auto___7697)){
var image_7698 = temp__4425__auto___7697;
var temp__4425__auto___7699__$1 = adzerk.boot_reload.reload.changed_uri.call(null,image_7698.src,changed);
if(cljs.core.truth_(temp__4425__auto___7699__$1)){
var href_uri_7700 = temp__4425__auto___7699__$1;
image_7698.src = href_uri_7700.makeUnique().toString();
} else {
}
} else {
}

var G__7701 = seq__7693;
var G__7702 = chunk__7694;
var G__7703 = count__7695;
var G__7704 = (i__7696 + (1));
seq__7693 = G__7701;
chunk__7694 = G__7702;
count__7695 = G__7703;
i__7696 = G__7704;
continue;
} else {
var temp__4425__auto__ = cljs.core.seq.call(null,seq__7693);
if(temp__4425__auto__){
var seq__7693__$1 = temp__4425__auto__;
if(cljs.core.chunked_seq_QMARK_.call(null,seq__7693__$1)){
var c__7225__auto__ = cljs.core.chunk_first.call(null,seq__7693__$1);
var G__7705 = cljs.core.chunk_rest.call(null,seq__7693__$1);
var G__7706 = c__7225__auto__;
var G__7707 = cljs.core.count.call(null,c__7225__auto__);
var G__7708 = (0);
seq__7693 = G__7705;
chunk__7694 = G__7706;
count__7695 = G__7707;
i__7696 = G__7708;
continue;
} else {
var s = cljs.core.first.call(null,seq__7693__$1);
var temp__4425__auto___7709__$1 = (images[s]);
if(cljs.core.truth_(temp__4425__auto___7709__$1)){
var image_7710 = temp__4425__auto___7709__$1;
var temp__4425__auto___7711__$2 = adzerk.boot_reload.reload.changed_uri.call(null,image_7710.src,changed);
if(cljs.core.truth_(temp__4425__auto___7711__$2)){
var href_uri_7712 = temp__4425__auto___7711__$2;
image_7710.src = href_uri_7712.makeUnique().toString();
} else {
}
} else {
}

var G__7713 = cljs.core.next.call(null,seq__7693__$1);
var G__7714 = null;
var G__7715 = (0);
var G__7716 = (0);
seq__7693 = G__7713;
chunk__7694 = G__7714;
count__7695 = G__7715;
i__7696 = G__7716;
continue;
}
} else {
return null;
}
}
break;
}
});
adzerk.boot_reload.reload.reload_js = (function adzerk$boot_reload$reload$reload_js(changed,p__7719){
var map__7722 = p__7719;
var map__7722__$1 = ((((!((map__7722 == null)))?((((map__7722.cljs$lang$protocol_mask$partition0$ & (64))) || (map__7722.cljs$core$ISeq$))?true:false):false))?cljs.core.apply.call(null,cljs.core.hash_map,map__7722):map__7722);
var on_jsload = cljs.core.get.call(null,map__7722__$1,new cljs.core.Keyword(null,"on-jsload","on-jsload",-395756602),cljs.core.identity);
var js_files = cljs.core.filter.call(null,((function (map__7722,map__7722__$1,on_jsload){
return (function (p1__7717_SHARP_){
return adzerk.boot_reload.reload.ends_with_QMARK_.call(null,p1__7717_SHARP_,".js");
});})(map__7722,map__7722__$1,on_jsload))
,changed);
if(cljs.core.seq.call(null,js_files)){
goog.net.jsloader.loadMany(cljs.core.clj__GT_js.call(null,cljs.core.map.call(null,((function (js_files,map__7722,map__7722__$1,on_jsload){
return (function (p1__7718_SHARP_){
return goog.Uri.parse(p1__7718_SHARP_).makeUnique();
});})(js_files,map__7722,map__7722__$1,on_jsload))
,js_files)),{"cleanupWhenDone": true}).addCallbacks(((function (js_files,map__7722,map__7722__$1,on_jsload){
return (function() { 
var G__7724__delegate = function (_){
return on_jsload.call(null);
};
var G__7724 = function (var_args){
var _ = null;
if (arguments.length > 0) {
var G__7725__i = 0, G__7725__a = new Array(arguments.length -  0);
while (G__7725__i < G__7725__a.length) {G__7725__a[G__7725__i] = arguments[G__7725__i + 0]; ++G__7725__i;}
  _ = new cljs.core.IndexedSeq(G__7725__a,0);
} 
return G__7724__delegate.call(this,_);};
G__7724.cljs$lang$maxFixedArity = 0;
G__7724.cljs$lang$applyTo = (function (arglist__7726){
var _ = cljs.core.seq(arglist__7726);
return G__7724__delegate(_);
});
G__7724.cljs$core$IFn$_invoke$arity$variadic = G__7724__delegate;
return G__7724;
})()
;})(js_files,map__7722,map__7722__$1,on_jsload))
,((function (js_files,map__7722,map__7722__$1,on_jsload){
return (function (e){
return console.error("Load failed:",e.message);
});})(js_files,map__7722,map__7722__$1,on_jsload))
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
var html_path = (cljs.core.truth_(adzerk.boot_reload.reload.ends_with_QMARK_.call(null,page_path,"/"))?[cljs.core.str(page_path),cljs.core.str("index.html")].join(''):page_path);
if(cljs.core.truth_(adzerk.boot_reload.reload.changed_uri.call(null,html_path,changed))){
return adzerk.boot_reload.reload.reload_page_BANG_.call(null);
} else {
return null;
}
});
adzerk.boot_reload.reload.group_log = (function adzerk$boot_reload$reload$group_log(title,things_to_log){
console.groupCollapsed(title);

var seq__7731_7735 = cljs.core.seq.call(null,things_to_log);
var chunk__7732_7736 = null;
var count__7733_7737 = (0);
var i__7734_7738 = (0);
while(true){
if((i__7734_7738 < count__7733_7737)){
var t_7739 = cljs.core._nth.call(null,chunk__7732_7736,i__7734_7738);
console.log(t_7739);

var G__7740 = seq__7731_7735;
var G__7741 = chunk__7732_7736;
var G__7742 = count__7733_7737;
var G__7743 = (i__7734_7738 + (1));
seq__7731_7735 = G__7740;
chunk__7732_7736 = G__7741;
count__7733_7737 = G__7742;
i__7734_7738 = G__7743;
continue;
} else {
var temp__4425__auto___7744 = cljs.core.seq.call(null,seq__7731_7735);
if(temp__4425__auto___7744){
var seq__7731_7745__$1 = temp__4425__auto___7744;
if(cljs.core.chunked_seq_QMARK_.call(null,seq__7731_7745__$1)){
var c__7225__auto___7746 = cljs.core.chunk_first.call(null,seq__7731_7745__$1);
var G__7747 = cljs.core.chunk_rest.call(null,seq__7731_7745__$1);
var G__7748 = c__7225__auto___7746;
var G__7749 = cljs.core.count.call(null,c__7225__auto___7746);
var G__7750 = (0);
seq__7731_7735 = G__7747;
chunk__7732_7736 = G__7748;
count__7733_7737 = G__7749;
i__7734_7738 = G__7750;
continue;
} else {
var t_7751 = cljs.core.first.call(null,seq__7731_7745__$1);
console.log(t_7751);

var G__7752 = cljs.core.next.call(null,seq__7731_7745__$1);
var G__7753 = null;
var G__7754 = (0);
var G__7755 = (0);
seq__7731_7735 = G__7752;
chunk__7732_7736 = G__7753;
count__7733_7737 = G__7754;
i__7734_7738 = G__7755;
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
var changed_STAR_ = cljs.core.map.call(null,(function (p1__7756_SHARP_){
return [cljs.core.str(new cljs.core.Keyword(null,"asset-host","asset-host",-899289050).cljs$core$IFn$_invoke$arity$1(opts)),cljs.core.str(p1__7756_SHARP_)].join('');
}),cljs.core.map.call(null,(function (p__7762){
var map__7763 = p__7762;
var map__7763__$1 = ((((!((map__7763 == null)))?((((map__7763.cljs$lang$protocol_mask$partition0$ & (64))) || (map__7763.cljs$core$ISeq$))?true:false):false))?cljs.core.apply.call(null,cljs.core.hash_map,map__7763):map__7763);
var canonical_path = cljs.core.get.call(null,map__7763__$1,new cljs.core.Keyword(null,"canonical-path","canonical-path",-1891747854));
var web_path = cljs.core.get.call(null,map__7763__$1,new cljs.core.Keyword(null,"web-path","web-path",594590673));
if(cljs.core._EQ_.call(null,"file:",(function (){var G__7765 = window;
var G__7765__$1 = (((G__7765 == null))?null:G__7765.location);
var G__7765__$2 = (((G__7765__$1 == null))?null:G__7765__$1.protocol);
return G__7765__$2;
})())){
return canonical_path;
} else {
return web_path;
}
}),changed));
adzerk.boot_reload.reload.group_log.call(null,"Reload",changed_STAR_);

adzerk.boot_reload.reload.reload_js.call(null,changed_STAR_,opts);

if(cljs.core.truth_(adzerk.boot_reload.reload.has_dom_QMARK_.call(null))){
var G__7766 = changed_STAR_;
adzerk.boot_reload.reload.reload_html.call(null,G__7766);

adzerk.boot_reload.reload.reload_css.call(null,G__7766);

adzerk.boot_reload.reload.reload_img.call(null,G__7766);

return G__7766;
} else {
return null;
}
});

//# sourceMappingURL=reload.js.map