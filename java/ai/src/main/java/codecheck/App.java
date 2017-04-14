package codecheck;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.Comparator;

public class App {

	public static void main(String[] args) {
		// TODO 自動生成されたメソッド・スタブ
		String prevWord;
		String currWord;
		prevWord = args[0];
		String str;
		String firstChar;;
		String lastChar;
		
		List<String> wordGroup = new ArrayList<>(Arrays.asList(Arrays.copyOfRange(args, 1, args.length)));
		
		// 直前の回答の末尾文字から始まる単語の末尾文字だけ抽出
		List<String> filteredWordGroup = wordGroup.stream()
				.filter(s -> s.startsWith(prevWord.substring(prevWord.length() - 1)))
				.collect(Collectors.toList());
		
//		System.out.println("初期単語群: " + wordGroup);
//		System.out.println("直前回答末尾文字から始まる単語群: " + filteredWordGroup);
		
		Map<String, Integer> firstHist = new HashMap<String, Integer>();
		Map<String, Integer> lastHist = new HashMap<String, Integer>();
		
		ListIterator<String> sItr = wordGroup.listIterator();
		
		// 先頭文字、末尾文字ごとに単語の出現数をカウント
		while (sItr.hasNext()){
			str = sItr.next();
			firstChar = str.substring(0, 1);
			lastChar = str.substring(str.length() - 1);
			
			if (firstHist.containsKey(firstChar)){
				firstHist.put(firstChar, firstHist.get(firstChar) + 1);
			} else {
				firstHist.put(firstChar, 1);
			}
			
			if (lastHist.containsKey(lastChar)){
				lastHist.put(lastChar, lastHist.get(lastChar) + 1);
			} else {
				lastHist.put(lastChar, 1);
			}
		}

		// 先頭文字と末尾文字の集合
		List<String> keys = new ArrayList<String>(firstHist.keySet());
		keys.addAll(new ArrayList<String>(lastHist.keySet()));
		keys = keys.stream().distinct().collect(Collectors.toList());
		
		// 先頭文字と末尾文字の出現頻度の差分を算出
		// 差分が大きい文字(key)で終わる単語を選択すると有利（る攻めの原理）
		Map<String, Integer> diffHist = new HashMap<String, Integer>();
		sItr = keys.listIterator();
		while (sItr.hasNext()){
			str = sItr.next();
			if (!lastHist.containsKey(str)){
				diffHist.put(str, 0 - firstHist.get(str));
			}
			else if (!firstHist.containsKey(str)){
				diffHist.put(str, lastHist.get(str) - 0);
			}
			else {
				diffHist.put(str, lastHist.get(str) - firstHist.get(str));
			}
		}
		
		// diffHistをvalueでソート(List<Entry>)
		List<Entry<String, Integer>> entries = new ArrayList<Entry<String, Integer>>(diffHist.entrySet());
		Collections.sort(entries, new Comparator<Entry<String, Integer>>(){
			@Override
			public int compare(Entry<String, Integer> e1, Entry<String, Integer> e2){
				return e2.getValue().compareTo(e1.getValue());		// 降順
				//return e1.getValue().compareTo(e2.getValue());	// 昇順
			}
		});
		
		// List<Entry>をLinkedHashMapに変換
		diffHist = entries.stream()
				.collect(Collectors.toMap(s -> s.getKey(), s -> s.getValue(), (k, v) -> k, LinkedHashMap::new));
		
//		System.out.println("LC-FC:" + diffHist);
		
		
		// i
		LinkedHashMap<String, Integer> filteredDiffHist = new LinkedHashMap<String, Integer>();
		
		sItr = filteredWordGroup.listIterator(filteredWordGroup.size());
		while (sItr.hasPrevious()){
			str = sItr.previous();
			lastChar = str.substring(str.length() - 1);
			filteredDiffHist.put(lastChar, diffHist.get(lastChar));
		}
		
		keys  = new ArrayList<String>(filteredDiffHist.keySet());
		
		
		sItr = keys.listIterator();
		ListIterator<String> sItr2 = filteredWordGroup.listIterator();
		currWord = null;
		firstChar = prevWord.substring(prevWord.length() - 1);
		while (sItr.hasNext()){
			lastChar = sItr.next();
			while (sItr2.hasNext()){
				String ragex = "^" + firstChar + ".*" + lastChar + "$";
				str = sItr2.next();
				if (str.matches(ragex)){
					currWord = str;
					System.out.println(currWord);
					break;
				}
			}
			if (currWord != null){
				break;
			}
		}
		
//		System.out.println("LC-FC:" + filteredDiffHist);
//		System.out.println("LC:" + lastHist);
//		System.out.println("FC:" + firstHist);
//		
//		ListIterator<Entry<String, Integer>> eItr = new ArrayList<Entry<String, Integer>>(filteredDiffHist.entrySet()).listIterator();
//		while (eItr.hasNext()){
//			Entry<String, Integer> e = eItr.next();
//			
//			
//		}
	}
}

