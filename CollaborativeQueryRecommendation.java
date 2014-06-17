import java.io.*;
import java.util.*;

public class CollaborativeQueryRecommendation {

	public static HashMap<String, User> users2;
	public static HashMap<String, ArrayList<String>> users = new HashMap<String, ArrayList<String>>();
	public static ArrayList<User> userList = new ArrayList<User>();

	public static void main(String[] args) throws IOException {
		users2 = new HashMap<String, User>();
		populateUserFeatures();
		System.out.println("No of users: "+users2.size());

		populateUsersFromAOLData();
		
		checkUserDetails();
	}

	private static void checkUserDetails() throws IOException {
		FileWriter fstream2 = new FileWriter("src/data/3000QueriesDump");
		BufferedWriter out2 = new BufferedWriter(fstream2);
		System.out.println("\n\n\n\n\nSize of users2 map: "+users2.size());
		Iterator<User> itr = users2.values().iterator();
		while(itr.hasNext())
		{
			User u = itr.next();
			System.out.println(u.userID+"  "+u.getNumQ()+"   "+u.getFeatures().size());
			ArrayList<String> queries = u.getQueries();
			for(int i=0;i<queries.size();i++)
			{
				out2.write(queries.get(i));
				out2.write("\n");
			}
			out2.write("\n");
		}
	}

	private static void populateUsersFromAOLData() throws IOException {
		String filename = "src/data/AOL1.txt";
		BufferedReader br;
		br = new BufferedReader(new FileReader(filename));
		String line = br.readLine();
		line = br.readLine();
		int start = 1;
		String prevUserID = "";
		int c=0, count=10;
		while(count>0)
		{
			filename = "src/data/AOL"+count+".txt";
			count--;
			br = new BufferedReader(new FileReader(filename));
			line = br.readLine();line = br.readLine();
			while(line!=null)
			{
				try{
					c++;
					//if(c==100) break;
					String parts[] = line.split("\t");
					String userID = "";
					
					
					if(line.length()<1 || parts.length<1) {line = br.readLine();continue;}
					userID = parts[0];
					if(!users2.containsKey(userID)) {line = br.readLine();continue;}
					//if(line.contains("2317930")) System.out.println("-----------------------"+userID);
					if(userID.compareTo(prevUserID) == 0)
					{
						ArrayList<String> queries = users.get(userID);
						queries.add(parts[1]);
						
						User u = users2.get(userID);
						u.setQueries(queries);
						u.setNumQ(queries.size());
						users2.put(userID, u);
						
						users.put(userID, queries);
					}
					else
					{
						if(prevUserID.length()>0)
						{
							User u = new User(prevUserID, users.get(prevUserID));
							userList.add(u);
							//System.out.println("Added user to userList: "+u.userID+" "+u.numQ);
						}
						prevUserID = userID;
						ArrayList<String> queries = new ArrayList<String>();
						queries.add(parts[1]);
						
						User u = users2.get(userID);
						u.setQueries(queries);
						u.setNumQ(queries.size());
						users2.put(userID, u);
						
						users.put(userID, queries);
					}
					line = br.readLine();
				} catch(Exception e) {e.printStackTrace();}

			}
			System.out.println("Done with AOL"+(count+1)+".txt & no of users right now: "+c);
		}

		System.out.println("The total no of users: "+users.size()+"_"+userList.size());
		System.out.println("Total no of queries scanned: "+c);
	}

	private static void populateUserFeatures() throws IOException {
		BufferedReader br = new BufferedReader(new FileReader("src/data/3000userFeaturesFinal"));
		String line = br.readLine();
		while(line!=null)
		{
			String parts[] = line.split("\t");
			String userID = parts[0].trim();
			User u = new User(userID, line);
			users2.put(userID, u);
			line = br.readLine();
		}
		Iterator<String> itr = users2.keySet().iterator();
		while(itr.hasNext())
		{
			String userID = itr.next();
			System.out.println("_"+userID+"_");
		}
	}

}
