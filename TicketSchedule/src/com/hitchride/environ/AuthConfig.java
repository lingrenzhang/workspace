package com.hitchride.environ;
//Basic authenication plan
//1. Admin: Some management related web page are only visible to Admin.
//2. Previleged Vip: Has more choice in matching and searching like able to use internal group search
//                   or list all the ride under same user group. Normally paid group or organization.
//                   Having authority to view other user's detailed(hidden) information.
//3. Vip: Upgrade from normal user after some committed ride. 
//        Have some special sign to show they are trusted person.
//4. Normal: Can search/manage ride, can create topic, have authority to all the basic function.
//5. Guest: Can only search ride. Do not able to use user center and manage ride. They can not create topic 
//   and only contact to Topic owner off-line. Will automatically be required to registered to Normal user.
	
//When client register to the web, they can use authentication code. Authentication code is predefined with 
//certain user group like companies/organizations/payed account etc.
//User Authority is associating with group authority

public class AuthConfig {
	public static int Admin=16; //
	public static int PrevilVip =8; //
	public static int Vip =4; //
	public static int Normal = 2;//
	public static int Guest = 1; //

}
