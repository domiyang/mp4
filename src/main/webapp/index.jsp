<%--
- Description: 	The responsive webapp for videos
- Date:			10-Dec-2016
- Author: 		Domi Yang (domi_yang@hotmail.com)
---------------------------------------------------------------------------
   Copyright 2016 Domi Yang (domi_yang@hotmail.com)

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
---------------------------------------------------------------------------
--%>

<%@ page pageEncoding="utf-8"%>
<%@page import="java.io.File"%>
<%@page import="java.io.FilenameFilter"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Arrays"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>

<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
	
	<!-- no caching at client -->
	<meta http-equiv="Cache-Control" content="no-cache, no-store, must-revalidate" />
	<meta http-equiv="Pragma" content="no-cache" />
	<meta http-equiv="Expires" content="0" />	

    <title>the mp4 site</title>
    <!-- Bootstrap -->
    <link href="css/bootstrap.min.css" rel="stylesheet" />
    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
      <script src="js/html5shiv.min.js"></script>
      <script src="js/respond.min.js"></script>
    <![endif]-->

  <script> 
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
	
	// play all videos
	function viewAll() { 		
		var playListJson='<%=getPlayListJson()%>';
		var playListObj = JSON.parse(playListJson);
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
  </script>

	<%!
	// check if targetStr end with list of lookupStr (case insensitive).
	boolean isStringEndWithListOfString(String targetStr, List<String> lookupStr){
		for (String lookup : lookupStr){
			if(targetStr.toLowerCase().endsWith(lookup.toLowerCase())){
				return true;
			}
		}
		
		return false;
	}

	List<File> getListOfFilesForDir(String dirPath, final List<String> fileSuffixList){
		List<File> list = new ArrayList<File>();
		File file = new File(dirPath);
		for (File f : file.listFiles()){
			if (f.isDirectory()){
				list.addAll(getListOfFilesForDir(f.getAbsolutePath(), fileSuffixList));
			}else{
				if (isStringEndWithListOfString(f.getName(), fileSuffixList)) {
					list.add(f);
				} else {
					// skipping f
				}
			}
		}
		return list;
	}
	
	// get list of files started from the folder (including files in all sub folders) in the app root and get files when suffix in the fileSuffixList.
	List<File> getListOfFiles(String folder, final List<String> fileSuffixList){
		ServletContext app = getServletContext();
		String filePath = app.getRealPath("/") + folder;
		
		return getListOfFilesForDir(filePath, fileSuffixList);
	}
	
	// to get the play list json string for all videos.
	String getPlayListJson(){
		return processVideoList().get("playListJson");
	}
	
	// to get the video link menu.	
	String getListOfFilesLink(){
		return processVideoList().get("videoLinkMenu");
	}

	// process the list of videos
	Map<String, String> processVideoList(){
		Map<String, String> vMap = new HashMap<String, String>();
		StringBuffer sbf = new StringBuffer();
		StringBuffer sbfMenu = new StringBuffer();
		
		// the start folder from webapp root dir
		String folder = "video";
		List<File> fl = getListOfFiles(folder, Arrays.asList(new String[] {".mp4",".mov",".mp3",".m4v"}));
		
		int videoCount = fl.size();
		String playAllMenu = "Play-All-[" + videoCount + "]";
		// add the play_all link at the begining
		sbfMenu.append("<input id=\"playAllMenu\" type=\"button\" title=\"" + playAllMenu + "\" class=\"btn-link\" href=\"#\" onclick=\"viewAll();\" value=\"" + playAllMenu + "\">");
		
		ServletContext app = getServletContext();
		String appPath = app.getRealPath("/");
		
		// to populate list of url/names for playlist
		sbf.append("{\"playList\":[");
		for(int i = 0; i < videoCount; i++){
			File f = fl.get(i);
			String filePath = f.getAbsolutePath();
			String fileName = f.getName();
			// remove the appPath, replace the \ to /
			String url = filePath.replace(appPath, "").replace("\\","/");
			
			// for link menu
			sbfMenu.append("<input type=\"button\" title=\"" + (i + 1) + "\" class=\"btn-link\" href=\"#\" onclick=\"view('" + url + "', '" + fileName + "'," + i + "," + videoCount + ");\" value=\"" + fileName + "\">");
			
			//for playListJson
			//append the , for 2nd+ entries
			if(!sbf.toString().endsWith("[")){
				sbf.append(",");
			}
			sbf.append("{\"url\":\"" + url + "\",\"fileName\":\"" + fileName + "\"}");
		}
		sbf.append("]}");
				
		vMap.put("playListJson",sbf.toString());
		vMap.put("videoLinkMenu",sbfMenu.toString());
		
		return vMap;
	}
	
	%>
    
  </head>
  <body>
  <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
  <script src="js/jquery.min.js"></script> 
  <!-- Include all compiled plugins (below), or include individual files as needed -->
   
  <script src="js/bootstrap.min.js"></script>
  <div class="container">
	<!--the header root-->
  	<div class="row">
		<div class="jumbotron">
			<h1>hi there, welcome to the mp4 site!</h1>
			<p>the mp4 site is a simple webapp that can stream your own videos in responsive way.</p>
		</div>
	</div>
	<div class="page-header">
		<h1>all videos</h1>
	</div>
	
	<!--row for link menu top-->
    <div class="row">
		<div class="panel panel-default">
			<div class="panel-heading">
				<h3>click any item to play, or click Play-All-[xx] to play all videos.</h3>
			</div>
			<div class="panel-body">
				<%=getListOfFilesLink()%>
			</div>
		</div>		
	</div>
	
	<!--row for the video display convas-->
	<div class="row">
		<div class="panel panel-default">
			<div class="panel-heading">
				<div id="title">
					<h3>select video to play</h3>
				</div>
			</div>
			<div class="panel-body">
				<div align="center" class="embed-responsive embed-responsive-16by9">
				  <video id="video" controls class="embed-responsive-item">
					<source src="" type="video/mp4" />
				  </video>
				</div>
			</div>
		</div>	
    </div>

	<!--row for link menu buttom-->
    <div class="row">
		<div class="panel panel-default">
			<div class="panel-heading">
				<h3>click any item to play, or click Play-All-[xx] to play all videos.</h3>
			</div>
			<div class="panel-body">
				<%=getListOfFilesLink()%>
			</div>
		</div>
	</div>
	<footer class="footer">
		<p>© 2016 dmb</p>
    </footer>
  </div>
  </body>
</html>
