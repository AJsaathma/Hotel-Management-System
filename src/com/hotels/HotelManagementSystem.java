package com.hotels;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

public class HotelManagementSystem {

	private static Connection con = null;
	private static Scanner scan = new Scanner(System.in);
	private static Statement stmt = null;
	private static ResultSet res = null;
	private static PreparedStatement  pstmt = null;
	
	public static void main(String[] args) {
		
		while(true)
		{
			System.out.println("Welcome to Hotel Management System ");
			System.out.println("1. Reserve a room");
			System.out.println("2. View Reservation");
			System.out.println("3. Get Room Number");
			System.out.println("4. Update Reservations");
			System.out.println("5. Delete Reservations");
			System.out.println("6. Exit");
			
			System.out.println("Choose an option");
			int choice = scan.nextInt();
			
			switch(choice)
			{
			case 1 :
				//reserve a room
				reserveRoom();
				System.out.println();
				break;
			case 2 : 
				//view reservations
				viewReservations();
				System.out.println();
				break;
			case 3:
				// get room number
				getRoomNumber();
				System.out.println();
				break;
			case 4:
				//update the reservations
				updateReservation();
				System.out.println();
				break;
			case 5:
				//delete reservations
				deleteReservation();
				System.out.println();
				break;
			case 6:
				//exit
				try {
				exit();
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
				scan.close();
				return;
			default:
				System.out.println("Choose option correctly..");
			}
			
			
		}
	}
		
		public static void reserveRoom()
		{
			System.out.println("Reserve a room");
			System.out.println("Enter your name:");
			scan.nextLine();
			String name = scan.nextLine();
			scan.nextLine();
			System.out.println("Enter your number:");
			String number = scan.next();
			scan.nextLine();
			System.out.println("Enter room number:");
			int room_number = scan.nextInt();
			scan.nextLine(); 
			
			String query = "Insert into reservation(user_name,user_number,room_number)values(?,?,?)";
			
			try {
			
				Class.forName("com.mysql.cj.jdbc.Driver");
				con = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotel","root","root");
				pstmt = con.prepareStatement(query);
				pstmt.setString(1, name);
				pstmt.setString(2, number);
				pstmt.setInt(3, room_number);
				
				int row = pstmt.executeUpdate();
				if(row > 0)
				{
					System.out.println("Reservation successful..");
				}
				else
				{
					System.out.println("Reservation failed..");
				}
				
				
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		
		public static void viewReservations()
		{
			String query = "Select * from reservation";
		
			try {
				con = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotel","root","root");
				stmt = con.createStatement();
				
				res = stmt.executeQuery(query);
				
				System.out.println("Reservations ");
				System.out.println("--------------------+--------------------+--------------------+--------------------+--------------------|");
				System.out.println("Reservation id      | User Name          | User Number        |    Room Number     |  Date              |");
				System.out.println("--------------------+--------------------+--------------------+--------------------+--------------------|");
				
				while(res.next()) {
					
					System.out.printf("%-20s|%-20s|%-20s|%-20s|%-20s \n",res.getInt(1),res.getString(2),res.getString(3),res.getInt(4),res.getString(5));
					System.out.println("--------------------+--------------------+--------------------+--------------------+--------------------|");
					
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		
		public static void getRoomNumber()
		{
			
			System.out.println("Enter name:");
			String name = scan.nextLine();
			scan.nextLine();
			System.out.println("Enter id:");
			int num = scan.nextInt();
			scan.nextLine();
			String query = "select room_number from reservation where name = ?  and id = ?";
			
			
			try {
				
				con = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotel","root","root");
				
				pstmt = con.prepareStatement(query);
				pstmt.setString(1,name);
				pstmt.setInt(2, num);
				
				String sql = "select room_number from reservation";
				stmt =  con.createStatement();
				res = stmt.executeQuery(sql);
				if(res.next() ) {
					int n = res.getInt("room_number");
					System.out.println("Romm number is:" + n);
					
				}
				
				else{
					System.out.println("Reservation not found for the given name");
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		
		public static void updateReservation()
		{
			try {
				con = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotel","root","root");
				System.out.println("Enter reservation id to update");
				int num = scan.nextInt();
				scan.nextLine();
				
				if(!reservationExists( num))
				{
					System.out.println("Reservation not found for the given id");
					return;
				}
				
				System.out.println("Enter new guest name:");
				String guest_name = scan.nextLine();
				scan.nextLine();
				System.out.println("Enter room number:");
				int number = scan.nextInt();
				System.out.println("Enter number:");
				String phone = scan.next();
				
				String query = "update reservation set user_name=? ,room_number= ?,user_number=?";
				
				try {
					
						pstmt = con.prepareStatement(query);
						pstmt.setString(1, guest_name);
						pstmt.setInt(2, number);
						pstmt.setString(3, phone);
						
						int row = pstmt.executeUpdate();
						if(row > 0)
						{
							System.out.println("Reservation updated successfully");
						}
						else
						{
							System.out.println("Reservation update failed");
						}
					
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}catch(Exception e)
			{
				e.printStackTrace();
			}
		}
				
			
		
		
		public static void deleteReservation()
		{
			try {
				con = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotel","root","root");
				System.out.println("Enter reservation id to delete");
				int id = scan.nextInt();
				scan.nextLine();
				
				if(!reservationExists(id))
				{
					System.out.println("Reservation not found for the given id");
					return ;
				}
				String sql = "delete from reservation where id=?";
				try {
					pstmt = con.prepareStatement(sql);
					pstmt.setInt(1, id);
					int rows = pstmt.executeUpdate();
					if(rows > 0)
					{
						System.out.println("Reservation deleted sucessfully");
						
					}
					else
					{
						System.out.println("Reservation deletion failed..");
					}
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	public static boolean reservationExists(int id)
	{
		try {
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotel","root","root");
			String query = "select id from reservation where id=?";
			try {
				pstmt = con.prepareStatement(query);
				pstmt.setInt(1, id);
				
				String sql ="select id from reservation";
				stmt = con.createStatement();
				res = stmt.executeQuery(sql);
				
				return res.next();
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return false;
	}
	public static void exit() throws InterruptedException{
		System.out.println("Exiting the system ");
		int i=2;
		while(i!=0)
		{
			System.out.print(".");
			Thread.sleep(1000);
			i--;
		}
		System.out.println();
		System.out.println("Thanking for your reservation in my hotel....");
	}

}
