import java.io.*;
import java.util.*;

public class CollaborativeQueryRecommendation implements Serializable{

	private static final long serialVersionUID = 1L;
	public static HashMap<String, User> users2;
	public static HashMap<String, ArrayList<String>> users = new HashMap<String, ArrayList<String>>();
	public static ArrayList<User> userList = new ArrayList<User>();
	public static ArrayList<User> usersArrayList;
	public static double[][] simU;
	public static HashMap<String, Double> perUserCutOff;

	public static void main(String[] args) throws IOException, ClassNotFoundException {
		int part = 2;
		if(part == 1)
		{
			users2 = new HashMap<String, User>();
			populateUserFeatures();
			System.out.println("No of users: "+users2.size());

			populateUsersFromAOLData();

			checkUserDetails();
			FileOutputStream fos = new FileOutputStream("src/data/users2HashMap");
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(users2);

			fos.close();
			System.exit(0);
		}
		else if(part == 2)
		{
			nextPart();
			userSimilarity();
			populateCandidatesForEachUser();
			calculateSimCutOff(10);
			calculateScoreForEachCandidateForEachUser();
			System.exit(0);
		}
		else if(part == 3)
		{
			
		}
	        
		
		
	}
	
	private static void calculateSimCutOff(int threshold) {
		perUserCutOff = new HashMap<String, Double>();
		int size = usersArrayList.size();
		for(int i=0;i<size;i++)
		{
			String userID = usersArrayList.get(i).userID;
			ArrayList<Double> scores = new ArrayList<Double>();
			for(int j=0;j<size;j++)
			{
				if(i==j) continue;
				scores.add(simU[i][j]);
			}
			Comparator<Double> comparator = Collections.reverseOrder();
			Collections.sort(scores, comparator);
			//for(int j=0;j<11;j++) System.out.print(scores.get(j)+" ");
			//System.out.println();
			perUserCutOff.put(userID, new Double(scores.get(10)));
		}
		//System.exit(0);
	}

	private static void calculateScoreForEachCandidateForEachUser() {
		System.out.println("Starting with score calculation for each candidate");
		int size = usersArrayList.size();
		for(int i=0;i<size;i++)
		{
			User u1 = usersArrayList.get(i);
			double max = 0;String maxQuery="";
			HashMap<String, Double> candidates = u1.getCandidateQueries();
			Iterator<String> itr = candidates.keySet().iterator();
			while(itr.hasNext())
			{
				String query = itr.next();
				if(query.compareTo("-")==0) continue;
				double score = 0;
				for(int j=0;j<size;j++)
				{
					if(i==j) continue;
					User u2 = usersArrayList.get(j);
					if(u2.getSelfQueries().containsKey(query))
					{
						if(simU[i][j]>=perUserCutOff.get(u1.userID))
							score += simU[i][j];
					}
				}
				candidates.put(query, new Double(score));
				if(max<score) {max = score;maxQuery=query;}
			}
			u1.setCandidateQueries(candidates);
			System.out.println("for user "+u1.userID+" max score: "+max+" for query: "+maxQuery);
		}
	}

	public static void populateCandidatesForEachUser()
	{
		int size = usersArrayList.size();
		for(int i=0;i<size;i++)
		{
			User u1 = usersArrayList.get(i);
			u1.candidateQueries = new HashMap<String, Double>();
			for(int j=0;j<size;j++)
			{
				if(i==j) continue;
				User u2= usersArrayList.get(j);
				ArrayList<String> queries = u2.getQueries();
				for(int k =0;k<queries.size();k++)
				{
					String query = queries.get(k);
					if(u1.candidateQueries.containsKey(query));
					else u1.candidateQueries.put(query, new Double(0));
				}
			}
			//System.out.println("User paopulated with candidate queries: "+usersArrayList.get(i).candidateQueries.size());
		}
	}
	
	private static void userSimilarity() {
		int size = usersArrayList.size();
		simU = new double[size][size];
		for(int i=0;i<209;i++)
		{
			for(int j=0;j<209;j++)
			{
				if(i==j) simU[i][j]=1;
				simU[i][j] = computeSim(usersArrayList.get(i),usersArrayList.get(j));
				//System.out.print(simU[i][j]+"__");
			}
			//System.out.println();
		}
	}
	
	public static double computeSim(User u1, User u2)
	{
		double sim = 0, dot=0, d1=0,d2=0;
		HashMap<Integer, Double> features1 = u1.getFeatures();
		HashMap<Integer, Double> features2 = u2.getFeatures();
		int numF=features1.size();
		for(int i=1;i<=numF;i++)
		{
			dot += (features1.get(new Integer(i))*features2.get(new Integer(i)));
			d1 +=  (features1.get(new Integer(i))*features1.get(new Integer(i)));
			d2 +=  (features2.get(new Integer(i))*features2.get(new Integer(i)));
		}
		d1 = Math.sqrt(d1);
		d2 = Math.sqrt(d2);
		double denom = d1*d2;
		sim = dot/denom;
		return sim;
	}

	public static void nextPart() throws IOException, ClassNotFoundException
	{
		FileInputStream fis = new FileInputStream("src/data/users2HashMap");
        ObjectInputStream ois = new ObjectInputStream(fis);
        users2 = (HashMap<String, User>) ois.readObject();
        ois.close();
        System.out.println(users2.size());
        usersArrayList = new ArrayList<User>();
        Iterator<User> itr = users2.values().iterator();
        while(itr.hasNext())
        {
        	User u = itr.next();
        	usersArrayList.add(u);
        }
        System.out.println("No of users added to arrayList: "+usersArrayList.size());
        /*
        for(int i=0;i<usersArrayList.size();i++)
        {
        	ArrayList<String> queries1 = usersArrayList.get(i).getQueries();
        	int max=0; String maxUserID="";
        	for(int j=0;j<usersArrayList.size();j++)
        	{
        		if(i==j) continue;
        		ArrayList<String> queries2 = usersArrayList.get(j).getQueries();
        		int c=0;
        		for(int k=0;k<queries1.size();k++)
        		{
        			String query1 = queries1.get(k);
        			if(queries1.get(k).compareTo("")==0 || query1.length()<2) continue;
        			for(int l=0;l<queries2.size();l++)
        			{
        				String query2=queries2.get(l);
        				if(queries2.get(l).compareTo("")==0 || query2.length()<2) continue;
        				if(queries1.get(k).compareTo(queries2.get(l))==0)
        				{
        					c++;
        					//System.out.println(queries1.get(k)+"------"+queries2.get(l));
        				}
        			}
        		}
        		if(max<c) {max=c;maxUserID=usersArrayList.get(j).userID;}
        		//System.out.print(c+"_");
        	}
        	System.out.println("numQ: "+usersArrayList.get(i).getNumQ()+" max: "+max+" for users___"+usersArrayList.get(i).userID+"__with__"+maxUserID);
        }
        */
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
						u.populateSelfQueries();
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
						u.populateSelfQueries();
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
