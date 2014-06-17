import java.util.*;
public class User {
	public String userID;
	public int numQ;
	public ArrayList<String> queries;
	public HashMap<Integer, Double> features;
	public String featureString;
	
	public User(String userID, ArrayList<String> queries)
	{
		this.userID = userID;
		this.queries = queries;
		this.numQ = queries.size();
	}
	
	public User(String userID, int numQ)
	{
		this.userID = userID;
		this.numQ = numQ;
	}
	
	public User(String userID, String featureString)
	{
		this.userID = userID;
		this.featureString = featureString;
		this.features = new HashMap<Integer,Double>();
		populateFeaturesMap();
	}

	private void populateFeaturesMap() {
		String parts[] = this.featureString.split("\t");
		//start loop from 1 as 0 is the userID
		for(int i=1;i<parts.length;i++)
		{
			Double d = Double.parseDouble(parts[i]);
			this.features.put(new Integer(i), d);
		}
	}
	
	public HashMap<Integer, Double> getFeatures() {
		return features;
	}

	public void setFeatures(HashMap<Integer, Double> features) {
		this.features = features;
	}
	
	public ArrayList<String> getQueries() {
		return queries;
	}

	public void setQueries(ArrayList<String> queries) {
		this.queries = queries;
	}

	public int getNumQ() {
		return numQ;
	}

	public void setNumQ(int numQ) {
		this.numQ = numQ;
	}
}
