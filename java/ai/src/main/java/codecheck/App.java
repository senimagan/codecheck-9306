package codecheck;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;
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

		List<String> order = Arrays.asList("x", "y", "k", "n", "z", "e", "l", "g", "t", "r", "o", "d", "h", "m", "w", "a", "s", "u", "f", "i", "p", "c", "b", "v", "j", "q");

		List<String> wordGroup = new ArrayList<String>(Arrays.asList(Arrays.copyOfRange(args, 1, args.length)));

		// 直前の回答の末尾文字から始まる単語の末尾文字だけ抽出
		List<String> filteredWordGroup = wordGroup.stream()
				.filter(s -> s.startsWith(prevWord.substring(prevWord.length() - 1)))
				.collect(Collectors.toList());

//		System.out.println("初期単語群: " + wordGroup);
//		System.out.println("直前回答末尾文字から始まる単語群: " + filteredWordGroup);

<<<<<<< HEAD
=======
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
				if (!keys.contains(firstChar)){
					keys.add(firstChar);
				}
			}
			if (lastHist.containsKey(lastChar)){
				lastHist.put(lastChar, lastHist.get(lastChar) + 1);
			}
			else {
				lastHist.put(lastChar, 1);
				if (!keys.contains(lastChar)){
					keys.add(lastChar);
				}
			}
		}
>>>>>>> f9bdcfbd4056cfa252f91c1319fb54e27486cfbe

		firstChar = prevWord.substring(prevWord.length() - 1);
		currWord = null;
		ListIterator<String> oItr = order.listIterator();

		currWord = null;
		while (oItr.hasNext()){
			lastChar = oItr.next();
			ListIterator<String> wgItr = filteredWordGroup.listIterator();
			while (wgItr.hasNext()){
				str = wgItr.next();
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

