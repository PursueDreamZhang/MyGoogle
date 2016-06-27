package com.huida.googleplayfinal.http;

public interface Url {
	//服务器的主机地址
	String SERVER_HOST = "http://127.0.0.1:8090/";
	//图片的url前缀
	String IMAGE_PREFIX = SERVER_HOST +"image?name=";
	//home页接口地址
	String Home = SERVER_HOST + "home?index=";
	//app页接口地址
	String App = SERVER_HOST + "app?index=";
	//Game页接口地址
	String Game = SERVER_HOST + "game?index=";
	//subject页接口地址
	String Subject = SERVER_HOST + "subject?index=";
	//Recommend页接口地址
	String Recommend = SERVER_HOST + "recommend?index=0";
	//Category页接口地址
	String Category = SERVER_HOST + "category?index=0";
	//Hot页接口地址
	String Hot = SERVER_HOST + "hot?index=0";
	//Detail页接口地址
	String Detail = SERVER_HOST + "detail?packageName=%1$s";//&index=%2$d
	//从头下载的接口地址
	String Download = SERVER_HOST + "download?name=%1$s";//&index=%2$d
	//断点下载的接口地址
	String Break_Download = SERVER_HOST + "download?name=%1$s&range=%2$d";//&index=%2$d
}
