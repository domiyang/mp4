# mp4 - a webapp that can stream your own videos in responsive way

## description
This is a simple webapp that can stream your videos and provide the responsive webpage for different divices (desktop/tablet/mobile etc.).

## features overview
1. simple webapp that can support responsive (for different devices, layout) video play
2. providing online media server for your own vidoes in your local server
3. can play all videos in a single click (Play_All)
4. can play any item you clicked (then it'll auto continue to next video)
5. all files, including the sub directories under the mp4/video will be listed
6. support utf-8 vidoe files naming
7. access control with basic username/pass defined from container (e.g.: conf/tomcat-users.xml with role mp4)

## simple jar version
* this branch is for war version, refer to branch [mp4-springboot](https://github.com/domiyang/mp4/tree/mp4-springboot) for the springboot jar version

## how to use
0. make sure to backup the mp4/video folder to some other path outside the webapps directory if already existing
1. simply drop the mp4.war into a servlet container (e.g.: tomcat/jboss, etc)
2. drop/restore your videos into the mp4/video folder in the container
3. access from http://host:port/mp4

## supported video types
* mp4,mov,mp3,m4v

## screenshots
* refer to https://github.com/domiyang/mp4/wiki
