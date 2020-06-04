package InfoSearchQuiz_jhlee;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.*;
import InfoSearchQuiz_jhlee.Test01;

public class Test02 {
	
	// return_key 중복처리하여 반환
	public static HashMap<String, ArrayList<String>> ret_key(HashMap<String, List<String>> s) {
		HashMap<String, ArrayList<String>> result = new HashMap<String, ArrayList<String>>();
		
		Iterator<String> kk = s.keySet().iterator();
		while (kk.hasNext()) {
			String key = kk.next();
			List<String> tmp_lst = s.get(key);
			ArrayList<String> alph_lst = new ArrayList<String>();
			for (String alph : tmp_lst) {
				
				if (!alph_lst.contains(alph)) {
					alph_lst.add(alph);
				}else {
					continue;
				}
			}
			result.put(key, alph_lst);
		}
		return result;
	}
	
	// 파일읽고 공백처리
	public static HashMap<String, List<String>> file_str_map(File[] fileList) {
		HashMap<String, List<String>> result = new HashMap<String, List<String>>();
		
		// 문장을 담을 리스트
		List<String> check_str_lst = new ArrayList<String>();
		
		for (File fl : fileList) {
			
			try {
				FileReader file_reader = new FileReader(fl);
				BufferedReader br = new BufferedReader(file_reader);
				String line = "";
				while((line = br.readLine()) != null) {
					check_str_lst = Arrays.asList(line.split(" "));
				}
				file_reader.close();
				br.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			result.put(fl.getName(), check_str_lst);
		}
		return result;
	}
	
	public static HashMap<String, Double> wordMatrix(HashMap<String, List<String>> str_map, HashMap<String, ArrayList<String>> file_after_str, String str) {
	
		HashMap<String, HashMap<String, Double>> word_matrix = new HashMap<String, HashMap<String, Double>>();
		HashMap<String, Double> inner_map = new HashMap<String, Double>();
		List<String> file_list = new ArrayList<String>(file_after_str.keySet());
		
		String alp = str;
		Scanner sc = new Scanner(System.in);
		System.out.print("window size 입력 : ");
		int window_size = sc.nextInt();
		
		Iterator <String> kk = file_after_str.keySet().iterator();
		while (kk.hasNext()) {
			String key = kk.next();
			for (String s : file_after_str.get(key)) {
				inner_map.put(s, (double)0);
			}
			for (String s : file_after_str.get(key)) {
				word_matrix.put(s, inner_map);
			}
		}
//		System.out.println(word_matrix);
		
		for (String file_name : file_list) {
			
			for (int idx = 0; idx<str_map.get(file_name).size() ; idx ++) {
				// 앞뒤 인덱스 설정
				int start, end = 0;
				if (idx-window_size < 0) {
					start = 0;
				} else {
					start = idx - window_size;
				}
				
				if (idx+window_size > str_map.get(file_name).size()-1) {
					end = str_map.get(file_name).size()-1;
				} else {
					end = idx+window_size;
				}
				
				HashMap<String, Double> tmp_map = new HashMap<String, Double>();
				tmp_map = (HashMap<String, Double>)word_matrix.get(str_map.get(file_name).get(idx)).clone();
				
				for (int j = start; j<=end ; j++) {
					if (j == idx) {
						continue;
					}
					double tmp_num = 0;
					
					if(!tmp_map.containsKey(str_map.get(file_name).get(j))) {
						tmp_map.put(str_map.get(file_name).get(j), (double)1);
					} else {
						tmp_num = tmp_map.get(str_map.get(file_name).get(j));
						tmp_map.put(str_map.get(file_name).get(j), tmp_num+1);
					}
				}
				word_matrix.put(str_map.get(file_name).get(idx), tmp_map);
			}
			
		}
//		System.out.println(word_matrix.get(alp));
		return word_matrix.get(alp);
	}
	
	public static HashMap<String, Double> wordMatrix(HashMap<String, List<String>> str_map, HashMap<String, ArrayList<String>> file_after_str) {
		
		HashMap<String, HashMap<String, Double>> word_matrix = new HashMap<String, HashMap<String, Double>>();
		HashMap<String, Double> inner_map = new HashMap<String, Double>();
		List<String> file_list = new ArrayList<String>(file_after_str.keySet());
			
		Scanner sc = new Scanner(System.in);
		System.out.print("알파벳을 입력하세요 : ");
		String alp = sc.next();
		System.out.print("window_size : ");
		int window_size = sc.nextInt();
		
		Iterator <String> kk = file_after_str.keySet().iterator();
		while (kk.hasNext()) {
			String key = kk.next();
			for (String s : file_after_str.get(key)) {
				inner_map.put(s, (double)0);
			}
			for (String s : file_after_str.get(key)) {
				word_matrix.put(s, inner_map);
			}
		}
//		System.out.println(word_matrix);
		
		for (String file_name : file_list) {
			
			for (int idx = 0; idx<str_map.get(file_name).size() ; idx ++) {
				// 앞뒤 인덱스 설정
				int start, end = 0;
				if (idx-window_size < 0) {
					start = 0;
				} else {
					start = idx - window_size;
				}
				
				if (idx+window_size > str_map.get(file_name).size()-1) {
					end = str_map.get(file_name).size()-1;
				} else {
					end = idx+window_size;
				}
				
				HashMap<String, Double> tmp_map = new HashMap<String, Double>();
				tmp_map = (HashMap<String, Double>)word_matrix.get(str_map.get(file_name).get(idx)).clone();
				
				for (int j = start; j<=end ; j++) {
					if (j == idx) {
						continue;
					}
					double tmp_num = 0;
					
					if(!tmp_map.containsKey(str_map.get(file_name).get(j))) {
						tmp_map.put(str_map.get(file_name).get(j), (double)1);
					} else {
						tmp_num = tmp_map.get(str_map.get(file_name).get(j));
						tmp_map.put(str_map.get(file_name).get(j), tmp_num+1);
					}
				}
				word_matrix.put(str_map.get(file_name).get(idx), tmp_map);
			}
			
		}
//		System.out.println(word_matrix.get(alp));
		return word_matrix.get(alp);
	}
	
	public static void sort_print(HashMap<String, Double> outputMap) {
		
		List<String> key_lst = new ArrayList<String>(outputMap.keySet());
		
		Collections.sort(key_lst, new Comparator<String>() {

			@Override
			public int compare(String o1, String o2) {
				// TODO Auto-generated method stub
				return outputMap.get(o2).compareTo(outputMap.get(o1));
			}
		}); 
		
		int i = key_lst.size()-1;
		int j = 0;
		int k = 0;
		for (String key : key_lst) {
			double d = outputMap.get(key);
			int ii = (int)d;
			if (j < i) {
				System.out.print(String.format(" %s:%d,\t", key, ii));
				k++;
			} else {
				System.out.print(String.format(" %s:%d", key, ii));
				return;
			}
			if(k == 3) {
				System.out.println();
				k = 0;
			}
			j++;
		}
	}
	
	public static void sort_print(HashMap<String, Double> outputMap, String input) {
		
		List<String> a = new ArrayList<String>();
		List<Double> b = new ArrayList<Double>();
		List<String> key_lst = new ArrayList<String>(outputMap.keySet());
		
		Collections.sort(key_lst, new Comparator<String>() {

			@Override
			public int compare(String o1, String o2) {
				// TODO Auto-generated method stub
				return outputMap.get(o2).compareTo(outputMap.get(o1));
			}
		});
		System.out.println(input+" -> ");
		int i = key_lst.size()-1;
		int j = 0;
		for (String key : key_lst) {
			if (j < i) {
				System.out.println(String.format(" %s:%.3f, ", key, outputMap.get(key)));	
			} else {
				System.out.println(String.format(" %s:%.3f", key, outputMap.get(key)));	
			}
			j++;
		}
	}
	
	

	
	public static void main(String[] args, String str) {
		Test01 t1 = new Test01();
		
		File[] fileList = t1.get_file();
		HashMap<String, List<String>> str_map = file_str_map(fileList);
		HashMap<String, ArrayList<String>> file_after_str = ret_key(str_map);
		HashMap<String, Double> word_matrix = wordMatrix(str_map, file_after_str, str);
		sort_print(word_matrix);
		System.out.println();
		System.out.println();
	}
	
	
	public static void main(String[] args) {
		Test01 t1 = new Test01();
		
		File[] fileList = t1.get_file();
		HashMap<String, List<String>> str_map = file_str_map(fileList);
		HashMap<String, ArrayList<String>> file_after_str = ret_key(str_map);
		HashMap<String, Double> word_matrix = wordMatrix(str_map, file_after_str);
		sort_print(word_matrix);
		System.out.println();
		System.out.println();
	}
	

}