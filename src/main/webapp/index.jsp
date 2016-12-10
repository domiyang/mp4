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

<%@page import="java.io.File"%>
<%@page import="java.io.FilenameFilter"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Arrays"%>

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

    <title>The mp4 site</title>
    <!-- Bootstrap -->
    <link href="css/bootstrap.min.css" rel="stylesheet" />
    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
      <script src="js/html5shiv.min.js"></script>
      <script src="js/respond.min.js"></script>
    <![endif]-->

  <script> 
	function view(url, fid) { 
		var video = document.getElementById("video"); 
		var title = document.getElementById("title"); 
		title.innerHTML='<div id="title"><h4>' + fid + '</h4></div>';
		video.src=url;
        video.play(); 
	}
  </script>

	<%! 
	boolean isStringEndWithListOfString(String targetStr, List<String> lookupStr){
		for (String lookup : lookupStr){
			if(targetStr.toLowerCase().endsWith(lookup.toLowerCase())){
				return true;
			}
		}
		
		return false;
	}
	
	File[] getListOfFiles(String folder, final List<String> fileSuffixList){
		ServletContext app = getServletContext();
		String filePath = app.getRealPath("/") + folder;
		File file = new File(filePath);
		return file.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				if (isStringEndWithListOfString(name, fileSuffixList)) {
					return true;
				} else {
					return false;
				}
			}
		});
	}

	String getListOfFilesLink(){
		StringBuffer sbf = new StringBuffer();
		String folder = "video";
		File[] fl = getListOfFiles(folder, Arrays.asList(new String[] {".mp4",".mov"}));
		for(int i = 0; i < fl.length; i++){
			File f = fl[i];
			String url = folder + "/" + f.getName();
			sbf.append("<a href=\"#\" onclick=\"view('" + url + "', '" + i + "');\">" + i + "</a>");
			sbf.append("&nbsp;|&nbsp;");
		}
		return sbf.toString();
	}
	
	%>
    
  </head>
  <body>
  <h1>Hi there!</h1>
  <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
  <script src="js/jquery.min.js"></script> 
  <!-- Include all compiled plugins (below), or include individual files as needed -->
   
  <script src="js/bootstrap.min.js"></script>
  <div class="container">
    <h1>Video List</h1>
    <p>Click to watch.</p>	
	<!--row for link menu top-->
    <div class="row">
		<div class="col-sm-12" style="background-color:lavenderblush;">
		<%=getListOfFilesLink()%>
		</div>
	</div>
	<br />
	
	<!--row for the video display convas-->
	<div class="row">
	<div class="col-sm-12" style="background-color:lavender;">
		<div id="title"></div>
        <div align="center" class="embed-responsive embed-responsive-16by9">
          <video id="video" controls class="embed-responsive-item">
            <source src="" type="video/mp4" />
          </video>
        </div>
	</div>
    </div>
    <br />

	<!--row for link menu buttom-->
    <div class="row">
		<div class="col-sm-12" style="background-color:lavenderblush;">
		<%=getListOfFilesLink()%>
		</div>
	</div>
	<br />

	</div>
  </body>
</html>
