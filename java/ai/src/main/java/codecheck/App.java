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
		String prevWord = args[0];	// 直前の回答
		String currWord;			// 今回の回答
		String str;					// Stringの一時変数
		String firstChar;			// 頭文字の一時変数
		String lastChar;			// 末尾文字の一時変数

		// 単語群をwordGroupに格納
		List<String> wordGroup = new ArrayList<>(Arrays.asList(Arrays.copyOfRange(args, 1, args.length)));
		
		// 直前の回答の末尾文字から始まる単語だけ抽出
		List<String> filteredWordGroup = wordGroup.stream()
				.filter(s -> s.startsWith(prevWord.substring(prevWord.length() - 1)))
				.collect(Collectors.toList());

		// 頭文字と末尾文字の頻度を算出
		Map<String, Double> firstHist = new HashMap<String, Double>();	// 頭文字の頻度分布
		Map<String, Double> lastHist = new HashMap<String, Double>();	// 末尾文字の頻度分布
		List<String> flList = new ArrayList<String>();					// 先頭文字と末尾文字の組み合わせの集合（例：matrix->mx）
		List<String> keys = new ArrayList<String>();					// 先頭あるいは末尾に含まれる文字の集合

		ListIterator<String> sItr = wordGroup.listIterator();
		while (sItr.hasNext()){
			str = sItr.next();
			firstChar = str.substring(0, 1);
			lastChar = str.substring(str.length() - 1);
			
			// firstHistに頭文字が含まれていれば加算、含まれていなければ追加
			// （末尾文字が含まれていない場合は末尾文字を追加、含まれている場合は何もしない）
			if (firstHist.containsKey(firstChar)){
				firstHist.put(firstChar, firstHist.get(firstChar) + 1.0);
			}
			else {
				firstHist.put(firstChar, 1.0);
			}
			if (!firstHist.containsKey(lastChar)){
				firstHist.put(lastChar, 0.0);
			}

			// lastHistに末尾文字が含まれていれば加算、含まれていなければ追加
			// （頭文字が含まれていない場合は頭文字を追加、含まれている場合は何もしない）
			if (lastHist.containsKey(lastChar)){
				lastHist.put(lastChar, lastHist.get(lastChar) + 1.0);
			}
			else {
				lastHist.put(lastChar, 1.0);
			}
			if (!lastHist.containsKey(firstChar)){
				lastHist.put(firstChar, 0.0);
			}
			
			// flListに頭文字と末尾文字の組み合わせが含まれていなければ追加
			if (!flList.contains(firstChar+lastChar)){
				flList.add(firstChar+lastChar);
			}
			
			// keysに頭文字もしくは末尾文字が含まれていなければ追加
			if (!keys.contains(lastChar)){
				keys.add(lastChar);
			}
			if (!keys.contains(firstChar)){
				keys.add(firstChar);
			}

		}

		// 先頭文字と末尾文字の出現頻度の割合を算出（先頭文字の頻度/末尾文字の頻度^2=割合）
		// 割合が大きい文字で終わる単語を選択すると有利（る攻めの原理）
		Map<String, Double> appearanceRate = new HashMap<String, Double>();	// 末尾文字と頭文字の出現頻度の割合
		sItr = keys.listIterator();
		while (sItr.hasNext()){
			str = sItr.next();
			if (lastHist.containsKey(str)){
				if (firstHist.containsKey(str)){
					appearanceRate.put(str, lastHist.get(str) / (firstHist.get(str)));
				}
				else {
					appearanceRate.put(str, Double.MAX_VALUE);
				}
			}
			else {
				appearanceRate.put(str, 0.0);
			}
		}

		// appearanceRateをvalueでソート(List<Entry>)
		List<Entry<String, Double>> entries = new ArrayList<Entry<String, Double>>(appearanceRate.entrySet());
		Collections.sort(entries, new Comparator<Entry<String, Double>>(){
			@Override
			public int compare(Entry<String, Double> e1, Entry<String, Double> e2){
				return e2.getValue().compareTo(e1.getValue());		// 降順
				//return e1.getValue().compareTo(e2.getValue());	// 昇順
			}
		});
		

		// NGリストの作成
		// 相手に、頭文字の出現頻度が0の文字が末尾となるような単語を回答されるとアウト
		// そのような単語に繋がる単語を回答すると負けが確定するため、NGリストに追加
		// 例：	直前の回答がcameraで語群が{matrix album ant term}の場合、
		// 		自：album→相：matrix→自：(none)で負けるため、末尾mをNGリストに追加
		// firstChar: filteredWordGroupの末尾文字（今回の自分の回答の末尾文字/次回の相手の回答の頭文字）
		// lastChar: 単語群の頭文字（次回の自分の回答の頭文字/次回の相手の回答の末尾文字）
		List<String> ngList = new ArrayList<String>();	// NGリスト
		sItr = filteredWordGroup.listIterator();
		while (sItr.hasNext()){
			str = sItr.next();
			firstChar = str.substring(str.length() - 1);
			ListIterator<String> sItr2 = flList.listIterator();
			while (sItr2.hasNext()){
				String fl = sItr2.next();
				lastChar = fl.substring(fl.length() - 1);
				// 今回、自分が回答する可能性のある回答の末尾文字から始まる単語で、
				// その単語の末尾文字から始まる単語の数が0の場合、NGリストに追加
				if (firstChar.equals(fl.substring(0, 1)) && firstHist.get(lastChar) == 0){
					if (!ngList.contains(firstChar))
						ngList.add(firstChar);
				}
			}
		}
		
		// 回答する単語を決定
		// 直前の回答の末尾文字(firstChar)と出現割合の高い文字(lastChar)で構成される単語の中で
		// lastCharがNGリストに入っておらず、lastCharで始まる単語が0もしくは2つ以上あるような単語を選択
		ListIterator<Entry<String, Double>> eItr = entries.listIterator();
		firstChar = prevWord.substring(prevWord.length() - 1);
		currWord = null;			// 回答する最適な単語
		String spareWord = null;	// 最適な単語がない場合の予備回答の単語（ほぼ負け確定の単語）
		while (eItr.hasNext()){
			sItr = filteredWordGroup.listIterator();
			Entry<String, Double> entry = eItr.next();
			while (sItr.hasNext()){
				str = sItr.next();
				lastChar = entry.getKey();
				String regex = "^" + firstChar + ".*" + lastChar + "$";
				if (str.matches(regex)){
					// 最適と思われる単語を選択
					if (!ngList.contains(lastChar) && firstHist.get(lastChar) != 1){
						currWord = str;
						break;
					}
					else {	// NGリストの単語など最適ではないが無回答より良い回答をストック
						if (spareWord == null)
							spareWord = str;
					}
				}
			}
			// 回答が決定したらループを抜ける
			if (currWord != null){
				break;
			}
		}
		
		// 最適な回答があればそれを出力
		// 最適な回答はないが、予備回答があればそれを出力
		// 最適な回答も呼びの回答もなければ出力なし
		if (currWord != null){
			System.out.print(currWord);
		}
		else {
			if (spareWord != null){
				System.out.print(spareWord);
			}
		}
	}
}
