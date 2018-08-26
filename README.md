# mp4 - a webapp that can stream your own videos in responsive way

## description
This is a simple webapp that can stream your videos and provide the responsive webpage for different divices (desktop/tablet/mobile etc.).

## features overview
1. simple webapp that can support responsive (for different devices, layout) video play
2. providing online media server for your own vidoes streaming
3. can play all videos in a single click (Play-All[x/y])
4. can play any item you clicked (then it'll auto continue to next video)
5. all files, including the all sub directories under you specified media path will be listed
6. support utf-8 vidoe files naming
7. access control with basic username/pass as you specified
8. support manually reload medias after media path data changes ([Reload-Media])
9. add loout support via ([Logout-xxx])

## how to use
0. select the media path to be stream, the port number (optional default as 8080) and username/pass (optional default as mp4/mp4pass) to be used for access control
1. get the java runtime environment (JRE 1.8+) if not already have that
2. run with java command as:
	* simple way: java -Dmp4.media.path=/path/to/video/folder -jar mp4.jar
	* full options: java -Dmp4.media.path=/path/to/video/folder -Dmp4.media.types=mp4,mov,mp3,m4v -Dmp4.server.port=8080 -Dmp4.user=mp4 -Dmp4.pass=mp4pass -jar mp4.jar
3. access http://localhost:8080 from browser

## supported video types
* mp4,mov,mp3,m4v

## screenshots
* refer to https://github.com/domiyang/mp4/wiki

# options (java command line arguments) available
* -Dmp4.media.path=/path/to/video/folder 	=> mandatory to specify the media path
* -Dmp4.media.types=mp4,mov,mp3,m4v 		=> optional to specify the accepted media types (default as mp4,mov,mp3,m4v)
* -Dmp4.server.port=8080 					=> optional to specify the server listening port (default as 8080)
* -Dmp4.user=mp4 							=> optional to specify the access user (default as mp4)
* -Dmp4.pass=mp4pass						=> optional to specify the access user pass (default as mp4pass)
