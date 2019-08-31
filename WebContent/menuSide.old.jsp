<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
<script src="modernizr-2.6.1.min.js"></script>
<script src="respond.js"></script>
<script src="highlight.min.js"></script>
<script src="/js/jquery.jpanelmenu.min.js"></script>
<script src="plugins.js"></script>

<style>
#jPanelMenu-menu {
background: #3b3b3b
}
#jPanelMenu-menu ul {
border-bottom: 1px solid #484848
}
#jPanelMenu-menu li a {
background: #3b3b3b;
background: -o-linear-gradient(top, #3e3e3e, #383838);
background: -ms-linear-gradient(top, #3e3e3e, #383838);
background: -moz-linear-gradient(top, #3e3e3e, #383838);
background: -webkit-gradient(linear, left top, left bottom, color-stop(0, #3e3e3e), color-stop(1, #383838));
background: -webkit-linear-gradient(#3e3e3e, #383838);
background: linear-gradient(top, #3e3e3e, #383838);
font-family: "museo-sans", "Museo Sans", "Helvetica Neue", Helvetica, Arial, sans-serif;
font-weight: 300;
font-weight: 700;
display: block;
padding: 0.5em 5%;
border-top: 1px solid #484848;
border-bottom: 1px solid #2e2e2e;
text-decoration: none;
text-shadow: 0 -1px 2px #222;
color: #f7f7f7
}
#jPanelMenu-menu li a:hover, #jPanelMenu-menu li a:focus {
background: #404040;
background: -o-linear-gradient(top, #484848, #383838);
background: -ms-linear-gradient(top, #484848, #383838);
background: -moz-linear-gradient(top, #484848, #383838);
background: -webkit-gradient(linear, left top, left bottom, color-stop(0, #484848), color-stop(1, #383838));
background: -webkit-linear-gradient(#484848, #383838);
background: linear-gradient(top, #484848, #383838)
}
#jPanelMenu-menu li a:active {
background: #363636;
background: -o-linear-gradient(top, #3e3e3e, #2e2e2e);
background: -ms-linear-gradient(top, #3e3e3e, #2e2e2e);
background: -moz-linear-gradient(top, #3e3e3e, #2e2e2e);
background: -webkit-gradient(linear, left top, left bottom, color-stop(0, #3e3e3e), color-stop(1, #2e2e2e));
background: -webkit-linear-gradient(#3e3e3e, #2e2e2e);
background: linear-gradient(top, #3e3e3e, #2e2e2e);
-moz-box-shadow: 0 2px 7px #222 inset;
-webkit-box-shadow: 0 2px 7px #222 inset;
box-shadow: 0 2px 7px #222 inset;
border-top-color: #222;
padding-top: 0.55em;
padding-bottom: 0.45em
}
.jPanelMenu-panel {
-moz-box-shadow: 0 0 25px #222;
-webkit-box-shadow: 0 0 25px #222;
box-shadow: 0 0 25px #222
}
</style>

<a href="#menu">&#xED50;</a>
<nav>
<ul>
<li><a href="#menu1">Menu 1</a></li>
<li><a href="#menu2">Menu 1</a></li>
<li><a href="#menu3">Menu 1</a></li>
...
</ul>
</nav>

<script>
var jPanelMenu = {};
$(function() {
	$('pre').each(function(i, e) {hljs.highlightBlock(e)});
	
	jPanelMenu = $.jPanelMenu({
		menu: 'header.main nav',
		animated: false
	});
	jPanelMenu.on();

	$(document).on('click',jPanelMenu.menu + ' li a',function(e){
		if ( jPanelMenu.isOpen() && $(e.target).attr('href').substring(0,1) == '#' ) { jPanelMenu.close(); }
	});

	$(document).on('click','#trigger-off',function(e){
		jPanelMenu.off();
		$('html').css('padding-top','40px');
		$('#trigger-on').remove();
		$('body').append('<a href="" title="Re-Enable jPanelMenu" id="trigger-on">Re-Enable jPanelMenu</a>');
		e.preventDefault();
	});

	$(document).on('click','#trigger-on',function(e){
		jPanelMenu.on();
		$('html').css('padding-top',0);
		$('#trigger-on').remove();
		e.preventDefault();
	});
});
</script>
