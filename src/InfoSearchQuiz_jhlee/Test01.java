package InfoSearchQuiz_jhlee;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.lang.Math;


public class Test01 {
	
	
	public static File[] get_file() {
	
		String path = ".\\searchInfo_SampleFile_1";
		File dir = new File(path);
		File[] fileList = dir.listFiles();
		      
		return fileList;
	}
	

	public static HashMap<String, HashMap<String, Double>> returnTfMap(File[] fileList) {

		HashMap<String, HashMap<String, Double>> return_map = new HashMap<String, HashMap<String, Double>>();
		double str_len = 0;
		
		List<String> check_str_lst = new ArrayList<String>();

		for (File fl : fileList) {
			// 파일명, map<알파벳, 등장 횟수>
			HashMap<String, Double> map = new HashMap<String, Double>();

			FileReader file_reader;
			try {
				file_reader = new FileReader(fl);
				
				BufferedReader br = new BufferedReader(file_reader);
				String line = "";
				while((line = br.readLine()) != null) {	
					check_str_lst = Arrays.asList(line.split(" "));  
					//line별로 입력받고 공백으로 나눠서 리스트에 저장
				}
				file_reader.close();
				br.close();
				
				for (String key : check_str_lst) {
					if (!map.containsKey(key)) {
						map.put(key, (double) 1);
					} else {
						double tmp_num = map.get(key);
						map.put(key, tmp_num+1);
					}
				}
				
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			str_len = check_str_lst.size();
			return_map.put(fl.getName(), map);
		}
		
		Iterator<String> kk = return_map.keySet().iterator();
		while (kk.hasNext()) {
			String key = kk.next();
			Iterator<String> mm = return_map.get(key).keySet().iterator();
			while (mm.hasNext()) {
				String mk = mm.next();
				double tmp_num = return_map.get(key).get(mk);
				return_map.get(key).put(mk, tmp_num/str_len);
			}

		}
		
		return return_map;
	}

	public static HashMap<String, Double> calcIDF (HashMap<String, HashMap<String, Double>> tfmap) {
		HashMap<String, Double> result = new HashMap<String, Double>();
		
		List<String> file_name_lst = new ArrayList<String>(tfmap.keySet());
		
		for (String fileName : file_name_lst) {
			HashMap<String, Double> tmp_map = tfmap.get(fileName);
			List<String> alph_lst = new ArrayList<String> (tmp_map.keySet());
			
			for (String alph : alph_lst) {
				if (tmp_map.get(alph) != 0) {
					if (!result.containsKey(alph)) {
						result.put(alph, (double) 1);
					} else {
						double tmp_num = result.get(alph);
						result.put(alph, tmp_num+1);
					}
				}
			}
		}
		
		
		Iterator<String> keys = result.keySet().iterator();
		while(keys.hasNext()) {
			String key = keys.next();
			double tmp_num = Math.log(1+(file_name_lst.size() / result.get(key)));
			result.put(key, tmp_num);
		}
		
//		System.out.println(result);
		return result;
	}
	
	public static void main(String[] args, String str) {
		HashMap<String, HashMap<String, Double>> file_tf_map = new HashMap<String, HashMap<String, Double>>();
		HashMap<String, Double> alph_idf_map = new HashMap<String, Double>();
		
		File[] file_list = get_file();		
		file_tf_map = returnTfMap(file_list);
		alph_idf_map = calcIDF(file_tf_map);
		
		String input = str;
		System.out.println(input + " 의  TF-IDF 스코어");
		
		for (File f : file_list) {
//			System.out.println(file_tf_map.get(f.getName()).get(input));
//			System.out.println(alph_idf_map.get(input));
			try {
				System.out.println(String.format("%s : %.4f", f.getName(), file_tf_map.get(f.getName()).get(input) * alph_idf_map.get(input)  ));
			} catch(NullPointerException e) {
				System.out.println(String.format("%s : 0", f.getName()));
			}
//			System.out.println(String.format("%s : %.4f", f.getName(), file_tf_map.get(f.getName()).get(input) * alph_idf_map.get(input)  ));
		}
		
		System.out.println();
	}
	
	public static void main(String[] args) {
		HashMap<String, HashMap<String, Double>> file_tf_map = new HashMap<String, HashMap<String, Double>>();
		HashMap<String, Double> alph_idf_map = new HashMap<String, Double>();
		
		File[] file_list = get_file();		
		file_tf_map = returnTfMap(file_list);
		alph_idf_map = calcIDF(file_tf_map);
		
		System.out.print("알파벳을 입력하세요 : ");
		Scanner sc = new Scanner(System.in);
		String input = sc.next();
		System.out.println(input + " 의  TF-IDF 스코어");
		for (File f : file_list) {
			try {
				System.out.println(String.format("%s : %.4f", f.getName(), 
						file_tf_map.get(f.getName()).get(input) * alph_idf_map.get(input)));
			} catch(NullPointerException e) {
				System.out.println(String.format("%s : 0", f.getName()));
			}
		}
		
		System.out.println();
	}
}