/*!
- Description: 	The mp4 js modules.
- Date:			22-Aug-2018
- Author: 		Domi Yang (domi_yang@hotmail.com)
 */
//populate the menu
  	function loadMenu(){
		$(document).ready(function() {
		    $.ajax({
	             type: "GET",
	             dataType: "html",
	             url: "/medias/links",
	             success: function(data){
	            	$('#medialinks1').html(data);
	                $('#medialinks2').html(data);
	                console.log("load menu done.");
	             },
	             error: function(msg, url, line){
	            	 console.log("failed load medialinks, msg=" + msg);
	             }
	         });
		});
  	}
	
	// play this video
	function view(url, fileName, idx, totalCount) { 
		var video = document.getElementById("video"); 
		var title = document.getElementById("title"); 
		title.innerHTML = '<div id="title"><input id="current" type="text" hidden="true" value="' + idx + '"><h3 title="' + (idx + 1) + '">selected video: ' + fileName + '</h3></div>';
		
		setPlayAllMenuDisplay(idx, totalCount);
		
		video.src = url;
        video.play();
	}

	// set the displaying for the play all menu.
	function setPlayAllMenuDisplay(idx, totalCount){
		var playAllMenu = document.getElementById("playAllMenu"); 
		var playAllMenuDisplay = 'Play-All-[' + (idx + 1) + '/' + totalCount + ']';
		playAllMenu.title = playAllMenuDisplay;
		playAllMenu.value = playAllMenuDisplay;
	}
	
	// reload all videos from media path
	function reloadMedia() { 	
		$(document).ready(function() {
		    $.ajax({
	             type: "GET",
	             dataType: "html",
	             url: "/medias/reload",
	             success: function(data){
	                loadMenu();
	                console.log("done reload medias data=" + data);
	             },
	             error: function(msg, url, line){
	            	 console.log("failed to reload the playlist, msg=" + msg);
	             }
	         });
		});

	}
	
	// play all videos
	function viewAll() { 	
		
		$(document).ready(function() {
		    $.ajax({
	             type: "GET",
	             dataType: "json",
	             url: "/medias",
	             success: function(data){
	                viewAllHandler(data);
	             },
	             error: function(msg, url, line){
	            	 console.log("failed load playlist, msg=" + msg);
	             }
	         });
		});

	}
	
	// view all handler
	function viewAllHandler(playListObj){
		var playList = playListObj.playList;
		var totalCount = playList.length;
		
		var i = 0;
		var url = playList[i].url;
		var fileName = playList[i].fileName;
		
		setPlayAllMenuDisplay(i, totalCount);
		
		view(url, fileName, i, totalCount);
		
		var video = document.getElementById("video");		
		// handle the event when video ended
		video.onended = function(){
			// the current playing index
			var current = document.getElementById("current");
			
			//parse 10 base int
			var idxNext = parseInt(current.value, 10) + 1;
			
			if(i < playList.length){
				url = playList[idxNext].url;
				fileName = playList[idxNext].fileName;			
				
				setPlayAllMenuDisplay(idxNext, totalCount);								
				
				view(url, fileName, idxNext, totalCount);
			}
		};
	}
