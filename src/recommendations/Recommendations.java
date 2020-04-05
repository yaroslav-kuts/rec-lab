package recommendations;

import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.HashMap;

public class Recommendations {
  public static double euclidean(double[] arr1, double[] arr2) {
    double sum_of_squares = 0;
    for (int i = 0; i < arr1.length-1; i++) {
      sum_of_squares += Math.pow(arr1[i] - arr2[i], 2);
    }
    return 1/(1+sum_of_squares);
  }

  public static double pearson(double[] arr1, double[] arr2) {
    double arr1Sum = 0;
    for (int i = 0; i < arr1.length; i++) {
      arr1Sum += arr1[i];
    }

    double arr2Sum = 0;
    for (int i = 0; i < arr2.length; i++) {
      arr2Sum += arr2[i];
    }

    double arr1Ave = arr1Sum / arr1.length;
    double arr2Ave = arr2Sum / arr2.length;

    double squareSum1 = 0;
    for (int i = 0; i < arr1.length; i++) {
      squareSum1 += Math.pow(arr1Ave - arr1[i], 2);
    }

    double squareSum2 = 0;
    for (int i = 0; i < arr2.length; i++) {
      squareSum2 += Math.pow(arr2Ave - arr2[i], 2);
    }

    double sum = 0;
    for (int i = 0; i < arr1.length; i++) {
      sum += (arr1Ave - arr1[i]) * (arr2Ave - arr2[i]);
    }

    double den =  Math.sqrt(squareSum1 * squareSum2);

    double result = 0;

    if (den != 0) {
      result = sum / den;
      if (result > 1) result = 1;
      if (result < -1) result = -1;
    }

    return result;
  }

  public static double tanimoto(double[] arr1, double[] arr2) {
    int length = arr1.length < arr2.length ? arr1.length : arr2.length;

    int matches = 0;
    for (int i = 0; i < length; i++) {
      if (arr1[i] == arr2[i]) matches++;
    }

    return (double) matches / (arr1.length + arr2.length - matches);
  }

  public static double compare(HashMap<String, Double> map1, HashMap<String, Double> map2, String simMeasure) {

    List<Double> list1 = new ArrayList<>();
    List<Double> list2 = new ArrayList<>();

    for (Map.Entry<String, Double> entry : map1.entrySet()) {

      String key = entry.getKey();
      if (map2.containsKey(key)) {
        list1.add(entry.getValue());
        list2.add(map2.get(key));
      }
    }

    double[] arr1 = new double[list1.size()];
    for (int i = 0; i < arr1.length; i++) {
      arr1[i] = list1.get(i);
    }

    double[] arr2 = new double[list2.size()];
    for (int i = 0; i < arr2.length; i++) {
      arr2[i] = list2.get(i);
    }

    double result = 0.0;

    switch (simMeasure) {
    case "pearson" : result = pearson(arr1, arr2);
    break;
    case "tanimoto" : result = tanimoto(arr1, arr2);
    break;
    case "euclidean" : result = euclidean(arr1, arr2);
    break;
    default : result = pearson(arr1, arr2);
    }

    return result;
  }

  public static ArrayList<Match> topMatches(String user, HashMap<String, HashMap<String, Double>> dictionary, String simMeasure) {

    ArrayList<Match> matchesList = new ArrayList<>();

    if (dictionary.containsKey(user)) {
      for (Entry<String, HashMap<String, Double>> entry : dictionary.entrySet()) {
        if (!entry.getKey().equals(user)) {
          Match match = new Match();
          match.setItem(entry.getKey());
          match.setRating(compare(dictionary.get(user), entry.getValue(), simMeasure));
          matchesList.add(match);
        }
      }
    }
    return bubbleSort(matchesList);
  }

    public static ArrayList<Match> topMatches(String user, Map<String, HashMap<String, Double>> dictionary, String simMeasure, int limit) {

    ArrayList<Match> matchesList = new ArrayList<>();

    if (dictionary.containsKey(user)) {
      for (Entry<String, HashMap<String, Double>> entry : dictionary.entrySet()) {
        if (!entry.getKey().equals(user)) {
          Match match = new Match();
          match.setItem(entry.getKey());
          match.setRating(compare(dictionary.get(user), entry.getValue(), simMeasure));
          matchesList.add(match);
        }
      }
    }
    return bubbleSort(matchesList, limit);
  }

    private static ArrayList<Match> bubbleSort(ArrayList<Match> list) {

    Match[] arr = new Match[list.size()];

    for (int i = 0; i < arr.length; i++) {
      arr[i] = list.get(i);
    }

    for (int i = arr.length - 1; i > 0; i--) {
      for (int j = 0; j < i; j++) {
        Match currentMatch = arr[j];
        Match nextMatch = arr[j+1];
        if (currentMatch.getRating() < nextMatch.getRating()) {
          Match tmp = arr[j];
          arr[j] = arr[j+1];
          arr[j+1] = tmp;
        }
      }
    }

    ArrayList<Match> resultList = new ArrayList<Match>();

    for (int i = 0; i < arr.length; i++) {
      resultList.add(arr[i]);
    }

    return resultList;
  }

  private static ArrayList<Match> bubbleSort(ArrayList<Match> list, int limit) {

    Match[] arr = new Match[list.size()];

    for (int i = 0; i < arr.length; i++) {
      arr[i] = list.get(i);
    }

    for (int i = arr.length - 1; i > 0; i--) {
      for (int j = 0; j < i; j++) {
        Match currentMatch = arr[j];
        Match nextMatch = arr[j+1];
        if (currentMatch.getRating() < nextMatch.getRating()) {
          Match tmp = arr[j];
          arr[j] = arr[j+1];
          arr[j+1] = tmp;
        }
      }
    }

    ArrayList<Match> resultList = new ArrayList<Match>();

    limit = limit > arr.length ? arr.length : limit;

    for (int i = 0; i < limit; i++) {
      resultList.add(arr[i]);
    }

    return resultList;
  }

  public static ArrayList<Match> getRecommendations(String user, HashMap<String, HashMap<String, Double>> dictionary, String simMeasure, int limit) {

    ArrayList<Match> listOfMatches = topMatches(user, dictionary, simMeasure);

    HashMap<String, HashMap<String, Double>> modifiedMap = new HashMap<>();

    for (int i = 0; i < listOfMatches.size(); i++) {

      if (listOfMatches.get(i).getRating() > 0) {
        String person = listOfMatches.get(i).getItem();
        HashMap<String, Double> rankings = dictionary.get(person);
        HashMap<String, Double> modifiedRanks = new HashMap<>();
        for (Map.Entry<String, Double> entry : rankings.entrySet()) {
          if (!dictionary.get(user).containsKey(entry.getKey())) {
            modifiedRanks.put(entry.getKey(), entry.getValue()*listOfMatches.get(i).getRating());
          }
        }
        modifiedMap.put(person, modifiedRanks);
      }
    }

    HashMap<String, HashMap<String, Double>> transformMap = transformPrefs(modifiedMap);

    HashMap<String, Double> mapOfMatches = new HashMap<>();

    for (int i = 0; i < listOfMatches.size(); i++) {
      mapOfMatches.put(listOfMatches.get(i).getItem(), listOfMatches.get(i).getRating());
    }

    ArrayList<Match> resultList = new ArrayList<Match>();

    for (Map.Entry<String, HashMap<String, Double>> entry1 : transformMap.entrySet()) {
      Double total = 0.0;
      Double simSum = 0.0;
      HashMap<String, Double> tmpMap = entry1.getValue();
      for (Map.Entry<String, Double> entry2 : tmpMap.entrySet()) {
        total += entry2.getValue();
        simSum += mapOfMatches.get(entry2.getKey());
      }
      Match match = new Match();
      match.setItem(entry1.getKey());
      match.setRating(total / simSum);
      resultList.add(match);
    }

    return bubbleSort(resultList, limit);
  }

  public static HashMap<String, HashMap<String, Double>> transformPrefs(HashMap<String, HashMap<String, Double>> dictionary) {

        HashMap<String, HashMap<String, Double>> subjectMap = new HashMap<>();

    for (Entry<String, HashMap<String, Double>> entry : dictionary.entrySet()) {
      HashMap<String, Double> tmpMap = entry.getValue();
      for (Map.Entry<String, Double> rank : tmpMap.entrySet()) {
        if (!subjectMap.containsKey(rank.getKey())) {
          HashMap<String, Double> newMap = new HashMap<>();
          newMap.put(entry.getKey(), rank.getValue());
          subjectMap.put(rank.getKey(), newMap);
        }
        else {
          subjectMap.get(rank.getKey()).put(entry.getKey(),rank.getValue());
        }
      }
    }
    return subjectMap;
  }

  public static HashMap<String, ArrayList<Match>> calculateSimilarItems(HashMap<String, HashMap<String, Double>> dictionary, String simMeasure, int limit) {
    HashMap<String, ArrayList<Match>> resultMap = new HashMap<String, ArrayList<Match>>();

    HashMap<String, HashMap<String, Double>> tmpMap = transformPrefs(dictionary);

    for (Map.Entry<String, HashMap<String, Double>> entry : tmpMap.entrySet()) {
      ArrayList<Match> matchList = topMatches(entry.getKey(), tmpMap, simMeasure, limit);
      resultMap.put(entry.getKey(), matchList);
    }
    return resultMap;
  }

  public static ArrayList<Match> getRecommendedItems(HashMap<String, HashMap<String, Double>> dictionary, HashMap<String, ArrayList<Match>> simItems, String user) {
    ArrayList<Match> resultList = new ArrayList<>();

    HashMap<String, HashMap<String, Double>> modifiedMap = new HashMap<>();

    if (dictionary.containsKey(user)) {
      HashMap<String, Double> tmpMap = dictionary.get(user);
      for (Map.Entry<String, Double> entry : tmpMap.entrySet()) {
        label : for (int i = 0; i < simItems.get(entry.getKey()).size(); i++) {
          if (tmpMap.containsKey(simItems.get(entry.getKey()).get(i).getItem())) {
            continue label;
          }
          if (!modifiedMap.containsKey(simItems.get(entry.getKey()).get(i).getItem())) {
            HashMap<String, Double> subjectMap = new HashMap<>();
            subjectMap.put(entry.getKey(), simItems.get(entry.getKey()).get(i).getRating()*entry.getValue());
            modifiedMap.put(simItems.get(entry.getKey()).get(i).getItem(), subjectMap);
          }
          else {
            modifiedMap.get(simItems.get(entry.getKey()).get(i).getItem()).put(entry.getKey(), simItems.get(entry.getKey()).get(i).getRating()*entry.getValue());
          }
        }
      }

      for (Map.Entry<String, HashMap<String, Double>> entry1 : modifiedMap.entrySet()) {
        Match match = new Match();
        match.setItem(entry1.getKey());
        double simSum = 0.0;
        double total = 0.0;
        for (int i = 0; i < simItems.get(entry1.getKey()).size(); i++) {
          if (entry1.getValue().containsKey(simItems.get(entry1.getKey()).get(i).getItem())) {
            simSum += simItems.get(entry1.getKey()).get(i).getRating();
            total += entry1.getValue().get(simItems.get(entry1.getKey()).get(i).getItem());
          }
        }
        if (simSum != 0) {
          match.setRating(total / simSum);
        }
        else {
          match.setRating(0.0);
        }
        resultList.add(match);
      }
    }
    return bubbleSort(resultList);
  }

  public static HashMap<String, HashMap<String, Double>> loadDataFromFile(String path, String delimiter) {
    HashMap<String, HashMap<String, Double>> resultMap = new HashMap<>();
    BufferedReader reader = null;
    try {
      reader = new  BufferedReader(new FileReader(path));
      String line;
      while ((line = reader.readLine()) != null) {
        String[] data = line.split(delimiter);
        if (!resultMap.containsKey(data[0])) {
          HashMap<String, Double> newItemsMap = new HashMap<>();
          newItemsMap.put(data[1], Double.valueOf(data[2]));
          resultMap.put(data[0], newItemsMap);
        }
        else {
          resultMap.get(data[0]).put(data[1], Double.valueOf(data[2]));
        }
      }
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    finally {
      try {
        reader.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return resultMap;
  }
}
