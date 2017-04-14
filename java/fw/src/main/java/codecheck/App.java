package codecheck;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class App {
	private static String firstAI;
	private static String secondAI;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String prevWord;
		String currWord;
		Boolean turn = true;
		Boolean isFinished = false;
		
		firstAI = args[0];
		secondAI = args[1];
		prevWord = args[2];
		List<String> wordGroup = new ArrayList<String>(Arrays.asList(Arrays.copyOfRange(args, 3, args.length)));
		
		while (!isFinished){
			//System.out.println(wordGroup);
			
			currWord = nextWord(true, prevWord, wordGroup);
			
			if (wordCheck(prevWord, currWord, wordGroup)){
				if (turn){
					System.out.println("FIRST (OK) " + currWord);
				}
				else {
					System.out.println("SECOND (OK) " + currWord);
				}
				
				wordGroup.remove(wordGroup.indexOf(currWord));
				prevWord = currWord;
			}
			else {
				if (turn){
					System.out.println("FIRST (NG) " + currWord);
					System.out.println("WIN - SECOND");
				}
				else {
					System.out.println("SECOND (NG) " + currWord);
					System.out.println("WIN - FIRST");
				}
				isFinished = true;
			}
			
			turn = !turn;
		}
	}
	
	//	turn: 先攻(true), 後攻(false)
	//	preWord: 直前の回答
	// 	wordGroup: 残りの語群
	//	return: 回答（有効な回答がない場合はnull）
	private static String nextWord(boolean turn, String prevWord, List<String> wordGroup)
	{
		List<String> aiArgs = new ArrayList<String>();
		int exitStatus;
		String answer;
		
		aiArgs.add("sh");
		if (turn)
			aiArgs.add(firstAI);
		else
			aiArgs.add(secondAI);
		aiArgs.add(prevWord);
		aiArgs.addAll(wordGroup);
		
		try {
			Process aiProcess = new ProcessBuilder(aiArgs).start();
			exitStatus = aiProcess.waitFor();
			if (exitStatus != 0){
				//System.out.println("Artificial intelligence has terminated abnormally.");
				return "";
			}
			InputStream is = aiProcess.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
			answer = br.readLine();
			if (answer == null){
				return "";
			}
			return answer;
			
		} catch (IOException | InterruptedException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
			return "";
		}
	}
	
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
	
	private static Boolean isEmpty(String str)
	{
		if (str == null || str.equals(""))
			return true;
		else 
			return false;
	}
}
