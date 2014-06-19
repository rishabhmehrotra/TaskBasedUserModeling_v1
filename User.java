import java.util.*;
import java.io.*;
public class User implements Serializable {

	private static final long serialVersionUID = 1L;
	public String userID;
	public int numQ;
	public ArrayList<String> queries;
	public HashMap<Integer, Double> features;
	public String featureString;
	public HashMap<String, Double> candidateQueries;
	public HashMap<String, String> selfQueries;
	public ArrayList<Query> candidateQList;

	public User(String userID, ArrayList<String> queries)
	{
		this.candidateQueries = new HashMap<String, Double>();
		//this.selfQueries = new HashMap<String, String>();
		this.userID = userID;
		this.queries = queries;
		this.numQ = queries.size();
	}
	
	public User(String userID, int numQ)
	{
		this.candidateQueries = new HashMap<String, Double>();
		//this.selfQueries = new HashMap<String, String>();
		this.userID = userID;
		this.numQ = numQ;
	}
	
	public User(String userID, String featureString)
	{
		this.candidateQueries = new HashMap<String, Double>();
		//this.selfQueries = new HashMap<String, String>();
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
	public HashMap<String, Double> getCandidateQueries() {
		return candidateQueries;
	}

	public void setCandidateQueries(HashMap<String, Double> candidateQueries) {
		this.candidateQueries = candidateQueries;
	}

	public void populateSelfQueries() {
		this.selfQueries = new HashMap<String, String>();
		Iterator<String> itr = this.queries.iterator();
		while(itr.hasNext())
		{
			String query = itr.next();
			if(this.selfQueries.containsKey(query));
			else this.selfQueries.put(query, query);
		}
	}
	
	public HashMap<String, String> getSelfQueries() {
		return selfQueries;
	}

	public void setSelfQueries(HashMap<String, String> selfQueries) {
		this.selfQueries = selfQueries;
	}

	public void populateCandidateList() {
		this.candidateQList = new ArrayList<Query>();
		Iterator<String> itr = this. candidateQueries.keySet().iterator();
		while(itr.hasNext())
		{
			String query =  itr.next();
			double score = this.candidateQueries.get(query);
			Query q = new Query(query,score);
			this.candidateQList.add(q);
		}
	}
}
