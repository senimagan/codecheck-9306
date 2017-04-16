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


