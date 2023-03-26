package com.jspider.multiplayer;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.rmi.server.Operation;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Properties;
import java.util.Scanner;



 import java.util.Random;

public class MultiplayerOperation {
	
	
	private static Connection connection;
	private static PreparedStatement preparedStatement;
	private static FileReader fileReader;
	private static Properties properties;
	private static ResultSet resultSet;
	private static String query;
	private static int result;
	private static String filepath=
			"D:\\WEJA1\\multiplayer\\resources\\dbinfo.properties";
	public static void openConnection() {
		try {
			fileReader=new FileReader(filepath);
			properties=new Properties();
			properties.load(fileReader);
			Class.forName(properties.getProperty("driverpath"));
			connection=DriverManager.getConnection(properties.getProperty("dburl"),properties);
		} catch (FileNotFoundException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	
	public static void closeConnection() {
		
		
		try {
			if(connection !=null) {
			connection.close();
			}
			if(preparedStatement !=null)
			{
				preparedStatement.close();
			}
			if(fileReader !=null)
			{
				fileReader.close();
			}
			if(preparedStatement !=null)
			{
				preparedStatement.close();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	
	}
	public static void removeSong() {
		  Scanner sc=new Scanner(System.in);
		System.out.println("if you want to delete entire song then press 1\n if not then press 2");
		  int delete=sc.nextInt();
		  if(delete==1) {
			  openConnection();
			  
			  try {
				 String query="truncate table song_table";
				 preparedStatement =connection.prepareStatement(query);
				preparedStatement.executeUpdate();
				 System.out.println("all songs are deleted");
				 closeConnection();
			  }catch(SQLException e) {
				  e.printStackTrace();
			  }
			  
		  }else {
			  boolean loop2=true;
			  playAllSongs();
			  while (loop2) {
				  openConnection();
				  
				  String query="delete from song_table where id=?";
				try {
					preparedStatement =connection.prepareStatement(query);
					System.out.println("enter the song no");
					delete=sc.nextInt();
					preparedStatement.setInt(1, delete);
					preparedStatement.executeUpdate();
					System.out.println("the song no "+delete+"delete succesfully");
					closeConnection();
					System.out.println("Press 0 to exist");
					delete=sc.nextInt();
					if(delete==0) {
						loop2=false;
					}
				}catch(SQLException e)
				{
					e.printStackTrace();
				}
				  
			}
		  }
	}

	public static void addSong() {
	  boolean loop=true;
	  while(loop) {
		  Scanner sc=new Scanner(System.in);
		  openConnection();
		  query="insert into song_table values(?,?,?,?,?)";
		  
		  try {
			  preparedStatement=connection.prepareStatement(query);
			  
		  }catch(SQLException e) {
			// TODO: handle exception
			  e.printStackTrace();
		}
		
		  
		  
		  Song song=new Song();
			System.out.println("Enter the id of song");
			 song.setId(sc.nextInt());
			 sc.nextLine();
			
			System.out.println("Enter the song name");
			  song.setName(sc.nextLine());
			
			System.out.println("Enter the song duration");
			song.setDuration(sc.nextDouble());
			  sc.nextLine();
			System.out.println("Enter the song Movie or album name");
			 song.setSinger(sc.nextLine());
			System.out.println("Enter the song singer");
			 song.setAlbum(sc.nextLine());
		 
		
		 
		
		
		 
	
		 
			
		try {
			  preparedStatement.setInt(1,song.getId());
			  preparedStatement.setString(2,song.getName());
			  preparedStatement.setDouble(3, song.getDuration());
			  preparedStatement.setString(4, song.getAlbum());
			  preparedStatement.setString(5, song.getSinger());
			  preparedStatement.executeUpdate();
			  System.out.println("song added succesfully");
				System.out.println("============================");
				System.out.println("enter 1 to add another song \nenter 0 to exit");
				if(sc.nextInt()==1)
				{
					loop=true;
				}
				else {
					loop=false;
				}
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		closeConnection();
	  }
		
	
		

	}

	public static void playRandomSong() {
		
		openConnection();

		try {
			
			//retrieve all ids from database
			query="select id from song_table";
			System.out.println("query pass");
			preparedStatement=connection.prepareStatement(query);
		   resultSet=preparedStatement.executeQuery();
		   //store the song IDS in an array
		   List<Integer>songIds=new ArrayList<>();
		   System.out.println("List pass");
		   while(resultSet.next()) {
			   songIds.add(resultSet.getInt("id"));
			
		}
		   //Select a random song Ids in an array
			System.out.println("Playing Random Song");
			Random random = new Random();
			int count=songIds.size();
			int upperbond =count;
			int int_random = songIds.get(random.nextInt(songIds.size()));
			query="select *from song_table where id=?";
			 preparedStatement = connection.prepareStatement(query);
			preparedStatement.setInt(1, int_random);
		    resultSet=preparedStatement.executeQuery();
				while(resultSet.next()) {
					System.out.println(resultSet.getInt(1)+"|| "+
							resultSet.getString(2)+"||"
										+resultSet.getDouble(3)+"||"
							+resultSet.getString(4)+"||"+resultSet.getString(5));
		
				}
			
			count=0;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
		closeConnection();
		
	}

	public static void playAllSongs() {
		openConnection();
		
		try {
			query="select * from song_table ";
			preparedStatement=connection.prepareStatement(query);
			resultSet=preparedStatement.executeQuery();
			while(resultSet.next())
			{
				
				Song song =new Song();
				song.setId(resultSet.getInt(1));
				song.setName(resultSet.getString(2));
				song.setDuration(resultSet.getDouble(3));
				song.setAlbum(resultSet.getString(4));
				song.setSinger(resultSet.getString(5));
				System.out.println(song);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		closeConnection();
	}

	public static void chooseSong() {
	/*	ListIterator lit2 = songList.listIterator();
		while (lit2.hasNext()) {
		System.out.println(lit2.next());
		}
		System.out.println("Choise song by Id");
		Scanner sc1 = new Scanner(System.in);
		int a = sc1.nextInt();

		for (int i = 0; i <= songList.size(); i++) {
			if (a == i) {
				System.out.println(songList.get(i - 1));
				break;
			}
		}*/
		
		openConnection();
		
		
		try {
			query="Select * "+"from song_table "+"where id=? ";
			preparedStatement=connection.prepareStatement(query);
			System.out.println("Choise song by Id");
			Scanner sc1 = new Scanner(System.in);
			int a = sc1.nextInt();
			preparedStatement.setInt(1, a);
			resultSet=preparedStatement.executeQuery();
			while (resultSet.next()) {
				
				if(resultSet.getInt(1)==a) {
				System.out.println(resultSet.getInt(1)+"|| "+
			resultSet.getString(2)+"||"
						+resultSet.getDouble(3)+"||"
			+resultSet.getString(4)+"||"+resultSet.getString(5));
				}else if(resultSet.next()==false){
					System.out.println("Enter valid Id");
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		closeConnection();
		
	}

	public static void changeSongName() {
		Scanner sc = new Scanner(System.in);
		playAllSongs();
		System.out.println("Select the song Id which you want to change");
		int change=sc.nextInt();
		
		System.out.println("Give the alter song name");
		String changeName= sc.next();
		try {
			openConnection();
			String query="UPDATE song_table"
					+" SET name=?"
					+" WHERE id=?" ;
			
			preparedStatement=connection.prepareStatement(query);
			preparedStatement.setString(1,changeName);
			preparedStatement.setInt(2, change);
		int result=	preparedStatement.executeUpdate();
		System.out.println(result+"row affected");
		}catch (SQLException e) {
	          e.printStackTrace();
		}
		
		closeConnection();
		

	}

	public static void changeSingerName() {
		Scanner sc = new Scanner(System.in);
		playAllSongs();
		System.out.println("Select the song Id which you want to change");
		int change=sc.nextInt();
		
		System.out.println("Give the alter singer name");
		String changeName= sc.next();
		try {
			openConnection();
			String query="UPDATE song_table"
					+" SET singer=?"
					+" WHERE id=?" ;
			
			preparedStatement=connection.prepareStatement(query);
			preparedStatement.setString(1,changeName);
			preparedStatement.setInt(2, change);
		int result=	preparedStatement.executeUpdate();
		System.out.println(result+"row affected");
		}catch (SQLException e) {
	          e.printStackTrace();
		}
		
		closeConnection();
		
	}

	public static void changeSongDuration() {
		Scanner sc = new Scanner(System.in);
		playAllSongs();
		System.out.println("Select the song Id which you want to change");
		int change=sc.nextInt();
		
		System.out.println("Give the alter song duration");
		double duration= sc.nextDouble();
		try {
			openConnection();
			String query="UPDATE song_table"
					+" SET duration=?"
					+" WHERE id=?" ;
			
			preparedStatement=connection.prepareStatement(query);
			preparedStatement.setDouble(1,duration);
			preparedStatement.setInt(2, change);
		int result=	preparedStatement.executeUpdate();
		System.out.println(result+"row affected");
		}catch (SQLException e) {
	          e.printStackTrace();
		}
		
		closeConnection();

	}

	public static void changeMovieAlbumName() {
		Scanner sc = new Scanner(System.in);
		playAllSongs();
		System.out.println("Select the song Id which you want to change");
		int change=sc.nextInt();
		sc.nextLine();
		System.out.println("Give the alter Movie or Album Name");
		String changeName= sc.nextLine();
		try {
			openConnection();
			String query="UPDATE song_table"
					+" SET album_movie=?"
					+" WHERE id=?" ;
			
			preparedStatement=connection.prepareStatement(query);
			preparedStatement.setString(1,changeName);
			preparedStatement.setInt(2, change);
		int result=	preparedStatement.executeUpdate();
		System.out.println(result+"row affected");
		}catch (SQLException e) {
	          e.printStackTrace();
		}
		
		closeConnection();
	}

	static ArrayList<Song> songList = new ArrayList<Song>();

	public static void menu() {
		int choice;

		Scanner sc = new Scanner(System.in);
		boolean loop = true;

		while (loop) {
			System.out
					.println("MENU \n 1)Play Song \n 2)Add/Remove Song \t \n " + "3)Update Song \t \n 4)Exist  \t \n");
			System.out.println("choose option");
			choice = sc.nextInt();
			switch (choice) {
			case 1:
				System.out.println("Play song");
				playSong();
				// loop=false;
				break;
			case 2:
				addRemoveSongs();
				break;
			case 3:
				updateSong();
				break;
			case 4:
				loop = false;

			}
		}

	}

	public static void addRemoveSongs() {

		boolean loop = true;
		while (loop) {
			Scanner sc = new Scanner(System.in);
			System.out.println("ADD AND REMOVE SONG MENU \n  1)Add Songs\n 2)Remove Songs\n " + " 3) GO Back ");
			System.out.println("chose option");
			int choice = sc.nextInt();

			switch (choice) {

			case 1:
				addSong();
				break;

			case 2:
				System.out.println("Which song you want to remove select by Id");
				removeSong();

				break;
			case 3:
				loop = false;

			default:
				System.out.println("Please choose valid Input");
				break;
			}

		}

	}

	public static void playSong() {
		boolean loop = true;
		while (loop) {
			Scanner sc = new Scanner(System.in);
			// boolean playSong = true;
			System.out.println(
					"Play Song Menu \n  1)Play All Song \n 2)choose Song \n " + "3)Play Random \n 4) GO Back ");
			System.out.println("choose option");
			int choice = sc.nextInt();

			switch (choice) {
			case 1:
				
				playAllSongs();

				break;
			case 2:
				chooseSong();

				break;

			case 3:
				playRandomSong();

				break;

			case 4:
				System.out.println("Go Back");
				loop = false;

				break;
			default:
				System.out.println("Please give valid input value");

			}
		}
	}

	public static void updateSong() {
		boolean loop = true;
		while (loop) {
			Scanner sc = new Scanner(System.in);
			System.out.println("UPDATE SONG MENU \n  1)Song name\n 2)Singer name\n 3)Song Duration\n 4)Movie/album\n  "
					+ " 5) GO Back ");
			System.out.println("chose option");
			int choice = sc.nextInt();

			switch (choice) {

			case 1:
				System.out.println("Choose song by Id");
				changeSongName();

				break;

			case 2:
				System.out.println("Choose song by Id");
				changeSingerName();
				break;
			case 3:
				changeSongDuration();
				break;
			case 4:
				changeMovieAlbumName();
				break;
			case 5:
				loop = false;
				break;

			default:
				System.out.println("Please choose valid Input");
				break;
			}

		}

	}

}
