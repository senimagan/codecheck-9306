package codecheck;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class App {

	public static void main(String[] args) {
		// TODO 自動生成されたメソッド・スタブ
		String prevWord;
		String currWord;
		prevWord = args[0];
		String str;
		String firstChar;
		String lastChar;

		List<String> wordGroup = new ArrayList<>(Arrays.asList(Arrays.copyOfRange(args, 1, args.length)));
		StringBuilder sbWordGroup = new StringBuilder();
		firstChar = prevWord.substring(prevWord.length() - 1);
		for (int i = 1; i < args.length; i++){
			if (args[i].matches("^" + firstChar + ".*$")){
				sbWordGroup.append(args[i]);
				sbWordGroup.append("\n");
			}
		}

		// 直前の回答の末尾文字から始まる単語の末尾文字だけ抽出
		List<String> filteredWordGroup = wordGroup.stream()
				.filter(s -> s.startsWith(prevWord.substring(prevWord.length() - 1)))
				.collect(Collectors.toList());

//		System.out.println("初期単語群: " + wordGroup);
//		System.out.println("直前回答末尾文字から始まる単語群: " + filteredWordGroup);

		// 先頭文字と末尾文字の出現頻度の差分を算出
		// 差分が大きい文字(key)で終わる単語を選択すると有利（る攻めの原理）
		Map<String, Integer> firstHist = new HashMap<String, Integer>();
		Map<String, Integer> lastHist = new HashMap<String, Integer>();
		List<String> keys = new ArrayList<String>();

		ListIterator<String> sItr = wordGroup.listIterator();
		while (sItr.hasNext()){
			str = sItr.next();
			firstChar = str.substring(0, 1);
			lastChar = str.substring(str.length() - 1);

			if (firstHist.containsKey(firstChar)){
				firstHist.put(firstChar, firstHist.get(firstChar) + 1);
			}
			else {
				firstHist.put(firstChar, 1);
				if (keys.contains(firstChar)){
					keys.add(firstChar);
				}
			}
			if (lastHist.containsKey(lastChar)){
				lastHist.put(lastChar, lastHist.get(lastChar) + 1);
			}
			else {
				lastHist.put(lastChar, 1);
				if (keys.contains(lastChar)){
					keys.add(lastChar);
				}
			}
		}

		Map<String, Double> appearanceRate = new HashMap<String, Double>();
		sItr = keys.listIterator();
		while (sItr.hasNext()){
			str = sItr.next();
			if (lastHist.containsKey(str)){
				if (firstHist.containsKey(str)){
					appearanceRate.put(str, (double)lastHist.get(str) / firstHist.get(str));
				}
				else {
					appearanceRate.put(str, Double.MAX_VALUE);
				}
			}
			else {
				appearanceRate.put(str, 0.0);
			}
		}

		// diffHistをvalueでソート(List<Entry>)
		List<Entry<String, Double>> entries = new ArrayList<Entry<String, Double>>(appearanceRate.entrySet());
		Collections.sort(entries, new Comparator<Entry<String, Double>>(){
			@Override
			public int compare(Entry<String, Double> e1, Entry<String, Double> e2){
				return e2.getValue().compareTo(e1.getValue());		// 降順
				//return e1.getValue().compareTo(e2.getValue());	// 昇順
			}
		});

		ListIterator<Entry<String, Double>> eItr = entries.listIterator();
		firstChar = prevWord.substring(prevWord.length() - 1);
		currWord = null;
		while (eItr.hasNext()){
			sItr = filteredWordGroup.listIterator();
			Entry<String, Double> entry = eItr.next();
			while (sItr.hasNext()){
				str = sItr.next();
				lastChar = entry.getKey();
				String regex = "^" + firstChar + ".*" + lastChar + "$";
				if (str.matches(regex)){
					currWord = str;
					System.out.println(currWord);
					break;
				}
			}
			if (currWord != null){
				break;
			}
		}
	}
}

