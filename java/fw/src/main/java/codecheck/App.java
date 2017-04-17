package codecheck;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;


public class App {
	private static String firstAI;	// 先攻のAIを指定する文字列
	private static String secondAI;	// 後攻のAIを指定する文字列
	
	public static void main(String[] args) {
		String prevWord;			// 直前の回答
		String currWord;			// 現在の回答
		Boolean turn = true;		// 現在のターン(true:先攻、false:後攻)
		Boolean isFinished = false;	// しりとりが終了しているかどうか
		
		// 引数から一連の変数を初期化
		firstAI = args[0];
		secondAI = args[1];
		prevWord = args[2];
		List<String> wordGroup = new ArrayList<>(Arrays.asList(Arrays.copyOfRange(args, 3, args.length)));
		
		// 引数の仕様をチェック
		Boolean isExit = false;
		if (wordGroup.size() > 1000){
			System.err.println("単語群が1000個を超えています");
			isExit = true;
		}
		ListIterator<String> sItr = wordGroup.listIterator();
		String word;
		while (sItr.hasNext()){
			word = sItr.next();
			if (word.length() > 10000){
				System.err.println("単語群に10000字を超えた単語が含まれています");
				isExit = true;
				break;
			}
			// すべてNFC正規形に正規化
			word = Normalizer.normalize(word, Normalizer.Form.NFC);
		}
		if (prevWord.length() > 10000){
			System.err.println("開始単語が10000字を超えています");
			isExit = true;
		}
		else {
			prevWord = Normalizer.normalize(prevWord, Normalizer.Form.NFC);
		}
		if (!(new File(firstAI)).exists()){
			System.err.println("先攻AIの指定が無いか、間違っています");
			isExit = true;
		}
		if (!(new File(secondAI)).exists()){
			System.err.println("後攻AIの指定が無いか、間違っています");
			isExit = true;
		}
		if (isExit){
			System.exit(1);
		}
		
		// しりとりが終了するまで繰り返し
		while (!isFinished){
			// 今回の回答を取得
			currWord = nextWord(true, prevWord, wordGroup);
			
			// 回答が正当かどうか評価
			if (wordCheck(prevWord, currWord, wordGroup)){
				if (turn){
					System.out.println("FIRST (OK): " + currWord);
				}
				else {
					System.out.println("SECOND (OK): " + currWord);
				}
				
				wordGroup.remove(wordGroup.indexOf(currWord));
				prevWord = currWord;
			}
			else {
				if (turn){
					System.out.println("FIRST (NG): " + currWord);
					System.out.println("WIN - SECOND");
				}
				else {
					System.out.println("SECOND (NG): " + currWord);
					System.out.println("WIN - FIRST");
				}
				isFinished = true;
			}
			
			turn = !turn;	// ターンを交代
		}
		return;
	}
	
	//	今回の回答をAIプロセスから取得する関数
	//	turn: 先攻(true)か後攻(false)か
	//	preWord: 直前の回答
	// 	wordGroup: 残りの語群
	//	return: 回答（有効な回答がない場合は空文字列("")）
	private static String nextWord(boolean turn, String prevWord, List<String> wordGroup)
	{
		List<String> aiArgs = new ArrayList<>();	// AIに渡す引数
		int exitStatus;		// 終了ステータス
		String currWord;	// 回答
		
		// AIに渡す引数を生成
		aiArgs.add("sh");
		if (turn)
			aiArgs.add(firstAI);
		else
			aiArgs.add(secondAI);
		aiArgs.add(prevWord);
		aiArgs.addAll(wordGroup);
		
		// AIのプロセスを実行
		try {
			Process aiProcess = new ProcessBuilder(aiArgs).start();
			exitStatus = aiProcess.waitFor();
			// AIプロセスが異常終了した場合はメッセージを表示
			if (exitStatus != 0){
				System.err.println("Artificial intelligence has terminated abnormally.");
				return "";
			}
			
			// AIプロセスの出力を取得し返却
			InputStream is = aiProcess.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
			currWord = br.readLine();
			if (currWord == null){
				return "";
			}
			return currWord;
			
		} catch (IOException | InterruptedException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
			return "";
		} 
	}
	
	//	回答が正しいか評価する関数
	//	prevWord: 直前の回答
	//	currWord: 現在の回答
	//	return: 現在の回答が正しければtrue，間違っていればfalse
	private static Boolean wordCheck(String prevWord, String currWord, List<String> wordGroup)
	{
		char lastChar = prevWord.charAt(prevWord.length() - 1);
		char firstChar;
		
		// 無回答あるいは単語群以外の回答かどうか
		if (currWord.isEmpty() || !wordGroup.contains(currWord)){
			return false;
		}
		
		// prevWordの最後の文字で始まっているかどうか
		firstChar = currWord.charAt(0);
		if (lastChar == firstChar)
			return true;
		else
			return false;
	}
}

