package InfoSearchQuiz_jhlee;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;


public class Test03 {
	
	public static void makeVector(HashMap<String, ArrayList<String>> str_map, String input) {
		HashMap <String, Double> csMap = new HashMap<String, Double>();
		HashMap <String, Double> origin_input_map = new HashMap<String, Double>();
		
		Test02 t2 = new Test02();
		
		ArrayList<String> file_list = new ArrayList<String>(str_map.keySet());
		
		// input값을 키로 갖는 map의 철자들을 input_map에 1로 초기화
		for (String alph : str_map.get(input)) {
			origin_input_map.put(alph, (double)1);
		}		
		
		// input을 제외한 나머지 compare_map들을 탐색
		for (String file : str_map.keySet()) {
			HashMap <String, Double> compare_map = new HashMap<String, Double>();
			
			if (!file.equals(input)) {
				for(String alph : str_map.get(file)) {
					compare_map.put(alph, (double)1);
				}
				// inputmap 키값 리스트 만들고 total key 만들기
				HashMap<String, Double> input_map = (HashMap<String, Double>) origin_input_map.clone();
				ArrayList<String> compare_key_lst = new ArrayList<String>(compare_map.keySet());
				ArrayList<String> total_key_lst = new ArrayList<String>();
				ArrayList<String> input_key_lst = new ArrayList<String>(input_map.keySet());
				
				for (String ik : input_key_lst) {
					if (!total_key_lst.contains(ik)) {
						total_key_lst.add(ik);
					}
				}
				for (String ck : compare_key_lst) {
					if (!total_key_lst.contains(ck)) {
						total_key_lst.add(ck);
					}
				}
				for (String tk : total_key_lst) {
					if (!input_map.containsKey(tk)) {
						input_map.put(tk, (double) 0);
					}
					if (!compare_map.containsKey(tk)) {
						compare_map.put(tk, (double) 0);
					}
				}
				double[] input_lst = mapToList(input_map);
				double[] compare_lst = mapToList(compare_map);
				double cs = cosinSimilarity(input_lst, compare_lst);
				csMap.put(file, cs);
				
				input_key_lst.clear();
				total_key_lst.clear();
			}
		}		
	    t2.sort_print(csMap, input);		
	}
	
	public static double[] mapToList(HashMap<String, Double> m) {
		double[] output = new double[m.size()];
		int i=0;
		Iterator<String> keys = m.keySet().iterator();
		while(keys.hasNext()) {
			String key = keys.next();
			output[i] = m.get(key);
			i++;
		}
		return output;
	}
	
	public static double cosinSimilarity(double[] A,double[] B) {
	      
	      if(A==null||B==null||A.length==0||B.length==0||A.length != B.length) {
	         
	         return 0;
	      }
	      
	      double sumProduct = 0;
	      double sumAsq = 0;
	      double sumBsq = 0;
	      for(int i = 0 ; i < A.length;i++) {
	         
	         sumProduct += A[i]*B[i];
	         sumAsq += A[i]*A[i];
	         sumBsq += B[i]*B[i];
	      }
	      
	      if(sumAsq == 0 && sumBsq ==0) {
	         return 0.0;
	      }
	      double db = sumProduct / (Math.sqrt(sumAsq)*Math.sqrt(sumBsq));
	      
	      return db;
	   }

	
	public static void main(String[] args) {
		Test01 t1 = new Test01();
		Test02 t2 = new Test02();
		
		File[] fileList = t1.get_file();
		HashMap<String, List<String>> str_map = t2.file_str_map(fileList);
		HashMap<String, ArrayList<String>> file_after_str = t2.ret_key(str_map);
		
		System.out.print("문서 이름을 입력하세요 : ");
		
		Scanner sc = new Scanner(System.in);
		String input = sc.next();
		
		makeVector(file_after_str, input);
		
	}
}
