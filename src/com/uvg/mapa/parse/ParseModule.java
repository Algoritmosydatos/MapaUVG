package com.uvg.mapa.parse;

import com.parse.Parse;
import com.parse.ParseAnalytics;

public class ParseModule{
	
	public void onCreate(){
		Parse.initialize(this, "App Id" , "Client Key");
	}
	
	public void SingUp(String name, String password, String email){
		ParseUser user = new ParseUser();
		user.setUsername(name);
		user.setPassword(password);
		user.setEmail(email);
		
		//Agregar campos extra en esta sección
		
		//
		
		user.singUpInBackground(new SingUpCallback(){
			public void done(PaseException e){
				if (e==null){
					
				}
				else{
					
				}
			}
		});
	}
	
	public void logIn(String name, String password){
		ParseUser.logInInBackground(name, password, new LogInCallback(){
			public void done(ParseUser user, ParseException e){
				if (user != null){
					//succesfull login
					usuario=user;
				}
				else{
					//login failed
					//necesaio revisa la excepción para ver porque no se pudo
				}
			}
		});
	}
	
	public void sendImage(String tag, int x, int y, int posx, int posy, byte[] pic){
		ParseFile file = new ParseFile("drawing.png",pic);
		ParseObject image = new ParseObject("Image");
		image.put("user",ParseUser.getCurrentUser());
		image.put("tag",tag);
		image.put("width", x);
		image.put("height", y);
		image.put("positionX", posx);
		image.put("positionY", posy);
		image.put("picture", file);
		
		image.saveInBackgound();
	}
	
	public void sendPost(String tag, int posx, int posy, String text){
		ParseObject post = new ParseObject("Post");
		post.put("user",ParseUser.getCurrentUser());
		post.put("positionX", posx);
		post.put("positionY", posy);
		post.put("text",text);
		
		post.saveInBackground();
	}
	
	public List<ParseObject> getTagImages(String tag){
		List<ParseObject> pictures;
		ParseQuery<ParseObject> que = ParseQuery.getQuery("Image");
		que.whereEqualTo("tag",tag);
		que.findInBackground(new findCallBack<ParseObject>(){
			public void done(List<ParseObject> imagenes, ParseException e){
				if(e==null){
					pictures=imagenes;
				}
				else{
					//error in query
				}
			}
		});
		return pictures;
	}
	
	public List<ParseObject> getTagPosts(String tag){
		List<ParseObject> posts;
		ParseQuery<ParseObject> que = ParseQuery.getQuery("Post");
		que.whereEqualTo("tag",tag);
		que.findInBackground(new findCallBack<ParseObject>(){
			public void done(List<ParseObject> publicaciones, ParseException e){
				if(e==null){
					posts=publicaciones;
				}
				else{
					//error in query
				}
			}
		});
		return posts;
	}
	
	public List<ParseObject> imagesUserFiltered(String user, String tag){
		List<ParseObject> images;
		ParseQuery<> userQuery = ParseQuery.getQuery(ParseUser);
		userQuery.whereEqualTo("username", user);
		ParseQuery<> imageQuery = ParseQuery.getQuery("Image");
		imageQuery.whereMatchesQuery("user",userQuery);
		imageQuery.whereEqualTo("tag", tag);
		imageQuery.findInBackground(new findCallBack<ParseObject>(){
			public void done(List<ParseObject> pictures, ParseException e){
				if (e==null){
					images=pictures;
				}
				else{
					//error in query
				}
			}
		});
		return images;
	}
	
	public List<ParseObject> postsUserFiltered(String user, String tag){
		List<ParseObject> posts;
		ParseQuery<> userQuery = ParseQuery.getQuery(ParseUser);
		userQuery.whereEqualTo("username", user);
		ParseQuery<> imageQuery = ParseQuery.getQuery("Post");
		imageQuery.whereMatchesQuery("user",userQuery);
		imageQuery.whereEqualTo("tag", tag);
		imageQuery.findInBackground(new findCallBack<ParseObject>(){
			public void done(List<ParseObject> publicaciones, ParseException e){
				if (e==null){
					posts=publicaciones;
				}
				else{
					//error in query
				}
			}
		});
		return posts;
	}
	
	
}