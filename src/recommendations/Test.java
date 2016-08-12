package recommendations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import static java.lang.System.out;

public class Test {
	
	public static void main(String[] args) {

		HashMap<String, Double> lisaMap = new HashMap<>();
		lisaMap.put("Lady in the Water", 2.5);
		lisaMap.put("Snakes on a Plane", 3.5);
		lisaMap.put("Just My Luck", 3.0);
		lisaMap.put("Superman Returns", 3.5);
		lisaMap.put("You, Me and Dupree", 2.5);
		lisaMap.put("The Night Listener", 3.0);
		
		HashMap<String, Double> geneMap = new HashMap<>();
		geneMap.put("Lady in the Water", 3.0);
		geneMap.put("Snakes on a Plane", 3.5);
		geneMap.put("Just My Luck", 1.5);
		geneMap.put("Superman Returns", 5.0);
		geneMap.put("You, Me and Dupree", 3.5);
		geneMap.put("The Night Listener", 3.0);
		
		HashMap<String, Double> michaelMap = new HashMap<>();
		michaelMap.put("Lady in the Water", 2.5);
		michaelMap.put("Snakes on a Plane", 3.0);
		michaelMap.put("Superman Returns", 3.5);
		michaelMap.put("The Night Listener", 4.0);
		
		HashMap<String, Double> claudiaMap = new HashMap<>();
		claudiaMap.put("Snakes on a Plane", 3.5);
		claudiaMap.put("Just My Luck", 3.0);
		claudiaMap.put("Superman Returns", 4.0);
		claudiaMap.put("You, Me and Dupree", 2.5);
		claudiaMap.put("The Night Listener", 4.5);
		
		HashMap<String, Double> mickMap = new HashMap<>();
		mickMap.put("Lady in the Water", 3.0);
		mickMap.put("Snakes on a Plane", 4.0);
		mickMap.put("Just My Luck", 2.0);
		mickMap.put("Superman Returns", 3.0);
		mickMap.put("You, Me and Dupree", 2.0);
		mickMap.put("The Night Listener", 3.0);
		
		HashMap<String, Double> jackMap = new HashMap<>();
		jackMap.put("Lady in the Water", 3.0);
		jackMap.put("Snakes on a Plane", 4.0);
		jackMap.put("Superman Returns", 5.0);
		jackMap.put("You, Me and Dupree", 3.5);
		jackMap.put("The Night Listener", 3.0);
		
		HashMap<String, Double> tobyMap = new HashMap<>();
		tobyMap.put("Snakes on a Plane", 4.5);
		tobyMap.put("Superman Returns", 4.0);
		tobyMap.put("You, Me and Dupree", 1.0);
		
		HashMap<String, HashMap<String, Double>> filmRatings = new HashMap<>();
		filmRatings.put("Lisa Rose", lisaMap);
		filmRatings.put("Gene Saymour", geneMap);
		filmRatings.put("Michael Phillips", michaelMap);
		filmRatings.put("Claudia Puig", claudiaMap);
		filmRatings.put("Mick LaSalle", mickMap);
		filmRatings.put("Jack Matthews", jackMap);
		filmRatings.put("Toby", tobyMap);
		
		//Check method 'euclidean'
		out.println(Recommendations.compare(lisaMap, geneMap, "euclidean"));
		
		//Check method 'pearson'
		out.println(Recommendations.compare(lisaMap, geneMap, "pearson"));
		
		//Check method 'topMatches'
		ArrayList<Match> topMatches = Recommendations.topMatches("Toby", filmRatings, "pearson", 3);
		for (int i = 0; i < topMatches.size(); i++) {
			out.println(topMatches.get(i).getItem() + ": " + topMatches.get(i).getRating());
		}
		
		//Check method 'getRecommendations' with simMeasure = 'pearson'
		ArrayList<Match> recommendations1 = Recommendations.getRecommendations("Toby", filmRatings, "pearson", 10);
		for (int i = 0; i < recommendations1.size(); i++) {
			out.println(recommendations1.get(i).getItem() + ": " + recommendations1.get(i).getRating());
		}
		
		//Check method 'getRecommendations' with simMeasure = 'euclidean'
		ArrayList<Match> recommendations2 = Recommendations.getRecommendations("Toby", filmRatings, "euclidean", 10);
		for (int i = 0; i < recommendations2.size(); i++) {
			out.println(recommendations2.get(i).getItem() + ": " + recommendations2.get(i).getRating());
		}
		
		//Check method 'transformPrefs'
		ArrayList<Match> recommendations3 = Recommendations.topMatches("Superman Returns", Recommendations.transformPrefs(filmRatings), "pearson");
		for (int i = 0; i < recommendations3.size(); i++) {
			out.println(recommendations3.get(i).getItem() + ": " + recommendations3.get(i).getRating());
		}
		
		//Check method 'transformPrefs'
		ArrayList<Match> recommendations4 = Recommendations.getRecommendations("Just My Luck", Recommendations.transformPrefs(filmRatings), "pearson", 10);
		for (int i = 0; i < recommendations4.size(); i++) {
			out.println(recommendations4.get(i).getItem() + ": " + recommendations4.get(i).getRating());
		}
		
		//Check method 'calculateSimilarItems'
		HashMap<String, ArrayList<Match>> resultMap = Recommendations.calculateSimilarItems(filmRatings, "pearson", 10);
		for (Entry<String, ArrayList<Match>> entry : resultMap.entrySet()) {
			out.println(entry.getKey());
			ArrayList<Match> tmpList = entry.getValue();
			for (int i = 0; i < tmpList.size(); i++) {
				out.println("  " + tmpList.get(i).getItem() + ": " + tmpList.get(i).getRating());
			}
		}
		
		//Check method 'getRecommendedItems'
		ArrayList<Match> recommendations5 = Recommendations.getRecommendedItems(filmRatings, resultMap, "Toby");
		for (int i = 0; i < recommendations5.size(); i++) {
			out.println(recommendations5.get(i).getItem() + ": " + recommendations5.get(i).getRating());
		}
		
		//Check method 'loadDataFromFile'
		HashMap<String, HashMap<String, Double>> resultMap2 = Recommendations.loadDataFromFile("u.data", "\t");
		for (Entry<String, HashMap<String, Double>> entry1 : resultMap2.entrySet()) {
			out.println(entry1.getKey());
			for (Entry<String, Double> entry2 : entry1.getValue().entrySet()) {
				out.println("  " + entry2.getKey() + ": " + entry2.getValue());
			}
		}
		
	}

}
